package com.lineage.server.timecontroller;

import com.lineage.server.timecontroller.npc.NpcBowTimer;
import com.lineage.server.timecontroller.npc.NpcBoxRepairTimer;
import com.lineage.server.timecontroller.npc.NpcChatTimer;
import com.lineage.server.timecontroller.npc.NpcDeadTimer;
import com.lineage.server.timecontroller.npc.NpcDeleteTimer;
import com.lineage.server.timecontroller.npc.NpcDigestItemTimer;
import com.lineage.server.timecontroller.npc.NpcExistTimer;
import com.lineage.server.timecontroller.npc.NpcHprTimer;
import com.lineage.server.timecontroller.npc.NpcMprTimer;
import com.lineage.server.timecontroller.npc.NpcRestTimer;
import com.lineage.server.timecontroller.npc.NpcShopTimer;
import com.lineage.server.timecontroller.npc.NpcSpawnBossTimer;
import com.lineage.server.timecontroller.npc.NpcWorkTimer;

public class StartTimer_Npc {
	public void start() throws InterruptedException {
		NpcChatTimer npcChatTimeController = new NpcChatTimer();
		npcChatTimeController.start();
		Thread.sleep(50L);

		NpcHprTimer npcHprTimer = new NpcHprTimer();
		npcHprTimer.start();
		Thread.sleep(50L);

		NpcMprTimer npcMprTimer = new NpcMprTimer();
		npcMprTimer.start();
		Thread.sleep(50L);

		NpcDeleteTimer npcDeleteTimer = new NpcDeleteTimer();
		npcDeleteTimer.start();
		Thread.sleep(50L);

		NpcExistTimer npcexistTimer = new NpcExistTimer();// BOSS存在時間限制
		npcexistTimer.start();
		Thread.sleep(50L);

		NpcDeadTimer npcDeadTimer = new NpcDeadTimer();
		npcDeadTimer.start();
		Thread.sleep(50L);

		NpcDigestItemTimer digestItemTimer = new NpcDigestItemTimer();
		digestItemTimer.start();
		Thread.sleep(50L);

		NpcSpawnBossTimer bossTimer = new NpcSpawnBossTimer();
		bossTimer.start();
		Thread.sleep(50L);

		NpcShopTimer shopTimer = new NpcShopTimer();
		shopTimer.start();
		Thread.sleep(50L);

		NpcRestTimer restTimer = new NpcRestTimer();
		restTimer.start();
		Thread.sleep(50L);

		NpcWorkTimer workTimer = new NpcWorkTimer();
		workTimer.start();
		Thread.sleep(50L);

		NpcBowTimer bow = new NpcBowTimer();
		bow.start();
	
		// NPC寶箱 by terry0412
		final NpcBoxRepairTimer boxRepair = new NpcBoxRepairTimer();
		boxRepair.start();
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.timecontroller.StartTimer_Npc JD-Core Version: 0.6.2
 */