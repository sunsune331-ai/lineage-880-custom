CURRENT GOAL = 建立 Reviewer ↔ Executor 連動流程，固定使用 AI_HANDOFF / CODEX_STATUS / CLIENT_UI_FUNCTION_MAP
STATUS = COMPLETE
CONFIRMED = 三份文件責任與每 Goal 的 read → execute/modify → runtime verify → status/function-map sync 流程已落地；InventoryItemIcon+0x94/+0x98 為 idle layout POINT pair
DISPROVEN = InventoryItemIcon+0x94 是手動排列的持久 order/slot producer；0x004C32B0/0x004C32B8 是 reorder writer
CHANGED = 新建 docs/AI_HANDOFF.md、docs/CODEX_STATUS.md；更新 docs/CLIENT_UI_FUNCTION_MAP.md 的文件入口、runtime 修訂與維護規則
RUNTIME EVIDENCE = 兩個 icon 的 +0x94 watch 共 225 次命中，全部 trap EIP=0x004C32BA；真實 store=0x004C32B8 mov [ecx],eax；0x004C32B0 複製 +0x94/+0x98 兩 DWORD；拖放回報前後兩 icon 0x250 bytes、grid+0x190、grid+0x1BC、manager+0x54 均無變化
BLOCKER = 目前 LinLogin.bin 未執行；下一個 live runtime Goal 需 client 重新啟動並登入
RECHECK NEEDED = 新 session 必須重驗 singleton/heap/vector 位址與四個 execute anchor bytes；不得沿用 PID 24660 或舊 heap VA
NEXT EXECUTABLE STEP = client live 後 attach，對 0x00DFB6E0、0x00DFC6B0、0x00DE0E40、0x00DFA580 做一次手動拖放 execute capture並保存四組 order/vector 前後差異
