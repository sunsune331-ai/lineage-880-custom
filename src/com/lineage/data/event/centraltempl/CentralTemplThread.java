package com.lineage.data.event.centraltempl;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.IdFactory;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NpcChatPacket;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.Random;
import com.lineage.server.world.World;

/**
 * 中央寺廟線程
 * 
 * @author sudawei
 */
public class CentralTemplThread extends Thread {
	short mapId;
	L1PcInstance pc;
	L1NpcInstance npc;
	int type = -1;
	private static final Log _log = LogFactory.getLog(CentralTemplThread.class);

	public CentralTemplThread(int mapId, L1PcInstance pc) {
		this.mapId = (short) mapId;
		this.pc = pc;
	}

	@Override
	public void run() {
		_log.info("中央寺廟副本開始：玩家:" + pc.getName() + ",mapid:" + mapId);
		try {
			L1Teleport.teleport(pc, 32795, 32867, mapId, 1, true);
			npc = spawn(new L1Location(32801, 32862, mapId), 190114, 1, 4).get(0);
			L1FieldObjectInstance object = spwanField(7572, 32801, 32862, mapId);
			ArrayList<L1NpcInstance> npclist = new ArrayList<L1NpcInstance>();
			npclist.addAll(spawn(new L1Location(32798, 32862, mapId), 190110, 1, 2));
			npclist.addAll(spawn(new L1Location(32801, 32865, mapId), 190110, 1, 0));
			npclist.addAll(spawn(new L1Location(32804, 32859, mapId), 190110, 1, 5));
			npclist.addAll(spawn(new L1Location(32798, 32861, mapId), 190111, 1, 2));
			npclist.addAll(spawn(new L1Location(32802, 32866, mapId), 190111, 1, 0));
			if (isKillNpc(npclist) == -1) {
				return;
			}
			Thread.sleep(1000);
			pc.getInventory().storeItem(640354, 1);
			object.deleteMe();
			pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND_NUMBER, 1, 3));
			sendMsg("$17947");
			npclist = new ArrayList<L1NpcInstance>();
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190098, 5, 4));
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190099, 5, 4));
			//
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190101, 5, 7));
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190100, 5, 7));
			//
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190102, 5, 0));
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190103, 5, 0));
			//
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190104, 5, 2));
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190105, 5, 2));
			// Thread.sleep(2000);
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190098, 5, 4));
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190099, 5, 4));
			//
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190101, 5, 7));
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190100, 5, 7));
			//
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190102, 5, 0));
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190103, 5, 0));
			//
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190104, 5, 2));
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190105, 5, 2));
			// Thread.sleep(2000);
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190098, 5, 4));
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190099, 5, 4));
			//
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190101, 5, 7));
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190100, 5, 7));
			//
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190102, 5, 0));
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190103, 5, 0));
			//
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190104, 5, 2));
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190105, 5, 2));
			npclist.addAll(spawn());

			Thread.sleep(5000L);
			sendMsg("$17701");
			isKillNpc(npclist);
			Thread.sleep(1000);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND_NUMBER, 2, 3));
			npclist = new ArrayList<L1NpcInstance>();
			sendMsg("$17969");
			type = 0;
			int small_boss = 190106 + Random.nextInt(4);
			switch (small_boss) {
			case 190106:
				sendMsg("$17941");
				npclist.addAll(spawn(new L1Location(32800, 32845, mapId), small_boss, 1, 4));
				break;
			case 190107:
				sendMsg("$17944");
				npclist.addAll(spawn(new L1Location(32817, 32862, mapId), small_boss, 1, 7));
				break;
			case 190108:
				sendMsg("$17942");
				npclist.addAll(spawn(new L1Location(32801, 32878, mapId), small_boss, 1, 0));
				break;
			case 190109:
				sendMsg("$17943");
				npclist.addAll(spawn(new L1Location(32785, 32861, mapId), small_boss, 1, 2));
				break;
			default:
				sendMsg("$17943");
				npclist.addAll(spawn(new L1Location(32785, 32861, mapId), small_boss, 1, 2));
				break;
			}
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190098, 15, 4));
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190099, 15, 4));
			//
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190101, 15, 7));
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190100, 15, 7));
			//
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190102, 15, 0));
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190103, 15, 0));
			//
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190104, 15, 2));
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190105, 15, 2));
			npclist.addAll(spawn());
			Thread.sleep(5000L);
			sendMsg("$17703");
			isKillNpc(npclist);
			Thread.sleep(1000);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND_NUMBER, 3, 3));
			type = 0;
			npclist = new ArrayList<L1NpcInstance>();
			small_boss = 190106 + Random.nextInt(4);
			switch (small_boss) {
			case 190106:
				sendMsg("$17941");
				npclist.addAll(spawn(new L1Location(32800, 32845, mapId), small_boss, 1, 4));
				break;
			case 190107:
				sendMsg("$17944");
				npclist.addAll(spawn(new L1Location(32817, 32862, mapId), small_boss, 1, 7));
				break;
			case 190108:
				sendMsg("$17942");
				npclist.addAll(spawn(new L1Location(32801, 32878, mapId), small_boss, 1, 0));
				break;
			case 190109:
				sendMsg("$17943");
				npclist.addAll(spawn(new L1Location(32785, 32861, mapId), small_boss, 1, 2));
				break;
			default:
				sendMsg("$17943");
				npclist.addAll(spawn(new L1Location(32785, 32861, mapId), small_boss, 1, 2));
				break;
			}
			sendMsg("$17969");
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190098, 15, 4));
			// npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190099, 15, 4));
			//
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190101, 15, 7));
			// npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190100, 15, 7));
			//
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190102, 15, 0));
			// npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190103, 15, 0));
			//
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190104, 15, 2));
			// npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190105, 15, 2));
			npclist.addAll(spawn());
			Thread.sleep(5000L);
			sendMsg("$17703");
			isKillNpc(npclist);
			Thread.sleep(1000);
			npclist = new ArrayList<L1NpcInstance>();
			small_boss = Random.nextInt(4);
			switch (small_boss) {
			case 0:
				npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190112, 1, 4));
				break;
			case 1:
				npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190112, 1, 7));
				break;
			case 2:
				npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190112, 1, 0));
				break;
			case 3:
				npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190112, 1, 2));
				break;
			default:
				npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190112, 1, 2));
				break;
			}
			sendMsg("$17995:$17713");
			npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190098, 10, 4));
			npclist.addAll(spawn(new L1Location(32800, 32845, mapId), 190099, 10, 4));

			npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190101, 10, 7));
			npclist.addAll(spawn(new L1Location(32817, 32862, mapId), 190100, 10, 7));

			npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190102, 10, 0));
			npclist.addAll(spawn(new L1Location(32801, 32878, mapId), 190103, 10, 0));

			npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190104, 10, 2));
			npclist.addAll(spawn(new L1Location(32785, 32861, mapId), 190105, 10, 2));
			Thread.sleep(5000);
			isKillNpc(npclist);
			sendMsg("$17707");
			npc.transform(190115);
			Thread.sleep(3000L);
			sendMsg("$17708");
			Thread.sleep(3000L);
			sendMsg("$17709");
			Thread.sleep(3000L);
			sendMsg("$17710");
			Thread.sleep(3000L);
			sendMsg("$17712");
			Thread.sleep(3000L);
			npc.broadcastPacketAll(new S_SkillSound(npc.getId(), 169));
			drop();
			npc.deleteMe();
			sendMsg("$17962");
			Thread.sleep(10000L);
			quitCentralTempl();
		} catch (InterruptedException e) {
			// TODO 自動生成的 catch 塊
			// e.printStackTrace();
		}
	}

	/**
	 * 是否在規定時間內清除怪物
	 * */
	private int isKillNpc(ArrayList<L1NpcInstance> list) {
		int count = 0;
		while (count++ < 300) {
			if (!isCentralTempl()) {
				quitCentralTempl();
				return -1;
			}
			boolean isAllDeath = true;
			for (L1NpcInstance npc : list) {
				// if (!npc.isDead()) {
				// break;
				// }
				if (type == 0 && (npc.getNpcId() >= 190106 && npc.getNpcId() <= 190109)) {
					sendMsg("$17968");
					pc.getInventory().storeItem(640355, 1);
					type = 1;
				}
				if (!npc.isDead()) {
					isAllDeath = false;
					break;
				}
				// isAllDeath = npc.isDead();
			}
			if (isAllDeath) {
				return count;
			}
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO 自動生成的 catch 塊
				// e.printStackTrace();
			}
		}
		sendMsg("$17714");
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e) {
			// TODO 自動生成的 catch 塊
			// e.printStackTrace();
		}
		sendMsg("$17715");
		npc.deleteMe();
		isCentralTempl();
		quitCentralTempl();
		return -1;
	}

	/** 掉落道具 */
	private void drop() {
		int[] drop1 = { 40087, 40074, 140087, 140074, 240087, 240074 };
		int[] drop2 = { 264, 262, 260, 263, 261, 326, 337, 336, 328, 329, 21152, 21154, 21153, 21155 };
		if (Random.nextInt(100) > 10) {
			L1ItemInstance item = ItemTable.get().createItem(drop1[Random.nextInt(drop1.length)]);
			World.get().getInventory(npc.getLocation()).storeItem(item);
		} else {
			L1ItemInstance item = ItemTable.get().createItem(drop2[Random.nextInt(drop2.length)]);
			World.get().getInventory(npc.getLocation()).storeItem(item);
		}
		L1ItemInstance item = ItemTable.get().createItem(640353);
		// npc.getInventory().insertItem(item);
		World.get().getInventory(npc.getLocation()).storeItem(item);
	}

	/**
	 * 判斷玩家是否在中央寺廟副本
	 * 
	 * @return 不在副本中返回false
	 * @return 在副本中返回true
	 * */
	private boolean isCentralTempl() {
		if (pc == null) {
			return false;
		}
		if (pc.getOnlineStatus() == 0) {
			return false;
		}
		if (pc.getMapId() != mapId) {
			return false;
		}
		if (pc.isDead()) {
			pc.sendPackets(new S_NpcChatPacket(pc, "$18636", 21));
			try {
				Thread.sleep(2000L);
			} catch (InterruptedException e) {
			}
			pc.sendPackets(new S_NpcChatPacket(pc, "$18637", 21));
			quitCentralTempl();
			return false;
		}
		return true;
	}

	/** 退出中央寺廟副本 */
	private void quitCentralTempl() {
		if (pc != null) {
			L1ItemInstance[] itemlist = pc.getInventory().findItemsId(640354);// 魔法魔杖(火風暴)
			// L1ItemInstance item = pc.getInventory().findItemId(640354);
			for (int i = 0; i < itemlist.length; i++) {
				pc.getInventory().removeItem(itemlist[i]);
			}
			itemlist = pc.getInventory().findItemsId(640355);// 魔法魔杖(淨化)
			for (int i = 0; i < itemlist.length; i++) {
				pc.getInventory().removeItem(itemlist[i]);
			}
			if (pc.getMapId() == mapId) {
				try {
					pc.sendPackets(new S_ServerMessage(1476));
					Thread.sleep(10000L);
					pc.sendPackets(new S_ServerMessage(1477));
					Thread.sleep(10000L);
					pc.sendPackets(new S_ServerMessage(1478));
					Thread.sleep(5000L);
					pc.sendPackets(new S_ServerMessage(1480));
					Thread.sleep(1000L);
					pc.sendPackets(new S_ServerMessage(1481));
					Thread.sleep(1000L);
					pc.sendPackets(new S_ServerMessage(1482));
					Thread.sleep(1000L);
					pc.sendPackets(new S_ServerMessage(1483));
					Thread.sleep(1000L);
					pc.sendPackets(new S_ServerMessage(1484));
				} catch (InterruptedException e) {
				}
				// pc.setSoulTower(0);
				// pc.stopTimeMap();
				L1Teleport.teleport(pc, 33703, 32502, (short) 4, 5, true);
			}

		}

		World.get().closeMap((int) mapId);
		L1CentralTemple.get().mapStat[mapId - 1936] = false;
		//_log.info("中央寺廟副本結束：mapid:" + mapId + "線程結束.");
		this.interrupt();
	}

	/**
	 * 召喚NPC
	 * 
	 * @param npcid
	 *            需要召喚的NPCID
	 * @param count
	 *            召喚NPC的數量
	 * @throws InterruptedException
	 * */
	private ArrayList<L1NpcInstance> spawn(L1Location loc, int npcid, int count, int heading) throws InterruptedException {
		ArrayList<L1NpcInstance> list = new ArrayList<L1NpcInstance>();
		if (count > 1) {
			for (int i = 0; i < count; i++) {
				list.add(spawnNpc(loc, npcid, 0, heading));
				Thread.sleep(10);
			}
		} else {
			list.add(spawnNpc(loc, npcid, 0, heading));
		}
		return list;
	}

	/** 召喚NPC */
	private L1NpcInstance spawnNpc(L1Location loc, int npcid, int randomRange, int heading) {
		L1Npc l1npc = NpcTable.get().getTemplate(npcid);
		L1NpcInstance field = null;
		if (l1npc == null) {
			_log.error("召喚的NPCID:" + npcid + "不存在");
			return null;
		}
		field = NpcTable.get().newNpcInstance(npcid);
		field.setId(IdFactory.get().nextId());
		field.setMap((short) loc.getMapId());
		// int tryCount = 0;
		// do {
		// tryCount++;
		// field.setX(loc.getX() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
		// field.setY(loc.getY() + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
		// if (field.getMap().isInMap(field.getLocation()) && field.getMap().isPassable(field.getLocation(), field)) {
		// // System.out.println("X坐標："+field.getX()+"，Y坐標："+field.getY()+",坐標狀態："+field.getMap().isPassable(field.getLocation(), field));
		// break;
		// }
		// try {
		// Thread.sleep(2);
		// } catch (InterruptedException e) {
		// }
		// } while (tryCount < 50);
		//
		// if (tryCount >= 50) {
		// field.getLocation().set(loc);
		// }
		field.getLocation().set(loc);
		field.setHomeX(field.getX());
		field.setHomeY(field.getY());
		field.setHeading(heading);
		field.setLightSize(l1npc.getLightSize());
		field.setLightSize(0);
		// L1WorldMap.get().getMap(mapId).setPassable(field.getLocation(), false);
		World.get().storeObject(field);
		World.get().addVisibleObject(field);
		// if(field instanceof L1MonsterInstance){
		// field.onNpcAI();
		// }
		return field;
	}

	/** 召喚景觀NPC */
	private L1FieldObjectInstance spwanField(int gfxid, int x, int y, int mapid) {
		final L1FieldObjectInstance field = (L1FieldObjectInstance) NpcTable.get().newNpcInstance(190000);
		if (field != null) {
			field.setId(IdFactoryNpc.get().nextId());
			field.setGfxId(gfxid);
			field.setTempCharGfx(gfxid);
			field.setMap((short) mapid);
			field.setX(x);
			field.setY(y);
			field.setHomeX(x);
			field.setHomeY(y);
			field.setHeading(5);
			World.get().storeObject(field);
			World.get().addVisibleObject(field);
			// _fieldList.put(new Integer(field.getId()), scenery);
		}
		return field;
	}

	private void sendMsg(String msg) {
		pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_COLOR_MESSAGE, 2, msg));
	}

	private ArrayList<L1NpcInstance> spawn() {
		ArrayList<L1NpcInstance> npclist = new ArrayList<L1NpcInstance>();
		try {
			npclist.addAll(spawn(new L1Location(32800, 32852, mapId), 190098, 5, 4));
			npclist.addAll(spawn(new L1Location(32801, 32852, mapId), 190099, 5, 4));

			npclist.addAll(spawn(new L1Location(32810, 32862, mapId), 190101, 5, 7));
			npclist.addAll(spawn(new L1Location(32810, 32863, mapId), 190100, 5, 7));

			npclist.addAll(spawn(new L1Location(32801, 32873, mapId), 190102, 5, 0));
			npclist.addAll(spawn(new L1Location(32800, 32873, mapId), 190103, 5, 0));

			npclist.addAll(spawn(new L1Location(32789, 32861, mapId), 190104, 5, 2));
			npclist.addAll(spawn(new L1Location(32789, 32862, mapId), 190105, 5, 2));
			Thread.sleep(5000);
			npclist.addAll(spawn(new L1Location(32800, 32852, mapId), 190098, 5, 4));
			npclist.addAll(spawn(new L1Location(32801, 32852, mapId), 190099, 5, 4));

			npclist.addAll(spawn(new L1Location(32810, 32862, mapId), 190101, 5, 7));
			npclist.addAll(spawn(new L1Location(32810, 32863, mapId), 190100, 5, 7));

			npclist.addAll(spawn(new L1Location(32801, 32873, mapId), 190102, 5, 0));
			npclist.addAll(spawn(new L1Location(32800, 32873, mapId), 190103, 5, 0));

			npclist.addAll(spawn(new L1Location(32789, 32861, mapId), 190104, 5, 2));
			npclist.addAll(spawn(new L1Location(32789, 32862, mapId), 190105, 5, 2));
			Thread.sleep(5000);
			npclist.addAll(spawn(new L1Location(32800, 32852, mapId), 190098, 5, 4));
			npclist.addAll(spawn(new L1Location(32801, 32852, mapId), 190099, 5, 4));

			npclist.addAll(spawn(new L1Location(32810, 32862, mapId), 190101, 5, 7));
			npclist.addAll(spawn(new L1Location(32810, 32863, mapId), 190100, 5, 7));

			npclist.addAll(spawn(new L1Location(32801, 32873, mapId), 190102, 5, 0));
			npclist.addAll(spawn(new L1Location(32800, 32873, mapId), 190103, 5, 0));

			npclist.addAll(spawn(new L1Location(32789, 32861, mapId), 190104, 5, 2));
			npclist.addAll(spawn(new L1Location(32789, 32862, mapId), 190105, 5, 2));
		} catch (InterruptedException e) {
			// TODO 自動生成的 catch 塊
			// e.printStackTrace();
		}
		return npclist;
	}
}
