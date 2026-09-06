# Reviewer ↔ Executor Handoff

最後更新：2026-09-07

## 固定角色

- Reviewer：提出新 Goal、驗收條件、已知證據、禁止事項與優先順序。
- Executor（Codex）：讀取交接文件後直接執行、修改與 runtime 驗證；每輪結束前主動保存狀態，不等待額外提醒。

## 每個 Goal 的固定流程

1. 完整讀取本文件。
2. 完整讀取 [CODEX_STATUS.md](CODEX_STATUS.md)。
3. 只讀取 Goal 所需的 [CLIENT_UI_FUNCTION_MAP.md](CLIENT_UI_FUNCTION_MAP.md) 區段；涉及既有結論修訂時讀取完整相關證據。
4. 依 Goal 直接執行、修改並做風險相稱的 runtime 驗證。
5. 每輪結束前覆寫 `CODEX_STATUS.md` 的九個固定欄位，包含失敗、反證與 blocker。
6. 只有跨 Goal 可重用且證據充分的知識才同步到本文件；函式級 VA/RVA/AOB、結構與 caller/callee 證據同步到 Function Map。

## 三份文件的責任邊界

| 文件 | 唯一責任 |
|---|---|
| `AI_HANDOFF.md` | Reviewer ↔ Executor 的長期契約、跨 Goal 決策與高層已確認知識 |
| `CODEX_STATUS.md` | 當前 Goal 的唯一即時狀態；每輪更新，不累積歷史日誌 |
| `CLIENT_UI_FUNCTION_MAP.md` | 8.8 client 的耐久函式/結構地圖與直接 evidence |

若文件內容衝突，以最新 Reviewer Goal 與最新直接 runtime evidence 為準；不得靜默覆蓋舊結論，必須在 `DISPROVEN` 與 Function Map 修訂記錄中說明。

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

