package com.lineage.server.serverpackets;

import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE3;

import java.util.Random;

import com.lineage.config.Config;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件封包 - 本身
 * 
 * @author dexc
 */
public class S_OwnCharPack extends ServerBasePacket {
	// private static final int STATUS_POISON = 1;
	private static final int STATUS_INVISIBLE = 2;
	private static final int STATUS_PC = 4;
	// private static final int STATUS_FREEZE = 8;
	private static final int STATUS_BRAVE = 16;
	private static final int STATUS_ELFBRAVE = 32;
	private static final int STATUS_FASTMOVABLE = 64;
	private static final int STATUS_GHOST = 128;
	public static final Random _random = new Random();

	private byte[] _byte = null;

	/**
	 * 物件封包 - 本身
	 * 
	 * @param pc
	 */
	public S_OwnCharPack(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		if (Config.Lohuver > 0) {
			writeByte(S_ObjectPack181.own(pc));
			return;
		}
		int status = STATUS_PC;

		if (pc.isInvisble() || pc.isGmInvis()) {
			status |= STATUS_INVISIBLE;
		}

		if (pc.isBrave() // 勇敢藥水效果
				|| pc.isElfHURRICANE() // 精靈狂怒之風效果
		) {
			status |= STATUS_BRAVE;
		}

		if (pc.isSuperBrave()) { // 荒神加速
		    status |= pc.getBraveSpeed() << 4;
		}

		if (pc.isElfBrave() // 精靈餅乾效果
				|| pc.isElfFOCUS() // 精靈波濤之水效果
		) {
			// 場合、STATUS_BRAVESTATUS_ELFBRAVE立。
			// STATUS_ELFBRAVE效果無？
			status |= STATUS_BRAVE;
			status |= STATUS_ELFBRAVE;
		}

		if (pc.isFastMovable()) {
			status |= STATUS_FASTMOVABLE;
		}

		if (pc.isGhost()) {
			status |= STATUS_GHOST;
		}

		writeC(S_OPCODE_CHARPACK);
		writeH(pc.getX());
		writeH(pc.getY());
		writeD(pc.getId());

		if (pc.isDead()) {
			writeH(pc.getTempCharGfxAtDead());
		} else {
			writeH(pc.getTempCharGfx());
		}

		if (pc.isDead()) {
			writeC(pc.getStatus());
		} else {
			writeC(pc.getCurrentWeapon());
		}

		writeC(pc.getHeading());

		writeC(pc.getOwnLightSize());
		writeC(pc.getMoveSpeed());
		writeD((int) pc.getExp());
		writeH(pc.getLawful());

		final StringBuilder stringBuilder = new StringBuilder();// src009
		if (pc.get_other().get_color() != 0) {
			stringBuilder.append(pc.get_other().color());
		}

		// if (pc.isLawfulName() && pc.getLawful() >= 32767) {
		// stringBuilder.append("\\f=" + pc.getViewName());
		// } else {
		stringBuilder.append(pc.getViewName());
		// }

		writeS(stringBuilder.toString());
		writeS(pc.getTitle());
		writeC(status);
		writeD(pc.getClanid() > 0 ? pc.getClan().getEmblemId() : 0); // 盟徽編號
		writeS(pc.getClanname());
		writeS(null);
		writeC(pc.getClanRank() > 0 ? pc.getClanRank() << 4 : 0xb0); // 階級 * 16

		if (pc.isInParty()) { // 中
			writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
		} else {
			writeC(255);
		}
		if (pc.hasSkillEffect(STATUS_BRAVE3)) {
			writeC(8);
		} else {
			writeC(0);
		}
		writeC(0x00); // LV
		writeC(0x00);
		writeC(0xff);
		writeC(0xff);
		writeC(0);
		writeC(pc.getPolyStatus());
		writeC(0xFF);
		writeH(0);
	}

//	private void buildPacket(final L1PcInstance pc) {
//		writeC(S_OPCODE_EXTENDED_PROTOBUF);
//		writeC(119);
//		writeC(0);
//		writeC(8);
//		writeBit(pc.getX(), pc.getY());
//
//		writeC(16);
//		writeBit(pc.getId());
//
//		writeC(24);
//		if (pc.isDead()) {
//			writeBit(pc.getTempCharGfxAtDead());
//			// } else if ((pc.isPrivateShop()) && (pc.???? != 0)) {
//			// writeBit(pc.????);
//		} else {
//			writeBit(pc.getTempCharGfx());
//		}
//
//		writeC(32);
//		if (pc.isDead()) {
//			writeBit(pc.getStatus()); // getActionStatus
//		} else if (pc.isPrivateShop()) {
//			writeBit(70L);
//		} else if (pc.isFishing()) {
//			writeBit(71L);
//		} else {
//			writeBit(pc.getCurrentWeapon());
//		}
//
//		writeC(40);
//		writeC(pc.getHeading());
//
//		writeC(48);
//		writeBit(pc.getOwnLightSize());
//
//		writeC(56);
//
//		writeBit(1L);
//		writeC(64);
//		writeBit(pc.getLawful());
//
//		writeC(74);
//
//		// StringBuilder sb = new StringBuilder();
//		// sb.append(pc.getName());
//		// if (pc.getHuntCount() > 0) {
//		// sb.append(" \\f=");
//		// for (int i = 0; i < pc.getHuntCount(); i++) {
//		// sb.append("★");
//		// }
//		// }
//		// String name = sb.toString();
//		// writeC(name.getBytes().length);
//		// writeByte(name.getBytes());
//
//		final StringBuilder stringBuilder = new StringBuilder();// src009
//		if (pc.get_other().get_color() != 0) {
//			stringBuilder.append(pc.get_other().color());
//		}
//		stringBuilder.append(pc.getViewName());
//		final String name = stringBuilder.toString();
//		writeC(name.getBytes().length);
//		writeByte(name.getBytes());
//
//		writeC(82);
//		if (pc.getTitle().equals("")) {
//			writeC(0);
//		} else {
//			writeC(pc.getTitle().getBytes().length);
//			writeByte(pc.getTitle().getBytes());
//		}
//
//		writeC(88);
//		writeC((pc.isHaste()) ? 1 : 0);
//
//		writeC(96);
//		int brave = 0;
//
//		if (pc.isBrave()) { // 勇敢藥水效果
//			brave = 1;
//		} else if (pc.isElfBrave()) { // 精靈餅乾效果
//			brave = 3;
//		} else if (pc.isFastMovable()) { // 神聖疾走效果 行走加速效果 風之疾走效果 生命之樹果實效果
//			brave = 4;
//		} else if (pc.isSuperBrave()) { // 荒神加速效果
//			brave = 5;
//		} else if (pc.isElfHURRICANE()) { // 精靈狂怒之風效果
//			brave = 9;
//		} else if (pc.isElfFOCUS()) { // 精靈波濤之水效果
//			// if (pc.Focus_Speed == 0) {
//			// brave = 10;
//			// } else if (pc.Focus_Speed == 1) {
//			// brave = 11;
//			// } else if (pc.Focus_Speed == 2) {
//			// brave = 12;
//			// }
//			brave = 10;
//		}
//		writeC(brave);
//
//		writeC(104);
//		if (pc.hasSkillEffect(STATUS_BRAVE3)) {
//			writeC(8);
//		} else {
//			writeC(0);
//		}
//
//		writeC(112);
//		if (pc.isGhost()) {
//			writeC(1);
//		} else {
//			writeC(0);
//		}
//
//		writeC(120);
//		writeC(pc.getParalysis() != null ? 1 : 0);
//
//		writeBit(128L);
//		writeC(1);
//
//		writeBit(136L);
//		writeBit((pc.isInvisble()) || (pc.isGmInvis()) ? 1L : 0L);
//
//		writeBit(144L);
//		writeC(pc.getPoison() != null ? 1 : 0);
//
//		writeBit(152L);
//		writeBit(pc.getClanid() > 0 ? pc.getClan().getEmblemId() : 0L); // 盟徽編號
//
//		writeBit(162L);
//		if (pc.getClanname().equals("")) {
//			writeC(0);
//		} else {
//			writeC(pc.getClanname().getBytes().length);
//			writeByte(pc.getClanname().getBytes());
//		}
//
//		writeBit(170L);
//		writeBit(0L);
//
//		writeBit(176L);
//		writeC(0);
//
//		writeBit(184L);
//		if (pc.isInParty()) {
//			writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
//			// } else if (pc.get_DuelLine() != 0) {
//			// writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
//		} else {
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(1);
//		}
//
//		writeBit(192L);
//		writeC(0); // level
//
//		writeBit(202L);
//		if ((pc.getShopChat() == null) || (pc.getShopChat().length <= 0)) {
//			writeC(0);
//		} else {
//			writeC(pc.getShopChat().length);
//			writeByte(pc.getShopChat());
//		}
//
//		writeBit(208L);
//		writeC(255);
//		writeC(255);
//		writeC(255);
//		writeC(255);
//		writeC(255);
//		writeC(255);
//		writeC(255);
//		writeC(255);
//		writeC(255);
//		writeC(1);
//
//		writeBit(216L);
//		writeC(0);
//
//		writeBit(224L);
//		int value = 0;
//		if (pc.getLevel() >= 80) {
//			value = 11;
//		} else if (pc.getLevel() >= 55) {
//			value = (pc.getLevel() - 25) / 5;
//		} else if (pc.getLevel() >= 52) {
//			value = 5;
//		} else if (pc.getLevel() >= 50) {
//			value = 4;
//		} else if (pc.getLevel() >= 15) {
//			value = pc.getLevel() / 15;
//		}
//		writeBit(value);
//
//		writeBit(240L);
//		if (pc.isInParty()) {
//			writeC(100 * pc.getCurrentMp() / pc.getMaxMp());
//			// } else if (pc.get_DuelLine() != 0) {
//			// writeC(100 * pc.getCurrentMp() / pc.getMaxMp());
//		} else {
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(255);
//			writeC(1);
//		}
//
//		writeBit(256L);
//		writeC(0); // // server id
//
//		writeBit(264L);
//		writeC(0); // 遊戲徽章
//
//		writeH(0);
//	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
			// _byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return getClass().getSimpleName();
	}
}
