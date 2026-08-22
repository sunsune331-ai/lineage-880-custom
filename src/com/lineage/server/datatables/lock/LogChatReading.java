package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.LogChatTable;
import com.lineage.server.datatables.storage.LogChatStorage;
import com.lineage.server.model.Instance.L1PcInstance;

public class LogChatReading {
	private final Lock _lock;
	private final LogChatStorage _storage;
	private static LogChatReading _instance;

	private LogChatReading() {
		_lock = new ReentrantLock(true);
		_storage = new LogChatTable();
	}

	public static LogChatReading get() {
		if (_instance == null) {
			_instance = new LogChatReading();
		}
		return _instance;
	}

	public void isTarget(L1PcInstance pc, L1PcInstance target, String text, int type) {
		_lock.lock();
		try {
			_storage.isTarget(pc, target, text, type);
		} finally {
			_lock.unlock();
		}
	}

	public void noTarget(L1PcInstance pc, String text, int type) {
		_lock.lock();
		try {
			_storage.noTarget(pc, text, type);
		} finally {
			_lock.unlock();
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.lock.LogChatReading JD-Core Version: 0.6.2
 */