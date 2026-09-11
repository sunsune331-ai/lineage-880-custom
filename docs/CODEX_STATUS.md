CURRENT GOAL = 驗證 Inventory Sort One-Click RC；確認 world-ready auto-install 與排序功能正常，成功提示窗延後到正式對外懶人包再處理
STATUS = SUCCESS
CONFIRMED = ONECLICK_RC_FUNCTIONAL = PASS；新版 world-ready gate 解決人物選擇→世界載入期間過早安裝造成的閃退；使用者已確認進入世界後整理正常、取得新物品不自動排序、按 Sort_Btn 才重新排序；accepted sorting stub / classifier / process-selection semantics 保持不變
DISPROVEN = 只靠 game window / GAME_READY 不足以保證可安全安裝 hook；人物選擇→世界載入轉換期間仍可能 crash，因此正式 auto-install 必須等待 manager/UI/grid/vector 與 inventory fingerprint 穩定後才進 INSTALL_BEGIN
CHANGED = 更新 docs/INVENTORY_SORT_PERSISTENT_V1.md 與本狀態檔，記錄 One-Click RC v1.02 world-ready gate 與使用者驗收結果；成功安裝提示窗不在目前功能性 RC 修改範圍
RUNTIME EVIDENCE = One-Click RC v1.02 使用者實測：點人物不閃退；WORLD_READY 後 auto-install；背包整理功能正常；新取得物品不自動重排；再按整理才重排
BLOCKER = 無功能性 blocker；僅有成功後「整理功能已開啟」提示視窗為 UX polish，使用者決定延後到正式 public release / 懶人包階段再移除
RECHECK NEEDED = 對外發佈前：移除成功/ALREADY_INSTALLED 提示窗（成功靜默、fatal error 才提示）、整理乾淨 release package、README/VERSION/SHA256；另驗證 IP 更新只替換 LinLauncher 時功能仍可正常工作
NEXT EXECUTABLE STEP = 目前停止功能修改；One-Click RC 作為已驗收 baseline。等使用者啟動「正式對外懶人包」Goal 時，只做 release UX/packaging polish，不重做 Sort core / classifier / world-ready gate
