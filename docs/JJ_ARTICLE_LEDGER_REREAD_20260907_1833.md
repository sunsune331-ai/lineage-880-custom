# J.J.'s Blogs article-level reread ledger — 2026-09-07 18:33

> Scope: reread only the previous batch of 50 completed articles. This file does **not** increment completed count. Formal baseline remains **88 / 783; remaining 695**.
>
> Evidence rule: all website claims are `research evidence`. They are not 8.8 facts until verified against the appropriate 8.8 Client / Server source / Runtime / packet capture / resource / DB persistence evidence.
>
> URL normalization used here: force `https`, remove fragment and tracking query, preserve distinct path/article slug, canonical URL ends with `/`.

## Checkpoint 10 — articles 1–10

### R01 — 天堂私服 | eat (吃檔教學)
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220204-private-lineage-71/
- **Original URL:** https://morosedog.gitlab.io/private-lineage-20220204-private-lineage-71/
- **Source / published:** J.J.'s Blogs / J.J. Huang / 2022-02-04
- **Classification:** Client
- **Lineage version:** Lineage 3.81C; launcher example Login_v380a
- **Access/status:** PUBLIC_FULL; main body reread successfully
- **Main content:** demonstrates the old-client "eat" patch-ingestion workflow and a concrete TBT icon replacement.
- **Technical details / tools:** `eat.exe` + `eat.dll` supplied with Login_v380a; tool is placed in the client directory. Author says launcher packages commonly include an eat tool or built-in eat functionality.
- **Files/resources:** patch files are placed in logical directories; example `icon/482.tbt` replaces the status icon for 體魄強健術; `.tbt` is treated as an icon resource.
- **Operation flow:** clean 3.81C client -> copy eat.exe/eat.dll into client root -> place patch file in matching resource directory -> run eat.exe -> update-like progress UI -> wait for `吃檔完成` -> restart game -> verify visible change.
- **Parameters/paths/version info:** client root; `icon` directory; resource ID `482`; `482.tbt`; Login_v380a.
- **Limits/exceptions/notes:** backs up original files before replacement; no executable download is provided by the article; article explicitly depends on prior IDX/PAK, extension and folder-mapping articles.
- **Class/method/field / packet/opcode:** none stated.
- **Cross-support:** supports the separate TBT/icon resource model seen in MTools/Lineage Icon and the logical resource-class model from patch-folder articles.
- **Conflicts:** none directly; it does not prove 8.8 still accepts loose-folder/eat overrides.
- **8.8 / current research relation:** useful for Client loader precedence and cache-invalidation experiments; do not modify production client while validating.
- **Verification tags:** `待驗證`: whether 8.8 has an equivalent ingestion/override path and precedence; `版本未確認`: all directory/tool behavior outside 3.81C; `僅 research evidence`.

### R02 — 天堂私服 | PakViewer 介紹
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220205-private-lineage-72/
- **Original URL:** same as canonical
- **Source / published:** J.J.'s Blogs / 2022-02-05
- **Classification:** Client
- **Lineage version:** general old Lineage resource-tool context; specific tools are legacy versions
- **Access/status:** PUBLIC_FULL
- **Main content:** defines `PakViewer` as a pack-file viewer family and enumerates common capabilities plus known tool variants.
- **Technical details / tool capabilities:** parse IDX/PAK and list entries; preview image/text/animation; export/import/delete; search filename/extension/text content; extension filtering; sorting by number/name/size/memory position/locale; select all; XML decrypt; IMG<->BMP conversion.
- **Named tools/versions:** Pakext.exe `Pack Extracter 0.06`; PakViewer.exe `Ver 0.04`; PakViewer Ver.3.0.exe `Ver.3.0`; PackViewer_beta2.exe `beta2`; L1Viewer.exe `v1.2`; LineageSpr.exe (version not stated); Lineage Icon v120119.exe `Ver.120119`; article continues a broader collected tool list.
- **Files/resources:** IDX, PAK, XML, IMG, BMP; preview may include dynamic image/text.
- **Operation flow:** conceptual capability matrix rather than one procedure.
- **Limits/exceptions/notes:** author warns downloaded tools should be treated as untrusted, scanned first and preferably run in a VM; stated virus-scan results are not guarantees.
- **Class/method/field / packet/opcode:** none.
- **Cross-support:** directly corroborated by the dedicated Pakext/PakViewe/PackViewer articles; supports `index/container inventory -> filter/search -> on-demand preview` Browser design.
- **Conflicts:** capability list is a union of common viewers; not every listed tool implements every capability.
- **8.8 relation:** use as UI/capability taxonomy only; compatibility with 8.8 containers is unknown.
- **Verification tags:** `待驗證`: 8.8 format compatibility and which metadata fields actually exist; `版本未確認`; `僅 research evidence`.

### R03 — 天堂私服 | Pakext 工具
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220206-private-lineage-73/
- **Original URL:** same
- **Source / published:** J.J.'s Blogs / 2022-02-06
- **Classification:** Client
- **Lineage version:** old Lineage resource container; exact client compatibility not stated in body
- **Access/status:** PUBLIC_FULL + COFFEE_RESOURCE_BLOCK (download password block unread by reader)
- **Main content:** dedicated introduction to `Pakext.exe` / PAK extraction utility.
- **Technical details / tools:** open via File -> Open old file -> choose client `*.idx`; per-entry `extract` and `delete`; Edit menu adds `append`, extract/delete and select-all; list-header sort supports ascending/descending.
- **Files/resources:** `*.idx` plus associated packed resources; no decoder format internals stated.
- **Operation flow:** open IDX -> inspect list -> select entry/all -> extract/delete/append -> optional sort.
- **Parameters/version:** Pakext; PakViewer intro identifies it as `Pack Extracter 0.06`.
- **Limits/exceptions/notes:** tool-download safety warning; resource password is coffee-locked; article does not establish 8.8 support.
- **Class/method/field / packet/opcode:** none.
- **Cross-support:** corroborates PakViewer-intro capabilities; contrasts with read-only Toolkit policy because this tool exposes mutation actions.
- **Conflicts:** none; mutation capability should not be interpreted as safe or needed for current project.
- **8.8 relation:** useful only for IDX inventory semantics and UX; current Toolkit should preserve source provenance and remain read-only for existing client data.
- **Verification tags:** `待驗證`: exact IDX schema/source mapping in 8.8; `版本未確認`; `僅 research evidence`.

### R04 — 天堂私服 | PakViewe 工具
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220207-private-lineage-74/
- **Original URL:** same
- **Source / published:** J.J.'s Blogs / 2022-02-07
- **Classification:** Client
- **Lineage version:** old Lineage resource environment
- **Access/status:** PUBLIC_FULL; download/password membership details are not counted as technical body
- **Main content:** dedicated `Lineage I Pack File Viewer (PakViewer.exe)` workflow.
- **Tool capabilities:** File->Open client `*.idx`; Export saves to root; Export TO chooses destination; select/unselect all; Edit->Filter; Tools->Export/Export TO/Delete/Add/Update/select; sort asc/desc; preview image/animation/text; zoom slider; fuzzy filename search.
- **Files/resources:** IDX/PAK-associated entries; images, animations, text.
- **Operation flow:** open IDX -> list/filter/search -> select -> preview/zoom -> optionally export/mutate.
- **Parameters/version:** PakViewer intro identifies this tool as `Ver 0.04`.
- **Limits/exceptions:** article is tool-centric, not a formal IDX/PAK specification; write operations remain unsafe for current production resources.
- **Class/method/field / packet/opcode:** none.
- **Cross-support:** strongly supports PakViewer Ver.3.0/L1Viewer index-first preview pattern.
- **Conflicts:** none; differs from Pakext by richer preview/filter UI, not necessarily data model.
- **8.8 relation:** strong UX evidence for click-to-decode preview and search/filter; does not prove 8.8 compatibility.
- **Verification tags:** `待驗證`, `版本未確認`, `僅 research evidence`.

### R05 — 天堂私服 | PackViewer_beta2 工具
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220209-private-lineage-76/
- **Original URL:** same
- **Source / published:** J.J.'s Blogs / 2022-02-09
- **Classification:** Client
- **Lineage version:** old Lineage resource environment
- **Access/status:** PUBLIC_FULL
- **Main content:** describes `PACK Viewer (PackViewer_beta2.exe)` and its resource operations.
- **Tool capabilities:** toolbar provides Open PAK file; Open LINEAGE folder; clear index list; show all/image-only/text-only; export/save/add/delete resource. Per-entry export/add/delete; lower icon/type filters.
- **Files/resources:** PAK, client directory, image/text resource classes.
- **Operation flow:** open PAK or Lineage folder -> build resource list -> type-filter -> select item -> export/save/add/delete.
- **Parameters/version:** `beta2` from PakViewer tool-list article.
- **Limits/exceptions:** author notes `Save resource` appeared not to work in actual use; this uncertainty is preserved verbatim as tool-behavior evidence.
- **Class/method/field / packet/opcode:** none.
- **Cross-support:** corroborates folder-level inventory plus single-container drill-down used by other viewers.
- **Conflicts:** `Save resource` UI exists but actual effect was questioned; treat capability as uncertain, not confirmed.
- **8.8 relation:** supports folder inventory architecture; mutation operations are outside current read-only scope.
- **Verification tags:** `待驗證`: save semantics and 8.8 parsing; `版本未確認`; `僅 research evidence`.

### R06 — 天堂私服 | MTools 工具
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220213-private-lineage-80/
- **Original URL:** same
- **Source / published:** J.J.'s Blogs / 2022-02-13
- **Classification:** Client
- **Lineage version:** old Lineage; exact client version not explicit in body
- **Access/status:** PUBLIC_FULL + COFFEE_RESOURCE_BLOCK
- **Main content:** introduces `TBT圖片包瀏覽器 (MTools.exe)`.
- **Tool capabilities:** File->Open -> choose client directory; select individual image and `抽出選擇的圖檔`; or `全部抽出`; destination can be chosen.
- **Files/resources:** TBT/icon imagery; article calls it a TBT image-package browser.
- **Operation flow:** set/open client directory -> browse image list -> select one/all -> export to chosen path.
- **Limits/exceptions:** no internal TBT structure described; download password locked; virus scan not a safety guarantee.
- **Class/method/field / packet/opcode:** none.
- **Cross-support:** reinforces separate Item Icon/TBT workflow alongside Lineage Icon and TBT compiler articles.
- **Conflicts:** none.
- **8.8 relation:** verify actual icon container/entry mapping before assuming TBT persists in 8.8.
- **Verification tags:** `待驗證`, `版本未確認`, `僅 research evidence`.

### R07 — 天堂私服 | XML加解密 工具
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220214-private-lineage-81/
- **Original URL:** same
- **Source / published:** J.J.'s Blogs / 2022-02-14
- **Classification:** Client
- **Lineage version:** old client context; article example uses `Tile.idx` / `polymorphList.xml`
- **Access/status:** PUBLIC_FULL + COFFEE_RESOURCE_BLOCK
- **Main content:** describes encrypted client XML and a reversible encrypt/decrypt utility.
- **Technical details/tools:** `XML加解密.exe`; author states client `*.xml` are encrypted in this context; unencrypted XML cannot simply be eaten back because startup emits `XML Encryption Check(xxxxx.xml)`.
- **Files/resources:** `Tile.idx`; `polymorphList.xml`; decrypt output suffix `_d.xml`; encrypt output suffix `_e.xml`.
- **Operation flow:** PakViewer extract `polymorphList.xml` from Tile.idx -> editor shows unreadable encrypted content -> decrypt -> edit readable XML -> encrypt -> rename appropriately -> ingest back.
- **Important observed property:** article says encrypting the decrypted example produced encrypted content identical to original.
- **Limits/exceptions:** rename generated file before eat; repeated decrypt/encrypt operations can corrupt/scramble content; article's blanket statement about XML applies to its legacy client context only.
- **Class/method/field / packet/opcode:** none.
- **Cross-support:** supports earlier morph-list evidence and patch-ingestion article.
- **Conflicts:** modern/other client generations may not store all XML identically; do not generalize `all XML encrypted` to 8.8.
- **8.8 relation:** locate equivalent polymorph/config source first (XML/BIN/other), then test encryption/signature/check path read-only.
- **Verification tags:** `待驗證`: 8.8 source + encryption gate; `版本未確認`; `僅 research evidence`.

### R08 — 天堂私服 | 對話檔加密解密 工具
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220216-private-lineage-83/
- **Original URL:** same
- **Source / published:** J.J.'s Blogs / 2022-02-16
- **Classification:** Shared
- **Lineage version:** old Lineage / L1J-style DB example
- **Access/status:** PUBLIC_FULL + COFFEE_RESOURCE_BLOCK
- **Main content:** client HTML dialogue files and their server-side NPC mapping, plus encryption/decryption workflow.
- **Technical details/tools:** `對話檔加密解密.exe`; decrypt: place target dialogue files in `Dtext`, run executable; encrypt: place files in `text`, run executable.
- **Files/resources:** `Text.idx`; example `colusher.html`; HTML/dialogue resource.
- **DB/class/field details:** example SQL `SELECT npcid FROM npc WHERE name like '競技場入場管理員'` -> `npcid=50019`; `SELECT normal_action FROM npcaction WHERE npcid='50019'` -> `colusher`, mapping to `colusher.html`.
- **Operation flow:** identify NPC -> DB `npc` -> `npcaction.normal_action` basename -> extract matching HTML from Text.idx -> decrypt/read/compare with in-game dialogue.
- **Packet/opcode:** none stated in this article.
- **Limits/exceptions:** tool/password safety/access caveats; DB/table names are old-version evidence only.
- **Cross-support:** strongly corroborates later NPC dialogue mapping and HTML link/action articles.
- **Conflicts:** none within old-version corpus; whether 8.8 still keys dialogue by basename is unknown.
- **8.8 relation:** high-value Shared bridge candidate: Server NPC action state -> transmitted dialog identifier -> Client text-resource resolver.
- **Verification tags:** `待驗證`: 8.8 DB fields, packet ID/name and client resolver; `版本未確認`; `僅 research evidence`.

### R09 — 天堂私服 | 登入器素材抽檔 工具
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220217-private-lineage-84/
- **Original URL:** same
- **Source / published:** J.J.'s Blogs / 2022-02-17
- **Classification:** Client
- **Lineage version:** legacy launcher/client context
- **Access/status:** PUBLIC_FULL + COFFEE_RESOURCE_BLOCK
- **Main content:** simple launcher-material extraction utility.
- **Technical details/tools:** `登入器素材抽檔.exe`; place executable in target client directory and run; extracted launcher material is written under `skin`.
- **Files/resources:** article does not enumerate each extracted extension; logical output is `skin` directory.
- **Operation flow:** copy tool to client root -> execute -> inspect generated `skin` folder.
- **Limits/exceptions:** no format/internal loader explanation; tool security warning; password locked.
- **Class/method/field / packet/opcode:** none.
- **Cross-support:** relates to later launcher intro/function articles that establish launcher as more than a start button.
- **Conflicts:** none.
- **8.8 relation:** candidate method for inventorying LinLogin/UI resource provenance, but this exact utility may be incompatible.
- **Verification tags:** `待驗證`: LinLogin asset source/container; `版本未確認`; `僅 research evidence`.

### R10 — 天堂私服 | Linskin4.04 工具(IMG↔BMP)
- **Canonical URL:** https://morosedog.gitlab.io/private-lineage-20220219-private-lineage-86/
- **Original URL:** same
- **Source / published:** J.J.'s Blogs / 2022-02-19
- **Classification:** Client
- **Lineage version:** legacy client UI resource context
- **Access/status:** PUBLIC_FULL + COFFEE_RESOURCE_BLOCK
- **Main content:** converts client UI `IMG` to editable `BMP`, then BMP back to IMG for re-ingestion; also supports PAK extraction.
- **Technical details/tools:** `linskin4.04.exe`; functions `IMG轉BMP`, `BMP轉IMG`, `個別轉換`; four directory settings: batch IMG input/output, batch BMP input/output, PAK path, PAK-extract output.
- **Files/resources:** IMG, BMP, PAK; intended for UI/surface modification.
- **Operation flow:** configure folders -> batch IMG->BMP -> edit BMP externally -> BMP->IMG -> eat modified IMG; optional PAK extraction path.
- **Limits/exceptions:** author reports `個別轉換` appeared not to work in actual test; tool/download safety caveats apply.
- **Class/method/field / packet/opcode:** none.
- **Cross-support:** corroborates PakViewer intro's IMG/BMP capability and legacy `Surf`/IMG UI-resource role.
- **Conflicts:** single-file conversion capability is uncertain despite UI presence.
- **8.8 relation:** first verify whether 8.8 still contains same IMG encoding and metadata; avoid assuming round-trip fidelity.
- **Verification tags:** `待驗證`: 8.8 IMG presence/pixel/alpha/offset semantics and tool behavior; `版本未確認`; `僅 research evidence`.

## Checkpoint 10 status
- Reread attempted: 10
- Full main body successfully reacquired: 10
- Main body inaccessible: 0
- Article-level records complete: 10
- Articles with material details not present in previous batch summary: 8
- Previous-summary claims not re-confirmed: 0 at this checkpoint
- Baseline impact: **none; remains 88 / 783**
