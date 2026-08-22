package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ExpMeteUpTable
{
  private static final Log _log = LogFactory.getLog(ExpMeteUpTable.class);
  
  private static ExpMeteUpTable _instance;
  
  private static final Map<Integer, Double> _expmeteupList = new HashMap<Integer, Double>();
  
  public static ExpMeteUpTable get() {
    if (_instance == null) {
      _instance = new ExpMeteUpTable();
    }
    return _instance;
  }
  
  public void load() {
    Connection con = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    try {
      con = DatabaseFactory.get().getConnection();
      pstm = con.prepareStatement("SELECT * FROM `exp_meteup`");
      rs = pstm.executeQuery();
      while (rs.next()) {
        int level = rs.getInt("meteup");
        double expPenalty = rs.getDouble("expPenalty");
        
        _expmeteupList.put(new Integer(level), new Double(expPenalty));
      }
      
      _log.info("轉生經驗減少->" + _expmeteupList.size());
    }
    catch (SQLException e) {
      _log.error(e.getLocalizedMessage(), e);
    }
    finally {
      SQLUtil.close(rs);
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }
  
  public double getRate(int meteup)
  {
    double expPenalty = 1.0D;
    if (_expmeteupList.isEmpty()) {
      return expPenalty;
    }
    
    if (_expmeteupList.containsKey(Integer.valueOf(meteup))) {
      return ((Double)_expmeteupList.get(Integer.valueOf(meteup))).doubleValue();
    }
    
    return expPenalty;
  }
}
