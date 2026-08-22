package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_ChatShouting extends ServerBasePacket {
	private byte[] _byte = null;

	public S_ChatShouting(L1PcInstance pc, String chat) {
		buildPacket(pc, chat);
	}

	private void buildPacket(L1PcInstance pc, String chat) {
		writeC(S_SAY);
		writeC(2);
		writeD(pc.isInvisble() ? 0 : pc.getId());
		if (pc.isProtector()) {
			writeS("<**守護者**> " + chat);
		} else {
			writeS("<" + pc.getName() + "> " + chat);
		}

		writeH(pc.getX());
		writeH(pc.getY());
	}

	public S_ChatShouting(L1NpcInstance npc, String chat) {
		writeC(S_SAY);
		writeC(2);
		writeD(npc.isInvisble() ? 0 : npc.getId());
		writeS("<" + npc.getNameId() + "> " + chat);

		writeH(npc.getX());
		writeH(npc.getY());
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
 * com.lineage.server.serverpackets.S_ChatShouting JD-Core Version: 0.6.2
 */
