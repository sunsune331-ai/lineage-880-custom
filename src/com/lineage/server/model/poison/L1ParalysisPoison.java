package com.lineage.server.model.poison;

import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_PARALYZED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_PARALYZING;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.ModelError;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SkillIconPoison;
import com.lineage.server.thread.GeneralThreadPool;

/**
 * 麻痺型中毒
 */
public class L1ParalysisPoison extends L1Poison {

	private static final Log _log = LogFactory.getLog(L1ParalysisPoison.class);

	// 麻痺毒性能一覽 猶予 持續 (參考值、未適用)
	//  20 45
	//  10 60
	// 蟻穴 14 30
	// D- 39 45

	private final L1Character _target;

	private Thread _timer;

	private final int _delay;

	private final int _time;

	private int _effectId = 1;

	private L1ParalysisPoison(L1Character cha, int delay, int time) {
		_target = cha;
		_delay = delay;
		_time = time;

		doInfection();
	}

	private class ParalysisPoisonTimer extends Thread {
		@Override
		public void run() {
			_target.setSkillEffect(STATUS_POISON_PARALYZING, 0);

			try {
				Thread.sleep(_delay); // 麻痺猶予時間待。

			} catch (InterruptedException e) {
				ModelError.isError(_log, e.getLocalizedMessage(), e);
				return;
			}

			// 綠色改灰色
			_effectId = 2;
			_target.setPoisonEffect(2);

			if (_target instanceof L1PcInstance) {
				L1PcInstance player = (L1PcInstance) _target;
				if (!player.isDead()) {
					player.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, true, _time)); // 麻痺狀態
					_timer = new ParalysisTimer();
					GeneralThreadPool.get().execute(_timer); // 麻痺計時開始
					if (isInterrupted()) { // XXX
						_timer.interrupt();
					}
				}
			}
		}
	}

	private class ParalysisTimer extends Thread {
		@Override
		public void run() {
			_target.killSkillEffectTimer(STATUS_POISON_PARALYZING);
			_target.setSkillEffect(STATUS_POISON_PARALYZED, 0);

			try {
				if (_target instanceof L1PcInstance) {
					// 被麻痺了圖標
					L1PcInstance _pc = (L1PcInstance) _target;
					_pc.sendPackets(new S_SkillIconPoison(2, _time / 1000));
				}
				Thread.sleep(_time);

			} catch (InterruptedException e) {
				ModelError.isError(_log, e.getLocalizedMessage(), e);
			}

			_target.killSkillEffectTimer(STATUS_POISON_PARALYZED);
			if (_target instanceof L1PcInstance) {
				L1PcInstance player = (L1PcInstance) _target;
				if (!player.isDead()) {
					player.sendPackets(new S_Paralysis(S_Paralysis.TYPE_PARALYSIS, false, 0)); // 麻痺狀態解除
					cure(); // 解毒處理
				}
			}
		}
	}

	public static boolean doInfection(L1Character cha, int delay, int time) {
		if (!L1Poison.isValidTarget(cha)) {
			return false;
		}

		cha.setPoison(new L1ParalysisPoison(cha, delay, time));
		return true;
	}

	private void doInfection() {
		sendMessageIfPlayer(_target, 212); // \f1你的身體漸漸麻痺。
		_target.setPoisonEffect(1);

		if (_target instanceof L1PcInstance) {
			_timer = new ParalysisPoisonTimer();
			GeneralThreadPool.get().execute(_timer);

			// 要被麻痺了前置時間圖標
			L1PcInstance _pc = (L1PcInstance) _target;
			//_pc.sendPackets(new S_SkillIconPoison(2, _time / 1000));
			_pc.sendPackets(new S_SkillIconPoison(2, _delay / 1000));
		}
	}

	@Override
	public int getEffectId() {
		return _effectId;
	}

	@Override
	public void cure() {
		_target.setPoisonEffect(0);
		_target.setPoison(null);

		if (_target instanceof L1PcInstance) {
			L1PcInstance _pc = (L1PcInstance) _target;
			_pc.sendPackets(new S_SkillIconPoison(0, 0));  // 解除毒圖標
		}

		if (_timer != null) { // XXX
			_timer.interrupt(); // 麻痺毒解除
		}
	}
}
