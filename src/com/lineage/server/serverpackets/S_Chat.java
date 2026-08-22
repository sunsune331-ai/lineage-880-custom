package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_Chat extends ServerBasePacket {
	private byte[] _byte = null;

	public S_Chat(L1PcInstance pc, String chat) {
		buildPacket(pc, chat);
	}

	private void buildPacket(L1PcInstance pc, String chat) {
		writeC(S_SAY);
		writeC(0);
		writeD(pc.isInvisble() ? 0 : pc.getId());
		//if (pc.isProtector()) { //2017/04/23
		//	writeS("**守護者**: " + chat);
		//} else {
			writeS(pc.getViewName() + ": " + chat);
		//}
	}

	public S_Chat(L1NpcInstance npc, String chat) {
		writeC(S_SAY);
		writeC(0);
		writeD(npc.isInvisble() ? 0 : npc.getId());
		writeS(npc.getNameId() + ": " + chat);
	}

	public S_Chat(L1Object object, String chat) {
		writeC(S_SAY);
		writeC(0);
		writeD(object.getId());
		writeS(chat);
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
