package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.FOU_SLAYER_BRAVE;
import static com.lineage.server.model.skill.L1SkillId.SHOCK_STUN;

import java.util.Random;

import com.lineage.config.ConfigSkill;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_PacketBoxDk;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 屠宰者
 * @author dexc
 */
public class FOE_SLAYER extends SkillMode {// SRC0808

	private static Random _random = new Random();

	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;

		srcpc.setFoeSlayer(true);

		for (int i = 0; i < 3; i++) {
			cha.onAction(srcpc);
			if (i == 2) {// 第三次
				srcpc.setFoeSlayer(false);
				// srcpc.set_weaknss(0, 0);
				// srcpc.sendPackets(new S_PacketBoxDk(0));

				// 習得屠宰者：強化 可進行弱點曝光第4段攻擊
				if (srcpc.isSkillMastery(FOU_SLAYER_BRAVE) && srcpc.get_weaknss() == 4) {
					if (ConfigSkill.AddPassiveSkillMsg) {
						srcpc.sendPackets(new S_SystemMessage("測試屠宰者：強化。"));
					}
				} else {
					srcpc.set_weaknss(0, 0);
					srcpc.sendPackets(new S_PacketBoxDk(0));
				}

			}
		}

		if (srcpc.isDragonKnight()
				&& srcpc.getMeteLevel() >= 4
				&& (_random.nextInt(100) + 1) < ConfigSkill.DK4RANDOM
		) { // SRC0808
			final int shock = ConfigSkill.DK4SHOCKTIME;

			cha.setSkillEffect(SHOCK_STUN, shock * 1000);

			L1SpawnUtil.spawnEffect(81162, shock, cha.getX(), cha.getY(), srcpc.getMapId(), srcpc, 0);

			if ((cha instanceof L1PcInstance)) {
				final L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));

			} else if (((cha instanceof L1MonsterInstance))
					|| ((cha instanceof L1SummonInstance))
					|| ((cha instanceof L1PetInstance))
			) {
				final L1NpcInstance tgnpc = (L1NpcInstance) cha;
				tgnpc.setParalyzed(true);
			}
		}

		srcpc.sendPacketsAll(new S_SkillSound(srcpc.getId(), 7020));// 屠宰者 加速封包
		srcpc.sendPacketsAll(new S_SkillSound(cha.getId(), 12119));// 屠宰者 特效動畫

		return dmg;
	}

	public int start(final L1NpcInstance npc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;
		for (int i = 0; i < 3; i++) {
			npc.attackTarget(cha);
		}
		npc.broadcastPacketAll(new S_SkillSound(cha.getId(), 7020));// 屠宰者 加速封包
		npc.broadcastPacketAll(new S_SkillSound(cha.getId(), 12119));// 屠宰者 特效動畫
		return dmg;
	}

	public void start(final L1PcInstance srcpc, final Object obj) throws Exception {
	}

	public void stop(final L1Character cha) throws Exception {
	}
}
