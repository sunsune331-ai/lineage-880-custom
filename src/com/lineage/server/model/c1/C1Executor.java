package com.lineage.server.model.c1;

import com.lineage.server.model.Instance.L1PcInstance;

public abstract class C1Executor {
	public abstract void set_power(int paramInt1, int paramInt2, int paramInt3);

	public abstract void set_c1(L1PcInstance paramL1PcInstance);

	public abstract void remove_c1(L1PcInstance paramL1PcInstance);
}