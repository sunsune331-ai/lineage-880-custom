package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.templates.L1Craft;
import com.lineage.server.utils.SQLUtil;

public class CraftListTable {

	private static final Log _log = LogFactory.getLog(CraftListTable.class);

	private static CraftListTable _instance;

	public static CraftListTable getInstance() {
		if (_instance == null) {
			_instance = new CraftListTable();
		}
		return _instance;
	}

	private final HashMap<Integer, L1Craft> _list = new HashMap<>();

	private CraftListTable() {

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM craft");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final int id = rs.getInt("id");
				final String note = rs.getString("note");
				final L1Craft craft = new L1Craft(id);

				final String craft_itemid = rs.getString("craft_itemid");
				final String craft_count = rs.getString("craft_count");
				final String craft_enchant = rs.getString("craft_enchant");
				
				final String craft_attrId = rs.getString("craft_attrId"); // 新增武器屬性
				final String craft_attrLv = rs.getString("craft_attrLv"); // 新增武器屬性

				final String[] craft_itemID_List = craft_itemid.split(",");
				final String[] craft_itemCount_List = craft_count.split(",");
				final String[] craft_itemEnchant_List = craft_enchant.split(",");
				
				final String[] craft_itemAttrId_List = craft_attrId.split(","); // 新增武器屬性
				final String[] craft_itemAttrLv_List = craft_attrLv.split(","); // 新增武器屬性

				for (int i = 0; i < craft_itemID_List.length; i++) {
					try {
						if (craft_itemID_List[i].trim().length() > 0) {
							final int craft_itemID = Integer.parseInt(craft_itemID_List[i]);
							final int craft_itemCount = Integer.parseInt(craft_itemCount_List[i]);
							final int craft_itemEnchant = Integer.parseInt(craft_itemEnchant_List[i]);
							
							final int craft_itemAttrId = Integer.parseInt(craft_itemAttrId_List[i]); // 新增武器屬性
							final int craft_itemAttrLv = Integer.parseInt(craft_itemAttrLv_List[i]); // 新增武器屬性

							// 新增武器屬性
							craft.setCraftItem(craft_itemID, craft_itemCount, craft_itemEnchant, craft_itemAttrId, craft_itemAttrLv);
						}
					} catch (final ArrayIndexOutOfBoundsException ae) {
						System.out.println("Craft table [" + note + "] errer : check craft item");
						continue;
					}
				}

				final String material = rs.getString("material");
				final String material_count = rs.getString("material_count");
				final String material_enchant = rs.getString("material_enchant");
				final String material_bless = rs.getString("material_bless");

				final String material_attrId = rs.getString("material_attrId"); // 新增武器屬性
				final String material_attrLv = rs.getString("material_attrLv"); // 新增武器屬性

				final String[] materialID_List = material.split(",");
				final String[] materialCount_List = material_count.split(",");
				final String[] materialEnchant_List = material_enchant.split(",");
				final String[] materialBless_List = material_bless.split(",");

				final String[] materialItemAttrId_List = material_attrId.split(","); // 新增武器屬性
				final String[] materialItemAttrLv_List = material_attrLv.split(","); // 新增武器屬性

				for (int i = 0; i < materialID_List.length; i++) {
					try {
						if (materialID_List[i].trim().length() > 0) {
							final int materialID = Integer.parseInt(materialID_List[i]);
							final int materialCount = Integer.parseInt(materialCount_List[i]);
							final int materialEnchant = Integer.parseInt(materialEnchant_List[i]);
							final int materialBless = Integer.parseInt(materialBless_List[i]);

							final int materialItemAttrId = Integer.parseInt(materialItemAttrId_List[i]); // 新增武器屬性
							final int materialItemAttrLv = Integer.parseInt(materialItemAttrLv_List[i]); // 新增武器屬性

							// 新增武器屬性
							craft.addMaterialItem(materialID, materialCount, materialEnchant, materialBless, materialItemAttrId, materialItemAttrLv);

						}
					} catch (final ArrayIndexOutOfBoundsException ae) {
						System.out.println("Craft table [" + note + "] errer : check material");
						continue;
					}
				}
				craft.setClassLimit(rs.getInt("class_limit"));
				craft.setLevelLimit(rs.getInt("min_level"), rs.getInt("max_level"));
				craft.setLawfulLimit(rs.getInt("min_lawful"), rs.getInt("max_lawful"));
				craft.setKarmaLimit(rs.getInt("min_karma"), rs.getInt("max_karma"));
				craft.setMaxCraftCount(rs.getInt("max_count"));
				craft.setSuccessChance(rs.getInt("change"));

				//craft.setAddChanceItem(rs.getInt("add_chance_itemid"));// 火神練化-增加機率道具
				craft.setAddChanceItem(rs.getInt("add_chance_itemid"), rs.getInt("add_chance_itemid_count"));// 火神練化-增加機率道具

				//craft.setCraftFailItem(rs.getInt("fail_itemid"), rs.getInt("fail_item_count"));//失敗時退回的道具
				craft.setCraftFailItem(rs.getInt("fail_itemid"), rs.getInt("fail_item_count"),
						rs.getInt("fail_item_enchant"), rs.getInt("fail_item_attrId"), rs.getInt("fail_item_attrLv"));// 失敗時退回的道具

				craft.setPerfectCance(rs.getInt("perfect_chance")); // 大成功機率
				craft.setCraftPerfectItem(rs.getInt("perfect_itemid"), rs.getInt("perfect_item_count"), rs.getInt("perfect_item_enchant")); // 大成功道具

				// final int perfect_itemid = rs.getInt("perfect_itemid");
				// if (craft.getPerfectCance() > 0) { // 大成功道具
				// final L1Item item = ItemTable.get().getTemplate(perfect_itemid);
				// if (item == null) {
				// _log.error("大成功道具不存在: craftID" + id);
				// continue;
				// }
				// }
				// craft.setCraftPerfectItem(perfect_itemid);

				craft.setShowWorld(rs.getInt("showworld")); // 打造道具成功是否公告
				craft.addIgnoreItem(rs.getInt("ignoreItemId")); // 身上已有此編號道具將不能在製作

				// craft_nameid
				craft.setCraftNameID(rs.getInt("craft_nameid"));
				// add
				if (_list.containsKey(craft.getCraftID())) {
					System.out.println("CraftListTable : craft ID = " + craft.getCraftID() + " repeat!!");
					continue;
				}
				_list.put(craft.getCraftID(), craft);
			}

			// exchange setting
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM craft_exchange");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final int craft_id = rs.getInt("craft_id");
				final int material_itemid = rs.getInt("material_itemid");
				final int exchange_itemid = rs.getInt("exchange_itemid");
				final int exchange_count = rs.getInt("exchange_count");
				final int exchange_enchant = rs.getInt("exchange_enchant");
				final int exchange_bless = rs.getInt("exchange_bless");
				final int exchange_attrid = rs.getInt("exchange_attrid"); // 新增武器屬性
				final int exchange_attrlv = rs.getInt("exchange_attrlv"); // 新增武器屬性

				if (!_list.containsKey(craft_id)) {
					System.out.println("craft_exchange Table: craftID" + craft_id + " is not exist");
					continue;
				}

				final L1Craft craft = _list.get(craft_id);

				for (final L1ItemInstance item : craft.getMaterialItems().values()) {

					if (item.getItemId() == material_itemid) {
						// 新增武器屬性
						craft.setExchangeable(material_itemid, exchange_itemid, exchange_count, exchange_enchant,
								exchange_bless, exchange_attrid, exchange_attrlv);
						break;
					}

				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		SQLUtil.close(rs, pstm, con);

	}

	public ArrayList<L1Craft> getList() {
		final ArrayList<L1Craft> array = new ArrayList<>();
		for (final L1Craft craft : _list.values()) {
			array.add(craft);
		}
		return array;
	}

	public L1Craft getList(final int id) {
		return _list.get(id);
	}
}
