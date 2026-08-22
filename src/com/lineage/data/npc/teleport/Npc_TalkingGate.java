package com.lineage.data.npc.teleport;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

/**
 * 話島守門人
 */
public class Npc_TalkingGate extends NpcExecutor {

	/**
	 *
	 */
	private Npc_TalkingGate() {
		// TODO Auto-generated constructor stub
	}

	public static NpcExecutor get() {
		return new Npc_TalkingGate();
	}

	@Override
	public int type() {
		return 3;
	}

	@Override
	public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
		pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "talkinggate02"));
	}

	@Override
	public void action(final L1PcInstance pc, final L1NpcInstance npc, final String cmd, final long amount) {
		if (cmd.equalsIgnoreCase("b")) {
			// 3秒無敵狀態
			// pc.setSkillEffect(ABSOLUTE_BARRIER, 3000);
			// pc.stopHpRegeneration();
			// pc.stopMpRegeneration();
			L1Teleport.teleport(pc, 32593, 32900, (short) 9, pc.getHeading(), true);

		} else if (cmd.equalsIgnoreCase("c")) {
			// 3秒無敵狀態
			// pc.setSkillEffect(ABSOLUTE_BARRIER, 3000);
			// pc.stopHpRegeneration();
			// pc.stopMpRegeneration();
			L1Teleport.teleport(pc, 32561, 32927, (short) 9, pc.getHeading(), true);

		} else if (cmd.equalsIgnoreCase("d")) {
			// 3秒無敵狀態
			// pc.setSkillEffect(ABSOLUTE_BARRIER, 3000);
			// pc.stopHpRegeneration();
			// pc.stopMpRegeneration();
			L1Teleport.teleport(pc, 32571, 32986, (short) 9, pc.getHeading(), true);

		} else if (cmd.equalsIgnoreCase("e")) {
			// 3秒無敵狀態
			// pc.setSkillEffect(ABSOLUTE_BARRIER, 3000);
			// pc.stopHpRegeneration();
			// pc.stopMpRegeneration();
			L1Teleport.teleport(pc, 32540, 32960, (short) 9, pc.getHeading(), true);
		}
	}
}
