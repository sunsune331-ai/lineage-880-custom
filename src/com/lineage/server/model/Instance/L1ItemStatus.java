package com.lineage.server.model.Instance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.lineage.server.datatables.ArmorSetTable;
import com.lineage.server.datatables.CheckItemPowerTable;
import com.lineage.server.datatables.DollPowerTable;
import com.lineage.server.datatables.ItemBuffTable;
import com.lineage.server.datatables.ItemVIPTable;
import com.lineage.server.datatables.PetItemTable;
import com.lineage.server.datatables.StonePowerTable;
import com.lineage.server.datatables.SuperRuneTable;
import com.lineage.server.datatables.WeaponSkillTable;
import com.lineage.server.model.L1WeaponSkill;
import com.lineage.server.model.doll.L1DollExecutor;
import com.lineage.server.templates.L1BossWeapon;
import com.lineage.server.templates.L1CheckItemPower;
import com.lineage.server.templates.L1CriticalHitStone;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ItemBuff;
import com.lineage.server.templates.L1ItemPower_bless;
import com.lineage.server.templates.L1ItemPower_name;
import com.lineage.server.templates.L1ItemVIP;
import com.lineage.server.templates.L1MagicWeapon;
import com.lineage.server.templates.L1PetItem;
import com.lineage.server.templates.L1StonePower;
import com.lineage.server.templates.L1SuperRune;
import com.lineage.server.utils.BinaryOutputStream;

import william.EnchantAccessory;
import william.EnchantOrginal;
import william.L1WeaponSoul;
import william.L1WilliamEnchantAccessory;
import william.L1WilliamEnchantOrginal;
import william.WeaponSoul;
import william.WilliamItemMessage;

/**
 * 物品詳細資料
 * 
 * @author dexc
 */
public class L1ItemStatus {
	private final L1ItemInstance _itemInstance;
	private final L1Item _item;
	private final BinaryOutputStream _os;
	private final L1ItemPower _itemPower;
	private L1PcInstance _pc;
	private boolean _statusx;// 是否取回物品轉移狀態
	private boolean _isMatch;

	/**
	 * 物品詳細資料
	 * 
	 * @param itemInstance
	 *            L1ItemInstance
	 */
	public L1ItemStatus(L1ItemInstance itemInstance) {
		_itemInstance = itemInstance;
		_item = itemInstance.getItem();
		_os = new BinaryOutputStream();
		_itemPower = new L1ItemPower(_itemInstance);
	}

	public L1ItemStatus(L1Item template) {
		_itemInstance = new L1ItemInstance();
		_itemInstance.setItem(template);
		_item = template;
		_os = new BinaryOutputStream();
		_itemPower = new L1ItemPower(_itemInstance);
	}

	public L1ItemStatus(final L1Item template, final int enchantLevel) {
		this(template);
		this._itemInstance.setEnchantLevel(enchantLevel);
	}

	/**
	 * vip 顯示設定
	 */
	private BinaryOutputStream etcitem_card(L1ItemVIP vip) {
		int add_ac = vip.get_add_ac(); // 防禦
		if (add_ac != 0) {
			_os.writeC(56);
			_os.writeC(add_ac);
		}

		int add_str = vip.get_add_str(); // 力量
		if (add_str != 0) {
			_os.writeC(8);
			_os.writeC(add_str);
		}
		int add_dex = vip.get_add_dex(); // 敏捷
		if (add_dex != 0) {
			_os.writeC(9);
			_os.writeC(add_dex);
		}
		int add_con = vip.get_add_con(); // 體質
		if (add_con != 0) {
			_os.writeC(10);
			_os.writeC(add_con);
		}
		int add_wis = vip.get_add_wis(); // 精神
		if (add_wis != 0) {
			_os.writeC(11);
			_os.writeC(add_wis);
		}
		int add_int = vip.get_add_int(); // 智力
		if (add_int != 0) {
			_os.writeC(12);
			_os.writeC(add_int);
		}
		int add_cha = vip.get_add_cha(); // 魅力
		if (add_cha != 0) {
			_os.writeC(13);
			_os.writeC(add_cha);
		}

		int add_dmg = vip.get_add_dmg(); // 近戰傷害
		if (add_dmg != 0) {
			_os.writeC(47);
			_os.writeC(add_dmg);
		}
		int add_hit = vip.get_add_hit(); // 近戰命中
		if (add_hit != 0) {
			_os.writeC(48);
			_os.writeC(add_hit);
		}
		int add_bow_dmg = vip.get_add_bow_dmg(); // 遠攻傷害
		if (add_bow_dmg != 0) {
			_os.writeC(35);
			_os.writeC(add_bow_dmg);
		}
		int add_bow_hit = vip.get_add_bow_hit(); // 遠攻命中
		if (add_bow_hit != 0) {
			_os.writeC(24);
			_os.writeC(add_bow_hit);
		}
		int add_mr = vip.get_add_mr(); // 魔法防禦
		if (add_mr != 0) {
			_os.writeC(15);
			_os.writeH(add_mr);
		}
		int add_sp = vip.get_add_sp(); // 魔攻
		if (add_sp != 0) {
			_os.writeC(17);
			_os.writeC(add_sp);
		}
		int add_fire = vip.get_add_fire(); // 火屬性
		if (add_fire != 0) {
			_os.writeC(27);
			_os.writeC(add_fire);
		}
		int add_wind = vip.get_add_wind(); // 風屬性
		if (add_wind != 0) {
			_os.writeC(29);
			_os.writeC(add_wind);
		}
		int add_earth = vip.get_add_earth(); // 地屬性
		if (add_earth != 0) {
			_os.writeC(30);
			_os.writeC(add_earth);
		}
		int add_water = vip.get_add_water(); // 水屬性
		if (add_water != 0) {
			_os.writeC(28);
			_os.writeC(add_water);
		}

		int add_hp = vip.get_add_hp(); // 血量
		if (add_hp != 0) {
			_os.writeC(14);
			_os.writeH(add_hp);
		}

		int add_mp = vip.get_add_mp(); // 魔量
		if (add_mp != 0) {
			_os.writeC(0x20);
			_os.writeH(add_mp);
		}

		int add_hpr = vip.get_add_hpr(); // 回血
		if (add_hpr != 0) {
			_os.writeC(37);
			_os.writeC(add_hpr);
		}
		int add_mpr = vip.get_add_mpr(); // 回魔
		if (add_mpr != 0) {
			_os.writeC(38);
			_os.writeC(add_mpr);
		}
		/*
		 * int add_freeze = vip.get_add_freeze(); if (add_freeze != 0) {
		 * _os.writeC(33); _os.writeC(1); _os.writeC(add_freeze); }
		 * 
		 * int add_stone = vip.get_add_stone(); if (add_stone != 0) {
		 * _os.writeC(33); _os.writeC(2); _os.writeC(add_stone); }
		 * 
		 * int add_sleep = vip.get_add_sleep(); if (add_sleep != 0) {
		 * _os.writeC(33); _os.writeC(3); _os.writeC(add_sleep); }
		 * 
		 * int add_blind = vip.get_add_blind(); if (add_blind != 0) {
		 * _os.writeC(33); _os.writeC(4); _os.writeC(add_blind); } int add_stun
		 * = vip.get_add_stun(); if (add_stun != 0) { _os.writeC(33);
		 * _os.writeC(5); _os.writeC(add_stun); }
		 * 
		 * int add_sustain = vip.get_add_sustain(); if (add_sustain != 0) {
		 * _os.writeC(33); _os.writeC(6); _os.writeC(add_sustain); }
		 */
		int wmd = vip.get_add_wmd(); // 魔武傷害增加
		if (wmd != 0) {
			_os.writeC(39);
			_os.writeS("魔武傷害:" + wmd + "%");
		}
		int wmc = vip.get_add_wmc(); // 魔武發動增加
		if (wmc != 0) {
			_os.writeC(39);
			_os.writeS("魔武發動:" + wmc + "%");
		}

		int dmgr = vip.get_add_dmg_r(); // 物理減傷
		if (dmgr != 0) {
			_os.writeC(39);
			_os.writeS("物理減傷: +" + dmgr);
		}

		int mdmgr = vip.get_add_magic_r(); // 魔法減傷
		if (mdmgr != 0) {
			_os.writeC(39);
			_os.writeS("魔法減傷: +" + mdmgr);
		}

		int exp = vip.get_add_exp(); // 經驗值增加
		if (exp != 0) {
			if (exp <= 120) {
				_os.writeC(36);
				_os.writeC(exp);
			} else {
				_os.writeC(0x27);
				_os.writeS("$6134 " + exp + "%");
			}
		}
		int gf = vip.get_add_adena(); // 金幣倍率增加
		if (gf != 0) {
			_os.writeC(39);
			_os.writeS("金幣倍率:" + gf + "%");
		}

		boolean item = vip.get_death_item(); // 防噴道具
		if (item) {
			_os.writeC(39);
			_os.writeS("防噴道具");
		}

		boolean exp1 = vip.get_death_exp(); // 防噴經驗
		if (exp1) {
			_os.writeC(39);
			_os.writeS("防噴經驗");
		}

		boolean skill = vip.get_death_skill(); // 防噴技能
		if (skill) {
			_os.writeC(39);
			_os.writeS("防噴技能");
		}
		boolean score = vip.get_death_score(); // 防噴積分
		if (score) {
			_os.writeC(39);
			_os.writeS("防噴積分");
		}
		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		this._os.writeC(0x17); // 材質
		this._os.writeC(this._item.getMaterial());
		this._os.writeD(this._itemInstance.getWeight());

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}
		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		return _os;
	}

	/**
	 * 傳回物品描述
	 * 
	 * @param statusx
	 *            (是否取回物品轉移狀態)
	 * @return
	 */
	public BinaryOutputStream getStatusBytes(boolean statusx) {
		_statusx = statusx;
		int use_type = _item.getUseType();
		switch (use_type) {
		case -11: // 對讀取方法調用無法分類的物品
		case -10: // 加速藥水
		case -9: // 技術書
		case -8: // 料理書
		case -7: // 增HP道具
		case -6: // 增MP道具
		case -5: // 食人妖精競賽票
		case -4: // 項圈
		case -1: // 無法使用(材料等)
		case 0: // 一般物品
		case 3: // 創造怪物魔杖(無須選取目標 - 無數量:沒有任何事情發生)
		case 5: // 魔杖類型(須選取目標)
		case 6: // 瞬間移動卷軸
		case 7: // 鑒定卷軸
		case 9: // 傳送回家的卷軸
		case 8: // 復活卷軸
		case 12: // 信紙
		case 13: // 信紙(寄出)
		case 14: // 請選擇一個物品(道具欄位)
		case 15: // 哨子
		case 16: // 變形卷軸
		case 17: // 選取目標 (近距離)
		case 26: // 對武器施法的卷軸
		case 27: // 對盔甲施法的卷軸
		case 28: // 空的魔法卷軸
		case 29: // 瞬間移動卷軸(祝福)
		case 30: // 魔法卷軸選取目標 (遠距離 無XY座標傳回)
		case 31: // 聖誕卡片
		case 32: // 聖誕卡片(寄出)
		case 33: // 情人節卡片
		case 34: // 情人節卡片(寄出)
		case 35: // 白色情人節卡片
		case 36: // 白色情人節卡片(寄出)
		case 39: // 選取目標 (遠距離)
		case 42: // 釣魚桿
		case 46: // 飾品強化卷軸
		case 55: // 請選擇魔法娃娃
		case 62: // 抽抽樂
		case 65: // 抽抽樂
			String classname = _item.getclassname();
			if (classname.startsWith("shop.VIP_Card_")) {
				return etcitem_card(classname);
			}
			if (ItemVIPTable.get().checkVIP(_item.getItemId())) {
				return etcitem_card(ItemVIPTable.get()
						.getVIP(_item.getItemId()));

			} else if (classname.equalsIgnoreCase("doll.Magic_Doll")) {
				return etcitem_doll();
			} else if (classname.equalsIgnoreCase("doll.Magic_Doll2")) {
				return etcitem_doll();
			} else if (classname.equalsIgnoreCase("doll.Magic_Doll_Power")) {
				return etcitem_doll();

			} else if (ItemBuffTable.get().checkItem(_item.getItemId())) { // 道具狀態系統
				return etcitem_Buff(ItemBuffTable.get().getUseEX(
						_item.getItemId()));

			} else if (CheckItemPowerTable.get().checkItem(_item.getItemId())) { // 身上持有道具給予能力系統
				return etcitem_checkitempower(CheckItemPowerTable.get()
						.getItem(_item.getItemId()));

			} else if ((_item.getItemId() == 56147) || // 真 妲蒂斯魔石
					(_item.getItemId() == 56148) || // 妲蒂斯魔石
					(_item.getItemId() == 56150) || // 守護者的靈魂
					(_item.getItemId() == 56152) // 戰神之魂
			) {
				return effective_item();
			}

			return etcitem();

		case -12: // 寵物用具
			final L1PetItem petItem = PetItemTable.get().getTemplate(
					this._item.getItemId());
			// 武器
			if (petItem.isWeapom()) {
				return this.petweapon(petItem);
				// 防具
			} else {
				return this.petarmor(petItem);
			}
		case -3: // 飛刀
		case -2: // 箭
			return this.arrow();

		case 38: // 食物
			return this.fooditem();

		case 10: // 照明道具
			return this.lightitem();
		case 2: // 盔甲
		case 18: // T恤
		case 19: // 斗篷
		case 20: // 手套
		case 21: // 靴
		case 22: // 頭盔
		case 25: // 盾牌
		case 51: // 肩甲
		case 52: // 徽章
		case 70: // 脛甲
			return this.armor();

		case 40: // 耳環
		case 23: // 戒指
		case 24: // 項鏈
		case 37: // 腰帶
			return this.accessories();
		case 43: // 符石/上左
		case 44: // 符石/下左
		case 45: // 符石/下右
		case 48: // 符石/下中
		case 49: // 符石/上右
			return this.accessories2();
		case 1: // 武器
			return this.weapon();
		}
		return null;
	}

	/** 身上持有道具給予能力系統 */
	private BinaryOutputStream etcitem_checkitempower(L1CheckItemPower power) {
		if (power.get_ac() != 0) { // 防禦
			_os.writeC(56);
			_os.writeC(power.get_ac());
		}
		if (power.get_str() != 0) { // 力量
			_os.writeC(8);
			_os.writeC(power.get_str());
		}
		if (power.get_dex() != 0) { // 敏捷
			_os.writeC(9);
			_os.writeC(power.get_dex());
		}
		if (power.get_con() != 0) { // 體質
			_os.writeC(10);
			_os.writeC(power.get_con());
		}
		if (power.get_wis() != 0) { // 精神
			_os.writeC(11);
			_os.writeC(power.get_wis());
		}
		if (power.get_intel() != 0) { // 智力
			_os.writeC(12);
			_os.writeC(power.get_intel());
		}
		if (power.get_cha() != 0) { // 魅力
			_os.writeC(13);
			_os.writeC(power.get_cha());
		}
		if (power.get_hp() != 0) { // 血量
			_os.writeC(14);
			_os.writeH(power.get_hp());
		}
		if (power.get_mp() != 0) { // 魔量
			_os.writeC(0x20);
			_os.writeH(power.get_mp());
		}
		if (power.get_mr() != 0) { // 魔法防禦
			_os.writeC(15);
			_os.writeH(power.get_mr());
		}
		if (power.get_sp() != 0) { // 魔攻
			_os.writeC(17);
			_os.writeC(power.get_sp());
		}
		if (power.get_dmg() != 0) { // 近戰傷害
			_os.writeC(47);
			_os.writeC(power.get_dmg());
		}
		if (power.get_bow_dmg() != 0) { // 遠攻傷害
			_os.writeC(35);
			_os.writeC(power.get_bow_dmg());
		}
		if (power.get_hit() != 0) { // 近戰命中
			_os.writeC(48);
			_os.writeC(power.get_hit());
		}
		if (power.get_bow_hit() != 0) { // 遠攻命中
			_os.writeC(24);
			_os.writeC(power.get_bow_hit());
		}
		if (power.get_dmg_r() != 0) { // 物理減傷
			_os.writeC(39);
			_os.writeS("物理減傷 +" + power.get_dmg_r());
		}
		if (power.get_magic_r() != 0) { // 魔法減傷
			_os.writeC(39);
			_os.writeS("魔法減傷 +" + power.get_magic_r());
		}
		if (power.get_fire() != 0) { // 火屬性
			_os.writeC(27);
			_os.writeC(power.get_fire());
		}
		if (power.get_water() != 0) { // 水屬性
			_os.writeC(28);
			_os.writeC(power.get_water());
		}
		if (power.get_wind() != 0) { // 風屬性
			_os.writeC(29);
			_os.writeC(power.get_wind());
		}
		if (power.get_earth() != 0) { // 地屬性
			_os.writeC(30);
			_os.writeC(power.get_earth());
		}

		/*
		 * if (power.get_freeze() != 0) { // 冰凍耐性 _os.writeC(33); _os.writeC(1);
		 * _os.writeC(power.get_freeze()); } if (power.get_stone() != 0) { //
		 * 石化耐性 _os.writeC(33); _os.writeC(2); _os.writeC(power.get_stone());1 }
		 * if (power.get_sleep() != 0) { // 睡眠耐性 _os.writeC(33); _os.writeC(3);
		 * _os.writeC(power.get_sleep()); } if (power.get_blind() != 0) { //
		 * 暗黑耐性 _os.writeC(33); _os.writeC(4); _os.writeC(power.get_blind()); }
		 * if (power.get_stun() != 0) { // 昏迷耐性 _os.writeC(33); _os.writeC(5);
		 * _os.writeC(power.get_stun()); } if (power.get_sustain() != 0) { //
		 * 支撐耐性 _os.writeC(33); _os.writeC(6); _os.writeC(power.get_sustain());
		 * }
		 */

		if (power.get_hpr() != 0) { // 體力回復量
			_os.writeC(37);
			_os.writeC(power.get_hpr());
		}
		if (power.get_mpr() != 0) { // 魔力回復量
			_os.writeC(38);
			_os.writeC(power.get_mpr());
		}
		if (power.get_exp() != 0) { // 狩獵經驗值
			if (power.get_exp() <= 120) {
				_os.writeC(36);
				_os.writeC(power.get_exp());
			} else {
				_os.writeC(0x27);
				_os.writeS("$6134 " + power.get_exp() + "%");
			}
		}
		if (power.get_gf() != 0) { // 金幣倍率
			_os.writeC(39);
			_os.writeS("金幣倍率 +" + power.get_gf() + "%");
		}
		boolean item = power.get_death_item(); // 死亡不噴道具
		if (item) {
			_os.writeC(39);
			_os.writeS("防噴道具");
		}
		boolean exp = power.get_death_exp(); // 死亡不噴經驗值
		if (exp) {
			_os.writeC(39);
			_os.writeS("防噴經驗");
		}
		boolean skill = power.get_death_skill(); // 死亡不噴技能
		if (skill) {
			_os.writeC(39);
			_os.writeS("防噴技能");
		}
		boolean score = power.get_death_score(); // 死亡不掉積分
		if (score) {
			_os.writeC(39);
			_os.writeS("防噴積分");
		}
		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		this._os.writeC(0x17); // 材質
		this._os.writeC(this._item.getMaterial());
		this._os.writeD(this._itemInstance.getWeight());

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return _os;
	}

	// TODO 持有效果道具
	private BinaryOutputStream effective_item() {
		if (_item.getItemId() == 56147) {// 真 妲蒂斯魔石
			_os.writeC(14);// 血量上限
			_os.writeH(100);

			_os.writeC(0x20);// 魔力上限
			_os.writeH(100);

			_os.writeC(47);// 近距離傷害
			_os.writeC(5);

			_os.writeC(35);// 遠距離傷害
			_os.writeC(5);

			_os.writeC(17);// 魔攻
			_os.writeC(5);

			_os.writeC(63); // 傷害減免
			_os.writeC(5);

			_os.writeC(37);// 體力回復量
			_os.writeC(5);

			_os.writeC(38);// 魔力回復量
			_os.writeC(5);
		} else if (_item.getItemId() == 56148) {// 妲蒂斯魔石
			_os.writeC(14);// 血量上限
			_os.writeH(30);

			_os.writeC(0x20);// 魔力上限
			_os.writeH(30);

			_os.writeC(47);// 近距離傷害
			_os.writeC(2);

			_os.writeC(35);// 遠距離傷害
			_os.writeC(2);

			_os.writeC(17);// 魔攻
			_os.writeC(2);

			_os.writeC(63); // 傷害減免
			_os.writeC(2);

			_os.writeC(37);// 體力回復量
			_os.writeC(2);

			_os.writeC(38);// 魔力回復量
			_os.writeC(2);
		} else if (_item.getItemId() == 56150) {// 守護者的靈魂
			_os.writeC(14);// 血量上限
			_os.writeH(400);

			_os.writeC(0x20);// 魔力上限
			_os.writeH(200);

			_os.writeC(47);// 近距離傷害
			_os.writeC(100);

			_os.writeC(17);// 魔攻
			_os.writeC(30);

			_os.writeC(63); // 傷害減免
			_os.writeC(60);

			_os.writeC(39);
			_os.writeS("防噴經驗");

			_os.writeC(39);
			_os.writeS("防噴道具");
		} else if (_item.getItemId() == 56152) {// 戰神之魂
			_os.writeC(14);// 血量上限
			_os.writeH(120);

			_os.writeC(0x20);// 魔力上限
			_os.writeH(100);

			_os.writeC(47); // 近距離傷害
			_os.writeC(15);

			_os.writeC(35);// 遠距離傷害
			_os.writeC(15);

			_os.writeC(17);// 魔攻
			_os.writeC(5);

			_os.writeC(63); // 傷害減免
			_os.writeC(8);

		}

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		this._os.writeC(0x17); // 材質
		this._os.writeC(this._item.getMaterial());
		this._os.writeD(this._itemInstance.getWeight());

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return _os;
	}

	/** 娃娃能力描述 */
	private BinaryOutputStream etcitem_doll() {
		final L1Doll doll = DollPowerTable.get().get_type(_item.getItemId());
		String msg = null;
		// _os.writeC(39);
		// _os.writeS("詳細數值如下");
		if (!doll.getPowerList().isEmpty()) {
			for (L1DollExecutor power : doll.getPowerList()) {
				if (power.getDollAc() != 0) { // 防禦
					_os.writeC(56);
					_os.writeC(power.getDollAc());
				}
				if (power.getDollStr() != 0) { // 力量
					_os.writeC(8);
					_os.writeC(power.getDollStr());
				}
				if (power.getDollDex() != 0) { // 敏捷
					_os.writeC(9);
					_os.writeC(power.getDollDex());
				}
				if (power.getDollCon() != 0) { // 體質
					_os.writeC(10);
					_os.writeC(power.getDollCon());
				}
				if (power.getDollWis() != 0) { // 精神
					_os.writeC(11);
					_os.writeC(power.getDollWis());
				}
				if (power.getDollInt() != 0) { // 智力
					_os.writeC(12);
					_os.writeC(power.getDollInt());
				}
				if (power.getDollCha() != 0) { // 魅力
					_os.writeC(13);
					_os.writeC(power.getDollCha());
				}
				if (power.getDollHp() != 0) { // 血量
					_os.writeC(14);
					_os.writeH(power.getDollHp());
				}
				if (power.getDollMp() != 0) { // 魔量
					_os.writeC(0x20);
					_os.writeH(power.getDollMp());
				}
				if (power.getDollHpr() != 0) { // 體力回覆量
					_os.writeC(37);
					_os.writeC(power.getDollHpr());
				}
				if (power.getDollMpr() != 0) { // 魔力回覆量
					_os.writeC(38);
					_os.writeC(power.getDollMpr());
				}
				if (power.getDollMr() != 0) { // 魔法防禦
					_os.writeC(15);
					_os.writeH(power.getDollMr());
				}
				if (power.getDollSp() != 0) { // 魔攻
					_os.writeC(17);
					_os.writeC(power.getDollSp());
				}
				if (power.getDollDmg() != 0) { // 近戰傷害
					_os.writeC(47);
					_os.writeC(power.getDollDmg());
				}
				if (power.getDollHit() != 0) { // 近戰命中
					_os.writeC(48);
					_os.writeC(power.getDollHit());
				}
				if (power.getDollBowDmg() != 0) { // 遠攻傷害
					_os.writeC(35);
					_os.writeC(power.getDollBowDmg());
				}
				if (power.getDollBowHit() != 0) { // 遠攻命中
					_os.writeC(24);
					_os.writeC(power.getDollBowHit());
				}
				if (power.getDollAllDmg_R() != 0) { // 傷害減免
					_os.writeC(63);
					_os.writeC(power.getDollAllDmg_R());
				}
				if (power.getDollExp() != 0) { // 狩獵經驗值
					if (power.getDollExp() <= 120) {
						_os.writeC(36);
						_os.writeC(power.getDollExp());
					} else {
						_os.writeC(0x27);
						_os.writeS("$6134 " + power.getDollExp() + "%");
					}
				}
				if (power.getDollWeight() != 0) { // 負重增加率(%)
					_os.writeC(68);
					_os.writeC(power.getDollWeight());
				}
				if (power.getDollWeight_R() != 0) { // 增加負重 +X
					_os.writeC(90);
					_os.writeH(power.getDollWeight_R());
				}
				if (power.getDollFire() != 0) { // 火屬性
					_os.writeC(27);
					_os.writeC(power.getDollFire());
				}
				if (power.getDollWater() != 0) { // 水屬性
					_os.writeC(28);
					_os.writeC(power.getDollWater());
				}
				if (power.getDollWind() != 0) { // 風屬性
					_os.writeC(29);
					_os.writeC(power.getDollWind());
				}
				if (power.getDollEarth() != 0) { // 地屬性
					_os.writeC(30);
					_os.writeC(power.getDollEarth());
				}
				if (power.getDollRegistTechnology() != 0) { // 技術耐性
					_os.writeC(117);
					_os.writeC(power.getDollRegistTechnology());
				}
				if (power.getDollRegistElf() != 0) { // 精靈耐性
					_os.writeC(118);
					_os.writeC(power.getDollRegistElf());
				}
				if (power.getDollRegistDragon() != 0) { // 龍屬耐性
					_os.writeC(119);
					_os.writeC(power.getDollRegistDragon());
				}
				if (power.getDollRegistHorror() != 0) { // 恐怖耐性
					_os.writeC(120);
					_os.writeC(power.getDollRegistHorror());
				}
				if (power.getDollRegistAll() != 0) { // 全部四大耐性
					_os.writeC(121);
					_os.writeC(power.getDollRegistAll());
				}
				// 耐性end
				// 命中
				if (power.getDollHitTechnology() != 0) { // 技術命中
					_os.writeC(122);
					_os.writeC(power.getDollHitTechnology());
				}
				if (power.getDollHitElf() != 0) { // 精靈命中
					_os.writeC(123);
					_os.writeC(power.getDollHitElf());
				}
				if (power.getDollHitDragon() != 0) { // 龍屬命中
					_os.writeC(124);
					_os.writeC(power.getDollHitDragon());
				}
				if (power.getDollHitHorror() != 0) { // 恐怖命中
					_os.writeC(125);
					_os.writeC(power.getDollHitHorror());
				}
				if (power.getDollHitAll() != 0) { // 全部四大命中
					_os.writeC(126);
					_os.writeC(power.getDollHitAll());
				}
				if (power.getDollpvpdmg() != 0) { // 狩獵經驗值	
						_os.writeC(0x27);
						_os.writeS("PVP傷害  " + power.getDollpvpdmg());
				}
				if (power.getDollpvpjm() != 0) { // 狩獵經驗值	
					_os.writeC(0x27);
					_os.writeS("PVP減免  " + power.getDollpvpjm());
			    }
				if (power.getDollyshzf() != 0) { // 狩獵經驗值	
					_os.writeC(0x27);
					_os.writeS("殷海薩祝消耗減少  " + power.getDollyshzf());
			   }	
				// 命中end
				if (power.getDollHaste() != 0) { // // 具備加速效果
					_os.writeC(0x12);
				}
				if (power.getDollStunLv() != 0) { // 昏迷等級
					_os.writeC(98);
					_os.writeD(23521); // 昏迷等級提升 +%d
					_os.writeH(power.getDollStunLv());
				}
				if (power.getDollBreakLv() != 0) { // 破壞等級提升
					_os.writeC(98);
					_os.writeD(11147); // 破壞盔甲命中 +%d--- 26608破壞等級 +3
					_os.writeH(power.getDollBreakLv());
				}
				if (power.getDollFoeSlayer() != 0) { // 屠宰者階段別傷害
					_os.writeC(101);
					_os.writeC(power.getDollFoeSlayer());
				}
				if (power.getDollTiTanHp() != 0) { // 泰坦系列技能發動 HP 區間增加
					_os.writeC(102);
					_os.writeC(power.getDollTiTanHp());// %
				}
				if (power.get_note() != null && !power.get_note().isEmpty()) {
					msg = power.get_note();
					_os.writeC(39);
					_os.writeS(msg);
				}
			}
		}

		if (doll.get_level() > 0) { // 娃娃等級
			// _os.writeC(0x27);
			// _os.writeS("娃娃等級 " + doll.get_level());
			_os.writeC(26); // desc-c.tbl -> 1184行
			_os.writeH(doll.get_level());
		}

		String classname = _item.getclassname();
		if (!classname.equalsIgnoreCase("doll.Magic_Doll_Power")) { // 祭司娃娃不顯示
			if (doll != null) {
				if (doll.get_need() != null) {
					final int[] itemids = doll.get_need();
					final int[] counts = doll.get_counts();
					for (int i = 0; i < itemids.length; i++) {
						if (itemids.length == 1 && itemids[i] == 41246) { // 魔法結晶體
							_os.writeC(0x27);
							// _os.writeS("需求魔法結晶體(" + counts[i] + ")個");
							_os.writeS("需求$5240 " + counts[i]);
						}
					}
				}
			}
		}

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		_os.writeC(0x17); // 材質
		_os.writeC(_item.getMaterial());
		_os.writeD(_itemInstance.getWeight());

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return _os;
	}

	/**
	 * 道具狀態系統
	 * 
	 * @param power
	 * @return
	 */
	private BinaryOutputStream etcitem_Buff(L1ItemBuff power) {
		if (power.get_ac() != 0) { // 防禦
			_os.writeC(56);
			_os.writeC(power.get_ac());
		}
		if (power.get_str() != 0) { // 力量
			_os.writeC(8);
			_os.writeC(power.get_str());
		}
		if (power.get_dex() != 0) { // 敏捷
			_os.writeC(9);
			_os.writeC(power.get_dex());
		}
		if (power.get_con() != 0) { // 體質
			_os.writeC(10);
			_os.writeC(power.get_con());
		}
		if (power.get_wis() != 0) { // 精神
			_os.writeC(11);
			_os.writeC(power.get_wis());
		}
		if (power.get_intel() != 0) { // 智力
			_os.writeC(12);
			_os.writeC(power.get_intel());
		}
		if (power.get_cha() != 0) { // 魅力
			_os.writeC(13);
			_os.writeC(power.get_cha());
		}
		if (power.get_hp() != 0) { // 血量
			_os.writeC(14);
			_os.writeH(power.get_hp());
		}
		if (power.get_mp() != 0) { // 魔量
			_os.writeC(0x20);
			_os.writeH(power.get_mp());
		}
		if (power.get_mr() != 0) { // 魔法防禦
			_os.writeC(15);
			_os.writeH(power.get_mr());
		}
		if (power.get_sp() != 0) { // 魔攻
			_os.writeC(17);
			_os.writeC(power.get_sp());
		}
		if (power.get_dmg() != 0) { // 近戰傷害
			_os.writeC(47);
			_os.writeC(power.get_dmg());
		}
		if (power.get_bow_dmg() != 0) { // 遠攻傷害
			_os.writeC(35);
			_os.writeC(power.get_bow_dmg());
		}
		if (power.get_hit() != 0) { // 近戰命中
			_os.writeC(48);
			_os.writeC(power.get_hit());
		}
		if (power.get_bow_hit() != 0) { // 遠攻命中
			_os.writeC(24);
			_os.writeC(power.get_bow_hit());
		}
		if (power.get_dmg_r() != 0) { // 物理減傷
			_os.writeC(39);
			_os.writeS("物理減傷 +" + power.get_dmg_r());
		}
		if (power.get_magic_r() != 0) { // 魔法減傷
			_os.writeC(39);
			_os.writeS("魔法減傷 +" + power.get_magic_r());
		}
		if (power.get_fire() != 0) { // 火屬性
			_os.writeC(27);
			_os.writeC(power.get_fire());
		}
		if (power.get_water() != 0) { // 水屬性
			_os.writeC(28);
			_os.writeC(power.get_water());
		}
		if (power.get_wind() != 0) { // 風屬性
			_os.writeC(29);
			_os.writeC(power.get_wind());
		}
		if (power.get_earth() != 0) { // 地屬性
			_os.writeC(30);
			_os.writeC(power.get_earth());
		}

		// if (power.get_freeze() != 0) { // 冰凍耐性
		// _os.writeC(33);
		// _os.writeC(1);
		// _os.writeC(power.get_freeze());
		// }
		// if (power.get_stone() != 0) { // 石化耐性
		// _os.writeC(33);
		// _os.writeC(2);
		// _os.writeC(power.get_stone());
		// }
		// if (power.get_sleep() != 0) { // 睡眠耐性
		// _os.writeC(33);
		// _os.writeC(3);
		// _os.writeC(power.get_sleep());
		// }
		// if (power.get_blind() != 0) { // 暗黑耐性
		// _os.writeC(33);
		// _os.writeC(4);
		// _os.writeC(power.get_blind());
		// }
		// if (power.get_stun() != 0) { // 昏迷耐性
		// _os.writeC(33);
		// _os.writeC(5);
		// _os.writeC(power.get_stun());
		// }
		// if (power.get_sustain() != 0) { // 支撐耐性
		// _os.writeC(33);
		// _os.writeC(6);
		// _os.writeC(power.get_sustain());
		// }
		if (power.get_pvpdmg() != 0) { // 增加PVP傷害
			_os.writeC(59);
			_os.writeC(power.get_pvpdmg());
		}
		if (power.get_pvpdmg_r() != 0) { // 減免PVP傷害
			_os.writeC(60);
			_os.writeC(power.get_pvpdmg_r());
		}
		if (power.get_hpr() != 0) { // 體力回復量
			_os.writeC(37);
			_os.writeC(power.get_hpr());
		}
		if (power.get_mpr() != 0) { // 魔力回復量
			_os.writeC(38);
			_os.writeC(power.get_mpr());
		}
		if (power.get_exp() != 0) { // 狩獵經驗值
			if (power.get_exp() <= 120) {
				_os.writeC(36);
				_os.writeC(power.get_exp());
			} else {
				_os.writeC(0x27);
				_os.writeS("$6134 " + power.get_exp() + "%");
			}
		}
		if (power.get_gf() != 0) { // 金幣倍率
			_os.writeC(39);
			_os.writeS("金幣倍率 +" + power.get_gf() + "%");
		}
		boolean item = power.get_death_item(); // 死亡不噴道具
		if (item) {
			_os.writeC(39);
			_os.writeS("防噴道具");
		}
		boolean exp = power.get_death_exp(); // 死亡不噴經驗值
		if (exp) {
			_os.writeC(39);
			_os.writeS("防噴經驗");
		}
		boolean skill = power.get_death_skill(); // 死亡不噴技能
		if (skill) {
			_os.writeC(39);
			_os.writeS("防噴技能");
		}
		boolean score = power.get_death_score(); // 死亡不掉積分
		if (score) {
			_os.writeC(39);
			_os.writeS("防噴積分");
		}
		if (power.get_buff_time() != 0) {
			_os.writeC(39);
			_os.writeS("\\f3時效 " + power.get_buff_time() + "秒");
		}

		if (power.getVipLevel() != 0) {
			_os.writeC(39);
			_os.writeS("\\f3使用VIP " + power.getVipLevel());
		}

		if (power.is_buff_save()) {
			_os.writeC(39);
			_os.writeS("\\aE重登狀態保留");
		} else {
			_os.writeC(39);
			_os.writeS("\\aE重登狀態消失");
		}
		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		this._os.writeC(0x17); // 材質
		this._os.writeC(this._item.getMaterial());
		this._os.writeD(this._itemInstance.getWeight());

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return _os;
	}

	private BinaryOutputStream etcitem_card(String classname) {
		int card_id = 0;
		try {
			String cardmode = classname.substring(14);
			card_id = Integer.parseInt(cardmode);
		} catch (Exception e) {
			String cardmode = classname.substring(15);
			card_id = Integer.parseInt(cardmode);
		}
		if (card_id == 0) {
			return _os;
		}

		int freeze = 0;

		int stone = 0;

		int sleep = 0;

		int blind = 0;

		int stun = 0;

		int sustain = 0;

		int addstr = 0;

		int adddex = 0;

		int addcon = 0;

		int addwis = 0;

		int addint = 0;

		int addcha = 0;

		String msg1 = "";
		String msg2 = "";
		String msg3 = "";

		switch (card_id) {
		case 1:
			msg1 = "Exp +10%";
			msg2 = "死亡不會損失經驗值";
			stun = 3;
			freeze = 3;
			stone = 3;
			sleep = 3;
			break;
		case 2:
			msg1 = "Exp +20%";
			msg2 = "死亡不會損失積分";
			addstr = 1;
			adddex = 1;
			addcon = 1;
			addwis = 1;
			addint = 1;
			addcha = 1;
			break;
		case 3:
			msg1 = "Exp +30%";
			msg2 = "死亡不會掉落道具";
			addstr = 2;
			adddex = 2;
			addcon = 2;
			addwis = 2;
			addint = 2;
			addcha = 2;
			break;
		case 4:
			msg1 = "Exp +40%";
			msg2 = "$5539 +5";
			msg3 = "$5541 +5";
			addstr = 3;
			adddex = 3;
			addcon = 3;
			addwis = 3;
			addint = 3;
			addcha = 3;
			break;
		case 5:
			msg1 = "Exp +50%";
			msg2 = "$5539 +10";
			msg3 = "$5541 +10";
			addstr = 4;
			adddex = 4;
			addcon = 4;
			addwis = 4;
			addint = 4;
			addcha = 4;
			break;
		}

		if (msg1.length() > 0) {
			_os.writeC(39);
			_os.writeS(msg1);
		}
		if (msg2.length() > 0) {
			_os.writeC(39);
			_os.writeS(msg2);
		}
		if (msg3.length() > 0) {
			_os.writeC(39);
			_os.writeS(msg3);
		}

		if (addstr != 0) {
			_os.writeC(8);
			_os.writeC(addstr);
		}

		if (adddex != 0) {
			_os.writeC(9);
			_os.writeC(adddex);
		}

		if (addcon != 0) {
			_os.writeC(10);
			_os.writeC(addcon);
		}

		if (addwis != 0) {
			_os.writeC(11);
			_os.writeC(addwis);
		}

		if (addint != 0) {
			_os.writeC(12);
			_os.writeC(addint);
		}

		if (addcha != 0) {
			_os.writeC(13);
			_os.writeC(addcha);
		}

		// if (freeze != 0) {
		// _os.writeC(15);
		// _os.writeH(freeze);
		// _os.writeC(33);
		// _os.writeC(1);
		// }
		// if (stone != 0) {
		// _os.writeC(15);
		// _os.writeH(stone);
		// _os.writeC(33);
		// _os.writeC(2);
		// }
		// if (sleep != 0) {
		// _os.writeC(15);
		// _os.writeH(sleep);
		// _os.writeC(33);
		// _os.writeC(3);
		// }
		// if (blind != 0) {
		// _os.writeC(15);
		// _os.writeH(blind);
		// _os.writeC(33);
		// _os.writeC(4);
		// }
		// if (stun != 0) {
		// _os.writeC(15);
		// _os.writeH(stun);
		// _os.writeC(33);
		// _os.writeC(5);
		// }
		// if (sustain != 0) {
		// _os.writeC(15);
		// _os.writeH(sustain);
		// _os.writeC(33);
		// _os.writeC(6);
		// }

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return _os;
	}

	/**
	 * 飛刀 箭
	 * 
	 * @return
	 */
	private BinaryOutputStream arrow() {
		_os.writeC(1);

		// _os.writeC(_item.getDmgSmall());
		// _os.writeC(_item.getDmgLarge());
		_os.writeC(1);
		_os.writeC(1);

		_os.writeC(_item.getMaterial());
		_os.writeD(_itemInstance.getWeight());

		// 箭類整理 etcitem表增加 "遠距離攻擊力" "遠距離命中" "武器屬性傷害"
		// 原有dmg_small dmg_large 不需要刪除，舊版箭類統一設定1/1。

		// 箭的武器屬性傷害
		// desc是$28295，暫未知os，改為自述
		if (_item.getArrowAttrDmg() != 0) {
			_os.writeC(39);
			_os.writeS("$28295：+" + _item.getArrowAttrDmg());
		}

		// 箭的遠距離傷害
		if (_item.getArrowBowDmg() != 0) {
			_os.writeC(35);
			_os.writeC(_item.getArrowBowDmg());
		}

		// 箭的遠距離命中
		if (_item.getArrowBowHit() != 0) {
			_os.writeC(24);
			_os.writeC(_item.getArrowBowHit());
		}

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}
		return _os;
	}

	/**
	 * 食物
	 * 
	 * @return
	 */
	private BinaryOutputStream fooditem() {
		_os.writeC(21);
		// 榮養
		_os.writeH(_item.getFoodVolume());
		_os.writeC(_item.getMaterial());
		_os.writeD(_itemInstance.getWeight());

		if (_item.getItemId() == 49825) { // 強壯的牛排
			_os.writeC(47); // 近距離攻擊+2
			_os.writeC(2); // 近距離攻擊+2

			_os.writeC(48); // 近距離命中+1
			_os.writeC(1); // 近距離命中+1

			_os.writeC(15); // 魔法防禦+10
			_os.writeH(10); // 魔法防禦+10

			_os.writeC(30); // 地屬性防禦+10
			_os.writeC(10); // 地屬性防禦+10

			_os.writeC(28); // 水屬性防禦+10
			_os.writeC(10); // 水屬性防禦+10

			_os.writeC(27); // 火屬性防禦+10
			_os.writeC(10); // 火屬性防禦+10

			_os.writeC(29); // 風屬性防禦+10
			_os.writeC(10); // 風屬性防禦+10

			_os.writeC(37); // 體力回復量+2
			_os.writeC(2); // 體力回復量+2

			_os.writeC(38); // 魔力回復量+2
			_os.writeC(2); // 魔力回復量+2

			_os.writeC(63); // 傷害減免+2
			_os.writeC(2); // 傷害減免+2

			_os.writeC(39);
			_os.writeS("狩獵經驗值 x1.02");
			_os.writeC(39);
			_os.writeS("\\f3效果時間： 1800秒 ");
		}
		if (_item.getItemId() == 49826) { // 敏捷的煎鮭魚
			_os.writeC(35); // 遠距離攻擊+2
			_os.writeC(2); // 遠距離攻擊+2

			_os.writeC(24); // 遠距離命中+1
			_os.writeC(1); // 遠距離命中+1

			_os.writeC(15); // 魔法防禦+10
			_os.writeH(10); // 魔法防禦+10

			_os.writeC(30); // 地屬性防禦+10
			_os.writeC(10); // 地屬性防禦+10

			_os.writeC(28); // 水屬性防禦+10
			_os.writeC(10); // 水屬性防禦+10

			_os.writeC(27); // 火屬性防禦+10
			_os.writeC(10); // 火屬性防禦+10

			_os.writeC(29); // 風屬性防禦+10
			_os.writeC(10); // 風屬性防禦+10

			_os.writeC(37); // 體力回復量+2
			_os.writeC(2); // 體力回復量+2

			_os.writeC(38); // 魔力回復量+2
			_os.writeC(2); // 魔力回復量+2

			_os.writeC(63); // 傷害減免+2
			_os.writeC(2); // 傷害減免+2

			_os.writeC(39);
			_os.writeS("狩獵經驗值 x1.02");
			_os.writeC(39);
			_os.writeS("\\f3效果時間： 1800秒 ");
		}
		if (_item.getItemId() == 49827) { // 炭烤的火雞
			_os.writeC(17); // 魔攻+2
			_os.writeC(2); // 魔攻+2

			_os.writeC(15); // 魔法防禦+10
			_os.writeH(10); // 魔法防禦+10

			_os.writeC(30); // 地屬性防禦+10
			_os.writeC(10); // 地屬性防禦+10

			_os.writeC(28); // 水屬性防禦+10
			_os.writeC(10); // 水屬性防禦+10

			_os.writeC(27); // 火屬性防禦+10
			_os.writeC(10); // 火屬性防禦+10

			_os.writeC(29); // 風屬性防禦+10
			_os.writeC(10); // 風屬性防禦+10

			_os.writeC(37); // 體力回復量+2
			_os.writeC(2); // 體力回復量+2

			_os.writeC(38); // 魔力回復量+3
			_os.writeC(3); // 魔力回復量+3

			_os.writeC(63); // 傷害減免+2
			_os.writeC(2); // 傷害減免+2

			_os.writeC(39);
			_os.writeS("狩獵經驗值 x1.02");
			_os.writeC(39);
			_os.writeS("\\f3效果時間： 1800秒 ");
		}

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}
		return _os;
	}

	/**
	 * 照明道具
	 * 
	 * @return
	 */
	private BinaryOutputStream lightitem() {
		_os.writeC(22);
		_os.writeH(_item.getLightRange());
		_os.writeC(_item.getMaterial());
		_os.writeD(_itemInstance.getWeight());

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		return _os;
	}

	// TODO 防具類
	/**
	 * 防具類
	 * 
	 * @return
	 */
	private BinaryOutputStream armor() {
		_os.writeC(19);
		int ac = _item.get_ac();
		if (ac < 0) {
			ac = Math.abs(ac);
		}
		_os.writeC(ac);

		_os.writeC(_item.getMaterial());
		_os.writeC(_item.get_greater());
		_os.writeD(_itemInstance.getWeight());

		if (_item.getUseType() != 52) {// 徽章
			if (_itemInstance.getEnchantLevel() != 0) {
				_os.writeC(2);
				_os.writeC(_itemInstance.getEnchantLevel());
			}
		}

		if (_itemInstance.get_durability() != 0) {
			_os.writeC(3);
			_os.writeC(_itemInstance.get_durability());
		}

		int s6_1 = 0;
		int s6_2 = 0;
		int s6_3 = 0;
		int s6_4 = 0;
		int s6_5 = 0;
		int s6_6 = 0;
		int aH_1 = 0;
		int aM_1 = 0;
		int aMR_1 = 0;
		int aSP_1 = 0;
		int aSS_1 = 0;
		int d4_1 = 0;
		int d4_2 = 0;
		int d4_3 = 0;
		int d4_4 = 0;
		// int k6_1 = 0;
		// int k6_2 = 0;
		// int k6_3 = 0;
		// int k6_4 = 0;
		// int k6_5 = 0;
		// int k6_6 = 0;
		int aHpr = 0;
		int aMpr = 0;
		int admg = 0;
		int drd = 0;
		int mdmg = 0;
		int mdrd = 0;
		int bdmg = 0;
		int hit = 0;
		int bhit = 0;
		int mcri = 0;
		// 古文字顯示

		if (_itemInstance.get_power_name() != null
				&& _itemInstance.get_power_name().get_power_id() > 0) {
			this._os.writeC(0x27);
			this._os.writeS(_itemInstance.get_power_name().get_power_name());
		}

		this._os.writeC(0x27);
		// this._os.writeS("安定值: " + _item.get_safeenchant());
		this._os.writeS("安定值:"
				+ ((_item.get_safeenchant() < 0) ? "不可強化" : _item
						.get_safeenchant()));

		/*
		 * final int oldEnchantLvl = _itemInstance.getEnchantLevel(); // 物品強化值
		 * final int safe_enchant = _item.get_safeenchant(); // 物品安定值 int Reduce
		 * = 0; // 強化值減安定值 if (oldEnchantLvl > safe_enchant) { Reduce =
		 * oldEnchantLvl - safe_enchant; }
		 * 
		 * // 防具如果是祝福 if (Reduce > 0) { //this._os.writeC(0x27);
		 * //this._os.writeS("減免傷害: +" + Reduce); _os.writeC(63);
		 * _os.writeC(Reduce); }
		 */

		/*
		 * int RatePlus = 0; String[] ratebyarmor =
		 * this._item.getclassname().split(" "); if
		 * ((ratebyarmor[0].equals("RatePlus")) &&
		 * (Integer.valueOf(ratebyarmor[1]).intValue() > 0)) { RatePlus +=
		 * Integer.valueOf(ratebyarmor[1]).intValue(); } if (RatePlus != 0) {
		 * this._os.writeC(39); this._os.writeS("武器魔法發動率: +" + RatePlus + "%");
		 * 
		 * }
		 */

		int RatePlus2 = 0;
		String[] ratebyarmor2 = this._item.getclassname().split(" ");
		if ((ratebyarmor2[0].equals("RatePlus"))
				&& (Integer.valueOf(ratebyarmor2[2]).intValue() > 0)) {
			RatePlus2 += Integer.valueOf(ratebyarmor2[2]).intValue();
		}

		if (RatePlus2 != 0) {
			this._os.writeC(39);
			this._os.writeS("屬性發動率: +" + RatePlus2 + "%");
		}

		/*
		 * if (_itemInstance.isMatch()) {// 套裝效果 s6_1 = _item.get_mode()[0];//
		 * 套裝效果:力量增加 s6_2 = _item.get_mode()[1];// 套裝效果:敏捷增加 s6_3 =
		 * _item.get_mode()[2];// 套裝效果:體質增加 s6_4 = _item.get_mode()[3];//
		 * 套裝效果:精神增加 s6_5 = _item.get_mode()[4];// 套裝效果:智力增加 s6_6 =
		 * _item.get_mode()[5];// 套裝效果:魅力增加 aH_1 = _item.get_mode()[6];//
		 * 套裝效果:HP增加 aM_1 = _item.get_mode()[7];// 套裝效果:MP增加 aMR_1 =
		 * _item.get_mode()[8];// 套裝效果:抗魔增加 aSP_1 = _item.get_mode()[9];//
		 * SP(魔攻) XXX aSS_1 = _item.get_mode()[10];// 加速效果 XXX d4_1 =
		 * _item.get_mode()[11];// 套裝效果:火屬性增加 d4_2 = _item.get_mode()[12];//
		 * 套裝效果:水屬性增加 d4_3 = _item.get_mode()[13];// 套裝效果:風屬性增加 d4_4 =
		 * _item.get_mode()[14];// 套裝效果:地屬性增加 k6_1 = _item.get_mode()[15];//
		 * 套裝效果:寒冰耐性增加 k6_2 = _item.get_mode()[16];// 套裝效果:石化耐性增加 k6_3 =
		 * _item.get_mode()[17];// 套裝效果:睡眠耐性增加 k6_4 = _item.get_mode()[18];//
		 * 套裝效果:暗闇耐性增加 k6_5 = _item.get_mode()[19];// 套裝效果:暈眩耐性增加 k6_6 =
		 * _item.get_mode()[20];// 套裝效果:支撐耐性增加 aHpr = _item.get_mode()[21];//
		 * 套裝效果:回血量增加 aMpr = _item.get_mode()[22];// 套裝效果:回魔量增加 admg =
		 * _item.get_mode()[23];// 套裝效果:套裝增加物理傷害 drd = _item.get_mode()[24];//
		 * 套裝效果:套裝減免物理傷害 mdmg = _item.get_mode()[25];// 套裝效果:套裝增加魔法傷害 mdrd =
		 * _item.get_mode()[26];// 套裝效果:套裝減免魔法傷害 bdmg = _item.get_mode()[27];//
		 * 套裝效果:套裝增加弓的物理傷害 hit = _item.get_mode()[28];// 套裝效果:套裝增加近距離命中率 bhit =
		 * _item.get_mode()[29];// 套裝效果:套裝增加遠距離命中率 mcri =
		 * _item.get_mode()[30];// 套裝效果:套裝增加魔法爆擊率
		 * 
		 * }
		 */

		int pw_s1 = _item.get_addstr();
		int pw_s2 = _item.get_adddex();
		int pw_s3 = _item.get_addcon();
		int pw_s4 = _item.get_addwis();
		int pw_s5 = _item.get_addint();
		int pw_s6 = _item.get_addcha();

		int pw_sHp = _itemPower.get_addhp();
		int pw_sMp = _itemPower.get_addmp();
		int pw_sMr = _itemPower.getMr();
		int pw_sSp = _itemPower.getSp() + _itemInstance.getItemSp();
		int pw_sWr = _item.getWeightReduction(); // 防具負重顯示

		int pw_sDg = _item.getDmgModifierByArmor();
		int pw_sHi = _itemPower.getHitModifierByArmor();
		int pw_mHi = _item.getMagicHitModifierByArmor();

		int pw_bDg = _item.getBowDmgModifierByArmor();
		int pw_bHi = _item.getBowHitModifierByArmor();

		int pw_d4_1 = _item.get_defense_fire();
		int pw_d4_2 = _item.get_defense_water();
		int pw_d4_3 = _item.get_defense_wind();
		int pw_d4_4 = _item.get_defense_earth();

		// int pw_k6_1 = _item.get_regist_freeze();
		// int pw_k6_2 = _item.get_regist_stone();
		// int pw_k6_3 = _item.get_regist_sleep();
		// int pw_k6_4 = _item.get_regist_blind();
		// int pw_k6_5 = _item.get_regist_stun();
		// int pw_k6_6 = _item.get_regist_sustain();
		int pw_sHpr = _item.get_addhpr();
		int pw_sMpr = _itemPower.getMpr();
		int addexp = _item.getExpPoint();
		int pw_drd = _itemPower.getDamageReduction();
		int uhp = _item.get_up_hp_potion();// 增加藥水回復量%
		int uhp_num = _item.get_uhp_number();// 增加藥水回復指定量

		int pvpDmg = 0/* +greater()[7] */; // 增加PVP傷害
		int pvpDmgdrd = 0 /* +greater()[11] */; // 減免PVP傷害
		int CloseCri = 0; // 近距離爆擊率
		int BowCri = 0; // 遠距離爆擊率
		int FearLevel = 0; // 恐怖等級
		int ExtraAc = 0; // 額外防禦
		int antiDamageReduction = _item.getAntiDamageReduction();// 無視減免
		int einhasadConsumeReduce = _item.getEinhasadConsumeReduce(); // 殷海薩祝福消耗減少

		// 耐性
		int add_regist_Technology = _item.getRegistTechnology(); // 技術耐性
		int add_regist_Elf = _item.getRegistElf(); // 精靈耐性
		int add_regist_Dragon = _item.getRegistDragon(); // 龍屬耐性
		int add_regist_Horror = _item.getRegistHorror(); // 恐怖耐性
		int add_regist_All = _item.getRegistAll(); // 全部四大耐性
		// 命中
		int add_hit_Technology = _item.getHitTechnology(); // 技術命中
		int add_hit_Elf = _item.getHitElf(); // 精靈命中
		int add_hit_Dragon = _item.getHitDragon(); // 龍屬命中
		int add_hit_Horror = _item.getHitHorror(); // 恐怖命中
		int add_hit_All = _item.getHitAll(); // 全部四大命中
		int add_mofabaoji = _item.getmofabaoji(); // 全部四大命中
		// 裝備強化能力系統
		L1WilliamEnchantOrginal armorOrginal1 = null;
		L1WilliamEnchantOrginal armorOrginalOk1 = null;
		L1WilliamEnchantOrginal[] armorOrginalSize1 = EnchantOrginal
				.getInstance().getArmorList();
		for (int i = 0; i < armorOrginalSize1.length; i++) {
			armorOrginalOk1 = EnchantOrginal.getInstance().getTemplate(i);
			if (armorOrginalOk1.getItemId() == _itemInstance.getItemId()
					&& _itemInstance.getEnchantLevel() >= armorOrginalOk1
							.getLevel()) {
				armorOrginal1 = armorOrginalOk1;
				// break;
			}
		}
		if (armorOrginal1 != null) {
			ExtraAc += armorOrginal1.getAddAc(); // 額外防禦
			pw_s1 += armorOrginal1.getAddStr();
			pw_s2 += armorOrginal1.getAddDex();
			pw_s3 += armorOrginal1.getAddCon();
			pw_s5 += armorOrginal1.getAddInt();
			pw_s4 += armorOrginal1.getAddWis();
			pw_s6 += armorOrginal1.getAddCha();
			pw_sHp += armorOrginal1.getAddMaxHp();
			pw_sMp += armorOrginal1.getAddMaxMp();
			pw_sHpr += armorOrginal1.getAddHpr();
			pw_sMpr += armorOrginal1.getAddMpr();
			pw_sDg += armorOrginal1.getAddDmg();
			pw_sHi += armorOrginal1.getAddHit();
			pw_bDg += armorOrginal1.getAddBowDmg();
			pw_bHi += armorOrginal1.getAddBowHit();
			pw_drd += armorOrginal1.getReduction_dmg();
			pw_sMr += armorOrginal1.getAddMr();
			pw_sSp += armorOrginal1.getAddSp();
			pw_d4_1 += armorOrginal1.getAddFire();
			pw_d4_3 += armorOrginal1.getAddWind();
			pw_d4_4 += armorOrginal1.getAddEarth();
			pw_d4_2 += armorOrginal1.getAddWater();
			pvpDmg += armorOrginal1.getAddPvpDmg(); // 增加PVP傷害
			pvpDmgdrd += armorOrginal1.getAddPvpDmg_R(); // 減免PVP傷害
			CloseCri += armorOrginal1.getAddCloseCri(); // 近距離爆擊率
			BowCri += armorOrginal1.getAddBowCri(); // 遠距離爆擊率
			mcri += armorOrginal1.getAddMagicCri(); // 魔法爆擊率

			// FearLevel += armorOrginal1.getAddFearLevel(); // 恐怖等級
			// 耐性
			add_regist_Technology += armorOrginal1.getAddRegistTechnology(); // 技術耐性
			add_regist_Elf += armorOrginal1.getAddRegistElf(); // 精靈耐性
			add_regist_Dragon += armorOrginal1.getAddRegistDragon(); // 龍屬耐性
			add_regist_Horror += armorOrginal1.getAddRegistHorror(); // 恐怖耐性
			add_regist_All += armorOrginal1.getAddRegistAll(); // 全部四大耐性
			// 命中
			add_hit_Technology += armorOrginal1.getAddHitTechnology(); // 技術命中
			add_hit_Elf += armorOrginal1.getAddHitElf(); // 精靈命中
			add_hit_Dragon += armorOrginal1.getAddHitDragon(); // 龍屬命中
			add_hit_Horror += armorOrginal1.getAddHitHorror(); // 恐怖命中
			add_hit_All += armorOrginal1.getAddHitAll(); // 全部四大命中
			einhasadConsumeReduce += armorOrginal1
					.getAddEinhasadConsumeReduce(); // 殷海薩祝福消耗減少
		}
		// 裝備強化能力系統 end

		int add_hit = pw_sHi + hit;// 近距離命中率
		if (add_hit != 0) {
			_os.writeC(48); // 近戰命中
			_os.writeC(add_hit);

		} else if (_item.getItemId() == 21309) {
			int value = (_itemInstance.getEnchantLevel() - 5) + 1;
			_os.writeC(48); // 近戰命中
			_os.writeC(value);
			// 激怒手套
			// 【+5】近距離命中+1
			// 【+6】近距離命中+2
			// 【+7】近距離命中+3
			// 【+8】近距離命中+4
			// 【+9】近距離命中+5
			// 【+10】近距離命中+6
			// 【+11】近距離命中+7
		}

		int add_sdg = pw_sDg + /* greater()[3] + */admg
				+ _itemInstance.getItemAttack();// 近距離傷害
		if (add_sdg != 0) {
			_os.writeC(47);
			_os.writeC(add_sdg);
		}

		int add_bowhit = pw_bHi + bhit;// 遠距離命中率
		if (add_bowhit != 0) {
			_os.writeC(24);
			_os.writeC(add_bowhit);
		}

		int add_bdg = pw_bDg + /* greater()[4] + */bdmg
				+ _itemInstance.getItemBowAttack();// 遠距離傷害
		if (add_bdg != 0) {
			_os.writeC(35);
			_os.writeC(add_bdg);
		}

		if (mdmg != 0) {// 魔法傷害
			_os.writeC(39);
			_os.writeS("魔法傷害 +" + mdmg);
		}

		if (mdrd != 0) {
			_os.writeC(39);
			_os.writeS("魔法傷害減免 +" + mdrd);
		}

		int bit = 0;
		bit |= (_item.isUseRoyal() ? 1 : 0);
		bit |= (_item.isUseKnight() ? 2 : 0);
		bit |= (_item.isUseElf() ? 4 : 0);
		bit |= (_item.isUseMage() ? 8 : 0);
		bit |= (_item.isUseDarkelf() ? 16 : 0);
		bit |= (_item.isUseDragonknight() ? 32 : 0);
		bit |= (_item.isUseIllusionist() ? 64 : 0);
		bit |= (_item.isUseWarrior() ? 128 : 0);
		_os.writeC(7);
		_os.writeC(bit);

		/*
		 * int safeenchant = _item.get_safeenchant(); if (safeenchant >= 0) {
		 * _os.writeC(39); _os.writeS("安定值: " + _item.get_safeenchant()); }
		 */

		int addstr = pw_s1 + s6_1 + _itemInstance.getItemStr();// 力量
		int adddex = pw_s2 + s6_2 + _itemInstance.getItemDex();// 敏捷
		int addcon = pw_s3 + s6_3 + _itemInstance.getItemCon();// 體質
		int addwis = pw_s4 + s6_4 + _itemInstance.getItemWis();
		;// 精神.
		int addint = pw_s5 + s6_5 + _itemInstance.getItemInt();// 智力
		int addcha = pw_s6 + s6_6 + _itemInstance.getItemCha();
		;// 魅力

		if (addstr == 1 && adddex == 1 && addcon == 1 && addwis == 1
				&& addint == 1 && addcha == 1) {
			_os.writeC(39);
			_os.writeS("全能力值 +1");
		} else {
			if (addstr != 0) {
				_os.writeC(8);
				_os.writeC(addstr);
			}

			if (adddex != 0) {
				_os.writeC(9);
				_os.writeC(adddex);
			}

			if (addcon != 0) {
				_os.writeC(10);
				_os.writeC(addcon);
			}

			if (addwis != 0) {
				_os.writeC(11);
				_os.writeC(addwis);
			}

			if (addint != 0) {
				_os.writeC(12);
				_os.writeC(addint);
			}

			if (addcha != 0) {
				_os.writeC(13);
				_os.writeC(addcha);
			}
		}

		int addhp = pw_sHp + aH_1 /* + greater()[1] */;// 血量上限
		if (addhp != 0) {
			_os.writeC(14);
			_os.writeH(addhp);
		}

		int addmp = pw_sMp + aM_1 /* + greater()[0] */;// 魔量上限;
		if (addmp != 0) {
			_os.writeC(0x20);
			_os.writeH(addmp);
		}

		int addhpr = pw_sHpr + aHpr; // 體力回復量
		if (addhpr != 0) {
			_os.writeC(37);
			_os.writeC(addhpr);
		}

		int addmpr = pw_sMpr + aMpr; // 魔力回復量
		if (addmpr != 0) {
			_os.writeC(38);
			_os.writeC(addmpr);
		}

		// int freeze = pw_k6_1 + k6_1; // 冰凍耐性
		// if (freeze != 0) {
		// _os.writeC(33);
		// _os.writeC(1);
		// _os.writeC(freeze);
		// }
		//
		// int stone = pw_k6_2 + k6_2; // 石化耐性
		// if (stone != 0) {
		// _os.writeC(33);
		// _os.writeC(2);
		// _os.writeC(stone);
		// }
		//
		// int sleep = pw_k6_3 + k6_3; // 睡眠耐性
		// if (sleep != 0) {
		// _os.writeC(33);
		// _os.writeC(3);
		// _os.writeC(sleep);
		// }
		//
		// int blind = pw_k6_4 + k6_4; // 暗黑耐性
		// if (blind != 0) {
		// _os.writeC(33);
		// _os.writeC(4);
		// _os.writeC(blind);
		// }
		//
		// int stun = pw_k6_5 + k6_5; // 昏迷耐性
		// if (stun != 0) {
		// _os.writeC(33);
		// _os.writeC(5);
		// _os.writeC(stun);
		// }
		//
		// int sustain = pw_k6_6 + k6_6; // 支撐耐性
		// if (sustain != 0) {
		// _os.writeC(33);
		// _os.writeC(6);
		// _os.writeC(sustain);
		// }

		int addmr = pw_sMr + aMR_1 /* + greater()[10] */; // 魔防

		if (addmr != 0) {
			_os.writeC(15);
			_os.writeH(addmr);
		}

		int addsp = pw_sSp + aSP_1 /* + greater()[9] */; // 魔攻
		if (addsp != 0) {
			_os.writeC(17);
			_os.writeC(addsp);
		}

		if (pw_sWr > 0) { // 防具負重
			// _os.writeC(0x5a); // 90 增加負重 +X
			// _os.writeH(pw_sWr);
			_os.writeC(68); // 68 負重增加率(%)
			_os.writeC(pw_sWr);
		}

		boolean haste = _item.isHasteItem();

		if (aSS_1 == 1) {
			haste = true;
		}
		if (haste) {
			_os.writeC(18);
		}

		int fire = pw_d4_1 + d4_1;
		if (fire != 0) {
			_os.writeC(27);
			_os.writeC(fire);
		}

		int water = pw_d4_2 + d4_2;
		if (water != 0) {
			_os.writeC(28);
			_os.writeC(water);
		}

		int wind = pw_d4_3 + d4_3;
		if (wind != 0) {
			_os.writeC(29);
			_os.writeC(wind);
		}

		int earth = pw_d4_4 + d4_4;
		if (earth != 0) {
			_os.writeC(30);
			_os.writeC(earth);
		}

		if (addexp != 0) { // 經驗加成 2017/0425
			if (addexp <= 120) {
				_os.writeC(36);
				_os.writeC(addexp);
			} else {
				this._os.writeC(39);
				this._os.writeS("$6134 " + addexp + "%");
			}
		}

		// int alldrd = pw_drd + drd /* + greater()[5] */;
		int alldrd = pw_drd + drd + _itemInstance.getItemReductionDmg(); // 傷害減免
		int d = 0;
		if (alldrd != 0) {
			_os.writeC(63);
			if (_item.getItemId() >= 21200 && _item.getItemId() <= 21203) {

				switch (_itemInstance.getEnchantLevel()) {
				case 7:
					d = 1;
					break;
				case 8:
					d = 2;
					break;
				case 9:
					d = 3;
					break;
				default:
					break;
				}

			}
			_os.writeC(alldrd + d);
		}

		int allmHi = pw_mHi /* + greater()[6] */;
		if (allmHi != 0) {// 魔法命中
			// _os.writeC(39);
			// _os.writeS("魔法命中 +" + allmHi);
			_os.writeC(40);
			_os.writeC(allmHi);
		}

		if (mcri != 0) {
			// _os.writeC(39);
			// _os.writeS("魔法爆擊率 +" + mcri);
			_os.writeC(50);
			_os.writeH(mcri);
		}

		// 56 額外防禦
		if (ExtraAc != 0) {
			_os.writeC(56);
			_os.writeC(ExtraAc);
		}

		StringBuilder name = new StringBuilder();
		int adduhp = uhp /* + greater()[2] */;
		if (adduhp != 0) {// 增加藥水回復量%
			name.append("藥水回復量 +" + adduhp + "%");
			if (uhp_num != 0) {// 增加藥水回復指定量
				name.append("+" + uhp_num);
			}
			_os.writeC(39);
			_os.writeS(name.toString());
		}

		switch (_item.getItemId()) {

		case 21384:
		case 21385:
		case 21386:
			pvpDmg = 1;
			break;
		case 21387:
		case 21388:
		case 21389:
			pvpDmg = 2;
			break;
		case 21390:
		case 21391:
		case 21392:
			pvpDmg = 4;
			break;
		case 21393:
		case 21394:
		case 21395:
			pvpDmg = 5;
			break;
		default:
			break;
		}
		int addpvpdmg = pvpDmg + _item.getPvpDmg(); // 增加PVP傷害
		if (addpvpdmg != 0) {
			// _os.writeC(39);
			// _os.writeS("\\f2PVP 額外傷害 +" + addpvpdmg);
			_os.writeC(59);
			_os.writeC(addpvpdmg);
		}

		switch (_item.getItemId()) {
		case 330012:
		case 330013:
		case 330014:
			pvpDmgdrd = 2;
			break;
		case 330015:
		case 330016:
		case 330017:
			pvpDmgdrd = 5;
			break;
		default:
			break;
		}
		int addpvpdmg_r = pvpDmgdrd + _item.getPvpDmg_R(); // 減免PVP傷害
		if (addpvpdmg_r != 0) {
			// _os.writeC(39);
			// _os.writeS("\\f2PVP 傷害減免 +" + pvpDmgdrd);
			_os.writeC(60);
			_os.writeC(addpvpdmg_r);
		}

		if ((_item.getclassname().equalsIgnoreCase("Venom_Resist"))
				|| (_item.getclassname()
						.equalsIgnoreCase("ElitePlateMail_Antharas"))) {
			// _os.writeC(39);
			// _os.writeS("防護中毒");
			_os.writeC(57); // 57 毒耐性
			_os.writeD(19128);
		}

		// 98 恐怖等級提升
		if (FearLevel != 0) {
			_os.writeC(98);
			_os.writeD(24131); // 恐怖命中 +%d---- 26607 恐怖等級 +3
			_os.writeH(FearLevel);
		}

		// 99 遠距離爆擊
		if (BowCri != 0) {
			_os.writeC(99);
			_os.writeC(BowCri);
		}

		// 100 近距離爆擊
		if (CloseCri != 0) {
			_os.writeC(100);
			_os.writeC(CloseCri);
		}

		final L1ItemPower_bless bless = this._itemInstance.get_power_bless();
		if (bless != null) {
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder stringBuilder1 = new StringBuilder();

			if (bless.get_hole_count() > 0) {
				this._os.writeC(0x27);
				for (int i = 0; i < bless.get_hole_count(); i++) {
					switch (i) {
					case 0:
						name.append(set_hole_name(bless.get_hole_1()));
						break;
					case 1:
						name.append(set_hole_name(bless.get_hole_2()));
						break;
					case 2:
						name.append(set_hole_name(bless.get_hole_3()));
						break;
					case 3:
						name.append(set_hole_name(bless.get_hole_4()));
						break;
					case 4:
						name.append(set_hole_name(bless.get_hole_5()));
						break;
					}
				}
				this._os.writeS(name.toString());
			}
			if ((bless.get_hole_str() != 0) || (bless.get_hole_dex() != 0)
					|| (bless.get_hole_int() != 0)
					|| (bless.get_hole_dmg() != 0)
					|| (bless.get_hole_bowdmg() != 0)
					|| (bless.get_hole_mcdmg() != 0)) {
				if (bless.get_hole_str() >= 0) {
					stringBuilder.append("力+" + bless.get_hole_str() + " ");
				} else {
					stringBuilder.append("力" + bless.get_hole_str() + " ");
				}

				if (bless.get_hole_dex() >= 0) {
					stringBuilder.append("敏+" + bless.get_hole_dex() + " ");
				} else {
					stringBuilder.append("敏" + bless.get_hole_dex() + " ");
				}

				if (bless.get_hole_int() >= 0) {
					stringBuilder.append("智+" + bless.get_hole_int() + " ");
				} else {
					stringBuilder.append("智" + bless.get_hole_int() + " ");
				}
				if (bless.get_hole_dmg() >= 0) {
					stringBuilder1.append("近戰+" + bless.get_hole_dmg() + " ");
				} else {
					stringBuilder1.append("近戰" + bless.get_hole_dmg() + " ");
				}
				if (bless.get_hole_bowdmg() >= 0) {
					stringBuilder1
							.append("遠弓+" + bless.get_hole_bowdmg() + " ");
				} else {
					stringBuilder1.append("遠弓" + bless.get_hole_bowdmg() + " ");
				}
				if (bless.get_hole_mcdmg() >= 0) {
					stringBuilder1.append("魔法+" + bless.get_hole_mcdmg() + " ");
				} else {
					stringBuilder1.append("魔法" + bless.get_hole_mcdmg() + " ");
				}
				// this._os.writeC(39);
				// this._os.writeS("祝福強化:");
				this._os.writeC(39);
				this._os.writeS(stringBuilder.toString().trim());
				this._os.writeC(39);
				this._os.writeS(stringBuilder1.toString().trim());
			}
		}

		// TODO PVP系統 防具減少傷害
		/*
		 * if (ConfigOther.PVP_ARMOR && _itemInstance.getItemId() >= 20001 &&
		 * _itemInstance.getItemId() <= 5000000 &&
		 * _itemInstance.getEnchantLevel() >= ConfigOther.PVP_plus2) {
		 * _os.writeC(60); _os.writeC(_itemInstance.getEnchantLevel() -
		 * ConfigOther.PVP_plus2 + 1); }* / if (pvpDmg != 0 ||
		 * _itemInstance.getItemReductionDmg() !=0) { // _os.writeC(39); //
		 * _os.writeS("PVP傷害 +" + pvpDmg); _os.writeC(60); _os.writeC(pvpDmg +
		 * _itemInstance.getItemReductionDmg()); }
		 */

		// 97 無視減免
		if (antiDamageReduction != 0) {
			_os.writeC(97);
			_os.writeC(antiDamageReduction);
		}

		// 116 殷海薩祝福消耗減少
		if (einhasadConsumeReduce != 0) {
			_os.writeC(116); // 祝福消耗率%
			_os.writeH(einhasadConsumeReduce);
		}

		// 耐性
		if (add_regist_Technology != 0) { // 技術耐性
			_os.writeC(117);
			_os.writeC(add_regist_Technology);
		}
		if (add_regist_Elf != 0) { // 精靈耐性
			_os.writeC(118);
			_os.writeC(add_regist_Elf);
		}
		if (add_regist_Dragon != 0) { // 龍屬耐性
			_os.writeC(119);
			_os.writeC(add_regist_Dragon);
		}
		if (add_regist_Horror != 0) { // 恐怖耐性
			_os.writeC(120);
			_os.writeC(add_regist_Horror);
		}
		if (add_regist_All != 0) { // 全部四大耐性
			_os.writeC(121);
			_os.writeC(add_regist_All);
		}
		// 耐性end

		// 命中
		if (add_hit_Technology != 0) { // 技術命中
			_os.writeC(122);
			_os.writeC(add_hit_Technology);
		}
		if (add_hit_Elf != 0) { // 精靈命中
			_os.writeC(123);
			_os.writeC(add_hit_Elf);
		}
		if (add_hit_Dragon != 0) { // 龍屬命中
			_os.writeC(124);
			_os.writeC(add_hit_Dragon);
		}
		if (add_hit_Horror != 0) { // 恐怖命中
			_os.writeC(125);
			_os.writeC(add_hit_Horror);
		}
		if (add_hit_All != 0) { // 全部四大命中
			_os.writeC(126);
			_os.writeC(add_hit_All);
		}
		if ( add_mofabaoji != 0) { // 全部四大命中
			_os.writeC(127);
			_os.writeS("魔法暴擊率 +"+add_mofabaoji);
		}
		// 命中end

		ItemAbility(); // 強化擴充能力

		checkArmorSet(); // 套裝能力顯示

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		String[] byarmor = this._item.getclassname().split(" "); // SRC0711
		if (byarmor[0].equals("SkillByArmor")) {
			int bonus2 = this._itemInstance.getEnchantLevel()
					- this._item.get_safeenchant();
			if (bonus2 <= 0)
				bonus2 = 0;
			if (bonus2 >= 0) {
				this._os.writeC(39);
				this._os.writeS("目前+" + this._itemInstance.getEnchantLevel()
						+ " "
						+ (bonus2 + Integer.valueOf(byarmor[1]).intValue())
						+ "%機率 發動" + byarmor[4]);
				this._os.writeC(39);
				this._os.writeS("傷害 " + Integer.valueOf(byarmor[3]));
			}
		}

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
		return _os;
	}

	// TODO 飾品類
	/**
	 * 飾品類
	 * 
	 * @return
	 */
	private BinaryOutputStream accessories() {
		_os.writeC(19);
		int ac = _item.get_ac()/* + greater()[8] */;
		if (ac < 0) {
			ac = Math.abs(ac);
		}
		_os.writeC(ac);

		_os.writeC(_item.getMaterial());
		_os.writeC(-1);
		_os.writeD(_itemInstance.getWeight());

		if (_item.get_greater() == 0) {// 0:耐性(耳環/項鏈)
			_os.writeC(0x43); // 特性：耐性
			_os.writeC(0x2b); // 特性：耐性
		} else if (_item.get_greater() == 1) {// 1:熱情(戒指)
			_os.writeC(0x43); // 特性：熱情
			_os.writeC(0x2c); // 特性：熱情
		} else if (_item.get_greater() == 2) {// 2:意志(皮帶)
			_os.writeC(0x43); // 特性：意志
			_os.writeC(0x2d); // 特性：意志
		}

		int s6_1 = 0;
		int s6_2 = 0;
		int s6_3 = 0;
		int s6_4 = 0;
		int s6_5 = 0;
		int s6_6 = 0;
		int aH_1 = 0;
		int aM_1 = 0;
		int aMR_1 = 0;
		int aSP_1 = 0;
		int aSS_1 = 0;
		int d4_1 = 0;
		int d4_2 = 0;
		int d4_3 = 0;
		int d4_4 = 0;
		// int k6_1 = 0;
		// int k6_2 = 0;
		// int k6_3 = 0;
		// int k6_4 = 0;
		// int k6_5 = 0;
		// int k6_6 = 0;
		int aHpr = 0;
		int aMpr = 0;
		int admg = 0;
		int drd = 0;
		int mdmg = 0;
		int mdrd = 0;
		int bdmg = 0;
		int hit = 0;
		int bhit = 0;
		int mcri = 0;

		/*
		 * if (_itemInstance.isMatch()) {// 套裝效果 s6_1 = _item.get_mode()[0];//
		 * 套裝效果:力量增加 s6_2 = _item.get_mode()[1];// 套裝效果:敏捷增加 s6_3 =
		 * _item.get_mode()[2];// 套裝效果:體質增加 s6_4 = _item.get_mode()[3];//
		 * 套裝效果:精神增加 s6_5 = _item.get_mode()[4];// 套裝效果:智力增加 s6_6 =
		 * _item.get_mode()[5];// 套裝效果:魅力增加 aH_1 = _item.get_mode()[6];//
		 * 套裝效果:HP增加 aM_1 = _item.get_mode()[7];// 套裝效果:MP增加 aMR_1 =
		 * _item.get_mode()[8];// 套裝效果:抗魔增加 aSP_1 = _item.get_mode()[9];//
		 * SP(魔攻) XXX aSS_1 = _item.get_mode()[10];// 加速效果 XXX d4_1 =
		 * _item.get_mode()[11];// 套裝效果:火屬性增加 d4_2 = _item.get_mode()[12];//
		 * 套裝效果:水屬性增加 d4_3 = _item.get_mode()[13];// 套裝效果:風屬性增加 d4_4 =
		 * _item.get_mode()[14];// 套裝效果:地屬性增加 k6_1 = _item.get_mode()[15];//
		 * 套裝效果:寒冰耐性增加 k6_2 = _item.get_mode()[16];// 套裝效果:石化耐性增加 k6_3 =
		 * _item.get_mode()[17];// 套裝效果:睡眠耐性增加 k6_4 = _item.get_mode()[18];//
		 * 套裝效果:暗闇耐性增加 k6_5 = _item.get_mode()[19];// 套裝效果:暈眩耐性增加 k6_6 =
		 * _item.get_mode()[20];// 套裝效果:支撐耐性增加 aHpr = _item.get_mode()[21];//
		 * 套裝效果:回血量增加 aMpr = _item.get_mode()[22];// 套裝效果:回魔量增加 admg =
		 * _item.get_mode()[23];// 套裝效果:套裝增加物理傷害 drd = _item.get_mode()[24];//
		 * 套裝效果:套裝減免物理傷害 mdmg = _item.get_mode()[25];// 套裝效果:套裝增加魔法傷害 mdrd =
		 * _item.get_mode()[26];// 套裝效果:套裝減免魔法傷害 bdmg = _item.get_mode()[27];//
		 * 套裝效果:套裝增加弓的物理傷害 hit = _item.get_mode()[28];// 套裝效果:套裝增加近距離命中率 bhit =
		 * _item.get_mode()[29];// 套裝效果:套裝增加遠距離命中率 mcri =
		 * _item.get_mode()[30];// 套裝效果:套裝增加魔法爆擊率 }
		 */

		int pw_s1 = _item.get_addstr() + _itemInstance.getItemStr();
		int pw_s2 = _item.get_adddex() + _itemInstance.getItemDex();
		int pw_s3 = _item.get_addcon() + _itemInstance.getItemCon();
		int pw_s4 = _item.get_addwis() + _itemInstance.getItemWis();
		int pw_s5 = _item.get_addint() + _itemInstance.getItemInt();
		int pw_s6 = _item.get_addcha() + _itemInstance.getItemCha();

		int pw_sHp = _itemPower.get_addhp();
		int pw_sMp = _itemPower.get_addmp();
		int pw_sMr = _itemPower.getMr();
		int pw_sSp = _itemPower.getSp() + _itemInstance.getItemSp();
		int pw_sWr = _item.getWeightReduction(); // 飾品負重顯示

		int pw_sDg = _item.getDmgModifierByArmor();
		int pw_sHi = _itemPower.getHitModifierByArmor();
		int pw_mHi = _item.getMagicHitModifierByArmor();

		int pw_bDg = _item.getBowDmgModifierByArmor();
		int pw_bHi = _item.getBowHitModifierByArmor();

		int pw_d4_1 = _item.get_defense_fire();
		int pw_d4_2 = _item.get_defense_water();
		int pw_d4_3 = _item.get_defense_wind();
		int pw_d4_4 = _item.get_defense_earth();

		// int pw_k6_1 = _item.get_regist_freeze();
		// int pw_k6_2 = _item.get_regist_stone();
		// int pw_k6_3 = _item.get_regist_sleep();
		// int pw_k6_4 = _item.get_regist_blind();
		// int pw_k6_5 = _item.get_regist_stun();
		// int pw_k6_6 = _item.get_regist_sustain();
		int pw_sHpr = _item.get_addhpr();
		int pw_sMpr = _itemPower.getMpr();
		int addexp = _item.getExpPoint();
		int pw_drd = _itemPower.getDamageReduction();
		int uhp = _itemPower.getUhp();// 增加藥水回復量%
		int uhp_num = _itemPower.getUhp_NUM();// 增加藥水回復指定量
		int addpvpdmg = _item.getPvpDmg(); // 增加PVP傷害
		int addpvpdmg_r = _item.getPvpDmg_R(); // 減免PVP傷害
		int allmHi = pw_mHi/* + greater()[6] */; // 魔法命中
		int ExtraAc = 0; // 額外防禦
		int antiDamageReduction = _item.getAntiDamageReduction();// 無視減免
		int einhasadConsumeReduce = _item.getEinhasadConsumeReduce(); // 殷海薩祝福消耗減少

		// 耐性
		int add_regist_Technology = _item.getRegistTechnology(); // 技術耐性
		int add_regist_Elf = _item.getRegistElf(); // 精靈耐性
		int add_regist_Dragon = _item.getRegistDragon(); // 龍屬耐性
		int add_regist_Horror = _item.getRegistHorror(); // 恐怖耐性
		int add_regist_All = _item.getRegistAll(); // 全部四大耐性
		// 命中
		int add_hit_Technology = _item.getHitTechnology(); // 技術命中
		int add_hit_Elf = _item.getHitElf(); // 精靈命中
		int add_hit_Dragon = _item.getHitDragon(); // 龍屬命中
		int add_hit_Horror = _item.getHitHorror(); // 恐怖命中
		int add_hit_All = _item.getHitAll(); // 全部四大命中
		int add_mofabaoji = _item.getmofabaoji(); // 全部四大命中
		int add_hit = pw_sHi + hit;// 近距離命中率
		int add_sdg = pw_sDg/* + greater()[3] */+ admg
				+ _itemInstance.getItemAttack();// 近距離傷害
		int add_bowhit = pw_bHi + bhit;// 遠距離命中率
		int add_bdg = pw_bDg/* + greater()[4] */+ bdmg
				+ _itemInstance.getItemBowAttack();// 遠距離傷害

		// 飾品加成能力系統
		L1WilliamEnchantAccessory accessoryOrginal = null;
		L1WilliamEnchantAccessory accessoryOrginalOk = null;
		final L1WilliamEnchantAccessory[] accessoryOrginalSize = EnchantAccessory
				.getInstance().getArmorList();
		for (int i = 0; i < accessoryOrginalSize.length; i++) {
			accessoryOrginalOk = EnchantAccessory.getInstance().getTemplate(i);
			if (accessoryOrginalOk.getType() == _itemInstance.getItem()
					.getType()) {
				if (accessoryOrginalOk.getType() == _itemInstance.getItem()
						.getType()
						&& accessoryOrginalOk.getStrength() == _itemInstance
								.getItem().get_greater()
						&& accessoryOrginalOk.getLevel() == _itemInstance
								.getEnchantLevel()) {
					accessoryOrginal = accessoryOrginalOk;
					break;
				}
			}
		}
		if (accessoryOrginal != null) {
			pw_s1 += accessoryOrginal.getAddStr();
			pw_s2 += accessoryOrginal.getAddDex();
			pw_s3 += accessoryOrginal.getAddCon();
			pw_s5 += accessoryOrginal.getAddInt();
			pw_s4 += accessoryOrginal.getAddWis();
			pw_s6 += accessoryOrginal.getAddCha();
			ExtraAc += accessoryOrginal.getAddAc();
			pw_sHp += accessoryOrginal.getAddMaxHp();
			pw_sMp += accessoryOrginal.getAddMaxMp();
			pw_sHpr += accessoryOrginal.getAddHpr();
			pw_sMpr += accessoryOrginal.getAddMpr();

			add_sdg += accessoryOrginal.getAddDmg();
			add_hit += accessoryOrginal.getAddHit();
			add_bdg += accessoryOrginal.getAddBowDmg();
			add_bowhit += accessoryOrginal.getAddBowHit();
			pw_drd += accessoryOrginal.getAddDmgReduction();
			pw_sMr += accessoryOrginal.getAddMr();
			pw_sSp += accessoryOrginal.getAddSp();
			addpvpdmg += accessoryOrginal.getPVPdmg(); // PVP傷害點數
			addpvpdmg_r += accessoryOrginal.getPVPdmgReduction(); // PVP傷害減免
			uhp += accessoryOrginal.getPotion_Heal(); // 增加治癒藥水恢復量(%)
			uhp_num += accessoryOrginal.getPotion_Healling(); // 增加治癒藥水恢復量(指定量)
			allmHi += accessoryOrginal.getAddMagicHit();
			// RegistFear += accessoryOrginal.getAddRegistFear();
			// 耐性
			add_regist_Technology += accessoryOrginal.getAddRegistTechnology(); // 技術耐性
			add_regist_Elf += accessoryOrginal.getAddRegistElf(); // 精靈耐性
			add_regist_Dragon += accessoryOrginal.getAddRegistDragon(); // 龍屬耐性
			add_regist_Horror += accessoryOrginal.getAddRegistHorror(); // 恐怖耐性
			add_regist_All += accessoryOrginal.getAddRegistAll(); // 全部四大耐性
			// 命中
			add_hit_Technology += accessoryOrginal.getAddHitTechnology(); // 技術命中
			add_hit_Elf += accessoryOrginal.getAddHitElf(); // 精靈命中
			add_hit_Dragon += accessoryOrginal.getAddHitDragon(); // 龍屬命中
			add_hit_Horror += accessoryOrginal.getAddHitHorror(); // 恐怖命中
			add_hit_All += accessoryOrginal.getAddHitAll(); // 全部四大命中
			einhasadConsumeReduce += accessoryOrginal
					.getAddEinhasadConsumeReduce(); // 殷海薩祝福消耗減少
		}
		// 飾品加成能力系統end

		// 近距離命中率
		if (add_hit != 0) {
			_os.writeC(48);
			_os.writeC(add_hit);
		}

		// 古文字顯示
		if (_itemInstance.get_power_name() != null
				&& _itemInstance.get_power_name().get_power_id() > 0) {
			this._os.writeC(0x27);
			this._os.writeS(_itemInstance.get_power_name().get_power_name());
		}
		final L1WeaponSkill weaponSkill = WeaponSkillTable.get().getTemplate(
				_itemInstance.getItemId());

		if (weaponSkill != null) {
			if (weaponSkill.getProbability() > 0) {
				this._os.writeC(0x27);
				this._os.writeS("魔法武器發動率: + "
						+ (weaponSkill.getProbability() + _itemInstance
								.getEnchantLevel()) + "%");
			}
			if (_itemInstance.getEnchantLevel() > 0) {
				this._os.writeC(0x27);
				this._os.writeS("武器魔法傷害: + " + _itemInstance.getEnchantLevel()
						+ "%");
			}
		}

		this._os.writeC(0x27);
		// this._os.writeS("安定值: " + _item.get_safeenchant());
		this._os.writeS("安定值:"
				+ ((_item.get_safeenchant() < 0) ? "不可強化" : _item
						.get_safeenchant()));

		/*
		 * if (FeatureItemSet.POWER_START) { // 附加屬性 final int attrEnchantLevel
		 * = _itemInstance.getAttrEnchantLevel(); if (attrEnchantLevel > 0) {
		 * int type = 0; switch (_itemInstance.getAttrEnchantKind()) { case 1:
		 * // 地 type = 0; break;
		 * 
		 * case 2: // 火 type = 1; break;
		 * 
		 * case 4: // 水 type = 2; break;
		 * 
		 * case 8: // 風 type = 3; break;
		 * 
		 * case 16: // 光 type = 4; break;
		 * 
		 * case 32: // 暗 type = 5; break;
		 * 
		 * case 64: // 聖 type = 6; break;
		 * 
		 * case 128: // 邪 type = 7; break; } this._os.writeC(0x27);
		 * this._os.writeS("魔化: " +
		 * _attrEnchantString[type][attrEnchantLevel-1]); } }
		 */

		// 攻擊成功
		// int addHitModifier = this._item.getHitModifier() + pw_sHi;
		// if (addHitModifier != 0 || _item.getInfluenceHitAndDmg() != 0) {
		// this._os.writeC(0x05);
		// this._os.writeC(addHitModifier + _itemInstance.getEnchantLevel()
		// * _item.getInfluenceHitAndDmg());
		// }

		// 追加打擊
		// int addDmgModifier = this._item.getDmgModifier() + pw_sDg;
		// if (addDmgModifier != 0 || _item.getInfluenceHitAndDmg() != 0) {
		// this._os.writeC(0x06);
		// this._os.writeC(addDmgModifier + _itemInstance.getEnchantLevel()
		// * _item.getInfluenceHitAndDmg());
		// }

		// 近距離傷害
		if (add_sdg != 0) {
			_os.writeC(47);
			_os.writeC(add_sdg);
		}

		// 遠距離命中率
		if (add_bowhit != 0) {
			_os.writeC(24);
			_os.writeC(add_bowhit);
		}

		// 遠距離傷害
		if (add_bdg != 0) {
			_os.writeC(35);
			_os.writeC(add_bdg);
		}

		if (mdmg != 0) {// 魔法傷害
			_os.writeC(39);
			_os.writeS("魔法傷害 +" + mdmg);
		}

		if (mdrd != 0) {
			_os.writeC(39);
			_os.writeS("魔法傷害減免 +" + mdrd);
		}

		int bit = 0;
		bit |= (_item.isUseRoyal() ? 1 : 0);
		bit |= (_item.isUseKnight() ? 2 : 0);
		bit |= (_item.isUseElf() ? 4 : 0);
		bit |= (_item.isUseMage() ? 8 : 0);
		bit |= (_item.isUseDarkelf() ? 16 : 0);
		bit |= (_item.isUseDragonknight() ? 32 : 0);
		bit |= (_item.isUseIllusionist() ? 64 : 0);
		bit |= (_item.isUseWarrior() ? 128 : 0);
		_os.writeC(7);
		_os.writeC(bit);

		int addstr = pw_s1 + s6_1;
		if (addstr != 0) {
			_os.writeC(8);
			_os.writeC(addstr);
		}

		int adddex = pw_s2 + s6_2;
		if (adddex != 0) {
			_os.writeC(9);
			_os.writeC(adddex);
		}

		int addcon = pw_s3 + s6_3;
		if (addcon != 0) {
			_os.writeC(10);
			_os.writeC(addcon);
		}

		int addwis = pw_s4 + s6_4;
		if (addwis != 0) {
			_os.writeC(11);
			_os.writeC(addwis);
		}

		int addint = pw_s5 + s6_5;
		if (addint != 0) {
			_os.writeC(12);
			_os.writeC(addint);
		}

		int addcha = pw_s6 + s6_6;
		if (addcha != 0) {
			_os.writeC(13);
			_os.writeC(addcha);
		}

		int addhp = pw_sHp + aH_1/* + greater()[1] */;// 血量上限
		if (addhp != 0) {
			_os.writeC(14);
			_os.writeH(addhp);
		}

		int addmp = pw_sMp + aM_1/* + greater()[0] */;// 魔量上限;
		if (addmp != 0) {
			_os.writeC(0x20);
			_os.writeH(addmp);
		}

		int addhpr = pw_sHpr + aHpr; // 體力回復量
		if (addhpr != 0) {
			_os.writeC(37);
			_os.writeC(addhpr);
		}

		int addmpr = pw_sMpr + aMpr; // 魔力回復量
		if (addmpr != 0) {
			_os.writeC(38);
			_os.writeC(addmpr);
		}

		// 56 額外防禦
		if (ExtraAc != 0) {
			_os.writeC(56);
			_os.writeC(ExtraAc);
		}

		// int freeze = pw_k6_1 + k6_1; // 冰凍耐性
		// if (freeze != 0) {
		// _os.writeC(33);
		// _os.writeC(1);
		// _os.writeC(freeze);
		// }
		//
		// int stone = pw_k6_2 + k6_2; // 石化耐性
		// if (stone != 0) {
		// _os.writeC(33);
		// _os.writeC(2);
		// _os.writeC(stone);
		// }
		//
		// int sleep = pw_k6_3 + k6_3; // 睡眠耐性
		// if (sleep != 0) {
		// _os.writeC(33);
		// _os.writeC(3);
		// _os.writeC(sleep);
		// }
		//
		// int blind = pw_k6_4 + k6_4; // 暗黑耐性
		// if (blind != 0) {
		// _os.writeC(33);
		// _os.writeC(4);
		// _os.writeC(blind);
		// }
		//
		// int stun = pw_k6_5 + k6_5; // 昏迷耐性
		// if (stun != 0) {
		// _os.writeC(33);
		// _os.writeC(5);
		// _os.writeC(stun);
		// }
		//
		// int sustain = pw_k6_6 + k6_6; // 支撐耐性
		// if (sustain != 0) {
		// _os.writeC(33);
		// _os.writeC(6);
		// _os.writeC(sustain);
		// }

		int addmr = pw_sMr + aMR_1/* + greater()[10] */;// 魔防
		if (addmr != 0) {
			_os.writeC(15);
			_os.writeH(addmr);
		}

		int addsp = pw_sSp + aSP_1/* + greater()[9] */+ _itemInstance.getItemSp();// 魔攻
		if (addsp != 0) {
			_os.writeC(17);
			_os.writeC(addsp);
		}

		if (pw_sWr > 0) { // 飾品負重顯示
			// _os.writeC(0x5a); // 90 增加負重 +X
			// _os.writeH(pw_sWr);
			_os.writeC(68); // 68 負重增加率(%)
			_os.writeC(pw_sWr);
		}

		boolean haste = _item.isHasteItem();
		if (aSS_1 == 1) {
			haste = true;
		}
		if (haste) {
			_os.writeC(18);
		}

		int defense_fire = pw_d4_1 + d4_1;
		if (defense_fire != 0) {
			_os.writeC(27);
			_os.writeC(defense_fire);
		}

		int defense_water = pw_d4_2 + d4_2;
		if (defense_water != 0) {
			_os.writeC(28);
			_os.writeC(defense_water);
		}

		int defense_wind = pw_d4_3 + d4_3;
		if (defense_wind != 0) {
			_os.writeC(29);
			_os.writeC(defense_wind);
		}

		int defense_earth = pw_d4_4 + d4_4;
		if (defense_earth != 0) {
			_os.writeC(30);
			_os.writeC(defense_earth);
		}

		if (addexp != 0) { // 經驗加成 2017/0425
			if (addexp <= 120) {
				_os.writeC(36);
				_os.writeC(addexp);
			} else {
				this._os.writeC(39);
				this._os.writeS("$6134 " + addexp + "%");
			}
		}

		int alldrd = pw_drd + drd/* + greater()[5] */
				+ _itemInstance.getItemReductionDmg(); // 傷害減免
		if (alldrd != 0) {
			_os.writeC(63);
			_os.writeC(alldrd);
		}

		// 魔法命中
		if (allmHi != 0) {
			// _os.writeC(39);
			// _os.writeS("魔法命中 +" + allmHi);
			_os.writeC(40);
			_os.writeC(allmHi);
		}

		if (mcri != 0) {
			_os.writeC(39);
			_os.writeS("魔法爆擊率 +" + mcri);
		}

		StringBuilder name = new StringBuilder();
		int adduhp = uhp/* + greater()[2] */;
		if (adduhp != 0) {// 增加藥水回復量%
			name.append("藥水回復量 +" + adduhp + "%");
			if (uhp_num != 0) {// 增加藥水回復指定量
				name.append("+" + uhp_num);
			}
			_os.writeC(39);
			_os.writeS(name.toString());
		}

		// int pvpDmg = +greater()[7];
		// int addpvpdmg = _item.getPvpDmg() + pvpDmg; // 增加PVP傷害
		if (addpvpdmg != 0) {
			_os.writeC(59);
			_os.writeC(addpvpdmg);
		}

		// int pvpDmgdrd = +greater()[11];
		// int addpvpdmg_r = _item.getPvpDmg_R() + pvpDmgdrd; // 減免PVP傷害
		if (addpvpdmg_r != 0) {
			_os.writeC(60);
			_os.writeC(addpvpdmg_r);
		}

		if ((_item.getclassname().equalsIgnoreCase("Venom_Resist"))
				|| (_item.getclassname()
						.equalsIgnoreCase("ElitePlateMail_Antharas"))) {
			// _os.writeC(39);
			// _os.writeS("防護中毒");
			_os.writeC(57); // 57 毒耐性
			_os.writeD(19128);
		}

		// 97 無視減免
		if (antiDamageReduction != 0) {
			_os.writeC(97);
			_os.writeC(antiDamageReduction);
		}

		// 116 殷海薩祝福消耗減少
		if (einhasadConsumeReduce != 0) {
			_os.writeC(116); // 祝福消耗率%
			_os.writeH(einhasadConsumeReduce);
		}

		// 耐性
		if (add_regist_Technology != 0) { // 技術耐性
			_os.writeC(117);
			_os.writeC(add_regist_Technology);
		}
		if (add_regist_Elf != 0) { // 精靈耐性
			_os.writeC(118);
			_os.writeC(add_regist_Elf);
		}
		if (add_regist_Dragon != 0) { // 龍屬耐性
			_os.writeC(119);
			_os.writeC(add_regist_Dragon);
		}
		if (add_regist_Horror != 0) { // 恐怖耐性
			_os.writeC(120);
			_os.writeC(add_regist_Horror);
		}
		if (add_regist_All != 0) { // 全部四大耐性
			_os.writeC(121);
			_os.writeC(add_regist_All);
		}
		// 耐性end

		// 命中
		if (add_hit_Technology != 0) { // 技術命中
			_os.writeC(122);
			_os.writeC(add_hit_Technology);
		}
		if (add_hit_Elf != 0) { // 精靈命中
			_os.writeC(123);
			_os.writeC(add_hit_Elf);
		}
		if (add_hit_Dragon != 0) { // 龍屬命中
			_os.writeC(124);
			_os.writeC(add_hit_Dragon);
		}
		if (add_hit_Horror != 0) { // 恐怖命中
			_os.writeC(125);
			_os.writeC(add_hit_Horror);
		}
		if (add_hit_All != 0) { // 全部四大命中
			_os.writeC(126);
			_os.writeC(add_hit_All);
		}
		if ( add_mofabaoji != 0) { // 全部四大命中
			_os.writeC(127);
			_os.writeS("魔法暴擊率 +"+add_mofabaoji);
		}
		// 命中end

		ItemAbility(); // 強化擴充能力

		checkArmorSet(); // 套裝能力顯示

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		String[] byarmor = this._item.getclassname().split(" "); // SRC0711
		if (byarmor[0].equals("SkillByArmor")) {
			int bonus2 = this._itemInstance.getEnchantLevel()
					- this._item.get_safeenchant();
			if (bonus2 <= 0)
				bonus2 = 0;
			if (bonus2 >= 0) {
				this._os.writeC(39);
				this._os.writeS("目前+" + this._itemInstance.getEnchantLevel()
						+ " "
						+ (bonus2 + Integer.valueOf(byarmor[1]).intValue())
						+ "%機率 發動" + byarmor[4]);
				this._os.writeC(39);
				this._os.writeS("傷害 " + Integer.valueOf(byarmor[3]));
			}
		}

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return _os;
	}

	/**
	 * 副助道具
	 * 
	 * @return
	 */
	private BinaryOutputStream accessories2() {
		_os.writeC(19);
		int ac = _item.get_ac();
		if (ac < 0) {
			ac = Math.abs(ac);
		}
		_os.writeC(ac);

		_os.writeC(_item.getMaterial());
		_os.writeC(_item.get_greater());
		_os.writeD(_itemInstance.getWeight());

		int pw_s1 = _item.get_addstr();
		int pw_s2 = _item.get_adddex();
		int pw_s3 = _item.get_addcon();
		int pw_s4 = _item.get_addwis();
		int pw_s5 = _item.get_addint();
		int pw_s6 = _item.get_addcha();

		int pw_sHp = _itemPower.get_addhp();
		int pw_sMp = _itemPower.get_addmp();
		int pw_sMr = _itemPower.getMr();
		int pw_sSp = _itemPower.getSp();
		int pw_sWr = _item.getWeightReduction(); // 副助道具負重顯示

		int pw_sDg = _item.getDmgModifierByArmor();
		int pw_sHi = _itemPower.getHitModifierByArmor();
		int pw_mHi = _item.getMagicHitModifierByArmor();

		int pw_bDg = _item.getBowDmgModifierByArmor();
		int pw_bHi = _item.getBowHitModifierByArmor();

		int pw_d4_1 = _item.get_defense_fire();
		int pw_d4_2 = _item.get_defense_water();
		int pw_d4_3 = _item.get_defense_wind();
		int pw_d4_4 = _item.get_defense_earth();

		// int pw_k6_1 = _item.get_regist_freeze();
		// int pw_k6_2 = _item.get_regist_stone();
		// int pw_k6_3 = _item.get_regist_sleep();
		// int pw_k6_4 = _item.get_regist_blind();
		// int pw_k6_5 = _item.get_regist_stun();
		// int pw_k6_6 = _item.get_regist_sustain();
		int pw_sHpr = _item.get_addhpr();
		int pw_sMpr = _itemPower.getMpr();
		int addexp = _item.getExpPoint();
		int pw_drd = _itemPower.getDamageReduction();
		int uhp = _item.get_up_hp_potion();// 增加藥水回復量%
		int uhp_num = _item.get_uhp_number();// 增加藥水回復指定量
		int antiDamageReduction = _item.getAntiDamageReduction();// 無視減免
		int einhasadConsumeReduce = _item.getEinhasadConsumeReduce(); // 殷海薩祝福消耗減少

		// 耐性
		int add_regist_Technology = _item.getRegistTechnology(); // 技術耐性
		int add_regist_Elf = _item.getRegistElf(); // 精靈耐性
		int add_regist_Dragon = _item.getRegistDragon(); // 龍屬耐性
		int add_regist_Horror = _item.getRegistHorror(); // 恐怖耐性
		int add_regist_All = _item.getRegistAll(); // 全部四大耐性
		// 命中
		int add_hit_Technology = _item.getHitTechnology(); // 技術命中
		int add_hit_Elf = _item.getHitElf(); // 精靈命中
		int add_hit_Dragon = _item.getHitDragon(); // 龍屬命中
		int add_hit_Horror = _item.getHitHorror(); // 恐怖命中
		int add_hit_All = _item.getHitAll(); // 全部四大命中
		int add_mofabaoji = _item.getmofabaoji(); // 全部四大命中
		if (pw_sHi != 0) { // 近戰命中
			_os.writeC(48);
			_os.writeC(pw_sHi);
		}

		int add_sdg = pw_sDg;// 近距離傷害
		if (add_sdg != 0) {
			_os.writeC(47);
			_os.writeC(add_sdg);
		}

		if (pw_bHi != 0) {
			_os.writeC(24);
			_os.writeC(pw_bHi);
		}

		int add_bdg = pw_bDg;// 遠距離傷害
		if (add_bdg != 0) {
			_os.writeC(35);
			_os.writeC(add_bdg);
		}

		int bit = 0;
		bit |= (_item.isUseRoyal() ? 1 : 0);
		bit |= (_item.isUseKnight() ? 2 : 0);
		bit |= (_item.isUseElf() ? 4 : 0);
		bit |= (_item.isUseMage() ? 8 : 0);
		bit |= (_item.isUseDarkelf() ? 16 : 0);
		bit |= (_item.isUseDragonknight() ? 32 : 0);
		bit |= (_item.isUseIllusionist() ? 64 : 0);
		bit |= (_item.isUseWarrior() ? 128 : 0);
		_os.writeC(7);
		_os.writeC(bit);

		int addstr = pw_s1;// 力量
		int adddex = pw_s2;// 敏捷
		int addcon = pw_s3;// 體質
		int addwis = pw_s4;// 精神.
		int addint = pw_s5;// 智力
		int addcha = pw_s6;// 魅力

		if (addstr == 1 && adddex == 1 && addcon == 1 && addwis == 1
				&& addint == 1 && addcha == 1) {
			_os.writeC(39);
			_os.writeS("全能力值 +1");
		} else {
			if (addstr != 0) {
				_os.writeC(8);
				_os.writeC(addstr);
			}

			if (adddex != 0) {
				_os.writeC(9);
				_os.writeC(adddex);
			}

			if (addcon != 0) {
				_os.writeC(10);
				_os.writeC(addcon);
			}

			if (addwis != 0) {
				_os.writeC(11);
				_os.writeC(addwis);
			}

			if (addint != 0) {
				_os.writeC(12);
				_os.writeC(addint);
			}

			if (addcha != 0) {
				_os.writeC(13);
				_os.writeC(addcha);
			}
		}

		int addhp = pw_sHp;
		if (addhp != 0) {
			_os.writeC(14);
			_os.writeH(addhp);
		}

		int addmp = pw_sMp;
		if (addmp != 0) {
			_os.writeC(0x20);
			_os.writeH(addmp);

		}

		int addhpr = pw_sHpr; // 體力回復量
		if (addhpr != 0) {
			_os.writeC(37);
			_os.writeC(addhpr);
		}

		int addmpr = pw_sMpr; // 魔力回復量
		if (addmpr != 0) {
			_os.writeC(38);
			_os.writeC(addmpr);
		}

		// int freeze = pw_k6_1; // 冰凍耐性
		// if (freeze != 0) {
		// _os.writeC(33);
		// _os.writeC(1);
		// _os.writeC(freeze);
		// }
		//
		// int stone = pw_k6_2; // 石化耐性
		// if (stone != 0) {
		// _os.writeC(33);
		// _os.writeC(2);
		// _os.writeC(stone);
		// }
		//
		// int sleep = pw_k6_3; // 睡眠耐性
		// if (sleep != 0) {
		// _os.writeC(33);
		// _os.writeC(3);
		// _os.writeC(sleep);
		// }
		//
		// int blind = pw_k6_4; // 暗黑耐性
		// if (blind != 0) {
		// _os.writeC(33);
		// _os.writeC(4);
		// _os.writeC(blind);
		// }
		//
		// int stun = pw_k6_5; // 昏迷耐性
		// if (stun != 0) {
		// _os.writeC(33);
		// _os.writeC(5);
		// _os.writeC(stun);
		// }
		//
		// int sustain = pw_k6_6; // 支撐耐性
		// if (sustain != 0) {
		// _os.writeC(33);
		// _os.writeC(6);
		// _os.writeC(sustain);
		// }

		int addmr = pw_sMr;
		if (addmr != 0) {
			_os.writeC(15);
			_os.writeH(addmr);
		}

		int addsp = pw_sSp;
		if (addsp != 0) {
			_os.writeC(17);
			_os.writeC(addsp);
		}

		if (pw_sWr > 0) { // 副助道具負重顯示
			// _os.writeC(0x5a); // 90 增加負重 +X
			// _os.writeH(pw_sWr);
			_os.writeC(68); // 68 負重增加率(%)
			_os.writeC(pw_sWr);
		}

		boolean haste = _item.isHasteItem();
		if (haste) {
			_os.writeC(18);
		}

		int defense_fire = pw_d4_1;
		if (defense_fire != 0) {
			_os.writeC(27);
			_os.writeC(defense_fire);
		}

		int defense_water = pw_d4_2;
		if (defense_water != 0) {
			_os.writeC(28);
			_os.writeC(defense_water);
		}

		int defense_wind = pw_d4_3;
		if (defense_wind != 0) {
			_os.writeC(29);
			_os.writeC(defense_wind);
		}

		int defense_earth = pw_d4_4;
		if (defense_earth != 0) {
			_os.writeC(30);
			_os.writeC(defense_earth);
		}

		if (addexp != 0) { // 經驗加成 2017/0425
			if (addexp <= 120) {
				_os.writeC(36);
				_os.writeC(addexp);
			} else {
				this._os.writeC(39);
				this._os.writeS("$6134 " + addexp + "%");
			}
		}

		if (pw_drd != 0) {// 傷害減免
			_os.writeC(63);
			_os.writeC(pw_drd);
		}

		if (pw_mHi != 0) {// 魔法命中
			// _os.writeC(39);
			// _os.writeS("魔法命中 +" + pw_mHi);
			_os.writeC(40);
			_os.writeC(pw_mHi);
		}

		StringBuilder name = new StringBuilder();
		int adduhp = uhp/* + greater()[2] */;
		if (adduhp != 0) {// 增加藥水回復量%
			name.append("藥水回復量 +" + adduhp + "%");
			if (uhp_num != 0) {// 增加藥水回復指定量
				name.append("+" + uhp_num);
			}
			_os.writeC(39);
			_os.writeS(name.toString());
		}

		int addpvpdmg = _item.getPvpDmg(); // 增加PVP傷害
		if (addpvpdmg != 0) {
			_os.writeC(59);
			_os.writeC(addpvpdmg);
		}

		int addpvpdmg_r = _item.getPvpDmg_R(); // 減免PVP傷害
		if (addpvpdmg_r != 0) {
			_os.writeC(60);
			_os.writeC(addpvpdmg_r);
		}

		// 97 無視減免
		if (antiDamageReduction != 0) {
			_os.writeC(97);
			_os.writeC(antiDamageReduction);
		}

		// 116 殷海薩祝福消耗減少
		if (einhasadConsumeReduce != 0) {
			_os.writeC(116); // 祝福消耗率%
			_os.writeH(einhasadConsumeReduce);
		}

		// 耐性
		if (add_regist_Technology != 0) { // 技術耐性
			_os.writeC(117);
			_os.writeC(add_regist_Technology);
		}
		if (add_regist_Elf != 0) { // 精靈耐性
			_os.writeC(118);
			_os.writeC(add_regist_Elf);
		}
		if (add_regist_Dragon != 0) { // 龍屬耐性
			_os.writeC(119);
			_os.writeC(add_regist_Dragon);
		}
		if (add_regist_Horror != 0) { // 恐怖耐性
			_os.writeC(120);
			_os.writeC(add_regist_Horror);
		}
		if (add_regist_All != 0) { // 全部四大耐性
			_os.writeC(121);
			_os.writeC(add_regist_All);
		}
		// 耐性end

		// 命中
		if (add_hit_Technology != 0) { // 技術命中
			_os.writeC(122);
			_os.writeC(add_hit_Technology);
		}
		if (add_hit_Elf != 0) { // 精靈命中
			_os.writeC(123);
			_os.writeC(add_hit_Elf);
		}
		if (add_hit_Dragon != 0) { // 龍屬命中
			_os.writeC(124);
			_os.writeC(add_hit_Dragon);
		}
		if (add_hit_Horror != 0) { // 恐怖命中
			_os.writeC(125);
			_os.writeC(add_hit_Horror);
		}
		if (add_hit_All != 0) { // 全部四大命中
			_os.writeC(126);
			_os.writeC(add_hit_All);
		}
		if ( add_mofabaoji != 0) { // 全部四大命中
			_os.writeC(127);
			_os.writeS("魔法暴擊率 +"+add_mofabaoji);
		}
		// 命中end

		ItemAbility(); // 強化擴充能力

		checkArmorSet(); // 套裝能力顯示

		final L1ItemPower_name power = _itemInstance.get_power_name();
		if (_item.isSuperRune()) {
			if (power != null) {

				if (_item.isSuperRune()) {
					_os.writeC(0x31);
					_os.writeC(0x01);

					for (int i = 0; i < 4; i++) {
						final StringBuilder powername1 = new StringBuilder();
						this._os.writeC(0x27);
						switch (i) {
						case 0:
							powername1.append("欄位1:"
									+ set_rune_name(power.get_super_rune_1()));
							break;
						case 1:
							powername1.append("欄位2:"
									+ set_rune_name(power.get_super_rune_2()));
							break;
						case 2:
							powername1.append("欄位3:"
									+ set_rune_name(power.get_super_rune_3()));
							break;
						case 3:

							powername1.append("欄位4:"
									+ set_rune_name2(power.get_super_rune_4()));
							break;
						}
						this._os.writeS(powername1.toString());
					}
					_os.writeC(0x31);
					_os.writeC(0x00);
				}
			} else {

				if (_item.isSuperRune()) {
					_os.writeC(0x31);
					_os.writeC(0x01);

					for (int i = 0; i < 4; i++) {
						final StringBuilder powername2 = new StringBuilder();
						this._os.writeC(0x27);
						switch (i) {
						case 0:
							powername2.append("欄位1:" + set_rune_name(0));
							break;
						case 1:
							powername2.append("欄位2:" + set_rune_name(0));
							break;
						case 2:
							powername2.append("欄位3:" + set_rune_name(0));
							break;
						case 3:

							powername2.append("欄位4:" + set_rune_name2(0));
							break;
						}
						this._os.writeS(powername2.toString());
					}
					_os.writeC(0x31);
					_os.writeC(0x00);
				}
			}
		}

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		String[] byarmor = this._item.getclassname().split(" "); // SRC0711
		if (byarmor[0].equals("SkillByArmor")) {
			int bonus2 = this._itemInstance.getEnchantLevel()
					- this._item.get_safeenchant();
			if (bonus2 <= 0)
				bonus2 = 0;
			if (bonus2 >= 0) {
				this._os.writeC(39);
				this._os.writeS("目前+" + this._itemInstance.getEnchantLevel()
						+ " "
						+ (bonus2 + Integer.valueOf(byarmor[1]).intValue())
						+ "%機率 發動" + byarmor[4]);
				this._os.writeC(39);
				this._os.writeS("傷害 " + Integer.valueOf(byarmor[3]));
			}
		}

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return _os;
	}

	// TODO 武器
	/**
	 * 武器
	 * 
	 * @return
	 */
	private BinaryOutputStream weapon() {
		_os.writeC(1);
		_os.writeC(_item.getDmgSmall());
		_os.writeC(_item.getDmgLarge());

		_os.writeC(_item.getMaterial());
		_os.writeD(_itemInstance.getWeight());

		if (_itemInstance.getEnchantLevel() != 0) {
			_os.writeC(2);
			_os.writeC(_itemInstance.getEnchantLevel());
		}

		// 武器劍靈系統
		L1WeaponSoul weaponsoul = WeaponSoul.getInstance().getTemplate(
				_itemInstance.getItemId());
		if (weaponsoul != null) {
			final int updateweaponsoul = _itemInstance.getUpdateWeaponSoul();
			if (updateweaponsoul > 0) {
				_os.writeC(0x27);
				_os.writeS("\\f3劍靈值：" + updateweaponsoul);
			} else {
				_os.writeC(0x27);
				_os.writeS("\\f3劍靈值：0");
			}
		}

		if (_itemInstance.get_durability() != 0) {
			_os.writeC(3);
			_os.writeC(_itemInstance.get_durability());
		}

		if (_item.isTwohandedWeapon()) {
			_os.writeC(4);
		}

		int get_addstr = _item.get_addstr() + _itemInstance.getItemStr();
		int get_adddex = _item.get_adddex() + _itemInstance.getItemDex();
		int get_addcon = _item.get_addcon() + _itemInstance.getItemCon();
		int get_addwis = _item.get_addwis() + _itemInstance.getItemWis();
		int get_addint = _item.get_addint() + _itemInstance.getItemInt();
		int get_addcha = _item.get_addcha() + _itemInstance.getItemCha();

		int get_addhp = _itemPower.get_addhp();
		int get_addmp = _itemPower.get_addmp();
		int mr = _itemPower.getMr();

		int addWeaponSp = _itemPower.getSp() + _itemInstance.getItemSp();

		// int addDmgModifier = _item.getDmgModifier();
		// int addHitModifier = _item.getHitModifier();
		int hit = 0; // 近戰命中
		int dmg = 0; // 近戰傷害
		int bowhit = 0; // 遠程命中
		int bowdmg = 0; // 遠程傷害

		int addHitModifier = _item.getHitModifier();// 武器(近戰or遠程)命中
		if (addHitModifier != 0) {
			if (_item.getType() == 4 || _item.getType() == 13) {
				bowhit += addHitModifier;
			} else {
				hit += addHitModifier;
			}
		}

		int addDmgModifier = _item.getDmgModifier();// 武器(近戰or遠程)傷害
		if (addDmgModifier != 0) {
			if (_item.getType() == 4 || _item.getType() == 13) {
				bowdmg += addDmgModifier;
			} else {
				dmg += addDmgModifier;
			}
		}

		int addmagicdmg = _item.getMagicDmgModifier();

		int pw_d4_1 = _item.get_defense_fire();
		int pw_d4_2 = _item.get_defense_water();
		int pw_d4_3 = _item.get_defense_wind();
		int pw_d4_4 = _item.get_defense_earth();

		// int pw_k6_1 = _item.get_regist_freeze();
		// int pw_k6_2 = _item.get_regist_stone();
		// int pw_k6_3 = _item.get_regist_sleep();
		// int pw_k6_4 = _item.get_regist_blind();
		// int pw_k6_5 = _item.get_regist_stun();
		// int pw_k6_6 = _item.get_regist_sustain();
		int pw_sHpr = _item.get_addhpr();
		int pw_sMpr = _itemPower.getMpr();
		int addexp = _item.getExpPoint();
		int stunlvl = _item.get_stunlvl();

		int addpvpdmg = _item.getPvpDmg(); // 增加PVP傷害
		int addpvpdmg_r = _item.getPvpDmg_R(); // 減免PVP傷害
		int damage_reduction = 0;// 傷害減免

		int CloseCri = 0; // 近距離爆擊率
		int BowCri = 0; // 遠距離爆擊率
		int MagicCri = 0; // 魔法爆擊率
		// int FearLevel = 0; // 恐怖等級
		int ExtraAc = 0; // 額外防禦
		int antiDamageReduction = _item.getAntiDamageReduction();// 無視減免
		int einhasadConsumeReduce = _item.getEinhasadConsumeReduce(); // 殷海薩祝福消耗減少

		// 耐性
		int add_regist_Technology = _item.getRegistTechnology(); // 技術耐性
		int add_regist_Elf = _item.getRegistElf(); // 精靈耐性
		int add_regist_Dragon = _item.getRegistDragon(); // 龍屬耐性
		int add_regist_Horror = _item.getRegistHorror(); // 恐怖耐性
		int add_regist_All = _item.getRegistAll(); // 全部四大耐性
		// 命中
		int add_hit_Technology = _item.getHitTechnology(); // 技術命中
		int add_hit_Elf = _item.getHitElf(); // 精靈命中
		int add_hit_Dragon = _item.getHitDragon(); // 龍屬命中
		int add_hit_Horror = _item.getHitHorror(); // 恐怖命中
		int add_hit_All = _item.getHitAll(); // 全部四大命中
		int add_mofabaoji = _item.getmofabaoji(); // 全部四大命中
		if (_item.get_canbedmg() != 1) {
			_os.writeC(131); // 不戰損
			_os.writeD(1);
		}

		int safeenchant = _item.get_safeenchant();
		if (safeenchant >= 0) {
			_os.writeC(39);
			_os.writeS("安定值: " + _item.get_safeenchant());
		}

		// 裝備強化能力系統
		L1WilliamEnchantOrginal armorOrginal1 = null;
		L1WilliamEnchantOrginal armorOrginalOk1 = null;
		L1WilliamEnchantOrginal[] armorOrginalSize1 = EnchantOrginal
				.getInstance().getArmorList();
		for (int i = 0; i < armorOrginalSize1.length; i++) {
			armorOrginalOk1 = EnchantOrginal.getInstance().getTemplate(i);
			if (armorOrginalOk1.getItemId() == _itemInstance.getItemId()
					&& _itemInstance.getEnchantLevel() >= armorOrginalOk1
							.getLevel()) {
				armorOrginal1 = armorOrginalOk1;
				// break;
			}
		}
		if (armorOrginal1 != null) {
			ExtraAc += armorOrginal1.getAddAc(); // 額外防禦
			get_addstr += armorOrginal1.getAddStr();
			get_adddex += armorOrginal1.getAddDex();
			get_addcon += armorOrginal1.getAddCon();
			get_addint += armorOrginal1.getAddInt();
			get_addwis += armorOrginal1.getAddWis();
			get_addcha += armorOrginal1.getAddCha();
			get_addhp += armorOrginal1.getAddMaxHp();
			get_addmp += armorOrginal1.getAddMaxMp();
			pw_sHpr += armorOrginal1.getAddHpr();
			pw_sMpr += armorOrginal1.getAddMpr();
			dmg += armorOrginal1.getAddDmg();
			hit += armorOrginal1.getAddHit();
			bowdmg += armorOrginal1.getAddBowDmg();
			bowhit += armorOrginal1.getAddBowHit();
			damage_reduction += armorOrginal1.getReduction_dmg();
			mr += armorOrginal1.getAddMr();
			addWeaponSp += armorOrginal1.getAddSp();
			pw_d4_1 += armorOrginal1.getAddFire();
			pw_d4_3 += armorOrginal1.getAddWind();
			pw_d4_4 += armorOrginal1.getAddEarth();
			pw_d4_2 += armorOrginal1.getAddWater();
			addpvpdmg += armorOrginal1.getAddPvpDmg(); // 增加PVP傷害
			addpvpdmg_r += armorOrginal1.getAddPvpDmg_R(); // 減免PVP傷害
			CloseCri += armorOrginal1.getAddCloseCri(); // 近距離爆擊率
			BowCri += armorOrginal1.getAddBowCri(); // 遠距離爆擊率
			MagicCri += armorOrginal1.getAddMagicCri(); // 魔法爆擊率
			// FearLevel += armorOrginal1.getAddFearLevel(); // 恐怖等級
			// 耐性
			add_regist_Technology += armorOrginal1.getAddRegistTechnology(); // 技術耐性
			add_regist_Elf += armorOrginal1.getAddRegistElf(); // 精靈耐性
			add_regist_Dragon += armorOrginal1.getAddRegistDragon(); // 龍屬耐性
			add_regist_Horror += armorOrginal1.getAddRegistHorror(); // 恐怖耐性
			add_regist_All += armorOrginal1.getAddRegistAll(); // 全部四大耐性
			// 命中
			add_hit_Technology += armorOrginal1.getAddHitTechnology(); // 技術命中
			add_hit_Elf += armorOrginal1.getAddHitElf(); // 精靈命中
			add_hit_Dragon += armorOrginal1.getAddHitDragon(); // 龍屬命中
			add_hit_Horror += armorOrginal1.getAddHitHorror(); // 恐怖命中
			add_hit_All += armorOrginal1.getAddHitAll(); // 全部四大命中
			einhasadConsumeReduce += armorOrginal1
					.getAddEinhasadConsumeReduce(); // 殷海薩祝福消耗減少
		}
		// 裝備強化能力系統 end

		// if (addHitModifier != 0) {
		// if (_item.getType() == 4 || _item.getType() == 13) {
		// //_os.writeC(39);
		// //_os.writeS("遠距離命中 +" + addHitModifier);
		// _os.writeC(24); // 遠距離命中率
		// _os.writeC(addHitModifier);
		// } else {
		// _os.writeC(48); // 近戰命中
		// _os.writeC(addHitModifier);
		// }
		// }
		//
		// if (addDmgModifier != 0 ) {
		// if (_item.getType() == 4 || _item.getType() == 13) {
		// //_os.writeC(39);
		// //_os.writeS("遠距離攻擊力 +" + addDmgModifier );
		// _os.writeC(35); // 遠距離傷害
		// _os.writeC(addDmgModifier );
		// } else {
		// _os.writeC(47); // 近戰攻擊
		// _os.writeC(addDmgModifier );
		// }
		//
		// }

		// 近戰傷害
		if (dmg != 0) {
			_os.writeC(47);
			_os.writeC(dmg);
		}

		// 近戰命中
		if (hit != 0) {
			_os.writeC(48);
			_os.writeC(hit);
		}

		// 遠程傷害
		if (bowdmg != 0) {
			_os.writeC(0x23);
			_os.writeC(bowdmg);
		}

		// 遠程命中
		if (bowhit != 0) {
			_os.writeC(0x18);
			_os.writeC(bowhit);
		}

		// if (_item.getDmgModifier() > 0) { // 武器(近戰or遠程)傷害
		if (_item.getBless() == 0 || _item.getBless() == 128) {
			_os.writeC(115); // 不死族,惡魔
			_os.writeD(1);

		} else if (_item.getMaterial() == 14 // 銀
				|| _item.getMaterial() == 17 // 米索莉
				|| _item.getMaterial() == 22) { // 奧裡哈魯根
			_os.writeC(114); // 不死族
			_os.writeD(1);
		}
		// }

		if (addmagicdmg != 0) {
			_os.writeC(39);
			_os.writeS("魔法傷害: " + addmagicdmg);
		}

		int bit = 0;
		bit |= (_item.isUseRoyal() ? 1 : 0);
		bit |= (_item.isUseKnight() ? 2 : 0);
		bit |= (_item.isUseElf() ? 4 : 0);
		bit |= (_item.isUseMage() ? 8 : 0);
		bit |= (_item.isUseDarkelf() ? 16 : 0);
		bit |= (_item.isUseDragonknight() ? 32 : 0);
		bit |= (_item.isUseIllusionist() ? 64 : 0);
		bit |= (_item.isUseWarrior() ? 128 : 0);
		_os.writeC(7);
		_os.writeC(bit);

		if ((_itemInstance.getItemId() == 126)
				|| (_itemInstance.getItemId() == 127)
				|| (_itemInstance.getItemId() == 259)
				|| (_itemInstance.getItemId() == 305)
				|| (_itemInstance.getItemId() == 310)
				|| (_itemInstance.getItemId() == 315)) {
			_os.writeC(16);
		}

		if ((_itemInstance.getItemId() == 262)
				|| (_itemInstance.getItemId() == 410157)
				|| (_itemInstance.getItemId() == 12)
				|| (_itemInstance.getItemId() == 410117)
				|| (_itemInstance.getItemId() == 410164)) {
			_os.writeC(34);
		}

		if (get_addstr != 0) {
			_os.writeC(8);
			_os.writeC(get_addstr);
		}

		if (get_adddex != 0) {
			_os.writeC(9);
			_os.writeC(get_adddex);
		}

		if (get_addcon != 0) {
			_os.writeC(10);
			_os.writeC(get_addcon);
		}

		if (get_addwis != 0) {
			_os.writeC(11);
			_os.writeC(get_addwis);
		}

		if (get_addint != 0) {
			_os.writeC(12);
			_os.writeC(get_addint);
		}

		if (get_addcha != 0) {
			_os.writeC(13);
			_os.writeC(get_addcha);
		}

		if (get_addhp != 0) {
			_os.writeC(14);
			_os.writeH(get_addhp);
		}

		if (get_addmp != 0) {
			_os.writeC(0x20);
			_os.writeH(get_addmp);
		}

		// 體力回復量
		if (pw_sHpr != 0) {
			_os.writeC(37);
			_os.writeC(pw_sHpr);
		}

		// 魔力回復量
		if (pw_sMpr != 0) {
			_os.writeC(38);
			_os.writeC(pw_sMpr);
		}

		if (mr != 0) {
			_os.writeC(15);
			_os.writeH(mr);
		}

		if (addWeaponSp != 0) {
			_os.writeC(17);
			_os.writeC(addWeaponSp);
		}

		if (_item.isHasteItem()) {
			_os.writeC(18);
		}

		if (pw_d4_1 != 0) {
			_os.writeC(27);
			_os.writeC(pw_d4_1);
		}

		if (pw_d4_2 != 0) {
			_os.writeC(28);
			_os.writeC(pw_d4_2);
		}

		if (pw_d4_3 != 0) {
			_os.writeC(29);
			_os.writeC(pw_d4_3);
		}

		if (pw_d4_4 != 0) {
			_os.writeC(30);
			_os.writeC(pw_d4_4);
		}

		// if (pw_k6_1 != 0) {
		// // _os.writeC(15);
		//
		// _os.writeC(33);
		// _os.writeC(1);
		// _os.writeC(pw_k6_1);
		// }
		//
		// if (pw_k6_2 != 0) {
		// // _os.writeC(15);
		//
		// _os.writeC(33);
		// _os.writeC(2);
		// _os.writeC(pw_k6_2);
		// }
		//
		// if (pw_k6_3 != 0) {
		// // _os.writeC(15);
		//
		// _os.writeC(33);
		// _os.writeC(3);
		// _os.writeC(pw_k6_3);
		// }
		//
		// if (pw_k6_4 != 0) {
		// // _os.writeC(15);
		//
		// _os.writeC(33);
		// _os.writeC(4);
		// _os.writeC(pw_k6_4);
		// }
		//
		// if (pw_k6_5 != 0) {
		// // _os.writeC(15);
		//
		// _os.writeC(33);
		// _os.writeC(5);
		// _os.writeC(pw_k6_5);
		// }
		//
		// if (pw_k6_6 != 0) {
		// // _os.writeC(15);
		//
		// _os.writeC(33);
		// _os.writeC(6);
		// _os.writeC(pw_k6_6);
		// }

		if (addexp != 0) { // 經驗加成 2017/0425
			if (addexp <= 120) {
				_os.writeC(36);
				_os.writeC(addexp);
			} else {
				this._os.writeC(39);
				this._os.writeS("$6134 " + addexp + "%");
			}
		}

		// 魔法爆擊率
		if (MagicCri != 0) {
			_os.writeC(50);
			_os.writeH(MagicCri);
		}

		// 56 額外防禦
		if (ExtraAc != 0) {
			_os.writeC(56);
			_os.writeC(ExtraAc);
		}

		// 傷害減免
		if (damage_reduction != 0) {
			_os.writeC(63);
			_os.writeC(damage_reduction);
		}

		// 貫通效果
		int isPenetrate = _item.get_penetrate(); // 貫通效果
		if (isPenetrate == 1) {
			_os.writeC(94); // 94 貫通效果
		}

		// 97 無視減免
		if (antiDamageReduction != 0) {
			_os.writeC(97);
			_os.writeC(antiDamageReduction);
		}

		// 昏迷等級
		if (stunlvl != 0) {
			_os.writeC(98);
			_os.writeD(23521); // 昏迷等級提升 +%d
			_os.writeH(stunlvl);
		}

		// 98 恐怖等級提升
		// if (FearLevel != 0) {
		// _os.writeC(98);
		// _os.writeD(24131); // 恐怖命中 +%d---- 26607 恐怖等級 +3
		// _os.writeH(FearLevel);
		// }

		// 99 遠距離爆擊
		if (BowCri != 0) {
			_os.writeC(99);
			_os.writeC(BowCri);
		}

		// 100 近距離爆擊
		if (CloseCri != 0) {
			_os.writeC(100);
			_os.writeC(CloseCri);
		}

		// 魔法武器DIY系統
		final L1ItemPower_name power = this._itemInstance.get_power_name();
		if (power != null) {

			final L1CriticalHitStone criticalHitStone = power
					.get_critical_hit_stone();
			if (criticalHitStone != null) {
				this._os.writeC(0x27);
				final StringBuilder name = new StringBuilder()
						.append(criticalHitStone.getName());
				this._os.writeS(name.toString());
			}

			final L1MagicWeapon magicWeapon = power.get_magic_weapon();
			if (magicWeapon != null) {
				this._os.writeC(0x27);
				final StringBuilder name = new StringBuilder()
						.append(magicWeapon.getSkillName());
				// 使用期限
				if (magicWeapon.getMaxUseTime() > 0
						&& power.get_date_time() != null) {
					name.append(sdf.format(power.get_date_time()));
				}
				this._os.writeS(name.toString());
			}
			L1BossWeapon bossWeapon = power.get_boss_weapon();
			if (bossWeapon != null) {
				this._os.writeC(0x27);
				final StringBuilder name = new StringBuilder()
						.append(bossWeapon.getBossName());
				if (bossWeapon.getMaxUseTime() > 0
						&& power.get_boss_date_time() != null) {
					name.append(sdf.format(power.get_boss_date_time()));
				}
				this._os.writeS(name.toString());
			}
		}

		final L1ItemPower_bless bless = this._itemInstance.get_power_bless();
		if (bless != null) {
			StringBuilder stonepower = new StringBuilder();
			StringBuilder stringBuilder = new StringBuilder();
			StringBuilder stringBuilder1 = new StringBuilder();

			if (bless.get_hole_count() > 0) {
				this._os.writeC(0x27);
				for (int i = 0; i < bless.get_hole_count(); i++) {
					switch (i) {
					case 0:
						stonepower.append(set_hole_name(bless.get_hole_1()));
						break;
					case 1:
						stonepower.append(set_hole_name(bless.get_hole_2()));
						break;
					case 2:
						stonepower.append(set_hole_name(bless.get_hole_3()));
						break;
					case 3:
						stonepower.append(set_hole_name(bless.get_hole_4()));
						break;
					case 4:
						stonepower.append(set_hole_name(bless.get_hole_5()));
						break;
					}
				}
				this._os.writeS(stonepower.toString());
			}
			if ((bless.get_hole_str() != 0) || (bless.get_hole_dex() != 0)
					|| (bless.get_hole_int() != 0)
					|| (bless.get_hole_dmg() != 0)
					|| (bless.get_hole_bowdmg() != 0)
					|| (bless.get_hole_mcdmg() != 0)) {
				if (bless.get_hole_str() >= 0) {
					stringBuilder.append("力+" + bless.get_hole_str() + " ");
				} else {
					stringBuilder.append("力" + bless.get_hole_str() + " ");
				}

				if (bless.get_hole_dex() >= 0) {
					stringBuilder.append("敏+" + bless.get_hole_dex() + " ");
				} else {
					stringBuilder.append("敏" + bless.get_hole_dex() + " ");
				}

				if (bless.get_hole_int() >= 0) {
					stringBuilder.append("智+" + bless.get_hole_int() + " ");
				} else {
					stringBuilder.append("智" + bless.get_hole_int() + " ");
				}
				if (bless.get_hole_dmg() >= 0) {
					stringBuilder1.append("近戰+" + bless.get_hole_dmg() + " ");
				} else {
					stringBuilder1.append("近戰" + bless.get_hole_dmg() + " ");
				}
				if (bless.get_hole_bowdmg() >= 0) {
					stringBuilder1
							.append("遠弓+" + bless.get_hole_bowdmg() + " ");
				} else {
					stringBuilder1.append("遠弓" + bless.get_hole_bowdmg() + " ");
				}
				if (bless.get_hole_mcdmg() >= 0) {
					stringBuilder1.append("魔法+" + bless.get_hole_mcdmg() + " ");
				} else {
					stringBuilder1.append("魔法" + bless.get_hole_mcdmg() + " ");
				}
				// this._os.writeC(39);
				// this._os.writeS("祝福強化:");
				this._os.writeC(39);
				this._os.writeS(stringBuilder.toString().trim());
				this._os.writeC(39);
				this._os.writeS(stringBuilder1.toString().trim());
			}
		}

		// TODO PVP系統 武器傷害增加
		/*
		 * if (ConfigOther.PVP_WEAPON && _itemInstance.getItemId() >= 1 &&
		 * _itemInstance.getItemId() <= 5000000 &&
		 * _itemInstance.getEnchantLevel() >= ConfigOther.PVP_plus) {
		 * _os.writeC(59); _os.writeC(_itemInstance.getEnchantLevel() -
		 * ConfigOther.PVP_plus + 1); }
		 * 
		 * if (_itemInstance.getItemAttack() != 0 ||
		 * _itemInstance.getItemBowAttack() != 0) { _os.writeC(59); // 增加PVP傷害 ？
		 * _os.writeC(_itemInstance.getItemAttack() +
		 * _itemInstance.getItemBowAttack()); }
		 */

		// 增加PVP傷害
		int allpvpdmg = addpvpdmg + _itemInstance.getItemAttack()
				+ _itemInstance.getItemBowAttack();
		if (allpvpdmg != 0) {
			_os.writeC(59);
			_os.writeC(allpvpdmg);
		}

		// 減免PVP傷害
		if (addpvpdmg_r != 0) {
			_os.writeC(60);
			_os.writeC(addpvpdmg_r);
		}

		// terry770106
		int dr = _itemInstance.getItemprobability();
		L1WeaponSkill weaponSkill = WeaponSkillTable.get().getTemplate(
				_itemInstance.getItemId());
		if (weaponSkill != null) {
			// 魔法武器發動的技能名稱
			if (weaponSkill.getSkillName() != null
					&& !weaponSkill.getSkillName().isEmpty()) {
				_os.writeC(74); // 74 觸發:+ S
				_os.writeS(weaponSkill.getSkillName());
			}
			_os.writeC(39);
			// _os.writeS("魔法發動率:" + Math.min(weaponSkill.getProbability() + dr,
			// 100) + "%");
			_os.writeS("$20274:"
					+ Math.min(weaponSkill.getProbability() + dr, 100) + "%"); // $20274=發動機率
		}

		// 武器屬性傷害
		// if (_itemInstance.getAttrEnchantLevel() != 0) {
		// _os.writeC(110);
		// _os.writeC(getAttrEnchantBit(_itemInstance.getAttrEnchantLevel()));
		// }

		// 116 殷海薩祝福消耗減少
		if (einhasadConsumeReduce != 0) {
			_os.writeC(116); // 祝福消耗率%
			_os.writeH(einhasadConsumeReduce);
		}

		// 耐性
		if (add_regist_Technology != 0) { // 技術耐性
			_os.writeC(117);
			_os.writeC(add_regist_Technology);
		}
		if (add_regist_Elf != 0) { // 精靈耐性
			_os.writeC(118);
			_os.writeC(add_regist_Elf);
		}
		if (add_regist_Dragon != 0) { // 龍屬耐性
			_os.writeC(119);
			_os.writeC(add_regist_Dragon);
		}
		if (add_regist_Horror != 0) { // 恐怖耐性
			_os.writeC(120);
			_os.writeC(add_regist_Horror);
		}
		if (add_regist_All != 0) { // 全部四大耐性
			_os.writeC(121);
			_os.writeC(add_regist_All);
		}
		// 耐性end

		// 命中
		if (add_hit_Technology != 0) { // 技術命中
			_os.writeC(122);
			_os.writeC(add_hit_Technology);
		}
		if (add_hit_Elf != 0) { // 精靈命中
			_os.writeC(123);
			_os.writeC(add_hit_Elf);
		}
		if (add_hit_Dragon != 0) { // 龍屬命中
			_os.writeC(124);
			_os.writeC(add_hit_Dragon);
		}
		if (add_hit_Horror != 0) { // 恐怖命中
			_os.writeC(125);
			_os.writeC(add_hit_Horror);
		}
		if (add_hit_All != 0) { // 全部四大命中
			_os.writeC(126);
			_os.writeC(add_hit_All);
		}
		if ( add_mofabaoji != 0) { // 全部四大命中
			_os.writeC(127);
			_os.writeS("魔法暴擊率 +"+add_mofabaoji);
		}
		// 命中end

		ItemAbility(); // 強化擴充能力

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return _os;
	}

	// 記錄時間格式
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"-[MM-dd HH:mm]");

	/**
	 * 一般道具
	 * 
	 * @return
	 */
	private BinaryOutputStream etcitem() {
		if (_item.getItemId() == 40312) {// 旅館鑰匙
			_os.writeC(39);
			_os.writeS("旅館編號:" + _itemInstance.getInnKeyName());
			_os.writeC(39);
			// _os.writeS("到期時間:(" + _itemInstance.getDueTime() + ")");
			_os.writeS("到期:" + _itemInstance.getDueTime());
		}

		if (_item.getItemId() == 82503) {// 訓練所鑰匙
			_os.writeC(39);
			_os.writeS("訓練所編號:" + _itemInstance.getKeyId());
			_os.writeC(39);
			// _os.writeS("到期時間:(" + _itemInstance.getDueTime() + ")");
			_os.writeS("到期:" + _itemInstance.getDueTime());
		}

		if (_item.getItemId() == 640321) { // 下層戰鬥強化卷軸
			_os.writeC(47); // 近距離傷害+30
			_os.writeC(30); // 近距離傷害+30

			_os.writeC(48); // 近距離命中率+30
			_os.writeC(30); // 近距離命中率+30

			_os.writeC(35); // 遠距離攻擊+30
			_os.writeC(30); // 遠距離攻擊+30

			_os.writeC(24); // 遠距離命中率+30
			_os.writeC(30); // 遠距離命中率+30

			_os.writeC(17); // 魔攻+30
			_os.writeC(30); // 魔攻+30

			_os.writeC(39);
			_os.writeS("\\f2限屍魂副本使用");
			// _os.writeC(39);
			// _os.writeS("\\f3效果時間： 600秒");
		}
		if (_item.getItemId() == 640322) { // 下層防禦強化卷軸
			_os.writeC(56); // 額外防禦+50
			_os.writeC(50); // 額外防禦+50

			_os.writeC(39);
			_os.writeS("\\f2限屍魂副本使用");
			// _os.writeC(39);
			// _os.writeS("\\f3效果時間： 600秒");
		}
		if (_item.getItemId() >= 640404 && _item.getItemId() <= 640406) { // 潘朵拉近戰魔石
			_os.writeC(14); // 體力上限+50
			_os.writeH(50); // 體力上限+50

			_os.writeC(47); // 近距離傷害+2
			_os.writeC(2); // 近距離傷害+2

			_os.writeC(37); // 體力回復量+3
			_os.writeC(3); // 體力回復量+3

			_os.writeC(8); // 力量+1
			_os.writeC(1); // 力量+1

			_os.writeC(39);
			_os.writeS("\\f2使用需魔法結晶體100個");
		}

		if (_item.getItemId() >= 640407 && _item.getItemId() <= 640409) { // 潘朵拉遠攻魔石
			_os.writeC(14); // 體力上限+25
			_os.writeH(25); // 體力上限+25

			_os.writeC(35); // 遠距離攻擊+2
			_os.writeC(2); // 遠距離攻擊+2

			_os.writeC(0x20); // 魔力上限+25
			_os.writeH(25); // 魔力上限+25

			_os.writeC(37); // 體力回復量+1
			_os.writeC(1); // 體力回復量+1

			_os.writeC(38); // 魔力回復量+1
			_os.writeC(1); // 魔力回復量+1

			_os.writeC(9); // 敏捷+1
			_os.writeC(1); // 敏捷+1

			_os.writeC(39);
			_os.writeS("\\f2使用需魔法結晶體100個");
		}

		if (_item.getItemId() >= 640410 && _item.getItemId() <= 640412) { // 潘朵拉魔攻魔石
			_os.writeC(0x20); // 魔力上限+50
			_os.writeH(50); // 魔力上限+50

			_os.writeC(38); // 魔力回復量+3
			_os.writeC(3); // 魔力回復量+3

			_os.writeC(12); // 智力+1
			_os.writeC(1); // 智力+1

			_os.writeC(17); // 魔攻+2
			_os.writeC(2); // 魔攻+2

			_os.writeC(39);
			_os.writeS("\\f2使用需魔法結晶體100個");
		}

		if (_item.getItemId() >= 640413 && _item.getItemId() <= 640415) { // 潘朵拉防禦魔石
			_os.writeC(14); // 體力上限+30
			_os.writeH(30); // 體力上限+30

			_os.writeC(0x20);// 魔力上限+30
			_os.writeH(30); // 魔力上限+30

			_os.writeC(38); // 魔力回復量+3
			_os.writeC(3); // 魔力回復量+3

			_os.writeC(56); // 額外防禦+5
			_os.writeC(5); // 額外防禦+5

			_os.writeC(15); // 魔法防禦+10
			_os.writeH(10); // 魔法防禦+10

			_os.writeC(63); // 傷害減免+2
			_os.writeC(1); // 傷害減免+2

			_os.writeC(39);
			_os.writeS("\\f2使用需魔法結晶體100個");
		}

		// if (_item.getItemId() == 82504) {// 龍門憑證
		// String npcname =
		// NpcTable.get().getNpcName(_itemInstance.getInnNpcId());//龍門名稱
		// _os.writeC(39);
		// _os.writeS(npcname);
		// _os.writeC(39);
		// _os.writeS("副本編號:" + _itemInstance.getKeyId());
		// _os.writeC(39);
		// _os.writeS("到期時間:(" + _itemInstance.getDueTime() + ")");
		// }

		_os.writeC(23);
		_os.writeC(_item.getMaterial());
		_os.writeD(_itemInstance.getWeight());

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}

		// 0X3D=61 X年X月X日 以后自动删除
		if (_itemInstance.get_time() != null) {
			// 1997 1 /1 16:00
			try {
				_os.writeC(61);
				final Date baseDate = new SimpleDateFormat("yyyy-MM-dd HH:mm")
						.parse("2019-12-31 23:00");
				_os.writeD((((int) (_itemInstance.get_time().getTime() / 1000)*6 - (int) (baseDate
						.getTime() / 1000)*6)));
			} catch (final ParseException e) {
				// log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

		return _os;
	}

	/**
	 * 寵物防具
	 * 
	 * @return
	 */
	private BinaryOutputStream petarmor(L1PetItem petItem) {
		_os.writeC(19);
		int ac = petItem.getAddAc();
		if (ac < 0) {
			ac = Math.abs(ac);
		}
		_os.writeC(ac);

		_os.writeC(_item.getMaterial());
		_os.writeC(-1); // 飾品級別 - 0:上等 1:中等 2:初等 3:特等
		_os.writeD(_itemInstance.getWeight());

		if (petItem.getHitModifier() != 0) {
			_os.writeC(5);
			_os.writeC(petItem.getHitModifier());
		}

		if (petItem.getDamageModifier() != 0) {
			_os.writeC(6);
			_os.writeC(petItem.getDamageModifier());
		}

		if (petItem.isHigher()) {
			// _os.writeC(7);
			// _os.writeC(128);
			_os.writeC(39);
			_os.writeS("高等寵物限定");
		}

		if (petItem.getAddStr() != 0) {
			_os.writeC(8);
			_os.writeC(petItem.getAddStr());
		}
		if (petItem.getAddDex() != 0) {
			_os.writeC(9);
			_os.writeC(petItem.getAddDex());
		}
		if (petItem.getAddCon() != 0) {
			_os.writeC(10);
			_os.writeC(petItem.getAddCon());
		}
		if (petItem.getAddWis() != 0) {
			_os.writeC(11);
			_os.writeC(petItem.getAddWis());
		}
		if (petItem.getAddInt() != 0) {
			_os.writeC(12);
			_os.writeC(petItem.getAddInt());
		}

		if (petItem.getAddHp() != 0) {
			_os.writeC(14);
			_os.writeH(petItem.getAddHp());
		}
		if (petItem.getAddMp() != 0) {
			_os.writeC(32);
			_os.writeH(petItem.getAddMp());
		}

		if (petItem.getAddMr() != 0) {
			_os.writeC(15);
			_os.writeH(petItem.getAddMr());
		}

		if (petItem.getAddSp() != 0) {
			_os.writeC(17);
			_os.writeC(petItem.getAddSp());
		}

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}
		return _os;
	}

	/**
	 * 寵物武器
	 * 
	 * @return
	 */
	private BinaryOutputStream petweapon(L1PetItem petItem) {
		_os.writeC(1);
		_os.writeC(0);
		_os.writeC(0);
		_os.writeC(_item.getMaterial());
		_os.writeD(_itemInstance.getWeight());

		if (petItem.getHitModifier() != 0) {
			_os.writeC(5);
			_os.writeC(petItem.getHitModifier());
		}

		if (petItem.getDamageModifier() != 0) {
			_os.writeC(6);
			_os.writeC(petItem.getDamageModifier());
		}

		if (petItem.isHigher()) {
			// _os.writeC(7);
			// _os.writeC(128);
			_os.writeC(39);
			_os.writeS("高等寵物限定");
		}

		if (petItem.getAddStr() != 0) {
			_os.writeC(8);
			_os.writeC(petItem.getAddStr());
		}
		if (petItem.getAddDex() != 0) {
			_os.writeC(9);
			_os.writeC(petItem.getAddDex());
		}
		if (petItem.getAddCon() != 0) {
			_os.writeC(10);
			_os.writeC(petItem.getAddCon());
		}
		if (petItem.getAddWis() != 0) {
			_os.writeC(11);
			_os.writeC(petItem.getAddWis());
		}
		if (petItem.getAddInt() != 0) {
			_os.writeC(12);
			_os.writeC(petItem.getAddInt());
		}

		if (petItem.getAddHp() != 0) {
			_os.writeC(14);
			_os.writeH(petItem.getAddHp());
		}
		if (petItem.getAddMp() != 0) {
			_os.writeC(32);
			_os.writeH(petItem.getAddMp());
		}

		if (petItem.getAddMr() != 0) {
			_os.writeC(15);
			_os.writeH(petItem.getAddMr());
		}

		if (petItem.getAddSp() != 0) {
			_os.writeC(17);
			_os.writeC(petItem.getAddSp());
		}

		if (_itemInstance.getItem().isTradable()) {
			_os.writeC(130);
			_os.writeD(1);
		}

		// 2017/04/21
		ArrayList<String> as = new ArrayList<String>();
		try {
			for (String s : WilliamItemMessage.getItemInfo(_itemInstance)) {
				if (s != null && !s.isEmpty()) {
					_os.writeC(39);
					_os.writeS(s);
				}
			}
		} finally {
			as.clear();
		}
		return _os;
	}

	// 強化飾品設置 -> DB化
	// /**
	// * [0]:MP [1]:HP [2]:藥水回復量% [3]:近距離傷害 [4]:遠距離傷害 [5]:傷害減免 [6]:魔法命中
	// [7]:PVP傷害
	// * [8]:防禦 [9]:魔攻 [10]:魔防 [11]:PVP傷害值減少
	// *
	// * @return
	// */
	// private int[] greater() {
	// int level = _itemInstance.getEnchantLevel();
	// if (level < 0) {// 強化負值不顯示加成
	// level = 0;
	// }
	//
	// int[] rint = new int[12];
	// switch (_itemInstance.getItem().get_greater()) {
	// case 0:// 0:耐性(耳環/項鏈)
	// // { MP,HP,藥水回復量%, 近距離傷害, 遠距離傷害, 傷害減免 , 魔法命中, PVP傷害, 防禦, 魔攻, 魔防,
	// // PVP傷害值減少 }
	// switch (level) {
	// case 0:
	// break;
	// case 1:
	// rint = new int[] { 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 2:
	// rint = new int[] { 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 3:
	// rint = new int[] { 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 4:
	// rint = new int[] { 0, 30, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 5:
	// rint = new int[] { 0, 40, 4, 2, 2, 1, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 6:
	// rint = new int[] { 0, 40, 6, 4, 4, 2, 2, 0, 0, 0, 0, 0 };
	// break;
	// case 7:// { MP,HP,藥水回復量%, 近距離傷害, 遠距離傷害, 傷害減免 , 魔法命中 , PVP傷害, 防禦, 魔攻,
	// // 魔防, PVP傷害值減少 }
	// rint = new int[] { 0, 50, 10, 5, 5, 3, 4, 1, 0, 0, 0, 0 };
	// break;
	// case 8:
	// rint = new int[] { 0, 50, 14, 6, 6, 3, 8, 2, 0, 0, 0, 0 };
	// break;
	// case 9:
	// rint = new int[] { 0, 100, 16, 8, 8, 5, 10, 5, 0, 0, 10, 0 };
	// break;
	// }
	// break;
	//
	// case 1:// 1:熱情(戒指)
	// switch (level) {
	// case 0:
	// break;
	// case 1:// { MP,HP,藥水回復量%, 近距離傷害, 遠距離傷害, 傷害減免, 魔法命中 , PVP傷害, 防禦, 魔攻,
	// // 魔防, PVP傷害值減少 }
	// rint = new int[] { 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 2:// { MP,HP,藥水回復量%, 近距離傷害, 遠距離傷害, 傷害減免, 魔法命中 , PVP傷害, 防禦 , 魔攻,
	// // 魔防, PVP傷害值減少 }
	// rint = new int[] { 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 3:
	// rint = new int[] { 0, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 4:
	// rint = new int[] { 0, 30, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 5:// { MP,HP,藥水回復量%, 近距離傷害, 遠距離傷害, 傷害減免, 魔法命中 , PVP傷害, 防禦 , 魔攻,
	// // 魔防, PVP傷害值減少}
	// rint = new int[] { 0, 40, 0, 3, 3, 3, 0, 0, -1, 0, 0, 0 };
	// break;
	// case 6:
	// rint = new int[] { 0, 40, 0, 4, 4, 3, 0, 0, -3, 1, 0, 0 };
	// break;
	// case 7:// { MP,HP,藥水回復量%, 近距離傷害, 遠距離傷害, 傷害減免, 魔法命中 , PVP傷害, 防禦 , 魔攻,
	// // 魔防, PVP傷害值減少 }
	// rint = new int[] { 0, 50, 0, 4, 4, 4, 0, 1, -4, 2, 0, 0 };
	// break;
	// case 8:
	// rint = new int[] { 0, 50, 0, 5, 5, 5, 0, 2, -5, 4, 0, 0 };
	// break;
	// case 9:
	// rint = new int[] { 0, 100, 0, 7, 7, 7, 0, 5, -7, 5, 0, 7 };
	// break;
	// }
	// break;
	//
	// case 2:// 2:意志(皮帶)
	// switch (level) {
	// case 0:
	// break;
	// case 1:// { MP,HP,藥水回復量%, 近距離傷害, 遠距離傷害, 傷害減免, 魔法命中 , PVP傷害, 防禦 , 魔攻,
	// // 魔防, PVP傷害值減少}
	// rint = new int[] { 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 2:// { MP,HP,藥水回復量%, 近距離傷害, 遠距離傷害, 傷害減免 , 魔法命中 , PVP傷害, 防禦 ,
	// // 魔攻, 魔防, PVP傷害值減少}
	// rint = new int[] { 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 3:
	// rint = new int[] { 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 4:
	// rint = new int[] { 30, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 };
	// break;
	// case 5:
	// rint = new int[] { 40, 0, 0, 0, 0, 2, 0, 0, 0, 0, 3, 0 };
	// break;
	// case 6:
	// rint = new int[] { 40, 0, 0, 0, 0, 4, 0, 0, -1, 0, 5, 0 };
	// break;
	// case 7:// { MP,HP,藥水回復量%, 近距離傷害, 遠距離傷害, 傷害減免 , 魔法命中 , PVP傷害, 防禦 ,
	// // 魔攻, 魔防, PVP傷害值減少}
	// rint = new int[] { 50, 0, 0, 0, 0, 4, 0, 0, -2, 0, 7, 1 };
	// break;
	// case 8:
	// rint = new int[] { 50, 0, 0, 0, 0, 5, 0, 0, -3, 0, 10, 2 };
	// break;
	// case 9:
	// rint = new int[] { 75, 0, 0, 0, 0, 7, 0, 0, -5, 0, 15, 5 };
	// break;
	// }
	// break;
	// }
	//
	// return rint;
	// }

	public String getQuality1() {
		return ArmorSetTable.get().getQuality1(_itemInstance.getItemId());
	}

	// public String getQuality2() {
	// return ArmorSetTable.get().getQuality2(_itemInstance.getItemId());
	// }
	//
	// public String getQuality3() {
	// return ArmorSetTable.get().getQuality3(_itemInstance.getItemId());
	// }
	//
	// public String getQuality4() {
	// return ArmorSetTable.get().getQuality4(_itemInstance.getItemId());
	// }
	//
	// public String getQuality5() {
	// return ArmorSetTable.get().getQuality5(_itemInstance.getItemId());
	// }
	//
	// public String getQuality6() {
	// return ArmorSetTable.get().getQuality6(_itemInstance.getItemId());
	// }
	//
	// public String getQuality7() {
	// return ArmorSetTable.get().getQuality7(_itemInstance.getItemId());
	// }
	//
	// public String getQuality8() {
	// return ArmorSetTable.get().getQuality8(_itemInstance.getItemId());
	// }
	//
	// public String getQuality9() {
	// return ArmorSetTable.get().getQuality9(_itemInstance.getItemId());
	// }

	public boolean isMatch() { // src008
		return _itemInstance.isMatch();
	}

	private String set_hole_name(int hole) { // src039

		final L1StonePower l1StonePower = StonePowerTable.getInstance().get(
				hole);
		final StringBuilder stringBuilder = new StringBuilder();

		if (l1StonePower != null) {
			stringBuilder.append(l1StonePower.get_Note() + " ");
		} else {
			stringBuilder.append("○");
		}
		return stringBuilder.toString();
	}

	private String set_rune_name(int rune) {

		final L1SuperRune l1SuperRune = SuperRuneTable.getInstance().get(rune);

		final StringBuilder stringBuilder = new StringBuilder();

		if (l1SuperRune != null) {

			stringBuilder.append(l1SuperRune.get_Note());

			final int ac = l1SuperRune.getAc();
			if (ac != 0) {
				stringBuilder.append(ac);
			}

			final int hp = l1SuperRune.getHp();
			if (hp != 0) {
				stringBuilder.append(hp);
			}

			final int mp = l1SuperRune.getMp();
			if (mp != 0) {
				stringBuilder.append(mp);
			}

			final int hpr = l1SuperRune.getHpr();
			if (hpr != 0) {
				stringBuilder.append(hpr);
			}

			final int mpr = l1SuperRune.getMpr();
			if (mpr != 0) {
				stringBuilder.append(mpr);
			}

			final int str = l1SuperRune.getStr();
			if (str != 0) {
				stringBuilder.append(str);
			}

			final int con = l1SuperRune.getCon();
			if (con != 0) {
				stringBuilder.append(con);
			}

			final int dex = l1SuperRune.getDex();
			if (dex != 0) {
				stringBuilder.append(dex);
			}

			final int wis = l1SuperRune.getWis();
			if (wis != 0) {
				stringBuilder.append(wis);
			}

			final int cha = l1SuperRune.getCha();
			if (cha != 0) {
				stringBuilder.append(cha);
			}

			final int inter = l1SuperRune.getInt();
			if (inter != 0) {
				stringBuilder.append(inter);
			}

			final int sp = l1SuperRune.getSp();
			if (sp != 0) {
				stringBuilder.append(sp);
			}

			final int mr = l1SuperRune.getMr();
			if (mr != 0) {
				stringBuilder.append(mr);
			}

			final int hitModifer = l1SuperRune.getHitModifier();
			if (hitModifer != 0) {
				stringBuilder.append(hitModifer);
			}

			final int dmgModifer = l1SuperRune.getDmgModifier();
			if (dmgModifer != 0) {
				stringBuilder.append(dmgModifer);
			}

			final int bowHit = l1SuperRune.getBowHitModifier();
			if (bowHit != 0) {
				stringBuilder.append(bowHit);
			}

			final int bowDmg = l1SuperRune.getBowDmgModifier();
			if (bowDmg != 0) {
				stringBuilder.append(bowDmg);
			}

			final int magicDmg = l1SuperRune.getMagicDmgModifier();
			if (magicDmg != 0) {
				stringBuilder.append(magicDmg);
			}

			final int magicReduction = l1SuperRune.getMagicDmgReduction();
			if (magicReduction != 0) {
				stringBuilder.append(magicReduction);
			}

			final int reductionDmg = l1SuperRune.getReductionDmg();
			if (reductionDmg != 0) {
				stringBuilder.append(reductionDmg);
			}

			// final int registStun = l1SuperRune.getRegistStun();
			// if (registStun != 0) {
			// stringBuilder.append(registStun);
			// }
			//
			// final int registSustain = l1SuperRune.getRegistSustain();
			// if (registSustain != 0) {
			// stringBuilder.append(registSustain);
			// }
			//
			// final int registStone = l1SuperRune.getRegistStone();
			// if (registStone != 0) {
			// stringBuilder.append(registStone);
			// }
			//
			// final int registSleep = l1SuperRune.getRegistSleep();
			// if (registSleep != 0) {
			// stringBuilder.append(registSleep);
			// }
			//
			// final int registFreeze = l1SuperRune.getRegistFreeze();
			// if (registFreeze != 0) {
			// stringBuilder.append(registFreeze);
			// }
			//
			// final int registBlind = l1SuperRune.getRegistBlind();
			// if (registBlind != 0) {
			// stringBuilder.append(registBlind);
			// }

		} else {
			stringBuilder.append("未附魔");
		}

		return stringBuilder.toString();
	}

	private String set_rune_name2(int rune) {

		final StringBuilder stringBuilder = new StringBuilder();

		if (rune == 0) {
			stringBuilder.append("未附魔");
		} else if (rune == 1) {
			stringBuilder.append("15%近戰還擊80傷害");
		} else if (rune == 2) {
			stringBuilder.append("15%遠攻還擊80傷害");
		}

		return stringBuilder.toString();
	}

	/**
	 * 強化擴充能力
	 * <p>
	 * 適用↓↓↓<br>
	 * 防具-飾品-輔助-武器<br>
	 * </p>
	 */
	private void ItemAbility() {
		if (_itemInstance.getUpdateStr() != 0
				|| _itemInstance.getUpdateDex() != 0
				|| _itemInstance.getUpdateCon() != 0
				|| _itemInstance.getUpdateWis() != 0
				|| _itemInstance.getUpdateInt() != 0
				|| _itemInstance.getUpdateCha() != 0
				|| _itemInstance.getUpdateHp() != 0
				|| _itemInstance.getUpdateMp() != 0
				|| _itemInstance.getUpdateEarth() != 0
				|| _itemInstance.getUpdateWind() != 0
				|| _itemInstance.getUpdateWater() != 0
				|| _itemInstance.getUpdateFire() != 0
				|| _itemInstance.getUpdateMr() != 0
				|| _itemInstance.getUpdateAc() != 0
				|| _itemInstance.getUpdateHpr() != 0
				|| _itemInstance.getUpdateMpr() != 0
				|| _itemInstance.getUpdateSp() != 0
				|| _itemInstance.getUpdateDmgModifier() != 0
				|| _itemInstance.getUpdateHitModifier() != 0
				|| _itemInstance.getUpdateBowDmgModifier() != 0
				|| _itemInstance.getUpdateBowHitModifier() != 0
				|| _itemInstance.getUpdatePVPdmg() != 0
				|| _itemInstance.getUpdatePVPdmg_R() != 0) {
			_os.writeC(0x27);
			_os.writeS("\\f3擴充能力：");
		}

		final int updatepvpdmg = _itemInstance.getUpdatePVPdmg();// pvp攻擊
		if (updatepvpdmg != 0) {
			_os.writeC(0x27);
			_os.writeS("\\f2PVP 額外傷害 +" + updatepvpdmg);
			// _os.writeC(59);
			// _os.writeC(updatepvpdmg);
		}

		final int updatepvpdmg_r = _itemInstance.getUpdatePVPdmg_R();// pvp減免
		if (updatepvpdmg_r != 0) {
			_os.writeC(0x27);
			_os.writeS("\\f2PVP 傷害減免 +" + updatepvpdmg_r);
			// _os.writeC(60);
			// _os.writeC(updatepvpdmg_r);
		}

		final int updateac = _itemInstance.getUpdateAc();// 防禦
		if (updateac != 0) {
			_os.writeC(56); // 額外防禦
			_os.writeC(updateac);
		}

		final int updatedmg = _itemInstance.getUpdateDmgModifier();// 近戰攻擊
		if (updatedmg != 0) {
			_os.writeC(47);
			_os.writeC(updatedmg);
		}

		final int updatehit = _itemInstance.getUpdateHitModifier();// 近戰命中
		if (updatehit != 0) {
			_os.writeC(48);
			_os.writeC(updatehit);
		}

		final int updatebowdmg = _itemInstance.getUpdateBowDmgModifier();// 遠攻攻擊
		if (updatebowdmg != 0) {
			_os.writeC(35);
			_os.writeC(updatebowdmg);
		}

		final int updatebowhit = _itemInstance.getUpdateBowHitModifier();// 遠攻命中
		if (updatebowhit != 0) {
			_os.writeC(24);
			_os.writeC(updatebowhit);
		}

		final int updatestr = _itemInstance.getUpdateStr();// 力量
		if (updatestr != 0) {
			_os.writeC(8);
			_os.writeC(updatestr);
		}

		final int updatedex = _itemInstance.getUpdateDex();// 敏捷
		if (updatedex != 0) {
			_os.writeC(9);
			_os.writeC(updatedex);
		}

		final int updatecon = _itemInstance.getUpdateCon();// 體質
		if (updatecon != 0) {
			_os.writeC(10);
			_os.writeC(updatecon);
		}

		final int updatewis = _itemInstance.getUpdateWis();// 精神
		if (updatewis != 0) {
			_os.writeC(11);
			_os.writeC(updatewis);
		}

		final int updateint = _itemInstance.getUpdateInt();// 智力
		if (updateint != 0) {
			_os.writeC(12);
			_os.writeC(updateint);
		}

		final int updatecha = _itemInstance.getUpdateCha();// 魅力
		if (updatecha != 0) {
			_os.writeC(13);
			_os.writeC(updatecha);
		}

		final int updatehp = _itemInstance.getUpdateHp();// 血量
		if (updatehp != 0) {
			_os.writeC(14);
			_os.writeH(updatehp);
		}

		final int updatemp = _itemInstance.getUpdateMp();// 魔量
		if (updatemp != 0) {
			_os.writeC(0x20);
			_os.writeH(updatemp);
		}

		final int updatehpr = _itemInstance.getUpdateHpr();// 回血
		if (updatehpr != 0) {
			_os.writeC(37);
			_os.writeC(updatehpr);
		}

		final int updatempr = _itemInstance.getUpdateMpr();// 回魔
		if (updatempr != 0) {
			_os.writeC(38);
			_os.writeC(updatempr);
		}

		final int updatemr = _itemInstance.getUpdateMr();// 抗魔
		if (updatemr != 0) {
			_os.writeC(15);
			_os.writeH(updatemr);
		}

		final int updatesp = _itemInstance.getUpdateSp();// 魔法攻擊
		if (updatesp != 0) {
			_os.writeC(17);
			_os.writeC(updatesp);
		}

		final int updatefire = _itemInstance.getUpdateFire();// 火屬性
		if (updatefire != 0) {
			_os.writeC(27);
			_os.writeC(updatefire);
		}

		final int updatewater = _itemInstance.getUpdateWater();// 水屬性
		if (updatewater != 0) {
			_os.writeC(28);
			_os.writeC(updatewater);
		}

		final int updatewind = _itemInstance.getUpdateWind();// 風屬性
		if (updatewind != 0) {
			_os.writeC(29);
			_os.writeC(updatewind);
		}

		final int updateearth = _itemInstance.getUpdateEarth();// 地屬性
		if (updateearth != 0) {
			_os.writeC(30);
			_os.writeC(updateearth);
		}
	}

	/**
	 * 套裝能力顯示
	 * <p>
	 * 適用↓↓↓<br>
	 * 防具-飾品-副助<br>
	 * </p>
	 */
	private void checkArmorSet() {
		// if ((ArmorSetTable.get().checkArmorSet(_itemInstance.getItemId())) &&
		// (getQuality1() != null) && (!getQuality1().equals(""))) { // src008
		if ((ArmorSetTable.get().checkArmorSet(_itemInstance.getItemId()))
				&& (_item.get_mode()[0] != 0 // 套裝效果:力量增加
						|| _item.get_mode()[1] != 0 // 套裝效果:敏捷增加
						|| _item.get_mode()[2] != 0 // 套裝效果:體質增加
						|| _item.get_mode()[3] != 0 // 套裝效果:精神增加
						|| _item.get_mode()[4] != 0 // 套裝效果:智力增加
						|| _item.get_mode()[5] != 0 // 套裝效果:魅力增加
						|| _item.get_mode()[6] != 0 // 套裝效果:HP增加
						|| _item.get_mode()[7] != 0 // 套裝效果:MP增加
						|| _item.get_mode()[8] != 0 // 套裝效果:抗魔增加
						|| _item.get_mode()[9] != 0 // 套裝效果:魔攻
						|| _item.get_mode()[10] != 0 // 套裝效果:加速效果
						|| _item.get_mode()[11] != 0 // 套裝效果:火屬性增加
						|| _item.get_mode()[12] != 0 // 套裝效果:水屬性增加
						|| _item.get_mode()[13] != 0 // 套裝效果:風屬性增加
						|| _item.get_mode()[14] != 0 // 套裝效果:地屬性增加

						// || _item.get_mode()[15] != 0 // 套裝效果:寒冰耐性增加
						// || _item.get_mode()[16] != 0 // 套裝效果:石化耐性增加
						// || _item.get_mode()[17] != 0 // 套裝效果:睡眠耐性增加
						// || _item.get_mode()[18] != 0 // 套裝效果:暗闇耐性增加
						// || _item.get_mode()[19] != 0 // 套裝效果:暈眩耐性增加
						// || _item.get_mode()[20] != 0 // 套裝效果:支撐耐性增加

						|| _item.get_mode()[15] != 0 // 套裝效果:減免所有傷害
						|| _item.get_mode()[16] != 0 // 套裝效果:技術耐性增加
						|| _item.get_mode()[17] != 0 // 套裝效果:精靈耐性增加
						|| _item.get_mode()[18] != 0 // 套裝效果:龍屬耐性增加
						|| _item.get_mode()[19] != 0 // 套裝效果:恐怖耐性增加
						|| _item.get_mode()[20] != 0 // 套裝效果:全部耐性增加

						|| _item.get_mode()[21] != 0 // 套裝效果:回血量增加
						|| _item.get_mode()[22] != 0 // 套裝效果:回魔量增加
						|| _item.get_mode()[23] != 0 // 套裝效果:套裝增加物理傷害
						|| _item.get_mode()[24] != 0 // 套裝效果:套裝減免物理傷害
						|| _item.get_mode()[25] != 0 // 套裝效果:套裝增加魔法傷害
						|| _item.get_mode()[26] != 0 // 套裝效果:套裝減免魔法傷害
						|| _item.get_mode()[27] != 0 // 套裝效果:套裝增加弓的物理傷害
						|| _item.get_mode()[28] != 0 // 套裝效果:套裝增加近距離命中率
						|| _item.get_mode()[29] != 0 // 套裝效果:套裝增加遠距離命中率
						|| _item.get_mode()[30] != 0 // 套裝效果:套裝增加魔法爆擊率
						|| _item.get_mode()[31] != 0 // 套裝效果:套裝增加防禦
						|| _item.get_mode()[32] > 0 // 套裝效果:套裝變身
						|| _item.get_mode()[33] != 0 // 套裝效果:套裝變身
						|| _item.get_mode()[34] != 0 // 套裝效果:套裝變身
						|| _item.get_mode()[35] != 0 // 套裝效果:套裝變身
						|| _item.get_mode()[36] != 0 // 套裝效果:套裝變身
						|| _item.get_mode()[37] != 0 // 套裝效果:套裝變身
						|| _item.get_mode()[38] != 0 // 套裝效果:套裝變身
						|| _item.get_mode()[39] != 0 // 套裝效果:套裝變身
						|| _item.get_mode()[40] != 0 // 套裝效果:套裝變身
				        || _item.get_mode()[41] != 0 // 套裝效果:套裝變身
				)) {
			// 0x45=69 [額外組合] // 0:結尾 1:橘色 2:灰色
			_os.writeC(69);
			if (isMatch() && _itemInstance.isEquipped()) { // 完成套裝並且是使用中的就顯示1:橘色
				_os.writeC(1); // 1:橘色
			} else {
				_os.writeC(2); // 2:灰色
			}

			if (_item.get_mode()[32] > 0 && _item.get_mode()[33] > 0) { // 變身
				_os.writeC(71);
				_os.writeH(_item.get_mode()[33]);
			} else if (_item.get_mode()[32] > 0 && _item.get_mode()[33] <= 0) {
				_os.writeC(0x27);
				_os.writeS("變身效果");
			}
			if (_item.get_mode()[31] != 0) { // 防禦
				_os.writeC(56); // 額外防禦
				_os.writeC(-_item.get_mode()[31]); // 多個 - 號
			}
			if (_item.get_mode()[0] != 0) { // 力量
				_os.writeC(0x08);
				_os.writeC(_item.get_mode()[0]);
			}
			if (_item.get_mode()[1] != 0) { // 敏捷
				_os.writeC(0x09);
				_os.writeC(_item.get_mode()[1]);
			}
			if (_item.get_mode()[2] != 0) { // 體質
				_os.writeC(0x0a);
				_os.writeC(_item.get_mode()[2]);
			}
			if (_item.get_mode()[3] != 0) { // 精神
				_os.writeC(0x0b);
				_os.writeC(_item.get_mode()[3]);
			}
			if (_item.get_mode()[4] != 0) { // 智力
				_os.writeC(0x0c);
				_os.writeC(_item.get_mode()[4]);
			}
			if (_item.get_mode()[5] != 0) { // 魅力
				_os.writeC(0x0d);
				_os.writeC(_item.get_mode()[5]);
			}
			if (_item.get_mode()[6] != 0) { // 血量上限
				_os.writeC(0x0e);
				_os.writeH(_item.get_mode()[6]);
			}
			if (_item.get_mode()[7] != 0) { // 魔量上限
				_os.writeC(0x20);
				_os.writeH(_item.get_mode()[7]);
			}
			if (_item.get_mode()[8] != 0) { // 抗魔
				_os.writeC(0x0f);
				_os.writeH(_item.get_mode()[8]);
			}
			if (_item.get_mode()[9] != 0) { // 魔攻
				_os.writeC(0x11);
				_os.writeC(_item.get_mode()[9]);
			}
			if (_item.get_mode()[10] != 0) { // 加速效果
				_os.writeC(0x12);
			}
			if (_item.get_mode()[11] != 0) { // 火屬性
				_os.writeC(0x1b);
				_os.writeC(_item.get_mode()[11]);
			}
			if (_item.get_mode()[12] != 0) { // 水屬性
				_os.writeC(0x1c);
				_os.writeC(_item.get_mode()[12]);
			}
			if (_item.get_mode()[13] != 0) { // 風屬性
				_os.writeC(0x1d);
				_os.writeC(_item.get_mode()[13]);
			}
			if (_item.get_mode()[14] != 0) { // 地屬性
				_os.writeC(0x1e);
				_os.writeC(_item.get_mode()[14]);
			}
			// if (_item.get_mode()[15] != 0) { // 寒冰耐性
			// _os.writeC(33);
			// _os.writeC(1);
			// _os.writeC(_item.get_mode()[15]);
			// }
			// if (_item.get_mode()[16] != 0) { // 石化耐性
			// _os.writeC(33);
			// _os.writeC(2);
			// _os.writeC(_item.get_mode()[16]);
			// }
			// if (_item.get_mode()[17] != 0) { // 睡眠耐性
			// _os.writeC(33);
			// _os.writeC(3);
			// _os.writeC(_item.get_mode()[17]);
			// }
			// if (_item.get_mode()[18] != 0) { // 暗黑耐性
			// _os.writeC(33);
			// _os.writeC(4);
			// _os.writeC(_item.get_mode()[18]);
			// }
			// if (_item.get_mode()[19] != 0) { // 昏迷耐性
			// _os.writeC(33);
			// _os.writeC(5);
			// _os.writeC(_item.get_mode()[19]);
			// }
			// if (_item.get_mode()[20] != 0) { // 支撐耐性
			// _os.writeC(33);
			// _os.writeC(6);
			// _os.writeC(_item.get_mode()[20]);
			// }

			if (_item.get_mode()[15] != 0) { // 減免所有傷害
				_os.writeC(63);
				_os.writeC(_item.get_mode()[15]);
			}

			if (_item.get_mode()[16] != 0) { // 技術耐性
				_os.writeC(117);
				_os.writeC(_item.get_mode()[16]);
			}
			if (_item.get_mode()[17] != 0) { // 精靈耐性
				_os.writeC(118);
				_os.writeC(_item.get_mode()[17]);
			}
			if (_item.get_mode()[18] != 0) { // 龍屬耐性
				_os.writeC(119);
				_os.writeC(_item.get_mode()[18]);
			}
			if (_item.get_mode()[19] != 0) { // 恐怖耐性
				_os.writeC(120);
				_os.writeC(_item.get_mode()[19]);
			}
			if (_item.get_mode()[20] != 0) { // 全部耐性
				_os.writeC(121);
				_os.writeC(_item.get_mode()[20]);
			}

			if (_item.get_mode()[21] != 0) { // 體力回復量
				_os.writeC(37);
				_os.writeC(_item.get_mode()[21]);
			}
			if (_item.get_mode()[22] != 0) { // 魔力回復量
				_os.writeC(38);
				_os.writeC(_item.get_mode()[22]);
			}
			if (_item.get_mode()[24] != 0) { // 減免物理傷害
				// _os.writeC(63);
				// _os.writeC(_item.get_mode()[24]);
				_os.writeC(39);
				_os.writeS("物理減傷 +" + _item.get_mode()[24]);
			}
			if (_item.get_mode()[23] != 0) { // 增加物理傷害 【近距離傷害】
				_os.writeC(47);
				_os.writeC(_item.get_mode()[23]);
			}
			if (_item.get_mode()[28] != 0) { // 增加近距離命中率
				_os.writeC(48);
				_os.writeC(_item.get_mode()[28]);
			}
			if (_item.get_mode()[27] != 0) { // 增加弓的物理傷害 【遠距離傷害】
				_os.writeC(35);
				_os.writeC(_item.get_mode()[27]);
			}
			if (_item.get_mode()[29] != 0) { // 增加遠距離命中率
				_os.writeC(24);
				_os.writeC(_item.get_mode()[29]);
			}
			if (_item.get_mode()[25] != 0) { // 增加魔法傷害
				_os.writeC(39);
				_os.writeS("魔法增傷 +" + _item.get_mode()[25]);
			}
			if (_item.get_mode()[26] != 0) { // 減免魔法傷害
				_os.writeC(39);
				_os.writeS("魔法減傷 +" + _item.get_mode()[26]);
			}
			if (_item.get_mode()[30] != 0) { // 增加魔法爆擊率
				_os.writeC(39);
				_os.writeS("魔法爆擊 +" + _item.get_mode()[30]);
			}
			if (_item.get_mode()[34] != 0) { // 增加魔法爆擊率
				_os.writeC(39);
				_os.writeS("技術命中 +" + _item.get_mode()[34]);
			}
			if (_item.get_mode()[35] != 0) { // 增加魔法爆擊率
				_os.writeC(39);
				_os.writeS("精靈命中 +" + _item.get_mode()[35]);
			}
			if (_item.get_mode()[36] != 0) { // 增加魔法爆擊率
				_os.writeC(39);
				_os.writeS("恐怖命中 +" + _item.get_mode()[36]);
			}
			if (_item.get_mode()[37] != 0) { // 增加魔法爆擊率
				_os.writeC(39);
				_os.writeS("龍屬命中 +" + _item.get_mode()[37]);
			}
			if (_item.get_mode()[38] != 0) { // 增加魔法爆擊率
				_os.writeC(39);
				_os.writeS("全部命中 +" + _item.get_mode()[38]);
			}
			if (_item.get_mode()[39] != 0) { // 增加魔法爆擊率
				_os.writeC(39);
				_os.writeS("PVP傷害 +" + _item.get_mode()[39]);
			}
			if (_item.get_mode()[40] != 0) { // 增加魔法爆擊率
				_os.writeC(39);
				_os.writeS("PVP減免 +" + _item.get_mode()[40]);
			}
			if (_item.get_mode()[41] != 0) { // 增加魔法爆擊率
				_os.writeC(39);
				_os.writeS("殷海薩祝消耗減少 +" + _item.get_mode()[41]);
			}
			if ((getQuality1() != null) && (!getQuality1().equals(""))) {
				_os.writeC(39);
				_os.writeS(getQuality1());
			}
			// if ((getQuality2() != null) && (!getQuality2().equals(""))) {
			// _os.writeC(39);
			// _os.writeS(getQuality2());
			// }
			// if ((getQuality3() != null) && (!getQuality3().equals(""))) {
			// _os.writeC(39);
			// _os.writeS(getQuality3());
			// }
			// if ((getQuality4() != null) && (!getQuality4().equals(""))) {
			// _os.writeC(39);
			// _os.writeS(getQuality4());
			// }
			// if ((getQuality5() != null) && (!getQuality5().equals(""))) {
			// _os.writeC(39);
			// _os.writeS(getQuality5());
			// }
			// if ((getQuality6() != null) && (!getQuality6().equals(""))) {
			// _os.writeC(39);
			// _os.writeS(getQuality6());
			// }
			// if ((getQuality7() != null) && (!getQuality7().equals(""))) {
			// _os.writeC(39);
			// _os.writeS(getQuality7());
			// }
			// if ((getQuality8() != null) && (!getQuality8().equals(""))) {
			// _os.writeC(39);
			// _os.writeS(getQuality8());
			// }
			// if ((getQuality9() != null) && (!getQuality9().equals(""))) {
			// _os.writeC(39);
			// _os.writeS(getQuality9());
			// }

			// 0x45=69 [額外組合] // 0:結尾 1:橘色 2:灰色
			_os.writeC(69);
			_os.writeC(0); // 0:結尾
		}
	}

	// private int getAttrEnchantBit(int attr) {
	// int attr_bit = 0;
	// int result_bit = 0;
	//
	// // 1/2/3/33/34 attr_bit = 1;
	// // 4/5/6/35/36 attr_bit = 2;
	// // 7/8/9/37/38 attr_bit = 3;
	// // 10/11/12/39/40 attr_bit = 4;
	//
	// switch (attr) {
	// case 1:
	// attr_bit = 1;
	// attr = 1;
	// break;
	// case 2:
	// attr_bit = 1;
	// attr = 2;
	// break;
	// case 3:
	// attr_bit = 1;
	// attr = 3;
	// break;
	// case 4:
	// attr_bit = 1;
	// attr = 4;
	// break;
	// case 5:
	// attr_bit = 1;
	// attr = 5;
	// break;
	// case 6:
	// attr_bit = 2;
	// attr = 1;
	// break;
	// case 7:
	// attr_bit = 2;
	// attr = 2;
	// case 8:
	// attr_bit = 2;
	// attr = 3;
	// break;
	// case 9:
	// attr_bit = 2;
	// attr = 4;
	// break;
	// case 10:
	// attr_bit = 2;
	// attr = 5;
	// break;
	// case 11:
	// attr_bit = 3;
	// attr = 1;
	// break;
	// case 12:
	// attr_bit = 3;
	// attr = 2;
	// break;
	// case 13:
	// attr_bit = 3;
	// attr = 3;
	// break;
	// case 14:
	// attr_bit = 3;
	// attr = 4;
	// break;
	// case 15:
	// attr_bit = 3;
	// attr = 5;
	// break;
	// case 16:
	// attr_bit = 4;
	// attr = 1;
	// break;
	// case 17:
	// attr_bit = 4;
	// attr = 2;
	// break;
	// case 18:
	// attr_bit = 4;
	// attr = 3;
	// break;
	// case 19:
	// attr_bit = 4;
	// attr = 4;
	// case 20:
	// attr_bit = 4;
	// attr = 5;
	// break;
	// default:
	// break;
	// }
	// if (attr > 0) {
	// result_bit = attr_bit + (16 * attr);
	// }
	// return result_bit;
	// }
}
