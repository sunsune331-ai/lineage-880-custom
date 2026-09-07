# J.J.'s Blogs 50-article article-level reread progress — 2026-09-07 18:33

## Scope / invariant

This run **only reread the previous batch of 50 articles**. It did not start article 89 or any new unread article.

Formal ledger invariant is unchanged:
- **Completed: 88 / 783**
- **Remaining: 695**
- Reread completion increment: **0**

No duplicate canonical URL, empty-page false positive, or prior completed-count error was found among these 50 during the reread. Therefore no baseline correction is proposed or applied.

All website content remains `research evidence`, not 8.8 verified project fact.

## URL normalization rule used

- scheme normalized to `https`
- fragments removed
- tracking query parameters removed when present
- canonical article path retained with trailing `/`
- redirect/same-page forms would resolve to one canonical record
- similar-looking paths are not merged unless they resolve to the same article/body

The 50 previous-batch URLs were distinct under this rule.

## Article-level ledger files

1. `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833.md` — R01–R10
2. `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833_011_020.md` — R11–R20
3. `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833_021_030.md` — R21–R30
4. `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833_031_040.md` — R31–R40
5. `docs/JJ_ARTICLE_LEDGER_REREAD_20260907_1833_041_050.md` — R41–R50

Cross-article synthesis is deliberately separate:
- `docs/JJ_LINEAGE_RESEARCH_MAP_REREAD_ADDENDUM_20260907_1833.md`

Previous batch history is preserved unchanged:
- `docs/JJ_RESEARCH_PROGRESS_20260907_1815.md`

This reread supplements/supersedes that old batch summary **for article-level detail only**; the old report remains historical evidence and is not deleted, moved or renamed.

## Checkpoint ledger

| Checkpoint | Reread | Full body reacquired | Inaccessible | Article records total | Articles with material detail absent from previous summary | Material new-detail / qualification groups | Previous-summary difference | Commit |
|---|---:|---:|---:|---:|---:|---:|---|---|
| 10 | 10 | 10 | 0 | 10 | 8 | 8 | yes: omitted details, no disproved claim | `c91a03002255dd23ce8873ea8ec15fe81d5daa51` |
| 20 | 10 | 10 | 0 | 20 | 10 | 6 | yes: SPR/SFD, map and dialog details | `e45a7c09c9ea79e9f07b9b5c6d56e2efd6b41fa2` |
| 30 | 10 | 10 | 0 | 30 | 10 | 8 | yes: action/ID and starter-item uncertainty | `9d3fecb2446c58af0738a67fca3185d3043a02c7` |
| 40 | 10 | 10 | 0 | 40 | 10 | 7 | yes: item/teleport/spawn/config detail | `4643017a10bb8fe34f977945f73a277f53e2a464` |
| 50 | 10 | 10 | 0 | 50 | 10 | 9 | yes: launcher/network file/config graph | `0573250cfc54688355c1a13e26f6c5c1755cb53e` |

`Material new-detail / qualification groups` counts discrete groups named in each checkpoint status; it is not a completed-article count.

## Final reread counts

- Successfully detailed-reread: **50 / 50**
- Unable to reacquire full main body: **0**
- Article-level records complete: **50 / 50**
- Articles containing material detail not present in the previous batch summary: **48 / 50**
- Previous-summary claims that could not be re-confirmed at all: **0**
- Previous-summary statements requiring qualification after reread: **multiple**, especially old-version/device-specific network/launcher statements and source-internal uncertainties.

R46 (`對外設定(撥接上網)`) produced a transient direct-reader Internal Error, but its actual indexed article body was reacquired through the site search result in this same run. It is therefore recorded as body successfully reacquired, not inferred from the old summary.

## Classification counts (same 50 articles)

- Client: **17**
- Server: **9**
- Protocol: **9**
- Shared: **11**
- Other: **4**
- Total: **50**

## Cross-article corroboration found

1. PakViewer/Pakext/PackViewer/MTools/Linskin/SPR/map tools independently reinforce index/container inventory -> filter/search -> selected preview/extract/convert.
2. TBT/icon and SPR/sprite repeatedly appear as separate resource workflows.
3. SPR-BMP article + SFDviewer history reinforce that sprite animation rendering depends on metadata/offsets beyond raw frame pixels.
4. NPC-dialogue/text/image/link/action/item-use articles form a DB/server -> dialog key -> client resource -> optional server action chain.
5. starter-item/Adena/create-item/itemset articles reinforce template -> instance -> inventory -> merge/insert -> persistence -> client update.
6. NPC mapping + insert-spawn articles reinforce template/impl -> persistent spawn -> runtime instance.
7. map tools + map attributes + teleporter/action + GM move reinforce visual map / gameplay rules / authoritative position / transition as separate layers.
8. main-program setup + launcher intro + launcher functions reinforce launcher as endpoint/config/process/resource/protocol/update component in that old ecosystem.
9. IP/internal-network/firewall/NAT/topology articles reinforce keeping network reachability separate from application protocol.

These are `CORROBORATED_OLD`, not confirmed 8.8 facts.

## Conflicts / source uncertainty found

- R05: PackViewer_beta2 `Save resource` UI exists but author says it appeared ineffective.
- R07: repeated XML decrypt/encrypt may scramble content; legacy XML statement cannot be generalized.
- R14: map-tool compare/repair functions are explicitly unknown.
- R20: `#312/#314` shop image mapping remains unresolved despite normal IMG ID expectations.
- R22: same article contains `70074` vs `70101` inconsistency for 梅林/mellin example.
- R27: `bless=0` expected blessed result was not observed; `charge_count` was also unresolved.
- R32: conclusion contains apparent `desc` typo while body analyzes `adena`.
- R34: examples spell `.imteset` while command is described as `itemset`.
- R40: map generation was initially unknown and later supplemented by 2022 MapTool note.
- R49: launcher XML priority is phrased by source as an assumption, not a direct loader trace.

No one of these changes the 88/783 completion ledger.

## New 8.8 verification backlog

### Client
- Actual resource resolver/container/precedence/cache for IDX/PAK/SPR/TBT/IMG/XML/TBL/HTML/map/tile/BIN equivalents.
- Item icon resolver vs sprite/morph resolver.
- Dialog markup/parser, dynamic values, locale/special image mapping.
- LinLogin -> Lin.bin process/resource/config handoff and launcher loose-resource priority.

### Server
- Item template/instance/inventory acceptance/stack merge/new insertion/attributes/persistence/client refresh.
- `bless`/identify/enchant/attribute field provenance through final runtime item.
- NPC template/implementation/factory/spawn persistence/runtime entity/world registration.
- Teleport authoritative state, trade cancellation, effect/ack and post-transition logic.
- Config/DB/XML source + load/cache/reload provenance.

### Runtime / Protocol
- Dialogue `link` vs `action` outbound packet behavior.
- Item-use -> server handler -> dialogue response.
- Inventory stack merge vs new-entry update and reorder producer.
- Teleport/map transition and client interpolation/resource load.
- Login endpoint/version/bootstrap, status probe, crypto adaptation and launcher participation.
- True listen transport/port before any firewall/NAT assumption.

### Resource / persistence
- SPR/SFD-equivalent frame metadata and rendering round-trip.
- Client map data vs server walkability/region/portal authority.
- Item/NPC/dialog/spawn persistence and cache behavior.

## Previous batch conclusion vs article-level reread

### New information
Exact resource-tool behaviors and failure notes; SPR/SFD round-trip details; map/tile/region/portal details; dialog markup limitations; item Type/Type2/enchant logic; inventory limits and persistence; XML item-set loader; teleport internals; NPC impl taxonomy; launcher file/config/dependency graph; legacy build/import troubleshooting.

### Strengthened information
Index-first/on-demand Browser architecture; TBT vs SPR separation; DB->code->runtime method; inventory and spawn provenance chains; launcher multi-role hypothesis; network reachability layering.

### Questioned information
Legacy XML/launcher precedence, 16-bit graphics requirements outside specific tools, bless semantics, some tool-button capabilities, special/locale IMG lookup, generic TCP/UDP/port statements as game evidence.

### Conflict information
R22 ID mismatch; R27 bless expected-vs-observed result; source editorial typos and unresolved R20 image mapping.

### Requires revalidation
Every old address/port, class/path/field/table name, packet behavior/code, item constant, resource format/priority, launcher feature and transport assumption before promotion to 8.8 project knowledge.

## GitHub history preservation

No old checkpoint/progress/tracker file was deleted, moved, renamed or blanked. No Java/Server/Client/Database functional source was modified. No article after the existing 88-completed baseline was started.

STATUS = `REREAD_50_OF_50_COMPLETE_STOP_BASELINE_UNCHANGED`
