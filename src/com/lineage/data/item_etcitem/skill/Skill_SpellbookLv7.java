package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpellbookLv7 extends ItemExecutor {
	public static ItemExecutor get() {
		return new Skill_SpellbookLv7();
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

			int magicLv = 7;

			if (nameId.equalsIgnoreCase("$547")) {
				skillid = 49;

				attribute = 1;
			} else if (nameId.equalsIgnoreCase("$548")) {
				skillid = 50;

				attribute = 0;
			} else if (nameId.equalsIgnoreCase("$549")) {
				skillid = 51;

				attribute = 2;
			} else if (nameId.equalsIgnoreCase("$550")) {
				skillid = 52;

				attribute = 1;
			} else if (nameId.equalsIgnoreCase("$551")) {
				skillid = 53;

				attribute = 0;
			} else if (nameId.equalsIgnoreCase("$1651")) {
				skillid = 54;

				attribute = 0;
			} else if (nameId.equalsIgnoreCase("$1866")) {
				skillid = 55;

				attribute = 0;
			} else if (nameId.equalsIgnoreCase("$1867")) {
				skillid = 56;

				attribute = 2;
			}

			Skill_Check.check(pc, item, skillid, 7, attribute);
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.data.item_etcitem.skill.Skill_SpellbookLv7 JD-Core Version: 0.6.2
 */