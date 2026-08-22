package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.BRAVE_AVATAR;
import static com.lineage.server.model.skill.L1SkillId.IMPACT;

import com.lineage.server.model.Broadcaster;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_InventoryIcon;
import com.lineage.server.serverpackets.S_NewSkillIcon;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 衝突強化222<br>
 * 所有耐性+5和命中+5
 */
public class IMPACT extends SkillMode {

	public IMPACT() {
	}
	
	@Override
	public int start(final L1PcInstance srcpc, final L1Character cha, final L1Magic magic, final int integer)
			throws Exception {
		final int dmg = 0;
        int upskill = srcpc.getLevel() - 80;
        if (upskill >= 5){
            upskill = 5;
        }
        if (upskill < 0){
            upskill = 0;
        }	
        int aa = 5+upskill;
		srcpc.removeSkillEffect(L1SkillId.IMPACT);
		
			srcpc.addRegistAll(aa);
			srcpc.setHitAll(aa);
			srcpc.sendPackets(new S_SPMR(srcpc));
			srcpc.sendPackets(new S_OwnCharStatus2(srcpc));
			srcpc.sendPackets(new S_NewSkillIcon(IMPACT, true, 15));
			srcpc.sendPackets(new S_SkillSound(srcpc.getId(), 14513));
             Broadcaster.broadcastPacket(srcpc, new S_SkillSound(srcpc.getId(), 14513));

			// buff time
			srcpc.setSkillEffect(L1SkillId.IMPACT, 15 * 1000);
		

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
			 int upskill = pc.getLevel() - 80;
		        if (upskill >= 5){
		            upskill = 5;
		        }
		        if (upskill < 0){
		            upskill = 0;
		        }	
		        int aa = 5+upskill;
			pc.addRegistAll(-aa);
			pc.setHitAll(-aa);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharStatus2(pc));
			//pc.sendPackets(new S_InventoryIcon(5735, false, 3432, 0, -1)); // 自訂

		}
	}
}
