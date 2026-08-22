package william;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 即時獎勵系統
 */
public class LimitedReward {

	private static Logger _log = Logger.getLogger(LimitedReward.class.getName());

	private static final Log _logx = LogFactory.getLog(LimitedReward.class.getName());

	private static LimitedReward _instance;

	public static final HashMap<Integer, L1WilliamLimitedReward> _itemIdIndex = new HashMap<Integer, L1WilliamLimitedReward>();

	public static final HashMap<Integer, L1WilliamLimitedReward> _itemIdIndex1 = new HashMap<Integer, L1WilliamLimitedReward>();

	public static LimitedReward getInstance() {
		if (_instance == null) {
			_instance = new LimitedReward();
		}
		return _instance;
	}

	public static void reload() {
		@SuppressWarnings("unused")
		final LimitedReward oldInstance = _instance;
		_instance = new LimitedReward();
		_itemIdIndex.clear();
	}

	private LimitedReward() {
		loadChackSerial();
	}

	private static void loadChackSerial() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM william_limitedreward");
			rs = pstm.executeQuery();
			fillChackSerial(rs);
		} catch (final SQLException e) {
			_log.log(Level.SEVERE, "error while creating william_limitedreward table", e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private static void fillChackSerial(final ResultSet rs) throws SQLException {
		final PerformanceTimer timer = new PerformanceTimer();
		while (rs.next()) {
			final int id = rs.getInt("id");
			final boolean activity = rs.getInt("activity") == 1;
			final int check_classId = rs.getInt("check_classId");
			final int check_level = rs.getInt("check_level");
			final int check_item = rs.getInt("check_item");
			final int check_itemCount = rs.getInt("check_itemCount");
			final String surplus_msg = rs.getString("surplus_msg");
			final int surplus_msg_color = rs.getInt("surplus_msg_color");
			final String itemId = rs.getString("give_item_id");
			final String count = rs.getString("give_itemCount");
			final String enchantlvl = rs.getString("give_itemEnchantlvl");
			final int totalCount = rs.getInt("total_People");
			final int appearCount = rs.getInt("appear_People");
			final int quest_id = rs.getInt("quest_id");
			final int quest_step = rs.getInt("quest_step");
			final String message = rs.getString("message");
			final String message_end = rs.getString("message_end");

			final L1WilliamLimitedReward armor_upgrade = new L1WilliamLimitedReward(id, activity, check_classId, check_level,
					check_item, check_itemCount, surplus_msg, surplus_msg_color, itemId, count, enchantlvl, totalCount,
					appearCount, quest_id, quest_step, message, message_end);

			_itemIdIndex.put(id, armor_upgrade);
			_itemIdIndex1.put(check_item, armor_upgrade);
		}
		_logx.info("載入即時獎勵系統數據資料數量: " + _itemIdIndex.size() + "(" + timer.get() + "ms)");
	}

	public L1WilliamLimitedReward[] getLimitedRewardList() {
		return _itemIdIndex.values().toArray(new L1WilliamLimitedReward[_itemIdIndex.size()]);
	}

	public L1WilliamLimitedReward getTemplate(final int id) {
		return _itemIdIndex.get(id);
	}

	public L1WilliamLimitedReward getTemplate1(final int check_item) {
		return _itemIdIndex1.get(check_item);
	}

	public static void limitedRewardToList(final int id, final long appearCount) {
		Connection cn = null;
		PreparedStatement ps = null;
		try {
			cn = DatabaseFactory.get().getConnection();
			ps = cn.prepareStatement("UPDATE william_limitedreward SET appear_People=? WHERE id=?");
			ps.setLong(1, appearCount);
			ps.setInt(2, id);
			ps.execute();
		} catch (final SQLException e) {
			e.getLocalizedMessage();

		} finally {
			SQLUtil.close(ps);
			SQLUtil.close(cn);
		}
	}

	public static void limitedRewardToList_isOver(final int id, final int activity) {
		Connection cn = null;
		PreparedStatement ps = null;
		try {
			cn = DatabaseFactory.get().getConnection();
			ps = cn.prepareStatement("UPDATE william_limitedreward SET activity=? WHERE id=?");
			ps.setInt(1, activity);
			ps.setInt(2, id);
			ps.execute();
		} catch (final SQLException e) {
			e.getLocalizedMessage();

		} finally {
			SQLUtil.close(ps);
			SQLUtil.close(cn);
		}
	}
}
