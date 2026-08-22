package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1MeteAbility;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

public final class ExtraMeteAbilityTable {
	private static final Log _log = LogFactory.getLog(ExtraMeteAbilityTable.class);
	private static ExtraMeteAbilityTable _instance;
	private static final Map<Integer, L1MeteAbility> _meteList = new LinkedHashMap<Integer, L1MeteAbility>();

	public static ExtraMeteAbilityTable getInstance() {
		if (_instance == null) {
			_instance = new ExtraMeteAbilityTable();
		}
		return _instance;
	}

	public final void load() {
		PerformanceTimer timer = new PerformanceTimer();
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = DatabaseFactory.get().getConnection();
			pstm = conn.prepareStatement("SELECT * FROM extra_mete_ability ORDER BY mete_level");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int mete_level = rs.getInt("mete_level");
				String title = rs.getString("title");
				int type = rs.getInt("type");
				int ac = rs.getInt("ac");
				int hp = rs.getInt("hp");
				int mp = rs.getInt("mp");
				int hpr = rs.getInt("hpr");
				int mpr = rs.getInt("mpr");
				int str = rs.getInt("str");
				int con = rs.getInt("con");
				int dex = rs.getInt("dex");
				int wis = rs.getInt("wis");
				int cha = rs.getInt("cha");
				int intel = rs.getInt("intel");
				int sp = rs.getInt("sp");
				int mr = rs.getInt("mr");
				int hit_modifier = rs.getInt("hit_modifier");
				int dmg_modifier = rs.getInt("dmg_modifier");
				int bow_hit_modifier = rs.getInt("bow_hit_modifier");
				int bow_dmg_modifier = rs.getInt("bow_dmg_modifier");
				int magic_dmg_modifier = rs.getInt("magic_dmg_modifier");
				int magic_dmg_reduction = rs.getInt("magic_dmg_reduction");
				int reduction_dmg = rs.getInt("reduction_dmg");
				int defense_water = rs.getInt("defense_water");
				int defense_wind = rs.getInt("defense_wind");
				int defense_fire = rs.getInt("defense_fire");
				int defense_earth = rs.getInt("defense_earth");
				// int regist_stun = rs.getInt("regist_stun");
				// int regist_stone = rs.getInt("regist_stone");
				// int regist_sleep = rs.getInt("regist_sleep");
				// int regist_freeze = rs.getInt("regist_freeze");
				// int regist_sustain = rs.getInt("regist_sustain");
				// int regist_blind = rs.getInt("regist_blind");
				final int gift_box = rs.getInt("gift_box");

				L1MeteAbility meteAbility = new L1MeteAbility(title, ac, hp, mp, hpr, mpr, str, con, dex, wis, cha,
						intel, sp, mr, hit_modifier, dmg_modifier, bow_hit_modifier, bow_dmg_modifier,
						magic_dmg_modifier, magic_dmg_reduction, reduction_dmg, defense_water, defense_wind,
						defense_fire, defense_earth/*, regist_stun, regist_stone, regist_sleep, regist_freeze,
						regist_sustain, regist_blind*/, gift_box);
				int index = mete_level;
				if (type != -1) {
					index = index * 10000 + type;
				}
				_meteList.put(Integer.valueOf(index), meteAbility);
			}
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}
		_log.info("載入轉生附加能力資料數量: " + _meteList.size() + "(" + timer.get() + "ms)");
	}

	public final L1MeteAbility get(int meteLevel, int type) {
		int index = meteLevel * 10000 + type;

		L1MeteAbility temp = (L1MeteAbility) _meteList.get(Integer.valueOf(index));
		if (temp != null) {
			return temp;
		}
		return (L1MeteAbility) _meteList.get(Integer.valueOf(meteLevel));
	}

	public static final void effectBuff(L1PcInstance pc, L1MeteAbility value, int negative) {
		pc.addAc(value.getAc() * negative);
		pc.addMaxHp(value.getHp() * negative);
		pc.addMaxMp(value.getMp() * negative);
		pc.addHpr(value.getHpr() * negative);
		pc.addMpr(value.getMpr() * negative);
		pc.addStr(value.getStr() * negative);
		pc.addCon(value.getCon() * negative);
		pc.addDex(value.getDex() * negative);
		pc.addWis(value.getWis() * negative);
		pc.addCha(value.getCha() * negative);
		pc.addInt(value.getInt() * negative);
		pc.addSp(value.getSp() * negative);
		pc.addMr(value.getMr() * negative);
		pc.addHitup(value.getHitModifier() * negative);
		pc.addDmgup(value.getDmgModifier() * negative);
		pc.addBowHitup(value.getBowHitModifier() * negative);
		pc.addBowDmgup(value.getBowDmgModifier() * negative);

		pc.addMagicDmgModifier(value.getMagicDmgModifier() * negative);

		pc.addMagicDmgReduction(value.getMagicDmgReduction() * negative);

		pc.addDamageReductionByArmor(value.getReductionDmg() * negative);

		pc.addWater(value.getDefenseWater() * negative);
		pc.addWind(value.getDefenseWind() * negative);
		pc.addFire(value.getDefenseFire() * negative);
		pc.addEarth(value.getDefenseEarth() * negative);

		// pc.addRegistStun(value.getRegistStun() * negative);
		// pc.addRegistStone(value.getRegistStone() * negative);
		// pc.addRegistSleep(value.getRegistSleep() * negative);
		// pc.add_regist_freeze(value.getRegistFreeze() * negative);
		// pc.addRegistSustain(value.getRegistSustain() * negative);
		// pc.addRegistBlind(value.getRegistBlind() * negative);
	}
}
