package com.lineage.server.timecontroller.npc;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldMob;

/**
 * NPC死亡時間軸
 * 
 * @author L1jJP,Dexc,Tom
 *
 */
public class NpcDeadTimer extends TimerTask {

	private static final Log _log = LogFactory.getLog(NpcDeadTimer.class);

	private ScheduledFuture<?> _timer;

	public static final Map<L1NpcInstance, Integer> MAP = new ConcurrentHashMap<L1NpcInstance, Integer>();

	private int _time = 0;

	public void start() {
		final int timeMillis = 1000;
		_timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis, timeMillis);
	}

	@Override
	public void run() {
		try {
			this._time++;
			// 不包含元素
			if (MAP.isEmpty()) {
				return;
			}
			for (final L1NpcInstance npc : MAP.keySet()) {
				Integer time = MAP.get(npc);
				time--;

				// System.out.println("tiaoshi1:" + time);
				// System.out.println("tiaoshi2:" + npc.getNpcId());

				if (time > 0) {
					MAP.put(npc, time);

				} else {
					MAP.remove(npc);
					if (npc.isDead()) {
						if (!npc.destroyed()) {
							npc.deleteMe();
						}
					}
				}
			}

			if (this._time >= 1800) {// 30分鐘
				checkAllNpc();
				this._time = 0;
			}

		} catch (final Exception e) {
			_log.error("NPC死亡時間軸異常重啟");
			GeneralThreadPool.get().cancel(_timer, false);
			final NpcDeadTimer tTNDeath = new NpcDeadTimer();
			tTNDeath.start();
		}
	}

	/**
	 * 重新檢查全服務器死亡NPC
	 */
	private static void checkAllNpc() {
		final Collection<L1MonsterInstance> allMob = WorldMob.get().all();
		// 不包含元素
		if (allMob.isEmpty()) {
			return;
		}

		for (final L1MonsterInstance npc : allMob) {
			// 未死亡(復活可能)
			if (!npc.isDead()) {
				continue;
			}
			npc.deleteMe();
		}

		final SimpleDateFormat sdFormat = new SimpleDateFormat("MM/dd hh:mm:ss");
		final Date date = new Date();
		final String strDate = sdFormat.format(date);
		_log.info("死亡NPC整體檢查完成" + strDate);
	}
}
