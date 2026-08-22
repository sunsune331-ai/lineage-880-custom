package com.lineage.server.serverpackets;

public class S_HelpMessage extends ServerBasePacket {
	private byte[] _byte = null;

	public S_HelpMessage(String name, String info) {
		writeC(S_MESSAGE);
		writeC(0);
		writeD(0);
		writeS(name + " --> \\f4" + info);
	}

	public S_HelpMessage(String string) {
		writeC(S_MESSAGE);
		writeC(0);
		writeD(0);
		writeS(string);
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
 * com.lineage.server.serverpackets.S_HelpMessage JD-Core Version: 0.6.2
 */
