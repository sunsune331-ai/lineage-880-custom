package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.WorldClan;

/**
 * 血盟祝福系統
 * @author Admin
 */
public class S_EinhasadClanBuff extends ServerBasePacket {
	{ suppressForProtocol181(); }

	private static final String _S_EinhasadClanBuff = "[S] S_EinhasadClanBuff";

	/**
	 * 血盟祝福系統
	 * @param pc
	 */
	public S_EinhasadClanBuff(final L1PcInstance pc) {
		writeC(S_EXTENDED_PROTOBUF);
		writeC(251);
		writeC(3);

		final L1Clan clan = WorldClan.get().getClan(pc.getClanid());
		if (clan.getBuffFirst() != 0) {
			writeC(10);
			writeC(5);
			writeC(8);
			writeBit(clan.getBuffFirst());
			writeC(16);
			if (clan.getEinhasadBlessBuff() == 0) {
				writeC(1);
			} else if (clan.getEinhasadBlessBuff() == clan.getBuffFirst()) {
				writeC(2);
			} else {
				writeC(3);
			}
		}

		if (clan.getBuffSecond() != 0) {
			writeC(10);
			writeC(5);
			writeC(8);
			writeBit(clan.getBuffSecond());
			writeC(16);
			if (clan.getEinhasadBlessBuff() == 0) {
				writeC(1);
			} else if (clan.getEinhasadBlessBuff() == clan.getBuffSecond()) {
				writeC(2);
			} else {
				writeC(3);
			}
		}

		if (clan.getBuffThird() != 0) {
			writeC(10);
			writeC(5);
			writeC(8);
			writeBit(clan.getBuffThird());
			writeC(16);
			if (clan.getEinhasadBlessBuff() == 0) {
				writeC(1);
			} else if (clan.getEinhasadBlessBuff() == clan.getBuffThird()) {
				writeC(2);
			} else {
				writeC(3);
			}
		}

		writeC(16);
		writeC(1);

		writeH(0);
	}

	@Override
	public byte[] getContent() {
		return _bao.toByteArray();
	}

    @Override
	public String getType() {
		return _S_EinhasadClanBuff;
	}
}
