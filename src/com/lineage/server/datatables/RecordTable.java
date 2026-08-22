package com.lineage.server.datatables;

import com.lineage.server.utils.L1QueryUtil;

/**
 * 各項記錄
 */
public class RecordTable {

	private static RecordTable _instance;

	public static RecordTable get() {
		if (_instance == null) {
			_instance = new RecordTable();
		}
		return _instance;
	}

	// 神熾贊助送禮系統
	public void recordeSponsorItem(final String accName, final String pcName, final int sponsor, final String ip) {
		final String sql = "INSERT INTO 贊助_滿額禮_紀錄 (帳號,玩家,滿額,IP,時間) VALUE (?, ?, ?, ?, SYSDATE())";
		L1QueryUtil.execute(sql, accName, pcName, sponsor, ip);
	}

}
