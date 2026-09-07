# J.J.'s Blogs 全站精讀進度 — 2026-09-07 18:03

## 本輪執行原則

- 執行前已重新讀取 `JJ_SITE_RESEARCH_TRACKER.md`、`JJ_LINEAGE_RESEARCH_MAP.md`、最新 `JJ_RESEARCH_PROGRESS_20260907_1625.md`。
- 排除上一輪已完成的 morph/action、PakViewer Ver.3.0、SFDviewer、TBT 編譯器、LineageSpr 與既有 x64dbg/OllyDBG/CE 篇目。
- 本輪只把實際取得主要技術正文的文章列為 `PUBLIC_FULL`；下載/解壓密碼若為咖啡會員加密，另記 `COFFEE_RESOURCE_BLOCK`，不把未解密區塊視為已讀。
- Lineage 3.81C / L1J-3.80c 只記為來源版本；任何路徑、欄位、封包結果碼、載入順序都不得直接升格為 8.8C 事實。

## 本輪新增 FULL：8 篇

| # | 標題 | URL | 分類 | 存取等級 | 來源版本 | 重點流程 / 可重用方法 | 8.8C 假設 / 驗證點 |
|---|---|---|---|---|---|---|---|
| 1 | 天堂私服 \| L1Viewer 工具 | https://morosedog.gitlab.io/private-lineage-20220210-private-lineage-77/ | 天堂私服 / 07.工具介紹/使用 | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | 舊版 Lineage resource tool | 選 client directory 後建立文件列表；支援 literal/case-sensitive/regex/filter 搜尋、文件內容全文檢索、TBT image list、SPR 播放與逐幀前進/後退、current/total frame 顯示 | Toolkit Browser 應把「entry metadata search」「content search」「format-specific thumbnail list」「SPR play/step」拆為可獨立能力；先驗證 8.8 IDX inventory 是否可直接驅動，而非先 export PNG |
| 2 | 天堂私服 \| Lineage Icon v120119 工具 | https://morosedog.gitlab.io/private-lineage-20220212-private-lineage-79/ | 天堂私服 / 07.工具介紹/使用 | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | 舊版 Lineage resource tool | TBT 查詢、TBT 瀏覽、SPR 查詢三條路徑分開；TBT 可依 ID 搜尋/瀏覽/匯出；SPR 需指定 client directory 後依編號查詢 | Item Icon Browser 與 GFX/SPR Browser 應共用 index/cache，但 resolver 與 UX 分開；8.8 要驗證 `invgfx -> icon resource` 與 `gfx/morph -> SPR` 是兩條不同 mapping |
| 3 | 天堂私服 \| 客戶端idx、pak說明 | https://morosedog.gitlab.io/private-lineage-20220201-private-lineage-68/ | 天堂私服 / 06.補丁介紹/說明 | PUBLIC_FULL | Lineage 3.81C | IDX 被作者視為資源索引；PAK 為實際壓縮資源容器；IDX 中可見 entry 名如 `10306-24.spr`，工具可由 PAK 解出該 SPR 多 frame | 8.8 固定驗證 `IDX entry -> PAK offset/source -> decoder`；不要把 3.81C 各容器用途或 launcher PAK 猜測直接套用；duplicate source 要保存 source container |
| 4 | 天堂私服 \| 補丁副檔名對應說明 | https://morosedog.gitlab.io/private-lineage-20220202-private-lineage-69/ | 天堂私服 / 06.補丁介紹/說明 | PUBLIC_FULL | Lineage 3.81C | `list.spr/list.spz` 描述 morph/action 對應；SPR 為動畫/圖片資源；`list.ico` 控制舊版 icon 可顯示編號上限；TBT/IMG/HTML/TBL 等各自有不同呈現角色 | 8.8 resource graph 要把 metadata/list、pixel resource、UI text/config 分層；`list.ico` 只列為舊版 hypothesis，需查 8.8 是否仍有 range gate / table |
| 5 | 天堂私服 \| 補丁吃檔資料夾對應 | https://morosedog.gitlab.io/private-lineage-20220203-private-lineage-70/ | 天堂私服 / 06.補丁介紹/說明 | PUBLIC_FULL | Lineage 3.81C | 舊版 override folder 對應：icon→TBT/ICO、sprite→SPR、Surf→IMG、text→HTML/TBL/list.spr/list.spz、Tile→XML/TIL；locale suffix 也在 text 資源中出現 | 8.8 不沿用資料夾名；但可用「format -> logical resource class -> override/search root」概念去找 Lin.bin/LinLogin.bin 的實際 open/read precedence |
| 6 | 天堂私服 \| 遊戲帳號分析/Debug (一)「L1J版」 | https://morosedog.gitlab.io/private-lineage-20210913-private-lineage-46/ | 天堂私服 / 05.核心分析/修改 | PUBLIC_FULL | L1J-3.80c | 從遊戲建立帳號的可觀察行為開始；找 `accounts` table；由 SQL `INSERT` 搜到 `Account.java`；由 object fields 對 DB columns；再用 Find Usages 追到 `C_AuthLogin.java` caller；最後 breakpoint/step 驗證 DB row 寫入 | 8.8 server 研究可直接套「observable action -> DB/table candidate -> SQL literal -> model/object -> caller/clientpacket -> runtime verify」；任何 Java path/line number 只作舊版例子 |
| 7 | 天堂私服 \| 遊戲帳號分析/Debug (二)「L1J版」 | https://morosedog.gitlab.io/private-lineage-20210914-private-lineage-47/ | 天堂私服 / 05.核心分析/修改 | PUBLIC_FULL | L1J-3.80c | Debug 時直接暫改 Variables 內 runtime config 值以進入特定 branch；重現兩個帳號相同 IP；step through online-account loop；觀察條件命中後 server 回傳 login-result packet 並 return | 對 8.8 的可重用點是「不改永久設定，runtime 控制單一 predicate 以驗證 branch」；若用於 8.8，需在隔離測試環境用 debugger/Argus 驗證，不沿用結果碼或欄位名 |
| 8 | 天堂私服 \| 遊戲帳號分析/Debug (三)「L1J版」 | https://morosedog.gitlab.io/private-lineage-20210915-private-lineage-48/ | 天堂私服 / 05.核心分析/修改 | PUBLIC_FULL | L1J-3.80c | 對 password/online/banned/max-user 等 branch 分別設斷點；用 Evaluate Expression 先評估 predicate；再 Step Over 觀察 packet/result 與 return；也可直接看 object state 欄位 | 8.8 runtime playbook 可加入 `break before branch -> evaluate predicate -> inspect owner state -> step -> capture outbound packet/state transition`；result code / branch order 必須重驗 |

## 新增來源事實（JJ_381_DIRECT / JJ_METHOD）

### A. L1Viewer 提供比「單純縮圖」更完整的 Browser interaction model

來源事實：舊工具可對文件名稱做 literal/case-sensitive/regex/filter 搜尋，也有內容全文檢索；SPR 可播放與逐 frame 前後移動；TBT 可切換 image-list 視圖。

可重用方法：Resource Browser 的索引 UI 不應只是一個 GFX ID textbox。至少要分：

`inventory/search -> format filter -> entry preview -> animation step/play -> content search(文字資源)`

8.8 推論：是否能直接在 17+ IDX 上即時全文/regex 搜尋，仍需量測；不能因舊工具能做就宣告 8.8 performance 已確認。

### B. Lineage Icon 再次支持 TBT 與 SPR 是不同 user task

來源事實：該工具把 TBT Query/Browse 與 SPR Query 分成三個主功能；TBT 有內建 icon dataset，SPR 則需要 client directory。

8.8 推論：Toolkit 可共用 container/index cache，但 Item Icon 與 Monster/Transform Sprite 應保留不同 resolver、不同驗收條件與不同搜尋欄位。

### C. IDX / PAK 應保存 source provenance

來源事實：3.81C 教學明確以 IDX entry name 定位 PAK 中的實際資源；同一 entry 可由工具解碼成多 frame。

8.8 推論：既然目前 8.8 已知存在多個 Sprite IDX/PAK 且可能重複 entry，browser row 應保存 `idx/pak source + entry name + duplicate rank/precedence`，不要只存裸 GFX ID。

### D. 補丁資料夾是「logical resource class」證據，不是 8.8 固定路徑

3.81C 把 icon/sprite/surf/text/tile 分成不同 override root。這能作為研究 resource loader 的分類框架，但不能直接假設 8.8 還讀同名資料夾。

8.8 驗證應轉成：

`format/resource class -> open/read calls -> search roots -> override order -> packed fallback`

### E. 核心 Debug 系列形成一個可直接重用的 server trace template

```text
observable game action
  -> DB table / configuration candidate
  -> search SQL/table literal
  -> model/object fields
  -> Find Usages / caller
  -> clientpacket or service entry
  -> breakpoint before condition
  -> evaluate predicate + owner state
  -> step into/out
  -> outbound packet / DB mutation / visible result
```

這個模板比直接從大量 Java class 名盲搜更有效，且與目前 8.8 的 DB→server→packet→client 研究方式一致。

## 鎖定處理

- L1Viewer、Lineage Icon 的主要技術正文可完整取得，故列 `PUBLIC_FULL`。
- 兩篇的下載解壓密碼區塊顯示咖啡會員加密；reader 未解密，因此另標 `COFFEE_RESOURCE_BLOCK`，不讀、不猜、不停留。
- 本輪未把任何 ADVANCED / FLAGSHIP 標題當正文證據。

## 本輪同步目標

- 更新 `docs/JJ_SITE_RESEARCH_TRACKER.md`：新增本輪 8 篇與 Resource Tool Matrix / server trace template 進度。
- 更新 `docs/JJ_LINEAGE_RESEARCH_MAP.md`：加入 L1Viewer/Lineage Icon、resource class / provenance 與 runtime predicate-debug 模板。
- 新增本檔 `docs/JJ_RESEARCH_PROGRESS_20260907_1803.md`。

## 下一輪優先

1. 繼續 `07.工具介紹/使用` 尚未讀的 Pakext / PakViewe / PackViewer_beta2 / MTools / XML-SPZ-HTML crypto / Linskin 等正文。
2. 再讀 `05.核心分析/修改` 中與 NPC / Item / GM create / packet state 直接相連的文章，擴充 server trace template。
3. 網頁搜尋服務可用時恢復 x64dbg / OllyDBG / Cheat Engine 新篇目的精讀；不重讀已記錄章節。
4. 之後補 x86、排序演算法、C/Java/MySQL/XML、Python/OpenCV 的 cross-reference。

STATUS = IN_PROGRESS
