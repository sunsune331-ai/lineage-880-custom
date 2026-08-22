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
import com.lineage.server.templates.L1MemoryBook;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 記憶書<br>
 * 
 * 道具nameid=$5839(對話檔telbook0.tbl)<br>
 * 道具nameid=$6415(對話檔telbook1.tbl)<br>
 * 道具nameid=$8450(對話檔telbook2.tbl)<br>
 * 道具nameid=$15994(對話檔telbook2.tbl)<br>
 */
public class MemoryBookTable {

	private static final Log _log = LogFactory.getLog(MemoryBookTable.class);

	private static MemoryBookTable _instance;

	private final Map<Integer, L1MemoryBook> list = new HashMap<Integer, L1MemoryBook>();

	public static MemoryBookTable get() {
		if (_instance == null) {
			_instance = new MemoryBookTable();
		}
		return _instance;
	}

	public void load() {
		final PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM extra_memory_book");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final L1MemoryBook data = new L1MemoryBook();
				data.setId(rs.getInt("id"));
				data.setType(rs.getInt("type"));
				data.setBookId(rs.getInt("bookid"));
				data.setLocx(rs.getInt("locx"));
				data.setLocy(rs.getInt("locy"));
				data.setMapid(rs.getInt("mapid"));
				list.put(data.getId(), data);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入記憶書設置數量: " + list.size() + "(" + timer.get() + "ms)");
	}

	public Map<Integer, L1MemoryBook> getAllHuntMemoryBook() {
		return list;
	}

	// public L1MemoryBook getHuntMemoryBook(final int id) {
	// return list.get(id);
	// }
}
