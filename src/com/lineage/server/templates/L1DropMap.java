package com.lineage.server.templates;

/**
 * 掉落物品資料(指定地圖)
 * 
 * @author daien
 */
public final class L1DropMap {

	private final int _mobId;
	private final int _mapid;
	private final int _itemId;
	private final int _enchant_min; // 最小強化值 by terry0412
	private final int _enchant_max; // 最大強化值 by terry0412
	private final int _min;
	private final int _max;
	private final int _chance;

	public L1DropMap(final int mobId, final int mapid, final int itemId,
			final int enchant_min, final int enchant_max, final int min,
			final int max, final int chance) {
		this._mobId = mobId;
		this._mapid = mapid;
		this._itemId = itemId;
		this._enchant_min = enchant_min;
		this._enchant_max = enchant_max;
		this._min = min;
		this._max = max;
		this._chance = chance;
	}

	/**
	 * 指定地圖
	 * 
	 * @return
	 */
	public int get_mapid() {
		return this._mapid;
	}

	/**
	 * 機率
	 * 
	 * @return
	 */
	public int getChance() {
		return this._chance;
	}

	/**
	 * 物品編號
	 * 
	 * @return
	 */
	public int getItemid() {
		return this._itemId;
	}

	/**
	 * 最大強化值
	 * 
	 * @return
	 */
	public int getEnchantMax() {
		return this._enchant_max;
	}

	/**
	 * 最小強化值
	 * 
	 * @return
	 */
	public int getEnchantMin() {
		return this._enchant_min;
	}

	/**
	 * 最大數量
	 * 
	 * @return
	 */
	public int getMax() {
		return this._max;
	}

	/**
	 * 最小數量
	 * 
	 * @return
	 */
	public int getMin() {
		return this._min;
	}

	/**
	 * NPC編號
	 * 
	 * @return
	 */
	public int getMobid() {
		return this._mobId;
	}


}
