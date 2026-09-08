import unittest
from pathlib import Path
import sys


sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "tools"))
import site_scan_ledger  # noqa: E402


def manifest_row(ordinal: int) -> dict:
    return {
        "scan_id": f"JJ-SCAN-{ordinal:06d}",
        "ordinal": ordinal,
        "archive_page": 1,
        "archive_position": ordinal,
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


if __name__ == "__main__":
    unittest.main()
