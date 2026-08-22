package com.lineage.data.npc.event;

import static com.lineage.server.model.skill.L1SkillId.CANCELLATION;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemVIPTable;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_CharReset;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;

/**
 * 點數重置諮詢^露露
 */
public class Npc_BaseReset extends NpcExecutor {

	private static final Log _log = LogFactory.getLog(Npc_BaseReset.class);

	private Npc_BaseReset() {
		// TODO Auto-generated constructor stub
	}

	public static NpcExecutor get() {
		return new Npc_BaseReset();
	}

	@Override
	public int type() {
		return 3;
	}

	@Override
	public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
		try {
			pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "baseReset"));

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void action(final L1PcInstance pc, final L1NpcInstance npc, final String cmd, final long amount) {
		try {
			if (cmd.equalsIgnoreCase("ent")) {// 點燃回憶蠟燭

				if (!pc.getInventory().checkItem(49142)) { // 回憶蠟燭
					pc.sendPackets(new S_ServerMessage(1290)); // 沒有角色初始化所需要的道具。
					return;
				}

				// if (pc.getLevel() < 99) {
				// pc.sendPackets(new S_SystemMessage("99級才能進行重置。"));
				// return;
				// }
				if (pc.getLevel() < pc.getHighLevel()) {
					pc.sendPackets(new S_SystemMessage("請把等級練回原先" + pc.getHighLevel() + "級才能繼續。"));
					return;
				}

				if (pc.getTempCharGfx() != pc.getClassId()) {
					pc.sendPackets(new S_SystemMessage("請先解除變身才能繼續。"));
					return;
				}

				if (pc.getMeteLevel() > 0) {
					pc.sendPackets(new S_SystemMessage("你已經轉生過了，所以無法進行重置。"));
					return;
				}

				_log.info(pc.getName() + "啟動回憶模式");
				pc.setOtherStats(pc.getOtherStats() + 1);
				pc.setAddPoint(0);
				pc.setDelPoint(0);
				pc.save();
				_log.info("存檔完成");
				// 消除現有技能狀態
				final L1SkillUse l1skilluse = new L1SkillUse();
				l1skilluse.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0, L1SkillUse.TYPE_LOGIN);

				pc.getInventory().takeoffEquip(945); // 脫除全部裝備
				/**
				 * 回憶時 脫掉所有符文 脛甲 肩甲 徽章 以及 VIP道具
				 */
				for (L1ItemInstance armor : pc.getInventory().getItems()) {
					if (armor != null) {
						if (armor.isEquipped()) { // 穿著中的
							if (armor.getItem().getType2() == 2) { // Armor
								if (armor.getItem().getType() == 14 // 上左
										|| armor.getItem().getType() == 15 // 下左
										|| armor.getItem().getType() == 16 // 脛甲
										|| armor.getItem().getType() == 17 // 下右
										|| armor.getItem().getType() == 18 // 下中
										|| armor.getItem().getType() == 23 // 上右
										|| armor.getItem().getType() == 29 // 肩甲
										|| armor.getItem().getType() == 30 // 徽章
								) {
									pc.getInventory().setEquipped(armor, false, false, false);
								}
							}
							// if (armor.getItem().getclassname().equals("extra.VIP")) {
							if (ItemVIPTable.get().checkVIP(armor.getItemId())) {
								armor.setEquipped(false);
								ItemVIPTable.get().deleItemVIP(pc, armor.getItemId()); // 移除道具vip效果
								pc.getInventory().updateItem(armor, L1PcInventory.COL_EQUIPPED);
								pc.getInventory().saveItem(armor, L1PcInventory.COL_EQUIPPED);
							}
						}
					}
				}

				// 傳送至轉生用地圖
				L1Teleport.teleport(pc, 32737, 32789, (short) 997, 4, false);

				final int initStatusPoint = 75 + pc.getElixirStats();// 初始點數+萬能藥點數

				// 目前能力值點數總和
				int pcStatusPoint = pc.getBaseStr() + pc.getBaseInt() + pc.getBaseWis() + pc.getBaseDex()
						+ pc.getBaseCon() + pc.getBaseCha();

				if (pc.getLevel() > 50) {// 角色等級超過50
					pcStatusPoint += (pc.getLevel() - 50 - pc.getBonusStats());// 目前點數加上升級未點的能力值點數

				}

				final int diff = pcStatusPoint - initStatusPoint;
				/**
				 * [50級以上]
				 * 
				 * 目前點數 - 初始點數 = 人物應有等級 - 50 -> 人物應有等級 = 50 + (目前點數 - 初始點數)
				 */
				int maxLevel = 1;

				if (diff > 0) {
					// 最高到99級:也就是不支援轉生
					maxLevel = Math.min(50 + diff, 99);

				} else {
					maxLevel = pc.getLevel();
				}

				pc.setTempMaxLevel(maxLevel);
				pc.setTempLevel(1);
				pc.setInCharReset(true);
				pc.sendPackets(new S_CharReset(pc));
			}
			pc.sendPackets(new S_CloseList(pc.getId()));

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	// public int getOtherStatus(final L1PcInstance pc) {
	// int value = 0;
	// if (pc.getBaseStr() > 35) {
	// value += pc.getBaseStr() - 35;
	// }
	// if (pc.getBaseDex() > 35) {
	// value += pc.getBaseDex() - 35;
	// }
	// if (pc.getBaseInt() > 35) {
	// value += pc.getBaseInt() - 35;
	// }
	// if (pc.getBaseCha() > 35) {
	// value += pc.getBaseCha() - 35;
	// }
	// return value;
	// }

}
