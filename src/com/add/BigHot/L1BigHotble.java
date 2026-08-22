package com.add.BigHot;

import java.util.ArrayList;

import com.add.L1Config;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NoSell;
import com.lineage.server.serverpackets.S_SystemMessage;

public class L1BigHotble {

	private static BigHotblingTimeList _BigHot = BigHotblingTimeList.BigHot();;

	private static L1BigHotble instance;

	public static L1BigHotble getInstance() {
		if (instance == null) {
			instance = new L1BigHotble();
		}
		return instance;
	}

	public void selltickets(final L1NpcInstance npc, final L1PcInstance pc) {
		final ArrayList<L1ItemInstance> list = sellList(pc);
		if (list.size() <= 0) {
			pc.sendPackets(new S_NoSell(npc));
		} else {
			pc.getBigHotSplist().set_copySellBigHot(list);

			pc.sendPackets(new S_ShopBuyListBigHot(npc.getId(), list));
		}
	}

	public void LookMoney(final L1PcInstance pc) {
		int price = 0;

		final int yuanbao = _BigHot.get_yuanbao();
		if (yuanbao == 0) {
			price += L1Config._2164;
		} else {
			price += _BigHot.get_yuanbao();
		}

		final L1BigHotbling BigHotInfo = BigHotblingLock.create().getBigHotbling(_BigHot.get_BigHotId() - 1);
		if (BigHotInfo != null) {
			if (BigHotInfo.get_count() == 0) {
				if (BigHotInfo.get_money1() < L1Config._2166) {
					price += BigHotInfo.get_money1();
				} else {
					price += L1Config._2165;
				}
			} else {
				price += L1Config._2165;
			}
			if (BigHotInfo.get_count1() == 0) {
				price += BigHotInfo.get_money2();
			}
			if (BigHotInfo.get_count2() == 0) {
				price += BigHotInfo.get_money3();
			}
		} else {
			price += L1Config._2165;
		}
		final int money = price * 7 / 10;
		pc.sendPackets(new S_SystemMessage("目前頭獎累積的彩金為(" + money + ")。"));
	}

	public void LookMoney1(final L1PcInstance pc) {
		int price = 0;

		final int yuanbao = _BigHot.get_yuanbao();
		if (yuanbao == 0) {
			price += L1Config._2164;
		} else {
			price += _BigHot.get_yuanbao();
		}

		final L1BigHotbling BigHotInfo = BigHotblingLock.create().getBigHotbling(_BigHot.get_BigHotId() - 1);
		if (BigHotInfo != null) {
			if (BigHotInfo.get_count() == 0) {
				if (BigHotInfo.get_money1() < L1Config._2166) {
					price += BigHotInfo.get_money1();
				} else {
					price += L1Config._2165;
				}
			} else {
				price += L1Config._2165;
			}
			if (BigHotInfo.get_count1() == 0) {
				price += BigHotInfo.get_money2();
			}
			if (BigHotInfo.get_count2() == 0) {
				price += BigHotInfo.get_money3();
			}
		} else {
			price += L1Config._2165;
		}
		final int money = price * 2 / 10;
		pc.sendPackets(new S_SystemMessage("目前壹獎累積的彩金為(" + money + ")。"));
	}

	public void LookMoney2(final L1PcInstance pc) {
		int price = 0;

		final int yuanbao = _BigHot.get_yuanbao();
		if (yuanbao == 0) {
			price += L1Config._2164;
		} else {
			price += _BigHot.get_yuanbao();
		}

		final L1BigHotbling BigHotInfo = BigHotblingLock.create().getBigHotbling(_BigHot.get_BigHotId() - 1);
		if (BigHotInfo != null) {
			if (BigHotInfo.get_count() == 0) {
				if (BigHotInfo.get_money1() < L1Config._2166) {
					price += BigHotInfo.get_money1();
				} else {
					price += L1Config._2165;
				}
			} else {
				price += L1Config._2165;
			}
			if (BigHotInfo.get_count1() == 0) {
				price += BigHotInfo.get_money2();
			}
			if (BigHotInfo.get_count2() == 0) {
				price += BigHotInfo.get_money3();
			}
		} else {
			price += L1Config._2165;
		}
		final int money = price / 10;
		pc.sendPackets(new S_SystemMessage("目前貳獎累積的彩金為(" + money + ")。"));
	}

	private static ArrayList<L1ItemInstance> sellList(final L1PcInstance pc) {
		final ArrayList<L1ItemInstance> BigHots = new ArrayList<L1ItemInstance>();

		final L1ItemInstance[] BigHotItems = pc.getInventory().findBigHot();
		if (BigHotItems.length <= 0) {
			return BigHots;
		}

		for (final L1ItemInstance gItem : BigHotItems) {
			final int BigHotId = gItem.getGamNo();
			final L1BigHotbling BigHotInfo = BigHotblingLock.create().getBigHotbling(BigHotId);
			if (BigHotInfo != null) {
				final String A = BigHotInfo.get_number();
				final String B = gItem.getStarNpcId();

				int ch = 0;
				for (int a = 0; a < A.split(",").length; a++) {
					final String[] pk = B.split(",");
					if (("," + A).indexOf("," + pk[a] + ",") >= 0)
						ch++;
				}
				if (ch >= 3) {
					BigHots.add(gItem);
				}
			}
		}

		return BigHots;
	}
}
