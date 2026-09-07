# J.J.'s Blogs 全站研究追蹤（783 篇）

最後更新：2026-09-07

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
- `COFFEE_LOCKED_BY_READER`：頁面標示咖啡會員，但目前網頁 reader 無法執行 localStorage/前端解密，因此加密區塊未取得。
- `ADVANCED_LOCKED`：頁面顯示管理員/進階或更高等級才可讀；不以標題猜正文。
- `FLAGSHIP_LOCKED`：頁面明確顯示旗艦層級才可讀。

網站 `/support/` 已確認 2026 咖啡會員年度密碼是公開提供，且只適用咖啡會員區塊；進階/旗艦是另一套綁帳號與期限的機制。reader 本身不繼承瀏覽器 localStorage，因此目前沒有把未解密區塊誤標為 FULL。

## 本輪已完成的深讀範圍

### A. 天堂私服：變身 / SPR / action 主鏈

已逐頁開啟並閱讀正文（`PUBLIC_FULL`，以 3.81C/L1J-3.80c 為舊版證據）：

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
15. 變身檔-僵直分析與修改
16. 變身檔-撿取分析與修改
17. 變身檔-指向指令分析與修改

主要新確認：
- 3.81C 測試中，未使用登入器自訂變身檔時 `list.spz` 才是實際生效來源；單改 `list.spr` 沒效果。
- 啟用登入器自訂 morph PAK 後，未在自訂檔定義的部分不 fallback 到 client `list.spz`；這是「source precedence」問題，不只是檔案在哪裡。
- `#5641 64=240` 這種格式證明「變身 ID」與「實際 sprite set/file prefix」可不同。
- `spr_action` 來源是 morph/action 編碼，欄位模型包含 `spr_id / act_id / framecount / framerate`；舊版 server 用它做移動/攻擊速度異常檢查。
- `framecount` 可由動作編碼中各段 frame 數累加；沒有 framerate 指令時舊版預設值是 24。
- `105.clothes` 不是裝飾註記，而是額外圖層；例：Death Knight 本體 + 光刀，Ice Queen 本體 + 發光 + 風雪。
- `106.weapon` 證明不同 client generation 的武器合成策略不同：有的 sprite 已把武器畫進 body，有的靠 weapon 指令疊合。
- `109.effect` 更精確可理解成飛行呈現 + 命中效果，而非只限定「魔法」。
- `8=784` 這種 action 指向會直接引用另一個 morph entry 的同 action 編碼與圖檔，不只是 SPR filename alias；舊版還觀察到 forward reference 可能崩潰，需視為 parser/order hypothesis，不得直接套 8.8。

### B. 天堂私服：資源工具 / Browser 行為

已逐頁開啟：
- PakViewer Ver.3.0 工具 — `PUBLIC_FULL` + `COFFEE_RESOURCE_BLOCK`
- SFDviewer 工具(圖檔座標定位) — `PUBLIC_FULL` + `COFFEE_RESOURCE_BLOCK`
- TBT編譯器(PNG↔TBT) — `PUBLIC_FULL` + `COFFEE_RESOURCE_BLOCK`
- LineageSpr 工具 — 正文可讀，下載解壓密碼區為咖啡會員鎖定

對 Toolkit 最重要的新證據：
- PakViewer Ver.3.0 選任一 `*.idx` 後會載入可解析的整組 IDX；支援 All Sprite / Sprite..Sprite15 / Text / Tile、模糊搜尋、排序、右側即時 preview、縮放、圖片/動態圖/文字預覽。
- 這直接支持我們的 Resource Browser 應採 `index-first + on-demand preview`，而不是 `export-all-to-PNG-first`。
- SFDviewer 可同時開兩個 SFD 並做「合成」，且顯示每 frame X/Y；它證明舊版工具模型本來就把多 layer 對位當一級功能。
- TBT 工具明確把 TBT 用在道具圖、魔法圖、人物狀態圖，支持 Item Icon Browser 與 GFX/SPR Browser 分開 resolver。

### C. Reverse engineering 方法層

本輪另逐頁深讀/驗證：
- Cheat Engine 第九章 Step 8：多級 pointer / Find writes / Find accesses / pointer scan
- x64dbg 第五章：字串搜尋（含中文檢索外掛方向）
- x64dbg 第三十四章：一步一步觀察反調試流程、辨識 API call、用行為縮小範圍
- OllyDBG 第八章：從可見 UI/錯誤訊息推測 API（例如 GetDlgItemText）再追 call flow

抽取給 8.8 的方法：
1. 先找「可觀察事件」而不是先猜函式名稱。
2. 能從 UI 字串/API/xref 進入就先做靜態 narrowing。
3. 對動態資料用 `what writes / what accesses` 思維找 owner/producer。
4. 遇到多級 owner chain，不把單次 heap address 當穩定位址；解析 module/static anchor → pointer chain → object field。
5. 最後回到 runtime breakpoint 驗證 caller、參數與 state transition。

## 已確認的鎖定情況

- 天堂私服 category 最新頁至少 4 篇（Java DOM、BOSS 重生設定分析、XML 檔案清單說明、JAXB 基礎）在列表直接顯示「管理員或更高等級」；目前記為 `ADVANCED_LOCKED`，未取得正文。
- 多個工具文章的下載解壓密碼區塊顯示「☕ 咖啡會員或更高」；reader 可讀完整工具正文但不能解密該區，記為 `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`，不誤標 `COFFEE_FULL`。
- 尚未遇到可明確確認正文為 `FLAGSHIP_LOCKED` 的本輪目標頁；只有 `/support/` 說明存在旗艦層級。

## 目前對 783 篇的完成度

- 全站數量/分類基線：完成。
- 天堂私服 130 篇：已完成 category 結構與多頁索引抽查；變身/SPR/action/資源工具核心鏈已深讀 20+ 篇，但「130 篇逐篇全文狀態表」仍未完成。
- x64dbg 36 / OllyDBG 16 / CE 10：方法層已開始深讀，尚未逐篇完成。
- C / Java / MySQL / XML / Python/OpenCV / 排序演算法：分類數量與方向已建立，逐篇交叉索引待續。
- Archives 783 篇 title-level inventory：尚未逐 79 頁全部落表。

STATUS = IN_PROGRESS

## 下一批優先序

1. 天堂私服 `07.工具介紹/使用` 24 篇：把每個工具對應的格式、container、preview 能力、限制整理成 Resource Tool Matrix。
2. 天堂私服 `06.補丁介紹/說明` + `08.對話檔`：完成 IDX/PAK/Text/TBT/IMG/SPR/HTML 的 container/resource map。
3. 天堂私服 `05.核心分析/修改` 23 篇：抽取 DB → Java → packet → client 的追蹤模板。
4. x64dbg 36 + OllyDBG 16：建立 `search entry → breakpoint → trace → caller → owner → stable anchor` checklist。
5. CE 10：建立 pointer/structure/what-writes 與 Argus/Ghidra 對照表。
6. 排序演算法 7：只抽與 inventory reorder/data structure 有關的算法觀念，不把一般教學硬套遊戲。
7. XML/MySQL/Java/C：按 Lineage cross-reference 讀，不做無關語法重複閱讀。
8. Python/OpenCV：優先天堂私服 YOLO/GUI/threading/preview 管線與圖像辨識文章。
9. 最後補齊 Archives 79 頁的 783 篇 title-level inventory 與閱讀狀態。
