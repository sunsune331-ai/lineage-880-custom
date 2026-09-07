# 8.8 主背包 Reorder Runtime Capture Pack

最後更新：2026-09-07

用途：在 Codex / client 不可用時先把已知證據、session rebind、capture 順序、判定規則與結果格式固定下來。此文件**不新增逆向結論**；只整理目前 Function Map 已支持的內容，未有來源支持的 ABI 一律標成 `UNCONFIRMED`。

## 1. 本輪唯一目標

用一次主背包手動拖放，確認：

1. 真正改變 inventory manager source order 的 runtime entry。
2. persisted item-ID order 的 commit/producer 時序。
3. reorder 後的 refresh/reconcile tail。
4. 形成後續「背包整理」最小 PoC 所需的可靠 call sequence；在 ABI 未確認前不得直接呼叫函式。

## 2. 已確認主線與排除項

主線固定：

`RenewalInventoryUI / InvWindow → source/order producer → reorder → refresh`

已排除：

- `PromoteDollUI` 不是主背包。
- `InventoryItemIcon+0x94/+0x98` 是 idle layout POINT pair。
- `0x004C32B0 / 0x004C32B8` 不是 reorder producer。
- 不再對 icon raw POINT field 做全域 watch。

## 3. 四個 execute anchors

目前 build image base = `0x00400000`。下表 RVA 為純位址換算；live 使用前仍需以本機 binary bytes/AOB 重驗。

| Role | Runtime VA | RVA | 目前直接證據 | 尚缺 runtime 證據 |
|---|---:|---:|---|---|
| UI reorder commit | `0x00DFB6E0` | `0x009FB6E0` | grid vtable+0x1A0；先 call base `0x00FC2BB0`，再 call `0x00DFC6B0` | 真實拖放是否命中、`this/args/return/caller` |
| Persisted order producer | `0x00DFC6B0` | `0x009FC6B0` | clear owner+0x16C；`0x00DF85B0` 取 current model sequence；逐 item 取 ID append | 拖放時序、before/after item-ID list |
| Source range reorder | `0x00DE0E40` | `0x009E0E40` | 對 manager+0x54 依 index/range 分割、erase/reinsert，回傳新 index | 真實手動拖放的 source/target ABI 與 vector delta |
| Reconcile/refresh | `0x00DFA580` | `0x009FA580` | desired list 找現有 index、補建、swap、建立 layout order、finalize；`DFBFA0` / `DF9EF0` 在 `DE0E40` 後會進此路徑 | 是否為該次手動 reorder 的實際 refresh/reconcile tail |

### 關聯函式（只作定位，不加 breakpoint）

- `0x00DFBFA0`：InventoryItemGrid vslot+0x1A4；多次呼叫 `DE0E40`，最後 `DFA580`。
- `0x00DF9EF0`：單項位置調整；呼叫 `DE0E40` 後 refresh。
- `0x00DF86B0`：persisted item-ID order → grid index list。
- `0x00FC2520`：使用 grid+0x1BC layout-index list 驅動 layout sink。
- `0x00DFAB70`：refresh dispatcher；有 grid 時 call `DFA580`，有 InvWindow 時 call `DFA4D0`。

## 4. Session rebind：禁止沿用舊 heap/PID

每次 client 重開都重新解析：

1. `RenewalInventoryUI global slot = 0x01A960B4`
   - 讀 live pointer。
   - 驗證 object vtable / RTTI 是 `RenewalInventoryUI`。
2. `RenewalInventoryUI +0x15C` → `InventoryItemGrid*`
   - 驗證 grid vtable / RTTI。
3. `RenewalInventoryUI +0x168` → `InvWindow*`
   - 僅作 owner/reconcile 參考。
4. `inventory manager global slot = 0x01952748`
   - 讀 live pointer；不得沿用舊 session value。
5. 重新解析四組需要 snapshot 的容器：
   - `manager +0x54`：source/order Item* vector。
   - `RenewalInventoryUI +0x16C`：persisted item-ID order vector。
   - `InventoryItemGrid +0x1BC`：layout-index vector。
   - `InventoryItemGrid +0x190`：grid icon pointer vector。

任何一步 RTTI/vtable/object chain 不吻合：停止，`STATUS = RECHECK`，不得靠舊 heap VA 繼續。

## 5. AOB / bytes gate

Function Map 已保存四 anchor 的目前-build 唯一 signature。這些 pattern 僅供**定位/驗證**；換 build 必須重新掃描，不代表可直接 patch。

### `DFB6E0` — Grid reorder commit

```text
55 8B EC 83 EC 0C 89 4D FC 0F B6 45 10 50 0F B6 4D 0C
```

目前 build hit：`0x00DFB6E0`。

補充：prologue 可直接看出會讀 stack 上 `[EBP+0x0C]`、`[EBP+0x10]` 的 byte-sized 值；**其語義仍為 UNCONFIRMED**，不得先命名成 source/target index。

### `DFC6B0` — Grid order → item-ID producer

```text
55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 28 A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 89 4D EC 8B 4D EC 81 C1 6C 01 00 00
```

目前 build hit：`0x00DFC6B0`。

### `DE0E40` — Manager source range move/reorder

```text
55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 81 EC A4 00 00 00 A1 ?? ?? ?? ?? 33 C5 50
```

目前 build hit：`0x00DE0E40`。

### `DFA580` — Grid reconcile/refresh

```text
55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 81 EC C4 00 00 00 56
```

目前 build hit：`0x00DFA580`。

### Gate 規則

1. 在 live module executable regions 掃描上述 signature。
2. 每個 pattern 必須只命中一次。
3. 命中 VA 必須對應預期 RVA（考慮實際 image base）。
4. 任一 pattern 0 hit / multi-hit / bytes 不符：`STATUS = RECHECK`，不掛 breakpoint。
5. 四個 anchor 全數通過後才進 session rebind 與 capture。

## 6. 一次性 capture 流程

### Phase A — 自動準備

1. attach `LinLogin.bin`。
2. 取得 image base/module size，確認仍為預期 build。
3. 做四 anchor bytes/AOB gate。
4. 做 session rebind。
5. Snapshot `BEFORE` 四組容器。
6. 只掛四個 execute anchors：`DFB6E0 / DFC6B0 / DE0E40 / DFA580`。
7. 清空舊 hit buffer。
8. 輸出唯一訊號：`CAPTURE_READY`。

### Phase B — 唯一人工操作

使用者只做一次：

- 在**主背包**內把一個明顯可辨識的物品拖到另一個位置。
- 不做第二次拖放、不切 tab、不關背包、不買賣物品。

### Phase C — 自動收尾

1. 收集四 anchor hits，保持實際時序。
2. 每個 hit 保存：
   - sequence/timestamp
   - EIP / anchor VA
   - ECX (`this` candidate)
   - ESP 與需要的 stack args
   - return address
   - direct caller
   - 若可安全解析，保存 source/target index candidate；不可確認則標 `UNCONFIRMED`
3. Snapshot `AFTER` 四組容器。
4. 對 BEFORE/AFTER 做 element-level diff。
5. 清除 breakpoint、detach。
6. 輸出 `CAPTURE_COMPLETE` 與固定 summary。

## 7. 必須保存的 before/after state

### manager source/order

- owner pointer
- begin/end/capacity
- size
- Item* sequence（至少完整 pointer sequence；若已有可靠 item-id accessor，可額外輸出 item ID）

### persisted order

- `RenewalInventoryUI+0x16C` begin/end/capacity
- 完整 item-ID sequence

### layout index

- `InventoryItemGrid+0x1BC` begin/end/capacity
- 完整 index sequence

### grid icon vector

- `InventoryItemGrid+0x190` begin/end/capacity
- 完整 icon pointer sequence
- 若已有可靠 icon→model→item-id chain，可附 item ID；不得重新使用已排除的 `+0x94` 作 order key

## 8. Runtime 判定矩陣

| Runtime 結果 | 判定 |
|---|---|
| `DE0E40` hit 且 manager+0x54 sequence 同步改變 | 支持 `DE0E40` 為真 source reorder 核心；再用 this/args/caller 固化 ABI |
| `DFB6E0 → DFC6B0` 連續命中，persisted item-ID list 隨後更新 | 支持 UI commit → persisted order producer chain |
| `DE0E40 → DFA580` 且 grid/layout state 後續 reconcile | 支持 `DFA580` 為 reorder 後 refresh tail |
| `DFC6B0` hit，但 manager vector 不變 | persisted commit 與 source reorder 可能是不同階段；不可把 `DFC6B0` 當 reorder core |
| `DFA580` hit，但前面沒有 reorder-related hit/state delta | 可能只是一般 refresh；不能單獨證明 reorder tail |
| 四 anchor 全未 hit，但手動拖放確實改變可見位置 | 只擴一層到已知直接 caller/callee；禁止 process-wide 無界搜尋 |
| UI 視覺移動但四組 state 完全不變 | 先確認拖放是否真的 commit；不得回頭把 `+0x94/+0x98` 提升為 order producer |

## 9. 最小 PoC：目前能說到哪裡

**已支持的候選流程：**

`UI move adapter (DFBFA0 / DF9EF0) → DE0E40 source reorder → DFA580 refresh`

以及另一條 persistence chain：

`DFB6E0 UI reorder commit → DFC6B0 persisted item-ID order producer`

目前**不能**直接寫成可呼叫 PoC，因為以下 ABI 尚未由 GitHub 現有證據確認：

- `DE0E40` 的精確 `this`、source index、target index/range 參數位置。
- `DFB6E0` 的 event/commit invocation context；目前只確認 prologue 會讀兩個 byte-sized stack args。
- `DFA580` 是否需要特定 owner/grid state 或前置條件。
- persistence 與 source reorder 兩條 chain 的精確相對時序。

因此下一個可執行 PoC gate 是：**一次 live drag capture 補齊 ABI + state delta**，不是直接 patch/call。

## 10. 固定結果格式

```text
STATUS = SUCCESS | NEED_USER_ACTION | BLOCKED | RECHECK
build/image-base =
session owner/grid/manager =
anchor gate =
CAPTURE_READY = yes/no
hit sequence =
DFB6E0 = hit/no-hit; this=; args=; caller=; return=
DFC6B0 = hit/no-hit; this=; args=; caller=; return=
DE0E40 = hit/no-hit; this=; args=; caller=; return=
DFA580 = hit/no-hit; this=; args=; caller=; return=
manager delta =
persisted-order delta =
layout-index delta =
grid-icon delta =
reorder entry =
persisted producer =
refresh tail =
minimal PoC =
next executable step =
```

## 11. 安全邊界

- 本 capture 全程唯讀。
- 不 patch、不寫 client memory。
- 不使用 Tripwire injection，除非 Reviewer 後續明確批准。
- 不因等待 client 而持續啟動 AI/model polling。
- 不把 elapsed time 當停止條件；有新增直接證據就可沿 evidence 繼續。
