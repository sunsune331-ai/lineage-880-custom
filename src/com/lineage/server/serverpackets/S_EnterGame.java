package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_EnterGame extends ServerBasePacket {
	private byte[] _byte = null;

	public S_EnterGame(L1PcInstance pc) {
		writeC(S_ENTER_WORLD_CHECK);
		writeC(0x03);
		writeC(0x15);
		writeC(0x8b);
		writeC(0x7b);
		writeC(0x94);
		writeC(0xf0);
		writeC(0x2f);
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
 * Location: C:\Users\kenny\Desktop\伊薇380\ Qualified Name:
 * com.lineage.server.serverpackets.S_EnterGame JD-Core Version: 0.6.2
 */