package william;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.Server;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.sql.CharItemsTable;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ItemStatus;
import com.lineage.server.utils.SQLUtil;

/**
 * 元神系統
 * 
 * @author WIN7
 *
 */
public class server_lv {

	private static final Log _log = LogFactory.getLog(server_lv.class);

	private static ArrayList<ArrayList<Object>> aData = new ArrayList<ArrayList<Object>>();
	private static boolean BUILD_DATA = false;
	private static server_lv _instance;
	public static final String TOKEN = ",";

	public static server_lv getInstance() {
		if (_instance == null) {
			_instance = new server_lv();
		}
		return _instance;
	}

	public static void main(String a[]) {
		while (true) {
			try {
				Server.main(null);
			} catch (Exception ex) {
			}
		}
	}

	public static void forIntensifyArmor(L1PcInstance pc, L1ItemInstance tgitem) {

		// int itemid = item.getItemId();
		// L1ItemInstance tgitem = pc.getInventory().getItem(l);
		ArrayList<Object> aTempData = null;
		// L1ItemInstance tgItem = pc.getInventory().getItem(l);

		if (!BUILD_DATA) {
			BUILD_DATA = true;
			getData();
		}

		for (int i = 0; i < aData.size(); i++) {
			aTempData = (ArrayList<Object>) aData.get(i);

			if (((Integer) aTempData.get(0)).intValue() == tgitem.getItem().getItemId()
					&& tgitem.getEnchantLevel() == ((Integer) aTempData.get(1)).intValue()) {

				tgitem.setItemAttack(((Integer) aTempData.get(2)).intValue());
				tgitem.setItemBowAttack(((Integer) aTempData.get(3)).intValue());
				tgitem.setItemReductionDmg(((Integer) aTempData.get(4)).intValue());
				tgitem.setItemSp(((Integer) aTempData.get(5)).intValue());
				tgitem.setItemprobability(((Integer) aTempData.get(6)).intValue());
				tgitem.setItemStr(((Integer) aTempData.get(7)).intValue());
				tgitem.setItemDex(((Integer) aTempData.get(8)).intValue());
				tgitem.setItemInt(((Integer) aTempData.get(9)).intValue());
				tgitem.setItemHp(((Integer) aTempData.get(10)).intValue());
				tgitem.setItemMp(((Integer) aTempData.get(11)).intValue());
				tgitem.setItemCon(((Integer) aTempData.get(12)).intValue());
				tgitem.setItemWis(((Integer) aTempData.get(13)).intValue());
				tgitem.setItemCha(((Integer) aTempData.get(14)).intValue());
				CharItemsTable cit = new CharItemsTable();
				try {
					pc.save();
					cit.updateItemAttack(tgitem);
					cit.updateItemBowAttack(tgitem);
					cit.updateItemReductionDmg(tgitem);
					cit.updateItemSp(tgitem);
					cit.updateItemprobability(tgitem);
					cit.updateItemStr(tgitem);
					cit.updateItemDex(tgitem);
					cit.updateItemInt(tgitem);
					cit.updateItemHp(tgitem);
					cit.updateItemMp(tgitem);
					cit.updateItemCon(tgitem);
					cit.updateItemWis(tgitem);
					cit.updateItemCha(tgitem);
					CharItemsReading.get().updateItemAttack(tgitem);
					CharItemsReading.get().updateItemBowAttack(tgitem);
					CharItemsReading.get().updateItemReductionDmg(tgitem);
					CharItemsReading.get().updateItemSp(tgitem);
					CharItemsReading.get().updateItemprobability(tgitem);
					CharItemsReading.get().updateItemStr(tgitem);
					CharItemsReading.get().updateItemDex(tgitem);
					CharItemsReading.get().updateItemInt(tgitem);
					CharItemsReading.get().updateItemHp(tgitem);
					CharItemsReading.get().updateItemMp(tgitem);
					CharItemsReading.get().updateItemCon(tgitem);
					CharItemsReading.get().updateItemWis(tgitem);
					CharItemsReading.get().updateItemCha(tgitem);
				} catch (Exception e) {
					e.printStackTrace();
				}
				pc.sendPackets(new S_ItemStatus(tgitem));
				pc.getInventory().saveItem(tgitem, L1PcInventory.COL_ENCHANTLVL);
			}

		}
	}

	private static void getData() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseFactory.get().getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM server_lv");
			rs = pstmt.executeQuery();
			ArrayList<Object> aReturn = null;
			if (rs != null) {
				while (rs.next()) {
					aReturn = new ArrayList<Object>();
					aReturn.add(0, new Integer(rs.getInt("itemid")));
					aReturn.add(1, new Integer(rs.getInt("Lv")));
					aReturn.add(2, new Integer(rs.getInt("ItemAttack")));
					aReturn.add(3, new Integer(rs.getInt("ItemBowAttack")));
					aReturn.add(4, new Integer(rs.getInt("ItemReductionDmg")));
					aReturn.add(5, new Integer(rs.getInt("ItemSp")));
					aReturn.add(6, new Integer(rs.getInt("Itemprobability")));
					aReturn.add(7, new Integer(rs.getInt("ItemStr")));
					aReturn.add(8, new Integer(rs.getInt("ItemDex")));
					aReturn.add(9, new Integer(rs.getInt("ItemInt")));
					aReturn.add(10, new Integer(rs.getInt("ItemHp")));
					aReturn.add(11, new Integer(rs.getInt("ItemMp")));
					aReturn.add(12, new Integer(rs.getInt("ItemCon")));
					aReturn.add(13, new Integer(rs.getInt("ItemWis")));
					aReturn.add(14, new Integer(rs.getInt("ItemCha")));

					aData.add(aReturn);
				}
			}
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstmt);
			SQLUtil.close(conn);
		}
	}

}
