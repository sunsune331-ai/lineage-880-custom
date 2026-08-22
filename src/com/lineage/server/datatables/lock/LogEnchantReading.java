package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.LogEnchantTable;
import com.lineage.server.datatables.storage.LogEnchantStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public class LogEnchantReading {
	private final Lock _lock;
	private final LogEnchantStorage _storage;
	private static LogEnchantReading _instance;

	private LogEnchantReading() {
		_lock = new ReentrantLock(true);
		_storage = new LogEnchantTable();
	}

	public static LogEnchantReading get() {
		if (_instance == null) {
			_instance = new LogEnchantReading();
		}
		return _instance;
	}

	public void failureEnchant(L1PcInstance pc, L1ItemInstance item) {
		_lock.lock();
		try {
			_storage.failureEnchant(pc, item);
		} finally {
			_lock.unlock();
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.lock.LogEnchantReading JD-Core Version: 0.6.2
 */