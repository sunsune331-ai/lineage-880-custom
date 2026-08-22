package com.lineage.server.datatables.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.AccountTable;
import com.lineage.server.datatables.storage.AccountStorage;
import com.lineage.server.templates.L1Account;

public class AccountReading {
	private final Lock _lock;
	private final AccountStorage _storage;
	private static AccountReading _instance;

	private AccountReading() {
		_lock = new ReentrantLock(true);
		_storage = new AccountTable();
	}

	public static AccountReading get() {
		if (_instance == null) {
			_instance = new AccountReading();
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

	public boolean isAccountUT(String loginName) {
		_lock.lock();
		boolean tmp = false;
		try {
			tmp = _storage.isAccountUT(loginName);
		} finally {
			_lock.unlock();
		}
		return tmp;
	}

	public L1Account create(String loginName, String pwd, String ip, String host, String spwd) {
		_lock.lock();
		L1Account tmp = null;
		try {
			tmp = _storage.create(loginName, pwd, ip, host, spwd);
		} finally {
			_lock.unlock();
		}
		return tmp;
	}

	public boolean isAccount(String loginName) {
		_lock.lock();
		boolean tmp = false;
		try {
			tmp = _storage.isAccount(loginName);
		} finally {
			_lock.unlock();
		}
		return tmp;
	}

	public L1Account getAccount(String loginName) {
		_lock.lock();
		L1Account tmp = null;
		try {
			tmp = _storage.getAccount(loginName);
		} finally {
			_lock.unlock();
		}
		return tmp;
	}

	public void updateWarehouse(String loginName, int pwd) {
		_lock.lock();
		try {
			_storage.updateWarehouse(loginName, pwd);
		} finally {
			_lock.unlock();
		}
	}

	public void updateLastActive(L1Account account) {
		_lock.lock();
		try {
			_storage.updateLastActive(account);
		} finally {
			_lock.unlock();
		}
	}

	public void updateCharacterSlot(String loginName, int count) {
		_lock.lock();
		try {
			_storage.updateCharacterSlot(loginName, count);
		} finally {
			_lock.unlock();
		}
	}

	public void updatePwd(String loginName, String newpwd) {
		_lock.lock();
		try {
			_storage.updatePwd(loginName, newpwd);
		} finally {
			_lock.unlock();
		}
	}

	public void updateLan(String loginName, boolean islan) {
		_lock.lock();
		try {
			_storage.updateLan(loginName, islan);
		} finally {
			_lock.unlock();
		}
	}

	public void updateLan() {
		_lock.lock();
		try {
			_storage.updateLan();
		} finally {
			_lock.unlock();
		}
	}

	public void updateAccessLevel(String loginName) {
		_lock.lock();
		try {
			_storage.updateAccessLevel(loginName);
		} finally {
			_lock.unlock();
		}
	}

	/**
	 * 是否首儲
	 * 
	 */
	public void updatefp(final String loginName, final int fp) { //SRC0701
		this._lock.lock();
		try {
			this._storage.updatefp(loginName, 1);

		} finally {
			this._lock.unlock();
		}
	}

	/**
	 * 更新TAM幣點數
	 * 
	 */
	public void updatetam(final String loginName, final int tam_point) { // 成長果實系統(Tam幣)
		this._lock.lock();
		try {
			this._storage.updatetam(loginName, tam_point);

		} finally {
			this._lock.unlock();
		}
	}

}
