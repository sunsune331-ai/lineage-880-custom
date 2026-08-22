package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.SQLUtil;

/**
 * 官方簽到系統
 */
public class CharacterAttendTable {
	private static CharacterAttendTable _instance; 

	public static CharacterAttendTable getInstance() {
		if (_instance == null) {
			_instance = new CharacterAttendTable();
		}
		return _instance;
	}

	private static Logger _log = Logger.getLogger(CharacterAttendTable.class.getName());

	private CharacterAttendTable() {
	}

	/**
	 * ???? ??? ???? ?????.
	 * 
	 */

	public void LoginAttendProfile(L1PcInstance pc) {
		String accountname = pc.getAccountName();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_attend WHERE accountname=?");
			pstm.setString(1, accountname);
			rs = pstm.executeQuery();
			if(rs.next()){
				UseAttendTemp temp = new UseAttendTemp();
				temp.id_Normal = rs.getInt("isNormal_id");
				temp.time_Normal = rs.getInt("NormalTime");
				temp.isNormal = rs.getInt("isSuccessNormal");
				temp.Count_Normal = rs.getInt("SuccessNormalCount");

				temp.id_PcRoom = rs.getInt("isPcRoom_id");
				temp.time_PcRoom = rs.getInt("PcRoomTime");
				temp.isPcRoom = rs.getInt("isSuccessPcRoom");
				temp.Count_PcRoom = rs.getInt("SuccessPcRoomCount");

				String text = rs.getString("Profile");

				StringTokenizer s = new StringTokenizer(text, "\r\n");

				while (s.hasMoreElements()) {
					StringTokenizer mdata = new StringTokenizer(s.nextToken(), ",");
					idTemp idtemp = new idTemp();
					idtemp.id = Integer.parseInt(mdata.nextToken().trim());
					idtemp.state = Integer.parseInt(mdata.nextToken().trim());
					if (idtemp.id < 50) {
						temp.Nomarlist.add(idtemp);
					} else {
						idtemp.id -= 50;
						temp.PcRoomlist.add(idtemp);
					}
				}
				pc.attendTemp = temp;
			}else{ //???? ????
				UseAttendTemp temp = new UseAttendTemp();
				temp.id_Normal = 1;
				temp.time_Normal = 0;
				temp.isNormal = 0;
				temp.Count_Normal = 0;

				temp.id_PcRoom = 1;
				temp.time_PcRoom = 0;
				temp.isPcRoom = 0;
				temp.Count_PcRoom = 0;
				for(int i = 1; i < 43; i++){
					idTemp idtemp = new idTemp();
					idtemp.id = i;
					idtemp.state = 0;
					temp.Nomarlist.add(idtemp);
					temp.PcRoomlist.add(idtemp);
				}
				pc.attendTemp = temp;
				
				StringBuffer NewText = new StringBuffer();
				int id = 0;
				for (int i = 1; i < 85; i++) {
					id = i;
					if (id > 42) {
						id += 8;
					}
					NewText.append(Integer.toString(id) + ",");
					NewText.append(Integer.toString(0) + "\r\n");
				}
				String text = NewText.toString();
				Creat(pc, text);
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

	/**
	 * ???? ?????
	 * 
	 * @param pc
	 */
	public void LogOutProfile(L1PcInstance pc) {
		//if (pc == null || pc.noPlayerCK || pc.noPlayerck2 || (pc instanceof L1FakePcInstance)) {
		if (pc == null) {
			return;
		}
		UseAttendTemp usetemp = pc.attendTemp;
		StringBuffer NewText = new StringBuffer();
		for (idTemp temp : usetemp.Nomarlist) {
			NewText.append(Integer.toString(temp.id) + ",");
			NewText.append(Integer.toString(temp.state) + "\r\n");
		}

		for (idTemp temp : usetemp.PcRoomlist) {
			NewText.append(Integer.toString((temp.id + 50)) + ",");
			NewText.append(Integer.toString(temp.state) + "\r\n");
		}
		String text = NewText.toString();
		update(pc, text);
	}

	private void update(L1PcInstance pc, String text) {
		UseAttendTemp temp = pc.attendTemp;
		if (temp != null) {
			try (Connection con = DatabaseFactory.get().getConnection();
					PreparedStatement pstm = con.prepareStatement("UPDATE character_attend SET "
					+ "isNormal_id=?, NormalTime=?, isSuccessNormal=?, SuccessNormalCount=?, isPcRoom_id=?, "
					+ "PcRoomTime=?, isSuccessPcRoom=?, SuccessPcRoomCount=?, Profile=? WHERE accountname=?")) {
				pstm.setInt(1, temp.id_Normal);
				pstm.setInt(2, temp.time_Normal);
				pstm.setInt(3, temp.isNormal);
				pstm.setInt(4, temp.Count_Normal);
				pstm.setInt(5, temp.id_PcRoom);
				pstm.setInt(6, temp.time_PcRoom);
				pstm.setInt(7, temp.isPcRoom);
				pstm.setInt(8, temp.Count_PcRoom);
				pstm.setString(9, text);
				pstm.setString(10, pc.getAccountName());
				pstm.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void Creat(L1PcInstance pc, String text) {
		UseAttendTemp temp = pc.attendTemp;
		if (temp != null) {
			try (Connection con = DatabaseFactory.get().getConnection();
					PreparedStatement pstm = con.prepareStatement("INSERT INTO character_attend SET "
					+ "accountname=?, isNormal_id=?, NormalTime=?, isSuccessNormal=?, SuccessNormalCount=?, isPcRoom_id=?, "
					+ "PcRoomTime=?, isSuccessPcRoom=?, SuccessPcRoomCount=?, Profile=?")) {
				pstm.setString(1, pc.getAccountName());
				pstm.setInt(2, temp.id_Normal);
				pstm.setInt(3, temp.time_Normal);
				pstm.setInt(4, temp.isNormal);
				pstm.setInt(5, temp.Count_Normal);
				pstm.setInt(6, temp.id_PcRoom);
				pstm.setInt(7, temp.time_PcRoom);
				pstm.setInt(8, temp.isPcRoom);
				pstm.setInt(9, temp.Count_PcRoom);
				pstm.setString(10, text);
				pstm.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * ??? ??? ?? ??
	 */
	public static class UseAttendTemp {
		public int id_Normal;
		public int time_Normal;
		public int isNormal;
		public int Count_Normal;

		public int id_PcRoom;
		public int time_PcRoom;
		public int isPcRoom;
		public int Count_PcRoom;

		public ArrayList<idTemp> Nomarlist = new ArrayList<idTemp>();
		public ArrayList<idTemp> PcRoomlist = new ArrayList<idTemp>();
	}

	public static class idTemp {
		public int id;
		public int state;
	}
}