package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.ILLUSION_DIA_GOLEM;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_InventoryIcon;
import com.lineage.server.serverpackets.S_NewSkillIcon;

public class ILLUSION_AVATAR extends SkillMode {
	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;
		if (((cha instanceof L1PcInstance)) && (!cha.hasSkillEffect(219))) {
			L1PcInstance pc = (L1PcInstance) cha;
	           if (pc.hasSkillEffect(219)) {
	            	pc.removeSkillEffect(219);
	         }
	           if (pc.hasSkillEffect(1219)) {
	            	pc.removeSkillEffect(1219);
	         }
            if (pc.hasSkillEffect(220)) {
            	pc.removeSkillEffect(220);
            	pc.sendPackets(new S_InventoryIcon(3101, false, 0, -1));
            }
            pc.addDmgup(-10);
            pc.addBowDmgup(-10); //遠傷害增加
			//pc.sendPackets(new S_NewSkillIcon(219, true, 128));
            pc.sendPackets(new S_InventoryIcon(3111, true,5120, 128));
			cha.setSkillEffect(219, integer * 1000);	
		
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
		if ((cha instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) cha;
            pc.addDmgup(-10);
            pc.addBowDmgup(-10); //遠傷害增加
			//pc.sendPackets(new S_NewSkillIcon(219, false, -1));
            pc.sendPackets(new S_InventoryIcon(3111, false, 0, -1));
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.model.skill.skillmode.ILLUSION_AVATAR JD-Core Version:
 * 0.6.2
 */