package com.lineage.server.serverpackets;

import com.lineage.config.Config;
import com.lineage.data.protobuf.PBMessageALL9;

/**
 * 伺服器版本 8.8C
 * @author dexc
 */
public class S_ServerVersion extends ServerBasePacket {

	private static final int CLIENT_LANGUAGE = Config.CLIENT_LANGUAGE;

	public S_ServerVersion() {
		// Srwh的PBMessageALL9
		writeC(S_EXTENDED_PROTOBUF);
		writeH(821); // SERVER_VERSION
		final PBMessageALL9.typeVersion.Builder builder = PBMessageALL9.typeVersion.newBuilder();
		builder.setValue1(0);
		builder.setValue2(Config.SERVERNO); // Server Id=
		builder.setValue3(1712292002); // server version
		builder.setValue4(1712292002); // cache version
		builder.setValue5(2015090301); // auth version
		builder.setValue6(1712292002); // npc version
		final int currentTime = (int) (System.currentTimeMillis() / 1000L);
		builder.setValue7(currentTime - 1);
		builder.setValue8(0);
		builder.setValue9(CLIENT_LANGUAGE); // Country: 0.US 3.Taiwan 4.Janpan 5.China
		builder.setValue10(0x7cff7d82);// Server Type =
		builder.setValue11(currentTime - 1);
		builder.setValue12(150316700); // global cache version
		builder.setValue13(150204901); // TAM version
		builder.setValue14(151118701); // arca version
		builder.setValue15(1710161002); // 
		builder.setValue16(30249014);
		builder.setValue17(3); // 
		builder.setValue18(currentTime - Config.Lohuver);
		writeByte(builder.build().toByteArray());
		writeH(0);

		// 韓
		// writeC(S_OPCODE_EXTENDED_PROTOBUF);
		// writeH(821); // SERVER_VERSION
		// writeH(8);
		// writeH(2320);
		// writeC(24);
		// writeBit(1901142505);
		// writeC(32);
		// writeBit(1901142505);
		// writeC(40);
		// writeBit(2015090301);
		// writeC(48);
		// writeBit(1901142505);
		// writeC(56);
		// writeBit(GameServer.getInstance().startTime);
		//
		// // writeH(64);
		// // writeH(72);
		// writeC(64);
		// writeBit(0);
		// writeC(72);
		// writeBit(CLIENT_LANGUAGE);
		//
		// writeC(80);
		// writeBit(getSetting()); // Server Type
		// writeC(88);
		// writeBit(UPTIME);
		// writeC(96);
		// writeBit(151112700);
		// writeC(104);
		// writeBit(161031701);
		// writeC(112);
		// writeBit(1712141302);
		// writeC(120);
		// writeBit(1712181002);
		// writeBit(128);
		// writeBit(1707111002);
		// writeBit(136);
		// writeBit(0);
		// writeH(0);

		// XXX add 8.8C
		/*byte[] data = new byte[] {
				(byte) S_OPCODE_EXTENDED_PROTOBUF,
				(byte) 0x35, (byte) 0x03,
				(byte) 0x08, (byte) 0x00,
				(byte) 0x10, (byte) 0x02,
				(byte) 0x18, (byte) 0xF1, (byte) 0xC3, (byte) 0xA6, (byte) 0xDC, (byte) 0x06,
				(byte) 0x20, (byte) 0xF1, (byte) 0xC3, (byte) 0xA6, (byte) 0xDC, (byte) 0x06,
				(byte) 0x28, (byte) 0xFD, (byte) 0xAC, (byte) 0xEF, (byte) 0xC0, (byte) 0x07,
				(byte) 0x30, (byte) 0xF1, (byte) 0xC3, (byte) 0xA6, (byte) 0xDC, (byte) 0x06,
				(byte) 0x38, (byte) 0xB4, (byte) 0xCD, (byte) 0xFA, (byte) 0xD6, (byte) 0x05,
				(byte) 0x40, (byte) 0x00,
				(byte) 0x48, (byte) CLIENT_LANGUAGE,

				//(byte) 0x50, (byte) 0xC2, (byte) 0xFB, (byte) 0xFD, (byte) 0xE7, (byte) 0x03,
				// 50 82 f3 fd e7 07 免服(經驗表/商城/抽抽樂)
				(byte) 0x50, (byte) 0x82, (byte) 0xF3, (byte) 0xFD, (byte) 0xE7, (byte) 0x07,

				(byte) 0x58, (byte) 0xC9, (byte) 0xD8, (byte) 0xFD, (byte) 0xD6, (byte) 0x05,
				(byte) 0x60, (byte) 0x9C, (byte) 0xCD, (byte) 0xD6, (byte) 0x47,
				(byte) 0x68, (byte) 0x00,
				(byte) 0x70, (byte) 0x00,
				(byte) 0x78, (byte) 0xEA, (byte) 0xF8, (byte) 0xBB, (byte) 0xAF, (byte) 0x06,
				(byte) 0x80, (byte) 0x01, (byte) 0x04,
				(byte) 0x88, (byte) 0x01, (byte) 0x03, (byte) 0xE4, (byte) 0x7F };
		for (int i = 0; i < data.length; i++) {
			writeC(data[i]);
		}*/
	}

	private int getSetting() {
		final boolean[] setting = { // 2的x次方
				false, // 0----------
				true, // 1* =是否載入.tbt?
				false, // 2 =在夢中不會受到死亡懲罰
				false, // 3----------
				false, // 4----------
				false, // 5----------
				false, // 6 =穿怪
				true, // 7*
				true, // 8*
				false, // 9----------
				false, // 10 =倉庫密碼加密
				true, // 11*
				true, // 12*
				true, // 13*
				true, // 14*
				false, // 15 =無法創新角色
				true, // 16*
				true, // 17*
				true, // 18*
				true, // 19*
				true, // 20*
				true, // 21*
				true, // 22*
				true, // 23* =連續攻擊
				false, // 24----------
				false, // 25----------
				true, // 26*
				true, // 27 =右鍵鎖定
				true, // 28*
				true, // 29*
				true, // 30 =免服(經驗表/商城/抽抽樂)
		};
		int val = 0;
		for (int i = 0; i < setting.length; i++) {
			if (setting[i]) {
				val |= 1 << i; // 2^i
			}
		}
		return val;
	}

	@Override
	public byte[] getContent() {
		return _bao.toByteArray();
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}
}
