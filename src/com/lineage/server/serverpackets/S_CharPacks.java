package com.lineage.server.serverpackets;

/**
 * 角色資訊
 * 
 * @author dexc
 */
public class S_CharPacks extends ServerBasePacket {

	private byte[] _byte = null;

	public S_CharPacks(String name, String clanName, int type, int sex, int lawful, int hp, int mp, int ac, int lv,
			int str, int dex, int con, int wis, int cha, int intel, int time) {
		this(name, clanName, type, sex, lawful, hp, mp, ac, lv, str, dex, con, wis, cha, intel, time, 0);
	}

	/**
	 * 角色資訊
	 * 
	 * @param name
	 * @param clanName
	 * @param type
	 * @param sex
	 * @param lawful
	 * @param hp
	 * @param mp
	 * @param ac
	 * @param lv
	 * @param str
	 * @param dex
	 * @param con
	 * @param wis
	 * @param cha
	 * @param intel
	 * @param time
	 */
	public S_CharPacks(String name, String clanName, int type, int sex, int lawful, int hp, int mp, int ac, int lv,
			int str, int dex, int con, int wis, int cha, int intel, int time, int accessLevel) {
		writeC(S_CHARACTER_INFO);
		writeS(name);
		writeS(clanName);
		writeC(type);
		writeC(sex);
		writeH(lawful);
		writeH(hp);
		writeH(mp);

		if (ac > 10) {
			writeC(10);
		} else {
			writeC(ac);
		}

		if (lv > 127)
			writeC(127);
		else {
			writeC(lv);
		}

		if (str > 127)
			writeC(127);
		else {
			writeC(str);
		}
		if (dex > 127)
			writeC(127);
		else {
			writeC(dex);
		}
		if (con > 127)
			writeC(127);
		else {
			writeC(con);
		}
		if (wis > 127)
			writeC(127);
		else {
			writeC(wis);
		}
		if (cha > 127)
			writeC(127);
		else {
			writeC(cha);
		}
		if (intel > 127)
			writeC(127);
		else {
			writeC(intel);
		}

		writeC(lv >= 55 ? accessLevel : 0);
		writeD(time);

		int checkcode = Math.min(lv, 127) ^ str ^ dex ^ con ^ wis ^ cha ^ intel;
		writeC(checkcode & 0xFF);
		writeD(0);
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
		return getClass().getSimpleName();
	}

}
