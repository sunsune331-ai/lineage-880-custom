package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;

public class TRIPLE_ARROW extends SkillMode {
	public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;

		
		
		
		boolean gfxcheck = false;
		int playerGFX = srcpc.getTempCharGfx();
		
		
		if(srcpc.getWeapon()!=null){
			if(srcpc.getWeapon().getItem().getType1()==20){
				gfxcheck = true;
			}
			
		}
		
//		switch (playerGFX) {
//		case 37:
//		case 138:
//		case 2284:
//		case 2323:
//		case 3105:
//		case 3126:
//		case 3145:
//		case 3148:
//		case 3151:
//		case 3420:
//		case 3860:
//		case 3871:
//		case 13450:// 巴風特雕像變身
//		case 3892:
//		case 3895:
//		case 3898:
//		case 3901:
//		case 4125:
//		case 4190:
//		case 4917:
//		case 4918:
//		case 4919:
//		case 4950:
//		case 6087:
//		case 6140:
//		case 6145:
//		case 6150:
//		case 6155:
//		case 6160:
//		case 6269:
//		case 6272:
//		case 6275:
//		case 6278:
//		case 6826:
//		case 6827:
//		case 6836:
//		case 6837:
//		case 6846:
//		case 6847:
//		case 6856:
//		case 6857:
//		case 6866:
//		case 6867:
//		case 6876:
//		case 6877:
//		case 6886:
//		case 6887:
//		case 7959:
//		case 7967:
//		case 7968:
//		case 7969:
//		case 7970:
//		case 8786:
//		case 8792:
//		case 8798:
//		case 8804:
//		case 8808:
//		case 8842:
//		case 8845:
//		case 8860:
//		case 8900:
//		case 8913:
//		case 9225:
//		case 9226:
//		case 10275:
//		case 10283:
//		case 10286:
//		case 12314:
//		case 13218:
//		case 13346:
//		case 13631:
//		case 13635:
//		case 13723:
//		case 13725:
//			gfxcheck = true;
//			break;
//		default:
//			break;
//		}

		if (!gfxcheck) {
			return 0;
		}

		for (int i = 0; i < 3; i++) {
			cha.onAction(srcpc);
		}

		srcpc.sendPacketsAll(new S_SkillSound(srcpc.getId(), 4394));// 加速封包
		srcpc.sendPacketsAll(new S_SkillSound(srcpc.getId(), 11764));// 特效動畫

		return dmg;
	}

	public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
		int dmg = 0;
		for (int i = 3; i > 0; i--) {
			npc.attackTarget(cha);
		}

		npc.broadcastPacketAll(new S_SkillSound(npc.getId(), 4394));// 加速封包
		npc.broadcastPacketAll(new S_SkillSound(npc.getId(), 11764));// 特效動畫

		return dmg;
	}

	public void start(L1PcInstance srcpc, Object obj) throws Exception {
	}

	public void stop(L1Character cha) throws Exception {
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.model.skill.skillmode.TRIPLE_ARROW JD-Core Version: 0.6.2
 */