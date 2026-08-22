package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Skills;

/**
 * 掛機開關符
 */
public class Hang_fu extends ItemExecutor {
	
    /**
	 *
	 */
    private Hang_fu() {
    	
    	// TODO Auto-generated constructor stub
    }

    public static ItemExecutor get() {
    	
    	return new Hang_fu();
    }

    /**
     * 道具物件執行
     * 
     * @param data
     *            參數
     * @param pc
     *            執行者
     * @param item
     *            物件
     */
    @Override
    public void execute(final int[] data, final L1PcInstance pc, final L1ItemInstance item) {
    	String kaiguai = "關閉狀態";
		if (pc.getygjnzc() > 0) {
			final L1Skills skill = SkillsTable.get().getTemplate(
					pc.getygjnzc());
			if (skill != null) {
				kaiguai = skill.getName();
			}
		}
		String shoulie = "關閉狀態";
		if (pc.getgjjnzc() > 0) {
			final L1Skills skill1 = SkillsTable.get().getTemplate(
					pc.getgjjnzc());
			if (skill1 != null) {
				shoulie = skill1.getName();
			}
		}
		int gjjnjg = pc.getgjjgsj();
		int gjml = pc.getgjml();
		String lmm="關閉中";
		if(pc.limao()){
			   lmm="開啟中";
		}			
		int gjfw =pc.getlsgjfw();
		String bosssy="關閉中";
		if(pc.getbossgjsykg()){
			   bosssy="開啟中";
		}	
		String gjsy="關閉中";
		if(pc.getgjsykg()){
			   gjsy="開啟中";
		}
		String[] info = new String[] { String.valueOf(kaiguai),
				String.valueOf(shoulie), String.valueOf(gjjnjg),
				String.valueOf(gjml), String.valueOf(lmm),
				String.valueOf(gjfw), String.valueOf(bosssy),
				String.valueOf(gjsy), };
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "guajiqd", info));
    }
}
