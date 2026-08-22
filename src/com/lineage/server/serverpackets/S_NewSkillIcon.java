package com.lineage.server.serverpackets;

import com.lineage.server.model.skill.L1SkillId;

/**
 * 8.1新技能圖示
 */
public class S_NewSkillIcon extends ServerBasePacket {

    private static final String S_NEWSKILLICON = "[S] S_NewSkillIcon";

    /**
     * 8.1新技能圖示
     * @param skillId
     * @param on
     * @param time
     */
    public S_NewSkillIcon(int skillId, boolean on, long time) {
        writeC(S_EXTENDED_PROTOBUF);
        writeH(0x6E);
        writeC(0x08);
        writeC(on ? 2 : 3);
        writeC(0x10);
        byteWrite(skillId);
        if (on) {
            writeC(0x18);
            if (time < 0) {
                byte[] minus = { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x01 };
                writeByte(minus);
            } else
                byteWrite(time);
            writeC(0x20);
            writeC(0x08);
            writeC(0x28);
            if (skillId == L1SkillId.ABSOLUTE_BLADE) { // 騎士新技能 絕御之刃
                byteWrite(7433);
            } else if (skillId == L1SkillId.DEATH_HEAL) { // 法師新技能 治癒逆行
                byteWrite(7439);
            } else if (skillId == L1SkillId.ASSASSIN) {// 黑妖新技能 暗殺者
                byteWrite(7445);
            } else if (skillId == L1SkillId.BLAZING_SPIRITS) { // 黑妖新技能 熾烈鬥志
                byteWrite(7448);
            } else if (skillId == L1SkillId.GRACE_AVATAR) { // 王族新技能 恩典庇護
                byteWrite(7428);
            } else if (skillId == L1SkillId.SOUL_BARRIER) { // 精靈新技能 魔力護盾
                byteWrite(7436);
            } else if (skillId == L1SkillId.DESTROY) { // 龍騎士新技能 撕裂護甲
                byteWrite(7451);
            } else if (skillId == L1SkillId.IMPACT) { // 幻術師新技能 衝突強化
                byteWrite(7457);
            } else if (skillId == L1SkillId.TITANL_RISING) { // 狂戰士新技能 泰坦狂暴
                byteWrite(7461);
            } else if (skillId == L1SkillId.ILLUSION_OGRE) { // 幻覺歐吉
                byteWrite(3117);
            } else if (skillId == L1SkillId.CUBE_IGNITION) { // 立方歐吉
                byteWrite(5312);
            } else if (skillId == L1SkillId.ILLUSION_LICH) { // 幻覺巫妖
                byteWrite(3115);
            } else if (skillId == L1SkillId.CUBE_SHOCK) { // 立方巫妖
                byteWrite(5309);
            } else if (skillId == L1SkillId.ILLUSION_DIA_GOLEM) { // 幻覺高崙
                byteWrite(3113);
            } else if (skillId == L1SkillId.CUBE_QUAKE) { // 立方高崙
                byteWrite(5314);
            } else if (skillId == L1SkillId.ILLUSION_AVATAR) { // 幻覺化身
                byteWrite(3111);
            } else if (skillId == L1SkillId.ILLUSION_AVATAR2) { // 幻覺化身
                byteWrite(3111);
            } else if (skillId == L1SkillId.CUBE_BALANCE) { // 立方化身
                byteWrite(3101);
            //} else if (skillId == L1SkillId.STATUS_CASHSCROLL3) { // 強化戰鬥卷軸 vs 龍之石 狀態 圖標
                //byteWrite(2430);
            }
        }
        writeH(0x0030);
        if (on) {
            writeC(0x38);
            writeC(0x03);
            writeC(0x40);
            int msgNum = 0;
            if (skillId == L1SkillId.ABSOLUTE_BLADE) // 騎士新技能 絕御之刃
                msgNum = 4735;
            else if (skillId == L1SkillId.DEATH_HEAL) // 法師新技能 治癒逆行
                msgNum = 4737;
            else if (skillId == L1SkillId.ASSASSIN) // 黑妖新技能 暗殺者
                msgNum = 4738;
            else if (skillId == L1SkillId.BLAZING_SPIRITS) // 黑妖新技能 熾烈鬥志
                msgNum = 4750;
            else if (skillId == L1SkillId.GRACE_AVATAR) // 王族新技能 恩典庇護
                msgNum = 4734;
            else if (skillId == L1SkillId.SOUL_BARRIER) // 精靈新技能 魔力護盾
                msgNum = 4736;
            else if (skillId == L1SkillId.DESTROY) // 龍騎士新技能 撕裂護甲
                msgNum = 4739;
            else if (skillId == L1SkillId.IMPACT) // 幻術師新技能 衝突強化
                msgNum = 4761;
            //else if (skillId == L1SkillId.STATUS_CASHSCROLL3) // 強化戰鬥卷軸 vs 龍之石 狀態 圖標
                //msgNum = 1316;
            else if (skillId == L1SkillId.TITANL_RISING) // 狂戰士新技能 泰坦狂暴
                msgNum = 4740;

            byteWrite(msgNum);
            writeC(0x48);
            writeC(0x00);
        }
        writeH(0x0050);
        if (on) {
            writeC(0x58);
            writeC(0x01);
            writeC(0x60);
            writeC(0x00);
            writeC(0x68);
            writeC(0x00);
            writeC(0x70);
            writeC(0x00);
        }
        writeH(0x00);
    }

    public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90, 0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b, 0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1,
            0xa2, 0xa3, 0xa4, 0xa5, 0xa6, 0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc, 0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5, 0xc6, 0xc7, 0xc8,
            0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2, 0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd, 0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef,
            0xf0, 0xf1, 0xf2, 0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe, 0xff };

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

	public S_NewSkillIcon(int buffid, int time, boolean CK) {
		if (buffid == 1) {
			buffid = 2272;
		} else if (buffid == 2) {
			buffid = 2273;
		} else if (buffid == 3) {
			buffid = 2274;
		} else if (buffid == 4) {
			buffid = 2275;
		} else if (buffid == 5) {
			buffid = 2276;
		}

		int[] buffState = getIcon(buffid);

		writeC(S_EXTENDED_PROTOBUF);
		writeC(0x6e);
		writeC(0x00);

		writeC(0x08);
		writeC(CK ? 0x02 : 0x03);

		writeC(0x10);
		writeBit(buffid);

		writeC(0x18);
		writeBit(time);

		writeC(0x20);
		writeC(0x08);

		writeC(0x28);
		writeBit(buffState[0]);
		writeC(0x30);
		writeC(0x00);

		writeC(0x38);
		writeC(0x03);

		writeC(0x40);
		writeBit(buffState[1]);

		writeC(0x48);
		writeC(0x00);

		writeC(0x50);
		writeC(0x00);

		writeC(0x58);
		writeC(0x01); // ??

		writeC(0x60);
		writeC(0x00);

		writeC(0x68);
		writeC(0x00);

		writeC(0x70);
		writeC(0x00);

		writeH(0x00);
	}

	private int[] getIcon(int skillid) {
		int[] id = new int[2];
		switch (skillid) {
		case 2272:
			id[0] = 8265;
			id[1] = 3913;
			break;
		case 2273:
			id[0] = 8266;
			id[1] = 4178;
			break;
		case 2274:
			id[0] = 8267;
			id[1] = 4179;
			break;
		case 2275:
			id[0] = 8268;
			id[1] = 5046;
			break;
		case 2276:
			id[0] = 8269;
			id[1] = 5047;
			break;
		case 2724:
			id[0] = 7233;
			id[1] = 4650;
			break;
		case 2725:
			id[0] = 7235;
			id[1] = 4651;
			break;
		case 2726:
			id[0] = 7237;
			id[1] = 4652;
			break;
		case 2727:
			id[0] = 7239;
			id[1] = 4653;
			break;
		case 8463:
			id[0] = 8463;
			id[1] = 5119;
			break;
		case 5157:
			id[0] = 8490;
			id[1] = 5157;
			break;
		case 5393:
			id[0] = 5393;
			id[1] = 849;
			break;

		case 8843:
			id[0] = 8843;
			id[1] = 5266;
			break;
		case 8880:
			id[0] = 8880;
			id[1] = 5268;
			break;

		case 3111:
			id[0] = 3111;
			id[1] = 1351;
			break;
		case 3113:
			id[0] = 3111;
			id[1] = 1347;
			break;
		case 3115:
			id[0] = 3111;
			id[1] = 1343;
			break;
		case 3117:
			id[0] = 3117;
			id[1] = 1340;
			break;

		case 5312:
			id[0] = 5312;
			id[1] = 3074;
			break;
		case 5309:
			id[0] = 5309;
			id[1] = 1348;
			break;
		case 5314:
			id[0] = 5314;
			id[1] = 3075;
			break;
		case 5322:
			id[0] = 5322;
			id[1] = 3073;
			break;

		case 4832:
			id[0] = 4832;
			id[1] = 5272;
			break;

		case 1562:
			id[0] = 1562;
			id[1] = 966;
			break;
		}

		return id;
	}

    @Override
    public byte[] getContent() {
		return _bao.toByteArray();
    }

    @Override
    public String getType() {
        return S_NEWSKILLICON;
    }
}
