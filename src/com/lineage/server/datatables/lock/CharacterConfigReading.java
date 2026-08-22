package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.CharacterConfigTable;
import com.lineage.server.datatables.storage.CharacterConfigStorage;
import com.lineage.server.templates.L1Config;

public class CharacterConfigReading {
	private final Lock _lock;
	private final CharacterConfigStorage _storage;
	private static CharacterConfigReading _instance;

	private CharacterConfigReading() {
		_lock = new ReentrantLock(true);
		_storage = new CharacterConfigTable();
	}

	public static CharacterConfigReading get() {
		if (_instance == null) {
			_instance = new CharacterConfigReading();
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

	public L1Config get(int objectId) {
		_lock.lock();
		L1Config tmp;
		try {
			tmp = _storage.get(objectId);
		} finally {
			_lock.unlock();
		}
		return tmp;
	}

	public void storeCharacterConfig(int objectId, int length, byte[] data) {
		_lock.lock();
		try {
			_storage.storeCharacterConfig(objectId, length, data);
		} finally {
			_lock.unlock();
		}
	}

	public void updateCharacterConfig(int objectId, int length, byte[] data) {
		_lock.lock();
		try {
			_storage.updateCharacterConfig(objectId, length, data);
		} finally {
			_lock.unlock();
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.lock.CharacterConfigReading JD-Core Version:
 * 0.6.2
 */