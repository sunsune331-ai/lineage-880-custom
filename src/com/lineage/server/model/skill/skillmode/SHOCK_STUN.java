package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.SHOCK_STUN;

import java.util.Random;

import com.lineage.config.ConfigSkill;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1GuardInstance;
import com.lineage.server.model.Instance.L1GuardianInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 衝擊之暈
 */
public class SHOCK_STUN extends SkillMode {

	public SHOCK_STUN() {
	}

	@Override
	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;

		Random random = new Random();
		//int shock = random.nextInt(3) + 2;
		String[] sec = ConfigSkill.STUN_SEC.split("~");
		int shock = random.nextInt(Integer.valueOf(sec[1]).intValue()
				- Integer.valueOf(sec[0]).intValue() + 1)
				+ Integer.valueOf(sec[0]).intValue();

		// 取回目標是否已被施展沖暈
		if (((cha instanceof L1PcInstance)) && (cha.hasSkillEffect(SHOCK_STUN))) {
			shock += cha.getSkillEffectTimeSec(SHOCK_STUN);// 累計時間
		}
		if (srcpc.isKnight() && srcpc.getMeteLevel() >= 2) { // SRC0808
			shock += ConfigSkill.K2;
		}

		if (shock > 6) {// 最大沖暈時間6秒
			shock = 6;
		}

		cha.setSkillEffect(SHOCK_STUN, shock * 1000);

		// L1SpawnUtil.spawnEffect(81162, shock, cha.getX(), cha.getY(), srcpc.getMapId(), srcpc, 0);
		L1SpawnUtil.spawnEffect(81162, shock, cha.getX(), cha.getY(), cha.getMapId(), cha, 0);

		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));

		} else if (((cha instanceof L1MonsterInstance))
				|| ((cha instanceof L1SummonInstance))
				|| ((cha instanceof L1PetInstance))) {
			L1NpcInstance tgnpc = (L1NpcInstance) cha;
			tgnpc.setParalyzed(true);
		}

		return dmg;
	}

	@Override
	public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;

		Random random = new Random();
		String[] sec = ConfigSkill.STUN_SEC.split("~");
		int shock = random.nextInt(Integer.valueOf(sec[1]).intValue() - Integer.valueOf(sec[0]).intValue() + 1)
				+ Integer.valueOf(sec[0]).intValue();

		// 取回目標是否已被施展沖暈
		if ((cha instanceof L1PcInstance) && cha.hasSkillEffect(SHOCK_STUN)) {
			shock += cha.getSkillEffectTimeSec(SHOCK_STUN);// 累計時間
		}

		if (shock > 8) {// 最大沖暈時間8秒
			shock = 8;
		}

		cha.setSkillEffect(SHOCK_STUN, shock * 1000);

		// L1SpawnUtil.spawnEffect(81162, shock, cha.getX(), cha.getY(), npc.getMapId(), npc, 0);
		L1SpawnUtil.spawnEffect(81162, shock, cha.getX(), cha.getY(), cha.getMapId(), cha, 0);

		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));

		} else if (((cha instanceof L1MonsterInstance))
				|| ((cha instanceof L1SummonInstance))
				|| ((cha instanceof L1GuardianInstance))
				|| ((cha instanceof L1GuardInstance))
				|| ((cha instanceof L1PetInstance))) {
			L1NpcInstance tgnpc = (L1NpcInstance) cha;
			tgnpc.setParalyzed(true);
		}

		return dmg;
	}

	@Override
	public void start(L1PcInstance srcpc, Object obj) throws Exception {
	}

	@Override
	public void stop(L1Character cha) throws Exception {
		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, false));

		} else if (((cha instanceof L1MonsterInstance))
				|| ((cha instanceof L1SummonInstance))
				|| ((cha instanceof L1GuardianInstance))
				|| ((cha instanceof L1GuardInstance))
				|| ((cha instanceof L1PetInstance))) {
			L1NpcInstance npc = (L1NpcInstance) cha;
			npc.setParalyzed(false);
		}
	}
}
