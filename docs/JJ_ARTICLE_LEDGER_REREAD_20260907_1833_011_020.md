# J.J. article-level reread — R11–R20

> Continuation of the 2026-09-07 18:33 reread. Baseline remains **88 / 783; remaining 695**. No completed-count increment.

### R11 — 天堂私服 | SPR-BMP互轉 工具(SPR↔BMP)
- Canonical/original URL: https://morosedog.gitlab.io/private-lineage-20220220-private-lineage-87/
- Source/date: J.J.'s Blogs / 2022-02-20; Classification: Client; Version: legacy Lineage resource workflow; Access: PUBLIC_FULL + COFFEE_RESOURCE_BLOCK.
- Main/technical: `SPR-BMP互轉` consists of `BMP轉SPR.BAT`, `sfd.exe`, `SPR轉BMP.BAT`, `spr.exe`. `spr.exe` reads SPR and emits BMP frames + SFD; `sfd.exe` reads SFD to rebuild SPR.
- Files/resources: example from `Sprite.pak`: `240-13.spr`, `240-20.spr`; output includes `240-13.sfd`, numbered BMP frames and `-a.bmp`; same pattern for 240-20.
- Flow: extract SPR -> edit BAT lines such as `spr 240-13.spr` -> run SPR-to-BMP -> edit BMP while retaining SFD -> edit reverse BAT with `sfd 240-20.sfd` -> run -> SPR is produced and may overwrite an existing SPR.
- Limits/exceptions: if `spr.exe` crashes, article suggests ingesting SPR and using LineageSpr to extract specific BMP; SFD is explicitly said to contain settings used with SFDviewer. Overwrite behavior requires caution.
- Class/method/packet: none.
- Cross-support/conflict: strongly supports SFDviewer offset/metadata importance and refines previous summary: round-trip is not BMP-only. No direct conflict.
- 8.8 relation/tags: validate actual SPR/SFD-like metadata, pixel format, offsets and lossless round-trip before designing compositor. `待驗證` `版本未確認` `僅 research evidence`.

### R12 — 天堂私服 | ViX 工具(圖檔轉位元)
- URL: https://morosedog.gitlab.io/private-lineage-20220501-private-lineage-96/ ; Source/date: J.J.'s Blogs / 2022-05-01; Classification: Client; Access: PUBLIC_FULL + COFFEE_RESOURCE_BLOCK.
- Main/technical: ViX.exe is used because SPR-extracted/edited BMP may become 32-bit or 24-bit while this old conversion workflow requires 16-bit BMP to turn back into SPR.
- Flow: open image directory -> Ctrl/Shift select files -> Image -> Batch Conversion -> `Decrease color depth` -> `Decreasing Color` -> choose bit depth -> confirm -> `Overwrite all` -> verify resulting 16-bit depth.
- Files/formats: BMP; dependency on SPR-BMP workflow. Tool has many other functions but article intentionally documents only bit-depth batch conversion.
- Limits: 16-bit requirement is specific old-workflow evidence, not a universal SPR rule; overwrites files.
- 8.8 relation: measure actual 8.8 pixel formats and decoder requirements first. `待驗證` `版本未確認` `僅 research evidence`.

### R13 — 天堂私服 | 超簡易地圖預覽 工具
- URL: https://morosedog.gitlab.io/private-lineage-20220222-private-lineage-89/ ; date 2022-02-22; Classification: Client; Access: PUBLIC_FULL + COFFEE_RESOURCE_BLOCK.
- Tool: `MapTool.exe`. Purpose: preview client maps and export read client map data to `.txt`.
- UI/capabilities: set Lineage/client path -> map list -> click map -> preview + map information. Toolbar `匯出` exports selected map; `全部匯出` creates `all` folder; default output format stated as `L1J`; default output location is MapTool program directory.
- Formats/resources: client map data -> L1J-style `.txt` export.
- Limits: article does not define binary map schema; output conversion semantics are tool-specific.
- Cross-support: supports index/list-once + selected preview pattern; complements Lineage Map V1.
- 8.8 relation: map Browser method is reusable, map encoding is not assumed. `待驗證` `版本未確認` `僅 research evidence`.

### R14 — 天堂私服 | Lineage Map V1 工具
- URL: https://morosedog.gitlab.io/private-lineage-20220223-private-lineage-90/ ; date 2022-02-23; Classification: Client; Access: PUBLIC_FULL.
- Main: tool exports client `.map` files and tile-material `.til` files.
- UI/flow: File->Open sets client path; map list/preview/map info; right-click save selected/all `.map`, save selected/all used `.til`, copy map ID.
- Tool items with unknown behavior preserved: `比對 (I)` purpose unknown; `修復 Tile .idx .pak` purpose unknown / not researched by author.
- Formats/resources: `.map`, `.til`, Tile IDX/PAK mentioned by UI.
- 8.8 relation: useful clues for map-to-tile dependency graph; unknown tool actions remain unknown. `待驗證` `版本未確認` `僅 research evidence`.

### R15 — 天堂私服 | 地圖屬性修改 工具
- URL: https://morosedog.gitlab.io/private-lineage-20220224-private-lineage-91/ ; date 2022-02-24; Classification: Shared; Access: PUBLIC_FULL.
- Main: modifies map-area attributes (normal/combat/safe), exports `MapRange` examples describing map boundaries, and `MapPortal` describing entrances/portal positions.
- Flow/capabilities: set client path -> map list -> right-click selected map -> convert safe/normal/combat blocks among those states; `全地圖皆可走動`; `清除地圖傳送點`; article contains additional export/operation examples.
- Files/semantics: map region flags, walkability, portal points, map boundaries; these bridge visible client maps with server/gameplay semantics.
- Limits: old tool semantics may combine data from client resource and server-oriented output; exact authority layer must be re-established in 8.8.
- Cross-support: corroborates map preview/export tools while adding collision/region/portal semantics.
- 8.8 relation: distinguish client visual map, server collision/region rules, map-transition state. `待驗證` `版本未確認` `僅 research evidence`.

### R16 — 天堂私服 | LiTo Map 工具
- URL: https://morosedog.gitlab.io/private-lineage-20220225-private-lineage-92/ ; date 2022-02-25; Classification: Client; Access: PUBLIC_FULL + COFFEE_RESOURCE_BLOCK.
- Tool: `LiToMap.exe`; purpose is extracting/converting client 無界擂台 maps into Lineage-usable files.
- Flow: start tool -> set client path -> right-click `轉換成天堂可用地圖` -> converts all listed target maps -> output files appear in program directory; article delegates later ingestion/use to prior patch articles.
- Limits: source/target map formats are not formally specified; one antivirus vendor flagged the tool in article's scan, which does not establish maliciousness or safety.
- 8.8 relation: only conversion-workflow evidence; format must be independently identified. `待驗證` `版本未確認` `僅 research evidence`.

### R17 — 天堂私服 | Flexible Renamer 工具(批量修改檔名)
- URL: https://morosedog.gitlab.io/private-lineage-20220502-private-lineage-97/ ; date 2022-05-02; Classification: Client; Access: PUBLIC_FULL + COFFEE_RESOURCE_BLOCK.
- Tool: `FlexibleRenamerPortable.exe`. Purpose: batch rename many SPR filenames while editing morph mappings.
- Concrete mapping example: old Death Knight/Fenyan SPR sets `11653→362`, `11654→363`, `11655→364`, `11656→365`.
- Flow: open image/resource directory -> `取代字串` -> enter source substring (e.g. 11653) -> preview current/new names -> enter target (362) -> `重新命名(R)`; repeat mappings.
- Formats/resources: SPR filenames/prefixes; no parser change implied.
- Limits: destructive rename; article text accidentally says "批量圖檔轉位元" in UI intro although actual operation is rename—preserved as editorial inconsistency, not technical claim.
- Cross-support: supports morph-ID vs actual sprite-file mapping problem, but rename is a patching method, not resolver proof.
- 8.8 relation: prefer virtual alias/mapping preserving provenance instead of physical rename. `待驗證` `版本未確認` `僅 research evidence`.

### R18 — 天堂私服 | NPC對應對話檔分析
- URL: https://morosedog.gitlab.io/private-lineage-20220226-private-lineage-93/ ; date 2022-02-26; Classification: Shared; Versions: Lineage 3.81C + L1J-3.80c; Access: PUBLIC_FULL.
- Main: locates a specific NPC's dialogue by joining runtime position, spawn DB and `npcaction`, then verifies matching extracted HTML.
- Concrete flow: stand near 奇岩村 商店村傳送師 艾巴 -> `/loc` yields `33439,32809` -> `SELECT * FROM spawnlist_npc WHERE location='商店村傳送師'` -> among 19 rows choose locx/locy-near row -> `npc_templateid=50034` -> query `npcaction.npcid=50034` -> inspect dialogue fields -> extract matching files with PakViewer -> compare in-game.
- Fields: `normal_action` (neutral/lawful dialogue), `caotic_action` (evil/chaotic dialogue), `teleport_url`, `teleport_urla`; example basenames `grtztele`, `grtztele1`.
- Files/resources: HTML/dialog files, packed client text resources; spawn tables + npcaction.
- Limits: field spellings/karma meaning are old-version evidence; position/name search is one lookup strategy, not necessarily unique.
- Cross-support: directly corroborates dialogue encryption article and later text/image/link/action series.
- 8.8 relation: build `runtime NPC -> server template/action -> transmitted dialog key -> client resource` evidence chain. `待驗證` `版本未確認` `僅 research evidence`.

### R19 — 天堂私服 | 文字對話檔分析
- URL: https://morosedog.gitlab.io/private-lineage-20220227-private-lineage-94/ ; date 2022-02-27; Classification: Shared; Versions: 3.81C/L1J-3.80c; Access: PUBLIC_FULL.
- Main: finds NPC 芬 (`npcid=70101`, dialogue `fiin1`), exports `fiin1-c.html`, explains/edit-tests the dialogue markup.
- DB flow: `spawnlist_npc.location='芬'` -> 70101 -> `npcaction.npcid=70101` -> `fiin1`.
- Markup/resources: `<body>`, `<font fg=ffffff>`, `<p align=left>`, `<br>`; article notes standard HTML `color` is ineffective in Lineage and client uses `fg`; color codes do not fully match ordinary web color tables. `<font size>` 1–7/default 3 and face are discussed; `<username>` is an extra game tag showing player name.
- Limits: support for other HTML tags must be empirically tested; client HTML is a subset/dialect, not browser-standard HTML.
- Cross-support: strongly supports image-dialog article and later link/action articles.
- 8.8 relation: identify 8.8 dialog parser/dialect and dynamic-variable expansion rather than assuming full HTML. `待驗證` `版本未確認` `僅 research evidence`.

### R20 — 天堂私服 | 圖片對話檔分析
- URL: https://morosedog.gitlab.io/private-lineage-20220228-private-lineage-95/ ; date 2022-02-28; Classification: Shared; Versions: 3.81C/L1J-3.80c; Access: PUBLIC_FULL.
- Main: extends `fiin1` dialogue with client IMG resources and documents legacy locale lookup behavior.
- Markup: `<img src="#3431" tooltip="...">`; `src` maps to client IMG name/ID; `tooltip` works. Article reports `border`, `alt`, `title`, `width`, `height` ineffective in this client. Closing `</img>` is optional/equivalent in tested case.
- Locale/resource rule: suffix examples `c` Traditional Chinese, `h` Simplified Chinese, `j` Japanese, `e` English, `k` Korean. In Traditional Chinese example, if both `3431c.img` and `3431.img` exist, `3431c.img` is used; if localized file absent, base `3431.img` is used.
- Unresolved evidence: shop Buy/Sell images `#312/#314` did not trivially match previewed 312/314 base/localized IMG files; author could not explain it from server source or client XML. Preserve as an explicit unresolved contradiction/exception.
- Flow: DB locate fiin1 -> export `fiin1-c.html` -> insert IMG markup -> save/eat -> reopen NPC dialogue -> visually verify.
- Cross-support: corroborates dialogue mapping/text articles; adds a real unresolved resource-selection exception.
- 8.8 relation: high-priority Client runtime test for locale/resource fallback and special UI image mapping. `待驗證` `版本未確認` `僅 research evidence`.

## Checkpoint 20 status
- Reread attempted this checkpoint: 10
- Full main body reacquired: 10
- Main body inaccessible: 0
- Article-level records completed total: 20 / 50
- Articles with new details vs previous batch summary: 10
- New verification items: SPR/SFD round-trip metadata; 16-bit legacy constraint; map/tile/range/portal authority; dialogue dialect; locale IMG fallback; unresolved #312/#314 mapping.
- Previous-summary claims not re-confirmed: 0
- Difference found vs previous summary: yes — substantial omitted detail, no proven false summary claim yet.
- Baseline impact: none; remains **88 / 783**.
