package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_PARALYZED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_PARALYZING;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 詛咒型麻痺
 */
public class L1CurseParalysis extends L1Paralysis {

	private static final Log _log = LogFactory.getLog(L1CurseParalysis.class);

	private final L1Character _target;

	private final int _delay;

	private final int _time;

	private Thread _timer;

	private class ParalysisDelayTimer extends Thread {
		@Override
		public void run() {
			_target.setSkillEffect(STATUS_CURSE_PARALYZING, 0);

			try {
				Thread.sleep(_delay); // 麻痺猶予時間待。
			} catch (InterruptedException e) {
				_target.killSkillEffectTimer(STATUS_CURSE_PARALYZING);

				ModelError.isError(_log, e.getLocalizedMessage(), e);
				return;
			}

			if (_target instanceof L1PcInstance) {
				L1PcInstance player = (L1PcInstance) _target;
				if (!player.isDead()) {
					player.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, true, (_time / 1000))); // 麻痺狀態
				}
			}
			_target.setParalyzed(true);
			_timer = new ParalysisTimer();
			GeneralThreadPool.get().execute(_timer); // 麻痺計時開始
			if (isInterrupted()) { // XXX
				_timer.interrupt();
			}
		}
	}

	private class ParalysisTimer extends Thread {
		@Override
		public void run() {
			_target.killSkillEffectTimer(STATUS_CURSE_PARALYZING);
			_target.setSkillEffect(STATUS_CURSE_PARALYZED, 0);

			try {
				Thread.sleep(_time);
			} catch (InterruptedException e) {
				ModelError.isError(_log, e.getLocalizedMessage(), e);
			}

			_target.killSkillEffectTimer(STATUS_CURSE_PARALYZED);
			if (_target instanceof L1PcInstance) {
				L1PcInstance player = (L1PcInstance) _target;
				if (!player.isDead()) {
					player.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, false, 0)); // 麻痺狀態解除
				}
			}
			_target.setParalyzed(false);
			cure(); // 解咒處理
		}
	}

	/**
	 * 魔法效果:麻痺
	 * @param cha 對像
	 * @param delay 延遲時間(毫秒)
	 * @param time 麻痺時間(毫秒)
	 */
	private L1CurseParalysis(L1Character cha, int delay, int time, int mode) {
		_target = cha;
		_delay = delay;
		_time = time;

		curse(mode);
	}

	private void curse(int mode) {
		if (_target instanceof L1PcInstance) {
			L1PcInstance player = (L1PcInstance) _target;
			switch (mode) {
			case 1:
				// 212 \f1你的身體漸漸麻痺。
				player.sendPackets(new S_ServerMessage(212));
				break;

			case 2:
				// 291 \f1你的身體正在迅速痲痺。
				player.sendPackets(new S_ServerMessage(291));
				break;
			}
		}

		_target.setPoisonEffect(2);

		_timer = new ParalysisDelayTimer();
		GeneralThreadPool.get().execute(_timer);
	}

	/**
	 * 魔法效果:麻痺
	 * @param cha 對像
	 * @param delay 延遲時間(毫秒)
	 * @param time 麻痺時間(毫秒)
	 * @param mode 1:你的身體漸漸麻痺。 2:你的身體正在迅速痲痺。
	 * @return
	 */
	public static boolean curse(L1Character cha, int delay, int time, int mode) {
		if (!((cha instanceof L1PcInstance) || (cha instanceof L1MonsterInstance))) {
			return false;
		}
		if (cha.hasSkillEffect(STATUS_CURSE_PARALYZING)
				|| cha.hasSkillEffect(STATUS_CURSE_PARALYZED)) {
			return false; // 既麻痺
		}

		cha.setParalaysis(new L1CurseParalysis(cha, delay, time, mode));
		return true;
	}

	@Override
	public int getEffectId() {
		return 2;
	}

	@Override
	public void cure() {
		_target.setPoisonEffect(0);
		_target.setParalaysis(null);

		if (_timer != null) { // XXX
			_timer.interrupt();
		}
	}

}
