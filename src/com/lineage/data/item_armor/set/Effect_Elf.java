package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 套裝效果:精靈耐性增加
 * @author Admin
 */
public class Effect_Elf implements ArmorSetEffect {
	private final int _add;

	public Effect_Elf(int add) {
		_add = add;
	}

	public void giveEffect(L1PcInstance pc) {
		pc.setHitElf(_add);
	}

	public void cancelEffect(L1PcInstance pc) {
		pc.setHitElf(-_add);
	}

	public int get_mode() {
		return _add;
	}
}
