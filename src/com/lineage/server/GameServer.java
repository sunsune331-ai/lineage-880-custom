package com.lineage.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.add.L1Config;
import com.add.L1ResetMapTime;
import com.add.L1SystemMessageTable;
import com.add.BigHot.BigHotblingLock;
import com.add.BigHot.T_BigHotbling;
import com.add.Crack.L1CrackStart;
import com.add.MJBookQuestSystem.Loader.MonsterBookCompensateLoader;
import com.add.MJBookQuestSystem.Loader.MonsterBookDelete;
import com.add.MJBookQuestSystem.Loader.MonsterBookLoader;
import com.add.MJBookQuestSystem.Loader.WeekQuestDelete;
import com.add.MJBookQuestSystem.Templates.WeekQuestDateCalculator;
import com.add.Mobbling.IdLoad;
import com.add.Mobbling.MobblingLock;
import com.add.Mobbling.T_Mobbling;
import com.add.system.L1BlendTable;
import com.add.system.L1FireSmithCrystalTable;
import com.eric.RandomMobTable;
import com.eric.StartCheckWarTime;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.config.ConfigSQL;
import com.lineage.data.event.MiniGame.MiniSiegeNpcStart;
import com.lineage.data.event.ice.IceQueenSystem;
import com.lineage.list.BadNamesList;
import com.lineage.server.Controller.AttendController;
import com.lineage.server.Controller.ComesMonsterController;
import com.lineage.server.Controller.FishingTimeController;
import com.lineage.server.Controller.ServerTimerController;
import com.lineage.server.Controller.TamController;
import com.lineage.server.Controller.UserRankingController;
import com.lineage.server.datatables.*;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.datatables.lock.BoardReading;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.lock.CharItemBlessReading;
import com.lineage.server.datatables.lock.CharItemPowerReading;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.datatables.lock.CharMapTimeReading;
import com.lineage.server.datatables.lock.CharOtherReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.lock.CharacterConfigReading;
import com.lineage.server.datatables.lock.CharacterQuestReading;
import com.lineage.server.datatables.lock.ClanAllianceReading;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.lock.DwarfForChaReading;
import com.lineage.server.datatables.lock.DwarfForClanReading;
import com.lineage.server.datatables.lock.DwarfForElfReading;
import com.lineage.server.datatables.lock.DwarfReading;
import com.lineage.server.datatables.lock.FurnitureSpawnReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.datatables.lock.MailReading;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.datatables.lock.ServerReading;
import com.lineage.server.datatables.lock.SpawnBossReading;
import com.lineage.server.datatables.lock.TownReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.datatables.sql.ServerGmCommandTable;
import com.lineage.server.model.L1AttackList;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1ItemPower;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.thread.DeAiThreadPool;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.thread.NpcAiThreadPool;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.timecontroller.StartTimer_Event;
import com.lineage.server.timecontroller.StartTimer_Npc;
import com.lineage.server.timecontroller.StartTimer_Pc;
import com.lineage.server.timecontroller.StartTimer_Pet;
import com.lineage.server.timecontroller.StartTimer_Server;
import com.lineage.server.timecontroller.StartTimer_Skill;
import com.lineage.server.timecontroller.event.ranking.RankingHeroTimer;
import com.lineage.server.utils.LineageUtil;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldCrown;
import com.lineage.server.world.WorldDarkelf;
import com.lineage.server.world.WorldDragonKnight;
import com.lineage.server.world.WorldElf;
import com.lineage.server.world.WorldIllusionist;
import com.lineage.server.world.WorldKnight;
import com.lineage.server.world.WorldPet;
import com.lineage.server.world.WorldSummons;
import com.lineage.server.world.WorldWarrior;
import com.lineage.server.world.WorldWizard;

import william.AbilityOrginal;
import william.EnchantAccessory;
import william.EnchantOrginal;
import william.LimitedReward;
import william.ReincarnationSkill;
import william.SystemMessage;
import william.WeaponSoul;
import william.WilliamBuff;
import william.William_Online_Reward;

public class GameServer {

	private static final Log _log = LogFactory.getLog(GameServer.class);

	public final int startTime = (int) (System.currentTimeMillis() / 1000);

	private static GameServer _instance;

	public static GameServer getInstance() {
		if (_instance == null) {
			_instance = new GameServer();
		}
		return _instance;
	}

	public void initialize() throws Exception {

		PerformanceTimer timer = new PerformanceTimer();
		try {
			_log.info("\n\r--------------------------------------------------\n\r"
		            + "\n\r       外部設置：經驗倍率: " + ConfigRate.RATE_XP
		            + "\n\r       外部設置：正義質倍率: " + ConfigRate.RATE_LA
		            + "\n\r       外部設置：友好度倍率: "+ ConfigRate.RATE_KARMA
		            + "\n\r       外部設置：物品掉落倍率: " + ConfigRate.RATE_DROP_ITEMS
					+ "\n\r       外部設置：金幣掉落倍率: " + ConfigRate.RATE_DROP_ADENA
					+ "\n\r       外部設置：廣播等級限制: " + ConfigAlt.GLOBAL_CHAT_LEVEL
					+ "\n\r       外部設置：PK設置: " + (ConfigAlt.ALT_NONPVP ? "允許" : "不允許")
					+ "\n\r       外部設置：最大連線設置: " + Config.MAX_ONLINE_USERS
					+ "\n\r--------------------------------------------------");

			ServerReading.get().load(); //src016

			IdFactory.get().load();

			IdLoad.getInstance();

			CharObjidTable.get().load();

			CharacterConfigReading.get().load();
			
			// 人物好友紀錄
			BuddyTable.getInstance(); // 7.6

			AccountReading.get().load();

			GeneralThreadPool.get();

			PcOtherThreadPool.get();

			NpcAiThreadPool.get();

			DeAiThreadPool.get();

			ActivityNoticeTable.get().load(); // 活動

			L1SystemMessageTable.get().loadSystemMessage();// DB化系統設定

			SystemMessage.getInstance(); // DB化訊息

		    ExpTable.get().load();

			SprTable.get().load();

			MapsTable.get().load();

			MapExpTable.get().load();

			MapLevelTable.get().load();

			ItemTimeTable.get().load();

			L1WorldMap.get().load();

			L1GameTimeClock.init();

			NpcTable.get().load();

			NpcScoreTable.get().load();

			CharacterTable.loadAllCharName();

			CharacterTable.clearOnlineStatus();

			CharacterTable.clearSpeedError();// 加速器檢測 逾時錯誤歸0

			CharacterTable.clearBanError();// 定時外掛驗證 逾時錯誤歸0

			CharacterTable.clearInputBanError();// 定時外掛驗證 輸入錯誤歸0

			World.get();

			WorldCrown.get();

			WorldKnight.get();

			WorldElf.get();

			WorldWizard.get();

			WorldDarkelf.get();

			WorldDragonKnight.get();

			WorldIllusionist.get();

			WorldWarrior.get();

			WorldPet.get();

			WorldSummons.get();

			TrapTable.get().load();

			TrapsSpawn.get().load();

			ItemTable.get().load();

			DropTable.get().load();

			DropMapTable.get().load();

			DropItemTable.get().load();

			DropItemEnchantTable.get().load();// 掉落道具強化值

			DropMobTable.get(); // 全怪物掉落物品

			SkillsTable.get().load();

			SkillsProbabilityTable.get(); // 負面技能幾率設置DB化

			SkillsItemTable.get().load();

			MobGroupTable.get().load();

			NPCTalkDataTable.get().load();

			NpcActionTable.load();

			SpawnTimeTable.getInstance();

			SpawnTable.get().load();

			PolyTable.get().load();

			ShopTable.get().load();

			ShopCnTable.get().load();

			DungeonTable.get().load();

			DungeonRTable.get().load();

			NpcSpawnTable.get().load();

			DwarfForClanReading.get().load();

			// 血盟同盟資料 by terry0412
			ClanAllianceReading.get().load();// 7.6

			ClanReading.get().load();

			ClanBuffTable.getInstance(); // 血盟祝福系統

			RewardClanSkillsTable.get();

			if (com.lineage.data.event.ClanSkillDBSet.START) {
				com.lineage.server.datatables.lock.ClanReading.get().load();
			}

			ClanEmblemReading.get().load();

			// L1ClanMatching.getInstance().loadClanMatching();// 血盟推薦

			ReincarnationSkill.getInstance(); // 轉生天賦

			MonsterBookDelete.getInstance(); // 怪物圖鑒刪除遺失玩家資料
			WeekQuestDelete.getInstance(); // 周任務刪除遺失玩家資料
			MonsterBookLoader.getInstance();
			MonsterBookCompensateLoader.getInstance();
			if (Config.Week_Quest) {
				WeekQuestDateCalculator.getInstance().run();
			}

			InvSwapTable.getInstance(); // 裝備切換

			/** [原碼] 怪物對戰系統 */
			if (L1Config._2151) {
				MobblingLock.create().load();
				T_Mobbling.getStart();
			}

			/** [原碼] 大樂透系統 */
			if (L1Config._2161) {
				BigHotblingLock.create().load();
				T_BigHotbling.getStart();
			}

			/** [原碼] 指定地圖隨機產生指定怪物 */
			RandomMobTable.getInstance().startRandomMob();

			/** [原碼] 修正攻城逾時不更新 */
			StartCheckWarTime.getInstance();

			/** [原碼] 時空裂痕 */
			//L1CrackTime.getStart();
			L1CrackStart.getInstance();

			/** [源碼] 道具自訂義訊息*/
			william.WilliamItemMessage.getData();
			
			/** [源碼] 武器防具煉化能力*/
			A_ResolventTable.get().load();

			CastleReading.get().load();

			L1CastleLocation.setCastleTaxRate();

			GetBackRestartTable.get().load();

			DoorSpawnTable.get().load();

			WeaponSkillTable.get().load();

			WeaponSkillPowerTable.get().load();

			GetbackTable.loadGetBack();

			PetTypeTable.load();

			PetItemTable.get().load();

			ItemBoxTable.get().load();

			ResolventTable.get().load();

			NpcTeleportTable.get().load();
			
			NpcTeleportOutTable.get().load(); // 指定地圖指定時間傳走玩家

			MemoryBookTable.get().load(); // 記憶書

			NpcChatTable.get().load();

			ArmorSetTable.get().load();

			ItemTeleportTable.get().load();

			ItemPowerUpdateTable.get().load();

			CommandsTable.get().load();

			BeginnerTable.get().load();

			ItemRestrictionsTable.get().load();
			
			ServerGmCommandTable.get().load();

			SpawnBossReading.get().load();

			HouseReading.get().load();

			IpReading.get().load();

			TownReading.get().load();

			MailReading.get().load();// 7.6信件資料

			AuctionBoardReading.get().load();

			BoardReading.get().load();

			CharBuffReading.get().load();

			CharSkillReading.get().load();

			CharOtherReading.get().load();

			CharacterQuestReading.get().load();

			BadNamesList.get().load();

			SceneryTable.get().load();

			L1SkillMode.get().load();

			L1AttackList.load();

			L1ItemPower.load();

			// 加載連續魔法減低損傷資料
			L1PcInstance.load();

			CharItemsReading.get().load();

			DwarfReading.get().load();

			DwarfForChaReading.get().load();

			DwarfForElfReading.get().load();

			DollPowerTable.get().load();

			PetReading.get().load();

			CharItemsTimeReading.get().load();

			L1PcOther.load();

			EventTable.get().load();

			if (EventTable.get().size() > 0) {
				EventSpawnTable.get().load();
			}

			QuestMapTable.get().load();

			FurnitureSpawnReading.get().load();

			ItemMsgTable.get().load();

			WeaponPowerTable.get().load();

			FishingTable.get().load();

			CastleWarGiftTable.get().load();

			MapHprMprTable.get(); // 地圖回血回魔系統

			T_OnlineGiftTable.get();

			T_RankTable.get();

			ArmorKitPowerTable.getInstance().load();

			ItemVIPTable.get(); // VIP道具加值

			VipSetsTable.get(); // VIP能力資料

			ExtraPolyPowerTable.getInstance().load(); // 載入變身增加能力值src014

			StonePowerTable.getInstance().load(); // src039

			CheckItemPowerTable.get(); // 身上持有道具給予能力系統

			T_GameMallTable.get();

			if (Config.Item_Craft == 2) { // 火神製作(DB化)
				CraftListTable.getInstance();
				CraftInfoTable.getIns();
				LineageUtil.makeCraftSha1Code(); // 取得道具製作的驗證碼
			}

			T_CraftConfigTable.get(); // 伊薇火神製作

			QuestNewSetTable.getInstance(); // 官服任務系統
			QuestNewTable.getInstance(); // 官服任務系統

			ExtraMeteAbilityTable.getInstance().load();

			// CharApprenticeTable.getInstance().load();

			ExtraAttrWeaponTable.getInstance().load();

			// BoardOrimReading.get().load();

			ItemUpgradeTable.getInstance(); // 火神合成系統

			RefineTable.getInstance(); // 火神精煉

			/** [原碼] 潘朵拉抽抽樂 */
			// LuckyLotteryTable.getInstance().loadData();
			LuckyLotteryTable.getInstance();

			/** 重置限時地監 */
			L1ResetMapTime.get().ResetTimingMap();

			/** 載入等級排行榜資料 */
			RankingHeroTimer.load();

			/** 道具製造DB化 */
			L1BlendTable.getInstance().loadBlendTable();

			/** 火神融煉道具 */
			L1FireSmithCrystalTable.get().load();

			LimitedReward.getInstance(); // 即時獎勵系統

			William_Online_Reward.getInstance(); // 線上抽獎系統

			MobItemTable.get(); // 特定怪物死亡掉落道具或給予狀態系統

			CharacterGiftTable.getInstance(); // 精靈的祝賀禮物

			// 爆擊
			ExtraCriticalHitStoneTable.getInstance().load();

			// 超能
			SuperRuneTable.getInstance().load();

			// PC檢查時間軸 by terry0412
			CheckTimeController.getInstance().start();

			// 建立資料 [地圖群組設置資料 (入場時間限制)] by terry0412
			MapsGroupTable.get().load();

			// 載入資料 [地圖入場時間紀錄] by terry0412
			CharMapTimeReading.get().load();

			ItemIntegrationTable.get();// 物品升級系統

			ExtraMagicWeaponTable.getInstance().load();// 武器魔法DIY

			ExtraBossWeaponTable.getInstance().load();// boss武器

			ExtraItemStealTable.getInstance().load();// 死亡奪取

			ExpMeteUpTable.get().load();// 轉生經驗減少

			// 載入資料 [人物物品凹槽資料] by terry0412
			CharItemPowerReading.get().load();

			CharItemBlessReading.get().load();

			CharItemPowerTable.get().load(); // 強化擴充能力

			ItemHtmlTable.get(); // 自訂道具對話系統

			WeaponSoul.getInstance(); // 武器劍靈系統

			ServerSponsorItemTable.get(); // 神熾贊助送禮系統

			// 載入資料 [物品融合系統(DB自製)] by terry0412
			BlendTable.getInstance().load();

			// 載入資料 [NPC寶箱資料] by terry0412
			NpcBoxTable.get().load();

			CheckFightTimeController.getInstance().start();

			/** [原碼] 底比斯大戰遊戲 */
			if (ConfigOther.Mini_Siege) {
				MiniSiegeNpcStart.getInstance();
			}

			if (Config.UserRanking) {
				UserRankingController.getInstance(); // 新排行系統
			}

			FieldSpawnTable.getInstance(); // 動作佈景召喚系統

			// 官方簽到系統
			if (Config.Attend) {
				AttenDanceTable.getInstance();
				CharacterAttendTable.getInstance();
				AttendController.getInstance();
			}
			// 官方簽到系統end

			WilliamBuff.load(); // NPC魔法輔助DB化系統

			AbilityOrginal.getInstance(); // 強化擴充能力

			EnchantOrginal.getInstance(); // 裝備強化能力系統

			EnchantAccessory.getInstance(); // 飾品加成能力系統

			ServerTimerController.getInstance(); // 各種時間控制

			StartTimer_Server startTimer = new StartTimer_Server();
			startTimer.start();

			StartTimer_Pc pcTimer = new StartTimer_Pc();
			pcTimer.start();

			StartTimer_Npc npcTimer = new StartTimer_Npc();
			npcTimer.start();

			StartTimer_Pet petTimer = new StartTimer_Pet();
			petTimer.start();

			StartTimer_Skill skillTimer = new StartTimer_Skill();
			skillTimer.start();

			// 活動專用時間軸
			final StartTimer_Event eventTimer = new StartTimer_Event();
		    eventTimer.start();

		    // 指定時間召怪召物系統
            ComesMonsterController comesMonsterController = ComesMonsterController.getInstance();
	        GeneralThreadPool.get().execute(comesMonsterController);

			Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

			DeNameTable.get().load();

			DeClanTable.get().loadIcon();// 虛擬血盟盟徽

			DeClanTable.get().load();

			DeTitleTable.get().load();

			DeShopChatTable.get().load();

			DeGlobalChatTable.get().load();

			DeShopItemTable.get().load();

			EchoServerTimer.get().start();

			IceQueenSystem.getInstance().load();

			// ValakasRoomSystem.getInstance().load();

			L1DoorInstance.openDoor();

			// 成長果實系統(Tam幣)
			TamController tamController = TamController.getInstance();
			GeneralThreadPool.get().scheduleAtFixedRate(tamController, 0, TamController.SLEEP_TIME);

			// 日版釣魚
			GeneralThreadPool.get().schedule(FishingTimeController.getInstance(), 300);

			// BraveavatarController.getInstance(); // 韓版王者加護

			// System.out.println("test");
			// C_ExportXml e = new C_ExportXml();
			// e.export();

			// NewMapUtil.load("./data/map/");
			// DummyFishingTable.get(); // 假人釣魚

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			String osname = System.getProperties().getProperty("os.name");
			String username = System.getProperties().getProperty("user.name");

			String ver = "\n\r--------------------------------------------------"
			        + "\n\r       主機位置: " + Config.GAME_SERVER_HOST_NAME
			        + "\n\r       監聽端口: " + Config.GAME_SERVER_PORT
					+ "\n\r       伺服器作業系統: " + osname
					+ "\n\r       伺服器使用者: " + username
					+ "\n\r       使用者名稱資料庫: " + ConfigSQL.DB_URL2_LOGIN
					+ "\n\r       伺服器檔案資料庫: " + ConfigSQL.DB_URL2
					+ "\n\r       綁定登入器設置: " + Config.LOGINS_TO_AUTOENTICATION
					+ "\n\r--------------------------------------------------"
					+ "\n\r       伺服器版本 : L1AtuTw_8.8C" + "\n\r [只用來研究學習使用!]"// src010
					+ "\n\r--------------------------------------------------";
			_log.info(ver);

			CmdEcho cmdEcho = new CmdEcho(timer.get());
			cmdEcho.runCmd();
		}
	}
}
