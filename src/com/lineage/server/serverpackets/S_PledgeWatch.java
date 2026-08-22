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
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS
 * CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package com.lineage.server.serverpackets;

import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.model.L1Clan;

/**
 * 血盟關注
 * @author l1j-tw
 */
public class S_PledgeWatch extends ServerBasePacket {

	// S_PLEDGE_WATCH (78:16) 2017.02.10 22:36:17
	// 0000: 4e 02 00 01 00 00 00 53 72 77 68 35 35 36 36 00 Srwh5566

	/**
	 * 注視的血盟清單 (被注視的血盟會顯示盟輝)
	 * @param clan
	 */
	public S_PledgeWatch(final L1Clan clan) {
		writeC(S_OPCODE_PLEDGE_WATCH);
		writeH(2);
		writeD(clan.getWatchClanList().size());
		for (final int clanid : clan.getWatchClanList()) {
			final L1Clan watch_clan = ClanReading.get().getTemplate(clanid);
			if (watch_clan == null) {
				writeS("null");
				continue;
			}
			writeS(watch_clan.getClanName());
		}
	}

	/**
	 * 無血盟注視
	 */
	public S_PledgeWatch() { // 無血盟注視
		writeC(S_OPCODE_PLEDGE_WATCH);
		writeH(2);
		writeD(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return "S_PledgeWatch";
	}
}
