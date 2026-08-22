/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
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
 * 飾品加成能力系統
 * @author
 */
public class EnchantAccessory {

	private static Logger _log = Logger.getLogger(EnchantAccessory.class.getName());

	private static final Log _logx = LogFactory.getLog(EnchantAccessory.class.getName());

	private static EnchantAccessory _instance;

	private final HashMap<Integer, L1WilliamEnchantAccessory> _ArmorIndex = new HashMap<Integer, L1WilliamEnchantAccessory>();

	public static EnchantAccessory getInstance() {
		if (_instance == null) {
			_instance = new EnchantAccessory();
		}
		return _instance;
	}

	private EnchantAccessory() {
		loadArmorOrginal();
	}

	private void loadArmorOrginal() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			// pstm = con.prepareStatement("SELECT * FROM william_lv_accessory");
			pstm = con.prepareStatement("select * from william_lv_accessory order by type,level asc");
			rs = pstm.executeQuery();
			fillWeaponSkill(rs);

		} catch (final SQLException e) {
			_log.log(Level.SEVERE, "error while creating william_lv_accessory table", e);

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
			final int type = rs.getInt("type");
			final int strength = rs.getInt("greater");
			final int level = rs.getInt("level");
			final byte addStr = rs.getByte("addStr");
			final byte addDex = rs.getByte("addDex");
			final byte addCon = rs.getByte("addCon");
			final byte addInt = rs.getByte("addInt");
			final byte addWis = rs.getByte("addWis");
			final byte addCha = rs.getByte("addCha");
			final int addAc = rs.getInt("addAc");
			final int addMaxHp = rs.getInt("addMaxHp");
			final int addMaxMp = rs.getInt("addMaxMp");
			final int addHpr = rs.getInt("addHpr");
			final int addMpr = rs.getInt("addMpr");
			final int addDmg = rs.getInt("addDmg");
			final int addBowDmg = rs.getInt("addBowDmg");
			final int addHit = rs.getInt("addHit");
			final int addBowHit = rs.getInt("addBowHit");
			final int addDmgReduction = rs.getInt("addDmgReduction");
			final int addMr = rs.getInt("addMr");
			final int addSp = rs.getInt("addSp");
			final int PVPdmg = rs.getInt("PVPdmg");
			final int PVPdmgReduction = rs.getInt("PVPdmgReduction");
			final int Potion_Heal = rs.getInt("Potion_Heal");
			final int Potion_Healling = rs.getInt("Potion_Healling");
			final int magic_hit = rs.getInt("add_magic_hit");

			// final int regist_fear = rs.getInt("add_regist_fear");
			
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

			final int addEinhasadConsumeReduce = rs.getInt("einConsumeReduce"); // 殷海薩祝福消耗減少(1=1%)

			final L1WilliamEnchantAccessory ArmorOrginal = new L1WilliamEnchantAccessory(id, type, strength, level,
					addStr, addDex, addCon, addInt, addWis, addCha, addAc, addMaxHp, addMaxMp, addHpr, addMpr, addDmg,
					addBowDmg, addHit, addBowHit, addDmgReduction, addMr, addSp, PVPdmg, PVPdmgReduction, Potion_Heal,
					Potion_Healling, magic_hit
					// , regist_fear
					, addRegistTechnology, addRegistElf, addRegistDragon, addRegistHorror, addRegistAll,
					addHitTechnology, addHitElf, addHitDragon, addHitHorror, addHitAll, addEinhasadConsumeReduce);
			_ArmorIndex.put(id, ArmorOrginal);
			id++;
		}
		_logx.info("載入飾品等級能力數據資料數量: " + _ArmorIndex.size() + "(" + timer.get() + "ms)");
	}

	public L1WilliamEnchantAccessory getTemplate(final int Armor) {
		return _ArmorIndex.get(Armor);
	}

	public L1WilliamEnchantAccessory[] getArmorList() {
		return _ArmorIndex.values().toArray(new L1WilliamEnchantAccessory[_ArmorIndex.size()]);
	}
}
