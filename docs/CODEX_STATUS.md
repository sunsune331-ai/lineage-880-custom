CURRENT GOAL = 將已驗收 Inventory Sort V1 完成為 external auto-apply persistent helper，並以 cold restart 驗證每次登入自動載入、取得新物品不自動排序、只有按 Sort_Btn 才整理
STATUS = SUCCESS
CONFIRMED = INVENTORY_SORT_PERSISTENT_V1 = COMPLETE；AUTO_APPLY_ON_LOGIN PASS；MANUAL_SORT_BUTTON PASS；AUTO_SORT_ON_ITEM_GAIN DISABLED；cold restart 中 helper 自動等待真正 game-ready LinLogin.bin、排除 HWND=0 child/transient process、完成 path/build/patch-site gate 與 hook read-back；使用者未手動執行 --mode install 即直接按整理成功；新取得物品不自動重排，再按整理後才重新排序
DISPROVEN = 第一個/最新 LinLogin.bin 不一定是真正遊戲 target；同一路徑可同時存在主遊戲與 MainWindowHandle=0 child/transient process；舊 launch 曾造成 NOT_INSTALLED 與 PROCESS_EXITED_HOOK_GONE，因此不得退回 first-PID/newest-PID 選擇；排序核心本身未失敗，manual install 已先證明 accepted V1 可正常工作
CHANGED = 新增 docs/INVENTORY_SORT_PERSISTENT_V1.md；更新 docs/AI_HANDOFF.md 與本狀態檔；本地 persistent helper 位於 LineageAIResourceToolkit outputs；未永久修改測試2 LinLogin.bin disk bytes，未修改 original own 8.8C client/resource/server/database
RUNTIME EVIDENCE = 最終 cold restart：PID 25952 從 HWND=0 → HWND=16843970 → WAITING_FOR_GAME_OBJECTS → GAME_READY，另一 PID 40988 HWND=0 被排除；STABLE_GAME_WINDOW_CONFIRMED 後 install；post_install_alive PASS、post_install_hook_readback PASS、process_path_gate PASS、build_bytes_gate PASS；使用者進遊戲後直接按整理成功，取得新物品不自動排序，再按整理才重排
BLOCKER = 無
RECHECK NEEDED = 若未來 client build 改變，重新驗證 module/build/patch-site bytes；若分類有誤，只修 concrete classifier rule；若 launch helper 再失敗，只查 process selection/wait/build gate/hook read-back，不重寫 accepted sorting core
NEXT EXECUTABLE STEP = Inventory Sort Persistent V1 已完成；等待使用者新 Goal。後續 Inventory Sort 先讀 INVENTORY_SORT_V1_CLOSEOUT.md、INVENTORY_SORT_PERSISTENT_V1.md、AI_HANDOFF.md；現行需求固定為登入自動載入、取得物品不自動整理、按 Sort_Btn 才整理
