package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.FOCUS_SPRITS;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NewSkillIcon;

/**
 * 幻術師新技能 專注鬥志223
 * @author admin
 */
public class FOCUS_SPRITS extends SkillMode {

	public FOCUS_SPRITS() {
	}

	// 魔法爆擊率增加5%

	@Override
	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;
		final L1PcInstance pc = (L1PcInstance) cha;

		if (pc.hasSkillEffect(FOCUS_SPRITS)) {
			pc.removeSkillEffect(FOCUS_SPRITS);
		}
		pc.setSkillEffect(FOCUS_SPRITS, integer * 1000);
		pc.addOriginalMagicCritical(5);
		pc.sendPackets(new S_NewSkillIcon(4832, integer, true));

		return dmg;
	}

	@Override
	public int start(final L1NpcInstance npc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;

		return dmg;
	}

	@Override
	public void start(final L1PcInstance srcpc, final Object obj) throws Exception {
	}

	@Override
	public void stop(final L1Character cha) throws Exception {
		final L1PcInstance pc = (L1PcInstance) cha;
		pc.addOriginalMagicCritical(-5);
		pc.sendPackets(new S_NewSkillIcon(4832, 0, false));
	}
}
