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

STATUS = IN_PROGRESS_10_OF_50
