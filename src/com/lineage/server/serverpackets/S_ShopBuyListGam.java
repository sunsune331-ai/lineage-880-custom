package com.lineage.server.serverpackets;

import java.util.Map;

import com.lineage.data.event.GamblingSet;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Gambling;

public class S_ShopBuyListGam extends ServerBasePacket {
	private byte[] _byte = null;

	public S_ShopBuyListGam(L1PcInstance pc, L1NpcInstance npc, Map<Integer, L1Gambling> sellList) {
		writeC(S_OPCODE_SHOWSHOPSELLLIST);
		writeD(npc.getId());

		if (sellList.isEmpty()) {
			writeH(0);
			return;
		}

		if (sellList.size() <= 0) {
			writeH(0);
			return;
		}

		writeH(sellList.size());

		for (Integer itemobjid : sellList.keySet()) {
			writeD(itemobjid.intValue());
			L1Gambling gam = (L1Gambling) sellList.get(itemobjid);
			int adena = (int) (GamblingSet.GAMADENA * gam.get_rate());
			writeD(adena);
		}
	}

	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return getClass().getSimpleName();
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.serverpackets.S_ShopBuyListGam JD-Core Version: 0.6.2
 */
