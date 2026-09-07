# JJ.全站精華 — research scoped instructions

此目錄是 J.J.'s Blogs 全站研究的 AI-first 知識庫。

## 啟動順序

每次新 Codex 工作／新聊天接手本研究時：
1. 讀 `research/task_control.json`。
2. 讀 `research/RESEARCH_TASK.md`。
3. 只讀最新必要狀態：`research/article_ledger.jsonl`、`research/checkpoint_ledger.jsonl`，以及需要時的 `docs/JJ_SITE_RESEARCH_TRACKER.md`。
4. 以 GitHub 最新可追溯 ledger 計算 baseline；不要硬編碼舊聊天中的 88/783 或其他數字。

## 核心規則

- 閱讀者 = 入庫者：每讀完一篇正文，當場建立一筆 article-level JSONL record。
- 一篇文章原則上只讀一次；只有 record 標記 `needs_reread=true` 或直接指令要求時才重讀。
- 網站內容一律先是 `research evidence`，不得直接標成 8.8 已驗證事實。
- 不因「舊、看似無用、版本未知、可能不適用 8.8」而省略技術資訊；保留並標記不確定性。
- 只有實際取得主要正文的文章可增加 completed count；空頁、分類頁、LOCKED、純下載／密碼區不得冒充 FULL。
- canonical URL 去重，但不同正文不得因 URL 相似而合併。
- 每 10 篇新 FULL article 建 checkpoint、commit、push，然後停止；`auto_continue=false` 時不得自行開下一批。
- 每 50 篇新 FULL article 才做一次高階 synthesis；批次摘要不能取代逐篇 record。
- context 壓縮或工作中斷後，一律從 GitHub ledger 恢復，不依賴聊天記憶。

## 禁止事項

- 不搬移、刪除、重新命名既有 `docs/JJ_*`、核心 handoff/status/function-map 文件。
- 不為格式轉換而重讀全部既有文章。
- 不修改 Client / Server / Java / Database 功能程式碼。
- 不 reset、force push、覆蓋使用者未提交成果。
- 不要求使用者複製長 prompt 作為正常工作流程；規則應留在 repo。