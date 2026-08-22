package com.lineage.server.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lineage.DatabaseFactory;

/**
 * 記錄
 */
public class L1QueryUtil {

	private static void setupPrepareStatement(final PreparedStatement pstm, final Object[] args) throws SQLException {
		for (int i = 0; i < args.length; i++) {
			pstm.setObject(i + 1, args[i]);
		}
	}

	public static <T> T selectFirst(final EntityFactory<T> factory, final String sql, final Object... args) {
		final List<T> result = selectAll(factory, sql, args);
		return result.isEmpty() ? null : result.get(0);
	}

	public static <T> List<T> selectAll(final EntityFactory<T> factory, final String sql, final Object... args) {
		final ArrayList<T> result = new ArrayList<T>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement(sql);
			setupPrepareStatement(pstm, args);
			rs = pstm.executeQuery();
			while (rs.next()) {
				final T entity = factory.fromResultSet(rs);
				if (entity == null) {
					throw new NullPointerException(factory.getClass().getSimpleName() + " returned null.");
				}
				result.add(entity);
			}

		} catch (final SQLException e) {
			throw new SecurityException(e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public static boolean execute(final Connection con, final String sql, final Object... args) {
		PreparedStatement pstm = null;
		try {
			pstm = con.prepareStatement(sql);
			setupPrepareStatement(pstm, args);
			return pstm.execute();

		} catch (final SQLException e) {
			throw new SecurityException(e);

		} finally {
			SQLUtil.close(pstm);
		}
	}

	public static boolean execute(final String sql, final Object... args) {
		Connection con = null;
		try {
			con = DatabaseFactory.get().getConnection();
			return execute(con, sql, args);

		} catch (final SQLException e) {
			throw new SecurityException(e);

		} finally {
			SQLUtil.close(con);
		}
	}

	public static abstract interface EntityFactory<T> {
		public abstract T fromResultSet(ResultSet paramResultSet) throws SQLException;
	}
}
