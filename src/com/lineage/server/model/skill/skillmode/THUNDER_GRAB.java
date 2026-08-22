package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.THUNDER_GRAB;
import static com.lineage.server.model.skill.L1SkillId.THUNDER_GRAB_BRAVE;

import java.util.Random;

import com.lineage.config.ConfigSkill;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.utils.L1SpawnUtil;

/**
 * 奪命之雷
 * @author Admin
 */
public class THUNDER_GRAB extends SkillMode {

	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = magic.calcMagicDamage(THUNDER_GRAB);
		final Random random = new Random();
		int bindtime = random.nextInt(4) + 1;

		if (((cha instanceof L1PcInstance)) && (cha.hasSkillEffect(THUNDER_GRAB))) {
			bindtime += cha.getSkillEffectTimeSec(THUNDER_GRAB);
		}

		if (bindtime > 4) {
			bindtime = 4;
		}

		if (srcpc.isSkillMastery(THUNDER_GRAB_BRAVE)) { // 習得奪命之雷：強化
			// 奪命之雷持續時間提升
			bindtime += 2;
			if (ConfigSkill.AddPassiveSkillMsg) {
				srcpc.sendPackets(new S_SystemMessage("測試奪命之雷：強化加時間。"));
			}
		}

		final boolean isProbability = magic.calcProbabilityMagic(THUNDER_GRAB);
		if ((isProbability) && (!cha.hasSkillEffect(4000))) {
			cha.setSkillEffect(THUNDER_GRAB, bindtime * 1000);
			if ((cha instanceof L1PcInstance)) {
				final L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4184));
				L1SpawnUtil.spawnEffect(81182, bindtime, pc.getX(), pc.getY(), srcpc.getMapId(), srcpc, 0);
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));

			} else if (((cha instanceof L1MonsterInstance))
					|| ((cha instanceof L1SummonInstance))
					|| ((cha instanceof L1PetInstance))
			) {
				final L1NpcInstance tgnpc = (L1NpcInstance) cha;
				tgnpc.broadcastPacketX8(new S_SkillSound(tgnpc.getId(), 4184));
				L1SpawnUtil.spawnEffect(81182, bindtime, tgnpc.getX(), tgnpc.getY(), srcpc.getMapId(), srcpc, 0);
				// tgnpc.setParalyzed(true);
				tgnpc.setPassispeed(0);
			}
		}
		return dmg;
	}

	public int start(final L1NpcInstance npc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = magic.calcMagicDamage(THUNDER_GRAB);
		final Random random = new Random();
		int bindtime = random.nextInt(4) + 1;

		if (((cha instanceof L1PcInstance)) && (cha.hasSkillEffect(THUNDER_GRAB))) {
			bindtime += cha.getSkillEffectTimeSec(THUNDER_GRAB);
		}

		if (bindtime > 4) {
			bindtime = 4;
		}

		final boolean isProbability = magic.calcProbabilityMagic(THUNDER_GRAB);
		if ((isProbability) && (!cha.hasSkillEffect(4000))) {
			cha.setSkillEffect(THUNDER_GRAB, bindtime * 1000);
			if ((cha instanceof L1PcInstance)) {
				final L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4184));
				L1SpawnUtil.spawnEffect(81182, bindtime, pc.getX(), pc.getY(), npc.getMapId(), npc, 0);
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));

			} else if (((cha instanceof L1MonsterInstance))
					|| ((cha instanceof L1SummonInstance))
					|| ((cha instanceof L1PetInstance))
			) {
				final L1NpcInstance tgnpc = (L1NpcInstance) cha;
				tgnpc.broadcastPacketAll(new S_SkillSound(tgnpc.getId(), 4184));
				L1SpawnUtil.spawnEffect(81182, bindtime, tgnpc.getX(), tgnpc.getY(), npc.getMapId(), npc, 0);
				// tgnpc.setParalyzed(true);
				tgnpc.setPassispeed(0);
			}
		}
		return dmg;
	}

	public void start(final L1PcInstance srcpc, final Object obj) throws Exception {
	}

	public void stop(final L1Character cha) throws Exception {
		if ((cha instanceof L1PcInstance)) {
			final L1PcInstance pc = (L1PcInstance) cha;
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));

		} else if (((cha instanceof L1MonsterInstance))
				|| ((cha instanceof L1SummonInstance))
				|| ((cha instanceof L1PetInstance))
		) {
			final L1NpcInstance npc = (L1NpcInstance) cha;
			// npc.setParalyzed(false);
			npc.setPassispeed(npc.getNpcTemplate().get_passispeed());
		}
	}
}
