package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

public class S_ItemStatus extends ServerBasePacket {
	private byte[] _byte = null;

	public S_ItemStatus(L1ItemInstance item) {
		if (item == null) {
			return;
		}
		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		writeC(S_OPCODE_ITEMAMOUNT);
		writeD(item.getId());
		writeS(item.getViewName());

		int count = (int) Math.min(item.getCount(), 2000000000L);

		writeD(count);

		if (!item.isIdentified()) {
			writeC(0);
		} else {
			byte[] status = item.getStatusBytes();
			writeC(status.length);
			for (byte b : status)
				writeC(b);
		}
		writeH(0);
	}

	public S_ItemStatus(L1ItemInstance item, long count) {
		writeC(S_OPCODE_ITEMAMOUNT);
		writeD(item.getId());
		writeS(item.getNumberedViewName(count));

		int out_count = (int) Math.min(count, 2000000000L);

		writeD(out_count);

		if (!item.isIdentified()) {
			writeC(0);
		} else {
			byte[] status = item.getStatusBytes();
			writeC(status.length);
			for (byte b : status)
				writeC(b);
		}
		writeH(0);
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

