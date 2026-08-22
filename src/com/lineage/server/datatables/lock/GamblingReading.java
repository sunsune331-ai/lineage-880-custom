package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.GamblingTable;
import com.lineage.server.datatables.storage.GamblingStorage;
import com.lineage.server.templates.L1Gambling;

public class GamblingReading {
	private final Lock _lock;
	private final GamblingStorage _storage;
	private static GamblingReading _instance;

	private GamblingReading() {
		_lock = new ReentrantLock(true);
		_storage = new GamblingTable();
	}

	public static GamblingReading get() {
		if (_instance == null) {
			_instance = new GamblingReading();
		}
		return _instance;
	}

	public void load() {
		_lock.lock();
		try {
			_storage.load();
		} finally {
			_lock.unlock();
		}
	}

	public L1Gambling getGambling(String key) {
		_lock.lock();
		L1Gambling tmp;
		try {
			tmp = _storage.getGambling(key);
		} finally {
			_lock.unlock();
		}
		return tmp;
	}

	public L1Gambling getGambling(int key) {
		_lock.lock();
		L1Gambling tmp;
		try {
			tmp = _storage.getGambling(key);
		} finally {
			_lock.unlock();
		}
		return tmp;
	}

	public void add(L1Gambling gambling) {
		_lock.lock();
		try {
			_storage.add(gambling);
		} finally {
			_lock.unlock();
		}
	}

	public void updateGambling(int id, int outcount) {
		_lock.lock();
		try {
			_storage.updateGambling(id, outcount);
		} finally {
			_lock.unlock();
		}
	}

	public int[] winCount(int npcid) {
		_lock.lock();
		int[] tmp;
		try {
			tmp = _storage.winCount(npcid);
		} finally {
			_lock.unlock();
		}
		return tmp;
	}

	public int maxId() {
		_lock.lock();
		int tmp;
		try {
			tmp = _storage.maxId();
		} finally {
			_lock.unlock();
		}
		return tmp;
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.lock.GamblingReading JD-Core Version: 0.6.2
 */