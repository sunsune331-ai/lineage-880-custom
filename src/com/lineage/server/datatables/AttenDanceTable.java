package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.SQLUtil;

/**
 * 官方簽到系統
 */
public class AttenDanceTable {
	private static Logger _log = Logger.getLogger(AttenDanceTable.class.getName());
	private static AttenDanceTable _instance;

	public static AttenDanceTable getInstance() {
		if (_instance == null) {
			_instance = new AttenDanceTable();
		}
		return _instance;
	}

	public static void reload() {
		AttenDanceTable oldInstance = _instance;
		_instance = new AttenDanceTable();
		oldInstance.NormalList.clear();
		oldInstance.PcRoomList.clear();
	}

	private ArrayList<Attendtemp> NormalList = new ArrayList<Attendtemp>();
	private ArrayList<Attendtemp> PcRoomList = new ArrayList<Attendtemp>();

	private AttenDanceTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM attendance");
			rs = pstm.executeQuery();
			while (rs.next()) {
				Attendtemp temp = new Attendtemp();
				temp.id = rs.getInt("id");
				temp.item_id = rs.getInt("item_id");
				temp.item_count = rs.getInt("item_count");
				if (temp.id > 50) {
					temp.id -= 50;
					PcRoomList.add(temp);
				} else {
					NormalList.add(temp);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public ArrayList<Attendtemp> getNormalList() {
		return NormalList;
	}

	public int getNormalSize() {
		return NormalList.size();
	}

	public ArrayList<Attendtemp> getPcRoomList() {
		return PcRoomList;
	}

	public int getPcRoomSize() {
		return PcRoomList.size();
	}

	public class Attendtemp {
		public int id;
		public int item_id;
		public int item_count;
	}
}