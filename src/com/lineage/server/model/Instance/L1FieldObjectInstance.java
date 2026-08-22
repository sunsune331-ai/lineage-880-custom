package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.CANCELLATION;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1HauntedHouse;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack_F;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

/**
 * 對像:景觀 控制項
 * @author dexc
 */
public class L1FieldObjectInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	private static final Log _log = LogFactory.getLog(L1FieldObjectInstance.class);

	public L1FieldObjectInstance(final L1Npc template) {
		super(template);
	}

	/**
	 * TODO 接觸資訊
	 */
	@Override
	public void onPerceive(final L1PcInstance perceivedFrom) {
		try {
			perceivedFrom.addKnownObject(this);
			perceivedFrom.sendPackets(new S_NPCPack_F(this));

			// 動作佈景召喚系統
			if (_fieldAI == null && getActionType() != -1) {
				_fieldAI = new fieldAI(this);
				_fieldAI.begin();
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	// 動作佈景召喚系統
	private fieldAI _fieldAI = null;

	private class fieldAI implements Runnable {

		private final L1FieldObjectInstance _npc;

		private fieldAI(final L1FieldObjectInstance npc) {
			_npc = npc;
		}

		@Override
		public void run() {
			// 動作間隔時間
			int sleep = _npc.getActionTime();
			if (sleep <= 0) {
				sleep = 1000;
			}

			int actid = ActionCodes.ACTION_Attack;
//			int moveRange = 0;
//			int moveDirection = 0;

			for (int i = 0; i < 5; i++) {

				if (_destroyed) {
					return;
				}

				switch (_npc.getActionType()) {

//				case -1:
//					break;

				case 1: // just attack
					_npc.broadcastPacketAll(new S_DoActionGFX(_npc.getId(), actid));
					break;

//				case 21: // bow attack
//					final L1Character dummy = new L1Character(); // null
//					dummy.setX(_npc.getX());
//					dummy.setY(_npc.getY() - 8);
//					_npc.broadcastPacket(new S_AttackPacket(_npc, dummy, actid, 2349, 0, S_AttackPacket.GFX_ARROW,
//							S_AttackPacket.EFF_NONE));
//					break;
//
//				case 0: // move
//					if (moveRange == 0) {
//						moveRange = Random.nextInt(5) + 1; // 沒目標時預計移動的距離
//						moveDirection = Random.nextInt(8 + 10); // 沒目標時移動的方向+不移動的可能
//						// 33% 的機率往HOME點移動
//						if ((getHomeX() != 0) && (getHomeY() != 0) && (moveDirection < 8) && (Random.nextInt(3) == 0)) {
//							moveDirection = getNextMoveDirection(getHomeX(), getHomeY());
//						}
//					} else {
//						moveRange--;
//					}
//					final int dir = nextPassableDirection(getX(), getY(), getMapId(), moveDirection);
//					if (dir != -1) {
//						moveToDirection(dir);
//					}
//					actid = ActionCodes.ACTION_Walk;
//					break;
//
//				case -10: // move
//					if (moveRange == 0) {
//						moveRange = Random.nextInt(15) + 1; // 沒目標時預計移動的距離
//						moveDirection = Random.nextInt(8 + 10); // 沒目標時移動的方向+不移動的可能
//						// 33% 的機率往HOME點移動
//						if ((getHomeX() != 0) && (getHomeY() != 0) && (moveDirection < 8) && (Random.nextInt(3) == 0)) {
//							moveDirection = getNextMoveDirection(getHomeX(), getHomeY());
//						}
//					} else {
//						moveRange--;
//					}
//					final int _dir = nextPassableDirection(getX(), getY(), getMapId(), moveDirection);
//					if (_dir != -1) {
//						moveToDirection(_dir);
//					}
//					actid = ActionCodes.ACTION_Walk;
//
//					for (final L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_npc, 2)) {
//						if (Random.nextInt(100) < 25) { // 25%
//							final int[][] locs = { { 32718, 33128 }, { 32815, 33124 }, { 32810, 33206 },
//									{ 32714, 33219 } };
//							final int[] loc = locs[Random.nextInt(locs.length)];
//							L1Teleport.teleport(pc, loc[0], loc[1], 4, 3, true);
//						} else {
//							L1Teleport.randomTeleport(pc, 50);
//						}
//					}
//					break;

				case -2:
					_npc.broadcastPacketAll(new S_DoActionGFX(_npc.getId(), 3));
					break;

				default:
					_npc.broadcastPacketAll(new S_DoActionGFX(_npc.getId(), _npc.getActionType()));
					actid = _npc.getActionType();
					break;
				}

				// interval
				try {
					// final int temp = ListSprReader.getInstance().getSprSpeed(_npc.getCurrentGfxId(), actid);
					// final int temp = SprTable.get().getSprSpeed(_npc.getTempCharGfx(), actid);
					// Thread.sleep(temp > 0 ? temp : sleep);

					Thread.sleep(sleep);

				} catch (final InterruptedException e) {
					_log.error(e.getLocalizedMessage(), e);
				}
			}

			// 如果旁邊還有人 繼續做
			if (!World.get().getVisiblePlayer(_npc).isEmpty()) {
				this.begin();
			} else {
				_fieldAI = null;
			}
		}

		private void begin() {
			// new Thread(this).start();
			GeneralThreadPool.get().execute(this);
		}
	}

	@Override
	public void onAction(final L1PcInstance pc) {
		if (getNpcTemplate().get_npcId() == 81171) {
			if (L1HauntedHouse.getInstance().getHauntedHouseStatus() == L1HauntedHouse.STATUS_PLAYING) {
				final int winnersCount = L1HauntedHouse.getInstance().getWinnersCount();
				final int goalCount = L1HauntedHouse.getInstance().getGoalCount();
				if (winnersCount == goalCount + 1) {
					final L1ItemInstance item = ItemTable.get().createItem(49280);
					final int count = 1;
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
							item.setCount(count);
							pc.getInventory().storeItem(item);
							// %0手入。
							pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
						}
					}
					L1HauntedHouse.getInstance().endHauntedHouse();

				} else if (winnersCount > goalCount + 1) {
					L1HauntedHouse.getInstance().setGoalCount(goalCount + 1);
					L1HauntedHouse.getInstance().removeMember(pc);
					L1ItemInstance item = null;
					if (winnersCount == 3) {
						if (goalCount == 1) {
							item = ItemTable.get().createItem(49278);
						} else if (goalCount == 2) {
							item = ItemTable.get().createItem(49279);
						}
					} else if (winnersCount == 2) {
						item = ItemTable.get().createItem(49279);
					}
					final int count = 1;
					if (item != null) {
						if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
							item.setCount(count);
							pc.getInventory().storeItem(item);
							// %0手入。
							pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
						}
					}

					final L1SkillUse l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(pc, CANCELLATION, pc.getId(), pc.getX(), pc.getY(), 0,
							L1SkillUse.TYPE_LOGIN);
					L1Teleport.teleport(pc, 32624, 32813, (short) 4, 5, true);
				}
			}
		}
	}

	@Override
	public void deleteMe() {
		try {
			_destroyed = true;
			if (getInventory() != null) {
				getInventory().clearItems();
			}
			World.get().removeVisibleObject(this);
			World.get().removeObject(this);
			for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
				pc.removeKnownObject(this);
				pc.sendPackets(new S_RemoveObject(this));
			}
			removeAllKnownObjects();

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	// 動作佈景召喚系統
	private int action_type = -1;

	public int getActionType() {
		return action_type;
	}

	public void setActionType(final int i) {
		action_type = i;
	}

	// 動作佈景召喚系統
	private int action_time = 1000;

	public int getActionTime() {
		return action_time;
	}

	public void setActionTime(final int i) {
		action_time = i;
	}
}
