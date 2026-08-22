package com.lineage.server.serverpackets;

import java.io.IOException;

/**
 * 被動技能
 * @author
 */
public class S_WarriorSkill extends ServerBasePacket {

	public static final int LOGIN = 0x91;

	public static final int ADD = 0x92;

	/**
	 * 學習技能時
	 * @param subcode
	 * @param type
	 */
	public S_WarriorSkill(final int subcode, final int type) {
		writeC(S_EXTENDED_PROTOBUF);
		writeC(subcode);
		switch (subcode) {
		/*case LOGIN:
			writeC(0x01);
			writeC(0x0a);
			writeC(0x02);
			writeC(0x08);
			writeC(type);
			writeC(0x82);
			writeC(0x0b);
			break;
		case ADD:
			writeC(0x01);
			writeC(0x08);
			writeC(type);
			writeC(0x10);
			writeC(0x0a);
			writeH(0x33c2);
			break;*/
		case LOGIN: // 登錄
            // b3 91 01 0a 02 08 03 8c c7
            writeC(0x01);
            writeC(0x0a);
			writeC(0x02);
			writeC(0x08);
			writeC(type);
			writeC(0x82);
			writeC(0x0b);
			break;
		case ADD:
            // b3 92 01 08 03 c2 33
			writeC(0x01);
			writeC(0x08);
			writeC(type);
			writeC(0x10);
			writeC(0x0a);
			writeH(0x33c2);
			break;
	    }
	}

	// boolean warriorSkill_1 = false;// 粉碎
	// boolean warriorSkill_2 = false;// 狂暴
	// boolean warriorSkill_3 = false;// 迅猛雙斧
	// boolean warriorSkill_5 = false;// 護甲身軀
	// boolean warriorSkill_6 = false;// 泰坦：岩石
	// boolean warriorSkill_7 = false;// 泰坦：子彈
	// boolean warriorSkill_8 = false;// 泰坦：魔法
	//
	// boolean warriorSkill_9 = false;// 熾烈鬥志
	//
	// /** 重新登入時 */
	// public S_WarriorSkill(final int subcode, int[] skills) {
	// writeC(S_OPCODE_EXTENDED_PROTOBUF);
	// writeC(subcode);
	// writeC(0x01);
	//
	// for (int i = 0; i < skills.length; i++) {
	// if (skills[i] == 1) {
	// warriorSkill_1 = true;
	// } else if (skills[i] == 2) {
	// warriorSkill_2 = true;
	// } else if (skills[i] == 3) {
	// warriorSkill_3 = true;
	// } else if (skills[i] == 5) {
	// warriorSkill_5 = true;
	// } else if (skills[i] == 6) {
	// warriorSkill_6 = true;
	// } else if (skills[i] == 7) {
	// warriorSkill_7 = true;
	// } else if (skills[i] == 8) {
	// warriorSkill_8 = true;
	//
	// } else if (skills[i] == 9) {// 熾烈鬥志
	// warriorSkill_9 = true;
	// }
	// }
	//
	// if (warriorSkill_1) {
	// writeC(0x0a);
	// writeC(0x02);
	// writeC(0x08);
	// writeC(0x01);
	// } else {
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// }
	//
	// if (warriorSkill_2) {
	// writeC(0x0a);
	// writeC(0x02);
	// writeC(0x08);
	// writeC(0x02);
	// } else {
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// }
	//
	// if (warriorSkill_3) {
	// writeC(0x0a);
	// writeC(0x02);
	// writeC(0x08);
	// writeC(0x03);
	// } else {
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// }
	//
	// if (warriorSkill_5) {
	// writeC(0x0a);
	// writeC(0x04);
	// writeC(0x08);
	// writeC(0x05);
	// writeC(0x10);
	// writeC(0x0a);
	// } else {
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// }
	//
	// if (warriorSkill_6) {
	// writeC(0x0a);
	// writeC(0x02);
	// writeC(0x08);
	// writeC(0x06);
	// } else {
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// }
	//
	// if (warriorSkill_7) {
	// writeC(0x0a);
	// writeC(0x02);
	// writeC(0x08);
	// writeC(0x07);
	// } else {
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// }
	//
	// if (warriorSkill_8) {
	// writeC(0x0a);
	// writeC(0x02);
	// writeC(0x08);
	// writeC(0x08);
	// } else {
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// }
	//
	// if (warriorSkill_9) {// 熾烈鬥志
	// writeC(0x0a);
	// writeC(0x02);
	// writeC(0x08);
	// writeC(0x09);
	// } else {
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// writeC(0);
	// }
	//
	// writeC(0x00);
	// writeC(0x00);
	//
	// }

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}

	@Override
	public byte[] getContent() throws IOException {
		return _bao.toByteArray();
	}
}
