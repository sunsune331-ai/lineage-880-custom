package com.lineage.server.serverpackets;

import com.lineage.config.Config;
import com.lineage.data.protobuf.PBMessageALL;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldQuest;

/**
 * 更新角色所在的地圖
 * @author dexc
 */
public class S_MapID extends ServerBasePacket {

	/**
	 * 更新角色所在的地圖
	 * @param pc 更新角色
	 * @param mapid 地圖編號
	 * @param isUnderwater 是否在水裡
	 */
	public S_MapID(final L1PcInstance pc, int mapid, final boolean isUnderwater) {
		// 副本地圖中判斷
		if (QuestMapTable.get().isQuestMap(pc.getMapId())) {
			// 是副本專用地圖

		} else {// 離開副本地圖
			// 正在參加副本
			if (pc.get_showId() != -1) {
				// 副本編號 是執行中副本
				if (WorldQuest.get().isQuest(pc.get_showId())) {
					// 移出副本
					WorldQuest.get().remove(pc.get_showId(), pc);
				}
			}
			// 重置副本編號
			pc.set_showId(-1);
		}

		// 虛擬地圖
		int newmapid = MapsTable.get().getCopyMapId(mapid);
		if (newmapid >= 0) {
			mapid = newmapid;
		}
		
		writeMapPacket(mapid, isUnderwater);
	}

	/**
	 * GM 移動專用
	 * @param mapid
	 */
	public S_MapID(final int mapid) {
		writeMapPacket(mapid, false);
	}

	private void writeMapPacket(final int mapid, final boolean isUnderwater) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(0x76);
		final PBMessageALL.type1.Builder builder = PBMessageALL.type1.newBuilder();
		builder.setValue1(mapid);
		builder.setValue2(Config.SERVERNO);
		builder.setValue3(isUnderwater ? 1 : 0);
		builder.setValue4(0);
		builder.setValue5(0);
		builder.setValue6(0);
		writeByte(builder.build().toByteArray());
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return this.getBytes();
	}
}
