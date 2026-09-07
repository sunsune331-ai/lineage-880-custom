# J.J.'s Blogs 天堂私服研究地圖（3.81C → 8.8C 驗證導向）

最後整理：2026-09-07 18:03

> 原則：J.J. 的 3.81C/L1J-3.80c 內容只作「舊版來源證據」。任何位址、載入優先序、action 編號、資料結構或 parser 行為都不得直接宣告為 8.8C 事實；8.8 必須用自己的 resource、Ghidra/Argus/runtime evidence 重驗。

## 1. 固定研究鏈

`遊戲可見行為 → DB/設定候選 → server source → packet/state → client owner → resource ID → IDX/PAK entry → decoder/compositor → 遊戲回驗`

逆向側固定使用：

`string/API/xref narrowing → breakpoint → what writes/accesses → caller chain → stable module/static anchor → runtime state transition`

## 2. 3.81C morph source precedence：直接證據

**來源版本：J.J. 3.81C；8.8 狀態：HYP_880。**

J.J. 實測：未使用登入器自訂變身檔時，client `Text.pak` 中真正影響變身的是 `list.spz`，單改 `list.spr` 沒效果；啟用登入器打包的自訂 morph PAK 後，自訂檔未定義項目不一定 fallback 到 client 原始 `list.spz`。

### 對 8.8 的直接影響

不要只問「morph/list 在哪」，要驗證：

`Lin.bin / LinLogin.bin / launcher → open 哪些 morph/list/bin/container → 載入順序 → duplicate ID precedence → missing-entry fallback`

建議用 resource open/read breakpoint + 單次變身事件建立 runtime trace；任何 controlled duplicate/fallback 測試只能在隔離副本進行。

## 3. GFX/poly/morph ID 與 sprite set ID 必須分層

**來源版本：3.81C；8.8 mapping 未確認。**

J.J. 的 `#5641 64=240` 例子證明變身 ID 可以解析到另一個 sprite set/file prefix。因此 Toolkit resolver 應為：

`server/client identifier → morph mapping → resolved sprite set → action/variant/direction → SPR entry`

`gfxid → gfxid-action.spr` 只能作 fast path，不能作唯一規則。

## 4. SPR frame 不是完整動畫

**來源版本：3.81C；8.8 encoding 待驗證。**

舊版動畫模型包含：SPR pixels、SFD X/Y offsets、morph action frame sequence、timing、framerate multiplier、overlay/weapon/effect layers。

所以 Resource Browser 應區分：
- Raw SPR preview：單檔 frames。
- Game-faithful preview：action sequence + timing + layers + offsets + direction/variant。

## 5. spr_action：舊版 server-side timing evidence

**來源版本：L1J-3.80c/3.81C；8.8 未確認。**

舊版流程：

`morph action encoding → spr_action.exe → spr_action.sql → DB spr_action`

欄位模型：`spr_id / act_id / framecount / framerate`。J.J. 顯示舊版 server 用它判斷移動/攻擊是否快於合法動畫時間；action encoding 可累加 frame count，未指定時舊版 framerate 預設為 24。

### HYP_880

搜尋 8.8 server 是否仍有 animation timing table/cache、movement/attack speed check，以及它是否能與 client action duration 閉環。若成立，這會成為 client resource ↔ server gameplay timing 的交叉證據。

## 6. 組合圖層：舊版直接證實

**來源版本：3.81C；8.8 layer encoding 未確認。**

`105.clothes` 實例包含 Death Knight body + 光刀、Ice Queen body + 發光 + 風雪。SFDviewer 又能同時開兩個 SFD、顯示每 frame X/Y 並合成。

因此 Toolkit compositor 應預留：base layer、N overlays、frame/direction synchronization、per-layer offsets、weapon/effect layer。這能解釋「抽出的 SPR 缺武器/特效，但遊戲畫面完整」。

## 7. Weapon generation difference

**來源版本：3.81C 教學比較不同 client generation。**

J.J. 指出某些版本 sprite 已把武器畫入 body，另一些靠 `106.weapon` 合成。因此 8.8 必須實測，不得預設。

驗證：選一個確定持武器外觀，比對 body pixels；改變 weapon state 時追 resource lookup / owner / render layer 是否新增資源。

## 8. Effect 是 presentation pipeline

**來源版本：3.81C。**

`109.effect` 可描述飛行呈現 + 命中效果，不宜只理解為「魔法」。8.8 schema 建議先用中性欄位：`presentation/effect layer → projectile? → impact? → timing/target semantics?`，再由 runtime 分類。

## 9. Action reference / alias

**來源版本：3.81C；8.8 parser/order 完全未驗證。**

`8=784` 類格式表示某 morph action 可引用另一 morph entry 的同 action。舊版另觀察到 forward reference/order 可能造成 crash。

對 8.8 的價值只有兩個可驗證假設：
- action table 可能有 alias/reference path，而非全部 inline。
- resolver 可能先建 ID table 再 resolve，或依序 resolve。

不得把舊版「必須放前面」直接套到 8.8。

## 10. PakViewer Ver.3.0：Browser 架構證據

**來源：J.J. PakViewer Ver.3.0 工具文章。**

文章描述：任選 IDX 後載入整組可解析 IDX；支援 All Sprite / Sprite..Sprite15 / Text / Tile、模糊搜尋、排序；選中 entry 後右側即時 preview；支援圖片、動態圖、文字與縮放。

### Toolkit 結論

主要互動路徑應為：

`IDX inventory cache → virtual entry list → search/filter → click → decode one entry → preview cache`

而不是：

`enumerate → export every SPR → hundreds of PNG → browser`

這與 8.8 既有量測「manifest/index 很快、SPR export 才是瓶頸」相符，但這裡是獨立的舊工具設計證據。

## 11. Item Icon：獨立 resolver

**來源版本：3.81C TBT/物品教學；8.8 mapping 待驗證。**

舊版 TBT 用於道具圖、魔法圖、人物狀態圖；物品 `invgfx` 對應 icon resource。

8.8 固定驗證鏈：

`DB invgfx → server getter → packet field → client icon ID → resource resolver → TBT/other icon entry → preview`

Item Browser 與 Monster GFX Browser 應是兩個 resolver，共用 container/index/cache infrastructure。

## 12. Reverse engineering checklist

由 x64dbg / OllyDBG / Cheat Engine 文章抽出的跨版本方法：

1. 從 UI/錯誤/狀態字串或預期 API/xref 縮小靜態範圍。
2. 重現單一遊戲事件。
3. `what writes` 找 producer，`what accesses` 找 consumer/read path。
4. 記 caller stack、register/arguments、owner pointer。
5. heap address 只算本 session evidence；追 module/static anchor 或 constructor/global owner。
6. 多級 pointer chain 逐層驗證，不因 pointer scan 命中就直接認定。
7. 回 runtime 驗證 state transition。

適合 8.8：current gfx/morph owner、selected target/NPC object、item icon ID、resource manager/index entry、animation/action state、renderer layer list。

## 13. Evidence labels

- `JJ_381_DIRECT`：J.J. 舊版正文直接證據。
- `JJ_METHOD`：跨版本可重用方法。
- `HYP_880`：由舊版導出的 8.8 可驗證假設。
- `STATIC_880`：8.8 binary/resource 靜態證據。
- `RUNTIME_880`：8.8 runtime 直接證據。
- `CONFIRMED_880`：8.8 證據閉環後才使用。

任何 `JJ_381_DIRECT` 都不能自動升格成 `CONFIRMED_880`。

## 14. L1Viewer：搜尋 + 播放 + 逐幀的 Browser interaction model

**來源：J.J. `L1Viewer 工具`；狀態：PUBLIC_FULL + COFFEE_RESOURCE_BLOCK。**

來源事實：
- 選取 client directory 後建立文件列表。
- 名稱搜尋支援所有單詞匹配、大小寫區分、regular expression、previous/next、filter。
- 另有文件內容全文檢索。
- TBT 可切成 image-file list。
- SPR 可播放，並可逐幀前進/後退；介面顯示 current frame / total frames。

### HYP_880 / Toolkit 驗證

Resource Browser 的交互面應分成：

`inventory metadata search`
`+ format filter`
`+ text-content search`
`+ on-demand preview`
`+ SPR step/play`

8.8 是否能對全部 IDX entry 直接做到 regex/full-text 搜尋仍需量測；舊工具功能只證明 UX/架構可行，不是 8.8 效能證據。

## 15. Lineage Icon：TBT 與 SPR 是不同工作流

**來源：J.J. `Lineage Icon v120119 工具`；狀態：PUBLIC_FULL + COFFEE_RESOURCE_BLOCK。**

來源事實：工具主要分為 `TBT查詢 / TBT瀏覽 / SPR查詢`。TBT 可依 ID 查詢、瀏覽與匯出；SPR 查詢則要求指定 client directory。

### Toolkit 影響

這再一次支持：

- `Item Icon Browser`：item/invgfx → icon resolver → TBT/other icon entry。
- `Sprite Browser`：gfx/morph → mapping/action → SPR entry。

兩者可共用 container/index/cache infrastructure，但 resolver、搜尋欄位與驗收條件應分離。

## 16. IDX / PAK：source provenance 必須是一級欄位

**來源版本：3.81C；8.8 mapping/precedence 未確認。**

`客戶端idx、pak說明`把 IDX 當 entry index，PAK 當實際資源容器；例子顯示 IDX 可看到 `10306-24.spr` 這類 entry 名，工具可由對應 PAK 解碼成多 frame。

### 8.8 直接設計要求

由於 8.8 已知存在多組 Sprite IDX/PAK 且有 duplicate source，Browser row 應保存：

- requested resource key
- entry name
- source IDX
- source PAK
- duplicate candidates
- selected/precedence source
- decode status

只保存裸 GFX ID 會失去 debug loader precedence 所需證據。

## 17. 補丁資料夾只作 logical resource class，不作 8.8 固定路徑

**來源版本：3.81C。**

舊版 override root 分成：`icon / sprite / Surf / text / Tile`，各自對應 TBT/ICO、SPR、IMG、HTML/TBL/list、XML/TIL 等類型。

### JJ_METHOD

保留的是：

`format → logical resource class → search root / packed fallback / override precedence`

### HYP_880

對 8.8 應追 `Lin.bin / LinLogin.bin` 實際 file-open/read calls，建立：

`resource class → candidate roots → loose override? → packed source? → duplicate precedence → fallback`

不得直接假設 8.8 仍讀 3.81C 同名資料夾。

## 18. Server Debug：DB → object → caller → predicate → packet/state

**來源：`遊戲帳號分析/Debug (一)(二)(三)`；來源版本 L1J-3.80c。**

三篇連續文章形成完整方法：

```text
observable game action
  → DB table / config candidate
  → search SQL/table literal
  → model/object fields
  → Find Usages / caller
  → clientpacket / service entry
  → breakpoint before condition
  → Evaluate Expression / owner state
  → Step Over / Step Into / Step Out
  → outbound packet / DB mutation / visible result
```

其中還有兩個可重用技巧：
- Debugger 中暫改 runtime variable 以強制走特定 branch，不必永久改 config。
- 在真正執行 branch 前用 Evaluate Expression 先驗證 predicate，再觀察 packet/result/state transition。

### 8.8 套用規則

可直接沿用的是研究方法，不是 Java path、line number、packet result code、DB 欄位名或 branch order。

適用於 8.8：NPC spawn/create、item create/add、gfx/invgfx 傳遞、login/selection state、skill effect、resource request 等任何能從可觀察事件回追 server chain 的問題。

## 19. 最新下一批閱讀優先序

1. 天堂私服工具介紹尚未讀：Pakext / PakViewe / PackViewer_beta2 / MTools / XML-SPZ-HTML crypto / Linskin 等，完成 Resource Tool Matrix。
2. 天堂私服核心分析/修改：優先 NPC / Item / GM create / packet state，擴充 server trace template。
3. x64dbg 36 / OllyDBG 16 / CE 10：debugger playbook。
4. x86 / 排序演算法：只抽能直接支援 binary/resource/inventory 的內容。
5. XML/MySQL/Java/C：只讀能補 DB/server/resource 鏈的文章。
6. Python/OpenCV：preview、GUI/threading、YOLO/vision 驗證與自動分類。
7. 最後補齊 783 篇 Archives title-level inventory 與逐篇閱讀狀態。
