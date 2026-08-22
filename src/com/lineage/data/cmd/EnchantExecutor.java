package com.lineage.data.cmd;

import java.util.Random;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public abstract class EnchantExecutor {
	public abstract void failureEnchant(L1PcInstance paramL1PcInstance, L1ItemInstance paramL1ItemInstance);

	public abstract void successEnchant(L1PcInstance paramL1PcInstance, L1ItemInstance paramL1ItemInstance,
			int paramInt);

	public int randomELevel(L1ItemInstance item, int bless) {
		int level = 0;
		switch (bless) {
		case 0:// 祝福
		case 128:
			if (item.getBless() < 3) {
				Random random = new Random();
				int i = random.nextInt(100) + 1;
				if(item.getItem().get_safeenchant()==0){
					
					
					if (i < 50) {
						level = 1;
					} else if ((i >= 50) && (i <= 80)) {
						level = 2;
					} else if ((i >= 81) && (i <= 100)) {
						level = 3;
					}
					
					
				} else if (item.getEnchantLevel() <= 2) {
					if (i < 32) {
						level = 1;
					} else if ((i >= 32) && (i <= 76)) {
						level = 2;
					} else if ((i >= 77) && (i <= 100)) {
						level = 3;
					}
				} else if ((item.getEnchantLevel() >= 3) && (item.getEnchantLevel() <= 5)) {
					if (i < 35) {
						level = 2;
					} else {
						level = 1;
					}
				} else {
					level = 1;
				}
			}
			break;
		case 1:// 一般
		case 129:
			if (item.getBless() < 3) {
				level = 1;
			}
			break;
		case 2:// 詛咒
		case 130:
			if (item.getBless() < 3) {
				level = -1;
			}
			break;
		case 3:// 幻象
		case 131:
			if (item.getBless() == 3) {
				level = 1;
			}
			break;
		}
		return level;
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.data.cmd.EnchantExecutor JD-Core Version: 0.6.2
 */