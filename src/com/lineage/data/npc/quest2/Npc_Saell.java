package com.lineage.data.npc.quest2;

import static com.lineage.server.model.skill.L1SkillId.ADLV80_1;
import static com.lineage.server.model.skill.L1SkillId.ADLV80_2;
import static com.lineage.server.model.skill.L1SkillId.ADLV80_3;
import static com.lineage.server.model.skill.L1SkillId.ADLV80_4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 莎爾的祝福(水龍副本)
 */
public class Npc_Saell extends NpcExecutor {

	private static final Log _log = LogFactory.getLog(Npc_Saell.class);

	public static NpcExecutor get() {
		return new Npc_Saell();
	}

	public int type() {
		return 1;
	}

	public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
		try {
			if (pc.hasSkillEffect(ADLV80_1)) { // 卡瑞的祝福(地龍副本)
				pc.removeSkillEffect(ADLV80_1);
			}
			if (pc.hasSkillEffect(ADLV80_3)) { // 甘特的祝福(風龍副本)
				pc.removeSkillEffect(ADLV80_3);
			}
			if (pc.hasSkillEffect(ADLV80_4)) { // 死亡騎士的祝福(巴拉卡斯副本)
				pc.removeSkillEffect(ADLV80_4);
			}
			if (pc.hasSkillEffect(ADLV80_2)) { // 莎爾的祝福(水龍副本)
				pc.sendPackets(new S_SystemMessage("已有莎爾的祝福狀態。"));
				return;
			}
			pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7680));

			final SkillMode mode = L1SkillMode.get().getSkill(ADLV80_2);
			if (mode != null) {
				try {
					mode.start(pc, null, null, 2400);
				} catch (final Exception e) {
					_log.error(e.getLocalizedMessage(), e);
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
}
