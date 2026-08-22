package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.FOCUS_WAVE;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_SkillBrave;

/**
 * 波濤之水<br>
 * 攻擊時好像有階段性增強，待實裝
 * @author dexc
 */
public class FOCUS_WAVE extends SkillMode {

	public FOCUS_WAVE() {
	}

	@Override
	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;
		final L1PcInstance pc = (L1PcInstance) cha;

		L1BuffUtil.braveStart(pc);

		pc.setSkillEffect(FOCUS_WAVE, integer * 1000);

		pc.setBraveSpeed(10);
		pc.sendPackets(new S_SkillBrave(pc.getId(), 10, integer));
		pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 10, 0));

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
