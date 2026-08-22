package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.DRAGON_BLOOD_4;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBox;

/**
 * 巴拉卡斯副本
 */
public class DRAGON_BLOOD4 extends SkillMode {

	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;

		if (!srcpc.hasSkillEffect(DRAGON_BLOOD_4)) {
			System.out.println("玩家:" + srcpc.getName() + " 發動火龍-龍之血痕效果");
			srcpc.addHitup(1);
			srcpc.addDmgup(1);
			srcpc.addBowHitup(1);
			srcpc.addBowDmgup(1);
			srcpc.setSkillEffect(DRAGON_BLOOD_4, integer * 1000);
			srcpc.sendPackets(new S_PacketBox(100, 91, integer / 60));
		}
		return dmg;
	}

	public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;

		return dmg;
	}

	public void start(L1PcInstance srcpc, Object obj) throws Exception {
	}

	public void stop(L1Character cha) throws Exception {
		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addHitup(-1);
			pc.addDmgup(-1);
			pc.addBowHitup(-1);
			pc.addBowDmgup(-1);
		}
	}
}
