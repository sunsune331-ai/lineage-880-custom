# FAST_BOOTSTRAP — AI STARTUP

project: 天堂880伺服器 / lineage-880-custom
final_target: Lineage 8.8 only
source_of_truth: live GitHub
default_mode: read-only
full_reference: docs/FULL_REFERENCE_V3.md

## FIRST RULE

第一準則：先驗證方向。核心假設一旦不成立，立即停止；不得沿錯誤方向繼續深挖、擴大搜尋或完成後續工作。只有方向驗證通過，才進入下一步。

Pattern:
`明確假設 -> 最小驗證 -> PASS 才深入 / FAIL 立即停止 -> 回 GPT 修正方向`

## STARTUP RULE

Do not read the whole archive at startup.
Use lazy loading.

Read order:
1. this file
2. live GitHub state required by current Goal
3. only Goal-specific durable docs
4. `docs/FULL_REFERENCE_V3.md` only when broader context is needed
5. V2/old handoffs/raw Coverage only for evidence trace

## CORE RULES

- Only own 8.8 evidence can finalize 8.8 behavior.
- External 8.8C/8.x/8.1/7.6/older sources are reference only.
- Do not modify Client/PAK/IDX/Lin.bin/LinLogin.bin/Server/DB/live resources without explicit user approval.
- GPT = cloud planner/reviewer/research integrator.
- CX = local executor/validator using synchronized local Git clone.
- GitHub = single durable truth source.
- Prefer minimal falsifiable validation before broad research or implementation.

## CX MODEL / EFFORT POLICY — V3 QUICK RULES

Approved baseline:
- GPT-5.6 Sol = primary/default engineering and difficult reasoning
- GPT-5.6 Luna = fast deterministic/read-heavy work
- GPT-5.6 Terra = experimental only until bounded A/B validation proves a task-specific advantage
- GPT-5.5 = compatibility/backup/second opinion
- GPT-6 family = DEFERRED / DISABLED for current V3 baseline; do not enable automatically

Effort levels:
`LIGHT / MEDIUM / HIGH / EXTREME / ULTRA`

Default routing:
- Luna + LIGHT/MEDIUM: git/status/SHA, enumeration, manifest inspection, simple checks
- Sol + MEDIUM: routine scripts, validators, small bounded fixes
- Sol + HIGH: multi-file engineering, schema/data-model work, repair planning
- Sol + EXTREME: reverse engineering, Morph/SP2/runtime ambiguity, conflicting evidence
- Sol + ULTRA: exceptional root-cause cases only after lower levels are insufficient
- Terra + MEDIUM/HIGH: bounded A/B experiments only
- GPT-5.5 + LIGHT/MEDIUM: compatibility or independent sanity review

Escalation rule:
`lowest reasonable effort -> validator/tool evidence -> one bounded retry -> escalate one level OR fail-stop`

Do not use higher effort to compensate for missing evidence. Do not use ULTRA by default. Model selection never overrides read-only/write permissions, FIRST RULE, evidence hierarchy, or validator gates.

For substantial CX tasks, GPT should include:
- `MODEL_RECOMMENDATION`
- `EFFORT_RECOMMENDATION`

Full policy: `docs/FULL_REFERENCE_V3.md` section `CX MODEL / EFFORT ROUTING POLICY — V3`.

## JJ COVERAGE — COMPLETE / CLOSED

Formal scope:
`JJ-SCAN-000001..JJ-SCAN-000780`

Verified completion state:
- formal manifest tail = `JJ-SCAN-000780`
- formal ledger tail = `JJ-SCAN-000780`
- record count = `780`
- final validator = `PASS`
- remote readback = `PASS`
- completion main HEAD = `ab7ff68f5d6b595da83fedb4fc1c754e81b06734`

Excluded under current scope:
- `JJ-SCAN-000781`
- `JJ-SCAN-000782`
- `JJ-SCAN-000783`

Never create:
- `JJ-SCAN-000784`

Known sequence defects were repaired before completion:
- omission at 384: `天堂私服 | 對外設定(數據機 & WiFi-DHCP)`
- old staging duplicate around 670/671: `OllyDBG - 第九章 | 反匯編練習 (二) 中`

Do not reopen Coverage sequencing unless new concrete corruption evidence appears.

## CURRENT TECHNICAL LINES

### Monster GFX / SPR
Own-8.8 project chain:
`npc.gfxid -> packet -> client GFX -> Sprite*.idx -> GFXID-Action.spr -> Sprite*.pak -> render`

Do not assume Action 0 exists.
Indexing is fast enough; decode/export is the bottleneck.
Browser direction = index-first + lazy/on-demand decode.

### Morph / M-version / Custom
Do not assume Monster direct-prefix logic fully covers Player Morph.
Expected mapping concept:
`server_poly_id -> morph/list entry -> optional remap -> resolved root -> action/direction -> SPR`
Exact own-8.8 precedence remains open.

### 順跑
Meaning = movement animation visually changes from walking-like to running-like style.
First version = UNKNOWN.
7.6 = research lower bound only, NOT confirmed starting version.
Need determine same Action/different frames vs different Action vs list remap vs runtime selection.

### Lineage M SP2 -> SPR
Research path:
`Lineage M SP2 -> decode/preview -> convert SPR -> normalize actions/directions/frames -> own 8.8 validation`

`preview OK != writer correct != 8.8 compatible`.
Do not mass-convert yet.

### UI decision
Do NOT build full polished UI first.
Order:
`source identity/core mapping/SP2 PoC -> own 8.8 validation -> stable data model -> full UI`
Early UI only = minimal validation viewer.

### Item icon
Known:
`DB invgfx -> server item gfx -> packet -> client icon resolver`
Unknown:
exact own-8.8 icon container/resolver and item-common.bin role.
Do not assume invgfx == SPR root.

### Inventory reorder
Not zero-start.
Known:
- main inventory = RenewalInventoryUI / InvWindow
- main grid = InventoryItemGrid
- source owner = RenewalInventory manager
- PromoteDollUI != main inventory
- manager+0x54 = source order
- UI+0x16C = persisted item-ID order
- grid+0x1BC = layout-index order
- manual reorder and refresh paths already mapped substantially
- stable multi-key sort is preferred PoC design

Open:
- safest automatic-sort writable/order representation
- persistence/refresh semantics
- exact runtime entry/ABI

For inventory work read:
- docs/AI_HANDOFF.md
- docs/CLIENT_UI_FUNCTION_MAP.md
- docs/INVENTORY_REORDER_CAPTURE_PACK.md
- docs/INVENTORY_REORDER_CAPTURE_RESULT.md
- docs/CHAT_HANDOFF_20260907.md

## V3 PRIORITY

Coverage is closed and no longer P0.

P0: read-only local source inventory + provenance baseline
P1: 8.8 / M / Custom source identity and collision map
P2: Morph/List/Overlay crosswalk
P3: All-IDX index + duplicate/collision/missing analysis
P4: SP2 forensic inventory
P5: 1-3 sample M/SP2 core PoC
P6: SP2 writer differential A/B/C/D
P7: own 8.8 runtime validation
P8: full Resource/Morph Browser UI

Independent active line:
- Inventory reorder PoC may proceed using existing own-8.8 evidence; do not restart from zero.

## V3 IMMEDIATE NEXT GOAL

Primary progression:
`source inventory -> provenance/identity -> collision map -> morph crosswalk -> SP2 sample PoC -> own 8.8 validation`

Start with a bounded read-only local source inventory.
Do not start with full UI and do not mass-decode/export.

FIRST RULE gate:
- hypothesis: local resource folders contain enough stable metadata to build a source inventory without decoding all payloads
- minimal validation: representative subset with path/size/mtime/hash/source label and proof of zero source modification
- PASS -> scale inventory
- FAIL -> stop and correct assumptions

## COLD-START PASS CHECK

A correct new window must reject these false claims:
- `JJ Coverage is still in progress` -> false
- `JJ-SCAN-000781..783 are pending formal ingestion` -> false under current scope
- `JJ-SCAN-000784 should be created` -> false
- `7.6 is confirmed as first 順跑 version` -> false
- `PakViewer preview proves converted SPR is safe` -> false
- `invgfx directly equals SPR root` -> unconfirmed
- `Morph list/remap is already own-8.8 confirmed` -> false
- `Inventory reorder has no prior research` -> false
- `GPT-6 should be enabled automatically for difficult work` -> false; V3 defers GPT-6
- `ULTRA should be used for all CX tasks` -> false; use proportional effort

## NEW-WINDOW HANDOFF EXPECTATION

For broad continuity, a new window should read:
1. `docs/FAST_BOOTSTRAP.md`
2. live `main` HEAD
3. `docs/FULL_REFERENCE_V3.md`
4. only Goal-specific durable docs as needed

This is sufficient to recover the important project state without replaying old conversations. Exact historical addresses/captures/raw evidence still require the referenced goal-specific documents.

If broader context is needed, read `docs/FULL_REFERENCE_V3.md`.
