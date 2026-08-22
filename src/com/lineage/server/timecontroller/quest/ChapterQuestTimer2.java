package com.lineage.server.timecontroller.quest;

import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldQuest;

public class ChapterQuestTimer2 extends TimerTask {

	private static final Log _log = LogFactory.getLog(ChapterQuestTimer2.class);

	private ScheduledFuture<?> _timer;

	public void start() {
		final int timeMillis = 1000;
		_timer = GeneralThreadPool.get().scheduleAtFixedRate(this, timeMillis, timeMillis);
	}

	public void run() {
		try {
			final Collection<L1QuestUser> allQuest = WorldQuest.get().all();
			if (allQuest.isEmpty()) {
				return;
			}

			for (final L1QuestUser quest : allQuest) {
				if (quest.get_orimR() != null) {
					final L1PcInstance leader = quest.get_orimR().party.getLeader();
					if ((leader.getX() == 32799) && (leader.getY() == 32808)) {
						if (leader.get_actionId() == 66) {
							quest.get_orimR().attack();
							leader.set_actionId(0);
						} else if (leader.get_actionId() == 69) {
							quest.get_orimR().defense();
							leader.set_actionId(0);
						}
					} else if ((leader.get_actionId() == 66) || (leader.get_actionId() == 69)) {
						leader.set_actionId(0);
					}
					if (quest.get_orimR().portal != null) {
						for (final L1PcInstance member : quest.pcList()) {
							quest.get_orimR().teleport(member, quest.get_orimR().getCabinLocation());
						}
					}
					quest.get_orimR().calcScore();

					Thread.sleep(1L);
				}
			}

		} catch (final Exception e) {
			_log.error("副本任務檢查時間軸<海戰副本>異常重啟", e);
			GeneralThreadPool.get().cancel(_timer, false);
			final ChapterQuestTimer2 questTimer = new ChapterQuestTimer2();
			questTimer.start();
		}
	}
}
