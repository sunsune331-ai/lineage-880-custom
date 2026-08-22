package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1SystemMessage;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SystemMessageTable {
	
	private static final Log _log = LogFactory.getLog(SystemMessageTable.class);
	   
	private static SystemMessageTable _instance;
	   
	private final HashMap<Integer, L1SystemMessage> _itemIdIndex = new HashMap<Integer, L1SystemMessage>();
	   
	public static SystemMessageTable getInstance() {
		if (_instance == null) {
			_instance = new SystemMessageTable();
		}
        return _instance;
    }

	private SystemMessageTable() {
		loadSystemMessage();
    }

	public static void reload() {
		SystemMessageTable oldInstance = _instance;
		_instance = new SystemMessageTable();
		oldInstance._itemIdIndex.clear();
	}

	private void loadSystemMessage() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM william_system_message");
			rs = pstm.executeQuery();
			fillSystemMessage(rs);
		} catch (SQLException e) {
			_log.error("error while creating william_system_message table", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void fillSystemMessage(ResultSet rs) throws SQLException {
		PerformanceTimer timer = new PerformanceTimer();
		while (rs.next()) {
			int Id = rs.getInt("id");
			String Message = rs.getString("message");

			L1SystemMessage System_Message = new L1SystemMessage(Id, Message);
			this._itemIdIndex.put(Integer.valueOf(Id), System_Message);
		}
		_log.info("載入額外訊息數據資料數量: " + this._itemIdIndex.size() + "(" + timer.get() + "ms)");
	}

	public L1SystemMessage getTemplate(int Id) {
		return (L1SystemMessage)this._itemIdIndex.get(Integer.valueOf(Id));
	}
	
}
