# J.J. article-level reread — R21–R30

> Baseline remains **88 / 783; remaining 695**. Website evidence is research-only.

### R21 — 天堂私服 | 點擊跳轉其他對話檔分析<Link篇>
- URL: https://morosedog.gitlab.io/private-lineage-20220503-private-lineage-98/ ; date 2022-05-03; Classification: Client; Versions: 3.81C/L1J-3.80c; Access: PUBLIC_FULL.
- Main: analyzes client dialogue-to-dialogue navigation using `link` rather than server action.
- Concrete DB/resource path: 銀騎士村倉庫 高特 -> `spawnlist_npc.location='高特'` -> npcid 60024 -> `npcaction` -> `gotham`; export `gotham-c.html`.
- Markup/details: `<a link="pgotham">`, `<a link="gothamwp">`; article defines `link` as target client text-dialogue filename. `<img src="#331" link="gotham">` shows images can also carry `link`. Custom dialogue can be targeted if the resource was correctly ingested.
- Important limitation: author states this demonstrated `link` path displays pure-text dialogue; action attributes coexist in same `gotham` page (`deposit`, `retrieve`, `history`) but are a different mechanism.
- Parameters: warehouse-password page text describes 6-digit password; `option11` linked from dialog.
- Packet/class: no packet shown; absence is not proof of no packet.
- Cross-support: complements R22 Action篇 and R19/R20 markup dialect. Potential distinction Client-link vs Protocol-action is a hypothesis requiring capture.
- 8.8: capture click and verify whether link is local-only or emits traffic. `待驗證` `版本未確認` `僅 research evidence`.

### R22 — 天堂私服 | 點擊跳轉其他對話檔分析<Action篇>
- URL: https://morosedog.gitlab.io/private-lineage-20220504-private-lineage-99/ ; date 2022-05-04; Classification: Protocol; Versions 3.81C/L1J-3.80c; Access PUBLIC_FULL.
- Main: distinguishes HTML `action` from `link`; action value is used for interaction with server-side behavior.
- Example markup: `<img src="#312" action="buy">`, `<img src="#314" action="sell">`, `<a action="mellin3">詢問最近的物價</a>`.
- DB/resource path: article says NPC 梅林 and first reports `npcid=70074`, dialogue files including `telescave1/telescave2`; however its shown SQL/comment later uses `npcid=70101 -- mellin`. **Internal inconsistency preserved; do not normalize away.**
- Technical interpretation: `<a action>` is described as action/behavior used for server interaction; image tags can also carry action.
- Cross-support: R20's unresolved #312/#314 image behavior appears here in actual buy/sell controls; R23/R24 show action dispatch consequences.
- 8.8: packet capture required: UI action string/value -> outbound packet -> server dispatcher -> response. `待驗證` `版本未確認` `僅 research evidence`.

### R23 — 天堂私服 | 執行行動分析<強化魔法師篇>
- URL: https://morosedog.gitlab.io/private-lineage-20220505-private-lineage-100/ ; date 2022-05-05; Classification: Protocol; Versions 3.81C/L1J-3.80c; Access PUBLIC_FULL.
- Main: follows a 強化魔法師 NPC dialogue action into server behavior; clicking the highlighted option when funds are sufficient applies melee-oriented buffs and returns completion dialogue.
- DB/resource path: 倫德羅 -> spawnlist_npc -> npcid 81356 -> npcaction -> `bs_01`; extract `bs_01-c.html`.
- Technical: article intentionally only sketches code; action string from HTML is used to find corresponding server-side handling; money condition, skill application and response dialog are separate observable effects.
- Files: HTML/dialog resources; server action path. No stable opcode documented.
- Cross-support: confirms R22 action is not merely page navigation; supports action-dispatch methodology.
- 8.8: trace action key, cost validation, skill/status owner, response packet/dialog. `待驗證` `版本未確認` `僅 research evidence`.

### R24 — 天堂私服 | 執行行動分析<傳送師篇>
- URL: https://morosedog.gitlab.io/private-lineage-20220506-private-lineage-101/ ; date 2022-05-06; Classification: Shared; Versions 3.81C/L1J-3.80c; Access PUBLIC_FULL.
- Main: maps transporter dialog actions to server teleport configuration and dynamic values.
- DB/resource path: 麥特 -> `spawnlist_npc` -> npcid 50056 -> `npcaction` -> `telesilver1`, `telesilver2`, `telesilver3`; exports localized HTML.
- Technical/resources: article connects HTML action values with an XML/config `Action Name`; teleport entries carry destination X/Y, Map, Heading and Price; `var src`/Data Value positions are used to inject server-side values into dialogue.
- Operation: identify NPC -> extract HTML -> inspect action/var -> locate corresponding server/XML definition -> trigger and verify teleport/cost/result.
- Limits: XML runtime authority and exact schema are old-version evidence only.
- 8.8: validate config/DB source, teleport authoritative state, map transition packet and dialog data injection. `待驗證` `版本未確認` `僅 research evidence`.

### R25 — 天堂私服 | 執行行動分析<道具篇>
- URL: https://morosedog.gitlab.io/private-lineage-20220507-private-lineage-102/ ; date 2022-05-07; Classification: Shared; Versions 3.81C/L1J-3.80c; Access PUBLIC_FULL.
- Main: uses `說話卷軸` to show item-use -> server branch -> client dialogue.
- Applications listed: item -> morph list; auto-hunt/menu; portable shop; portable warehouse.
- DB/code details: `SELECT * FROM etcitem WHERE NAME='說話卷軸'` -> item_id `40641`; search literal 40641 -> `src/l1j/server/server/clientpackets/C_ItemUSe.java`; branch `else if (itemId == 40641)`; sends `pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tscrollp"));`.
- Packet/protocol: class names imply inbound item-use client packet handler and outbound NPC-talk-return packet object; no opcode value is given.
- Resource: target dialogue key `tscrollp`.
- Cross-support: direct Server->Protocol->Client resource bridge, corroborating NPC dialog articles.
- 8.8: locate equivalent item-use parser, item handler, response packet and dialog resolver. `待驗證` `版本未確認` `僅 research evidence`.

### R26 — 天堂私服 | 人物出生道具分析/修改 (一)「L1J版」
- URL: https://morosedog.gitlab.io/private-lineage-20210907-private-lineage-40/ ; date 2021-09-07; Classification: Server; Version L1J-3.80c; Access PUBLIC_FULL.
- Main: derives starter-item configuration from DB by comparing newly created characters, then traces loader code.
- DB observations: `beginner` table; mage inventory matches rows with `activate IN ('A','W')`; knight matches `('A','K')`; A appears common, W/K class-specific. This is an empirical old-version inference.
- Code: search `beginner` -> `src/l1j/server/server/model/Beginner.java`; SQL `SELECT * FROM beginner WHERE activate IN(?,?)`, verified manually with concrete A/W and A/K queries.
- Related stores: later flow relates item IDs to `etcitem`, `weapon`, `armor` and materialized character inventory.
- Method: visible new-character inventory -> table hypothesis -> SQL reproduction -> code literal -> runtime verification.
- Limits: `activate` semantics inferred by tests; exact codes/classes not transferable.
- 8.8: use same provenance audit for starter items and inventory materialization. `待驗證` `版本未確認` `僅 research evidence`.

### R27 — 天堂私服 | 人物出生道具分析/修改 (二)「L1J版」
- URL: https://morosedog.gitlab.io/private-lineage-20210908-private-lineage-41/ ; date 2021-09-08; Classification: Server; Version L1J-3.80c; Access PUBLIC_FULL.
- Fields recorded: `id`, `item_id`, `count`, `charge_count` (author explicitly uncertain), `enchantlvl`, `item_name`, `activate`, `bless`.
- Tests: `UPDATE beginner SET enchantlvl=9 WHERE id=21` -> newly created mage's Ivory Tower staff observed +9. Editing `item_name` has no effect because actual item names come from `etcitem/weapon/armor`; those are preloaded and server restart is needed for name changes.
- `bless` test: author expects 0=blessed, 1=normal; sets `bless=0` but new staff is not blessed. This failed expectation is preserved. Article proceeds into code analysis to understand why, suggesting a missing/not-applied assignment path rather than proving DB semantics.
- Limits/conflict: `charge_count` unresolved; `bless` expected mapping not borne out by observed result. This is an important corpus-level counterexample to trusting field names alone.
- 8.8: field-by-field provenance must reach final instance + persistence, not stop at DB schema. `待驗證` `版本未確認` `僅 research evidence`.

### R28 — 天堂私服 | GM指令的程式碼邏輯分析「L1J版」
- URL: https://morosedog.gitlab.io/private-lineage-20210917-private-lineage-50/ ; date 2021-09-17; Classification: Server; Version L1J-3.80c; Access PUBLIC_FULL.
- Main: establishes GM command dispatch architecture using `.allbuff` example.
- DB fields: `commands.name`, `access_level`, `class_name`; author tests that `characters.access_level` is the primary permission value rather than assuming accounts access_level.
- Code/class: search table literal -> `src/l1j/server/server/command/L1Commands.java`; command registry loads class names; subsequent usage leads to dispatcher/executor behavior.
- Operation: learn command syntax -> DB command row -> class lookup -> debug execution/caller.
- Cross-support: foundation for Who/HPBar/item/spawn command articles.
- 8.8: determine whether command registry is DB-driven/static and trace permission + dispatcher runtime. `待驗證` `版本未確認` `僅 research evidence`.

### R29 — 天堂私服 | GM指令/一般指令分析 (Who)「L1J版」
- URL: https://morosedog.gitlab.io/private-lineage-20210919-private-lineage-52/ ; date 2021-09-19; Classification: Protocol; Version L1J-3.80c; Access PUBLIC_FULL.
- Main: compares GM `.who` / `.who all` with ordinary `/who` / `/who <name>`; same visible domain has distinct entry paths.
- GM path: `commands` row `who`, class_name `L1Who` -> GM executor.
- General path: article traces ordinary command from clientpacket handling (`C_Who` in previous summary/flow) where input string is parsed and server sends response; exact opcode is not presented here as portable fact.
- Method: do not conflate chat/client packet command with GM command registry merely because UI text is similar.
- 8.8: capture both paths separately: command/chat entry -> parser -> world/player lookup -> response. `待驗證` `版本未確認` `僅 research evidence`.

### R30 — 天堂私服 | GM指令分析/修改 (怪物血條)「L1J版」
- URL: https://morosedog.gitlab.io/private-lineage-20210920-private-lineage-53/ ; date 2021-09-20; Classification: Shared; Version L1J-3.80c; Access PUBLIC_FULL.
- Main: `.hpbar on/off` shows HP bars for visible objects including NPCs, visible only to invoking user, and explores extending behavior.
- DB/class: `commands.hpbar`, class_name `L1HpBar`; `src/l1j/server/server/command/executor/L1HpBar.java`.
- Code behavior described: on branch sets status/buff `GMSTATUS_HPBAR`; off removes it; lines 42–46 in author's source create HP-bar packet data for visible objects. Article defines visible object broadly (monsters/NPCs/doors/items etc.).
- Packet/protocol: packet object/data generation is involved, but numeric opcode is not a portable claim.
- Cross-support: useful Shared example of server state flag -> per-viewer packet generation -> client-visible overlay.
- 8.8: validate HP owner, visibility list, update cadence, packet and client bar-render owner. `待驗證` `版本未確認` `僅 research evidence`.

## Checkpoint 30 status
- Reread attempted this checkpoint: 10
- Full main body reacquired: 10
- Main body inaccessible: 0
- Article-level records completed total: 30 / 50
- Articles with new details vs previous summary: 10
- New verification items: link-vs-action network boundary; action/NPC ID inconsistency; dynamic dialog vars; item-use->dialog packet path; starter-item cache/provenance; failed bless expectation; dual Who entry paths; HPBar viewer-specific packet/update.
- Previous-summary claims not re-confirmed: 0; one prior summary is now qualified by article-internal inconsistent IDs in R22.
- Difference vs previous summary: yes; substantial omitted details and two explicit uncertainty/conflict items.
- Baseline impact: none; **88 / 783** unchanged.
