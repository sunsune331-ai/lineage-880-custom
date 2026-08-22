package com.lineage.server.templates;

/**
 * 物品凹槽強化暫存
 * 強化擴充能力
 *
 */
public class L1CharItemPower {
	
	public L1CharItemPower() {
	}
	
	private int _id;// 強化物品OBJID
	
	/**
	 * 傳回強化物品OBJID
	 * @return
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * 設置強化物品的OBJID
	 * @param i
	 */
	public void setId(int i) {
		_id = i;
	}

	// 近戰攻擊
	private int _updateDmgModifier;

	public int getUpdateDmgModifier() {
		return _updateDmgModifier;
	}

	public void setUpdateDmgModifier(int i) {
		_updateDmgModifier = i;
	}

	// 近戰命中
	private int _updateHitModifier;

	public int getUpdateHitModifier() {
		return _updateHitModifier;
	}

	public void setUpdateHitModifier(int i) {
		_updateHitModifier = i;
	}

	// 遠攻攻擊
	private int _updateBowDmgModifier;

	public int getUpdateBowDmgModifier() {
		return _updateBowDmgModifier;
	}

	public void setUpdateBowDmgModifier(int i) {
		_updateBowDmgModifier = i;
	}

	// 遠攻命中
	private int _updateBowHitModifier;

	public int getUpdateBowHitModifier() {
		return _updateBowHitModifier;
	}

	public void setUpdateBowHitModifier(int i) {
		_updateBowHitModifier = i;
	}

	// 力量
	private int _updateStr;

	public int getUpdateStr() {
		return _updateStr;
	}

	public void setUpdateStr(int i) {
		_updateStr = i;
	}

	// 敏捷
	private int _updateDex;

	public int getUpdateDex() {
		return _updateDex;
	}

	public void setUpdateDex(int i) {
		_updateDex = i;
	}

	// 體質
	private int _updateCon;

	public int getUpdateCon() {
		return _updateCon;
	}

	public void setUpdateCon(int i) {
		_updateCon = i;
	}

	// 精神
	private int _updateWis;

	public int getUpdateWis() {
		return _updateWis;
	}

	public void setUpdateWis(int i) {
		_updateWis = i;
	}

	// 智力
	private int _updateInt;

	public int getUpdateInt() {
		return _updateInt;
	}

	public void setUpdateInt(int i) {
		_updateInt = i;
	}

	// 魅力
	private int _updateCha;

	public int getUpdateCha() {
		return _updateCha;
	}

	public void setUpdateCha(int i) {
		_updateCha = i;
	}

	// 血量
	private int _updateHp;

	public int getUpdateHp() {
		return _updateHp;
	}

	public void setUpdateHp(int i) {
		_updateHp = i;
	}

	// 魔量
	private int _updateMp;

	public int getUpdateMp() {
		return _updateMp;
	}

	public void setUpdateMp(int i) {
		_updateMp = i;
	}

	// 地屬性
	private int _updateEarth;

	public int getUpdateEarth() {
		return _updateEarth;
	}

	public void setUpdateEarth(int i) {
		_updateEarth = i;
	}

	// 風屬性
	private int _updateWind;

	public int getUpdateWind() {
		return _updateWind;
	}

	public void setUpdateWind(int i) {
		_updateWind = i;
	}

	// 水屬性
	private int _updateWater;

	public int getUpdateWater() {
		return _updateWater;
	}

	public void setUpdateWater(int i) {
		_updateWater = i;
	}

	// 火屬性
	private int _updateFire;

	public int getUpdateFire() {
		return _updateFire;
	}

	public void setUpdateFire(int i) {
		_updateFire = i;
	}

	// 抗魔
	private int _updateMr;

	public int getUpdateMr() {
		return _updateMr;
	}

	public void setUpdateMr(int i) {
		_updateMr = i;
	}

	// 防禦
	private int _updateAc;

	public int getUpdateAc() {
		return _updateAc;
	}

	public void setUpdateAc(int i) {
		_updateAc = i;
	}

	// 回血
	private int _updateHpr;

	public int getUpdateHpr() {
		return _updateHpr;
	}

	public void setUpdateHpr(int i) {
		_updateHpr = i;
	}

	// 回魔
	private int _updateMpr;

	public int getUpdateMpr() {
		return _updateMpr;
	}

	public void setUpdateMpr(int i) {
		_updateMpr = i;
	}

	// 魔法攻擊
	private int _updateSp;

	public int getUpdateSp() {
		return _updateSp;
	}

	public void setUpdateSp(int i) {
		_updateSp = i;
	}

    // 增加PVP傷害
	private int _PvpDmg;

	public int getUpdatePvpDmg() {
		return _PvpDmg;
	}

	public void setUpdatePvpDmg(final int i) {
		_PvpDmg = i;
	}

    // 減免PVP傷害
	private int _PvpDmg_R;

	public int getUpdatePvpDmg_R() {
		return _PvpDmg_R;
	}

	public void setUpdatePvpDmg_R(final int i) {
		_PvpDmg_R = i;
	}

    // 武器劍靈系統
	private int _Weapon_Soul;

	/**
	 * 武器劍靈值
	 * @return
	 */
	public int getUpdateWeaponSoul() {
		return _Weapon_Soul;
	}

	/**
	 * 武器劍靈值
	 * @param i
	 */
	public void setUpdateWeaponSoul(final int i) {
		_Weapon_Soul = i;
	}
	
}
