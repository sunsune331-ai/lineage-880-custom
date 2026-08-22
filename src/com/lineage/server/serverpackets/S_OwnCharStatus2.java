package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_OwnCharStatus2 extends ServerBasePacket {

	private byte[] _byte = null;

	public S_OwnCharStatus2(L1PcInstance pc) {
		if (pc == null) {
			return;
		}
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(S_OPCODE_OWNCHARSTATUS2);
		// writeC(pc.getStr());
		// writeC(pc.getInt());
		// writeC(pc.getWis());
		// writeC(pc.getDex());
		// writeC(pc.getCon());
		// writeC(pc.getCha());
		// 8.8C
		writeH(pc.getStr());
		writeH(pc.getInt());
		writeH(pc.getWis());
		writeH(pc.getDex());
		writeH(pc.getCon());
		writeH(pc.getCha());

		writeC(pc.getInventory().getWeight240());
	}

	// public S_OwnCharStatus2(L1PcInstance pc, int str) {
	// writeC(S_OPCODE_OWNCHARSTATUS2);
	// writeC(str);
	// writeC(pc.getInt());
	// writeC(pc.getWis());
	// writeC(pc.getDex());
	// writeC(pc.getCon());
	// writeC(pc.getCha());
	// writeC(pc.getInventory().getWeight240());
	// }

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
