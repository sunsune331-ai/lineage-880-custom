# J.J.'s Blogs 研究進度 — 2026-09-07 16:08

本檔記錄第一批**實際打開正文並核對**的內容。舊版天堂文章主要是 Lineage 3.81C / L1J-3.80c；所有結論只作 8.8C 的研究導航，不直接沿用位址或版本特定檔案優先序。

## 全站範圍確認

- Posts: 783
- Categories: 86
- 天堂私服: 130
- x64dbg: 36
- OllyDBG: 16
- Cheat Engine: 10
- 排序演算法: 7
- x86組合語言: 3
- Python: 83（OpenCV 78）
- C: 41 / Java: 38 / MySQL: 30 / XML: 20

## 第一批已實際閱讀 / 核對正文

### 天堂私服 / Client resource / morph

1. 客戶端idx、pak說明
   - Sprite*.idx/pak 包含 SPR、TBT、PNG、IMG 等資源。
   - Text.idx/pak 包含 HTML、TBL、list.spr/list.spz。
   - 研究方法：先 IDX 定位 entry，再解析對應 PAK；不應把所有資源都先批量 export。

2. 補丁副檔名對應說明
   - list.spr/list.spz 定義變身動作與圖檔對應。
   - TBT 是 icon 類資源的重要候選格式。

3. 補丁吃檔資料夾對應
   - 舊版 icon / sprite / Surf / text 的資源類型分工明確。
   - 對 8.8C 僅保留「資源角色分類」作導航，不假設相同資料夾即相同 precedence。

4. SPZ、XML加解密 工具
   - list.spz 與部分 XML 是加密資源；舊版抽出後需解密才能編輯/閱讀。
   - PakViewer 被用來抽取 Tile/Text 中 XML / SPZ。

5. 客戶端和登入器與變身檔關係分析
   - 3.81C 實驗中，不使用登入器自定義變身檔時，修改 list.spz 有效、單改 list.spr 無效。
   - 登入器可以提供自定義 PAK 變身資料。
   - 對 8.8C 應驗證：Lin.bin/LinLogin.bin 實際載入哪個 morph/list/bin、原生與 launcher 自定義資源的 precedence/fallback。

6. 變身檔與 gfxid、polyid 分析
   - 重要分層：變身編號 != 必然等於實際 SPR prefix。
   - 例：#5641 64=240 -> 變身 5641 實際使用 sprite set 240。
   - 對 Toolkit：gfx/poly identifier -> morph mapping -> resolved sprite set -> action SPR。

7. 資料庫中 gfxid、polyid 分析
   - 舊版 DB 中多個 gfxid/polyid 欄位最終都需再經變身清單解析，才能得到實際 SPR set。
   - 例：polymorphs.polyid 6137 -> #6137 64=240 -> 240.spr。
   - 對 8.8C：DB field / server getter / packet / client mapping / sprite resolver 要分層驗證。

8. 變身檔-初步說明與規則
   - list 內有總 entry 數量與 #ID entry；缺號/重複 action 定義可能造成舊版錯誤或缺動作。
   - 動作不能只看 SPR frame，morph entry 定義是動畫行為的一部分。

9. 變身檔-附加物件的教學
   - 105.clothes 類指令證明舊版可把本體與附加 object 分層組合。
   - 對 8.8 Browser 必須預留 body + clothes/weapon/effect + per-frame offset 的 composite renderer。

### Reverse-engineering methodology

10. OllyDBG - 訊息斷點及 RUN 追蹤的補充（分類頁摘要核對）
   - 核心問題不是「隨便哪裡下 breakpoint」，而是如何從事件處理確認 breakpoint 是否落在正確路徑。
   - 可轉成目前 Argus 原則：observable UI event -> narrow execute anchor -> verify caller/state delta -> only expand one layer。

11. Cheat Engine 教學系列（分類頁摘要核對）
   - Code finder 用於變動地址；pointer / multi-level pointer 用於跨 session 定位；shared-code 教學強調同一 writer 可能服務多個 entity。
   - 直接呼應目前 8.8 原則：heap VA 不跨 session；用 global slot/AOB/object chain rebind；同一 generic writer 不等於主背包 reorder producer。

12. x86 組合語言系列（分類頁摘要核對）
   - 暫存器、基本指令與 x86 架構是逆向 ABI 判讀基礎。
   - 直接用於解讀 ECX=this、stack args、mov/store writer 與 calling convention。

13. 排序演算法 | 簡介
   - 明確區分穩定排序與不穩定排序。
   - 對背包整理的直接設計價值：若主鍵相等，stable sort 可以保持玩家現有相對順序，避免整理後同類物品無理由亂跳。

## 第一批對 8.8C 最重要的新導航

### A. GFX / SPR resolver 不應再只做同號 lookup

舊版可靠流程：

`gfx/poly identifier -> morph/list #ID -> optional remap (=sprite set) -> action/direction -> SPR`

8.8C 要找的不是特定 list.spz，而是「等價 mapping layer」。

### B. Resource Browser 應從 export-first 改成 resolve/decode-on-demand

`IDX enumerate -> source PAK -> resolve logical ID -> decode selected entry -> preview`

這比全量 SPR -> PNG 更符合 PakViewer 類工具實際使用方式，也能降低等待時間。

### C. Composite sprite 是正式需求，不是例外

Browser 架構要預留：

`base body + attached object + weapon + effect + timing + offset`

否則單看某一 SPR 會持續遇到「背面、缺武器、缺特效、跟遊戲畫面不同」。

### D. 背包排序演算法應優先考慮 stable multi-key

候選設計：

`category -> equip/consumable/etc -> name/id -> enchant/bless -> original order as tie-breaker`

這只是 Toolkit/PoC 的演算法方向；client 內既有 ItemSortPred/merge-sort family 是否可用，仍須 8.8 runtime 證明。

## 鎖定狀況

- 天堂私服分類頁可見部分文章直接標示「需要管理員或更高等級」；咖啡會員公開碼不足以解這類內容。
- 本研究只把真正能取得正文/公開段落的文章標為已閱讀；不以標題猜正文。

## 下一批優先順序

1. 天堂私服 09.變身檔分析/修改剩餘文章：格式、type、attr、weapon、effect、framerate、walk/attack/spell/damage/get、指向。
2. 天堂私服 07.工具介紹/使用：PakViewer、LineageSpr、Icon、SFD 等，補足 Browser decoder/preview 架構。
3. 天堂私服 05.核心分析/修改：找 Server -> packet -> client resource ID 的完整案例。
4. OllyDBG 第五/六章與 x64dbg 基礎/trace 章節：整理 event-to-entry / caller-chain 的可重用 debug playbook。
5. Cheat Engine 5~10：整理 code finder / pointer / shared code 對 Argus capture 的對應原則。
6. 排序演算法 7 篇：只抽取對「背包 stable multi-key reorder」真正有用的性質，不為了讀完而讀完。
