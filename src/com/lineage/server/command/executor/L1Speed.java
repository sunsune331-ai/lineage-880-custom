package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;

/**
 * 賦予GM加速狀態
 * @author dexc
 */
public class L1Speed implements L1CommandExecutor {

	private static final Log _log = LogFactory.getLog(L1Speed.class);

	private L1Speed() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Speed();
	}

	@Override
	public void execute(final L1PcInstance pc, final String cmdName, final String arg) {
		try {
			L1BuffUtil.haste(pc, 3600 * 1000);

			// L1BuffUtil.brave(pc, 3600 * 1000);
			L1BuffUtil.superbrave(pc, 3600 * 1000); // 荒神加速效果

			// final StringTokenizer st = new StringTokenizer(arg);
			// final int mr = Integer.parseInt(st.nextToken());
			//
			// int magicHit = 0;
			// try {
			// magicHit = Integer.parseInt(st.nextToken());
			// } catch (final Exception e) {
			// magicHit = 1;
			// }
			//
			// double mrFloor = 0;
			//
			// if (mr < 100) {
			// mrFloor = Math.floor((mr - magicHit) / 2);
			// } else if (mr >= 100) {
			// mrFloor = Math.floor((mr - magicHit) / 10);
			// }
			// double mrCoefficient = 0;
			// if (mr < 100) {
			// mrCoefficient = 1 - 0.01 * mrFloor;
			// } else if (mr >= 100) {
			// mrCoefficient = 0.6 - 0.01 * mrFloor;
			// }
			//
			// pc.sendPackets(new
			// S_ServerMessage("mrCoefficient="+mrCoefficient));

		} catch (final Exception e) {
			_log.error("錯誤的GM指令格式: " + this.getClass().getSimpleName() + " 執行的GM:" + pc.getName());
			// 261 \f1指令錯誤。
			pc.sendPackets(new S_ServerMessage(261));
		}
	}
}
