package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.ARMOR_BREAK;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 破壞盔甲112
 * @author Admin
 */
public class ARMOR_BREAK extends SkillMode {

	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		final int dmg = 0;
		final boolean isProbability = magic.calcProbabilityMagic(ARMOR_BREAK);
		if ((isProbability)) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance tgpc = (L1PcInstance) cha;
				if (tgpc.hasSkillEffect(ARMOR_BREAK)) {
					tgpc.killSkillEffectTimer(ARMOR_BREAK);
				}
				tgpc.setSkillEffect(ARMOR_BREAK, 8 * 1000);
				srcpc.sendPackets(new S_SystemMessage("破壞盔甲 施放成功!"));
				srcpc.sendPacketsAll(new S_SkillSound(tgpc.getId(), 3400));
				// tgpc.sendPackets(new S_PacketBoxIconAura(119, 8));

			} else if ((cha instanceof L1MonsterInstance)
					|| (cha instanceof L1SummonInstance)
					|| (cha instanceof L1PetInstance)
			) {
				final L1NpcInstance npc = (L1NpcInstance) cha;
				if (npc.hasSkillEffect(ARMOR_BREAK)) {
					npc.killSkillEffectTimer(ARMOR_BREAK);
				}
				// npc.broadcastPacketAll(new S_SkillSound(npc.getId(), 8977));
				srcpc.sendPacketsAll(new S_SkillSound(npc.getId(), 3400));
			}

		}
		return dmg;
	}

	public void stop(L1Character cha) throws Exception {

	}

	@Override
	public int start(L1NpcInstance paramL1NpcInstance, L1Character paramL1Character, L1Magic paramL1Magic, int paramInt)
			throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void start(L1PcInstance paramL1PcInstance, Object paramObject) throws Exception {
		// TODO Auto-generated method stub

	}
}
