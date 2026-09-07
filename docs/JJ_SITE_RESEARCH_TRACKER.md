# J.J.'s Blogs 全站研究追蹤（783 篇）

最後更新：2026-09-07 18:16

## 原則

- 網站內容全部視為 `research evidence`，不是 8.8C 已驗證專案事實。
- 3.81C / L1J-3.80c 位址、class、欄位、packet code、resource precedence、action 編號不得直接套到 8.8C。
- 只有實際取得主要正文的文章計入完成；分類頁摘要、標題、LOCKED、未解密會員區塊不計入 FULL。
- `COFFEE_RESOURCE_BLOCK`：主要正文已讀，但下載/解壓密碼區未解密；不把鎖定區算已讀。
- `ADVANCED_LOCKED / FLAGSHIP_LOCKED / COFFEE_LOCKED_BY_READER`：標記後跳過。
- 後續每輪以 URL/標題去重，禁止重讀 tracker/progress 已完成項。
- 後續每個 research Goal 最多重讀/精讀 10 篇；逐篇證據規格不降低。每 10 篇更新 tracker / progress / map、commit、push 後停止，下一批必須等待使用者再次下令；重讀既有文章不增加完成數。

- 資訊保留規則：凡正文涉及天堂 Client、Server、Protocol、資源/修改工具、封包或檔案格式，即使暫時無法驗證、版本不明或可能不適用 8.8，也保留在當輪 progress / research map，並標記 `待驗證`、`版本未確認` 或對應 evidence label；只有可確認為完全重複的內容才去除。是否能直接轉成 8.8 PoC 只是額外欄位，不是收錄門檻。

來源：
- https://morosedog.gitlab.io/archives/
- https://morosedog.gitlab.io/categories/
- https://morosedog.gitlab.io/support/

## 全站基線（已重新驗證）

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

執行 `JJ_RESEARCH_PROGRESS_20260907_1815.md` 前，對 tracker 與歷次 progress 中明確標記「已取得正文/完成」的標題與 URL 去重，得到 **38 篇基線**；18:15 批次新增 50 篇。本次 `1816` 精讀 9 篇，其中 8 篇已在 18:15 批次，`SPZ、XML 加解密` 已在 16:08 正文核對；去重後新增 0 篇。

- **累計明確完成：88 / 783**
- **剩餘：695**
- 完成率：約 11.24%
- 最新 progress：`docs/JJ_RESEARCH_PROGRESS_20260907_1816.md`
- 已保存 checkpoint：18:15 的 10 / 20 / 30 / 40 / 50，以及本次去重/完整事實補錄。

> 88 是目前 GitHub 可追溯、去重後的完成數；本次 9 篇只增加完整事實與不確定標記，不增加 article count。未來所有輪次都從此 ledger 增加，不再用「約 30+」估算。

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

### Client / Resource 詳細補錄

已讀：
- PakViewer Ver.3.0 — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- SFDviewer — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- TBT 編譯器(PNG↔TBT) — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- LineageSpr — 正文可讀；下載密碼區鎖定
- L1Viewer — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- Lineage Icon v120119 — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- 客戶端 idx、pak 說明 — `PUBLIC_FULL`
- 補丁副檔名對應說明 — `PUBLIC_FULL`
- 補丁吃檔資料夾對應 — `PUBLIC_FULL`
- Pakext — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- PakViewe — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- PackViewer_beta2 — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- MTools — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- XML 加解密 — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- SPZ、XML 加解密 — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- 對話檔加密解密 — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- 登入器素材抽檔 — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`
- Linskin4.04 (IMG↔BMP) — `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`

新增 Resource Tool Matrix 證據：
- PakViewer Ver.3.0：`index-first + search/filter + on-demand preview`；可跨 Sprite/Text/Tile，支援動態圖/文字預覽。
- L1Viewer：檔名搜尋支援 literal/case-sensitive/regex/filter；另有文件內容全文檢索；TBT 可用 image-list；SPR 可播放、逐幀前進/後退，顯示 current/total frame。
- Lineage Icon：TBT Query / TBT Browse / SPR Query 分離；再次支持 Item Icon Browser 與 SPR Browser 應有不同 resolver/UX。
- SFDviewer：可同時開兩份 SFD，顯示每 frame X/Y 並做合成，支持 compositor 一級功能。
- TBT 工具：TBT 用於道具圖、魔法圖、人物狀態圖。
- 3.81C IDX/PAK：IDX 作 entry index，PAK 作實際資源容器；工具可由 entry 解出 SPR 多 frame。8.8 必須保留 source provenance，尤其處理 duplicate entry。
- 3.81C override folders 顯示 icon/sprite/surf/text/tile 是不同 logical resource class；8.8 只能把這當 loader/search-root 的研究框架，不沿用固定目錄名。
- Pakext：單一 IDX 開啟後可 extract/delete，並可 add/extract/delete 與依欄位排序；文章沒有提供 preview 證據。
- PakViewe：單一 IDX 開啟，具 export/add/update/delete、類型過濾、模糊搜尋、排序、圖像/動態圖/文字 preview 與縮放。
- PackViewer_beta2：可開單一 PAK 或 client folder，具類型 filter、search/sort、preview、export/add/delete；文章特別記錄 XML preview 會自動解密，`Save resource` 在作者實測中疑似無效。
- MTools：TBT image-package browser，可從 client directory 抽出 selected/all images；正文未顯示搜尋或寫回能力。
- XML 加解密：單檔工具以 `_d.xml` / `_e.xml` 產生輸出；作者警告不要反覆多次 decrypt/encrypt，且寫回前必須恢復正確檔名。
- SPZ、XML 加解密：同目錄批次模式以 `.dec` / `.e` 控制方向；文章範例對 `polymorphList.xml`、`list.spz` 做 round trip，重新加密結果與原始加密內容一致。
- 對話檔加密解密：`Dtext` 解密、`text` 加密；3.81C 文章實測 HTML 對話檔加密或未加密皆可被 client 使用，並由 server `npcaction.normal_action` 的 basename 連到 client dialogue resource。
- 登入器素材抽檔：專用 extractor 放在 client root 執行，輸出至 `skin`；只能作特殊 extractor 證據，不能推定一般 IDX/PAK loader 行為。
- Linskin4.04：支援批次 IMG↔BMP，另可列出 PAK、按副檔名批量抽取或雙擊單筆抽取；作者記錄單檔轉換按鈕疑似無效。
- 新工具矩陣證明應分離五層：`container inventory`、`search/filter`、`on-demand preview/decrypt`、`export/convert`、`container mutation/repack`；8.8 第一階段只採唯讀前三層，寫回功能必須在隔離副本另驗。

上述 9 篇中，Pakext、PakViewe、PackViewer_beta2、MTools、XML 加解密、對話檔加解密、登入器素材抽檔與 Linskin4.04 已出現在 18:15 的 50 篇 ledger；`SPZ、XML 加解密` 已在 `JJ_RESEARCH_PROGRESS_20260907_1608.md` 記錄正文核對。本次全部不重複計數，只補齊完整逐項事實，詳見 `docs/JJ_RESEARCH_PROGRESS_20260907_1816.md`。

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

執行門檻：等待使用者下一個明確 research Goal；每批固定最多 10 篇，完成 checkpoint 後停止。

1. 天堂私服尚未完成的核心分析 / GM / NPC / Item / packet state，優先補完整 Server↔Protocol↔Client 案例。
2. x64dbg 36 / OllyDBG 16 / CE 10：逐篇完成 debugger playbook，不再只做方法抽樣。
3. x86 3 + 排序演算法 7：只抽 binary ABI / stable multi-key inventory reorder 直接有用部分。
4. Java/MySQL/XML/C：依 Lineage cross-reference 讀，優先 DB loader、packet/state、resource parser。
5. Python/OpenCV：優先 preview/GUI/threading/YOLO/vision 自動分類與驗證。
6. 同步補 783 篇 title-level inventory，使每篇都有 `UNREAD / PUBLIC_FULL / LOCKED / IRRELEVANT` 狀態。

STATUS = IN_PROGRESS
NEXT_BASELINE = 88/783

## 最新增量 checkpoint

- 執行前 GitHub 可追溯基線：88 / 783。
- 本次實際精讀：9 篇；與 16:08、18:15 ledger 以 URL/標題去重後新增 0 篇。
- 累計明確完成：88 / 783。
- 剩餘：695。
- 本次 9 篇仍保留較完整 Client/Server/resource/tool/format 事實，不重複增加完成數。
- 天堂私服 130 篇的精確已完成數需以完整 URL ledger 再按 category 去重；本 checkpoint 不以舊摘要反推，避免假精確。
- Archives 79 頁 title-level inventory 仍未完成；695 是尚未標記完成的全站剩餘數，不等於已逐篇判定 access status。

## 最新進度檔

- `docs/JJ_RESEARCH_PROGRESS_20260907_1816.md`
