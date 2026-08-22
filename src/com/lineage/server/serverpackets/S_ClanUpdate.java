package com.lineage.server.serverpackets;

import com.google.protobuf.ByteString;

import l1j.server.server.datas.protobuf.PBMessageALL5;

/**
 * 更新血盟數據
 * 
 * @author KZK
 */
public class S_ClanUpdate extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * 更新血盟數據(加入 創立)
	 * 
	 * @param pc [Server] opcode = 97 0000: 61 c2 6b b1 00 a4 d1 b0 f3 31 00 00
	 *            00 00 00 00 a.k......1...... 0010: 07 25 cb 44 06 5a b4 3a
	 *            .%.D.Z.:
	 */
	public S_ClanUpdate(final int objid, final String Clanname, final int rank) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(537);
		final PBMessageALL5.type17.Builder builder = PBMessageALL5.type17.newBuilder();
		builder.setArray1(ByteString.copyFromUtf8(Clanname));
		builder.setValue2(rank);
		writeByte(builder.build().toByteArray());
		writeH(0);
	}

	/**
	 * 更新血盟數據(驅逐退出解散血盟)
	 * 
	 * @param objid
	 */
	public S_ClanUpdate(final int objid) {
		writeC(S_PLEDGE);
		writeD(objid);
		writeS("");
		writeD(0);
		writeC(0);
		writeC(0x0b);
		writeH(0);
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
		return this.getClass().getSimpleName();
	}
}
