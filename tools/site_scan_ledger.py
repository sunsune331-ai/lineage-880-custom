#!/usr/bin/env python3
"""Append and validate manifest-bound shallow site-scan batches.

This ledger is intentionally independent from the AI-first article/checkpoint
ledgers.  A scan row must reproduce the immutable archive manifest metadata;
only its shallow semantic assessment may add information.
"""

from __future__ import annotations

import argparse
import json
import os
import tempfile
from pathlib import Path
from typing import Any


CLASSIFICATIONS = {"HIGH", "MEDIUM", "REVIEW", "LOW", "SKIP"}


class ScanLedgerError(ValueError):
    """Raised when a manifest or scan ledger invariant is violated."""


def read_jsonl(path: Path, *, allow_missing: bool = False) -> list[dict[str, Any]]:
    if allow_missing and not path.exists():
        return []
    try:
        lines = path.read_text(encoding="utf-8").splitlines()
    except FileNotFoundError as exc:
        raise ScanLedgerError(f"missing JSONL: {path}") from exc
    rows: list[dict[str, Any]] = []
    for number, line in enumerate(lines, 1):
        if not line.strip():
            raise ScanLedgerError(f"{path}:{number}: blank JSONL line")
        try:
            row = json.loads(line)
        except json.JSONDecodeError as exc:
            raise ScanLedgerError(f"{path}:{number}: invalid JSON: {exc.msg}") from exc
        if not isinstance(row, dict):
            raise ScanLedgerError(f"{path}:{number}: JSONL value must be an object")
        rows.append(row)
    return rows


def validate_manifest(rows: list[dict[str, Any]]) -> None:
    if len(rows) != 50:
        raise ScanLedgerError(f"manifest must have 50 rows, got {len(rows)}")
    urls: set[str] = set()
    for ordinal, row in enumerate(rows, 1):
        expected_id = f"JJ-SCAN-{ordinal:06d}"
        if row.get("scan_id") != expected_id or row.get("ordinal") != ordinal:
            raise ScanLedgerError(f"manifest row {ordinal}: scan id/ordinal mismatch")
        if row.get("archive_page") != (ordinal - 1) // 10 + 1:
            raise ScanLedgerError(f"{expected_id}: archive_page mismatch")
        if row.get("archive_position") != (ordinal - 1) % 10 + 1:
            raise ScanLedgerError(f"{expected_id}: archive_position mismatch")
        canonical = row.get("canonical_url")
        if not isinstance(canonical, str) or not canonical:
            raise ScanLedgerError(f"{expected_id}: canonical_url required")
        if canonical in urls:
            raise ScanLedgerError(f"{expected_id}: duplicate canonical_url")
        urls.add(canonical)


def _validate_scan_row(row: dict[str, Any], manifest_row: dict[str, Any]) -> None:
    scan_id = manifest_row["scan_id"]
    for field in ("scan_id", "ordinal", "archive_page", "archive_position", "title", "published_at", "url", "canonical_url"):
        if row.get(field) != manifest_row.get(field):
            raise ScanLedgerError(f"{scan_id}: {field} does not match immutable manifest")
    if row.get("classification") not in CLASSIFICATIONS:
        raise ScanLedgerError(f"{scan_id}: classification must be one of {sorted(CLASSIFICATIONS)}")
    if row.get("body_read") is not True:
        raise ScanLedgerError(f"{scan_id}: body_read must be true")
    rationale = row.get("rationale")
    if not isinstance(rationale, str) or not rationale.strip():
        raise ScanLedgerError(f"{scan_id}: non-empty rationale required")
    signals = row.get("matched_signals")
    if not isinstance(signals, list) or not all(isinstance(value, str) and value for value in signals):
        raise ScanLedgerError(f"{scan_id}: matched_signals must be a list of non-empty strings")
    if not isinstance(row.get("already_in_ai_first_ledger"), bool):
        raise ScanLedgerError(f"{scan_id}: already_in_ai_first_ledger must be boolean")


def validate_ledger(manifest: list[dict[str, Any]], ledger: list[dict[str, Any]]) -> dict[str, Any]:
    if len(ledger) > len(manifest):
        raise ScanLedgerError("scan ledger exceeds immutable manifest")
    counts = {name: 0 for name in sorted(CLASSIFICATIONS)}
    for index, row in enumerate(ledger):
        _validate_scan_row(row, manifest[index])
        counts[row["classification"]] += 1
    return {"record_count": len(ledger), "tail_id": ledger[-1]["scan_id"] if ledger else None, "classification_counts": counts}


def write_atomic(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    fd, name = tempfile.mkstemp(prefix=f".{path.name}.", suffix=".tmp", dir=path.parent, text=True)
    temp = Path(name)
    try:
        with os.fdopen(fd, "w", encoding="utf-8", newline="") as handle:
            handle.write(content)
            handle.flush()
            os.fsync(handle.fileno())
        os.replace(temp, path)
    finally:
        temp.unlink(missing_ok=True)


def append(manifest_path: Path, ledger_path: Path, incoming_path: Path, dry_run: bool) -> dict[str, Any]:
    manifest = read_jsonl(manifest_path)
    validate_manifest(manifest)
    ledger = read_jsonl(ledger_path, allow_missing=True)
    validate_ledger(manifest, ledger)
    incoming = read_jsonl(incoming_path)
    if not incoming:
        raise ScanLedgerError("incoming scan batch is empty")
    start = len(ledger)
    if start + len(incoming) > len(manifest):
        raise ScanLedgerError("incoming batch exceeds manifest")
    for offset, row in enumerate(incoming):
        _validate_scan_row(row, manifest[start + offset])
    if not dry_run:
        original = ledger_path.read_text(encoding="utf-8") if ledger_path.exists() else ""
        prefix = original if not original or original.endswith("\n") else original + "\n"
        content = prefix + "".join(json.dumps(row, ensure_ascii=False, separators=(",", ":")) + "\n" for row in incoming)
        write_atomic(ledger_path, content)
        validate_ledger(manifest, read_jsonl(ledger_path))
    return {"dry_run": dry_run, "incoming": len(incoming), "first_id": incoming[0]["scan_id"], "last_id": incoming[-1]["scan_id"]}


def main() -> int:
    parser = argparse.ArgumentParser(description="Append and validate manifest-bound site scan batches")
    parser.add_argument("--manifest", type=Path, default=Path("research/site_scan_manifest.jsonl"))
    parser.add_argument("--ledger", type=Path, default=Path("research/site_scan_ledger.jsonl"))
    sub = parser.add_subparsers(dest="command", required=True)
    sub.add_parser("validate")
    append_parser = sub.add_parser("append")
    append_parser.add_argument("--incoming", type=Path, required=True)
    append_parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()
    try:
        manifest = read_jsonl(args.manifest)
        validate_manifest(manifest)
        if args.command == "validate":
            print(json.dumps(validate_ledger(manifest, read_jsonl(args.ledger, allow_missing=True)), ensure_ascii=False, sort_keys=True))
        else:
            print(json.dumps(append(args.manifest, args.ledger, args.incoming, args.dry_run), ensure_ascii=False, sort_keys=True))
    except ScanLedgerError as exc:
        print(f"error: {exc}", file=os.sys.stderr)
        return 2
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
