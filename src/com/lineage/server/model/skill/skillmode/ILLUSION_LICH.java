package com.lineage.server.model.skill.skillmode;

import static com.lineage.server.model.skill.L1SkillId.CUBE_BALANCE;
import static com.lineage.server.model.skill.L1SkillId.CUBE_IGNITION;
import static com.lineage.server.model.skill.L1SkillId.CUBE_QUAKE;
import static com.lineage.server.model.skill.L1SkillId.ILLUSION_DIA_GOLEM;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_InventoryIcon;
import com.lineage.server.serverpackets.S_NewSkillIcon;
import com.lineage.server.serverpackets.S_SPMR;

public class ILLUSION_LICH extends SkillMode {
	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;
		if (!cha.hasSkillEffect(209)) { 
			if ((cha instanceof L1PcInstance)) {
				L1PcInstance pc = (L1PcInstance) cha;  
		           if (pc.hasSkillEffect(209)) {
		        	   pc.removeSkillEffect(209);
		            }
		           if (pc.hasSkillEffect(215)) {
		        	   pc.removeSkillEffect(215);
		        	   pc.sendPackets(new S_InventoryIcon(5309, false, 0, -1));
		            }
				pc.addSp(2);
				//pc.sendPackets(new S_NewSkillIcon(209, true, 128));
				pc.sendPackets(new S_InventoryIcon(3115, true,5123, 128));
				pc.sendPackets(new S_SPMR(pc));
				pc.setSkillEffect(209, integer * 1000);
			} else if (((cha instanceof L1MonsterInstance)) || ((cha instanceof L1SummonInstance))
					|| ((cha instanceof L1PetInstance))) {
				L1NpcInstance tgnpc = (L1NpcInstance) cha;
				tgnpc.addSp(2);

				tgnpc.setSkillEffect(209, integer * 1000);
			}
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
			pc.addSp(-2);
			//pc.sendPackets(new S_NewSkillIcon(209, false, -1));
			 pc.sendPackets(new S_InventoryIcon(3115, false, 0, -1));
			pc.sendPackets(new S_SPMR(pc));
		} else if (((cha instanceof L1MonsterInstance)) || ((cha instanceof L1SummonInstance))
				|| ((cha instanceof L1PetInstance))) {
			L1NpcInstance tgnpc = (L1NpcInstance) cha;
			tgnpc.addSp(-2);			
		}
	}
}