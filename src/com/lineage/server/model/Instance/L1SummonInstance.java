package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static com.lineage.server.model.skill.L1SkillId.PHANTASM;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.ActionCodes;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.drop.SetDrop;
import com.lineage.server.model.drop.SetDropExecutor;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_NPCPack_Summon;
import com.lineage.server.serverpackets.S_PetMenuPacket;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;

/**
 * 召喚獸控制項
 * @author Admin
 */
public class L1SummonInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	private static final Log _log = LogFactory.getLog(L1SummonInstance.class);

	private static final int _summonTime = 3600;

	private int _currentPetStatus;

	private int _checkMove = 0;

	private boolean _tamed;

	private boolean _isReturnToNature = false;

	private static Random _random = new Random();

	public boolean tamed() {
		return _tamed;
	}

	// 場合處理
	@Override
	public boolean noTarget() {
		switch (_currentPetStatus) {
		case 3: // 休息
			return true;

		case 4: // 散開
			if ((_master != null)
					&& (_master.getMapId() == getMapId())
					&& (getLocation().getTileLineDistance(_master.getLocation()) < 5)) {
				if (_npcMove != null) {
					int dir = _npcMove.targetReverseDirection(_master.getX(), _master.getY());
					dir = _npcMove.checkObject(dir);

					_npcMove.setDirectionMove(dir);
					setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
				}
			} else {
				_currentPetStatus = 3;
				return true;
			}
			break;

		case 5: // 警戒
			if ((Math.abs(getHomeX() - getX()) > 1)
					|| (Math.abs(getHomeY() - getY()) > 1)) {
				if (_npcMove != null) {
					int dir = _npcMove.moveDirection(getHomeX(), getHomeY());
					if (dir == -1) {
						// 離現在地
						setHomeX(getX());
						setHomeY(getY());
					} else {
						_npcMove.setDirectionMove(dir);
						setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
					}
				}
			}
			break;

		default:
			if ((_master != null) && (_master.getMapId() == getMapId())) {
				// 主人跟隨
				int location = getLocation().getTileLineDistance(_master.getLocation());
				//System.out.println("主人跟隨 距離: "+location);
				if (location > 2) {
					if (_npcMove != null) {
						int dir = _npcMove.moveDirection(_master.getX(), _master.getY());
						//System.out.println("控制者遺失次數: "+_checkMove);
						if (dir == -1) {
							_checkMove += 1;
							if (_checkMove >= 10) {
								// 控制者遺失
								//System.out.println("控制者遺失次數: "+_checkMove);
								_checkMove = 0;
								_currentPetStatus = 3;
								return true;
							}
						} else {
							_checkMove = 0;
							_npcMove.setDirectionMove(dir);
							setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
						}
					}
				}

			} else {
				// 控制者遺失
				//System.out.println("控制者遺失else");
				_currentPetStatus = 3;
				return true;
			}
			break;
		}
		return false;
	}

	/**
	 * 召喚獸
	 * @param template
	 * @param master
	 */
	public L1SummonInstance(L1Npc template, L1Character master) {
		super(template);
		setId(IdFactoryNpc.get().nextId());
		// 副本編號
		set_showId(master.get_showId());

		set_time(_summonTime);
		/*
		 * if (SummonTimer.MAP.get(this) == null) { SummonTimer.MAP.put(this,
		 * _summonTime); }
		 */

		setMaster(master);
		setX(master.getX() + _random.nextInt(5) - 2);
		setY(master.getY() + _random.nextInt(5) - 2);
		setMap(master.getMapId());
		setHeading(5);
		setLightSize(template.getLightSize());

		_currentPetStatus = 3;
		_tamed = false;

		World.get().storeObject(this);
		World.get().addVisibleObject(this);

		for (L1PcInstance pc : World.get().getRecognizePlayer(this)) {
			onPerceive(pc);
		}

		master.addPet(this);// 加入使用中寵物清單

		if (master instanceof L1PcInstance) {
			// 增加物件組人
			addMaster((L1PcInstance) master);

		} else if (master instanceof L1NpcInstance) {
			//L1NpcInstance npc = (L1NpcInstance) master;
			// 增加物件組人
			//npc.broadcastPacketX10(new S_NewMaster(npc.getNameId(), this));
			_currentPetStatus = 1;
			//System.out.println("增加物件組人:"+npc.getNameId());
		}
	}

	/**
	 * 造屍術
	 * @param target
	 * @param master
	 * @param isCreateZombie
	 */
	public L1SummonInstance(L1NpcInstance target, L1Character master, boolean isCreateZombie) {
		super(null);
		setId(IdFactoryNpc.get().nextId());
		// 副本編號
		set_showId(master.get_showId());

		if (isCreateZombie) { // 
			int npcId = 45065;
			final L1PcInstance pc = (L1PcInstance) master;
			final int level = pc.getLevel();
			if (pc.isWizard()) {
				if ((level >= 24) && (level <= 31)) {
					npcId = 81183;
				} else if ((level >= 32) && (level <= 39)) {
					npcId = 81184;
				} else if ((level >= 40) && (level <= 43)) {
					npcId = 81185;
				} else if ((level >= 44) && (level <= 47)) {
					npcId = 81186;
				} else if ((level >= 48) && (level <= 51)) {
					npcId = 81187;
				} else if (level >= 52) {
					npcId = 81188;
				}
			} else if ((pc.isElf()) && (level >= 48)) {
				npcId = 81183;
			}

			L1Npc template = NpcTable.get().getTemplate(npcId).clone();
			setting_template(template);
		} else {
			setting_template(target.getNpcTemplate());
			setCurrentHpDirect(target.getCurrentHp());
			setCurrentMpDirect(target.getCurrentMp());
		}

		set_time(_summonTime);

		setMaster(master);
		setX(target.getX());
		setY(target.getY());
		setMap(target.getMapId());
		setHeading(target.getHeading());
		setLightSize(target.getLightSize());
		setPetcost(6);

		if (((target instanceof L1MonsterInstance)) && (!((L1MonsterInstance) target).is_storeDroped())) {
			SetDropExecutor setDropExecutor = new SetDrop();
			setDropExecutor.setDrop(target, target.getInventory());
		}

		setInventory(target.getInventory());
		target.setInventory(null);

		_currentPetStatus = 3;
		_tamed = true;

		// 攻擊中場合止
		for (L1NpcInstance each : master.getPetList().values()) {
			each.targetRemove(target);
		}

		target.deleteMe();
		World.get().storeObject(this);
		World.get().addVisibleObject(this);

		for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
			this.onPerceive(pc);
		}

		master.addPet(this);

		if (master instanceof L1PcInstance) {
			// 增加物件組人
			addMaster((L1PcInstance) master);
		}
	}

	public L1SummonInstance(final L1Npc template, final L1Character master,
			final int summonTime) {
		super(template);
		this.setId(IdFactoryNpc.get().nextId());
		// 副本編號
		this.set_showId(master.get_showId());

		this.set_time(summonTime);

		this.setMaster(master);
		this.setX(master.getX() + _random.nextInt(5) - 2);
		this.setY(master.getY() + _random.nextInt(5) - 2);
		this.setMap(master.getMapId());
		this.setHeading(5);
		this.setLightSize(template.getLightSize());

		_currentPetStatus = 3;
		_tamed = false;

		World.get().storeObject(this);
		World.get().addVisibleObject(this);

		for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
			this.onPerceive(pc);
		}
		master.addPet(this);
		if (master instanceof L1PcInstance) {
			// 增加物件組人
			addMaster((L1PcInstance) master);

		} else if (master instanceof L1NpcInstance) {

			_currentPetStatus = 1;

		}
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) { // 攻擊ＨＰ減使用
		ISASCAPE = false;
		if (getCurrentHp() > 0) {
			if (damage > 0) {
				setHate(attacker, 0); // 無
				removeSkillEffect(FOG_OF_SLEEPING); // 66
				removeSkillEffect(PHANTASM); // 212

				if (!isExsistMaster()) {
					_currentPetStatus = 1;
					setTarget(attacker);
				}
			}

			if (((attacker instanceof L1PcInstance)) && (damage > 0)) {
				L1PcInstance player = (L1PcInstance) attacker;
				player.setPetTarget(this);
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0) {
				Death(attacker);
			} else {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) { // 念
			_log.error("NPC hp減少處理失敗 可能原因: 初始hp為0(" + getNpcId() + ")");
			Death(attacker);
		}
	}

	/**
	 * 死亡
	 * @param lastAttacker
	 */
	public synchronized void Death(L1Character lastAttacker) {
		if (!isDead()) {
			setDead(true);
			setCurrentHp(0);
			setStatus(ActionCodes.ACTION_Die);

			getMap().setPassable(getLocation(), true);

			// 怪物解散處理
			L1Inventory targetInventory = null; // 主人的背包
			if (this._master != null) {
				if (this._master.getInventory() != null) {// 主人存在並且背包不為空
					targetInventory = _master.getInventory();
				}
			}

			try {
				if (this._inventory != null) {
					List<L1ItemInstance> items = _inventory.getItems();
					if (items != null && !items.isEmpty()) {
						for (L1ItemInstance item : items) {
							if (targetInventory != null) {
								// 容量重量確認及送信
								if (_master.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
									_inventory.tradeItem(item, item.getCount(), targetInventory);
									// 143:\f1%0%s 給你 %1%o 。
									((L1PcInstance) _master).sendPackets(new S_ServerMessage(143, getName(), item.getLogName()));

								} else { // 超過持有物件數量(掉落地面)
									item.set_showId(get_showId());
									targetInventory = World.get().getInventory(getX(), getY(), getMapId());
									_inventory.tradeItem(item, item.getCount(), targetInventory);
								}
							} else { // 主人遺失(掉落地面)
								item.set_showId(get_showId());
								targetInventory = World.get().getInventory(getX(), getY(), getMapId());
								_inventory.tradeItem(item, item.getCount(), targetInventory);
							}
						}
					}
				}

			} catch (final Exception e) {
				_log.error(e.getLocalizedMessage(), e);
			}

			if (_tamed) {
				broadcastPacketAll(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die));
				// this.startDeleteTimer(ConfigAlt.NPC_DELETION_TIME * 2);
				this.startDeleteTimer(1);

			} else {
				deleteMe();
			}
		}
	}

	public synchronized void returnToNature() {
		_isReturnToNature = true;
		if (!_tamed) {
			getMap().setPassable(getLocation(), true);
			// 怪物解散處理
			L1Inventory targetInventory = null; // 主人的背包
			if (_master != null) {
				if (_master.getInventory() != null) { // 主人存在並且背包不為空
					targetInventory = _master.getInventory();
				}
			}

			List<L1ItemInstance> items = _inventory.getItems();
			for (L1ItemInstance item : items) {
				if (targetInventory != null) {
					// 容量重量確認及送信
					if (_master.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
						_inventory.tradeItem(item, item.getCount(), targetInventory);
						// 143:\f1%0%s 給你 %1%o 。
						((L1PcInstance) _master).sendPackets(new S_ServerMessage(143, getName(), item.getLogName()));

					} else { // 超過持有物件數量(掉落地面)
						item.set_showId(get_showId());
						targetInventory = World.get().getInventory(getX(), getY(), getMapId());
						_inventory.tradeItem(item, item.getCount(), targetInventory);
					}

				} else { // 主人遺失(掉落地面)
					item.set_showId(get_showId());
					targetInventory = World.get().getInventory(getX(), getY(), getMapId());
					_inventory.tradeItem(item, item.getCount(), targetInventory);
				}
			}
			deleteMe();

		} else {
			liberate();
		}
	}

	// 消去處理
	@Override
	public synchronized void deleteMe() {
		if (_master != null) {
			_master.removePet(this);
		}
		if (_destroyed) {
			return;
		}
		if ((!_tamed) && (!_isReturnToNature)) {
			broadcastPacketX8(new S_SkillSound(getId(), 169));
		}
		//_master.getPetList().remove(getId());
		super.deleteMe();
	}

	// 、時解放處理
	public void liberate() {
		L1MonsterInstance monster = new L1MonsterInstance(getNpcTemplate());
		monster.setId(IdFactoryNpc.get().nextId());

		monster.setX(getX());
		monster.setY(getY());
		monster.setMap(getMapId());
		monster.setHeading(getHeading());
		monster.set_storeDroped(true);
		monster.setInventory(getInventory());
		setInventory(null);
		monster.setCurrentHpDirect(getCurrentHp());
		monster.setCurrentMpDirect(getCurrentMp());
		monster.setExp(0L);

		deleteMe();
		World.get().storeObject(monster);
		World.get().addVisibleObject(monster);
	}

	public void setTarget(L1Character target) {
		if ((target != null)
				&& ((_currentPetStatus == 1)
						|| (_currentPetStatus == 2)
						|| (_currentPetStatus == 5))
		) {
			setHate(target, 0);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	/**
	 * 設置主人目標
	 * @param target
	 */
	public void setMasterTarget(L1Character target) {
		//System.out.println("設置主人目標");
		if ((target != null)
				&& ((_currentPetStatus == 1) // 攻擊
						|| (_currentPetStatus == 5)) // 警戒
		) {
			setHate(target, 10);
			if (!isAiRunning()) {
				startAI();
			}
		}
	}

	/**
	 * 對該物件攻擊的調用
	 */
	@Override
	public void onAction(L1PcInstance player) {
		if (player == null) {
			return;
		}
		L1Character cha = getMaster();
		if (cha == null) {
			return;
		}
		L1PcInstance master = (L1PcInstance) cha;
		if (master.isTeleport()) { // 傳送處理中
			return;
		}
		if (master.equals(player)) {// 攻擊者是主人
			L1AttackMode attack_mortion = new L1AttackPc(player, this); // 攻擊判斷
			attack_mortion.action();
			return;
		}
		if (((isSafetyZone()) || (player.isSafetyZone())) && (isExsistMaster())) {// 安全區域中
			L1AttackMode attack_mortion = new L1AttackPc(player, this); // 攻擊判斷
			attack_mortion.action();
			return;
		}

		if (player.checkNonPvP(player, this)) {
			return;
		}

		L1AttackMode attack = new L1AttackPc(player, this);
		if (attack.calcHit()) {
			attack.calcDamage();
		}
		attack.action();
		attack.commit();
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		if (isDead()) {
			return;
		}
		if (_master.equals(player)) {
			player.sendPackets(new S_PetMenuPacket(this, 0));
		}
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
		int status = Integer.parseInt(action);
		//int status = ActionType(action);
		switch (status) {
		case 0:
			return;

		case 6: // 解散
			if (_tamed) {
				// 、解放
				liberate();
			} else {
				// 解散
				Death(null);
			}
			break;

		default:
			// 同主人狀態更新
			Object[] petList = _master.getPetList().values().toArray();
			for (Object petObject : petList) {
				if ((petObject instanceof L1SummonInstance)) {
					// 
					L1SummonInstance summon = (L1SummonInstance) petObject;
					summon.set_currentPetStatus(status);
					
				} else {
					// 
				}
			}
			break;
		}
	}

	/**
	 * TODO 接觸資訊
	 */
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		try {
			// 副本ID不相等 不相護顯示
			if (perceivedFrom.get_showId() != get_showId()) {
				return;
			}
			perceivedFrom.addKnownObject(this);
			perceivedFrom.sendPackets(new S_NPCPack_Summon(this, perceivedFrom));
			if ((getMaster() != null) && (perceivedFrom.getId() == getMaster().getId())) {
				//perceivedFrom.sendPackets(new S_HPMeter(getId(), 100 * getCurrentHp() / getMaxHp()));
				// 7.6
				perceivedFrom.sendPackets(new S_HPMeter(getId(), (100 * getCurrentHp()) / getMaxHp(), 0xff));
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void onItemUse() {
		if (!isActived()) {
			// １００％確率使用
			useItem(USEITEM_HASTE, 100);
		}
		if (getCurrentHp() * 100 / getMaxHp() < 40) {
			// ＨＰ４０％
			// １００％確率回覆使用
			useItem(USEITEM_HEAL, 100);
		}
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		if (getNpcTemplate().get_digestitem() > 0) {
			setDigestItem(item);
		}
		Arrays.sort(healPotions);
		Arrays.sort(haestPotions);
		if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
			if (getCurrentHp() != getMaxHp()) {
				useItem(USEITEM_HEAL, 100);
			}
		} else if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
			useItem(USEITEM_HASTE, 100);
		}
	}

	@Override
	public void setCurrentHp(int i) {
		int currentHp = Math.min(i, getMaxHp());

		if (getCurrentHp() == currentHp) {
			return;
		}

		setCurrentHpDirect(currentHp);

		// 寵物血條更新
		if (_master instanceof L1PcInstance) {
			final int hpRatio = (100 * currentHp) / getMaxHp();
			L1PcInstance master = (L1PcInstance) _master;
			//master.sendPackets(new S_HPMeter(getId(), hpRatio));
			master.sendPackets(new S_HPMeter(getId(), hpRatio, 0xff));// 7.6
		}
	}

	@Override
	public void setCurrentMp(int i) {
		int currentMp = Math.min(i, getMaxMp());

		if (getCurrentMp() == currentMp) {
			return;
		}

		setCurrentMpDirect(currentMp);
	}

	public void set_currentPetStatus(int i) {
		_currentPetStatus = i;
		set_tempModel();
		switch (_currentPetStatus) {
		case 5:
			setHomeX(getX());
			setHomeY(getY());
			break;

		case 3:
			allTargetClear();
			break;

		// case 4:
		default:
			if (!isAiRunning()) {
				startAI();
			}
			break;
		}
	}

	public int get_currentPetStatus() {
		return _currentPetStatus;
	}

	/**
	 * 主人是否在線上
	 * @return
	 */
	public boolean isExsistMaster() {
		boolean isExsistMaster = true;
		if (getMaster() != null) {
			String masterName = getMaster().getName();
			if (World.get().getPlayer(masterName) == null) {
				isExsistMaster = false;
			}
		}
		return isExsistMaster;
	}

	private int _time = 0;

	/**
	 * 剩餘使用時間
	 * @return
	 */
	public int get_time() {
		return _time;
	}

	/**
	 * 設置剩餘使用時間
	 * @param time
	 */
	public void set_time(int time) {
		_time = time;
	}

	private int _tempModel = 3;

	public void set_tempModel() {
		_tempModel = _currentPetStatus;
	}

	public void get_tempModel() {
		_currentPetStatus = _tempModel;
	}
}
