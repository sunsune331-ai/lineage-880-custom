# Inventory Reorder Capture Result

> 這是固定結果模板。Live capture 前保持 `PENDING`；Executor 完成一次拖放 capture 後覆寫本檔。未知值必須填 `UNCONFIRMED`，不得猜測。

STATUS = PENDING

## Session

- timestamp =
- client build =
- PID =
- image base =
- module size =
- RenewalInventoryUI =
- InventoryItemGrid =
- InvWindow =
- inventory manager =

## Anchor Gate

| Anchor | resolved VA | signature hits | bytes verified | status |
|---|---:|---:|---|---|
| DFB6E0 | | | | PENDING |
| DFC6B0 | | | | PENDING |
| DE0E40 | | | | PENDING |
| DFA580 | | | | PENDING |

CAPTURE_READY = no

## BEFORE

### manager+0x54 source/order

- begin/end/capacity =
- size =
- sequence =

### RenewalInventoryUI+0x16C persisted item-ID order

- begin/end/capacity =
- size =
- sequence =

### InventoryItemGrid+0x1BC layout-index

- begin/end/capacity =
- size =
- sequence =

### InventoryItemGrid+0x190 grid icons

- begin/end/capacity =
- size =
- sequence =

## Execute Hit Sequence

| seq | anchor | EIP | ECX/this | stack args | return | caller | notes |
|---:|---|---:|---:|---|---:|---:|---|

## AFTER

### manager+0x54 source/order

- begin/end/capacity =
- size =
- sequence =
- delta =

### RenewalInventoryUI+0x16C persisted item-ID order

- begin/end/capacity =
- size =
- sequence =
- delta =

### InventoryItemGrid+0x1BC layout-index

- begin/end/capacity =
- size =
- sequence =
- delta =

### InventoryItemGrid+0x190 grid icons

- begin/end/capacity =
- size =
- sequence =
- delta =

## Verdict

- reorder entry = UNCONFIRMED
- reorder ABI = UNCONFIRMED
- DFB6E0 role = UNCONFIRMED
- DFC6B0 role = UNCONFIRMED
- persisted-order timing = UNCONFIRMED
- refresh tail = UNCONFIRMED
- minimal PoC call sequence = UNCONFIRMED

## Next Executable Step

PENDING
