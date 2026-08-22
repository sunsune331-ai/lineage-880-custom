package william;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1ItemInstance;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GiveBack {
	public static final List<Integer> savepcid = new ArrayList();
	public static final List<L1ItemInstance> saveweapon = new ArrayList();
	public static final List<String> savename = new ArrayList();

	public static void load() {
		Connection conn = null;
		try {
			conn = DatabaseFactory.get().getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat
					.executeQuery("SELECT * FROM william_npcdieshare");
			ArrayList arraylist = null;
			while ((rs != null) && (rs.next())) {
			}
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
			}
		} catch (Exception ex) {
		}
	}

	public static boolean isNpc(int npocid) {
		Connection conn = null;
		try {
			conn = DatabaseFactory.get().getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat
					.executeQuery("SELECT * FROM william_npcdieshare");
			ArrayList arraylist = null;
			if (rs != null) {
				while (rs.next()) {
					int npcsid = rs.getInt("npcid");
					if (npocid == npcsid) {
						return true;
					}
				}
			}
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
			}
		} catch (Exception ex) {
			return false;
		}
		return false;
	}

	public static String getItem(int npocid) {
		Connection conn = null;
		try {
			conn = DatabaseFactory.get().getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat
					.executeQuery("SELECT * FROM william_npcdieshare");
			ArrayList arraylist = null;
			if (rs != null) {
				while (rs.next()) {
					int npcsid = rs.getInt("npcid");
					String itemid = rs.getString("itemid");
					if (npocid == npcsid) {
						return itemid;
					}
				}
			}
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
			}
		} catch (Exception ex) {
			return null;
		}
		return null;
	}

	public static String getCount(int npocid) {
		Connection conn = null;
		try {
			conn = DatabaseFactory.get().getConnection();
			Statement stat = conn.createStatement();
			ResultSet rs = stat
					.executeQuery("SELECT * FROM william_npcdieshare");
			ArrayList arraylist = null;
			if (rs != null) {
				while (rs.next()) {
					int npcsid = rs.getInt("npcid");
					String count = rs.getString("count");
					if (npocid == npcsid) {
						return count;
					}
				}
			}
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
			}
		} catch (Exception ex) {
			return null;
		}
		return null;
	}
}

