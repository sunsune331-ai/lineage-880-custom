package com.lineage.data.npc;

import com.lineage.config.ConfigQuest;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;

/**
 * 每日獎勵
 */
public class Npc_Day_Reward extends NpcExecutor {
	
	private Npc_Day_Reward() {
		// TODO Auto-generated constructor stub
	}

	public static NpcExecutor get() {
		return new Npc_Day_Reward();
	}

	@Override
	public int type() {
		return 3;
	}

	@Override
	public void talk(final L1PcInstance pc, final L1NpcInstance npc) {
		pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "day_reward"));
	}

	@Override
	public void action(final L1PcInstance pc, final L1NpcInstance npc, final String cmd, final long amount) {
        if (cmd.equalsIgnoreCase("dayreward")) {
    	    if (pc.getQuest().get_step(ConfigQuest.Day_Reward_QuestId) == 255) {
    	        pc.sendPackets(new S_SystemMessage("每天只能領取一次。"));
    	        pc.sendPackets(new S_CloseList(pc.getId()));
    	        return;
    	    }

    		if (ConfigQuest.Day_Reward_Levelmet != 0) {
    			if (pc.getMeteLevel() < ConfigQuest.Day_Reward_Levelmet) {
    				pc.sendPackets(new S_SystemMessage("轉生次數低於[" + ConfigQuest.Day_Reward_Levelmet + "]無法領取。"));
    				pc.sendPackets(new S_CloseList(pc.getId()));
    				return;
    			}
    		}

    		if (ConfigQuest.Day_Reward_Levelvip != 0) {
    			if (pc.get_vipLevel() < ConfigQuest.Day_Reward_Levelvip) {
    				pc.sendPackets(new S_SystemMessage("VIP等級低於[" + ConfigQuest.Day_Reward_Levelvip + "]無法領取。"));
    				pc.sendPackets(new S_CloseList(pc.getId()));
    				return;
    			}
    		}

    		if (ConfigQuest.Day_Reward_Level != 0) {
    			if (pc.getLevel() < ConfigQuest.Day_Reward_Level) {
    				pc.sendPackets(new S_SystemMessage("等級低於[" + ConfigQuest.Day_Reward_Level + "]無法領取。"));
    				pc.sendPackets(new S_CloseList(pc.getId()));
    				return;
    			}
    		}

    	    final int size = ConfigQuest.Day_Reward_Item.length;
    	    for (int j = 0; j < size; j++) {
    			pc.getInventory().storeItem(ConfigQuest.Day_Reward_Item[j][0], ConfigQuest.Day_Reward_Item[j][1]);
    		    final L1Item DayRewardItem = ItemTable.get().getTemplate(ConfigQuest.Day_Reward_Item[j][0]);
    			pc.sendPackets(new S_SystemMessage("恭喜獲得每日獎勵[" + DayRewardItem.getName() + "("+ ConfigQuest.Day_Reward_Item[j][1] +")]。"));
    	    }
    	    pc.getQuest().set_step(ConfigQuest.Day_Reward_QuestId, 255); // 給任務編號
    	    pc.sendPackets(new S_CloseList(pc.getId()));
        }
	}

}
