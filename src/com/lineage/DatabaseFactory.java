package com.lineage;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigSQL;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DatabaseFactory {
	private static final Log _log = LogFactory.getLog(DatabaseFactory.class);
	private static DatabaseFactory _instance;
	private ComboPooledDataSource _source;
	private static String _driver;
	private static String _url;
	private static String _user;
	private static String _password;

	public static void setDatabaseSettings() {
		_driver = ConfigSQL.DB_DRIVER;
		_url = ConfigSQL.DB_URL1 + ConfigSQL.DB_URL2 + ConfigSQL.DB_URL3;
		_user = ConfigSQL.DB_LOGIN;
		_password = ConfigSQL.DB_PASSWORD;
	}

	public DatabaseFactory() throws SQLException {
		try {
			_source = new ComboPooledDataSource();
			_source.setDriverClass(_driver);
			_source.setJdbcUrl(_url);
			_source.setUser(_user);
			_source.setPassword(_password);

			_source.getConnection().close();
		} catch (SQLException e) {
			_log.fatal("資料庫讀取錯誤!", e);
		} catch (Exception e) {
			_log.fatal("資料庫讀取錯誤!", e);
		}
	}

	public void shutdown() {
		try {
			_source.close();
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		try {
			_source = null;
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public static DatabaseFactory get() throws SQLException {
		if (_instance == null) {
			_instance = new DatabaseFactory();
		}
		return _instance;
	}

	public Connection getConnection() {
		Connection con = null;

		while (con == null) {
			try {
				con = _source.getConnection();
			} catch (SQLException e) {
				_log.error(e.getLocalizedMessage(), e);
			}
		}
		return con;
	}
}
