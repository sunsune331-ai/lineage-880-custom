package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public abstract interface LogEnchantStorage {
	public abstract void failureEnchant(L1PcInstance paramL1PcInstance, L1ItemInstance paramL1ItemInstance);
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.storage.LogEnchantStorage JD-Core Version:
 * 0.6.2
 */