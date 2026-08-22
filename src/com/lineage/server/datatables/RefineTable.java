package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1Refine;
import com.lineage.server.utils.SQLUtil;

/**
 * 火神精煉
 * 
 * @author shinogame
 */
public class RefineTable { // src016
	private static Log _log = LogFactory.getLog(RefineTable.class);

	private static RefineTable _instance;
	private final ArrayList<L1Refine> _refineLists;

	public static RefineTable getInstance() {
		if (_instance == null) {
			_instance = new RefineTable();
		}
		return _instance;
	}

	private RefineTable() {
		this._refineLists = allRefineList();
	}

	public static void reload() {
		RefineTable oldInstance = _instance;
		_instance = new RefineTable();
		oldInstance._refineLists.clear();
	}

	private ArrayList<L1Refine> allRefineList() {
		ArrayList<L1Refine> refineList = new ArrayList<L1Refine>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("select * from system_Vulcan_refinery");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int itemId = rs.getInt("itemId");// 道具
				int enchantLevel = rs.getInt("enchantLevel");// 加成
				int newItemId = rs.getInt("new_item_id");// 給予
				int newItemCount = rs.getInt("new_item_count");// 數量
				int plusItemId = rs.getInt("plus_item_id");// 增加成功機率的道具
				int plusItemCount = rs.getInt("pulus_counts");// 數量
				int plusItemChance = rs.getInt("pulus_add_chance");// 機率

				L1Refine refine = new L1Refine(itemId, enchantLevel, newItemId, newItemCount, plusItemId, plusItemCount,
						plusItemChance);

				refineList.add(refine);
			}
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return refineList;
	}

	public L1Refine getRefine(int itemId, int enchantLevel) {
		for (L1Refine refine : this._refineLists) {
			if ((refine.getItemId() == itemId) && (refine.getEnchantLevel() == enchantLevel)) {
				return refine;
			}
		}
		return null;
	}

	public boolean isRefineable(int itemId) {
		for (L1Refine refine : this._refineLists) {
			if (refine.getItemId() == itemId) {
				return true;
			}
		}
		return false;
	}

	public boolean isRefineable(int itemId, int enchantLevel) {
		for (L1Refine refine : this._refineLists) {
			if ((refine.getItemId() == itemId) && (refine.getEnchantLevel() == enchantLevel)) {
				return true;
			}
		}
		return false;
	}
}
