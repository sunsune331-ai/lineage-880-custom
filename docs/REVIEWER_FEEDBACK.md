# Reviewer Feedback

最後更新：2026-09-07

STATUS = RECHECK

GOAL = 在 8.8 client 尚未啟動期間，先完成主背包 reorder runtime 驗證所需的離線準備，讓使用者回家後只需啟動 client、登入、開背包並做一次拖放即可取得關鍵證據。

REVIEW BASIS =
- 目前 `CODEX_STATUS.md` 的唯一 live blocker 是 `LinLogin.bin` 未執行；不得因此讓 Executor 閒置等待。
- `InventoryItemIcon+0x94/+0x98` 已被 runtime 反證為 idle layout POINT pair，不再追該 raw field。
- live session-specific PID / heap / singleton value 不可沿用；但程式級 RVA/AOB/函式邊界、capture harness、離線 caller/callee 與 rebind 流程可先完成。
- 主線仍固定為 `RenewalInventoryUI / InvWindow → source/order producer → reorder → refresh`。

EXECUTOR ACTION（client offline 時立即執行） =
1. 不等待 client；先完成四個既定 execute anchor 的離線靜態 recheck 與 AOB 固化：
   - `0x00DFB6E0`
   - `0x00DFC6B0`
   - `0x00DE0E40`
   - `0x00DFA580`
   對每個 anchor 保存：RVA、函式範圍、預期 bytes、唯一 AOB、直接 caller/callee、已知 role、需要 runtime 補證的欄位。
2. 建立/完善一個一次性 runtime capture harness，目標是 client live 後一鍵完成：
   - attach `LinLogin.bin`
   - 驗證四 anchor bytes/AOB
   - 重新定位 RenewalInventoryUI / InventoryItemGrid / inventory manager
   - 重新定位 manager source/order vector、persisted item-ID order、layout-index mapping、grid icon vector
   - 僅掛四個既定 execute capture
   - 完成後輸出 `CAPTURE_READY`，不要提前要求使用者操作。
3. 建立 session rebind 邏輯：禁止硬編碼舊 PID / heap VA；只允許以 global slot、RTTI/vtable、AOB、已確認 object chain 重新解析 live object。
4. 對 `DE0E40 / DFC6B0 / DFB6E0 / DFA580` 做離線直接 caller/callee 追蹤與參數 ABI 整理，僅限一層；若能從靜態證據明確確認 source index / target index / persisted-order / refresh 參數，寫入 Function Map，否則標記 UNCONFIRMED。
5. 對已知 `DE1D40` 批次 consumer 做有限離線補強：只整理其 this / table layout / 180-entry 使用方式與直接 callers；不要再把它提升為主整理入口，除非有新的直接證據。
6. 把 runtime capture 前後差異輸出格式固定為同一份 JSON/Markdown summary，至少包含：
   - manager vector before/after
   - persisted order before/after
   - layout index before/after
   - grid icon vector before/after
   - 每個 execute hit 的 EIP / this / args / return / caller
7. 建立「使用者回家後唯一操作流程」文件/提示：
   - 開 8.8 client
   - 登入
   - 開主背包
   - 等 `CAPTURE_READY`
   - 只拖動一次指定物品
   其餘步驟由 Executor 自動完成。
8. client 未啟動時，不要持續用模型輪詢；允許輕量 heartbeat watcher，但 AI 工作應在本輪離線準備完成後停止，等待真正 live event。

LIVE PHASE（偵測到 client live 後自動接續） =
1. 重新驗證四 anchor bytes/AOB。
2. 重新定位本 session 的 owner/grid/manager/vectors。
3. 一鍵掛四個 execute capture，確認 `CAPTURE_READY` 後才要求使用者一次拖放。
4. 拖放前後同步比較四組 order/vector state。
5. 直接判定：
   - `DE0E40` 是否為真實手動 reorder entry
   - `DFC6B0` 在拖放前/後的角色
   - `DFB6E0` 的事件/保存角色
   - `DFA580` 是否為 reorder 後 refresh/reconcile tail
6. 若任一候選命中，沿真實 runtime 證據分析 this/args/caller/前後 state；不要擴大全域搜尋。

SUCCESS CRITERIA =
- 離線階段先交付可重複使用的 AOB + session rebind + capture harness，讓 live 階段不再臨時查工具/位址。
- live 階段 `reorder entry` 有真實 execute hit + this/args + 前後 order/vector 變化支持。
- `refresh tail` 有 execute/call-chain 證據。
- 能形成不靠猜位址的最小 PoC 呼叫方案。

STOP / USER ACTION =
- client offline：不得把等待本身當工作；完成離線準備後停在 `NEED_USER_ACTION`。
- 只有 capture 已 ready 且需要一次拖放時，才要求遊戲內操作。
- 若只剩 patch / injection / runtime write 才能繼續，先停止並回報 Reviewer；未授權前不得執行。
- 若得到重大新證據，可沿證據繼續；只有重複無進展或偏離 Goal 才停止。

RETURN FORMAT =
- `STATUS = SUCCESS | NEED_USER_ACTION | BLOCKED | RECHECK`
- `offline prep complete =`
- `anchor AOBs =`
- `session rebind =`
- `capture harness =`
- `static ABI findings =`
- `live blocker =`
- `user action after work =`
- `next executable step =`

## 強制同步 / 自動 push 規則

1. 每一輪 Executor 工作在準備停止前，必須先更新 `docs/CODEX_STATUS.md`；若有跨 Goal 可重用的新證據，同步更新 `docs/CLIENT_UI_FUNCTION_MAP.md`。
2. 狀態只允許在文件已寫妥後進入 `SUCCESS`、`NEED_USER_ACTION`、`BLOCKED` 或 `RECHECK`。
3. 每輪結束時必須自動執行 `git add` → `git commit` → `git push origin main`，不得等待使用者手動提醒。
4. commit message 必須能辨識本輪結果，例如 `executor: offline inventory prep complete`、`executor: waiting for live inventory capture`。
5. push 成功後才視為本輪已對 Reviewer 完成回報；本機做完但未 push 不算完成。
6. 若 push 失敗，可做有限自動重試與一次 `git pull --rebase`/衝突檢查；不得用 force push。仍失敗時把 `STATUS = BLOCKED`、錯誤原因寫入 `CODEX_STATUS.md`，保留本機 commit，等待使用者處理。
7. heartbeat/polling 只允許用普通 PowerShell/git 檢查 `REVIEWER_FEEDBACK.md` 的 remote commit；沒有新 commit 時不得啟動 Codex 模型。只有偵測到新 Reviewer commit 才啟動一次 Executor 工作。
8. 偵測到新 Reviewer commit 後先 `git pull`，完整讀取最新 `REVIEWER_FEEDBACK.md`，執行完成後依本節規則自動 push，再結束該次 Codex 執行。

完成本輪後：更新 `CODEX_STATUS.md`；跨 Goal 可重用且證據充分的函式級結論同步更新 `CLIENT_UI_FUNCTION_MAP.md`，並依上方「強制同步 / 自動 push 規則」自動 push GitHub。
