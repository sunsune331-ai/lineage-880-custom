# J.J.'s Blogs 全站精讀進度 — 2026-09-07 16:25

## 本輪原則

- 先讀 `JJ_SITE_RESEARCH_TRACKER.md`、`JJ_LINEAGE_RESEARCH_MAP.md` 與既有研究進度，避免重複摘要。
- 本輪只把**實際取得完整正文**的文章計入新增 FULL。
- 舊版 Lineage 3.81C / L1J-3.80c 的位址、常數、欄位與 parser 限制都不直接套到 8.8C。
- 8.8 結論分成 `hypothesis -> static evidence -> runtime confirmed`。

## 本輪新增 FULL：12 篇

| # | 標題 | URL | 分類 | 存取 | 來源版本 | 可重用重點 | 8.8 驗證點 |
|---|---|---|---|---|---|---|---|
| 1 | 天堂私服 | GM指令使用 (人物變身) | https://morosedog.gitlab.io/private-lineage-20220510-private-lineage-105/ | 天堂私服 | PUBLIC_FULL | 3.81C | 無完整 morph 編碼時，單一 SPR 仍可顯示但角色行為不完整 | 把 resource decode / action resolver / runtime equivalence 分層驗收 |
| 2 | 天堂私服 | 資料庫中 spr_action 分析 | https://morosedog.gitlab.io/private-lineage-20220515-private-lineage-110/ | 天堂私服 | PUBLIC_FULL | 3.81C / L1J-3.80c | spr_id/act_id/framecount/framerate 與 server interval check 的關係 | 查 8.8 等價 timing table/cache 與 authoritative interval |
| 3 | 天堂私服 | 變身檔-武器指令的教學 | https://morosedog.gitlab.io/private-lineage-20220522-private-lineage-117/ | 天堂私服 | PUBLIC_FULL | 3.81C | 高版本可能把武器 baked into sprite；舊 overlay 強套會重疊 | 8.8 composite renderer 的 weapon layer 必須 optional |
| 4 | 天堂私服 | 變身檔-魔法效果的教學 | https://morosedog.gitlab.io/private-lineage-20220523-private-lineage-118/ | 天堂私服 | PUBLIC_FULL | 3.81C / L1J-3.80c | 109.effect 可拆 projectile / impact；遠距武器也可使用 | 查 skill effect DB->packet->client resolver->resource |
| 5 | 天堂私服 | 變身檔-加速指令的教學 | https://morosedog.gitlab.io/private-lineage-20220524-private-lineage-119/ | 天堂私服 | PUBLIC_FULL | 3.81C | framerate 是 stateful directive，可影響後續 action | 8.8 parser 需查 current timing state，而非只查 action FPS |
| 6 | 天堂私服 | 變身檔-走路分析與修改 | https://morosedog.gitlab.io/private-lineage-20220525-private-lineage-120/ | 天堂私服 | PUBLIC_FULL | 3.81C / L1J-3.80c | frame 數可縮但總時間可保持；server Move interval 另行檢查 | 分離 visual duration 與 server move gate |
| 7 | 天堂私服 | 變身檔-攻擊分析與修改 | https://morosedog.gitlab.io/private-lineage-20220526-private-lineage-121/ | 天堂私服 | PUBLIC_FULL | 3.81C / L1J-3.80c | attack sequence 有 event/sound trigger frame；Attack interval 是另一層 | 建立 frame event metadata + server interval capture |
| 8 | 天堂私服 | 變身檔-魔法分析與修改 | https://morosedog.gitlab.io/private-lineage-20220527-private-lineage-122/ | 天堂私服 | PUBLIC_FULL | 3.81C / L1J-3.80c | 18.spell dir / 19.spell nodir；舊版缺失時存在 fallback | 8.8 查 action fallback/default resolver |
| 9 | 天堂私服 | 變身檔-受傷分析與修改 | https://morosedog.gitlab.io/private-lineage-20220528-private-lineage-123/ | 天堂私服 | PUBLIC_FULL | 3.81C | damage 動作可縮 frame；視覺 timing 與事件 timing 不必相同 | 建 damage event timing 與 visual sequence 對照 |
| 10 | 天堂私服 | 變身檔-撿取分析與修改 | https://morosedog.gitlab.io/private-lineage-20220529-private-lineage-124/ | 天堂私服 | PUBLIC_FULL | 3.81C | 15.get 可移除/縮成單 frame；可有特定 trigger frame | 把 pickup 納入 Action Dictionary 與 event-frame 模型 |
| 11 | 天堂私服 | 變身檔-指向指令分析與修改 | https://morosedog.gitlab.io/private-lineage-20220530-private-lineage-125/ | 天堂私服 | PUBLIC_FULL | 3.81C | action 可 reference 另一 morph ID 的 action；resolver 不是單層映射 | 8.8 resolver 加 reference chain/cycle/missing target 診斷 |
| 12 | 天堂私服 | 變身檔-魔法方向性分析與修改 | https://morosedog.gitlab.io/private-lineage-20220531-private-lineage-126/ | 天堂私服 | PUBLIC_FULL | 3.81C | dir/nodir 的視覺方向性與 action encoding 需分開理解 | 8.8 實測 caster facing、target direction 與 resource selection |

## 本輪最重要的新證據

### A. `gfxid -> gfxid-action.spr` 明顯不足

新增正文再次證明 resolver 至少要允許：

`visible ID -> morph entry -> sprite remap -> action local/reference -> frames`

其中 action 還可能跨 morph ID 指向另一 entry。這直接影響目前 GFX Browser 的資料模型。

### B. 「一張 SPR 能顯示」不等於「變身可用」

舊版 GM 變身實驗顯示，缺 morph/action 編碼時可以看到靜態圖，但不能正常移動、轉向、攻擊。因此 13715 單圖驗收只證明 decode path，不代表 action mapping 已驗證。

### C. 動畫速度必須拆成兩個時鐘

1. client visual animation duration
2. server authoritative Move/Attack/Spell interval

這兩者不能混成一個 `framerate` 欄位。8.8 若要分析速度、動作或 anti-speed，必須同步 capture。

### D. Weapon/effect 不是固定 overlay 模式

武器可能已 baked-in；effect 又可能拆 projectile / impact。Toolkit 的 composite model 要做成可選 layer/phase，而不是硬編 `body + weapon + effect`。

## 已同步文件

- 新增：`docs/JJ_LINEAGE_RESEARCH_MAP_ADDENDUM_20260907_1623.md`
- 新增：本檔 `docs/JJ_RESEARCH_PROGRESS_20260907_1625.md`

## 下一輪

優先順序：
1. 天堂私服剩餘 morph 格式/type/attr/工具正文。
2. x64dbg。
3. OllyDBG。
4. Cheat Engine。
5. x86。
6. C / Java / MySQL / XML cross-reference。
7. Python / OpenCV 中可直接支援資源辨識與預覽的內容。

鎖定文章處理規則維持：COFFEE 無法解鎖、ADVANCED、FLAGSHIP 只標 LOCKED/SKIP，不停留、不臆測正文。
