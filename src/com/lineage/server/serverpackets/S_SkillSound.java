package com.lineage.server.serverpackets;

public class S_SkillSound extends ServerBasePacket {
	private byte[] _byte = null;

	public S_SkillSound(int objid, int gfxid) {
		buildPacket(objid, gfxid);
	}

	private void buildPacket(int objid, int gfxid) {
		writeC(S_OPCODE_SKILLSOUNDGFX);
		writeD(objid);
		writeH(gfxid);
	}

	public S_SkillSound(int objid, int gfxid, int time) {
		writeC(S_OPCODE_SKILLSOUNDGFX);
		writeD(objid);
		writeH(gfxid);
		writeH(time);
	}

	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return getClass().getSimpleName();
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.serverpackets.S_SkillSound JD-Core Version: 0.6.2
 */
