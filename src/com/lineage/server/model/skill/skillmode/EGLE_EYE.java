package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.EGLE_EYE;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NewSkillIcon;

/**
 * 鷹眼150(遠距離爆擊率加2%)
 * @author admin
 */
public class EGLE_EYE extends SkillMode {

	public EGLE_EYE() {
	}

	@Override
	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;
		final L1PcInstance pc = (L1PcInstance) cha;

		if (pc.hasSkillEffect(EGLE_EYE)) {
			pc.removeSkillEffect(EGLE_EYE);
		}
		pc.setSkillEffect(EGLE_EYE, integer * 1000);
		pc.addBowCritical(2);
		pc.sendPackets(new S_NewSkillIcon(5157, integer, true));

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
		pc.addBowCritical(-2);
		pc.sendPackets(new S_NewSkillIcon(5157, 0, false));
	}
}
