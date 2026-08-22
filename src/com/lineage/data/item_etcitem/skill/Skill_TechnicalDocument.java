package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BLADE;
import static com.lineage.server.model.skill.L1SkillId.BLOW_ATTACK;
import static com.lineage.server.model.skill.L1SkillId.COUNTER_BARRIER_VETERAN;
import static com.lineage.server.model.skill.L1SkillId.PRIDE;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_TechnicalDocument extends ItemExecutor {
	public static ItemExecutor get() {
		return new Skill_TechnicalDocument();
	}

	public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
		if (item == null) {
			return;
		}

		if (pc == null) {
			return;
		}

		if (!pc.isKnight()) {
			S_ServerMessage msg = new S_ServerMessage(79);
			pc.sendPackets(msg);
		} else {
			String nameId = item.getItem().getNameId();

			int skillid = 0;

			int attribute = 0;

			int magicLv = 0;

			if (nameId.equalsIgnoreCase("$3259")) {
				skillid = 87;

				attribute = 5;

				magicLv = 31;
			} else if (nameId.equalsIgnoreCase("$4007")) {
				skillid = 88;

				attribute = 5;

				magicLv = 31;
			} else if (nameId.equalsIgnoreCase("$4008")) {
				skillid = 89;

				attribute = 5;

				magicLv = 32;
			} else if (nameId.equalsIgnoreCase("$4712")) {
				skillid = 90;

				attribute = 5;

				magicLv = 31;
			} else if (nameId.equalsIgnoreCase("$4713")) {
				skillid = 91;

				attribute = 5;

				magicLv = 31;
			} else if (nameId.equalsIgnoreCase("$23458")) { // 絕御之刃
				skillid = ABSOLUTE_BLADE;

				attribute = 5;

				magicLv = 32;

			} else if (nameId.equalsIgnoreCase("$28225")) { // 技術書(榮耀心)
				skillid = PRIDE;

				attribute = 5;

				magicLv = 32;

			} else if (nameId.equalsIgnoreCase("$28226")) { // 技術書(榮耀盾)
				skillid = BLOW_ATTACK;

				attribute = 5;

				magicLv = 32;

			} else if (nameId.equalsIgnoreCase("$28227")) { // \aE技術書(反擊屏障:強化)
				skillid = COUNTER_BARRIER_VETERAN;

				attribute = 5;

				magicLv = 32;
			}

			Skill_Check.check(pc, item, skillid, magicLv, attribute);
		}
	}
}
