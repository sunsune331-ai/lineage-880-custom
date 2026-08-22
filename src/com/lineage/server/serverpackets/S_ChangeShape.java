package com.lineage.server.serverpackets;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件外型改變
 * 
 * @author dexc
 * 
 */
public class S_ChangeShape extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * 物件外型改變
	 * 
	 * @param obj
	 * @param polyId
	 */
	public S_ChangeShape(final L1Character obj, final int polyId) {
		this.buildPacket181(obj, polyId, currentWeapon(obj));
	}

	/**
	 * 物件外型改變
	 * 
	 * @param obj
	 * @param polyId
	 * @param weaponTakeoff
	 */
	public S_ChangeShape(final L1Character obj, final int polyId, final boolean weaponTakeoff) {
		this.buildPacket181(obj, polyId, weaponTakeoff ? 0 : currentWeapon(obj));
	}

	private static int currentWeapon(final L1Character obj) {
		if (obj instanceof L1PcInstance) {
			return ((L1PcInstance) obj).getCurrentWeapon();
		}
		return 0;
	}

	private void buildPacket181(final L1Character obj, final int polyId, final int currentWeapon) {
		this.writeC(S_OPCODE_POLY);
		this.writeD(obj.getId());
		this.writeH(polyId);
		// 何故29不明
		this.writeC(currentWeapon);
		this.writeH(0xffff);
		this.writeC(0);
		this.writeH(0);
	}

	/**
	 * NPC改變外型(寵物 迷魅使用)
	 * 
	 * @param pc
	 *            執行命令PC
	 * @param npc
	 *            執行命令NPC
	 * @param polyId
	 *            代號
	 */
	public S_ChangeShape(final L1PcInstance pc, final L1NpcInstance npc, final int polyId) {
		this.buildPacket181(npc, polyId, 0);
	}

	// @Override
	public byte[] getContent() {
		if (this._byte == null) {
			this._byte = this.getBytes();
		}
		return this._byte;
	}
}
