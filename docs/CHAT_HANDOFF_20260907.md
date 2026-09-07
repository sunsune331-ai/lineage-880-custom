# Chat Handoff Snapshot — 2026-09-07

此檔用來讓目前超長 ChatGPT 對話可以安全關閉/刪除後，仍可從 GitHub 接續專案研究。它不是逐字聊天備份，而是目前可執行的專案狀態與研究方向快照。

## 1. J.J.'s Blogs 全站研究

目標：完整研究 https://morosedog.gitlab.io/archives/ 的 783 篇文章，採高品質模式：精讀 + 判斷對 8.8C 的價值 + 整理研究方法 + 寫回 GitHub + 與既有 8.8 證據交叉比對。

原則：
- 不把 3.81C 位址/檔名/優先序直接套到 8.8C。
- 只把舊版文章當成研究導航，8.8C 一律另做 static/runtime 驗證。
- 文章狀態區分 PUBLIC_FULL / COFFEE_FULL / COFFEE_LOCKED_BY_READER / ADVANCED_LOCKED / FLAGSHIP_LOCKED。
- 遇鎖定文章先標記跳過，不讓單篇卡住全站進度。

目前已知全站規模：783 posts、86 categories；天堂私服 130、x64dbg 36、OllyDBG 16、Cheat Engine 10、排序演算法 7、x86 組合語言 3；另有 Python/OpenCV、C、Java、MySQL、XML 等大量文章。

重要研究文件：
- docs/JJ_SITE_RESEARCH_TRACKER.md
- docs/JJ_LINEAGE_RESEARCH_MAP.md
- docs/JJ_RESEARCH_PROGRESS_20260907_1608.md

第一批已抽出的高價值方向：
1. gfxid/polyid 不應視為必然等於實際 SPR prefix：應走 identifier -> morph/list mapping -> optional remap -> sprite set -> action/direction SPR。
2. 3.81C 的 list.spr/list.spz/launcher custom morph 關係提示 8.8C 應驗證 Lin.bin/LinLogin.bin 的實際 mapping layer、precedence、fallback。
3. 舊版 105.clothes / weapon / effect 等證據顯示角色畫面可能是 base body + attached object + weapon + effect + timing + offset 的 composite rendering。
4. Resource Browser 架構應優先 IDX enumerate -> source PAK -> resolve logical ID -> on-demand decode -> preview，而不是全量 SPR -> PNG。
5. item icon 應視為獨立 resolver 鏈，舊版方向是 DB invgfx -> icon ID -> TBT/list.ico 類 mapping；8.8C 需另驗證。
6. OllyDBG/x64dbg/CE 方法要轉成 Argus playbook：observable event -> narrow execute anchor -> caller/args/state delta -> only expand one layer；heap VA 不跨 session。
7. 背包整理演算法可優先考慮 stable multi-key，但 client 既有 ItemSortPred/merge-sort family 尚未證明連到主背包。

目前已建立每小時接續的 J.J. 全站精讀排程；排程在雲端執行，不依賴使用者 Edge/ChatGPT 分頁保持開啟。

## 2. Inventory Reorder / 背包整理

目前 Goal：驗證 8.8 主背包手動 reorder entry、persisted-order producer 與 refresh tail。

已確認主鏈：
manager+0x54 vector<Item*> -> DF8450 -> DF9E60 -> DFA580 -> FC2520

三種 order 機制：
1. manager source vector order
2. UI persisted item-ID order
3. grid layout index order

重要函式角色：
- 0x00DFB6E0 = UI reorder commit，InventoryItemGrid vtable+0x1A0；兩個 byte args 語意仍未確認。
- 0x00DFC6B0 = persisted order producer；clear owner+0x16C，snapshot current model sequence，per-item ID append。
- 0x00DE0E40 = source range reorder mutator on manager+0x54；exact ABI 未確認。
- 0x00DFBFA0 = UI source-move adapter，vtable+0x1A4；可多次呼叫 DE0E40，後接 DFA580。
- 0x00DF9EF0 = single item position adjustment；可呼叫 DE0E40，後接 refresh。
- 0x00DF86B0 = persisted item-ID order -> grid indices。
- 0x00FC2520 = layout-index consumer。
- 0x00DFA580 = grid reconcile/refresh；desired list、find/create/swap、layout order、finalize。
- 0x00DFAB70 = refresh dispatcher；grid -> DFA580，InvWindow -> DFA4D0。
- 0x00DE1D40 = batch order-table consumer，未證明是主背包 live manual reorder producer。

目前四個 execute anchors / AOB：
1. DFB6E0: 55 8B EC 83 EC 0C 89 4D FC 0F B6 45 10 50 0F B6 4D 0C
2. DFC6B0: 55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 83 EC 28 A1 ?? ?? ?? ?? 33 C5 50 8D 45 F4 64 A3 00 00 00 00 89 4D EC 8B 4D EC 81 C1 6C 01 00 00
3. DE0E40: 55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 81 EC A4 00 00 00 A1 ?? ?? ?? ?? 33 C5 50
4. DFA580: 55 8B EC 6A FF 68 ?? ?? ?? ?? 64 A1 00 00 00 00 50 81 EC C4 00 00 00 56

Session rebind：
- module image base historical 0x00400000；每 session live revalidate。
- RenewalInventoryUI global slot = 0x01A960B4
- RenewalInventoryUI +0x15C -> InventoryItemGrid*
- RenewalInventoryUI +0x168 -> InvWindow*
- inventory manager global slot = 0x01952748
- manager +0x54 = source/order Item* vector
- RenewalInventoryUI +0x16C = persisted item-ID vector
- InventoryItemGrid +0x1BC = layout-index vector
- InventoryItemGrid +0x190 = grid icon pointer vector

Runtime capture spec 已在：
- docs/INVENTORY_REORDER_CAPTURE_PACK.md
- docs/INVENTORY_REORDER_CAPTURE_RESULT.md

PoC gate：live capture 未完成前，不做 patch、不做 memory write、不用 Tripwire injection。Argus 優先。

## 3. Sprite/GFX/Animation / Item Icon

Server -> Client sprite chain 已確認：
MySQL npc(npcid/name/gfxid) -> NpcTable/L1Npc -> L1NpcInstance -> S_NPCPack -> client -> sprite resolver -> SPR render。

重要修正：GFX ID 與 SPR prefix 不能再當成永遠同號；需加入 morph/list remap layer 的驗證。

已知 Toolkit 路線：
- 初版 GFX 110 PoC 可匯出 frame PNG/manifest/index。
- 單一 Action 漸進載入、低並行 queue/cancel 已做過方向驗證。
- CLI 批量 SPR export 很慢；代表 4 actions 約 43 秒。
- GFX 13715 單一驗收成功：Sprite10 中 13715-0 不存在，實際使用 13715-18.spr；12 frames；frame3 可辨識角色與弓；輸出 test_13715_Sprite10.png。
- 長期 Browser 應改成 PakViewer 類 on-demand decode/thumbnail，而不是全量 export-first。

Item icon：
- server item packet 直接送 item.get_gfxid()。
- 三張 item DB table 以 invgfx 保存背包圖示 ID。
- client 端實際 icon resource container / item-common.bin 角色仍要驗證。
- 不應先套 Sprite 規則；先查 DB -> packet -> client icon mapping/resource。

InventoryItemIcon historical structure hints：
- vtable 0x0163C754
- +0xE8 grid backpointer
- +0x94/+0x98 idle POINT pair（已反證不是 reorder producer）
- +0x160 visual/invgfx candidate
- +0x1B8 count candidate
- +0x22C item-model-like pointer；target vtable 0x0163C47C，model+0x20 count，model+0x24 low16 invgfx
- +0x230 CountText control vtable 0x01655BD8

## 4. Argus / Tripwire / Codex 工作規則

Argus/Tripwire toolkit：C:\架設功具\記憶體工具包_20260724\
- Argus MCP：static + dynamic HWBP；優先使用。
- Tripwire MCP：page protection/VEH injection；只有明確批准才用。

Reviewer <-> Executor contract：
每輪 Executor：
1. read AI_HANDOFF.md
2. read CODEX_STATUS.md
3. read latest REVIEWER_FEEDBACK.md
4. read needed CLIENT_UI_FUNCTION_MAP section
5. execute/modify/runtime verify
6. update CODEX_STATUS
7. sync durable evidence to Function Map
8. git add -> commit -> push origin main before round complete

local completed but not pushed != complete；push fail limited retries + one safe git pull --rebase；never force push。

Polling/heartbeat 只能用普通 PowerShell/git，不得因為沒有新 Reviewer commit 就消耗 Codex model。

Codex quota 曾在 2026-09-07 暫時到上限，當時提示 17:39 後可重試；重開後最小接續 prompt：
「【直接繼續】git pull，讀 AI_HANDOFF.md、CODEX_STATUS.md、REVIEWER_FEEDBACK.md，依共享 Goal 直接續跑；每輪自動更新、commit、push。」

## 5. 目前最重要的下一步

A. J.J. 研究：
- 先完成天堂私服 130 篇 per-article FULL/PARTIAL/LOCKED/8.8-RECHECK 分類。
- 再 x64dbg / OllyDBG / CE / x86 / 排序 / C / Java / MySQL / XML / Python/OpenCV。
- 每輪只增加新正文閱讀與新證據，避免重複摘要。

B. Inventory reorder：
- client live 後，AOB 重新驗證四 anchors。
- rebind RenewalInventoryUI / InventoryItemGrid / manager / vectors。
- BEFORE snapshot。
- 只設四 execute captures。
- 使用者只做一次主背包手動 drag。
- 收集 hit sequence + ECX/stack args/caller + AFTER vectors。
- 判定 DFB6E0 / DFC6B0 / DE0E40 / DFA580 是否形成真正 live reorder chain。

C. Resource Browser：
- 之後應以 IDX/PAK on-demand resolver + morph remap + composite preview 為架構，不再回到全量 PNG export-first。

## 6. 使用者偏好的執行方式

- 使用者 = Goal。
- Codex = Executor：直接查、改、跑、驗證、自除錯；只有真的需要人工遊戲操作/權限/高風險 patch 才停。
- ChatGPT = Reviewer：收窄 Goal、review 結果、只複查會影響實作的關鍵事實。
- 不要長時間反覆「查詢 -> 除錯 -> 查詢 -> 除錯」卻沒有可見功能進度。
- 不要只因 elapsed time 長就中止；只在沒有新 evidence、重複同一失敗、失控擴張或明顯偏題時停。
- 分析/PoC 期間不修改 client、PAK/IDX、Lin.bin、server、database；產物只放 Toolkit outputs。

此快照完成後，即使目前 ChatGPT 超長對話被關閉或刪除，後續新對話仍可從 GitHub 的上述文件重新建立專案工作狀態。