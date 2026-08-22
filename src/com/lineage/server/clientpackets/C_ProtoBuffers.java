/**
 *                           License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package com.lineage.server.clientpackets;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.lineage.config.Config;
import com.lineage.config.ConfigRecord;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.CraftInfoTable;
import com.lineage.server.datatables.CraftListTable;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AllChannelsChat;
import com.lineage.server.serverpackets.S_MapTimerOut;
import com.lineage.server.serverpackets.S_ProtoBuffers;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_ServerVersion;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Craft;
import com.lineage.server.templates.L1QuestNew;
import com.lineage.server.utils.Random;
import com.lineage.server.world.World;

import l1j.server.server.datas.protobuf.PBMessageALL;
import l1j.server.server.datas.protobuf.PBMessageALL3;
import l1j.server.server.datas.protobuf.PBMessageALL5;

public class C_ProtoBuffers extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_ProtoBuffers.class);

	@Override
	public void start(final byte[] decrypt, final ClientExecutor client) {
		try {
			// 資料載入
			read(decrypt);

			final int type = readH();

			if (type == 0x0334) {
				client.out().encrypt(new S_ServerVersion());
				return;
			}

			final L1PcInstance pc = client.getActiveChar();

			if (pc == null) {
				return;
			}

			if (is181UiCommand(type)) {
				new C_ItemCraft1().start(decrypt, client);
				return;
			}

			// 官服任務系統
			if (type == 520) { // 任務指引
				final int dataLength = readH();
				final byte[] data = read(dataLength);
				final PBMessageALL.type1 msg = PBMessageALL.type1.parseFrom(data);
				final int questID = msg.getValue1();
				final L1QuestNew qn = pc.getQuestList().get(questID);
				// 只顯示新兵
				if (questID == 256 && qn != null && !qn.isQuestEnd()) {
					if (pc.getLevel() == 1) {
						pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.QUEST_GUIDE, questID));
					}
				}

			} else if (type == 0x020c) { // 0x020c=524 任務視窗-選擇獎勵
				final int dataLength = readH();
				final byte[] data = read(dataLength);
				final PBMessageALL.type1 msg = PBMessageALL.type1.parseFrom(data);
				final int questID = msg.getValue1();
				final L1QuestNew qn = pc.getQuestList().get(questID);
				if (qn != null && !qn.isQuestEnd()) {

					for (int i = 0; i < qn.getRewardItemid().length; i++) {
						ItemTable.createNewItem(pc, qn.getRewardItemid()[i], qn.getRewardItemCount()[i], qn.getRewardItemEnchant()[i]);
					}

					if (msg.hasValue2()) {
						final int idx = msg.getValue2();// 獎勵有選擇時 0 1 2 3
						ItemTable.createNewItem(pc, qn.getRewardSelectItemid()[idx], qn.getRewardSelectItemCount()[idx], qn.getRewardSelectItemEnchant()[idx]);
					}
					if (qn.getRewardExp() > 0) {
						final double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
						pc.addExp((int) (qn.getRewardExp() * exppenalty));

					}

					qn.setQuestEnd(true);
					pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.QUEST_END, questID));

					if (qn.get獲得道具編號().length > 0 && qn.isRecoverRequireItem()) {
						for (int i = 0; i < qn.get獲得道具編號().length; i++) {
							pc.getInventory().consumeEnchantItem(qn.get獲得道具編號()[i], qn.get獲得道具加成()[i], qn.get獲得道具數量()[i], 3);
						}
					}

					// 每日任務另外判斷
					if (qn.getId() == 305) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew1) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew1, L1PcQuest.QUEST_END);
						}
					}
					if (qn.getId() == 306) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew2) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew2, L1PcQuest.QUEST_END);
						}
					}
					if (qn.getId() == 307) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew3) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew3, L1PcQuest.QUEST_END);
						}
					}
					if (qn.getId() == 308) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew4) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew4, L1PcQuest.QUEST_END);
						}
					}
					if (qn.getId() == 309) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew5) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew5, L1PcQuest.QUEST_END);
						}
					}
					if (qn.getId() == 310) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew6) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew6, L1PcQuest.QUEST_END);
						}
					}
					if (qn.getId() == 311) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew7) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew7, L1PcQuest.QUEST_END);
						}
					}
					if (qn.getId() == 312) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew8) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew8, L1PcQuest.QUEST_END);
						}
					}
					if (qn.getId() == 313) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew9) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew9, L1PcQuest.QUEST_END);
						}
					}
					if (qn.getId() == 314) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew10) != L1PcQuest.QUEST_END) {
							pc.getQuest().set_step(L1PcQuest.QuestNew10, L1PcQuest.QUEST_END);
						}
					}
				}

			} else if (type == 0x020f) { // 0x020f=527 任務視窗-移動位置
				final int dataLength = readH();
				final byte[] data = read(dataLength);
				final PBMessageALL.type1 msg = PBMessageALL.type1.parseFrom(data);
				final int questID = msg.getValue1();

				final L1QuestNew qn = pc.getQuestList().get(questID);

				if (qn != null && qn.getTeleportLoc().length > 0) {
					L1Teleport.teleport(pc, qn.getTeleportLoc()[0], qn.getTeleportLoc()[1], (short) qn.getTeleportLoc()[2], 5, true);
				}

			} else if (type == 54) { // 道具清單驗證
				final int dataLength = readH();
				final byte[] data = read(dataLength);

				// 正服的驗證碼(XXX 可替換為自定清單的驗證碼.)
				final byte[] true_data = ToByteArray(Config.CraftinfoCode);

				if (pc.getNetConnection().isLoadCraft() || Arrays.equals(data, true_data)) {
					// load
					pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_LIST, 3));
				} else {
					System.out.println("更新道具清單驗證，玩家：(" + pc.getName() + ")");

					// creat new
					pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_LIST, 0));
					// edit
					final ArrayList<L1Craft> list = CraftListTable.getInstance().getList();
					for (final L1Craft craft : list) {
						pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_LIST, craft, 1));
					}
					// save
					pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_LIST, 2));
					pc.getNetConnection().setLoadCraft(true);
				}
			} else if (type == 56) { // 要求道具清單
				final int dataLength = readH();
				final byte[] data = read(dataLength);
				int objid = 0;

				final PBMessageALL.type1 msg = PBMessageALL.type1.parseFrom(data);
				objid = msg.getValue1();

				final L1Object obj = World.get().findObject(objid);

				if (obj instanceof L1NpcInstance) {
					final L1NpcInstance npc = (L1NpcInstance) obj;

					// 原本
					// final String[] keys = npc.getCraftList();
					//
					// if (keys.length == 0) {
					// // System.out.println("npcid: " + npc.getNpcId());
					// return;
					// }
					// -------------
					// 修改
					final String[] keys = CraftInfoTable.getIns().getCraftNpc(npc.getNpcId());

					if (keys == null || keys.length == 0) {
						// 沒有符合製作條件的清單
						pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_GET, pc));
						return;
					}

					pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_GET, keys));
				}

			} else if (type == 92) { // 製作道具-確定 7.2:道具製作時間
				pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_TIME, 33));

			} else if (type == 58) { // 製作道具-確定

				if (pc.getInventory().getWeight240() >= 197) { // 重量過重
					pc.sendPackets(new S_ServerMessage(82)); // 82無法領取，請確認負重與道具欄位。
					return;
				}

				if (pc.getInventory().getSize() > 180) {
					pc.sendPackets(new S_ServerMessage(263)); // 一個角色最多可攜帶180個道具。
					return;
				}

				final int dataLength = readH();
				final byte[] data = read(dataLength);

				// -------------
				/*int objid = 0;
				final PBMessageALL.type1 msg1 = PBMessageALL.type1.parseFrom(data);
				objid = msg1.getValue1();
				final L1Object obj = World.get().findObject(objid);
				if (obj instanceof L1NpcInstance) {
					final L1NpcInstance npc = (L1NpcInstance) obj;
	    			final int difflocx = Math.abs(pc.getX() - npc.getX());
	    			final int difflocy = Math.abs(pc.getY() - npc.getY());
	    			if ((pc.getMapId() != npc.getMapId()) || (difflocx > 10) || (difflocy > 10)) {
	    				// _log.info("玩家離NPC太遠，玩家：" + pc.getName());
	    				pc.sendPackets(new S_SystemMessage("您離NPC過遠，請走近後再試"));
	    				return;
	    			}
				}*/
				// -------------

				final PBMessageALL5.type16 msg = PBMessageALL5.type16.parseFrom(data);
				final L1Craft craft = CraftListTable.getInstance().getList(msg.getValue2());

				final L1ItemInstance addchanceitem = craft.getAddChanceItem(); // 增加機率道具

				final int counts = msg.getValue3();

				final ArrayList<L1ItemInstance> trueMaterialList = new ArrayList<>();

				for (final ByteString bs : msg.getArray4List()) {
					final PBMessageALL3.type7 msg7 = PBMessageALL3.type7.parseFrom(bs);

					final int systemid = msg7.getValue2();
					// int addChance = msg7.getValue3();
					final int enchant = msg7.getValue4();
					// int bless = msg7.getValue5();

					// 有增加機率道具
					if (addchanceitem != null && addchanceitem.getItem().getItemDescId() == systemid) {
						continue; // 留到後面判斷
					}

					// 依據材料清單挑出符合回傳值的材料
					for (final L1ItemInstance item : craft.getMaterialItems().values()) {
						// 先找原本的材料
						if (item.getItem().getItemDescId() == systemid && item.getEnchantLevel() == enchant) {
							trueMaterialList.add(item);
							break;
						}
						// 原本材料不是 改找替代清單
						for (final L1ItemInstance exchange : craft.getExchangeItemList().get(item.getItemId())) {
							if (exchange.getItem().getItemDescId() == systemid && exchange.getEnchantLevel() == enchant) {
								trueMaterialList.add(exchange);
								break;
							}
						}
					}
				}

				// 正式的材料清單數量應與設定的材料清單相同 (扣掉增加機率道具的情況下)
				if (trueMaterialList.size() != craft.getMaterialItems().size()) {
					_log.info("Item Craft has MaterialList Error with [" + pc.getName() + "] craft id=" + craft.getCraftID());
					return;
				}

				for (final L1ItemInstance item : trueMaterialList) {

					if (item.getItem().getType2() == 1) { // 武器類
						if (!pc.getInventory().consumeEnchantItem(item.getItemId(), item.getEnchantLevel(),
								(int) item.getCount() * counts, item.getBless(), item.getAttrEnchantKind(),
								item.getAttrEnchantLevel())
						) {
							if (item.getAttrEnchantKind() > 0 || item.getAttrEnchantLevel() > 0) {
								// pc.sendPackets(new S_SystemMessage("材料屬性不符，無法製作。"));
								// 2:材料道具錯誤
								pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_RESULT, craft,
										craft.getCraftItem(), 2));
							} else {
								_log.info("Item Craft has Consume Error with [" + pc.getName() + "] craft id=" + craft.getCraftID());
							}
							return;
						}
					} else {
						if (!pc.getInventory().consumeEnchantItem(item.getItemId(), item.getEnchantLevel(),
								(int) item.getCount() * counts, item.getBless())
						) {
							_log.info("Item Craft has Consume Error with [" + pc.getName() + "] craft id=" + craft.getCraftID());
							return;
						}
					}

					// 道具製作 金幣類抽稅
					/*if (item.getItemId() == L1ItemId.ADENA) {
						final int castleId = URandom.nextInt(4) + 1;
						L1Castle castle = CastleReading.get().getCastleTable(castleId);
						synchronized (castle) {
							long money = castle.getPublicMoney();
							money += item.getCount() * counts * castle.getTaxRate() / 100;
							castle.setPublicMoney(money);
							CastleReading.get().updateCastle(castle);
						}
					}*/
				}

				// 增加機率道具的判斷
				int chance = craft.getSuccessChance();
				if (addchanceitem != null) {
					for (final ByteString bs : msg.getArray4List()) {
						final PBMessageALL5.type16 msg2 = PBMessageALL5.type16.parseFrom(bs);
						if (msg2.getValue2() == addchanceitem.getItem().getItemDescId()) {
							if (pc.getInventory().consumeItem(addchanceitem.getItemId(), msg2.getValue3() * counts)) {
								chance += msg2.getValue3() * counts; // add chance
							}
							break;
						}
					}
				}

				if (Random.nextInt(100) < chance) { // success

					final L1ItemInstance craft_item = craft.getCraftItem();

					final int itemid = craft_item.getItemId();
					final int count = (int) craft_item.getCount() * counts;
					final int enchant = craft_item.getEnchantLevel();
					int bless = craft_item.getBless();

					// 新增武器屬性
					final int attrId = craft_item.getAttrEnchantKind();
					final int attrLv = craft_item.getAttrEnchantLevel();

					// 大成功
					if (craft.getPerfectCance() > 0 && Random.nextInt(100) < craft.getPerfectCance()) { // perfect
						final L1ItemInstance perfectitem = craft.getCraftPerfectItem();
						if (perfectitem != null) {
							pc.sendPackets(new S_SkillSound(pc.getId(), 2047));
							pc.broadcastPacketAll(new S_SkillSound(pc.getId(), 2047));
							// 給予大成功道具
							ItemTable.createNewItem(pc, perfectitem.getItemId(), (int) perfectitem.getCount() * counts,
									perfectitem.getEnchantLevel());
							if (craft.getShowWorld() != 0) {
								if (!pc.isGm()) {
		                        	// 2綠色 3紅色  13黃色 14白色 22淺綠 44淺藍 45淺紅
		                            World.get().broadcastPacketToAll(new S_AllChannelsChat("恭喜玩家 "+ pc.getName() +" 成功製作大成功道具 +" + perfectitem.getEnchantLevel() + perfectitem.getName() + "(" + perfectitem.getCount() * counts + ")。", 2));
								}
	                        }
							ConfigRecord.recordToFiles("火神製作記錄",
									"玩家 " + pc.getName() + " 成功製作大成功道具 +" + perfectitem.getEnchantLevel() + perfectitem.getName() + " 數量("
											+ perfectitem.getCount() * counts + ")。" + "時間:("
											+ new Timestamp(System.currentTimeMillis()) + ")");
							pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_RESULT, craft, craft_item, 0));// 0:道具製作成功
						} else {
							pc.sendPackets(new S_SystemMessage("製作出錯，請聯繫管理員！"));
							_log.info("大成功道具未設置或不存在: craftID" + craft.getCraftID() + " 製作玩家：" + pc.getName());
						}
					} else {
						if (craft.getShowWorld() != 0) {
							if (!pc.isGm()) {
	                        	// 2綠色 3紅色  13黃色 14白色 22淺綠 44淺藍 45淺紅
	                            World.get().broadcastPacketToAll(new S_AllChannelsChat("恭喜玩家 "+ pc.getName() +" 成功製作 +" + enchant + craft_item.getName() + "(" + count + ")。", 2));
							}
						}
						ConfigRecord.recordToFiles("火神製作記錄", "玩家 " + pc.getName() + " 成功製作 +" + enchant + craft_item.getName()
								+ " 數量(" + count + ")。" + "時間:(" + new Timestamp(System.currentTimeMillis()) + ")");

						// System.out.println(bless);
						// 給予製作成功道具
						ItemTable.createNewItem(pc, itemid, count, enchant, bless, attrId, attrLv, true);
						pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_RESULT, craft, craft_item, 0));// 0:道具製作成功
					}
				} else {
					// 火神痕跡
					final L1ItemInstance failitem = craft.getCraftFailItem();
					if (failitem != null) {
						// 給予製作失敗道具
						ItemTable.createNewItem(pc, failitem.getItemId(), (int) failitem.getCount() * counts,
								failitem.getEnchantLevel(), failitem.getAttrEnchantKind(), failitem.getAttrEnchantLevel());
						ConfigRecord.recordToFiles("火神製作記錄",
								"玩家 " + pc.getName() + " 製作失敗，獲得失敗道具 +" + failitem.getEnchantLevel()
										+ failitem.getName() + " 數量(" + failitem.getCount() + ")。" + "時間:("
										+ new Timestamp(System.currentTimeMillis()) + ")");
					}
					pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.CRAFT_RESULT, craft, craft.getCraftItem(), 1));// 1:道具製作失敗
				}
				if (pc.isGm()) {
					pc.sendPackets(new S_SystemMessage("本次製作機率：" + chance));
				}

			} else if (type == 0x0322) { // 0x0322=802 計時地圖
				// opcode: 21 [C_ProtoBuffers]
				// 0000: 15 22 03 00 00 01 00 00
				pc.sendPackets(new S_MapTimerOut(pc));
			}

		} catch (final InvalidProtocolBufferException e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	private byte[] ToByteArray(final String tsxt) {
		final String[] ss = tsxt.trim().split(" ");
		final byte[] data = new byte[ss.length];
		for (int i = 0; i < ss.length; i++) {
			data[i] = hexToBytes(ss[i])[0];
		}
		return data;
	}

	private static boolean is181UiCommand(final int type) {
		switch (type) {
		case 0x0064:
		case 0x007a:
		case 0x007c:
		case 0x0087:
		case 0x008f:
		case 0x013d:
		case 0x013f:
		case 0x0142:
		case 0x0146:
		case 0x014c:
		case 0x0152:
		case 0x01cc:
		case 0x01e0:
		case 0x01e4:
		case 0x0202:
		case 0x0233:
		case 0x0235:
		case 0x032b:
		case 0x032f:
		case 0x033c:
		case 0x03ee:
		case 0x03f8:
		case 0x03f9:
			return true;
		default:
			return false;
		}
	}

	private byte[] hexToBytes(final String hexString) {
		final char[] hex = hexString.toCharArray();
		// 轉rawData長度減半
		final int length = hex.length / 2;
		final byte[] rawData = new byte[length];
		for (int i = 0; i < length; i++) {
			// 先將hex資料轉10進位數值
			final int high = Character.digit(hex[i * 2], 16);
			final int low = Character.digit(hex[i * 2 + 1], 16);
			// 將第一個值的二進位值左平移4位,ex: 00001000 => 10000000 (8=>128)
			// 然後與第二個值的二進位值作聯集ex: 10000000 | 00001100 => 10001100 (137)
			int value = (high << 4) | low;
			// 與FFFFFFFF作補集
			if (value > 127) {
				value -= 256;
			}
			// 最後轉回byte就OK
			rawData[i] = (byte) value;
		}
		return rawData;
	}

	@Override
	public String getType() {
		return "C_ProtoBuffers";
	}

}
