package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

public class ShopXTable {
	private static final Log _log = LogFactory.getLog(ShopXTable.class);
	private static ShopXTable _instance;
	private static final Map<Integer, String> _notShopList = new HashMap<Integer, String>();

	public static ShopXTable get() {
		if (_instance == null) {
			_instance = new ShopXTable();
		}
		return _instance;
	}

	public void load() {
		PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `server_shopx`");
			rs = pstm.executeQuery();

			while (rs.next()) {
				int itemid = rs.getInt("itemid");
				if (ItemTable.get().getTemplate(itemid) == null) {
					_log.error("禁止拍賣物品資料錯誤: 沒有這個編號的道具:" + itemid);
					delete(itemid);
				} else {
					String note = rs.getString("note");
					_notShopList.put(new Integer(itemid), note);
				}
			}
			_log.info("載入禁止拍賣物品資料數量: " + _notShopList.size() + "(" + timer.get() + "ms)");
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private static void delete(int item_id) {
		Connection cn = null;
		PreparedStatement ps = null;
		try {
			cn = DatabaseFactory.get().getConnection();
			ps = cn.prepareStatement("DELETE FROM `server_shopx` WHERE `itemid`=?");
			ps.setInt(1, item_id);
			ps.execute();
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(ps);
			SQLUtil.close(cn);
		}
	}

	public String getTemplate(int itemid) {
		return (String) _notShopList.get(new Integer(itemid));
	}

	public Map<Integer, String> getList() {
		return _notShopList;
	}

	public int size() {
		return _notShopList.size();
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.ShopXTable JD-Core Version: 0.6.2
 */