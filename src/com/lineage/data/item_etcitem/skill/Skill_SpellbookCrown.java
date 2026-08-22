package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.BRAVE_AVATAR;
import static com.lineage.server.model.skill.L1SkillId.GRACE_AVATAR;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_SpellbookCrown extends ItemExecutor {
	public static ItemExecutor get() {
		return new Skill_SpellbookCrown();
	}

	public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
		if (item == null) {
			return;
		}

		if (pc == null) {
			return;
		}

		if (!pc.isCrown()) {
			S_ServerMessage msg = new S_ServerMessage(79);
			pc.sendPackets(msg);
		} else {
			String nameId = item.getItem().getNameId();

			int skillid = 0;

			int attribute = 4;

			int magicLv = 0;

			if (nameId.equalsIgnoreCase("$1959")) {
				skillid = 113;

				magicLv = 21;
			} else if (nameId.equalsIgnoreCase("$2089")) {
				skillid = 116;

				magicLv = 22;
			} else if (nameId.equalsIgnoreCase("魔法書 (灼熱武器)")) {
				skillid = 114;

				magicLv = 23;
			} else if (nameId.equalsIgnoreCase("$3260")) {
				skillid = 118;

				magicLv = 24;
			} else if (nameId.equalsIgnoreCase("魔法書 (勇猛意志)")) {
				skillid = 117;

				magicLv = 25;
			} else if (nameId.equalsIgnoreCase("魔法書 (閃亮之盾)")) {
				skillid = 115;

				magicLv = 26;
			} else if (nameId.equalsIgnoreCase("$15721")) { // 王者加護
				skillid = BRAVE_AVATAR;

				magicLv = 27;
			} else if (nameId.equalsIgnoreCase("$23457")) { // 恩典庇護
				skillid = GRACE_AVATAR;

				magicLv = 27;
			}

			Skill_Check.check(pc, item, skillid, magicLv, 4);
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.data.item_etcitem.skill.Skill_SpellbookCrown JD-Core Version:
 * 0.6.2
 */