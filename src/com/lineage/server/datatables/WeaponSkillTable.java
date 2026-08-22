package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1WeaponSkill;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

public class WeaponSkillTable {
	private static final Log _log = LogFactory.getLog(WeaponSkillTable.class);
	private static WeaponSkillTable _instance;
	private static final Map<Integer, L1WeaponSkill> _weaponIdIndex = new HashMap<Integer, L1WeaponSkill>();

	public static WeaponSkillTable get() {
		if (_instance == null) {
			_instance = new WeaponSkillTable();
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
			pstm = con.prepareStatement("SELECT * FROM `weapon_skill`");
			rs = pstm.executeQuery();
			fillWeaponSkillTable(rs);
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入技能武器資料數量: " + _weaponIdIndex.size() + "(" + timer.get() + "ms)");
	}

	private void fillWeaponSkillTable(ResultSet rs) throws SQLException {
		while (rs.next()) {
			int weaponId = rs.getInt("weapon_id");
			final String skillName = rs.getString("skill_name"); // 魔法武器發動的技能名稱
			int probability = rs.getInt("probability");
			int fixDamage = rs.getInt("fix_damage");
			int randomDamage = rs.getInt("random_damage");
			int area = rs.getInt("area");
			int skillId = rs.getInt("skill_id");
			int skillTime = rs.getInt("skill_time");
			int effectId = rs.getInt("effect_id");
			int effectTarget = rs.getInt("effect_target");
			boolean isArrowType = rs.getBoolean("arrow_type");
			int attr = rs.getInt("attr");
			int effectId1 = rs.getInt("effect_id1");
			int effectTarget1 = rs.getInt("effect_target1");
			int effect_xy1 = rs.getInt("effect_xy1");
			int effectId2 = rs.getInt("effect_id2");
			int effectTarget2 = rs.getInt("effect_target2");
			int effect_xy2 = rs.getInt("effect_xy2");
			int effectId3 = rs.getInt("effect_id3");
			int effectTarget3 = rs.getInt("effect_target3");
			int effect_xy3 = rs.getInt("effect_xy3");
			int effectId4 = rs.getInt("effect_id4");
			int effectTarget4 = rs.getInt("effect_target4");
			int effect_xy4 = rs.getInt("effect_xy4");
			
			L1WeaponSkill weaponSkill = new L1WeaponSkill();
			weaponSkill.setWeaponId(weaponId);
			weaponSkill.setSkillName(skillName); // 魔法武器發動的技能名稱
			weaponSkill.setProbability(probability);
			weaponSkill.setFixDamage(fixDamage);
			weaponSkill.setRandomDamage(randomDamage);
			weaponSkill.setArea(area);
			weaponSkill.setSkillId(skillId);
			weaponSkill.setSkillTime(skillTime);
			weaponSkill.setEffectId(effectId);
			weaponSkill.setEffectTarget(effectTarget);
			weaponSkill.setArrowType(isArrowType);
			weaponSkill.setAttr(attr);
			weaponSkill.setEffectId1(effectId1);
			weaponSkill.setEffectId2(effectId2);
			weaponSkill.setEffectId3(effectId3);
			weaponSkill.setEffectId4(effectId4);
			weaponSkill.setEffectTarget1(effectTarget1);
			weaponSkill.setEffectTarget2(effectTarget2);
			weaponSkill.setEffectTarget3(effectTarget3);
			weaponSkill.setEffectTarget4(effectTarget4);
			weaponSkill.setEffectXY1(effect_xy1);
			weaponSkill.setEffectXY2(effect_xy2);
			weaponSkill.setEffectXY3(effect_xy3);
			weaponSkill.setEffectXY4(effect_xy4);
			
			_weaponIdIndex.put(Integer.valueOf(weaponId), weaponSkill);
		}
	}

	public L1WeaponSkill getTemplate(int weaponId) {
		return (L1WeaponSkill) _weaponIdIndex.get(Integer.valueOf(weaponId));
	}
}

