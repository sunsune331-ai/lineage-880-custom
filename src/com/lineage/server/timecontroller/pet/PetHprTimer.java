package com.lineage.server.timecontroller.pet;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldPet;

public class PetHprTimer extends TimerTask {
	private static final Log _log = LogFactory.getLog(PetHprTimer.class);
	private ScheduledFuture<?> _timer;
	private static int _time = 0;

	public void start() {
		_time = 0;
		int timeMillis = 1000;
		_timer = GeneralThreadPool.get().scheduleAtFixedRate(this, 1000L, 1000L);
	}

	public void run() {
		try {
			_time += 1;

			Collection allPet = WorldPet.get().all();

			if (allPet.isEmpty()) {
				return;
			}

			for (Iterator iter = allPet.iterator(); iter.hasNext();) {
				L1PetInstance pet = (L1PetInstance) iter.next();
				if (HprPet.hpUpdate(pet, _time))
					Thread.sleep(5L);
			}
		} catch (Exception e) {
			_log.error("Pet HP自然回復時間軸異常重啟", e);
			GeneralThreadPool.get().cancel(_timer, false);
			PetHprTimer petHprTimer = new PetHprTimer();
			petHprTimer.start();
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.timecontroller.pet.PetHprTimer JD-Core Version: 0.6.2
 */