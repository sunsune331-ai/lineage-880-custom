package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import com.lineage.DatabaseFactory;

public class CraftInfoTable {

	private static CraftInfoTable ins;

	private final HashMap<Integer, String[]> craftNpcList = new HashMap<Integer, String[]>();

	public static CraftInfoTable getIns() {
		if (ins == null)
			ins = new CraftInfoTable();
		return ins;
	}

	private CraftInfoTable() {
		loadCraftNpcs();
	}

	// public void reLoad() {
	// CraftInfoTable oldIns = ins;
	// ins = new CraftInfoTable();
	// oldIns.craftNpcList.clear();
	// oldIns = null;
	// }

	private void loadCraftNpcs() {
		try (Connection con = DatabaseFactory.get().getConnection();
				PreparedStatement pstm = con.prepareStatement("SELECT * FROM craft_npcs");
				ResultSet rs = pstm.executeQuery()) {
			while (rs.next()) {
				final int npcId = rs.getInt("npc_id");
				final String[] craftList = rs.getString("craft_id_list").split(",");
				if (craftList.length == 0 || craftList == null) {
					System.out.println("craft_npcs製作清單編號錯誤 npcId : " + npcId);
				} else {
					craftNpcList.put(npcId, craftList);
				}
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getCraftNpc(final int npcId) {
		return craftNpcList.get(npcId);
	}
}
