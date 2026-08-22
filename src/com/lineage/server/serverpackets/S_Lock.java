package com.lineage.server.serverpackets;

public class S_Lock extends ServerBasePacket {
	private byte[] _byte = null;

	public S_Lock() {
		buildPacket();
	}

	private void buildPacket() {
		writeC(S_BLINK);
		writeC(0);
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