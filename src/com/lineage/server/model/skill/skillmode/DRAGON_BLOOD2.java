package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.DRAGON_BLOOD_2;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_PacketBox;

public class DRAGON_BLOOD2 extends SkillMode {
	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;
		if (!srcpc.hasSkillEffect(DRAGON_BLOOD_2)) {
			System.out.println("玩家:"+srcpc.getName()+" 發動水龍-龍之血痕效果");
			srcpc.addHpr(3);
			srcpc.addMpr(1);
			srcpc.addWind(50);
			srcpc.sendPackets(new S_OwnCharAttrDef(srcpc));
			srcpc.setSkillEffect(DRAGON_BLOOD_2, integer * 1000);
			srcpc.sendPackets(new S_PacketBox(100, 85, integer / 60));
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
			pc.addHpr(-3);
			pc.addMpr(-1);
			pc.addWind(-50);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
		}
	}
}
