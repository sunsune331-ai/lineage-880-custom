package com.lineage.server.templates;

/**
 * 掉落物品資料(全怪物)
 * @author daien
 *
 */
public class L1DropMob {
	
	int _min;
	
	/**
	 * 最小量
	 * @return
	 */
	public int getMin() {
		return this._min;
	}
	
	public void setMin(int i) {
		this._min = i;
	}
	
	int _max;
	
	/**
	 * 最大量
	 * @return
	 */
	public int getMax() {
		return this._max;
	}
	
	public void setMax(int i) {
		this._max = i;
	}
	
	int _chance;
	
	/**
	 * 機率
	 * @return
	 */
	public int getChance() {
		return this._chance;
	}
	
	public void setChance(int i) {
		this._chance = i;
	}
}
