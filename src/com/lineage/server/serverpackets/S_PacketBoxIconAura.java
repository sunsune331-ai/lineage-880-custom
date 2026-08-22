package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

public class S_PacketBoxIconAura extends ServerBasePacket {
	private byte[] _byte = null;
	public static final int ICON_AURA = 22;
	public static final int ICON_OS = 125;
	public static final int ICON_E3 = 227;
	public static final int TYPE_CURSE = 221; // 炎魔/火焰之影的烙印

	public S_PacketBoxIconAura(int iconid, int time) {
		writeC(S_EVENT);
		writeC(ICON_AURA);
		writeC(iconid);
		writeH(time);
	}

	public S_PacketBoxIconAura(int type, int time, int value, int test) {
		writeC(S_EVENT);
		writeC(ICON_AURA);
		writeC(type);
		writeH(time);
		writeH(test);
		writeH(value);
	}

	/**
	 * 鋼鐵士氣
	 * 
	 * @param type
	 * @param time
	 * @param pc
	 */
	public S_PacketBoxIconAura(int type, int time, L1PcInstance pc) {
		writeC(S_EVENT);
		writeC(ICON_AURA);
		writeC(type);
		writeH(time);
		writeD(pc.getAc()); // ac
	}

	/**
	 * 火焰之影 炎魔攻擊圖示
	 * @param iconid
	 * @param time
	 * @param type
	 */
	public S_PacketBoxIconAura(final int iconid, final int time, final int type) {
		writeC(S_EVENT);
		writeC(ICON_AURA);
		writeC(iconid); //221
		writeH(time); //time
		writeC(type); //1:炎魔 2:火焰之影
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return getClass().getSimpleName();
	}
}
