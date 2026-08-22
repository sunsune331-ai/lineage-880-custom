package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_ChatParty2 extends ServerBasePacket {
	private byte[] _byte = null;

	public S_ChatParty2(L1PcInstance pc, String chat) {
		buildPacket(pc, chat);
	}

	private void buildPacket(L1PcInstance pc, String chat) {
		writeC(S_SAY);
		writeC(14);
		writeD(pc.isInvisble() ? 0 : pc.getId());
		writeS("(" + pc.getName() + ") " + chat);
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
 * com.lineage.server.serverpackets.S_ChatParty2 JD-Core Version: 0.6.2
 */
