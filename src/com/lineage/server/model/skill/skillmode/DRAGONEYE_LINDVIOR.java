package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class DRAGONEYE_LINDVIOR extends SkillMode {
	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;
		if (!srcpc.hasSkillEffect(6686)) {
			// srcpc.addRegistSleep(3); // 睡眠耐性
			srcpc.addOriginalMagicCritical(2);
			srcpc.setSkillEffect(6686, integer * 1000);
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
		// cha.addRegistSleep(-3); // 睡眠耐性
		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addOriginalMagicCritical(-2);
		}
	}
}
