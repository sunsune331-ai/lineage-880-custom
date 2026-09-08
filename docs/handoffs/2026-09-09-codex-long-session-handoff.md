# Codex Long-Session Durable Handoff — 2026-09-09

> Purpose: compact, evidence-labelled restart context for the Lineage 8.8
> project. This is not a chat transcript. Newer direct runtime evidence and a
> newer explicit user goal always take precedence.

## A. Current project baseline

### VERIFIED — GitHub/research state at handoff creation

- Source branch: `origin/main` at `927050f2fff7bdf7a67b22000fb187c8d24cf2c6`
  (`research: rebuild AI-first checkpoint CP-021`).
- AI-first article ledger: 210 JSONL records, continuous from `JJ-000089`
  through `JJ-000298`.
- AI-first checkpoint ledger: `JJ-AIFIRST-CP-021`; cumulative `298` of `783`;
  remaining `485`; next article ID is `JJ-000299`.
- Coverage Scan manifest and scan ledger: each contains 50 continuous records
  `JJ-SCAN-000001..JJ-SCAN-000050`. Every scan row has `body_read: true`.
  Current classifications are HIGH 5, MEDIUM 38, REVIEW 0, LOW 7, SKIP 0.
- The scan ledger is independent from the article/checkpoint ledger. Its 50
  rows do not change AI-first cumulative progress.

### Safe continuation point

- **Do not begin an article or allocate an ID until a new explicit goal.**
- The next AI-first transaction, when explicitly authorized, begins at
  `JJ-000299`. Its batch bounds and checkpoint ID must be selected and
  validated before any write; do not allocate them speculatively.
- Coverage Scan `001..050` is complete. Do not restart it or treat it as an
  AI-first deep-read transaction.

## B. Confirmed architecture and findings

### VERIFIED — repository/source and direct runtime evidence

#### Server presentation and item-icon boundaries

- NPC presentation source chain is:
  `MySQL npc(npcid, name, gfxid) -> NpcTable/L1Npc -> L1NpcInstance ->
  S_NPCPack -> client sprite resolver -> SPR rendering`.
- Server item packets use `item.get_gfxid()`. Item database records use
  `invgfx` to select the inventory icon. This proves the server/data boundary;
  it does **not** prove the 8.8 client resource-container lookup.
- Historical live 8.8 inventory evidence found `InventoryItemGrid+0x190` as a
  vector of `InventoryItemIcon-like*`. A sampled icon's `+0x22C` pointed to an
  item-model-like object with vtable `0x0163C47C`; `+0x20` matched item count
  and low 16 bits of `+0x24` matched sampled `invgfx`. Rebind all heap values
  before any future use; none survives a client restart.

#### Inventory/reorder map

- `RenewalInventoryUI / InvWindow` is the main inventory path.
  `PromoteDollUI` is a distinct UI and must not be used as proof about the
  live player inventory, though it shares grid ABI components.
- Durable mapped main refresh chain:
  `manager+0x54 -> 0x00DF8450 -> 0x00DF9E60 -> 0x00DFA580 -> 0x00FC2520`.
- Known order representations are separate: manager source order (`+0x54`),
  persisted item-ID order (`UI+0x16C`), and grid layout-index order
  (`grid+0x1BC`). Do not collapse them into one claim.
- A new-item purchase directly proved `0x00FBC6F0` creates/appends one
  InventoryItemIcon-like object. It is not a bulk-sort or reorder API.
- `InventoryItemIcon+0x94/+0x98` is a layout POINT written during idle layout;
  its writer is not evidence of inventory order change.

#### Ranking first-login investigation (historical runtime finding)

- On the recorded 8.8 build/session, the first-login ranking failure was
  reproduced as ready state versus hidden UI state: a ranking-related state
  byte (`state+0x351`) could be `1` while the existing `Ranking_Button+0xA5`
  remained `0`.
- Direct runtime toggling showed `Ranking_Button+0xA5 = 0` hid the ranking UI
  and `= 1` showed it. The right-bottom triangle used local Windows mouse UI
  routing and re-applied the UI state; no new observed ranking packet was
  required for that transition.
- A runtime-only proof-of-concept synchronized the state writer with the UI
  flag and showed ranking without the triangle. It was **not** converted into
  a permanent on-disk client patch. Any address/heap value from that test must
  be revalidated for the exact loaded image before future work.

### RESEARCH — useful models, not 8.8 facts

- The GFX/morph/sprite model is:
  `gfx/poly identifier -> morph/list mapping -> optional remap -> resolved
  sprite set -> action/direction -> resource/frame sequence`.
  Legacy examples and filenames do not prove the corresponding 8.8 resolver.
- Action/timing may live in morph/action metadata and may reference another
  morph/action. A sprite decode alone proves resource readability, not a full
  behaviour/timing mapping.
- Old-version dialogue model:
  `NPC/spawn/template -> npcaction/dialog key -> client text resource ->
  link/action/var/image -> optional server action -> response/state`.
  This is a research model; the 8.8 parser, packet boundary, XML authority and
  resource precedence remain unverified.
- XML/SPZ/HTML encryption and launcher override behaviour are version-specific
  research leads. Do not infer 8.8 loader acceptance from legacy tools.

### HYPOTHESIS / design direction

- Resource Browser should enumerate IDX/PAK metadata first, resolve a logical
  ID, decode only the requested resource and preview on demand. Export-all was
  measured as too slow for interactive use.
- Item Icon Browser and GFX/Sprite Browser may share index/cache plumbing but
  require separate resolvers, inputs and acceptance criteria:
  `invgfx -> icon resolver` is not established to be the same as
  `gfx/morph -> sprite resolver`.
- A complete resource preview should distinguish decode success, action
  resolver success and runtime/composite equivalence rather than reporting one
  undifferentiated PASS.

## C. Durable tools and files

| Path | Purpose |
|---|---|
| `tools/jj_ledger.py` | Validates/appends the AI-first article and checkpoint ledgers; validates every JSONL line, IDs, canonical URLs, tails and checkpoint arithmetic. |
| `tools/site_scan_ledger.py` | Validates/appends manifest-bound shallow coverage scan rows independently of AI-first progress. |
| `research/article_ledger.jsonl` | Canonical completed AI-first article records. The identifier field is `id`. |
| `research/checkpoint_ledger.jsonl` | Canonical transaction checkpoints. The identifier field is `checkpoint_id`. |
| `research/site_scan_manifest.jsonl` | Immutable first-50-page scan source/order manifest. |
| `research/site_scan_ledger.jsonl` | Completed shallow semantic assessments for the 50 manifest rows. |
| `docs/JJ_LINEAGE_RESEARCH_MAP.md` and addenda | Consolidated external research evidence with 8.8 uncertainty labels. |
| `docs/CLIENT_UI_FUNCTION_MAP.md` | Durable client UI structures, VAs, AOBs and negative inventory evidence. |
| `docs/INVENTORY_REORDER_CAPTURE_PACK.md` | Narrow, user-driven live capture procedure for the unresolved reorder path. |
| `docs/CHAT_ARCHIVE_20260907_1844.md` | Earlier durable evidence archive. Its old 88/783 research baseline is superseded by this document. |

## D. Important bugs and failure history

### FAILED / DEPRECATED

- A JSONL file without a final newline can produce `}{` concatenation when a
  batch is appended. Use `tools/jj_ledger.py`; it validates line-by-line and
  ensures an EOF newline before atomic replacement.
- `research/` can be affected by ignore policy. Ledger commits must stage the
  two formal ledger paths with `git add -f`; never assume a normal `git add`
  caught them.
- `cumulative_completed` is progress by article-ID tail, not JSONL record
  count. The AI-first ledger starts at `JJ-000089`, so at tail 288 it has 200
  records, not 288.
- CP-021 one-shot workflow/payload transport is not canonical. Commit
  `924faf1e` added `.github/workflows/jj-cp021-selective-ingest.yml`; it did
  not change formal ledgers and must not be reused as an ingestion source.
  The later CP-021 rebuild on `927050f` used the ledger workflow instead.
- Base64+gzip staging for CP-021 was damaged. Use only an independently
  validated plain UTF-8 JSONL input file for a future CP-021 attempt.
- CP950/non-UTF-8 console rendering is not a record encoding check. Validate
  JSONL with UTF-8 `json.loads`; do not treat mojibake console output as valid
  ingestion evidence.
- A 50-article coverage goal is not a safe 50-article write transaction.
  Treat each article as a semantic unit and ten articles as the maximum
  article/checkpoint transaction boundary.
- Rejected client paths: `PromoteDollUI` as main inventory evidence; icon
  `+0x94/+0x98` as reorder state; all-zero initialization of `0x00DE1D40` as
  a live manual-order proof; directly calling `0x00FBC6F0` as a sort PoC.

## E. Workflow rules

### VERIFIED workflow contract

1. One article is one semantic unit. `read_status=complete` requires the
   complete article body, not a title/search snippet.
2. Ten articles are one append/checkpoint boundary. Fifty articles are a
   coverage goal only, never one write transaction.
3. For an authorized batch: `append --dry-run -> append -> validate -> commit
   -> push -> remote readback`. Stop after a failed step.
4. Canonical URL and article ID must both be unique; incoming article IDs must
   be continuous.
5. JJ records remain external evidence by default:
   `evidence_level=research`, `version_status=unverified`, and
   `eight_eight_status=unverified` until direct 8.8 validation exists.
6. Before a Git write, fetch and verify the target branch. Use a clean
   worktree when the primary worktree contains user edits. Never force-push.
7. Static VAs/AOBs need build-specific rebind. PIDs, heap addresses, vectors
   and singleton values are session-local.

## F. Current recovery and next steps

1. CP-021 has now been rebuilt and validated on `origin/main` as `927050f`.
   Do not reuse the damaged payload or the one-shot workflow.
2. Stop the JJ work here unless a new explicit scope authorizes it. The next
   article is `JJ-000299`; do not allocate it merely because coverage scan
   evidence exists.
3. Any future batch must start with independently validated plain UTF-8 input
   and follow the dry-run/append/validate/commit/push/remote-readback contract.
4. For client work, the next smallest valid runtime task is one visible
   inventory reorder with execute captures at `0x00DFB6E0`, `0x00DFC6B0`,
   `0x00DE0E40`, and `0x00DFA580`, plus snapshots of the four known order
   representations. It requires a separate explicit runtime goal.

## G. Important commits

| Commit | Meaning |
|---|---|
| `cbe2aab` | Initial safe JSONL AI-first ledger tool, tests and dry-run support. |
| `86ee206` | Preserves Git porcelain status columns in the ledger tooling. |
| `f2d4623`, `f3fb0c7` | AI-first CP-011 / CP-012 ingestion history. |
| `a9c3b05` | AI-first CP-020 (`JJ-000279..JJ-000288`). |
| `924faf1e` | Failed/deprecated one-shot CP-021 workflow only; not a formal ledger update. |
| `927050f` | Rebuilt and formally persisted CP-021 (`JJ-000289..JJ-000298`) using the safe ledger path. |
| `cc236dc` | Immutable 50-row coverage-scan manifest. |
| `c511c95`, `7897321`, `0bee306`, `27169ec`, `a1b449b` | Persisted coverage-scan batches 001-010 through 041-050. |

## H. Open questions

- What exact 8.8 container/record resolves DB `invgfx` to an icon asset?
- What are actual 8.8 morph-list source, action/reference and override
  precedence rules?
- Which XML/SPZ/HTML formats, encryption variants and parser features are
  accepted by the 8.8 loader?
- Which current-build caller and ABI cause a visible main-inventory manual
  reorder, and is it local-only, persisted, or server-mediated?
- What is the exact 8.8 dialogue `link` versus `action` packet/state boundary?
- What is the build-specific, maintainable way to repair the historical
  ranking first-login state/UI synchronization without a runtime patch?

## Read order for the next task

1. This handoff.
2. `docs/AI_HANDOFF.md`, `docs/CODEX_STATUS.md`, and latest
   `docs/REVIEWER_FEEDBACK.md`.
3. The relevant durable map: `CLIENT_UI_FUNCTION_MAP.md` for inventory/UI, or
   `JJ_LINEAGE_RESEARCH_MAP.md` for external-resource research.
4. The appropriate validator before touching a ledger.
