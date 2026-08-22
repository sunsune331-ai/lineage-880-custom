package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物品清單
 * 
 * @author daien
 */
public class S_PowerItemList extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * 物品清單
	 * 
	 * @param pc
	 * @param items
	 */
	public S_PowerItemList(L1PcInstance pc, int objid, List<L1ItemInstance> items) {
		writeC(S_OPCODE_SHOWRETRIEVELIST);
		writeD(objid);
		writeH(items.size());
		writeC(0x03);
		for (L1ItemInstance item : items) {
			int itemobjid = item.getId();
			writeD(itemobjid);
			writeC(item.getItem().getUseType() >= 0 ? item.getItem().getUseType() : 0);
			writeH(item.get_gfxid());
			writeC(item.getBless());
			writeD(1);
			writeC(item.isIdentified() ? 1 : 0);
			writeS(item.getViewName());
			writeStatus(item);
		}
		writeFooter();
		items.clear();
	}

	public S_PowerItemList(int objid, List<L1ItemInstance> items) {
		writeC(S_OPCODE_SHOWRETRIEVELIST);
		writeD(objid);
		writeH(items.size());
		writeC(12);
		for (L1ItemInstance item : items) {
			int itemobjid = item.getId();
			writeD(itemobjid);
			writeC(0);
			writeH(item.get_gfxid());
			writeC(item.getBless());
			writeD(1);
			writeC(item.isIdentified() ? 1 : 0);
			writeS(item.getViewName());
			writeStatus(item);
		}
		writeFooter();
		items.clear();
	}

	private void writeStatus(final L1ItemInstance item) {
		if (!item.isIdentified()) {
			writeC(0);
			return;
		}
		final byte[] status = item.getStatusBytes();
		writeC(status.length);
		for (final byte value : status) {
			writeC(value);
		}
	}

	private void writeFooter() {
		writeD(30);
		writeH(0x0000);
		writeD(0x00000000);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return getClass().getSimpleName();
	}
}
