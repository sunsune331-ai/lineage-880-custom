package com.lineage.server.timecontroller.pc;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import com.lineage.server.world.WorldWizard;

public class UpdateObjectWTimer extends TimerTask {
	private static final Log _log = LogFactory.getLog(UpdateObjectWTimer.class);
	private ScheduledFuture<?> _timer;

	public void start() {
		int timeMillis = 350;
		_timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 350L, 350L);
	}

	public void run() {
		try {
			Collection allPc = WorldWizard.get().all();

			if (allPc.isEmpty()) {
				return;
			}

			for (Iterator iter = allPc.iterator(); iter.hasNext();) {
				L1PcInstance tgpc = (L1PcInstance) iter.next();
				if (UpdateObjectCheck.check(tgpc)) {
					tgpc.updateObject();
				}

			}

		} catch (Exception e) {
			_log.error("Pc 可見物更新處理時間軸(法師)異常重啟", e);
			PcOtherThreadPool.get().cancel(_timer, false);
			UpdateObjectWTimer objectWTimer = new UpdateObjectWTimer();
			objectWTimer.start();
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.timecontroller.pc.UpdateObjectWTimer JD-Core Version:
 * 0.6.2
 */