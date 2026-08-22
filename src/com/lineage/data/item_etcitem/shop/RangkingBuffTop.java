package com.lineage.data.item_etcitem.shop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

/**
 * 最強的祝福
 * delay_effect 設延遲時間3600
 */
public class RangkingBuffTop extends ItemExecutor {

	@SuppressWarnings("unused")
	private static final Log _log = LogFactory.getLog(RangkingBuffTop.class);

	/**
	 *
	 */
	private RangkingBuffTop() {
		// TODO Auto-generated constructor stub
	}

	public static ItemExecutor get() {
		return new RangkingBuffTop();
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
		if (pc == null) {
			return;
		}
		if (item == null) {
			return;
		}

		// 新排行系統
		if (!Config.UserRanking) {
			pc.sendPackets(new S_ServerMessage("排行系統未開啟"));
			return;
		}

		// 停止初始技能狀態
		if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_TOP)) {
			pc.removeSkillEffect(L1SkillId.RANKING_BUFF_TOP);
		}

		pc.setSkillEffect(L1SkillId.RANKING_BUFF_TOP, 600 * 1000);
		pc.sendPackets(new S_SkillSound(pc.getId(), 12536));
		pc.broadcastPacketAll(new S_SkillSound(pc.getId(), 12536));
	}

}
