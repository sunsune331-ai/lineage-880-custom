# J.J. AI-first Research Synthesis — first 50 new articles

範圍：`JJ-000089` ～ `JJ-000138`。逐篇主資料仍以 `research/article_ledger.jsonl` 為準；本檔只保存跨文章 synthesis，不取代 article-level records。

> Evidence rule: 本檔所有來源結論均為 `research evidence` / `unverified`。不得直接升級為 8.8 Client / Server / Runtime 已驗證事實。

## 1. 新的重要研究方向

### A. Semantic owner 必須與 shared helper / writer 分離

x64dbg 與 Cheat Engine 多篇文章反覆顯示：共享 API、Dialog helper、damage writer、runtime DLL helper 都可能被多個事件或 entity 共用。單純命中 `GetWindowTextA`、MessageBox/Dialog、writer instruction 或 generic helper，不足以證明它是研究目標的 semantic owner。

8.8 建議固定證據鏈：

`observable event -> exact breakpoint hit time -> module/caller/call stack -> owner/base pointer -> predicate/state delta -> visible result`

### B. Dynamic address 不等於 stable owner

CE exact/unknown scans、what-writes、pointer lesson與 Game Cheats 實作共同支持：

`visible state -> candidate address -> writer/accessor -> base/object context -> pointer chain -> cross-session rebind`

任何單一 heap VA 只算 session evidence。若要形成 8.8 可重用定位，需再追 module/static anchor、global owner、constructor 或其他穩定來源。

### C. Persistence / config provenance 要獨立追蹤

x64dbg INI、registry、trial-state文章與 Server warehouse/config文章共同提示：UI 顯示、memory flag、DB row、INI/registry/file 都可能只是某一層 projection。

8.8 應記錄：

`persistent source -> loader/cache -> runtime owner -> mutation producer -> client/server projection -> refresh/reload condition`

### D. Server inventory / NPC provenance 已形成可驗證模板

前 10 篇 Server 文章形成：

`command/request -> template/config/DB -> instance/object -> inventory/world owner -> persistence -> refresh/packet/visible state`

Inventory 特別要區分 stack merge、新 item insertion、warehouse persistence 與 live inventory refresh；NPC 則區分 persistent spawn definition、temporary spawn instance、summon/master ownership。

### E. External visual validation 可成為第三條獨立證據線

`JJ-000138` 提供 hwnd/PID -> PrintWindow -> OpenCV HSV ROI -> HP/MP ratio 的外部視覺觀察架構。它不依賴 client memory address 或 server packet，因此可作為 8.8 runtime/packet/memory 假設的獨立可見結果比對來源。

## 2. 多文章互相佐證

- x64dbg API/string/xref/call-stack系列互相支持「事件導向 narrowing，再追 caller/producer」，而不是 API 命中即下結論。
- CE Step 2/3/4/5/6/9 與 Game Cheats 1–4 互相支持「scan -> writer -> pointer/object -> external consumer」的連續方法。
- CE shared-code 與 x64dbg shared-dialog/helper案例共同支持「shared implementation != semantic owner」。
- Server warehouse/present/lvpresent 與 client-runtime pointer/persistence文章共同支持「可見結果與 persistence/cache/owner 必須分層」。
- `JJ-000138` 與既有 Client Browser/Toolkit 方向共同支持以外部、read-only preview/monitor 作驗證，而不必修改 client/resource。

## 3. 衝突 / 不確定性

- 舊文章中的固定地址、offset、opcode bytes、compiler/runtime、Windows API 行為與 breakpoint限制均屬特定 target/tool 時代，不能移植為 8.8 常數。
- 部分文章以修改 branch/flags/bytes 驗證語義；8.8 專案目前約束為不修改既有 Client/Server/DB/resource，因此只保留「觀察 producer/branch/state」的方法，不採用 patch 行為。
- CE pointer 教學正文含未完成段落；只能保留已明確取得內容。
- `JJ-000138` 的 `main.py` 正文為咖啡會員加密，本輪未取得；只保存公開的 window capture、HPMonitor、架構描述與圖片 caption，不推測加密內容。

## 4. 新增待 8.8 驗證

### Client / Runtime
- Lin.bin / LinLogin.bin 是否存在 debugger-induced behavior / anti-debug divergence。
- Resource/UI/file API 命中後的真正 caller、owner與 request context。
- Item/GFX/animation/resource state 的 what-writes/what-accesses + stable anchor。
- `PrintWindow(PW_RENDERFULLCONTENT)` 對 8.8 client 的可用性、最小化/遮擋/DWM 行為。
- HP/MP ROI、HSV threshold 與 client UI scaling/theme 的穩定性。

### Server
- warehouse/inventory instance、stack merge/new insertion、persistence與 live refresh 的 8.8 source chain。
- NPC persistent spawn vs temporary runtime spawn vs summon/master ownership。

### Cross-layer
- memory owner / server authoritative state / packet projection / visible UI 是否能在同一 controlled event 閉環。
- shared writer/helper 命中時，如何以 object identity、caller與事件時間區分真正 entity/feature owner。

## 5. 狀態

- AI-first 新增完成：50 篇（`JJ-000089` ～ `JJ-000138`）
- 正式全站累計：138 / 783
- 剩餘：645
- Article-level source of truth：`research/article_ledger.jsonl`
- Checkpoint source of truth：`research/checkpoint_ledger.jsonl`

STATUS = `AI_FIRST_SYNTHESIS_0050_COMPLETE`
