# Codex persistent routing

本檔只做任務入口，不保存大量專案知識。

## 一般專案工作

先讀：
- `docs/AI_HANDOFF.md`
- `docs/CODEX_STATUS.md`
- `docs/REVIEWER_FEEDBACK.md`

依 Goal 再讀必要文件；不要把整個 `docs/` 一次載入上下文。

## JJ.全站精華 / J.J.'s Blogs 網站研究

如果任務名稱、使用者要求或目前工作涉及「JJ.全站精華」、「J.J.'s Blogs」、「783 篇網站研究」、`research/` 或 `JJ_*` 研究文件：

1. 必須先讀 `research/AGENTS.md`。
2. 再讀 `research/task_control.json` 與 `research/RESEARCH_TASK.md`。
3. 以 GitHub `research/*.jsonl` 為長期狀態來源，不依賴舊聊天記憶。
4. 不要求使用者在 ChatGPT / Codex 之間搬運長指令；工作規則與 checkpoint 應直接持久化到 repo。

直接使用者指令優先於本檔；若研究規則與一般 handoff 衝突，以直接使用者指令與 `research/AGENTS.md` 的研究專用規則為準，但不得破壞既有 Client / Server / runtime 工作成果。