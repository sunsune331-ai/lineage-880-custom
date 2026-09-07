# JJ.全站精華 — Persistent Research Task

## Mission

持續研究 J.J.'s Blogs 全站 783 篇文章，將每篇可取得正文的內容直接轉成 AI-first 結構化知識並寫入 GitHub；不依賴長聊天保存知識。

## Source of truth

- `research/article_ledger.jsonl`：逐篇主資料，一篇一行。
- `research/checkpoint_ledger.jsonl`：批次進度與累計基線。
- `research/relation_index.jsonl`：文章間 supports/conflicts/relates_to/needs_validation 關係。
- `research/validation_ledger.jsonl`：8.8 Client / Server / Runtime / Packet / DB / Resource 實驗驗證。
- `docs/JJ_SITE_RESEARCH_TRACKER.md`：人類可讀研究狀態，僅作輔助與舊基線交叉核對。

開始工作時必須從 GitHub 最新 ledger 動態計算目前 completed / remaining；不要沿用聊天中舊數字。

## Per-article ingestion

每一篇真正取得主要正文的文章，立即建立／補齊唯一 article record。至少保存：

- `id`
- `url`
- `canonical_url`
- `title`
- `source`
- `published_at`
- `domains`: Client / Server / Protocol / Shared / Other，可多值
- `topics`
- `versions`
- `claims`
- `technical_details`
- `artifacts`
- `tools`
- `classes`
- `methods`
- `fields`
- `packets`
- `opcodes`
- `resources`
- `procedures`
- `limitations`
- `project_relevance`
- `supports`
- `conflicts`
- `verify`
- `evidence_level`，網站文章預設 `research`
- `version_status`
- `eight_eight_status`
- `read_status`
- 若由舊資料遷移且資訊不足：`migration_status="incomplete"`, `needs_reread=true`

沒有資料時用 `[]`、`null` 或明確 `unknown`；禁止補造。

## Completeness policy

本階段以完整性與可追溯性優先，不做「看起來沒用就省略」的主觀篩除。

涉及天堂 Client、Server、Protocol、Database、UI、Packet、resource、SPR、PAK、IDX、TBT、IMG、BIN、Lin.bin、LinLogin.bin、NPC、Item、Inventory、GFX、Morph、launcher、network、修改/加解密/分析工具等技術資訊，都應保留。

版本太舊、來源可信度低、可能不適用 8.8、用途未明的內容：保留並標記 `unverified` / `version unknown` / `needs validation`，不得直接刪掉。

## Counting and inaccessible pages

只有實際取得主要正文並完成 article-level record 的文章才能增加 completed count。

以下不得冒充 FULL：
- 分類/索引頁
- 只有標題或摘要
- 空頁
- 純下載頁無正文
- LOCKED / COFFEE 鎖定而正文未取得
- 其他無法取得正文的頁面

無法取得正文時可記一筆狀態資料，但 `read_status` 必須反映 inaccessible/locked，且不增加 completed count。

## Dedupe

以 canonical URL 為主要去重鍵，正規化 http/https、尾斜線、fragment、常見 tracking parameters；但 URL 相似而正文不同時不得合併。

若既有 completed record 已存在，不重複計數。

## Batch behavior

由 `research/task_control.json` 控制。當前預設：

- 每批處理 10 篇新的 FULL article。
- 每完成 10 篇：更新 ledgers，commit，push，驗證遠端可讀，然後停止。
- `auto_continue=false`：不得自行開始下一批。
- 每累積 50 篇新 FULL article 才做一次跨文章 synthesis。

新聊天／新工作只要進入此研究任務，就從 GitHub 最新 checkpoint 接續，不需要使用者搬運舊聊天內容。

## Synthesis

Markdown synthesis 只保存高階結論、互相佐證、衝突與待驗證方向。逐篇資料仍以 `article_ledger.jsonl` 為主。

網站 research 即使多篇一致，也不得直接升級成 8.8 confirmed。正式專案事實必須由適當的 Client / Server source / Runtime / Packet / DB / Resource evidence 驗證，寫入 `validation_ledger.jsonl` 後再提升。

## Existing JJ Markdown

既有 `docs/JJ_*` 文件、歷史 progress、tracker、research map、checkpoint 報告全部保留。現階段：

- 不刪除
- 不搬移
- 不改名
- 不因 JSONL 新格式而重讀全部既有文章

舊資料不足時只標記 `needs_reread=true`，未來用到或明確要求時再補讀。

## Git and safety

- 每批 checkpoint 必須 commit + push。
- push 前先避免覆蓋最新 main；必要時安全同步，不 force push。
- 不 reset 使用者未提交成果。
- 不修改 Client / Server / Java / Database 功能程式碼。
- 不改動與本研究無關的工作。

## Return after each batch

每批完成後只需精簡回報：

- 本批新增 FULL 數
- 最新 completed / 783
- remaining
- Client / Server / Protocol / Shared / Other 統計
- inaccessible/locked 數
- 新增重要 research findings
- 新增 needs-validation 項
- duplicate canonical URL / article ID 檢查
- commit SHA
- push/remote verify 結果

完成 checkpoint 後停止，等待下一次啟動。