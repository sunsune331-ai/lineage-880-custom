package com.lineage.server.serverpackets;

/**
 * 魔法效果:精準目標
 * @author DaiEn
 */
public class S_TrueTarget extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * 魔法效果:精準目標
	 * @param targetId 目標OBJID
	 * @param objectId 施展者OBJID
	 * @param message 附加訊息
	 */
	public S_TrueTarget(final int targetId, final int objectId, final String message) {
		this.buildPacket(targetId, objectId, message);
	}

	private void buildPacket(final int targetId, final int objectId, final String message) {
		this.writeC(S_OPCODE_TRUETARGET);
		this.writeD(targetId);
		this.writeD(objectId);
		this.writeS(message);
	}

	public S_TrueTarget(final int targetId, final int gfxid) {
		this.writeC(S_OPCODE_TRUETARGET);
		this.writeD(targetId);
		this.writeD(targetId);
		this.writeS(null);
		this.writeH(gfxid);
	}

	/**
	 * 任務怪物顯示特效
	 * @param targetObjId
	 * @param gfxid
	 * @param type
	 */
	public S_TrueTarget(int targetObjId, int gfxid, int type) {
		writeC(S_EVENT);
		writeC(194);
		writeD(targetObjId);
		writeD(gfxid);
		writeD(type);
		writeH(0x00);
	}

	/**
	 * 精準目標 -> 新
	 * @param targetId
	 * @param isEffect
	 */
	public S_TrueTarget(int targetId, boolean isEffect) {
		writeC(S_EVENT);
		writeC(194);
		writeD(targetId);

		// writeD(12299); // gfxid= 12299
		// writeD(isEffect ? 1 : 0);
		// writeH(0x00);
		// 8.8C修改
		writeH(13135);
		writeH(0x00);
		writeD(isEffect ? 1 : 0);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (this._byte == null) {
			this._byte = this.getBytes();
		}
		return this._byte;
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}
}
