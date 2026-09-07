# J.J.'s Blogs 全站精讀進度 — 2026-09-07 18:15

> 本輪上限 50 篇；每 10 篇 checkpoint。網站內容一律視為 research evidence，不直接升格為 8.8C 專案事實。

## Checkpoint 10 / 50
完成：eat、PakViewer 介紹、Pakext、PakViewe、PackViewer_beta2、MTools、XML加解密、對話檔加密解密、登入器素材抽檔、Linskin4.04。分類累計：Client 9 / Shared 1。

## Checkpoint 20 / 50
新增：SPR-BMP互轉、ViX、超簡易地圖預覽、Lineage Map V1、地圖屬性修改、LiTo Map、Flexible Renamer、NPC對應對話檔、文字對話檔、圖片對話檔。分類累計：Client 15 / Shared 5。

## Checkpoint 30 / 50
新增：Link篇、Action篇、強化魔法師、傳送師、道具篇、出生道具一/二、GM command dispatcher、Who、怪物血條。分類累計：Client 16 / Server 3 / Protocol 4 / Shared 7。

## Checkpoint 40 / 50
新增：描述、金幣、創立道具、創立套裝、移動、創怪/創NPC、Server/Client 概念、DB 概念、匯入 DB、Server config/boot。分類累計：Client 16 / Server 9 / Protocol 4 / Shared 9 / Other 2。

## Checkpoint 50 / 50

|#|標題|URL|分類|存取|來源版本|重點流程 / 可重用方法|8.8C 假設 / 驗證點|
|---|---|---|---|---|---|---|---|
|41|天堂私服 \| 主程式安裝及登入器設定與登入|https://morosedog.gitlab.io/private-lineage-20210723-private-lineage-11/|Shared|PUBLIC_FULL|Lineage 3.81C / L1J-3.80c|Login encoder 設 server name/IP/port/version→Login.ini→launcher files 匯入 client→Login.exe server list→連線/login|8.8 驗證 LinLogin/Lin.bin 的 server-list source、endpoint/version metadata、launcher→client handoff 與 connection bootstrap；不沿用 2000/舊 Login.ini|
|42|天堂私服 \| IP 位址 (IP Address)|https://morosedog.gitlab.io/private-lineage-20210724-private-lineage-12/|Other|PUBLIC_FULL|通用網路概念|IPv4/IPv6、loopback/特殊位址、device addressing 作為連線研究基線|只作網路語彙，不構成 8.8 endpoint/protocol 事實|
|43|天堂私服 \| 內部、外部網路|https://morosedog.gitlab.io/private-lineage-20210725-private-lineage-13/|Other|PUBLIC_FULL|通用網路概念|localhost/127.0.0.1/0.0.0.0、LAN/WAN、DHCP/撥接與常見拓樸|8.8 連線問題需分 bind address、advertised endpoint、NAT/public endpoint；實際值 runtime 驗證|
|44|天堂私服 \| 防火牆輸入輸出規則|https://morosedog.gitlab.io/private-lineage-20210726-private-lineage-14/|Protocol|PUBLIC_FULL|通用 + 舊 L1J setup|以 inbound/outbound、TCP/UDP、Port 建 rule；舊示例開 2000|8.8 先從 server listen socket / client connect trace 確認 transport/port，再決定 firewall rule；不沿用舊 port|
|45|天堂私服 \| 虛擬服務器和通訊埠轉發|https://morosedog.gitlab.io/private-lineage-20210727-private-lineage-15/|Protocol|PUBLIC_FULL|通用網路概念|NAT/Virtual Server 將 external IP:port 轉到 internal IP:port；需先確定 LAN host 與 service port|8.8 遠端測試分 server bind、NAT mapping、public reachability；不把 NAT 問題誤判 client/server protocol bug|
|46|天堂私服 \| 對外設定(撥接上網)|https://morosedog.gitlab.io/private-lineage-20210728-private-lineage-16/|Protocol|PUBLIC_FULL|舊部署示例|直接取得 public IP 時 launcher endpoint 指向 public IP，仍需 host firewall|8.8 僅保留 endpoint/reachability 分層方法；ISP/NAT 情況需現場判斷|
|47|天堂私服 \| 對外設定(數據機DHCP)|https://morosedog.gitlab.io/private-lineage-20210729-private-lineage-17/|Protocol|PUBLIC_FULL|舊部署示例|modem DHCP 配 LAN address、public IP 位於 gateway/edge；launcher 使用 public endpoint|8.8 建議將 local bind address 與 advertised/public address 分欄記錄|
|48|天堂私服 \| 對外設定(WiFi-DHCP)|https://morosedog.gitlab.io/private-lineage-20210730-private-lineage-18/|Protocol|PUBLIC_FULL|舊部署示例|WiFi router DHCP→LAN IP；NAT port-forward external port→internal host/port|8.8 network runbook 應單獨驗證 DHCP address stability/NAT rule，不與 packet logic 混在一起|
|49|天堂私服 \| 登入器簡介|https://morosedog.gitlab.io/private-lineage-20210801-private-lineage-20/|Client|PUBLIC_FULL|Login_v380a / L1J-3.80c|launcher 目錄含 LoginUpdate、Login.exe、Encode.exe；另有 packet encryption 與 movement-packet compatibility docs|8.8 inventory LinLogin/launcher components，區分 bootstrap/update/config/protocol adaptation；舊檔名只作線索|
|50|天堂私服 \| 登入器功能說明|https://morosedog.gitlab.io/private-lineage-20210802-private-lineage-21/|Shared|PUBLIC_FULL|Login_v380a / L1J-3.80c|encoder 保存 server name/IP/port/version 到加密 ServerData；功能另含 RSA packet encryption、anti-cheat、morph PAK、multi-client、server-list update；工具可產 key/morph PAK|8.8 必須分別驗證 LinLogin 是否介入 endpoint config、crypto handshake、resource/morph loading、update/patch、multi-instance；不能假定任何舊 feature 仍存在|

### 本輪最終分類（50 篇）
- Client: 17
- Server: 9
- Protocol: 9
- Shared: 11
- Other: 4
- Total: 50

## 本輪重要新發現（research only）

1. **Resource Browser 功能模型已更完整**：舊工具反覆採 `index/container inventory -> search/filter -> single-entry preview -> optional extract/convert`，並把 TBT、SPR、IMG、map、text 分成不同工作流。這強化 8.8 Toolkit 的 `index-first + on-demand decode` 方向。
2. **對話 UI 有 Client-only 與 Server-action 兩條路**：舊版 HTML `link` 可做資源內頁面跳轉，而 `action` 會導向 server-side action handling；`var src` 又提示 server data injection。8.8 需要 packet capture 才能分界。
3. **Inventory server 模型提供新的驗證模板**：舊 `storeItem` 區分 stack merge / new instance；GM item/create-set 與 beginner item 又展示 template -> instance -> inventory -> persistence。這對 8.8 背包 owner/reorder 研究有直接方法價值，但 class/field 不可照搬。
4. **NPC spawn 可形成端到端鏈**：template/impl -> persistent spawn row -> runtime NPC instance -> client visibility，可與目前 `npc.gfxid -> packet -> client resource` 研究接起來。
5. **Launcher 可能是多角色元件**：舊 Login_v380a 不只提供 server list，也可能介入 packet encryption、morph resource、updates、multi-instance。這提示 8.8 應把 LinLogin/Lin.bin 載入責任做 runtime provenance，而不是預設所有資源由 client 原生載入。
6. **Network reachability 與 protocol bugs 要分層**：bind address / public endpoint / firewall / NAT / service port 應與 packet parser、crypto、game state 分開診斷。

## 需要 8.8 Client / Server / Runtime 驗證

### Client
- IDX/PAK/TBT/SPR/IMG/Text/Map 的實際 8.8 container、decoder、locale fallback、cache 與 override precedence。
- `Lin.bin / LinLogin.bin` 誰載入 morph/resource/server-list/update metadata。
- Dialog `link` 是否 client-only；IMG/resource locale fallback 是否仍存在。
- Item icon resolver 與 sprite resolver 的實際 mapping。

### Server
- Inventory owner/container、stack merge/new instance、DB persistence、reorder/sort producer 與 client refresh。
- NPC template -> spawn persistence -> runtime instance -> gfx/resource ID 的 8.8 實際鏈。
- Dialog action dispatcher、teleport/item-use handlers、config/XML/DB source。
- Config/default/env 的 provenance 與 boot consumers。

### Runtime / Protocol
- UI `action` click -> outbound packet -> server dispatcher -> response/dialog transition。
- Login bootstrap、endpoint/version negotiation、crypto handshake，以及 LinLogin 是否改寫/代理 packet path。
- Move/teleport/map transition 的 authoritative state 與 client render/interpolation。
- Server listen transport/port、NAT/firewall 只在確認 runtime socket 後處理。

## 累計基線

執行前依 `JJ_SITE_RESEARCH_TRACKER.md` 與歷次 `JJ_RESEARCH_PROGRESS_*.md` 對明確標記「已實際取得正文/完成」的文章做標題/URL 去重，得到 **38 篇明確完成基線**；分類頁摘要、只有標題、LOCKED、未解密會員段落不計入。

- 本輪完成：50
- 累計明確完成：88 / 783
- 剩餘：695

> 注意：這裡的 88 是「目前 GitHub 可追溯、已去重的明確完成 ledger」，不是臆測歷史閱讀量。後續所有輪次以此 ledger 增加，避免再出現只能估算剩餘篇數的問題。

STATUS = COMPLETE_50_OF_50_STOP
