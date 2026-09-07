# J.J.'s Blogs 天堂私服研究地圖（3.81C → 8.8C 驗證導向）

最後整理：2026-09-07 18:16

> J.J. 的 3.81C / L1J-3.80c 文章是 `research evidence`。位址、class、欄位、packet code、action ID、resource precedence、parser 行為都不能直接宣告為 8.8C 事實。8.8 必須以自身 source/resource、Ghidra/Argus、packet/runtime evidence 重驗。

## 1. Evidence labels

- `JJ_381_DIRECT`：舊版正文直接證據。
- `JJ_METHOD`：跨版本可重用研究方法。
- `HYP_880`：舊版證據導出的 8.8 可驗證假設。
- `STATIC_880`：8.8 source/binary/resource 靜態證據。
- `RUNTIME_880`：8.8 runtime/packet 直接證據。
- `CONFIRMED_880`：8.8 多證據閉環後才使用。

任何 `JJ_381_DIRECT` 不得自動升格 `CONFIRMED_880`。

## 2. 固定跨層研究鏈

```text
observable game/UI event
 -> DB/config/resource candidate
 -> server model/owner/handler
 -> packet/state transition
 -> client owner/resolver
 -> resource ID/name
 -> IDX/PAK/other entry
 -> decode/composite/render
 -> runtime visual/gameplay verification
```

逆向側：

```text
string/API/xref narrowing
 -> breakpoint
 -> what writes / what accesses
 -> caller chain / owner
 -> module/static anchor or rebind path
 -> controlled state transition
 -> evidence closure
```

## 3. Morph/GFX/Sprite resolver

### JJ_381_DIRECT

舊版證據顯示：
- morph ID 不必等於實際 SPR prefix，例如 `#5641 64=240`。
- client 原生 morph source 與 launcher 自訂 morph PAK 有 precedence/fallback 行為。
- action 可 local encode，也可 reference 另一 morph entry。
- SPR pixels/frames 本身不等於完整 animation semantics。
- timing、direction、weapon/effect/overlay、SFD offsets 可能共同決定遊戲畫面。

### HYP_880

8.8 resolver 應按：

```text
server/client visible identifier
 -> 8.8 morph/list/bin mapping
 -> optional remap / resolved sprite set
 -> action local/reference
 -> direction/variant
 -> SPR/resource entry
 -> timing/events/layers/offsets
```

`gfxid -> gfxid-action.spr` 只能是 fast path。

## 4. Animation timing 與 server interval 分離

舊 `spr_action` 模型含 `spr_id / act_id / framecount / framerate`，並被 server 用於 Move/Attack/Spell interval 類檢查。這提供資料模型，但不是 8.8 timing fact。

8.8 要分：
- visual frame sequence
- nominal client animation duration
- event/trigger frame
- server authoritative action interval
- optional framerate/parser state

需要同步 client animation capture + server/packet interval 才能確認。

## 5. Composite layers

舊 `105.clothes`、`106.weapon`、`109.effect` 與 SFDviewer 證明舊 client 可存在 body + overlay/weapon/projectile/impact 等多 layer；較高版本也可能把武器 baked into body。

8.8 Browser compositor 預留：
- base body
- N overlays
- optional weapon layer
- projectile/impact phase
- per-layer offsets
- frame/direction synchronization

但只有 runtime/resource evidence 確認後才啟用某 layer。

## 6. Resource Browser architecture

多篇舊工具文一致支持：

```text
container/index inventory
 -> search/filter/sort
 -> select one entry
 -> on-demand decode/preview
 -> optional frame play/step
 -> cache
```

而不是：

`export all SPR/TBT/IMG to PNG first`。

### 工具證據

- PakViewer Ver.3.0：跨 Sprite/Text/Tile inventory、搜尋、排序、on-demand preview。
- L1Viewer：literal/case-sensitive/regex/filter、內容全文檢索、TBT image-list、SPR play/step/current-total frame。
- Lineage Icon / MTools：TBT query/browse 與 SPR query 是不同任務。
- Pakext / PakViewe / PackViewer_beta2：IDX/PAK inventory、extract/import/delete 等舊 capability；Toolkit 目前只取 read-only architecture 方法。
- SFDviewer：frame X/Y 與兩層合成。
- SPR↔BMP/SFD：frame pixels 與 offset/meta 需成套處理。

## 7. Source provenance 是一級欄位

8.8 已知存在多組 Sprite IDX/PAK 與 duplicate source，因此 Browser entry 至少保存：

- requested logical ID/key
- entry name
- source IDX
- source PAK/container
- duplicate candidates
- precedence/selected source
- decode status
- mapping/reference chain

不能只保存裸 GFX ID。

## 8. Item Icon 與 Sprite resolver 分開

舊 TBT 工具與 `invgfx` 教學反覆支持 icon 與 sprite 是不同 resource task。

### 8.8 驗證鏈

```text
DB invgfx
 -> server item model/getter
 -> packet/icon field
 -> client icon ID
 -> icon resolver
 -> TBT/other icon resource
 -> preview
```

與：

```text
gfx/morph identifier
 -> mapping/action
 -> SPR/resource resolver
```

兩者共用 index/cache infrastructure，但 resolver、搜尋欄位、驗收條件分離。

## 9. Logical resource classes / override

3.81C override folders `icon / sprite / Surf / text / Tile` 只保留成研究抽象：

`format -> logical resource class -> candidate search roots -> loose override? -> packed fallback? -> precedence`。

8.8 要追 `Lin.bin / LinLogin.bin` 實際 file-open/read path，不能直接沿用舊目錄名。

## 10. Dialog：Client link 與 Server action 分界

### JJ_381_DIRECT

舊 HTML-like dialog 中：
- `link` 可做 client resource/page 跳轉。
- `action` 可進入 server-side action handling。
- `var src` 可顯示 server 注入資料。
- `<img src="#ID">` 走 client image resource resolver。

### HYP_880

8.8 應 capture：

```text
open dialog
 -> resource/template ID
 -> click link/action
 -> 是否 outbound packet?
 -> action key/arguments
 -> server dispatcher
 -> response/state
 -> next client resource/render
```

這能把純 Client UI navigation 與 Protocol/Server behavior 分開。

## 11. Dialog / NPC / XML / Item-use 的 Shared bridge

舊版案例提供：
- DB `npcaction` 類欄位 -> HTML basename。
- Teleporter：HTML action -> XML `Action Name` -> X/Y/Map/Heading/Price。
- item use -> server item-id handler -> dialog response resource。

8.8 驗證重點不是舊表名，而是找等價的：

`server state/config -> UI action identifier -> client resource`。

## 12. Inventory server model：新高價值研究方向

GM 金幣/創物、出生道具等舊版文章提供了很有用的 data-flow 模板：

```text
item template/config
 -> item instance materialization
 -> inventory owner/container
 -> stack merge OR new entry
 -> persistence
 -> client refresh/packet
```

舊 `storeItem` 的 stack/new 分支只是 `JJ_381_DIRECT`；8.8 要重新找到：
- authoritative inventory owner
- item collection/data structure
- stack-equivalence predicate
- insert/remove/update producer
- DB persistence timing
- outbound inventory refresh/update
- reorder/sort producer 是否與 add/remove 共用 generic container path

這直接服務目前背包 reorder 研究。

## 13. NPC spawn 端到端鏈

舊 GM 創怪/NPC：

`npc template + impl type -> spawn persistent row -> runtime instance -> client visibility`。

8.8 可與已知 gfx 研究合併成：

```text
NPC DB/template
 -> runtime NPC instance
 -> spawn/state packet
 -> gfx/morph identifier
 -> client resolver
 -> sprite/resource
```

要分 template identity、runtime object identity、resource identity。

## 14. Server Debug template

帳號 Debug、出生道具、GM command 系列形成固定方法：

```text
observable action
 -> DB/config candidate
 -> SQL/table/class literal
 -> model/object field
 -> Find Usages/caller
 -> service/clientpacket/command dispatcher
 -> breakpoint before predicate
 -> inspect/evaluate owner state
 -> step branch
 -> packet/DB mutation/visible result
```

可用 debugger 暫改 runtime variable 來驗證 branch，不必永久修改設定。

8.8 使用時只沿方法，不沿 Java path、line number、packet result code、舊欄位。

## 15. Command path 與 normal packet path 要分開

舊 `/who` vs GM `.who` 證明同一個可見功能可能有兩條入口：
- GM command dispatcher
- normal client packet handler

8.8 研究 UI/game action 時不能因找到一個 admin/debug path 就認定那是正常遊戲 producer。

## 16. Launcher / LinLogin 不應預設為單純啟動器

舊 Login_v380a research 顯示 launcher 生態可能包含：
- server list / name / endpoint / version metadata
- Login.ini 類設定
- packet encryption/key generation 選項
- movement packet compatibility option
- morph PAK generation/loading
- update/server-list update
- anti-cheat / multi-client 等

### HYP_880

對 8.8 要逐項驗證 `LinLogin.bin / Lin.bin / launcher` 的責任：

```text
endpoint config?
version negotiation?
crypto bootstrap?
resource/morph injection?
patch/update?
process launch/handoff only?
```

不可因 3.81C launcher 有某功能就假定 8.8 也有。

## 17. Network reachability 與 game protocol 分層

本輪 IP/LAN/WAN/firewall/NAT/DHCP 文章主要提供診斷分層方法：

```text
server bind/listen
 -> local firewall
 -> LAN address
 -> NAT/port-forward
 -> public/advertised endpoint
 -> launcher connect bootstrap
 -> transport/crypto/protocol
 -> login/game state
```

8.8 實際 transport/port 必須由 runtime socket/packet capture 確認；不能沿用舊 2000/TCP-UDP 範例。

## 18. Map：render 與 authoritative property 分開

舊 map viewer/converter/property 工具提示：client 可見 map/tile resource 與 server collision/region/map attributes 可能分層。

若未來研究 8.8 map：

`client render map/resource` 與 `server movement/collision/map state` 要分開建立 provenance，再以 coordinate transition 回驗。

## 19. Reverse engineering playbook

由 CE/x64dbg/OllyDBG 與 Server Debug 文抽出的固定流程：

1. 從可觀察事件、UI 字串、錯誤、API 或 xref 縮小範圍。
2. 只重現一個事件。
3. `what writes` 找 producer；`what accesses` 找 consumer/read path。
4. 記 caller stack、arguments/registers、owner pointer。
5. heap VA 只算 session evidence；追 static/module anchor 或 constructor/global owner。
6. 多級 pointer chain 逐層驗證。
7. breakpoint 前先 inspect/evaluate predicate；必要時控制 runtime variable 走指定 branch。
8. capture packet/DB/resource/state transition，閉環後才標 `CONFIRMED_880`。

## 20. 本輪 50 篇分類與累計

本輪：
- Client 17
- Server 9
- Protocol 9
- Shared 11
- Other 4

18:15 的 50 篇逐篇證據：`docs/JJ_RESEARCH_PROGRESS_20260907_1815.md`；本次 9 篇完整事實補錄與去重結果：`docs/JJ_RESEARCH_PROGRESS_20260907_1816.md`。

可追溯累計：**88 / 783**；剩餘 **695**。本次 9 篇均已在 16:08 或 18:15 ledger 記錄完成，因此不重複計數；本次只新增更完整的工具操作、格式、server/client 交叉資訊與待驗證標記。

## 21. Resource Tool Matrix：能力必須分層

**來源：J.J. Pakext / PakViewe / PackViewer_beta2 / MTools / XML 加解密 / SPZ、XML 加解密 / 對話檔加密解密 / 登入器素材抽檔 / Linskin4.04；狀態均為 `PUBLIC_FULL + COFFEE_RESOURCE_BLOCK`。**

**版本標記：九篇各自頁面未明列精確 client build；暫依周邊研究脈絡歸在 3.81C 工具鏈，但版本未確認。以下工具行為全數保留為舊版文章直接記錄，不因尚未適用 8.8 而刪除。**

舊工具的功能不是同一層：

| Tool | Container inventory | Search/filter | Preview/decrypt | Export/convert | Mutate/repack |
|---|---|---|---|---|---|
| Pakext | 單一 IDX | 欄位排序 | 未見正文證據 | extract | add/delete |
| PakViewe | 單一 IDX | filter/fuzzy search/sort | image/animation/text + zoom | export/export-to | add/update/delete |
| PackViewer_beta2 | PAK 或 client folder | type filter/search/sort | image/animation/text；XML auto-decrypt | export | add/delete；Save 實測疑似無效 |
| MTools | client directory / TBT package | 未見正文證據 | TBT image list | selected/all extract | 未見正文證據 |
| Linskin4.04 | PAK list | extension filter | 未見 browser preview 證據 | PAK extract、IMG↔BMP | 未見正文證據 |

### Toolkit 結論

8.8 Browser 的可重用 pipeline 應拆成：

`container inventory → metadata search/filter → on-demand preview/decrypt → export/convert → optional isolated mutation/repack`

前四層也不應綁死在同一 UI。第一個安全里程碑只實作唯讀 inventory/search/preview；add/update/delete/repack 必須另有隔離副本、round-trip 與 client load validation，不可因舊工具存在按鈕就視為 8.8 已支援。

## 22. XML / SPZ：ciphertext、plaintext 與名稱正規化是三個不同狀態

**來源版本：周邊研究定位為 3.81C，但這兩篇頁面未明列精確 client build；版本、8.8 cipher/loader 均未確認。**

舊版兩組工具提供互補證據：

- 單檔 XML 工具以 `_d.xml` / `_e.xml` 產生解密與加密輸出；作者警告反覆多次轉換會使內容錯亂。
- 批次 SPZ/XML 工具對同目錄檔案解密為 `.dec`，加密前要求輸入改為 `.e`；文章示範 `polymorphList.xml` 與 `list.spz` round trip 後 ciphertext 與原始內容一致。
- 舊版未加密 XML 直接吃檔會觸發 `XML Encryption Check(...)`；因此 parser 可讀性與 loader 接受性不能混為一談。

### 8.8 驗證要求

Browser row 至少保存：`packed/raw bytes`、`detected encryption state`、`decoded bytes/text`、`logical entry name`、`temporary conversion suffix`、`round-trip hash`。任何 3.81C suffix 或 cipher 都只能當工具 workflow 證據，不能直接套用到 8.8。

## 23. HTML dialogue：server basename 與 client resource 是可交叉驗證鏈

**來源版本：server SQL/path 符合周邊 L1J-3.80c 研究脈絡，但文章頁未明列精確 client build；版本、8.8 欄位與接受策略待驗。**

文章以 server `npcaction.normal_action = colusher` 找到 client `colusher.html`，修改並吃檔後由同一 NPC 對話畫面回驗；舊版 client 在該實驗中可接受加密或未加密 HTML。

這提供一條可重用研究鏈：

`NPC interaction → server dialogue basename → packet/action → client HTML entry → decode/render → visible text`

8.8 不得先假設欄位仍叫 `normal_action`，也不得先假設明文 HTML 仍可載入；應由已知 NPC 對話事件同步查 server lookup 與 client resource open。

## 24. Special-purpose extractors 不能冒充 generic loader evidence

`登入器素材抽檔.exe` 放在 client root 執行後輸出 `skin`，只證明該工具有專用素材抽取流程。它沒有提供「遊戲 client 實際從哪個 container/offset 讀取」的正文證據。

對 8.8 的規則：special extractor 的輸出可作 corpus/visual comparison，但 source provenance 仍要由 IDX/PAK inventory 或 runtime file-open trace建立。

## 25. 8.8 Resource Browser 最小驗收條件

由本輪工具矩陣導出的 acceptance criteria：

1. 任一 entry 都能顯示 logical name、source IDX、source PAK、offset/size、format、encryption/decode status。
2. 搜尋/排序只改 view，不改底層 entry order 或 container bytes。
3. 點選後才 decode；圖片、動畫、文字/XML preview 各自回報 decoder 與失敗原因。
4. 原始 bytes 與 decode result 分開 cache；同名 duplicate 不覆蓋 source identity。
5. XML/SPZ/HTML 顯示「可解析」不等於「client 可接受」；需要獨立 loader validation。
6. export/convert 的 round-trip 必須以 hash、entry metadata 與重新載入結果驗證。
7. add/update/delete/repack 不屬第一階段唯讀 PoC。

## 26. 最新下一批閱讀優先序

等待使用者明確下令後才執行；每批最多 10 篇，完整保存事實、去重計數、更新 checkpoint、commit、push 後停止。重讀既有文章不增加 88/783 基線。

1. 天堂私服尚未完成的 Server/Protocol 核心文章：優先 NPC / Item / packet/state 的正常遊戲路徑，擴充 server trace template。
2. x64dbg 36 / OllyDBG 16 / CE 10：逐篇完成 debugger playbook。
3. 補齊工具矩陣時先以 URL ledger 排除 18:15 已讀的 PakViewer 介紹、吃檔與 map/SPR 轉換文章。
4. x86 / 排序演算法：只抽能直接支援 binary/resource/inventory 的內容。
5. XML/MySQL/Java/C：只讀能補 DB/server/resource 鏈的文章。
6. Python/OpenCV：preview、GUI/threading、YOLO/vision 驗證與自動分類。
7. 最後補齊 783 篇 Archives title-level inventory 與逐篇閱讀狀態。
