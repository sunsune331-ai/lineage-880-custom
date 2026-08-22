package com.lineage.data.item_etcitem.skill;

import static com.lineage.server.model.skill.L1SkillId.DESPERADO;
import static com.lineage.server.model.skill.L1SkillId.DESPERADO_ABSOLUTE;
import static com.lineage.server.model.skill.L1SkillId.TITANL_RISING;
import static com.lineage.server.model.skill.L1SkillId.GIGANTIC;
import static com.lineage.server.model.skill.L1SkillId.HOWL;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_ARMORGARDE;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_CRASH;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_FURY;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_SLAYER;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_TITANBULLET;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_TITANMAGIC;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_TITANROCK;
import static com.lineage.server.model.skill.L1SkillId.POWERGRIP;
import static com.lineage.server.model.skill.L1SkillId.TOMAHAWK;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_Warrior extends ItemExecutor {
	private Skill_Warrior() {

	}

	public static ItemExecutor get() {
		return new Skill_Warrior();
	}

	@Override
	public void execute(final int[] data, final L1PcInstance pc, final L1ItemInstance item) {
		// 例外狀況:物件為空
		if (item == null) {
			return;
		}
		// 例外狀況:人物為空
		if (pc == null) {
			return;
		}

		if (!pc.isWarrior()) {

			final S_ServerMessage msg = new S_ServerMessage(79);
			pc.sendPackets(msg);

		} else {

			final String nameId = item.getItem().getNameId();

			int skillid = 0;

			final int attribute = 9;

			int magicLv = 0;

			if (nameId.equalsIgnoreCase("$17823")) { // 狂戰士 粉碎

				skillid = PASSIVE_CRASH;

				magicLv = 73;

			} else if (nameId.equalsIgnoreCase("$17824")) { // 狂戰士 狂暴

				skillid = PASSIVE_FURY;

				magicLv = 76;

			} else if (nameId.equalsIgnoreCase("$17825")) { // 狂戰士 迅猛雙斧

				skillid = PASSIVE_SLAYER;

				magicLv = 71;

			} else if (nameId.equalsIgnoreCase("$17827")) { // 狂戰士 護甲身軀

				skillid = PASSIVE_ARMORGARDE;

				magicLv = 74;

			} else if (nameId.equalsIgnoreCase("$17828")) { // 狂戰士 泰坦：岩石

				skillid = PASSIVE_TITANROCK;

				magicLv = 76;

			} else if (nameId.equalsIgnoreCase("$17829")) { // 狂戰士 泰坦：子彈

				skillid = PASSIVE_TITANBULLET;

				magicLv = 79;

			} else if (nameId.equalsIgnoreCase("$17830")) { // 狂戰士 泰坦：魔法

				skillid = PASSIVE_TITANMAGIC;

				magicLv = 78;

			} else if (nameId.equalsIgnoreCase("$17831")) { // 狂戰士 咆哮

				skillid = HOWL;

				magicLv = 72;

			} else if (nameId.equalsIgnoreCase("$17832")) { // 狂戰士 體能強化

				skillid = GIGANTIC;

				magicLv = 75;

			} else if (nameId.equalsIgnoreCase("$17834")) { // 狂戰士 拘束移動

				skillid = POWERGRIP;

				magicLv = 77;

			} else if (nameId.equalsIgnoreCase("$17840")) { // 狂戰士 戰斧投擲

				skillid = TOMAHAWK;

				magicLv = 73;

			} else if (nameId.equalsIgnoreCase("$17856")) { // 狂戰士 亡命之徒

				skillid = DESPERADO;

				magicLv = 76;
				
			} else if (nameId.equalsIgnoreCase("$23463")) { // 狂戰士新技能 泰坦狂暴

				skillid = TITANL_RISING;

				magicLv = 76;

			} else if (nameId.equalsIgnoreCase("$28245")) { // 狂戰士新技能 亡命之徒:強化

				skillid = DESPERADO_ABSOLUTE;

				magicLv = 80; // 80=85級可學
			}

			Skill_Check.check(pc, item, skillid, magicLv, attribute);
		}
	}
}
