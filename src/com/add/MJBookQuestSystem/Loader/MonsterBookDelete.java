package com.add.MJBookQuestSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.utils.SQLUtil;

/**
 * 用於怪物圖鑒刪除遺失玩家資料
 */
public class MonsterBookDelete {

	private static final Log _log = LogFactory.getLog(MonsterBookDelete.class);

	private static MonsterBookDelete _instance;

	public static MonsterBookDelete getInstance() {
		if (_instance == null) {
			_instance = new MonsterBookDelete();
		}
		return _instance;
	}

	private MonsterBookDelete() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM tb_user_monster_book");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final int objid = rs.getInt("char_id");

				if (CharObjidTable.get().isChar(objid) == null) {
					// 刪除遺失玩家資料
					delete(objid);
					// _log.info(">>>>>>>>刪除tb_user_monster_book遺失玩家資料記錄，objid:" + objid);
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 刪除遺失玩家資料
	 * @param objid
	 */
	private static void delete(final int objid) {
		Connection cn = null;
		PreparedStatement ps = null;
		try {
			cn = DatabaseFactory.get().getConnection();
			ps = cn.prepareStatement("DELETE FROM `tb_user_monster_book` WHERE `char_id`=?");
			ps.setInt(1, objid);
			ps.execute();

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(ps);
			SQLUtil.close(cn);
		}
	}
}
