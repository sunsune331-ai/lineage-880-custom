package com.lineage.server.command.executor;

import static com.lineage.server.model.skill.L1SkillId.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_WarriorSkill;
import com.lineage.server.templates.L1Skills;

/**
 * 賦予該gm職業所有技能
 * 
 * @author dexc
 */
public class L1AddSkill implements L1CommandExecutor {

	private static final Log _log = LogFactory.getLog(L1AddSkill.class);

	private L1AddSkill() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1AddSkill();
	}

	@Override
	public void execute(final L1PcInstance pc, final String cmdName, final String arg) {
		try {
			int cnt = 0; // 
			String skill_name = ""; // 名
			int skill_id = 0; // ID

			final int object_id = pc.getId(); // objectid取得
			pc.sendPacketsX8(new S_SkillSound(object_id, '\343')); // 魔法習得效果音鳴

			if (pc.isCrown()) {// 王族
				pc.sendPackets(new S_AddSkill(pc, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 7, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0));

				for (cnt = 1; cnt <= 16; cnt++) {// LV1~2魔法
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

				for (cnt = 113; cnt <= 122; cnt++) {// 魔法
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

			} else if (pc.isKnight()) {// 騎士
				pc.sendPackets(new S_AddSkill(pc, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0));

				for (cnt = 1; cnt <= 8; cnt++) {// LV1魔法
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

				for (cnt = 87; cnt <= 94; cnt++) {// 魔法
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

				if (pc.isKnight()) {
					final L1Skills l1skills = SkillsTable.get().getTemplate(COUNTER_BARRIER_VETERAN); // 反擊屏障：強化
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄

					pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.ADD, l1skills.getSkillNumber()));
					pc.setSkillMastery(l1skills.getSkillId());
				}

			} else if (pc.isElf()) {// 精靈
				pc.sendPackets(new S_AddSkill(pc, 255, 255, 127, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 127, 3,
						255, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0));
				for (cnt = 1; cnt <= 48; cnt++) {// LV1~6魔法
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}
				for (cnt = 129; cnt <= 179; cnt++) {// 魔法 176->179
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

			} else if (pc.isWizard()) {// 法師
				pc.sendPackets(new S_AddSkill(pc, 255, 255, 127, 255, 255, 255, 255, 255, 255, 255, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
				for (cnt = 1; cnt <= 80; cnt++) {// LV1~10魔法
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

			} else if (pc.isDarkelf()) {// 黑妖
				pc.sendPackets(new S_AddSkill(pc, 255, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255, 255, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 0, 0, 0, 255, 2));
				for (cnt = 1; cnt <= 16; cnt++) {// LV1~2魔法
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}
				for (cnt = 97; cnt <= 112; cnt++) {// DE魔法
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

				if (pc.isDarkelf()) {
					final L1Skills l1skills = SkillsTable.get().getTemplate(LUCIFER); // 黑妖新技能 暗影屏障
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

				// 破壞盔甲：強化
				// 雙重破壞：強化
				for (cnt = ARMOR_DESTINY; cnt <= BRAKE_DESTINY; cnt++) {
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄

					pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.ADD, l1skills.getSkillNumber()));
					pc.setSkillMastery(l1skills.getSkillId());
				}

			} else if (pc.isDragonKnight()) {// 龍騎
				pc.sendPackets(new S_AddSkill(pc, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 255,
						255, 255, 0, 0, 0, 0, 0, 0));
				for (cnt = 181; cnt <= 197; cnt++) {// 秘技
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

				// 奪命之雷：強化
				// 屠宰者：強化
				for (cnt = THUNDER_GRAB_BRAVE; cnt <= FOU_SLAYER_BRAVE; cnt++) {
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄

					pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.ADD, l1skills.getSkillNumber()));
					pc.setSkillMastery(l1skills.getSkillId());
				}

			} else if (pc.isIllusionist()) {// 幻術師
				pc.sendPackets(new S_AddSkill(pc, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 255, 255, 255, 0, 0, 0));
				for (cnt = 201; cnt <= 226; cnt++) {// 魔法
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

			} else if (pc.isWarrior()) {
				pc.sendPackets(new S_AddSkill(pc, 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
						0, 0, 0, 0, 0, 255, 255, 0));

				for (cnt = 1; cnt <= 8; cnt++) {
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

				for (cnt = HOWL; cnt <= TITANL_RISING; cnt++) {
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄
				}

				// 被動
				for (cnt = PASSIVE_CRASH; cnt <= PASSIVE_SLAYER; cnt++) {
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄

					pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.ADD, l1skills.getSkillNumber()));
					pc.setSkillMastery(l1skills.getSkillId());
				}

				for (cnt = PASSIVE_ARMORGARDE; cnt <= PASSIVE_TITANMAGIC; cnt++) {
					final L1Skills l1skills = SkillsTable.get().getTemplate(cnt); // 情報取得
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄

					pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.ADD, l1skills.getSkillNumber()));
					pc.setSkillMastery(l1skills.getSkillId());
				}

				if (pc.isWarrior()) {
					final L1Skills l1skills = SkillsTable.get().getTemplate(DESPERADO_ABSOLUTE); // 亡命之徒：強化
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					CharSkillReading.get().spellMastery(object_id, skill_id, skill_name, 0, 0); // DB登錄

					pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.ADD, l1skills.getSkillNumber()));
					pc.setSkillMastery(l1skills.getSkillId());
				}
			}

		} catch (final Exception e) {
			_log.error("錯誤的GM指令格式: " + this.getClass().getSimpleName() + " 執行的GM:" + pc.getName());
			// 261 \f1指令錯誤。
			pc.sendPackets(new S_ServerMessage(261));
		}
	}
}
