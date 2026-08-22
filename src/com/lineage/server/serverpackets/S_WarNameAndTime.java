package com.lineage.server.serverpackets;

/**
 * 戰爭
 * 
 * @author simlin
 */
public class S_WarNameAndTime extends ServerBasePacket {

	public S_WarNameAndTime(final boolean isAtt, final int time, final String name) {
		writeC(S_EXTENDED_PROTOBUF); // XXX S_OPCODE_EXTENDED_PROTOBUF 修改為 S_OPCODE_CRAFTSYSTEM
		writeH(76);
		writeCC(1, isAtt ? 2 : 1);
		if (time > 0) {
			writeCC(2, time * 2);
			writeCS(3, name);
			writeH(0);
		} else {
			writeH(0x00);
		}
	}

	public S_WarNameAndTime() {
		writeC(S_EXTENDED_PROTOBUF); // XXX S_OPCODE_EXTENDED_PROTOBUF 修改為 S_OPCODE_CRAFTSYSTEM
		writeH(76);
		writeCC(1, 1L);
	}

	@Override
	public byte[] getContent() {
		return _bao.toByteArray();
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}
}
