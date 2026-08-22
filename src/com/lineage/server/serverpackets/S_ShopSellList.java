package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.config.ConfigNew;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1TaxCalculator;
import com.lineage.server.model.Instance.L1ItemStatus;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.shop.L1Shop;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopItem;
import com.lineage.server.world.World;

/**
 * NPC物品販賣
 * 
 * @author dexc
 */
public class S_ShopSellList extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * NPC物品販賣
	 */
	public S_ShopSellList(int objId) {
		writeC(S_OPCODE_SHOWSHOPBUYLIST);
		writeD(objId);

		L1Object npcObj = World.get().findObject(objId);
		if (!(npcObj instanceof L1NpcInstance)) {
			writeH(0);
			return;
		}
		int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();

		L1TaxCalculator calc = new L1TaxCalculator(npcId);
		L1Shop shop = ShopTable.get().get(npcId);
		List<?> shopItems = shop.getSellingItems();

		if (shopItems.size() <= 0) {
			writeH(0);
			return;
		}

		writeH(shopItems.size());

		for (int i = 0; i < shopItems.size(); i++) {
			L1ShopItem shopItem = (L1ShopItem) shopItems.get(i);
			L1Item item = shopItem.getItem();
			// int price = (int) (shopItem.getPrice() * ConfigRate.RATE_SHOP_SELLING_PRICE);// 物品單價
			int price = calc.layTax((int) (shopItem.getPrice() * ConfigRate.RATE_SHOP_SELLING_PRICE));

			if (((L1NpcInstance) npcObj).getNpcTemplate().getMapId() == 5124
					|| ((L1NpcInstance) npcObj).getNpcTemplate().getMapId() == ConfigNew.FishMapId
					|| ((L1NpcInstance) npcObj).getNpcTemplate().getMapId() == 5301
					|| ((L1NpcInstance) npcObj).getNpcTemplate().getMapId() == 5302) {
				price = (int) (shopItem.getPrice() * ConfigRate.RATE_SHOP_SELLING_PRICE);// 物品單價
			}
			writeD(i);// 排序

			writeH(shopItem.getItem().getGfxId());// 圖形

			writeD(price);// 售價

			/** [原碼] 出售強化物品 */
			String nameString = "";
			if (shopItem.getEnchantLevel() > 1) {
				nameString = ("+" + shopItem.getEnchantLevel() + " ");
			}
			if (shopItem.getPackCount() > 1) {
				writeS(item.getNameId() + " (" + shopItem.getPackCount() + ")");
			} else {
				writeS(nameString + item.getNameId());
			}

			// writeD(0);
			L1Item template = ItemTable.get().getTemplate(item.getItemId());
			this.writeD(template.getUseType());// XXX 7.6新增商品分類

			if (ConfigOther.SHOPINFO) {
				L1ItemStatus itemInfo = new L1ItemStatus(item);
				byte[] status = itemInfo.getStatusBytes(true).getBytes();
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
			} else {
				// 降低封包量 不傳送詳細資訊
				writeC(0);
			}

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

	/**
	 * NPC物品販賣(無稅率顯示)
	 */
	public S_ShopSellList(L1NpcInstance npc) {
		writeC(S_OPCODE_SHOWSHOPBUYLIST);
		writeD(npc.getId());

		int npcId = npc.getNpcTemplate().get_npcId();

		L1Shop shop = ShopTable.get().get(npcId);
		List<?> shopItems = shop.getSellingItems();

		if (shopItems.size() <= 0) {
			writeH(0);
			return;
		}

		writeH(shopItems.size());

		for (int i = 0; i < shopItems.size(); i++) {
			L1ShopItem shopItem = (L1ShopItem) shopItems.get(i);
			L1Item item = shopItem.getItem();
			int price = shopItem.getPrice();
			writeD(i);
			writeH(shopItem.getItem().getGfxId());
			writeD(price);
			if (shopItem.getPackCount() > 1) {
				writeS(item.getNameId() + " (" + shopItem.getPackCount() + ")");
			} else {
				writeS(item.getNameId());
			}

			//L1Item template = ItemTable.get().getTemplate(item.getItemId());
			//this.writeD(template.getUseType());// XXX 7.6新增商品分類
			
			writeC(0);
		}

		writeH(7);
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
