package com.lineage.server.templates;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃各項能力設置抽像接口
 * 
 * @author daien
 *
 */
public abstract class L1DollExecutor {

	/**
	 * 設置能力設定值
	 * 
	 * @param int1
	 * @param int2
	 * @param int3
	 */
	public abstract void set_power(int int1, int int2, int int3);

	/** 取回娃娃能力描述 */
	public abstract String get_note();

	/**
	 * 設置娃娃能力描述
	 * 
	 * @param note
	 */
	public abstract void set_note(String note);

	/**
	 * 裝備娃娃效果
	 * 
	 * @param pc
	 * @return
	 */
	public abstract void setDoll(L1PcInstance pc);

	/**
	 * 解除娃娃效果
	 * 
	 * @param pc
	 * @return
	 */
	public abstract void removeDoll(L1PcInstance pc);

	/**
	 * 是否具有輔助技能
	 * 
	 * @return
	 */
	public abstract boolean is_reset();

	// 新增娃娃能力描述↓↓↓
	// 新增娃娃能力描述↓↓↓
	// 新增娃娃能力描述↓↓↓
	private int _DollAc = 0; // 娃娃能力描述 - 防禦

	/** 娃娃能力描述 - 防禦 */
	public int getDollAc() {
		return _DollAc;
	}

	/** 娃娃能力描述 - 防禦 */
	public void setDollAc(final int i) {
		_DollAc = i;
	}

	private int _DollStr = 0; // 娃娃能力描述 - 力量

	/** 娃娃能力描述 - 力量 */
	public int getDollStr() {
		return _DollStr;
	}

	/** 娃娃能力描述 - 力量 */
	public void setDollStr(final int i) {
		_DollStr = i;
	}

	private int _DollDex = 0; // 娃娃能力描述 - 敏捷

	/** 娃娃能力描述 - 敏捷 */
	public int getDollDex() {
		return _DollDex;
	}

	/** 娃娃能力描述 - 敏捷 */
	public void setDollDex(final int i) {
		_DollDex = i;
	}

	private int _DollCon = 0; // 娃娃能力描述 - 體質

	/** 娃娃能力描述 - 體質 */
	public int getDollCon() {
		return _DollCon;
	}

	/** 娃娃能力描述 - 體質 */
	public void setDollCon(final int i) {
		_DollCon = i;
	}

	private int _DollWis = 0; // 娃娃能力描述 - 精神

	/** 娃娃能力描述 - 精神 */
	public int getDollWis() {
		return _DollWis;
	}

	/** 娃娃能力描述 - 精神 */
	public void setDollWis(final int i) {
		_DollWis = i;
	}

	private int _DollInt = 0; // 娃娃能力描述 - 智力

	/** 娃娃能力描述 - 智力 */
	public int getDollInt() {
		return _DollInt;
	}

	/** 娃娃能力描述 - 智力 */
	public void setDollInt(final int i) {
		_DollInt = i;
	}

	private int _DollCha = 0; // 娃娃能力描述 - 魅力

	/** 娃娃能力描述 - 魅力 */
	public int getDollCha() {
		return _DollCha;
	}

	/** 娃娃能力描述 - 魅力 */
	public void setDollCha(final int i) {
		_DollCha = i;
	}

	private int _DollHp = 0; // 娃娃能力描述 - 血量

	/** 娃娃能力描述 - 血量 */
	public int getDollHp() {
		return _DollHp;
	}

	/** 娃娃能力描述 - 血量 */
	public void setDollHp(final int i) {
		_DollHp = i;
	}

	private int _DollMp = 0; // 娃娃能力描述 - 魔量

	/** 娃娃能力描述 - 魔量 */
	public int getDollMp() {
		return _DollMp;
	}

	/** 娃娃能力描述 - 魔量 */
	public void setDollMp(final int i) {
		_DollMp = i;
	}

	private int _DollHpr = 0; // 娃娃能力描述 - 回血

	/** 娃娃能力描述 - 回血 */
	public int getDollHpr() {
		return _DollHpr;
	}

	/** 娃娃能力描述 - 回血 */
	public void setDollHpr(final int i) {
		_DollHpr = i;
	}

	private int _DollMpr = 0; // 娃娃能力描述 - 回魔

	/** 娃娃能力描述 - 回魔 */
	public int getDollMpr() {
		return _DollMpr;
	}

	/** 娃娃能力描述 - 回魔 */
	public void setDollMpr(final int i) {
		_DollMpr = i;
	}

	private int _DollMr = 0; // 娃娃能力描述 - 魔防

	/** 娃娃能力描述 - 抗魔 */
	public int getDollMr() {
		return _DollMr;
	}

	/** 娃娃能力描述 - 抗魔 */
	public void setDollMr(final int i) {
		_DollMr = i;
	}

	private int _DollSp = 0; // 娃娃能力描述 - 魔攻

	/** 娃娃能力描述 - 魔法攻擊 */
	public int getDollSp() {
		return _DollSp;
	}

	/** 娃娃能力描述 - 魔法攻擊 */
	public void setDollSp(final int i) {
		_DollSp = i;
	}

	private int _DollDmg = 0; // 娃娃能力描述 - 近戰攻擊

	/** 娃娃能力描述 - 近戰攻擊 */
	public int getDollDmg() {
		return _DollDmg;
	}

	/** 娃娃能力描述 - 近戰攻擊 */
	public void setDollDmg(final int i) {
		_DollDmg = i;
	}

	private int _DollHit = 0; // 娃娃能力描述 - 近戰命中

	/** 娃娃能力描述 - 近戰命中 */
	public int getDollHit() {
		return _DollHit;
	}

	/** 娃娃能力描述 - 近戰命中 */
	public void setDollHit(final int i) {
		_DollHit = i;
	}

	private int _DollBowDmg = 0; // 娃娃能力描述 - 遠攻攻擊

	/** 娃娃能力描述 - 遠攻攻擊 */
	public int getDollBowDmg() {
		return _DollBowDmg;
	}

	/** 娃娃能力描述 - 遠攻攻擊 */
	public void setDollBowDmg(final int i) {
		_DollBowDmg = i;
	}

	private int _DollBowHit = 0; // 娃娃能力描述 - 遠攻命中

	/** 娃娃能力描述 - 遠攻命中 */
	public int getDollBowHit() {
		return _DollBowHit;
	}

	/** 娃娃能力描述 - 遠攻命中 */
	public void setDollBowHit(final int i) {
		_DollBowHit = i;
	}

	private int _DollAllDmg_R = 0; // 娃娃能力描述 - 傷害減免

	/** 娃娃能力描述 - 傷害減免 */
	public int getDollAllDmg_R() {
		return _DollAllDmg_R;
	}

	/** 娃娃能力描述 - 傷害減免 */
	public void setDollAllDmg_R(final int i) {
		_DollAllDmg_R = i;
	}

	private int _DollExp = 0; // 娃娃能力描述 - 狩獵經驗值

	/** 娃娃能力描述 - 狩獵經驗值 */
	public int getDollExp() {
		return _DollExp;
	}

	/** 娃娃能力描述 - 狩獵經驗值 */
	public void setDollExp(final int i) {
		_DollExp = i;
	}

	private int _DollWeight = 0; // 娃娃能力描述 - 負重能力+%

	/** 娃娃能力描述 - 負重能力+% */
	public int getDollWeight() {
		return _DollWeight;
	}

	/** 娃娃能力描述 - 負重能力+% */
	public void setDollWeight(final int i) {
		_DollWeight = i;
	}

	private int _DollWeight_R = 0; // 娃娃能力描述 - 增加負重 +X

	/** 娃娃能力描述 - 增加負重 +X */
	public int getDollWeight_R() {
		return _DollWeight_R;
	}

	/** 娃娃能力描述 - 增加負重 +X */
	public void setDollWeight_R(final int i) {
		_DollWeight_R = i;
	}

	private int _DollEarth = 0; // 娃娃能力描述 - 地屬性

	/** 娃娃能力描述 - 地屬性 */
	public int getDollEarth() {
		return _DollEarth;
	}

	/** 娃娃能力描述 - 地屬性 */
	public void setDollEarth(int i) {
		_DollEarth = i;
	}

	private int _DollWind = 0; // 娃娃能力描述 - 風屬性

	/** 娃娃能力描述 - 風屬性 */
	public int getDollWind() {
		return _DollWind;
	}

	/** 娃娃能力描述 - 風屬性 */
	public void setDollWind(int i) {
		_DollWind = i;
	}

	private int _DollWater = 0; // 娃娃能力描述 - 水屬性

	/** 娃娃能力描述 - 水屬性 */
	public int getDollWater() {
		return _DollWater;
	}

	/** 娃娃能力描述 - 水屬性 */
	public void setDollWater(int i) {
		_DollWater = i;
	}

	private int _DollFire = 0; // 娃娃能力描述 - 火屬性

	/** 娃娃能力描述 - 火屬性 */
	public int getDollFire() {
		return _DollFire;
	}

	/** 娃娃能力描述 - 火屬性 */
	public void setDollFire(int i) {
		_DollFire = i;
	}

	private int _DollRegistTechnology = 0; // 娃娃能力描述 - 技術耐性

	/** 娃娃能力描述 - 技術耐性 */
	public int getDollRegistTechnology() {
		return _DollRegistTechnology;
	}

	/** 娃娃能力描述 - 技術耐性 */
	public void setDollRegistTechnology(final int i) {
		_DollRegistTechnology = i;
	}

	private int _DollRegistElf = 0; // 娃娃能力描述 - 精靈耐性

	/** 娃娃能力描述 - 精靈耐性 */
	public int getDollRegistElf() {
		return _DollRegistElf;
	}

	/** 娃娃能力描述 - 精靈耐性 */
	public void setDollRegistElf(final int i) {
		_DollRegistElf = i;
	}

	private int _DollRegistDragon = 0; // 娃娃能力描述 - 龍屬耐性

	/** 娃娃能力描述 - 龍屬耐性 */
	public int getDollRegistDragon() {
		return _DollRegistDragon;
	}

	/** 娃娃能力描述 - 龍屬耐性 */
	public void setDollRegistDragon(final int i) {
		_DollRegistDragon = i;
	}

	private int _DollRegistHorror = 0; // 娃娃能力描述 - 恐怖耐性

	/** 娃娃能力描述 - 恐怖耐性 */
	public int getDollRegistHorror() {
		return _DollRegistHorror;
	}

	/** 娃娃能力描述 - 恐怖耐性 */
	public void setDollRegistHorror(final int i) {
		_DollRegistHorror = i;
	}

	private int _DollRegistAll = 0; // 娃娃能力描述 - 全部四大耐性

	/** 娃娃能力描述 - 全部四大耐性 */
	public int getDollRegistAll() {
		return _DollRegistAll;
	}

	/** 娃娃能力描述 - 全部四大耐性 */
	public void setDollRegistAll(final int i) {
		_DollRegistAll = i;
	}

	private int _DollHitTechnology = 0; // 娃娃能力描述 - 技術命中

	/** 娃娃能力描述 - 技術命中 */
	public int getDollHitTechnology() {
		return _DollHitTechnology;
	}

	/** 娃娃能力描述 - 技術命中 */
	public void setDollHitTechnology(final int i) {
		_DollHitTechnology = i;
	}

	private int _DollHitElf = 0; // 娃娃能力描述 - 精靈命中

	/** 娃娃能力描述 - 精靈命中 */
	public int getDollHitElf() {
		return _DollHitElf;
	}

	/** 娃娃能力描述 - 精靈命中 */
	public void setDollHitElf(final int i) {
		_DollHitElf = i;
	}

	private int _DollHitDragon = 0; // 娃娃能力描述 - 龍屬命中

	/** 娃娃能力描述 - 龍屬命中 */
	public int getDollHitDragon() {
		return _DollHitDragon;
	}

	/** 娃娃能力描述 - 龍屬命中 */
	public void setDollHitDragon(final int i) {
		_DollHitDragon = i;
	}

	private int _DollHitHorror = 0; // 娃娃能力描述 - 恐怖命中

	/** 娃娃能力描述 - 恐怖命中 */
	public int getDollHitHorror() {
		return _DollHitHorror;
	}

	/** 娃娃能力描述 - 恐怖命中 */
	public void setDollHitHorror(final int i) {
		_DollHitHorror = i;
	}

	private int _DollHitAll = 0; // 娃娃能力描述 - 全部四大命中

	/** 娃娃能力描述 - 全部四大命中 */
	public int getDollHitAll() {
		return _DollHitAll;
	}

	/** 娃娃能力描述 - 全部四大命中 */
	public void setDollHitAll(final int i) {
		_DollHitAll = i;
	}
	private int _pvpdmg = 0; // 娃娃能力描述 - PVP伤害命中

	/** 娃娃能力描述 - PVP伤害 */
	public int getDollpvpdmg() {
		return _pvpdmg;
	}
	/** 娃娃能力描述 - PVP伤害 */
	public void setDollpvpdmg(final int i) {
		_pvpdmg = i;
	}
	private int _pvpjm = 0; // 娃娃能力描述 - PVP减免

	/** 娃娃能力描述 - PVP减免 */
	public int getDollpvpjm() {
		return _pvpjm;
	}
	/** 娃娃能力描述 - PVP减免 */
	public void setDollpvpjm(final int i) {
		_pvpjm = i;
	}
	private int _yshzf = 0; // 娃娃能力描述 - 殷海薩祝

	/** 娃娃能力描述 - 殷海薩祝 */
	public int getDollyshzf() {
		return _yshzf;
	}
	/** 娃娃能力描述 - 殷海薩祝 */
	public void setDollyshzf(final int i) {
		_yshzf = i;
	}
	private int _DollHaste = 0;// 娃娃能力描述 - 加速效果

	/** 娃娃能力描述 - 加速效果 */
	public int getDollHaste() {
		return _DollHaste;
	}

	/** 娃娃能力描述 - 加速效果 */
	public void setDollHaste(final int i) {
		_DollHaste = i;
	}

	private int _DollStunLv = 0;// 娃娃能力描述 - 昏迷等級

	/** 娃娃能力描述 - 昏迷等級 */
	public int getDollStunLv() {
		return _DollStunLv;
	}

	/** 娃娃能力描述 - 昏迷等級 */
	public void setDollStunLv(final int i) {
		_DollStunLv = i;
	}

	private int _DollBreakLv = 0;// 娃娃能力描述 - 破壞盔甲命中

	/** 娃娃能力描述 - 破壞盔甲命中 */
	public int getDollBreakLv() {
		return _DollBreakLv;
	}

	/** 娃娃能力描述 - 破壞盔甲命中 */
	public void setDollBreakLv(final int i) {
		_DollBreakLv = i;
	}

	private int _DollFoeSlayer = 0;// 娃娃能力描述 - 屠宰者階段別傷害

	/** 娃娃能力描述 - 屠宰者階段別傷害 */
	public int getDollFoeSlayer() {
		return _DollFoeSlayer;
	}

	/** 娃娃能力描述 - 屠宰者階段別傷害 */
	public void setDollFoeSlayer(final int i) {
		_DollFoeSlayer = i;
	}

	private int _DollTiTanHp = 0;// 娃娃能力描述 - 泰坦系列技能發動 HP 區間增加

	/** 娃娃能力描述 - 泰坦系列技能發動 HP 區間增加 */
	public int getDollTiTanHp() {
		return _DollTiTanHp;
	}

	/** 娃娃能力描述 - 泰坦系列技能發動 HP 區間增加 */
	public void setDollTiTanHp(final int i) {
		_DollTiTanHp = i;
	}
	// 新增娃娃能力描述↑↑↑
	// 新增娃娃能力描述↑↑↑
	// 新增娃娃能力描述↑↑↑
}
