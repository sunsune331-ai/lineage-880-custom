package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.config.ConfigRecord;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.SQLUtil;

/**
 * 神熾贊助送禮系統
 * 贊助滿額獎勵
 */
public class ServerSponsorItemTable {

	private static Logger _log = Logger.getLogger(ServerSponsorItemTable.class.getName());

	private static final Log _logx = LogFactory.getLog(ServerSponsorItemTable.class);

	private static ServerSponsorItemTable _instance;

	private static final Map<Integer, Gift> _list = new HashMap<Integer, Gift>();

	private final ArrayList<String> _listAcc = new ArrayList<String>();

	public static ServerSponsorItemTable get() {
		if (_instance == null) {
			_instance = new ServerSponsorItemTable();
		}
		return _instance;
	}

	private ServerSponsorItemTable() {
		load();
		loadAcc();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM 贊助_滿額禮");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final int id = rs.getInt("id");
				final int min = rs.getInt("min");
				final int max = rs.getInt("max");
				final int[] item_id = getArrayInt(rs.getString("item_id"));
				final int[] count = getArrayInt(rs.getString("count"));
				final boolean only = rs.getBoolean("only");

				final Gift g = new Gift();
				g._min = min;
				g._max = max;
				g._itemid = item_id;
				g._count = count;
				g._only = only;

				_list.put(id, g);
			}

		} catch (final SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_logx.info("贊助滿額獎勵->" + _list.size());
	}

	private void loadAcc() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM 贊助_滿額禮_紀錄");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final String accName = rs.getString("帳號"); // 玩家

				if (!_listAcc.contains(accName.toLowerCase())) {
					_listAcc.add(accName.toLowerCase());
				}
			}

		} catch (final SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_logx.info("贊助首儲紀錄->" + _listAcc.size());
	}

	private static int[] getArrayInt(final String s) {
		if (s == null || s.isEmpty() || s.equals("")) {
			return null;
		}
		final StringTokenizer st = new StringTokenizer(s, ",");
		final int iSize = st.countTokens();
		String sTemp = null;

		final int[] iReturn = new int[iSize];
		for (int i = 0; i < iSize; i++) {
			sTemp = st.nextToken();
			iReturn[i] = Integer.parseInt(sTemp);
		}
		return iReturn;
	}

	public void checkItem(final L1PcInstance pc, final int count) {
		if (_list.isEmpty()) {
			return;
		}

		for (final Integer key : _list.keySet()) {
			final Gift g = _list.get(key);
			if (count >= g._min && count <= g._max) {
				if (g._only) {
					if (!_listAcc.contains(pc.getAccountName().toLowerCase())) {
						final StringBuilder name = new StringBuilder();
						//name.append("\\aL獲得贊助首儲禮:\n");
						name.append("\\aC獲得首儲滿" + count + "好禮:");
						for (int i = 0; i < g._itemid.length; i++) {
							final L1ItemInstance item = ItemTable.get().createItem(g._itemid[i]);
							if (item != null) {
								if (g._count[i] != 1) {
									item.setCount(g._count[i]);
								}

								if (i == g._itemid.length - 1) {
									name.append("\\aC" + item.getLogName());
								} else {
									name.append("\\aC" + item.getLogName() + "\n");
								}
								pc.getInventory().storeItem(item);
							}
						}
						pc.sendPackets(new S_ServerMessage(name.toString()));
						// 加入首儲紀錄
						RecordTable.get().recordeSponsorItem(pc.getAccountName(), pc.getName(), key, pc.getIp());
						_listAcc.add(pc.getAccountName().toLowerCase());
					}

				} else if (!g._only) {
					final StringBuilder name = new StringBuilder();
					//name.append("\\aL獲得贊助滿額禮:\n");
					name.append("\\aL獲得滿額滿" + count + "好禮:");
					for (int i = 0; i < g._itemid.length; i++) {
						final L1ItemInstance item = ItemTable.get().createItem(g._itemid[i]);
						if (item != null) {
							if (g._count[i] != 1) {
								item.setCount(g._count[i]);
							}

							if (i == g._itemid.length - 1) {
								name.append("\\aL" + item.getLogName());
							} else {
								name.append("\\aL" + item.getLogName() + "\n");
							}
							pc.getInventory().storeItem(item);
						}
					}
					pc.sendPackets(new S_ServerMessage(name.toString()));
					// 滿額記錄
					Timestamp timestamp = new Timestamp(System.currentTimeMillis());
					ConfigRecord.recordToFiles("贊助滿額領取紀錄",
							"IP(" + pc.getNetConnection().getIp() + ")帳號:【" + pc.getAccountName().toLowerCase()
									+ "】玩家:【" + pc.getName() + "】【" + name.toString() + "】 時間:(" + timestamp + ")",
							timestamp);
				}
			}
		}
	}

	private class Gift {
		private int _min;
		private int _max;
		private int[] _itemid;
		private int[] _count;
		private boolean _only;
	}
}
