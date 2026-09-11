# Inventory Sort V1 Closeout

最後更新：2026-09-11

## 狀態

- `INVENTORY_SORT_V1_STATUS = ACCEPTED_BY_USER`
- 這一版已由使用者在 own 8.8C 測試副本實際驗收可接受。
- 原始 own 8.8C client 保持未修改；本輪成功版本仍是 **runtime PoC**，client restart 後 runtime hook 會消失。
- 永久化尚未執行。

## 使用者接受的排序規則

排序大類固定為：

1. 武器
2. 防具
3. 飾品
4. 藥水
5. 卷軸
6. 材料
7. 其他

同分類內目前使用：

- `item_id` ascending
- stable tie-break = original order

## CONFIRMED — own 8.8 runtime / structure

### UI 與 callback

- 8.8C `ReInventory.xml` 原始 `Sort_Btn` 文字仍存在，但被 XML comment 包住；active DOM 中不存在。
- slot-preserving PAK importer 恢復 `Sort_Btn` 後，client 可正常載入，按鈕位置正確。
- `MouseEvent="Sort"` binding 存在。
- Sort handler = `0x00DF9930`。
- event code `0x202` 是共同 click-event guard；Sort / Promote / SealButton / CloseWindow 都使用。
- `RenewalInventoryUI +0x15C` 為有效 `InventoryItemGrid*`，guard 通過。

### Sort / refresh path

8.8C 已確認鏈：

```text
Sort_Btn
→ MouseEvent="Sort"
→ binder 0x00FC8E10
→ Sort handler 0x00DF9930
→ manager getter 0x005EE200
→ wrapper 0x00DE2280
→ refresh dispatcher 0x00DFAB70
→ 0x00DFA580 / 0x00DFA4D0
```

`0x00DFAB70` 是 refresh dispatcher；原始 8.8C Sort path 保留 refresh，但沒有實際產生新順序的 producer call。

### Inventory source / item metadata

- RenewalInventory manager global slot = `0x01952748`
- manager source order = `manager+0x54` `vector<Item*>`
- `source_item+0x08` = item ID（已以 live vector 對照驗證）
- `source_item+0x18` = `use_type`（可提供部分分類訊號）
- 寫入 manager source pointer order、再走 8.8C 原生 refresh，可直接改變主背包視覺順序。

### Runtime PoC 驗證序列

1. **2-item swap**：PASS
   - 交換 manager source 前兩個有效 item pointer。
   - 使用者實測前兩格確實互換。

2. **first-5 stable sort by item ID**：PASS
   - 前 5 個有效 item 依 `source_item+0x08` stable ascending。
   - 使用者實測成功。

3. **full inventory stable sort by item ID**：PASS
   - 當次 inventory 共 67 個有效 entries。
   - 全背包可安全重排；pointer count / multiset / begin-end-capacity invariants 保持。
   - 使用者實測確實有排序，但 item-ID-only 規則不符合期望分類順序。

4. **hybrid seven-category sort**：USER ACCEPTED
   - 排序：武器 → 防具 → 飾品 → 藥水 → 卷軸 → 材料 → 其他。
   - 同類內 item ID ascending。
   - 使用者確認此版可以先採用。

## USER_ACCEPTED — V1 classifier

`CATEGORY_METHOD = USE_TYPE_PLUS_NAME_FALLBACK`

目前 classifier 是可用的 pragmatic heuristic，不是已完整證明的 canonical item-category system。

### 已直接觀察的 use_type 線索

- WEAPON：`use_type = 1`
- ARMOR：目前樣本觀察 `2 / 19 / 20 / 21 / 22`
- ACCESSORY：目前樣本觀察 `43`
- POTION / SCROLL / MATERIAL / OTHER：存在多值與重疊，不能只靠單一 `use_type` 完整分類。

### Ambiguous / fallback 規則

- `use_type=9` 具有歧義，不能直接全部判為 SCROLL。
- 名稱符合卷軸／傳送等規則時才可落入 SCROLL；例如「商隊排賣」必須維持 OTHER。
- 部分藥水、卷軸、材料與其他使用 bounded item-name fallback。
- 若未來新增物品出現誤分類，只修該 concrete rule，不回頭重做整套 category RE。

## 外部 8.5M working reference 的角色

外部 working reference：`C:\架設功具\永恆8.5M天堂0825`，actual game process 曾解析為 `TW1610202200.bin`。

僅用來回答 working Sort path 的 differential，不是 own 8.8 implementation authority。

8.5M working path 找到：

```text
Sort wrapper 0x00A032E0
→ source manager+0x54
→ destination/order manager+0x33C
→ producer 0x009F9B40
→ refresh 0x00A19460
```

對照 8.8C：`0x00DE2280` 直接走 refresh，沒有 corresponding producer call。

因此根因分類：

`ROOT_CAUSE = SORT_PRODUCER_REMOVED_FROM_PATH`

但沒有直接移植 8.5M machine code；最後使用 own 8.8 manager source ordering + native refresh 完成 PoC。

## DISPROVEN / FAILED — 必須保留，避免重踩

1. **XML-only restore 不等於功能恢復**
   - XML 可讓按鈕出現，但原 8.8 Sort path 只 refresh，不會自動產生新 order。

2. **8.8 Sort binding / handler 並未被刪除**
   - binding、handler、event guard、grid guard 都存在。

3. **surface guard 不是 blocker**
   - `0x202` common click guard 正常；`InventoryItemGrid*` guard 正常。

4. **不要把 `0x00DFAB70` 說成 reorder core**
   - 它是 refresh dispatcher。

5. **manager source item layout != UI/grid item layout**
   - 不得套用 UI/grid item offset 到 manager source Item*。

6. **use_type alone 不能完整七分類**
   - 必須允許 fallback；已知 `use_type=9` 有歧義。

7. **舊 execute-HWBP capture design 禁止重用**
   - 舊 helper 在 instruction breakpoint 命中後未妥善 RF/one-shot，造成同 EIP 大量 #DB retrigger livelock（曾產生約 58 萬次重複命中）並造成 client 終止。
   - 後續若真需要 debugger capture，必須使用 one-shot、RF、graceful detach、bounded logging 等安全設計。

8. **PAK delete/add replacement 不相容**
   - 舊方式把 `ReInventory.xml` entry append 到 index 尾端，client loader 報無法讀取。
   - slot-preserving importer 保留原 slot / offset / entry order，真實 8.8 client 才可正常載入。

9. **不要把未連結 named sorting engine 當成主背包已用功能**
   - 仍需 runtime/path evidence 才能提升。

## Pak importer V1 — 已驗證必要背景

`ReInventory.xml` slot-preserving replacement 驗證：

- original slot = 4767
- derived slot = 4767
- offset preserved = YES
- entry count preserved = YES
- entry order preserved = YES
- 僅 slot 4767 stored-size 欄位按 payload 大小改變
- roundtrip payload = PASS
- XML parse = PASS
- original own 8.8C unchanged = PASS

這條工具路線是恢復 Sort_Btn UI 的基礎，後續不得退回 delete/add archive mutation。

## Durable workflow rule — 2026-09-11

本 Inventory Sort 工作正式確立以下專案執行節奏：

> **當一個 hypothesis 已有足夠證據支持 bounded、reversible PoC，就立刻小步實作；不要等待整套 reverse engineering 全部完成。**

固定流程：

```text
hypothesis
→ minimum validation
→ small reversible implementation
→ runtime test

PASS
→ 擴大一小步

FAIL
→ 只調查該次 FAIL 暴露的 blocker
→ 修正
→ 再測
```

Inventory Sort V1 是標準 reference example：

```text
2-item swap
→ first-5 item-ID sort
→ full item-ID sort
→ seven-category sort
```

只有當實驗無法做到 bounded / reversible，或證據不足以保護 client/data 時，才繼續擴大研究。

## 原始 client 保護規則

原始 own 8.8C：

`C:\架設功具\天堂(Lineage 8.8C)`

維持：

- `ORIGINAL_8_8C_UNCHANGED = PASS`
- 所有寫入只允許在測試副本 / runtime / derived outputs。
- 不直接修改 original PAK/IDX、Lin.bin/LinLogin.bin、Server、DB。

## Local derived artifacts — 保留，不刪

已建立／使用的主要 outputs：

- `inventory_88_sort_swap_poc`
- `inventory_88_sort_first5_itemid_poc`
- `inventory_88_sort_full_itemid_poc`
- `inventory_88_sort_hybrid_category_poc`
- `inventory_88_sort_handler_analysis`
- `inventory_85m_sort_reference`
- `pak_importer_v1`

其中 preview / manifest / rollback / backup / analysis JSON/TXT 應保留，未來修改 V1 時直接沿用，不從零開始。

## 下一次 Inventory Sort Goal 的起點

1. 先讀本文件。
2. 將 V1 視為已驗收 baseline，不重查「能不能排序」。
3. 若使用者要調整分類，只修改具體 classifier rule 並做 preview → bounded PoC。
4. 若要永久化，新的 Goal 是「將已驗收 runtime V1 轉成可重啟的 derived/test-client implementation」，不得重新做 Sort 根因研究。
5. 永久化前仍維持 original own 8.8C strict read-only。
