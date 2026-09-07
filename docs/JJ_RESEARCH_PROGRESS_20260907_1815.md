# J.J.'s Blogs 全站精讀進度 — 2026-09-07 18:15

> 本輪上限 50 篇；每 10 篇 checkpoint。網站內容一律視為 research evidence，不直接升格為 8.8C 專案事實。

## Checkpoint 10 / 50

已完成 10 篇：eat、PakViewer 介紹、Pakext、PakViewe、PackViewer_beta2、MTools、XML加解密、對話檔加密解密、登入器素材抽檔、Linskin4.04。

分類：Client 9 / Shared 1。

## Checkpoint 20 / 50

新增 10 篇：SPR-BMP互轉、ViX、超簡易地圖預覽、Lineage Map V1、地圖屬性修改、LiTo Map、Flexible Renamer、NPC對應對話檔、文字對話檔、圖片對話檔。

分類累計：Client 15 / Shared 5。

## Checkpoint 30 / 50

|#|標題|URL|分類|存取|來源版本|重點流程 / 可重用方法|8.8C 驗證點|
|---|---|---|---|---|---|---|---|
|21|天堂私服 \| 點擊跳轉其他對話檔分析<Link篇>|https://morosedog.gitlab.io/private-lineage-20220503-private-lineage-98/|Client|PUBLIC_FULL|3.81C / L1J-3.80c|HTML `<a link="dialog">` 或 `<img link>` 可在 client 對話資源間跳頁；DB→npcaction→basename 找入口|8.8 驗證 link 是否完全 client-side、dialog resolver/cache 與 resource basename 規則|
|22|天堂私服 \| 點擊跳轉其他對話檔分析<Action篇>|https://morosedog.gitlab.io/private-lineage-20220504-private-lineage-99/|Protocol|PUBLIC_FULL|3.81C / L1J-3.80c|`action` 與 `link` 不同；action 值可送入 server 行動處理，`var src` 顯示 server 提供變數|8.8 capture UI click→outbound packet→server action key→response/dialog state；確認 var data ownership|
|23|天堂私服 \| 執行行動分析<強化魔法師篇>|https://morosedog.gitlab.io/private-lineage-20220505-private-lineage-100/|Protocol|PUBLIC_FULL|3.81C / L1J-3.80c|HTML action 值由 `C_NPCAction` 類 server path 解析，可觸發狀態/兌換/購買等行為|8.8 找等價 client action packet 與 server dispatcher，不沿用 class/path|
|24|天堂私服 \| 執行行動分析<傳送師篇>|https://morosedog.gitlab.io/private-lineage-20220506-private-lineage-101/|Shared|PUBLIC_FULL|3.81C / L1J-3.80c|HTML `action` 對 XML `Action Name`；XML Teleport 帶 X/Y/Map/Heading/Price；`var src` 對 Data Value 位置|8.8 驗證 action→server config/DB→teleport state→client transition，確認 XML 是否仍 runtime source|
|25|天堂私服 \| 執行行動分析<道具篇>|https://morosedog.gitlab.io/private-lineage-20220507-private-lineage-102/|Shared|PUBLIC_FULL|3.81C / L1J-3.80c|item use→server item-id branch→send dialog resource name→client HTML；說話卷軸案例|8.8 驗證 `item use packet -> item handler -> UI/dialog response -> resource resolver`|
|26|天堂私服 \| 人物出生道具分析/修改 (一)「L1J版」|https://morosedog.gitlab.io/private-lineage-20210907-private-lineage-40/|Server|PUBLIC_FULL|L1J-3.80c|從 beginner table→搜尋 SQL literal→Beginner model→etcitem/weapon/armor→character_items→遊戲建立角色回驗|8.8 server 以同方法追 starter-item config→inventory mutation；欄位/類名重驗|
|27|天堂私服 \| 人物出生道具分析/修改 (二)「L1J版」|https://morosedog.gitlab.io/private-lineage-20210908-private-lineage-41/|Server|PUBLIC_FULL|L1J-3.80c|逐欄追 beginner→character_items assignment，並用缺失 bless assignment 發現可能 bug|8.8 採 field-by-field provenance audit；不能假定舊 bug 仍存在|
|28|天堂私服 \| GM指令的程式碼邏輯分析「L1J版」|https://morosedog.gitlab.io/private-lineage-20210917-private-lineage-50/|Server|PUBLIC_FULL|L1J-3.80c|commands table/class_name→L1Commands lookup→Find Usages→GMCommands dispatcher→parse command/args→execute/remember last command|8.8 建 command-dispatch graph，確認 DB-driven 或 static registry、caller/permission checks|
|29|天堂私服 \| GM指令/一般指令分析 (Who)「L1J版」|https://morosedog.gitlab.io/private-lineage-20210919-private-lineage-52/|Protocol|PUBLIC_FULL|L1J-3.80c|GM `.who` 經 command executor；一般 `/who` 從 clientpacket `C_Who`，readS 解析 client 字串；同可見功能有兩條入口|8.8 對同一功能分 command/chat packet path；runtime capture opcode/parser/response|
|30|天堂私服 \| GM指令分析/修改 (怪物血條)「L1J版」|https://morosedog.gitlab.io/private-lineage-20210920-private-lineage-53/|Shared|PUBLIC_FULL|L1J-3.80c|以 GM command 切換/呈現怪物血量資訊，提供從 command state 到 client-visible UI 的跨層分析入口|8.8 驗證 HP state owner、packet/update cadence 與 client bar rendering owner|

### Checkpoint 30 累計分類
- Client: 16
- Server: 3
- Protocol: 4
- Shared: 7
- Other: 0

STATUS = IN_PROGRESS_30_OF_50
