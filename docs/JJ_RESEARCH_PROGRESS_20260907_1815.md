# J.J.'s Blogs 全站精讀進度 — 2026-09-07 18:15

> 本輪上限 50 篇；每 10 篇 checkpoint。網站內容一律視為 research evidence，不直接升格為 8.8C 專案事實。

## Checkpoint 10 / 50

本 checkpoint 新增 10 篇，均已實際取得主要正文；下載/解壓密碼若為會員區塊，僅標 `COFFEE_RESOURCE_BLOCK`，未讀區不算正文。

|#|標題|URL|分類|存取|來源版本|重點流程 / 可重用方法|8.8C 驗證點|
|---|---|---|---|---|---|---|---|
|1|天堂私服 \| eat (吃檔教學)|https://morosedog.gitlab.io/private-lineage-20220204-private-lineage-71/|Client|PUBLIC_FULL|3.81C|override file → 對應邏輯資源目錄 → launcher/eat pack/update → 遊戲回驗；以 482.tbt 狀態 icon 替換示範|驗證 8.8 client 是否仍有 external override/pack ingestion、實際 precedence 與 cache invalidation|
|2|天堂私服 \| PakViewer 介紹|https://morosedog.gitlab.io/private-lineage-20220205-private-lineage-72/|Client|PUBLIC_FULL|舊工具能力模型：IDX/PAK inventory、preview、import/export/delete、filename/content search、extension filter、sort、XML decrypt、IMG/BMP conversion|Toolkit 可採 capability matrix；8.8 僅驗證哪些格式/metadata 仍存在，不沿用舊工具假設|
|3|天堂私服 \| Pakext 工具|https://morosedog.gitlab.io/private-lineage-20220206-private-lineage-73/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|舊工具開 IDX，對 entry extract/delete/append/select-all/sort|8.8 Browser 可先做 read-only inventory；任何 write/import 都需隔離副本驗證，不碰原 client|
|4|天堂私服 \| PakViewe 工具|https://morosedog.gitlab.io/private-lineage-20220207-private-lineage-74/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|以 IDX 開啟 container，提供單檔 export/import/delete 與 preview/search 類能力|驗證 8.8 IDX→PAK source resolution 與 entry metadata；不假定工具支援 8.8 格式|
|5|天堂私服 \| PackViewer_beta2 工具|https://morosedog.gitlab.io/private-lineage-20220209-private-lineage-76/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|可直接開 PAK 或 client folder，整合多種檔案瀏覽/轉換功能|8.8 Toolkit 應支援 folder-level inventory + single-container drill-down，先 read-only|
|6|天堂私服 \| MTools 工具|https://morosedog.gitlab.io/private-lineage-20220213-private-lineage-80/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|TBT 專用瀏覽器；指定 client directory，單選或全部抽出圖檔|Item Icon path 應獨立於 SPR resolver；驗證 8.8 icon resource format/source container|
|7|天堂私服 \| XML加解密 工具|https://morosedog.gitlab.io/private-lineage-20220214-private-lineage-81/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|舊版 XML 存在 client-side encryption check；PakViewer 抽 Tile.idx 的 polymorphList.xml→decrypt→edit→encrypt；重複加解密可能破壞內容|8.8 先判斷 XML 是否仍為 runtime source、是否有 encryption/signature gate、polymorph 等價資料是否已移到 bin/other container|
|8|天堂私服 \| 對話檔加密解密 工具|https://morosedog.gitlab.io/private-lineage-20220216-private-lineage-83/|Shared|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|HTML 對話檔由 Text.idx 抽出；DB npc/npcaction normal_action 可對到 html basename，形成 Server DB→Client text resource bridge|8.8 驗證 `npcaction/packet/state -> dialog resource id/name -> client text resolver`，不可直接沿用表欄位名|
|9|天堂私服 \| 登入器素材抽檔 工具|https://morosedog.gitlab.io/private-lineage-20220217-private-lineage-84/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|工具放 client folder，抽取 launcher/login UI 素材到 skin folder|8.8 驗證 LinLogin/launcher UI 素材來源與 container，不推定仍有相同 skin 格式|
|10|天堂私服 \| Linskin4.04 工具(IMG↔BMP)|https://morosedog.gitlab.io/private-lineage-20220219-private-lineage-86/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|IMG↔BMP 轉換，作為舊 client surface/image 資源處理鏈|8.8 先 inventory `.img` 是否仍存在、decoder 與 alpha/palette/offset metadata 是否相容|

### Checkpoint 10 分類
- Client: 9
- Server: 0
- Protocol: 0
- Shared: 1
- Other: 0

## Checkpoint 20 / 50

|#|標題|URL|分類|存取|來源版本|重點流程 / 可重用方法|8.8C 驗證點|
|---|---|---|---|---|---|---|---|
|11|天堂私服 \| SPR-BMP互轉 工具(SPR↔BMP)|https://morosedog.gitlab.io/private-lineage-20220220-private-lineage-87/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|SPR→BMP+SFD；SFD+BMP→SPR；SFD 保存轉回所需 metadata，舊工具以 240-13/240-20 示範多 frame|Toolkit decoder 驗證 frame pixels 與 SFD-like offset/meta 是否可對 8.8 資源閉環|
|12|天堂私服 \| ViX 工具(圖檔轉位元)|https://morosedog.gitlab.io/private-lineage-20220501-private-lineage-96/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|舊 SPR 回編要求 BMP 位深 16；ViX 用批次轉換 24/32→16-bit|8.8 decoder/encoder 需先確認 pixel format；不要沿用 16-bit 假設|
|13|天堂私服 \| 超簡易地圖預覽 工具|https://morosedog.gitlab.io/private-lineage-20220222-private-lineage-89/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|指定 client path→地圖 inventory→preview/info→單張/全部匯出 L1J txt|若未來做 Map Browser，可沿 index-first/on-demand-preview；8.8 map format 另驗|
|14|天堂私服 \| Lineage Map V1 工具|https://morosedog.gitlab.io/private-lineage-20220223-private-lineage-90/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|舊 client map viewer/editor 類工具，可從 client path 建 map list/preview|8.8 是否仍適用其 map/tile format、坐標與属性模型需重驗|
|15|天堂私服 \| 地圖屬性修改 工具|https://morosedog.gitlab.io/private-lineage-20220224-private-lineage-91/|Shared|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|地圖可見像素/tiles 與 Server 可行走/屬性資料是兩層；工具用於修改 map attribute|8.8 應分 client render map 與 server collision/region/map-id semantics，雙端對照|
|16|天堂私服 \| LiTo Map 工具|https://morosedog.gitlab.io/private-lineage-20220225-private-lineage-92/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|指定 client path→列無界擂台地圖→批次轉成天堂可用 map files|驗證 8.8 map container/variant conversion 是否仍存在，不沿用 LiTo format|
|17|天堂私服 \| Flexible Renamer 工具(批量修改檔名)|https://morosedog.gitlab.io/private-lineage-20220502-private-lineage-97/|Client|PUBLIC_FULL + COFFEE_RESOURCE_BLOCK|大量 SPR prefix 重映射時以批次 rename 同步檔名；例 11653..→362..|Toolkit 不應靠 destructive rename 做 resolver；應以 virtual mapping/alias table 保存 source provenance|
|18|天堂私服 \| NPC對應對話檔分析|https://morosedog.gitlab.io/private-lineage-20220226-private-lineage-93/|Shared|PUBLIC_FULL|DB npcaction normal/caotic/teleport URL 欄位→HTML basename→client dialog|8.8 驗證 DB/packet/client dialog ID，欄位名與 karma branch 只作 hypothesis|
|19|天堂私服 \| 文字對話檔分析|https://morosedog.gitlab.io/private-lineage-20220227-private-lineage-94/|Shared|PUBLIC_FULL|舊 client dialog 為 HTML-like markup；文字/排版標籤直接影響 UI 呈現|8.8 查 dialog parser 是否仍 HTML-like、encoding/markup subset 與 resource source|
|20|天堂私服 \| 圖片對話檔分析|https://morosedog.gitlab.io/private-lineage-20220228-private-lineage-95/|Shared|PUBLIC_FULL|HTML `<img src="#ID">` 引用 IMG；舊繁中資源實例顯示 `IDc.img` 優先、缺少則 fallback `ID.img`|8.8 需 runtime 驗證 locale variant lookup order、resource ID resolver 與 UI image cache|

### Checkpoint 20 累計分類
- Client: 15
- Server: 0
- Protocol: 0
- Shared: 5
- Other: 0

STATUS = IN_PROGRESS_20_OF_50
