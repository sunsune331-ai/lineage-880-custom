package com.lineage.data.npc.teleport;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 風龍棲息地水晶
 */
public class Npc_H_field extends NpcExecutor {

	/**
	 *
	 */
	private Npc_H_field() {
		// TODO Auto-generated constructor stub
	}

	public static NpcExecutor get() {
		return new Npc_H_field();
	}

	@Override
	public int type() {
		return 3;
	}

	@Override
	public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
		pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "H_field"));
	}

	@Override
	public void action(final L1PcInstance pc, final L1NpcInstance npc, final String cmd, final long amount) {
		if (cmd.equalsIgnoreCase("a")) {
			if (!pc.getInventory().checkItem(L1ItemId.ADENA, 2000)) {
				// \f1金幣不足。
				pc.sendPackets(new S_ServerMessage(189));
				// 關閉對話窗
				pc.sendPackets(new S_CloseList(pc.getId()));
				return;
			}
			pc.getInventory().consumeItem(L1ItemId.ADENA, 2000);
			L1Teleport.teleport(pc, 34145, 32794, (short) 15410, 2, true);
		}
	}
}
