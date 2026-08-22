package com.lineage.server.serverpackets;

import java.util.List;

import com.lineage.server.Controller.UserRankingController;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.templates.L1UserRanking;

/**
 * 新排行系統
 */
public class S_UserRankSystem extends ServerBasePacket {
	{ suppressForProtocol181(); }

	private static final String S_USERRANKSYSTEM = "[S] S_UserRankSystem";

	private byte[] _byte = null;

	public static final int USER_RANKING = 0x88;

	public static final int USER_LIST_RANKING = 0x89;

	public S_UserRankSystem(int skillId, boolean on, int classId, long time) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(0x6E);
		writeC(0x08);
		writeC(on ? 2 : 3);
		writeC(0x10);
		byteWrite(skillId);
		writeC(0x18);
		if (time < 0) {
			byte[] minus = { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0x01 };
			writeByte(minus);
		} else {
			byteWrite(time);
		}
		writeC(0x20);
		writeC(0x08);
		writeC(0x28);
		// R圖標
		if (skillId == L1SkillId.RANKING_BUFF_11) {
			byteWrite(7096);
		} else if (skillId == L1SkillId.RANKING_BUFF_10) {
			byteWrite(7095);
		} else if (skillId == L1SkillId.RANKING_BUFF_9) {
			byteWrite(7094);
		} else if (skillId == L1SkillId.RANKING_BUFF_8) {
			byteWrite(7093);
		} else if (skillId == L1SkillId.RANKING_BUFF_7) {
			byteWrite(7093);
		} else if (skillId == L1SkillId.RANKING_BUFF_6) {
			byteWrite(7093);
		} else if (skillId == L1SkillId.RANKING_BUFF_5) {
			byteWrite(8532);
		} else if (skillId == L1SkillId.RANKING_BUFF_4) {
			byteWrite(8532);
		} else if (skillId == L1SkillId.RANKING_BUFF_3) {
			byteWrite(8532);
		} else if (skillId == L1SkillId.RANKING_BUFF_2) {
			byteWrite(8532);
		} else if (skillId == L1SkillId.RANKING_BUFF_1) {
			byteWrite(8532);
		}
		writeH(0x0030);
		writeC(0x38);
		writeC(0x03);
		writeC(0x40);

		int msgNum = 0;

		if (skillId == L1SkillId.RANKING_BUFF_11) {
			msgNum = 5148;
		} else if (skillId == L1SkillId.RANKING_BUFF_10) {
			msgNum = 5145;
		} else if (skillId == L1SkillId.RANKING_BUFF_9) {
			msgNum = 5144;
		} else if (skillId == L1SkillId.RANKING_BUFF_8) {
			msgNum = 5143;
		} else if (skillId == L1SkillId.RANKING_BUFF_7) {
			msgNum = 5142;
		} else if (skillId == L1SkillId.RANKING_BUFF_6) {
			msgNum = 5141;
		} else if (skillId == L1SkillId.RANKING_BUFF_5) {
			msgNum = 5140;
		} else if (skillId == L1SkillId.RANKING_BUFF_4) {
			msgNum = 5139;
		} else if (skillId == L1SkillId.RANKING_BUFF_3) {
			msgNum = 5138;
		} else if (skillId == L1SkillId.RANKING_BUFF_2) {
			msgNum = 5137;
		} else if (skillId == L1SkillId.RANKING_BUFF_1) {
			msgNum = 5136;
		}

		if (classId == 2 && msgNum >= 5145) {
			msgNum += 1;
		} else if (classId == 3 && msgNum >= 5145 || classId == 6 && msgNum >= 5145) {
			msgNum += 2;
		}

		byteWrite(msgNum);

		writeC(0x48);
		writeC(0x00);
		writeH(0x0050);
		writeC(0x58);
		writeC(0x01);
		writeC(0x60);
		writeC(0x00);
		writeC(0x68);
		writeC(0x00);
		writeC(0x70);
		writeC(0x00);
		writeH(0x00);
	}

	public S_UserRankSystem(int type, List<L1UserRanking> list, int classId, int totalPage, int curPage) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case USER_RANKING:
			writeH(0x08);
			writeC(0x10);
			byteWrite(System.currentTimeMillis() / 1000);
			writeC(0x18);
			writeC(classId);
			writeC(0x20);
			writeC(totalPage);
			writeC(0x28);
			writeC(curPage);

			for (L1UserRanking user : list) {
				byte[] name = user.getName().getBytes();
				int rank = user.getCurRank();
				int oldRank = user.getOldRank();

				int buffCount = 0;

				if (classId == 8) {
					if (rank >= 1 && rank <= 10) {
						buffCount = 11;
					} else if (rank >= 11 && rank <= 20) {
						buffCount = 10;
					} else if (rank >= 21 && rank <= 40) {
						buffCount = 9;
					} else if (rank >= 41 && rank <= 60) {
						buffCount = 8;
					} else if (rank >= 61 && rank <= 80) {
						buffCount = 7;
					} else if (rank >= 81 && rank <= 100) {
						buffCount = 6;
					} else if (rank >= 101 && rank <= 120) {
						buffCount = 5;
					} else if (rank >= 121 && rank <= 140) {
						buffCount = 4;
					} else if (rank >= 141 && rank <= 160) {
						buffCount = 3;
					} else if (rank >= 161 && rank <= 180) {
						buffCount = 2;
					} else if (rank >= 181 && rank <= 200) {
						buffCount = 1;
					}
				} else {
					buffCount = UserRankingController.getInstance().getStarCount(user.getName());
				}

				int length = 8 + name.length + byteWriteCount(rank) + byteWriteCount(oldRank);

				writeC(0x32);
				byteWrite(length);

				writeC(0x08);
				writeC(buffCount);
				writeC(0x10);
				byteWrite(rank);
				writeC(0x18);
				byteWrite(oldRank);
				writeC(0x20);
				writeC(user.getClassId());
				writeC(0x2A);
				writeC(name.length);
				writeByte(name);
			}
			writeH(0x00);
			break;
		}
	}

	public S_UserRankSystem(int type, L1UserRanking rank) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case USER_LIST_RANKING:
			int star = UserRankingController.getInstance().getStarCount(rank.getName());
			byte[] name = rank.getName().getBytes();
			int length = 11 + byteWriteCount(rank.getCurRank()) + byteWriteCount(rank.getOldRank())
					+ byteWriteCount(rank.getClassId()) + name.length;
			writeC(0x0A);
			writeC(length);
			writeC(0x08);
			writeC(star);
			writeC(0x10);
			byteWrite(rank.getCurRank());
			writeC(0x18);
			byteWrite(rank.getOldRank());
			writeC(0x20);
			byteWrite(rank.getClassId());
			writeC(0x2A);
			writeC(name.length);
			writeByte(name);
			writeC(0x30);
			writeC(0x00);
			writeC(0x38);
			writeC(0x00);

			L1UserRanking classRank = UserRankingController.getInstance().getClassRank(rank.getClassId(),
					rank.getName());

			length = 7 + byteWriteCount(classRank.getCurRank()) + byteWriteCount(classRank.getOldRank())
					+ byteWriteCount(rank.getClassId()) + name.length;
			writeC(0x12);
			writeC(length);
			writeC(0x08);
			writeC(star);
			writeC(0x10);
			byteWrite(classRank.getCurRank());
			writeC(0x18);
			byteWrite(classRank.getOldRank());
			writeC(0x20);
			byteWrite(rank.getClassId());
			writeC(0x2A);
			writeC(name.length);
			writeByte(name);

			if (rank.getCurRank() != 1) {
				writeC(0x18);
				writeC(0x01);
			}
			writeC(0x20);
			writeC(0x01);
			writeC(0x30);
			writeC(0x01);

			writeH(0x00);
			break;
		}
	}

	private int byteWriteCount(long value) {
		long temp = value / 128;
		int count = 0;
		if (temp > 0) {
			count++;
			while (temp >= 128) {
				count++;
				temp = temp / 128;
			}
			if (temp > 0)
				count++;
		} else {
			if (value == 0) {
				count++;
			} else {
				count += 2;
			}
		}
		return count;
	}

	private void byteWrite(long value) {
		long temp = value / 128;
		if (temp > 0) {
			writeC(hextable[(int) value % 128]);
			while (temp >= 128) {
				writeC(hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				writeC((int) temp);
		} else {
			if (value == 0) {
				writeC(0);
			} else {
				writeC(hextable[(int) value]);
				writeC(0);
			}
		}
	}

	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c,
			0x8d, 0x8e, 0x8f, 0x90, 0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b, 0x9c, 0x9d, 0x9e,
			0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6, 0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0,
			0xb1, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc, 0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2,
			0xc3, 0xc4, 0xc5, 0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2, 0xd3, 0xd4,
			0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd, 0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6,
			0xe7, 0xe8, 0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8,
			0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe, 0xff };

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_USERRANKSYSTEM;
	}

}
