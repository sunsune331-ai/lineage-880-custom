package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_ACTION_UI extends ServerBasePacket {
	{ suppressForProtocol181(); }

	private byte[] _byte = null;

	public static final int SAFETYZONE = 0xcf; // 安全區域右下顯示死亡懲罰狀態圖示

	public static final int DUNGEON_TIME = 0x23; // 顯示計時地圖剩餘時間

	public static final int RESIST = 503; // [技術/精靈/龍屬/恐怖]命中or耐性

	private static final String S_ACTION_UI = "S_ACTION_UI";

	/**
	 * @param 安全區域右下顯示死亡懲罰狀態圖示
	 * @param isOpen
	 *            on/off
	 **/
	public S_ACTION_UI(int code, boolean isOpen) {
		writeC(S_EXTENDED_PROTOBUF);
		writeC(code);
		switch (code) {
		case SAFETYZONE: {
			writeC(0x01);
			writeC(0x08);
			write7B(isOpen ? 128 : 0);
			writeC(0x10);
			writeC(0x00);
			writeC(0x18);
			writeC(0x00);
			writeH(0);
			break;
		}
		}
	}

	/**
	 * [技術/精靈/龍屬/恐怖]四大屬性<br>
	 * 0x12=命中：1技術，2精靈，3龍屬，4恐怖，5全部<br>
	 * 0x0a=耐性：1技術，2精靈，3龍屬，4恐怖，5全部<br>
	 * @param pc
	 * @param subCode
	 * @param type
	 * @param resistType
	 */
	public S_ACTION_UI(L1PcInstance pc, int subCode, int type, int resistType) {
		writeC(S_EXTENDED_PROTOBUF);
		writeBit(subCode);
		switch (subCode) {
		case RESIST: // [技術/精靈/龍屬/恐怖]命中or耐性
			writeC(type); // 0x0a=耐性, 0x12=命中
			writeC(0x04);
			if (type == 0x0a) { // 耐性
				writeC(0x08);
				writeC(resistType); // 耐性：1技術，2精靈，3龍屬，4恐怖，5全部
				switch (resistType) {
				case 1:
					writeC(0x10);
					writeBit(pc.getRegistTechnology()); // 技術耐性
					break;
				case 2:
					writeC(0x10);
					writeBit(pc.getRegistElf()); // 精靈耐性
					break;
				case 3:
					writeC(0x10);
					writeBit(pc.getRegistDragon()); // 龍屬耐性
					break;
				case 4:
					writeC(0x10);
					writeBit(pc.getRegistHorror()); // 恐怖耐性
					break;
				case 5:
					writeC(0x10);
					writeBit(pc.getRegistAll()); // 全部耐性
					break;
				}
			} else if (type == 0x12) { // 命中
				writeC(0x08);
				writeC(resistType); // 命中：1技術，2精靈，3龍屬，4恐怖，5全部

				switch (resistType) {
				case 1:
					writeC(0x10);
					writeBit(pc.getHitTechnology()); // 技術命中
					break;
				case 2:
					writeC(0x10);
					writeBit(pc.getHitElf()); // 精靈命中
					break;
				case 3:
					writeC(0x10);
					writeBit(pc.getHitDragon()); // 龍屬命中
					break;
				case 4:
					writeC(0x10);
					writeBit(pc.getHitHorror()); // 恐怖命中
					break;
				case 5:
					writeC(0x10);
					writeBit(pc.getHitAll()); // 全部命中
					break;
				}
			}
			break;
		}
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_ACTION_UI;
	}
}
