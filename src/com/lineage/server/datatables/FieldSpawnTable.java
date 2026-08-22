package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 動作佈景召喚系統
 */
public class FieldSpawnTable {

	private static final Log _log = LogFactory.getLog(FieldSpawnTable.class);

	private static FieldSpawnTable _instance;

	public static FieldSpawnTable getInstance() {
		if (_instance == null) {
			_instance = new FieldSpawnTable();
		}
		return _instance;
	}

	private final ArrayList<String> checkList = new ArrayList<>();

	private FieldSpawnTable() {
		PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_field");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final int id = rs.getInt("id");
				final int gfxid = rs.getInt("gfxid");
				final String nameid = rs.getString("nameid");
				final int x = rs.getInt("locx");
				final int y = rs.getInt("locy");
				final int mapid = rs.getInt("mapid");
				final int action = rs.getInt("action_type");

				// 避免重複
				if (checkList.contains(x + "," + y + "," + mapid + "," + gfxid)) {
					System.out.println("FieldSpawnTable id: " + id + " is repeat");
					continue;
				}
				checkList.add(x + "," + y + "," + mapid + "," + gfxid);

				final L1Npc l1npc = NpcTable.get().getTemplate(190000);
				final L1FieldObjectInstance field = new L1FieldObjectInstance(l1npc);
				field.setId(IdFactoryNpc.get().nextId());

				field.setGfxId(gfxid);
				field.setTempCharGfx(gfxid);

				// if (gfxid >= 12901 && gfxid <= 12905) {
				// field.setNameId("$19274"); // 修練者
				// } else if (gfxid == 14348 || gfxid == 14350) {
				// field.setTitle("$22945"); // 武器防具商城
				// } else if (gfxid == 14352 || gfxid == 14354) {
				// field.setTitle("$22944"); // 雜貨商城
				// }

				if (nameid != null && !nameid.isEmpty()) {
					field.setNameId(nameid);
				}

				field.setX(x);
				field.setY(y);
				field.setHomeX(x);
				field.setHomeY(y);
				field.setMap((short) mapid);
				field.setHeading(rs.getInt("heading"));
				field.setChaLightSize(rs.getInt("light"));
				if (action == 28 || gfxid == 1026) {
					field.setActionType(-1);
					field.setStatus(action);
				} else {
					field.setActionType(action);
				}
				field.setActionTime(rs.getInt("action_time"));

				World.get().storeObject(field);
				World.get().addVisibleObject(field);
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入動作佈景召喚系統設置數量: " + checkList.size() + "(" + timer.get() + "ms)");
	}
}
