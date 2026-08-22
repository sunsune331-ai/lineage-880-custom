package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.lineage.list.Announcements;

/**
 * 服務器基礎設置
 *
 * @author dexc
 *
 */
public final class Config {

	/** 是否開啟新排行系統 **/
	public static boolean UserRanking;

	// 官方簽到系統
	/** 是否開啟簽到系統 **/
	public static boolean Attend;
	/** 一般簽到次數 **/
	public static int NormalMaxCount = 1;
	/** 網吧簽到次數 **/
	public static int PcRoomMaxCount = 1;
	/** 一般簽到時間 **/
	public static int NormalMaxTime;
	/** 網吧簽到時間 **/
	public static int PcRoomMaxTime = 3600;
	/** 幾點更新 **/
	public static int ClearClock;
	/** 幾級能簽到 **/
	public static int AttendLevel = 1;
	// 官方簽到系統end

	public static String CraftinfoCode;

	/** 是否用【beanfun】方式登錄伺服器 true=LoginTest登錄器登陸(可建帳號) false=原本 **/
	public static boolean Bean_Fun;
	/** 當Bean_Fun=false時 true=正常登陸(有專用登陸器時) false=登入核心指定帳號 **/
	public static boolean BenJi;

	/** 火神系統類型 1=原本伊薇火神 2=火神製作(DB化) */
	public static int Item_Craft;

	/** 是否開啟周任務 **/
	public static boolean Week_Quest;
	public static int WQ_UPDATE_TYPE;
	public static int WQ_UPDATE_WEEK;
	public static int WQ_UPDATE_TIME;

	/** 伺服器執行登入器驗證 */
	public static boolean LOGINS_TO_AUTOENTICATION=false;
	public static String RSA_KEY_E;
	public static String RSA_KEY_N;

	// /////////////////////////////////////////////
	/** 版本編號 */
	public static final String VER = "880.0.00";

	/** 客戶端對應 */
	public static final String SRCVER = "Lineage8.80";

	// 8.1 TW
	/*
	 * 1b b8 a9 00 90 b5 a9 00 ba 6e cf 77 2d 3e a9 00 c3 86 1b 4f
	 */
	public static final int SVer = 161006201;
	public static final int CVer = 161006201;
	public static final int AVer = 2015090301;
	public static final int NVer = 161006201;

	/** 除錯模式 */
	public static boolean DEBUG = false;

	/** 是否開啟封包顯示 -C */
	public static boolean opcode_C;
	/** 是否開啟封包顯示 -S */
	public static boolean opcode_S;

	/** 伺服器編號 */
	public static int SERVERNO;

	/** 作業系統是UBUNTU */
	public static boolean ISUBUNTU = false;

	/** 伺服器位置 */
	public static String GAME_SERVER_HOST_NAME;

	/** 伺服器端口 */// 服務器監聽端口以"-"減號分隔 允許設置多個(允許設置一個)
	public static String GAME_SERVER_PORT;

	/** 服務器名稱 */
	public static String SERVERNAME;

	/** 廣播伺服器位置 */
	public static String CHAT_SERVER_HOST_NAME;

	/** 廣播伺服器端口 */
	public static int CHAT_SERVER_PORT;

	/** 時區設置 */
	public static String TIME_ZONE;

	/** 伺服器語系 */
	public static int CLIENT_LANGUAGE;

	/** 伺服器語系字串源 */
	public static String CLIENT_LANGUAGE_CODE;

	/** 伺服器語系定位陣列 */
	public static String[] LANGUAGE_CODE_ARRAY = { "UTF8", "EUCKR", "UTF8", "BIG5", "SJIS", "GBK" };

	/** 重新啟動時間設置 */
	public static String[] AUTORESTART = null;

	/** 允許自動註冊 */
	public static boolean AUTO_CREATE_ACCOUNTS;

	/** 允許最大玩家 */
	public static short MAX_ONLINE_USERS = 10;

	/** 人物資料自動保存時間 */
	public static int AUTOSAVE_INTERVAL;

	/** 人物背包自動保存時間 */
	public static int AUTOSAVE_INTERVAL_INVENTORY;

	/** 客戶端接收信息範圍 (-1為畫面內可見) */
	public static int PC_RECOGNIZE_RANGE;

	/** 端口重置時間(單位:分鐘) */
	public static int RESTART_LOGIN;

	/** 是否顯示公告 */
	public static boolean NEWS;

	/** 是否顯示管理視窗 */
	public static boolean GUI;

	/** 1810102501 版本時間偏移 */
	public static int Lohuver;

	/** 伺服器素質選取方式 0:玩家自選 1:骰子隨機點 */
	// public static int POWER = 0;

	private static final String SERVER_CONFIG_FILE = "./config/server.properties";

	private static final String PROTOCOL_181_CONFIG_FILE = "./config/protocol181.properties";

	public static void load() throws ConfigErrorException {
		// TODO 伺服器捆綁
		Properties pack = new Properties();
		try {
			InputStream is = new FileInputStream(new File("./config/pack.properties"));
			pack.load(is);
			is.close();
			LOGINS_TO_AUTOENTICATION = Boolean.parseBoolean(pack.getProperty("Autoentication", "false"));
			RSA_KEY_E = pack.getProperty("RSA_KEY_E", "0");
			RSA_KEY_N = pack.getProperty("RSA_KEY_N", "0");

		} catch (final Exception e) {
			System.err.println("沒有找到登入器加密設置檔案: ./config/pack.properties");

		} finally {
			pack.clear();
		}

		// _log.info("載入服務器基礎設置!");
		final Properties set = new Properties();
		try {
			final InputStream is = new FileInputStream(new File(SERVER_CONFIG_FILE));
			set.load(is);
			is.close();

			// 是否開啟新排行系統
			UserRanking = Boolean.parseBoolean(set.getProperty("UserRanking", "false"));

			 // 是否開啟簽到系統
			Attend = Boolean.parseBoolean(set.getProperty("Attend", "false"));
			// 簽到時間
			NormalMaxTime = Integer.parseInt(set.getProperty("NormalMaxTime", "3600"));
			// 幾點更新
			ClearClock = Integer.parseInt(set.getProperty("ClearClock", "6"));

			// 是否用【beanfun】方式登錄伺服器 true=LoginTest登錄器登陸(可建帳號) false=原本
			Bean_Fun = Boolean.parseBoolean(set.getProperty("Bean_Fun", "false"));
			// 當Bean_Fun=false時 true=正常登陸(有專用登陸器時) false=登入核心指定帳號
			BenJi = Boolean.parseBoolean(set.getProperty("BenJi", "false"));

			// 是否開啟封包顯示 -C
			opcode_C = Boolean.parseBoolean(set.getProperty("opcode_C", "false"));
			// 是否開啟封包顯示 -S
			opcode_S = Boolean.parseBoolean(set.getProperty("opcode_S", "false"));

			// 火神系統類型
			Item_Craft = Integer.parseInt(set.getProperty("Item_Craft", "1"));

			// 是否開啟周任務
			Week_Quest = Boolean.parseBoolean(set.getProperty("Week_Quest", "false"));
			WQ_UPDATE_TYPE = Integer.parseInt(set.getProperty("WeekQuest_UpdateType", "1"));
			WQ_UPDATE_WEEK = Integer.parseInt(set.getProperty("WeekQuest_UpdateWeek", "4"));
			WQ_UPDATE_TIME = Integer.parseInt(set.getProperty("WeekQuest_UpdateTime", "10"));

			GUI = Boolean.parseBoolean(set.getProperty("GUI", "true"));

			// 伺服器編號
			SERVERNO = Integer.parseInt(set.getProperty("ServerNo", "1"));

			// 通用
			GAME_SERVER_HOST_NAME = set.getProperty("GameserverHostname", "*");

			// 服務器監聽端口以"-"減號分隔 允許設置多個(允許設置一個)
			GAME_SERVER_PORT = set.getProperty("GameserverPort", "2000-2001");

			// 語系
			CLIENT_LANGUAGE = Integer.parseInt(set.getProperty("ClientLanguage", "3"));

			CLIENT_LANGUAGE_CODE = LANGUAGE_CODE_ARRAY[CLIENT_LANGUAGE];

			String tmp = set.getProperty("AutoRestart", "");
			if (!tmp.equalsIgnoreCase("null")) {
				AUTORESTART = tmp.split(",");
			}

			TIME_ZONE = set.getProperty("TimeZone", "CST");

			AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(set.getProperty("AutoCreateAccounts", "true"));

			MAX_ONLINE_USERS = Short.parseShort(set.getProperty("MaximumOnlineUsers", "30"));

			AUTOSAVE_INTERVAL = Integer.parseInt(set.getProperty("AutosaveInterval", "1200"), 10);

			AUTOSAVE_INTERVAL /= 60;
			if (AUTOSAVE_INTERVAL <= 0) {
				AUTOSAVE_INTERVAL = 20;
			}

			AUTOSAVE_INTERVAL_INVENTORY = Integer.parseInt(set.getProperty("AutosaveIntervalOfInventory", "300"), 10);

			AUTOSAVE_INTERVAL_INVENTORY /= 60;
			if (AUTOSAVE_INTERVAL_INVENTORY <= 0) {
				AUTOSAVE_INTERVAL_INVENTORY = 5;
			}

			PC_RECOGNIZE_RANGE = Integer.parseInt(set.getProperty("PcRecognizeRange", "13"));

			// SEND_PACKET_BEFORE_TELEPORT = Boolean.parseBoolean(set.getProperty("SendPacketBeforeTeleport", "false"));

			RESTART_LOGIN = Integer.parseInt(set.getProperty("restartlogin", "30"));

			NEWS = Boolean.parseBoolean(set.getProperty("News", "false"));

			// POWER = Integer.parseInt(set.getProperty("power", "0"));

			if (NEWS) {
				Announcements.get().load();
			}

		} catch (final Exception e) {
			throw new ConfigErrorException("設置檔案遺失: " + SERVER_CONFIG_FILE);

		} finally {
			set.clear();
		}
		loadProtocol181();
	}

	private static void loadProtocol181() throws ConfigErrorException {
		final Properties set = new Properties();
		try {
			final InputStream is = new FileInputStream(new File(PROTOCOL_181_CONFIG_FILE));
			set.load(is);
			is.close();
			Lohuver = Integer.parseInt(set.getProperty("Lohuver", "740517168").trim());
			if (Lohuver <= 0) {
				throw new IllegalArgumentException("Lohuver must be positive");
			}
		} catch (final Exception e) {
			throw new ConfigErrorException("設置檔案遺失或錯誤: " + PROTOCOL_181_CONFIG_FILE);
		} finally {
			set.clear();
		}
	}
}
