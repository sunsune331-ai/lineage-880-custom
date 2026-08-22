package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.ICE_METEOR;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpellbookLv10 extends ItemExecutor {

	public static ItemExecutor get() {
		return new Skill_SpellbookLv10();
	}

	public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
		if (item == null) {
			return;
		}

		if (pc == null) {
			return;
		}

		if (!pc.isWizard()) {
			S_ServerMessage msg = new S_ServerMessage(79);
			pc.sendPackets(msg);
		} else {
			String nameId = item.getItem().getNameId();

			int skillid = 0;

			int attribute = 0;

			int magicLv = 10;

			if (nameId.equalsIgnoreCase("$23460")) {
				skillid = 73;

				attribute = 0;
			} else if (nameId.equalsIgnoreCase("$563")) {
				skillid = 74;

				attribute = 0;
			} else if (nameId.equalsIgnoreCase("$564")) {
				skillid = 75;

				attribute = 1;
			} else if (nameId.equalsIgnoreCase("$28229")) { // 魔法書 (集體緩速術) 改 冰霜彗星
				skillid = ICE_METEOR;

				attribute = 2;
			} else if (nameId.equalsIgnoreCase("$566")) {
				skillid = 77;

				attribute = 1;
			} else if (nameId.equalsIgnoreCase("$1872")) {
				skillid = 78;

				attribute = 0;
			} else if (nameId.equalsIgnoreCase("$1873")) {
				skillid = 79;

				attribute = 0;
			} else if (nameId.equalsIgnoreCase("$1874")) {
				skillid = 80;

				attribute = 2;
			}

			Skill_Check.check(pc, item, skillid, magicLv, attribute);
		}
	}
}
