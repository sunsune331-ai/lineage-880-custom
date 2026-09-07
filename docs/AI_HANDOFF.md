# Reviewer ↔ Executor Handoff

最後更新：2026-09-07

## 固定角色

- Reviewer：提出新 Goal、驗收條件、已知證據、禁止事項與優先順序。
- Executor（Codex）：讀取交接文件後直接執行、修改與 runtime 驗證；每輪結束前主動保存狀態，不等待額外提醒。

## 每個 Goal 的固定流程

1. 完整讀取本文件。
2. 完整讀取 [CODEX_STATUS.md](CODEX_STATUS.md)。
3. 完整讀取最新 [REVIEWER_FEEDBACK.md](REVIEWER_FEEDBACK.md)；其內容為目前 Reviewer 指令來源。
4. 只讀取 Goal 所需的 [CLIENT_UI_FUNCTION_MAP.md](CLIENT_UI_FUNCTION_MAP.md) 區段；涉及既有結論修訂時讀取完整相關證據。
5. 依 Goal 直接執行、修改並做風險相稱的 runtime 驗證。
6. 每輪結束前覆寫 `CODEX_STATUS.md` 的九個固定欄位，包含失敗、反證與 blocker。
7. 只有跨 Goal 可重用且證據充分的知識才同步到本文件；函式級 VA/RVA/AOB、結構與 caller/callee 證據同步到 Function Map。
8. 每輪工作在停止前必須完成 commit + push；push 成功後才算該輪完成並已向 Reviewer 回報。

## 三份文件的責任邊界

| 文件 | 唯一責任 |
|---|---|
| `AI_HANDOFF.md` | Reviewer ↔ Executor 的長期契約、跨 Goal 決策與高層已確認知識 |
| `CODEX_STATUS.md` | 當前 Goal 的唯一即時狀態；每輪更新，不累積歷史日誌 |
| `CLIENT_UI_FUNCTION_MAP.md` | 8.8 client 的耐久函式/結構地圖與直接 evidence |

若文件內容衝突，以最新 Reviewer Goal 與最新直接 runtime evidence 為準；不得靜默覆蓋舊結論，必須在 `DISPROVEN` 與 Function Map 修訂記錄中說明。

## 強制完成 / 自動 push 契約

- Executor 每輪在準備停止前必須先更新 `docs/CODEX_STATUS.md`；有耐久新證據時同步更新 `docs/CLIENT_UI_FUNCTION_MAP.md`。
- 每輪結束必須自動執行 `git add` → `git commit` → `git push origin main`，不得等待使用者手動提醒。
- 本機分析完成但尚未 push，不算完成；只有 push 成功後才能回報該輪 `SUCCESS`、`NEED_USER_ACTION`、`BLOCKED` 或 `RECHECK` 已交付 Reviewer。
- commit message 應能辨識本輪結果，例如 `executor: offline inventory prep complete` 或 `executor: waiting for live inventory capture`。
- push 失敗時允許有限重試與一次安全的 `git pull --rebase` / 衝突檢查；禁止 force push。仍失敗時在 `CODEX_STATUS.md` 記錄 `STATUS = BLOCKED` 與錯誤原因，保留本機 commit 等待人工處理。
- heartbeat/polling 只允許用普通 PowerShell/git 檢查遠端 Reviewer 指令；沒有新 Reviewer commit 時不得啟動 Codex 模型。
- 偵測到新 Reviewer commit 後：`git pull` → 讀最新 `REVIEWER_FEEDBACK.md` → 執行 → 更新狀態文件 → commit → push → 結束該次 Executor 工作。

## 證據與 session 規則

- `CONFIRMED` 只放直接反組譯、RTTI/vtable、caller/callee、精確 data/execute breakpoint 或前後狀態一致支持的結論。
- `DISPROVEN` 保留被 runtime 證據推翻的假設，避免後續重走同一路徑。
- 靜態程式 VA 必須以目前 build 的 bytes/AOB 重驗；PID、heap object、singleton live value 與 vector buffer 不得跨 client session 沿用。
- data breakpoint 的 exception EIP 是 store 後位址；writer VA 必須反向解碼並驗證 effective address。
- 未命中、同值 idle writer、或前後 state 無變化，不得提升成 reorder/refresh 證據。
- 未經 Goal 授權，不 patch、不寫 client memory、不擴大到其他 UI；Tripwire 僅在 Reviewer 明確要求時使用。

## 目前跨 Goal handoff

- 主線固定為 `RenewalInventoryUI / InvWindow → source container → order producer → sort/reorder → refresh`。
- `PromoteDollUI != 主背包`；其共用 grid ABI 成果保留，但不可混入主背包角色判定。
- 已確認 `InventoryItemIcon+0x94/+0x98` 是會被 idle layout 持續重寫的 POINT/座標 pair；`0x004C32B0`/writer `0x004C32B8` 不是 reorder producer。
- 真正手動 reorder 仍應用一次精確 execute capture 驗證 `0x00DFB6E0 / 0x00DFC6B0` 與 `0x00DE0E40 / 0x00DFA580`，並同步比較 manager、persisted order、layout-index 與 grid icon vectors。

## 2026-09-07 長聊天封存與研究節奏

- 刪除舊聊天後，先讀 [`CHAT_ARCHIVE_20260907_1844.md`](CHAT_ARCHIVE_20260907_1844.md)；它是本輪聊天的去重索引，詳細證據仍以本檔、`CLIENT_UI_FUNCTION_MAP.md`、capture 文件與 JJ research 文件為準。
- J.J. 網站研究正式基線為 `88 / 783`、剩餘 `695`；重讀不增加 completed count。
- 後續網站研究只在使用者下達新 Goal 後開始，每批最多 10 篇，維持 article-level 完整紀錄；完成一批即更新 ledger/tracker/progress、commit、push、停止。
- 舊聊天或舊 handoff 中的「自動連續研究／一次處理剩餘文章」已失效，不得用來啟動下一批。
- 主 worktree 有使用者未提交修改；不得 reset、覆蓋或清理。必要時使用獨立 worktree。
