package com.lineage.server.timecontroller.quest;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.quest.ADLv80_3;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_PacketBoxGree;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldQuest;

/**
 * 風龍副本 - 處理時間軸
 * 
 * @author
 */
public final class AD80_3_Timer extends TimerTask {

	private static final Log _log = LogFactory.getLog(AD80_3_Timer.class);

	private static final Random _random = new Random();

	private ScheduledFuture<?> _timer;

	private int _qid = -1;

	// 計數器1
	private int _counter;

	// 中心座標
	private L1Location _loc;

	public void start() {
		_qid = ADLv80_3.QUEST.get_id();
		_loc = new L1Location(32848, 32877, ADLv80_3.MAPID);
		_timer = GeneralThreadPool.get().scheduleAtFixedRate(this, 2000L, 2000L);
	}

	@Override
	public void run() {
		try {
			// 執行中任務副本
			final ArrayList<L1QuestUser> questList = WorldQuest.get().getQuests(_qid);
			// 不包含元素
			if (questList.isEmpty()) {
				return;
			}

			for (final Object object : questList.toArray()) {
				final L1QuestUser quest = (L1QuestUser) object;
				for (final L1NpcInstance npc : quest.npcList()) {
					if (npc.isDead()) {
						continue;
					}
					L1Location loc = npc.getLocation().randomLocation(12, true);

					if ((npc.getNpcId() >= 97204) && (npc.getNpcId() <= 97209)) { // 風龍未死亡
						if (++_counter >= 45) { // 45秒後出生
							_counter = 0;

							boolean check_spawn = false;
							for (final Iterator<?> localIterator2 = quest.npcList().iterator(); localIterator2
									.hasNext();) {
								L1NpcInstance check_npc = (L1NpcInstance) localIterator2.next();
								if (check_npc.getNpcId() == 97210) {// 烏雲大精靈
									check_spawn = true;
									break;
								}
							}
							if (!check_spawn) {
								loc = _loc.randomLocation(12, true);
								L1SpawnUtil.spawn(97210, loc, 0, quest.get_id());
							}
						}
					}

					if ((npc.getNpcId() >= 97204) && (npc.getNpcId() <= 97206)) {
						if (_random.nextInt(100) < 5) {
							for (final L1PcInstance tgpc : quest.pcList()) {
								switch (_random.nextInt(3)) {
								case 0:
									npc.getNpcTemplate().set_weakAttr(4);
									tgpc.sendPackets(new S_PacketBoxGree(6));
									break;
								case 1:
									npc.getNpcTemplate().set_weakAttr(1);
									tgpc.sendPackets(new S_PacketBoxGree(7));
									break;
								case 2:
									npc.getNpcTemplate().set_weakAttr(8);
									tgpc.sendPackets(new S_PacketBoxGree(8));
									break;
								}
							}
						}

						if (npc.getStatus() == 13) {// 飛天狀態
							if (npc.getSkyTime() >= 8) { // 飛天8秒
								for (final L1PcInstance tgpc : quest.pcList()) {
									tgpc.sendPackets(new S_Weather(World.get().getWeather()));
								}
								for (L1PcInstance pc : World.get().getRecognizePlayer(npc)) {
									pc.sendPackets(new S_RemoveObject(npc));
									pc.removeKnownObject(npc);
								}

								npc.setLocation(loc);
								npc.broadcastPacketAll(new S_NPCPack(npc));
								npc.broadcastPacketAll(new S_DoActionGFX(npc.getId(), 11));
								npc.setHiddenStatus(0);
								npc.setSkyTime(0); // 飛天時間歸0
								break;
							}

							npc.setSkyTime(npc.getSkyTime() + 1); // 飛天時間 + 1

							int range;
							int effectId;
							if (_random.nextInt(100) < 70) {
								effectId = 10405;
								range = 3;
							} else {
								effectId = 10407;
								range = 5;
							}
							final S_EffectLocation packet = new S_EffectLocation(loc.getX(), loc.getY(), effectId);

							for (final L1PcInstance tgpc : quest.pcList()) {
								if (tgpc.getLocation().isInScreen(loc)) {
									tgpc.sendPackets(packet);
								}
								if (tgpc.getLocation().getTileLineDistance(loc) < range) {
									tgpc.sendPacketsAll(new S_DoActionGFX(tgpc.getId(), 2));
									tgpc.receiveDamage(npc, _random.nextInt(301) + 300, true, true);
								}
							}
						} else if (_random.nextInt(100) < 5) {// 5%機率飛天
							npc.allTargetClear();
							npc.setStatus(13);
							npc.broadcastPacketAll(new S_NPCPack(npc));
							npc.broadcastPacketAll(new S_DoActionGFX(npc.getId(), 13));
							npc.setHiddenStatus(2);

							for (final L1PcInstance tgpc : quest.pcList()) {
								tgpc.sendPackets(new S_Weather(17));
							}
						}
					}
				}
			}
			questList.clear();

		} catch (final ConcurrentModificationException e) {

		} catch (final Exception e) {
			_log.error("風龍副本 傷害計能施放 時間軸異常重啟", e);
			GeneralThreadPool.get().cancel(_timer, false);
			final AD80_3_Timer ad80_3Timer = new AD80_3_Timer();
			ad80_3Timer.start();
		}
	}
}
