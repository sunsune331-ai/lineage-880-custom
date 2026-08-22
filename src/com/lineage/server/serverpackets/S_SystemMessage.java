package com.lineage.server.serverpackets;

public class S_SystemMessage extends ServerBasePacket {
	private byte[] _byte = null;

	public S_SystemMessage(String msg) {
		writeC(S_OPCODE_GLOBALCHAT);
		writeC(9);
		writeS(msg);
	}

	public S_SystemMessage(String msg, boolean nameid) {
		writeC(S_SAY_CODE);
		writeC(2);
		writeD(0);
		writeS(msg);
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
 * com.lineage.server.serverpackets.S_SystemMessage JD-Core Version: 0.6.2
 */
