package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1QuestNewSet;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 官服任務系統
 */
public class QuestNewSetTable {

	private static final Log _log = LogFactory.getLog(QuestNewSetTable.class);

	private final HashMap<Integer, L1QuestNewSet> _list = new HashMap<>();

	private static QuestNewSetTable _instance;

	public static QuestNewSetTable getInstance() {
		if (_instance == null) {
			_instance = new QuestNewSetTable();
		}
		return _instance;
	}

	private QuestNewSetTable() {
		PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT *FROM 官服任務系統");
			rs = pstm.executeQuery();
			while (rs.next()) {

				final L1QuestNewSet data = new L1QuestNewSet();

				data.questid = rs.getInt("questid");
				data.isOpen = rs.getBoolean("是否啟用");
				if (!data.isOpen) {
					continue;
				}
				data.minQuestLevel = rs.getInt("最低等級");
				data.maxQuestLevel = rs.getInt("最高等級");
				data.mapid = rs.getInt("任務地圖");
				data.teleportLoc = getArrayInt(rs.getString("傳送座標"));
				data.rewardItemid = getArrayInt(rs.getString("固定獎勵編號"));
				data.rewardItemCount = getArrayInt(rs.getString("固定獎勵數量"));
				data.rewardItemEnchant = getArrayInt(rs.getString("固定獎勵加成"));
				data.rewardSelectItemid = getArrayInt(rs.getString("選擇獎勵編號"));
				data.rewardSelectItemCount = getArrayInt(rs.getString("選擇獎勵數量"));
				data.rewardSelectItemEnchant = getArrayInt(rs.getString("選擇獎勵加成"));
				data.rewardExp = rs.getInt("獎勵經驗");
				data.requireClassType = rs.getString("判斷職業");
				data.達到等級 = rs.getInt("達到等級");
				data.isRecoverRequireItem = rs.getBoolean("是否刪除收集道具");
				data.獵殺怪物編號 = getArrayInt(rs.getString("獵殺怪物編號"));
				data.獵殺怪物數量 = getArrayInt(rs.getString("獵殺怪物數量"));
				data.獲得道具編號 = getArrayInt(rs.getString("收集道具編號"));
				data.獲得道具數量 = getArrayInt(rs.getString("收集道具數量"));
				data.獲得道具加成 = getArrayInt(rs.getString("收集道具加成"));
				data.使用道具編號 = getArrayInt(rs.getString("使用道具編號"));
				data.使用道具數量 = getArrayInt(rs.getString("使用道具數量"));

				_list.put(data.questid, data);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
        }
		_log.info("載入官服任務系統設置數量: " + _list.size() + "(" + timer.get() + "ms)");
	}

	private static int[] getArrayInt(final String s) {
		if (s == null || s.isEmpty() || s.equals("")) {
			return null;
		}
		final StringTokenizer st = new StringTokenizer(s, ",");
		final int iSize = st.countTokens();
		String sTemp = null;

		final int[] iReturn = new int[iSize];
		for (int i = 0; i < iSize; i++) {
			sTemp = st.nextToken();
			iReturn[i] = Integer.parseInt(sTemp);
		}
		return iReturn;
	}

	public L1QuestNewSet getTemplate(final int questid) {
		return _list.get(questid);
	}

	public HashMap<Integer, L1QuestNewSet> getAllList() {
		return _list;
	}
}
