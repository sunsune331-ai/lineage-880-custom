package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 受到殷海薩的祝福，增加了些許的狩獵經驗值
 */
public class S_PacketBoxExp extends ServerBasePacket {

	private byte[] _byte = null;

	public static final int EINHASAD = 1020;

	/**
	 * 受到殷海薩的祝福，增加了些許的狩獵經驗值
	 * 
	 * @param exp 經驗值增加率
	 */
	public S_PacketBoxExp(final int exp, final L1PcInstance pc) {
		writeC(S_EVENT);
		writeC(0x52);
		writeD(exp);
		writeD(7700);
		writeD(0);
	}

	/**
	 * 解除 受到殷海薩的祝福，增加了些許的狩獵經驗值
	 */
	public S_PacketBoxExp() {
		writeC(S_EVENT);
		writeC(0x52);
		writeD(0);
		writeD(7700);
		writeD(0);
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
