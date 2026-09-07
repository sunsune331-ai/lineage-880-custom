# J.J. AI-first ledger tooling

`jj_ledger.py` validates and appends the two research JSONL ledgers as one
safe transaction. It uses only Python's standard library.

The AI-first article ledger begins at `JJ-000089`. `cumulative_completed`
tracks the numeric article tail, not the JSONL row count.

## Validate the current baseline

```powershell
py -3 tools\jj_ledger.py validate
```

## Standard 10-record batch

Put only the ten completed JSON article records in `incoming\articles.jsonl`
and exactly one matching checkpoint record in `incoming\checkpoint.jsonl`.
For the next batch, incoming IDs must be `JJ-000189` through `JJ-000198` and
the checkpoint must be `JJ-AIFIRST-CP-011` with `cumulative_completed: 198`
and `remaining: 585`.

```powershell
py -3 tools\jj_ledger.py append --articles incoming\articles.jsonl --checkpoint incoming\checkpoint.jsonl --dry-run
py -3 tools\jj_ledger.py append --articles incoming\articles.jsonl --checkpoint incoming\checkpoint.jsonl
py -3 tools\jj_ledger.py validate
py -3 tools\jj_ledger.py commit --message "research: append JJ AI-first batch 011"
py -3 tools\jj_ledger.py push
```

`append --dry-run` performs every validation but writes no files. Normal
append writes complete replacement files and rolls back a replaced ledger if
the paired replacement fails. `commit` validates before and after committing,
uses `git add -f` for both research ledgers, and refuses unrelated working-tree
or staged changes. `push` validates again and requires a clean, expected tree.
