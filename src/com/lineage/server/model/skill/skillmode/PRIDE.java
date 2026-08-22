package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.PRIDE;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HPUpdate;

/**
 * 榮耀心93
 */
public class PRIDE extends SkillMode {

	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;

		if (!cha.hasSkillEffect(PRIDE)) {

			// 持續時間內施法者的最大體力上限提升等級/4=(%)

			final L1PcInstance pc = (L1PcInstance) cha;
			double percent = pc.getLevel() / 4;
			int addHp = (int) Math.round(pc.getBaseMaxHp() * (percent * 0.01));
			pc.setPrideHp(addHp);
			pc.addMaxHp(pc.getPrideHp());
			if (pc.isInParty()) {
				pc.getParty().updateMiniHP(pc);
			}
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
		}
		cha.setSkillEffect(PRIDE, integer * 1000);

		return dmg;
	}

	@Override
	public int start(final L1NpcInstance npc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void start(final L1PcInstance srcpc, final Object obj) throws Exception {
		// TODO Auto-generated method stub

	}

	public void stop(final L1Character cha) throws Exception {
		if ((cha instanceof L1PcInstance)) {
			final L1PcInstance pc = (L1PcInstance) cha;
			// final int HP = (pc.getBaseMaxHp() / 100) * (pc.getLevel() / 2);

			pc.addMaxHp(-pc.getPrideHp());
			pc.setPrideHp(0);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				pc.getParty().updateMiniHP(pc);
			}
		}
	}
}
