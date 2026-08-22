package com.add.BigHot;

import java.util.concurrent.locks.ReentrantLock;

public class BigHotblingLock {
	private static BigHotblingLock _instance;
	private final BigHotblingStorage _BigHotSt;
	private final ReentrantLock _lock;

	public static BigHotblingLock create() {
		if (_instance == null) {
			_instance = new BigHotblingLock();
		}
		return _instance;
	}

	public BigHotblingLock() {
		_BigHotSt = new MySqlBigHotblingStorage();
		_lock = new ReentrantLock(true);
	}

	public void load() {
		_BigHotSt.load();
	}

	public void create(final int id, final String number, final int totalPrice, final int money1, final int count,
			final int money2, final int count1, final int money3, final int count2, final int count3) {
		_lock.lock();
		try {
			_BigHotSt.create(id, number, totalPrice, money1, count, money2, count1, money3, count2, count3);
		} finally {
			_lock.unlock();
		}
	}

	public L1BigHotbling[] getBigHotblingList() {
		return _BigHotSt.getBigHotblingList();
	}

	public L1BigHotbling getBigHotbling(final int id) {
		return _BigHotSt.getBigHotbling(id);
	}
}
