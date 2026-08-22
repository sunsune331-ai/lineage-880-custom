package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_Sound;

public class Whistle extends ItemExecutor {
	public static ItemExecutor get() {
		return new Whistle();
	}

	public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
		pc.sendPacketsX8(new S_Sound(437));

		Object[] petList = pc.getPetList().values().toArray();
		for (Object petObject : petList)
			if ((petObject instanceof L1PetInstance)) {
				L1PetInstance pet = (L1PetInstance) petObject;
				pet.call();
			}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.data.item_etcitem.Whistle JD-Core Version: 0.6.2
 */