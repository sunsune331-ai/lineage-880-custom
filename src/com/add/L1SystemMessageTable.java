package com.add;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.utils.collections.Maps;

public class L1SystemMessageTable {

	private static final Log _log = LogFactory.getLog(L1SystemMessageTable.class);

	private static L1SystemMessageTable _instance;

	private final Map<Integer, L1SystemMessage> _ConfigIndex = Maps.newHashMap();

	public static L1SystemMessageTable get() {
		if (_instance == null) {
			_instance = new L1SystemMessageTable();
		}
		return _instance;
	}

	public void loadSystemMessage() {
		final PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM system_message");
			rs = pstm.executeQuery();
			fillSystemMessage(rs);

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入DB化系統設定檔資料數量: " + _ConfigIndex.size() + "(" + timer.get() + "ms)");
	}

	private void fillSystemMessage(final ResultSet rs) throws SQLException {
		while (rs.next()) {
			final int Id = rs.getInt("id");
			final String Message = rs.getString("message");
			final Timestamp time = rs.getTimestamp("resetMaptime");

			Calendar resetmaptime = null;
			if (time != null) {
				resetmaptime = timestampToCalendar(rs.getTimestamp("resetMaptime"));
			}

			final L1SystemMessage System_Message = new L1SystemMessage(Id, Message, resetmaptime);

			_ConfigIndex.put(Id, System_Message);

		}
	}

	public L1SystemMessage getTemplate(final int Id) {
		return _ConfigIndex.get(Id);
	}

	private Calendar timestampToCalendar(final Timestamp ts) {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(ts.getTime());
		return cal;
	}

}
