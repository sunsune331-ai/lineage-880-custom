package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1Clan;
import com.lineage.server.utils.SQLUtil;

/**
 * 血盟祝福系統
 * @author Admin
 */
public final class ClanBuffTable {
	public class ClanBuff {
		public int id = 0;
		public int buffnumber = 0;
		public String mapname = null;
		public int teleportX = 0;
		public int teleportY = 0;
		public int teleportM = 0;
		public String buffmaplist = null;
	}

	private static ClanBuffTable _instance;

	private static Logger _log = Logger.getLogger(ClanBuffTable.class.getName());

	private static final HashMap<Integer, ClanBuff> _ClanBuff = new HashMap<Integer, ClanBuff>();

	public static ClanBuffTable getInstance() {
		if (_instance == null) {
			_instance = new ClanBuffTable();
		}
		return _instance;
	}

	private ClanBuffTable() {
		loadMapsFromDatabase();
	}

	public static void reload() {
		_instance = new ClanBuffTable();
		_ClanBuff.clear();
	}

	private void loadMapsFromDatabase() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_bless_buff");
			ClanBuff data = null;
			for (rs = pstm.executeQuery(); rs.next();) {
				data = new ClanBuff();
				final int id = rs.getInt("number");
				data.buffnumber = rs.getInt("buff_id");
				data.mapname = rs.getString("map_name");
				data.teleportX = rs.getInt("teleport_x");
				data.teleportY = rs.getInt("teleport_y");
				data.teleportM = rs.getInt("teleport_map_id");
				data.buffmaplist = rs.getString("buff_map_list");
				_ClanBuff.put(new Integer(id), data);
			}
			_log.config("Maps_Item " + _ClanBuff.size());

		} catch (final SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int getBuff(final int number) {
		final ClanBuff Buff = _ClanBuff.get(number);
		if (Buff == null) {
			return 0;
		}
		return Buff.buffnumber;
	}

	public static ClanBuff getBuffList(final int number) {
		for (int i = 1; i < _ClanBuff.size() + 1; i++) {
			final ClanBuff Buff = _ClanBuff.get(i);
			if (Buff.buffnumber == number) {
				return Buff;
			}
		}
		return null;
	}

	private static Random _random = new Random(System.nanoTime());

	public static int getRandomBuff(final L1Clan clan) {
		int Buff = 0;
		while (true) {
			ClanBuff ClanBuff = _ClanBuff.get(_random.nextInt(_ClanBuff.size()) + 1);
			if (ClanBuff.buffnumber != clan.getBuffFirst()
					&& ClanBuff.buffnumber != clan.getBuffSecond()
					&& ClanBuff.buffnumber != clan.getBuffThird()
			) {
				Buff = ClanBuff.buffnumber;
				break;
			}
		}
		return Buff;
	}

	public int getBuffSize() {
		return _ClanBuff.size();
	}
}
