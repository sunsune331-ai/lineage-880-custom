package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

//妖精倉庫
public class S_RetrieveElfList extends ServerBasePacket {
	private byte[] _byte = null;

	public boolean NonValue = false;

	public S_RetrieveElfList(int objid, L1PcInstance pc) {
		if (pc.getInventory().getSize() < 180) {
			int size = pc.getDwarfForElfInventory().getSize();
			if (size > 0) {
				writeC(S_OPCODE_SHOWRETRIEVELIST);
				writeD(objid);
				writeH(size);
				writeC(9);
				for (final Object itemObject : pc.getDwarfForElfInventory().getItems()) {
					final L1ItemInstance item = (L1ItemInstance) itemObject;
					this.writeD(item.getId());
					int i = item.getItem().getUseType();
					if (i < 0) {
						i = 0;
					}
					this.writeC(i);
					this.writeH(item.get_gfxid());
					this.writeC(item.getBless());
					this.writeD((int) Math.min(item.getCount(), 2000000000));
					this.writeC(item.isIdentified() ? 0x01 : 0x00);

					this.writeS(item.getViewName());
				}
				this.writeH(2);
				this.writeD(0);
				this.writeD(0);
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
