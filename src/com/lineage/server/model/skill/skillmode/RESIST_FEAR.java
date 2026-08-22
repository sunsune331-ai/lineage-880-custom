package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxIcon1;

public class RESIST_FEAR extends SkillMode {
	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;

		if ((!cha.hasSkillEffect(188)) && ((cha instanceof L1PcInstance))) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.setSkillEffect(188, integer * 1000);
			pc.add_dodge_down(25);
			pc.sendPackets(new S_PacketBoxIcon1(false, pc.get_dodge_down()));
		}

		return dmg;
	}

	public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;

		return 0;
	}

	public void start(L1PcInstance srcpc, Object obj) throws Exception {
	}

	public void stop(L1Character cha) throws Exception {
		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.add_dodge_down(-25);

			pc.sendPackets(new S_PacketBoxIcon1(false, pc.get_dodge_down()));
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.model.skill.skillmode.RESIST_FEAR JD-Core Version: 0.6.2
 */