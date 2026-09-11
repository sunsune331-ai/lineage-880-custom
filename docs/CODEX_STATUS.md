CURRENT GOAL = 完成 Inventory Sort FRIEND TEST RC1 首次安裝與第二次啟動驗收，確認 Tile 自動備份/吃檔、One-Click world-ready auto-install、整理功能與靜默成功提示均可交付朋友測試
STATUS = SUCCESS
CONFIRMED = FRIEND_TEST_RC1 = READY_TO_SEND_FRIEND；FIRST_INSTALL_TEST PASS；SECOND_LAUNCH PASS；ORIGINAL_TILE_DETECTED / BACKUP_CREATED / TILE_PATCH / SORT_BUTTON_VISIBLE / OTHER_TILE_PRESERVED 全 PASS；ONECLICK_LOGIN / CHARACTER_ENTRY / WORLD_READY / SORT_RUNTIME 全 PASS；新物品不自動排序、按 Sort_Btn 才重新排序；第二次啟動正確偵測 RESOURCE_ALREADY_INSTALLED、不覆寫第一份 backup、整理仍 PASS；成功提示窗已移除
DISPROVEN = RC1 不需要整包覆蓋朋友 Tile.idx/Tile.pak；較安全方案為 supported SHA pair gate + 玩家自己的 Tile + slot-preserving ReInventory.xml Sort_Btn patch；未知 Tile pair 必須 fail closed；首次安裝與第二次啟動都已在 disposable client 驗證
CHANGED = 新增 docs/INVENTORY_SORT_FRIEND_RC1.md；朋友版整合 Tile hash gate、immutable backup、transactional patch、pair atomicity、rollback(--restore-tile)、idempotent patched-state detection、使用說明書與成功靜默 UX；未修改 original own 8.8C
RUNTIME EVIDENCE = disposable client 首次安裝：Tile backup/patch PASS、Sort_Btn visible、One-Click login、character entry、WORLD_READY、runtime Sort PASS；新物品不自動排序、再次按整理才重排；第二次 cold launch 偵測 RESOURCE_ALREADY_INSTALLED 且 backup 不被覆寫，Sort 再次 PASS
BLOCKER = 無；RC1 已可傳給朋友測試
RECHECK NEEDED = 朋友端若出現 UNSUPPORTED_TILE_BUILD / WRONG_CLIENT_BUILD / PATCH_SITE_MISMATCH / 登入閃退 / 整理無反應，只收集最新 logs 並針對該 concrete compatibility blocker 處理；不得重寫已驗收 sorting core
NEXT EXECUTABLE STEP = 將 `天堂880_背包整理_RC1_朋友測試版.zip` 傳給朋友；ZIP SHA256 = 8F6C726DB7899DF7AACD67D1CB2EBE6E1B978A3D5E9F91CE4759A560B09A6A49。RC1 acceptance disposable client 可在確認保留 ZIP/hash/docs/log manifest 後刪除以回收容量
