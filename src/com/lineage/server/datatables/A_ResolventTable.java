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

public final class A_ResolventTable {

	private static final Log _log = LogFactory.getLog(A_ResolventTable.class);

	private static A_ResolventTable _instance;

	private static final Map<Integer, Integer> _resolvent = new HashMap<Integer, Integer>();

	public static A_ResolventTable get() {
		if (_instance == null) {
			_instance = new A_ResolventTable();
		}
		return _instance;
	}
	

	public void load() {
		final PerformanceTimer timer = new PerformanceTimer();
		Connection cn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cn = DatabaseFactory.get().getConnection();
			ps = cn.prepareStatement("SELECT * FROM z_aresolvent");

			for (rs = ps.executeQuery(); rs.next();) {
				final int itemId = rs.getInt("item_id");
				final int crystalCount = rs.getInt("crystal_count");
				

				_resolvent.put(new Integer(itemId), crystalCount);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
			
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(ps);
			SQLUtil.close(cn);
		}
		_log.info("載入提煉物品設置資料數量: " + _resolvent.size() + "(" + timer.get()
				+ "ms)");
	}

	public int getCrystalCount(final int itemId) {
		int crystalCount = 0;
		if (_resolvent.containsKey(itemId)) {
			crystalCount = _resolvent.get(itemId);
		}
		return crystalCount;
	}

}
