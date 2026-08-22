package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1LuckyLottery;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 潘朵拉幸運抽獎
 */
public class LuckyLotteryTable {

	public static final Log _log = LogFactory.getLog(LuckyLotteryTable.class);

	private static LuckyLotteryTable _instance;

	private final ArrayList<L1LuckyLottery> _list = new ArrayList<L1LuckyLottery>();

	public int total = 0;

	public static LuckyLotteryTable getInstance() {
		if (_instance == null) {
			_instance = new LuckyLotteryTable();
		}
		return _instance;
	}

	private LuckyLotteryTable() {
		loadData();
	}

	public static void reload() {
		final LuckyLotteryTable oldInstance = _instance;
		_instance = new LuckyLotteryTable();
		oldInstance._list.clear();
	}

	public void loadData() {
		final PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM pandora_luckylottery");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final L1LuckyLottery item = new L1LuckyLottery();

				// item.setItem_id(rs.getInt("item_id"));

				final int itemid = rs.getInt("item_id");
				if (ItemTable.get().getTemplate(itemid) == null) {
					_log.error("潘朵拉抽抽樂資料錯誤: 沒有這個編號的道具:" + itemid);
					continue;
				}
				item.setItem_id(itemid);

				item.setCount(rs.getInt("count"));
				item.setEnchantlvl(rs.getInt("enchantlvl"));
				item.setIsbroad(rs.getInt("isbroad") == 1);
				final int value = rs.getInt("random");
				item.setRandom(this.total + 1);
				item.setTotal(this.total += value);
				this._list.add(item);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.info("載入潘朵拉抽抽樂數量: " + _list.size() + "(" + timer.get() + "ms)");
	}

	public ArrayList<L1LuckyLottery> getList() {
		return this._list;
	}
}
