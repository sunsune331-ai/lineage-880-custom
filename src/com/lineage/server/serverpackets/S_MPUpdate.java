package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.RangeInt;

public class S_MPUpdate extends ServerBasePacket {
	private byte[] _byte = null;

	private static final RangeInt _mpRangeA = new RangeInt(0, 32767);

	private static final RangeInt _mpRangeX = new RangeInt(1, 32767);

	public S_MPUpdate(int currentmp, int maxmp) {
		buildPacket(currentmp, maxmp);
	}

	public S_MPUpdate(L1PcInstance pc) {
		buildPacket(pc.getCurrentMp(), pc.getMaxMp());
	}

	private void buildPacket(int currentmp, int maxmp) {
		writeC(S_OPCODE_MPUPDATE);
		writeH(_mpRangeA.ensure(currentmp));
		writeH(_mpRangeX.ensure(maxmp));
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
 * com.lineage.server.serverpackets.S_MPUpdate JD-Core Version: 0.6.2
 */