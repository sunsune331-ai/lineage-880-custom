package com.lineage.server.serverpackets;

import java.util.Collection;

import com.lineage.config.Config;
import com.lineage.server.datatables.MapsGroupTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1MapsLimitTime;
import com.lineage.server.utils.LineageUtil;

import l1j.server.server.datas.protobuf.PBMessageALL3;

/**
 * 地圖剩餘時間(ctrl+q)
 */
public class S_MapTimerOut extends ServerBasePacket {

	private byte[] _byte = null;

	private static final String S_MAP_TIMER_OUT = "[S] S_MapTimerOut";

//	/**
//	 * ctrl+Q中地圖剩餘時間
//	 */
//	public static final int DISPLAY_MAP_TIME = 159;
//
//	/**
//	 * ctrl+Q顯示剩餘時間
//	 * 
//	 * @param pc
//	 */
//	public S_MapTimerOut(final L1PcInstance pc) {
//		final Collection<L1MapsLimitTime> mapLimitList = MapsGroupTable.get()
//				.getGroupMaps().values();
//		
//		writeC(S_OPCODE_PACKETBOX);
//		writeC(DISPLAY_MAP_TIME);
//		writeD(mapLimitList.size());
//
//		for (final L1MapsLimitTime mapLimit : mapLimitList) {
//			final int order_id = mapLimit.getOrderId();
//			final int used_time = pc.getMapsTime(order_id);
//			final int time_str = (mapLimit.getLimitTime() - used_time) / 60;
//			// write
//			writeD(order_id);
//			writeS(mapLimit.getMapName());
//			writeD(time_str);
//		}
//	}

	// 8.8C
	/**
	 * ctrl+Q中地圖剩餘時間
	 */
	public static final int DISPLAY_MAP_TIME = 803;

	/**
	 * ctrl+Q顯示剩餘時間
	 * 
	 * @param pc
	 */
	public S_MapTimerOut(final L1PcInstance pc) {
		if (Config.Lohuver > 0) {
			suppressForProtocol181();
			return;
		}
		writeC(S_EXTENDED_PROTOBUF);
		writeH(DISPLAY_MAP_TIME);

		final PBMessageALL3.type8.Builder builder38 = PBMessageALL3.type8.newBuilder();
		///
		final PBMessageALL3.type10.Builder builder108 = PBMessageALL3.type10.newBuilder();
		///
		final Collection<L1MapsLimitTime> mapLimitList = MapsGroupTable.get().getGroupMaps().values();
		///
		// writeD(mapLimitList.size());
		///
		for (final L1MapsLimitTime mapLimit : mapLimitList) {
			final int order_id = mapLimit.getOrderId();
			final int used_time = pc.getMapsTime(order_id);
			final int time_str = (mapLimit.getLimitTime() - used_time); // / 60
            //地監時間顯示到退出遊戲界面
			// UserPlayDun.xml
			// 可顯示的order_id：1/2/14/15/41/53/55/56/59/100/101/102
			if (order_id == 1 // 第1頁第1個
					|| order_id == 2 // 第1頁第2個
					|| order_id == 15 // 第1頁第3個
					|| order_id == 101// 第1頁第4個
					// 第2頁
					|| order_id == 100// 第2頁第1個
					|| order_id == 14 // 第2頁第2個
					|| order_id == 102// 第2頁第3個
					|| order_id == 53 // 第2頁第4個
					// 第3頁
					|| order_id == 55 // 第3頁第1個
					|| order_id == 56 // 第3頁第2個
					|| order_id == 41 // 第3頁第3個
					|| order_id == 59 // 第3頁第4個
			) {
				builder108.setValue1(order_id); // index
				builder108.addArray2(LineageUtil.getByteString(mapLimit.getMapName())); // 地圖名稱
				builder108.setValue3(time_str); // 地圖已使用時間
				builder108.setValue4(mapLimit.getLimitTime()); // 地圖總使用時間

				builder38.addArray1(builder108.build().toByteString());
			}
		}
		writeByte(builder38.build().toByteArray());
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		if (this._byte == null) {
			// this._byte = this.getBytes();
			_byte = _bao.toByteArray();
		}
		return this._byte;
	}

	@Override
	public String getType() {
		return S_MAP_TIMER_OUT;
	}
}
