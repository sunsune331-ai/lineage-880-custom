package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.SAND_STORM;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_SkillBrave;

/**
 * 奔崩之土
 * @author dexc
 */
public class SAND_STORM extends SkillMode {

	public SAND_STORM() {
	}

	@Override
	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;
		final L1PcInstance pc = (L1PcInstance) cha;

		L1BuffUtil.braveStart(pc);

		pc.setSkillEffect(SAND_STORM, integer * 1000);

		pc.setBraveSpeed(1);
		pc.sendPackets(new S_SkillBrave(pc.getId(), 1, integer));
		pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 1, 0));

		return dmg;
	}

	@Override
	public int start(final L1NpcInstance npc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		return 0;
	}

	@Override
	public void start(final L1PcInstance srcpc, final Object obj) throws Exception {
	}

	@Override
	public void stop(final L1Character cha) throws Exception {
		cha.setBraveSpeed(0);
		if (cha instanceof L1PcInstance) {
			final L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
		}
	}
}
