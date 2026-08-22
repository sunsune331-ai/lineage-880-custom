package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 套裝效果:技術耐性增加
 * @author Admin
 */
public class EffectRegist_Technology implements ArmorSetEffect {
	private final int _add;

	public EffectRegist_Technology(int add) {
		_add = add;
	}

	public void giveEffect(L1PcInstance pc) {
		pc.addRegistTechnology(_add);
	}

	public void cancelEffect(L1PcInstance pc) {
		pc.addRegistTechnology(-_add);
	}

	public int get_mode() {
		return _add;
	}
}
