package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Config;

public abstract interface CharacterConfigStorage {
	public abstract void load();

	public abstract L1Config get(int paramInt);

	public abstract void storeCharacterConfig(int paramInt1, int paramInt2, byte[] paramArrayOfByte);

	public abstract void updateCharacterConfig(int paramInt1, int paramInt2, byte[] paramArrayOfByte);
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.storage.CharacterConfigStorage JD-Core Version:
 * 0.6.2
 */