package com.lineage.server.datatables;

import static com.lineage.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.SQLUtil;

/**
 * 地圖回血回魔系統
 */
public class MapHprMprTable {

	private static final Log _log = LogFactory.getLog(MapHprMprTable.class);

	private static MapHprMprTable _instance;

	private static final Map<Integer, MapHprMpr> _maphprmprList = new HashMap<Integer, MapHprMpr>();

	public static MapHprMprTable get() {
		if (_instance == null) {
			_instance = new MapHprMprTable();
		}
		return _instance;
	}

	private MapHprMprTable() {
		load();
	}

	public void load() {
		Connection co = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			co = DatabaseFactory.get().getConnection();
			ps = co.prepareStatement("SELECT * FROM `mapids_hprmpr`");
			rs = ps.executeQuery();

			while (rs.next()) {
				final int mapid = rs.getInt("mapid");
				final int hpr = rs.getInt("hpr");
				final int mpr = rs.getInt("mpr");

				final MapHprMpr map = new MapHprMpr();
				map._hpr = hpr;
				map._mpr = mpr;

				_maphprmprList.put(new Integer(mapid), map);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(ps);
			SQLUtil.close(co);
		}
		_log.info("地圖回血回魔->" + _maphprmprList.size());
	}

	/**
	 * 在水中區域HP減少判斷
	 */
	public int getMapHpr(final L1PcInstance pc, final int mapid) {
		if (_maphprmprList.containsKey(mapid)) {
			// 在水中
			/*if (pc.getMap().isUnderwater()) {
				if (pc.getInventory().checkEquipped(20207)) { // 深水長靴
					return 0;
				}
				if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) { // 伊娃的祝福藥水效果
					return 0;
				}
				if (pc.getInventory().checkEquipped(21048) // 修好的戒指
						&& pc.getInventory().checkEquipped(21049) // 修好的耳環
						&& pc.getInventory().checkEquipped(21050) // 修好的項鏈
				) {
					return 0;
				}
			}*/
			return _maphprmprList.get(mapid)._hpr;
		}
		return 0;
	}

	public int getMapMpr(final int mapid) {
		if (_maphprmprList.containsKey(mapid)) {
			return _maphprmprList.get(mapid)._mpr;
		}
		return 0;
	}

	private class MapHprMpr {
		private int _hpr;
		private int _mpr;
	}
}
