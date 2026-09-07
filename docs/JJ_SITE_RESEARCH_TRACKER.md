# J.J.'s Blogs 全站研究追蹤（783 篇）

最後更新：2026-09-07 18:03

## 目標

把 J.J.'s Blogs 當成完整技術語料庫，而不是只挑「天堂私服」單篇文章。3.81C 的位址、檔名優先序、欄位與演算法都不能直接套到 8.8；只抽取研究流程、資料鏈、檔案角色、驗證方法與可驗證假設。

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
- Python：83；OpenCV 子分類：78
- C：41
- Java：38
- x64dbg：36
- MySQL：30
- XML：20
- OllyDBG：16
- Cheat Engine：10
- 排序演算法：7
- x86 組合語言：3

Category 有交叉/巢狀，不能把數量直接相加當唯一文章總數。

## 閱讀狀態代碼

- `PUBLIC_FULL`：文章主要技術正文可完整取得；若只有下載密碼是咖啡會員鎖定，技術正文仍可標此狀態並另註 `COFFEE_RESOURCE_BLOCK`。
- `COFFEE_FULL`：咖啡會員加密正文已實際解鎖並讀取。
- `COFFEE_LOCKED_BY_READER`：頁面標示咖啡會員，但目前 reader 無法解密該區塊。
- `ADVANCED_LOCKED`：管理員/進階或更高等級才可讀；不以標題猜正文。
- `FLAGSHIP_LOCKED`：旗艦層級才可讀。

網站 `/support/` 已確認 2026 咖啡會員年度密碼是公開提供，但 reader 不繼承瀏覽器 localStorage；因此未解密區塊不誤標 FULL。ADVANCED/FLAGSHIP 為不同機制，遇到後只標記並跳過。

## 已完成深讀主題

### A. 天堂私服：變身 / SPR / action 主鏈

已逐頁讀取主要技術正文（3.81C/L1J-3.80c 舊版證據）：

1. 客戶端和登入器與變身檔關係分析
2. 變身檔與 gfxid、polyid 分析
3. 資料庫中 gfxid、polyid 分析
4. 登入器的 spr_action 產生
5. 資料庫中 spr_action 分析
6. 動畫圖檔 spr 基礎了解說明
7. 變身檔-格式與編碼基礎
8. 變身檔-附加物件的教學
9. 變身檔-武器指令的教學
10. 變身檔-魔法效果的教學
11. 變身檔-加速指令的教學
12. 變身檔-走路分析與修改
13. 變身檔-攻擊分析與修改
14. 變身檔-施法分析與修改
15. 變身檔-僵直/受傷分析與修改
16. 變身檔-撿取分析與修改
17. 變身檔-指向指令分析與修改
18. 變身檔-魔法方向性分析與修改
19. GM 指令使用（人物變身）

主要確認：
- 3.81C 實測中，未使用登入器自訂 morph 時 `list.spz` 才是實際生效來源；單改 `list.spr` 無效。
- 啟用登入器自訂 morph PAK 後，自訂檔未定義項目不一定 fallback 到 client 原始 `list.spz`；屬 source precedence 問題。
- `#5641 64=240` 證明 morph ID 與實際 sprite set/file prefix 可不同。
- `spr_action` 舊版模型包含 `spr_id / act_id / framecount / framerate`，server 用於 Move/Attack/Spell interval 類速度檢查。
- `105.clothes` 是額外圖層；`106.weapon` 顯示不同 client generation 可能採 baked-in 或 overlay 武器；`109.effect` 可拆 projectile / impact。
- action 可引用另一 morph entry，例如 `8=784`；因此 resolver 不應是單層 `gfxid -> filename`。
- 單一 SPR 能顯示只證明 decode path，不證明完整 morph/action/runtime mapping。

### B. 天堂私服：資源工具 / Browser / 補丁

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

新增 Resource Tool Matrix 證據：
- PakViewer Ver.3.0：`index-first + search/filter + on-demand preview`；可跨 Sprite/Text/Tile，支援動態圖/文字預覽。
- L1Viewer：檔名搜尋支援 literal/case-sensitive/regex/filter；另有文件內容全文檢索；TBT 可用 image-list；SPR 可播放、逐幀前進/後退，顯示 current/total frame。
- Lineage Icon：TBT Query / TBT Browse / SPR Query 分離；再次支持 Item Icon Browser 與 SPR Browser 應有不同 resolver/UX。
- SFDviewer：可同時開兩份 SFD，顯示每 frame X/Y 並做合成，支持 compositor 一級功能。
- TBT 工具：TBT 用於道具圖、魔法圖、人物狀態圖。
- 3.81C IDX/PAK：IDX 作 entry index，PAK 作實際資源容器；工具可由 entry 解出 SPR 多 frame。8.8 必須保留 source provenance，尤其處理 duplicate entry。
- 3.81C override folders 顯示 icon/sprite/surf/text/tile 是不同 logical resource class；8.8 只能把這當 loader/search-root 的研究框架，不沿用固定目錄名。

### C. 天堂私服：核心分析 / Debug

新增完整正文：
- 遊戲帳號分析/Debug (一)「L1J版」
- 遊戲帳號分析/Debug (二)「L1J版」
- 遊戲帳號分析/Debug (三)「L1J版」

抽取出的 server trace template：

`observable game action -> DB/config candidate -> SQL/table literal -> model/object fields -> Find Usages/caller -> clientpacket/service entry -> breakpoint -> evaluate predicate/owner state -> step -> outbound packet / DB mutation / visible result`

其中 Debug (二)/(三) 另外示範：
- 不必永久改設定，可在 debugger Variables 中暫改 runtime predicate 以進入特定 branch。
- 可用 Evaluate Expression 在執行 branch 前先確認條件值，再 Step Over 觀察結果。
- packet result code、Java path、line number、欄位名都只屬 L1J-3.80c 舊版證據，8.8 需重驗。

### D. Reverse engineering 方法層

已深讀/驗證：
- Cheat Engine 第九章 Step 8：多級 pointer / Find writes / Find accesses / pointer scan
- x64dbg 第五章：字串搜尋
- x64dbg 第三十四章：反調試流程 / API call / 行為 narrowing
- OllyDBG 第八章：由可見 UI/錯誤訊息推測 API，再追 call flow

固定 8.8 方法：
1. 先找可觀察事件，不先猜函式名。
2. 字串/API/xref 先做靜態 narrowing。
3. `what writes / what accesses` 找 producer/consumer。
4. heap address 只算 session evidence；追 module/static anchor -> pointer chain -> object field。
5. breakpoint 驗證 caller、參數、owner state、state transition。

## 已確認鎖定

- 天堂私服 category 最新頁至少 4 篇（Java DOM、BOSS 重生設定分析、XML 檔案清單說明、JAXB 基礎）為 `ADVANCED_LOCKED`。
- 多個工具文章下載/解壓密碼區塊為咖啡會員鎖定；若主要技術正文可讀，記 `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`。
- 尚未把任何僅見 FLAGSHIP 標題的內容當正文證據。

## 目前完成度

- 全站數量/分類基線：完成。
- 天堂私服 130 篇：已完成 category 結構與多頁索引抽查；morph/SPR/action、資源工具/補丁、核心 Debug 主鏈已深讀 30+ 篇級別，但「130 篇逐篇全文狀態表」仍未完成。
- x64dbg 36 / OllyDBG 16 / CE 10：方法層已開始，尚未逐篇完成。
- C / Java / MySQL / XML / Python/OpenCV / 排序演算法：分類數量與方向已建立，逐篇 cross-reference 待續。
- Archives 783 篇 title-level inventory：尚未逐 79 頁全部落表。

STATUS = IN_PROGRESS

## 最新進度檔

- `docs/JJ_RESEARCH_PROGRESS_20260907_1803.md`

## 下一批優先序

1. 天堂私服 `07.工具介紹/使用` 尚未讀：Pakext / PakViewe / PackViewer_beta2 / MTools / XML-SPZ-HTML crypto / Linskin 等，完成 Resource Tool Matrix。
2. 天堂私服 `05.核心分析/修改`：優先 NPC / Item / GM create / packet state，擴充 DB -> Java -> packet -> client 模板。
3. x64dbg 36 + OllyDBG 16：建立 `search entry -> breakpoint -> trace -> caller -> owner -> stable anchor` checklist。
4. CE 10：建立 pointer/structure/what-writes 與 Argus/Ghidra 對照表。
5. x86 / 排序演算法：只抽能直接支援 binary/resource/inventory 的內容。
6. XML/MySQL/Java/C：按 Lineage cross-reference 讀，不做無關語法重複閱讀。
7. Python/OpenCV：優先天堂私服 YOLO/GUI/threading/preview/vision 驗證。
8. 最後補齊 Archives 79 頁、783 篇 title-level inventory 與逐篇狀態。
