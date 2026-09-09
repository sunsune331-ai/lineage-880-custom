# FULL_REFERENCE_V2 — AI HANDOFF

meta:
  project: 天堂880伺服器 / lineage-880-custom
  target: Lineage 8.8 only
  audience: ChatGPT/GPT reviewer, Codex/CX executor
  purpose: durable AI-readable project state; not transcript
  source_of_truth: live GitHub
  default_mode: read-only
  last_compiled: 2026-09-10

## 0. NON-NEGOTIABLE RULES

- Final truth target = **Lineage 8.8 Client / Server only**.
- External versions are reference/evidence only; never auto-promote to `8.8 CONFIRMED`.
- Evidence priority: `own 8.8 runtime/source/resource > external 8.8/8.8C > external 8.x > 8.1 > 7.6 > older historical/tooling clues`.
- Default read-only. Do not modify Client, Sprite*.idx/pak, Lin.bin/LinLogin.bin, Server, DB, list.spr/list.spz, launcher/live resources unless user explicitly approves.
- PoC outputs should stay under `LineageAIResourceToolkit/outputs/`.
- Unknown third-party EXE/forum tools: research first, do not execute by default.
- Do not reuse session-local PID/heap/singleton values across restarts; static VA also requires build/AOB revalidation.

## 1. GPT / CX / GITHUB ARCHITECTURE

- GPT = upstream planner/reviewer/research integrator; primarily reads cloud/GitHub.
- CX/Codex = local executor/validator; primarily reads local Git clone and local Client/resources/runtime.
- GitHub = only durable Source of Truth.
- Local repo = synchronized high-speed working copy, not a second truth source.
- Recommended cycle: `GitHub -> pull -> local analysis/validation -> commit/push -> GitHub remote readback`.
- Local derived indexes/cache are allowed (e.g. research.sqlite, gfx_index.json, morph_index.json) if source data remains unchanged.

## 2. NEW-WINDOW LOADING POLICY

Use lazy loading. Do not read all archives/history at startup.

Startup order:
1. `docs/FAST_BOOTSTRAP.md`
2. live GitHub state needed by current Goal
3. only Goal-specific docs
4. archive/old handoffs/raw Coverage only when evidence trace is required

Goal-specific examples:
- JJ Coverage -> coverage handoff/ledgers only
- Morph/M/SP2 -> morph/resource/PakViewer/external evidence only
- Inventory reorder -> AI_HANDOFF + CLIENT_UI_FUNCTION_MAP + capture docs

## 3. J.J. FULL-SITE COVERAGE

fixed_total: 783
producer: GPT
consumer: CX
staging_branch: jj-scan-inbox
batch_size: 10
batch_files: manifest.jsonl + scan.jsonl + ready.json

Producer rules:
- read full article body in archive/site order
- every 10 = permanent transaction boundary
- READY only after manifest+scan complete/validated
- do not modify `research/article_ledger.jsonl` or `research/checkpoint_ledger.jsonl` during Coverage
- do not allocate `JJ-000299`

Consumer rules:
- re-read live GitHub every run
- compute highest contiguous READY and freeze `SNAPSHOT_TARGET`
- consume only through that target; do not chase newer READY created mid-run
- each batch: preflight -> validate -> dry-run -> formal ingest -> validate -> diff -> commit -> push -> remote readback
- one batch per commit
- never merge `jj-scan-inbox`
- fail-stop on duplicate/gap/mismatch/validator failure/unexpected diff/conflict/push reject/readback mismatch/timeout

last_verified_snapshot:
  main_head: 4ac4a1045831a94fb36797eb3e528a31a4a1c800
  main_commit: research: ingest coverage scan 621-630
  formal_tail: JJ-SCAN-000630
  inbox_head: 5adc1019eac086acbde41e1421e3735a042b576b
  inbox_commit: research: mark coverage scan 651-660 ready
  ready_batches: [631-640, 641-650, 651-660]
  producer_ready: 660/783
  formal_main: 630/783
  pending_for_cx: 30 articles / 3 batches
  next_producer: 661-670
  producer_remaining: 123
  formal_remaining: 153

IMPORTANT: these are snapshot values, not permanent live truth. Any status question must re-check GitHub.

Historical AI-first ledger reached 298/783 and is a separate pipeline. Never confuse `298` with current exhaustive Coverage.

## 4. MONSTER GFX / SPR — 8.8 STRONG EVIDENCE

server_client_chain:
`MySQL npc(name,gfxid) -> NpcTable/L1Npc -> L1NpcInstance -> S_NPCPack -> client GFX -> Sprite*.idx -> GFXID-Action.spr -> Sprite*.pak -> render`

Known:
- Monster GFX root strongly corresponds to SPR filename prefix.
- suffix = action (`110-0.spr`, `110-1.spr`, ...).
- same root can exist in multiple SpriteXX/client sources; source identity must be retained.
- do not assume Action 0 exists.

Historical inventory stats:
- 17 Sprite manifest text files
- ~13.9 MB
- ~252,636 lines
- ~228,918 parsed SPR entries
- ~10,353 GFX roots

Performance conclusion:
- indexing is not the main bottleneck
- decode/export is the main bottleneck
- browser should be index-first + on-demand decode, not export-all

## 5. GFX 13715 ACCEPTANCE

source: Sprite10.idx / Sprite10.pak
requested: 13715-0.spr
result: absent
actual: 13715-18.spr
action: 18
frames: 12
selected_frame: 3
visible: character + bow
acceptance_png: test_13715_Sprite10.png
meaning: SPR -> frame -> render pipeline works; never assume action 0.

## 6. RESOURCE / GFX BROWSER DIRECTION

Do not return to export-first.

preferred_architecture:
`IDX inventory -> virtual list -> lazy decode -> memory render -> thumbnail cache -> cancellation -> low concurrency`

PakViewer is high-value reference for:
- VirtualMode/RetrieveVirtualItem
- lazy thumbnail
- cache
- background task/cancellation
- Gallery
- All IDX
- SPR playback

Future Browser modes:
- 8.8 Native / Monster GFX
- Player Morph
- M-version Morph
- Lineage-M-converted
- Custom Morph
- Unknown GFX
- Item Icon (separate resolver under same shell)

## 7. MORPH — EVIDENCE DISCIPLINE

Do NOT treat Monster GFX direct-prefix rules as complete Player Morph rules.

External/legacy evidence strongly supports a mapping layer:
`server_poly_id -> morph/list entry -> optional remap -> resolved sprite root -> action/direction -> SPR`

But exact 8.8 chain/precedence is still unverified.

Keep separate fields:
- server_poly_id
- morph_list_entry_id
- resolved_gfx_root_id
- sprite_source

Potential composite/overlay model from old evidence:
`body + clothes/weapon/effect + per-frame timing/offset`

Keep `effect_sprite_ids` / `overlay_refs`; do not assume overlay ID directly equals SPR root.

8.8 unresolved:
- list.spr vs list.spz runtime priority
- PolymorphList.xml role
- Lin.bin/LinLogin.bin loader behavior
- launcher custom-resource precedence/fallback
- exact server poly -> client resolved root chain

## 8. "順跑" DEFINITION — CORRECTED

Do not interpret 順跑 as a version progression term.

Project meaning:
- 順跑 = morph movement animation visually changes from classic walking-like frames to a more continuous/running style.
- It may be only different frames under the same movement Action; this is not yet proven.

version_policy:
- first version with 順跑 = UNKNOWN
- research lower-bound reference = 7.6
- `7.6` is NOT confirmed as the first 順跑 version
- versions 7.6+, 8.1, 8.x, 8.8 are searched only to trace evidence/evolution

Candidate `movement_style` values:
- WALK_STYLE
- RUN_STYLE
- UNKNOWN

Open questions:
1. same Action, different run-looking frames?
2. different Action ID?
3. morph/list action remap?
4. client runtime special selection logic?
5. can own 8.8 client directly play such run-style SPR?

## 9. M-VERSION / CUSTOM MORPH — PRIMARY RESEARCH LINE

Problem space:
- internet custom morphs
- M-version morphs
- old migrated morphs
- unknown-source SPR
- wrong name/ID mapping
- same root different visual across sources
- morph exists but SPR missing / SPR exists but morph unknown
- incomplete directions/actions

Target crosswalk:
`display_name -> source/version -> server poly ID -> morph/list entry -> resolved root -> Sprite source -> actions -> directions -> frames -> weapon/pose -> overlay/effect -> 8.8 compatibility`

Canonical source classes:
- 8.8_NATIVE
- M_VERSION
- LINEAGE_M_SP2_DIRECT
- LINEAGE_M_SP2_REBUILT
- LINEAGE_M_SP2_UNKNOWN_CONVERTER
- M_STYLE_RECREATED
- CUSTOM
- OLD_CLIENT
- LAUNCHER_CUSTOM
- UNKNOWN

Never merge `LINEAGE_M_*` with `M_STYLE_RECREATED` without provenance evidence.

Minimum sprite identity:
`source_client + source_version + SpriteXX + gfx_root + action`
Prefer adding entry/content hash.

Crosswalk statuses:
- CONFIRMED
- LIKELY
- CONFLICT
- MISSING
- UNKNOWN

Visual match:
- EXACT / NEAR / DIFFERENT / UNKNOWN

Collision types:
- NONE
- SAME_ID_DIFFERENT_VISUAL
- DIFFERENT_ID_SAME_VISUAL
- LIST_REMAP
- SOURCE_COLLISION
- OVERLAY_DIFFERENCE

## 10. LINEAGE M SP2 -> LINEAGE 1 SPR

Public evidence supports this production path:
`Lineage M client -> identify/extract SP2 -> preview/decode SP2 -> SP2 -> SPR -> normalize actions/directions/frames -> validate on own 8.8`

Correct public term found: **SP2 -> SPR** (not PS2 -> SPR unless separate evidence later appears).

Known SP2 parser clues from public PakViewer source:
- Lineage M SP2
- Brotli compression
- RGB565 palette
- multi-direction structure
- 24x24 block model
- direction -> frames[]
- frame offsets/bounds/block references/block data
- delta-encoded block IDs observed

Critical rule:
`SP2 preview OK != SPR writer correct != 8.8 compatible`

PakViewer Reader/Preview and SP2->SPR Writer must be rated separately.
Public issue evidence reported preview success but generated SPR corruption on older clients; therefore writer = reference/conflict until own 8.8 proof.

Differential set:
A = original SP2
B = LineageViewer2023 output SPR
C = PakViewer output SPR
D = known-good native 8.8 SPR

Highest-value first comparison: B vs C.
Final gold standard: D + own 8.8 runtime.

SP2 status progression:
- SP2_ONLY
- PREVIEW_OK
- CONVERTED
- STRUCTURE_MATCH
- ACTION_MATCH
- RUNTIME_PASS
- RUNTIME_FAIL
- UNKNOWN

Only `RUNTIME_PASS` can be considered for 8.8 confirmation.

## 11. UI ORDER DECISION — LOCKED

Do NOT build full polished Morph/Resource Browser UI first.

Order:
`M/M-style/SP2 source identity -> core mapping/parse/convert PoC -> 1-3 sample validations -> 8.8 structure/runtime validation -> data model stabilizes -> full UI`

Allowed early UI = minimal validation viewer only:
- source
- morph/GFX ID
- action
- direction
- frame
- prev/next
- play/pause

Defer polished Gallery/search/cards/favorites/full taxonomy/export-all until core data model is stable.

Reason: current major risk is data/mapping/writer compatibility, not UI technology.

## 12. ITEM INVENTORY ICON

Server/DB evidence:
- three item tables use `invgfx`
- item-related packets use `item.get_gfxid()`

Known boundary:
`DB invgfx -> server item gfx -> packet -> client icon resolver`

Unresolved on own 8.8:
- exact item-common.bin role
- final icon resource container
- exact invgfx -> asset mapping
- whether assets resolve through TBT/IMG/PNG/Sprite or another DAT/container

Do not assume monster SPR rules.

Historical runtime clue (session-local, must revalidate):
- `InventoryItemGrid+0x190` vector of InventoryItemIcon-like pointers
- sampled icon `+0x22C` -> item-model-like object
- low 16 bits at item-model-like `+0x24` matched sampled invgfx

PakViewer remains relevant because it supports TBT/IMG/PNG/DAT/Gallery, but item icon requires its own resolver.

## 13. INVENTORY REORDER — EXISTING 8.8 RESEARCH

This is NOT a zero-start problem.

Main inventory identity:
- `RenewalInventoryUI / InvWindow` = main inventory
- `InventoryItemGrid` = main grid
- `RenewalInventory manager` = source owner
- `PromoteDollUI != main inventory`

Known order representations:
- `manager +0x54` = source order
- `UI +0x16C` = persisted item-ID order
- `grid +0x1BC` = layout-index order

Known/recorded main refresh chain:
`manager+0x54 -> 0x00DF8450 -> 0x00DF9E60 -> 0x00DFA580 -> 0x00FC2520`

Known manual/local reorder path evidence:
`grid reorder event -> InventoryItemGrid vslot+0x1A0 = 0x00DFB6E0 -> base grid reorder 0x00FC2BB0 -> 0x00DFC6B0`

Known exclusions:
- `0x00FBC6F0` creates/appends one InventoryItemIcon-like object; not proven bulk reorder/sort
- `InventoryItemIcon+0x94/+0x98` is idle layout POINT, not reorder proof

J.J. / design evidence already extracted:
- use stable sort to preserve relative order for equal keys
- candidate PoC key:
  `category -> equip/consumable/etc -> name/id -> enchant/bless -> original order tie-breaker`
- existing client `ItemSortPred/merge-sort family` is known as a candidate but NOT proven connected to main inventory runtime path

Current real blocker:
- prove safest writable/order representation and persistence/refresh semantics for one automatic-sort PoC
- exact runtime producer/entry/ABI still requires own 8.8 validation

Relevant durable docs:
- `docs/AI_HANDOFF.md`
- `docs/CLIENT_UI_FUNCTION_MAP.md`
- `docs/INVENTORY_REORDER_CAPTURE_PACK.md`
- `docs/INVENTORY_REORDER_CAPTURE_RESULT.md`
- `docs/CHAT_HANDOFF_20260907.md`
- `docs/JJ_RESEARCH_PROGRESS_20260907_1608.md`

## 14. RANKING FIRST-LOGIN — HISTORICAL SECONDARY LINE

Historical runtime evidence:
- ranking state byte `state+0x351` could be 1 while `Ranking_Button+0xA5` stayed 0
- toggling button `+0xA5` hid/showed ranking UI
- right-bottom triangle appeared to route/reapply local UI state; no new ranking packet observed in that capture
- runtime-only PoC synchronized state writer with UI flag and showed ranking without triangle

All addresses/build/session assumptions must be revalidated before reuse. No on-disk patch without explicit approval.

## 15. HIGH-VALUE EXTERNAL REPOS / EVIDENCE

Highest-priority external 8.8 reference:
`Yilanchanlong/lineage-8.8`

Recorded external findings include:
- 8.8C Sprite IDX `_EXT` header
- record size 128 bytes
- offset +0x00, size +0x04, compressed_size +0x08, flags +0x0C, filename[112] +0x10
- 228,982 indexed .spr entries reported
- strict SPR structural validation passed 174,145 / 228,982; therefore do not assume every .spr payload shares identical frame structure
- Text.idx old-style count + 28-byte records; Text.pak decode research

Treat as `[EXTERNAL 8.8/8.8C][HIGH VALUE][OWN 8.8 VALIDATION REQUIRED]`.

Other important repos:
- `tony1223/PakViewer` — open source parser/viewer/editor; high-value resource/tool reference
- `L1j-Kiyoshi/kys8.1` — 8.1 server-side polymorph evolution reference (`PolyTable`, `L1PolyMorph`, polymorph DB); not 8.8 client mapping proof
- `baboqoo/L1J-Wanted` — newer/Remaster reference; includes Spritor/list.spz-related material
- `L1Rj/L1j-TW`
- `uglyoldbob/l1j-client`
- `WantedGaming/L1JR-RemasterConnector`
- `hillpath/L1J-KR_3.80`
- `JackerSyu/L1J-Tw-Husky`
- `l1j-en/classic`
- `l1j-en/launcher`

Discovery/download/audit states must be kept separate. Do not infer `downloaded` merely from `discovered`.

## 16. SOURCE INVENTORY / FORENSIC FIRST

Before large-scale Morph/SP2 work, inventory local sources read-only:
- *.idx
- *.pak
- *.spr
- *.sp2
- *.spx
- list.spr
- list.spz
- PolymorphList.xml
- *morph*.xml / *morph*.bin
- launcher config / Login.ini / custom *.pak / DAT

Per source retain:
- source label
- version
- relative path
- size
- mtime
- sha256

Preferred first-stage outputs (metadata only):
- source_inventory.jsonl
- sprite_entry_index.jsonl
- morph_resource_inventory.jsonl
- source_collisions.jsonl
- m_only_candidates.jsonl
- unknown_gfx_candidates.jsonl

Find:
- M-only root
- 8.8-only root
- same ID different hash/visual
- different ID same visual
- missing actions
- morph exists / SPR missing
- SPR exists / morph missing
- unknown GFX

For SP2 first-stage forensic inventory retain:
- source/path/size/sha256
- compression probe
- decoded marker
- palette mode/count
- raw direction/direction count
- frames per direction
- bounds
- block count
- parse success/failure

Do not mass-convert at this stage.

## 17. EVIDENCE LABELS / CONFIDENCE

Recommended qualifiers:
- [8.8 NATIVE]
- [EXTERNAL]
- [VERSION UNKNOWN]
- [8.8 UNVERIFIED]
- [STATIC]
- [RUNTIME]
- [SOURCE]
- [TOOL]
- [OLD CLIENT]
- [M VERSION]
- [LINEAGE_M_CONVERTED]
- [CUSTOM]
- [UNKNOWN SOURCE]

Never convert an external/legacy clue into a local 8.8 fact without direct validation.

## 18. CURRENT PRIORITY ORDER

P0:
- continue JJ Coverage to 783
- local Source Inventory
These can proceed in parallel.

P1:
- establish reliable 8.8 / M / Custom source identity

P2:
- Morph/List/Overlay crosswalk

P3:
- All-IDX read-only index + collision/missing analysis

P4:
- SP2 forensic inventory

P5:
- small M/SP2 core PoC (1-3 samples), not mass conversion

P6:
- SP2 writer differential A/B/C/D

P7:
- own 8.8 runtime validation

P8:
- full Resource/Morph Browser UI after data model stabilizes

Inventory reorder PoC is a separate active-capable line and already has significant prior research; do not restart from zero.

## 19. WHAT IS RELATIVELY CONFIRMED VS OPEN

Relatively strong own-project evidence:
- 8.8 Monster npc.gfxid -> packet -> client GFX basic chain
- Monster GFX root -> SPR filename prefix/action inventory relationship
- 13715 Sprite10 single SPR decode/render success
- DB invgfx / server item gfx basic boundary
- index build is not main performance bottleneck; decode/export is
- main inventory identity and multiple order representations have substantial static/runtime evidence

External/legacy confirmed but own 8.8 not final:
- Morph list/remap layer concept
- Morph overlay/composite concept
- list.spr/list.spz behavior on older client
- PolymorphList.xml / launcher relationships from external/legacy sources
- SP2 reader/preview and public converter existence
- PakViewer lazy/gallery architecture
- 8.1 PolyTable/L1PolyMorph server behavior
- external 8.8C `_EXT` IDX structure

Open on own 8.8:
- complete Morph precedence
- exact server poly -> list entry -> resolved root chain
- list.spr vs list.spz runtime priority
- PolymorphList.xml exact role
- launcher custom precedence/fallback
- first version of 順跑
- complete M-version name/ID/source mapping
- any SP2 writer's true 8.8 compatibility
- item icon final asset container/resolver
- safest automatic inventory reorder write/persist entry and exact ABI

## 20. NEW-WINDOW ACCEPTANCE TEST

A cold new window should answer correctly without old-chat copy/paste:

1. Final target? -> 8.8 only.
2. Can it modify live resources by default? -> no, read-only until explicit approval.
3. GPT/CX roles? -> GPT cloud reviewer/research; CX local executor/validator; GitHub truth.
4. JJ status? -> must re-check live GitHub; snapshot only says READY 660 / Formal 630.
5. 順跑 starts at 7.6? -> no; start version unknown, 7.6 only research lower bound.
6. 順跑 meaning? -> movement animation style (walking-like -> running-like), not version sequence.
7. M/SP2: full UI first? -> no; core/source/mapping/runtime first, minimal validation viewer only.
8. SP2 preview OK means usable SPR? -> no; reader/writer/runtime are separate gates.
9. invgfx directly equals SPR root? -> not confirmed; item icon resolver/container still open.
10. Inventory reorder starts from zero? -> no; substantial 8.8 function/order/refresh research already exists; remaining issue is safe write/persist/runtime validation.

PASS only if the new window preserves evidence boundaries, re-checks live state when needed, and does not revive already-disproved paths.
