package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ConfigOther {

	private static final Log _log = LogFactory.getLog(ConfigOther.class);

	// /** 連續攻擊開關 */
	// public static boolean Pc_Atk;
	/** 連續攻擊延遲時間(1000=1秒，默認300) */
	public static int Pc_Atk_Time = 100;

	/** 媽祖狀態經驗倍數 */
	public static double Mazu_Exp;
	/** 殷海薩龍之祝福指數外置設定 */
	public static int LEAVES_MAXEXP;
	/** 劍靈狀態圖示編號 **/
	public static int Weapon_Soul_IconId;
	/** 劍靈狀態圖示訊息編號 **/
	public static int Weapon_Soul_StringId;
	/** Tam幣系統-多少秒獲得1次Tam幣 **/
	public static int Tam_Time;
	/** Tam幣系統-1次獲得多少Tam幣(會乘以玩家開通的數量，比如設10000，玩家開通2個號就是20000個，最多只能開通3個) **/
	public static int TAM_COUNT;
	/** Tam幣系統-是否開啟獲得Tam幣提示訊息 **/
	public static boolean Tam_Msg;
	/** 重置轉生天賦需要的道具 **/
	public static int ReiItemId;
	/** 重置轉生天賦需要的道具數量 **/
	public static int ReiItemCount;
	/** 是否開啟使用轉生藥水給予天賦點數 **/
	public static boolean ReiPoint;
	/** 轉生藥水增加轉生天賦點數 **/
	public static String ReiPoinMete;
	/** 轉生天賦點數上限 **/
	public static int ReiPointUp;
	/** 全職第一項天賦技能等級上限 **/
	public static int ReiPointLv_1;
	/** 全職第二項天賦技能等級上限 **/
	public static int ReiPointLv_2;
	/** 全職第三項天賦技能等級上限 **/
	public static int ReiPointLv_3;

	/** 是否開啟底比斯大戰遊戲 **/
	public static boolean Mini_Siege;

	/** 底比斯大戰遊戲-摧毀守護塔獎勵道具 **/
	public static int A_TowerItemId;
	/** 底比斯大戰遊戲-摧毀守護塔獎勵道具數量 **/
	public static int A_TowerItemCount;

	/** 底比斯大戰遊戲-摧毀中塔獎勵道具 **/
	public static int B_TowerItemId;
	/** 底比斯大戰遊戲-摧毀中塔獎勵道具數量 **/
	public static int B_TowerItemCount;

	/** 底比斯大戰遊戲-最終獲勝獎勵道具 **/
	public static int C_TowerItemId;
	/** 底比斯大戰遊戲-最終獲勝獎勵道具數量 **/
	public static int C_TowerItemCount;

	/** 底比斯大戰遊戲-最終召喚BOSS編號 **/
	// public static int MiniSiege_BossId;
	public static int[] MiniSiege_BossId;

	/** 伺服器開啟後什麼時間才開啟底比斯大戰遊戲NPC召喚 **/
	public static Calendar MiniSiege_StartTime;

	/** 底比斯大戰遊戲-加入or裂痕NPC存在時間 **/
	public static int MiniSiege_ReadyTime;

	/** 底比斯大戰遊戲-遊戲時間 (秒) **/
	public static int MiniSiege_PlayTime;

	/** 底比斯大戰遊戲-下次開啟時間(分) **/
	public static int MiniSiege_NextTime;

	/** 底比斯大戰遊戲-遊戲最少人數 **/
	public static int MiniSiege_MinPlayer;

	/** 是否開啟紅騎士團 **/
	public static boolean Red_Knight;

	/** 參加紅騎士團等級 **/
	public static int Red_Knight_Lv;

	/** 是否開啟升級經驗獎勵狀態 **/
	public static boolean LEVEL_UP;
	/** 開啟後多久級給予經驗獎勵狀態 **/
	public static int LEVEL_UP_LV;
	/** 開啟後升級經驗獎勵狀態經驗倍數 **/
	public static double LEVEL_UP_EXP;

	/** 2個娃娃合成幾率 **/
	public static int DOLLSIZE2_CHANCE;
	/** 3個娃娃合成幾率 **/
	public static int DOLLSIZE3_CHANCE;
	/** 4個娃娃合成幾率 **/
	public static int DOLLSIZE4_CHANCE;
	/** 娃娃大成功合成幾率 **/
	public static int DOLL_CHANCE_BIG;

	/** 潘朵拉銅像使用扣除道具 **/
	public static int STATUE_MAGIC_ITEMID;
	/** 潘朵拉銅像使用扣除道具數量 **/
	public static int STATUE_MAGIC_ITEMCOUNT;
	/** 潘朵拉銅像指定輔助魔法ID **/
	public static int[] STATUE_MAGIC_SKILLID;
	/** 潘朵拉銅像指定輔助魔法時間 **/
	public static int[] STATUE_MAGIC_SKILLTIME;
	/** 潘朵拉銅像是否補滿血量 **/
	public static boolean STATUE_MAGIC_MAXHP;

	public static boolean SPEED = false;

	public static double SPEED_TIME = 1.0D;

	public static boolean KILLRED = true;

	public static int RATE_XP_WHO = 1;
	public static boolean CLANDEL;
	public static boolean CLANTITLE;
	public static int CLANCOUNT;
	public static boolean LIGHT;
	public static int WEAPON100;
	public static int ARMOR100;
	public static boolean HPBAR;
	public static boolean SHOPINFO;
	// public static int HOMEHPR;
	// public static int HOMEMPR;
	// public static int INNHPR;
	// public static int INNMPR;
	// public static int CASTLEHPR;
	// public static int CASTLEMPR;
	// public static int FORESTHPR;
	// public static int FORESTMPR;
	public static boolean WAR_DOLL;
	public static int SET_GLOBAL;
	public static int SET_GLOBAL_COUNT;
	public static int SET_GLOBAL_TIME;
	public static int ENCOUNTER_LV;
	public static int ILLEGAL_SPEEDUP_PUNISHMENT;

	public static double BOSS_POWER;
	public static int BOSS_HIT;
	public static int CHANGE_COUNT;
	public static int R_83055;
	public static int R_80033;

	public static int AC_170;
	public static int AC_160;
	public static int AC_150;
	public static int AC_140;
	public static int AC_130;
	public static int AC_120;
	public static int AC_110;
	public static int AC_100;
	public static int AC_90;
	public static int AC_80;
	public static int AC_70;
	public static int AC_60;
	public static int AC_50;
	public static int AC_40;
	public static int AC_30;
	public static int AC_20;
	public static int AC_10;
	/** 等级**/
	public static int pclevel;
	/** 殺BOSS領物品設置 **/
	public static int guiwugjbl;	
	/** 殺BOSS領物品設置 **/
	public static int guiwugjblmf;
	/** 殺BOSS領物品設置 **/
	public static int Npc_Conquest;
	public static int Npc_Conquest2;
	/** 血盟設置 **/
	public static int clancreatelv;
	public static int clanforwarlv;
	public static int clanskillitem1;
	public static int clanskillitem2;
	public static int clanskillitem3;
	// 是否開放給其他人看見[陣營稱號]和[轉生稱號]
	public static boolean SHOW_SP_TITLE;
	/** 冰女副本限制可開啟地圖清單 */
	public static List<Integer> iceKeyMapList = new ArrayList<Integer>();
	/** PVP設置 **/
	public static boolean PVP_WEAPON;// PVP武
	public static int PVP_plus;// PVP+
	public static boolean PVP_ARMOR;// PVP防
	public static int PVP_plus2;// PVP+
	public static int ELYOS_ENCHANT;// 最高只能強化到
	public static int ELYOS2_ENCHANT;// 最高只能強化到
	// 元寶偵測紀錄 by terry0412
	/** 啟動開關 */
	public static boolean ADENA_CHECK_SWITCH;
	/** 每XX秒判斷一次 */
	public static int ADENA_CHECK_TIME_SEC;
	/** 差異數量達到多少以上才紀錄 (位置:\物品操作日誌\元寶差異紀錄) */
	public static int ADENA_CHECK_COUNT_DIFFER;

	// 地圖使用時間已重置
	public static int[] Reset_Map_Time;

	public static boolean FREE_FIGHT_SWITCH; // src015

	/** 是否開啟全地圖掃街 */
	public static boolean FREE_FIGHT_ALLMAP;

	public static int FREE_FIGHT_DROP_CHANCE_A;

	public static int FREE_FIGHT_DROP_CHANCE_B;

	public static int FREE_FIGHT_REMAIN_TIME;

	public static String[] FREE_FIGHT_TIME_LIST;

	public static int FREE_FIGHT_MAP_MIN;

	public static int FREE_FIGHT_MAP_MAX;

	public static String[] FREE_FIGHT_MAP_LIST;

	public static int FREE_FIGHT_MAX_DROP;

	public static Calendar QUEST_SET_RESET_TIME; // src035

	public static Calendar MAZU_RESET_TIME; // src040

	public static int INJUSTICE_COUNT;

	public static int JUSTICE_COUNT;

	public static int CHECK_STRICTNESS;

	public static int CHECK_MOVE_STRICTNESS;

	public static int PUNISHMENT_TYPE;

	public static int PUNISHMENT_TIME;

	public static int PUNISHMENT_MAP_ID;

	/** 自創陣營戰系統 2016/10/12 By Erics4179新增 */

	public static int RedBlueJoin_itemid;// 報名道具編號
	public static int RedBlueJoin_count;// 報名道具數量
	public static int RedBluePc_amount;// 各隊報名人數
	public static int RedBlueLv_min;// 角色最低等級限制
	public static int RedBlueLv_max;// 角色最高等級限制
	public static int RedBlueTime_all;// 活動時間
	public static int RedBlueTime_clear;// 活動場次清潔時間
	public static int RedBlueEffect_time;// 活動開始凍結玩家時間
	public static int[] RedBlueEnd_map;// 活動結束傳送的地圖座標
	public static int[] RedBlueRed_map1;// ROOM1 紅隊傳送的地圖座標
	public static int[] RedBlueBlue_map1;// ROOM1 藍隊傳送的地圖座標
	public static int[] RedBlueRed_map2;// ROOM2 紅隊傳送的地圖座標
	public static int[] RedBlueBlue_map2;// ROOM2 藍隊傳送的地圖座標
	public static int RedBlueStart_point;// 各隊角色初始積分
	public static int RedBlueNormal_point;// 擊殺敵方成員所得積分
	public static int RedBlueLeader_point;// 擊殺敵方對長所得積分
	public static int RedBlueBonus_itemid;// 活動獎勵道具
	public static int RedBlueBonus_count;// 活動獎勵數量
	public static int RedBlueReward_times;// 可以領取幾次獎勵
    /** 大陸玩家登錄端口設置 */
 	public static List<Integer> wawaid1 = new ArrayList<Integer>();
 	/** 台灣玩家端口設置 */
 	public static List<Integer> wawaid2 = new ArrayList<Integer>();
    /** 大陸玩家登錄端口設置 */
 	public static List<Integer> wawaid3 = new ArrayList<Integer>();
 	/** 台灣玩家端口設置 */
 	public static List<Integer> wawaid4 = new ArrayList<Integer>();

	public static Calendar RedBlueReward_RESET_TIME; // src040

	private static final String LIANG = "./config/other.properties";

	public static void load() throws ConfigErrorException {
		Properties set = new Properties();
		try {
			InputStream is = new FileInputStream(new File("./config/other.properties"));
			set.load(is);
			is.close();

			// 媽祖狀態經驗倍數
			Mazu_Exp = Double.parseDouble(set.getProperty("Mazu_Exp", "1.0"));
			// 殷海薩龍之祝福指數外置設定
			LEAVES_MAXEXP = Integer.parseInt(set.getProperty("LEAVES_MAXEXP", "500"));
			// 劍靈狀態圖示編號
			Weapon_Soul_IconId = Integer.parseInt(set.getProperty("Weapon_Soul_IconId", "5223"));
			// 劍靈狀態圖示訊息編號
			Weapon_Soul_StringId = Integer.parseInt(set.getProperty("Weapon_Soul_StringId", "2174"));
			// Tam幣系統-多少秒獲得1次Tam幣
			Tam_Time = Integer.parseInt(set.getProperty("Tam_Time", "600")); // 成長果實系統(Tam幣)
			// Tam幣系統-1次獲得多少Tam幣(會乘以玩家開通的數量，比如設10000，玩家開通2個號就是20000個，最多只能開通3個)
			TAM_COUNT = Integer.parseInt(set.getProperty("TAM_COUNT", "10000")); // 成長果實系統(Tam幣)
			// Tam幣系統-是否開啟獲得Tam幣提示訊息
			Tam_Msg = Boolean.parseBoolean(set.getProperty("Tam_Msg", "false"));
			// 重置轉生天賦需要的道具
			ReiItemId = Integer.parseInt(set.getProperty("ReiItemId", "44070"));
			// 重置轉生天賦需要的道具數量
			ReiItemCount = Integer.parseInt(set.getProperty("ReiItemCount", "100"));
			// 是否開啟使用轉生藥水給予天賦點數
			ReiPoint = Boolean.parseBoolean(set.getProperty("ReiPoint", "false"));
			// 轉生藥水增加轉生天賦點數
			ReiPoinMete = set.getProperty("ReiPoinMete", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20");
			// 轉生天賦點數上限
			ReiPointUp = Integer.parseInt(set.getProperty("ReiPointUp", "20"));
			// 全職第一項天賦技能等級上限**/
			ReiPointLv_1 = Integer.parseInt(set.getProperty("ReiPointLv_1", "10"));
			// 全職第二項天賦技能等級上限**/
			ReiPointLv_2 = Integer.parseInt(set.getProperty("ReiPointLv_2", "10"));
			// 全職第三項天賦技能等級上限**/
			ReiPointLv_3 = Integer.parseInt(set.getProperty("ReiPointLv_3", "10"));

			// 是否開啟底比斯大戰遊戲
			Mini_Siege = Boolean.parseBoolean(set.getProperty("Mini_Siege", "false"));

			// 伺服器開啟後什麼時間才開啟底比斯大戰遊戲NPC召喚
			final String tmp13 = set.getProperty("minisiege_starttime", "");
			if (!tmp13.equalsIgnoreCase("null")) {
				final String[] temp = tmp13.split(":");
				if (temp.length == 3) {
					final Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(temp[1]));
					cal.set(Calendar.SECOND, Integer.parseInt(temp[2]));

					MiniSiege_StartTime = cal;

				} else {
					_log.info("[底比斯大戰遊戲]啟動時間有誤, 請重新設置!");
				}
			}

			// 底比斯大戰遊戲-摧毀守護塔獎勵道具
			A_TowerItemId = Integer.parseInt(set.getProperty("A_TowerItemId", "40308"));
			// 底比斯大戰遊戲-摧毀守護塔獎勵道具數量
			A_TowerItemCount = Integer.parseInt(set.getProperty("A_TowerItemCount", "10000"));

			// 底比斯大戰遊戲-摧毀中塔獎勵道具
			B_TowerItemId = Integer.parseInt(set.getProperty("B_TowerItemId", "40308"));
			// 底比斯大戰遊戲-摧毀中塔獎勵道具數量
			B_TowerItemCount = Integer.parseInt(set.getProperty("B_TowerItemCount", "20000"));

			// 底比斯大戰遊戲-最終獲勝獎勵道具
			C_TowerItemId = Integer.parseInt(set.getProperty("C_TowerItemId", "40308"));
			// 底比斯大戰遊戲-最終獲勝獎勵道具數量
			C_TowerItemCount = Integer.parseInt(set.getProperty("C_TowerItemCount", "30000"));

			// 底比斯大戰遊戲-最終召喚BOSS編號
			// MiniSiege_BossId =
			// Integer.parseInt(set.getProperty("MiniSiege_BossId", "45601"));
			String[] s1;
			s1 = set.getProperty("MiniSiege_BossId", "").split(",");
			MiniSiege_BossId = new int[s1.length];
			for (int i = 0; i < s1.length; i++) {
				MiniSiege_BossId[i] = Integer.parseInt(s1[i]);
			}

			// 底比斯大戰遊戲-裂痕NPC存在時間 (秒)
			MiniSiege_ReadyTime = Integer.parseInt(set.getProperty("MiniSiege_ReadyTime", "60"));

			// 底比斯大戰遊戲-遊戲時間 (秒)
			MiniSiege_PlayTime = Integer.parseInt(set.getProperty("MiniSiege_PlayTime", "300"));

			// 底比斯大戰遊戲-下次開啟時間(分)
			MiniSiege_NextTime = Integer.parseInt(set.getProperty("MiniSiege_NextTime", "60"));

			// 底比斯大戰遊戲-遊戲最少人數
			MiniSiege_MinPlayer = Integer.parseInt(set.getProperty("MiniSiege_MinPlayer", "3"));

			// 是否開啟紅騎士團
			Red_Knight = Boolean.parseBoolean(set.getProperty("red_knight", "false"));

			// 參加紅騎士團等級
			Red_Knight_Lv = Integer.parseInt(set.getProperty("red_knight_lv", "70"));

			// 是否開啟升級經驗獎勵狀態
			LEVEL_UP = Boolean.parseBoolean(set.getProperty("level_up", "false"));
			// 開啟後多久級給予經驗獎勵狀態
			LEVEL_UP_LV = Integer.parseInt(set.getProperty("level_up_lv", "60"));
			// 開啟後升級經驗獎勵狀態經驗倍數
			LEVEL_UP_EXP = Double.parseDouble(set.getProperty("level_up_exp", "1.23"));

			// 2個娃娃合成幾率
			DOLLSIZE2_CHANCE = Integer.parseInt(set.getProperty("dollsize2_chance", "10"));
			// 3個娃娃合成幾率
			DOLLSIZE3_CHANCE = Integer.parseInt(set.getProperty("dollsize3_chance", "15"));
			// 4個娃娃合成幾率
			DOLLSIZE4_CHANCE = Integer.parseInt(set.getProperty("dollsize4_chance", "20"));
			// 娃娃大成功合成幾率
			DOLL_CHANCE_BIG = Integer.parseInt(set.getProperty("doll_chance_big", "10"));

			// 潘朵拉銅像使用道具
			STATUE_MAGIC_ITEMID = Integer.parseInt(set.getProperty("Statue_Magic_ItemId", "40308"));
			// 潘朵拉銅像使用扣除道具數量
			STATUE_MAGIC_ITEMCOUNT = Integer.parseInt(set.getProperty("Statue_Magic_ItemCount", "10000"));
			// 潘朵拉銅像指定輔助魔法ID 魔法時間
			String[] s;
			s = set.getProperty("Statue_Magic_SkillId", "").split(",");
			STATUE_MAGIC_SKILLID = new int[s.length];
			for (int i = 0; i < s.length; i++) {
				STATUE_MAGIC_SKILLID[i] = Integer.parseInt(s[i]);
			}
			s = set.getProperty("Statue_Magic_SkillTime", "").split(",");
			STATUE_MAGIC_SKILLTIME = new int[s.length];
			for (int i = 0; i < s.length; i++) {
				STATUE_MAGIC_SKILLTIME[i] = Integer.parseInt(s[i]);
			}
			// 潘朵拉銅像是否補滿血量
			STATUE_MAGIC_MAXHP = Boolean.parseBoolean(set.getProperty("Statue_Magic_Maxhp", "false"));

			SPEED = Boolean.parseBoolean(set.getProperty("speed", "false"));

			SPEED_TIME = Double.parseDouble(set.getProperty("speed_time", "1.0"));

			ILLEGAL_SPEEDUP_PUNISHMENT = Integer.parseInt(set.getProperty("Punishment", "0"));

			ENCOUNTER_LV = Integer.parseInt(set.getProperty("encounter_lv", "20"));

			KILLRED = Boolean.parseBoolean(set.getProperty("kill_red", "false"));

			RATE_XP_WHO = Integer.parseInt(set.getProperty("rate_xp_who", "1"));

			CLANDEL = Boolean.parseBoolean(set.getProperty("clanadel", "false"));

			CLANTITLE = Boolean.parseBoolean(set.getProperty("clanatitle", "false"));

			CLANCOUNT = Integer.parseInt(set.getProperty("clancount", "100"));

			LIGHT = Boolean.parseBoolean(set.getProperty("light", "false"));

			WEAPON100 = Integer.parseInt(set.getProperty("weapon100", "30"));
			ARMOR100 = Integer.parseInt(set.getProperty("armor100", "30"));

			HPBAR = Boolean.parseBoolean(set.getProperty("hpbar", "false"));

			SHOPINFO = Boolean.parseBoolean(set.getProperty("shopinfo", "false"));

			// HOMEHPR = Integer.parseInt(set.getProperty("homehpr", "10"));
			// HOMEMPR = Integer.parseInt(set.getProperty("homempr", "10"));
			// INNHPR = Integer.parseInt(set.getProperty("innhpr", "10"));
			// INNMPR = Integer.parseInt(set.getProperty("innmpr", "10"));
			// CASTLEHPR = Integer.parseInt(set.getProperty("castlehpr", "10"));
			// CASTLEMPR = Integer.parseInt(set.getProperty("castlempr", "10"));
			// FORESTHPR = Integer.parseInt(set.getProperty("foresthpr", "10"));
			// FORESTMPR = Integer.parseInt(set.getProperty("forestmpr", "10"));

			SET_GLOBAL = Integer.parseInt(set.getProperty("set_global", "100"));

			SET_GLOBAL_COUNT = Integer.parseInt(set.getProperty("set_global_count", "100"));

			SET_GLOBAL_TIME = Integer.parseInt(set.getProperty("set_global_time", "5"));

			WAR_DOLL = Boolean.parseBoolean(set.getProperty("war_doll", "true"));

			BOSS_POWER = Double.parseDouble(set.getProperty("bosspower", "3.0"));

			BOSS_HIT = Integer.parseInt(set.getProperty("bosshit", "50"));

			CHANGE_COUNT = Integer.parseInt(set.getProperty("changecount", "1000000"));

			R_83055 = Integer.parseInt(set.getProperty("r_83055", "2000000"));
			R_80033 = Integer.parseInt(set.getProperty("r_80033", "85"));

			AC_170 = Integer.parseInt(set.getProperty("AC_170", "0"));
			AC_160 = Integer.parseInt(set.getProperty("AC_160", "0"));
			AC_150 = Integer.parseInt(set.getProperty("AC_150", "0"));
			AC_140 = Integer.parseInt(set.getProperty("AC_140", "0"));
			AC_130 = Integer.parseInt(set.getProperty("AC_130", "0"));
			AC_120 = Integer.parseInt(set.getProperty("AC_120", "0"));
			AC_110 = Integer.parseInt(set.getProperty("AC_110", "0"));
			AC_100 = Integer.parseInt(set.getProperty("AC_100", "0"));
			AC_90 = Integer.parseInt(set.getProperty("AC_90", "0"));
			AC_80 = Integer.parseInt(set.getProperty("AC_80", "0"));
			AC_70 = Integer.parseInt(set.getProperty("AC_70", "0"));
			AC_60 = Integer.parseInt(set.getProperty("AC_60", "0"));
			AC_50 = Integer.parseInt(set.getProperty("AC_50", "0"));
			AC_40 = Integer.parseInt(set.getProperty("AC_40", "0"));
			AC_30 = Integer.parseInt(set.getProperty("AC_30", "0"));
			AC_20 = Integer.parseInt(set.getProperty("AC_20", "0"));
			AC_10 = Integer.parseInt(set.getProperty("AC_10", "0"));
			
			pclevel = Integer.parseInt(set.getProperty("pclevel", "50"));
			
			guiwugjbl = Integer.parseInt(set.getProperty("guiwugjbl", "1"));
			
			guiwugjblmf = Integer.parseInt(set.getProperty("guiwugjblmf", "1"));
			/** 殺BOSS領物品設置 **/
			Npc_Conquest = Integer.parseInt(set.getProperty("Npc_Conquest", "50"));

			Npc_Conquest2 = Integer.parseInt(set.getProperty("Npc_Conquest2", "50"));
			// 血盟設置
			clancreatelv = Integer.parseInt(set.getProperty("clancreatelv", "50"));

			clanforwarlv = Integer.parseInt(set.getProperty("clanforwarlv", "50"));

			clanskillitem1 = Integer.parseInt(set.getProperty("clanskillitem1", "50"));

			clanskillitem2 = Integer.parseInt(set.getProperty("clanskillitem2", "50"));

			clanskillitem3 = Integer.parseInt(set.getProperty("clanskillitem3", "50"));

			INJUSTICE_COUNT = Integer.parseInt(set.getProperty("InjusticeCount", "8"));

			JUSTICE_COUNT = Integer.parseInt(set.getProperty("JusticeCount", "12"));

			CHECK_STRICTNESS = Integer.parseInt(set.getProperty("CheckStrictness", "110"));

			CHECK_MOVE_STRICTNESS = Integer.parseInt(set.getProperty("CheckMoveStrictness", "110"));

			PUNISHMENT_TYPE = Integer.parseInt(set.getProperty("PunishmentType", "1"));
			PUNISHMENT_TIME = Integer.parseInt(set.getProperty("PunishmentTime", "5"));
			PUNISHMENT_MAP_ID = Integer.parseInt(set.getProperty("PunishmentMap", "666"));

			// 是否開放給其他人看見[陣營稱號]和[轉生稱號]
			SHOW_SP_TITLE = Boolean.parseBoolean(set.getProperty("ShowSpTitle", "false"));
			// PVP系統 武器
			PVP_WEAPON = Boolean.parseBoolean(set.getProperty("PVP_WEAPON", "false"));
			// PVP+
			PVP_plus = Integer.parseInt(set.getProperty("PVP_plus", "1"));
			// PVP系統 防具
			PVP_ARMOR = Boolean.parseBoolean(set.getProperty("PVP_ARMOR", "false"));
			// PVP+
			PVP_plus2 = Integer.parseInt(set.getProperty("PVP_plus2", "1"));
			// 最高只能強化到
			ELYOS_ENCHANT = Integer.parseInt(set.getProperty("ElyosEnchant", "0"));
			// 最高只能強化到
			ELYOS2_ENCHANT = Integer.parseInt(set.getProperty("Elyos2Enchant", "0"));
			if (set.getProperty("wawaid1") != null) {
	         	for (final String str : set.getProperty("wawaid1").split(
	         	  ",")) {
	         		wawaid1.add(Integer.parseInt(str));
	         	}
	         	}
	            // 台灣玩家登錄端口
	         	if (set.getProperty("wawaid2") != null) {
	         	for (final String str : set.getProperty("wawaid2").split(
	         	  ",")) {
	         		wawaid2.add(Integer.parseInt(str));
	         	}
	         	}
	         	if (set.getProperty("wawaid3") != null) {
	             	for (final String str : set.getProperty("wawaid3").split(
	             	  ",")) {
	             		wawaid3.add(Integer.parseInt(str));
	             	}
	             	}
	                // 台灣玩家登錄端口
	             	if (set.getProperty("wawaid4") != null) {
	             	for (final String str : set.getProperty("wawaid4").split(
	             	  ",")) {
	             		wawaid4.add(Integer.parseInt(str));
	             	}
	             	}
			// 元寶偵測紀錄 by terry0412
			/** 啟動開關 */
			ADENA_CHECK_SWITCH = Boolean.parseBoolean(set.getProperty("AdenaCheckSwitch", "false"));
			/** 每XX秒判斷一次 */
			ADENA_CHECK_TIME_SEC = Integer.parseInt(set.getProperty("AdenaCheckTimeSec", "5"));
			/** 差異數量達到多少以上才紀錄 (位置:\物品操作日誌\元寶差異紀錄) */
			ADENA_CHECK_COUNT_DIFFER = Integer.parseInt(set.getProperty("AdenaCheckCountDiffer", "100"));

			// 地圖使用時間已重置
			final String tmp14 = set.getProperty("Reset_Map_Time", "");
			if (!tmp14.equalsIgnoreCase("null")) {
				final String[] temp = tmp14.trim().split(":");
				if (temp.length == 3) {
					Reset_Map_Time = new int[3];
					Reset_Map_Time[0] = Integer.parseInt(temp[0]);
					Reset_Map_Time[1] = Integer.parseInt(temp[1]);
					Reset_Map_Time[2] = Integer.parseInt(temp[2]);

				} else {
					_log.info("[地圖使用] 重置時間有誤, 請重新設置!");
				}
			}

			FREE_FIGHT_SWITCH = Boolean.parseBoolean(set.getProperty("FreeFightSwitch", "false"));

			FREE_FIGHT_ALLMAP = Boolean.parseBoolean(set.getProperty("FreeFightAllMap", "false"));

			FREE_FIGHT_DROP_CHANCE_A = Integer.parseInt(set.getProperty("FreeFightDropChanceA", "0"));

			FREE_FIGHT_DROP_CHANCE_B = Integer.parseInt(set.getProperty("FreeFightDropChanceB", "0"));

			String temp_3 = set.getProperty("FreeFightTimeList");
			if (temp_3 != null) {
				FREE_FIGHT_TIME_LIST = temp_3.split(",");
			}

			FREE_FIGHT_REMAIN_TIME = Integer.parseInt(set.getProperty("FreeFightRemainTime", "1"));

			FREE_FIGHT_MAX_DROP = Integer.parseInt(set.getProperty("FreeFightMaxDrop", "1"));

			String fightTmp = set.getProperty("FreeFightMapChoice", "");
			if (!fightTmp.equalsIgnoreCase("null")) {
				String[] fightTmp2 = fightTmp.split("-");

				if (fightTmp2.length == 2) {
					FREE_FIGHT_MAP_MIN = Integer.parseInt(fightTmp2[0]);
					FREE_FIGHT_MAP_MAX = Integer.parseInt(fightTmp2[1]);
				}
			}

			String temp_4 = set.getProperty("FreeFightMapList");
			if (temp_4 != null) {
				FREE_FIGHT_MAP_LIST = temp_4.split(",");
			}

			final String tmp10 = set.getProperty("QuestSetResetTime", "");
			if (!tmp10.equalsIgnoreCase("null")) {
				final String[] temp = tmp10.split(":");
				if (temp.length == 3) {
					final Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(temp[1]));
					cal.set(Calendar.SECOND, Integer.parseInt(temp[2]));

					QUEST_SET_RESET_TIME = cal;

				} else {
					_log.info("[每日重置任務] 重置時間有誤, 請重新設置!");
				}
			}

			final String tmp11 = set.getProperty("MazuResetTime", "");
			if (!tmp11.equalsIgnoreCase("null")) {
				final String[] temp = tmp11.split(":");
				if (temp.length == 3) {
					final Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(temp[1]));
					cal.set(Calendar.SECOND, Integer.parseInt(temp[2]));

					MAZU_RESET_TIME = cal;

				} else {
					_log.info("[媽祖加持] 重置時間有誤, 請重新設置!");
				}
			}

			// 自創陣營戰系統 相關設定
			RedBlueJoin_itemid = Integer.parseInt(set.getProperty("RedBlueJoin_itemid", "40308"));
			RedBlueJoin_count = Integer.parseInt(set.getProperty("RedBlueJoin_count", "100"));
			RedBluePc_amount = Integer.parseInt(set.getProperty("RedBluePc_amount", "5"));
			RedBlueLv_min = Integer.parseInt(set.getProperty("RedBlueLv_min", "70"));
			RedBlueLv_max = Integer.parseInt(set.getProperty("RedBlueLv_max", "99"));
			RedBlueTime_all = Integer.parseInt(set.getProperty("RedBlueTime_all", "600"));
			RedBlueTime_clear = Integer.parseInt(set.getProperty("RedBlueTime_clear", "1800"));
			RedBlueEffect_time = Integer.parseInt(set.getProperty("RedBlueEffect_time", "10"));
			RedBlueStart_point = Integer.parseInt(set.getProperty("RedBlueStart_point", "5"));
			RedBlueNormal_point = Integer.parseInt(set.getProperty("RedBlueNormal_point", "1"));
			RedBlueLeader_point = Integer.parseInt(set.getProperty("RedBlueLeader_point", "5"));
			RedBlueBonus_itemid = Integer.parseInt(set.getProperty("RedBlueBonus_itemid", "40308"));
			RedBlueBonus_count = Integer.parseInt(set.getProperty("RedBlueBonus_count", "1000"));
			RedBlueReward_times = Integer.parseInt(set.getProperty("RedBlueReward_times", "5"));

			String rb1 = set.getProperty("RedBlueEnd_map", "33080,33392,4");
			if (!rb1.equalsIgnoreCase("null")) {
				String[] rb11 = rb1.split(",");
				int[] rb111 = { Integer.valueOf(rb11[0]), Integer.valueOf(rb11[1]), Integer.valueOf(rb11[2]) };
				RedBlueEnd_map = rb111;
			}
			String rb2 = set.getProperty("RedBlueRed_map1", "33080,33392,4");
			if (!rb2.equalsIgnoreCase("null")) {
				String[] rb22 = rb2.split(",");
				int[] rb222 = { Integer.valueOf(rb22[0]), Integer.valueOf(rb22[1]), Integer.valueOf(rb22[2]) };
				RedBlueRed_map1 = rb222;
			}
			String rb3 = set.getProperty("RedBlueBlue_map1", "33080,33392,4");
			if (!rb3.equalsIgnoreCase("null")) {
				String[] rb33 = rb3.split(",");
				int[] rb333 = { Integer.valueOf(rb33[0]), Integer.valueOf(rb33[1]), Integer.valueOf(rb33[2]) };
				RedBlueBlue_map1 = rb333;
			}
			String rb4 = set.getProperty("RedBlueRed_map2", "33080,33392,4");
			if (!rb4.equalsIgnoreCase("null")) {
				String[] rb44 = rb4.split(",");
				int[] rb444 = { Integer.valueOf(rb44[0]), Integer.valueOf(rb44[1]), Integer.valueOf(rb44[2]) };
				RedBlueRed_map2 = rb444;
			}
			String rb5 = set.getProperty("RedBlueBlue_map2", "33080,33392,4");
			if (!rb5.equalsIgnoreCase("null")) {
				String[] rb55 = rb5.split(",");
				int[] rb555 = { Integer.valueOf(rb55[0]), Integer.valueOf(rb55[1]), Integer.valueOf(rb55[2]) };
				RedBlueBlue_map2 = rb555;
			}

			final String tmp12 = set.getProperty("RedBlueReward_RESET_TIME", "");
			if (!tmp12.equalsIgnoreCase("null")) {
				final String[] temp = tmp12.split(":");
				if (temp.length == 3) {
					final Calendar cal = Calendar.getInstance();
					cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(temp[0]));
					cal.set(Calendar.MINUTE, Integer.parseInt(temp[1]));
					cal.set(Calendar.SECOND, Integer.parseInt(temp[2]));

					RedBlueReward_RESET_TIME = cal;

				} else {
					_log.info("[對戰獎勵] 重置時間有誤, 請重新設置!");
				}
			}

			if (set.getProperty("iceKeyMapList") != null) {
				for (String str : set.getProperty("iceKeyMapList").split(",")) {
					iceKeyMapList.add(Integer.valueOf(Integer.parseInt(str)));
				}
			}

		} catch (Exception e) {
			throw new ConfigErrorException("設置檔案遺失: " + LIANG);
		} finally {
			set.clear();
		}
	}
}
