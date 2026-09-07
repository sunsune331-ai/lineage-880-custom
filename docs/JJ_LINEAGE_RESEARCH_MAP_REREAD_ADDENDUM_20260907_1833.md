# J.J. 50-article reread — research synthesis / map addendum

> This synthesis is derived from the article-level reread records R01–R50. It does **not** replace the article ledger and does **not** change completion. Formal baseline: **88 / 783; remaining 695**.
>
> Evidence level for every web-derived statement below: `research evidence`. Cross-article agreement may raise verification priority, not truth status for 8.8.

## 1. Article-ledger source set

- R01–R10: `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833.md`
- R11–R20: `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833_011_020.md`
- R21–R30: `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833_021_030.md`
- R31–R40: `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833_031_040.md`
- R41–R50: `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833_041_050.md`

## 2. Strong cross-article corroboration (old-version corpus only)

### A. Resource container/browser model
Corroborated by R02 PakViewer intro, R03 Pakext, R04 PakViewe, R05 PackViewer_beta2, R06 MTools, R10 Linskin, R11 SPR-BMP, R13/R14 map tools, plus earlier PakViewer/L1Viewer research.

Common pattern:
`open client/folder/IDX/PAK -> enumerate entries -> filter/search -> select -> preview/decode/export/convert`

Implication for 8.8 validation: keep `source IDX/PAK + entry + decoder + selected preview` as first-class provenance. This is architecture guidance, not proof that any legacy tool parses 8.8.

### B. Icon/TBT and Sprite/SPR are distinct tasks
R01 uses `icon/482.tbt`; R06 is a TBT-specific browser; R11 handles SPR via SPR/SFD/BMP; launcher patch tooling R49 lists HTML/TBL, SPR, XML, TIL separately.

Priority hypothesis:
`item/invgfx -> icon resolver -> icon-format entry`
versus
`gfx/morph -> mapping/action -> SPR/resource entry`

Do not merge them into one same-ID lookup without 8.8 evidence.

### C. SPR round-trip requires metadata beyond pixels in old tools
R11 explicitly outputs SFD plus BMP frames and consumes SFD to rebuild SPR; prior SFDviewer research shows per-frame X/Y/composition. R12 adds a legacy 16-bit BMP conversion constraint.

8.8 verification priority: identify pixel encoding, frame table, offsets/origin, duration/action linkage and any SFD-equivalent metadata. A decoded PNG alone is not game-faithful animation proof.

### D. Dialogue bridge crosses DB / server action / client resource
R08, R18, R19, R20, R21–R25 form a coherent old-version chain:
`NPC/spawn/template -> npcaction/dialog key -> client HTML-like resource -> link/action/var/img -> optional server action -> response/state`

Sub-branches:
- `link`: demonstrated as dialogue-resource navigation.
- `action`: used by server-interactive behavior (buff, teleport, item use, shop controls).
- `var`: server-provided dynamic values in dialogue.
- `img src="#ID"`: client image resource lookup with observed locale behavior in R20.

8.8 must packet-capture `link` and `action` separately rather than assuming one is local and one networked from naming alone.

### E. Item creation/inventory chain
R26/R27 starter items, R32 Adena, R33 create item, R34 item set mutually support a layered old-server model:
`template/config -> instance construction -> inventory acceptance -> stack merge or new instance -> attributes/enchant -> persistence -> client-visible update`

R33 explicitly mentions `character_items`, stack/non-stack branches, limits and special item handling. R27 demonstrates why a DB field cannot be assumed authoritative just because it exists (`bless` test did not produce expected result).

8.8 priority: trace final runtime instance and persistence, not just DB schema or Java getter.

### F. NPC spawn persistence and runtime creation
R18 locates NPC via spawn data; R36 shows `.insert mob/npc` selecting template/impl, persisting to `spawnlist` or `spawnlist_npc`, then calling runtime spawn; restart preserves entities.

Candidate 8.8 chain:
`NPC template -> implementation/factory -> persistent spawn source -> runtime instance -> visibility packet -> gfx/resource resolver`

Every component name/table is version-specific until verified.

### G. Teleport/map authority is multi-layered
R13/R14 expose client map/tile resources; R15 deals region/walkability/portal-style properties; R24 server action carries X/Y/Map/Heading/Price; R35 calls a server teleport routine, changes map/position/heading, handles trade/effect/wait-for-client/post-teleport logic.

8.8 should separate:
`visual map resource` / `server map rules` / `authoritative position` / `transition protocol` / `client renderer/interpolation`.

### H. Launcher is a multi-role component in the old ecosystem
R41, R49, R50 strongly corroborate launcher roles beyond process launch: endpoint/server-list config, client executable selection, optional packet encryption, movement-packet exception, morph PAK, patch ingestion, auto update, web list update, status light, announcement/market web UI, multi-client toggle, helper config.

Legacy file graph includes:
`Login.exe`, `Encode.exe`, `Login.ini`, `Login.cfg`, `Update.ini`, `pack.properties`, `TW13081901.bin`, `TW13081901.txt/.pak`, `eat.exe/.dll`, `skin`, launcher XML/resource folder.

8.8 must establish responsibility by runtime provenance across `LinLogin` and `Lin.bin`; no individual legacy function is assumed present.

### I. Network reachability is separate from application protocol
R42–R48 consistently separate address identity, LAN/public topology, firewall, NAT/port-forward, and advertised endpoint. Old examples use port 2000 and assorted consumer devices.

8.8 diagnostic order should be evidence-driven:
`listen socket -> local reachability -> LAN reachability -> NAT/public mapping -> firewall -> application handshake/packet parser`.

## 3. Explicit uncertainties / conflicts preserved

1. **R05 PackViewer_beta2:** UI exposes Save resource, but author reports it appeared ineffective. Capability remains uncertain.
2. **R07 XML crypto:** repeated decrypt/encrypt is reported capable of scrambling content; blanket legacy statement about client XML encryption must not be generalized.
3. **R14 Lineage Map V1:** `比對` and `修復 Tile .idx .pak` functions are shown but purpose was not understood by author.
4. **R17 Flexible Renamer:** article contains an editorial phrase referring to bit-depth conversion while actual procedure is batch rename; treated as editorial inconsistency.
5. **R20 image dialogue:** locale suffix fallback is observed/described, but shop image IDs `#312/#314` did not trivially map to previewed corresponding IMG resources; author left it unresolved.
6. **R22 Action篇:** article text identifies 梅林 as `npcid=70074`, while a shown SQL/comment line uses `70101 -- mellin`; unresolved internal article inconsistency.
7. **R27 starter items:** expected `bless=0` outcome failed in runtime test; `charge_count` meaning was also unresolved. This directly cautions against field-name inference.
8. **R32 Adena:** conclusion appears to mistakenly say L1Adena handles `desc`; body consistently analyzes `adena`.
9. **R34 itemset:** examples visibly spell `.imteset` while article/command are `itemset`; retained as source typo, not normalized evidence.
10. **R40 maps:** article initially said generation of server map txt was unknown, then later 2022 note supplements it with client MapTool export. This is a documented evolution/correction in the same article.
11. **R49 launcher XML priority:** source itself says it can be *assumed* that launcher XML folder gains priority; this is explicitly weaker than direct loader trace and must stay hypothesis-only.
12. **R44 networking:** generic TCP/UDP tutorial statements and old port 2000 firewall example do not establish actual 8.8 transport.

No evidence in this reread proves the formal 88-entry completion ledger contains a duplicate URL or empty-page false positive. Baseline is therefore not changed.

## 4. New 8.8 verification backlog created by reread

### Client
- Trace actual 8.8 resource open/read path for IDX/PAK/SPR/TBT/IMG/XML/TBL/HTML/map/tile/BIN equivalents.
- Preserve duplicate source provenance and measure precedence/fallback/cache.
- Test whether any loose/launcher XML/resource directory overrides packed content; R49 is hypothesis-only.
- Identify actual item-icon resolver separately from sprite/morph resolver.
- Determine dialog parser dialect, supported tags/attributes, dynamic variables and locale image fallback, including special #312/#314-type mapping behavior.
- Establish LinLogin -> Lin.bin process/resource/config handoff and actual executable selection.

### Server
- Inventory: template lookup -> `instance` -> accept limits -> stack merge/new insert -> item attributes -> DB persistence -> outbound refresh.
- Re-test bless/identify/enchant/attribute provenance end-to-end; do not trust schema labels.
- NPC: template/implementation -> persistent spawn source -> runtime entity -> world registration -> client visibility/gfx.
- Teleport: authoritative X/Y/map/heading, active-trade handling, effects, acknowledgement/wait mode and post-teleport state.
- Config: source/default/cache/reload provenance for DB, XML and properties equivalents.

### Runtime / Protocol
- Capture dialogue `link` vs `action` clicks and compare outbound traffic/state transitions.
- Capture item-use -> handler -> dialogue response chain.
- Capture inventory new-item vs stack-merge updates and any reorder/sort producer.
- Capture teleport transition and map load/interpolation boundary.
- Determine actual login/list/version/endpoint bootstrap and whether launcher participates in crypto or packet adaptation.
- Identify true listen transport/port before firewall/NAT configuration; do not inherit 2000 or TCP/UDP assumptions.

### Resource / DB persistence
- Verify whether SPR decode needs SFD-equivalent offsets/meta and whether round-trip preserves rendering.
- Verify map visual data vs server walkability/region/portal data and generation pipeline.
- Verify `item`, `NPC`, `dialog`, `spawn` persistence sources and caches directly in 8.8.

## 5. Previous batch synthesis vs article-level reread

### New information
- Exact old item Type/Type2 and accessory/weapon enchant processing examples.
- Inventory acceptance checks, special-item branches and explicit `character_items` persistence in create-item path.
- XML item-set loader path and concrete ItemSet format.
- Teleport branch details: trade cancellation, effect toggle, wait-for-client/config and post-transition routine.
- Large `npc.impl` taxonomy and explicit persistence INSERT shapes.
- Launcher file/config graph and feature dependencies.
- Dialog markup limitations and unresolved image-resource edge case.
- Historical build/import troubleshooting and later corrections embedded in articles.

### Strengthened information
- Index-first/on-demand resource browser model.
- TBT/icon vs SPR/sprite separation.
- DB -> Java/config -> runtime -> client-visible result methodology.
- Spawn persistence -> runtime entity -> client visibility research chain.
- Network reachability vs packet/application fault separation.

### Questioned information
- XML/launcher resource priority.
- Legacy 16-bit graphic constraints outside those specific tools.
- `bless` field semantics and some unresolved item fields.
- Tool buttons that exist in UI but were not shown working.
- Locale/special image ID resolution.

### Conflicting/inconsistent information
- R22 `70074` vs `70101` NPC ID usage.
- R27 bless expectation vs observed item result.
- Several source editorial typos (`imteset`, Adena conclusion, Renamer wording); preserved rather than silently corrected.

### Requires revalidation
- All old ports, result codes, class paths, table/field names, item/type constants, loader priorities, packet encryption behavior, launcher responsibilities and resource formats.

## 6. Evidence labels

- `REREAD_WEB_SOURCE`: article body successfully reacquired in this reread.
- `CORROBORATED_OLD`: two or more old-version articles independently support a pattern.
- `SOURCE_UNCERTAINTY`: author explicitly uncertain or source internally inconsistent.
- `HYP_880`: useful 8.8 hypothesis, not verified.
- `NEEDS_CLIENT_880`, `NEEDS_SERVER_880`, `NEEDS_RUNTIME_880`, `NEEDS_PACKET_880`, `NEEDS_RESOURCE_880`, `NEEDS_DB_880`.

No `CORROBORATED_OLD` statement becomes `CONFIRMED_880` without direct 8.8 evidence.
