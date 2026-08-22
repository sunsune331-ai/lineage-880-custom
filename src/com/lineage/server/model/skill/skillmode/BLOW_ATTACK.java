package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.BLOW_ATTACK;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NewSkillIcon;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 榮耀盾94
 */
public class BLOW_ATTACK extends SkillMode {

	// 透過盾牌力量進行猛烈攻擊
	// 機率性發動近距離傷害增加1.5倍

	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;
		final L1PcInstance pc = (L1PcInstance) cha;
		if (pc.getInventory().getTypeEquipped(2, 7) >= 1 // 盾牌
				|| pc.getInventory().getTypeEquipped(2, 13) >= 1 // 臂甲
		) {
			pc.setSkillEffect(BLOW_ATTACK, integer * 1000);
			pc.sendPackets(new S_NewSkillIcon(8843, integer, true));

		} else {
			pc.sendPackets(new S_ServerMessage("\\aE你並未裝備盾牌或臂甲。"));
		}

		return dmg;
	}

	public int start(final L1NpcInstance npc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;

		return dmg;
	}

	public void start(final L1PcInstance srcpc, final Object obj) throws Exception {
	}

	public void stop(final L1Character cha) throws Exception {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_NewSkillIcon(8843, 0, false));
		}
	}

}
