# JJ Coverage Scan Window Handoff — 2026-09-09

## Purpose

This file is the durable handoff for closing the current ChatGPT window and continuing JJ full-site Coverage Scan in a fresh project chat.

The new chat must treat GitHub as the source of truth and must re-read current branch state before continuing. Do not rely on remembered chat state when it conflicts with GitHub.

This handoff was first committed to `main` as `a12d22f59819b44be88b8876c9a59fc69576f212`. The formal scan ledgers were not changed by that documentation commit. Because further commits may occur, always re-read live `main` before resuming.

## Current verified GitHub state at handoff creation

Repository: `sunsune331-ai/lineage-880-custom`

### `main`

Formal-ledger baseline immediately before the handoff documentation commit:

`369addeee38ea6eaff798443dde20eaa809b4082`

Commit message:

`research: ingest coverage scan 061-070`

Formal Coverage Scan state on `main`:

- `research/site_scan_manifest.jsonl` tail: `JJ-SCAN-000070`
- `research/site_scan_ledger.jsonl` tail: `JJ-SCAN-000070`
- formal Coverage progress: `70 / 783`

Formal AI-first state on `main` remains separate from Coverage:

- Article tail: `JJ-000298`
- Checkpoint: `JJ-AIFIRST-CP-021`
- cumulative AI-first count: `298 / 783`
- remaining under the legacy AI-first checkpoint semantics: `485`
- `JJ-000299` is NOT allocated

Important: `298 / 783` and `70 / 783` are different metrics. `298 / 783` is the legacy/formal AI-first research progression; `70 / 783` is the newer exhaustive full-site Coverage ledger progression.

### `jj-scan-inbox`

Verified HEAD at handoff time:

`4d70cd45226f0d885005f1c772489898a4c247ed`

Commit message:

`research: mark coverage scan 071-080 ready`

The following READY batches are present in `research/incoming/site_scan/`:

- `JJ-SCAN-000061..000070` READY
- `JJ-SCAN-000071..000080` READY

For `071..080`, all three staging files exist:

- `jj_scan_000071_000080.manifest.jsonl`
- `jj_scan_000071_000080.scan.jsonl`
- `jj_scan_000071_000080.ready.json`

At the exact handoff verification time, the first batch not confirmed READY is therefore `JJ-SCAN-000081..000090`.

However, a scheduled automation is active and may advance `jj-scan-inbox` after this file is written. A new chat MUST NOT blindly assume `081..090` is still next. It must re-read `jj-scan-inbox` and find the first contiguous non-READY batch dynamically.

## Agreed Coverage strategy

The user wants the complete JJ site scanned first, then classification/research-map consolidation, and only after full-site Coverage is complete should 8.8 implementation/static/runtime validation begin.

Coverage rules:

1. Scan every JJ article in website/archive order through `JJ-SCAN-000783`.
2. Do not skip an article merely because it appears unrelated to Lineage 8.8.
3. `HIGH / MEDIUM / REVIEW / LOW / SKIP` is only Coverage metadata; it must not gate whether a page is scanned.
4. Read the full body. Title-only, archive snippet, search snippet, or partial body is insufficient for `body_read=true`.
5. If the article is inaccessible/partial/dead, preserve that status truthfully instead of pretending it is complete.
6. Do not replace a failed/partial ordinal with a different article merely to make a batch contain ten complete bodies.
7. Coverage order and ordinals are authoritative.

## Batch and campaign model

Permanent transaction/quality boundary:

`10 articles`

Operational campaign ceiling per run:

`up to 100 articles`

Meaning:

- 100 is a ceiling, NOT a quota.
- Each ten-article batch must independently complete research, validation, cloud staging, and READY before the next ten begin.
- If a run only completes 30, 40, 50, or 70 articles, that is acceptable.
- Never lower research quality to force 100 articles into a time window.
- On interruption, stop at the last fully READY ten-article checkpoint. A later run resumes from the first non-READY batch.

Required flow for every ten-article batch:

1. Lock the correct ten archive entries.
2. Open and read each full article body.
3. Create the existing scan record fields:
   - `scan_id`
   - `ordinal`
   - `archive_page`
   - `archive_position`
   - `title`
   - `published_at`
   - `url`
   - `canonical_url`
   - `classification`
   - `body_read`
   - `rationale`
   - `matched_signals`
   - `already_in_ai_first_ledger`
4. Independently verify continuity/order/canonical URLs/body-read truthfulness/rationale/signals/AI-first lookup.
5. Write `.manifest.jsonl` and `.scan.jsonl` to branch `jj-scan-inbox` under `research/incoming/site_scan/`.
6. ONLY after both are complete and verified, create the matching `.ready.json` marker.
7. Once READY exists, do not overwrite that batch during normal continuation.
8. Continue to the next ten without waiting for CX.

## Producer / consumer separation

### GPT = research producer

GPT is responsible for:

- locating the correct JJ articles
- reading complete article bodies
- understanding each article
- Coverage classification
- rationale
- matched signals
- checking whether the article already exists in the AI-first article ledger
- ten-article quality review
- writing staging files to `jj-scan-inbox`
- creating READY markers
- continuing to the next non-READY batch

GPT must NOT delegate semantic article research/classification back to CX.

### CX / Codex = deterministic formal-ingestion consumer

CX is responsible for:

- reading READY batches from `jj-scan-inbox`
- consuming only the next contiguous READY batch after the formal `main` tail
- using `tools/site_scan_ledger.py`
- dry-run / validation
- append to formal manifest and site-scan ledger
- commit
- push
- remote readback
- stop on the first failed transaction rather than skipping ahead

CX must not re-research or reclassify GPT-fixed READY records unless a specific error investigation is requested.

Do NOT merge the entire `jj-scan-inbox` branch into `main`. The GitHub “Compare & pull request” banner for this branch is not the ingestion workflow. `jj-scan-inbox` is a staging queue, not a merge branch.

## Formal ledgers that Coverage GPT must not modify

During full-site Coverage, do NOT modify:

- `research/article_ledger.jsonl`
- `research/checkpoint_ledger.jsonl`

Do NOT allocate `JJ-000299` or any new formal AI-first article ID during Coverage.

Do NOT begin selective AI-first deep-read merely because a HIGH/MEDIUM article is encountered. Finish full-site Coverage first.

## Scheduled automation

An enabled ChatGPT automation named `JJ Coverage Scan` exists.

Schedule:

- every 2 hours
- timezone: `Asia/Taipei`
- exact schedule
- each run processes at most 100 articles
- each ten-article batch must become READY before continuing
- each run dynamically re-reads `main` and `jj-scan-inbox`
- each run starts from the first contiguous non-READY batch
- existing READY batches are not overwritten
- if time/tool/site limitations occur, the run stops at the last safe READY checkpoint
- scanning stops permanently after `JJ-SCAN-000783`

The scheduled automation is intentionally state-independent from a long-lived chat. GitHub is the recovery/state mechanism.

## Manual chat / connection interruption

The previous manual scan chat experienced a UI/streaming connection interruption after `071..080` had already become READY.

The durable conclusion is:

- `071..080` is safe in GitHub staging.
- Anything after `080` that was not READY is not counted as complete.
- A fresh chat or scheduled run must re-read GitHub and resume from the first non-READY batch.

Do not repeat a READY batch just because the previous browser chat disconnected.

## Stop condition

Coverage is complete only when the exhaustive Coverage sequence has reached:

`JJ-SCAN-000783`

After that:

1. stop creating new scan IDs;
2. verify `783 / 783` Coverage;
3. then perform the planned full-site consolidated classification / topic clustering / research map;
4. only after that begin the Lineage 8.8 implementation/static/runtime validation phase.

## Fresh-window bootstrap procedure

A new project chat should first read this handoff and then verify live GitHub state. Its first actions must be:

1. Read `docs/handoffs/2026-09-09-jj-coverage-scan-window-handoff.md` from `main`.
2. Read live `main` HEAD and formal site-scan tail.
3. Read live `jj-scan-inbox` HEAD and enumerate READY files/batches.
4. Compute the first contiguous non-READY batch from live GitHub state.
5. Continue Coverage from that batch with the ten-article checkpoint model.
6. Never redo existing READY batches.
7. Never allocate `JJ-000299` during Coverage.
8. Never modify Article/Checkpoint ledgers during Coverage.
9. Stop at `JJ-SCAN-000783`.

This live-GitHub rebaseline is mandatory because the scheduled automation may have progressed while the manual chat was closed.
