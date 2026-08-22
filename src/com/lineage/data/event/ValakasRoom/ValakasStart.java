package com.lineage.data.event.ValakasRoom;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.DoorSpawnTable;
import com.lineage.server.model.Broadcaster;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_MapTimer;
import com.lineage.server.serverpackets.S_NpcChatPacket;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import com.mchange.v2.holders.ThreadSafeIntHolder;

/** 火龍副本線程 */

public class ValakasStart implements Runnable {  //src022
	private short _map;
	private int stage = 1;
	private static final int FIRST_STEP = 1;
	private static final int WAIT_RAID = 2;
	/** BOSS出現 */
	private static final int VALAKAS = 3;
	private static final int END = 4;

	private L1NpcInstance death;
	private L1NpcInstance ifrit;
	private L1NpcInstance phoenix;
	/** 第4只BOSS */
	private L1NpcInstance valakas;
	private L1NpcInstance leo1;
	private L1NpcInstance leo2;
	private L1NpcInstance leo3;

	private L1DoorInstance briddge1;
	private L1DoorInstance briddge2;
	private L1DoorInstance briddge3;

	private L1PcInstance pc;

	private boolean Running = true;

	public CopyOnWriteArrayList<L1NpcInstance> BasicNpcList;
	private CopyOnWriteArrayList<L1NpcInstance> NpcList;
	public CopyOnWriteArrayList<L1NpcInstance> BossList;

	public ValakasStart(int id, L1PcInstance pc) {
		_map = (short) id;
		this.pc = pc;
	}

	@Override
	public void run() {
		setting();
		int i=0;
		NpcList = ValakasRoomSpawn.getInstance().fillSpawnTable(_map, 1, true);
		pc.sendPackets(new S_MapTimer(1800));
		pc.ValakasInnEndTel();//副本時間軸
		while (Running) {
			try {
				Sleep(300);
				checkPc();

				if (NpcList != null) {
					for (L1NpcInstance npc : NpcList) {
						if (npc == null || npc.isDead())
							NpcList.remove(npc);
					}
				}
				// (4070019);
				//if (leo1 != null && leo1.isDead() && ((briddge1.getOpenStatus() == ActionCodes.ACTION_Close))) {
				if (leo1 != null && leo1.isDead() && !leo1.isDeathProcessing() && ((briddge1.getOpenStatus() == ActionCodes.ACTION_Close))) {
					NpcList.remove(leo1);
					briddge1.open();
					
				}
				// (4070020);
				//if (leo2 != null && leo2.isDead() && ((briddge2.getOpenStatus() == ActionCodes.ACTION_Close))) {
				if (leo2 != null && leo2.isDead() && !leo2.isDeathProcessing()  && ((briddge2.getOpenStatus() == ActionCodes.ACTION_Close))) {
					NpcList.remove(leo2);
					briddge2.open();
					
				}
				// (4070021);
				//if (leo3 != null && leo3.isDead() && ((briddge3.getOpenStatus() == ActionCodes.ACTION_Close))) {
				if (leo3 != null && leo3.isDead() && !leo3.isDeathProcessing()  && ((briddge3.getOpenStatus() == ActionCodes.ACTION_Close))) {
					NpcList.remove(leo3);
					briddge3.open();
					
				}
				// if (BossList != null && valakas != null && pc.isInValakasBoss) {
				// stage = VALAKAS;
				// }
				if ((leo3 == null || leo3.isDead()) && (leo2 == null || leo2.isDead()) && (leo1 == null || leo1.isDead())) {
					stage = VALAKAS;
				}

				if (ifrit != null && ifrit.isDead()) {
					BossList.remove(ifrit);
					stage = END;
				}
				if (phoenix != null && phoenix.isDead()) {
					BossList.remove(phoenix);
					stage = END;
				}
				if (valakas != null && valakas.isDead()) {
					BossList.remove(valakas);
					stage = END;
				}

				switch (stage) {
				case FIRST_STEP:
					Thread.sleep(2000);
					pc.sendPackets(new S_NpcChatPacket(death, "$18644"));
					Thread.sleep(3000);
					pc.sendPackets(new S_NpcChatPacket(death, "$18645"));
					Thread.sleep(3000);
					pc.sendPackets(new S_NpcChatPacket(death, "$18646"));
					Thread.sleep(3000);
					pc.sendPackets(new S_NpcChatPacket(death, "$18647"));
					stage = WAIT_RAID;
					break;
				case WAIT_RAID:
					//if(i>=240 && _map==pc.getMapId()){
					if(i>=1800 && _map==pc.getMapId()){
						pc.sendPackets(new S_ServerMessage("閒置時間過長,副本強制關閉"));
						System.out.println(pc.getName()+" 閒置時間過長,副本強制關閉 mapId:"+_map);
						stage=END;
					}else {
						i++;
					}
					break;
				case VALAKAS:
					L1NpcInstance boss = null;
					if (ifrit != null) {
						boss = ifrit;
					} else if (phoenix != null) {
						boss = phoenix;
					} else if (valakas != null) {
						boss = valakas;
					}
					//System.out.println("pc.ValakasStatus:"+pc.ValakasStatus+" "+valakas);
					if (pc.ValakasStatus==2 && valakas!=null && boss.getLocation().getLineDistance(pc.getLocation()) <= 10) {
						//System.out.println("test");
						pc.sendPackets(new S_NpcChatPacket(valakas, "$18869"));
						Thread.sleep(2000);
						if (pc.getMapId() == _map) {
							pc.sendPackets(new S_NpcChatPacket(pc, "$18870"));
						}else{
							endRaid();
						}

						Thread.sleep(2000);
						if (pc.getMapId() == _map) {
						pc.sendPackets(new S_NpcChatPacket(valakas, "$18871"));
						}else{
							endRaid();
						}
						Thread.sleep(2000);
						if (pc.getMapId() == _map) {
							pc.sendPackets(new S_NpcChatPacket(pc, "$18872"));
						}else{
							endRaid();
						}
						Thread.sleep(2000);
						if (pc.getMapId() == _map) {
						pc.sendPackets(new S_NpcChatPacket(valakas, "$18873"));
						}else{
							endRaid();
						}
						Thread.sleep(2000);
						pc.sendPackets(new S_NpcChatPacket(valakas, "$18874"));
						
					}
					pc.ValakasStatus =3;
					stage = WAIT_RAID;
					break;
				case END:
					Thread.sleep(5000);
					if (pc.getMapId() == _map) {
						pc.sendPackets(new S_ServerMessage(1478));
						
					}
					Thread.sleep(5000);
					if (pc.getMapId() == _map) {
						pc.sendPackets(new S_ServerMessage(1480));
						
					}
					Thread.sleep(1000);
					if (pc.getMapId() == _map) {
						pc.sendPackets(new S_ServerMessage(1481));
						
					}
					Thread.sleep(1000);
					if (pc.getMapId() == _map) {
						pc.sendPackets(new S_ServerMessage(1482));
						
					}
					Thread.sleep(1000);
					if (pc.getMapId() == _map) {
						pc.sendPackets(new S_ServerMessage(1483));
						
					}
					Thread.sleep(1000);
					if (pc.getMapId() == _map) {
						pc.sendPackets(new S_ServerMessage(1484));
						
					}
					Thread.sleep(1000);
					if (pc.getMapId() == _map) {
						L1Teleport.teleport(pc, 33705, 32504, (short) 4, 5, true);// 傳送至威頓村
						
					}
					endRaid();
					break;
				default:
					break;
				}
			} catch (Exception e) {
			} finally {
				try {
					Thread.sleep(1500);
				} catch (Exception e) {
				}
			}
		}
		endRaid();
	}

	public void Start() {
		GeneralThreadPool.get().execute(this);
		System.out.println(pc.getName()+" [開始]火龍副本開始: " + _map);
	}

	private void Sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
		}
	}

	private void setting() {
		for (L1NpcInstance npc : BasicNpcList) {
			if (npc != null) {
				if (npc.getNpcId() == 46165) {//奄奄一息的死亡騎士
					death = npc;
				}
				if (npc.getNpcId() == 4070009) {//第一關卡BOSS
					leo1 = npc;
				}
				if (npc.getNpcId() == 4070008) {//第二關卡BOSS
					leo2 = npc;
				}
				if (npc.getNpcId() == 4070010) {//第三關卡BOSS
					leo3 = npc;
				}
				//副本中 橋的判斷
				if (npc instanceof L1DoorInstance) {
					L1DoorInstance door = (L1DoorInstance) npc;
					if (door.getDoorId() == 4070019) {//第一關卡橋
						briddge1 = door;
					}
					if (door.getDoorId() == 4070020) {//第二關卡橋
						briddge2 = door;
					}
					if (door.getDoorId() == 4070021) {//第三關卡橋
						briddge3 = door;
					}
					
				}
			}
		}

		for (L1NpcInstance npc : BossList) {
			if (npc != null) {
				
				if (npc.getNpcId() == 4070082) { // 伊弗利特
					ifrit = npc;
				}
				if (npc.getNpcId() == 4070080) { // 不死鳥
					phoenix = npc;
				}
				if (npc.getNpcId() == 4070081) { // 巴拉卡斯
					valakas = npc;
				}
				
			}
		}
	}

	private void checkPc() {

		if(pc==null||pc.getOnlineStatus()==0){
			endRaid();
			return;
		}
		if(_map!=pc.getMapId()){
			endRaid();
			return;
		}
	}

	private void endRaid() {
		if (Running) {
			Collection<L1Object> cklist = World.get().getVisibleObjects(_map).values();
			for (L1Object ob : cklist) {
				if (ob == null){
					continue;
				}
				if (ob instanceof L1ItemInstance) {
					L1ItemInstance obj = (L1ItemInstance) ob;
					L1Inventory groundInventory = World.get().getInventory(obj.getX(), obj.getY(), obj.getMapId());
					groundInventory.removeItem(obj);
				} else if (ob instanceof L1DoorInstance) {
					((L1DoorInstance) ob).deleteMe();
				} else if (ob instanceof L1FieldObjectInstance) {
					((L1FieldObjectInstance) ob).deleteMe();
				} else if (ob instanceof L1NpcInstance) {
					((L1NpcInstance) ob).deleteMe();
				}
			}
			
			if (pc != null && pc.getOnlineStatus() == 1) {
				pc.getInventory().consumeItem(5010); //SRC0712 結束副本取消變身
			}
			L1PolyMorph.undoPoly(pc);
			ValakasRoomSystem.getInstance().removeStart(_map);
			System.out.println(pc.getName()+" [火窟副本主要任務地圖完成,釋放地圖完成 ]" + _map);
						
			Running = false;
			pc = null;
			death = null;
			ifrit = null;
			phoenix = null;
			valakas = null;
			leo1 = null;
			leo2 = null;
			leo3 = null;
			DoorSpawnTable.get().removeDoor(briddge1);
			DoorSpawnTable.get().removeDoor(briddge2);
			DoorSpawnTable.get().removeDoor(briddge3);
			briddge1 = null;
			briddge2 = null;
			briddge3 = null;
			BasicNpcList = null;
			NpcList = null;
			BossList = null;
			pc.ValakasStatus = 0;
			
			
		}
	}
}
