package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.IMMUNE_TO_HARM;
import static com.lineage.server.model.skill.L1SkillId.LUCIFER;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NewSkillIcon;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 黑妖新技能 暗影屏障
 */
public class LUCIFER extends SkillMode {

	// 施法者所承受的傷害減少10%

	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;

		final L1PcInstance pc = (L1PcInstance) cha;
		if (pc.hasSkillEffect(IMMUNE_TO_HARM)) {
			pc.sendPackets(new S_SystemMessage("你已有更強的聖結界狀態咯。"));
			return dmg;
		}
		if (pc.hasSkillEffect(LUCIFER)) {
			pc.removeSkillEffect(LUCIFER);
		}
		pc.setSkillEffect(LUCIFER, integer * 1000);
		pc.sendPackets(new S_NewSkillIcon(8880, integer, true));

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
			pc.sendPackets(new S_NewSkillIcon(8880, 0, false));
		}
	}

}
