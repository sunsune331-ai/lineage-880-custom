package com.lineage.server.serverpackets;

/**
 * 毒麻痺負面效果圖示
 */
public class S_SkillIconPoison extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * 毒麻痺負面效果圖示
	 * 
	 * @param type 1 中毒狀態。 (一般毒)
	 * @param type 2 身體要被麻痺了 (麻痺毒)
	 * @param type 6 突然感覺到混亂。 (卡毒)
	 * @param time 效果時間
	 */
	public S_SkillIconPoison(final int type, final int time) {
		writeC(S_EVENT);
		writeC(S_PacketBox.POISON_ICON);
		writeC(type);
		// 2麻痺
		// if (type >= 2) {
		if (type == 2) {// 麻痺毒額外判斷
			// System.out.println("type"+type);
			writeH(0x0000);// 這邊如果是麻痺毒的話這邊就是完全麻痺的時間
			writeC(time);// 前置時間
			writeC(0x00);
			// 中毒
		} else {
			writeH(time);
			writeC(0x00);
			writeC(0x00);
		}
		// System.out.println("type:"+type+" time:"+time);
	}

	public S_SkillIconPoison(final int type, final int n, final int time) {
		writeC(S_EVENT);
		writeC(S_PacketBox.POISON_ICON);
		writeC(type);
		if (type >= 2) {
			// System.out.println("type"+type);
			writeH(0x0000);
			writeC(time);
			writeC(0x00);
		} else {
			writeH(time);
			writeC(0x00);
			writeC(0x00);
		}
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
