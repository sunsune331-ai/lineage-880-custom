package com.add.BigHot;

public abstract interface BigHotblingStorage {

	public abstract void create(int id, String number, int totalPrice, int money1, int count, int money2, int count1,
			int money3, int count2, int count3);

	public abstract void load();

	public abstract L1BigHotbling[] getBigHotblingList();

	public abstract L1BigHotbling getBigHotbling(int id);
}