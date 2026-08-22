package com.lineage.server.timecontroller.pc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.WorldIllusionist;

public class MprTimerIllusionist extends TimerTask {
	private static final Log _log = LogFactory.getLog(MprTimerIllusionist.class);
	private ScheduledFuture<?> _timer;

	public void start() {
		int timeMillis = 1000;
		_timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 1000L, 1000L);
	}

	public void run() {
		try {
			Collection allPc = WorldIllusionist.get().all();

			if (allPc.isEmpty()) {
				return;
			}

			for (Iterator iter = allPc.iterator(); iter.hasNext();) {
				L1PcInstance tgpc = (L1PcInstance) iter.next();
				MprExecutor mpr = MprExecutor.get();
				if (mpr.check(tgpc)) {
					mpr.checkRegenMp(tgpc);
					Thread.sleep(1L);
				}

			}

		} catch (Exception e) {
			_log.error("Pc(幻術) MP自然回復時間軸異常重啟", e);
			PcOtherThreadPool.get().cancel(_timer, false);
			MprTimerIllusionist mprIllusionist = new MprTimerIllusionist();
			mprIllusionist.start();
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.timecontroller.pc.MprTimerIllusionist JD-Core Version:
 * 0.6.2
 */