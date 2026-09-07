# J.J.'s Blogs 天堂私服研究地圖補充 — 2026-09-07 16:23

> 本檔是 `JJ_LINEAGE_RESEARCH_MAP.md` 的本輪增量。來源文章版本為 Lineage 3.81C / L1J-3.80c；以下只把「資料模型 / runtime 行為 / 驗證方法」翻譯成 8.8C 待驗證項，**不沿用舊版位址與版本特定常數**。

## 本輪新增完整正文證據

### 1. `spr_action` 不是獨立真相，而是由 morph/action 編碼導出的 server-side timing model

來源：`天堂私服 | 資料庫中 spr_action 分析`
- URL: https://morosedog.gitlab.io/private-lineage-20220515-private-lineage-110/
- Access: PUBLIC_FULL
- Source version: Lineage 3.81C / L1J-3.80c

舊版事實：
- `spr_action.spr_id` 對應變身編號，而不是必然等於實際 SPR prefix。
- `act_id` 對應 morph entry 的動作編碼。
- `framecount` 由動作編碼中每段圖片張數/時間單位推導。
- `framerate` 沒有額外 `110.framerate()` 時預設為 24；存在加速指令時使用該設定。
- server 核心使用這些資料判斷攻擊、移動、施法速度是否異常。

8.8 驗證點：
1. 查 8.8 server 是否仍有 `spr_action` 或等價 timing table/cache。
2. 若有，確認 key 是 morph ID、resolved sprite set ID 還是 client GFX ID。
3. 把 client 實際動畫時間與 server interval gate 分開量測，避免把「畫面播放快」誤判成「server 允許更快操作」。
4. Resource Browser 可保留 `action -> frame sequence -> nominal timing` 欄位，但不得把 3.81C 的 `1 unit = 1/24 sec` 直接標成 8.8 confirmed。

### 2. `110.framerate` 有狀態延續語義，不能只視為單一 action 屬性

來源：`天堂私服 | 變身檔-加速指令的教學`
- URL: https://morosedog.gitlab.io/private-lineage-20220524-private-lineage-119/
- Access: PUBLIC_FULL
- Source version: Lineage 3.81C

舊版事實：
- `110.framerate(24)` 等同原速。
- `110.framerate(X)` 會影響後續動作編碼，除非之後重新設定回原速。
- 官方某些高速變身並非透過這個指令實作。

8.8 驗證點：
- 如果 8.8 存在等價 morph bytecode/parser，解析器要允許「狀態型指令」，不能假設每個 action 都自包含全部 timing metadata。
- 逆向時應找 parser state / current framerate context，而不是只找 `Action -> FPS` 靜態表。

### 3. 武器 layer 在版本演進中可能由 external overlay 轉成 baked-in sprite

來源：`天堂私服 | 變身檔-武器指令的教學`
- URL: https://morosedog.gitlab.io/private-lineage-20220522-private-lineage-117/
- Access: PUBLIC_FULL
- Source version: Lineage 3.81C

舊版事實：
- `106.weapon` 主要是低版本資源模型。
- 文章指出較高版本圖檔多數已直接帶武器。
- 把舊式 weapon overlay 強套到高版本編碼會出現武器重疊/殘影。

8.8 影響：
- Composite renderer 必須支援「optional overlay」，不能預設每個武器狀態都一定需要額外 weapon SPR。
- 對 8.8 的正確流程應是：先單體 SPR 預覽 → 與遊戲畫面比對 → 只有確認缺 layer 才追 `weapon/clothes/effect` mapping。
- 這能降低把「已 baked-in 的武器」再次疊圖造成錯誤預覽的風險。

### 4. `109.effect` 更像 projectile/impact mapping，不只是 decoration

來源：`天堂私服 | 變身檔-魔法效果的教學`
- URL: https://morosedog.gitlab.io/private-lineage-20220523-private-lineage-118/
- Access: PUBLIC_FULL
- Source version: Lineage 3.81C / L1J-3.80c

舊版事實：
- 文章把 `109.effect(X X)` 重解讀為飛行效果 + 命中後效果設定。
- 第一個值會影響飛行動畫方向/呈現，第二個值偏向 hit/impact effect。
- 不只魔法使用，遠距武器箭矢效果也能使用。
- 舊版另有 `fly` / `fire` / `animation` 等不同效果型態。

8.8 驗證點：
- Browser 的 effect preview 應把 `projectile`、`impact`、`caster/body animation` 分成不同 layer/phase。
- 查 8.8 skill DB 的 `castgfx/castgfx2` 或等價欄位，沿 `DB -> packet -> client effect resolver -> sprite/effect resource` 建鏈。

### 5. Walk / Attack / Spell / Damage 的「圖片張數」與「總時間」是不同維度

來源：
- https://morosedog.gitlab.io/private-lineage-20220525-private-lineage-120/ — walk
- https://morosedog.gitlab.io/private-lineage-20220526-private-lineage-121/ — attack
- https://morosedog.gitlab.io/private-lineage-20220527-private-lineage-122/ — spell
- https://morosedog.gitlab.io/private-lineage-20220528-private-lineage-123/ — damage
- Access: PUBLIC_FULL
- Source version: Lineage 3.81C / L1J-3.80c

舊版共同事實：
- 動畫可用較少 frame，但保持總時間不變。
- server 有 Move / Attack / Spell interval 檢查；縮短 client 動畫總時間可能造成回溯、偵測或退回。
- attack/damage 等事件存在「某一 frame 觸發聲音/受擊表現」的語義，不能只把 frame 當純視覺序列。
- `18.spell dir` / `19.spell nodir` 缺失時，文章記錄舊版 BIN 會 fallback 到 `0.0:1`。

8.8 影響：
- Action model 應至少區分：`visual frame sequence`、`nominal duration`、`event trigger frame`、`server authoritative interval`。
- 缺 action 時不要立刻判定「資源不存在」；要檢查 client 是否有 fallback/default action。
- 對 8.8 runtime capture：一次只測 Move / Attack / Spell 其中一類，記錄 client animation start/end 與 server packet/ack interval，避免混淆。

### 6. `15.get` 也證明 action 可被移除或縮成單 frame，且事件 trigger 與 visual sequence 可分離

來源：`天堂私服 | 變身檔-撿取分析與修改`
- URL: https://morosedog.gitlab.io/private-lineage-20220529-private-lineage-124/
- Access: PUBLIC_FULL
- Source version: Lineage 3.81C

舊版事實：
- `15.get` 可以被移除或縮成 1 frame。
- 文章建議 2 frame 時由第二張觸發撿取動作與聲音。

8.8 驗證點：
- 把 `get/pickup` 納入 Action Dictionary。
- Resource Browser 未來若做可播放預覽，要能標示「事件 frame」候選，而不是只有圖片列表。

### 7. Action 可以跨 morph ID 指向另一個 entry；resolver 需要遞迴/依賴解析

來源：`天堂私服 | 變身檔-指向指令分析與修改`
- URL: https://morosedog.gitlab.io/private-lineage-20220530-private-lineage-125/
- Access: PUBLIC_FULL
- Source version: Lineage 3.81C

舊版事實：
- 例如 `8=784` 表示目前 morph 的 `8.death` 直接使用 `#784` 的死亡動作編碼與其圖檔。
- 指向的是「另一個變身編號」，不是單純把 suffix 改成另一個 SPR number。
- 文章實驗顯示某些 forward reference 會崩潰，而 backward reference 可成功；該限制屬舊版 parser 行為。

8.8 重大影響：
- GFX/morph resolver 不能只做單層 `ID -> sprite set`。
- 需要資料模型：
  `morph ID -> action -> local action encoding | reference(other morph/action) -> resolved sprite set/frame sequence`。
- 應加入 cycle detection / missing target / invalid ordering 診斷。
- 這是目前解釋「某 GFX 找得到 entry，但特定 action 看起來引用別組圖片」的重要候選機制。

### 8. 無完整 morph 編碼時，單一 SPR 仍可能顯示，但角色行為不完整

來源：`天堂私服 | GM指令使用 (人物變身)`
- URL: https://morosedog.gitlab.io/private-lineage-20220510-private-lineage-105/
- Access: PUBLIC_FULL
- Source version: Lineage 3.81C

舊版事實：
- 沒有變身編碼時可只顯示某一張 SPR frame，但無法正常移動/轉向/攻擊，施法也可能沒有角色動畫。

8.8 影響：
- 「能解碼出 PNG」只能證明資源存在，不能證明 GFX/morph/action mapping 完整。
- Toolkit 驗收應分成：`resource decode PASS`、`action resolver PASS`、`composite/runtime equivalence PASS` 三層。

## 本輪形成的 8.8 Resource Browser resolver 草案

```text
server/client visible ID
  -> morph/gfx mapping
  -> morph entry
     -> optional sprite-set remap
     -> per-action local encoding OR action reference
     -> stateful timing directives
     -> body frames
     -> optional baked-in / overlay weapon decision
     -> optional projectile/effect phases
     -> per-frame offset/timing/event trigger
  -> composite preview
```

### 必要診斷欄位

- source container / entry
- requested ID
- resolved morph ID
- resolved sprite set ID
- action ID + candidate semantic
- local vs referenced action
- reference chain
- frame count
- nominal timing source
- event-trigger frame candidate
- base / overlay / projectile / impact layers
- fallback used? yes/no
- source-version confidence (`3.81C hypothesis`, `8.8 static`, `8.8 runtime confirmed`)

## 下一輪優先

1. 把剩餘 morph 章節的格式/type/attr 等正文補齊到同一資料模型。
2. 精讀 PakViewer / LineageSpr / Icon / SFDviewer 工具功能，判斷哪些可直接轉成 Toolkit decoder/preview acceptance criteria。
3. 轉進 x64dbg / OllyDBG / Cheat Engine，建立 `observable event -> breakpoint -> caller/state -> rebinding` 的 8.8 runtime playbook。
