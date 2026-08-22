package com.lineage.server.templates;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.protobuf.ByteString;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.utils.LineageUtil;
import com.lineage.server.utils.Random;

import l1j.server.server.datas.protobuf.PBMessageALL;
import l1j.server.server.datas.protobuf.PBMessageALL3;
import l1j.server.server.datas.protobuf.PBMessageALL4;
import l1j.server.server.datas.protobuf.PBMessageALL6;

public class L1Craft {

	private int _craftID = 0;

	public int getCraftID() {
		return _craftID;
	}

	public L1Craft(final int craftID) {
		_craftID = craftID;

		// bit |= getItem().isUseRoyal() ? 1 : 0;
		// bit |= getItem().isUseKnight() ? 2 : 0;
		// bit |= getItem().isUseElf() ? 4 : 0;
		// bit |= getItem().isUseMage() ? 8 : 0;
		// bit |= getItem().isUseDarkelf() ? 16 : 0;
		// bit |= getItem().isUseDragonknight() ? 32 : 0;
		// bit |= getItem().isUseIllusionist() ? 64 : 0;
		// bit |= getItem().isUseWarrior() ? 128 : 0;
		/*if (craftID == 576) { // 製作戰士的印記(護甲身軀)
			_classLimit = 128;
		} else if (craftID == 577) { // 製作龍騎士書板(屠宰者)
			_classLimit = 32;
		} else if (craftID == 578) { // 製作精靈水晶(三重矢)
			_classLimit = 4;
		} else if (craftID == 616) {// 一拳手套(1小時)
			final L1ItemInstance item = ItemTable.get().createItem(413);
			_ignore_list.put(item.getId(), item);
		}*/
	}

	private int _classLimit = 0;// 可製作的角色職業屬性/ 與物品詳細資訊的判斷職業bit相同/不能製作的職業無法顯示出製作清單

	/**
	 * 可製作的角色職業屬性/ 與物品詳細資訊的判斷職業bit相同/不能製作的職業無法顯示出製作清單<br>
	 * 不判斷-0<br>
	 * 王族-1<br>
	 * 騎士-2<br>
	 * 妖精-4<br>
	 * 法師-8<br>
	 * 黑妖-16<br>
	 * 龍騎-32<br>
	 * 幻術-64<br>
	 * 戰士-128
	 */
	public int getClassLimit() {
		return _classLimit;
	}

	/**
	 * 可製作的角色職業屬性/ 與物品詳細資訊的判斷職業bit相同/不能製作的職業無法顯示出製作清單<br>
	 * 不判斷-0<br>
	 * 王族-1<br>
	 * 騎士-2<br>
	 * 妖精-4<br>
	 * 法師-8<br>
	 * 黑妖-16<br>
	 * 龍騎-32<br>
	 * 幻術-64<br>
	 * 戰士-128
	 */
	public void setClassLimit(final int i) {
		_classLimit = i;
	}

	private final ArrayList<L1ItemInstance> _craft_item_list = new ArrayList<>();

	/**
	 * 製作給予的道具
	 * @param itemid
	 * @param count
	 * @param enchant
	 * @param attrid
	 * @param attrlv
	 */
	public void setCraftItem(final int itemid, final int count, final int enchant, final int attrid, final int attrlv) {
		final L1ItemInstance item = ItemTable.get().createItem(itemid);
		// if (!item.isStackable() && count > 1) {
		if (item == null) {
			System.out.println("L1Craft CraftItem is null itemid=" + itemid + "craftid=" + _craftID);
			return;
		}
		item.setCount(count);
		// item.setBless(1);
		item.setEnchantLevel(enchant);
		// item.setItemEffectLoad();

		// 新增武器屬性
		if (item.getItem().getType2() == 1) { // 武器類
			item.setAttrEnchantKind(attrid);
			item.setAttrEnchantLevel(attrlv);
		}

		_craft_item_list.add(item);
	}

	public L1ItemInstance getCraftItem() {
		return _craft_item_list.get(Random.nextInt(_craft_item_list.size()));
	}

	/*
	 * private L1ItemInstance _craft_item = null;
	 * 
	 * public void setCraftItem(int itemid, int count, int enchant) {
	 * L1ItemInstance item = ItemTable.get().createItem(itemid);
	 * item.setCount(count);
	 * item.setBless(1);
	 * item.setEnchantLevel(enchant);
	 * _craft_item = item;
	 * 
	 * }
	 * 
	 * public L1ItemInstance getCraftItem() {
	 * return _craft_item;
	 * }
	 */

	private final HashMap<Integer, L1ItemInstance> _material_list = new HashMap<>();
	private final HashMap<Integer, ArrayList<L1ItemInstance>> _exchange_list = new HashMap<>();
	private final HashMap<Integer, L1ItemInstance> _ignore_list = new HashMap<>();

	private final ArrayList<Integer> _sequence = new ArrayList<>();

	private int _craft_nameid = 0;

	public void setCraftNameID(final int i) {
		_craft_nameid = i;
	}

	public int getCraftNameID() {
		return _craft_nameid;
	}

	private L1ItemInstance _add_chance_item = null; // 加增機率的道具

	/**
	 * 加增機率的道具
	 * @return
	 */
	public L1ItemInstance getAddChanceItem() {
		return _add_chance_item;
	}

	/**
	 * 加增機率的道具
	 * @param itemid
	 * @param count
	 */
	public void setAddChanceItem(final int itemid, final int count) {
		if (itemid <= 0 || count <= 0) {
			return;
		}
		final L1ItemInstance item = ItemTable.get().createItem(itemid);
		// item.setCount(10);
		item.setCount(count);
		item.setBless(1);

		_add_chance_item = item;
	}

	private L1ItemInstance _craft_fail_item = null; // 失敗時退回的道具

	/**
	 * 失敗時退回的道具
	 * @return
	 */
	public L1ItemInstance getCraftFailItem() {
		return _craft_fail_item;
	}

	/**
	 * 失敗時退回的道具
	 * @param itemid
	 * @param count
	 * @param enchant
	 * @param attrid
	 * @param attrlv
	 */
	public void setCraftFailItem(final int itemid, final int count, final int enchant, final int attrid,
			final int attrlv) {
		if (itemid <= 0 || count <= 0) {
			return;
		}
		final L1ItemInstance item = ItemTable.get().createItem(itemid);
		item.setCount(count);
		//item.setBless(1);
		item.setEnchantLevel(enchant);

		// 新增武器屬性
		if (item.getItem().getType2() == 1) { // 武器類
			item.setAttrEnchantKind(attrid);
			item.setAttrEnchantLevel(attrlv);
		}

		_craft_fail_item = item;
	}

	private int _perfect_chance = 0; // 大成功機率

	/**
	 * 大成功機率
	 * @return
	 */
	public int getPerfectCance() {
		return _perfect_chance;
	}

	/**
	 * 大成功機率
	 * @param i
	 */
	public void setPerfectCance(final int i) {
		_perfect_chance = i;
	}

	private L1ItemInstance _craft_perfect_item = null; // 大成功時道具

	/**
	 * 大成功時道具
	 * @return
	 */
	public L1ItemInstance getCraftPerfectItem() {
		return _craft_perfect_item;
	}

	/**
	 * 大成功時道具
	 * @param itemid
	 * @param count
	 * @param enchant
	 */
	public void setCraftPerfectItem(final int itemid, final int count, final int enchant) {
		if (itemid <= 0 || count <= 0) {
			return;
		}
		final L1ItemInstance item = ItemTable.get().createItem(itemid);
		if (item == null) {
			System.out.println("L1Craft PerfectItem is null itemid=" + itemid + "craftid=" + _craftID);
			return;
		}
		item.setCount(count);
		// item.setBless(1);
		item.setEnchantLevel(enchant);

		_craft_perfect_item = item;
	}

	private int _showWorld; // 打造道具成功是否公告

	/**
	 * 打造道具成功是否公告
	 * @return
	 */
	public int getShowWorld() {
		return _showWorld;
	}

	/**
	 * 打造道具成功是否公告
	 * @param i
	 */
	public void setShowWorld(final int i) {
		_showWorld = i;
	}

	// private int _ignoreItem; // 身上已有此編號道具將不能在製作
	//
	// /**
	// * 身上已有此編號道具將不能在製作
	// * @return
	// */
	// public int getIgnoreItem() {
	// return _ignoreItem;
	// }

	/**
	 * 身上已有此編號道具將不能在製作
	 * @param itemid
	 */
	public void addIgnoreItem(final int itemid) {
		if (itemid <= 0) {
			return;
		}
		final L1ItemInstance item = ItemTable.get().createItem(itemid);
		if (item == null) {
			System.out.println("L1Craft addIgnoreItem is null id=" + itemid + "craftid=" + _craftID);
			return;
		}
		_ignore_list.put(item.getId(), item);
	}

	/**
	 * 所需的材料
	 * @param itemid
	 * @param count
	 * @param enchant
	 * @param bless
	 * @param attrid
	 * @param attrlv
	 */
	public void addMaterialItem(final int itemid, final int count, final int enchant, final int bless, final int attrid,
			final int attrlv) {
		final L1ItemInstance item = ItemTable.get().createItem(itemid);

		if (item == null) {
			System.out.println("L1Craft addMaterialItem is null id=" + itemid + "craftid=" + _craftID);
			return;
		}
		item.setCount(count);
		item.setBless(bless);
		item.setEnchantLevel(enchant);
		item.setIdentified(true);

		// 新增武器屬性
		if (item.getItem().getType2() == 1) { // 武器類
			item.setAttrEnchantKind(attrid);
			item.setAttrEnchantLevel(attrlv);
		}

		_material_list.put(item.getItemId(), item);
		_exchange_list.put(itemid, new ArrayList<L1ItemInstance>()); // 預先配置可替換清單
		_sequence.add(itemid); // 順序
	}

	/**
	 * 可替換的材料
	 * @param materialID
	 * @param exchangeItemid
	 * @param exchangeItemCount
	 * @param exchangeItemEnchant
	 * @param exchangeItemBless
	 * @param exchangeItemAttrid
	 * @param exchangeItemAttrlv
	 */
	public void setExchangeable(final int materialID, final int exchangeItemid, final int exchangeItemCount,
			final int exchangeItemEnchant, final int exchangeItemBless, final int exchangeItemAttrid,
			final int exchangeItemAttrlv) {
		final ArrayList<L1ItemInstance> list = _exchange_list.get(materialID);
		// if (!list.containsKey(exchangeItemid)) {
		final L1ItemInstance item = ItemTable.get().createItem(exchangeItemid);
		item.setCount(exchangeItemCount);
		item.setBless(exchangeItemBless);
		item.setEnchantLevel(exchangeItemEnchant);
		item.setIdentified(true);
		// 新增武器屬性
		if (item.getItem().getType2() == 1) { // 武器類
			item.setAttrEnchantKind(exchangeItemAttrid);
			item.setAttrEnchantLevel(exchangeItemAttrlv);
		}
		list.add(item);
		// }
	}

	public HashMap<Integer, L1ItemInstance> getMaterialItems() {
		return _material_list;
	}

	public HashMap<Integer, ArrayList<L1ItemInstance>> getExchangeItemList() {
		return _exchange_list;
	}

	public ByteString getByteString() {
		final PBMessageALL3.type6.Builder builder = PBMessageALL3.type6.newBuilder();
		builder.setValue1(_craftID);
		builder.setArray2(getNormalSetting()); // 一般設定
		builder.setValue3(_classLimit);// 可製作的角色職業屬性/ 與物品詳細資訊的判斷職業bit相同/不能製作的職業無法顯示出製作清單
		builder.setArray4(getQuestSetting());
		builder.setArray5(getIntArray(0)); // poly
		builder.setArray6(getListCheckSetting()); // 製作清單的設定
		builder.setArray7(getMaterialList()); // 材料道具
		builder.setArray8(getCraftList()); // 製作道具
		builder.setValue9(3);// 原本:0 XXX
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	private ByteString getListCheckSetting() {
		final PBMessageALL.type2.Builder builder2 = PBMessageALL.type2.newBuilder();

		builder2.setValue1(1); // 如果關閉的話該結構以下的設定將無效

		builder2.setValue2(_ignore_list.size());// 背包具有物品的判斷

		for (final L1ItemInstance item : _ignore_list.values()) {
			final PBMessageALL.type1.Builder builder1 = PBMessageALL.type1.newBuilder();
			builder1.setValue1(item.getItem().getItemDescId());
			builder1.setValue2((int) item.getCount());
			builder1.setValue3(1); // 0x00:必須要有該物品以及足夠的數量才能製作 0x01:身上具有該物品 無論多少數量皆無法製作
			builder2.addArray3(builder1.build().toByteString());
		}

		return ByteString.copyFrom(builder2.build().toByteArray());
	}

	/**
	 * 一般設定
	 * @return
	 */
	private ByteString getNormalSetting() {
		final PBMessageALL.type1.Builder builder = PBMessageALL.type1.newBuilder();

		final String name = _craft_item_list.get(0).getItem().getNameId().trim();

		int nameid = 994;
		if (!name.isEmpty() && name.contains("$")) {
			final String[] splite = name.split("\\$");
			nameid = Integer.parseInt(splite[splite.length - 1].trim());
		}
		builder.setValue1(_craft_nameid > 0 ? _craft_nameid : nameid);// craft item name (without $)
		builder.setValue2(_minLevel);// min level
		builder.setValue3(_maxLevel);// max level
		builder.setValue4(2);
		builder.setValue5(_minLawful);// min lawful
		builder.setValue6(_maxLawful);// max lawful
		builder.setValue7(_minKarma);// KARMA
		builder.setValue8(_maxKarma);
		builder.setValue9(_maxCraftCount);// max craft count
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	/**
	 * 製作道具icon屬性
	 * @param _craft_item
	 * @param isSettingBless
	 * @return
	 */
	public ByteString getCraftItemData(final L1ItemInstance _craft_item, final boolean isSettingBless) {
		final PBMessageALL.type3.Builder builder = PBMessageALL.type3.newBuilder();
		builder.setValue1(_craft_item.getItemId());
		builder.setValue2((int) _craft_item.getCount());
		builder.setValue3(-1); // 材料窗口順序?
		builder.setValue4(_craft_item.getEnchantLevel());
		builder.setValue5(isSettingBless ? 0 : _craft_item.getBless()); // getBless()

		// 官服顯示
		builder.setValue6(_craft_item.getAttrEnchantKind()); // 屬性類型getAttrEnchantKind()-1火/2水/3風/4地
		builder.setValue7(_craft_item.getAttrEnchantLevel());// 屬性等級getAttrEnchantLevel()

		builder.setArray8(LineageUtil.getByteString(_craft_item.getName()));
		// builder.setArray8(LineageUtil.getByteString(_craft_item.getLogName())); // 新增武器屬性修改

		builder.setValue9(0); // DescId
		builder.setValue10(0);
		builder.setValue11(_craft_item.get_gfxid());
		builder.setArray12(ByteString.copyFromUtf8(""));
		builder.setArray13(ByteString.copyFrom(_craft_item.getStatusBytes()));
		builder.setValue14(0);
		builder.setValue15(0);
		builder.setValue16(_perfect_chance > 0 ? 1 : 0);// boolean 大成功時道具
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	/**
	 * 製作道具icon大成功時屬性
	 * @return
	 */
	public ByteString getCraftItemBlessSetting() {
		// type13

		final PBMessageALL4.type13.Builder builder = PBMessageALL4.type13.newBuilder();
		builder.setValue1(1);
		builder.setValue2(0);
		builder.setValue3(1);
		// builder.setArray5(getCraftItemData(_craft_item_list.get(0), true));

		// 大成功時道具
		if (_perfect_chance > 0) {
			if (_craft_perfect_item != null) {
				if (_craft_perfect_item.getBless() == 0) {
					builder.setArray5(getCraftItemData(_craft_perfect_item, true));
				} else {
					builder.setArray5(getCraftItemData(_craft_perfect_item, false));
				}
			} else {
				//builder.setArray5(getCraftItemData(_craft_item_list.get(0), true));
				System.out.println("大成功道具未設置或不存在: craftID" + _craftID);
				System.out.println("大成功道具未設置或不存在: craftID" + _craftID);
				System.out.println("大成功道具未設置或不存在: craftID" + _craftID);
			}
		}

		return ByteString.copyFrom(builder.build().toByteArray());
	}

	/**
	 * 製作材料icon屬性
	 * @param index
	 * @param item
	 * @return
	 */
	private ByteString getMaterialItemData(final int index, final L1ItemInstance item) {
		final PBMessageALL3.type7.Builder builder = PBMessageALL3.type7.newBuilder();

		builder.setValue1(item.getItem().getItemDescId()); // id
		builder.setValue2((int) item.getCount()); // count
		builder.setValue3(index);// index
		builder.setValue4(item.getEnchantLevel()); // enchant level

		builder.setValue5(item.getBless());// _craft_item_list.get(0).getBless()); // getBless() 正服 3 : 皆可用

		// name
		// builder.setArray6(LineageUtil.getByteString(item.getName()));
		builder.setArray6(LineageUtil.getByteString(item.getLogName()));

		builder.setValue7(item.get_gfxid()); // inv gfx
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	/**
	 * 目前只能做一個道具..
	 * @return
	 */
	private ByteString getCraftList() {
		final PBMessageALL.type4.Builder builder = PBMessageALL.type4.newBuilder();
		builder.setArray1(getCraftSetting());
		builder.setArray2(getUnKnowData4());// XXX 失敗時的產物
		builder.setValue3(_chance * 10000); // chance *10000
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	/**
	 * 材料清單
	 * @return
	 */
	private ByteString getMaterialList() {
		final PBMessageALL4.type15.Builder builder = PBMessageALL4.type15.newBuilder();
		int index = 0;

		for (final int i : _sequence) {
			// for (L1ItemInstance item : _material_list.values()) {
			final L1ItemInstance item = _material_list.get(i);
			builder.addArray1(getMaterialItemData(++index, item));

			// 有可替換的...index要相同
			if (_exchange_list.containsKey(item.getItemId())) {
				for (final L1ItemInstance exChange : _exchange_list.get(item.getItemId())) {
					builder.addArray1(getMaterialItemData(index, exChange));
				}
			}

		}
		// builder.addArray2--->增加機率道具

		if (_add_chance_item != null) {
			builder.addArray2(getMaterialItemData(++index, _add_chance_item));
		}
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	private ByteString getQuestSetting() {
		final PBMessageALL.type2.Builder builder = PBMessageALL.type2.newBuilder();
		builder.setValue1(1);
		builder.setValue2(0);
		builder.addArray3(getIntArray(0, 0));// (quest,step)
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	private ByteString getUnKnowData4() {
		final PBMessageALL.type4.Builder builder = PBMessageALL.type4.newBuilder();
		builder.setArray1(getIntArray(0, 0));
		builder.setArray2(getIntArray(4294967295L, 4294967295L)); // long
		builder.setValue3(0);
		builder.setValue4(0);

		if (_craft_fail_item != null) {
			builder.setValue4(1);
			builder.setArray6(getCraftFailItemData());
		}

		builder.setValue8(0);

		return ByteString.copyFrom(builder.build().toByteArray());
	}

	/**
	 * 製作失敗時道具icon屬性
	 * @return
	 */
	public ByteString getCraftFailItemData() {
		final PBMessageALL.type3.Builder builder = PBMessageALL.type3.newBuilder();
		builder.setValue1(_craft_fail_item.getItemId());// 7386
		builder.setValue2((int) _craft_fail_item.getCount());
		builder.setValue3(-1);
		builder.setValue4(_craft_fail_item.getEnchantLevel());
		builder.setValue5(_craft_fail_item.getBless()); // getBless()
		builder.setValue6(0); // 屬性類型 1火/2水/3風/4地
		builder.setValue7(0); // 屬性等級

		builder.setArray8(LineageUtil.getByteString(_craft_fail_item.getName()));
		// builder.setArray8(LineageUtil.getByteString(_craft_fail_item.getLogName())); // 新增武器屬性修改

		builder.setValue9(0);
		builder.setValue10(0);
		builder.setValue11(_craft_fail_item.get_gfxid());
		builder.setArray12(ByteString.copyFromUtf8(""));
		builder.setArray13(ByteString.copyFrom(_craft_fail_item.getStatusBytes()));
		// builder.setValue14(0);
		// builder.setValue15(0);
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	private ByteString getCraftSetting() {
		final PBMessageALL6.type25.Builder builder = PBMessageALL6.type25.newBuilder();
		builder.setArray1(getIntArray(0, 0));
		builder.setArray2(getIntArray(4294967295L, 4294967295L)); // long

		final int size = _craft_item_list.size();
		builder.setValue3(size > 1 ? size : 0); // SIZE :大於0表示隨機送
		builder.setValue4(1);

		if (size > 1) {
			// 隨機送
			for (final L1ItemInstance _craft_item : _craft_item_list) {
				builder.addArray5(getCraftItemData(_craft_item, false));
			}
		} else {
			builder.setArray6(getCraftItemData(_craft_item_list.get(0), false));
			//builder.setArray6(getCraftItemData(_craft_perfect_item, false));
			builder.setArray7(getCraftItemBlessSetting());
		}

		builder.setValue8(0);

		return ByteString.copyFrom(builder.build().toByteArray());
	}

	private ByteString getIntArray(final int i) {
		final PBMessageALL4.type11.Builder builder = PBMessageALL4.type11.newBuilder();
		builder.setValue1(i);
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	public ByteString getIntArray(final long i, final long j) {
		final PBMessageALL4.type11.Builder builder = PBMessageALL4.type11.newBuilder();
		builder.setValue1(i);
		builder.setValue2(j);
		return ByteString.copyFrom(builder.build().toByteArray());
	}

	// -----------額外屬性設定

	private int _minLevel = 1;
	private int _maxLevel = 99;
	private int _maxLawful = 32767;
	private int _minLawful = -32768;
	private int _maxCraftCount = 100;
	private int _minKarma = -2147483648;
	private int _maxKarma = 2147483647;
	private int _chance = 100;

	public void setSuccessChance(final int chance) {
		_chance = chance;
	}

	public int getSuccessChance() {
		return _chance;
	}

	public void setLevelLimit(final int min, final int max) {
		_minLevel = min;
		_maxLevel = max;
	}

	public void setLawfulLimit(final int min, final int max) {
		_minLawful = min;
		_maxLawful = max;
	}

	public void setKarmaLimit(final int min, final int max) {
		_minKarma = min;
		_maxKarma = max;
	}

	public void setMaxCraftCount(final int count) {
		_maxCraftCount = count;
	}

}
