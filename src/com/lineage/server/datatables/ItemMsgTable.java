package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

public class ItemMsgTable {
	private static final Log _log = LogFactory.getLog(ItemMsgTable.class);

	private static final ArrayList<Integer> _idList = new ArrayList();
	private static ItemMsgTable _instance;

	public static ItemMsgTable get() {
		if (_instance == null) {
			_instance = new ItemMsgTable();
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
			pstm = con.prepareStatement("SELECT * FROM `server_msg_item_id`");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int item_id = rs.getInt("itemid");
				if (!_idList.contains(Integer.valueOf(item_id)))
					_idList.add(Integer.valueOf(item_id));
			}
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入打寶公告物編號數量: " + _idList.size() + "(" + timer.get() + "ms)");
	}

	public boolean contains(int item_id) {
		return _idList.contains(Integer.valueOf(item_id));
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.ItemMsgTable JD-Core Version: 0.6.2
 */