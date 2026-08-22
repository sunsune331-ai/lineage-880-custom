package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.SQLUtil;

/**
 * DB化config設置
 */
public class ConfigTable {

	private static final Log _log = LogFactory.getLog(ConfigTable.class);

	private final ConcurrentHashMap<String, String> paramList = new ConcurrentHashMap<>();

	public String getProperty(final String parameter, final String defaultValue) {
		if (paramList.containsKey(parameter)) {
			return paramList.get(parameter);
		}
		// System.out.println(parameter + " has no setting in DB !! Use defaultValue= " + defaultValue);
		System.out.println(parameter + " 在DB中沒有設置 !! 使用默認值= " + defaultValue);
		return defaultValue;
	}

	private static ConfigTable _instance;

	public static ConfigTable getInstance() {
		if (_instance == null) {
			_instance = new ConfigTable();
		}
		return _instance;
	}

	private ConfigTable() {
		load("z_config_new");
	}

	private void load(final String tableName) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM " + tableName);

			rs = pstm.executeQuery();
			while (rs.next()) {

				final String parameter = rs.getString("parameter");
				final String value = rs.getString("value");

				if (paramList.containsKey(parameter)) {
					// System.out.println("[Errer]" + tableName + " has repeated parameter= " + parameter);
					System.out.println("[錯誤]" + tableName + " 有重複的參數= " + parameter);
					continue;
				}
				paramList.put(parameter, value);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
}
