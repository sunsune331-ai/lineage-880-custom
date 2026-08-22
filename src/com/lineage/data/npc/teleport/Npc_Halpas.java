package com.lineage.data.npc.teleport;

import java.util.Random;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 傳送師^魔法師立德<br>
 * 支配者結界傳送師
 */
public class Npc_Halpas extends NpcExecutor {

	private static final Random _random = new Random();

	/**
	 *
	 */
	private Npc_Halpas() {
		// TODO Auto-generated constructor stub
	}

	public static NpcExecutor get() {
		return new Npc_Halpas();
	}

	@Override
	public int type() {
		return 3;
	}

	@Override
	public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
		pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "halpas1"));
	}

	@Override
	public void action(final L1PcInstance pc, final L1NpcInstance npc, final String cmd, final long amount) {
		if (cmd.equalsIgnoreCase("control")) {

			if (pc.getLevel() < _level) {
				pc.sendPackets(new S_SystemMessage("等級低於" + _level + "無法進入。"));
				// 關閉對話窗
				pc.sendPackets(new S_CloseList(pc.getId()));
				return;
			}

			if (!pc.getInventory().checkItem(L1ItemId.ADENA, 10000)) {
				// \f1金幣不足。
				pc.sendPackets(new S_ServerMessage(189));
				// 關閉對話窗
				pc.sendPackets(new S_CloseList(pc.getId()));
				return;
			}
			pc.getInventory().consumeItem(L1ItemId.ADENA, 10000);
			L1Teleport.teleport(pc, 32835 + _random.nextInt(5), 32796 + _random.nextInt(2), (short) 15403,
					pc.getHeading(), true);
		}
	}

	private int _level = 80;

	@Override
	public void set_set(final String[] set) {
		try {
			_level = Integer.parseInt(set[1]);
		} catch (final Exception e) {
		}
	}
}
