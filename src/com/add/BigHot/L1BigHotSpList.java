package com.add.BigHot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.add.L1Config;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class L1BigHotSpList {

	private final L1PcInstance _pc;

	public L1BigHotSpList(final L1PcInstance pc) {
		_pc = pc;
	}

	public void clear() {
		_BigHotList.clear();

		_BigHotSellList.clear();
	}

	private final ArrayList<int[]> _BigHotList = new ArrayList<int[]>();

	public void set_copyBigHot(final ArrayList<int[]> invList) {
		clear();

		_BigHotList.addAll(invList);
	}

	public ArrayList<int[]> get_BigHotList() {
		return _BigHotList;
	}

	/*
	 * public void addBigHotItem(int order, int count) { int[] BigHotItem =
	 * (int[])this._BigHotList.get(order);
	 * 
	 * int price = L1Config._2163 * count;
	 * 
	 * checkBigHotShopItem(BigHotItem, count, price); }
	 */

	/*
	 * private void checkBigHotShopItem(int[] BigHotItem, int amount, int
	 * totalPrice) { int price = 0; L1ItemInstance priceItem = null;
	 * 
	 * priceItem = this._pc.getInventory().findItemId(L1Config._2167);
	 * 
	 * if (priceItem != null) { price = (int) priceItem.getCount(); } if
	 * (totalPrice > price) { this._pc.sendPackets(new
	 * S_SystemMessage("商城金幣不足，無法押注。")); } else {
	 * this._pc.getInventory().removeItem(priceItem, totalPrice);
	 * 
	 * L1PcInventory inv = this._pc.getInventory();
	 * 
	 * int itemId = L1Config._2170; int BigHotNo = BigHotItem[1]; int
	 * BigHotNpcId = BigHotItem[0];
	 * 
	 * L1ItemInstance item = ItemTable.get().createItem(itemId);
	 * 
	 * item.setCount(amount); item.setIdentified(true); item.setGamNo(BigHotNo);
	 * item.setGamNpcId(BigHotNpcId);
	 * 
	 * inv.storeItem(item); } }
	 */

	public void checkBigHotShop() {
		clear();
	}

	private final ArrayList<L1ItemInstance> _BigHotSellList = new ArrayList<L1ItemInstance>();

	public void set_copySellBigHot(final ArrayList<L1ItemInstance> invList) {
		clear();

		_BigHotSellList.addAll(invList);
	}

	public ArrayList<L1ItemInstance> get_BigHotSellList() {
		return _BigHotSellList;
	}

	public void addSellBigHotItem(final int objid, final int count) {
		boolean isOk = false;
		final L1ItemInstance BigHotItem = _pc.getInventory().getItem(objid);

		for (final L1ItemInstance chItem : _BigHotSellList) {
			if (chItem == BigHotItem) {
				isOk = true;
			}
		}

		if (isOk) {
			final int BigHotId = BigHotItem.getGamNo();

			int price = 0;

			final L1BigHotbling BigHotInfo = BigHotblingLock.create().getBigHotbling(BigHotId);

			if (BigHotInfo != null) {
				final String A = BigHotInfo.get_number();
				final String B = BigHotItem.getStarNpcId();

				int AB = BigHotInfo.get_money1();
				final int BC = BigHotInfo.get_count();
				if (BC != 0) {
					AB /= BC;
				}

				int CD = BigHotInfo.get_money2();
				final int DE = BigHotInfo.get_count1();
				if (DE != 0) {
					CD /= DE;
				}

				int EF = BigHotInfo.get_money3();
				final int FG = BigHotInfo.get_count2();
				if (FG != 0) {
					EF /= FG;
				}

				int ch = 0;
				for (int a = 0; a < A.split(",").length; a++) {
					final String[] pk = B.split(",");
					if (("," + A).indexOf("," + pk[a] + ",") >= 0)
						ch++;
				}
				if (ch >= 3) {
					switch (ch) {
					case 3:
						price = 50 * count;
						break;
					case 4:
						price = EF * count;
						break;
					case 5:
						price = CD * count;
						break;
					case 6:
						price = AB * count;
						break;
					}
				}
			}

			checkBigHotSellItem(BigHotItem, count, price);
		}
	}

	private void checkBigHotSellItem(final L1ItemInstance BigHotItem, final int count, final int amount) {
		if (BigHotItem == null) {
			return;
		}

		_pc.getInventory().removeItem(BigHotItem, count);

		final L1PcInventory inv = _pc.getInventory();

		L1ItemInstance item = null;

		item = ItemTable.get().createItem(L1Config._2167);

		item.setCount(amount);

		inv.storeItem(item);

		Bingo("IP(" + _pc.getNetConnection().getIp() + ")" + "玩家【 " + _pc.getName() + " 】 " + "中了第【 "
				+ BigHotItem.getGamNo() + " 】 場，彩票號碼：【 " + BigHotItem.getStarNpcId() + " 】，" + "取得彩金：【 " + amount
				+ " 】，" + "時間：(" + new Timestamp(System.currentTimeMillis()) + ")。");
	}

	public void checkBigHotSell() {
		clear();
	}

	public static void Bingo(final String info) {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter("record/yuanbao/大樂透中獎紀錄.txt", true));
			out.write(info + "\r\n");
			out.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
