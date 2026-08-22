package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 減免所有傷害
 */
public class Effect_All_Reduction_dmg implements ArmorSetEffect {
	private final int _add;

	/**
	 * 減免所有傷害
	 * @param add
	 */
	public Effect_All_Reduction_dmg(int add) {
		_add = add;
	}

	public void giveEffect(L1PcInstance pc) {
		pc.addDamageReductionByArmor(_add);
	}

	public void cancelEffect(L1PcInstance pc) {
		pc.addDamageReductionByArmor(-_add);
	}

	public int get_mode() {
		return _add;
	}
}
