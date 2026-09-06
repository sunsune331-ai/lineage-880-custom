# 天堂 8.8 Client UI Function Map

最後更新：2026-09-07  
最後已驗證目標：32-bit LinLogin.bin，PID 24660，image base 0x00400000，module size 0x01D6E000  
最新 session recheck：2026-09-07 Argus attach 回報 `LinLogin.bin 未執行`；下列 heap/live object 值目前只作歷史證據，下一次 attach 後必須重新定位。

本文件只記錄已沿資料流、RTTI、vtable、factory 或 live object 驗證的 UI 函式。所有 Runtime VA 都屬於本次 8.8 build；client 重開或換 build 後，heap object 位址不可沿用，程式 VA 也應先用 RVA/AOB 重新驗證。

專案連動文件：[AI_HANDOFF.md](AI_HANDOFF.md) 定義 Reviewer ↔ Executor 契約；[CODEX_STATUS.md](CODEX_STATUS.md) 是當前 Goal 的唯一即時狀態。本文件只保存跨 Goal 可重用的函式級證據。

狀態用語：

- 確認：有直接指令、RTTI/vtable、caller/callee 或 live object 證據。
- 強候選：角色由完整資料流支持，但尚缺命名符號或一次專門 runtime hit。
- 未連結：函式本身存在，但沒有證據證明它位於目前主背包執行路徑。

## 關鍵分類

> **PromoteDollUI != 主背包**

PromoteDollUI 是獨立的魔法娃娃升階 UI。它擁有自己的 XmlScrollGridIcon 元件與暫存 source vector。它不是 RenewalInventoryUI，也不是 live 主背包 InventoryItemGrid。

本 session 的直接物件證據：

| 物件 | Owner / global | Live address | RTTI / vtable | 結論 |
|---|---:|---:|---:|---|
| PromoteDollUI | [0x019550B8] | 0x1E5361B0 | .?AVPromoteDollUI@Lineage@@ / 0x01626EC4 | 獨立 UI |
| PromoteDollUI.Grid | PromoteDollUI+0x170 | 0x26ED1158 | .?AVXmlScrollGridIcon@@ / 0x0165C1D4 | 升階 UI 自有 grid |
| RenewalInventoryUI | [0x01A960B4] | 0x1E3ECBE0 | .?AVRenewalInventoryUI@@ / 0x0163CDB4 | 主背包 owner |
| InventoryItemGrid | RenewalInventoryUI+0x15C | 0x1E44A6C8 | .?AVInventoryItemGrid@@ / 0x0163C8F4 | 主背包 grid |
| InvWindow | RenewalInventoryUI+0x168 | 0x1E3EB0C8 | .?AVInvWindow@@ / 0x0163CC3C | 主背包計數/附屬視窗 |
| RenewalInventory manager | [0x01952748] | 0x2D523AF8 | ctor 0x00DDAB80 | 真正 item source owner |

兩個 grid 類別雖不同，vtable+0x184、+0x188、+0x18C 都分別指向同一組 clear / create-add / add-existing 實作；這是可重用的共用 grid ABI，不代表兩個 owner 是同一 UI。

## PromoteDollUI

### Identity、owner 與生命週期

| Role | Runtime VA | RVA | Evidence |
|---|---:|---:|---|
| RTTI TypeDescriptor | 0x01920C54 | 0x01520C54 | +0x08 是 .?AVPromoteDollUI@Lineage@@ |
| Primary COL / vtable | 0x0174EBF8 / 0x01626EC4 | 0x0134EBF8 / 0x01226EC4 | COL offset=0；slot 0=0x00BA1AB0 |
| Secondary COL / vtable | 0x0174EC4C / 0x01626F9C | 0x0134EC4C / 0x01226F9C | COL offset=0x15C；與 ctor secondary vptr 寫入吻合 |
| Global singleton slot | 0x019550B8 | 0x015550B8 | live value 0x1E5361B0；object vptr=0x01626EC4 |
| Named factory branch | 0x013873AF–0x01387446 | 0x00F873AF–0x00F87446 | 比對 PromoteDollUI，配置 0x200，call 0x00BA0610，於 0x01387425 寫 global slot |
| Direct constructor | 0x00BA0610 | 0x007A0610 | 0xBA0660 寫 primary vptr；0xBA0669 寫 this+0x15C secondary vptr |
| Named/default constructor | 0x00BA0860 | 0x007A0860 | 使用 PromoteDollUI literal，建立相同雙 vptr |
| Destructor | 0x00BA0F80 | 0x007A0F80 | 銷毀 +0x1C8、+0x1D4、+0x1DC 等 owned state |
| Deleting destructor | 0x00BA1AB0 | 0x007A1AB0 | primary vslot 0；delete size 0x200 |

### Object layout 與 UI 元件

元件綁定入口為 0x00BA28B0，名稱查找共用 0x00FC9640。

| Offset | XML/UI name | 類型或用途 |
|---:|---|---|
| +0x15C | secondary subobject | secondary vptr=0x01626F9C |
| +0x16C | DollSlot | slot list/container |
| +0x170 | Grid | XmlScrollGridIcon；live 0x26ED1158 |
| +0x174 | GrabIcon | icon control |
| +0x178 | Title | text/image control |
| +0x17C | Promote_Button | button |
| +0x180 | Cancel_Button | button |
| +0x184 | Retry_Button | button |
| +0x188 | Roulette | PromoteDollRoulette-like control |
| +0x18C | CoverImg | image |
| +0x190 | BeginEffect | effect |
| +0x194 | CompleteEffect | effect |
| +0x198 | HitEffect | effect |
| +0x19C | BlinkEffect | effect |
| +0x1A0 | PredictionEffect | effect |
| +0x1A4 | ProbUpEffect | effect |
| +0x1A8 | ProbUpTextEffect | effect |
| +0x1AC | ResultBackground | image |
| +0x1B4 | Slot_Center | center slot owner |
| +0x1B8 | DollSlot_Center | center slot |
| +0x1BC | GuideBox | guide container |
| +0x1C0 | GuideOption | option control |
| +0x1C4 | AutoProcessBtns | button group/list |
| +0x1DC/+0x1E0/+0x1E4 | source Vector28 | begin/end/capacity |
| +0x1FC | provider | source provider pointer；目前 live 為 0 |

PromoteDollRoulette component factory 為 0x00BA6E10：名稱等於 PromoteDollRoulette 時配置 0x198 並呼叫 0x00B46310；其餘回到 base factory。

### Button、list 與 grid callback

callback binder 是 0x00BA4390；一般名稱使用共用 0x00FC8E10，KeyEvent 使用 0x00FC8D80。

| UI name | Wrapper / handler VA | 已確認作用 |
|---|---:|---|
| Close | 0x00BA2880 | 0x202 event 後對 singleton 呼叫 0x00BA30C0(false) |
| Promote | 0x00BA54F0 → 0x00BA5210 | 檢查狀態與 +0x1D4 item count，再進升階前置流程 |
| Retry | 0x00BA5C70 → 0x00BA5C30 | retry/reset，後接 0x00BA6090(false) |
| Cancel | 0x00BA2180 → 0x00BA2160 | cancel path |
| DollSlot | 0x00BA2DF0 → 0x00BA6B20 | 依 0x200-based event 更新 +0x1EC selection 與 +0x174 |
| CenterDollSlot | 0x00BA21B0 → 0x00BA21D0 | center slot path |
| Grid | 0x00BA3FB0 → 0x00BA3E00 | 選 grid index，讀 source record+0x0C item pointer，更新 +0x174 |
| CheckGuideOption | 0x00BA2580 → 0x00BA2510 | guide option |
| KeyEvent | 0x00BA4AF0 | ESC/0x1B path 關閉 UI |
| AutoProcess | 0x00BA1FB0 → 0x00BA1D30 | source/list auto-process path |

### Virtual methods

Primary vtable 0x01626EC4 有 53 個有效 slot；其餘多數是共用 base UI 實作。下表只列已辨識的 PromoteDollUI override：

| Vtable offset | VA | Role / evidence |
|---:|---:|---|
| +0x00 | 0x00BA1AB0 | deleting destructor |
| +0x28 | 0x00BA3FD0 | grid/UI event；檢查 +0x16C、+0x1EA、+0x1EC |
| +0x2C | 0x00BA42E0 | 迴圈走訪 this+0x170 grid items，呼叫 item vslot+0x8C |
| +0x34 | 0x00BA30A0 | thin forwarder → 0x00FC90F0 |
| +0x44 | 0x00BA30C0 | show/state/refresh entry；true path 進 0x00BA5800 |
| +0x50 | 0x00BA5A50 | thin forwarder → 0x00FCA670 |
| +0x54 | 0x00BA5B80 | reset/hide，清 provider +0x1FC |
| +0x84 | 0x00BA28B0 | component binding |
| +0x88 | 0x00BA6E10 | PromoteDollRoulette component factory |
| +0xC0 | 0x00BA50A0 | provider setter / open path |
| +0xC4 | 0x00BA5170 | Promote-specific action |
| +0xC8 | 0x00BA4810 | Promote-specific action |
| +0xCC | 0x00BA5520 | Promote-specific action |
| +0xD0 | 0x00BA5A40 | Promote-specific action |

Secondary vtable 0x01626F9C：

| Vtable offset | VA | Role |
|---:|---:|---|
| +0x00 | 0x00BA19A3 | secondary destructor thunk |
| +0x04 | 0x00BA40C0 | event dispatcher；0xBA4149 或 0xBA41BB 路徑可進 0x00BA5800 |

### Source container 與 rebuild / refresh

資料流：

    PromoteDollUI+0x1FC provider
      → 0x00BA35E0 snapshot/filter producer
      → PromoteDollUI+0x1DC Vector28
      → 0x00BA1AF0 clear + loop
      → PromoteDollUI+0x170 XmlScrollGridIcon
      → shared vslot +0x188 (0x00FBC6F0)

| Role | VA | Evidence |
|---|---:|---|
| Source clear | 0x00BA2860 | this+0x1DC → 0x00BA9AD0 |
| Source builder | 0x00BA35E0 | output=[arg+8]，provider=this+0x1FC；snapshot 後依 provider 順序迭代/filter |
| Vector28 append | 0x00BAB380 | builder 於 0xBA37B8 呼叫；element stride 0x28 |
| Grid clear dispatcher | 0x00BA2830 | this+0x170 → vptr+0x184 |
| Bulk consumer | 0x00BA1AF0 | 先 clear；逐 0x28-byte record，record+0x0C item/model pointer 送入 vslot+0x188；ret 0x0C |
| Refresh wrapper | 0x00BA5800 | 0x00BA5820 後接 0x00BA58B0 |
| Refresh core | 0x00BA5820 | source clear → BA35E0 → source copy → BA1AF0 → BA5D00 |
| Post-source state | 0x00BA5D00 | 再走訪 +0x1DC，更新 provider/selection state |
| Post-list rendering | 0x00BA58B0 | 更新 DollSlot/list labels/details |

Vector28 的已知元素 ABI：

- stride = 0x28。
- record+0x0C = item/model pointer，為 grid add 的直接輸入。
- record+0x10 起含一個 24-byte string-like member。
- 0x00BA35E0 內沒有 sort/reverse；輸出次序保留 provider snapshot 的迭代次序。因此 PromoteDollUI 的 order 決定於更上游 provider，不在 BA5820 或 BA1AF0。

### 與主背包共用的 grid ABI

| Class / vtable | +0x184 | +0x188 | +0x18C |
|---|---:|---:|---:|
| XmlScrollGridIcon / 0x0165C1D4 | 0x00FBD520 | 0x00FBC6F0 | 0x00FBC670 |
| InventoryItemGrid / 0x0163C8F4 | 0x00FBD520 | 0x00FBC6F0 | 0x00FBC670 |

| Shared implementation | Runtime VA | Role |
|---|---:|---|
| 0x00FBD520 | 0x00FBD520 | clear this+0x190 icon pointer vector；逐 item dtor/delete，再重置 grid state |
| 0x00FBC6F0 | 0x00FBC6F0 | 由 item/model pointer 配置並建立一個 icon object，append 至 this+0x190 |
| 0x00FBC670 | 0x00FBC670 | 將已建立的 UI element 加入 grid |
| 0x00FC9640 | 0x00FC9640 | 共用 XML component lookup |
| 0x00FC8E10 | 0x00FC8E10 | 共用 named callback binder |

## RenewalInventoryUI / InvWindow（主背包）

### Identity、owner 與生命週期

| Class / role | Runtime VA | RVA | Evidence |
|---|---:|---:|---|
| RenewalInventoryUI RTTI | 0x01927ACC | 0x01527ACC | +0x08 = .?AVRenewalInventoryUI@@ |
| RenewalInventoryUI COL / vtable | 0x01757DF4 / 0x0163CDB4 | 0x01357DF4 / 0x0123CDB4 | live object vptr 相符 |
| RenewalInventoryUI global | 0x01A960B4 | 0x016960B4 | live value 0x1E3ECBE0 |
| Global factory branch | 0x01385D70；關鍵 call 0x013881F3 | 0x00F85D70 / 0x00F881F3 | Inventory 分支 call 0x00DF5FF0，0x01388223 寫 global |
| RenewalInventoryUI ctor | 0x00DF5FF0 | 0x009F5FF0 | object size 0x18C；base ctor 0x00FC7B10；設定 vtable/fields/callback |
| RenewalInventoryUI cleanup | 0x00DF65B0 | 0x009F65B0 | 清 owned fields 並將 [0x01A960B4] 歸零 |
| RenewalInventoryUI deleting dtor | 0x00DF69D0 | 0x009F69D0 | vslot 0 |
| InventoryItemGrid RTTI | 0x01927A78 | 0x01527A78 | +0x08 = .?AVInventoryItemGrid@@ |
| InventoryItemGrid COL / vtable | 0x01757C88 / 0x0163C8F4 | 0x01357C88 / 0x0123C8F4 | live grid vptr 相符 |
| InventoryItemGrid ctor / cleanup | 0x00DF5B60 / 0x00DF63D0 | 0x009F5B60 / 0x009F63D0 | object size 0x210；base 0x00FBAE30；初始化至 +0x20C |
| InventoryItemGrid deleting dtor | 0x00DF6950 | 0x009F6950 | vslot 0 |
| InvWindow RTTI | 0x01927AB4 | 0x01527AB4 | +0x08 = .?AVInvWindow@@ |
| InvWindow COL / vtable | 0x01757D6C / 0x0163CC3C | 0x01357D6C / 0x0123CC3C | live object vptr 相符 |
| InvWindow ctor / cleanup | 0x00DF5AE0 / 0x00DF6370 | 0x009F5AE0 / 0x009F6370 | object size 0x188；base 0x00F574E0 |
| InvWindow deleting dtor | 0x00DF6910 | 0x009F6910 | vslot 0 |

RenewalInventoryUI component factory 0x00DFC290：

| XML class name | Allocation / ctor | Owner field | Live value |
|---|---|---:|---:|
| InventoryItemGrid | 0x210 / 0x00DF5B60 | +0x15C | 0x1E44A6C8 |
| InvWindow | 0x188 / 0x00DF5AE0 | +0x168 | 0x1E3EB0C8 |
| InvDelButton | 0x198 / 0x00DF5A80 | factory return | grid/InvWindow 目前引用 0x23618160 |
| Other | base fallback 0x00FCA7C0 | — | — |

### Runtime layout

| Object / offset | Current value | Meaning |
|---|---:|---|
| InventoryItemGrid+0x190 | 0x2CC72F48 | icon pointer vector begin |
| InventoryItemGrid+0x194 | 0x2CC73054 | icon pointer vector end |
| InventoryItemGrid+0x198 | 0x2CC730C0 | icon pointer vector capacity |
| InventoryItemGrid+0x1B8 | 0x227E3480 | layout/list sink child |
| InventoryItemGrid+0x1BC/+0x1C0/+0x1C4 | 0x28D7A428 / 0x28D7A428 / 0x28D7A474 | layout-order index vector；目前 size 0 |
| InventoryItemGrid+0x1FC | 0x23618160 | InventoryDeleteButton |
| InventoryItemGrid+0x208 | 0 | tab/filter mode |
| InventoryItemGrid+0x20C | 0 | transient reorder/action context |
| RenewalInventoryUI+0x16C/+0x170/+0x174 | 0x28D7ADC8 / 0x28D7ADC8 / 0x28D7AE14 | persisted item-ID order vector；目前 size 0 |
| InvWindow+0x178 | 0x23618160 | InventoryDeleteButton |
| InvWindow+0x17C | 0 | 尚未命名 |
| InvWindow+0x180 | 0x247E14C8 | count text/control |

本次 live 驗證中，manager item vector 與 grid icon vector 都是 67 個元素；persisted item-ID 與 layout-index vectors 目前為空。這同時驗證 source/grid 數量對得上，也表示本次狀態沒有可沿用的已保存手動 order entries。

### InventoryItemIcon 座標欄位 runtime 修訂

2026-09-07 對兩個 live icon 做 DR0/DR1 data-write capture：

| Icon | `+0x94` | `+0x160 invgfx` | `+0x1B8 count` | watch address |
|---:|---:|---:|---:|---:|
| 0x251D6308 | 0x00000000 | 0x00000A2E | 0xFFFFFFFF | 0x251D639C |
| 0x251D4AF8 | 0x00000022 | 0x0000004D | 0xFFFFFFFF | 0x251D4B8C |

225 次 idle 命中全部在 store 後 `EIP=0x004C32BA`；精確 writer 是：

    0x004C32B0  push ebp
    0x004C32B1  mov ebp,esp
    0x004C32B3  mov edx,[ebp+8]
    0x004C32B6  mov eax,[edx]
    0x004C32B8  mov [ecx],eax       ; writes icon+0x94
    0x004C32BA  mov eax,[edx+4]
    0x004C32BD  mov [ecx+4],eax     ; writes icon+0x98
    0x004C32C3  ret 4

因此 `InventoryItemIcon+0x94/+0x98` 是一組 POINT/座標 pair，會由 generic copy helper `0x004C32B0` 在 idle layout path 反覆重寫；`0x004C32B0` 與 `0x004C32B8` **不是 reorder producer**。捕捉到的 EBP return chain 為：

    0x00F8188B → 0x00FC37AE → 0x00FC392F → 0x00DFC5BF
    → 0x00DFC6A8 → 0x00FBE340 → 0x00DF7553 → 0x00F65E2A

該次回報拖放前後，兩個 icon 各自 0x250 bytes、grid+0x190 icon vector、grid+0x1BC layout vector、manager+0x54 Item* vector 都完全相同，故該輪沒有捕捉到可歸因於拖放的 reorder/refresh。舊假設「`+0x94` 是持久 grid slot/order 欄位」已被反證；後續不得再以同值 idle write 當 reorder evidence。

### UI callback 與 refresh entry

RenewalInventoryUI ctor 0x00DF5FF0 透過共用 binder 0x00FC8E10 註冊：

| UI name | Handler VA | Role |
|---|---:|---|
| CloseWindow | 0x00DF8F50 | 關閉/隱藏；呼叫 owner vslot+0x44 |
| Sort | 0x00DF9930 | 只驗證 grid，再呼叫 manager 0x00DE2280 → UI refresh dispatcher |
| Promote | 0x00DF9820 | inventory promote action |
| CashWareHouse | 0x00DF8E70 | cash warehouse action |
| SealButton | 0x00DF9850 | seal action |
| ClickTab | 0x00DF8E90 | 取得 tab index，最後以 0x00DFBE20 寫 grid+0x208 並 refresh |

重要：Sort callback 0x00DF9930 本身沒有呼叫 comparator 或 merge-sort。它的已確認鏈只有：

    0x00DF9930
      → 0x005EE200 RenewalInventory manager getter
      → 0x00DE2280
      → [0x01A960B4] RenewalInventoryUI
      → 0x00DFAB70 refresh dispatcher

### Source container

真正的 inventory data owner 是 RenewalInventory singleton：

| Role | VA / field | Evidence |
|---|---:|---|
| Singleton getter | 0x005EE200 | 回傳 [0x01952748] |
| Global slot | 0x01952748 | live manager=0x2D523AF8 |
| Manager ctor | 0x00DDAB80 | +0x54 呼叫 0x00DDA640 建 container；+0x60/+0x64 建 iterator state |
| Source vector | manager+0x54/+0x58/+0x5C | vector<Item*> begin/end/capacity |
| Live source | 0x2CC747C8 / 0x2CC748D4 / 0x2CC74940 | size=(end-begin)/4=67 |
| Size helper | 0x00DE5F50 | (end-begin)>>2 |
| Index helper | 0x00DDB2C0 | begin + index*4 |
| First iterator | 0x00DDF1E0 | 以 manager+0x54 初始化 manager+0x60，回傳 Item* |
| Next iterator | 0x00DE1410 | 推進 manager+0x60，回傳下一個 Item* |
| Predicate first/next | 0x00DDF1A0 / 0x00DE1360 | 使用 manager+0x64 iterator，跳至下一個符合 unary predicate 的 Item* |
| New item append | 0x00DE11E0 | 配置 0x6C0 item object，append 到 manager+0x54，然後 0x00DFAB70 refresh |

manager+0x54 的元素型別是 Item*：除了 stride 4 與直接解參考，0x00DE0570 經 DDB2C0 取元素後對 item+0x670 寫欄位，形成額外的型別證據。

最初由 packet/data 批次建立並填滿 manager+0x54 的單一上游 producer 尚未唯一定位；不可把 0x00DE11E0 說成唯一 producer。0x00DE11E0 是已確認的單 item append 路徑。

### Refresh / rebuild / order pipeline

已確認的主資料流：

    [0x01952748] RenewalInventory
      → manager+0x54 vector<Item*>
      → 0x00DF8450 enumerate（可套 unary filter，保留 source iteration order）
      → 0x00DF9E60 desired-list dispatcher
      → 0x00DFA580 reconcile InventoryItemGrid
           ├─ missing item → 0x00DFA020 construct → grid vslot+0x18C add
           ├─ wrong pointer-vector index → 0x00DF5480 swap
           ├─ RenewalUI+0x16C item IDs → 0x00DF86B0
           ├─ grid+0x1BC layout indices → 0x00FC2520
           └─ 0x00DFAF60 + 0x00DFB260 finalize/layout/visibility

| Function | Runtime VA | Role / direct evidence |
|---|---:|---|
| Inventory grid getter | 0x00DF8060 | 回傳 RenewalInventoryUI+0x15C |
| Refresh dispatcher | 0x00DFAB70 | 有 grid 時 call 0x00DFA580；有 InvWindow 時 call 0x00DFA4D0 |
| InvWindow refresh | 0x00DFA4D0 | 更新目前/容量 count text |
| Desired-list dispatcher | 0x00DF9E60 | 讀 grid+0x208，0x00DF6FB0 選 strategy，再由 0x00DF6730 執行 |
| Source enumerator | 0x00DF8450 | 經 0x00DF6690/0x00DF66E0 走 manager+0x54，append Item* 到 desired list |
| Reconcile/refresh | 0x00DFA580 | 對 desired list 找現有 index、補建、swap；建立 layout order，最後 finalize |
| Find current item index | 0x00DF8130 | 由 Item* 找目前 grid index |
| Construct missing icon | 0x00DFA020 | 配置 0x250 object，ctor 0x00DF5C70，交 vslot+0x18C |
| Pointer swap | 0x00DF5480 | 0xDFA6EA 的兩槽交換 |
| Current model sequence snapshot | 0x00DF85B0 | 走 grid current elements，轉回 model/item sequence |
| Item IDs → grid indices | 0x00DF86B0 | 走 RenewalUI+0x16C IDs，0x00DF8260/0x00DF81E0 對照 item ID，輸出 index vector |
| Layout-index consumer | 0x00FC2520 | 逐 grid+0x1BC index 取 icon，更新 +0x1B8 layout/list sink |
| Final group/state pass | 0x00DFAF60 | 呼叫 DFABF0/DFAFA0/DFB0D0/DFABB0/DFAFB0/DFADB0 |
| Final visibility/layout | 0x00DFB260 | 依 rows/cols/count 更新 cell visibility/layout |

grid+0x208 strategy：

| Mode | Strategy | Unary selection predicate | Order behavior |
|---:|---:|---:|---|
| default | 0x00DF7E60 | none | 保留 manager+0x54 iteration order |
| 1 | 0x00DF7EC0 | 0x00B4F650 | item-type/category filter；不做 binary sort |
| 2 | 0x00DF8500 | 0x00B4F930 | name/id-list-like filter；不做 binary sort |
| 3 | 0x00DF7F70 | 0x00B4F8E0 | composition of B4F930/B4F650；不做 binary sort |

### Order producer 與 reorder

主背包目前確認有三個不同層次的 order 機制：

1. manager source vector 的順序。
2. UI 保存的 item-ID order。
3. grid layout 使用的 index order。

手動/局部 reorder：

    grid reorder event
      → InventoryItemGrid vslot+0x1A0 = 0x00DFB6E0
      → base grid reorder 0x00FC2BB0
      → [0x01A960B4] 0x00DFC6B0
      → 0x00DF85B0 snapshot current grid model sequence
      → item 0x00106C090 get ID
      → RenewalInventoryUI+0x16C vector<int> append

另一條把 UI move 轉成 source vector move 的路徑：

    InventoryItemGrid vslot+0x1A4 = 0x00DFBFA0
      → 0x005EE200 manager
      → 0x00DE0E40 range split / erase / reinsert on manager+0x54
      → 0x00DFA580 refresh

0x00DF9EF0 也會呼叫 0x00DE0E40 後 refresh，用於單項位置調整。

| Role | Runtime VA | Evidence |
|---|---:|---|
| UI reorder commit | 0x00DFB6E0 | grid vtable+0x1A0；先 call base 0x00FC2BB0，再 call 0x00DFC6B0 |
| Persisted order producer | 0x00DFC6B0 | clear owner+0x16C；0x00DF85B0 取 current model sequence；逐 item 取 ID 並 append |
| Source range reorder | 0x00DE0E40 | 對 manager+0x54 依 index/range 分割到兩個暫存 pointer vectors，再 erase/reinsert；回傳新 index |
| UI source-move adapter | 0x00DFBFA0 | grid vtable+0x1A4；多次呼叫 DE0E40，最後 DFA580 |
| Persisted order restore | 0x00DF86B0 | item-ID list 轉成 grid index list |
| Layout order apply | 0x00FC2520 | 使用 grid+0x1BC index list 驅動 layout sink |

批次 order-adjust：

| Role | Runtime VA | Evidence |
|---|---:|---|
| Bulk order adjust / reorder | 0x00DE1D40 | 讀 manager+0x54；依 arg 選 manager+0x340 或 +0x68 的 0xB4-entry order table；建立 current key array；兩階段以 0x00DD9FC0 交換 Item* |
| Key extraction | 0x00106BD50 / 0x00106C090 / 0x00CD7BB0 | name/id 與 fallback key path |

0x00DE1D40 是目前最強的批次 reorder 函式；0x00DE0E40 是局部/range reorder mutator；0x00DFC6B0 是 UI order persistence producer。三者角色不同，不應合併成一個模糊的 sort 函式。

### InventoryItemGrid virtual methods

InventoryItemGrid primary vtable 0x0163C8F4 有 111 個有效 slot（0..110；+0x1BC 已進下一個 COL）。下表列出 class-specific 或主線需要的 slots：

| Vtable offset | VA | Role |
|---:|---:|---|
| +0x00 | 0x00DF6950 | deleting destructor |
| +0x174 | 0x00FBF010 | shared grid method |
| +0x178 | 0x00DF93F0 | InventoryItemGrid override |
| +0x17C | 0x00FBF5A0 | shared grid method |
| +0x180 | 0x00FBD610 | shared grid method |
| +0x184 | 0x00FBD520 | clear |
| +0x188 | 0x00FBC6F0 | create/add from Item* |
| +0x18C | 0x00FBC670 | add already-created element |
| +0x190 | 0x00DFC5D0 | scrollbar metric update，最後調用 vslot+0x194 |
| +0x194 | 0x00DFC5B0 | base/list finalize + 0x00DFAF60 |
| +0x198 | 0x00DF7E30 | manager item operation後 refresh |
| +0x19C | 0x00DF9EF0 | item position adjustment；可呼叫 0x00DE0E40 |
| +0x1A0 | 0x00DFB6E0 | grid reorder commit + save item-ID order |
| +0x1A4 | 0x00DFBFA0 | source vector range move adapter |
| +0x1A8 | 0x00DF7B80 | reverse cell-state/cleanup pass |
| +0x1AC | 0x00DFB400 | 建立/保存 transient context +0x20C |
| +0x1B0 | 0x00DFC430 | 釋放 transient context |
| +0x1B4 | 0x00DFB6B0 | 執行 transient context callback |
| +0x1B8 | 0x00DFC4A0 | item removal後修正 order indices並 call 0x00FC2520 |

### RenewalInventoryUI virtual methods

Primary vtable 0x0163CDB4 有 48 個有效 slot；多數是 base UI 共用實作。已辨識 overrides：

| Vtable offset | VA | Role |
|---:|---:|---|
| +0x00 | 0x00DF69D0 | deleting destructor |
| +0x24 | 0x00DF8DF0 | Renewal-specific event path |
| +0x44 | 0x00DF7D20 | show/hide；open path 經 0x00DFB430 → 0x00DFAB70 |
| +0x54 | 0x00DFB580 | child/list close/reset path |
| +0x80 | 0x00DF6EB0 | base-forwarding UI method |
| +0x88 | 0x00DFC290 | component factory |

### Named sorting engine：存在，但未證明接入主背包

| Symbol family / role | Runtime VA | Status |
|---|---:|---|
| RenewalItemMergeSortNode recursive engine | 0x00DEF5F0 | 未連結 |
| Graph/policy builder | 0x00DEF750 | 只有 0x00DEEEBA caller |
| Builder wrapper/ctor | 0x00DEEEB0 | 無直接 caller |
| ItemTypePred | 0x00DEF500 | engine family only |
| EquipmentPred | 0x00DEF470 | engine family only |
| EnchantPred | 0x00DEF400 | engine family only |
| BlessCodePred | 0x00DEF330 | engine family only |
| Name-ID sort wrapper/core | 0x00DF0B60 → 0x00DEE1D0 | introsort-like core；未連結主線 |
| Enchant sort wrapper/core | 0x00DF0B00 → 0x00DEE0D0 | introsort-like core；未連結主線 |
| inventoryItemOrderKey descriptor | 0x0160BDB0 | protobuf field #39，repeated packed int32；與 UI+0x16C 尚無直接 xref 證明 |
| inventoryLitoItemOrderKey descriptor | 0x0160C0C6 | protobuf field #60，repeated packed int32；尚無直接 xref 證明 |

精確 caller/ref 邊界：

- 0x00DEF5F0 只有自身遞迴 caller。
- 0x00DEF750 只有 0x00DEEEBA caller。
- 0x00DEEEB0 無直接 caller。
- 目前模組內對 DEF5F0 / DF0B60 / DF0B00 / DEEEB0 / DEF750 的 exact pointer refs 為 0。
- 已確認的 DF7D20 → DFAB70 → DFA580 → DF9E60、mode strategies、DE0E40 與 DE1D40 都沒有呼叫此 engine family。

因此此 family 保留在 Function Map，但不得宣稱它已被 RenewalInventoryUI 主路徑使用。

## AOB / signature anchors

驗證規則：

- 掃描目前 LinLogin.bin 主模組全部 executable regions。
- 相對 CALL/JMP/Jcc operand，以及落在主模組範圍的 absolute immediate / memory displacement 已遮罩。
- 下列 pattern 在目前 build 均只命中表列入口一次。
- 「目前 build 唯一」不是跨版本保證；換 client build 後必須重新掃描與驗證。
- 這些 signature 用於定位/驗證，不代表授權 patch client。

### PromoteDollUI

PromoteDollUI ctor dual-vptr，hit 0x00BA0660：

    C7 01 ?? ?? ?? ?? 8B 55 F0 ?? ?? ?? ?? 00 00 ?? ?? ?? ?? ?? ?? ?? ?? 74 0D 8B 45 ?? ?? ?? ?? 00 00 89 45 EC EB 07 ?? ?? ?? ?? 00 00 00 8B 4D EC 51 8B

Control binder，hit 0x00BA4390：

    55 8B EC 51 89 4D ?? ?? ?? ?? 00 00 6A 5B 6A 5F

Grid-clear dispatcher，hit 0x00BA2830：

    55 8B EC 51 89 4D FC 8B 45 FC ?? ?? ?? ?? 00 00 8B ?? ?? ?? ?? ?? ?? ?? ?? 00 00 ?? ?? ?? ?? 00 00 FF D2 8B E5

Source builder，hit 0x00BA35E0：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 7C A1 ?? ?? ?? ?? 33 C5 89 45 F0 50 8D 45 ?? ?? ?? ?? 00 00 00 89 4D B4 8D

Vector28 append，hit 0x00BAB380：

    55 8B EC 83 EC 10 56 89 4D FC 8B 45 08 50 E8 ?? ?? ?? ?? 83 C4 04 50 8B 4D FC E8 ?? ?? ?? ?? 0F B6 C8 85 C9 ?? ?? ?? ?? 00 00 8B 55 08 52 E8 ?? ?? ?? ?? 83 C4 04 8B F0 8B 4D FC E8 ?? ?? ?? ?? 8B 00 50 E8 ?? ?? ?? ?? 83 C4 04 2B F0 8B C6 99 B9 28 00 00 00 F7 F9 89 45 F0 8B 4D FC E8 CE

Rebuild consumer，hit 0x00BA1AF0：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 28 A1 ?? ?? ?? ?? 33 C5 50 8D 45 ?? ?? ?? ?? 00 00 00 89 4D E8 ?? ?? ?? ?? 00 00 00 8B 4D

Refresh wrapper，hit 0x00BA5800：

    55 8B EC 51 89 4D FC 8B 4D FC E8 ?? ?? ?? ?? 8B 4D ?? ?? ?? ?? 00 00 8B E5 5D C3 CC CC CC CC CC 55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 10

Refresh core，hit 0x00BA5820：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 10 A1 ?? ?? ?? ?? 33 C5 50 8D 45 ?? ?? ?? ?? 00 00 00 89 4D F0 8B 4D F0 E8 ?? ?? ?? ?? 8B 45 ?? ?? ?? ?? 00 00 50

State refresh virtual，hit 0x00BA30C0：

    55 8B EC 51 89 4D FC 8B 45 FC ?? ?? ?? ?? 00 00 01 75 ?? ?? ?? ?? 00

Shared grid clear，hit 0x00FBD520：

    55 8B EC 83 EC 18 89 4D FC ?? ?? ?? ?? 00 00 00 EB 09 8B 45 ?? ?? ?? ?? 89 45 F4 8B 4D FC ?? ?? ?? ?? 00

Shared grid create/add，hit 0x00FBC6F0：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 14 A1 ?? ?? ?? ?? 33 C5 50 8D 45 ?? ?? ?? ?? 00 00 00 89 4D EC 68

### RenewalInventoryUI / InventoryItemGrid

RenewalInventoryUI ctor，hit 0x00DF5FF0：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 51 A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 89 4D F0 8B 45 08 50 8B 4D F0 E8 ?? ?? ?? ?? C7 45 FC 00 00 00 00 8B 4D F0 C7 01 ?? ?? ?? ?? 8B 55 F0 C7 82 68 01 00 00 00 00 00 00

Component factory，hit 0x00DFC290：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 2C A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 89 4D F0 68 ?? ?? ?? ??

Grid reconcile/refresh，hit 0x00DFA580：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 81 EC C4 00 00 00 56

Desired-list dispatcher，hit 0x00DF9E60：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 34 A1 ?? ?? ?? ?? 33 C5 89 45 F0 50 8D 45 F4 64 A3 00 00 00 00 89 4D C0 C7 45 C4 00 00 00 00

Inventory source enumeration，hit 0x00DF8450：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 1C A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 C7 45 EC 00 00 00 00

Refresh dispatcher，hit 0x00DFAB70：

    55 8B EC 51 89 4D FC 8B 45 FC 83 B8 5C 01 00 00 00 74 ?? 8B 4D FC 8B 89 5C 01 00 00 E8 ?? ?? ?? ?? 8B 55 FC 83 BA 68 01 00 00 00

Sort callback，hit 0x00DF9930：

    55 8B EC 81 7D 0C 02 02 00 00 75 ?? 8B 0D ?? ?? ?? ?? E8 ?? ?? ?? ?? 85 C0 74 ??

Grid mode setter/refresh，hit 0x00DFBE20：

    55 8B EC 51 89 4D FC 8B 45 FC 8B 4D 08 8B 11 89 90 08 02 00 00

Grid order → item-ID producer，hit 0x00DFC6B0：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 28 A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 89 4D EC 8B 4D EC 81 C1 6C 01 00 00

Grid reorder commit，hit 0x00DFB6E0：

    55 8B EC 83 EC 0C 89 4D FC 0F B6 45 10 50 0F B6 4D 0C

Item IDs → grid indices，hit 0x00DF86B0：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 28 A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 89 4D E8 C7 45 E0 00 00 00 00

Consume layout/order indices，hit 0x00FC2520：

    55 8B EC 83 EC 20 56 89 4D FC 8B 4D FC E8 ?? ?? ?? ??

Manager source range move/reorder，hit 0x00DE0E40：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 81 EC A4 00 00 00 A1 ?? ?? ?? ?? 33 C5 50

New item append + refresh，hit 0x00DE11E0：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 14 A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 89 4D E0 68 C0 06 00 00

Current model-sequence snapshot，hit 0x00DF85B0：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 2C A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 89 4D E8 C7 45 E0 00 00 00 00 8D 4D C8

Construct missing grid item，hit 0x00DFA020：

    55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 14 A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 89 4D E8 68 50 02 00 00

Bulk order adjust/reorder，hit 0x00DE1D40：

    55 8B EC 81 EC 44 05 00 00 A1 ?? ?? ?? ?? 33 C5 89 45 FC 56 89 8D F8 FA FF FF 8B 8D F8 FA FF FF 83 C1 54 E8 ?? ?? ?? ??

## 下一個可直接執行的唯讀 PoC

主線最有資訊量的 PoC 是一次「手動拖曳背包物品改變位置」runtime capture，而不是直接 call client function：

1. 在 0x00DFB6E0、0x00DFC6B0、0x00DE0E40、0x00DFA580 建立短時 execute capture。
2. 只做一次同一頁內的物品拖曳/reorder。
3. 保存 DFB6E0 的 index/flags args、DE0E40 的三個 range/index args，以及進出前後：
   - manager+0x54 vector<Item*> pointer order；
   - RenewalInventoryUI+0x16C vector<int itemId>；
   - InventoryItemGrid+0x1BC vector<int index>；
   - InventoryItemGrid+0x190 vector<UI item*>。
4. 驗證 call sequence 是 DFB6E0 → DFC6B0 與/或 DFBFA0/DF9EF0 → DE0E40 → DFA580，並確認 item-ID persistence 與 layout-index restore 的一一對應。

這個 PoC 全程可以保持唯讀 debugger capture，不需直接呼叫函式、不需 patch、不需寫 client memory，也不需 Tripwire。

## 維護規則

- 後續確認任何 UI 共用函式時，追加到本文件，不覆寫已確認的 evidence。
- 每個新 Goal 開始前先讀 `AI_HANDOFF.md` 與 `CODEX_STATUS.md`；當前進度只寫入 `CODEX_STATUS.md`，本文件不保存短期執行狀態。
- 每筆至少保留 class/RTTI、owner/global、runtime VA、RVA、caller/callee 或欄位資料流，以及可用時的 AOB。
- 若新 runtime 證據推翻舊角色，保留修訂說明與舊結論來源，不靜默改名。
- PromoteDollUI 與其他共用 grid 使用者可以保留，但主線優先順序固定為 RenewalInventoryUI / InvWindow → source container → order producer → sort/reorder → refresh。
