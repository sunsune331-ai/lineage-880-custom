package com.lineage.server.model.Instance;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;

public class L1TeleporterInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	private static final Log _log = LogFactory.getLog(L1TeleporterInstance.class);

	private boolean _isNowDely = false;

	public L1TeleporterInstance(L1Npc template) {
		super(template);
	}

	public void onAction(L1PcInstance pc) {
		try {
			L1AttackMode attack = new L1AttackPc(pc, this);

			attack.action();
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void onTalkAction(L1PcInstance player) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(getNpcTemplate().get_npcId());
		int npcid = getNpcTemplate().get_npcId();
		L1PcQuest quest = player.getQuest();
		String htmlid = null;

		if (talking != null) {
			if (npcid == 50001) {
				if (player.isElf())
					htmlid = "barnia3";
				else if ((player.isKnight()) || (player.isCrown()))
					htmlid = "barnia2";
				else if ((player.isWizard()) || (player.isDarkelf())) {
					htmlid = "barnia1";
				}

			}

			if (htmlid != null) {
				player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
			} else if (player.getLawful() < -1000) {
				player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
			} else
				player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
		}
	}

	public void onFinalAction(L1PcInstance player, String action) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(getNpcTemplate().get_npcId());
		if (action.equalsIgnoreCase("teleportURL")) {
			L1NpcHtml html = new L1NpcHtml(talking.getTeleportURL());
			
            /*String[] price = null;
            int npcid = getNpcTemplate().get_npcId();
            switch (npcid) {
                case 50015: // 說話之島 傳送師^盧卡斯
                    price = new String[] { "1500" };
                    break;
                case 50017: // 說話之島 傳送師^凱瑟
                    price = new String[] { "50" };
                    break;
                case 50020: // 史坦利
                    price = new String[] { "50", "50", "120", "120", "50", "180", "120", "120", "180", "200", "200", "420", "600", "1155", "7100" };
                    break;
                case 50024: // 史提夫
                    price = new String[] { "132", "55", "198", "55", "132", "264", "55", "7777", "7777", "198", "264", "220", "220", "420", "550", "1155", "7480" };
                    break;
                case 50036: // 爾瑪 4070083
                    price = new String[] { "126", "126", "52", "189", "52", "52", "189", "126", "126", "315", "315", "420", "735", "1155", "875" };
                    break;
                case 50039:  // 萊思利
                    price = new String[] { "185", "185", "123", "247", "51", "123", "247", "51", "185", "420", "412", "412", "824", "1155", "7931" };
                    break;
                case 50044:  // 西裡烏斯
                case 50046:  // 艾勒裡斯
                    price = new String[] { "259", "129", "194", "129", "54", "324", "194", "259", "420", "450", "540", "540", "972", "1155", "7992" };
                    break;
                case 50051: // 吉利烏斯
                    price = new String[] { "240", "240", "180", "300", "120", "180", "300", "50", "240", "420", "500", "500", "900", "1155", "8000" };
                    break;
                case 50054: // 特萊
                    price = new String[] { "50", "50", "120", "120", "180", "180", "180", "240", "240", "300", "200", "200", "420", "500", "6500" };
                    break;
                case 50056:  // 麥特
                    price = new String[] { "55", "55", "55", "132", "132", "132", "198", "198", "270", "7777", "7777", "246", "420", "770", "7480" };
                    break;
                case 50066: // 裡歐
                    price = new String[] { "180", "50", "120", "120", "50", "50", "240", "120", "180", "420", "400", "400", "800", "1155", "7100" };
                    break;
                case 50068:  // 迪亞諾斯
                    price = new String[] { "1500", "800", "600", "1800", "1800", "1000", "300" };  /////////////////////
                    break;
                case 50072: // 迪亞路哲
                    price = new String[] { "2200", "1800", "1000", "1600", "2200", "1200", "1300", "2000", "2000" };
                    break;
                case 50073: // 迪雅貝斯
                    price = new String[] { "380", "850", "290", "290", "290", "180", "480", "150", "150", "380", "480", "380", "850" };
                    break;
                case 50079: // 丹尼爾
                    price = new String[] { "550", "550", "600", "550", "700", "600", "600", "750", "750", "550", "550", "700", "650" };
                    break;
                case 3000005: // 
                    price = new String[] { "50", "50", "50", "50", "120", "120", "180", "180", "180", "240", "240", "400", "400", "800", "7700" };
                    break;
                case 3100005: // 
                    price = new String[] { "50", "50", "50", "120", "180", "180", "240", "240", "240", "300", "300", "500", "500", "900", "8000" };
                    break;
                case 50026: // 商店村傳送師(古魯丁)
                    price = new String[] { "0", "0", "0" };
                    break;
                case 50033: // 商店村傳送師(奇岩)
                    price = new String[] { "0", "0", "0" };
                    break;
                case 50049:  // 商店村傳送師(歐瑞)
                    price = new String[] { "0", "0", "0" };
                    break;
                case 50059:  // 商店村傳送師(騎士村)
                    price = new String[] { "0", "0", "0" };
                    break;
                case 6000014:  //侍從長
                    price = new String[] { "14000" };
                    break;
                case 6000016:  // 信女
                    price = new String[] { "1000" };
                    break;
                case 900056: //象牙塔
                    price = new String[] { "7000", "7000", "7000", "14000", "14000" };
                    break;
                case 5069: // 
                    price = new String[] { "82", "82", "82", "198", "198", "198", "198", "297", "297", "495", "495", "495", "1155", "12210" };
                    break;
                case 900057:
                    break;
                case 5091: // [妖精森]
                    price = new String[] { "57", "57", "57", "138", "138", "138", "138", "207", "207", "230", "230", "690" };
                    break;
                default:
                    price = new String[] { "" };
                    break;
            }
            //player.sendPackets(new S_NPCTalkReturn(objid, html, price));*/
			
			player.sendPackets(new S_NPCTalkReturn(objid, html));
		} else if (action.equalsIgnoreCase("teleportURLA")) {
			L1NpcHtml html = new L1NpcHtml(talking.getTeleportURLA());
			player.sendPackets(new S_NPCTalkReturn(objid, html));
		}

		if (action.startsWith("teleport "))
			doFinalAction(player, action);
	}

	private void doFinalAction(L1PcInstance player, String action) {
		int objid = getId();

		int npcid = getNpcTemplate().get_npcId();
		String htmlid = null;
		boolean isTeleport = true;

		if (npcid == 50043) {
			if (_isNowDely) {
				isTeleport = false;
			}
		} else if ((npcid == 50625) && (_isNowDely)) {
			isTeleport = false;
		}

		if (isTeleport)
			try {
				if (action.equalsIgnoreCase("teleport 29")) {
					L1PcInstance kni = null;
					L1PcInstance elf = null;
					L1PcInstance wiz = null;

					if ((kni != null) && (elf != null) && (wiz != null)) {
						L1Teleport.teleport(player, 32723, 32850, (short) 2000, 2, true);
						L1Teleport.teleport(kni, 32750, 32851, (short) 2000, 6, true);
						L1Teleport.teleport(elf, 32878, 32980, (short) 2000, 6, true);
						L1Teleport.teleport(wiz, 32876, 33003, (short) 2000, 0, true);
						TeleportDelyTimer timer = new TeleportDelyTimer();
						GeneralThreadPool.get().execute(timer);
					}
				} else if (action.equalsIgnoreCase("teleport barlog")) {
					L1Teleport.teleport(player, 32755, 32844, (short) 2002, 5, true);
					TeleportDelyTimer timer = new TeleportDelyTimer();
					GeneralThreadPool.get().execute(timer);
				}
			} catch (Exception localException) {
			}
		if (htmlid != null)
			player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
	}

	class TeleportDelyTimer implements Runnable {
		public TeleportDelyTimer() {
		}

		public void run() {
			try {
				_isNowDely = true;
				Thread.sleep(900000L);
			} catch (Exception e) {
				_isNowDely = false;
			}
			_isNowDely = false;
		}
	}

}
