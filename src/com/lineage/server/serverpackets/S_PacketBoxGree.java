package com.lineage.server.serverpackets;

import java.util.Iterator;
import java.util.List;

import com.lineage.server.templates.L1Rank;

public class S_PacketBoxGree extends ServerBasePacket {
	private byte[] _byte = null;
	private static final int GREEN_MESSAGE = 84;
	private static final int SECRETSTORY_GFX = 83;

	public S_PacketBoxGree(String msg) {
		writeC(S_EVENT);
		writeC(GREEN_MESSAGE);
		writeC(2);
		writeS(msg);
	}

	public S_PacketBoxGree(int type) {
		writeC(S_EVENT);
		writeC(SECRETSTORY_GFX);
		writeD(type);
		writeC(0);
		writeC(0);
		writeC(0);
	}

	public S_PacketBoxGree(int type, String msg) {
		writeC(S_EVENT);
		writeC(GREEN_MESSAGE);
		writeC(type);
		writeS(msg);
	}

	public S_PacketBoxGree(List<?> totalList, int totalSize, int this_order, int this_score) {
		_byte = null;
		writeC(S_EVENT);
		writeC(112);
		writeD(0);
		writeD(totalSize);
		for (Iterator<?> iterator = totalList.iterator(); iterator.hasNext();) {
			L1Rank rank = (L1Rank) iterator.next();
			writeC(rank.getMemberSize());
			writeD(rank.getScore());
			writeS(rank.getPartyLeader());
			String memberName;
			for (Iterator<String> iterator1 = rank.getPartyMember().iterator(); iterator1.hasNext(); writeS(memberName))
				memberName = (String) iterator1.next();

		}

		writeC(this_order);
		writeD(this_score);
	}

	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return getClass().getSimpleName();
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.serverpackets.S_PacketBoxGree JD-Core Version: 0.6.2
 */
