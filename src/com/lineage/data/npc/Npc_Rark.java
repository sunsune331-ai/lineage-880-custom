package com.lineage.data.npc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

public class Npc_Rark extends NpcExecutor {
	private static final Log _log = LogFactory.getLog(Npc_Rark.class);

	public static NpcExecutor get() {
		return new Npc_Rark();
	}

	public int type() {
		return 1;
	}

	public void talk(L1PcInstance pc, L1NpcInstance npc) {
		try {
			pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "rark1"));
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.data.npc.Npc_Rark JD-Core Version: 0.6.2
 */