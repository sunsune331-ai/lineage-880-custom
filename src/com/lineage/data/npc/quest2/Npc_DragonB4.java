package com.lineage.data.npc.quest2;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_PacketBoxGree;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.WorldQuest;

/**
 * 巴拉卡斯副本
 */
public class Npc_DragonB4 extends NpcExecutor {

	private static final Log _log = LogFactory.getLog(Npc_DragonB4.class);

	/** 啟動執行緒 */
	public static final Map<Integer, checkDragonTimer4> _timer = new HashMap<Integer, checkDragonTimer4>();

	private Npc_DragonB4() {
		// TODO Auto-generated constructor stub
	}

	public static NpcExecutor get() {
		return new Npc_DragonB4();
	}

	@Override
	public int type() {
		return 3;
	}

	@Override
	public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
		try {
			// 存在副本物件
			final L1QuestUser quest = WorldQuest.get().get(pc.get_showId());
			if (quest != null) {
				if (_timer.get(pc.get_showId()) == null) {
					boolean isFound = false;
					if (!quest.npcList().isEmpty()) {
						for (final L1NpcInstance find_npc : quest.npcList()) {
							if (find_npc.getNpcId() >= 230761 && find_npc.getNpcId() <= 230763) {
								isFound = true;
								break;
							}
						}
					}

					if (!isFound) {
						// 簡易執行緒啟動
						final checkDragonTimer4 timer = new checkDragonTimer4(npc.getMapId(), quest);
						timer.begin();
						// 放入啟動執行緒列表
						_timer.put(pc.get_showId(), timer);
						// 召喚火
						spawn_fire(quest.get_id(), npc.getMapId(), 82); // 82秒
					}
				}
				// 傳送到目的地
				final L1Location loc = new L1Location(32771, 32895, npc.getMapId()).randomLocation(5, false);
				L1Teleport.teleport(pc, loc.getX(), loc.getY(), npc.getMapId(), 4, true);
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void action(final L1PcInstance pc, final L1NpcInstance npc, final String cmd, final long amount) {
	}

	// 簡易執行緒4
	private class checkDragonTimer4 extends TimerTask {
		private final int mapId;
		private final L1QuestUser quest;

		public checkDragonTimer4(final int mapId, final L1QuestUser quest) {
			this.mapId = mapId;
			this.quest = quest;
		}

		@Override
		public void run() {
			cancel();
			try {
				sendServerMessage("$25548"); // 歐林: 大家小心......他已經醒來了!
				Thread.sleep(2000);

				sendServerMessage("$25549"); // 歐林: 周邊的火焰…可能是為了包圍大家的陷阱
				Thread.sleep(2000);

				sendServerMessage("\\f3$25533"); // 巴拉卡斯: 歐歐歐.... 來了幾隻小蟲....
				Thread.sleep(2000);

				sendServerMessage("\\f3$25534"); // 巴拉卡斯: 都是哈爾巴斯的眷屬嗎...?
				Thread.sleep(2000);

				// 14367=閃電黑屏特效
				L1NpcInstance dummy1 = L1SpawnUtil.spawn(quest.get_id(), 14367, 32773, 32889, mapId, 1);
				final ServerBasePacket packet1 = new S_NPCPack(dummy1);
				sendServerBasePacket(packet1);

				// 15934=落塵特效
				L1NpcInstance dummy2 = L1SpawnUtil.spawn(quest.get_id(), 15934, 32773, 32889, mapId, 1);
				final ServerBasePacket packet2 = new S_NPCPack(dummy2);
				sendServerBasePacket(packet2);
				Thread.sleep(8000);

				sendServerMessage("\\f3$25535"); // 巴拉卡斯: 阿無關..小蟲們就是要殺掉.
				Thread.sleep(2000);

				// 15892=龍眼特效
				L1NpcInstance dummy3 = L1SpawnUtil.spawn(quest.get_id(), 15892, 32773, 32889, mapId, 1);
				final ServerBasePacket packet3 = new S_NPCPack(dummy3);
				sendServerBasePacket(packet3);
				Thread.sleep(6000);

				sendServerMessage("\\f3$25536"); // 巴拉卡斯: 這神聖的地方竟然用污贓的身體進入一定會後悔...!
				Thread.sleep(2000);

				final L1Location loc = new L1Location(32771, 32893, mapId).randomLocation(5, true);
				L1SpawnUtil.spawn(230761, loc, new Random().nextInt(8), quest.get_id());

			} catch (final Exception e) {

			} finally {
				_timer.remove(quest.get_id());
			}
		}

		private final void sendServerMessage(final String msg) {
			if (!quest.pcList().isEmpty()) {
				for (final L1PcInstance pc : quest.pcList()) {
					pc.sendPackets(new S_PacketBoxGree(msg));
				}
			}
		}

		private final void sendServerBasePacket(final ServerBasePacket packet) {
			if (!quest.pcList().isEmpty()) {
				for (final L1PcInstance pc : quest.pcList()) {
					pc.sendPackets(packet);
				}
			}
		}

		public final void begin() {
			GeneralThreadPool.get().schedule(this, 60 * 1000);
		}
	}

	/**
	 * 召喚火堆
	 * @param showid
	 * @param mapid
	 * @param time
	 */
	private void spawn_fire(final int showid, final int mapid, final int time) {
		final int spawn_list[][] = {
				{	32763,	32897	},
				{	32764,	32898	},
				{	32762,	32899	},
				{	32762,	32899	},
				{	32761,	32898	},
				{	32762,	32901	},
				{	32763,	32901	},
				{	32765,	32903	},
				{	32767,	32902	},
				{	32766,	32903	},
				{	32763,	32902	},
				{	32768,	32903	},
				{	32770,	32902	},
				{	32770,	32902	},
				{	32771,	32903	},
				{	32772,	32902	},
				{	32774,	32903	},
				{	32775,	32903	},
				{	32776,	32903	},
				{	32776,	32903	},
				{	32777,	32901	},
				{	32779,	32903	},
				{	32779,	32903	},
				{	32777,	32899	},
				{	32779,	32897	},
				{	32781,	32897	},
				{	32781,	32897	},
				{	32781,	32897	},
				{	32778,	32900	},
				{	32783,	32899	},
				{	32784,	32901	},
				{	32780,	32895	},
				{	32782,	32892	},
				{	32780,	32891	},
				{	32783,	32892	},
				{	32783,	32892	},
				{	32781,	32887	},
				{	32781,	32887	},
				{	32780,	32886	},
				{	32781,	32884	},
				{	32780,	32883	},
				{	32779,	32882	},
				{	32778,	32882	},
				{	32781,	32882	},
				{	32782,	32882	},
				{	32778,	32880	},
				{	32776,	32880	},
				{	32774,	32880	},
				{	32774,	32880	},
				{	32773,	32879	},
				{	32773,	32879	},
				{	32773,	32878	},
				{	32770,	32881	},
				{	32770,	32881	},
				{	32770,	32881	},
				{	32767,	32883	},
				{	32765,	32884	},
				{	32763,	32885	},
				{	32760,	32887	},
				{	32760,	32888	},
				{	32760,	32888	},
				{	32758,	32891	},
				{	32758,	32891	},
				{	32760,	32892	},
				{	32760,	32892	},
				{	32758,	32894	},
				{	32759,	32895	},
				{	32759,	32895	},
				{	32758,	32899	},
				{	32757,	32898	},
				{	32758,	32885	},
				{	32758,	32884	},
				{	32758,	32883	},
				{	32758,	32883	},
				{	32759,	32882	},
				{	32762,	32882	},
				{	32763,	32881	},
				{	32765,	32880	},
				{	32774,	32902	},
				{	32777,	32901	},
				{	32769,	32876	},
				{	32776,	32876	},
				{	32780,	32877	},
				};
		for (final int[] spawn_dat : spawn_list) {
			L1SpawnUtil.spawn(showid, 15920, spawn_dat[0], spawn_dat[1], mapid, time);
		}
	}
}
