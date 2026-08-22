package com.lineage.server.Controller;

import static com.lineage.server.model.skill.L1SkillId.BRAVE_AVATAR;
import static com.lineage.server.model.skill.L1SkillId.BRAVE_AVATAR_3RD;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

/**
 * 韓版王者加護<br>
 * 不用但保留
 */
public class BraveavatarController implements Runnable {

	private static BraveavatarController _instance;

	public static BraveavatarController getInstance() {
		if (_instance == null) {
			_instance = new BraveavatarController();
		}
		return _instance;
	}

	public BraveavatarController() {
		GeneralThreadPool.get().schedule(this, 1000);
	}

	@Override
	public void run() {
		try {
			for (final L1PcInstance pc : World.get().getAllPlayers()) {
				if (pc == null || pc.getNetConnection() == null) {
					continue;
				}
				if (pc.getParty() != null
						&& pc.getParty().getLeader().isCrown() // 
						&& pc.getParty().getLeader().isSkillMastery(BRAVE_AVATAR) // 習得王者加護
						&& pc.getParty().getLeader().getLocation().getTileLineDistance(pc.getLocation()) <= 18 // 
				) {
					if (!pc.hasSkillEffect(BRAVE_AVATAR_3RD)) {
						pc.addInt(1);
						pc.addDex(1);
						pc.addStr(1);
						pc.addMr(10);
						pc.addRegistAll(2);
						pc.sendPackets(new S_SPMR(pc));
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 479));
					}
					pc.setSkillEffect(BRAVE_AVATAR_3RD, 30 * 1000);
				} else {
					if (pc.hasSkillEffect(BRAVE_AVATAR_3RD)) {
						pc.removeSkillEffect(BRAVE_AVATAR_3RD);
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.get().schedule(this, 1000);
	}

}
