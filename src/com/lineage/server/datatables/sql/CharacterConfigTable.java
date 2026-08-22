package com.lineage.server.datatables.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharacterConfigStorage;
import com.lineage.server.templates.L1Config;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

public class CharacterConfigTable implements CharacterConfigStorage {
	private static final Log _log = LogFactory.getLog(CharacterConfigTable.class);

	private static final Map<Integer, L1Config> _configList = new HashMap();

	@Override
	public void load() {
		PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `character_config`");
			rs = pstm.executeQuery();
			L1Config l1Config;
			while (rs.next()) {
				int objid = rs.getInt("object_id");

				if (CharObjidTable.get().isChar(objid) != null) {
					l1Config = new L1Config();
					l1Config.setObjid(objid);
					l1Config.setLength(rs.getInt("length"));
					l1Config.setData(rs.getBytes("data"));

					_configList.put(Integer.valueOf(objid), l1Config);
					

				} else {
					delete(objid);
				}
			}
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入人物快速鍵紀錄資料數量: " + _configList.size() + "(" + timer.get() + "ms)");
	}

	private static void delete(int objid) {
		Connection cn = null;
		PreparedStatement ps = null;
		try {
			cn = DatabaseFactory.get().getConnection();
			ps = cn.prepareStatement("DELETE FROM `character_config` WHERE `object_id`=?");
			ps.setInt(1, objid);
			ps.execute();
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(ps);
			SQLUtil.close(cn);
		}
	}

	@Override
	public L1Config get(int objectId) {
		
		return (L1Config) _configList.get(Integer.valueOf(objectId));
	}

	@Override
	public void storeCharacterConfig(int objectId, int length, byte[] data) {
		L1Config config = new L1Config();

		config.setObjid(objectId);
		config.setLength(length);
		config.setData(data);

		_configList.put(Integer.valueOf(objectId), config);

		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("INSERT INTO `character_config` SET `object_id`=?,`length`=?,`data`=?");

			int i = 0;
			pstm.setInt(++i, config.getObjid());
			pstm.setInt(++i, config.getLength());
			pstm.setBytes(++i, config.getData());
			pstm.execute();
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public void updateCharacterConfig(int objectId, int length, byte[] data) {
		L1Config config = (L1Config) _configList.get(Integer.valueOf(objectId));

		config.setObjid(objectId);
		config.setLength(length);
		config.setData(data);

		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("UPDATE `character_config` SET `length`=?,`data`=? WHERE `object_id`=?");

			int i = 0;
			pstm.setInt(++i, config.getLength());
			pstm.setBytes(++i, config.getData());
			pstm.setInt(++i, config.getObjid());
			pstm.execute();
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.sql.CharacterConfigTable JD-Core Version: 0.6.2
 */