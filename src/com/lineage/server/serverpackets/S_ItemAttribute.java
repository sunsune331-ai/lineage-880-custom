/**
 * License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1ItemInstance;

public class S_ItemAttribute extends ServerBasePacket {

	/**
	 * 變更道具屬性 (S_AddItem下半部)-封印道具
	 */

	// S_EVENT-149[ITEM_ATTRIBUTE] (108:34) 2016.09.29 15:12:49
	// 0000: 6c 95 52 6b 17 00 2e 18 00 00 00 00 00 00 bc 84 Rk
	// 0010: 04 00 00 00 00 00 00 00 00 00 03 00 00 00 00 00
	// 0020: 1d 1a
	public S_ItemAttribute(final L1ItemInstance item) {
		writeC(S_EVENT);
		writeC(S_PacketBox.ITEM_STATUS); // 149 同172 只是多了getItemStatusX() 欄位

		writeD(item.getId());
		// writeC(item.getItemStatusX()); // 3.80C 物品驗證機制
		int statusX = 0;
		if (item.isIdentified()) {
			statusX |= 1;
		}
		if (!item.getItem().isTradable()) {
			statusX |= 2;
		}
		if (item.getItem().isCantDelete()) {
			statusX |= 4;
		}
		if ((item.getItem().get_safeenchant() < 0)
				|| (item.getItem().getUseType() == -3)
				|| (item.getItem().getUseType() == -2)) {
			statusX |= 8;
		}
		if (item.getBless() >= 128) {
			statusX = 32;
			if (item.isIdentified()) {
				statusX |= 1;
				statusX |= 2;
				statusX |= 4;
				statusX |= 8;
			} else {
				statusX |= 2;
				statusX |= 4;
				statusX |= 8;
			}
		}
		writeC(statusX);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "S_ItemAttribute";
	}

}
