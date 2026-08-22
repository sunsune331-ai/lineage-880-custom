package com.lineage.server.datatables.storage;

import java.util.ArrayList;

import com.lineage.server.templates.L1UserSkillTmp;

public abstract interface CharSkillStorage {
	public abstract void load();

	public abstract ArrayList<L1UserSkillTmp> skills(int paramInt);

	public abstract void spellMastery(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4);

	public abstract void spellLost(int paramInt1, int paramInt2);

	public abstract boolean spellCheck(int paramInt1, int paramInt2);

	public abstract void setAuto(int paramInt1, int paramInt2, int paramInt3);
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.storage.CharSkillStorage JD-Core Version: 0.6.2
 */