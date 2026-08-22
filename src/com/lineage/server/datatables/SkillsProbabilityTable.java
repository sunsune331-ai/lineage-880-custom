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
import com.lineage.server.templates.L1Skills;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 負面技能機率設置DB化
 */
public class SkillsProbabilityTable {

	private static final Log _log = LogFactory.getLog(SkillsProbabilityTable.class);

	private static SkillsProbabilityTable _instance;

	private final Map<Integer, L1Skills> _skills = new HashMap<Integer, L1Skills>();

	public static SkillsProbabilityTable get() {
		if (_instance == null) {
			_instance = new SkillsProbabilityTable();
		}
		return _instance;
	}

	private SkillsProbabilityTable() {
		load();
	}

	public static void reload() {
		SkillsProbabilityTable oldInstance = _instance;
		_instance = new SkillsProbabilityTable();
		oldInstance._skills.clear();
		oldInstance = null;
	}

	private void load() {
		final PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `skills_pc_probability`");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final int skill_id = rs.getInt("skill_id");
				final L1Skills l1skills = SkillsTable.get().getTemplate(skill_id);
				if (l1skills == null) {
					_log.error("負面技能機率DB化設置資料 不存在技能編號: " + skill_id);
				}
				l1skills.setSkillId(skill_id);
				l1skills.setName(rs.getString("name"));
				l1skills.setOpen(rs.getInt("是否啟用"));
				l1skills.setProbability_Lv1(rs.getInt("小於被打者"));
				l1skills.setProbability_Lv2(rs.getInt("等於被打者"));
				l1skills.setProbability_Lv3(rs.getInt("高於被打者"));
				l1skills.set_intel_add_probability(rs.getInt("智力加百分比"));
				l1skills.set_intel_add_probability_max(rs.getInt("智力加上限"));
				l1skills.set_magic_hit_probability(rs.getInt("魔法命中加百分比"));
                l1skills.set_level_met(rs.getInt("幾轉開始加"));
                l1skills.set_level_met_probability(rs.getInt("幾轉加多少"));
                l1skills.setHitTechnology(rs.getInt("啟用技術命中"));
                l1skills.setHitElf(rs.getInt("啟用精靈命中"));
                l1skills.setHitDragon(rs.getInt("啟用龍屬命中"));
                l1skills.setHitHorror(rs.getInt("啟用恐怖命中"));
                // 被打者
				l1skills.setProbability_Mr(rs.getInt("被打者魔防扣機率百分比"));
				l1skills.setRegistTechnology(rs.getInt("被打者啟用技術耐性"));
				l1skills.setRegistElf(rs.getInt("被打者啟用精靈耐性"));
				l1skills.setRegistDragon(rs.getInt("被打者啟用龍屬耐性"));
				l1skills.setRegistHorror(rs.getInt("被打者啟用恐怖耐性"));
				_skills.put(new Integer(skill_id), l1skills);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入負面技能機率DB化設置資料數量: " + this._skills.size() + "(" + timer.get() + "ms)");
	}

	public L1Skills getTemplate(final int skill_id) {
		if (_skills.containsKey(skill_id)) {
			return _skills.get(skill_id);
		}
		return null;
	}
}
