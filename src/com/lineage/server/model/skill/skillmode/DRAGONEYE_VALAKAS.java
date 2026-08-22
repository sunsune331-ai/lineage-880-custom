package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class DRAGONEYE_VALAKAS extends SkillMode {
	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;
		if (!srcpc.hasSkillEffect(6683)) {
			srcpc.addDmgup(2);
			// srcpc.addRegistStun(3); // 昏迷耐性
			srcpc.setSkillEffect(6683, integer * 1000);
		}

		return dmg;
	}

	public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;

		return dmg;
	}

	public void start(L1PcInstance srcpc, Object obj) throws Exception {
	}

	public void stop(L1Character cha) throws Exception {
		cha.addDmgup(-2);
		// cha.addRegistStun(-3); // 昏迷耐性
	}
}
