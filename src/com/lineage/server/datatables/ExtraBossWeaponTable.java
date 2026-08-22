package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1BossWeapon;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ExtraBossWeaponTable
{
  private static final Log _log = LogFactory.getLog(ExtraBossWeaponTable.class);
  
  private static ExtraBossWeaponTable _instance;
  
  private static final Map<Integer, L1BossWeapon> _BossList = new LinkedHashMap<Integer, L1BossWeapon>();
  
  public static ExtraBossWeaponTable getInstance() {
    if (_instance == null) {
      _instance = new ExtraBossWeaponTable();
    }
    return _instance;
  }
  
  public final void load() {
    PerformanceTimer timer = new PerformanceTimer();
    Connection con = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    try {
      con = DatabaseFactory.get().getConnection();
      pstm = con
        .prepareStatement("SELECT * FROM extra_weapon_boss ORDER BY item_id");
      rs = pstm.executeQuery();
      
      while (rs.next()) {
        int item_id = rs.getInt("item_id");
        int boss_lv = rs.getInt("boss_lv");
        String boss_name = rs.getString("boss_name");
        int success_random = rs.getInt("success_random");
        int max_use_time = rs.getInt("max_use_time");
        String success_msg = rs.getString("success_msg");
        String failure_msg = rs.getString("failure_msg");
        int probability = rs.getInt("probability");
        boolean isLongRange = rs.getBoolean("isLongRange");
        int fixDamage = rs.getInt("fixDamage");
        int randomDamage = rs.getInt("randomDamage");
        double doubleDmgValue = rs.getInt("doubleDmgValue");
        int gfxId = rs.getInt("gfxId");
        boolean gfxIdTarget = rs.getBoolean("gfxIdTarget");
        boolean arrowType = rs.getBoolean("arrowType");
        int effectId = rs.getInt("effectId");
        int effectTime = rs.getInt("effectTime");
		final int negativeId = rs.getInt("negativeId");
		final int negativeTime = rs.getInt("negativeTime");
        int attr = rs.getInt("attr");
        int hpAbsorb = rs.getInt("hpAbsorb");
        int mpAbsorb = rs.getInt("mpAbsorb");
        boolean type_remove_weapon = rs.getBoolean("type_remove_weapon");
		int type_remove_armor = rs.getInt("type_remove_armor");
        
        L1BossWeapon bossStone = new L1BossWeapon(item_id, 
          boss_lv, boss_name, success_random, max_use_time, 
          success_msg, failure_msg, probability, isLongRange, 
          fixDamage, randomDamage, doubleDmgValue, gfxId, 
          gfxIdTarget, arrowType, effectId, effectTime,
			negativeId, negativeTime,
          attr, 
          hpAbsorb, mpAbsorb,type_remove_weapon,type_remove_armor);
        
        int index = item_id * 100 + boss_lv;
        
        _BossList.put(Integer.valueOf(index), bossStone);
      }
    }
    catch (SQLException e) {
      _log.error(e.getLocalizedMessage(), e);
    }
    finally {
      SQLUtil.close(rs);
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
    _log.info("載入boss武器資料數量: " + _BossList.size() + "(" + timer.get() + 
      "ms)");
  }
  
  public final L1BossWeapon get(int id, int boss_lv) {
    int index = id * 100 + boss_lv;
    return (L1BossWeapon)_BossList.get(Integer.valueOf(index));
  }
  
  public final int BossWeaponMax()
  {
    int max = 0;
    for (Integer key : _BossList.keySet()) {
      L1BossWeapon bossWeapon = (L1BossWeapon)_BossList.get(key);
      if (bossWeapon.getItemId() > max) {
        max = bossWeapon.getItemId();
      }
    }
    return max;
  }

public L1BossWeapon get(int itemId) {
	// TODO Auto-generated method stub
	return null;
}
}
