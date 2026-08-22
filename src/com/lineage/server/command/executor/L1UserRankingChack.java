package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.Controller.UserRankingController;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 新排行系統
 */
public class L1UserRankingChack implements L1CommandExecutor {

	private static final Log _log = LogFactory.getLog(L1UserRankingChack.class);

	private L1UserRankingChack() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1UserRankingChack();
	}

	@Override
	public void execute(final L1PcInstance pc, final String cmdName, final String arg) {
		try {

			UserRankingController.isRenewal = true;
			pc.sendPackets(new S_SystemMessage("排行榜更新完畢。"));

		} catch (final Exception e) {
			_log.error("錯誤的GM指令格式: " + this.getClass().getSimpleName() + " 執行的GM:" + pc.getName());
			// 261 \f1指令錯誤。
			pc.sendPackets(new S_ServerMessage(261));
		}
	}
}
