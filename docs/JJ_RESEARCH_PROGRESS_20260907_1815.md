# J.J.'s Blogs 全站精讀進度 — 2026-09-07 18:15

> 本輪上限 50 篇；每 10 篇 checkpoint。網站內容一律視為 research evidence，不直接升格為 8.8C 專案事實。

## Checkpoint 10 / 50
完成：eat、PakViewer 介紹、Pakext、PakViewe、PackViewer_beta2、MTools、XML加解密、對話檔加密解密、登入器素材抽檔、Linskin4.04。分類累計：Client 9 / Shared 1。

## Checkpoint 20 / 50
新增：SPR-BMP互轉、ViX、超簡易地圖預覽、Lineage Map V1、地圖屬性修改、LiTo Map、Flexible Renamer、NPC對應對話檔、文字對話檔、圖片對話檔。分類累計：Client 15 / Shared 5。

## Checkpoint 30 / 50
新增：Link篇、Action篇、強化魔法師、傳送師、道具篇、出生道具一/二、GM command dispatcher、Who、怪物血條。分類累計：Client 16 / Server 3 / Protocol 4 / Shared 7。

## Checkpoint 40 / 50

|#|標題|URL|分類|存取|來源版本|重點流程 / 可重用方法|8.8C 假設 / 驗證點|
|---|---|---|---|---|---|---|---|
|31|天堂私服 \| GM指令分析/修改 (描述)「L1J版」|https://morosedog.gitlab.io/private-lineage-20210921-private-lineage-54/|Server|PUBLIC_FULL|L1J-3.80c|GM desc command 解析目標玩家，從 world/player owner 與 inventory/state 組合描述資訊，再以系統訊息回傳|8.8 驗證 player registry/owner、state getter 與 outbound system-message path|
|32|天堂私服 \| GM指令分析/修改 (金幣)「L1J版」|https://morosedog.gitlab.io/private-lineage-20211015-private-lineage-55/|Server|PUBLIC_FULL|L1J-3.80c|由 Adena command 追到 `pc.getInventory()`、inventory owner/items；`storeItem` 區分 stack merge 與 new item insertion|8.8 inventory 研究優先驗證 owner/container、stack merge、new-entry insertion、DB persistence 與 client refresh 是否分層|
|33|天堂私服 \| GM指令分析 (創立道具)「L1J版」|https://morosedog.gitlab.io/private-lineage-20211016-private-lineage-56/|Server|PUBLIC_FULL|L1J-3.80c|`.item` 解析 name/count/enchant/identify/attr 等參數→item template→inventory materialization|8.8 驗證 item-template lookup、instance construction、inventory insert、packet/update path|
|34|天堂私服 \| GM指令分析 (創立套裝)「L1J版」|https://morosedog.gitlab.io/private-lineage-20211017-private-lineage-57/|Server|PUBLIC_FULL|L1J-3.80c|GM command 由套裝/清單定義批次建立多個 item instance，最後進角色 inventory|8.8 可用來驗證 bulk materialization 與 inventory batch refresh；舊清單格式不沿用|
|35|天堂私服 \| GM指令分析 (移動)「L1J版」|https://morosedog.gitlab.io/private-lineage-20211020-private-lineage-60/|Shared|PUBLIC_FULL|L1J-3.80c|GM move 解析座標/地圖→server 更新位置/teleport state→client 可見位置同步|8.8 capture position owner、teleport/move packet、map transition 與 client interpolation/render state|
|36|天堂私服 \| GM指令分析 (創怪/創NPC)「L1J版」|https://morosedog.gitlab.io/private-lineage-20211027-private-lineage-67/|Shared|PUBLIC_FULL|L1J-3.80c|`.insert mob/npc npcid`→NPC template/impl type→spawnlist 或 spawnlist_npc persistent row→runtime instance spawn→client 可見|8.8 驗證 `npc template -> spawn persistence -> runtime instance -> packet/gfx -> client resource` 完整鏈|
|37|天堂私服 \| Server、Client 是什麼？|https://morosedog.gitlab.io/private-lineage-20210713-private-lineage-1/|Other|PUBLIC_FULL|通用概念|以 target/location/request/process/response 解釋 client-server request/response；作研究語彙基線|只作概念模型，不形成任何 8.8 implementation fact|
|38|天堂私服 \| 資料庫 Database(DB) 是什麼？|https://morosedog.gitlab.io/private-lineage-20210718-private-lineage-6/|Other|PUBLIC_FULL|通用 / L1J 教學|帳號、角色、怪物掉落等資料由 DB 保存，server 透過 SQL/DAO 使用|8.8 逐資料域驗證 DB 是否 authoritative、cache 是否介入、client 是否只接收 projection|
|39|天堂私服 \| 匯入模擬器的資料庫|https://morosedog.gitlab.io/private-lineage-20210721-private-lineage-9/|Server|PUBLIC_FULL|L1J-3.80c|初始化 schema/seed SQL，批次腳本將多份 SQL 匯入 MySQL；DB schema 是 server 啟動依賴之一|8.8 inventory/schema 研究需同時檢查 DDL、seed/migration、loader，不只看 Java getter|
|40|天堂私服 \| 設定檔說明設定與啟動模擬器|https://morosedog.gitlab.io/private-lineage-20210722-private-lineage-10/|Server|PUBLIC_FULL|L1J-3.80c|server config→DB/network/game settings→boot sequence；設定值會決定 runtime branch/服務行為|8.8 建 config provenance：file/env/default→parsed field→consumer→runtime effect；不沿用舊 key/value|

### Checkpoint 40 累計分類
- Client: 16
- Server: 9
- Protocol: 4
- Shared: 9
- Other: 2

STATUS = IN_PROGRESS_40_OF_50
