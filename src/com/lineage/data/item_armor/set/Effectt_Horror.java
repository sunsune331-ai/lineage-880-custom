package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 套裝效果:恐怖耐性增加
 * @author Admin
 */
public class Effectt_Horror implements ArmorSetEffect {
	private final int _add;

	public Effectt_Horror(int add) {
		_add = add;
	}

	public void giveEffect(L1PcInstance pc) {
		pc.setHitHorror(_add);
	}

	public void cancelEffect(L1PcInstance pc) {
		pc.setHitHorror(-_add);
	}

	public int get_mode() {
		return _add;
	}
}
