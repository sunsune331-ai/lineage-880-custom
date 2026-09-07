# J.J.'s Blogs 天堂私服研究地圖（3.81C → 8.8C 驗證導向）

最後整理：2026-09-07

## 目的

這份文件不是把 3.81C 結論直接套到 8.8C，而是把 J.J.'s Blogs「天堂私服架設教學」公開內容的**研究流程、資料流、檔案角色與驗證方法**整理成 8.8 專案可重複使用的導航圖。

來源主目錄：
- https://morosedog.gitlab.io/private-lineage-20210712-private-lineage-0/

目前已系統瀏覽的公開範圍：
- 初出茅廬 / Server-Client / Java / DB / 啟動與連線
- 登入器相關
- IDE / 編譯 / Debug / Remote Debug / Jar 反編譯
- L1J 核心分析與 GM 指令系列
- 客戶端 IDX/PAK、補丁格式、吃檔
- PakViewer / Pakext / PakViewe / L1Viewer / LineageSpr / Lineage Icon / MTools 等工具介紹
- XML/SPZ/對話檔加解密、TBT/IMG/SPR 轉檔、SFDviewer
- NPC 對話檔與 action 對應
- 變身檔快速入門、gfxid/polyid、spr_action、SPR 基礎
- 變身檔進階：格式、物件、濾鏡、附加物件、武器、魔法、framerate、walk/attack/spell/damage/get、指向指令
- XML 基礎公開入口

會員/加密內容只能使用公開可見段落與標題，不能視為已取得內文。

---

## 1. 這系列真正值得保留的「研究方法」

文章跨很多主題，但反覆使用同一種可靠流程：

1. **先觀察遊戲內可見行為**
   - 角色身上物品、NPC、動畫、對話、GM 指令效果。
2. **從資料庫命名/欄位找候選**
   - 例如 beginner、character_items、npcaction、gfxid、polyid、invgfx。
3. **用資料庫結果反查 server source**
   - 搜表名、欄位名、class_name、SQL 字串。
4. **用 Debug 驗證實際 runtime 路徑**
   - 不只看名稱，確認真的被呼叫、參數和 state 如何變。
5. **把 server 資料流接到 client 可見結果**
   - packet / gfx / poly / action / HTML / resource ID。
6. **再從 client resource container 找實際檔案**
   - IDX → PAK → list / SPR / TBT / IMG / HTML / XML。
7. **最後回遊戲驗證**
   - 改一個變數、做單一操作、比對實際畫面。

### 對 8.8 的固定翻譯

任何 3.81C 教學都只保留「方法」與「角色」，不直接保留位址/檔名/欄位必然性：

`3.81 假設 → 在 8.8 找等價資料來源 → 靜態證據 → runtime evidence → 寫入 Function Map`

---

## 2. Server ↔ Client 的總體觀念

文章的基本模型是：

`資料庫/Server state → Java 處理 → client request/response → client resource lookup → 顯示`

對目前專案，這個方法已經被多次證實有效：
- NPC：DB `gfxid` → server instance → packet → client resource。
- Item：DB `invgfx` → server item model/packet → client icon resource。
- 對話：`npcaction` → HTML 檔名 → Text resource → UI action。

因此遇到「畫面上看得到，但不知道資料從哪來」時，不應只在 client 盲找；要同時建立：

`DB field → server getter → packet / state → client model → resource ID → resource file`

---

## 3. 客戶端資源容器：IDX / PAK

3.81C 文章將 IDX 視為資源索引、PAK 視為實際壓縮資源容器；客戶端透過 IDX 定位 PAK 內資源。

已整理到的舊版分類：
- `Sprite*.idx/pak`：SPR、TBT、PNG、IMG 等。
- `Text.idx/pak`：HTML、TBL、`list.spr`、`list.spz` 等。
- `Tile.idx/pak`：地圖 tile。
- LiTo 另有對應 LTicon/LTsprite/LTsurf/LTtext/LTtile。

### 對 8.8 的意義

8.8 已實際存在多個 `Sprite*.idx/pak`；因此工具設計應優先：

`enumerate IDX → resolve source PAK → decode on demand → preview`

而不是：

`先把所有 SPR 全部轉 PNG 才能瀏覽`

這也解釋 PakViewer GUI 為何能比批次 CLI export 快很多：GUI 是按需 decode/preview，批次輸出則需要大量完整解碼與檔案 I/O。

---

## 4. Item Icon：invgfx → TBT

舊版文章提供了很具體的鏈：

- `list.ico`：控制可顯示的 icon 編號範圍。
- `*.tbt`：遊戲中的道具圖、魔法圖、狀態圖。
- `weapon/armor/... .invgfx`：對應 icon ID。

例子中 `invgfx` 直接找同號 `.tbt`。

### 8.8 不直接假設同號，但應照此順序驗證

1. DB `invgfx`
2. server item getter / packet field
3. client 收到的 icon ID
4. 8.8 是否仍有 list/icon range 類控制資料
5. resource index 中同號/映射後的 TBT/icon entry
6. preview

### 對 Toolkit

Item Browser 不應沿用 GFX→SPR 規則；應獨立建立：

`item_id/name/invgfx → icon-resource resolver → TBT decoder/preview`

---

## 5. 變身編號、圖檔編號、gfxid、polyid 必須分層

舊版最重要的觀念之一：

- **變身編號**：`list.spr/list.spz` 中的 `#ID`。
- **圖檔編號**：實際 SPR filename prefix。
- `#240 64`：未指定 remap 時，可使用同號 240 圖檔。
- `#5641 64=240`：變身 ID 是 5641，但實際圖檔編號為 240。

因此：

`transform/poly ID != 必然等於 SPR prefix`

### 對我們之前 GFX Browser 的修正

原本：

`gfxid → gfxid-action.spr`

應升級成：

`server/client gfx/poly identifier`
`→ 8.8 morph/list mapping（若存在）`
`→ resolved sprite set ID`
`→ action/direction SPR`

同號只能視為 fast path，不是唯一規則。

---

## 6. 客戶端 vs 登入器：誰提供變身資料

舊版文章區分：

1. 客戶端內 `Text.pak` 的 `list.spr/list.spz`。
2. 私服登入器自訂變身檔，打包成自訂 PAK 後由登入器設定載入。

作者也特別指出某些版本甚至直接用 `lin.bin` 登入；而 8.8 本身就是不同 generation，不能假定 3.81 的載入優先序。

### 8.8 要驗證的真正問題

- `Lin.bin / LinLogin.bin` 到底讀哪個 list / bin / packed morph table？
- client 原生 morph table 和 launcher custom resource 是否共存？
- precedence / fallback 是什麼？
- 重複 ID 時誰覆蓋誰？

這比只問「SPR 在哪個 PAK」更重要。

---

## 7. SPR 本身不是完整動畫邏輯

文章用 SPR→BMP/SFD 說明：

- 一個 SPR 可以含多個 frame。
- `.sfd` 保存 frame count、透明色、每 frame X/Y offset。
- SPR frame 集合本身不等同 GIF 自動播放。
- 真正 frame sequence / timing 由變身動作編碼控制。

因此 Toolkit 若要「像遊戲一樣播放」不能只做：

`SPR frame0 → frame1 → frame2 固定速度`

而應做：

`resolved morph action`
`→ frame references`
`→ per-frame timing`
`→ overlay/object references`
`→ position offsets`
`→ composite preview`

---

## 8. 方向性：Action 與 SPR filename 不是單純 action number

舊版例子指出同一種動作有 8 個方向，SPR 圖檔編號按方向排列，例如某組 `-0 ... -7` 對應：

1. 左上
2. 上
3. 右上
4. 右
5. 右下
6. 下
7. 左下
8. 左

這表示我們目前看到的 `13715-18.spr` 之類 filename suffix，不能只叫「Action 18」就結束；最終需要理解它在該 sprite set 中是：

`動作類型 × 武器狀態 × 方向 / variant`

8.8 的實際編號配置仍需重新建表。

---

## 9. 動作語義：建立 Action Dictionary，而不是裸數字

舊版變身檔明確定義多種 action 名稱。重要例子：

- `0.walk`：走路
- `1.attack`：攻擊
- `2.damage`：受擊/僵直
- `15.get`：撿取
- `18.spell dir`：有方向施法
- `19.spell nodir`：無方向施法
- 還有不同武器 walk/attack/damage、fishing、alt attack 等大量 action。

### Toolkit 應做

8.8 Resource Browser 最終顯示：

`Action 18` → `候選語義：spell dir（需 8.8 驗證）`

並保存：
- 來源版本
- 是否在 8.8 實際命中
- frame count
- direction count
- weapon variant

不要把 3.81 action 名稱直接標成 8.8 CONFIRMED。

---

## 10. Frame timing / spr_action

舊版流程：

`morph list → spr_action.exe → spr_action.sql → DB spr_action`

欄位包括：
- `spr_id`
- `act_id`
- `framecount`
- `framerate`

文章以 `1 unit = 1/24 sec` 解釋 timing，並有 `110.framerate(X)`：`X/24` 作為倍率的舊版規則。

### 對 8.8 的用法

這提供的是非常好的**資料模型**：

`SpriteSet + Action → frame sequence/time → effective animation speed`

但 8.8 是否仍由 server `spr_action` 控制、欄位是否一致、client 是否內建 timing，全部需實測。

---

## 11. 組合圖：答案是「確實可能」

這一點直接回答之前的疑問。

舊版 `105.clothes(X X)` 被解釋為附加物件：

`105.clothes(物件數量, 物件變身/圖號)`

例子：死亡騎士本體與光刀分開存在；兩個 SPR 的 frame 張數對齊，再依 SFD 座標疊合。

因此某些畫面不是單一 SPR，而是：

`base body SPR`
`+ weapon / clothes / overlay SPR`
`+ effects`

### 對 8.8 Browser

必須預留：
- base layer
- additional object layers
- per-layer SFD/frame offsets
- direction synchronization
- frame count synchronization

否則會出現「抽出來只有背面/沒武器/缺特效，但遊戲裡完整」的現象。

---

## 12. Weapon / Effect / Object Type 不是 decoration，而是行為資料

舊版進階指令包含：

- `102.type(X)`：物件類型（player/NPC/MOB/item/door/shadow/effect 等）
- `105.clothes(...)`：附加 layer
- `106.weapon(...)`：武器資源/狀態
- `109.effect(...)`：魔法/效果
- `110.framerate(...)`：動畫速率

這表示 morph/list 不是單純「sprite filename table」，而更像一份 client-side rendering/action schema。

### 8.8 研究方向

不要只搜尋 `gfxid → SPR`；應搜尋：

`morph entry → type → base sprite → action → weapon/overlay/effect → timing`

---

## 13. 指向/繼承：Action 可以引用另一個變身 entry

舊版文章最值得保留的另一個規則：

`8=784`

表示目前變身 entry 的 action 8，直接引用另一個**變身編號 #784 的 action 8 定義與圖檔路徑**；784 不是單純 sprite filename。

因此解析器必須支援：

`action definition`
- inline action
- redirect/reference action
- recursive resolve
- cycle detection

這也是「為什麼看某個 GFX 自己的 SPR 集合會覺得缺動作」的重要候選原因。

---

## 14. DB gfxid / polyid 的研究方法

文章不是只看 `npc.gfxid`，而是先掃整個 schema 找含 gfx/poly 的欄位，再按用途分類。例如舊版有：
- npc gfx / transform gfx
- door
- mobskill
- trap
- weapon skill
- armor set poly
- character buff poly
- polymorphs poly

### 對 8.8 的固定方法

遇到新視覺效果時先：

```sql
INFORMATION_SCHEMA.COLUMNS
→ 搜 gfx / poly / invgfx / effect / icon / sprite 類欄位
```

再從欄位反查 server code。

這比只在 `npc` 表猜欄位效率高很多。

---

## 15. Server-side 文章對我們最大的價值：不是舊 class name，而是追查套路

例如人物出生物品系列採用：

`遊戲觀察 → beginner table → 搜 SQL 字串 → Beginner.java → character_items → etcitem/weapon/armor → runtime 驗證`

GM 指令系列則常用：

`commands table → class_name → command executor → utility/model → packet/state change`

### 8.8 以後 server feature 都應先用此模式

不要第一步全專案 grep「功能中文名稱」；先找到**資料表/設定/封包/command registry 的穩定入口**。

---

## 16. 對話檔系列提供的跨層追蹤模式

舊版：

`spawnlist_npc / NPC identity`
`→ npcaction`
`→ normal_action / teleport_url`
`→ Text PAK HTML`
`→ link/action`
`→ server-side NPC action handler`

這個模式可泛化到 8.8 UI：

`UI name / XML component`
`→ callback binder`
`→ handler`
`→ state/model`
`→ packet/server action`

這和目前 `RenewalInventoryUI` 的逆向方法本質上相同。

---

## 17. 對目前「背包整理」的具體幫助

J.J. 系列沒有直接提供 8.8 背包 sort/reorder 函式，但它強化了一個重要判斷：

**先判定功能屬於哪一層，再追真正 producer，不要從畫面結果亂猜。**

我們目前背包整理主線：

`RenewalInventoryUI / InventoryItemGrid`
`→ source/order state`
`→ reorder producer`
`→ persisted order`
`→ refresh/reconcile`

下一步仍是 live capture：
- `DFB6E0`
- `DFC6B0`
- `DE0E40`
- `DFA580`

這網站不能替代 runtime evidence，但它支持我們現在使用的研究方式，而不是回頭盲掃 UI 座標欄位。

---

## 18. 對 Resource Toolkit 的直接設計修正

### GFX/Sprite Browser 應從 v1 升級成 v2 模型

舊：

`GFX ID → 同號 SPR → frames`

新：

`DB/server GFX/poly`
`→ client morph/list entry`
`→ sprite ID remap`
`→ action definition`
`→ action redirect resolve`
`→ direction/weapon variant`
`→ base SPR`
`→ overlays/clothes/weapon/effect`
`→ frame timing + SFD offsets`
`→ composite preview`

### Item Browser

`item table`
`→ invgfx`
`→ icon range/list metadata`
`→ TBT/icon resource`
`→ preview`

兩個 Browser 不再混用 resolver。

---

## 19. 8.8 驗證清單

下列全部標成「舊版提供方向、8.8 必須驗證」：

- [ ] 8.8 morph/list 真正來源檔名與 container
- [ ] Lin.bin / LinLogin.bin morph table precedence
- [ ] GFX/poly → sprite set remap 格式
- [ ] action redirect/reference 語法是否仍存在
- [ ] 8 directional numbering 是否仍一致
- [ ] action ID dictionary 是否仍相容
- [ ] overlay/clothes/weapon/effect schema 是否存在或已換格式
- [ ] SFD/SPR offsets 是否仍由同類結構提供
- [ ] frame timing 是否仍以 1/24 為基準
- [ ] server `spr_action` 在 8.8 是否仍參與 gameplay/action timing
- [ ] invgfx → TBT/icon resource 的實際 8.8 resolver
- [ ] list.ico 或等價 icon range metadata

---

## 20. 以後避免「一直找方法」的固定決策樹

### 問題：遊戲畫面上的東西不知道從哪來

1. 是 server state 還是純 client resource？
2. DB 有沒有對應 ID/欄位？
3. server 有沒有把 ID 送給 client？
4. client model 收到後存在哪？
5. renderer 用的是原始 ID、remap ID 還是 action reference？
6. resource index 實際 resolve 到哪個 entry？
7. 是否是 composite layer？
8. 是否由外部 action/timing schema 控制？
9. 用一次最小 runtime 操作驗證。

### 禁止重走的低效路線

- 只因 filename 同號就認定 GFX=SPR。
- 只看某一張 SPR 就認定遊戲完整畫面應該長那樣。
- 只靠 UI object raw field 猜 order/action producer。
- 因 3.81 有某欄位/檔名就直接套 8.8。
- 大範圍盲掃後沒有 before/after state 對照。

---

## 結論

J.J.'s Blogs 對 8.8 最有價值的不是舊版位址或 class name，而是建立了完整的跨層思考：

`DB → Server → Packet/State → Client Model → Morph/Action Schema → Resource Index → Sprite/Icon → Render`

以及：

`遊戲觀察 → 找穩定識別碼 → 靜態追查 → runtime 驗證 → 最小修改 → 回遊戲驗收`

後續 8.8 的研究應優先沿這兩條框架前進；3.81 文章中的 ID、格式與命名只作 hypothesis，不作 CONFIRMED evidence。
