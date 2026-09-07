# J.J.'s Blogs 全站研究追蹤（783 篇）

最後更新：2026-09-07 18:15

## 原則

- 網站內容全部視為 `research evidence`，不是 8.8C 已驗證專案事實。
- 3.81C / L1J-3.80c 位址、class、欄位、packet code、resource precedence、action 編號不得直接套到 8.8C。
- 只有實際取得主要正文的文章計入完成；分類頁摘要、標題、LOCKED、未解密會員區塊不計入 FULL。
- `COFFEE_RESOURCE_BLOCK`：主要正文已讀，但下載/解壓密碼區未解密；不把鎖定區算已讀。
- `ADVANCED_LOCKED / FLAGSHIP_LOCKED / COFFEE_LOCKED_BY_READER`：標記後跳過。
- 後續每輪以 URL/標題去重，禁止重讀 tracker/progress 已完成項。

## 全站基線

- Posts：783
- Archives：79 頁
- Categories：86
- Tags：167
- 天堂私服：130
- Python：83（OpenCV 78）
- C：41
- Java：38
- x64dbg：36
- MySQL：30
- XML：20
- OllyDBG：16
- Cheat Engine：10
- 排序演算法：7
- x86 組合語言：3

## 可追溯完成 ledger

執行 `JJ_RESEARCH_PROGRESS_20260907_1815.md` 前，對 tracker 與歷次 progress 中明確標記「已取得正文/完成」的標題與 URL 去重，得到 **38 篇基線**。本輪新增 **50 篇不重複正文**。

- **累計明確完成：88 / 783**
- **剩餘：695**
- 完成率：約 11.24%
- 最新 progress：`docs/JJ_RESEARCH_PROGRESS_20260907_1815.md`
- 本輪 checkpoint：10 / 20 / 30 / 40 / 50 均已 commit 保存。

> 88 是目前 GitHub 可追溯、去重後的完成數；未來所有輪次都從此 ledger 增加，不再用「約 30+」估算。

## 已完成研究主鏈

### Client / Resource

已讀範圍包含：
- morph/list/gfxid/polyid/spr_action/action/SPR/SFD/overlay/weapon/effect/framerate/fallback/reference。
- IDX/PAK、補丁副檔名、吃檔/override、PakViewer Ver.3.0、PakViewer 介紹、Pakext、PakViewe、PackViewer_beta2、L1Viewer、LineageSpr、Lineage Icon、MTools。
- TBT、IMG、SPR↔BMP/SFD、ViX 16-bit 舊工具鏈、XML/SPZ/HTML 加解密、launcher/login skin 素材。
- Map preview/convert/property 工具。

研究結論：Resource Browser 應採 `inventory/index-first -> search/filter -> single-entry on-demand decode -> preview/cache`；TBT/Icon 與 SPR/GFX 是不同 resolver；IDX/PAK source provenance、duplicate candidates、locale/fallback 都要保留。這些是研究設計方向，8.8 格式與 precedence 仍需 runtime/static 驗證。

### Shared / Dialog / NPC

已讀範圍包含：
- DB `npcaction` 類欄位 -> HTML basename。
- HTML text/image、舊 locale IMG fallback。
- `<a link>` 類 client dialog jump。
- `action` -> server-side action handling、`var src` server data injection。
- Teleporter：HTML action -> XML Action Name -> coordinates/map/price。
- Item-use -> server handler -> dialog resource response。
- GM spawn：NPC template/impl -> persistent spawn row -> runtime instance -> client visibility。

8.8 應驗證：`UI/dialog resource -> click/action packet -> server dispatcher/config/DB -> response/state -> client resolver/render`。

### Server / Inventory / Debug

已讀範圍包含：
- 帳號 Debug (一)(二)(三)：DB/config -> SQL literal -> model -> caller -> packet/service -> breakpoint -> predicate/state -> result。
- Beginner item (一)(二)：starter config -> item template -> character inventory persistence。
- GM commands：dispatcher、Who、描述、金幣、創立道具、創立套裝、移動、創怪/NPC、怪物血條。
- DB schema import、server config/boot。

新方法重點：
- inventory 舊版 `storeItem` 有 stack merge / new instance 分支；8.8 要重新驗證 owner/container、merge、insert、persistence、client refresh。
- command/create/spawn 案例可用來驗證 `template -> instance -> owner/container -> DB -> packet -> visible state`。
- runtime branch 可用 debugger 暫改 variable、Evaluate Expression、Step Over/Into/Out 來驗證，不需永久改 config。

### Protocol / Launcher / Network

已讀範圍包含：
- client dialog action -> server handler。
- Who clientpacket path。
- login/main client + Login_v380a server list/config。
- launcher 功能：endpoint/version metadata、舊 packet encryption 選項、morph PAK、server-list update、multi-client 等。
- IP/LAN/WAN/firewall/NAT/port-forward/DHCP 部署文章。

研究結論：連線問題要拆成：
`server bind/listen -> advertised endpoint -> firewall -> NAT/public reachability -> launcher bootstrap -> crypto/protocol -> login/game state`。
不能把 NAT/firewall 問題與 packet/parser/crypto 問題混為一談。

### Reverse Engineering 方法

已深讀：
- CE Step 8：pointer / multi-level pointer / what writes / what accesses / pointer scan。
- x64dbg：字串搜尋、API/call narrowing、反調試流程觀察。
- OllyDBG：由 UI/錯誤/API 推入口、message breakpoint/call flow 方法。

固定 8.8 playbook：
`observable event -> static narrowing -> breakpoint -> what writes/accesses -> caller/owner -> stable anchor -> state transition -> evidence closure`。

## 本輪 50 篇分類

- Client：17
- Server：9
- Protocol：9
- Shared：11
- Other：4
- Total：50

詳細逐篇 URL / 存取 / 來源版本 / 方法 / 8.8 驗證點見：`docs/JJ_RESEARCH_PROGRESS_20260907_1815.md`。

## 鎖定狀態

已知 ADVANCED_LOCKED 至少包含天堂私服分類中的 Java DOM、BOSS 重生設定分析、XML 檔案清單說明、JAXB 基礎等。主要正文公開、只有下載密碼鎖定的工具文標 `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`。未讀鎖定區不作推論。

## 下一輪優先序

1. 天堂私服尚未完成的核心分析 / GM / NPC / Item / packet state，優先補完整 Server↔Protocol↔Client 案例。
2. x64dbg 36 / OllyDBG 16 / CE 10：逐篇完成 debugger playbook，不再只做方法抽樣。
3. x86 3 + 排序演算法 7：只抽 binary ABI / stable multi-key inventory reorder 直接有用部分。
4. Java/MySQL/XML/C：依 Lineage cross-reference 讀，優先 DB loader、packet/state、resource parser。
5. Python/OpenCV：優先 preview/GUI/threading/YOLO/vision 自動分類與驗證。
6. 同步補 783 篇 title-level inventory，使每篇都有 `UNREAD / PUBLIC_FULL / LOCKED / IRRELEVANT` 狀態。

STATUS = IN_PROGRESS
NEXT_BASELINE = 88/783
