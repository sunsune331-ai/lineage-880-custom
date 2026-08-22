package com.lineage.server.clientpackets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.T_CraftConfigTable;
import com.lineage.server.datatables.T_CraftConfigTable.NewL1NpcMakeItemAction;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1ObjectAmount;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.model.npc.action.L1NpcAction;
import com.lineage.server.serverpackets.S_ItemCraftList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_PacketBoxGree;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.RandomArrayList;
import com.lineage.server.world.World;

public class C_ItemCraft extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_ItemCraft.class);

	@Override
	public void start(final byte[] decrypt, final ClientExecutor client) {
		final L1PcInstance maker = client.getActiveChar();
		try {
			// 資料載入
			read(decrypt);

			// 使用者
			final L1PcInstance pc = client.getActiveChar();
			
			if (pc == null) { // 角色為空
				return;
			}
	
			final int type0 = readH();


			if (type0 == 54) {
				this.readH();
				byte[] sha1 = readCraftB();
				String sha1String = T_CraftConfigTable.update(sha1);
				if (sha1String.equals(T_CraftConfigTable.get().getSHAkey())) {
					pc.sendPackets(new S_ItemCraftList(3));
				} else {
					Collection<NewL1NpcMakeItemAction> all = T_CraftConfigTable
							.get().getNpcMakeItemList().values();
					boolean first = true;
					S_ItemCraftList stemCraftList = null;
					for (NewL1NpcMakeItemAction l1NpcMakeItemAction : all) {
						if (first) {
							stemCraftList = new S_ItemCraftList(
									l1NpcMakeItemAction, first);
							pc.sendPackets(stemCraftList);
							first = false;
						} else {
							stemCraftList = new S_ItemCraftList(
									l1NpcMakeItemAction, false);
							pc.sendPackets(stemCraftList);
						}
					}
					pc.sendPackets(new S_ItemCraftList(null, false));
				}
			} else if (type0 == 56) {
				this.readH();
				int npcObjId = readCraft();
				L1Object obj = World.get().findObject(npcObjId);
				if ((obj != null) && ((obj instanceof L1NpcInstance))) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					HashMap<Integer, NewL1NpcMakeItemAction> npcMakeItemActionMap = T_CraftConfigTable
							.get().getNpcMakeItemActionList(npc.getNpcId());
					if (npcMakeItemActionMap != null)
						pc.sendPackets(new S_ItemCraftList(npcMakeItemActionMap
								.values()));
					else
						pc.sendPackets(new S_ItemCraftList(null));
				} else {
					pc.sendPackets(new S_ItemCraftList(null));
				}

			} else if (type0 == 58) {
				this.readH();
				int npcObjId = readCraft();
				int actionId = readCraft();
				int changeCount = readCraft();

				L1Object npc = World.get().findObject(npcObjId);
				if (!(npc instanceof L1NpcInstance)) {
					return;
				}

				int difflocx = Math.abs(pc.getX() - npc.getX());
				int difflocy = Math.abs(pc.getY() - npc.getY());
				if (pc.getMapId() != npc.getMapId() || (difflocx > 10)
						|| (difflocy > 10)) {
					return;
				}
				L1NpcInstance npcObj = (L1NpcInstance) npc;
				HashMap<Integer, NewL1NpcMakeItemAction> npcMakeItemActions = T_CraftConfigTable.get()
						.getNpcMakeItemActionList(npcObj.getNpcId());
				if (npcMakeItemActions == null) {
					return;
				}
				T_CraftConfigTable.NewL1NpcMakeItemAction npcMakeItemAction = (NewL1NpcMakeItemAction) npcMakeItemActions
						.get(Integer.valueOf(actionId));
				if (npcMakeItemAction == null) {
					return;
				}

				ArrayList<Integer> polyIds = npcMakeItemAction.getCraftPolyList();
				if ((polyIds != null) && (polyIds.indexOf(Integer.valueOf(pc.getTempCharGfx())) == -1)) {
					return;
				}

				if (!npcMakeItemAction.getAmountLevelRange().includes(pc.getLevel())) {
					return;
				}

				if (!npcMakeItemAction.getAmountLawfulRange().includes(pc.getLawful())) {
					return;
				}

				if (!npcMakeItemAction.getAmountKarmaRange().includes(pc.getKarma())) {
					return;
				}

				int key = 0;
				ArrayList<Integer> materialItemDescs = new ArrayList<Integer>();
				HashMap<Integer, Integer> aidCounts = new HashMap<Integer, Integer>();
				while (true) {
					byte[] materialItemArray = readCraftB();
					if (materialItemArray == null)
						break;
					if (materialItemArray.length == 0){
						break;
					}
					C_Empty pack = new C_Empty(materialItemArray);
					int materialItemIndex = pack.readCraft();
					int materialItemDescId = pack.readCraft();
					materialItemDescs.add(Integer.valueOf(materialItemDescId));
					if (!pack.jdField_do()) {
						int aidCount = pack.readCraft();
						if (aidCount <= 0)
							continue;
						aidCounts.put(Integer.valueOf(materialItemDescId),
								Integer.valueOf(aidCount));
					}
				}

				List<L1ObjectAmount<Integer>> materials = npcMakeItemAction.getAmountMeterialList();
				List<L1ObjectAmount<Integer>> aidMaterials = npcMakeItemAction
						.getAmountAidMeterialList();

				int materialsSize = materials.size();
				int aidMaterialsSize = aidMaterials.size();
				int materialsSumCount = materialsSize + aidMaterialsSize;
				if (materialItemDescs.size() != materialsSumCount) {
					return;
				}

				ArrayList<L1ObjectAmount<Integer>> materialItemIds = new ArrayList<L1ObjectAmount<Integer>>();
				int descId = 0;
				L1ObjectAmount<Integer> materialObj = null;
				L1ObjectAmount<Integer> substituteObj = null;
				L1ItemInstance materialItemObj = null;
				ArrayList<L1ObjectAmount<Integer>> substitutes = null;
				ItemTable itemTable = ItemTable.get();
				for (int i = 0; i < materialsSize; i++) {
					descId = ((Integer) materialItemDescs.get(i)).intValue();
					materialObj = (L1ObjectAmount<Integer>) materials.get(i);
					materialItemObj = itemTable
							.createItem(((Integer) materialObj.getObject())
									.intValue());
					if (materialItemObj == null) {
						pc.sendPackets(new S_ItemCraftList(false, null));
						return;
					}

					if (descId == materialItemObj.getItem().getItemDescId()) {
						materialItemIds.add(materialObj);
					} else {
						substitutes = materialObj.getAmountList();
						if (substitutes == null) {
							return;
						}
						for (L1ObjectAmount<Integer> l1ObjectAmount : substitutes) {
							materialItemObj = itemTable
									.createItem(((Integer) l1ObjectAmount
											.getObject()).intValue());
							if (materialItemObj == null) {
								return;
							}
							if (descId == materialItemObj.getItem()
									.getItemDescId()) {
								substituteObj = l1ObjectAmount;
								materialItemIds.add(substituteObj);
								break;
							}
						}
						if (substituteObj == null) {
							return;
						}
					}

				}

				ArrayList<L1ObjectAmount<Integer>> aidMaterialItemIds = new ArrayList<L1ObjectAmount<Integer>>();
				for (int i = 0; i < aidMaterialsSize; i++) {
					descId = ((Integer) materialItemDescs
							.get(materialsSize + i)).intValue();
					materialObj = (L1ObjectAmount<Integer>) aidMaterials.get(i);
					materialItemObj = itemTable
							.createItem(((Integer) materialObj.getObject())
									.intValue());
					if (materialItemObj == null) {
						return;
					}

					if (descId == materialItemObj.getItem().getItemDescId()) {
						if (aidCounts.get(Integer.valueOf(descId)) != null)
							aidMaterialItemIds.add(materialObj);
					} else {
						return;
					}
				}

				L1PcInventory pcInv = pc.getInventory();
				L1ItemInstance[] items = (L1ItemInstance[]) null;
				L1ItemInstance item = null;
				L1ItemInstance itemTemp = null;
				ArrayList<L1ObjectAmount<Integer>> delItemObjIds = new ArrayList<L1ObjectAmount<Integer>>();
				boolean flag = false;
				int tempCount;
				for (L1ObjectAmount<Integer> material : materialItemIds) {
					items = pcInv.findItemsId(((Integer) material.getObject())
							.intValue());
					itemTemp = ItemTable.get().createItem(
							((Integer) material.getObject()).intValue());
					if (itemTemp.isStackable()) {
						flag = false;
						for (int i = 0; i < items.length; i++) {
							item = items[i];
							if ((item.getEnchantLevel() != material
									.getAmountEnchantLevel())
									|| ((material.getAmountBless() != 3) && (item
											.getBless() != material
											.getAmountBless()))
									|| (item.getCount() < material.getAmount()
											* changeCount))
								continue;
							delItemObjIds.add(new L1ObjectAmount<Integer>(
									Integer.valueOf(item.getItemId()), material
											.getAmount() * changeCount, material.getAmountEnchantLevel(),
											material.getAmountBless()));
							flag = true;
							break;
						}

						if (!flag) {
							return;
						}
					} else {
						tempCount = 0;
						for (int i = 0; i < items.length; i++) {
							item = items[i];
							if ((item.getEnchantLevel() != material
									.getAmountEnchantLevel())
									|| ((material.getAmountBless() != 3) && (item
											.getBless() != material
											.getAmountBless()))
									|| (tempCount >= material.getAmount()
											* changeCount))
								continue;
							delItemObjIds.add(new L1ObjectAmount<Integer>(
									Integer.valueOf(item.getItemId()), 1, material.getAmountEnchantLevel(),
									material.getAmountBless()));
							tempCount++;
						}

						if (tempCount < material.getAmount() * changeCount) {
							return;
						}
					}

				}

				ArrayList<L1ObjectAmount<Integer>> delAidItemObjIds = null;
				int tempCount1;
				if ((npcMakeItemAction.getSucceedRandom() < 1000000)
						&& (aidMaterialItemIds != null)) {
					delAidItemObjIds = new ArrayList<L1ObjectAmount<Integer>>();
					for (L1ObjectAmount<Integer> material : aidMaterialItemIds) {
						items = pcInv.findItemsId(((Integer) material
								.getObject()).intValue());
						itemTemp = ItemTable.get().createItem(
								((Integer) material.getObject()).intValue());
						if (itemTemp.isStackable()) {
							flag = false;
							for (int i = 0; i < items.length; i++) {
								item = items[i];
								if ((item.getEnchantLevel() != material
										.getAmountEnchantLevel())
										|| ((material.getAmountBless() != 3) && (item
												.getBless() != material
												.getAmountBless()))
										|| (item.getCount() < ((Integer) aidCounts
												.get(Integer.valueOf(itemTemp
														.getItem()
														.getItemDescId())))
												.intValue()))
									continue;
								delAidItemObjIds
										.add(new L1ObjectAmount<Integer>(
												Integer.valueOf(item
														.getItemId()),
												((Integer) aidCounts.get(Integer
														.valueOf(itemTemp
																.getItem()
																.getItemDescId())))
														.intValue()));
								flag = true;
								break;
							}

							if (!flag) {
								pc.sendPackets(new S_SystemMessage("您的加成材料不足."));
								pc.sendPackets(new S_ItemCraftList(false, null));
								return;
							}
						} else {
							tempCount1 = 0;
							for (int i = 0; i < items.length; i++) {
								item = items[i];
								if ((item.getEnchantLevel() != material
										.getAmountEnchantLevel())
										|| ((material.getAmountBless() != 3) && (item
												.getBless() != material
												.getAmountBless()))
										|| (tempCount1 >= ((Integer) aidCounts
												.get(Integer.valueOf(itemTemp
														.getItem()
														.getItemDescId())))
												.intValue()))
									continue;
								delAidItemObjIds
										.add(new L1ObjectAmount<Integer>(
												Integer.valueOf(item
														.getItemId()), 1));
								tempCount1++;
							}

							if (tempCount1 < ((Integer) aidCounts
									.get(Integer.valueOf(itemTemp.getItem()
											.getItemDescId()))).intValue()) {
								pc.sendPackets(new S_SystemMessage("您的加成材料不足."));
								pc.sendPackets(new S_ItemCraftList(false, null));
								return;
							}
						}
					}

				}

				for (L1ObjectAmount<Integer> delItemAmount : delItemObjIds) {
					if (!pcInv.checkItem(
							((Integer) delItemAmount.getObject()).intValue(),
							delItemAmount.getAmount())) {
						pc.sendPackets(new S_SystemMessage("您的材料不足."));
						pc.sendPackets(new S_ItemCraftList(false, null));
						return;
					}
				}
				int sumAidCount = 0;
				if (delAidItemObjIds != null) {
					for (L1ObjectAmount<Integer> delAidItemAmount : delAidItemObjIds) {
						sumAidCount += delAidItemAmount.getAmount();
						if (!pcInv.checkItem(((Integer) delAidItemAmount
								.getObject()).intValue(), delAidItemAmount
								.getAmount())) {
							pc.sendPackets(new S_SystemMessage("您的加成材料不足."));
							pc.sendPackets(new S_ItemCraftList(false, null));
							return;
						}
					}
				}

				int random = RandomArrayList.getInc(1000000, 1);
				

				final List<L1ObjectAmount<Integer>> test = npcMakeItemAction.getAmountItemList();
				//設置
				
				String itemName="";
				for (final L1ObjectAmount<Integer> giveItem : test) {
					int itemId=giveItem.getObject().intValue();
					L1Item itemCheck=itemTable.getTemplate(itemId);
					itemName=itemCheck.getName();
					_log.info("玩家:"+pc.getName()+"開始製作:"+itemName+" 數量("+changeCount+")個。");	
				}

				if (random <= npcMakeItemAction.getSucceedRandom() + 10000 * sumAidCount) {
					_log.info("製作成功,製作機率:"+random);
					List<L1ObjectAmount<Integer>> successItems = npcMakeItemAction
							.getAmountItemList();
					for (L1ObjectAmount<Integer> delItemAmount : delItemObjIds) {
						if (pcInv.checkItem(
								((Integer) delItemAmount.getObject()).intValue(),
								delItemAmount.getAmount())) {
							pc.getInventory().consumeEnchantItem(delItemAmount.getObject().intValue(),
									delItemAmount.getAmountEnchantLevel(), (int) delItemAmount.getAmount(),
									delItemAmount.getAmountBless());
						}
					}

					if (sumAidCount !=0) {
						for (L1ObjectAmount<Integer> delAidItemAmount : delAidItemObjIds) {
							int AidItemId = ((Integer) delAidItemAmount.getObject()).intValue();
							if (pcInv.checkItem(AidItemId,sumAidCount)) {
								pc.getInventory().consumeItem(AidItemId, sumAidCount);
							}
						}
					}
					
					//List<L1ItemInstance> giveItemObjs = craftadditem(pc, pcInv, changeCount, successItems);
					final List<L1ItemInstance> giveItemObjs = craftadditem(pc, pcInv, changeCount, successItems,
							npcMakeItemAction.isAmountBroad(), npcMakeItemAction.getSystemMessage());

					int r = RandomArrayList.getInt(npcMakeItemAction.getAmountRandom());
					int change = 0;
					for (L1ObjectAmount<Integer> objectAmount : npcMakeItemAction
							.getAmountRandomItemList()) {
						change += objectAmount.getAmountRandom();
						int itemId=	objectAmount.getObject().intValue();	
						long amount= objectAmount.getAmount();
						int amountEnchant = objectAmount.getAmountEnchantLevel();
						int amountBless = objectAmount.getAmountBless();
						if (r < change) {
							L1Item itemCheck=itemTable.getTemplate(itemId);
							itemName=itemCheck.getName();
							giveItemObjs.addAll(b(pc, pcInv, 1, itemId,(int)amount,amountEnchant,amountBless,
									npcMakeItemAction.isAmountBroad(), npcMakeItemAction.getSystemMessage()));
							break;
						}
					}

					pc.sendPackets(new S_ItemCraftList(true, giveItemObjs));

					L1NpcAction actionOnSucceed = npcMakeItemAction
							.getAmountSuceedAction();
					if (actionOnSucceed != null) {
						L1NpcHtml result = actionOnSucceed.execute(String
								.format("request craft%d",
										new Object[] { Integer
												.valueOf(npcMakeItemAction
														.getAmountActionID()) }),
								pc, npc, null);
						if (result != null) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
									result));
						}
					}

					if (npcMakeItemAction.isAmountBroad()) {
						String msg = npcMakeItemAction.getSystemMessage();
						int msgId = npcMakeItemAction.getSystemMessageID();
						if ((msg != null) && (((String) msg).length() > 0)) {
							//World.get().broadcastPacketToAll(new S_SystemMessage(String.format((String) msg, new Object[] { pc.getName(),itemName+"("+changeCount+")個" })));
						} else if (msgId != -1) {
							World.get().broadcastPacketToAll(new S_ServerMessage(msgId));
						}
					}
					giveItemObjs.clear();

				} else {
					_log.info("製作失敗,機率:"+random);
					List<L1ObjectAmount<Integer>> failItems = npcMakeItemAction
							.getFailItemList();
					//List<L1ItemInstance> giveItemObjs = craftadditem(pc, pcInv, changeCount, failItems);
					final List<L1ItemInstance> giveItemObjs = craftadditem(pc, pcInv,
							changeCount, failItems,
							npcMakeItemAction.isAmountBroad(),
							npcMakeItemAction.getSystemMessage());

					int r = RandomArrayList.getInt(npcMakeItemAction
							.getFailRandom());
					int change = 0;
					for (L1ObjectAmount<Integer> msg : npcMakeItemAction
							.getFailAmountRandomItemList()) {
						change += msg.getAmountRandom();
						if (r < change) {
							//giveItemObjs.addAll(craftadditem(pc, pcInv, changeCount, failItems));
							giveItemObjs.addAll(craftadditem(pc, pcInv, changeCount,
									failItems,
									npcMakeItemAction.isAmountBroad(),
									npcMakeItemAction.getSystemMessage()));
							break;
						}
					}

					L1NpcAction actionOnFail = npcMakeItemAction.getFailAction();
					if (actionOnFail != null) {
						L1NpcHtml result = actionOnFail.execute(String.format(
								"request craft%d", new Object[] { Integer
										.valueOf(npcMakeItemAction
												.getAmountActionID()) }), pc, npc,
								null);
						if (result != null) {
							pc.sendPackets(new S_NPCTalkReturn(pc.getId(),
									result));
						}
					}

					String msg1 = npcMakeItemAction.getFailMessage();
					int msgId = npcMakeItemAction.getFailMessageID();
					if ((msg1 != null) && (msg1.length() > 0))
						pc.sendPackets(new S_SystemMessage(msg1));
					else if (msgId != -1) {
						pc.sendPackets(new S_ServerMessage(msgId));
					}
					for (L1ObjectAmount<Integer> delItemAmount : delItemObjIds) {
						if (pcInv.checkItem(
								((Integer) delItemAmount.getObject()).intValue(),
								delItemAmount.getAmount())) {
							pc.getInventory().consumeEnchantItem(delItemAmount.getObject().intValue(),
									delItemAmount.getAmountEnchantLevel(), (int) delItemAmount.getAmount(),
									delItemAmount.getAmountBless());
							//pc.sendPackets(new S_SystemMessage("失敗材料已扣除."));
						}
					}
					if (sumAidCount !=0) {
						for (L1ObjectAmount<Integer> delAidItemAmount : delAidItemObjIds) {
							int AidItemId = ((Integer) delAidItemAmount.getObject()).intValue();
							if (pcInv.checkItem(AidItemId,sumAidCount)) {
								pc.getInventory().consumeItem(AidItemId, sumAidCount);
							}
						}
					}
					pc.sendPackets(new S_ItemCraftList(false, giveItemObjs));
				}
			}
			
		} catch (final Exception e) {
		    
			_log.error(e.getLocalizedMessage(), e);
			long count1 = maker.getInventory().countItems(80028);
			long count2 = maker.getInventory().countItems(80029);
			_log.info("玩家:"+maker.getName()+" 1:"+count1+" 2:"+count2+" 錯誤碼:"+e.getMessage()+","+e.getStackTrace());
			
		} finally {
			over();
		}
	}

	public List<L1ItemInstance> craftadditem(L1PcInstance pc, L1PcInventory pcInv,
			int changeCount, List<L1ObjectAmount<Integer>> amounts, boolean ok, String msg) {
		L1ItemInstance giveItemObj = null;
		List<L1ItemInstance> giveItemObjs = new ArrayList<L1ItemInstance>();
		for (L1ObjectAmount<Integer> giveItem : amounts) {
			giveItemObj = ItemTable.get().createItem(
					((Integer) giveItem.getObject()).intValue());
			int count = (int) giveItem.getAmount();
			int enchantLevel = giveItem.getAmountEnchantLevel();
			int bless = giveItem.getAmountBless();
			if (giveItemObj != null) {
				if (pcInv.checkAddItem(giveItemObj, count) == 0) {
					if (giveItemObj.isStackable()) {
						giveItemObj.setCount(count * changeCount);
						giveItemObj.setIdentified(true);
						giveItemObj.setBless(bless);

						if (giveItemObj.getItem().getMaxUseTime() != 0) {
							giveItemObj.startEquipmentTimer(pc);
						}
						pcInv.storeItem(giveItemObj);
						giveItemObjs.add(giveItemObj);
					} else {
						for (int y = 0; y < count * changeCount; y++) {
							L1ItemInstance itemTemp1 = ItemTable.get()
									.createItem(
											((Integer) giveItem.getObject())
													.intValue());
							itemTemp1.setCount(1);
							itemTemp1.setIdentified(true);
							itemTemp1.setEnchantLevel(enchantLevel);
							itemTemp1.setBless(bless);

							if (itemTemp1.getItem().getMaxUseTime() != 0) {
								itemTemp1.startEquipmentTimer(pc);
							}
							pcInv.storeItem(itemTemp1);
							giveItemObjs.add(itemTemp1);
						}
					}
					if (ok) {
						World.get().broadcastPacketToAll(
								new S_PacketBoxGree(0x02, String.format(msg,
										new Object[] { pc.getName(),
												giveItemObj.getLogName() })));
					}
					pc.sendPackets(new S_ServerMessage(403, giveItemObj
							.getLogName()));
				} else {
					pc.sendPackets(new S_SystemMessage("超過可攜帶物品數量,獲取物品["
							+ giveItemObj.getLogName() + "(" + count
							+ ")]失敗!請截圖反饋至GM!"));
				}
			}
		}
		return giveItemObjs;
	}
	
	public List<L1ItemInstance> b(final L1PcInstance pc, final L1PcInventory pcInv, final int changeCount,
			final int itemId,final int amount,final int amountEnchantLevel,int amountBless, boolean ok, String msg) {
		L1ItemInstance giveItemObj = null;
		final List<L1ItemInstance> giveItemObjs = new ArrayList<L1ItemInstance>();
		
			_log.info("itemId:"+itemId+" count:"+amount+" enchantLevel:"+amountEnchantLevel+" bless:"+amountEnchantLevel);
			giveItemObj = ItemTable.get().createItem(itemId);
			final int count = amount;
			final int enchantLevel = amountEnchantLevel;
			final int bless = amountBless;
			if (giveItemObj != null) {
				if (pcInv.checkAddItem(giveItemObj, count) == 0) {
					if (giveItemObj.isStackable()) {
						giveItemObj.setCount(count * changeCount);
						giveItemObj.setIdentified(true);
						giveItemObj.setBless(bless);

						pcInv.storeTradeItem(giveItemObj);
						giveItemObjs.add(giveItemObj);
					} else {
						for (int y = 0; y < (count * changeCount); y++) {
							final L1ItemInstance itemTemp1 = ItemTable.get().createItem(itemId);
							itemTemp1.setCount(1);
							itemTemp1.setIdentified(true);
							itemTemp1.setEnchantLevel(enchantLevel);
							itemTemp1.setBless(bless);

							pcInv.storeTradeItem(itemTemp1);
							giveItemObjs.add(itemTemp1);
						}
					}
					if (ok) {
						World.get().broadcastPacketToAll(
								new S_PacketBoxGree(0x02, String.format(msg,
										new Object[] { pc.getName(),
												giveItemObj.getLogName() })));
					}
					pc.sendPackets(new S_ServerMessage(403, giveItemObj.getLogName()));
				} else {
					pc.sendPackets(new S_SystemMessage(
							"超過可攜帶物品數量,獲取物品[" + giveItemObj.getLogName() + "(" + count + ")]失敗!請截圖反饋至GM!"));
				}
			}
		
		return giveItemObjs;
	}

	public String jdMethod_else() {
		return "[C] C_ItemCraft";
	}
}