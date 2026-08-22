package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.data.item_armor.set.ArmorSet;
import com.lineage.server.templates.L1ArmorSets;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

public class ArmorSetTable {

	private static final Log _log = LogFactory.getLog(ArmorSetTable.class);
	private static ArmorSetTable _instance;
	private static final ArrayList<L1ArmorSets> _armorSetList = new ArrayList<L1ArmorSets>();
	private static HashMap<Integer, Integer> _armor_id_list = new HashMap<Integer, Integer>();

	public static ArmorSetTable get() {
		if (_instance == null) {
			_instance = new ArmorSetTable();
		}
		return _instance;
	}

	public void load() {
		PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `armor_set`");
			rs = pstm.executeQuery();
			fillTable(rs);
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		_log.info("載入套裝設置數量: " + _armorSetList.size() + "(" + timer.get() + "ms)");

		ArmorSet.load();
	}

	private void fillTable(ResultSet rs) throws SQLException {
		while (rs.next()) {
			L1ArmorSets as = new L1ArmorSets();
			as.setId(rs.getInt("id"));
			String sets = rs.getString("sets");
			as.setSets(rs.getString("sets"));
			for (int key : getArray(sets)) {
				_armor_id_list.put(Integer.valueOf(key), Integer.valueOf(rs.getInt("id")));
			}
			as.setPolyId(rs.getInt("polyid"));
			as.setPolyDesc(rs.getInt("poly_desc")); // 變身名字編號
			as.setAc(rs.getInt("防禦"));
			as.setHp(rs.getInt("血量"));
			as.setMp(rs.getInt("魔量"));
			as.setHpr(rs.getInt("體力回復"));
			as.setMpr(rs.getInt("魔力回復"));
			as.setMr(rs.getInt("魔防"));

			as.setStr(rs.getInt("力量"));
			as.setDex(rs.getInt("敏捷"));
			as.setCon(rs.getInt("體質"));
			as.setWis(rs.getInt("精神"));
			as.setCha(rs.getInt("魅力"));
			as.setIntl(rs.getInt("智力"));

			as.setDefenseWater(rs.getInt("水屬性增加"));
			as.setDefenseWind(rs.getInt("風屬性增加"));
			as.setDefenseFire(rs.getInt("火屬性增加"));
			as.setDefenseEarth(rs.getInt("地屬性增加"));
			as.set_modifier_dmg(rs.getInt("物理傷害"));
			as.set_reduction_dmg(rs.getInt("物理減免"));
			as.set_magic_modifier_dmg(rs.getInt("魔法傷害"));
			as.set_magic_reduction_dmg(rs.getInt("魔法減免"));
			as.set_bow_modifier_dmg(rs.getInt("遠程傷害"));
			as.set_haste(rs.getInt("加速"));
			as.set_sp(rs.getInt("魔攻"));
			as.set_hit_modifier(rs.getInt("近戰命中"));
			as.set_bow_hit_modifier(rs.getInt("遠程命中"));
			as.set_magiccritical_chance(rs.getInt("魔法暴擊"));

			as.set_all_reduction_dmg(rs.getInt("所有傷害減免"));
			as.set_Technology(rs.getInt("技術命中"));
			as.set_registTechnology(rs.getInt("技術耐性"));
			as.set_Elf(rs.getInt("精靈命中"));
			as.set_registElf(rs.getInt("精靈耐性"));
			as.set_Dragon(rs.getInt("龍屬命中"));
			as.set_registDragon(rs.getInt("龍屬耐性"));
			as.set_Horror(rs.getInt("恐怖命中"));
			as.set_registHorror(rs.getInt("恐怖耐性"));
			as.set_All(rs.getInt("所有命中"));
			as.set_registAll(rs.getInt("所有耐性"));
			as.set_pvpdmg(rs.getInt("pvp傷害"));
			as.set_pvpjm(rs.getInt("pvp減免"));
			as.set_yszfjs(rs.getInt("殷海薩祝"));			
			String gfx = rs.getString("gfx");
			if ((gfx != null) && (!gfx.equals(""))) {
				String[] gfxs = gfx.replaceAll(" ", "").split(",");
				int[] out = new int[gfxs.length];
				for (int i = 0; i < gfxs.length; i++) {
					out[i] = Integer.parseInt(gfxs[i]);
				}

				as.set_gfxs(out);
			}
			as.setEffectId(rs.getInt("effect_id"));
			as.setInterval(rs.getInt("interval"));
			as.setQuality1(rs.getString("quality1"));// src008
			_armorSetList.add(as);
		}
	}

	public L1ArmorSets[] getAllList() {
		return (L1ArmorSets[]) _armorSetList.toArray(new L1ArmorSets[_armorSetList.size()]);
	}

	private static int[] getArray(String s) {
		StringTokenizer st = new StringTokenizer(s, ",");
		int iSize = st.countTokens();
		String sTemp = null;
		int[] iReturn = new int[iSize];
		for (int i = 0; i < iSize; i++) {
			sTemp = st.nextToken();
			iReturn[i] = Integer.parseInt(sTemp);
		}
		return iReturn;
	}

	public boolean checkArmorSet(int item_id) {
		return _armor_id_list.containsKey(Integer.valueOf(item_id));
	}

	private static int[] getArray(String s, String sToken) {
		StringTokenizer st = new StringTokenizer(s, sToken);
		int size = st.countTokens();
		String temp = null;
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			temp = st.nextToken();
			array[i] = Integer.parseInt(temp);
		}
		return array;
	}

	public String getQuality1(int item_id) {// src008
		for (L1ArmorSets armorSets : get().getAllList()) {
			int[] tgItemId = getArray(armorSets.getSets(), ",");
			for (int i = 0; i < tgItemId.length; i++) {
				if (tgItemId[i] == item_id) {
					return armorSets.getQuality1();
				}
			}
		}
		return null;
	}
}
	