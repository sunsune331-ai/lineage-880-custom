package com.lineage.server.model.drop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigBoxMsg;
import com.lineage.server.datatables.ItemMsgTable;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.utils.RandomArrayList;
import com.lineage.server.world.World;

/**
 * NPC掉落物品的分配
 * 
 * @author dexc
 *
 */
public class DropShare implements DropShareExecutor {

	@SuppressWarnings("unused")
	private static final Log _log = LogFactory.getLog(DropShare.class);

	// 正向
	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	/**
	 * 掉落物品的分配
	 * 
	 * @param npc
	 *            死亡的NPC
	 * @param acquisitorList
	 *            掉落目標清單
	 * @param hateList
	 *            仇恨清單
	 */
	@Override
	public void dropShare(final L1NpcInstance npc, final ArrayList<L1Character> acquisitorList,
			final ArrayList<Integer> hateList) {
		DropShareR dropShareR = new DropShareR(npc, acquisitorList, hateList);
		GeneralThreadPool.get().schedule(dropShareR, 0);
	}

	private class DropShareR implements Runnable {

		final L1NpcInstance _npc;
		final ArrayList<L1Character> _acquisitorList;
		final ArrayList<Integer> _hateList;

		private DropShareR(L1NpcInstance npc, ArrayList<L1Character> acquisitorList, ArrayList<Integer> hateList) {
			_npc = npc;
			_acquisitorList = acquisitorList;
			_hateList = hateList;
		}

		@Override
		public void run() {
			try {
				
				final L1Inventory inventory = _npc.getInventory();
				if (inventory == null) {
					return;
				}
				if (inventory.getSize() <= 0) {
					return;
				}
				if (_acquisitorList.size() != _hateList.size()) {
					// _log.info("acquisitorList.size() != hateList.size()");
					return;
				}
				// 合計取得
				int totalHate = 0;
				L1Character acquisitor;
				for (int i = _hateList.size() - 1; i >= 0; i--) {
					acquisitor = _acquisitorList.get(i);

					if ((ConfigAlt.AUTO_LOOT == 2) // ２場合及省
							&& ((acquisitor instanceof L1SummonInstance) || (acquisitor instanceof L1PetInstance))) {
						_acquisitorList.remove(i);
						_hateList.remove(i);

					} else if ((acquisitor != null) && (acquisitor.getMapId() == _npc.getMapId()) && (acquisitor
							.getLocation().getTileLineDistance(_npc.getLocation()) <= ConfigAlt.LOOTING_RANGE)) {
						totalHate += _hateList.get(i);

					} else {
						// _log.info("NPC掉落物品分配無對象 刪除掉落物: " + npc.getName());
						_acquisitorList.remove(i);
						_hateList.remove(i);
					}
				}

				// 掉落物品的分配
				L1Inventory targetInventory = null;
				L1PcInstance player;
				final Random random = new Random();
				int randomInt;
				int chanceHate;
				int itemId;
				final List<L1ItemInstance> list = inventory.getItems();

				if (list.isEmpty()) {
					return;
				}

				if (list.size() <= 0) {
					return;
				}
				for (L1ItemInstance item : list) {
					itemId = item.getItemId();

					if ((item.getItem().getType2() == 0) && (item.getItem().getType() == 2)) { // 照明道具
						item.setNowLighting(false);
					}
					// 沙蟲
					if (_npc.getNpcId() == 97259 && itemId == 80026) {
						equalityDrop(_npc, _acquisitorList, itemId);// 物品平均分配
						continue;
					}
					// 巨蟻女皇
					if (_npc.getNpcId() == 97258 && itemId == 80024) {
						equalityDrop(_npc, _acquisitorList, itemId);// 物品平均分配
						continue;
					}
					// 傑羅斯
					if (_npc.getNpcId() == 107034 && itemId == 82239) {
						equalityDrop(_npc, _acquisitorList, itemId);// 物品平均分配
						continue;
					}
					// 巨大飛龍
					if (_npc.getNpcId() == 107035 && itemId == 82236) {
						equalityDrop(_npc, _acquisitorList, itemId);// 物品平均分配
						continue;
					}
					// 巨型骷髏
					if (_npc.getNpcId() == 99019 && itemId == 85010) {
						equalityDrop(_npc, _acquisitorList, itemId);// 物品平均分配
						continue;
					}
					// 副本安塔瑞斯
					if (_npc.getNpcId() == 71016 && itemId == 80015) {
						equalityDrop(_npc, _acquisitorList, itemId);// 物品平均分配
						continue;
					}
					// 副本法利昂
					if (_npc.getNpcId() == 71028 && itemId == 80016) {
						equalityDrop(_npc, _acquisitorList, itemId);// 物品平均分配
						continue;
					}
					// 赤鬼
					if (_npc.getNpcId() == 99012 && itemId == 56313) {
						equalityDropScreen(_npc, itemId);// 物品平均分配
						continue;
					}
					long itemCount = item.getCount();
					
					boolean isDropGround = false;
					if (_npc.getNpcTemplate().getDropGround() != 0) {
						isDropGround = true;
					}
					
					if (((ConfigAlt.AUTO_LOOT != 0) || (itemId == L1ItemId.ADENA)) && (totalHate > 0) && (!isDropGround)) {
						randomInt = random.nextInt(totalHate);
						chanceHate = 0;
						for (int j = _hateList.size() - 1; j >= 0; j--) {
							Thread.sleep(1);
							chanceHate += _hateList.get(j);
							if (chanceHate > randomInt) {
								acquisitor = _acquisitorList.get(j);
								if (acquisitor.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
									targetInventory = acquisitor.getInventory();

									if (acquisitor instanceof L1PcInstance) {
										player = (L1PcInstance) acquisitor; // 具有隊伍
										 if (itemId == 40308) {
						                      double addadena = player.getGF();
						                      if (addadena > 0.0D) {
						                    	  itemCount = (long) (itemCount + itemCount * addadena);
						                        item.setCount(itemCount);
						                      }
						                    }
										if (player.isInParty()) {
											//final Object[] pcs = player.getParty().partyUsers().values().toArray();
											// 7.6
											final Object[] pcs = player.getParty().getMemberList().toArray();
											if (pcs.length <= 0) {
												return;
											}
											for (Object obj : pcs) {
												if (obj instanceof L1PcInstance) {
													final L1PcInstance tgpc = (L1PcInstance) obj;
													// 813 隊員%2%s 從%0 取得 %1%o
													tgpc.sendPackets(new S_ServerMessage(813, _npc.getNameId(),
															item.getLogName(), player.getName()));
												}

											}

										} else {
											// 143 \f1%0%s 給你 %1%o 。
											player.sendPackets(
													new S_ServerMessage(143, _npc.getNameId(), item.getLogName()));
										}

										if (ConfigBoxMsg.ISMSG) {
											if (ItemMsgTable.get().contains(item.getItemId())) {
												ConfigBoxMsg.msg(player.getName(), _npc.getNameId(), item.getLogName());
											}
										}
									}
								} else {
									item.set_showId(_npc.get_showId());
									targetInventory = World.get().getInventory(acquisitor.getX(), acquisitor.getY(),
											acquisitor.getMapId()); // 持足元落
								}
								break;
							}
						}

					} else {
						final List<Integer> dirList = new ArrayList<Integer>();
						for (int j = 0; j < 8; j++) {
							dirList.add(j);
						}
						int x = 0;
						int y = 0;
						int dir = 0;
						do {
							if (dirList.size() == 0) {
								x = 0;
								y = 0;
								break;
							}
							randomInt = random.nextInt(dirList.size());
							dir = dirList.get(randomInt);
							dirList.remove(randomInt);

							// x = HEADING_TABLE_X[dir];
							// y = HEADING_TABLE_Y[dir];
							// Thread.sleep(1);

							if (!isDropGround) { // 怪物物品掉落範圍化
								x = HEADING_TABLE_X[dir];
								y = HEADING_TABLE_Y[dir];
							} else {
		                    	x = RandomArrayList.getInt(_npc.getNpcTemplate().getDropGround() * 2) - _npc.getNpcTemplate().getDropGround();
		                        y = RandomArrayList.getInt(_npc.getNpcTemplate().getDropGround() * 2) - _npc.getNpcTemplate().getDropGround();
		                    }
							Thread.sleep(1);

						} while (!_npc.getMap().isPassable(_npc.getX(), _npc.getY(), dir, null));
						item.set_showId(_npc.get_showId());
						targetInventory = World.get().getInventory(_npc.getX() + x, _npc.getY() + y, _npc.getMapId());
						ListMapUtil.clear(dirList);
					}

					inventory.tradeItem(item, item.getCount(), targetInventory);
				}
				ListMapUtil.clear(list);
				// _npc.turnOnOffLight();

			} catch (final Exception e) {
				// _log.error(e.getLocalizedMessage(), e);

			} finally {
				// 移除此 ArrayList 中的所有元素
				ListMapUtil.clear(_acquisitorList);
				ListMapUtil.clear(_hateList);
			}
		}

		/**
		 * 物品平均分配(同畫面且在仇恨清單中)
		 * 
		 * @param npc
		 * @param recipientList
		 * @param itemId
		 */
		public void equalityDrop(L1NpcInstance npc, ArrayList<?> recipientList, int itemId) {
			// 無場合
			L1Inventory inventory = npc.getInventory();
			if (inventory.getSize() == 0) {
				return;
			}
			// 非對像者除外(、、中心畫面範圍外)
			L1Character recipient;
			L1Location pt = npc.getLocation();// 怪物所在地點
			for (int i = recipientList.size() - 1; i >= 0; i--) {
				recipient = (L1Character) recipientList.get(i);
				if (recipient instanceof L1SummonInstance || recipient instanceof L1PetInstance) {// 掉落目標為寵物或召喚獸
					recipientList.remove(i);
				} else if (recipient == null) {// 掉落目標為空
					recipientList.remove(i);
				} else if (npc.getMapId() != recipient.getMapId()) {// 不同地圖
					recipientList.remove(i);
				} else if (!pt.isInScreen(recipient.getLocation())) {// 不在畫面內
					if (npc.getNpcId() != 99019) {// 巨型骷髏除外
						recipientList.remove(i);
					}
				}
			}
			// 分配
			for (L1ItemInstance drop : inventory.getItems()) {
				for (int i = recipientList.size() - 1; i >= 0; i--) {
					L1PcInstance pc = (L1PcInstance) recipientList.get(i);
					if (drop.getItemId() == itemId) {// 物品編號相等
						if (pc.getInventory().checkAddItem(drop, drop.getCount()) == L1Inventory.OK) {
							pc.getInventory().storeItem(drop.getItemId(), drop.getCount());
							// 具有隊伍
							if (pc.isInParty()) {
								//final Object[] pcs = pc.getParty().partyUsers().values().toArray();
								// 7.6
								final Object[] pcs = pc.getParty().getMemberList().toArray();
								if (pcs.length <= 0) {
									return;
								}
								for (Object obj : pcs) {
									if (obj instanceof L1PcInstance) {
										final L1PcInstance tgpc = (L1PcInstance) obj;
										// 813 隊員%2%s 從%0 取得 %1%o
										tgpc.sendPackets(new S_ServerMessage(813, _npc.getNameId(), drop.getLogName(),
												pc.getName()));
									}

								}

							} else {
								// 143 \f1%0%s 給你 %1%o 。
								pc.sendPackets(new S_ServerMessage(143, _npc.getNameId(), drop.getLogName()));
							}
						} else {
							// 一杯場合足元
							drop.set_showId(npc.get_showId());
							L1Inventory ground = World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId());
							ground.storeItem(drop.getItemId(), drop.getCount());
						}
					}
				}
			}
		}

		/**
		 * 物品平均分配(只判定是否在畫面中)
		 * 
		 * @param npc
		 * @param recipientList
		 * @param itemId
		 */
		public void equalityDropScreen(L1NpcInstance npc, int itemId) {
			// 怪物背包無物品
			L1Inventory inventory = npc.getInventory();
			if (inventory.getSize() == 0) {
				return;
			}
			// 畫面內可見PC
			ArrayList<L1PcInstance> recipientList = World.get().getVisiblePlayer(npc);

			// 物品分配
			for (L1ItemInstance drop : inventory.getItems()) {
				for (int i = recipientList.size() - 1; i >= 0; i--) {
					L1PcInstance pc = recipientList.get(i);
					if (drop.getItemId() == itemId) {// 物品編號相等
						if (pc.getInventory().checkAddItem(drop, drop.getCount()) == L1Inventory.OK) {
							pc.getInventory().storeItem(drop.getItemId(), drop.getCount());
							// 具有隊伍
							if (pc.isInParty()) {
								//final Object[] pcs = pc.getParty().partyUsers().values().toArray();
								// 7.6
								final Object[] pcs = pc.getParty().getMemberList().toArray();
								if (pcs.length <= 0) {
									return;
								}
								for (Object obj : pcs) {
									if (obj instanceof L1PcInstance) {
										final L1PcInstance tgpc = (L1PcInstance) obj;
										// 813 隊員%2%s 從%0 取得 %1%o
										tgpc.sendPackets(new S_ServerMessage(813, _npc.getNameId(), drop.getLogName(),
												pc.getName()));
									}

								}

							} else {
								// 143 \f1%0%s 給你 %1%o 。
								pc.sendPackets(new S_ServerMessage(143, _npc.getNameId(), drop.getLogName()));
							}
						} else {
							// 一杯場合足元
							drop.set_showId(npc.get_showId());
							L1Inventory ground = World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId());
							ground.storeItem(drop.getItemId(), drop.getCount());
						}
					}
				}
			}
		}
	}
}