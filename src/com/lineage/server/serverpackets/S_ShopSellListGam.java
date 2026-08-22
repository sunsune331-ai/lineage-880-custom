package com.lineage.server.serverpackets;

import java.util.Map;

import com.lineage.data.event.GamblingSet;
import com.lineage.data.event.gambling.Gambling;
import com.lineage.data.event.gambling.GamblingNpc;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.timecontroller.event.GamblingTime;

/**
 * 買食人妖精競賽票
 * 
 * @author dexc
 */
public class S_ShopSellListGam extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * 買食人妖精競賽票
	 * 
	 * @param npc
	 */
	public S_ShopSellListGam(L1PcInstance pc, L1NpcInstance npc) {
		writeC(S_OPCODE_SHOWSHOPBUYLIST);
		writeD(npc.getId());

		Gambling gambling = GamblingTime.get_gambling();
		Map<Integer, GamblingNpc> list = gambling.get_allNpc();

		if (list.size() <= 0) {
			writeH(0);
			return;
		}

		writeH(list.size());

		// 食人妖精競賽票
		L1Item item = ItemTable.get().getTemplate(40309);

		int i = 0;
		for (GamblingNpc gamblingNpc : list.values()) {
			i++;
			pc.get_otherList().add_gamList(gamblingNpc, i);

			writeD(i);
			writeH(item.getGfxId());
			writeD(GamblingSet.GAMADENA);

			int no = GamblingTime.get_gamblingNo();
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(gamblingNpc.get_npc().getNameId());
			stringBuilder.append(" [" + no + "-" + gamblingNpc.get_npc().getNpcId() + "]");

			writeS(stringBuilder.toString());
			writeD(item.getUseType());// XXX 7.6新增商品分類
			writeC(0);
		}

		writeH(0x0007); // 1810 金幣
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
