import json
import subprocess
import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "tools"))
import jj_ledger  # noqa: E402


def article(number: int, suffix: str | None = None) -> dict:
    suffix = suffix or str(number)
    return {"id": f"JJ-{number:06d}", "canonical_url": f"https://example.test/{suffix}"}


def checkpoint(number: int, start: int, end: int, total: int = 783) -> dict:
    return {
        "checkpoint_id": f"JJ-AIFIRST-CP-{number:03d}",
        "article_start_id": f"JJ-{start:06d}",
        "article_end_id": f"JJ-{end:06d}",
        "completed_in_batch": end - start + 1,
        "cumulative_completed": end,
        "total_articles": total,
        "remaining": total - end,
    }


class LedgerTests(unittest.TestCase):
    def setUp(self) -> None:
        self.temp = tempfile.TemporaryDirectory()
        self.root = Path(self.temp.name)
        research = self.root / "research"
        research.mkdir()
        self.articles = research / "article_ledger.jsonl"
        self.checkpoints = research / "checkpoint_ledger.jsonl"
        self.paths = jj_ledger.LedgerPaths(self.articles, self.checkpoints)
        self.write_jsonl(self.articles, [article(89), article(90)])
        self.write_jsonl(self.checkpoints, [checkpoint(1, 89, 90)])

    def tearDown(self) -> None:
        self.temp.cleanup()

    @staticmethod
    def write_jsonl(path: Path, rows: list[dict], newline: bool = True) -> None:
        content = "\n".join(json.dumps(row) for row in rows)
        path.write_text(content + ("\n" if newline else ""), encoding="utf-8")

    def incoming(self, rows: list[dict], cp: dict) -> tuple[Path, Path]:
        article_path = self.root / "incoming_articles.jsonl"
        checkpoint_path = self.root / "incoming_checkpoint.jsonl"
        self.write_jsonl(article_path, rows)
        self.write_jsonl(checkpoint_path, [cp])
        return article_path, checkpoint_path

    def test_validate_uses_article_id_tail_not_record_count(self) -> None:
        report = jj_ledger.validate(self.paths)
        self.assertEqual(2, report["articles"]["record_count"])
        self.assertEqual(90, report["checkpoints"]["cumulative_completed"])

    def test_dry_run_leaves_ledgers_byte_identical(self) -> None:
        incoming_articles, incoming_checkpoint = self.incoming([article(91), article(92)], checkpoint(2, 91, 92))
        before = (self.articles.read_bytes(), self.checkpoints.read_bytes())
        result = jj_ledger.append_batch(self.paths, incoming_articles, incoming_checkpoint, dry_run=True)
        self.assertTrue(result["dry_run"])
        self.assertEqual(before, (self.articles.read_bytes(), self.checkpoints.read_bytes()))

    def test_cli_dry_run_leaves_ledgers_byte_identical(self) -> None:
        incoming_articles, incoming_checkpoint = self.incoming([article(91), article(92)], checkpoint(2, 91, 92))
        before = (self.articles.read_bytes(), self.checkpoints.read_bytes())
        completed = subprocess.run(
            [
                sys.executable,
                str(Path(jj_ledger.__file__).resolve()),
                "--root",
                str(self.root),
                "append",
                "--articles",
                str(incoming_articles),
                "--checkpoint",
                str(incoming_checkpoint),
                "--dry-run",
            ],
            check=False,
            capture_output=True,
            text=True,
        )
        self.assertEqual(0, completed.returncode, completed.stderr)
        self.assertIn('"dry_run": true', completed.stdout)
        self.assertEqual(before, (self.articles.read_bytes(), self.checkpoints.read_bytes()))

    def test_append_adds_eof_newline_without_json_concatenation(self) -> None:
        self.articles.write_text(self.articles.read_text(encoding="utf-8").rstrip("\n"), encoding="utf-8")
        incoming_articles, incoming_checkpoint = self.incoming([article(91)], checkpoint(2, 91, 91))
        jj_ledger.append_batch(self.paths, incoming_articles, incoming_checkpoint, dry_run=False)
        self.assertTrue(self.articles.read_bytes().endswith(b"\n"))
        self.assertEqual("JJ-000091", jj_ledger.validate(self.paths)["articles"]["tail_id"])

    def test_rejects_duplicate_canonical_url(self) -> None:
        incoming_articles, incoming_checkpoint = self.incoming([article(91, "89")], checkpoint(2, 91, 91))
        with self.assertRaisesRegex(jj_ledger.LedgerError, "duplicate canonical_url"):
            jj_ledger.append_batch(self.paths, incoming_articles, incoming_checkpoint, dry_run=True)

    def test_rejects_noncontinuous_incoming_ids(self) -> None:
        incoming_articles, incoming_checkpoint = self.incoming([article(92)], checkpoint(2, 92, 92))
        with self.assertRaisesRegex(jj_ledger.LedgerError, "incoming ids must be continuous"):
            jj_ledger.append_batch(self.paths, incoming_articles, incoming_checkpoint, dry_run=True)

    def test_rejects_inconsistent_checkpoint_progress(self) -> None:
        bad = checkpoint(2, 91, 91)
        bad["remaining"] = 0
        incoming_articles, incoming_checkpoint = self.incoming([article(91)], bad)
        with self.assertRaisesRegex(jj_ledger.LedgerError, "self-consistent"):
            jj_ledger.append_batch(self.paths, incoming_articles, incoming_checkpoint, dry_run=True)

    def test_rejects_malformed_existing_jsonl(self) -> None:
        self.articles.write_text('{"id":"JJ-000089"}\n{bad}\n', encoding="utf-8")
        with self.assertRaisesRegex(jj_ledger.LedgerError, "invalid JSON"):
            jj_ledger.validate(self.paths)


    def test_git_status_preserves_first_porcelain_status_column(self) -> None:
        subprocess.run(["git", "init", "-b", "main"], cwd=self.root, check=True, capture_output=True, text=True)
        subprocess.run(["git", "config", "user.name", "Ledger Test"], cwd=self.root, check=True)
        subprocess.run(["git", "config", "user.email", "ledger-test@example.invalid"], cwd=self.root, check=True)
        subprocess.run(
            ["git", "add", "-f", "research/article_ledger.jsonl", "research/checkpoint_ledger.jsonl"],
            cwd=self.root,
            check=True,
        )
        subprocess.run(["git", "commit", "-m", "baseline"], cwd=self.root, check=True, capture_output=True, text=True)
        self.articles.write_text(self.articles.read_text(encoding="utf-8") + "\n", encoding="utf-8")
        porcelain = jj_ledger._git(self.root, "status", "--porcelain")
        self.assertTrue(porcelain.startswith(" M research/article_ledger.jsonl"), porcelain)
        jj_ledger._require_clean_or_ledger_only(self.root, self.paths)


if __name__ == "__main__":
    unittest.main()
