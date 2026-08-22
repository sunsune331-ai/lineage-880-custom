package com.lineage.server.timecontroller.server;

import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.LightSpawnTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

/**
 * 照明物件召喚時間軸
 * @author dexc
 */
public class ServerLightTimer extends TimerTask {

	private static final Log _log = LogFactory.getLog(ServerLightTimer.class);

	private ScheduledFuture<?> _timer;

	private static boolean isSpawn = false;

	public void start() {
		final int timeMillis = 60 * 1000; // 1分鐘
		_timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis, timeMillis);
	}

	public void run() {
		try {
			checkLightTime();

			// NpcSpawnTable.get().checkMaps();

		} catch (final Exception e) {
			_log.error("照明物件召喚時間軸異常重啟", e);
			GeneralThreadPool.get().cancel(_timer, false);
			final ServerLightTimer lightTimer = new ServerLightTimer();
			lightTimer.start();
		}
	}

	private static void checkLightTime() {
		try {
			final int serverTime = L1GameTimeClock.getInstance().currentTime().getSeconds();

			final int nowTime = serverTime % 86400;

			if ((nowTime >= 21300) && (nowTime < 64500)) {
				if (isSpawn) {
					isSpawn = false;
					for (final L1Object object : World.get().getObject()) {
						if ((object instanceof L1FieldObjectInstance)) {
							final L1FieldObjectInstance npc = (L1FieldObjectInstance) object;
							if (((npc.getNpcTemplate().get_npcId() == 81177)
									|| (npc.getNpcTemplate().get_npcId() == 81178)
									|| (npc.getNpcTemplate().get_npcId() == 81179)
									|| (npc.getNpcTemplate().get_npcId() == 81180)
									|| (npc.getNpcTemplate().get_npcId() == 81181))
									&& ((npc.getMapId() == 0) || (npc.getMapId() == 4))) {
								npc.deleteMe();
							}
						}
					}
				}

				// 召喚照明
			} else if (((nowTime >= 64500) && (nowTime <= 86400))
					|| ((nowTime >= 0) && (nowTime < 21300) && (!isSpawn))) {
				isSpawn = true;
				LightSpawnTable.getInstance();
			}
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
}
