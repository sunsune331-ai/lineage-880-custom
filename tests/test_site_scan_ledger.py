import unittest
import json
import tempfile
from pathlib import Path
import sys


sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "tools"))
import site_scan_ledger  # noqa: E402


def manifest_row(ordinal: int) -> dict:
    return {
        "scan_id": f"JJ-SCAN-{ordinal:06d}",
        "ordinal": ordinal,
        "archive_page": (ordinal - 1) // 10 + 1,
        "archive_position": (ordinal - 1) % 10 + 1,
        "title": f"Article {ordinal}",
        "published_at": "2026-01-01",
        "url": f"https://example.test/{ordinal}",
        "canonical_url": f"https://example.test/{ordinal}/",
    }


def scan_row(manifest: dict, body_read: object) -> dict:
    return {
        **manifest,
        "classification": "LOW",
        "body_read": body_read,
        "rationale": "Structural validation fixture.",
        "matched_signals": ["fixture"],
        "already_in_ai_first_ledger": False,
    }


class ScanLedgerTests(unittest.TestCase):
    def setUp(self) -> None:
        self.manifest = [manifest_row(ordinal) for ordinal in range(1, 11)]

    def test_body_read_true_and_false_are_valid(self) -> None:
        for body_read in (True, False):
            with self.subTest(body_read=body_read):
                report = site_scan_ledger.validate_ledger(
                    self.manifest, [scan_row(self.manifest[0], body_read)]
                )
                self.assertEqual(1, report["record_count"])

    def test_body_read_missing_null_and_non_boolean_are_rejected(self) -> None:
        for body_read in (None, "true", 1):
            with self.subTest(body_read=body_read):
                with self.assertRaisesRegex(site_scan_ledger.ScanLedgerError, "body_read must be boolean"):
                    site_scan_ledger.validate_ledger(
                        self.manifest, [scan_row(self.manifest[0], body_read)]
                    )

        missing = scan_row(self.manifest[0], False)
        del missing["body_read"]
        with self.assertRaisesRegex(site_scan_ledger.ScanLedgerError, "body_read must be boolean"):
            site_scan_ledger.validate_ledger(self.manifest, [missing])

    def test_ingest_dry_run_accepts_false_body_read_without_writing(self) -> None:
        with tempfile.TemporaryDirectory() as temp:
            root = Path(temp)
            manifest_path = root / "manifest.jsonl"
            ledger_path = root / "ledger.jsonl"
            incoming_manifest_path = root / "incoming_manifest.jsonl"
            incoming_scan_path = root / "incoming_scan.jsonl"
            initial_manifest = self.manifest
            initial_ledger = [scan_row(row, True) for row in initial_manifest]
            incoming_manifest = [manifest_row(ordinal) for ordinal in range(11, 21)]
            incoming_scan = [scan_row(row, False) for row in incoming_manifest]
            for path, rows in (
                (manifest_path, initial_manifest),
                (ledger_path, initial_ledger),
                (incoming_manifest_path, incoming_manifest),
                (incoming_scan_path, incoming_scan),
            ):
                path.write_text("".join(json.dumps(row) + "\n" for row in rows), encoding="utf-8")
            before = (manifest_path.read_bytes(), ledger_path.read_bytes())
            report = site_scan_ledger.ingest(
                manifest_path, ledger_path, incoming_manifest_path, incoming_scan_path, dry_run=True
            )
            self.assertTrue(report["dry_run"])
            self.assertEqual(before, (manifest_path.read_bytes(), ledger_path.read_bytes()))


if __name__ == "__main__":
    unittest.main()
