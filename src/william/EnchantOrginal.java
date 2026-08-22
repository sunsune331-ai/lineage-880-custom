package william;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 裝備強化能力系統
 */
public class EnchantOrginal {

	private static Logger _log = Logger.getLogger(EnchantOrginal.class.getName());

	private static final Log _logx = LogFactory.getLog(EnchantOrginal.class.getName());

	private static EnchantOrginal _instance;

	private final HashMap<Integer, L1WilliamEnchantOrginal> _ArmorIndex = new HashMap<Integer, L1WilliamEnchantOrginal>();

	public static EnchantOrginal getInstance() {
		if (_instance == null) {
			_instance = new EnchantOrginal();
		}
		return _instance;
	}

	private EnchantOrginal() {
		loadArmorOrginal();
	}

	private void loadArmorOrginal() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			// pstm = con.prepareStatement("SELECT * FROM william_lv_armor_weapon");
			pstm = con.prepareStatement("select * from william_lv_armor_weapon order by itemid,enchantlevel asc");
			rs = pstm.executeQuery();
			fillWeaponSkill(rs);

		} catch (final SQLException e) {
			_log.log(Level.SEVERE, "error while creating william_lv_armor_weapon table", e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void fillWeaponSkill(final ResultSet rs) throws SQLException {
		final PerformanceTimer timer = new PerformanceTimer();
		int id = 0;
		while (rs.next()) {
			final int itemid = rs.getInt("itemid");
			final int level = rs.getInt("enchantlevel");
			final int addAc = rs.getInt("addAc"); // 額外防禦
			final byte addStr = rs.getByte("addStr");
			final byte addDex = rs.getByte("addDex");
			final byte addCon = rs.getByte("addCon");
			final byte addInt = rs.getByte("addInt");
			final byte addWis = rs.getByte("addWis");
			final byte addCha = rs.getByte("addCha");
			final int addMaxHp = rs.getInt("addMaxHp");
			final int addMaxMp = rs.getInt("addMaxMp");
			final int addHpr = rs.getInt("addHpr");
			final int addMpr = rs.getInt("addMpr");
			final int addDmg = rs.getInt("addDmg");
			final int addBowDmg = rs.getInt("addBowDmg");
			final int addHit = rs.getInt("addHit");
			final int addBowHit = rs.getInt("addBowHit");
			final int reduction_dmg = rs.getInt("addDmgReduction"); // 所有傷害減免
			final int addMr = rs.getInt("addMr");
			final int addSp = rs.getInt("addSp");
			final int addFire = rs.getInt("addFire");
			final int addWind = rs.getInt("addWind");
			final int addEarth = rs.getInt("addEarth");
			final int addWater = rs.getInt("addWater");
			final int addPvpDmg = rs.getInt("addPvpDmg"); // 增加PVP傷害
			final int addPvpDmg_R = rs.getInt("addPvpDmg_R"); // 減免PVP傷害
			final int addCloseCri = rs.getInt("addCloseCri"); // 近距離爆擊率
			final int addBowCri = rs.getInt("addBowCri"); // 遠距離爆擊率
			final int addMagicCri = rs.getInt("addMagicCri"); // 魔法爆擊率

			// final int addFearLevel = rs.getInt("addFearLevel"); // 恐怖等級

			final int addRegistTechnology = rs.getInt("技術耐性"); // 技術耐性
			final int addRegistElf = rs.getInt("精靈耐性"); // 精靈耐性
			final int addRegistDragon = rs.getInt("龍屬耐性"); // 龍屬耐性
			final int addRegistHorror = rs.getInt("恐怖耐性"); // 恐怖耐性
			final int addRegistAll = rs.getInt("全部耐性"); // 全部四大耐性

			final int addHitTechnology = rs.getInt("技術命中"); // 技術命中
			final int addHitElf = rs.getInt("精靈命中"); // 精靈命中
			final int addHitDragon = rs.getInt("龍屬命中"); // 龍屬命中
			final int addHitHorror = rs.getInt("恐怖命中"); // 恐怖命中
			final int addHitAll = rs.getInt("全部命中"); // 全部四大命中
			final int magic_hit_modifier = rs.getInt("魔法命中"); // 全部四大命中
			//final int jilvjianmian = rs.getInt("几率減免"); // 全部四大命中a

			final int addEinhasadConsumeReduce = rs.getInt("einConsumeReduce"); // 殷海薩祝福消耗減少(1=1%)

			final L1WilliamEnchantOrginal ArmorOrginal = new L1WilliamEnchantOrginal(id, itemid, level, addAc, addStr,
					addDex, addCon, addInt, addWis, addCha, addMaxHp, addMaxMp, addHpr, addMpr, addDmg, addBowDmg,
					addHit, addBowHit, reduction_dmg, addMr, addSp, addFire, addWind, addEarth, addWater, addPvpDmg,
					addPvpDmg_R, addCloseCri, addBowCri, addMagicCri
					// , addFearLevel
					, addRegistTechnology, addRegistElf, addRegistDragon, addRegistHorror, addRegistAll,
					addHitTechnology, addHitElf, addHitDragon, addHitHorror, addHitAll,magic_hit_modifier , addEinhasadConsumeReduce);
			_ArmorIndex.put(id, ArmorOrginal);
			id++;
		}
		_logx.info("載入裝備強化能力數據資料數量: " + _ArmorIndex.size() + "(" + timer.get() + "ms)");
	}

	public L1WilliamEnchantOrginal getTemplate(final int Armor) {
		return _ArmorIndex.get(Armor);
	}

	public L1WilliamEnchantOrginal[] getArmorList() {
		return _ArmorIndex.values().toArray(new L1WilliamEnchantOrginal[_ArmorIndex.size()]);
	}
}
