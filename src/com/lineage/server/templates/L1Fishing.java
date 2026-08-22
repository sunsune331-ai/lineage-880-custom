package com.lineage.server.templates;

/**
 * 漁獲資料暫存
 * 
 * @author daien
 */
public class L1Fishing {

	private int _fishitemid; // 魚竿編號

	/** 魚竿編號 */
	public int getFishItemId() {
		return _fishitemid;
	}

	/** 魚竿編號 */
	public void setFishItemId(final int i) {
		_fishitemid = i;
	}

	private int _fishtime; // 魚竿釣魚時間

	/** 魚竿釣魚時間 */
	public int getFishTime() {
		return _fishtime;
	}

	/** 魚竿釣魚時間 */
	public void setFishTime(final int i) {
		_fishtime = i;
	}

	private int _giveitemid; // 釣魚成功給予的物品編號

	/** 釣魚成功給予的物品編號 */
	public int getGiveItemId() {
		return _giveitemid;
	}

	/** 釣魚成功給予的物品編號 */
	public void setGiveItemId(final int i) {
		_giveitemid = i;
	}

	private int _givecount; // 釣魚成功給予的物品數量

	/** 釣魚成功給予的物品數量 */
	public int getGiveCount() {
		return _givecount;
	}

	/** 釣魚成功給予的物品數量 */
	public void setGiveCount(final int i) {
		_givecount = i;
	}

	private int _failitemid; // 釣魚失敗給予的物品編號

	/** 釣魚失敗給予的物品編號 */
	public int getFailItemId() {
		return _failitemid;
	}

	/** 釣魚失敗給予的物品編號 */
	public void setFailItemId(final int i) {
		_failitemid = i;
	}

	private int _failcount; // 釣魚失敗給予的物品數量

	/** 釣魚失敗給予的物品數量 */
	public int getFailCount() {
		return _failcount;
	}

	/** 釣魚失敗給予的物品數量 */
	public void setFailCount(final int i) {
		_failcount = i;
	}

	private int _random; // 釣魚機率(百萬)

	/** 釣魚機率(百萬) */
	public int getRandom() {
		return _random;
	}

	/** 釣魚機率(百萬) */
	public void setRandom(final int i) {
		_random = i;
	}

	private int _fishgfx; // 成功特效

	/** 成功特效 */
	public int getFishGfx() {
		return _fishgfx;
	}

	/** 成功特效 */
	public void setFishGfx(final int i) {
		_fishgfx = i;
	}

	private int _showworld; // 是否公告

	/** 是否公告 */
	public int getShowWorld() {
		return _showworld;
	}

	/** 是否公告 */
	public void setShowWorld(final int i) {
		_showworld = i;
	}
}
