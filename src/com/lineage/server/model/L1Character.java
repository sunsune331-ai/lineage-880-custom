package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.BONE_BREAK;
import static com.lineage.server.model.skill.L1SkillId.DARK_BLIND;
import static com.lineage.server.model.skill.L1SkillId.EARTH_BIND;
import static com.lineage.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static com.lineage.server.model.skill.L1SkillId.ICE_LANCE;
import static com.lineage.server.model.skill.L1SkillId.PHANTASM;
import static com.lineage.server.model.skill.L1SkillId.SHOCK_STUN;
import static com.lineage.server.model.skill.L1SkillId.STATUS_CURSE_PARALYZED;
import static com.lineage.server.model.skill.L1SkillId.STATUS_POISON_PARALYZED;
import static com.lineage.server.model.skill.L1SkillId.DESPERADO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigOther;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1DollInstance2;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.poison.L1Poison;
import com.lineage.server.model.skill.L1SkillTimer;
import com.lineage.server.model.skill.L1SkillTimerCreator;
import com.lineage.server.serverpackets.S_ACTION_UI;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_Light;
import com.lineage.server.serverpackets.S_PetCtrlMenu;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_SkillIconPoison;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.types.Point;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.world.World;

public class L1Character extends L1Object {
	private static final Log _log = LogFactory.getLog(L1Character.class);
	private static final long serialVersionUID = 1L;
	private L1Poison _poison = null;
	private boolean _sleeped;
	private final Map<Integer, L1NpcInstance> _petlist = new HashMap<Integer, L1NpcInstance>();

	private final HashMap<Integer, L1SkillTimer> _skillEffect = new HashMap<Integer, L1SkillTimer>();

	private final Map<Integer, L1ItemDelay.ItemDelayTimer> _itemdelay = new HashMap<Integer, L1ItemDelay.ItemDelayTimer>();

	private final Map<Integer, L1FollowerInstance> _followerlist = new HashMap<Integer, L1FollowerInstance>();

	private int _secHp = -1;
	private int _currentHp;
	private int _currentMp;
	private boolean _paralyzed;
	L1Paralysis _paralysis;
	protected static final byte[] HEADING_TABLE_X = { 0, 1, 1, 1, 0, -1, -1, -1 };
	protected static final byte[] HEADING_TABLE_Y = { -1, -1, 0, 1, 1, 1, 0, -1 };

	private boolean _isSkillDelay = false;

	private final Map<Integer, L1DollInstance> _dolls = new HashMap<Integer, L1DollInstance>();
	private long _exp;
	private final List<L1Object> _knownObjects = new CopyOnWriteArrayList<L1Object>();
	private final List<L1PcInstance> _knownPlayer = new CopyOnWriteArrayList<L1PcInstance>();
	private String _name;
	private int _level;
	private int _maxHp = 0;
	private int _trueMaxHp = 0;

	private short _maxMp = 0;
	private int _trueMaxMp = 0;

	private int _ac = 10;
	private int _trueAc = 0;

	private short _str = 0;
	private short _trueStr = 0;

	private short _con = 0;
	private short _trueCon = 0;

	private short _dex = 0;
	private short _trueDex = 0;

	private short _cha = 0;
	private short _trueCha = 0;

	private short _int = 0;
	private short _trueInt = 0;

	private short _wis = 0;
	private short _trueWis = 0;

	private int _wind = 0;
	private int _trueWind = 0;

	private int _water = 0;
	private int _trueWater = 0;

	private int _fire = 0;
	private int _trueFire = 0;

	private int _earth = 0;
	private int _trueEarth = 0;
	private int _addAttrKind;

	// private int _registStun = 0;
	// private int _trueRegistStun = 0;
	//
	// private int _registStone = 0;
	// private int _trueRegistStone = 0;
	//
	// private int _registSleep = 0;
	// private int _trueRegistSleep = 0;
	//
	// private int _registFreeze = 0;
	// private int _trueRegistFreeze = 0;
	//
	// private int _registSustain = 0;
	// private int _trueRegistSustain = 0;
	//
	// private int _registBlind = 0;
	// private int _trueRegistBlind = 0;

	private int _registTechnology = 0; // 技術耐性
	private int _trueRegistTechnology = 0; // 技術耐性
	
	private int _registElf = 0; // 精靈耐性
	private int _trueRegistElf = 0; // 精靈耐性

	private int _registDragon = 0; // 龍屬耐性
	private int _trueRegistDragon = 0; // 龍屬耐性

	private int _registHorror = 0; // 恐怖耐性
	private int _trueRegistHorror = 0; // 恐怖耐性

	private int _registAll = 0; // 全部耐性
	private int _trueRegistAll = 0; // 全部耐性

	private int _dmgup = 0;
	private int _trueDmgup = 0;

	private int _bowDmgup = 0;
	private int _trueBowDmgup = 0;

	private int _hitup = 0;
	private int _trueHitup = 0;

	private int _bowHitup = 0;
	private int _trueBowHitup = 0;

	private int _mr = 0;
	private int _trueMr = 0;

	private int _sp = 0;
	private boolean _isDead;
	private int _status;
	private String _title;
	private int _lawful;
	private int _heading;
	private int _moveSpeed;
	private int _braveSpeed;
	private int _tempCharGfx;
	private int _gfxid;
	private int _karma;
	private int _chaLightSize;
	private int _ownLightSize;
	private int _tmp_targetid;
	private int _tmp_mr;
	private int _dodge_up = 0;

	private int _dodge_down = 0;

	private boolean _decay_potion = false;
	private int _innRoomNumber;
	private boolean _isHall;

	public L1Character() {
		_level = 1;
	}

	/**
	 * 傳回與目標相反的面向
	 * 
	 * @param cha
	 * @return heading
	 */
	public int targetReverseHeading(L1Character cha) {
		int heading = cha.getHeading();
		heading += 4;
		if (heading > 7) {
			heading -= 8;
		}
		return heading;
	}

	public void resurrect(int hp) {
		try {
			if (!isDead()) {
				return;
			}
			if (hp <= 0) {
				hp = 1;
			}

			setDead(false);

			setCurrentHp(hp);

			setStatus(0);

			L1PolyMorph.undoPoly(this);

			for (L1PcInstance pc : World.get().getRecognizePlayer(this)) {
				pc.sendPackets(new S_RemoveObject(this));
				pc.removeKnownObject(this);
				pc.updateObject();
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	/**
	 * 發送該物件可見HP
	 * 
	 * @param pc
	 */
	public void broadcastPacketHP(L1PcInstance pc) {
		try {
			if (_secHp != getCurrentHp()) {
				_secHp = getCurrentHp();
				pc.sendPackets(new S_HPMeter(this));
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public int getCurrentHp() {
		return _currentHp;
	}

	public void setCurrentHp(int i) {
		_currentHp = i;
		if (_currentHp >= getMaxHp())
			_currentHp = getMaxHp();
	}

	public void setCurrentHpDirect(int i) {
		_currentHp = i;
	}

	public int getCurrentMp() {
		return _currentMp;
	}

	public void setCurrentMp(int i) {
		_currentMp = i;
		if (_currentMp >= getMaxMp())
			_currentMp = getMaxMp();
	}

	public void setCurrentMpDirect(int i) {
		_currentMp = i;
	}

	public boolean isSleeped() {
		return _sleeped;
	}

	public void setSleeped(boolean sleeped) {
		_sleeped = sleeped;
	}

	/**
	 * 無法攻擊/使用道具/技能/回城的狀態
	 * 
	 * @return true:狀態中 false:無
	 */
	public boolean isParalyzedX() {
		if (hasSkillEffect(STATUS_CURSE_PARALYZED)) {
			return true;
		}

		if (hasSkillEffect(STATUS_POISON_PARALYZED)) {
			return true;
		}

		if (hasSkillEffect(FOG_OF_SLEEPING)) {
			return true;
		}

		if (hasSkillEffect(SHOCK_STUN)) {
			return true;
		}

		if (hasSkillEffect(PHANTASM)) {
			return true;
		}

		if (hasSkillEffect(ICE_LANCE)) {
			return true;
		}

		if (hasSkillEffect(EARTH_BIND)) {
			return true;
		}

		if (hasSkillEffect(DARK_BLIND)) {
			return true;
		}

		if (hasSkillEffect(BONE_BREAK)) {
			return true;
		}

		return false;
	}

	/**
	 * 鬼魂/傳送/商店
	 * 
	 * @param pc
	 * 
	 * @return  true:狀態中 false:無
	 */
	public boolean isParalyzedX1(final L1PcInstance pc) {
		// 鬼魂模式
		if (pc.isGhost()) {
			return true; 
		}
		// 傳送中
		if (pc.isTeleport()) {
			return true;
		}
		// 商店村模式
		if (pc.isPrivateShop()) {
			return true;
		}
		return false;
	}

	public boolean isParalyzed() {
		return _paralyzed;
	}

	public void setParalyzed(boolean paralyzed) {
		_paralyzed = paralyzed;
	}

	public L1Paralysis getParalysis() {
		return _paralysis;
	}

	public void setParalaysis(L1Paralysis p) {
		_paralysis = p;
	}

	public void cureParalaysis() {
		if (_paralysis != null)
			_paralysis.cure();
	}
	/**
	 * 該物件全部可見範圍封包發送(不包含自己)自定義 防具
	 * 
	 * @param packet
	 *            封包
	 */
	public void broadcastPacketArmorYN(final ServerBasePacket packet) {
		try {
			for (final L1PcInstance pc : World.get().getVisiblePlayer(this)) {
				// 副本ID相等
				if (pc.get_showId() == this.get_showId()
						&& pc.armorhe()
						&& !L1CastleLocation.checkInAllWarArea(pc.getX(),
								pc.getY(), pc.getMapId())) {
					pc.sendPackets(packet);
				}
			}
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 該物件全部可見範圍封包發送(不包含自己)自定義 武器
	 * 
	 * @param packet
	 *            封包
	 */
	public void broadcastPacketYN(final ServerBasePacket packet) {
		try {
			for (final L1PcInstance pc : World.get().getVisiblePlayer(this)) {
				// 副本ID相等
				/*
				 * if (pc.get_showId() == this.get_showId()&& pc.attackhe() &&
				 * L1CastleLocation.checkInAllWarArea(pc.getX(), pc.getY(),
				 * pc.getMapId())) {
				 */
			
				if (pc.get_showId() == this.get_showId()
						&& pc.attackhe()
						&& !L1CastleLocation.checkInAllWarArea(pc.getX(),
								pc.getY(), pc.getMapId())) {
					pc.sendPackets(packet);
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	/**
	 * 該物件全部可見範圍封包發送(不包含自己)
	 * 
	 * @param packet
	 *            封包
	 */
	public void broadcastPacketAll(ServerBasePacket packet) {
		try {
			for (L1PcInstance pc : World.get().getVisiblePlayer(this)) {
				if (pc.get_showId() == get_showId())
					pc.sendPackets(packet);
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	/**
	 * 該物件指定範圍封包發送(範圍10)
	 * 
	 * @param packet
	 *            封包
	 */
	public void broadcastPacketX10(ServerBasePacket packet) {
		try {
			for (L1PcInstance pc : World.get().getVisiblePlayer(this, 10)) {
				if (pc.get_showId() == get_showId())
					pc.sendPackets(packet);
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	/**
	 * 該物件指定範圍封包發送(範圍8)
	 * 
	 * @param packet
	 *            封包
	 */
	public void broadcastPacketX8(ServerBasePacket packet) {
		try {
			for (L1PcInstance pc : World.get().getVisiblePlayer(this, 8)) {
				if (pc.get_showId() == get_showId())
					pc.sendPackets(packet);
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	/**
	 * 該物件指定範圍封包發送(指定範圍)
	 * 
	 * @param packet
	 *            封包
	 * @param r
	 *            指定範圍
	 */
	public void broadcastPacketXR(ServerBasePacket packet, int r) {
		try {
			for (L1PcInstance pc : World.get().getVisiblePlayer(this, r)) {
				if (pc.get_showId() == get_showId())
					pc.sendPackets(packet);
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	/**
	 * 該物件50格範圍封包發送
	 * 
	 * @param packet
	 *            封包
	 */
	public void wideBroadcastPacket(ServerBasePacket packet) {
		for (L1PcInstance pc : World.get().getVisiblePlayer(this, 50)) {
			if (pc.get_showId() == get_showId())
				pc.sendPackets(packet);
		}
	}
	  public void broadcastPacketBossWeaponAll(ServerBasePacket packet)
	  {
	    try
	    {
	      for (L1PcInstance pc : World.get().getVisiblePlayer(this))
	      {
				if (pc.get_showId() == this.get_showId()
						&& pc.attackhe()
						&& !L1CastleLocation.checkInAllWarArea(pc.getX(),
								pc.getY(), pc.getMapId())) {
					pc.sendPackets(packet);
				}
	      }
	    }
	    catch (Exception e) {
	      _log.error(e.getLocalizedMessage(), e);
	    }
	  }
		/**
		 * 該物件可見範圍封包發送, (指定物件)
		 * 
		 * @param packet
		 *            封包
		 * @param target
		 *            指定物件
		 */
	  public void broadcastPacketExceptTargetSight(ServerBasePacket packet, L1Character target) {
		boolean isX8 = false;

		if (ServerWarExecutor.get().checkCastleWar() > 0) {
			isX8 = true;
		}
		if (isX8) {
			for (L1PcInstance tgpc : World.get().getVisiblePlayerExceptTargetSight(this, target, 8)) {
				tgpc.sendPackets(packet);
			}
		} else
			for (L1PcInstance tgpc : World.get().getVisiblePlayerExceptTargetSight(this, target))
				tgpc.sendPackets(packet);
	}

	public int[] getFrontLoc() {
		int[] loc = new int[2];
		int x = getX();
		int y = getY();
		int heading = getHeading();

		x += HEADING_TABLE_X[heading];
		y += HEADING_TABLE_Y[heading];

		loc[0] = x;
		loc[1] = y;
		return loc;
	}

	public int targetDirection(int tx, int ty) {
		float dis_x = Math.abs(getX() - tx);
		float dis_y = Math.abs(getY() - ty);
		float dis = Math.max(dis_x, dis_y);
		if (dis == 0.0F) {
			return getHeading();
		}
		int avg_x = (int) Math.floor(dis_x / dis + 0.59F);
		int avg_y = (int) Math.floor(dis_y / dis + 0.59F);

		int dir_x = 0;
		int dir_y = 0;
		if (getX() < tx) {
			dir_x = 1;
		}
		if (getX() > tx) {
			dir_x = -1;
		}
		if (getY() < ty) {
			dir_y = 1;
		}
		if (getY() > ty) {
			dir_y = -1;
		}

		if (avg_x == 0) {
			dir_x = 0;
		}
		if (avg_y == 0) {
			dir_y = 0;
		}

		switch (dir_x) {
		case -1:
			switch (dir_y) {
			case -1:
				return 7;
			case 0:
				return 6;
			case 1:
				return 5;
			}
			break;
		case 0:
			switch (dir_y) {
			case -1:
				return 0;
			case 1:
				return 4;
			case 0:
			}
			break;
		case 1:
			switch (dir_y) {
			case -1:
				return 1;
			case 0:
				return 2;
			case 1:
				return 3;
			}
			break;
		}
		return getHeading();
	}

	/**
	 * 檢查是否有障礙物阻擋
	 * 
	 * @param tx
	 * @param ty
	 * @return true:無障礙物;false:有障礙物阻擋
	 */
	public boolean glanceCheck(int tx, int ty) {
		L1Map map = getMap();
		int chx = getX();
		int chy = getY();

		for (int i = 0; i < 15; i++) {
			if (((chx == tx) && (chy == ty)) || ((chx + 1 == tx) && (chy - 1 == ty)) || ((chx + 1 == tx) && (chy == ty))
					|| ((chx + 1 == tx) && (chy + 1 == ty)) || ((chx == tx) && (chy + 1 == ty))
					|| ((chx - 1 == tx) && (chy + 1 == ty)) || ((chx - 1 == tx) && (chy == ty))
					|| ((chx - 1 == tx) && (chy - 1 == ty)) || ((chx == tx) && (chy - 1 == ty))) {
				break;
			}
			int th = targetDirection(tx, ty);
			// System.out.println("("+i+")_isHit:" + map.isArrowPassable(chx,
			// chy, th)+","+chx+","+chy+","+th+","+ getName());
			if (!map.isArrowPassable(chx, chy, th)) {
				return false;
			}
			if (chx < tx) {
				if (chy == ty) {
					chx++;
				} else if (chy > ty) {
					chx++;
					chy--;
				} else if (chy < ty) {
					chx++;
					chy++;
				}

			} else if (chx == tx) {
				if (chy < ty) {
					chy++;
				} else if (chy > ty) {
					chy--;
				}
			} else if (chx > tx) {
				if (chy == ty) {
					chx--;
				} else if (chy < ty) {
					chx--;
					chy++;
				} else if (chy > ty) {
					chx--;
					chy--;
				}
			}
		}

		return true;
	}

	/**
	 * 是否到達可攻擊的距離
	 * 
	 * @param x
	 *            目標的X座標
	 * @param y
	 *            目標的Y座標
	 * @param range
	 *            攻擊距離
	 * @return
	 */
	public boolean isAttackPosition(int x, int y, int range) {
		if (getLocation().getTileLineDistance(new Point(x, y)) > range) {
			return false;
		}

		return glanceCheck(x, y);
	}

	public L1Inventory getInventory() {
		return null;
	}

	/**
	 * 加入技能效果清單
	 * 
	 * @param skillId
	 * @param timeMillis
	 */
	private void addSkillEffect(int skillId, int timeMillis) {
		L1SkillTimer timer = null;
		if (timeMillis > 0) {
			timer = L1SkillTimerCreator.create(this, skillId, timeMillis);
			timer.begin();
		}
		_skillEffect.put(Integer.valueOf(skillId), timer);
	}

	/**
	 * 給予技能效果與時間
	 * 
	 * @param skillId
	 * @param timeMillis
	 */
	public void setSkillEffect(int skillId, int timeMillis) {
		L1SkillTimer timer = (L1SkillTimer) _skillEffect.get(Integer.valueOf(skillId));
		if (timer != null) {// 已有計時器
			int remainingTimeMills = timer.getRemainingTime();
			timeMillis /= 1000;

			if ((remainingTimeMills >= 0) && ((remainingTimeMills < timeMillis) || (timeMillis == 0))) {
				timer.setRemainingTime(timeMillis);
			}
		} else {// 沒有計時器
			addSkillEffect(skillId, timeMillis);
		}
	}

	/**
	 * 刪除技能效果
	 * 
	 * @param skillId
	 */
	public void removeSkillEffect(int skillId) {
		L1SkillTimer timer = (L1SkillTimer) _skillEffect.remove(Integer.valueOf(skillId));
		if (timer != null) {
			timer.end();
		}
	}

	/**
	 * 刪除全部技能效果
	 */
	public void clearAllSkill() {
		for (L1SkillTimer timer : _skillEffect.values()) {
			if (timer != null) {
				timer.end();
			}
		}
		_skillEffect.clear();
	}

	/**
	 * 刪除技能計時器
	 * 
	 * @param skillId
	 */
	public void killSkillEffectTimer(int skillId) {
		L1SkillTimer timer = (L1SkillTimer) _skillEffect.remove(Integer.valueOf(skillId));
		if (timer != null) {
			timer.kill();
		}
	}

	/**
	 * 刪除全部技能計時器
	 */
	public void clearSkillEffectTimer() {
		for (L1SkillTimer timer : _skillEffect.values()) {
			if (timer != null) {
				timer.kill();
			}
		}
		_skillEffect.clear();
	}

	public boolean hasSkillEffect(int skillId) {
		return _skillEffect.containsKey(Integer.valueOf(skillId));
	}

	public Set<Integer> getSkillEffect() {
		return _skillEffect.keySet();
	}

	public boolean getSkillisEmpty() {
		return _skillEffect.isEmpty();
	}

	public int getSkillEffectTimeSec(int skillId) {
		L1SkillTimer timer = (L1SkillTimer) _skillEffect.get(Integer.valueOf(skillId));
		if (timer == null) {
			return -1;
		}
		return timer.getRemainingTime();
	}

	public void setSkillDelay(boolean flag) {
		_isSkillDelay = flag;
	}

	public boolean isSkillDelay() {
		return _isSkillDelay;
	}

	public void addItemDelay(int delayId, L1ItemDelay.ItemDelayTimer timer) {
		_itemdelay.put(Integer.valueOf(delayId), timer);
	}

	public void removeItemDelay(int delayId) {
		_itemdelay.remove(Integer.valueOf(delayId));
	}

	public boolean hasItemDelay(int delayId) {
		return _itemdelay.containsKey(Integer.valueOf(delayId));
	}

	public L1ItemDelay.ItemDelayTimer getItemDelayTimer(int delayId) {
		return (L1ItemDelay.ItemDelayTimer) _itemdelay.get(Integer.valueOf(delayId));
	}

	public void addPet(L1NpcInstance npc) {
		_petlist.put(Integer.valueOf(npc.getId()), npc);

		sendPetCtrlMenu(npc, true);
	}
	/**
	 * 移除寵物清單
	 * 
	 * @param npc
	 */
	public void removePet(L1NpcInstance npc) {
		_petlist.remove(Integer.valueOf(npc.getId()));

		sendPetCtrlMenu(npc, false);
	}
	/**
	 * 傳回寵物控制清單
	 * 
	 * @return
	 */
	public Map<Integer, L1NpcInstance> getPetList() {
		return _petlist;
	}
	/**
	 * 寵物選單控制
	 * 
	 * @param npc
	 * @param type
	 *            true:顯示 false:關閉
	 */
	private final void sendPetCtrlMenu(L1NpcInstance npc, boolean type) {
		if ((npc instanceof L1PetInstance)) {
			L1PetInstance pet = (L1PetInstance) npc;
			L1Character cha = pet.getMaster();

			if ((cha instanceof L1PcInstance)) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PetCtrlMenu(pc, pet, type));

				if (type) {
					pc.sendPackets(new S_HPMeter(pet));
				}
			}
		} else if ((npc instanceof L1SummonInstance)) {
			L1SummonInstance summon = (L1SummonInstance) npc;
			L1Character cha = summon.getMaster();

			if ((cha instanceof L1PcInstance)) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_PetCtrlMenu(pc, summon, type));

				if (type)
					pc.sendPackets(new S_HPMeter(summon));
			}
		}
	}

	private L1DollInstance _usingdoll;// 正在使用的娃娃

	public void setUsingDoll(L1DollInstance doll) {
		_usingdoll = doll;
	}

	public L1DollInstance getUsingDoll() {
		return _usingdoll;
	}

	/**
	 * 加入使用中娃娃清單
	 * 
	 * @param doll
	 */
	public void addDoll(L1DollInstance doll) {
		_dolls.put(Integer.valueOf(doll.getItemObjId()), doll);
	}

	/**
	 * 從使用中娃娃清單中移出
	 * 
	 * @param doll
	 */
	public void removeDoll(L1DollInstance doll) {
		_dolls.remove(Integer.valueOf(doll.getItemObjId()));
	}

	/**
	 * 取回使用中的娃娃資料
	 * 
	 * @param ItemObjId
	 * @return
	 */
	public L1DollInstance getDoll(int ItemObjId) {
		return (L1DollInstance) _dolls.get(Integer.valueOf(ItemObjId));
	}

	/**
	 * 取回使用中的娃娃清單
	 * 
	 * @return
	 */
	public Map<Integer, L1DollInstance> getDolls() {
		return _dolls;
	}
	//newdoll
	
	private final Map<Integer, L1DollInstance2> _dolls2 = new HashMap<Integer, L1DollInstance2>();

	/**
	 * 設置娃娃
	 * 
	 * @param doll
	 */
	public void addDoll2(final L1DollInstance2 doll) {
		_dolls2.put(doll.getItemObjId(), doll);
	}

	/**
	 * 移除娃娃
	 */
	public void removeDoll2(final L1DollInstance2 doll) {
		_dolls2.remove(doll.getItemObjId());
	}

	/**
	 * 移除娃娃
	 * 
	 * @return
	 */
	public L1DollInstance2 getDoll2(final int key) {
		return _dolls2.get(key);
	}

	/**
	 * 目前攜帶的娃娃
	 * 
	 * @return 目前攜帶的娃娃
	 */
	public Map<Integer, L1DollInstance2> getDolls2() {
		return _dolls2;
	}
	
	private L1DollInstance _power_doll = null;// 超級娃娃

	/**
	 * 設置超級娃娃
	 * 
	 * @param doll
	 */
	public void add_power_doll(final L1DollInstance doll) {
		_power_doll = doll;
	}

	/**
	 * 移除超級娃娃
	 */
	public void remove_power_doll() {
		_power_doll = null;
	}

	private L1DollInstance2 _usingdoll2;// 正在使用的娃娃

	public void setUsingDoll2(L1DollInstance2 doll) {
		_usingdoll2 = doll;
	}

	public L1DollInstance2 getUsingDoll2() {
		return _usingdoll2;
	}
	/**
	 * 目前攜帶的超級娃娃
	 * 
	 * @return 目前攜帶的超級娃娃
	 */
	public L1DollInstance get_power_doll() {
		return _power_doll;
	}
	public void addFollower(L1FollowerInstance follower) {
		_followerlist.put(Integer.valueOf(follower.getId()), follower);
	}

	public void removeFollower(L1FollowerInstance follower) {
		_followerlist.remove(Integer.valueOf(follower.getId()));
	}

	public Map<Integer, L1FollowerInstance> getFollowerList() {
		return _followerlist;
	}

	public void setPoison(L1Poison poison) {
		_poison = poison;
	}

	public void curePoison() {
		if (_poison == null) {
			return;
		}
		_poison.cure();
	}

	public L1Poison getPoison() {
		return _poison;
	}

	public void setPoisonEffect(int effectId) {
		broadcastPacketX8(new S_Poison(getId(), effectId));
	}

	// 發送中毒圖示
	public void setPoisonIcon(int type, int time) {
		
		broadcastPacketX8(new S_SkillIconPoison(type, time));
	}

	public int getZoneType() {
		if (getMap().isSafetyZone(getLocation()))
			return 1;
		if (getMap().isCombatZone(getLocation())) {
			return -1;
		}
		return 0;
	}

	public boolean isSafetyZone() {
		if (getMap().isSafetyZone(getLocation())) {
			return true;
		}
		return false;
	}

	public boolean isCombatZone() {
		if (getMap().isCombatZone(getLocation())) {
			return true;
		}
		return false;
	}

	public boolean isNoneZone() {
		return getZoneType() == 0;
	}

	public long getExp() {
		return _exp;
	}

	public void setExp(long exp) {
		_exp = exp;
	}

	public boolean knownsObject(L1Object obj) {
		return _knownObjects.contains(obj);
	}

	public List<L1Object> getKnownObjects() {
		return _knownObjects;
	}

	public List<L1PcInstance> getKnownPlayers() {
		return _knownPlayer;
	}

	public void addKnownObject(L1Object obj) {
		if (!_knownObjects.contains(obj)) {
			_knownObjects.add(obj);
			if ((obj instanceof L1PcInstance)) {
				_knownPlayer.add((L1PcInstance) obj);
			}
		}
	}

	public void removeKnownObject(L1Object obj) {
		_knownObjects.remove(obj);
		if ((obj instanceof L1PcInstance))
			_knownPlayer.remove(obj);
	}

	public void removeAllKnownObjects() {
		_knownObjects.clear();
		_knownPlayer.clear();
	}

	public String getName() {
		return _name;
	}

	public void setName(String s) {
		_name = s;
	}

	public synchronized int getLevel() {
		return _level;
	}

	public synchronized void setLevel(int level) {
		_level = level;
	}

	public int getMaxHp() {
    	// 7.6 空身體質額外獎勵
		int maxhp = 0;
        if (this instanceof L1PcInstance) {
        	L1PcInstance pc = (L1PcInstance) this;
        	if (pc.getBaseCon() >= 25) {
        		maxhp += 50;
        	}
            if (pc.getBaseCon() >= 35) {
        		maxhp += 100;
        	}
            if (pc.getBaseCon() >= 45) {
        		maxhp += 150;
        	}
        	if (pc.isWizard() && pc.getReincarnationSkill()[2] > 0// 法師天賦技能血之聖光
                	|| pc.isIllusionist() && pc.getReincarnationSkill()[2] > 0 // 幻術天賦技能血之聖光
        			) {
        		maxhp += pc.getReincarnationSkill()[2] * 400;
        	}
    		return this._maxHp + maxhp;
        }
		return _maxHp;
	}

	public void setMaxHp(int hp) {
		_trueMaxHp = hp;
		_maxHp = RangeInt.ensure(_trueMaxHp, 1, 500000);
		_currentHp = Math.min(_currentHp, _maxHp);
	}

	public void addMaxHp(int i) {
		setMaxHp(_trueMaxHp + i);
	}

	public short getMaxMp() {
    	// 7.6 空身精神額外獎勵
		int maxmp = 0;
        if (this instanceof L1PcInstance) {
        	L1PcInstance pc = (L1PcInstance) this;
        	if (pc.getBaseWis() >= 25) {
        		maxmp += 50;
        	}
            if (pc.getBaseWis() >= 35) {
        		maxmp += 100;
        	}
            if (pc.getBaseWis() >= 45) {
        		maxmp += 150;
        	}
    		return (short) (this._maxMp + maxmp);
        }
		return _maxMp;
	}

	public void setMaxMp(int mp) {
		_trueMaxMp = mp;
		_maxMp = ((short) RangeInt.ensure(_trueMaxMp, 0, 32767));
		_currentMp = Math.min(_currentMp, _maxMp);
	}

	public void addMaxMp(int i) {
		setMaxMp(_trueMaxMp + i);
	}

	public int getAc() {
		int ac = _ac;
		if ((this instanceof L1PcInstance)) {
			L1PcInstance pc = (L1PcInstance) this;
			switch (pc.guardianEncounter()) {
			case 0:
				ac -= 2;
				break;
			case 1:
				ac -= 4;
				break;
			case 2:
				ac -= 6;
				break;
			}
		}

		//return RangeInt.ensure(ac, -211, 44);
		return RangeInt.ensure(ac, -999, 44);
	}

	public void setAc(int i) {
		_trueAc = i;
		//_ac = RangeInt.ensure(i, -211, 44);
		_ac = RangeInt.ensure(i, -999, 44);
	}

	public void addAc(int i) {
		setAc(_trueAc + i);
	}
	private short _trueS = 0; // ● 本當ＳＴＲ
	private short _add_magic_plus = 0;
	private short _add_damage_plus = 0;
	private short _add_range_plus = 0;
	
	public short getStr() {
		return _str;
	}

	public void setStr(final int i) {
		this._trueStr = (short) i;
		this._str = (short) RangeInt.ensure(i, 1, 254);
	}

	public void setS(final int i) {
		this._trueS = (short) i;
		this._trueS = (short) RangeInt.ensure(i, 1, 254);
	}


	public void addStr(int i) {
		setStr(_trueStr + i);
	}
	/**
	 * 增加(減少)魔攻
	 * 
	 * @param i
	 */
	public void add_magic_plus(final int i) {
		this.setS(this._add_magic_plus + i);
	}

	/**
	 * 增加(減少)攻擊
	 * 
	 * @param i
	 */
	public void add_damage_plus(final int i) {
		this.setS(this._add_damage_plus + i);
	}

	/**
	 * 增加(減少)額外攻擊
	 * 
	 * @param i
	 */
	public void add_range_plus(final int i) {
		this.setS(this._add_range_plus + i);
	}

	public short getCon() {
		return _con;
	}

	public void setCon(int i) {
		_trueCon = ((short) i);
		_con = ((short) RangeInt.ensure(i, 1, 254));
	}

	public void addCon(int i) {
		setCon(_trueCon + i);
	}

	public short getDex() {
		return _dex;
	}

	public void setDex(int i) {
		_trueDex = ((short) i);
		_dex = ((short) RangeInt.ensure(i, 1, 254));
	}

	public void addDex(int i) {
		setDex(_trueDex + i);
	}

	public short getCha() {
		return _cha;
	}

	public void setCha(int i) {
		_trueCha = ((short) i);
		_cha = ((short) RangeInt.ensure(i, 1, 254));
		
	}

	public void addCha(int i) {
		setCha(_trueCha + i);
	}

	public short getInt() {
		return _int;
	}

	public void setInt(int i) {
		_trueInt = ((short) i);
		_int = ((short) RangeInt.ensure(i, 1, 254));
	}

	public void addInt(int i) {
		setInt(_trueInt + i);
	}

	public short getWis() {
		return _wis;
	}

	public void setWis(int i) {
		_trueWis = ((short) i);
		_wis = ((short) RangeInt.ensure(i, 1, 254));
	}

	public void addWis(int i) {
		setWis(_trueWis + i);
	}

	public int getWind() {
		return _wind;
	}

	public void addWind(int i) {
		_trueWind += i;
		if (_trueWind >= 150)
			_wind = 150;
		else if (_trueWind <= -150)
			_wind = -150;
		else
			_wind = _trueWind;
	}

	public int getWater() {
		return _water;
	}

	public void addWater(int i) {
		_trueWater += i;
		if (_trueWater >= 150)
			_water = 150;
		else if (_trueWater <= -150)
			_water = -150;
		else
			_water = _trueWater;
	}

	public int getFire() {
		return _fire;
	}

	public void addFire(int i) {
		_trueFire += i;
		if (_trueFire >= 150)
			_fire = 150;
		else if (_trueFire <= -150)
			_fire = -150;
		else
			_fire = _trueFire;
	}

	public int getEarth() {
		return _earth;
	}

	public void addEarth(int i) {
		_trueEarth += i;
		if (_trueEarth >= 150) {
			_earth = 150;
		} else if (_trueEarth <= -150) {
			_earth = -150;
		} else
			_earth = _trueEarth;
	}

	public int getAddAttrKind() {
		return _addAttrKind;
	}

	public void setAddAttrKind(int i) {
		_addAttrKind = i;
	}

	// public int getRegistStun() {
	// return _registStun;
	// }
	//
	// public void addRegistStun(int i) {
	// _trueRegistStun += i;
	// if (_trueRegistStun > 127)
	// _registStun = 127;
	// else if (_trueRegistStun < -128)
	// _registStun = -128;
	// else
	// _registStun = _trueRegistStun;
	// }
	//
	// public int getRegistStone() {
	// return _registStone;
	// }
	//
	// public void addRegistStone(int i) {
	// _trueRegistStone += i;
	// if (_trueRegistStone > 127)
	// _registStone = 127;
	// else if (_trueRegistStone < -128)
	// _registStone = -128;
	// else
	// _registStone = _trueRegistStone;
	// }
	//
	// public int getRegistSleep() {
	// return _registSleep;
	// }
	//
	// public void addRegistSleep(int i) {
	// _trueRegistSleep += i;
	// if (_trueRegistSleep > 127)
	// _registSleep = 127;
	// else if (_trueRegistSleep < -128)
	// _registSleep = -128;
	// else
	// _registSleep = _trueRegistSleep;
	// }
	//
	// public int getRegistFreeze() {
	// return _registFreeze;
	// }
	//
	// public void add_regist_freeze(int i) {
	// _trueRegistFreeze += i;
	// if (_trueRegistFreeze > 127)
	// _registFreeze = 127;
	// else if (_trueRegistFreeze < -128)
	// _registFreeze = -128;
	// else
	// _registFreeze = _trueRegistFreeze;
	// }
	//
	// public int getRegistSustain() {
	// return _registSustain;
	// }
	//
	// public void addRegistSustain(int i) {
	// _trueRegistSustain += i;
	// if (_trueRegistSustain > 127)
	// _registSustain = 127;
	// else if (_trueRegistSustain < -128)
	// _registSustain = -128;
	// else
	// _registSustain = _trueRegistSustain;
	// }
	//
	// public int getRegistBlind() {
	// return _registBlind;
	// }
	//
	// public void addRegistBlind(int i) {
	// _trueRegistBlind += i;
	// if (_trueRegistBlind > 127)
	// _registBlind = 127;
	// else if (_trueRegistBlind < -128)
	// _registBlind = -128;
	// else
	// _registBlind = _trueRegistBlind;
	// }

	/**
	 * 技術耐性
	 * @return
	 */
	public int getRegistTechnology() {
		return _registTechnology;
	}

	/**
	 * 技術耐性
	 * @param i
	 */
	public void addRegistTechnology(int i) {
		_trueRegistTechnology += i;
		if (_trueRegistTechnology > 127) {
			_registTechnology = 127;
		} else if (_trueRegistTechnology < -128) {
			_registTechnology = -128;
		} else {
			_registTechnology = _trueRegistTechnology;
		}
        if (this instanceof L1PcInstance) {
        	L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_ACTION_UI(pc, S_ACTION_UI.RESIST, 0x0a, 1));
        }
	}

	/**
	 * 精靈耐性
	 * @return
	 */
	public int getRegistElf() {
		return _registElf;
	}

	/**
	 * 精靈耐性
	 * @param i
	 */
	public void addRegistElf(int i) {
		_trueRegistElf += i;
		if (_trueRegistElf > 127) {
			_registElf = 127;
		} else if (_trueRegistElf < -128) {
			_registElf = -128;
		} else {
			_registElf = _trueRegistElf;
		}
        if (this instanceof L1PcInstance) {
        	L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_ACTION_UI(pc, S_ACTION_UI.RESIST, 0x0a, 2));
        }
	}

	/**
	 * 龍屬耐性
	 * @return
	 */
	public int getRegistDragon() {
		return _registDragon;
	}

	/**
	 * 龍屬耐性
	 * @param i
	 */
	public void addRegistDragon(int i) {
		_trueRegistDragon += i;
		if (_trueRegistDragon > 127) {
			_registDragon = 127;
		} else if (_trueRegistDragon < -128) {
			_registDragon = -128;
		} else {
			_registDragon = _trueRegistDragon;
		}
        if (this instanceof L1PcInstance) {
        	L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_ACTION_UI(pc, S_ACTION_UI.RESIST, 0x0a, 3));
        }
	}

	/**
	 * 恐怖耐性
	 * @return
	 */
	public int getRegistHorror() {
		return _registHorror;
	}

	/**
	 * 恐怖耐性
	 * @param i
	 */
	public void addRegistHorror(int i) {
		_trueRegistHorror += i;
		if (_trueRegistHorror > 127) {
			_registHorror = 127;
		} else if (_trueRegistHorror < -128) {
			_registHorror = -128;
		} else {
			_registHorror = _trueRegistHorror;
		}
        if (this instanceof L1PcInstance) {
        	L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_ACTION_UI(pc, S_ACTION_UI.RESIST, 0x0a, 4));
        }
	}

	/**
	 * 全部四大耐性
	 * @return
	 */
	public int getRegistAll() {
		return _registAll;
	}

	/**
	 * 全部四大耐性
	 * @param i
	 */
	public void addRegistAll(int i) {
		_trueRegistAll += i;
		if (_trueRegistAll > 127) {
			_registAll = 127;
		} else if (_trueRegistAll < -128) {
			_registAll = -128;
		} else {
			_registAll = _trueRegistAll;
		}
        if (this instanceof L1PcInstance) {
        	L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_ACTION_UI(pc, S_ACTION_UI.RESIST, 0x0a, 5));
        }
	}

	public int getDmgup() {
		return _dmgup;
	}

	public void addDmgup(int i) {
		_trueDmgup += i;
		if (_trueDmgup >= 127)
			_dmgup = 127;
		else if (_trueDmgup <= -128)
			_dmgup = -128;
		else
			_dmgup = _trueDmgup;
	}

	public int getBowDmgup() {
		return _bowDmgup;
	}

	public void addBowDmgup(int i) {
		_trueBowDmgup += i;
		if (_trueBowDmgup >= 127)
			_bowDmgup = 127;
		else if (_trueBowDmgup <= -128)
			_bowDmgup = -128;
		else
			_bowDmgup = _trueBowDmgup;
	}

	public int getHitup() {
		return _hitup;
	}

	public void addHitup(int i) {
		_trueHitup += i;
		if (_trueHitup >= 127)
			_hitup = 127;
		else if (_trueHitup <= -128)
			_hitup = -128;
		else
			_hitup = _trueHitup;
	}

	public int getBowHitup() {
		return _bowHitup;
	}

	public void addBowHitup(int i) {
		_trueBowHitup += i;
		if (_trueBowHitup >= 127)
			_bowHitup = 127;
		else if (_trueBowHitup <= -128)
			_bowHitup = -128;
		else
			_bowHitup = _trueBowHitup;
	}

	public int getMr() {
		if (hasSkillEffect(153)) {
			return _mr >> 2;
		}
		return _mr;
	}

	public int getTrueMr() {
		return _trueMr;
	}

	public void addMr(int i) {
		_trueMr += i;
		if (_trueMr <= 0)
			_mr = 0;
		else
			_mr = _trueMr;
	}

	/**
	 * 魔攻
	 * 
	 * @return
	 */
	public int getSp() {
		return getTrueSp() + _sp;
	}

	/**
	 * 魔法等級+魔法額外點數
	 * 
	 * @return
	 */
	public int getTrueSp() {
		return getMagicLevel() + getMagicBonus();
	}

	public void addSp(int i) {
		_sp += i;
	}

	public boolean isDead() {
		return _isDead;
	}

	public void setDead(boolean flag) {
		_isDead = flag;

	}

	public int getStatus() {
		return _status;
	}

	public void setStatus(int i) {
		_status = i;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String s) {
		_title = s;
	}

	public int getLawful() {
		return _lawful;
	}

	public void setLawful(int i) {
		_lawful = i;
	}

	public int getHeading() {
		return _heading;
	}

	public void setHeading(int i) {
		_heading = i;
	}

	/**
	 * 移動加速狀態(綠水)
	 * 
	 * @param i
	 *            0:無 1:加速 2:緩速
	 */
	public int getMoveSpeed() {
		return _moveSpeed;
	}

	/**
	 * 移動加速狀態(綠水)
	 * 
	 * @param i
	 *            0:無 1:加速 2:緩速
	 */
	public void setMoveSpeed(int i) {
		_moveSpeed = i;
	}

	/**
	 * 魔法效果:勇敢藥水纇
	 * 
	 * @param objid
	 * @param mode
	 *            <br>
	 *            0:你的情緒回復到正常。(解除 )<br>
	 *            1:從身體的深處感到熱血沸騰。(第一階段勇水)<br>
	 *            3:身體內深刻的感覺到充滿了森林的活力。(精靈餅乾)<br>
	 *            4:風之疾走 / 神聖疾走 / 行走加速 / 生命之樹果實效果<br>
	 *            5:從身體的深處感到熱血沸騰。(第二階段勇水)<br>
	 *            6:引發龍之血暴發出來了。<br>
	 *            7:無<br>
	 *            8:舞躍之火？<br>
	 *            9:狂怒之風<br>
	 *            10:波濤之水1階段<br>
	 *            11:波濤之水2階段<br>
	 *            12:波濤之水3階段<br>
	 * @param time
	 */
	public int getBraveSpeed() {
		return _braveSpeed;
	}

	/**
	 * 魔法效果:勇敢藥水纇
	 * 
	 * @param objid
	 * @param mode
	 *            <br>
	 *            0:你的情緒回復到正常。(解除 )<br>
	 *            1:從身體的深處感到熱血沸騰。(第一階段勇水)<br>
	 *            3:身體內深刻的感覺到充滿了森林的活力。(精靈餅乾)<br>
	 *            4:風之疾走 / 神聖疾走 / 行走加速 / 生命之樹果實效果<br>
	 *            5:從身體的深處感到熱血沸騰。(第二階段勇水)<br>
	 *            6:引發龍之血暴發出來了。<br>
	 *            7:無<br>
	 *            8:舞躍之火？<br>
	 *            9:狂怒之風<br>
	 *            10:波濤之水1階段<br>
	 *            11:波濤之水2階段<br>
	 *            12:波濤之水3階段<br>
	 * @param time
	 */
	public void setBraveSpeed(int i) {
		_braveSpeed = i;
	}

	public int getTempCharGfx() {
		return _tempCharGfx;
	}

	public void setTempCharGfx(int i) {
		_tempCharGfx = i;
	}

	public int getGfxId() {
		return _gfxid;
	}

	public void setGfxId(int i) {
		_gfxid = i;
	}

	/**
	 * 魔法等級
	 * @return
	 */
	public int getMagicLevel() {
		return getLevel() / 4;
	}

	/**
	 * 額外魔法點數
	 * @return
	 */
	public int getMagicBonus() {
		int i = getInt();
		// if (i <= 5) {
		// return -2;
		// } else if (i <= 8) {
		// return -1;
		// } else if (i <= 11) {
		// return 0;
		// } else if (i <= 14) {
		// return 1;
		// } else if (i <= 17) {
		// return 2;
		// } else if (i <= 24) {
		// return i - 15;
		// } else if (i <= 35) {
		// return 10;
		// } else if (i <= 42) {
		// return 11;
		// } else if (i <= 49) {
		// return 12;
		// } else if (i <= 50) {
		// return 13;
		// } else {
		// return 13;
		// }
		// XXX 7.6C ADD
		if (this instanceof L1PcInstance) {
			final L1PcInstance pc = (L1PcInstance) this;
			return L1ClassFeature.calcIntMagicBonus(pc.getType(), i);

		} else { // 怪物
			return L1ClassFeature.calcIntMagicBonusMob(i);
		}
	}

	public boolean isInvisble() {
		return (hasSkillEffect(60)) || (hasSkillEffect(97));
	}

	public void healHp(int pt) {
		setCurrentHp(getCurrentHp() + pt);
	}

	public int getKarma() {
		return _karma;
	}

	public void setKarma(int karma) {
		_karma = karma;
	}

	public void setMr(int i) {
		_trueMr = i;
		if (_trueMr <= 0)
			_mr = 0;
		else
			_mr = _trueMr;
	}

	public void turnOnOffLight() {
		int lightSize = 0;
		if ((this instanceof L1NpcInstance)) {
			L1NpcInstance npc = (L1NpcInstance) this;
			lightSize = npc.getLightSize();
		}

		for (L1ItemInstance item : getInventory().getItems()) {
			if ((item.getItem().getType2() == 0) && (item.getItem().getType() == 2)) {
				int itemlightSize = item.getItem().getLightRange();
				if ((itemlightSize != 0) && (item.isNowLighting()) && (itemlightSize > lightSize)) {
					lightSize = itemlightSize;
				}
			}

		}

		if (hasSkillEffect(2)) {
			lightSize = 14;
		}

		if ((this instanceof L1PcInstance)) {
			if (ConfigOther.LIGHT) {
				lightSize = 20;
			}
			L1PcInstance pc = (L1PcInstance) this;
			pc.sendPackets(new S_Light(pc.getId(), lightSize));
		}

		if (!isInvisble()) {
			broadcastPacketAll(new S_Light(getId(), lightSize));
		}

		setOwnLightSize(lightSize);
		setChaLightSize(lightSize);
	}

	public int getChaLightSize() {
		if (isInvisble()) {
			return 0;
		}
		if (ConfigOther.LIGHT) {
			return 14;
		}
		return _chaLightSize;
	}

	public void setChaLightSize(int i) {
		_chaLightSize = i;
	}

	public int getOwnLightSize() {
		if (isInvisble()) {
			return 0;
		}
		if (ConfigOther.LIGHT) {
			return 14;
		}
		return _ownLightSize;
	}

	public void setOwnLightSize(int i) {
		_ownLightSize = i;
	}

	/**
	 * 傳回目前目標objid
	 * 
	 * @return
	 */
	public int get_tmp_targetid() {
		return _tmp_targetid;
	}

	/**
	 * 暫存目前目標objid
	 * 
	 * @param targetid
	 */
	public void set_tmp_targetid(int targetid) {
		_tmp_targetid = targetid;
	}

	public int get_tmp_mr() {
		return _tmp_mr;
	}

	public void set_tmp_mr(int tmp) {
		_tmp_mr = tmp;
	}

	public int get_dodge() {
		return _dodge_up;
	}

	public void add_dodge(int i) {
		_dodge_up += i;
		if (_dodge_up > 50) {
			_dodge_up = 50;
		} else if (_dodge_up < 0)
			_dodge_up = 0;
	}

	public int get_dodge_down() {
		return _dodge_down;
	}

	public void add_dodge_down(int i) {
		_dodge_down += i;
		if (_dodge_down > 50) {
			_dodge_down = 50;
		} else if (_dodge_down < 0)
			_dodge_down = 0;
	}

	public void set_decay_potion(boolean b) {
		_decay_potion = b;
	}

	public boolean is_decay_potion() {
		return _decay_potion;
	}

	public String getViewName() {
		return _name;
	}

	public int getInnRoomNumber() {
		return _innRoomNumber;
	}

	public void setInnRoomNumber(int i) {
		_innRoomNumber = i;
	}

	public boolean checkRoomOrHall() {
		return _isHall;
	}

	public void setHall(boolean i) {
		_isHall = i;
	}

	private ArrayList<L1EffectInstance> _TrueTargetEffectList = new ArrayList<L1EffectInstance>();

	/**
	 * 加入精準目標特效清單
	 * 
	 * @param effect
	 */
	public void add_TrueTargetEffect(L1EffectInstance effect) {
		_TrueTargetEffectList.add(effect);
	}

	/**
	 * 傳回精準目標特效清單
	 * 
	 * @return
	 */
	public ArrayList<L1EffectInstance> get_TrueTargetEffectList() {
		return _TrueTargetEffectList;
	}

	// // 恐怖耐性
	// private int _registFear = 0;
	//
	// private int _trueRegistFear = 0;
	//
	// /**
	// * 恐怖耐性
	// * @return
	// */
	// public int getRegistFear() {
	// return _registFear;
	// }
	//
	// /**
	// * 恐怖耐性
	// * @param i
	// */
	// public void addRegistFear(final int i) {
	// _trueRegistFear += i;
	// if (_trueRegistFear > 127) {
	// _registFear = 127;
	// } else if (_trueRegistFear < -128) {
	// _registFear = -128;
	// } else {
	// _registFear = _trueRegistFear;
	// }
	// }

	private static BufferedWriter out;

	public static void writeReceivelog(L1Character player1, L1Character player2, String s, double dmg, int pcHp) {
		try {
			File DeleteLog = new File("./log/Receive.txt");
			if (DeleteLog.createNewFile()) {
				out = new BufferedWriter(new FileWriter("./log/Receive.txt", false));
				out.write("※受攻擊紀錄※" + "\r\n");
				out.close();
			}
			String p1name = "";
			Short p1map = 0;
			String p2name = "";
			if (player1 != null) {
				p1name = player1.getName();
				p1map = player1.getMapId();
			}
			if (player2 != null) {
				p2name = player2.getName();
			}
			out = new BufferedWriter(new FileWriter("./log/Receive.txt", true));
			out.write("\r\n");// 每次填寫資料都控一行
			out.write("攻擊者: " + p1name + " 被攻擊者: " + p2name + " 血量: " + pcHp + " 傷害值: " + dmg + " 地圖: " + p1map
					+ " 時間: " + new Timestamp(System.currentTimeMillis()) + "(" + s + ")" + "\r\n");
			out.close();
		} catch (IOException e) {
			System.out.println("以下是錯誤訊息: " + e.getMessage());
		}
	}

}
