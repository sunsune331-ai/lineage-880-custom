package com.add.BigHot;

import java.util.ArrayList;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.world.World;

public class S_ShopBuyListBigHot extends ServerBasePacket {

	public S_ShopBuyListBigHot(final int objid, final ArrayList<L1ItemInstance> list) {
		final L1Object object = World.get().findObject(objid);
		if (!(object instanceof L1NpcInstance)) {
			return;
		}

		writeC(S_OPCODE_SHOWSHOPSELLLIST);
		writeD(objid);
		writeH(list.size());

		for (final L1ItemInstance item : list) {
			writeD(item.getId());
			final int BigHotId = item.getGamNo();
			final L1BigHotbling BigHotInfo = BigHotblingLock.create().getBigHotbling(BigHotId);
			if (BigHotInfo != null) {
				final String A = BigHotInfo.get_number();
				final String B = item.getStarNpcId();

				int AB = BigHotInfo.get_money1();
				final int BC = BigHotInfo.get_count();
				if (BC != 0) {
					AB /= BC;
				}

				int CD = BigHotInfo.get_money2();
				final int DE = BigHotInfo.get_count1();
				if (DE != 0) {
					CD /= DE;
				}

				int EF = BigHotInfo.get_money3();
				final int FG = BigHotInfo.get_count2();
				if (FG != 0) {
					EF /= FG;
				}

				int ch = 0;
				for (int a = 0; a < A.split(",").length; a++) {
					final String[] pk = B.split(",");
					if (("," + A).indexOf("," + pk[a] + ",") >= 0)
						ch++;
				}
				if (ch >= 3) {
					switch (ch) {
					case 3:
						writeD(50);
						break;
					case 4:
						writeD(EF);
						break;
					case 5:
						writeD(CD);
						break;
					case 6:
						writeD(AB);
						break;
					}
				}
			}
		}
	}

	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return "[S] S_ShopBuyListBigHot";
	}
}
