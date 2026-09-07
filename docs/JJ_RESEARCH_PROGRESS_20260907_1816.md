# J.J.'s Blogs 全站精讀進度 — 2026-09-07 18:16

## 本輪執行原則

- 沿用 `docs/JJ_SITE_RESEARCH_TRACKER.md` 的 783 篇基線與 access-status 定義。
- 本地研究從 18:03 checkpoint 的下一批接續；推送前發現 GitHub 已有 18:15 的並行 50 篇 checkpoint，因此再以 URL/標題做一次去重。
- 只把公開技術正文當證據；下載檔與解壓密碼仍鎖定時另記 `COFFEE_RESOURCE_BLOCK`。
- 3.81C 工具功能只標 `JJ_381_DIRECT`；8.8 設計結論保持為待 runtime/resource 驗證的 `HYP_880`。
- 收錄門檻不是「能否立即驗證/套用 8.8」：凡涉及天堂 Client、Server、Protocol、資源/修改工具、封包或檔案格式都保存；不確定內容明確標為 `待驗證 / 版本未確認`。
- 未移動、改名或重新分類任何核心 docs 或 JJ 文件。

## 本輪精讀 FULL：9 篇；與既有 ledger 去重後新增 0 篇

九篇均實際讀到公開技術正文。Pakext、PakViewe、PackViewer_beta2、MTools、XML 加解密、對話檔加解密、登入器素材抽檔、Linskin4.04 已出現在 18:15 的 50 篇 ledger；`SPZ、XML 加解密` 已在 16:08 progress 記錄為正文核對。故九篇都只補充更完整事實、不重複計數。

| # | Article | URL | Access | JJ_381_DIRECT | HYP_880 / 可執行影響 |
|---:|---|---|---|---|---|
| 1 | 天堂私服 \| Pakext 工具 | https://morosedog.gitlab.io/private-lineage-20220206-private-lineage-73/ | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | 開單一 IDX；可抽取/刪除，亦可追加；清單支援欄位排序；正文未提供 preview 證據 | 只作 container inventory/mutation 對照；8.8 read-only Browser 不納入 add/delete |
| 2 | 天堂私服 \| PakViewe 工具 | https://morosedog.gitlab.io/private-lineage-20220207-private-lineage-74/ | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | 開單一 IDX；export/export-to、add/update/delete、type filter、fuzzy search、sort、image/animation/text preview、zoom | `inventory → search/filter → on-demand preview` 是可重用互動；寫回路徑獨立驗證 |
| 3 | 天堂私服 \| PackViewer_beta2 工具 | https://morosedog.gitlab.io/private-lineage-20220209-private-lineage-76/ | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | 開 PAK 或 client folder；type filter/search/sort/preview/export/add/delete；XML preview 自動解密；作者記錄 Save resource 疑似無效 | decoder dispatch 可掛在 preview 層；不能因 UI 有 Save 就宣告 round-trip 可靠 |
| 4 | 天堂私服 \| MTools 工具 | https://morosedog.gitlab.io/private-lineage-20220213-private-lineage-80/ | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | TBT image-package browser；開 client directory；可抽出 selected/all images | 補強 Item Icon Browser 的 package-list/export acceptance；無正文證據的 search/write 不補推 |
| 5 | 天堂私服 \| XML加解密 工具 | https://morosedog.gitlab.io/private-lineage-20220214-private-lineage-81/ | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | 單檔 encrypt/decrypt；輸出 `_e.xml` / `_d.xml`；反覆多次轉換會亂；寫回前需恢復正確檔名 | logical entry name、temporary suffix、raw/decoded bytes 必須分開保存；8.8 cipher 另驗 |
| 6 | 天堂私服 \| SPZ、XML加解密 工具 | https://morosedog.gitlab.io/private-lineage-20220215-private-lineage-82/ | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | 同目錄批次處理 XML/SPZ；decrypt 產生 `.dec`，encrypt 輸入用 `.e`；`polymorphList.xml`/`list.spz` 範例 round trip 與原 ciphertext 一致 | 建立可量化 round-trip hash 驗收；不沿用舊版 suffix/cipher 作 8.8 事實 |
| 7 | 天堂私服 \| 對話檔加密解密 工具 | https://morosedog.gitlab.io/private-lineage-20220216-private-lineage-83/ | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | `Dtext` 解密、`text` 加密；3.81C 實驗中 HTML 明文/密文皆可；server `npcaction.normal_action` basename 對應 client dialogue resource | 形成 `NPC → server basename → client HTML → visible text` 交叉驗證鏈；8.8 field/loader acceptance 另驗 |
| 8 | 天堂私服 \| 登入器素材抽檔 工具 | https://morosedog.gitlab.io/private-lineage-20220217-private-lineage-84/ | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | 工具放 client root 執行，登入器素材輸出至 `skin` | 可作 special extractor corpus；不視為 generic IDX/PAK 或遊戲 loader source 證據 |
| 9 | 天堂私服 \| Linskin4.04 工具(IMG↔BMP) | https://morosedog.gitlab.io/private-lineage-20220219-private-lineage-86/ | PUBLIC_FULL + COFFEE_RESOURCE_BLOCK | 批次 IMG↔BMP；可列 PAK entries、按 extension 批量抽取、雙擊單筆抽取；作者記錄 individual conversion 疑似無效 | 拆成 container extract 與 format convert 兩個 service；8.8 IMG codec/round-trip 另驗 |

## 文章事實完整保留層

以下保留的是文章中與 Client / Server / resource format / modification workflow 有關的內容，不以 8.8 可用性篩除。九篇頁面本身都沒有逐一寫明精確 client build；本專案暫以周邊 3.81C 系列脈絡分類，但每項均標記「精確版本未確認」。

### 1. Pakext (`Pakext.exe`)

- 用途名稱：PAK 抽取程式；頁面實際操作入口是開啟 client directory 下的 `*.idx`。
- `File → Open`：開單一 IDX。
- 對選中 entry：`抽取檔案`、`刪除檔案`。
- `Edit` menu：`追加檔案`、`抽取檔案`、`刪除檔案`、`選擇全部`。
- 點 list header 可 asc/desc 排序。
- 文章未展示圖片/動畫/文字 preview、搜尋、解密或 round-trip verification；這些能力一律記為「未確認」，不是「不存在」。
- 文章列出的 archive 名為 `Pakext.7z`；密碼區塊鎖定，未下載或執行。

### 2. PakViewe / Lineage I Pack File Viewer (`PakViewer.exe`)

- `File → Open`：開單一 `*.idx`。
- selected entry actions：`Export`（文章稱輸出到程式根目錄）、`Export TO...`（可選位置）、Unselect All、Select All。
- `Edit → Filter`：依檔案類型勾選/取消顯示。
- `Tools`：Export、Export TO、Delete、Add、Update、Unselect All、Select All。
- 點 list header 可 asc/desc；名稱輸入可做模糊搜尋/過濾。
- 右側可 preview 圖片、動態圖、文字等；左側 slider 可縮放 preview。
- archive 名為 `PakViewer.7z`；密碼區塊鎖定，未下載或執行。
- 精確版本/支援哪些 IDX 版本、Add/Update 寫回格式與可靠度均未確認。

### 3. PACK Viewer (`PackViewer_beta2.exe`)

- toolbar 十個 actions：Open PAK file、Open LINEAGE folder、Clear index list、Show all file types、Show only image files、Show only text files、Export resource、Save resource、Add resource、Delete resource。
- 文章操作範例用 Open PAK file 選 client directory；Open PAK 與 Open LINEAGE folder 的實際差異未進一步說明，記「待驗證」。
- selected entry 支援 export（可選位置）、save、add、delete；作者實測 `Save resource`「好像沒效果」，保留為工具行為觀察，不改寫成確定 bug。
- 支援類型 icon filter、list-header asc/desc、名稱模糊搜尋。
- 右側可 preview 圖片、動態圖與文字；XML preview 會自動解密，頁面並稱 XML 預設加密。
- archive 名為 `PackViewer_beta2.7z`；密碼區塊鎖定，未下載或執行。

### 4. TBT 圖片包瀏覽器 (`MTools.exe`)

- `File → Open` 選 client directory。
- 可對 selected image 使用「抽出選擇的圖檔」，輸出位置可選。
- 可「全部抽出」，輸出位置可選。
- 文章沒有展示 search/filter、TBT ID mapping、preview controls、修改或 repack；均標「待驗證」。
- archive 名為 `MTools.7z`；密碼區塊鎖定，未下載或執行。

### 5. XML 加解密 (`XML加解密.exe`)

- 頁面稱 client `*.xml` 有加密；文字編輯器不能直接閱讀/編輯，未加密檔直接吃回 client 會出現 `XML Encryption Check(xxxxx.xml)`。
- UI 可選 `加密` 或 `解密`，再選單一 XML。
- encrypt 產生 `_e.xml` suffix；decrypt 產生 `_d.xml` suffix。
- 範例由 `Tile.idx` 抽出 `polymorphList.xml`，解密成可讀 XML，再重新加密；頁面稱重新加密 bytes 與原始加密內容一致。
- 吃檔前要把工具 suffix 改回 logical filename。
- 作者警告多次重複 decrypt/encrypt 會讓內容錯亂；原因與可重現條件未提供，記「待驗證」。
- archive 名為 `XML加解密.7z`；密碼區塊鎖定，未下載或執行。

### 6. SPZ、XML 加解密 (`Lineage-spz-xml.exe`)

- 頁面稱 client XML 被加密，明文直接吃回會出現 `XML Encryption Check(...)`。
- 頁面另稱 `list.spz` 的「新東西」有加密，並推測其變身格式理論上與 `list.spr` 相同；此句是作者推測，保留為 `待驗證 / 精確版本未確認`，不升格為格式事實。
- decrypt：把同目錄下所有 encrypted XML/SPZ 一次處理，輸出 extension 改為 `.dec`。
- encrypt：把同目錄下解密過的 XML/SPZ 一次處理，輸入 extension 先改為 `.e`。
- 範例由 `Tile.idx` 抽 `polymorphList.xml`、由 `Text.idx` 抽 `list.spz`；原始檔先移到 `bak`，解密檔改名為 `polymorphList.xml.e` / `list.spz.e` 再加密。
- 頁面稱兩個範例重新加密後與原始 ciphertext 一致；吃檔前仍要改回正確 logical filename。
- archive 名為 `Lineage-spz-xml.7z`；密碼區塊鎖定，未下載或執行。

### 7. 對話檔加密解密 (`對話檔加密解密.exe`)

- 頁面稱 client `*.html` 通常叫「對話檔」，用於 client 與 server 互動。
- decrypt workflow：待解密對話檔放 `Dtext` 後執行工具。
- encrypt workflow：待加密對話檔放 `text` 後執行工具。
- 範例由 `Text.idx` 抽 `colusher.html`，對應「競技場入場管理員」。
- server 查找示例：`SELECT npcid FROM npc WHERE name LIKE '競技場入場管理員'` 得 `50019`；再由 `SELECT normal_action FROM npcaction WHERE npcid = '50019'` 得 `colusher`。
- 修改顯示文字、加密、放進 client `text` 目錄、執行吃檔後，NPC 對話畫面顯示新內容。
- 頁面結論：該舊版實驗中 HTML 加密或未加密都能被 client 使用。
- 文章步驟後段把檔名寫成 `colusher.xml`，與前文/標題的 `colusher.html` 不一致；保留此 source inconsistency，不能自行當成 XML 實證。
- archive 名為 `對話檔加解密工具.7z`；密碼區塊鎖定，未下載或執行。

### 8. 登入器素材抽檔 (`登入器素材抽檔.exe`)

- executable 放進要抽取的 client directory 後直接執行。
- 輸出的登入器素材放在 `skin` directory。
- 頁面未說明輸入 container 名、entry table、檔案類型、加密、覆蓋優先序或回寫能力；均標「待驗證」。
- archive 名為 `登入器素材抽檔.7z`；密碼區塊鎖定，未下載或執行。

### 9. Linskin4.04 (`linskin4.04.exe`)

- 文章定位：為修改 client UI，把 IMG 轉 BMP 編輯，再轉回 IMG 並吃檔；精確 client generation 未確認。
- 兩組主要能力：`img ↔ bmp` conversion 與 `pak` extraction。
- 四個路徑設定：IMG batch input/output、BMP batch input/output、要批量抽取的 PAK、抽取輸出 directory。
- `IMG轉BMP`：把 IMG directory 全部 IMG 轉到 BMP directory；`BMP轉IMG` 做反向批次轉換。
- `個別轉換` 可選單一 IMG/BMP，但作者實測「好像沒效果」；保留為觀察，未確認根因。
- PAK extraction 的 `類型` 是使用者輸入 extension；文章例示 `img`、`spr`、`thml`、`tbt`、`xml`。其中 `thml` 很可能是原文 typo，但保留原字並標記「待確認是否指 html」。
- `列出檔案` 解析選定 PAK 並列 entries；`解開檔案` 依類型批量抽取；雙擊 list entry 可抽單一檔到輸出 directory。
- 範例從 `Sprite.pak` 抽 `0.img`，批次 IMG→BMP 後產生 `0.bmp`；BMP→IMG 只以文字說明，沒有另一組完整圖示步驟。
- archive 名為 `linskin4.04.7z`；密碼區塊鎖定，未下載或執行。

### 工具安全資訊的保留方式

- 每篇都有「舊工具可能含惡意程式、掃描結果不保證安全」的警告；本研究未下載/執行任何 archive。
- 頁面中的歷史 scan 摘要數字與語意在 reader 文字化後並不一致（部分顯示 57、55、9、4，部分不顯示數字）；完整保留為「作者當時提供 scan image/summary」，但不判定現今 archive 安全，也不把數字解讀成確切 detection ratio。
- 這些 safety metadata 與工具功能一樣不因「暫時無法用於 8.8」而省略；需要使用工具時必須重新取得檔案 hash、來源與現時掃描結果。

## 本輪形成的 Resource Tool Matrix

### A. 五層 pipeline

```text
container inventory
  → metadata search/filter/sort
  → on-demand preview/decrypt
  → export/format conversion
  → optional container mutation/repack
```

安全切線：8.8 第一階段只做唯讀 inventory/search/preview/export。`add / update / delete / repack` 必須在隔離副本另做 byte-level round-trip、metadata 與 client load validation。

### B. format-specific decoder 不應污染 container layer

PackViewer_beta2 的 XML auto-decrypt 與 Linskin 的 IMG↔BMP 證明，container entry lookup 和 format decoder/converter 是可分離能力。8.8 實作應讓同一 entry 保留 raw bytes/source provenance，再由 XML/SPZ/IMG/TBT/SPR decoder 產生派生 view。

### C. encrypted textual resources 不能只保存 plaintext

XML、SPZ、HTML 的舊版接受策略不同。至少要分開保存：

- logical entry name
- packed/raw bytes
- detected encryption state
- decoded bytes/text
- temporary tool suffix
- round-trip hash
- client loader acceptance result

「可以解碼」不等於「client 會接受重新封裝結果」。

### D. special extractor 只作 corpus source

登入器素材抽檔的 `client root → skin` 工作流可提供素材 corpus，但沒有證明 client runtime 的 source container、offset 或 precedence；這些仍需 IDX/PAK inventory 或 file-open runtime trace。

## 進度與剩餘數量

GitHub 18:15 checkpoint 已建立 88 / 783 的 URL/標題去重基線。本次 9 篇全部可在 16:08 或 18:15 ledger 找到：

- 執行前可追溯基線：88 / 783
- 本次實際精讀正文：9
- 重疊但補充完整事實：9
- 新增 unique article：0
- 累計明確完成：88 / 783
- 尚未標為完成：695
- 完成率：約 11.24%

`天堂私服` category 的精確已完成數需用完整 URL ledger 再按 category 去重；本輪不把舊的摘要數與並行 50 篇直接相加，避免假精確。

Archives 79 頁的 783 篇 title-level inventory 仍未完成，因此「695」是尚未標記完成的全站剩餘數，不是假裝已逐篇判定 access status。

## 鎖定與證據邊界

- 9 篇主要技術正文均公開可讀。
- 9 篇的下載/解壓密碼區塊均未作技術事實來源，統一標記 `COFFEE_RESOURCE_BLOCK`。
- 沒有下載、執行或安全信任任何舊工具。
- 沒有把文章中的病毒掃描敘述升格成目前檔案安全證明。
- 沒有修改 client、server、database、PAK/IDX/SPR 或 runtime state。

## 本輪同步文件

- `docs/JJ_SITE_RESEARCH_TRACKER.md`
- `docs/JJ_LINEAGE_RESEARCH_MAP.md`
- `docs/JJ_LINEAGE_RESEARCH_MAP_ADDENDUM_20260907_1623.md`
- `docs/JJ_RESEARCH_PROGRESS_20260907_1816.md`
- `docs/CODEX_STATUS.md`

## 下一輪優先

只有收到使用者下一個明確 research Goal 才開始；每批最多 10 篇，逐篇完整保留 Client/Server/Protocol/resource/tool/packet/format 事實，更新文件、commit、push 後立即停止。重讀不增加 88/783 正式基線。

1. 天堂私服尚未完成的 Server/Protocol 核心文章：NPC / Item / packet/state 的正常遊戲路徑，而非只看 GM path。
2. x64dbg / OllyDBG / Cheat Engine 逐篇完成，形成 debugger playbook。
3. x86 / 排序演算法只抽 ABI、pointer、stable multi-key reorder 直接相關內容。
4. 依 Lineage cross-reference 讀 XML/MySQL/Java/C；不做無關語法重複閱讀。
5. 最後補齊 Archives 79 頁、783 篇 title-level inventory 與逐篇狀態。
