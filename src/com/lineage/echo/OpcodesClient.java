package com.lineage.echo;

public class OpcodesClient {

	// 1810102501 登入階段協定
	public static final int C_VERSION = 1;

	public static final int C_READ_NEWS = 110;

	public static final int C_SHIFT_SERVER = 155;

	public static final int C_EXTENDED_PROTOBUF = 198;

	public static final int C_LOGIN = 248;

	public static final int C_GM_TELEPORT = 27;

	public static final int C_GOTO_PORTAL = 83;

	protected static final byte _firstPacket[] = {
		-14, -99, 50, 2, -80, 66, -72, 64
	};

	// Ver->TW1901142505
	public static final int C_OPCODE_USEITEM = 50;

	public static final int C_OPCODE_FISHCLICK = 158;//dcl

	public static final int C_OPCODE_SELECTTARGET = 216;//dcl

	public static final int C_OPCODE_CHATGLOBAL = 247;//dcl

	public static final int C_OPCODE_RANK = 157;

	public static final int C_OPCODE_EMBLEMDOWNLOAD = 238;

	public static final int C_OPCODE_WHO = 182;

	public static final int C_OPCODE_FIRE_SMITH_P = 210;//dcl

	public static final int C_OPCODE_TELEPORTLOCK = 35;

	public static final int C_OPCODE_DUNGEONTELEPORT = 73;

	public static final int C_OPCODE_CHAT = 249;

	public static final int C_OPCODE_TRADE = 231;

	public static final int C_OPCODE_CHECKPK = 100;

	public static final int C_OPCODE_ATTR = 180;

	public static final int C_OPCODE_CHATWHISPER = 185;

	public static final int C_OPCODE_PRIVATESHOPLIST = 6;

	public static final int C_OPCODE_BOOKMARKDELETE = 245;

	public static final int C_OPCODE_ADDBUDDY = 74;

	public static final int C_OPCODE_SKILLBUYOK = 42;

	public static final int C_OPCODE_CHANGECHAR = 3;

	public static final int C_OPCODE_SELECTLIST = 103;

	public static final int C_OPCODE_BOARDDELETE = 68;

	public static final int C_OPCODE_EXTCOMMAND = 171;

	public static final int C_OPCODE_NEWCHAR = 144;
	
	public static final int C_OPCODE_LOGOUT = 240;
	
	public static final int C_OPCODE_PLEDGE = 136;

	public static final int C_OPCODE_EXIT_GHOST = 0;

	public static final int C_OPCODE_CREATECLAN = 122;//dcl

	public static final int C_OPCODE_MOVECHAR = 114;

	public static final int C_OPCODE_NPCACTION = 170;

	public static final int C_OPCODE_CHARACTERCONFIG = 233;

	public static final int C_OPCODE_BUDDYLIST = 115;

	public static final int C_OPCODE_LOGINPACKET = 248;//dcl

	public static final int C_OPCODE_BOARDBACK = 163;

	public static final int C_OPCODE_PARTYLIST = 149;

	public static final int C_OPCODE_LOGINTOSERVEROK = 243;

	public static final int C_OPCODE_AMOUNT = 73;

	public static final int C_OPCODE_SKILLBUYITEMOK = 105;

	public static final int C_OPCODE_BOARD = 75;

	public static final int C_OPCODE_PETMENU = 169;

	public static final int C_OPCODE_ENTERPORTAL = 44;

	public static final int C_OPCODE_FIGHT = 206;

	public static final int C_OPCODE_SKILLBUY = 33;

	public static final int C_OPCODE_ARROWATTACK = 9;

	public static final int C_OPCODE_DELETEINVENTORYITEM = 148;

	public static final int C_OPCODE_LEAVECLANE = 250;

	public static final int C_OPCODE_BANPARTY = 141;

	public static final int C_OPCODE_JOINCLAN = 70;

	public static final int C_OPCODE_MATCH_MAKING = -1;//112

	public static final int C_OPCODE_DRAWAL = 205;

	public static final int C_OPCODE_DROPITEM = 176;

	public static final int C_OPCODE_TITLE = 181;

	public static final int C_OPCODE_PLEDGE_WATCH = 95;

	public static final int C_OPCODE_CREATEPARTY = 17;

	public static final int C_OPCODE_TAXRATE = 37;

	public static final int C_OPCODE_DEPOSIT = 204;

	public static final int C_OPCODE_LOGINTOSERVER = 234;

	public static final int C_OPCODE_QUITGAME = 4;

	public static final int C_OPCODE_WAREHOUSELOCK = 134;

	public static final int C_OPCODE_CLIENT_READY = 15;

	public static final int C_OPCODE_SKILLBUYITEM = 162;

	public static final int C_OPCODE_KEEPALIVE = 106;

	public static final int C_OPCODE_BANCLAN = 29;

	public static final int C_OPCODE_TRADEADDITEM = 143;

	public static final int C_OPCODE_BOOKMARK = 167;

	public static final int C_OPCODE_TRADEADDCANCEL = 192;

	public static final int C_OPCODE_TELEPORT = 32;

	public static final int C_OPCODE_CAHTPARTY = 195;

	public static final int C_OPCODE_DELBUDDY = 241;

	public static final int C_OPCODE_ATTACK = 226;

	public static final int C_OPCODE_COMMONCLICK = 110;

	public static final int C_OPCODE_PICKUPITEM = 132;

	public static final int C_OPCODE_BEANFUNLOGINPACKET = 155;

	public static final int C_OPCODE_GIVEITEM = 52;

	public static final int C_OPCODE_BOARDWRITE = 150;

	public static final int C_OPCODE_ATTACKRUNING = 12;

	public static final int C_OPCODE_RESTART = 94;

	public static final int C_OPCODE_RESULT = 174;

	public static final int C_OPCODE_SHOP = 111;

	public static final int C_OPCODE_NPCTALK = 80;

	public static final int C_OPCODE_CHANGEHEADING = 118;

	public static final int C_OPCODE_WAR = 48;

	public static final int C_OPCODE_BOARDREAD = 65;

	public static final int C_OPCODE_CHARRESET = 84;

	public static final int C_OPCODE_USEPETITEM = 97;

	public static final int C_OPCODE_DOOR = 8;

	public static final int C_OPCODE_FIX_WEAPON_LIST = 254;

	public static final int C_OPCODE_LEAVEPARTY = 253;

	public static final int C_OPCODE_SENDLOCATION = 14;

	public static final int C_OPCODE_USESKILL = 86;

	public static final int C_OPCODE_MAIL = 57;

	public static final int C_OPCODE_DELETECHAR = 72;

	public static final int C_OPCODE_EXTENDED_PROTOBUF = 198;

	public static final int C_OPCODE_TRADEADDOK = 112;

	public static final int C_OPCODE_SHIP = 101;

	public static final int C_OPCODE_PROPOSE = 38;

	public static final int C_OPCODE_EXCLUDE = 23;

	public static final int C_OPCODE_EMBLEMUPLOAD = 78;

	public static final int C_OPCODE_CLIENTVERSION = 1;

	public static final int C_OPCODE_CALL = 88;

	// 8.8C->未用到
	// public static final int C_SAVE = 6;
	// public static final int C_TELEPORT = 7;
	// public static final int C_WANTED = 10;
	// public static final int C_MERCENARYEMPLOY = 13;
	// public static final int C_KICK = 21;
	// public static final int C_ADDR = 31;
	// public static final int C_MATCH_MAKING = 40;
	// public static final int C_START_CASTING = 43;
	// public static final int C_ALT_ATTACK = 49;
	// public static final int C_SMS = 59;
	// public static final int C_BOOK = 67;
	// public static final int C_INVITE_PARTY = 71;
	// public static final int C_LOGIN_RESULT = 82;
	// public static final int C_BUILDER_CONTROL = 84;
	// public static final int C_CHANGE_CASTLE_SECURITY = 93;
	// public static final int C_LOGIN_TEST = 103;
	// public static final int C_MERCENARYARRANGE = 107;
	// public static final int C_WISH = 110;
	// public static final int C_NEW_ACCOUNT = 114;
	// public static final int C_QUERY_CASTLE_SECURITY = 123;
	// public static final int C_READ_NOTICE = 128;
	// public static final int C_ENTER_SHIP = 139;
	// public static final int C_INCLUDE = 140;
	// public static final int C_MERCENARYSELECT = 155;
	// public static final int C_MERCENARYNAME = 157;
	// public static final int C_SELECT_TIME = 165;
	// public static final int C_SELECTABLE_TIME = 174;
	// public static final int C_SERVER_SELECT = 200;
	// public static final int C_CONTROL_WEATHER = 220;
	// public static final int C_ARCHERARRANGE = 223;
	// public static final int C_BAN = 233;
	// public static final int C_CHANGE_ACCOUNTINFO = 240;
	// public static final int C_CHANGE_PASSWORD = 243;
	// public static final int C_REGISTER_QUIZ = 244;
	// public static final int C_REQUEST_ROLL = 246;
	// public static final int C_SUMMON = 249;
	// public static final int C_SILENCE = 250;
	// public static final int C_MONITOR_CONTROL = 251;
	// public static final int C_EXTENDED = 252;
	// public static final int C_EXTENDED_HYBRID = 253;
}
