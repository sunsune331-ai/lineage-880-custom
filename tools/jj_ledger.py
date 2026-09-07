#!/usr/bin/env python3
"""Safely validate and append AI-first J.J. research ledger batches.

The article ledger began at JJ-000089.  `cumulative_completed` is an
external progress value (the article-id tail), not the count of JSONL rows.
"""

from __future__ import annotations

import argparse
import json
import os
import re
import subprocess
import sys
import tempfile
from dataclasses import dataclass
from pathlib import Path
from typing import Any, Iterable


ARTICLE_ID = re.compile(r"^JJ-(\d{6})$")
CHECKPOINT_ID = re.compile(r"^JJ-AIFIRST-CP-(\d{3})$")
AI_FIRST_START = 89


class LedgerError(ValueError):
    """Raised when a ledger does not meet the AI-first invariant."""


@dataclass(frozen=True)
class LedgerPaths:
    articles: Path
    checkpoints: Path


def _number(value: str, pattern: re.Pattern[str], label: str) -> int:
    match = pattern.fullmatch(value) if isinstance(value, str) else None
    if not match:
        raise LedgerError(f"invalid {label}: {value!r}")
    return int(match.group(1))


def _require_int(record: dict[str, Any], field: str, context: str) -> int:
    value = record.get(field)
    if isinstance(value, bool) or not isinstance(value, int):
        raise LedgerError(f"{context}: {field} must be an integer")
    return value


def read_jsonl(path: Path) -> list[dict[str, Any]]:
    """Read every JSONL line with json.loads; blank/non-object lines fail."""
    try:
        lines = path.read_text(encoding="utf-8").splitlines()
    except FileNotFoundError as exc:
        raise LedgerError(f"ledger missing: {path}") from exc

    records: list[dict[str, Any]] = []
    for line_number, line in enumerate(lines, start=1):
        if not line.strip():
            raise LedgerError(f"{path}:{line_number}: blank JSONL line")
        try:
            record = json.loads(line)
        except json.JSONDecodeError as exc:
            raise LedgerError(f"{path}:{line_number}: invalid JSON: {exc.msg}") from exc
        if not isinstance(record, dict):
            raise LedgerError(f"{path}:{line_number}: JSONL value must be an object")
        records.append(record)
    if not records:
        raise LedgerError(f"ledger is empty: {path}")
    return records


def validate_articles(records: list[dict[str, Any]], *, expected_start: int = AI_FIRST_START) -> dict[str, Any]:
    seen_ids: set[str] = set()
    seen_urls: set[str] = set()
    expected = expected_start
    for offset, record in enumerate(records, start=1):
        article_id = record.get("id")
        number = _number(article_id, ARTICLE_ID, f"article id at record {offset}")
        if number != expected:
            raise LedgerError(f"article ids must be continuous: expected JJ-{expected:06d}, got {article_id}")
        if article_id in seen_ids:
            raise LedgerError(f"duplicate article id: {article_id}")
        canonical = record.get("canonical_url")
        if not isinstance(canonical, str) or not canonical.strip():
            raise LedgerError(f"{article_id}: canonical_url is required")
        if canonical in seen_urls:
            raise LedgerError(f"duplicate canonical_url: {canonical}")
        seen_ids.add(article_id)
        seen_urls.add(canonical)
        expected += 1
    return {
        "record_count": len(records),
        "first_id": records[0]["id"],
        "tail_id": records[-1]["id"],
        "tail_number": expected - 1,
        "ids": seen_ids,
        "canonical_urls": seen_urls,
    }


def validate_checkpoints(records: list[dict[str, Any]], article_tail: int) -> dict[str, Any]:
    previous_cp: int | None = None
    previous_end: int | None = None
    total_articles: int | None = None
    for index, record in enumerate(records, start=1):
        checkpoint_id = record.get("checkpoint_id")
        cp_number = _number(checkpoint_id, CHECKPOINT_ID, f"checkpoint id at record {index}")
        if previous_cp is not None and cp_number != previous_cp + 1:
            raise LedgerError(f"checkpoint ids must be continuous: expected CP-{previous_cp + 1:03d}")
        start = _number(record.get("article_start_id"), ARTICLE_ID, f"{checkpoint_id} article_start_id")
        end = _number(record.get("article_end_id"), ARTICLE_ID, f"{checkpoint_id} article_end_id")
        completed = _require_int(record, "completed_in_batch", checkpoint_id)
        cumulative = _require_int(record, "cumulative_completed", checkpoint_id)
        remaining = _require_int(record, "remaining", checkpoint_id)
        total = _require_int(record, "total_articles", checkpoint_id)
        if start > end or completed != end - start + 1:
            raise LedgerError(f"{checkpoint_id}: article range and completed_in_batch disagree")
        if previous_end is not None and start != previous_end + 1:
            raise LedgerError(f"{checkpoint_id}: checkpoint article ranges are not continuous")
        # Deliberately compare progress to IDs, never JSONL record count.
        if cumulative != end:
            raise LedgerError(f"{checkpoint_id}: cumulative_completed must equal article_end_id number")
        if remaining != total - cumulative:
            raise LedgerError(f"{checkpoint_id}: remaining must equal total_articles - cumulative_completed")
        if total_articles is not None and total != total_articles:
            raise LedgerError(f"{checkpoint_id}: total_articles changed from {total_articles} to {total}")
        previous_cp, previous_end, total_articles = cp_number, end, total
    assert previous_cp is not None and previous_end is not None and total_articles is not None
    if previous_end != article_tail:
        raise LedgerError(
            f"checkpoint tail JJ-{previous_end:06d} does not match article tail JJ-{article_tail:06d}"
        )
    return {
        "record_count": len(records),
        "tail_id": records[-1]["checkpoint_id"],
        "tail_number": previous_cp,
        "article_tail_number": previous_end,
        "cumulative_completed": records[-1]["cumulative_completed"],
        "remaining": records[-1]["remaining"],
        "total_articles": total_articles,
    }


def validate(paths: LedgerPaths) -> dict[str, Any]:
    articles = read_jsonl(paths.articles)
    article_report = validate_articles(articles)
    checkpoints = read_jsonl(paths.checkpoints)
    checkpoint_report = validate_checkpoints(checkpoints, article_report["tail_number"])
    return {"articles": article_report, "checkpoints": checkpoint_report}


def public_report(report: dict[str, Any]) -> dict[str, Any]:
    """Keep CLI output reviewable without exposing internal de-duplication sets."""
    return {
        "articles": {key: value for key, value in report["articles"].items() if key not in {"ids", "canonical_urls"}},
        "checkpoints": report["checkpoints"],
    }


def _validate_incoming_articles(
    incoming: list[dict[str, Any]], existing: dict[str, Any]
) -> dict[str, Any]:
    if not incoming:
        raise LedgerError("incoming article batch is empty")
    expected = existing["tail_number"] + 1
    seen_urls = set(existing["canonical_urls"])
    for record in incoming:
        article_id = record.get("id")
        number = _number(article_id, ARTICLE_ID, "incoming article id")
        if number != expected:
            raise LedgerError(f"incoming ids must be continuous: expected JJ-{expected:06d}, got {article_id}")
        canonical = record.get("canonical_url")
        if not isinstance(canonical, str) or not canonical.strip():
            raise LedgerError(f"{article_id}: canonical_url is required")
        if canonical in seen_urls:
            raise LedgerError(f"duplicate canonical_url: {canonical}")
        seen_urls.add(canonical)
        expected += 1
    return {"first": existing["tail_number"] + 1, "last": expected - 1, "count": len(incoming)}


def _validate_incoming_checkpoint(
    incoming: list[dict[str, Any]], checkpoint_report: dict[str, Any], batch: dict[str, int]
) -> None:
    if len(incoming) != 1:
        raise LedgerError("incoming checkpoint JSONL must contain exactly one record")
    record = incoming[0]
    checkpoint_id = record.get("checkpoint_id")
    cp_number = _number(checkpoint_id, CHECKPOINT_ID, "incoming checkpoint id")
    if cp_number != checkpoint_report["tail_number"] + 1:
        raise LedgerError(f"expected checkpoint CP-{checkpoint_report['tail_number'] + 1:03d}")
    start = _number(record.get("article_start_id"), ARTICLE_ID, f"{checkpoint_id} article_start_id")
    end = _number(record.get("article_end_id"), ARTICLE_ID, f"{checkpoint_id} article_end_id")
    completed = _require_int(record, "completed_in_batch", checkpoint_id)
    cumulative = _require_int(record, "cumulative_completed", checkpoint_id)
    remaining = _require_int(record, "remaining", checkpoint_id)
    total = _require_int(record, "total_articles", checkpoint_id)
    if (start, end, completed) != (batch["first"], batch["last"], batch["count"]):
        raise LedgerError(f"{checkpoint_id}: incoming article range does not match incoming records")
    if cumulative != end or remaining != total - cumulative:
        raise LedgerError(f"{checkpoint_id}: cumulative_completed / remaining are not self-consistent")
    if total != checkpoint_report["total_articles"]:
        raise LedgerError(f"{checkpoint_id}: total_articles does not match existing checkpoint ledger")


def _jsonl_append(original: str, records: Iterable[dict[str, Any]]) -> str:
    prefix = original if not original or original.endswith("\n") else original + "\n"
    return prefix + "".join(json.dumps(record, ensure_ascii=False, separators=(",", ":")) + "\n" for record in records)


def _write_atomic_transaction(replacements: dict[Path, str]) -> None:
    staged: dict[Path, Path] = {}
    originals = {target: target.read_text(encoding="utf-8") for target in replacements}
    replaced: list[Path] = []
    try:
        for target, content in replacements.items():
            fd, temp_name = tempfile.mkstemp(prefix=f".{target.name}.", suffix=".tmp", dir=target.parent, text=True)
            with os.fdopen(fd, "w", encoding="utf-8", newline="") as handle:
                handle.write(content)
                handle.flush()
                os.fsync(handle.fileno())
            staged[target] = Path(temp_name)
        for target, staged_path in staged.items():
            os.replace(staged_path, target)
            replaced.append(target)
    except Exception:
        for target in reversed(replaced):
            fd, rollback_name = tempfile.mkstemp(prefix=f".{target.name}.rollback.", suffix=".tmp", dir=target.parent, text=True)
            with os.fdopen(fd, "w", encoding="utf-8", newline="") as handle:
                handle.write(originals[target])
                handle.flush()
                os.fsync(handle.fileno())
            os.replace(rollback_name, target)
        raise
    finally:
        for staged_path in staged.values():
            staged_path.unlink(missing_ok=True)


def append_batch(paths: LedgerPaths, incoming_articles_path: Path, incoming_checkpoint_path: Path, dry_run: bool) -> dict[str, Any]:
    report = validate(paths)
    incoming_articles = read_jsonl(incoming_articles_path)
    incoming_checkpoint = read_jsonl(incoming_checkpoint_path)
    batch = _validate_incoming_articles(incoming_articles, report["articles"])
    _validate_incoming_checkpoint(incoming_checkpoint, report["checkpoints"], batch)

    if not dry_run:
        article_text = paths.articles.read_text(encoding="utf-8")
        checkpoint_text = paths.checkpoints.read_text(encoding="utf-8")
        _write_atomic_transaction(
            {
                paths.articles: _jsonl_append(article_text, incoming_articles),
                paths.checkpoints: _jsonl_append(checkpoint_text, incoming_checkpoint),
            }
        )
        validate(paths)
    return {"dry_run": dry_run, "incoming": batch, "next_checkpoint": incoming_checkpoint[0]["checkpoint_id"]}


def _git(root: Path, *args: str) -> str:
    completed = subprocess.run(["git", *args], cwd=root, check=True, text=True, capture_output=True)
    return completed.stdout.rstrip("\r\n")


def _require_clean_or_ledger_only(root: Path, paths: LedgerPaths) -> None:
    allowed = {str(paths.articles.relative_to(root)).replace("\\", "/"), str(paths.checkpoints.relative_to(root)).replace("\\", "/")}
    lines = _git(root, "status", "--porcelain").splitlines()
    unexpected = [line for line in lines if line[3:].replace("\\", "/") not in allowed]
    if unexpected:
        raise LedgerError("working tree has unexpected changes: " + "; ".join(unexpected))


def commit_ledgers(root: Path, paths: LedgerPaths, message: str) -> str:
    validate(paths)
    _require_clean_or_ledger_only(root, paths)
    _git(root, "add", "-f", str(paths.articles.relative_to(root)), str(paths.checkpoints.relative_to(root)))
    staged = set(_git(root, "diff", "--cached", "--name-only").splitlines())
    allowed = {str(paths.articles.relative_to(root)).replace("\\", "/"), str(paths.checkpoints.relative_to(root)).replace("\\", "/")}
    if staged != allowed:
        raise LedgerError(f"staged paths are not exactly the two ledgers: {sorted(staged)}")
    _git(root, "commit", "-m", message)
    validate(paths)
    return _git(root, "rev-parse", "HEAD")


def push_ledgers(root: Path, paths: LedgerPaths) -> None:
    validate(paths)
    _require_clean_or_ledger_only(root, paths)
    if _git(root, "status", "--porcelain"):
        raise LedgerError("working tree must be clean before push")
    _git(root, "fetch", "origin", "main")
    subprocess.run(["git", "merge-base", "--is-ancestor", "origin/main", "HEAD"], cwd=root, check=True)
    _git(root, "push")


def _paths(root: Path) -> LedgerPaths:
    return LedgerPaths(root / "research" / "article_ledger.jsonl", root / "research" / "checkpoint_ledger.jsonl")


def main(argv: list[str] | None = None) -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--root", type=Path, default=Path.cwd())
    subparsers = parser.add_subparsers(dest="command", required=True)
    subparsers.add_parser("validate")
    append_parser = subparsers.add_parser("append")
    append_parser.add_argument("--articles", type=Path, required=True)
    append_parser.add_argument("--checkpoint", type=Path, required=True)
    append_parser.add_argument("--dry-run", action="store_true")
    commit_parser = subparsers.add_parser("commit")
    commit_parser.add_argument("--message", required=True)
    subparsers.add_parser("push")
    args = parser.parse_args(argv)
    root = args.root.resolve()
    paths = _paths(root)
    try:
        if args.command == "validate":
            print(json.dumps(public_report(validate(paths)), ensure_ascii=False, indent=2))
        elif args.command == "append":
            print(json.dumps(append_batch(paths, args.articles, args.checkpoint, args.dry_run), ensure_ascii=False, indent=2))
        elif args.command == "commit":
            print(commit_ledgers(root, paths, args.message))
        elif args.command == "push":
            push_ledgers(root, paths)
            print("pushed")
    except (LedgerError, subprocess.CalledProcessError) as exc:
        print(f"error: {exc}", file=sys.stderr)
        return 2
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
