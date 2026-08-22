package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.data.ItemClass;
import com.lineage.data.item_armor.set.ArmorSet;
import com.lineage.server.IdFactory;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Armor;
import com.lineage.server.templates.L1EtcItem;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1Weapon;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.Random;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

public class ItemTable {
	private static final Log _log = LogFactory.getLog(ItemTable.class);

	// 防具類型核心分類
	private static final Map<String, Integer> _armorTypes = new HashMap<String, Integer>();

	// 武器類型核心分類
	private static final Map<String, Integer> _weaponTypes = new HashMap<String, Integer>();

	// 武器類型觸發事件
	private static final Map<String, Integer> _weaponId = new HashMap<String, Integer>();

	// 材質類型核心分類
	private static final Map<String, Integer> _materialTypes = new HashMap<String, Integer>();

	// 道具類型核心分類
	private static final Map<String, Integer> _etcItemTypes = new HashMap<String, Integer>();

	// 道具類型觸發事件
	private static final Map<String, Integer> _useTypes = new HashMap<String, Integer>();

	private static ItemTable _instance;

	private L1Item _allTemplates[];

	private static Map<Integer, L1EtcItem> _etcitems;

	private static Map<Integer, L1Armor> _armors;

	private static Map<Integer, L1Weapon> _weapons;
	private static int _cdescid = 20000;

	public static final List<Integer> itembuff = new ArrayList<Integer>();

	public static final List<String> itembuffs = new ArrayList<String>();

	public static synchronized int cdescid() {
		return _cdescid++;
	}

	static {
		// 物品類型
		_etcItemTypes.put("arrow", new Integer(0));// 箭
		_etcItemTypes.put("wand", new Integer(1));// 魔杖
		_etcItemTypes.put("light", new Integer(2));// 照明
		_etcItemTypes.put("gem", new Integer(3));// 寶石
		_etcItemTypes.put("totem", new Integer(4));// 圖騰
		_etcItemTypes.put("firecracker", new Integer(5));// 煙火
		_etcItemTypes.put("potion", new Integer(6));// 藥水
		_etcItemTypes.put("food", new Integer(7));// 食物
		_etcItemTypes.put("scroll", new Integer(8));// 卷軸
		_etcItemTypes.put("questitem", new Integer(9));// 任務物品
		_etcItemTypes.put("spellbook", new Integer(10));// 魔法書
		_etcItemTypes.put("petitem", new Integer(11));// 寵物物品
		_etcItemTypes.put("other", new Integer(12));// 其他
		_etcItemTypes.put("material", new Integer(13));// 材料
		_etcItemTypes.put("event", new Integer(14));// 活動物品
		_etcItemTypes.put("sting", new Integer(15));// 飛刀
		_etcItemTypes.put("treasure_box", new Integer(16));// 寶盒

		// 物品使用封包類型(編號由S_AddItem測出)
		_useTypes.put("petitem", new Integer(-12)); // 寵物用具
		_useTypes.put("other", new Integer(-11)); // 對讀取方法調用無法分類的物品
		_useTypes.put("power", new Integer(-10)); // 加速藥水
		_useTypes.put("book", new Integer(-9)); // 技術書
		_useTypes.put("makecooking", new Integer(-8));// 料理書
		_useTypes.put("hpr", new Integer(-7));// 增HP道具
		_useTypes.put("mpr", new Integer(-6));// 增MP道具
		_useTypes.put("ticket", new Integer(-5)); // 食人妖精競賽票/死亡競賽票/彩票
		_useTypes.put("petcollar", new Integer(-4)); // 項圈
		_useTypes.put("sting", new Integer(-3)); // 飛刀
		_useTypes.put("arrow", new Integer(-2)); // 箭
		_useTypes.put("none", new Integer(-1)); // 無法使用(材料等)
		_useTypes.put("normal", new Integer(0));// 一般物品
		_useTypes.put("weapon", new Integer(1));// 武器
		_useTypes.put("armor", new Integer(2));// 盔甲
		_useTypes.put("spell_1", new Integer(3)); // 創造怪物魔杖(無須選取目標 - 無數量:沒有任何事情發生)
		_useTypes.put("4", new Integer(4)); // 希望魔杖 XXX
		_useTypes.put("spell_long", new Integer(5)); // 魔杖類型(須選取目標/座標)
		_useTypes.put("ntele", new Integer(6));// 瞬間移動卷軸
		_useTypes.put("identify", new Integer(7));// 鑒定卷軸
		_useTypes.put("res", new Integer(8));// 復活卷軸
		_useTypes.put("home", new Integer(9)); // 傳送回家的卷軸
		_useTypes.put("light", new Integer(10)); // 照明道具
		_useTypes.put("letter", new Integer(12));// 信紙
		_useTypes.put("letter_card", new Integer(13)); // 信紙(寄出)
		_useTypes.put("choice", new Integer(14));// 請選擇一個物品(道具欄位)
		_useTypes.put("instrument", new Integer(15));// 哨子
		_useTypes.put("sosc", new Integer(16));// 變形卷軸
		_useTypes.put("spell_short", new Integer(17)); // 選取目標 (近距離)
		_useTypes.put("T", new Integer(18));// T恤
		_useTypes.put("cloak", new Integer(19));// 斗篷
		_useTypes.put("glove", new Integer(20)); // 手套
		_useTypes.put("boots", new Integer(21));// 靴
		_useTypes.put("helm", new Integer(22));// 頭盔
		_useTypes.put("ring", new Integer(23));// 戒指
		_useTypes.put("amulet", new Integer(24));// 項鏈
		_useTypes.put("shield", new Integer(25));// 盾牌
		_useTypes.put("guarder", new Integer(25));// 臂甲
		_useTypes.put("dai", new Integer(26));// 對武器施法的卷軸
		_useTypes.put("zel", new Integer(27));// 對盔甲施法的卷軸
		_useTypes.put("blank", new Integer(28));// 空的魔法卷軸
		_useTypes.put("btele", new Integer(29));// 瞬間移動卷軸(祝福)
		_useTypes.put("spell_buff", new Integer(30)); // 魔法卷軸選取目標 (遠距離 無XY座標傳回)
		_useTypes.put("ccard", new Integer(31));// 聖誕卡片
		_useTypes.put("ccard_w", new Integer(32));// 聖誕卡片(寄出)
		_useTypes.put("vcard", new Integer(33));// 情人節卡片
		_useTypes.put("vcard_w", new Integer(34));// 情人節卡片(寄出)
		_useTypes.put("wcard", new Integer(35));// 白色情人節卡片
		_useTypes.put("wcard_w", new Integer(36));// 白色情人節卡片(寄出)
		_useTypes.put("belt", new Integer(37));// 腰帶
		_useTypes.put("food", new Integer(38)); // 食物
		_useTypes.put("spell_long2", new Integer(39)); // 選取目標 (遠距離)
		_useTypes.put("earring", new Integer(40)); // 耳環
		_useTypes.put("fishing_rod", new Integer(42));// 釣魚桿
		_useTypes.put("enc", new Integer(46)); // 飾品強化卷軸
		_useTypes.put("pants", new Integer(70)); // 脛甲
		_useTypes.put("choice_doll", new Integer(55));// 請選擇魔法娃娃
	    _useTypes.put("lottery", new Integer(62));//抽抽樂
	    _useTypes.put("lottery2", new Integer(65));//抽抽樂

		_useTypes.put("talisman", new Integer(43)); // 符石/上左(鎖定) 
		_useTypes.put("runeword_left", new Integer(44)); // 符石/下左
		_useTypes.put("runeword_right", new Integer(45)); // 符石/下右
		_useTypes.put("runeword_middle", new Integer(48)); // 符石/下中
		_useTypes.put("talisman2", new Integer(49)); // 符石/上右
		
		// _useTypes.put("talisman3", new Integer(51)); // 蒂蜜特的符文
		// _useTypes.put("vip", new Integer(52)); // vip
	    _useTypes.put("shoulder", new Integer(51)); // 肩甲
	    _useTypes.put("badge", new Integer(52)); // 徽章

		// 防具類型
		_armorTypes.put("none", new Integer(0));
		_armorTypes.put("helm", new Integer(1));// 頭盔
		_armorTypes.put("armor", new Integer(2));// 盔甲
		_armorTypes.put("T", new Integer(3));// 內衣
		_armorTypes.put("cloak", new Integer(4));// 斗篷
		_armorTypes.put("glove", new Integer(5));// 手套
		_armorTypes.put("boots", new Integer(6));// 長靴
		_armorTypes.put("shield", new Integer(7));// 盾牌
		_armorTypes.put("amulet", new Integer(8));// 項鏈
		_armorTypes.put("ring", new Integer(9));// 戒指
		_armorTypes.put("belt", new Integer(10));// 腰帶
		_armorTypes.put("ring2", new Integer(11));// 戒指2
		_armorTypes.put("earring", new Integer(12));// 耳環
		_armorTypes.put("guarder", new Integer(13));// 臂甲
		_armorTypes.put("talisman", new Integer(14));// 符石/上左(鎖定) 
		_armorTypes.put("runeword_left", new Integer(15));// 符石/下左
		_armorTypes.put("runeword_right", new Integer(17));// 符石/下右
		_armorTypes.put("pants", new Integer(16));// 脛甲
		_armorTypes.put("runeword_middle", new Integer(18));// 六芒星護身符 符石/下中
		_armorTypes.put("talisman2", new Integer(23));// 蒂蜜特的紋樣系列 符石/上右

		// _armorTypes.put("talisman3", new Integer(20));// 蒂蜜特的符文
		// _armorTypes.put("vip", new Integer(21));// vip
		_armorTypes.put("shoulder", new Integer(29)); // 肩甲
		_armorTypes.put("badge", new Integer(30)); // 徽章

		// 武器類型
		_weaponTypes.put("none", new Integer(0));// 空手
		_weaponTypes.put("sword", new Integer(1));// 劍(單手)
		_weaponTypes.put("dagger", new Integer(2));// 匕首(單手)
		_weaponTypes.put("tohandsword", new Integer(3));// 雙手劍(雙手)
		_weaponTypes.put("bow", new Integer(4));// 弓(雙手)
		_weaponTypes.put("spear", new Integer(5));// 矛(雙手)
		_weaponTypes.put("blunt", new Integer(6));// 斧(單手)
		_weaponTypes.put("staff", new Integer(7));// 魔杖(單手)
		_weaponTypes.put("throwingknife", new Integer(8));// 飛刀
		_weaponTypes.put("arrow", new Integer(9));// 箭
		_weaponTypes.put("gauntlet", new Integer(10));// 鐵手甲
		_weaponTypes.put("claw", new Integer(11));// 鋼爪(雙手)
		_weaponTypes.put("edoryu", new Integer(12));// 雙刀(雙手)
		_weaponTypes.put("singlebow", new Integer(13));// 弓(單手)
		_weaponTypes.put("singlespear", new Integer(14));// 矛(單手)
		_weaponTypes.put("tohandblunt", new Integer(15));// 雙手斧(雙手)
		_weaponTypes.put("tohandstaff", new Integer(16));// 魔杖(雙手)
		_weaponTypes.put("kiringku", new Integer(17));// 奇古獸(單手)
		_weaponTypes.put("chainsword", new Integer(18));// 鎖鏈劍(單手)

		_weaponId.put("sword", new Integer(4));// 劍
		_weaponId.put("dagger", new Integer(46));// 匕首
		_weaponId.put("tohandsword", new Integer(50));// 雙手劍
		_weaponId.put("bow", new Integer(20));// 弓
		_weaponId.put("blunt", new Integer(11));// 斧(單手)
		_weaponId.put("spear", new Integer(24));// 矛(雙手)
		_weaponId.put("staff", new Integer(40));// 魔杖
		_weaponId.put("throwingknife", new Integer(2922));// 飛刀
		_weaponId.put("arrow", new Integer(66));// 箭
		_weaponId.put("gauntlet", new Integer(62));// 鐵手甲
		_weaponId.put("claw", new Integer(58));// 鋼爪
		_weaponId.put("edoryu", new Integer(54));// 雙刀
		_weaponId.put("singlebow", new Integer(20));// 弓(單手)
		_weaponId.put("singlespear", new Integer(24));// 矛(單手)
		_weaponId.put("tohandblunt", new Integer(11));// 雙手斧
		_weaponId.put("tohandstaff", new Integer(40));// 魔杖(雙手)
		_weaponId.put("kiringku", new Integer(58));// 奇古獸
		_weaponId.put("chainsword", new Integer(24));// 鎖鏈劍

		// 材質
		_materialTypes.put("none", new Integer(0));// 無
		_materialTypes.put("liquid", new Integer(1));// 憶體
		_materialTypes.put("web", new Integer(2));// 蠟
		_materialTypes.put("vegetation", new Integer(3));// 植物
		_materialTypes.put("animalmatter", new Integer(4));// 動物
		_materialTypes.put("paper", new Integer(5));// 紙
		_materialTypes.put("cloth", new Integer(6));// 布
		_materialTypes.put("leather", new Integer(7));// 皮革
		_materialTypes.put("wood", new Integer(8));// 木
		_materialTypes.put("bone", new Integer(9));// 骨頭
		_materialTypes.put("dragonscale", new Integer(10));// 龍鱗
		_materialTypes.put("iron", new Integer(11));// 鐵
		_materialTypes.put("steel", new Integer(12));// 鋼
		_materialTypes.put("copper", new Integer(13));// 銅
		_materialTypes.put("silver", new Integer(14));// 銀
		_materialTypes.put("gold", new Integer(15));// 黃金
		_materialTypes.put("platinum", new Integer(16));// 白金
		_materialTypes.put("mithril", new Integer(17));// 米索莉
		_materialTypes.put("blackmithril", new Integer(18));// 黑色米索莉
		_materialTypes.put("glass", new Integer(19));// 玻璃
		_materialTypes.put("gemstone", new Integer(20));// 寶石
		_materialTypes.put("mineral", new Integer(21));// 礦物
		_materialTypes.put("oriharukon", new Integer(22));// 奧裡哈魯根
	}

	public static ItemTable get() {
		if (_instance == null) {
			_instance = new ItemTable();
		}
		return _instance;
	}

	public void load() {
		final PerformanceTimer timer = new PerformanceTimer();
		_etcitems = this.allEtcItem();
		_weapons = this.allWeapon();
		_armors = this.allArmor();
		this.buildFastLookupTable();
		_log.info("載入道具,武器,防具資料: " + _etcitems.size() + "+" + _weapons.size() + "+" + _armors.size() + "="
				+ +(_etcitems.size() + _weapons.size() + _armors.size()) + "(" + timer.get() + "ms)");
	}

	public void loadarmors() {
		PerformanceTimer timer = new PerformanceTimer();
		_armors = allArmor();
		this.buildFastLookupTable();
		_log.info("載入防具資料: " + _armors.size() + "=" + "(" + timer.get() + "ms)");
	}

	public void loadweapons() {
		PerformanceTimer timer = new PerformanceTimer();
		_weapons = allWeapon();
		this.buildFastLookupTable();
		_log.info("載入武器資料: " + _weapons.size() + "=" + "(" + timer.get() + "ms)");
	}

	public void loaditems() {
		PerformanceTimer timer = new PerformanceTimer();
		_etcitems = allEtcItem();
		this.buildFastLookupTable();
		_log.info("載入道具資料: " + _etcitems.size() + "=" + "(" + timer.get() + "ms)");
	}

	private Map<Integer, L1EtcItem> allEtcItem() {
		Map<Integer, L1EtcItem> result = new HashMap<Integer, L1EtcItem>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1EtcItem item = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `etcitem`");
			rs = pstm.executeQuery();
			while (rs.next()) {
				item = new L1EtcItem();
				int itemid = rs.getInt("item_id");
				item.setItemId(itemid);
				item.setName(rs.getString("name"));
				final String classname = rs.getString("classname");
				if (classname.startsWith("ItemBuff")) {
					itembuff.add(Integer.valueOf(itemid));
					itembuffs.add(classname);
				}
				item.setClassname(classname);
				item.setNameId(rs.getString("name_id"));
				item.setType(((Integer) _etcItemTypes.get(rs.getString("item_type"))).intValue());
				item.setUseType(((Integer) _useTypes.get(rs.getString("use_type"))).intValue());
				item.setType2(0);
				item.setMaterial(((Integer) _materialTypes.get(rs.getString("material"))).intValue());
				item.setWeight(rs.getInt("weight"));
				item.setGfxId(rs.getInt("invgfx"));
				item.setGroundGfxId(rs.getInt("grdgfx"));

				int itemDescId = rs.getInt("itemdesc_id");
				itemDescId = itemDescId <= 0 ? cdescid() : itemDescId;
				item.setItemDescId(itemDescId);

				item.setMinLevel(rs.getInt("min_lvl"));
				item.setMaxLevel(rs.getInt("max_lvl"));
				item.setBless(rs.getInt("bless"));
				item.setTradable(rs.getInt("trade") == 0);
				item.setCantDelete(rs.getInt("cant_delete") == 1);
				item.setDmgSmall(rs.getInt("dmg_small"));
				item.setDmgLarge(rs.getInt("dmg_large"));
				item.set_stackable(rs.getInt("stackable") == 1);
				item.setMaxChargeCount(rs.getInt("max_charge_count"));
				item.setMaxUseTime(rs.getInt("max_use_time"));
				item.set_delayid(rs.getInt("delay_id"));
				item.set_delaytime(rs.getInt("delay_time"));
				item.set_delayEffect(rs.getInt("delay_effect"));
				item.setFoodVolume(rs.getInt("food_volume"));
				item.setToBeSavedAtOnce(rs.getBoolean("save_at_once"));
				// 職業使用判斷欄位 (1王族.2騎士.4妖精.8法師.16黑妖.32龍騎.64幻術.128戰士.255共用)
				final int use_career = rs.getInt("use_career");
				item.setUseRoyal((use_career & 1) == 1 ? true : false);
				item.setUseKnight((use_career & 2) == 2 ? true : false);
				item.setUseElf((use_career & 4) == 4 ? true : false);
				item.setUseMage((use_career & 8) == 8 ? true : false);
				item.setUseDarkelf((use_career & 16) == 16 ? true : false);
				item.setUseDragonknight((use_career & 32) == 32 ? true : false);
				item.setUseIllusionist((use_career & 64) == 64 ? true : false);
				item.setUseWarrior((use_career & 128) == 128 ? true : false);
				// 陣營使用判斷欄位 (1-魏.2-蜀.4-吳.7-共用)
				item.setCampSet(rs.getInt("use_camp"));
				// 此项不为O时强化道具必须对应编号才能使用
				item.setitemxf(rs.getInt("item_xf"));
	            // 最低使用需求 (轉生次數) by terry0412
				item.setMeteLevel(rs.getInt("MeteLevel"));
				
				// 最高使用需求 (轉生次數) by terry0412
				item.setMeteLevelMAX(rs.getInt("MeteLevelMAX"));

				item.setArrowAttrDmg(rs.getInt("arrowAttrDmg")); // 箭的武器屬性傷害
				item.setArrowBowDmg(rs.getInt("arrowBowDmg")); // 箭的遠距離傷害
				item.setArrowBowHit(rs.getInt("arrowBowHit")); // 箭的遠距離命中

				ItemClass.get().addList(itemid, classname, 0);
				result.put(new Integer(item.getItemId()), item);
			}
		} catch (NullPointerException e) {
			_log.error("加載失敗: " + item.getItemId(), e);
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	private Map<Integer, L1Weapon> allWeapon() {
		Map<Integer, L1Weapon> result = new HashMap<Integer, L1Weapon>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Weapon weapon = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `weapon`");
			rs = pstm.executeQuery();
			while (rs.next()) {
				weapon = new L1Weapon();
				int itemid = rs.getInt("item_id");
				weapon.setItemId(itemid);
				weapon.setName(rs.getString("name"));
				String classname = rs.getString("classname");
				weapon.setClassname(classname);
				weapon.setNameId(rs.getString("name_id"));
				weapon.setType(((Integer) _weaponTypes.get(rs.getString("type"))).intValue());
				weapon.setType1(((Integer) _weaponId.get(rs.getString("type"))).intValue());
				weapon.setType2(1);
				weapon.setUseType(1);
				weapon.setMaterial(((Integer) _materialTypes.get(rs.getString("material"))).intValue());
				weapon.setWeight(rs.getInt("weight"));
				weapon.setGfxId(rs.getInt("invgfx"));
				weapon.setGroundGfxId(rs.getInt("grdgfx"));

				int itemDescId = rs.getInt("itemdesc_id");
				itemDescId = itemDescId <= 0 ? cdescid() : itemDescId;
				weapon.setItemDescId(itemDescId);

				weapon.setDmgSmall(rs.getInt("dmg_small"));
				weapon.setDmgLarge(rs.getInt("dmg_large"));
				weapon.setRange(rs.getInt("range"));
				weapon.set_safeenchant(rs.getInt("safenchant"));
				weapon.setUseRoyal(rs.getInt("use_royal") != 0);
				weapon.setUseKnight(rs.getInt("use_knight") != 0);
				weapon.setUseElf(rs.getInt("use_elf") != 0);
				weapon.setUseMage(rs.getInt("use_mage") != 0);
				weapon.setUseDarkelf(rs.getInt("use_darkelf") != 0);
				weapon.setUseDragonknight(rs.getInt("use_dragonknight") != 0);
				weapon.setUseIllusionist(rs.getInt("use_illusionist") != 0);
				weapon.setUseWarrior(rs.getInt("use_warrior") == 0 ? false : true);
				weapon.setHitModifier(rs.getInt("hitmodifier"));
				weapon.setDmgModifier(rs.getInt("dmgmodifier"));
				weapon.set_addstr(rs.getByte("add_str"));
				weapon.set_adddex(rs.getByte("add_dex"));
				weapon.set_addcon(rs.getByte("add_con"));
				weapon.set_addint(rs.getByte("add_int"));
				weapon.set_addwis(rs.getByte("add_wis"));
				weapon.set_addcha(rs.getByte("add_cha"));
				weapon.set_addhp(rs.getInt("add_hp"));
				weapon.set_addmp(rs.getInt("add_mp"));
				weapon.set_addhpr(rs.getInt("add_hpr"));
				weapon.set_addmpr(rs.getInt("add_mpr"));
				weapon.set_addsp(rs.getInt("add_sp"));
				weapon.set_mdef(rs.getInt("m_def"));
				weapon.setDoubleDmgChance(rs.getInt("double_dmg_chance"));
				weapon.setMagicDmgModifier(rs.getInt("magicdmgmodifier"));
				weapon.set_canbedmg(rs.getInt("canbedmg"));
				weapon.setMinLevel(rs.getInt("min_lvl"));
				weapon.setMaxLevel(rs.getInt("max_lvl"));
				weapon.setBless(rs.getInt("bless"));
				weapon.setTradable(rs.getInt("trade") == 0);
				weapon.setCantDelete(rs.getInt("cant_delete") == 1);
				weapon.setHasteItem(rs.getInt("haste_item") != 0);
				weapon.setMaxUseTime(rs.getInt("max_use_time"));
				weapon.setExpPoint(rs.getInt("exp_point"));
				// 陣營使用判斷欄位 (1-魏.2-蜀.4-吳.7-共用)
				weapon.setCampSet(rs.getInt("use_camp"));
				// 此项不为O时强化道具必须对应编号才能使用
				weapon.setitemxf(rs.getInt("item_xf"));
				// 最低使用需求 (轉生次數) by terry0412
				weapon.setMeteLevel(rs.getInt("MeteLevel"));

				// 最高使用需求 (轉生次數) by terry0412
				weapon.setMeteLevelMAX(rs.getInt("MeteLevelMAX"));
				weapon.set_stunlvl(rs.getInt("stunlvl"));
				// weapon.set_stunPVP(rs.getInt("stunPVP"));
				weapon.setPvpDmg(rs.getInt("pvp_dmg"));// 增加PVP傷害
				weapon.setPvpDmg_R(rs.getInt("pvp_dmg_reduction"));// 減免PVP傷害

				weapon.setRegistTechnology(rs.getInt("技術耐性"));// 技術耐性
				weapon.setRegistElf(rs.getInt("精靈耐性"));// 精靈耐性
				weapon.setRegistDragon(rs.getInt("龍屬耐性"));// 龍屬耐性
				weapon.setRegistHorror(rs.getInt("恐怖耐性"));// 恐怖耐性
				weapon.setRegistAll(rs.getInt("全部耐性"));// 全部四大耐性
				weapon.setHitTechnology(rs.getInt("技術命中"));// 技術命中
				weapon.setHitElf(rs.getInt("精靈命中"));// 精靈命中
				weapon.setHitDragon(rs.getInt("龍屬命中"));// 龍屬命中
				weapon.setHitHorror(rs.getInt("恐怖命中"));// 恐怖命中
				weapon.setHitAll(rs.getInt("全部命中"));// 全部四大命中
				weapon.setmofabaoji(rs.getInt("魔法暴擊"));// 無視減免
				weapon.set_penetrate(rs.getInt("is_penetrate"));// 貫通效果
				weapon.setAntiDamageReduction(rs.getInt("antiDamageReduction"));// 無視減免
				weapon.setEinhasadConsumeReduce(rs.getInt("einConsumeReduce"));// 殷海薩祝福消耗減少

				ItemClass.get().addList(itemid, classname, 1);
				result.put(new Integer(weapon.getItemId()), weapon);
			}
		} catch (NullPointerException e) {
			_log.error("加載失敗: " + weapon.getItemId(), e);
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return result;
	}

	public static void init() {
		_instance = new ItemTable();
	}

	private Map<Integer, L1Armor> allArmor() {
		Map<Integer, L1Armor> result = new HashMap<Integer, L1Armor>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Armor armor = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `armor`");
			rs = pstm.executeQuery();
			while (rs.next()) {
				armor = new L1Armor();
				int itemid = rs.getInt("item_id");
				armor.setItemId(itemid);
				armor.setName(rs.getString("name"));
				String classname = rs.getString("classname");
				armor.setClassname(classname);
				armor.setNameId(rs.getString("name_id"));
				armor.setType(((Integer) _armorTypes.get(rs.getString("type"))).intValue());
				armor.setType2(2);
				armor.setUseType(((Integer) _useTypes.get(rs.getString("type"))).intValue());
				armor.setMaterial(((Integer) _materialTypes.get(rs.getString("material"))).intValue());
				armor.setWeight(rs.getInt("weight"));
				armor.setGfxId(rs.getInt("invgfx"));
				armor.setGroundGfxId(rs.getInt("grdgfx"));
				int itemDescId = rs.getInt("itemdesc_id");
				itemDescId = itemDescId <= 0 ? cdescid() : itemDescId;
				armor.setItemDescId(itemDescId);
				armor.set_ac(rs.getInt("ac"));
				armor.set_safeenchant(rs.getInt("safenchant"));
				armor.setUseRoyal(rs.getInt("use_royal") != 0);
				armor.setUseKnight(rs.getInt("use_knight") != 0);
				armor.setUseElf(rs.getInt("use_elf") != 0);
				armor.setUseMage(rs.getInt("use_mage") != 0);
				armor.setUseDarkelf(rs.getInt("use_darkelf") != 0);
				armor.setUseDragonknight(rs.getInt("use_dragonknight") != 0);
				armor.setUseIllusionist(rs.getInt("use_illusionist") != 0);
				armor.setUseWarrior(rs.getInt("use_warrior") == 0 ? false : true);
				armor.set_addstr(rs.getByte("add_str"));
				armor.set_addcon(rs.getByte("add_con"));
				armor.set_adddex(rs.getByte("add_dex"));
				armor.set_addint(rs.getByte("add_int"));
				armor.set_addwis(rs.getByte("add_wis"));
				armor.set_addcha(rs.getByte("add_cha"));
				armor.set_addhp(rs.getInt("add_hp"));
				armor.set_addmp(rs.getInt("add_mp"));
				armor.set_addhpr(rs.getInt("add_hpr"));
				armor.set_addmpr(rs.getInt("add_mpr"));
				armor.set_addsp(rs.getInt("add_sp"));
				armor.setMinLevel(rs.getInt("min_lvl"));
				armor.setMaxLevel(rs.getInt("max_lvl"));
				armor.set_mdef(rs.getInt("m_def"));
				armor.setDamageReduction(rs.getInt("damage_reduction"));
				armor.setWeightReduction(rs.getInt("weight_reduction"));
				armor.setHitModifierByArmor(rs.getInt("hit_modifier"));
				armor.setDmgModifierByArmor(rs.getInt("dmg_modifier"));
				armor.setBowHitModifierByArmor(rs.getInt("bow_hit_modifier"));
				armor.setBowDmgModifierByArmor(rs.getInt("bow_dmg_modifier"));
				armor.setHasteItem(rs.getInt("haste_item") != 0);
				armor.setBless(rs.getInt("bless"));
				armor.setTradable(rs.getInt("trade") == 0);
				armor.setCantDelete(rs.getInt("cant_delete") == 1);
				armor.set_defense_earth(rs.getInt("defense_earth"));
				armor.set_defense_water(rs.getInt("defense_water"));
				armor.set_defense_wind(rs.getInt("defense_wind"));
				armor.set_defense_fire(rs.getInt("defense_fire"));
				// armor.set_regist_stun(rs.getInt("regist_stun"));
				// armor.set_regist_stone(rs.getInt("regist_stone"));
				// armor.set_regist_sleep(rs.getInt("regist_sleep"));
				// armor.set_regist_freeze(rs.getInt("regist_freeze"));
				// armor.set_regist_sustain(rs.getInt("regist_sustain"));
				// armor.set_regist_blind(rs.getInt("regist_blind"));
				armor.setMaxUseTime(rs.getInt("max_use_time"));
				armor.set_greater(rs.getInt("greater"));
				armor.setExpPoint(rs.getInt("exp_point"));
				// 陣營使用判斷欄位 (1-魏.2-蜀.4-吳.7-共用)
				armor.setCampSet(rs.getInt("use_camp"));
				// 此项不为O时强化道具必须对应编号才能使用
				armor.setitemxf(rs.getInt("item_xf"));
               // 最低使用需求 (轉生次數) by terry0412
				armor.setMeteLevel(rs.getInt("MeteLevel"));

				// 最高使用需求 (轉生次數) by terry0412
				armor.setMeteLevelMAX(rs.getInt("MeteLevelMAX"));
				armor.setMagicHitModifierByArmor(rs.getInt("magic_hit_modifier"));
				armor.set_up_hp_potion(rs.getInt("up_hp_potion"));
				armor.set_uhp_number(rs.getInt("uhp_number"));
				//armor.set_stunPVP2(rs.getInt("stunPVP"));
				armor.setActivity(rs.getBoolean("is_activity"));  //src013
				armor.set_kitType(rs.getInt("kit_type"));
				armor.setSuperRune(rs.getBoolean("is_superrune"));
				armor.setPvpDmg(rs.getInt("pvp_dmg"));// 增加PVP傷害
				armor.setPvpDmg_R(rs.getInt("pvp_dmg_reduction"));// 減免PVP傷害

				armor.setRegistTechnology(rs.getInt("技術耐性"));// 技術耐性
				armor.setRegistElf(rs.getInt("精靈耐性"));// 精靈耐性
				armor.setRegistDragon(rs.getInt("龍屬耐性"));// 龍屬耐性
				armor.setRegistHorror(rs.getInt("恐怖耐性"));// 恐怖耐性
				armor.setRegistAll(rs.getInt("全部耐性"));// 全部四大耐性
				armor.setHitTechnology(rs.getInt("技術命中"));// 技術命中
				armor.setHitElf(rs.getInt("精靈命中"));// 精靈命中
				armor.setHitDragon(rs.getInt("龍屬命中"));// 龍屬命中
				armor.setHitHorror(rs.getInt("恐怖命中"));// 恐怖命中
				armor.setHitAll(rs.getInt("全部命中"));// 全部四大命中
				armor.setmofabaoji(rs.getInt("魔法暴擊"));// 無視減免
				armor.setAntiDamageReduction(rs.getInt("antiDamageReduction"));// 無視減免
				armor.setEinhasadConsumeReduce(rs.getInt("einConsumeReduce"));// 殷海薩祝福消耗減少

				ItemClass.get().addList(itemid, classname, 2);
				result.put(new Integer(armor.getItemId()), armor);
			}
		} catch (NullPointerException e) {
			_log.error("加載失敗: " + armor.getItemId(), e);
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return result;
	}

	private void buildFastLookupTable() {
		int highestId = 0;

		final Collection<L1EtcItem> items = _etcitems.values();
		for (final L1EtcItem item : items) {
			if (item.getItemId() > highestId) {
				highestId = item.getItemId();
			}
		}

		final Collection<L1Weapon> weapons = _weapons.values();
		for (final L1Weapon weapon : weapons) {
			if (weapon.getItemId() > highestId) {
				highestId = weapon.getItemId();
			}
		}

		final Collection<L1Armor> armors = _armors.values();
		for (final L1Armor armor : armors) {
			if (armor.getItemId() > highestId) {
				highestId = armor.getItemId();
			}
		}

		this._allTemplates = new L1Item[highestId + 1];

		for (final Iterator<Integer> iter = _etcitems.keySet().iterator(); iter.hasNext();) {
			final Integer id = iter.next();
			final L1EtcItem item = _etcitems.get(id);
			this._allTemplates[id.intValue()] = item;
		}

		for (final Iterator<Integer> iter = _weapons.keySet().iterator(); iter.hasNext();) {
			final Integer id = iter.next();
			final L1Weapon item = _weapons.get(id);
			this._allTemplates[id.intValue()] = item;
		}

		for (final Iterator<Integer> iter = _armors.keySet().iterator(); iter.hasNext();) {
			final Integer id = iter.next();
			final L1Armor item = _armors.get(id);
			this._allTemplates[id.intValue()] = item;
		}
	}

	/**
	 * 套裝效果
	 */
	public void se_mode() {
		PerformanceTimer timer = new PerformanceTimer();
		for (L1Item item : _allTemplates) {
			if (item != null) {
				for (Integer key : ArmorSet.getAllSet().keySet()) {
					ArmorSet armorSet = (ArmorSet) ArmorSet.getAllSet().get(key);

					if (armorSet.isPartOfSet(item.getItemId())) {
						item.set_mode(armorSet.get_mode());
					}
				}
			}
		}
		_log.info("載入套裝效果數字陣列: " + timer.get() + "ms)");
	}

	/**
	 * 傳回指定編號物品資料
	 * 
	 * @param itemid
	 * @return
	 */
	public L1Item getTemplate(final int itemid) {
		try {
			return this._allTemplates[itemid];

		} catch (final Exception e) {
		}
		return null;
	}

	/**
	 * 傳回指定名稱物品資料
	 * 
	 * @param nameid
	 * @return
	 */
	public L1Item getTemplate(final String nameid) {
		for (final L1Item item : this._allTemplates) {
			if ((item != null) && item.getNameId().equals(nameid)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * 產生新物件
	 * 
	 * @param itemId
	 * @return
	 */
	public L1ItemInstance createItem(final int itemId) {
		final L1Item temp = this.getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		final L1ItemInstance item = new L1ItemInstance();
		item.setId(IdFactory.get().nextId());
		item.setItem(temp);
		item.setBless(temp.getBless());

		World.get().storeObject(item);
		return item;
	}

	public L1ItemInstance createItem(final int itemId, final boolean flag) {
		final L1Item temp = this.getTemplate(itemId);
		if (temp == null) {
			return null;
		}
		final L1ItemInstance item = new L1ItemInstance();
		item.setItem(temp);
		if (flag) {
			item.setId(IdFactory.get().nextId());
			item.setBless(temp.getBless());
			World.get().storeObject(item);
		}
		return item;
	}

	/**
	 * 依名稱(NameId)找回itemid
	 * 
	 * @param name
	 * @return
	 */
	public int findItemIdByName(final String name) {
		int itemid = 0;
		for (final L1Item item : this._allTemplates) {
			if ((item != null) && item.getNameId().equals(name)) {
				itemid = item.getItemId();
				break;
			}
		}
		return itemid;
	}

	/**
	 * 依名稱(中文)找回itemid
	 * 
	 * @param name
	 * @return
	 */
	public int findItemIdByNameWithoutSpace(final String name) {
		int itemid = 0;
		for (final L1Item item : this._allTemplates) {
			if ((item != null) && item.getNameId().replace(" ", "").equals(name)) {
				itemid = item.getItemId();
				break;
			}
		}
		return itemid;
	}
	/**
	 * 依名称(中文)找回itemid
	 * 
	 * @param name
	 * @return
	 */
	public int findItemIdByNameWithoutSpace1(final String name) {
		int itemid = 0;
		for (final L1Item item : this._allTemplates) {
			// 屏掉这行改为下面那行getNameId 改为 getName QQ：403471355
			// if ((item != null) && item.getNameId().replace(" ",
			// "").equals(name)) {
			if ((item != null) && item.getName().replace(" ", "").equals(name)) {
				itemid = item.getItemId();
				break;
			}
		}
		return itemid;
	}
	/**
	 * 依名稱(中文)找回itemid
	 * 
	 * @param name
	 * @return
	 */
	public int findItemIdByNameWithoutSpace2(final String name) {
		int itemid = 0;
		for (final L1Item item : this._allTemplates) {
			if ((item != null) && item.getName().replace(" ", "").equals(name)) {
				itemid = item.getItemId();
				break;
			}
		}
		return itemid;
	}
	
    public String findItemIdByName(int itemid) {
        String name = null;
        for (L1Item item : _allTemplates) {
            if (item != null && item.getItemId() == itemid) {
                name = item.getName();
                return name;
            }
        }
        return null;
    }

	// XXX l1j-tw create new item
    // 新增武器屬性
	public static L1ItemInstance createNewItemGround(final L1Character target, final int item_id, final int count) {
		return createNewItem(target, item_id, count, 0, null, false, true, 0, 1, 0, 0, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count) {
		return createNewItem(pc, item_id, count, 0, null, false, false, 0, 1, 0, 0, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final boolean isIdentified) {
		return createNewItem(pc, item_id, count, 0, null, isIdentified, false, 0, 1, 0, 0, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final int enchant, final int bless, final boolean isIdentified) {
		return createNewItem(pc, item_id, count, enchant, null, isIdentified, false, 0, bless, 0, 0, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final int enchant, final int bless, final boolean isIdentified, final int useDay) {
		return createNewItem(pc, item_id, count, enchant, null, isIdentified, false, useDay, bless, 0, 0, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final int enchant) {
		return createNewItem(pc, item_id, count, enchant, null, false, false, 0, 1, 0, 0, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final int enchant, final boolean showMessage) {
		return createNewItem(pc, item_id, count, enchant, null, false, false, 0, 1, 0, 0, showMessage);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final int enchant, final boolean isIdentified, final boolean showMessage) {
		return createNewItem(pc, item_id, count, enchant, null, isIdentified, false, 0, 1, 0, 0, showMessage);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final int enchant, final int useDay) {
		return createNewItem(pc, item_id, count, enchant, null, false, false, useDay, 1, 0, 0, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final String name) {
		return createNewItem(pc, item_id, count, 0, name, false, false, 0, 1, 0, 0, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final int enchant, final String name) {
		return createNewItem(pc, item_id, count, enchant, name, false, false, 0, 1, 0, 0, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final int enchant, final int bless, final int attrid, final int attrlv, final boolean isIdentified) {
		return createNewItem(pc, item_id, count, enchant, null, isIdentified, false, 0, bless, attrid, attrlv, true);
	}

	public static L1ItemInstance createNewItem(final L1PcInstance pc, final int item_id, final int count,
			final int enchant, final int attrid, final int attrlv) {
		return createNewItem(pc, item_id, count, enchant, null, false, false, 0, 1, attrid, attrlv, true);
	}

	private static L1ItemInstance createNewItem(final L1Character target, final int item_id, final int count,
			final int enchant, final String npcName, final boolean isIdentified, final boolean isGround,
			final int useDay, final int bless, final int attrid, final int attrlv, final boolean isMsg) {

		final L1ItemInstance item = ItemTable.get().createItem(item_id);

		if (item == null) {
			//log.log(Level.SEVERE, "ItemTable createNewItem item_id= [" + item_id + "] is null");
			_log.error("ItemTable createNewItem item_id= [" + item_id + "] is null");
			return null;
		}

		if (item.isStackable()) {
			item.setCount(count);
			item.setEnchantLevel(enchant);
			// item.setAttrEnchantKind(attrid);
			// item.setAttrEnchantLevel(attrlv);
			item.setIdentified(isIdentified);
			if (bless != 1) {
				item.setBless(bless);
			}
			// item.setItemEffectLoad();
			if (isGround) {
				World.get().getInventory(target.getX(), target.getY(), target.getMapId()).storeItem(item);
			} else if (target instanceof L1PcInstance) {
				final L1PcInstance pc = (L1PcInstance) target;
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
					pc.getInventory().storeItem(item);

				} else {
					item.set_showId(pc.get_showId());
					// 掉落地面
					World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
				}
			}
		} else {
			for (int i = 0; i < count; i++) {
				L1ItemInstance each_item;
				if (i == 0) {
					each_item = item; // 回傳第一個
				} else {
					each_item = ItemTable.get().createItem(item_id);
				}

				// XXX 中古商
				if (enchant == -1) {
					int rnd_enchant = 0;
					final int chance = Random.nextInt(100) + 1;
					if (chance <= 15) {
						rnd_enchant = -2;
					} else if ((chance >= 16) && (chance <= 30)) {
						rnd_enchant = -1;
					} else if ((chance >= 31) && (chance <= 70)) {
						rnd_enchant = 0;
					} else if ((chance >= 71) && (chance <= 87)) {
						rnd_enchant = Random.nextInt(2) + 1;
					} else if ((chance >= 88) && (chance <= 97)) {
						rnd_enchant = Random.nextInt(3) + 3;
					} else if ((chance >= 98) && (chance <= 99)) {
						rnd_enchant = 6;
					} else if (chance == 100) {
						rnd_enchant = 7;
					}
					each_item.setEnchantLevel(rnd_enchant);
				} else {
					each_item.setEnchantLevel(enchant);
				}

				each_item.setIdentified(isIdentified);

				// 新增武器屬性
				if (each_item.getItem().getType2() == 1) { // 武器類
					each_item.setAttrEnchantKind(attrid);
					each_item.setAttrEnchantLevel(attrlv);
				}

				if (bless != 1) {
					each_item.setBless(bless);
				}
				/*each_item.setItemEffectLoad();

				// XXX 特例... 一拳手套(1小時)
				if (each_item.getItemId() == 413) {
					each_item.setLimitTime(new Timestamp(System.currentTimeMillis() + 60 * 60 * 1000L));
				}

				// has use time limit
				if (useDay > 0) {
					final Timestamp limit = new Timestamp(System.currentTimeMillis() + useDay * 24 * 60 * 60 * 1000L);
					each_item.setLimitTime(limit);
				}*/

				if (isGround) {
					World.get().getInventory(target.getX(), target.getY(), target.getMapId()).storeItem(each_item);
				} else if (target instanceof L1PcInstance) {
					final L1PcInstance pc = (L1PcInstance) target;
					if (pc.getInventory().checkAddItem(each_item, count) == L1Inventory.OK) {
						pc.getInventory().storeItem(each_item);
					} else {
						each_item.set_showId(pc.get_showId());
						// 掉落地面
						World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(each_item);
					}
				}
			}
		}
		if (target instanceof L1PcInstance && isMsg) {
			final L1PcInstance pc = (L1PcInstance) target;
			if (npcName != null) {
				pc.sendPackets(new S_ServerMessage(143, npcName, item.getLogName())); // ...給你
			} else {
				pc.sendPackets(new S_ServerMessage(403, item.getLogName())); // 獲得
			}

		}
		return item;
	}
	// XXX l1j-tw create new item -> end
}
