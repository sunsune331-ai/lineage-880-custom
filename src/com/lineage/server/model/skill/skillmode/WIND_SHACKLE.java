package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxWindShackle;

public class WIND_SHACKLE extends SkillMode {
	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;
		if (cha.hasSkillEffect(167)) {
			return 0;
		}
		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (!pc.isWarrior()) {
				pc.sendPackets(new S_PacketBoxWindShackle(pc.getId(), integer));
			}
		}
		cha.setSkillEffect(167, integer * 1000);
		return dmg;
	}

	public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;
		if (cha.hasSkillEffect(167)) {
			return 0;
		}
		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_PacketBoxWindShackle(pc.getId(), integer));
		}
		cha.setSkillEffect(167, integer * 1000);
		return dmg;
	}

	public void start(L1PcInstance srcpc, Object obj) throws Exception {
	}

	public void stop(L1Character cha) throws Exception {
		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_PacketBoxWindShackle(pc.getId(), 0));
		}
	}
}
