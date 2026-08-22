package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_OwnCharStatus2;

public class BS_GX09 extends SkillMode {
	private static final int _addhp = 100;
	private static final int _addhit = 2;
	private static final int _addhpr = 5;
	private static final int _adddmg = 2;
	private static final int _addstr = 1;

	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;
		if (!srcpc.hasSkillEffect(4409)) {
			srcpc.addMaxHp(100);
			srcpc.addHitup(2);
			srcpc.addHpr(5);
			srcpc.addDmgup(2);
			srcpc.addStr(1);
			srcpc.setSkillEffect(4409, integer * 1000);
			srcpc.sendPackets(new S_HPUpdate(srcpc.getCurrentHp(), srcpc.getMaxHp()));
			srcpc.sendPackets(new S_OwnCharStatus2(srcpc));
			srcpc.sendDetails();
		}
		return 0;
	}

	public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;

		return 0;
	}

	public void start(L1PcInstance srcpc, Object obj) throws Exception {
	}

	public void stop(L1Character cha) throws Exception {
		cha.addMaxHp(-100);
		cha.addHitup(-2);
		cha.addDmgup(-2);
		cha.addStr(-1);
		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
			pc.addHpr(-5);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			pc.sendPackets(new S_OwnCharStatus2(pc));
			pc.sendDetails();
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.model.skill.skillmode.BS_GX09 JD-Core Version: 0.6.2
 */