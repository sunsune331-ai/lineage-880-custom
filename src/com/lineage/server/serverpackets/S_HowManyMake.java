package com.lineage.server.serverpackets;

public class S_HowManyMake extends ServerBasePacket {
	private byte[] _byte = null;

	public S_HowManyMake(int objId, int max, String htmlId) {
		writeC(S_OPCODE_INPUTAMOUNT);
		writeD(objId);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(max);
		writeH(0);
		writeS("request");
		writeS(htmlId);
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
 * com.lineage.server.serverpackets.S_HowManyMake JD-Core Version: 0.6.2
 */