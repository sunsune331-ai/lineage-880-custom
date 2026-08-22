package com.lineage.server.serverpackets;

import java.util.Iterator;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class S_RetrieveChaList extends ServerBasePacket {
	private byte[] _byte = null;

	private byte[] status = null;

	public boolean NonValue = false;

	public S_RetrieveChaList(int objid, L1PcInstance pc) {
		if (pc.getInventory().getSize() < 180) {
			int size = pc.getDwarfForChaInventory().getSize();
			if (size > 0) {
				writeC(S_OPCODE_SHOWRETRIEVELIST);
				writeD(objid);
				writeH(size);
				writeC(18);
				for (Iterator<?> localIterator = pc.getDwarfForChaInventory().getItems().iterator(); localIterator
						.hasNext();) {
					Object itemObject = localIterator.next();
					L1ItemInstance item = (L1ItemInstance) itemObject;
					writeD(item.getId());
					int i = item.getItem().getUseType();
					if (i < 0) {
						i = 0;
					}
					writeC(i);
					writeH(item.get_gfxid());
					writeC(item.getBless());
					writeD((int) Math.min(item.getCount(), 2000000000L));
					writeC(item.isIdentified() ? 1 : 0);

					writeS(item.getViewName());
					if (!item.isIdentified()) {
						this.writeC(0);
					} else {
						status = item.getStatusBytes();
						this.writeC(status.length);
						for (byte b : status) {
							this.writeC(b);
						}
					}
				}
				this.writeD(30);
				this.writeH(0x00);
				this.writeD(0x00);
			} else
				NonValue = true;
		} else {
			pc.sendPackets(new S_ServerMessage(263));
		}
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
