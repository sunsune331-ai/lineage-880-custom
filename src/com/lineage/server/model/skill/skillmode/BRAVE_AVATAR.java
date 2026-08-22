package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.BRAVE_AVATAR;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_InventoryIcon;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_SPMR;

/**
 * 王者加護
 * @author dexcIMPACT
 */
public class BRAVE_AVATAR extends SkillMode {

	public BRAVE_AVATAR() {
	}
	
	@Override
	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;

		srcpc.removeSkillEffect(BRAVE_AVATAR);

		if (srcpc.isInParty()) {
			srcpc.addInt(1);
			srcpc.addDex(1);
			srcpc.addStr(1);
			srcpc.addMr(10);
			srcpc.addRegistAll(2);
			srcpc.sendPackets(new S_SPMR(srcpc));
			srcpc.sendPackets(new S_OwnCharStatus2(srcpc));
			srcpc.sendPackets(new S_InventoryIcon(5735, true, 3432, 0, -1)); // 自訂

			// buff time
			srcpc.setSkillEffect(BRAVE_AVATAR, 86400 * 1000);
		}

		return dmg;
	}

	@Override
	public int start(final L1NpcInstance npc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;

		return dmg;
	}

	@Override
	public void start(final L1PcInstance srcpc, final Object obj) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop(final L1Character cha) throws Exception {
		// TODO Auto-generated method stub
		if (cha instanceof L1PcInstance) {
			final L1PcInstance pc = (L1PcInstance) cha;
			pc.addInt(-1);
			pc.addDex(-1);
			pc.addStr(-1);
			pc.addMr(-10);
			pc.addRegistAll(-2);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharStatus2(pc));
			pc.sendPackets(new S_InventoryIcon(5735, false, 3432, 0, -1)); // 自訂

		}
	}
}
