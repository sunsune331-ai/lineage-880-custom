package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1AttrWeapon;
import com.lineage.server.templates.L1CriticalHitStone;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

public final class ExtraCriticalHitStoneTable {

	private static final Log _log = LogFactory
			.getLog(ExtraCriticalHitStoneTable.class);

	private static final Map<Integer, L1CriticalHitStone> _stoneList = new HashMap<Integer, L1CriticalHitStone>();

	private static ExtraCriticalHitStoneTable _instance;

	public static ExtraCriticalHitStoneTable getInstance() {
		if (_instance == null) {
			_instance = new ExtraCriticalHitStoneTable();
		}
		return _instance;
	}

	public final void load() {
		final PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM extra_critical_hit_stone");
			rs = pstm.executeQuery();

			while (rs.next()) {
				final int item_id = rs.getInt("item_id");
				final int next_item_id = rs.getInt("next_item_id");
				final String name = rs.getString("name");

				final int chance = rs.getInt("chance");
				final int critical_hit_chance = rs
						.getInt("critical_hit_chance");
				final int critical_hit_damage = rs
						.getInt("critical_hit_damage");
				final int gfxId = rs.getInt("gfxId");
				final boolean gfxIdTarget = rs.getBoolean("gfxIdTarget");
				final boolean arrowType = rs.getBoolean("arrowType");

				final L1CriticalHitStone stone = new L1CriticalHitStone(
						item_id, next_item_id, name, chance,
						critical_hit_chance, critical_hit_damage, gfxId,
						gfxIdTarget, arrowType);

				_stoneList.put(item_id, stone);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入暴擊寶石鑲嵌資料數量: " + _stoneList.size() + "(" + timer.get()
				+ "ms)");
	}

	public final L1CriticalHitStone get(final int itemId) {
		return _stoneList.get(itemId);
	}
}
