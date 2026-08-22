package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.server.datatables.ShopTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.shop.L1AssessedItem;
import com.lineage.server.model.shop.L1Shop;
import com.lineage.server.world.World;

public class S_ShopBuyList extends ServerBasePacket {
	private byte[] _byte = null;

	public S_ShopBuyList(int objid, L1PcInstance pc) {
		L1Object object = World.get().findObject(objid);
		if (!(object instanceof L1NpcInstance)) {
			return;
		}
		L1NpcInstance npc = (L1NpcInstance) object;
		int npcId = npc.getNpcTemplate().get_npcId();
		L1Shop shop = ShopTable.get().get(npcId);
		if (shop == null) {
			pc.sendPackets(new S_NoSell(npc));
			return;
		}

		List<L1AssessedItem> assessedItems = shop.assessItems(pc.getInventory());

		if (assessedItems.isEmpty()) {
			pc.sendPackets(new S_NoSell(npc));
			return;
		}

		if (assessedItems.size() <= 0) {
			pc.sendPackets(new S_NoSell(npc));
			return;
		}

		writeC(S_OPCODE_SHOWSHOPSELLLIST);
		writeD(objid);

		writeH(assessedItems.size());

		for (L1AssessedItem item : assessedItems) {
			writeD(item.getTargetId());
			writeD(item.getAssessedPrice());
		}

		if (npcId == 7200002 || npcId == 7200003) { // 成長果實系統(Tam幣)
			writeH(65533); // 官服

		} else if (npcId == 110641) { // 貝利商人
			writeH(14921); // 官服

		} else if (npcId == 200206) { // 湖中女神^妲蒂絲
			writeH(13371); // 自設

		} else {
			writeH(0x0007); // 7 = 金幣為單位 顯示總金額
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
