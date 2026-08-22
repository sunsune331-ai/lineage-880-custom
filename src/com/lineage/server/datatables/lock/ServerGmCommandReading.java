package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.ServerGmCommandTable;
import com.lineage.server.datatables.storage.ServerGmCommandStorage;
import com.lineage.server.model.Instance.L1PcInstance;

public class ServerGmCommandReading {
	private final Lock _lock;
	private final ServerGmCommandStorage _storage;
	private static ServerGmCommandReading _instance;

	private ServerGmCommandReading() {
		_lock = new ReentrantLock(true);
		_storage = new ServerGmCommandTable();
	}

	public static ServerGmCommandReading get() {
		if (_instance == null) {
			_instance = new ServerGmCommandReading();
		}
		return _instance;
	}

	public void create(L1PcInstance pc, String cmd) {
		_lock.lock();
		try {
			_storage.create(pc, cmd);
		} finally {
			_lock.unlock();
		}
	}
	public void createTradeControl(int objId, String pcName) {
		_lock.lock();
		try {
			_storage.createTradeControl(objId, pcName);
		} finally {
			_lock.unlock();
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.lock.ServerGmCommandReading JD-Core Version:
 * 0.6.2
 */