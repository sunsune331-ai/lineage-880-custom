# J.J. article-level reread — R31–R40

> Continuation of the 2026-09-07 18:33 article-level reread. Formal completed baseline remains **88 / 783; remaining 695**. No reread increments completion.

### R31 — 天堂私服 | GM指令分析/修改 (描述)「L1J版」
- **Canonical/original URL:** https://morosedog.gitlab.io/private-lineage-20210921-private-lineage-54/
- **Source/date:** J.J.'s Blogs / J.J. Huang / 2021-09-21
- **Classification:** Server
- **Version/access:** L1J-3.80c / PUBLIC_FULL
- **Main content:** analyzes `.desc ${角色名稱}` and modifies the command from describing the caller to describing a named online target.
- **DB/config:** `commands` row `desc`; `class_name=L1Describe`.
- **Class/method/field:** `src/l1j/server/server/command/executor/L1Describe.java`; `L1PcInstance` extends `L1Character`; target lookup through `L1World.getInstance().getPlayer(arg)`; response through `S_SystemMessage`.
- **Technical fields gathered by old command:** name; HP regen `getHpr()+getInventory().hpRegenPerTick()`; MP regen equivalent; `getDmgup()`, `getHitup()`, `getMr()`, `getKarma()`, inventory `getSize()`.
- **Operation flow:** commands table -> L1Describe -> inspect caller `pc` -> replace/augment with world target lookup -> target getters -> if target null send offline/not-found message -> verify with GM + second account.
- **Packet/protocol:** system-message packet class is involved; no numeric opcode established.
- **Limits/notes:** this is an old GM-inspection example; field meanings, visibility/permissions and message packet are version-specific. It potentially exposes information about another player in that old implementation, but that is not a current-project claim.
- **Cross-support/conflict:** corroborates R28 command dispatcher and R32/R33 use of `L1World`/inventory owners; no technical contradiction found.
- **8.8 relation:** reusable owner lookup -> state getter -> outbound display trace. Verify actual permissions, player registry and packet in 8.8.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R32 — 天堂私服 | GM指令分析/修改 (金幣)「L1J版」
- **Canonical/original URL:** https://morosedog.gitlab.io/private-lineage-20211015-private-lineage-55/
- **Source/date:** J.J.'s Blogs / 2021-10-15
- **Classification/version/access:** Server / L1J-3.80c / PUBLIC_FULL
- **Main content:** analyzes `.adena ${金幣數量}` and uses it to inspect player inventory ownership, stack merging and item insertion.
- **DB/class:** `commands` class `L1Adena`; `src/l1j/server/server/command/executor/L1Adena.java`; `src/l1j/server/server/model/L1PcInventory.java`.
- **Runtime object details:** evaluates `pc.getInventory()`; object is `L1PcInventory`; `_owner` is `L1PcInstance`; `_items` corresponds to held items; nested instance can be `L1Weapon`, with `_name` example `歐西斯匕首`.
- **Method/flow:** `pc.getInventory().storeItem(L1ItemId.ADENA,count)` returns `L1ItemInstance`; store path checks count/item validity, contains a special branch for item 40312 (hotel key, not further analyzed), checks stackability; if no existing stack adds new item, otherwise increments quantity and updates; command then sends result/system message.
- **Modified example:** accepts `me` or username; target lookup via `L1World`; if absent sends `S_ServerMessage(73,char_name)` in this old source; target inventory receives Adena.
- **Limits/conflicts:** numeric message 73 is old-version only. Article conclusion appears to contain an editorial typo saying L1Adena handles `desc`; body consistently analyzes `adena`, so typo is preserved rather than normalized.
- **Cross-support:** strongly corroborates R33 create-item and R26/R27 inventory materialization.
- **8.8 relation:** high-priority inventory trace: owner/container -> stack search -> merge/new instance -> persistence/update -> client refresh.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R33 — 天堂私服 | GM指令分析 (創立道具)「L1J版」
- **Canonical/original URL:** https://morosedog.gitlab.io/private-lineage-20211016-private-lineage-56/
- **Source/date/category:** J.J.'s Blogs / 2021-10-16 / 天堂私服 05.核心分析/修改
- **Classification/version/access:** Server / L1J-3.80c / PUBLIC_FULL
- **Main content:** detailed `.item` implementation, including argument parsing, template lookup, stack/non-stack materialization, inventory checks, persistence, type mappings, accessory enchant bonuses and weapon attribute enchant.
- **Command syntax:** `.item ${物品名稱} ${數量} ${強化等級} ${鑑定狀態} ${武器屬性} ${屬性等級}`.
- **DB/class/method:** `commands.class_name=L1CreateItem`; `src/l1j/server/server/command/executor/L1CreateItem.java`; `src/l1j/server/server/model/L1PcInventory.java`; type mapping in `src/l1j/server/server/datatables/ItemTable.java` old lines 66–213. Item template lookup spans `etcitem`, `weapon`, `armor` via `ItemTable`.
- **Parsed fields:** `nameid`, `count`, `enchant`, `isId`, `attr`, `attLevel`. `nameid` is interpreted as numeric ID when possible, otherwise first matching item name is resolved; missing item yields a client-visible error.
- **Stackable flow:** fetch template -> if nonexistent report error -> check stackability -> construct `L1ItemInstance`; stack items normally forced no enchant + count; identification optional; inventory acceptance checks include old limits of max 180 item kinds, weight capacity, and count <= 2,000,000,000; `storeItem` includes special handling for competition ticket/hotel key/pine/ebony/maple wand/military-horse helmet etc.; persistence writes `character_items`; response reports obtained item. Article notes `createItem` can be followed to understand object-ID generation and `L1World` update.
- **Type2:** `0=L1EtcItem`, `1=L1Weapon`, `2=L1Armor`.
- **Type mappings preserved:** etcitem 0 arrow,1 wand,2 light,3 gem,4 totem,5 firecracker,6 potion,7 food,8 scroll,9 questitem,10 spellbook,11 petitem,12 other,13 material,14 event,15 sting,16 treasure_box,17 magic_doll,18 spellscroll,19 spellwand,20 spellicon,21 protect_scroll. Weapon: 1 sword,2 twohandsword,3 dagger,4 bow,5 arrow,6 spear,7 blunt,8 staff,9 claw,10 dualsword,11 gauntlet,12 sting,13 chainsword,14 kiringku. Armor: 1 helm,2 t_shirts,3 armor,4 cloak,5 glove,6 boots,7 shield,8 guarder,10 amulet,11 ring,12 earring,13 belt,14 pattern_back,15 pattern_left,16 pattern_right,17 talisman_left,18 talisman_right.
- **Accessory enchant logic:** for armor Type2=2 and Type 8–12, loops per enchant level; Grade 0/1/2/3 get different incremental bonuses; at loop index 5 (`+6`) an extra reward is applied. Examples in code: Grade0 elemental MR +1 per level and +HPR/+MPR at +6; Grade1 HP +2 per level and MR extra at +6; Grade2 MP +1 per level and SP extra at +6; Grade3 says Taiwan version not implemented.
- **Weapon attribute logic:** Type2=1; old mapping earth `1`, fire `2`, water `4`, wind `8`; attribute level accepted >0 and <=3 and written via `setAttrEnchantKind/Level`; article says tested level 1–3 did not change the displayed name.
- **Limits/notes:** many constants/categories are old-source facts only. Article itself says analysis is partial because creation touches many subsystems.
- **Cross-support:** strengthens R32 storeItem and R27 field-provenance lesson; strongly relevant to inventory owner/persistence research.
- **8.8 relation:** verify actual template registry, acceptance limits, stack merge, instance ID allocator, persistence, special-item branches and outbound inventory update; do not reuse constants.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R34 — 天堂私服 | GM指令分析 (創立套裝)「L1J版」
- **URL/source/date:** https://morosedog.gitlab.io/private-lineage-20211017-private-lineage-57/ / J.J.'s Blogs / 2021-10-17
- **Classification/version/access:** Server / L1J-3.80c / PUBLIC_FULL
- **Main content:** `.itemset ${套裝名稱}` exposes XML-driven GM item-set configuration.
- **DB/class:** `commands.class_name=L1CreateItemSet`; `src/l1j/server/server/command/executor/L1CreateItemSet.java`.
- **Debug flow:** unknown set `1` produces `1 是未定義的套裝內容`; breakpoint/evaluate `GMCommandsConfig.ITEM_SETS` reveals key `dkset`; using it creates Death Knight set at +10.
- **Article typo preserved:** examples show `.imteset` (misspelled) although command is consistently described as `itemset`; do not silently treat typo as protocol fact.
- **Config/code:** `src/l1j/server/server/GMCommandsConfig.java` extends `ListLoaderAdapter`, overrides `loadElement`; tracing `ITEM_SETS.put`, `loadItem(elem)`, `loadElement`, `load` reaches `./data/xml/GmCommands/GMCommands.xml`.
- **XML structure:** top-level portions include `ItemSetList` and `RoomList`. Example `<ItemSet Name="DKSet">` contains items Id 58, 20010, 20100, 20166, 20198 with Amount=1, Enchant=10.
- **Materialization:** for non-stackable items with nonzero target enchant, old code loops creation/enchant progression to requested +10; IDs/enchant data live in `GMCommandsConfig.ITEM_SETS` after XML load.
- **Cross-support:** corroborates R33 item creation and R24 XML-as-server-config pattern.
- **8.8 relation:** verify whether equivalent bulk item sets/config are XML, DB or code, when loaded/cached, and whether inventory refresh is batched.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R35 — 天堂私服 | GM指令分析 (移動)「L1J版」
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20211020-private-lineage-60/ / 2021-10-20
- **Classification/version/access:** Shared / L1J-3.80c / PUBLIC_FULL
- **Main:** `.move X Y mapid` traces server map metadata and teleport state/Client update.
- **DB:** `commands.class_name=L1Move`; `mapids` fields documented: `mapid`, `locationname`, `startX/endX/startY/endY`, `monster_amount`, `drop_rate`, `underwater`, `markable`, `teleportable`, `escapable`, `resurrection`, `painwand`, `penalty`, `take_pets`, `recall_pets`, `usable_item`, `usable_skill`. Example mapid 4 = whole mainland in old DB.
- **Examples:** `.move 32671 32836 69` hidden valley; `.move 32782 32756 68` singing island.
- **Classes/methods:** `src/l1j/server/server/command/executor/L1Move.java`; parses `locx`, `locy`, `mapid`; calls `L1Teleport.teleport(pc,locx,locy,mapid,5,false)`. Old heading mapping 0 upper-left,1 up,2 upper-right,3 right,4 lower-right,5 down,6 lower-left,7 left; false = no teleport beam/effect.
- **Deeper flow:** teleport closes active trade; optionally emits effect/delay; sets client/player location, map and heading; consults `server.properties` setting to decide whether to wait for client notification; then `Teleportation.actionTeleportation` performs post-teleport logic (`src/l1j/server/server/utils/Teleportation.java`).
- **Cross-support:** supports R24 teleporter action and map-attribute R15; adds authoritative state transition details.
- **8.8 relation:** capture position owner, transaction cancellation, teleport packet/ack, map transition and renderer/interpolation; config branch must be reidentified.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R36 — 天堂私服 | GM指令分析 (創怪/創NPC)「L1J版」
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20211027-private-lineage-67/ / 2021-10-27
- **Classification/version/access:** Shared / L1J-3.80c / PUBLIC_FULL
- **Main:** `.insert mob ${npcid}` and `.insert npc ${npcid}` persist spawn definitions and immediately create runtime entity.
- **DB/command:** `commands.class_name=L1InsertSpawn`; `npc.impl` enumerated with SQL DISTINCT/GROUP BY. Article maps many impl strings: L1Scarecrow, L1Monster, L1Teleporter, L1Merchant, L1Housekeeper, L1Dwarf, L1Guard, L1Npc, L1Guardian, L1Quest, L1Board, L1FieldObject, L1Doll, L1Furniture, L1Effect, empty impl, L1Tower, L1Crown, L1Door, L1AuctionBoard, L1Signboard, L1DragonPortal, L1Fish. Author broadly groups into L1Monster vs non-L1Monster.
- **Concrete templates:** 安塔瑞斯 45682 (`impl=L1Monster`); 梅林 70074 (`L1Merchant`); fish 81308 (`L1Fish`). Commands are run at fixed positions; after server restart all remain, demonstrating persistence in that source.
- **Class/method/persistence:** `src/l1j/server/server/command/executor/L1InsertSpawn.java`; mob requires impl L1Monster then `SpawnTable.storeSpawn` -> INSERT `spawnlist` fields including location,count,npc_templateid,group_id,locx,locy,randomx,randomy,heading,min/max_respawn_delay,mapid. npc path uses `NpcSpawnTable.storeSpawn` -> INSERT `spawnlist_npc` location,count,npc_templateid,locx,locy,heading,mapid. Afterwards `L1SpawnUtil.spawn` immediately creates entity.
- **Architecture note:** author states impl classification corresponds to core class instances, example `L1Fish -> L1FishInstance`.
- **Cross-support:** strongly corroborates R18 NPC template lookup and current gfx chain; links persistence and runtime instance.
- **8.8 relation:** verify template -> impl/factory -> spawn persistence -> runtime instance -> visibility packet -> gfx/resource ID end-to-end.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R37 — 天堂私服 | Server、Client 是什麼？
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210713-private-lineage-1/ / 2021-07-13
- **Classification/version/access:** Other / general concepts, old Lineage teaching context / PUBLIC_FULL
- **Main:** introductory definitions: server may mean hardware or software that provides services; the Lineage "emulator" is described as software/service simulating a game server; client is the service consumer and need not literally be a person.
- **Model:** uses convenience-store analogy to explain request/response/service availability. The article treats server/client relationship at conceptual level, not packet structure.
- **Files/classes/packet:** none specific.
- **Limits:** educational analogy only; no 8.8 architecture fact.
- **Cross-support:** supplies terminology used by later DB/network/config articles.
- **8.8 relation:** none directly beyond keeping authority/request/response boundaries explicit.
- **Tags:** `待驗證` only where mapped to 8.8; `版本未確認` n/a for general concept; `僅 research evidence`.

### R38 — 天堂私服 | 資料庫 Database(DB) 是什麼？
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210718-private-lineage-6/ / 2021-07-18
- **Classification/access:** Other / PUBLIC_FULL
- **Version:** general DB/SQL teaching with Lineage motivation; no implementation version asserted in core definition.
- **Main:** database/DBMS concepts; relational tables/rows/columns; SQL for query/manipulation/definition/access control. Example `SELECT * FROM CUSTOMER WHERE AGE = 18;`.
- **Teaching model:** database=warehouse, table=shelf, column/cell=storage position; alternative Excel analogy database=file, table=sheet, field/cell=A1.
- **Tools/formats/classes/packet:** no Lineage-specific class or packet in this conceptual article.
- **Limits:** analogies are pedagogical, not normalized schema definitions.
- **Cross-support:** foundation for R39 DB import and all later DB-to-code tracing.
- **8.8 relation:** reinforces that DB evidence must be joined to loader/cache/consumer before declaring runtime authority.
- **Tags:** `待驗證` for any 8.8 mapping; `僅 research evidence`.

### R39 — 天堂私服 | 匯入模擬器的資料庫
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210721-private-lineage-9/ / 2021-07-21
- **Classification/version/access:** Server / L1J-3.80c / PUBLIC_FULL
- **Environment/tools:** Oracle VM VirtualBox; clean Windows 7 x64 Ultimate; Notepad++; Navicat Premium 15.
- **DB files:** simulator provides `Table_schema` and `Initialization_data` as `.sql`. Under `db`: Japan (Japanese SQL), Taiwan (translated Chinese SQL), tips, `Pack_DB.bat`.
- **Batch detail:** `Pack_DB.bat` effectively `copy .\Taiwan\*.sql l1jdb_Taiwan.sql`; if not used, SQL can be imported one-by-one.
- **Important workaround:** after packing, open `l1jdb_Taiwan.sql`, remove trailing `SUB`; otherwise article shows MySQL error 1064 near `?` at line 1. This is preserved as environment/source-generation behavior, not a universal SQL rule.
- **Import flow:** Navicat -> create DB `380c` -> execute SQL file `l1jdb_Taiwan.sql` -> expect no import errors.
- **Limits:** exact schema/seed content not explained here; tool versions/encoding/import quirks are historical.
- **Cross-support:** R40 server.properties points JDBC URL to this DB; schema/seed distinction supports boot-provenance research.
- **8.8 relation:** inspect DDL, seed/migrations/loaders and runtime caches together; do not assume legacy import pipeline.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

### R40 — 天堂私服 | 設定檔說明設定與啟動模擬器
- **URL/date:** https://morosedog.gitlab.io/private-lineage-20210722-private-lineage-10/ / 2021-07-22
- **Classification/version/access:** Server / L1J-3.80c / PUBLIC_FULL
- **Environment/files:** VirtualBox Win7 x64; simulator config mostly `.properties`/`.xml`. `config` contains `altsettings.properties`, `c3p0-config.xml`, `charsettings.properties`, `fights.properties`, `java.properties`, `log.properties`, `rates.properties`, `record.properties`, `server.properties`.
- **JDBC example:** old `server.properties`: `Driver=com.mysql.jdbc.Driver`; URL changes from `jdbc:mysql://localhost/l1jtw?useUnicode=true&characterEncoding=utf8` to `jdbc:mysql://localhost/380c?...`; Login/Password example root/root.
- **Maps:** article originally instructs creating `maps`, copying/extracting `360_maps.zip` from L1J-3.50c, yielding server-used `*.txt`; author initially did not know generation process, then 2022-02-22 update says these can be generated from client with the later `超簡易地圖預覽` tool. This later correction/support is preserved.
- **Build:** `build/ant`, `BuildManagement.bat`; choose option 1, expect BUILD SUCCESSFUL; produces `l1jloader.jar`, `l1jserver.jar`; latter described as main server executable.
- **Boot:** `ServerStart.bat`; firewall prompt may appear; startup console logs vary by version; absence of Exception/Error taken as basic success in article.
- **Troubleshooting examples:** unknown database `l1jtw` -> DB missing or JDBC URL not changed; missing `380c.npc` -> source-specific table rename issue `npc380_131029 -> npc`; Access denied root -> wrong password; map initialization NPE -> missing maps; duplicate door warning in article tied to missing/misnested map txt; OutOfMemory -> raise launch memory 512→1024+ or restart approach suggested; later 2023 note Source/Target option 7 unsupported -> use JDK7/8 for this old build.
- **Limits/conflicts:** several fixes are highly environment/version specific and should not be treated as modern best practice; periodic restart as memory mitigation is article advice, not root-cause verification.
- **Cross-support:** directly corroborates R39 DB import and R13 server-map export; shows an article later updating earlier unknown map-generation statement.
- **8.8 relation:** build config provenance `file/default/env -> parsed field -> consumer -> runtime effect`; independently inventory modern DB driver/build/map sources.
- **Tags:** `待驗證` `版本未確認` `僅 research evidence`.

## Checkpoint 40 status
- **Reread attempted this checkpoint:** 10
- **Full main body reacquired:** 10
- **Main body inaccessible:** 0
- **Article-level records completed total:** 40 / 50
- **Articles with material details absent from previous batch summary:** 10
- **New verification items:** inventory hard/weight/count checks and persistence; item Grade/Type mappings; XML itemset loader/cache; teleport trade/effect/ack/post-state; full NPC impl/factory/persistence chain; DB schema-vs-seed/import provenance; server config/build/map provenance.
- **Previous-summary claims not re-confirmed:** 0; prior summaries are materially under-detailed but not disproved here.
- **Differences/conflicts newly preserved:** R32 `desc` editorial typo; R34 `.imteset` typo; R40 original unknown map generation later supplemented/corrected by 2022 tool note; environment-specific troubleshooting is explicitly not generalized.
- **Baseline impact:** none; remains **88 / 783, remaining 695**.
