package com.lineage.server.model.poison;

import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_SILENCE;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillIconPoison;

/**
 * 沉默型中毒
 */
public class L1SilencePoison extends L1Poison {

	private final L1Character _target;

	public static boolean doInfection(L1Character cha) {
		if (!L1Poison.isValidTarget(cha)) {
			return false;
		}

		cha.setPoison(new L1SilencePoison(cha));
		return true;
	}

	private L1SilencePoison(L1Character cha) {
		_target = cha;

		doInfection();
	}

	private void doInfection() {
		_target.setPoisonEffect(1);
		sendMessageIfPlayer(_target, 310);

		int time = 300; // 300

		_target.setSkillEffect(STATUS_POISON_SILENCE, time * 1000);
		if (_target instanceof L1PcInstance) {
			L1PcInstance _pc = (L1PcInstance) _target;
			_pc.sendPackets(new S_SkillIconPoison(6, time));
		}
	}

	public int getEffectId() {
		return 1;
	}

	public void cure() {
		_target.setPoisonEffect(0);
		sendMessageIfPlayer(_target, 311);

		_target.killSkillEffectTimer(STATUS_POISON_SILENCE);
		_target.setPoison(null);
		if (_target instanceof L1PcInstance) {
			L1PcInstance _pc = (L1PcInstance) _target;
			_pc.sendPackets(new S_SkillIconPoison(0, 0)); // 解除毒圖標
		}
	}
}
