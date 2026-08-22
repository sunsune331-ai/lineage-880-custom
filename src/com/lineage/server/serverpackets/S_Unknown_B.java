package com.lineage.server.serverpackets;

/**
 * 未知 B 人物列表之前
 * 
 * @author dexc
 */
public class S_Unknown_B extends ServerBasePacket {
	
	private byte[] _byte = null;

	/**
	 * 未知 B 人物列表之前
	 * 
	 * @param i
	 */
	public S_Unknown_B(final int value) {
		writeC(S_VOICE_CHAT);
		writeC(0x0a);
		writeD(value);
	}

	public S_Unknown_B() {
		writeC(S_VOICE_CHAT);
		writeC(0x40);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}
}