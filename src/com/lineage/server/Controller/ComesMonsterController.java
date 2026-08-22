package com.lineage.server.Controller;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import william.ComesMonsterSpawn;

/**
 * 指定時間召怪召物系統
 */
public class ComesMonsterController implements Runnable {

	public static final Log _log = LogFactory.getLog(ComesMonsterController.class);

    private static final Map<Integer, Object[]> map = new ConcurrentHashMap<Integer, Object[]>();

    private static final List<Integer> task = new ArrayList<Integer>();

    private static ComesMonsterController ins;

    public static ComesMonsterController getInstance() {
        if (ins == null) {
            ins = new ComesMonsterController();
        }
        return ins;
    }
    
    private ComesMonsterController() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            ps = con.prepareStatement("SELECT * FROM william_Comes_monster");
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] object = new Object[14];

                object[0] = rs.getInt("type");
                object[1] = rs.getInt("objid");
                object[2] = rs.getInt("count");
                object[3] = rs.getInt("mapId");
                object[4] = rs.getInt("x");
                object[5] = rs.getInt("y");
                object[6] = rs.getInt("x_end");
                object[7] = rs.getInt("y_end");
                object[8] = getArray(rs.getString("Day"));
                object[9] = rs.getTime("Time");
                object[10] = rs.getString("Message");
                object[11] = rs.getInt("SpawnGfxId");
                object[12] = rs.getInt("DeleteTime");
                object[13] = rs.getInt("CycleTime");

                map.put(rs.getInt("id"), object);
            }
        } catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs, ps, con);
        }
    }
    
    @Override
    public void run() {

        final Calendar cal = Calendar.getInstance();

        while (true)
        {
            try {
                Thread.sleep(1000);
                cal.setTimeInMillis(System.currentTimeMillis());
                final int day = cal.get(Calendar.DAY_OF_WEEK) - 1;

                for (final Integer i : map.keySet()) {

                    boolean to = false;
                    final Object[] obj = map.get(i);
                    for (int d : (int[])obj[8]) {

                        if (d == 7 || d == day) {
                            to = true;
                            break;
                        }
                    }

                    if (!to) {
                        continue;
                    }

                    if (!task.contains(i)) {
                        new ComesMonsterSpawn(obj);
                        task.add(i);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final int[] getArray(String args) {
        final StringTokenizer st = new StringTokenizer(args, ",");
        final int[] sk = new int[st.countTokens()];

        for (int i = 0; i < sk.length; i++) {
            sk[i] = Integer.parseInt(st.nextToken());
        }
        return sk;
    }

}
