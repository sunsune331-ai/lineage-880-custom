package com.lineage.data.npc.shop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_GmMessage;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

public class NPC_Ezpay2 extends NpcExecutor {
	private static final Log _log = LogFactory.getLog(NPC_Ezpay2.class);

	public static NpcExecutor get() {
		return new NPC_Ezpay2();
	}

	public int type() {
		return 3;
	}

	public void talk(L1PcInstance pc, L1NpcInstance npc) {
		try {
			pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_s_0"));
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {

		if (cmd.equalsIgnoreCase("1")) {
			try {
				getSponsor(pc);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (cmd.equalsIgnoreCase("2")) {

			checkSponsor(pc);

		} /*
			 * else if (cmd.equalsIgnoreCase("3")){
			 * 
			 * try { getExpDoll(pc); } catch (ParseException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); }
			 * 
			 * }
			 */

		pc.sendPackets(new S_CloseList(pc.getId()));

	}

	/**
	 * 領取經驗公主
	 * 
	 * @param pc
	 * @throws ParseException
	 */
	private static synchronized void getExpDoll(L1PcInstance pc) throws ParseException {
		L1ItemInstance item = ItemTable.get().createItem(155008);

		if (item != null) {

			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			PreparedStatement pstm2 = null;
			/**
			 * 
			 * 贊助金額累積滿7萬會升級魔法娃娃：經驗公主(Lv1) 贊助金額累積滿8萬會升級魔法娃娃：經驗公主(Lv2)
			 * 贊助金額累積滿9萬會升級魔法娃娃：經驗公主(Lv3) 贊助金額累積滿10萬會升級魔法娃娃：經驗公主(Lv4)
			 * 贊助金額累積滿11萬會升級魔法娃娃：經驗公主(Lv5) 贊助金額累積滿12萬會升級魔法娃娃：經驗公主(Lv6)
			 * 贊助金額累積滿14萬會升級魔法娃娃：經驗公主(Lv7) 贊助金額累積滿16萬會升級魔法娃娃：經驗公主(Lv8)
			 * 贊助金額累積滿18萬會升級魔法娃娃：經驗公主(Lv9) 贊助金額累積滿20萬會升級魔法娃娃：經驗公主(Lv10) 155008
			 * 255008 355008 455008 555008 655008 755008 855008 955008 1055008
			 * 
			 **/
			int[] list = { 155008, 255008, 355008, 455008, 555008, 655008, 755008, 855008, 955008, 1055008 };
			try {

				String AccountName = pc.getAccountName();
				con = DatabaseFactory.get().getConnection();
				pstm = con.prepareStatement("select sum(ps) as price from ezpay where payname ='" + AccountName + "'");
				rs = pstm.executeQuery();

				int price = 0;
				while (true) {
					if (!rs.next() || rs == null) {
						break;
					}

					price = rs.getInt("price");

				}
				for (int i = 0; i < list.length; i++) {
					pc.getInventory().consumeItem(list[i], 1);
				}
				if (price >= 200000) {
					pc.getInventory().storeItem(1055008, 1);
				} else if (price >= 180000) {
					pc.getInventory().storeItem(955008, 1);
				} else if (price >= 160000) {
					pc.getInventory().storeItem(855008, 1);
				} else if (price >= 140000) {
					pc.getInventory().storeItem(755008, 1);
				} else if (price >= 120000) {
					pc.getInventory().storeItem(655008, 1);
				} else if (price >= 110000) {
					pc.getInventory().storeItem(555008, 1);
				} else if (price >= 100000) {
					pc.getInventory().storeItem(455008, 1);
				} else if (price >= 90000) {
					pc.getInventory().storeItem(355008, 1);
				} else if (price >= 80000) {
					pc.getInventory().storeItem(255008, 1);
				} else if (price >= 70000) {
					pc.getInventory().storeItem(155008, 1);
				}

				pc.sendPackets(new S_GmMessage("目前累積贊助金額: " + price, "\\aE"));

			} catch (SQLException e) {
				_log.error(e.getLocalizedMessage());
				// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(pstm2);
				SQLUtil.close(con);
			}
		} else {
			pc.sendPackets(new S_GmMessage("一服限定商品!", "\\aE"));
		}
	}

	/**
	 * 領取贊助金額
	 * 
	 * @param pc
	 * @throws ParseException
	 */
	private static synchronized void getSponsor(L1PcInstance pc) throws ParseException {
		Connection con = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm1 = null;
		PreparedStatement pstm2 = null;
		PreparedStatement pstm3 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;

		try {
			String AccountName = pc.getAccountName();
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("select * from ezpay where state = 0 and payname ='" + AccountName + "'");
			rs = pstm.executeQuery();

			boolean isfind = false;

			while (true) {
				if (!rs.next() || rs == null) {
					break;
				}

				// final SimpleDateFormat sdf = new
				// SimpleDateFormat("yyyy/MM/dd");
				final SimpleDateFormat sdf_ps = new SimpleDateFormat("yyyy/MM");
				// final TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
				// final String date =
				// sdf.format(Calendar.getInstance(tz).getTime());

				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
				Date insertDay = dateFormat.parse(rs.getString("date"));
				// final String date_ps = sdf_ps.format(insertDay);

				pstm1 = con.prepareStatement("select * from ezpay_pc where account ='" + AccountName + "'");
				rs1 = pstm1.executeQuery();

				String serial = rs.getString("ordernumber");

				int count = rs.getInt("amount");
				// int count_action = rs.getInt("action_count");
				int count_ps = rs.getInt("ps");

				if (pc.getAccountName().equalsIgnoreCase(rs.getString("payname"))) {
					isfind = true;
					pstm2 = con.prepareStatement("update ezpay set state = 1 where ordernumber = ?");
					pstm2.setString(1, serial);
					pstm2.execute();

					if (rs1.next() && rs1 != null) {
						int money = rs1.getInt("amount") + count_ps;
						pstm3 = con.prepareStatement(
								"update ezpay_pc set amount = " + money + " where account = '" + AccountName + "'");
						pstm3.execute();
					} else {
						pstm3 = con.prepareStatement(
								"insert into ezpay_pc (account,amount) values('" + AccountName + "'," + count_ps + ")");
						pstm3.execute();
					}

					GiveItem(pc, 44070, count, 1);

				}
			}

			if (!isfind) {
				pc.sendPackets(new S_GmMessage("沒有您的贊助資料。 ", "\\aE"));
			}
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage());
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {

			SQLUtil.close(rs);
			SQLUtil.close(rs1);
			SQLUtil.close(pstm);
			SQLUtil.close(pstm2);
			SQLUtil.close(pstm3);
			SQLUtil.close(con);
		}
	}

	// 查詢總共累積
	private static synchronized void checkSponsor(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		PreparedStatement pstm2 = null;

		try {

			String AccountName = pc.getAccountName();
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("select sum(ps) as price from ezpay where payname ='" + AccountName + "'");
			rs = pstm.executeQuery();

			int price = 0;
			while (true) {
				if (!rs.next() || rs == null) {
					break;
				}

				price = rs.getInt("price");

			}

			pc.sendPackets(new S_GmMessage("目前累積贊助金額: " + price, "\\aE"));

		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage());
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
	}

	// private static ArrayList<ArrayList<Object>> list = new
	// ArrayList<ArrayList<Object>>();
	// 領累積獎勵
	private static synchronized void checkSponsor2(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		PreparedStatement pstm1 = null;
		ResultSet rs1 = null;
		ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();
		list.clear();
		int price = 0;
		try {

			String AccountName = pc.getAccountName();
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("select sum(ps) as price from ezpay where payname ='" + AccountName + "'");

			rs = pstm.executeQuery();

			if (!rs.next() || rs == null) {
				price = 0;
			} else {
				price = rs.getInt("price");
			}

		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage());
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		try {

			String AccountName = pc.getAccountName();
			con = DatabaseFactory.get().getConnection();

			pstm = con.prepareStatement("SELECT * FROM ezpay_gifts_pc where account='" + AccountName + "'");

			rs = pstm.executeQuery();

			ArrayList<Object> aReturn = null;

			if (rs != null)

				while (rs.next()) {
					aReturn = new ArrayList<Object>();
					aReturn.add(0, rs.getString("account"));
					aReturn.add(1, rs.getInt("questid"));
					// aReturn.add(2, rset.getString("Note"));
					list.add(aReturn);

				}

		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		try {

			String AccountName = pc.getAccountName();
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("select * from ezpay_gifts where amount <= " + price + "");
			rs = pstm.executeQuery();

			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			final TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
			final String date = sdf.format(Calendar.getInstance(tz).getTime());

			while (rs.next()) {
				boolean isGet = false;
				for (ArrayList<Object> objects : list) {
					String account = (String) objects.get(0);
					if (account.equals(AccountName)) {

						int a = rs.getInt("questid");
						int b = (Integer) objects.get(1);
						// System.out.println("a:"+a+"b:"+b);
						if (a == b) {
							isGet = true;
							break;
						}
					}
				}

				if (!isGet) {
					pstm1 = con.prepareStatement("insert into ezpay_gifts_pc (account,questid,date) values('"
							+ AccountName + "'," + rs.getInt("questid") + ",'" + date + "')");
					pstm1.execute();

					GiveItem_gifts(pc, rs.getInt("item_id"), rs.getInt("item_count"), rs.getInt("questid"));

				}

			}

		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage());
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(rs1);
			SQLUtil.close(pstm);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
			list.clear();
		}
	}

	// 領滿額幣
	private static synchronized void checkSponsor3(L1PcInstance pc) throws ParseException {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		int amount = 0;
		boolean isfind = false;

		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");

		final TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		final String date = sdf.format(Calendar.getInstance(tz).getTime());
		System.out.println(date);
		try {
			String AccountName = pc.getAccountName();
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("select * from ezpay_pc where account ='" + AccountName + "'");
			rs = pstm.executeQuery();

			if (rs.next() && rs != null) {

				amount = rs.getInt("amount");
			}

			if (amount - 5000 >= 0) {
				isfind = true;
			}

			if (amount == 0) {
				isfind = false;
			}

		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage());
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		if (!isfind) {
			int a = 5000 - amount;
			pc.sendPackets(new S_GmMessage("還差" + a + "就可以領取滿額幣", "\\aE"));

			return;
		}

		try {

			int money = (amount - 5000);
			String AccountName = pc.getAccountName();
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement(
					"update ezpay_pc set amount = " + money + " where account ='" + AccountName + "'");
			pstm.execute();

			GiveItem(pc, 65318, 1, 2);

		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage());
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void GiveItem(L1PcInstance pc, int itemId, int count, int type) {
		L1ItemInstance item = ItemTable.get().createItem(itemId);
		item.setCount(count);

		if (pc.getInventory().checkAddItem(item, count) == 0) {
			pc.getInventory().storeItem(item);
			pc.sendPackets(new S_GmMessage("領取: " + item.getLogName() + "。", "\\aE"));
			toGmMsg(pc, count, type);

		}
	}

	public static void GiveItem_gifts(L1PcInstance pc, int itemId, int count, int type) {
		L1ItemInstance item = ItemTable.get().createItem(itemId);
		item.setCount(count);

		if (pc.getInventory().checkAddItem(item, count) == 0) {
			pc.getInventory().storeItem(item);
			pc.sendPackets(new S_GmMessage("領取: " + item.getLogName() + "。", "\\aE"));
			toGmMsg(pc, count, type);

		}
	}

	/**
	 * 通知GM
	 */
	private static void toGmMsg(L1PcInstance pc, int adenaCount, int type) {
		try {
			String s = "";
			final Collection<L1PcInstance> allPc = World.get().getAllPlayers();
			if (type == 1) {

				s = "天寶";
			} else if (type == 2) {

				s = "滿額幣";
			} else if (type >= 3) {

				s = "累積禮";
			}
			// LOG紀錄
			writeSponsorlog(pc, adenaCount, s);

			for (L1PcInstance tgpc : allPc) {
				if (tgpc.isGm()) {
					final StringBuilder topc = new StringBuilder();

					topc.append("人物:" + pc.getName() + "領取" + s + ":" + adenaCount);

					tgpc.sendPackets(new S_ServerMessage(166, topc.toString()));
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 領取贊助
	 * 
	 * @param player
	 */
	public static void writeSponsorlog(L1PcInstance player, int count, String s) {
		try {
			File DeleteLog = new File("./log/Sponsor.txt");
			BufferedWriter out;
			if (DeleteLog.createNewFile()) {
				out = new BufferedWriter(new FileWriter("./log/Sponsor.txt", false));
				out.write("※以下是玩家[領取贊助]的所有紀錄※" + "\r\n");
				out.close();
			}
			out = new BufferedWriter(new FileWriter("./log/Sponsor.txt", true));
			out.write("\r\n");// 每次填寫資料都控一行
			out.write("來自帳號: " + player.getAccountName() + "來自ip: " + player.getNetConnection().getIp() + ",來自玩家: "
					+ player.getName() + ",領取了: " + count + " 個" + s + ",<領取時間:"
					+ new Timestamp(System.currentTimeMillis()) + ">" + "\r\n");
			out.close();
		} catch (IOException e) {
			System.out.println("以下是錯誤訊息: " + e.getMessage());
		}
	}

}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.data.npc.shop.NPC_Ezpay JD-Core Version: 0.6.2
 */