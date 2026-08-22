package com.lineage.server.serverpackets;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import com.lineage.server.Controller.UserRankingController;
import com.lineage.server.templates.L1UserRanking;

/**
 * 1810102501 character ranking packet.
 */
public class S_CharRank181 extends ServerBasePacket {

	public S_CharRank181(final String name, final int classId, final boolean rankingEnabled) {
		int star = 0;
		int currentRank = 0;
		int oldRank = 0;
		int currentClassRank = 0;
		int oldClassRank = 0;

		if (rankingEnabled) {
			try {
				final UserRankingController controller = UserRankingController.getInstance();
				final L1UserRanking rank = controller.getTotalRank(name);
				if (rank != null) {
					star = controller.getStarCount(name);
					currentRank = Math.max(0, rank.getCurRank());
					oldRank = Math.max(0, rank.getOldRank());
				}
				final L1UserRanking classRank = controller.getClassRank(classId, name);
				if (classRank != null) {
					currentClassRank = Math.max(0, classRank.getCurRank());
					oldClassRank = Math.max(0, classRank.getOldRank());
				}
			} catch (final Exception e) {
				// Ranking data is optional at the character-list stage.
			}
		}

		final byte[] nameBytes = name.getBytes(Charset.forName(CLIENT_LANGUAGE_CODE));
		final byte[] total = buildRankEntry(star, currentRank, oldRank, classId, nameBytes);
		final byte[] classValue = buildRankEntry(star, currentClassRank, oldClassRank, classId, nameBytes);
		final ByteArrayOutputStream payload = new ByteArrayOutputStream();
		writeBytesField(payload, 1, total);
		writeBytesField(payload, 2, classValue);

		writeC(S_EXTENDED_PROTOBUF);
		writeH(137);
		writeByte(payload.toByteArray());
		writeH(0);
	}

	private static byte[] buildRankEntry(final int star, final int currentRank, final int oldRank,
			final int classId, final byte[] nameBytes) {
		final ByteArrayOutputStream entry = new ByteArrayOutputStream();
		writeIntField(entry, 1, star);
		writeIntField(entry, 2, currentRank);
		writeIntField(entry, 3, oldRank);
		writeIntField(entry, 4, Math.max(0, classId));
		writeBytesField(entry, 5, nameBytes);
		return entry.toByteArray();
	}

	private static void writeIntField(final ByteArrayOutputStream out, final int field, final int value) {
		writeVarInt(out, field << 3);
		writeVarInt(out, value);
	}

	private static void writeBytesField(final ByteArrayOutputStream out, final int field, final byte[] value) {
		writeVarInt(out, (field << 3) | 2);
		writeVarInt(out, value.length);
		out.write(value, 0, value.length);
	}

	private static void writeVarInt(final ByteArrayOutputStream out, final int value) {
		long data = value & 0xffffffffL;
		while ((data & ~0x7fL) != 0L) {
			out.write(((int) data & 0x7f) | 0x80);
			data >>>= 7;
		}
		out.write((int) data);
	}

	@Override
	public byte[] getContent() {
		return _bao.toByteArray();
	}

	@Override
	public String getType() {
		return getClass().getSimpleName();
	}
}
