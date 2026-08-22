package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ConfigAlt {
	private static final Log _log = LogFactory.getLog(ConfigAlt.class);
	/** 交易等級限制 */
	public static int Trade_Level;
	/** 交倉庫使用等級限制 */
	public static int Dwarf_Level;
	/** 奪魂特效編號 */
	public static int Soul_Hp_Gfx;
	/** 反彈特效編號 */
	public static int Death_Pant_Gfx;
	public static short GLOBAL_CHAT_LEVEL;
	public static short WHISPER_CHAT_LEVEL;
	public static byte AUTO_LOOT;
	public static int LOOTING_RANGE;
	public static boolean ALT_NONPVP;
	public static boolean ALT_PUNISHMENT;
	public static boolean ALT_ATKMSG;
	public static boolean CLAN_ALLIANCE;
	public static int ALT_ITEM_DELETION_TIME;
	public static boolean ALT_WHO_COMMANDX;
	public static int ALT_WHO_TYPE;
	public static double ALT_WHO_COUNT;
	public static int ALT_WAR_TIME;
	public static int ALT_WAR_TIME_UNIT;
	public static int ALT_WAR_INTERVAL;
	public static int ALT_WAR_INTERVAL_UNIT;
	public static int ALT_RATE_OF_DUTY;
	public static boolean SPAWN_HOME_POINT;
	public static int SPAWN_HOME_POINT_RANGE;
	public static int SPAWN_HOME_POINT_COUNT;
	public static int SPAWN_HOME_POINT_DELAY;
	public static int ELEMENTAL_STONE_AMOUNT;
	public static int HOUSE_TAX_INTERVAL;
	public static int HOUSE_TAX_ADENA;
	public static int MAX_DOLL_COUNT;/** 最大娃娃攜帶量 */
	public static int MAX_DOLL_COUNT2;
	public static String MAX_DOLL_METE;//轉生娃娃攜帶量
	public static int MAX_NPC_ITEM;
	public static int MAX_PERSONAL_WAREHOUSE_ITEM;
	public static int MAX_CLAN_WAREHOUSE_ITEM;
	public static int DELETE_CHARACTER_AFTER_LV;
	public static boolean DELETE_CHARACTER_AFTER_7DAYS;
	public static int NPC_DELETION_TIME;
	public static int DEFAULT_CHARACTER_SLOT;
	public static int MEDICINE;
	public static int POWER;
	public static int POWERMEDICINE;
	public static boolean DORP_ITEM;
	public static final int MAX_NPC = 35;
	public static int MAX_PARTY_SIZE;
	public static int METE_LEVEL;
	public static int METE_MAX_COUNT;
	public static int METE_EXP_REDUCE;
	public static int METE_REMAIN_HP;
	public static int METE_REMAIN_MP;
	public static boolean METE_GIVE_POTION;
	public static boolean APPRENTICE_SWITCH;
	public static int APPRENTICE_LEVEL;
	public static int APPRENTICE_EXP_BONUS;
	public static int APPRENTICE_ITEM_ID;
	public static int ELYOS_ENCHANT_SUCCESS;
	/** 飾品開放最高強化到多少(強化最高等級需對應william_lv_accessory表) */
	public static int EnchantAccessory;
	/** 普通歐林的飾品強化卷軸幾率 */
	public static int Orim_ENCHANT_SUCCESS;
	/** 受祝福歐林的飾品強化卷軸幾率 */
	public static int Orim_ENCHANT_SUCCESS_BLESS;
	public static List<Integer> DRAGON_KEY_MAP_LIST = new ArrayList<Integer>();
	public static List<Integer> GIVE_ITEM_LIST = new ArrayList<Integer>();
	public static List<Integer> NO_AI_MAP_LIST = new ArrayList<Integer>();
	public static boolean WHO_ONLINE_MSG_ON;
	public static boolean GM_OVERHEARD;
	public static boolean GM_OVERHEARD0;
	public static boolean GM_OVERHEARD2;
	public static boolean GM_OVERHEARD4;
	public static boolean GM_OVERHEARD11;
	public static boolean GM_OVERHEARD13;
	public static boolean ALT_WARPUNISHMENT;
	/** 初始記憶座標數量上限 */
	public static int CHAR_BOOK_INIT_COUNT;
	/** 最大可擴充數量上限 */
	//public static int CHAR_BOOK_MAX_CHARGE;
	//public static int DRAGON_DROP_MONEY;
	//public static int BOSS_DROP_MONEY;
	public static boolean Dividend;//紅利
	public static int DividendItem;
	public static int DividendQuantity;//紅利END
	public static int DividendQuantityCount;// SRC0910
	public static int enchantovertell1;//防武安定廣播
	public static int enchantovertell2;
	public static boolean enchantoverTF;   
	public static int enchantweapon;
	public static int enchantarmor;
	public static int enchantoverType;//防武安定廣播END
	// public static int CUSTOM_HPR;// 自定義地圖回血量
	// public static int CUSTOM_MPR;// 自定義地圖回魔量
	// public static int CUSTOM_MAPID;// 自定義回血魔地圖
	//public static boolean WAR_Hier;// 攻城旗幟內是否允許攜帶祭司 true:允許 false:禁止
    public static boolean WAR_summon;// 攻城旗幟內是否允許召喚寵物 true:允許 false:禁止
	// 是否開放給其他人看見[陣營稱號]和[轉生稱號] 
	//public static boolean SHOW_SP_TITLE;
	public static boolean ReincarnationBroad;// 轉生是否會公告
	/** BOSS水晶強化機率 */
	public static boolean Cross_BOSS;//boss水晶
    public static boolean DE_BOSS;//boss水晶
    public static boolean NewCreate;//出生公告
    public static String Protector_Name;//***守護者***
	/** GM上線是否自動隱身 */
	public static boolean ALT_GM_HIDE;
	/** 連線獎勵等級限制 */
	public static short ONLINE_GIFT_LEVEL;
	/** 掉寶公告是否顯示螢幕 */
	public static boolean BOX_BROAD_SCREEN;
	/** 殺人公告是否顯示螢幕 */
	public static boolean KILL_BROAD_SCREEN;
	/*是否啟動組隊同地圖經驗加成*/
	public static boolean partyexp = true;
	
	public static int partynum10;
	
	public static int partynum20;
	
	public static int partynum30;
	
	public static int partyexplv;//END
	// 武器加成特效 
	/** 特效延遲時間 (單位:秒) */
	public static int WEAPON_EFFECT_DELAY;
	/** 武器額外持續特效1 (過3-過4) */
	public static int WEAPON_EFFECT_1;
	/** 武器額外持續特效2 (過5-過6) */
	public static int WEAPON_EFFECT_2;
	/** 武器額外持續特效3 (過7-過8) */
	public static int WEAPON_EFFECT_3;
	/** 武器額外持續特效4 (過9-過10) */
	public static int WEAPON_EFFECT_4;
	/** 武器額外持續特效5 (過11-過12) */
	public static int WEAPON_EFFECT_5;
	/** 武器額外持續特效6 (過13-過14) */
	public static int WEAPON_EFFECT_6;
	public static int WEAPON_EFFECT_7;
	public static int WEAPON_EFFECT_8;
	public static int WEAPON_EFFECT_9;
	public static int WEAPON_EFFECT_10;
	public static int WEAPON_EFFECT_11;
	public static int WEAPON_EFFECT_12;
	public static int WEAPON_EFFECT_13;
	public static int WEAPON_EFFECT_14;
	public static int WEAPON_EFFECT_15;
	public static int WEAPON_EFFECT_16;
	public static int WEAPON_EFFECT_17;
	public static int WEAPON_EFFECT_18;
	public static int WEAPON_EFFECT_19;
	public static int WEAPON_EFFECT_20;
	public static int WEAPON_EFFECT_21;
	public static int WEAPON_EFFECT_22;
	public static int WEAPON_EFFECT_23;
	public static int WEAPON_EFFECT_24;
	public static int WEAPON_EFFECT_25;
	public static int WEAPON_EFFECT_26;
	public static int WEAPON_EFFECT_27;
	public static int WEAPON_EFFECT_28;
	public static int WEAPON_EFFECT_29;
	public static int WEAPON_EFFECT_30;

	
	public static boolean WEAPON_POWER;// 武器+9(含)以上附加額外增加傷害值 是否開啟
    public static int[] WEAPON_POWER_LIST;/** 各階段強化值附加傷害 */
    //public static String CreateCharInfo;// 玩家出生公告 (null = 不公告)
    public static int[] NEW_CHAR_LOC;//新手出生座標
   // public static int PVP_WEAPON;//武器PVP
    private static final String ALT_SETTINGS_FILE = "./config/altsettings.properties";

	public static void load() throws ConfigErrorException {
		Properties set = new Properties();
		try {
			InputStream is = new FileInputStream(new File("./config/altsettings.properties"));

			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			set.load(isr);
			is.close();
			
			// 交易等級限制
			Trade_Level = Integer.parseInt(set.getProperty("trade_level", "52"));
			// 交倉庫使用等級限制
			Dwarf_Level = Integer.parseInt(set.getProperty("dwarf_level", "5"));
			// 奪魂特效編號
			Soul_Hp_Gfx = Integer.parseInt(set.getProperty("soul_hp_gfx", "11677"));
			// 反彈特效編號
			Death_Pant_Gfx = Integer.parseInt(set.getProperty("death_pant_gfx", "10710"));

			WHO_ONLINE_MSG_ON = Boolean.parseBoolean(set.getProperty("WHO_ONLINE_MSG_ON", "true"));

			GM_OVERHEARD = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD", "false"));
			GM_OVERHEARD0 = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD0", "false"));
			GM_OVERHEARD2 = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD2", "false"));
			GM_OVERHEARD4 = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD4", "false"));
			GM_OVERHEARD11 = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD11", "false"));
			GM_OVERHEARD13 = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD13", "false"));

			GLOBAL_CHAT_LEVEL = Short.parseShort(set.getProperty("GlobalChatLevel", "30"));

			WHISPER_CHAT_LEVEL = Short.parseShort(set.getProperty("WhisperChatLevel", "5"));

			AUTO_LOOT = Byte.parseByte(set.getProperty("AutoLoot", "2"));

			LOOTING_RANGE = Integer.parseInt(set.getProperty("LootingRange", "3"));

			ALT_NONPVP = Boolean.parseBoolean(set.getProperty("NonPvP", "true"));

			ALT_PUNISHMENT = Boolean.parseBoolean(set.getProperty("Punishment", "true"));

			ALT_WARPUNISHMENT = Boolean.parseBoolean(set.getProperty("WarPunishment", "false"));

			CLAN_ALLIANCE = Boolean.parseBoolean(set.getProperty("ClanAlliance", "true"));

			ALT_ITEM_DELETION_TIME = Integer.parseInt(set.getProperty("ItemDeletionTime", "10"));
			if (ALT_ITEM_DELETION_TIME > 60) {
				ALT_ITEM_DELETION_TIME = 60;
			}

			ALT_WHO_COMMANDX = Boolean.parseBoolean(set.getProperty("WhoCommandx", "false"));

			// WHO 顯示 額外設置方式 0:對話視窗顯示 1:視窗顯示
			// 這一項設置必須在WhoCommandx = true才有作用
			ALT_WHO_TYPE = Integer.parseInt(set.getProperty("Who_type", "0"));

			ALT_WHO_COUNT = Double.parseDouble(set.getProperty("WhoCommandcount", "1.0"));
			if (ALT_WHO_COUNT < 1.0D) {
				ALT_WHO_COUNT = 1.0D;
			}

			String strWar = set.getProperty("WarTime", "2h");
			if (strWar.indexOf("d") >= 0) {
				ALT_WAR_TIME_UNIT = Calendar.DATE;
				strWar = strWar.replace("d", "");
			} else if (strWar.indexOf("h") >= 0) {
				ALT_WAR_TIME_UNIT = Calendar.HOUR_OF_DAY;
				strWar = strWar.replace("h", "");
			} else if (strWar.indexOf("m") >= 0) {
				ALT_WAR_TIME_UNIT = Calendar.MINUTE;
				strWar = strWar.replace("m", "");
			}
			ALT_WAR_TIME = Integer.parseInt(strWar);// 攻城戰持續時間

			strWar = set.getProperty("WarInterval", "4d");
			if (strWar.indexOf("d") >= 0) {
				ALT_WAR_INTERVAL_UNIT = Calendar.DATE;
				strWar = strWar.replace("d", "");
			} else if (strWar.indexOf("h") >= 0) {
				ALT_WAR_INTERVAL_UNIT = Calendar.HOUR_OF_DAY;
				strWar = strWar.replace("h", "");
			} else if (strWar.indexOf("m") >= 0) {
				ALT_WAR_INTERVAL_UNIT = Calendar.MINUTE;
				strWar = strWar.replace("m", "");
			}
			ALT_WAR_INTERVAL = Integer.parseInt(strWar);// 攻城戰間隔時間

			SPAWN_HOME_POINT = Boolean.parseBoolean(set.getProperty("SpawnHomePoint", "true"));

			SPAWN_HOME_POINT_COUNT = Integer.parseInt(set.getProperty("SpawnHomePointCount", "2"));

			SPAWN_HOME_POINT_DELAY = Integer.parseInt(set.getProperty("SpawnHomePointDelay", "100"));

			SPAWN_HOME_POINT_RANGE = Integer.parseInt(set.getProperty("SpawnHomePointRange", "8"));

			ELEMENTAL_STONE_AMOUNT = Integer.parseInt(set.getProperty("ElementalStoneAmount", "300"));

			HOUSE_TAX_INTERVAL = Integer.parseInt(set.getProperty("HouseTaxInterval", "10"));

			HOUSE_TAX_ADENA = Integer.parseInt(set.getProperty("HouseTaxAdena", "2000"));

			MAX_DOLL_COUNT = Integer.parseInt(set.getProperty("MaxDollCount", "1"));/** 最大娃娃攜帶量 */

			MAX_DOLL_COUNT2 = Integer.parseInt(set.getProperty("MaxDollCount2","1"));/** 最大娃娃攜帶量 */
			
			MAX_DOLL_METE = set.getProperty("MAX_DOLL_METE","1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20");//轉生娃娃攜帶量
			
			MAX_NPC_ITEM = Integer.parseInt(set.getProperty("MaxNpcItem", "8"));

			MAX_PERSONAL_WAREHOUSE_ITEM = Integer.parseInt(set.getProperty("MaxPersonalWarehouseItem", "100"));

			MAX_CLAN_WAREHOUSE_ITEM = Integer.parseInt(set.getProperty("MaxClanWarehouseItem", "200"));

			DELETE_CHARACTER_AFTER_LV = Integer.parseInt(set.getProperty("DeleteCharacterAfterLV", "60"));

			DELETE_CHARACTER_AFTER_7DAYS = Boolean.parseBoolean(set.getProperty("DeleteCharacterAfter7Days", "True"));

			NPC_DELETION_TIME = Integer.parseInt(set.getProperty("NpcDeletionTime", "10"));

			DEFAULT_CHARACTER_SLOT = Integer.parseInt(set.getProperty("DefaultCharacterSlot", "4"));

			MEDICINE = Integer.parseInt(set.getProperty("Medicine", "20"));

			POWER = Integer.parseInt(set.getProperty("Power", "35"));

			POWERMEDICINE = Integer.parseInt(set.getProperty("MedicinePower", "45"));

			DORP_ITEM = Boolean.parseBoolean(set.getProperty("dorpitem", "true"));

			MAX_PARTY_SIZE = Integer.parseInt(set.getProperty("MaxPT", "8"));

			METE_LEVEL = Integer.parseInt(set.getProperty("MeteLevel", "99"));

			METE_MAX_COUNT = Integer.parseInt(set.getProperty("MeteMaxCount", "20"));

			METE_EXP_REDUCE = Integer.parseInt(set.getProperty("MeteExpReduce", "0"));

			METE_REMAIN_HP = Integer.parseInt(set.getProperty("MeteRemainHp", "15"));

			METE_REMAIN_MP = Integer.parseInt(set.getProperty("MeteRemainMp", "15"));

			METE_GIVE_POTION = Boolean.parseBoolean(set.getProperty("MeteGivePotion", "false"));

			APPRENTICE_SWITCH = Boolean.parseBoolean(set.getProperty("ApprenticeSwitch", "true"));

			APPRENTICE_LEVEL = Integer.parseInt(set.getProperty("ApprenticeLevel", "70"));

			APPRENTICE_EXP_BONUS = Integer.parseInt(set.getProperty("ApprenticeExpBonus", "0"));

			APPRENTICE_ITEM_ID = Integer.parseInt(set.getProperty("ApprenticeItemId", "30336"));

			ELYOS_ENCHANT_SUCCESS = Integer.parseInt(set.getProperty("ElyosEnchantSuccess", "0"));

			// 飾品開放最高強化到多少(強化最高等級需對應william_lv_accessory表)
			EnchantAccessory = Integer.parseInt(set.getProperty("EnchantAccessory", "9"));
			Orim_ENCHANT_SUCCESS = Integer.parseInt(set.getProperty("OrimEnchantSuccess", "0"));
			Orim_ENCHANT_SUCCESS_BLESS = Integer.parseInt(set.getProperty("OrimEnchantSuccessbless", "0"));

			CHAR_BOOK_INIT_COUNT = Integer.parseInt(set.getProperty("CharBookInitCount", "60"));

			//CHAR_BOOK_MAX_CHARGE = Integer.parseInt(set.getProperty("CharBookMaxCharge", "4"));

			//DRAGON_DROP_MONEY = Integer.parseInt(set.getProperty("DrgonDropMoney", "1000"));

			//BOSS_DROP_MONEY = Integer.parseInt(set.getProperty("BossDropMoney", "3000"));

			
			//贊助紅利
			Dividend = Boolean.parseBoolean(set.getProperty("Dividend", "false"));
			
			DividendItem = Integer.parseInt(set.getProperty("DividendItem", "1"));
			
			DividendQuantity = Integer.parseInt(set.getProperty("DividendQuantity", "1"));
			
			DividendQuantityCount = Integer.parseInt(set.getProperty("DividendQuantityCount", "1"));// SRC0910
			
			//防武安定沖裝廣播
			enchantweapon = Integer.parseInt(set.getProperty("enchantweapon", "20"));
			
			enchantarmor = Integer.parseInt(set.getProperty("enchanarmor", "20"));
			
			enchantovertell1 = Integer.parseInt(set.getProperty("enchantovertell1", "3"));
			
			enchantovertell2 = Integer.parseInt(set.getProperty("enchantovertell2", "3"));
			
			enchantoverTF = Boolean.parseBoolean(set.getProperty("enchantoverTF", "false"));
			
			enchantoverType = Integer.parseInt(set.getProperty("enchantoverType", "1"));
			
			// 自訂義回血魔區
			// CUSTOM_HPR = Integer.parseInt(set.getProperty("costom_hpr", "20"));
			// CUSTOM_MPR = Integer.parseInt(set.getProperty("costom_mpr", "20"));
			// CUSTOM_MAPID = Integer.parseInt(set.getProperty("costom_mapid", "4"));
			
			// 攻城旗幟內是否允許攜帶祭司 true:允許 false:禁止
			//WAR_Hier = Boolean.parseBoolean(set.getProperty("war_hier", "true"));
			// 攻城旗幟內是否允許召喚寵物 true:允許 false:禁止
			WAR_summon = Boolean.parseBoolean(set.getProperty("war_summon","true"));
			// 是否開放給其他人看見[陣營稱號]和[轉生稱號] 
			//SHOW_SP_TITLE = Boolean.parseBoolean(set.getProperty("ShowSpTitle","false"));
			// 轉生是否會公告
			ReincarnationBroad = Boolean.parseBoolean(set.getProperty("ReincarnationBroad", "false"));
			
			//PVP_WEAPON = Integer.parseInt(set.getProperty("PVP_WEAPON", "",""));
			//boss水晶
			Cross_BOSS = Boolean.parseBoolean(set.getProperty("Cross_BOSS", "false"));
			//boss水晶   
	        DE_BOSS = Boolean.parseBoolean(set.getProperty("DE_BOSS", "false"));
			//** 出生公告 **//
	        NewCreate = Boolean.parseBoolean(set.getProperty("NewCreate", "false"));
	        //***守護者***/
			Protector_Name = set.getProperty("Protector_Name", "");
			/** GM上線是否自動隱身 */
			ALT_GM_HIDE = Boolean.parseBoolean(set.getProperty("GmHide", "true"));
			/** 連線獎勵等級限制 */
			ONLINE_GIFT_LEVEL = Short.parseShort(set.getProperty("onlinegiftlevel", "5"));
			/** 掉寶公告是否顯示螢幕 */
			BOX_BROAD_SCREEN = Boolean.parseBoolean(set.getProperty("BOX_BROAD_SCREEN", "false"));
			/** 殺人公告是否顯示螢幕 */
			KILL_BROAD_SCREEN = Boolean.parseBoolean(set.getProperty("KILL_BROAD_SCREEN", "false"));
			/*是否啟動組隊同地圖經驗加成*/
			partyexp = Boolean.parseBoolean(set.getProperty("partyexp", "true"));
			
			partynum10 = Integer.parseInt(set.getProperty("partynum10", "10"));
			
			partynum20 = Integer.parseInt(set.getProperty("partynum20", "10"));
			
			partynum30 = Integer.parseInt(set.getProperty("partynum30", "10"));
			
			partyexplv = Integer.parseInt(set.getProperty("partyexplv", "99"));//END
			

			
			// 武器加成特效 
			/** 特效延遲時間 (單位:秒) */
			WEAPON_EFFECT_DELAY = Integer.parseInt(set.getProperty("WeaponEffectDelay", "0"));
			/** 武器額外持續特效1 (過3-過4) */
			WEAPON_EFFECT_1 = Integer.parseInt(set.getProperty("WeaponEffect1","-1"));
			/** 武器額外持續特效2 (過5-過6) */
			WEAPON_EFFECT_2 = Integer.parseInt(set.getProperty("WeaponEffect2","-1"));
			/** 武器額外持續特效3 (過7-過8) */
			WEAPON_EFFECT_3 = Integer.parseInt(set.getProperty("WeaponEffect3","-1"));
			/** 武器額外持續特效4 (過9-過10) */
			WEAPON_EFFECT_4 = Integer.parseInt(set.getProperty("WeaponEffect4","-1"));
			/** 武器額外持續特效5 (過11-過12) */
			WEAPON_EFFECT_5 = Integer.parseInt(set.getProperty("WeaponEffect5","-1"));
			/** 武器額外持續特效6 (過13-過14) */
			WEAPON_EFFECT_6 = Integer.parseInt(set.getProperty("WeaponEffect6","-1"));
			WEAPON_EFFECT_7 = Integer.parseInt(set.getProperty("WeaponEffect7", "-1"));
			WEAPON_EFFECT_8 = Integer.parseInt(set.getProperty("WeaponEffect8", "-1"));
			WEAPON_EFFECT_9 = Integer.parseInt(set.getProperty("WeaponEffect9", "-1"));
			WEAPON_EFFECT_10 = Integer.parseInt(set.getProperty("WeaponEffect10", "-1"));
			WEAPON_EFFECT_11 = Integer.parseInt(set.getProperty("WeaponEffect11", "-1"));
			WEAPON_EFFECT_12 = Integer.parseInt(set.getProperty("WeaponEffect12", "-1"));
			WEAPON_EFFECT_13 = Integer.parseInt(set.getProperty("WeaponEffect13", "-1"));
			WEAPON_EFFECT_14 = Integer.parseInt(set.getProperty("WeaponEffect14", "-1"));
			WEAPON_EFFECT_15 = Integer.parseInt(set.getProperty("WeaponEffect15", "-1"));
			WEAPON_EFFECT_16 = Integer.parseInt(set.getProperty("WeaponEffect16", "-1"));
			WEAPON_EFFECT_17 = Integer.parseInt(set.getProperty("WeaponEffect17", "-1"));
			WEAPON_EFFECT_18 = Integer.parseInt(set.getProperty("WeaponEffect18", "-1"));
			WEAPON_EFFECT_19 = Integer.parseInt(set.getProperty("WeaponEffect19", "-1"));
			WEAPON_EFFECT_20 = Integer.parseInt(set.getProperty("WeaponEffect20", "-1"));
			WEAPON_EFFECT_21 = Integer.parseInt(set.getProperty("WeaponEffect21", "-1"));
			WEAPON_EFFECT_22 = Integer.parseInt(set.getProperty("WeaponEffect22", "-1"));
			WEAPON_EFFECT_23 = Integer.parseInt(set.getProperty("WeaponEffect23", "-1"));
			WEAPON_EFFECT_24 = Integer.parseInt(set.getProperty("WeaponEffect24", "-1"));
			WEAPON_EFFECT_25 = Integer.parseInt(set.getProperty("WeaponEffect25", "-1"));
			WEAPON_EFFECT_26 = Integer.parseInt(set.getProperty("WeaponEffect26", "-1"));
			WEAPON_EFFECT_27 = Integer.parseInt(set.getProperty("WeaponEffect27", "-1"));
			WEAPON_EFFECT_28 = Integer.parseInt(set.getProperty("WeaponEffect28", "-1"));
			WEAPON_EFFECT_29 = Integer.parseInt(set.getProperty("WeaponEffect29", "-1"));
			WEAPON_EFFECT_30 = Integer.parseInt(set.getProperty("WeaponEffect30", "-1"));
			
			// 武器+9(含)以上附加額外增加傷害值 
			/** 是否開啟 */
			WEAPON_POWER = Boolean.parseBoolean(set.getProperty("WeaponPower","false"));

			/** 各階段強化值附加傷害 */
			if (WEAPON_POWER == true) {
				WEAPON_POWER_LIST = new int[] {
						Integer.parseInt(set.getProperty("WeaponPower09", "0")),
						Integer.parseInt(set.getProperty("WeaponPower10", "0")),
						Integer.parseInt(set.getProperty("WeaponPower11", "0")),
						Integer.parseInt(set.getProperty("WeaponPower12", "0")),
						Integer.parseInt(set.getProperty("WeaponPower13", "0")),
						Integer.parseInt(set.getProperty("WeaponPower14", "0")),
						Integer.parseInt(set.getProperty("WeaponPower15", "0")),
						Integer.parseInt(set.getProperty("WeaponPower16", "0")),
						Integer.parseInt(set.getProperty("WeaponPower17", "0")),
						Integer.parseInt(set.getProperty("WeaponPower18", "0")),
						Integer.parseInt(set.getProperty("WeaponPower19", "0")),
						Integer.parseInt(set.getProperty("WeaponPower20", "0")), };
			}

			// 角色出生座標 (格式: locx, locy, mapid) (設置 null 則啟動內建出生座標) by terry0412
			final String tmp12 = set.getProperty("NewCharLoc", "");
			if (!tmp12.equalsIgnoreCase("null")) {
				final String[] temp = tmp12.trim().split(","); // 去掉空白再分開
				if (temp.length == 3) { // 固定值: 3
					NEW_CHAR_LOC = new int[3];
					NEW_CHAR_LOC[0] = Integer.parseInt(temp[0]);
					NEW_CHAR_LOC[1] = Integer.parseInt(temp[1]);
					NEW_CHAR_LOC[2] = Integer.parseInt(temp[2]);

				} else {
					_log.info("[角色出生座標] 座標格式有誤, 請重新設置!");
				}
			}

			if (set.getProperty("DragonKeyMapList") != null) {
				for (String str : set.getProperty("DragonKeyMapList").split(",")) {
					DRAGON_KEY_MAP_LIST.add(Integer.valueOf(Integer.parseInt(str)));
				}
			}

			if (set.getProperty("GiveItemList") != null) {
				for (String str : set.getProperty("GiveItemList").split(",")) {
					GIVE_ITEM_LIST.add(Integer.valueOf(Integer.parseInt(str)));
				}
			}

			if (set.getProperty("NoAIMapList") != null) {
				for (String str : set.getProperty("NoAIMapList").split(",")) {
					NO_AI_MAP_LIST.add(Integer.valueOf(Integer.parseInt(str)));
				}
			}

		} catch (Exception e) {
			throw new ConfigErrorException("設置檔案遺失: " + ALT_SETTINGS_FILE);
		} finally {
			set.clear();
		}
	}
}
