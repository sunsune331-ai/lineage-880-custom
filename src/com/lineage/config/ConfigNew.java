package com.lineage.config;

import com.lineage.server.datatables.ConfigTable;

/**
 * 額外設置
 * 
 * @author dexc
 */
public final class ConfigNew {

	/** 是否開啟絕對還原設定(開新服專用) */
	public static boolean DBClearAll;
	/** 釣魚池地圖編號 */
	public static int FishMapId;

	private static final String CONFIG_NEW_FILE = "z_config_new.sql";

	public static void load() throws ConfigErrorException {
		// _log.info("載入服務器限制設置!");
		final ConfigTable set = ConfigTable.getInstance();
		try {

			// 是否開啟絕對還原設定(開新服專用)
			DBClearAll = Boolean.parseBoolean(set.getProperty("DBClearAll", "false"));

			// 釣魚池地圖編號
			FishMapId = Integer.parseInt(set.getProperty("FishMapId", "5490"));

		} catch (final Exception e) {
			throw new ConfigErrorException("設置檔案遺失: " + CONFIG_NEW_FILE);

		} finally {
			//set.clear();
		}
	}
}
