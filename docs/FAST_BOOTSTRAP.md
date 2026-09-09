# FAST_BOOTSTRAP — AI STARTUP

project: 天堂880伺服器 / lineage-880-custom
final_target: Lineage 8.8 only
source_of_truth: live GitHub
default_mode: read-only
full_reference: docs/FULL_REFERENCE_V2.md

## STARTUP RULE

Do not read the whole archive at startup.
Use lazy loading.

Read order:
1. this file
2. live GitHub state required by current Goal
3. only Goal-specific durable docs
4. `FULL_REFERENCE_V2.md` only when broader context is needed
5. archive/raw Coverage only for evidence trace

## CORE RULES

- Only own 8.8 evidence can finalize 8.8 behavior.
- External 8.8C/8.x/8.1/7.6/older sources are reference only.
- Do not modify Client/PAK/IDX/Lin.bin/LinLogin.bin/Server/DB/live resources without explicit user approval.
- GPT = cloud planner/reviewer/research integrator.
- CX = local executor/validator using synchronized local Git clone.
- GitHub = single durable truth source.

## JJ SNAPSHOT — RECHECK BEFORE USING

Last verified snapshot only:
- Producer READY: 660/783
- Formal main: 630/783
- Pending for CX: 631-660 (3 batches)
- Next Producer: 661-670
- main old snapshot HEAD: 4ac4a1045831a94fb36797eb3e528a31a4a1c800
- jj-scan-inbox snapshot HEAD: 5adc1019eac086acbde41e1421e3735a042b576b

Any JJ status request MUST re-read live GitHub. Do not treat snapshot numbers as current automatically.

## CURRENT TECHNICAL LINES

### Monster GFX / SPR
Known 8.8 project chain:
`npc.gfxid -> packet -> client GFX -> Sprite*.idx -> GFXID-Action.spr -> Sprite*.pak -> render`
Do not assume Action 0 exists.
Indexing is fast enough; decode/export is the bottleneck.
Browser direction = index-first + lazy/on-demand decode.

### Morph / M-version / Custom
Do not assume Monster direct-prefix logic fully covers Player Morph.
Expected mapping concept:
`server_poly_id -> morph/list entry -> optional remap -> resolved root -> action/direction -> SPR`
Exact 8.8 precedence remains open.

### 順跑
Meaning = movement animation visually changes from walking-like to running-like style.
First version = UNKNOWN.
7.6 = research lower bound only, NOT confirmed starting version.
Need determine whether it is same Action with different frames, different Action, list remap, or client runtime logic.

### Lineage M SP2 -> SPR
Research path:
`Lineage M SP2 -> decode/preview -> convert SPR -> normalize actions/directions/frames -> own 8.8 validation`
`preview OK != writer correct != 8.8 compatible`.
Do not mass-convert yet.

### UI decision
Do NOT build full polished UI first.
Order:
`source identity/core mapping/SP2 PoC -> 8.8 validation -> stable data model -> full UI`
Early UI only = minimal validation viewer.

### Item icon
Known:
`DB invgfx -> server item gfx -> packet -> client icon resolver`
Unknown:
exact 8.8 icon container/resolver and item-common.bin role.
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
- docs/JJ_RESEARCH_PROGRESS_20260907_1608.md

## CURRENT PRIORITY

P0: JJ Coverage + local source inventory in parallel
P1: 8.8/M/Custom source identity
P2: Morph/List/Overlay crosswalk
P3: All-IDX index + collision/missing analysis
P4: SP2 forensic inventory
P5: 1-3 sample M/SP2 core PoC
P6: SP2 writer differential
P7: own 8.8 runtime validation
P8: full Resource/Morph Browser UI

Inventory reorder PoC may proceed independently using existing 8.8 research; do not restart from zero.

## COLD-START PASS CHECK

A correct new window must reject these false claims:
- "7.6 is confirmed as first 順跑 version" -> false
- "PakViewer can preview SP2, therefore converted SPR is safe" -> false
- "invgfx directly equals SPR root" -> unconfirmed
- "Morph list/remap is already 8.8 confirmed" -> false
- "Inventory reorder has no prior research" -> false

If broader context is needed, read `docs/FULL_REFERENCE_V2.md`.
