package com.lineage.server.timecontroller.npc;

import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.NpcSpawnTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldNpc;

public class NpcExistTimer extends TimerTask {

	private static final Log _log = LogFactory.getLog(NpcExistTimer.class);

	private ScheduledFuture<?> _timer;

	public void start() {
		// final int timeMillis = 10 * 60 * 1000;// 10分鐘
		final int timeMillis = 1 * 60 * 1000;// 1分鐘
		_timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis, timeMillis);
	}

	public void run() {
		try {
			// Map<Integer, L1Spawn> BossSpawn = SpawnBossReading.get().get_bossSpawnTable();
			//
			// for (L1Spawn spawn : BossSpawn.values()) {
			// if (spawn.get_existTime() > 0) {// 具有BOSS存在時間限制(分)
			// long existTime = spawn.get_existTime() * 60 * 1000;// 存在時間限制轉換成毫秒
			// long spawnTime = spawn.get_nextSpawnTime().getTimeInMillis();// 出生時間轉換成毫秒
			// long nowTime = System.currentTimeMillis();// 現在時間
			//
			// L1NpcInstance npc = spawn.getNpcTemp();
			// if (existTime + spawnTime < nowTime) {// 已超過存在時間限制
			// npc.deleteMe();
			// }
			// }
			// Thread.sleep(50L);
			// }

			final Collection<L1NpcInstance> allNpc = WorldNpc.get().all();
			// 不包含元素
			if (allNpc.isEmpty()) {
				return;
			}

			for (final L1NpcInstance npc : allNpc) {
				// 不具有刪除時間
				if (!npc.isNpcDeleteTime()) {
					continue;
				}
				NpcSpawnTable.get().checkMaps(npc);
			}

		} catch (final Exception e) {
			// _log.error("BOSS存在時間限制時間軸異常重啟", e);
			_log.error("NPC刪除時間軸異常重啟", e);
			GeneralThreadPool.get().cancel(_timer, false);
			final NpcExistTimer npcexistTimer = new NpcExistTimer();
			npcexistTimer.start();
		}
	}

}
