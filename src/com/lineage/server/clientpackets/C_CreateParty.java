package com.lineage.server.clientpackets;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.config.ConfigOther;
import com.lineage.data.event.CampSet;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * 要求邀請加入隊伍(要求創立隊伍)
 * @author daien
 */
public class C_CreateParty extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_CreateParty.class);

	/*
	 * public C_CreateParty() { }
	 * 
	 * public C_CreateParty(final byte[] abyte0, final ClientExecutor client) {
	 * super(abyte0); try { this.start(abyte0, client);
	 * 
	 * } catch (final Exception e) { _log.error(e.getLocalizedMessage(), e); } }
	 */

	@Override
	public void start(final byte[] decrypt, final ClientExecutor client) {
		try {
			// 資料載入
			this.read(decrypt);

			final L1PcInstance pc = client.getActiveChar();

			if (pc.isGhost()) { // 鬼魂模式
				return;
			}

			if (pc.isDead()) { // 死亡
				return;
			}

			if (pc.isTeleport()) { // 傳送中
				return;
			}

			final int type = this.readC();

			switch (type) {
			case 0:
			case 1:
		    case 4:
            case 5:
				// (自動分配ON/OFF異)
				if (type == 0 || type == 1 || type == 4 || type == 5) {// 0.?? // 1.??
					int targetId = 0;
					L1Object temp = null;
					if (type == 4 || type == 5) {
						String name = readS();

						// 是否開放給其他人看見[陣營稱號]和[轉生稱號] by terry0412
						if (ConfigOther.SHOW_SP_TITLE) {// src009
							// 去除陣營
							/*if (CampSet.CAMPSTART) {
								int i = 1;
								int j = 1;
								L1Name_Power typename = C1_Name_Type_Table.get().get(i, j);
								while (typename != null) {
									typename = C1_Name_Type_Table.get().get(i, j);
									while (typename != null) {
										typename = C1_Name_Type_Table.get().get(i, j);
										if (typename != null) {
											name = name.replace(typename.get_c1_name_type(), "");
											j++;
										}
									}
									i++;
									j = 1;
								}
							}

							// 去除轉身
							int i = 1; // LEVEL
							int j = 0; // TYPE
							L1MeteAbility MeteAbility = ExtraMeteAbilityTable.getInstance().get(i, j);
							while (MeteAbility != null) {
								MeteAbility = ExtraMeteAbilityTable.getInstance().get(i, j);
								while (MeteAbility != null) {
									MeteAbility = ExtraMeteAbilityTable.getInstance().get(i, j);
									if (MeteAbility != null) {
										name = name.replace(MeteAbility.getTitle(), "");
										i++;
									}
								}
								j++;
								i = 1;
							}*/
							for (Iterator<L1Object> iterator = World.get()
									.getVisibleObjects(pc, Config.PC_RECOGNIZE_RANGE).iterator(); iterator.hasNext();) {
								L1Object visible = iterator.next();
								if (visible instanceof L1PcInstance) {
									L1PcInstance PartyPc = (L1PcInstance) visible;
									// 陣營
									if (CampSet.CAMPSTART) {
										if ((PartyPc.get_c_power() != null) && (PartyPc.get_c_power().get_c1_type() != 0)) {
											final String c_power_name = PartyPc.get_c_power().get_power().get_c1_name_type();
											name = name.replace(c_power_name, ""); // 去除陣營
										}
									}
									// 轉生
									if (PartyPc.getMeteAbility() != null) {
										final String mete_ability_name = PartyPc.getMeteAbility().getTitle();
										name = name.replace(mete_ability_name, ""); // 去除轉身
									}
								}
							}
						}

						temp = World.get().getPlayer(name);

					} else {
						targetId = readD();
						temp = World.get().findObject(targetId);
					}

					// 不是人物
					if (!(temp instanceof L1PcInstance)) {
						return;
					}

					if (temp instanceof L1PcInstance) {
						final L1PcInstance targetPc = (L1PcInstance) temp;
						if (pc.getId() == targetPc.getId()) {
							return;
						}

						if ((!pc.getLocation().isInScreen(targetPc.getLocation())
								|| (pc.getLocation().getTileLineDistance(targetPc.getLocation()) > 7))) {
							// 邀請組隊時，對像不再螢幕內或是7步內
							pc.sendPackets(new S_ServerMessage(952));
							return;
						}

						if (targetPc.isInParty()) {
							// 您無法邀請已經參加其他隊伍的人。
							pc.sendPackets(new S_ServerMessage(415));
							return;
						}

						if (pc.isInParty()) {
							if (pc.getParty().isLeader(pc)) {
								targetPc.setPartyID(pc.getId());
								// 玩家 %0%s 邀請您加入隊伍？(Y/N)
								targetPc.sendPackets(new S_Message_YN(953, pc.getName()));

							} else {
								// 只有領導者才能邀請其他的成員。
								pc.sendPackets(new S_ServerMessage(416));
							}

						} else {
							targetPc.setPartyID(pc.getId());
							// 玩家 %0%s 邀請您加入隊伍？(Y/N)
							targetPc.sendPackets(new S_Message_YN(953, pc.getName()));
						}
					}
				}
				break;

			case 2: // 
				final String name = this.readS();
				final L1PcInstance targetPc = World.get().getPlayer(name);
				if (targetPc == null) {
					// 沒有叫%0的人。
					pc.sendPackets(new S_ServerMessage(109));
					return;
				}

				if (pc.getId() == targetPc.getId()) {
					return;
				}

				if ((!pc.getLocation().isInScreen(targetPc.getLocation())
						|| (pc.getLocation().getTileLineDistance(targetPc.getLocation()) > 7))) {
					// 邀請組隊時，對像不再螢幕內或是7步內
					pc.sendPackets(new S_ServerMessage(952));
					return;
				}

				if (targetPc.isInChatParty()) {
					// 您無法邀請已經參加其他隊伍的人。
					pc.sendPackets(new S_ServerMessage(415));
					return;
				}

				if (pc.isInChatParty()) {
					if (pc.getChatParty().isLeader(pc)) {
						targetPc.setPartyID(pc.getId());
						// 您要接受玩家 %0%s 提出的隊伍對話邀請嗎？(Y/N)
						targetPc.sendPackets(new S_Message_YN(951, pc.getName()));

					} else {
						// 只有領導者才能邀請其他的成員。
						pc.sendPackets(new S_ServerMessage(416));
					}

				} else {
					targetPc.setPartyID(pc.getId());
					// 您要接受玩家 %0%s 提出的隊伍對話邀請嗎？(Y/N)
					targetPc.sendPackets(new S_Message_YN(951, pc.getName()));
				}
				break;

			case 3:// 隊長轉移
				L1Party part = pc.getParty();
				if (part == null) {
					return;
				}
				if (!part.isLeader(pc)) {
					// 不是隊長時, 不可使用
					pc.sendPackets(new S_ServerMessage(1697));
					return;
				}
				// 取得目標物件編號
				int targetId = readD();
				// 嘗試取得目標
				L1Object temp = World.get().findObject(targetId);

				if (temp == null) {
					pc.sendPackets(new S_ServerMessage(1694));// 沒有選擇目標
					return;
				}
				if (temp instanceof L1PcInstance) {
					L1PcInstance member = (L1PcInstance) temp;
					if (pc.getId() == member.getId()) { // 是自己
						return;
					}
					if (!part.isMember(member)) {// 不是是自己的隊員
						pc.sendPackets(new S_ServerMessage(1696));
						return;
					}
					part.passLeader(member);
				}
				break;
			}

		} catch (final Exception e) {
			// _log.error(e.getLocalizedMessage(), e);

		} finally {
			this.over();
		}
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}
}
