# J.J.'s Blogs 全站研究追蹤（783 篇）

最後更新：2026-09-07

## 目標

把 J.J.'s Blogs 當成一個完整技術語料庫，而不是只挑「天堂私服」單篇文章。目的不是把舊版 3.8/3.81C 結論硬套到 8.8，而是抽取：

- 研究流程
- 逆向方法
- Server ↔ Client 資料流追法
- DB / Java / XML / packet / resource 的對照方式
- x86 / debugger / memory / breakpoint 方法
- IDX/PAK/SPR/TBT/IMG/list/morph/action 等資源觀念
- 可重複使用的工具操作與驗證策略

所有版本相依結論都標記為「舊版證據」，8.8 必須另外驗證。

## 全站基線

依網站目前 Archives / Categories：

- Posts：783
- Archives：79 頁（每頁約 10 篇）
- Categories：86
- Tags：網站頁面顯示值可能因快取時間不同而有差異，僅作索引參考

## 研究層級

### A. 全 783 篇：索引層

每篇至少記錄：
- 標題
- 日期
- category/tag
- 是否可讀
- 是否與 8.8 專案相關
- 關聯主題（Server / Client / DB / Reverse / Resource / Tool / Build / Network / UI / Vision 等）

### B. 高關聯文章：全文深讀

優先群：

1. 天堂私服（目前 category 顯示 130 篇）
2. x64dbg（36）
3. OllyDBG（16）
4. Cheat Engine（10）
5. x86 組合語言（3）
6. XML（20）
7. C（41）
8. Java（38）
9. MySQL（30）
10. Tools / Visual Studio / IntelliJ / Eclipse / Debug / Reverse 相關文章
11. Python/OpenCV 中與天堂、資源預覽、影像辨識、工具 GUI 有關的文章

注意：category 有重疊，以上數量不能直接相加成唯一文章數。

### C. 低關聯文章：方法抽取

例如 Spring Boot、Docker、Kafka、Redis、Go 等，不直接套入天堂 8.8；但若文章包含通用的 debug、profiling、binary/data parsing、build/automation、tooling 方法，則抽取方法到研究地圖。

## 目前已深讀 / 已建立結論的主題

- 天堂私服總目錄與章節架構
- 客戶端 / 登入器 / 變身檔關係
- gfxid / polyid / morph ID / sprite set ID 分層
- DB 中 gfxid / polyid 用途
- spr_action 資料模型
- SPR frame / SFD offset / timing 概念
- 變身檔格式與動作語義
- 附加物件 / clothes / weapon / effect 疊圖概念
- walk / attack / damage / spell / get 等 action 概念
- IDX / PAK / TBT / IMG / SPR / Text resource 分類
- PakViewer / L1Viewer / LineageSpr 等工具方向
- x64dbg / OllyDBG 系列的 breakpoint / trace / 反匯編分析方法（已開始建立方法層，不把破解案例本身當專案結論）

詳細 Lineage 導航已另存：`docs/JJ_LINEAGE_RESEARCH_MAP.md`。

## 未完成工作

1. 783 篇 archive title-level inventory 尚未逐頁完整寫入索引。
2. 天堂私服 130 篇需逐篇標成：FULL / PARTIAL / LOCKED / IRRELEVANT / 8.8-RECHECK。
3. x64dbg 36 篇需抽取 debugger 操作方法與適用情境。
4. OllyDBG 16 篇需抽取 message breakpoint / run trace / call-flow 方法。
5. Cheat Engine 10 篇需抽取 pointer / scan / what-writes / structure 方法，與 Argus 能力對照。
6. x86 / C / XML / Java / MySQL 中與 client/server/resource 研究有關內容需建立 cross-reference。
7. 所有「咖啡會員 / 管理員」加密文章：只有實際可取得正文時才標 FULL；只看到標題或鎖定提示時標 LOCKED，不臆測內容。

## 8.8 固定使用規則

- J.J. 文章回答「方向 / 流程 / 常見資料結構 / 驗證方法」。
- 8.8 自己的 Ghidra / Argus / runtime evidence 回答「本版本實際位置 / ABI / owner / caller / state」。
- 兩者衝突時，以 8.8 直接 runtime evidence 為準。
- 文章提供的方法若能減少盲搜，優先轉成可執行 checklist / AOB / capture plan / resource resolver。

## 狀態

STATUS = IN_PROGRESS
NEXT = 完成天堂私服 130 篇逐篇索引與深讀分類，再擴到 x64dbg / OllyDBG / Cheat Engine / x86 / C / XML / Java / MySQL，最後完成其餘 Archives 全站索引。
