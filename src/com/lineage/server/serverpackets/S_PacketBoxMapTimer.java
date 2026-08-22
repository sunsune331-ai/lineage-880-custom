package com.lineage.server.serverpackets;

import java.util.Collection;

import com.lineage.data.protobuf.PBMessageALL3;
import com.lineage.server.datatables.MapsGroupTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1MapsLimitTime;

/**
 * @author terry0412
 */
public class S_PacketBoxMapTimer extends ServerBasePacket {

	private byte[] _byte = null;

	public S_PacketBoxMapTimer(final L1PcInstance pc) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(803);

		final Collection<L1MapsLimitTime> mapLimitList = MapsGroupTable.get()
				.getGroupMaps().values();
		final PBMessageALL3.type8.Builder builder = PBMessageALL3.type8.newBuilder();

		for (final L1MapsLimitTime mapLimit : mapLimitList) {
			final int order_id = mapLimit.getOrderId();
			final int used_time = pc.getMapsTime(order_id);
			final int time_str = mapLimit.getLimitTime() - used_time;
			final PBMessageALL3.type10.Builder entry = PBMessageALL3.type10.newBuilder();
			entry.setValue1(order_id);
			entry.addArray2(getByteString(mapLimit.getMapName()));
			entry.setValue3(time_str);
			entry.setValue4(mapLimit.getLimitTime());
			builder.addArray1(entry.build().toByteString());
		}
		writeByte(builder.build().toByteArray());
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (this._byte == null) {
			this._byte = this.getBytes();
		}
		return this._byte;
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}
}
