package com.add.BigHot;

public class BigHotblingTimeList {

	boolean _isStart = false;

	boolean _isWaiting = false;

	boolean _isBuy = false;

	private int _BigHotId = 0;

	private String _BigHotId1 = null;

	private int _yuanbao = 0;

	private int _count1 = 0;

	private int _count2 = 0;

	private int _count3 = 0;

	private int _count4 = 0;

	private int _bigmoney1 = 0;

	private int _bigmoney2 = 0;

	private int _bigmoney3 = 0;

	private static BigHotblingTimeList _instance;

	public static BigHotblingTimeList BigHot() {
		if (_instance == null) {
			_instance = new BigHotblingTimeList();
		}
		return _instance;
	}

	public void clear() {
		_isStart = false;

		_isBuy = false;

		_isWaiting = false;

		_BigHotId = 0;

		_BigHotId1 = null;

		_yuanbao = 0;

		_count1 = 0;
		_count2 = 0;
		_count3 = 0;
		_count4 = 0;

		_bigmoney1 = 0;
		_bigmoney2 = 0;
		_bigmoney3 = 0;
	}

	public boolean get_isStart() {
		return _isStart;
	}

	public void set_isStart(final boolean b) {
		_isStart = b;
	}

	public boolean get_isBuy() {
		return _isBuy;
	}

	public void set_isBuy(final boolean b) {
		_isBuy = b;
	}

	public boolean get_isWaiting() {
		return _isWaiting;
	}

	public void set_isWaiting(final boolean b) {
		_isWaiting = b;
	}

	public int get_BigHotId() {
		return _BigHotId;
	}

	public void set_BigHotId(final int i) {
		_BigHotId = i;
	}

	public String get_BigHotId1() {
		return _BigHotId1;
	}

	public void set_BigHotId1(final String i) {
		_BigHotId1 = i;
	}

	public int get_yuanbao() {
		return _yuanbao;
	}

	public void add_yuanbao(final int i) {
		_yuanbao += i;
	}

	public int get_count1() {
		return _count1;
	}

	public void add_count1(final int i) {
		_count1 += i;
	}

	public int get_count2() {
		return _count2;
	}

	public void add_count2(final int i) {
		_count2 += i;
	}

	public int get_count3() {
		return _count3;
	}

	public void add_count3(final int i) {
		_count3 += i;
	}

	public int get_count4() {
		return _count4;
	}

	public void add_count4(final int i) {
		_count4 += i;
	}

	public int get_bigmoney1() {
		return _bigmoney1;
	}

	public int get_bigmoney2() {
		return _bigmoney2;
	}

	public int get_bigmoney3() {
		return _bigmoney3;
	}

	public void computationBigHot() {
		final int AllMoney = _yuanbao - _count4 * 10;

		_bigmoney1 = (AllMoney * 7 / 10);
		_bigmoney2 = (AllMoney * 2 / 10);
		_bigmoney3 = (AllMoney / 10);
	}
}
