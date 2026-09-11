# Reviewer ↔ Executor Handoff

最後更新：2026-09-11

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

## 實作節奏規則（2026-09-11）

當一個 hypothesis 已有足夠證據支持 bounded、reversible PoC 時，**不要等待整套 reverse engineering 全部完成才實作**。

固定流程：

`hypothesis → minimum validation → small reversible implementation → runtime test`

- PASS：擴大一小步。
- FAIL：只調查該次 FAIL 暴露的 blocker，修正後再測。
- 只有當實驗無法做到 bounded / reversible，或證據不足以保護 client/data 時，才繼續擴大研究。

Inventory Sort V1 是標準 reference example：`2-item swap → first-5 item-ID sort → full item-ID sort → seven-category sort → persistent helper cold restart`。

## 目前跨 Goal handoff

- 主線固定為 `RenewalInventoryUI / InvWindow → source container → order producer → sort/reorder → refresh`。
- `PromoteDollUI != 主背包`；其共用 grid ABI 成果保留，但不可混入主背包角色判定。
- 已確認 `InventoryItemIcon+0x94/+0x98` 是會被 idle layout 持續重寫的 POINT/座標 pair；`0x004C32B0`/writer `0x004C32B8` 不是 reorder producer。
- 真正手動 reorder 仍可用 `0x00DFB6E0 / 0x00DFC6B0` 與 `0x00DE0E40 / 0x00DFA580` 追 persistence / manual-move semantics，但 **Inventory Sort V1 已不再需要先完成這條 capture 才能繼續**。

## Inventory Sort V1 — ACCEPTED baseline（2026-09-11）

runtime / classifier closeout：[`INVENTORY_SORT_V1_CLOSEOUT.md`](INVENTORY_SORT_V1_CLOSEOUT.md)。

persistent completion：[`INVENTORY_SORT_PERSISTENT_V1.md`](INVENTORY_SORT_PERSISTENT_V1.md)。

後續 Inventory Sort Goal 必須先讀這兩份檔案，不得從零重查。

已由使用者實測確認：

- XML `Sort_Btn` restore：PASS；slot-preserving PAK importer 可被 own 8.8 client 正常載入。
- 8.8 Sort binding / handler 仍存在；handler = `0x00DF9930`。
- manager source order = `manager+0x54 vector<Item*>`。
- `source_item+0x08` = item ID；`source_item+0x18` = use_type（部分分類訊號）。
- runtime patch point = `0x00DE2280`；native refresh = `0x00DFAB70 → 0x00DFA580 / 0x00DFA4D0`。
- 2-item swap = PASS。
- first-5 stable item-ID sort = PASS。
- full inventory item-ID sort = PASS。
- hybrid 7-category sort = `ACCEPTED_BY_USER`。

目前接受排序：

`武器 → 防具 → 飾品 → 藥水 → 卷軸 → 材料 → 其他`

同類內：item ID ascending + original-order stable tie-break。

分類器目前是 `use_type + bounded item-name fallback` heuristic，不得宣稱為完整 canonical category model。已知 `use_type=9` 有歧義，不能全部直接判為 SCROLL。

重要根因：8.8 原 Sort path 保留 callback / handler / refresh，但缺 actual order-producing step；外部 8.5M working reference 只用於 differential，不是 own 8.8 implementation authority。

### Inventory Sort Persistent V1 — COMPLETE

使用者已完成 cold restart 最終驗收：

- `AUTO_APPLY_ON_LOGIN = PASS`
- `MANUAL_SORT_BUTTON = PASS`
- `AUTO_SORT_ON_ITEM_GAIN = DISABLED`
- `INVENTORY_SORT_PERSISTENT_V1 = COMPLETE`

persistent implementation 採 external auto-apply helper；測試副本 `LinLogin.bin` disk bytes 不需要永久改寫。helper 每次啟動後等待真正 game-ready `LinLogin.bin`，以測試2 ExecutablePath、`MainWindowHandle != 0`、alive/responding、game objects、module/build/patch-site bytes 與 bounded stability gate 選定 target，再安裝 accepted V1 hook並 read-back 驗證。

曾反證「第一個 / 最新 LinLogin.bin 就是正確 target」：同路徑可同時存在主遊戲與 HWND=0 child/transient process。後續不得退回第一 PID / newest PID 選擇法。

最終 cold restart 成功輪次選定 PID 25952（MainWindowHandle 16843970），另一 PID 40988 因 HWND=0 被排除；`post_install_alive`、hook read-back、path gate、build bytes gate 均 PASS。使用者未手動執行 `--mode install` 即直接按整理成功；取得新物品時不自動重排，再按整理後才重新排序。

原始 own 8.8C `C:\架設功具\天堂(Lineage 8.8C)` 維持 strict read-only / unchanged。現行 persistent helper 已符合使用者需求；除非另開 Goal，不要改成 item-gain 自動排序，也不要為「更永久」而直接 patch disk binary。

## 2026-09-07 長聊天封存與研究節奏

- 刪除舊聊天後，先讀 [`CHAT_ARCHIVE_20260907_1844.md`](CHAT_ARCHIVE_20260907_1844.md)；它是本輪聊天的去重索引，詳細證據仍以本檔、`CLIENT_UI_FUNCTION_MAP.md`、capture 文件與 JJ research 文件為準。
- J.J. 網站研究正式基線為 `88 / 783`、剩餘 `695`；重讀不增加 completed count。
- 後續網站研究只在使用者下達新 Goal 後開始，每批最多 10 篇，維持 article-level 完整紀錄；完成一批即更新 ledger/tracker/progress、commit、push、停止。
- 舊聊天或舊 handoff 中的「自動連續研究／一次處理剩餘文章」已失效，不得用來啟動下一批。
- 主 worktree 有使用者未提交修改；不得 reset、覆蓋或清理。必要時使用獨立 worktree。
