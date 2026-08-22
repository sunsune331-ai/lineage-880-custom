package com.lineage.server.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.ActionCodes;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1BowInstance;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.gametime.L1GameTime;
import com.lineage.server.model.gametime.L1GameTimeAdapter;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1SpawnTime;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.npc.NpcSpawnBossTimer;
import com.lineage.server.types.Point;
import com.lineage.server.utils.collections.UListFactory;
import com.lineage.server.world.World;

/**
 * 召喚控制項
 * @author Admin
 */
public class L1Spawn extends L1GameTimeAdapter {  //src053

	private static final Log _log = LogFactory.getLog(L1Spawn.class);

	private final L1Npc _template; // NPC模板

	private int _id; // 管理ID

	private String _location; // 地點STR

	private int _maximumCount; // 最大召喚數量

	private int _npcid; // NPCID

	private int _groupId; // 隊伍ID

	private int _locx; // 座標點X軸

	private int _locy; // 座標點Y軸

	private int _tmplocx; // 本次召喚X座標

	private int _tmplocy; // 本次召喚Y座標

	private short _tmpmapid; // 本次召喚地圖ID

	private int _randomx; // 隨機X軸

	private int _randomy; // 隨機Y軸

	private int _locx1; // 座標點X軸

	private int _locy1; // 座標點Y軸

	private int _locx2; // 座標點X軸

	private int _locy2; // 座標點Y軸

	private int _heading;

	private int _minRespawnDelay;

	private int _maxRespawnDelay;

	private short _mapid; // 座標點地圖ID

	private boolean _respaenScreen; // 屏幕內有玩家是否重新召喚

	private int _movementDistance;

	private boolean _rest;

	private int _spawnType;

	private int _killTime;

	private int _delayInterval;

	private L1SpawnTime _time;

	private Calendar _nextSpawnTime = null;

	private long _spawnInterval = 0L;

	private int _existTime = 0;

	private Map<Integer, Point> _homePoint = null; // 初始化時候的座標點

	private List<L1NpcInstance> _mobs = new ArrayList<L1NpcInstance>();

	private Random _random = new Random();

	private String _name;

	private boolean _initSpawn = false;

	private boolean _spawnHomePoint;

	/**
	 * 定時召喚控制類
	 */
	private class SpawnTask implements Runnable {
		private int _spawnNumber;
		private int _objectId;
		private long _delay;

		/**
		 * 
		 * @param spawnNumber 召喚管理編號
		 * @param objectId 世界物件編號
		 * @param delay 延遲時間
		 */
		private SpawnTask(int spawnNumber, int objectId, long delay) {
			_spawnNumber = spawnNumber;
			_objectId = objectId;
			_delay = delay;
		}

		/**
		 * 啟動線程
		 */
		public void getStart() {
			GeneralThreadPool.get().schedule(this, _delay);
		}

		@Override
		public void run() {
			doSpawn(_spawnNumber, _objectId);
		}
	}

	public L1Spawn(L1Npc mobTemplate) {
		_template = mobTemplate;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public short getMapId() {
		return _mapid;
	}

	public void setMapId(short _mapid) {
		this._mapid = _mapid;
	}

	/**
	 * 是否出生於隊長周圍
	 * @return
	 */
	public boolean isRespawnScreen() {
		return _respaenScreen;
	}

	/**
	 * 設定是否出生於隊長周圍
	 * @param flag
	 */
	public void setRespawnScreen(boolean flag) {
		_respaenScreen = flag;
	}

	/**
	 * 移動距離
	 * @return
	 */
	public int getMovementDistance() {
		return _movementDistance;
	}

	/**
	 * 移動距離
	 * @param i
	 */
	public void setMovementDistance(int i) {
		_movementDistance = i;
	}

	/**
	 * 數量
	 * @return
	 */
	public int getAmount() {
		return _maximumCount;
	}

	/**
	 * 隊伍召喚編號
	 * @return
	 */
	public int getGroupId() {
		return _groupId;
	}

	/**
	 * 傳回召喚編號
	 * 
	 * @return
	 */
	public int getId() {
		return _id;
	}

	public String getLocation() {
		return _location;
	}

	public int getLocX() {
		return _locx;
	}

	public int getLocY() {
		return _locy;
	}

	public int getNpcId() {
		return _npcid;
	}

	public int getHeading() {
		return _heading;
	}

	public int getRandomx() {
		return _randomx;
	}

	public int getRandomy() {
		return _randomy;
	}

	public int getLocX1() {
		return _locx1;
	}

	public int getLocY1() {
		return _locy1;
	}

	public int getLocX2() {
		return _locx2;
	}

	public int getLocY2() {
		return _locy2;
	}

	/**
	 * 召喚延遲
	 * @return 單位:秒
	 */
	public int getMinRespawnDelay() {
		return _minRespawnDelay;
	}

	/**
	 * 召喚延遲
	 * @return 單位:秒
	 */
	public int getMaxRespawnDelay() {
		return _maxRespawnDelay;
	}

	/**
	 * 數量
	 * @param amount
	 */
	public void setAmount(int amount) {
		_maximumCount = amount;
	}

	/**
	 * 設定召喚編號
	 * @param id
	 */
	public void setId(int id) {
		_id = id;
	}

	/**
	 * 隊伍召喚編號
	 * @param i
	 */
	public void setGroupId(int i) {
		_groupId = i;
	}

	public void setLocation(String location) {
		_location = location;
	}

	public void setLocX(int locx) {
		_locx = locx;
	}

	public void setLocY(int locy) {
		_locy = locy;
	}

	public void setNpcid(int npcid) {
		_npcid = npcid;
	}

	public void setHeading(int heading) {
		_heading = heading;
	}

	/**
	 * 召喚隨機範圍
	 * @param randomx
	 */
	public void setRandomx(int randomx) {
		_randomx = randomx;
	}

	/**
	 * 召喚隨機範圍
	 * @param randomy
	 */
	public void setRandomy(int randomy) {
		_randomy = randomy;
	}

	public void setLocX1(int locx1) {
		_locx1 = locx1;
	}

	public void setLocY1(int locy1) {
		_locy1 = locy1;
	}

	public void setLocX2(int locx2) {
		_locx2 = locx2;
	}

	public void setLocY2(int locy2) {
		_locy2 = locy2;
	}

	/**
	 * 召喚延遲(秒)
	 * 
	 * @param i
	 */
	public void setMinRespawnDelay(int i) {
		_minRespawnDelay = i;
	}

	/**
	 * 召喚延遲
	 * @param i 單位:秒
	 */
	public void setMaxRespawnDelay(int i) {
		_maxRespawnDelay = i;
	}

	public int getTmpLocX() {
		return _tmplocx;
	}

	public int getTmpLocY() {
		return _tmplocy;
	}

	public short getTmpMapid() {
		return _tmpmapid;
	}

	/**
	 * 是否到達出生時間
	 * 
	 * @param npcTemp
	 * @return
	 */
	private boolean isSpawnTime(L1NpcInstance npcTemp) {
		if (_nextSpawnTime != null) {
			// 取得目前時間
			Calendar cals = Calendar.getInstance();
			long nowTime = System.currentTimeMillis();
			cals.setTimeInMillis(nowTime);

			if (cals.after(_nextSpawnTime)) {// 已到達時間
				//System.out.println("抵達召喚時間");
				return true;

			} else {
				if (NpcSpawnBossTimer.MAP.get(npcTemp) == null) {// 尚未加入出生時間清單
					long spawnTime = _nextSpawnTime.getTimeInMillis();
					// 加入等候清單(5秒誤差補正)
					long spa = (spawnTime - nowTime) / 1000L + 5L;
					// 加入等候清單(5秒誤差補正)
					NpcSpawnBossTimer.MAP.put(npcTemp, Long.valueOf(spa));// 加入出生時間清單
				}
				return false;
			}
		}
		return true;
	}

	/**
	 * 下次召喚時間
	 * @return 
	 */
	public Calendar get_nextSpawnTime() {
		return _nextSpawnTime;
	}

	/**
	 * 下次召喚時間
	 * @param next_spawn_time
	 */
	public void set_nextSpawnTime(Calendar next_spawn_time) {
		_nextSpawnTime = next_spawn_time;
	}

	/**
	 * 差異時間(單位:分鐘)
	 * @param spawn_interval
	 */
	public void set_spawnInterval(long spawn_interval) {
		_spawnInterval = spawn_interval;
	}

	/**
	 * 差異時間(單位:分鐘)
	 * @param spawn_interval
	 * @return 
	 */
	public long get_spawnInterval() {
		return _spawnInterval;
	}

	/**
	 * 設定BOSS存在時間限制(分)
	 * 
	 * @param exist_time
	 */
	public void set_existTime(int exist_time) {
		_existTime = exist_time;
	}

	/**
	 * 傳回BOSS存在時間限制(分)
	 * @return
	 */
	public int get_existTime() {
		return _existTime;
	}

	private final int calcRespawnDelay() {
		int respawnDelay = _minRespawnDelay * 1000;
		if (_delayInterval > 0) {
			respawnDelay += _random.nextInt(_delayInterval) * 1000;
		}

		if (_time != null) {
			if ((_time.getWeekDays() != null) && !_time.getWeekDays().isEmpty()) {
				final Calendar cal = Calendar.getInstance();

				final int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

				cal.set(Calendar.YEAR, 1970);
				cal.set(Calendar.MONTH, 0);
				cal.set(Calendar.DATE, 1);

				respawnDelay = (int) (_time.getTimeStart().getTime()
						- new Time(cal.getTimeInMillis()).getTime());
				if (respawnDelay < 0) {
					respawnDelay += 24 * 3600L * 1000L;
				}

				// 如果星期不相同
				if (!_time.getWeekDays().contains(String.valueOf(day_of_week))) {
					long diff = 0L;
					// 可用 , 同時設定
					final String[] weekDays = _time.getWeekDays().split(",");
					// 1 - 7
					for (final String str : weekDays) {
						final int value = Integer.parseInt(str) - day_of_week;
						if (value > 0) {
							diff = (value - 1) * 24 * 3600L * 1000L;
							break;
						}
					}
					// 如果以上都不是
					if (diff == 0L) {
						int week = Integer.parseInt(weekDays[0]);
						diff = (getWeekdayArea(day_of_week, week)) * 24 * 3600L * 1000L;
					}
					respawnDelay += diff;
				}

			} else {
				// 指定時間外指定時間時間足
				final L1GameTime currentTime = L1GameTimeClock.getInstance().currentTime();

				if (!_time.getTimePeriod().includes(currentTime)) {
					long diff = (_time.getTimeStart().getTime() - currentTime.toTime().getTime());
					if (diff < 0) {
						diff += 24 * 3600L * 1000L;
					}
					diff /= 6; // real time to game time
					respawnDelay = (int) diff;
				}
			}
		}
		return respawnDelay;
	}

	private int getWeekdayArea(int NowWeek, int oldWeek) {
		// 判斷取得的數值等於星期幾
		switch (NowWeek) {
		case Calendar.MONDAY:
			switch (oldWeek) {
			case Calendar.MONDAY:
				return 0;
			case Calendar.TUESDAY:
				return 1;
			case Calendar.WEDNESDAY:
				return 2;
			case Calendar.THURSDAY:
				return 3;
			case Calendar.FRIDAY:
				return 4;
			case Calendar.SATURDAY:
				return 5;
			case Calendar.SUNDAY:
				return 6;
			}
		case Calendar.TUESDAY:
			switch (oldWeek) {
			case Calendar.MONDAY:
				return 6;
			case Calendar.TUESDAY:
				return 0;
			case Calendar.WEDNESDAY:
				return 1;
			case Calendar.THURSDAY:
				return 2;
			case Calendar.FRIDAY:
				return 3;
			case Calendar.SATURDAY:
				return 4;
			case Calendar.SUNDAY:
				return 5;
			}
		case Calendar.WEDNESDAY:
			switch (oldWeek) {
			case Calendar.MONDAY:
				return 5;
			case Calendar.TUESDAY:
				return 6;
			case Calendar.WEDNESDAY:
				return 0;
			case Calendar.THURSDAY:
				return 1;
			case Calendar.FRIDAY:
				return 2;
			case Calendar.SATURDAY:
				return 3;
			case Calendar.SUNDAY:
				return 4;
			}
		case Calendar.THURSDAY:
			switch (oldWeek) {
			case Calendar.MONDAY:
				return 4;
			case Calendar.TUESDAY:
				return 5;
			case Calendar.WEDNESDAY:
				return 6;
			case Calendar.THURSDAY:
				return 0;
			case Calendar.FRIDAY:
				return 1;
			case Calendar.SATURDAY:
				return 2;
			case Calendar.SUNDAY:
				return 3;
			}
		case Calendar.FRIDAY:
			switch (oldWeek) {
			case Calendar.MONDAY:
				return 3;
			case Calendar.TUESDAY:
				return 4;
			case Calendar.WEDNESDAY:
				return 5;
			case Calendar.THURSDAY:
				return 6;
			case Calendar.FRIDAY:
				return 0;
			case Calendar.SATURDAY:
				return 1;
			case Calendar.SUNDAY:
				return 2;
			}
		case Calendar.SATURDAY:
			switch (oldWeek) {
			case Calendar.MONDAY:
				return 2;
			case Calendar.TUESDAY:
				return 3;
			case Calendar.WEDNESDAY:
				return 4;
			case Calendar.THURSDAY:
				return 5;
			case Calendar.FRIDAY:
				return 6;
			case Calendar.SATURDAY:
				return 0;
			case Calendar.SUNDAY:
				return 1;
			}
		case Calendar.SUNDAY:
			switch (oldWeek) {
			case Calendar.MONDAY:
				return 1;
			case Calendar.TUESDAY:
				return 2;
			case Calendar.WEDNESDAY:
				return 3;
			case Calendar.THURSDAY:
				return 4;
			case Calendar.FRIDAY:
				return 5;
			case Calendar.SATURDAY:
				return 6;
			case Calendar.SUNDAY:
				return 0;
			}
		}
		return 0;
	}

	/**
	 * SpawnTask的啟動
	 * @param spawnNumber 管理編號
	 * @param objectId 世界物件編號
	 */
	public void executeSpawnTask(int spawnNumber, int objectId) {
		if (_nextSpawnTime != null) {
			doSpawn(spawnNumber, objectId);
		} else {
			SpawnTask task = new SpawnTask(spawnNumber, objectId, calcRespawnDelay());
			task.getStart();
		}
	}

	/**
	 * 執行初始化召喚
	 */
	public void init() {
		// 怪物指定時間範圍存在
		if ((_time != null) && _time.isDeleteAtEndTime()) {
			// 時間外削除指定、時間經過通知受。
			L1GameTimeClock.getInstance().addListener(this);
		}

		_delayInterval = (_maxRespawnDelay - _minRespawnDelay); // 計算延遲
		_initSpawn = true; // 正在召喚標記
		// 定點召喚
		if ((ConfigAlt.SPAWN_HOME_POINT)
				&& (ConfigAlt.SPAWN_HOME_POINT_COUNT <= getAmount())
				&& (ConfigAlt.SPAWN_HOME_POINT_DELAY >= getMinRespawnDelay())
				&& (isAreaSpawn())
		) {
			_spawnHomePoint = true;
			_homePoint = new HashMap<Integer, Point>();
		}

		int spawnNum = 0;
		while (spawnNum < _maximumCount) {
			doSpawn(++spawnNum);
		}
		_initSpawn = false;
	}

	/**
	 * 執行召喚
	 * @param spawnNumber
	 */
	protected void doSpawn(final int spawnNumber) { // 初期配置
		// 指定時間外、次spawn予約終。
		if (_time != null) {
			if ((_time.getWeekDays() != null) && !_time.getWeekDays().isEmpty()) {
				executeSpawnTask(spawnNumber, 0);
				return;
			}

			// 指定時間外指定時間時間足
			final L1GameTime currentTime = L1GameTimeClock.getInstance().currentTime();

			if (!_time.getTimePeriod().includes(currentTime)) {
				executeSpawnTask(spawnNumber, 0);
				return;
			}
		}
		this.doSpawn(spawnNumber, 0);
	}

	/**
	 * 開始怪物出生處理
	 * @param spawnNumber 召喚管理編號
	 * @param objectId 世界物件編號
	 */
	protected void doSpawn(int spawnNumber, int objectId) {
		_tmplocx = 0;
		_tmplocy = 0;
		_tmpmapid = 0;

		L1NpcInstance npcTemp = null;

		try {
			int newlocx = getLocX();
			int newlocy = getLocY();
			int tryCount = 0;

			npcTemp = NpcTable.get().newNpcInstance(_template);
			synchronized (_mobs) {
				_mobs.add(npcTemp);
			}

			if (objectId == 0) {
				npcTemp.setId(IdFactoryNpc.get().nextId());
			} else {
				npcTemp.setId(objectId); // 世界物件編號再利用
			}

			if ((getHeading() >= 0) && (getHeading() <= 7)) {
				npcTemp.setHeading(getHeading());
			} else {
				npcTemp.setHeading(5);
			}

			if (getKillTime() > 0) { // 回收
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npcTemp, getKillTime() * 1000);
				timer.begin();
			}
			
			// boss隨機出生樓層
			int npcId = npcTemp.getNpcTemplate().get_npcId();
			if ((npcId == 45488) && (getMapId() == 9)) {
				npcTemp.setMap((short) (getMapId() + _random.nextInt(2)));
			} else if ((npcId == 45601) && (getMapId() == 11)) {
				npcTemp.setMap((short) (getMapId() + _random.nextInt(3)));
			} else if ((npcId == 45649) && (getMapId() == 80)) {
				npcTemp.setMap((short) (getMapId() + _random.nextInt(3)));
			} else if ((npcId == 105040) && (getMapId() == 78)) {
				npcTemp.setMap((short) (getMapId() + _random.nextInt(5)));
			} else if ((npcId == 105070) && (getMapId() == 80)) {
				npcTemp.setMap((short) (getMapId() + _random.nextInt(3)));
			} else {
				npcTemp.setMap(getMapId());
			}

			npcTemp.setMovementDistance(getMovementDistance());
			npcTemp.setRest(isRest());

			// 設置召喚的XY座標位置
			while (tryCount <= 50) {
				// 區域召喚
				if (isAreaSpawn()) {
					Point pt = null;
					// if ((_spawnHomePoint) && ((pt = (Point) _homePoint.get(Integer.valueOf(spawnNumber))) != null)) {
					if (this._spawnHomePoint && (null != (pt = this._homePoint.get(spawnNumber)))) {
						L1Location loc = new L1Location(pt, getMapId()).randomLocation(ConfigAlt.SPAWN_HOME_POINT_RANGE, false);
						newlocx = loc.getX();
						newlocy = loc.getY();
					} else {
						int rangeX = getLocX2() - getLocX1();
						int rangeY = getLocY2() - getLocY1();
						newlocx = _random.nextInt(rangeX) + getLocX1();
						newlocy = _random.nextInt(rangeY) + getLocY1();
					}

					// 已經召喚失敗次數
					if (tryCount > 49) {
						if (_nextSpawnTime == null) {
							newlocx = this.getLocX();
							newlocy = this.getLocY();

						} else {
							// 延後5秒後重試
							final SpawnTask task = new SpawnTask(spawnNumber, npcTemp.getId(), 5000L);
							task.getStart();
							return;
						}
					}

				} else if (isRandomSpawn()) {// 範圍召喚
					newlocx = getLocX() + ((int) (Math.random() * getRandomx()) - (int) (Math.random() * getRandomx()));
					newlocy = getLocY() + ((int) (Math.random() * getRandomy()) - (int) (Math.random() * getRandomy()));

				} else {// 定點召喚
					newlocx = getLocX();
					newlocy = getLocY();
				}

				if (getSpawnType() == SPAWN_TYPE_PC_AROUND) {// 閃避PC
					L1Location loc = new L1Location(newlocx, newlocy, getMapId());
					// 13格內PC物件
					ArrayList<L1PcInstance> pcs = World.get().getVisiblePc(loc);
					if (pcs.size() > 0) {
						L1Location newloc = loc.randomLocation(20, false);
						newlocx = newloc.getX();
						newlocy = newloc.getY();
					}
				}

				npcTemp.setX(newlocx);
				npcTemp.setHomeX(newlocx);
				npcTemp.setY(newlocy);
				npcTemp.setHomeY(newlocy);

				if (_nextSpawnTime == null) {
					if (npcTemp.getMap().isInMap(npcTemp.getLocation())
							&& npcTemp.getMap().isPassable(npcTemp.getLocation(), npcTemp)) {
						if (npcTemp instanceof L1MonsterInstance) {
							// 是否與PC同屏幕召喚開關
							if (this.isRespawnScreen()) {
								break;
							}

							// 19格內PC物件
							final ArrayList<L1PcInstance> pcs = World.get().getVisiblePc(npcTemp.getLocation());
							if (pcs.size() == 0) {
								break;
							}

							// 畫面內具有PC物件 延後5秒
							final SpawnTask task = new SpawnTask(spawnNumber, npcTemp.getId(), 5000L);
							task.getStart();
							return;
						}
					}

				} else {
					// 座標可通行決定召喚位置
					if (npcTemp.getMap().isPassable(npcTemp.getLocation(), npcTemp)) {
						break;
					}
				}

				tryCount++;// 失敗次數+1
			}

			if ((npcTemp instanceof L1MonsterInstance)) {
				((L1MonsterInstance) npcTemp).initHide();
			}

			npcTemp.setSpawn(this);// 暫存出生資訊
			npcTemp.setreSpawn(true);
			npcTemp.setSpawnNumber(spawnNumber); // L1Spawn對每個NPC成員的管理編號

			if ((_initSpawn) && (_spawnHomePoint)) {
				Point pt = new Point(npcTemp.getX(), npcTemp.getY());
				_homePoint.put(Integer.valueOf(spawnNumber), pt);
			}

			if ((_nextSpawnTime != null) && (!isSpawnTime(npcTemp))) {// 尚未到達出生時間
				return;
			}

			// 地獄不掉落物品
			if ((npcTemp instanceof L1MonsterInstance)) {
				L1MonsterInstance mob = (L1MonsterInstance) npcTemp;
				if (mob.getMapId() == 666) {
					mob.set_storeDroped(true);
				}
			}

			// 招換巴風特傳出玩家到定點
			if ((npcId == 45573) && (npcTemp.getMapId() == 2)) {
				for (L1PcInstance pc : World.get().getAllPlayers()) {
					if (pc.getMapId() == 2) {
						L1Teleport.teleport(pc, 32664, 32797, (short) 2, 0, true);
					}
				}
			}

			// 招換冰人惡魔傳出玩家到定點
			if (((npcId == 46142) && (npcTemp.getMapId() == 73)) || ((npcId == 46141) && (npcTemp.getMapId() == 74))) {
				for (L1PcInstance pc : World.get().getAllPlayers()) {
					if ((pc.getMapId() >= 72) && (pc.getMapId() <= 74)) {
						L1Teleport.teleport(pc, 32840, 32833, (short) 72, pc.getHeading(), true);
					}
				}
			}

			doCrystalCave(npcId);

			World.get().storeObject(npcTemp);
			World.get().addVisibleObject(npcTemp);

			if ((npcTemp instanceof L1MonsterInstance)) {
				L1MonsterInstance mobtemp = (L1MonsterInstance) npcTemp;
				if ((!_initSpawn) && (mobtemp.getHiddenStatus() == 0)) {
					mobtemp.onNpcAI();
				}

				if (_existTime > 0) {
					mobtemp.set_spawnTime(_existTime * 60); // 存在時間(秒)
				}
			}

			if (getGroupId() != 0) {// 具有隊伍資訊
				L1MobGroupSpawn.getInstance().doSpawn(npcTemp, getGroupId(), isRespawnScreen(), _initSpawn);
			}

			// NPC設了刪除的判斷
			if (getDeleteTime() > 0L) {
				npcTemp.setNpcDeleteTime(true);
			}

			npcTemp.turnOnOffLight();
			npcTemp.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); //src053

			if (isBroadcast() && getBroadcastInfo() != null && !getBroadcastInfo().isEmpty()) {
				World.get().broadcastPacketToAll(
						new S_SystemMessage(String.format(getBroadcastInfo(), npcTemp.getName())));
			}

			if (this._time != null) {

				final String msg = this._time.getSpawnMsg();
				if (msg != null && !msg.isEmpty()) {

					final ServerBasePacket packet;
					if (msg.startsWith("$")) {

						packet = new S_ServerMessage(Integer.parseInt(msg.substring(1)), "    " + npcTemp.getNameId());

					} else {
						packet = new S_ServerMessage("\\F3" + msg);
					}

					World.get().broadcastPacketToAll(packet);
				}
			}

			_tmplocx = newlocx;
			_tmplocy = newlocy;
			_tmpmapid = npcTemp.getMapId();

			boolean setPassable = true;
			if ((npcTemp instanceof L1DollInstance)) {
				setPassable = false;
			}
			if ((npcTemp instanceof L1EffectInstance)) {
				setPassable = false;
			}
			if ((npcTemp instanceof L1FieldObjectInstance)) {
				setPassable = false;
			}
			if ((npcTemp instanceof L1FurnitureInstance)) {
				setPassable = false;
			}
			if ((npcTemp instanceof L1DoorInstance)) {
				setPassable = false;
			}
			if ((npcTemp instanceof L1BowInstance)) {
				setPassable = false;
			}
			if (setPassable) {
				L1WorldMap.get().getMap(npcTemp.getMapId()).setPassable(npcTemp.getX(), npcTemp.getY(), false, 2);
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void setRest(boolean flag) {
		_rest = flag;
	}

	public boolean isRest() {
		return _rest;
	}

	private static final int SPAWN_TYPE_PC_AROUND = 1;

	private int getSpawnType() {
		return _spawnType;
	}

	/**
	 * 召喚模式
	 * 0:無 1:閃避PC
	 * @param type
	 */
	public void setSpawnType(int type) {
		_spawnType = type;
	}
	
	private int getKillTime() {
		return _killTime;
	}

	public void setKillTime(int i) {
		_killTime = i;
	}

	/**
	 * 區域召喚
	 * @return
	 */
	private boolean isAreaSpawn() {
		return (getLocX1() != 0) && (getLocY1() != 0) && (getLocX2() != 0) && (getLocY2() != 0);
	}

	/**
	 * 範圍召喚
	 * @return
	 */
	private boolean isRandomSpawn() {
		return (getRandomx() != 0) || (getRandomy() != 0);
	}

	public L1SpawnTime getTime() {
		return _time;
	}

	public void setTime(L1SpawnTime time) {
		_time = time;
	}

	@Override
	public void onMinuteChanged(final L1GameTime time) {
		if (this._time.getWeekDays() != null
				&& !this._time.getWeekDays().isEmpty()) {
			if (this._time.getTimePeriod().includes(
					time.toRealTime(System.currentTimeMillis()))) {
				return;
			}

		} else {
			if (this._time.getTimePeriod().includes(time)) {
				return;
			}
		}

		// synchronized (this._mobs) {
		// if (this._mobs.isEmpty()) {
		// return;
		// }
		// // 指定時間外削除
		// for (final L1NpcInstance mob : this._mobs) {
		// mob.setCurrentHpDirect(0);
		// mob.setDead(true);
		// mob.setStatus(ActionCodes.ACTION_Die);
		// mob.deleteMe();
		// }
		// this._mobs.clear();
		// }
		// 三道
		List<L1NpcInstance> curMobList;
		synchronized (this._mobs) {
			curMobList = UListFactory.newArrayList(this._mobs);
		}
		if (curMobList.isEmpty()) {
			return;
		}
		// 指定時間外削除
		for (final L1NpcInstance mob : curMobList) {
			mob.setCurrentHpDirect(0);
			mob.setDead(true);
			mob.setStatus(ActionCodes.ACTION_Die);
			mob.deleteMe();
		}
		curMobList.clear();
	}

	/**
	 * 三道
	 * @param mob
	 */
	public void removeMob(final L1NpcInstance mob) {
		synchronized (this._mobs) {
			this._mobs.remove(mob);
		}
	}

	public static void doCrystalCave(int npcId) {
		int[] npcId2 = { 46143, 46144, 46145, 46146, 46147, 46148, 46149, 46150, 46151, 46152 };
		int[] doorId = { 5001, 5002, 5003, 5004, 5005, 5006, 5007, 5008, 5009, 5010 };

		for (int i = 0; i < npcId2.length; i++) {
			if (npcId == npcId2[i]) {
				closeDoorInCrystalCave(doorId[i]);
			}
		}
	}

	// private L1NpcInstance npcTemp;
	//
	// /**
	// * 傳回NpcInstance資料
	// * @return
	// */
	// public final L1NpcInstance getNpcTemp() {
	// return npcTemp;
	// }

	private long deleteTime; // 限時刪除

	/**
	 * 限時刪除
	 * @return
	 */
	public final long getDeleteTime() {
		return deleteTime;
	}

	/**
	 * 限時刪除
	 * @param deleteTime
	 */
	public final void setDeleteTime(long deleteTime) {
		this.deleteTime = deleteTime;
	}

	private static void closeDoorInCrystalCave(int doorId) {
		for (L1Object object : World.get().getObject()) {
			if (object instanceof L1DoorInstance) {
				L1DoorInstance door = (L1DoorInstance) object;
				if (door.getDoorId() == doorId) {
					door.close();
				}
			}
		}
	}

	private boolean _isBroadcast; // 是否出生公告

	/**
	 * 是否出生公告
	 * @return
	 */
	public final boolean isBroadcast() {
		return this._isBroadcast;
	}

	/**
	 * 是否出生公告
	 * @param isBroadcast
	 */
	public final void setBroadcast(final boolean isBroadcast) {
		this._isBroadcast = isBroadcast;
	}

	private String _broadcastInfo; // 出生公告內容

	/**
	 * 出生公告內容
	 * @return
	 */
	public final String getBroadcastInfo() {
		return this._broadcastInfo;
	}

	/**
	 * 出生公告內容
	 * @param broadcastInfo
	 */
	public final void setBroadcastInfo(final String broadcastInfo) {
		this._broadcastInfo = broadcastInfo;
	}
}
