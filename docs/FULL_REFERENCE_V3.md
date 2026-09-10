# FULL_REFERENCE_V3 — AI HANDOFF

meta:
  project: 天堂880伺服器 / lineage-880-custom
  target: Lineage 8.8 only
  audience: ChatGPT/GPT reviewer, Codex/CX executor
  purpose: durable AI-readable current project state; not transcript
  source_of_truth: live GitHub
  default_mode: read-only
  compiled_from: FULL_REFERENCE_V2 + completed JJ Coverage + live main state
  last_compiled: 2026-09-10

## 0. FIRST RULE — DIRECTION BEFORE DEPTH

第一準則：先驗證方向。核心假設一旦不成立，立即停止；不得沿錯誤方向繼續深挖、擴大搜尋或完成後續工作。只有方向驗證通過，才進入下一步。

Operational pattern:
`explicit hypothesis -> smallest falsifiable validation -> PASS continue / FAIL stop -> return to GPT reviewer`

This does not mean skipping analysis. It means form the strongest testable hypothesis first and fail-stop early if it is wrong.

## 1. NON-NEGOTIABLE RULES

- Final truth target = **Lineage 8.8 Client / Server only**.
- External versions are evidence/reference only; never auto-promote to `8.8 CONFIRMED`.
- Evidence priority: `own 8.8 runtime/source/resource > external 8.8/8.8C > external 8.x > 8.1 > 7.6 > older historical/tooling clues`.
- Default read-only. Do not modify Client, Sprite*.idx/pak, Lin.bin/LinLogin.bin, Server, DB, list.spr/list.spz, launcher/live resources unless user explicitly approves.
- PoC outputs should stay under `LineageAIResourceToolkit/outputs/`.
- Unknown third-party EXE/forum tools: research first; do not execute by default.
- Do not reuse session-local PID/heap/singleton values across restarts; static VA also requires build/AOB revalidation.
- GitHub is the only durable Source of Truth.

## 2. GPT / CX / GITHUB ARCHITECTURE

- GPT = upstream planner/reviewer/research integrator; primarily reads cloud/GitHub.
- CX/Codex = local executor/validator; primarily reads synchronized local Git clone and local 8.8 resources/runtime.
- Local clone is not an independent truth source.
- Standard cycle: `GitHub live read -> local sync -> minimal validation -> execution -> validator/diff -> commit/push -> remote readback`.
- Local derived indexes/cache are allowed if source resources remain unchanged.

## 3. NEW-WINDOW LOADING POLICY

Use lazy loading; do not reload the whole project archive every window.

Startup order:
1. `docs/FAST_BOOTSTRAP.md`
2. live GitHub state required by current Goal
3. only Goal-specific durable docs
4. `docs/FULL_REFERENCE_V3.md` for broad context
5. old handoffs/raw Coverage only when evidence trace is required

Old V1/V2 handoffs remain historical evidence, not the preferred startup source.

## 4. J.J. FULL-SITE COVERAGE — CLOSED / COMPLETE

fixed_site_total_observed: 783
formal_ingestion_scope: JJ-SCAN-000001..JJ-SCAN-000780
excluded: JJ-SCAN-000781, JJ-SCAN-000782, JJ-SCAN-000783
forbidden_nonexistent: JJ-SCAN-000784
status: COMPLETE

Final verified formal state:
- `FINAL_FORMAL_MANIFEST_TAIL = JJ-SCAN-000780`
- `FINAL_FORMAL_LEDGER_TAIL = JJ-SCAN-000780`
- `FINAL_RECORD_COUNT = 780`
- `FINAL_VALIDATOR = PASS`
- `REMOTE_READBACK = PASS`
- `UNEXPECTED_FILES_CHANGED = NONE`
- completion main HEAD: `ab7ff68f5d6b595da83fedb4fc1c754e81b06734`

Sequence repair history now resolved:
1. First divergence at `JJ-SCAN-000384` was an omission:
   - title: `天堂私服 | 對外設定(數據機 & WiFi-DHCP)`
   - canonical URL: `https://morosedog.gitlab.io/private-lineage-20210731-private-lineage-19/`
   - correct archive position: page 39 / position 4
2. Old staging duplicated `OllyDBG - 第九章 | 反匯編練習 (二) 中` at old 670/671.
3. Producer built a corrected 001-640 candidate, rebuilt affected 641-680 staging, preserved 681-780, and validated combined 001-780 with zero canonical duplicates and zero ordinal gaps.
4. CX replaced formal 001-640 and ingested 641-780 in 10-row commits through final 780.

Important closure rule:
- Do not reopen Coverage sequencing unless new concrete evidence shows a formal corruption.
- Do not allocate 781-783 into the formal Coverage scope under the current decision.
- Never create `JJ-SCAN-000784`.
- Historical AI-first article/checkpoint ledger is a separate pipeline; do not conflate it with completed Coverage.

## 5. MONSTER GFX / SPR — OWN 8.8 STRONG EVIDENCE

Server-to-client chain:
`MySQL npc(name,gfxid) -> NpcTable/L1Npc -> L1NpcInstance -> S_NPCPack -> client GFX -> Sprite*.idx -> GFXID-Action.spr -> Sprite*.pak -> render`

Known:
- Monster GFX root strongly corresponds to SPR filename prefix.
- suffix = Action.
- same root can exist in multiple SpriteXX/client sources; retain source identity.
- do not assume Action 0 exists.
- direct monster mapping does not prove complete Player Morph resolution.

Historical inventory:
- 17 Sprite manifest text files
- ~13.9 MB
- ~252,636 lines
- ~228,918 parsed SPR entries
- ~10,353 GFX roots

Performance conclusion:
- indexing is not the main bottleneck
- SPR decode/export is the main bottleneck
- browser architecture must be index-first + lazy/on-demand decode

## 6. GFX 13715 ACCEPTANCE

source: Sprite10.idx / Sprite10.pak
requested: 13715-0.spr
result: absent
actual: 13715-18.spr
action: 18
frames: 12
selected_frame: 3
visible: character + bow
acceptance_png: test_13715_Sprite10.png

Meaning:
- SPR decode -> frame -> render path works.
- Action 0 must never be assumed.

## 7. RESOURCE / GFX BROWSER DIRECTION

Do not return to export-first.

Preferred architecture:
`IDX inventory -> virtual list -> lazy decode -> memory render -> thumbnail cache -> cancellation -> low concurrency`

PakViewer is a high-value reference for:
- VirtualMode / virtual item retrieval
- lazy thumbnails
- cache
- background task/cancellation
- Gallery / All IDX
- SPR playback

Future Browser modes:
- 8.8 Native / Monster GFX
- Player Morph
- M-version Morph
- Lineage-M-converted
- Custom Morph
- Unknown GFX
- Item Icon using a separate resolver under the same shell

Full polished UI remains deferred until mapping/data model/writer compatibility are stable.

## 8. MORPH — EVIDENCE DISCIPLINE

Do not treat Monster direct-prefix rules as complete Player Morph rules.

External/legacy evidence supports the conceptual chain:
`server_poly_id -> morph/list entry -> optional remap -> resolved sprite root -> action/direction -> SPR`

Exact own-8.8 precedence remains unresolved.

Keep separate fields:
- server_poly_id
- morph_list_entry_id
- resolved_gfx_root_id
- sprite_source

Potential composite/overlay model from older evidence:
`body + clothes/weapon/effect + per-frame timing/offset`

Preserve overlay/effect references; do not assume overlay ID equals SPR root.

Own 8.8 unresolved:
- list.spr vs list.spz runtime priority
- PolymorphList.xml role
- Lin.bin/LinLogin.bin loader behavior
- launcher custom-resource precedence/fallback
- exact server poly -> client resolved root chain

## 9. 順跑 — LOCKED DEFINITION

Do not interpret 順跑 as version progression.

Project definition:
- morph movement animation visually changes from classic walking-like frames to a more continuous/running style.
- it may be different frames under the same movement Action, but this is not yet proven.

Policy:
- first version with 順跑 = UNKNOWN
- 7.6 = research lower bound only, NOT confirmed starting version

Candidate movement_style values:
- WALK_STYLE
- RUN_STYLE
- UNKNOWN

Open hypotheses:
A. same Action, different frames
B. different Action ID
C. morph/list action remap
D. client runtime alternate animation selection

Need own 8.8 vs M/run-style comparison.

## 10. M-VERSION / CUSTOM MORPH — PRIMARY TECHNICAL LINE

Problem space:
- internet custom morphs
- M-version morphs
- Lineage M-derived assets
- old migrated morphs
- unknown-source SPR
- wrong name/ID mapping
- same root different visual across sources
- morph exists but SPR missing / SPR exists but morph identity unknown
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
Prefer content/entry hash.

Crosswalk statuses:
- CONFIRMED
- LIKELY
- CONFLICT
- MISSING
- UNKNOWN

Visual match:
- EXACT
- NEAR
- DIFFERENT
- UNKNOWN

Collision types:
- NONE
- SAME_ID_DIFFERENT_VISUAL
- DIFFERENT_ID_SAME_VISUAL
- LIST_REMAP
- SOURCE_COLLISION
- OVERLAY_DIFFERENCE

## 11. LINEAGE M SP2 -> LINEAGE 1 SPR

Public evidence supports:
`Lineage M client -> identify/extract SP2 -> preview/decode -> convert SPR -> normalize actions/directions/frames -> own 8.8 validation`

Correct public term currently supported: **SP2 -> SPR**.

PakViewer/public parser clues:
- Brotli compression
- RGB565 palette
- multi-direction structure
- 24x24 block model
- direction -> frames
- offsets/bounds/block references/block data
- delta-encoded block IDs observed

Critical rule:
`SP2 preview OK != SPR writer correct != 8.8 compatible`

Reader/Preview and Writer must be rated independently.

Durable external writer evidence — preserve as trace, not as 8.8 proof:
- PakViewer Issue #7 (`https://github.com/tony1223/PakViewer/issues/7`, opened 2026-03-08): reporter states SP2 preview is correct in PakViewer but the exported SPR is corrupted in Lineage 3.8; the same SP2 converted by LineageViewer2023 works in that older client.
- Follow-up on 2026-03-10 reports the same symptom on Lineage 3.63 and notes PakViewer-rendered frame dimensions appeared larger than older tools. This is external old-client evidence only, but it proves preview correctness is insufficient to validate writer compatibility.
- PakViewer added SPX/SP2 read/preview/export/SPR conversion in commit `844ffcb2c07d4bdee2c8a0246fb298d0baa9ae29` (2026-02-12). At public PakViewer main snapshot `fbad69239e3e3c12ec18dfc7722bed284be093cc` (2026-07-16), `SprConvertHelper.cs` still had blob `4aba0a2a5ec5b80a3662cd8e3640afe39886a74b`, matching the writer introduced with that conversion feature; no public writer change was visible in that interval.
- Current public writer method is a rebuild path: `SP2 decode -> RGBA -> re-slice 24x24 blocks -> recompute SPR A/B/block layout -> write new SPR`; it also writes SPR `Unknown1/Unknown2` as zero and quantizes through `RGB565 -> RGBA -> RGB555`. These are differential-inspection targets, not proven root causes of corruption.
- Operational rating until own-8.8 proof: PakViewer SP2 Reader/Preview = useful reference; PakViewer SP2->SPR Writer = `CONFLICT / reference-only`; LineageViewer2023 output = comparison candidate, not 8.8-confirmed.

Differential set:
A = original SP2
B = LineageViewer2023 output SPR
C = PakViewer output SPR
D = known-good native 8.8 SPR

Highest-value comparison first: B vs C.
Final gold standard: D + own 8.8 runtime.

Status progression:
- SP2_ONLY
- PREVIEW_OK
- CONVERTED
- STRUCTURE_MATCH
- ACTION_MATCH
- RUNTIME_PASS
- RUNTIME_FAIL
- UNKNOWN

Only `RUNTIME_PASS` supports 8.8 confirmation.

## 12. ITEM INVENTORY ICON

Server/DB evidence:
- three item tables use `invgfx`
- item-related packets use `item.get_gfxid()`

Known boundary:
`DB invgfx -> server item gfx -> packet -> client icon resolver`

Own 8.8 unresolved:
- exact item-common.bin role
- final icon resource container
- exact invgfx -> asset mapping
- whether final assets resolve through TBT/IMG/PNG/Sprite or another DAT/container

Do not assume invgfx == monster SPR root.

Historical runtime clue, session-local and requiring revalidation:
- `InventoryItemGrid+0x190` vector of InventoryItemIcon-like pointers
- sampled icon `+0x22C` -> item-model-like object
- low 16 bits at item-model-like `+0x24` matched sampled invgfx

PakViewer remains relevant due to TBT/IMG/PNG/DAT support, but item icon needs its own resolver.

## 13. INVENTORY REORDER — EXISTING OWN-8.8 RESEARCH

Not a zero-start problem.

Main inventory identity:
- `RenewalInventoryUI / InvWindow` = main inventory
- `InventoryItemGrid` = main grid
- RenewalInventory manager = source owner
- `PromoteDollUI != main inventory`

Known order representations:
- manager `+0x54` = source order
- UI `+0x16C` = persisted item-ID order
- grid `+0x1BC` = layout-index order

Refresh chain:
`manager+0x54 -> 0x00DF8450 -> 0x00DF9E60 -> 0x00DFA580 -> 0x00FC2520`

Manual reorder evidence:
`grid reorder event -> InventoryItemGrid vslot+0x1A0 = 0x00DFB6E0 -> base grid reorder 0x00FC2BB0 -> 0x00DFC6B0`

Known exclusions:
- `0x00FBC6F0` creates/appends one InventoryItemIcon-like object; not proven bulk reorder
- `InventoryItemIcon+0x94/+0x98` is idle layout POINT, not reorder proof

Design direction:
- stable sort preferred
- candidate key: `category -> equip/consumable/etc -> name/id -> enchant/bless -> original order tie-breaker`
- ItemSortPred/merge-sort family = candidate only, not proven main-inventory runtime path

Current blocker:
- prove safest writable/order representation
- persistence/refresh semantics
- exact runtime entry/ABI

Apply FIRST RULE:
1. read existing evidence
2. rank manager/UI/grid representations
3. select strongest candidate
4. smallest runtime validation
5. FAIL -> stop, do not broaden binary archaeology

Relevant docs:
- `docs/AI_HANDOFF.md`
- `docs/CLIENT_UI_FUNCTION_MAP.md`
- `docs/INVENTORY_REORDER_CAPTURE_PACK.md`
- `docs/INVENTORY_REORDER_CAPTURE_RESULT.md`
- `docs/CHAT_HANDOFF_20260907.md`

## 14. RANKING FIRST-LOGIN — SECONDARY / HISTORICAL

Historical runtime evidence:
- ranking state byte `state+0x351` could be 1 while `Ranking_Button+0xA5` stayed 0
- toggling `Ranking_Button+0xA5` hid/showed ranking UI
- right-bottom triangle appeared to reapply local UI state; no ranking packet was observed in that capture
- runtime-only PoC synchronized state writer with UI flag and showed ranking without triangle

All addresses are build/session dependent and require revalidation. No disk patch without explicit approval.

## 15. EXTERNAL REPOSITORIES / EVIDENCE

Highest-value external 8.8 reference:
`Yilanchanlong/lineage-8.8`

Recorded external findings:
- 8.8C Sprite IDX `_EXT` header
- 128-byte records
- +0x00 offset
- +0x04 size
- +0x08 compressed_size
- +0x0C flags
- +0x10 filename[112]
- ~228,982 indexed `.spr` entries reported
- strict SPR structural pass ~174,145 / 228,982
- Text.idx old-style count + 28-byte records

Label these as:
`[EXTERNAL 8.8/8.8C][HIGH VALUE][OWN 8.8 VALIDATION REQUIRED]`

Other important references:
- `tony1223/PakViewer`
- `L1j-Kiyoshi/kys8.1`
- `baboqoo/L1J-Wanted`
- `L1Rj/L1j-TW`
- `uglyoldbob/l1j-client`
- `WantedGaming/L1JR-RemasterConnector`
- `hillpath/L1J-KR_3.80`
- `JackerSyu/L1J-Tw-Husky`
- `l1j-en/classic`
- `l1j-en/launcher`

Discovery/download/audit/high-value states must remain separate.

## 16. LOCAL SOURCE INVENTORY — NEXT CORE FOUNDATION

Read-only inventory targets:
- `*.idx`
- `*.pak`
- `*.spr`
- `*.sp2`
- `*.spx`
- `list.spr`
- `list.spz`
- `PolymorphList.xml`
- morph-related XML/BIN
- launcher config/Login.ini/custom PAK/DAT

Store at minimum:
- source label
- source version
- path
- size
- mtime
- sha256

Derived outputs:
- `source_inventory.jsonl`
- `sprite_entry_index.jsonl`
- `morph_resource_inventory.jsonl`
- `source_collisions.jsonl`
- `m_only_candidates.jsonl`
- `unknown_gfx_candidates.jsonl`

SP2 inventory should retain:
- path/size/hash
- compression marker
- decoded marker
- palette
- directions
- frames
- bounds
- block count
- parse status

Do not mass-convert SP2 at inventory stage.

## 17. EVIDENCE LABELS

Use explicit provenance labels:
- `[8.8 NATIVE]`
- `[EXTERNAL]`
- `[VERSION UNKNOWN]`
- `[8.8 UNVERIFIED]`
- `[STATIC]`
- `[RUNTIME]`
- `[SOURCE]`
- `[TOOL]`
- `[OLD CLIENT]`
- `[M VERSION]`
- `[LINEAGE_M_CONVERTED]`
- `[CUSTOM]`
- `[UNKNOWN SOURCE]`

User-facing status wording should prefer:
- 已確認
- 重建
- 未確認

## 18. V3 PRIORITIES

JJ Coverage is no longer P0; it is closed.

P0: read-only local source inventory + current source/provenance baseline
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

Secondary line:
- ranking first-login behavior only when explicitly reprioritized.

## 19. V3 IMMEDIATE NEXT GOAL

Primary project movement after Coverage closure:

`source inventory -> provenance/identity -> collision map -> morph crosswalk -> SP2 sample PoC -> own 8.8 validation`

The next implementation/research task should begin with a bounded, read-only local source inventory. Do not start with full UI and do not mass-decode/export.

FIRST RULE gate for next task:
- hypothesis: existing local client/resource folders contain enough stable file identity metadata to build a non-destructive source inventory without decoding all payloads.
- minimal validation: enumerate a small representative subset, capture path/size/mtime/hash/source label, verify zero source modification.
- PASS -> scale inventory.
- FAIL -> stop and correct inventory assumptions before building downstream mapping.

## 20. COLD-START PASS CHECK

A correct new window must reject these false claims:
- `JJ Coverage is still in progress` -> false; formal 001-780 is complete
- `JJ-SCAN-000781..783 are pending formal ingestion` -> false under current scope
- `JJ-SCAN-000784 should be created` -> false
- `7.6 is confirmed as first 順跑 version` -> false
- `PakViewer preview proves converted SPR is safe` -> false
- `invgfx directly equals SPR root` -> unconfirmed
- `Morph list/remap is already own-8.8 confirmed` -> false
- `Inventory reorder has no prior research` -> false

## 21. DURABLE DOCUMENTS

Primary startup/current state:
- `docs/FAST_BOOTSTRAP.md`
- `docs/FULL_REFERENCE_V3.md`

Historical broad reference:
- `docs/FULL_REFERENCE_V2.md`
- older chat/handoff archives

Goal-specific durable docs:
- `docs/AI_HANDOFF.md`
- `docs/CODEX_STATUS.md`
- `docs/REVIEWER_FEEDBACK.md`
- `docs/CLIENT_UI_FUNCTION_MAP.md`
- `docs/INVENTORY_REORDER_CAPTURE_PACK.md`
- `docs/INVENTORY_REORDER_CAPTURE_RESULT.md`
- `docs/CHAT_HANDOFF_20260907.md`
- `docs/JJ_LINEAGE_RESEARCH_MAP.md`
- `docs/JJ_LINEAGE_RESEARCH_MAP_ADDENDUM_20260907_1623.md`
- `docs/JJ_SITE_RESEARCH_TRACKER.md`

## 22. V3 STATE SUMMARY

- JJ full-site formal Coverage: COMPLETE through 780.
- V2 served as the broad pre-completion handoff and remains historical reference.
- V3 becomes the current broad handoff.
- Main technical center of gravity now moves from Coverage acquisition to local source identity/provenance, M/M-style/SP2, Morph crosswalk, and own-8.8 validation.
- Resource Browser remains downstream of data-model stabilization.
- Inventory reorder remains an independent active-capable line with substantial prior evidence.

## 23. CX MODEL / EFFORT ROUTING POLICY — V3

Purpose: use CX modules and reasoning effort proportionally to task complexity while keeping results reproducible and avoiding unnecessary cost/latency.

Approved V3 baseline modules:
- `GPT-5.6 Sol` = primary/default engineering and difficult reasoning module.
- `GPT-5.6 Luna` = fast mechanical/read-heavy work when the task is explicit and easy to validate.
- `GPT-5.6 Terra` = experimental/optional; do not assign a permanent specialty until a bounded A/B test demonstrates an advantage for a specific task class.
- `GPT-5.5` = compatibility/backup/second-opinion module; not the default for new V3 technical work.
- `GPT-6 family` = DEFERRED / DISABLED for the current V3 baseline. Do not enable automatically.

Available effort levels:
- `LIGHT / 輕度`
- `MEDIUM / 中`
- `HIGH / 高`
- `EXTREME / 極高`
- `ULTRA`

Routing defaults:

| Task class | Preferred module | Effort | Examples |
|---|---|---|---|
| deterministic status/read/search | Luna | LIGHT-MEDIUM | git status, HEAD/SHA checks, file enumeration, manifest inspection, simple identity checks |
| routine implementation/validation | Sol | MEDIUM | small scripts, validators, bounded transforms, ordinary fixes |
| multi-file engineering/reasoning | Sol | HIGH | repair plans, schema changes, cross-file tracing, data-model work |
| reverse engineering / ambiguous evidence | Sol | EXTREME | Morph resolution, SP2 structure, client runtime path, conflicting source evidence |
| exceptional root-cause problem | Sol | ULTRA | multiple plausible hypotheses remain after EXTREME, high-cost error risk, difficult A/B/C/D differential |
| experimental A/B only | Terra | MEDIUM-HIGH | compare one bounded task against the current Sol/Luna baseline |
| compatibility / independent second opinion | GPT-5.5 | LIGHT-MEDIUM | legacy workflow check or independent sanity review |

Escalation rules:
1. Start at the lowest reasonable module/effort that can safely solve and validate the task.
2. Deterministic operations should rely on tools/validators, not higher reasoning effort.
3. If failure is caused by missing evidence, do NOT solve it by blindly increasing effort; stop and obtain the missing evidence.
4. If the direction is valid but reasoning complexity is the blocker, escalate one level at a time.
5. Do not repeat the same failing approach at the same model/effort indefinitely. One bounded retry is enough before escalation or fail-stop.
6. `ULTRA` is not a default quality mode. Use it only when the problem genuinely requires it.
7. Model choice never overrides read-only/write permissions, evidence hierarchy, validator gates, or the FIRST RULE.
8. A new model/module is not promoted into the baseline because it is newer. It must first win a bounded, reproducible A/B validation on the relevant task type.

Task-specific examples:
- V3 P0 source inventory: Luna/LIGHT for enumeration -> Sol/MEDIUM for schema/hash/index script -> Sol/HIGH only for identity/collision logic -> EXTREME only when provenance conflicts cannot be resolved mechanically.
- Morph/SP2 mapping: Sol/HIGH to start; EXTREME for conflicting mappings/format reasoning; ULTRA only after smaller hypotheses fail cleanly.
- SP2 writer A/B/C/D differential: Sol/HIGH for structural diff -> EXTREME for writer/action/frame root cause -> ULTRA only if evidence remains genuinely ambiguous.
- Inventory reorder: Sol/HIGH for existing-evidence synthesis and minimal runtime plan; EXTREME only for unresolved ABI/runtime ownership questions.

CX instruction contract:
- When GPT hands a substantial task to CX, include `MODEL_RECOMMENDATION` and `EFFORT_RECOMMENDATION` when useful.
- CX should report the actual module/effort used if it differs from the recommendation.
- If the recommended module is unavailable, use the nearest approved fallback rather than enabling GPT-6 automatically.
- For critical conclusions, reproducibility and validator/readback evidence matter more than model tier.

## 24. NEW-WINDOW HANDOFF CONTRACT

A new GPT/CX window should be able to recover the important project state without replaying old conversations.

Minimum cold-start sequence:
1. read `docs/FAST_BOOTSTRAP.md`
2. re-read live `main` HEAD
3. read `docs/FULL_REFERENCE_V3.md`
4. load only the durable docs required by the current Goal

`FULL_REFERENCE_V3.md` is the broad distilled handoff: it carries the important prior conclusions, constraints, evidence discipline, closed JJ state, current technical lines, priorities, and CX model-routing policy. It is intentionally not a verbatim transcript and does not contain every raw experiment/address/log.

Therefore:
- For broad project continuity, V3 is sufficient.
- For exact historical evidence, runtime addresses, detailed captures, or implementation specifics, follow the goal-specific durable-doc references instead of guessing.
- Always prefer live GitHub state over snapshot SHA values embedded in V3.
- Do not reread V1/V2 by default; use them only when tracing provenance/history.