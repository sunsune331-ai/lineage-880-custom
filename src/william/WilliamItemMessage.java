package william;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1ItemInstance;

/**
 * 道具自訂義訊息
 */
public class WilliamItemMessage {

	private static ArrayList<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();

	public static ArrayList<String> getItemInfo(final L1ItemInstance item) {
		final ArrayList<String> a = new ArrayList<String>();

		for (final ArrayList<Object> objects : list) {
			final int itemid = ((Integer) objects.get(0)).intValue();

            if (itemid == -1 || itemid == item.getItemId()) {
				a.add((String) objects.get(1));
				a.add((String) objects.get(2));
				a.add((String) objects.get(3));
				a.add((String) objects.get(4));
				a.add((String) objects.get(5));
				a.add((String) objects.get(6));
				a.add((String) objects.get(7));
				a.add((String) objects.get(8));
				a.add((String) objects.get(9));
				a.add((String) objects.get(10));
			}
		}

		return a;
	}

	// 2017/04/21
	public static void getData() {
		Connection con = null;
		try {
			con = DatabaseFactory.get().getConnection();
			final Statement stat = con.createStatement();
			final ResultSet rset = stat.executeQuery("SELECT * FROM server_item_msg");
			ArrayList<Object> aReturn = null;

			if (rset != null) {
				while (rset.next()) {
					aReturn = new ArrayList<Object>();
					aReturn.add(0, new Integer(rset.getInt("Item_Id")));
					aReturn.add(1, rset.getString("strings1"));
					aReturn.add(2, rset.getString("strings2"));
					aReturn.add(3, rset.getString("strings3"));
					aReturn.add(4, rset.getString("strings4"));
					aReturn.add(5, rset.getString("strings5"));
					aReturn.add(6, rset.getString("strings6"));
					aReturn.add(7, rset.getString("strings7"));
					aReturn.add(8, rset.getString("strings8"));
					aReturn.add(9, rset.getString("strings9"));
					aReturn.add(10, rset.getString("strings10"));

					list.add(aReturn);
				}
			}
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (final Exception e) {
		}
	}
}
