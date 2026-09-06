# Reviewer Feedback

最後更新：2026-09-07

STATUS = RECHECK

GOAL = 完成 8.8 主背包手動 reorder 路徑的 runtime 驗證，取得真正 reorder entry / refresh tail，為背包整理最小 PoC 建立可執行依據。

REVIEW BASIS =
- 依 `CODEX_STATUS.md`，`InventoryItemIcon+0x94/+0x98` 已被 runtime 反證為 idle layout POINT pair，不再作為 reorder producer。
- 下一個 live runtime Goal 必須在新 client session 重新驗證 singleton / heap / vector 位址與四個 execute anchor bytes；不得沿用舊 PID / heap VA。
- 本輪只允許沿主背包 `RenewalInventoryUI / InvWindow` 路徑，不回頭追 PromoteDollUI 或已排除的 icon raw-field watch。

EXECUTOR ACTION =
1. 啟動並登入 8.8 client 後，先用現有 AOB / bytes 重新驗證以下 execute anchors：
   - `0x00DFB6E0`
   - `0x00DFC6B0`
   - `0x00DE0E40`
   - `0x00DFA580`
2. 重新定位本 session 的 RenewalInventoryUI、InventoryItemGrid、inventory manager 與相關 source/order vectors；session-specific heap VA 不得沿用舊值。
3. 僅建立一次短時 execute capture，監看上述四個既定入口。
4. Capture ready 後才要求使用者做一次手動拖放；不要在尚未 ready 時要求任何遊戲操作。
5. 拖放前後同步保存並比較：
   - inventory manager source/order vector
   - persisted item-ID order（若存在）
   - layout-index mapping
   - grid icon vector
6. 直接判定：
   - `DE0E40` 是否為真實手動 reorder entry
   - `DFC6B0` 在拖放前/後的角色
   - `DFB6E0` 的事件/保存角色
   - `DFA580` 是否為 reorder 後 refresh/reconcile tail
7. 若任一候選命中，立即沿該真實 runtime 證據分析 caller/args/前後 state；不要擴大到全域掃描。
8. 若四個 anchor 全未命中，允許只擴大一層到其直接 caller/callee，不做 heap-wide / process-wide 無界搜尋。

SUCCESS CRITERIA =
- `reorder entry` 有真實 execute hit + this/args + 前後 order/vector 變化支持。
- `refresh tail` 有 execute/call-chain 證據。
- 能形成一個不靠猜位址的最小 PoC 呼叫方案。

STOP / USER ACTION =
- 只有在 capture 已 ready 且需要一次拖放時，回報 `NEED_USER_ACTION` 並明確指定唯一操作。
- 若只剩 patch / injection / runtime write 才能繼續，先停止並回報 Reviewer；未授權前不得執行。
- 若本輪得到重大新證據，可沿證據繼續，不因時間中斷；只有重複無進展或偏離 Goal 才停止。

RETURN FORMAT =
- `STATUS = SUCCESS | NEED_USER_ACTION | BLOCKED | RECHECK`
- `reorder entry =`
- `this/args =`
- `order/vector delta =`
- `DFC6B0 role =`
- `DFB6E0 role =`
- `refresh tail =`
- `runtime evidence =`
- `minimal PoC =`
- `next executable step =`

完成本輪後：更新 `CODEX_STATUS.md`；跨 Goal 可重用且證據充分的函式級結論同步更新 `CLIENT_UI_FUNCTION_MAP.md`，再 push GitHub。
