# Inventory Sort Persistent V1

最後更新：2026-09-11

## 最終狀態

- `INVENTORY_SORT_PERSISTENT_V1 = COMPLETE`
- `AUTO_APPLY_ON_LOGIN = PASS`
- `MANUAL_SORT_BUTTON = PASS`
- `AUTO_SORT_ON_ITEM_GAIN = DISABLED`
- `ORIGINAL_8_8C_UNCHANGED = PASS`
- 測試副本磁碟上的 `LinLogin.bin` 未被永久修改；目前持久化方式是 **external auto-apply helper**，每次登入流程中自動對真正 game process 安裝已驗收 runtime hook。

## 使用者最終驗收

使用者在 cold restart 後實測：

1. 完全關閉既有 client / LinLogin.bin。
2. 執行 persistent helper `--mode launch`。
3. helper 先等待真正 game process，不對 launcher / transient child 直接宣告成功。
4. 使用者按「開始遊戲」。
5. helper 自動選到真正有主遊戲視窗的 `LinLogin.bin`，完成 path/build/bytes gate 與 hook read-back。
6. 進遊戲後 **未手動執行 `--mode install`**。
7. 直接按背包「整理」按鈕，七分類排序正常。
8. 新取得物品時背包 **不會自動重排**。
9. 再按「整理」後，新物品才依規則重新排序。

因此需求正式固定為：

```text
每次登入 / 啟動 client
→ helper 自動安裝 Inventory Sort V1 hook

平常取得新物品
→ 不自動排序

使用者按 Sort_Btn
→ 才對當下整個背包重新排序
```

## 接受的排序規則

大類順序：

1. 武器
2. 防具
3. 飾品
4. 藥水
5. 卷軸
6. 材料
7. 其他

同分類：

- `item_id` ascending
- stable tie-break = original order

分類器仍沿用 accepted V1：

`CATEGORY_METHOD = USE_TYPE_PLUS_NAME_FALLBACK`

這是已實用驗收的 heuristic，不是 canonical item-category model。已知 `use_type=9` 有歧義，不得全部直接判為 SCROLL；未來若有誤分類，只修 concrete classifier rule。

## Helper

本機 helper：

`C:\架設功具\LineageAIResourceToolkit\outputs\inventory_sort_v1_persistent_helper\inventory_sort_v1_persistent.py`

主要模式：

- `--mode launch`：啟動 / 等待真正 game process / 自動安裝 V1 hook。
- `--mode install`：對已執行的正確 game process 手動安裝；已驗證 PASS，保留作 bounded recovery / diagnostic path。
- `--mode status`：唯讀檢查目前 hook 狀態。

accepted sorting stub SHA256：

`C792FFA6A87EF592D0A97B516812E61B41A4834C401978952BFF194FEC0D6130`

不得因後續修 launch/wait 流程而無故重寫排序核心。

## Confirmed runtime hook data

沿用已驗收 V1：

- manager source = `manager+0x54 vector<Item*>`
- item ID = `source_item+0x08`
- use_type = `source_item+0x18`
- item name = CP950 inline `source_item+0x2B`
- patch wrapper = `0x00DE2280`
- native refresh = `0x00DFAB70`
- refresh targets = `0x00DFA580 / 0x00DFA4D0`

## Persistent launch blocker 與修正

### 第一個失敗：launch 太早完成

最初 `--mode launch` 在真正 game process 尚未完成前結束，導致登入後：

- Sort_Btn 存在；
- 按整理無反應；
- `--mode status` = `NOT_INSTALLED`。

manual `--mode install` 對已執行遊戲安裝後：

- `status = INSTALLED`
- `process_path_gate = PASS`
- `build_bytes_gate = PASS`
- `hook_readback = PASS`
- 使用者按整理立即成功。

因此排序核心不是 blocker；問題只在 launch timing / target selection。

### 第二個失敗：裝到 transient / 舊 PID

後續 cold launch 曾出現：

`status = PROCESS_EXITED_HOOK_GONE`

manifest 指向已退出 PID。實際同時存在多個同路徑 `LinLogin.bin`，例如一輪觀察：

- PID 16096：`MainWindowHandle != 0`，真正遊戲主視窗。
- PID 42284：`MainWindowHandle = 0`，不可作 Sort hook target。

因此禁止再用「第一個 LinLogin.bin」或「最新 PID」作 target。

### 最終 target-process gate

`--mode launch` 現在必須：

1. process name = `LinLogin.bin`
2. `ExecutablePath` 完全位於測試 client：
   `C:\架設功具\天堂(Lineage 8.8C)測試2\`
3. `MainWindowHandle != 0`
4. process responding / still alive
5. game-ready objects gate 通過
6. module / build / patch-site bytes gate PASS
7. candidate 穩定存在 bounded 2 秒後才 install
8. 同時有 HWND!=0 與 HWND=0 candidates 時，選真正有主遊戲視窗者
9. 安裝後再次確認 target PID 仍存活，hook read-back PASS；若 target 消失，不得宣告 SUCCESS，回到 wait/replacement flow

不允許自動 kill `MainWindowHandle=0` process；只是不選它當 target。

## 最終 cold restart evidence

成功輪次輸出：

```text
WAITING_FOR_GAME
→ candidate PID 25952 initially HWND=0
→ PID 25952 HWND=16843970 but WAITING_FOR_GAME_OBJECTS
→ additional PID 40988 HWND=0 被排除
→ PID 25952 GAME_READY
→ STABLE_GAME_WINDOW_CONFIRMED
→ install
```

最終安裝結果：

- `game_pid = 25952`
- `main_window_handle = 16843970`
- `post_install_alive = PASS`
- `post_install_hook_readback = PASS`
- `status = INSTALLED`
- `process_path_gate = PASS`
- `build_bytes_gate = PASS`
- module base = `0x00400000`
- wrapper address = `0x00DE2280`
- refresh address = `0x00DFAB70`

該輪使用者未執行 manual install，直接進遊戲按整理即成功，因此 `AUTO_APPLY_ON_LOGIN = PASS`。

## Safety / scope

原始 own 8.8C：

`C:\架設功具\天堂(Lineage 8.8C)`

維持 strict read-only / unchanged。

測試副本：

`C:\架設功具\天堂(Lineage 8.8C)測試2`

目前 persistent V1 不需要永久改寫其 `LinLogin.bin` disk bytes；helper 每次啟動時動態安裝 runtime hook。

後續除非使用者另開新 Goal，**不要改成自動取得物品就排序**，也不要為了「更永久」而直接改 binary。現行 external auto-apply helper 已符合使用者接受需求。

## 後續維護規則

- 此版本視為日常可用 baseline，不再重查 Sort 根因。
- 若分類錯誤：只修 specific classifier rule，做 preview → bounded runtime test。
- 若 launch helper 再失敗：只查 process selection / wait / build gate / hook read-back，不動 accepted sorting stub。
- 若 client build 改變：必須重新驗證 patch-site bytes / module layout，不得盲套舊 VA。
- 若未來要包裝成更方便的一鍵啟動器，可在 helper 外層加 shortcut / launcher wrapper；功能語意仍維持「登入自動載入、取得物品不自動排序、按整理才排序」。
