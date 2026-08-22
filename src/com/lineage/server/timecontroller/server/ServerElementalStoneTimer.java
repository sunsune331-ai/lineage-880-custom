package com.lineage.server.timecontroller.server;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1GroundInventory;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;

public class ServerElementalStoneTimer extends TimerTask {
	private static final Log _log = LogFactory.getLog(ServerElementalStoneTimer.class);
	private ScheduledFuture<?> _timer;
	private static final short ELVEN_FOREST_MAPID = 4;
	private static final int MAX_COUNT = ConfigAlt.ELEMENTAL_STONE_AMOUNT;
	private static final int INTERVAL = 3;
	private static final int SLEEP_TIME = 30;
	private static final int FIRST_X = 32911;
	private static final int FIRST_Y = 32210;
	private static final int LAST_X = 33141;
	private static final int LAST_Y = 32500;
	private static final int ELEMENTAL_STONE_ID = 40515;
	private ArrayList<L1GroundInventory> _itemList = new ArrayList(MAX_COUNT);

	private final L1Map _map = L1WorldMap.get().getMap((short) 4);

	private Random _random = new Random();

	private final L1Object _dummy = new L1Object();

	public void start() {
		int timeMillis = 30000;
		_timer = GeneralThreadPool.get().scheduleAtFixedRate(this, 30000L, 30000L);
	}

	private boolean canPut(L1Location loc) {
		_dummy.setMap(loc.getMap());
		_dummy.setX(loc.getX());
		_dummy.setY(loc.getY());

		if (World.get().getVisiblePlayer(_dummy).size() > 0) {
			return false;
		}
		return true;
	}

	private Point nextPoint() {
		int newX = _random.nextInt(230) + 32911;
		int newY = _random.nextInt(290) + 32210;

		return new Point(newX, newY);
	}

	private void removeItemsPickedUp() {
		for (int i = 0; i < _itemList.size(); i++) {
			L1GroundInventory gInventory = (L1GroundInventory) _itemList.get(i);
			if (!gInventory.checkItem(40515)) {
				_itemList.remove(i);
				i--;
			}
		}
	}

	private void putElementalStone(L1Location loc) {
		L1GroundInventory gInventory = World.get().getInventory(loc);

		L1ItemInstance item = ItemTable.get().createItem(40515);
		item.setEnchantLevel(0);
		item.setCount(1L);
		gInventory.storeItem(item);
		_itemList.add(gInventory);
	}

	public void run() {
		try {
			removeItemsPickedUp();

			while (_itemList.size() < MAX_COUNT) {
				L1Location loc = new L1Location(nextPoint(), _map);

				if (canPut(loc)) {
					putElementalStone(loc);

					Thread.sleep(3000L);
				}
			}
		} catch (Throwable e) {
			_log.error("元素石生成時間軸異常重啟", e);
			GeneralThreadPool.get().cancel(_timer, false);
			ServerElementalStoneTimer elementalStoneTimer = new ServerElementalStoneTimer();
			elementalStoneTimer.start();
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.timecontroller.server.ServerElementalStoneTimer JD-Core
 * Version: 0.6.2
 */