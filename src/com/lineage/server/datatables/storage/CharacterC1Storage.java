package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1User_Power;

public abstract interface CharacterC1Storage {
	public abstract void load();

	public abstract L1User_Power get(int paramInt);

	public abstract void storeCharacterC1(L1PcInstance paramL1PcInstance);

	public abstract void updateCharacterC1(int paramInt1, int paramInt2, String paramString);
}
