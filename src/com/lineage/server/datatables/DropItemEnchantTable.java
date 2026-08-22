package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1DropEnchant;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.Random;
import com.lineage.server.utils.SQLUtil;

/**
 * 掉落物品強化值資料
 *
 * @author dexc
 *
 */
public final class DropItemEnchantTable {

	private static final Log _log = LogFactory.getLog(DropItemEnchantTable.class);

	private static DropItemEnchantTable _instance;

	private static final HashMap<Integer, ArrayList<L1DropEnchant>> _dropItemEnchant = new HashMap<Integer, ArrayList<L1DropEnchant>>();

	public static DropItemEnchantTable get() {
		if (_instance == null) {
			_instance = new DropItemEnchantTable();
		}
		return _instance;
	}

	public void load() {
		final PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `drop_item_enchant`");
			rs = pstm.executeQuery();

			while (rs.next()) {
				final int npcid = rs.getInt("npc_id");
				final int itemid = rs.getInt("item_id");
				if (ItemTable.get().getTemplate(itemid) == null) {
					_log.error("掉落物品機率資料錯誤: 沒有這個編號的道具:" + itemid);
					errorItem(itemid);
					continue;
				}

				// 強化值陣列
				String dropenchants = rs.getString("drop_enchants").replaceAll(" ", "");
				final String[] enchants_tmp = dropenchants.split(",");

				final int[] enchants = new int[enchants_tmp.length];
				for (int i = 0; i < enchants_tmp.length; i++) {
					enchants[i] = Integer.parseInt(enchants_tmp[i]);
				}

				L1DropEnchant data = new L1DropEnchant(itemid, enchants);

				ArrayList<L1DropEnchant> datalist = _dropItemEnchant.get(npcid);
				if (datalist == null) {
					datalist = new ArrayList<L1DropEnchant>();
				}
				datalist.add(data);

				_dropItemEnchant.put(npcid, datalist);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入掉落物品強化值資料數量: " + _dropItemEnchant.size() + "(" + timer.get() + "ms)");
	}

	/**
	 * 取回NPC掉落道具強化值資料
	 * 
	 * @param npcid
	 * @return
	 */
	public ArrayList<L1DropEnchant> getDatalist(final int npcid) {
		final ArrayList<L1DropEnchant> datalist = _dropItemEnchant.get(npcid);
		if (datalist == null) {
			return null;
		}
		return datalist;
	}

	/**
	 * 隨機決定強化值
	 * 
	 * @param itemid
	 * @param npcid
	 * @return
	 */
	public int getEnchant(L1DropEnchant data) {
		int[] enchants = data.getEnchants();
		int level = enchants[Random.nextInt(enchants.length)];
		return level;
	}

	/**
	 * 刪除錯誤物品資料
	 * 
	 * @param objid
	 */
	private static void errorItem(int itemid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("DELETE FROM `drop_item_enchant` WHERE `item_id`=?");
			pstm.setInt(1, itemid);
			pstm.execute();

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

}
