package com.lineage.server.templates;

import com.lineage.server.datatables.ItemTable;

public class L1ShopItem {
	private final int _id;
	private final int _itemId;
	private final L1Item _item;
	private final int _price;
	private final int _packCount;
	/** [原碼] 出售強化物品 */
	private final int _EnchantLevel;
	private final int _checkclass;
	/**
	 * 商城物品用
	 * 
	 * @param id
	 * @param itemId
	 * @param price
	 * @param packCount
	 * @param enchantlevel
	 */
	public L1ShopItem(int id, int itemId, int price, int packCount, int enchantlevel,int checkclass) {
		_id = id;
		_itemId = itemId;
		_item = ItemTable.get().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
		_EnchantLevel = enchantlevel;
		this._checkclass = checkclass;	
	}
	public L1ShopItem(int itemId, int price, int packCount, int enchantlevel,int checkclass) {
		_id = -1;
		_itemId = itemId;
		_item = ItemTable.get().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
		_EnchantLevel = enchantlevel;
		this._checkclass = checkclass;
	}

	public int getId() {
		return _id;
	}

	public int getItemId() {
		return _itemId;
	}

	public L1Item getItem() {
		return _item;
	}

	public int getPrice() {
		return _price;
	}

	public int getPackCount() {
		return _packCount;
	}

	/** [原碼] 出售強化物品 */
	public int getEnchantLevel() {
		return _EnchantLevel;
	}
	
	public int getCheckClass() {
		return this._checkclass;
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.templates.L1ShopItem JD-Core Version: 0.6.2
 */