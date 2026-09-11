# Inventory Sort Friend Test RC1

最後更新：2026-09-11

## 狀態

- `FRIEND_TEST_RC1 = READY_TO_SEND_FRIEND`
- `FIRST_INSTALL_TEST = PASS`
- `SECOND_LAUNCH_TEST = PASS`
- `ORIGINAL_8_8C_UNCHANGED = PASS`
- 所有首次安裝驗收均在 disposable client copy 進行；原始 own 8.8C Tile hash 維持不變。

## RC1 內容與行為

RC1 為朋友測試用懶人版，整合三層：

1. Tile slot-preserving Sort_Btn patch。
2. Tile backup / transactional install / rollback wrapper。
3. One-Click world-ready runtime Inventory Sort auto-install。

玩家功能語意固定：

- 登入流程自動載入 Inventory Sort runtime hook。
- 新取得物品不自動排序。
- 只有按背包 `Sort_Btn` 才重新排序。
- 排序順序：武器 → 防具 → 飾品 → 藥水 → 卷軸 → 材料 → 其他。

成功提示窗已移除；正常成功與 `RESOURCE_ALREADY_INSTALLED` / `ALREADY_INSTALLED` 為靜默成功，真正 fatal error 才提示。

## Tile installer safety

- `PAIR_HASH_GATE = PASS`
- `SUPPORTED_PAIR_COUNT = 1`
- `PATCHED_STATE_DETECTION = PASS`
- `BACKUP_IMPLEMENTED = PASS`
- `BACKUP_IMMUTABLE = PASS`
- `TRANSACTIONAL_WORKDIR = PASS`
- `PAIR_ATOMICITY = PASS`
- `ROLLBACK_IMPLEMENTED = PASS` (`--restore-tile`)
- `ONLY_REINVENTORY_CHANGED = PASS`
- `SORT_BUTTON_ACTIVE = PASS` (count = 1)
- `OTHER_TILE_CONTENT_PRESERVED = PASS`

Importer 使用玩家自己的 `Tile.idx` / `Tile.pak`，只對 `ReInventory.xml` 套用 slot-preserving Sort_Btn patch；未知 Tile pair SHA 必須 `UNSUPPORTED_TILE_BUILD` fail closed。

## Offline wrapper tests

- TEST A supported original pair → patch PASS。
- TEST B patched pair → idempotent PASS。
- TEST C unknown SHA pair → fail closed PASS。
- TEST D second-file replacement failure → rollback both PASS。
- TEST E restore backup → original hashes recovered PASS。

## Local first-install acceptance

在 disposable client copy 上驗證：

- `ORIGINAL_TILE_DETECTED = PASS`
- `BACKUP_CREATED = PASS`
- `TILE_PATCH = PASS`
- `SORT_BUTTON_VISIBLE = PASS`
- `OTHER_TILE_PRESERVED = PASS`
- `ONECLICK_LOGIN = PASS`
- `CHARACTER_ENTRY = PASS`
- `WORLD_READY = PASS`
- `SUCCESS_POPUP_ABSENT = PASS`
- `SORT_RUNTIME = PASS`
- `NEW_ITEM_NO_AUTOSORT = PASS`
- `MANUAL_RESORT = PASS`

第二次啟動驗證：

- `SECOND_LAUNCH = PASS`
- `RESOURCE_ALREADY_INSTALLED = PASS`
- `BACKUP_NOT_OVERWRITTEN = PASS`
- `SECOND_SORT_TEST = PASS`

## Runtime safety baseline

保留 One-Click world-ready v1.02 gate：

- real `LinLogin.bin` target
- ExecutablePath gate
- `MainWindowHandle != 0`
- responding / process alive
- transient child rejection
- RenewalInventory manager / UI / InventoryItemGrid sanity
- vector `begin <= end <= capacity`
- sane item count 1–512
- pointer/item-ID readability
- inventory fingerprint stable >= 8 sec / >= 8 samples
- PID change resets readiness
- build / patch-site bytes gate
- hook read-back / post-install alive

accepted sorting stub SHA256 remains:

`C792FFA6A87EF592D0A97B516812E61B41A4834C401978952BFF194FEC0D6130`

## RC1 package

ZIP：`天堂880_背包整理_RC1_朋友測試版.zip`

已記錄 ZIP SHA256：

`8F6C726DB7899DF7AACD67D1CB2EBE6E1B978A3D5E9F91CE4759A560B09A6A49`

README 與使用說明書均包含於 ZIP 根目錄。

## Cleanup

RC1 驗收完成後，僅為 acceptance 使用的 disposable client copy 可刪除以回收磁碟空間；刪除前應保留最終 ZIP、SHA256、README/VERSION、必要 logs/manifest 與 source/build manifest。原始 own 8.8C 與已驗證 Test2 baseline 不得誤刪。
