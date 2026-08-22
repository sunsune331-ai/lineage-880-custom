/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package com.lineage.server.serverpackets;

import java.io.IOException;

import com.lineage.config.Config;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.BinaryOutputStream;

/**
 * 隊伍系統
 * @author admin
 *
 */
public class S_Party extends ServerBasePacket {

	private byte[] _byte = null;
	
//	//[Client] opcode = 130 創立隊伍 隊長:超重音 隊員:超重低音 隊長發包部分
//	//0000: 82 00 f5 4b 28 00 00 00                            ...K(...
//	//======================================================================
//	//[Server] opcode = 51
//	//0000: 33 88 bb 26 00 00 24 04                            3..&..$.
//	//======================================================================
//	//[Server] opcode = 10
//	//0000: 0a 69 f5 4b 28 00 b6 57 ad ab a7 43 ad b5 00 72    .i.K(..W...C...r
//	//0010: 01 00 00 db 7f 12 80 67                            ......g
//	//======================================================================
//	//[Server] opcode = 17
//	//0000: 11 a8 01 01 b6 57 ad ab a7 43 ad b5 00 24 4e a3    .....W...C...$N.
//	//======================================================================
//	//[Server] opcode = 51
//	//0000: 33 f5 4b 28 00 64 5a 92                            3.K(.dZ.
//	//======================================================================
//	//[Server] opcode = 10
//	//0000: 0a 6e 02 88 bb 26 00 72 01 00 00 dc 7f 13 80 f5    .n...&.r.......
//	//0010: 4b 28 00 72 01 00 00 db 7f 12 80 e4 78 0f 02 14    K(.r.......x...
//	//======================================================================
//	
//	
//	//[Server] opcode = 70 創立隊伍  隊長:超重低音 隊員:超重音 隊員發包部分
//	//0000: 46 00 00 13 01 00 00 b9 03 b6 57 ad ab a7 43 ad    F.........W...C.
//	//0010: b5 00 28 00 c2 7f ea 7f                            ..(...
//	//======================================================================
//	//[Client] opcode = 129
//	//0000: 81 00 00 13 01 00 00 b9 03 01 00 00                ............
//	//======================================================================
//	//[Server] opcode = 51
//	//0000: 33 f5 4b 28 00 64 26 09                            3.K(.d&.
//	//======================================================================
//	//[Server] opcode = 17
//	//0000: 11 a8 01 01 b6 57 ad ab ad b5 00 cd 1c 00 00 0d    .....W..........
//	//======================================================================
//	//[Server] opcode = 51
//	//0000: 33 88 bb 26 00 00 00 7a                            3..&...z
//	//======================================================================
//	//[Server] opcode = 10
//	//0000: 0a 68 01 f5 4b 28 00 b6 57 ad ab a7 43 ad b5 00    .h..K(..W...C...
//	//0010: 64 72 01 00 00 db 7f 12 80 88 bb 26 00 b6 57 ad    dr........&..W.
//	//0020: ab ad b5 00 64 72 01 00 00 dc 7f 13 80 00 26 80    ....dr.......&.
//	//======================================================================
//	//[Server] opcode = 10
//	//0000: 0a 6e 02 f5 4b 28 00 72 01 00 00 db 7f 12 80 88    .n..K(.r.......
//	//0010: bb 26 00 72 01 00 00 dc 7f 13 80 16 00 03 08 00    .&.r...........
//	//======================================================================
//
//	/**
//	 * 隊伍系統
//	 * @param type
//	 * @param pc
//	 */
//	public S_Party(int type, L1PcInstance pc) {
//		switch (type) {
//		case S_PacketBox.PARTY_ADD_NEWMEMBER:
//			newMember(pc);
//			break;
//		case S_PacketBox.PARTY_OLD_MEMBER:
//			oldMember(pc);
//			break;
//		case S_PacketBox.PARTY_CHANGE_LEADER:
//			changeLeader(pc);
//		case S_PacketBox.PARTY_REFRESH:
//			refreshParty(pc);
//			break;
//		default:
//			break;
//		}
//	}
//
//	public S_Party(String htmlid, int objid) {
//		buildPacket(htmlid, objid, "", "", 0);
//	}
//
//	public S_Party(String htmlid, int objid, String partyname,
//			String partymembers) {
//		buildPacket(htmlid, objid, partyname, partymembers, 1);
//	}
//
//	public S_Party(int type, L1PcInstance pc, int live) {// 3.3C 組隊系統死亡更新
//		refreshName(pc, live);
//	}
//
//	private void buildPacket(String htmlid, int objid, String partyname,
//			String partymembers, int type) {
//		writeC(S_OPCODE_SHOWHTML);
//		writeD(objid);
//		writeS(htmlid);
//		writeH(type);
//		writeH(0x02);
//		writeS(partyname);
//		writeS(partymembers);
//	}
//
//	/**
//	 * 新加入隊伍的玩家
//	 * @param pc
//	 */
//	public void newMember(L1PcInstance pc) {
//		L1PcInstance leader = pc.getParty().getLeader();
//		L1PcInstance member[] = pc.getParty().getMembers();
//		//double nowhp = 0.0d;
//		//double maxhp = 0.0d;
//		if (pc.getParty() == null) {
//			return;
//		} else {
//			writeC(S_OPCODE_PACKETBOX);
//			writeC(S_PacketBox.PARTY_ADD_NEWMEMBER);// 0x68
//			//nowhp = leader.getCurrentHp();
//			//maxhp = leader.getMaxHp();
//			writeC(member.length - 1);
//			writeD(leader.getId());
//			writeS(leader.getName());
//			
//			//writeC((int) (nowhp / maxhp) * 100);
//			writeC(leader.getType());
//			writeC(0x00);
//			writeC(0x00);
//			
//			writeC((leader.getCurrentHp() * 100) / leader.getMaxHp());
//			writeC((leader.getCurrentMp() * 100) / leader.getMaxMp());
//			//writeC(100);
//			//writeC(100);
//		      
//			writeD(leader.getMapId());
//			writeH(leader.getX());
//			writeH(leader.getY());
//			//writeC(0);
//			writeC(0);
//			writeC(0);
//			writeC(0);
//			writeC(0);
//			writeC(1);
//		      
//			for (int i = 0, a = member.length; i < a; i++) {
//				if (member[i].getId() == leader.getId() || member[i] == null) {
//					continue;
//				}
//
//				//nowhp = member[i].getCurrentHp();
//				//maxhp = member[i].getMaxHp();
//				//writeD(i);//
//				
//				writeD(member[i].getId());
//				writeS(member[i].getName());
//				
//				writeC(member[i].getType());
//				writeC(0x00);// unknow
//				writeC(0x00);// unknow
//				
//				//writeC((int) (nowhp / maxhp) * 100);
//				writeC((member[i].getCurrentHp() * 100) / member[i].getMaxHp());
//				writeC((member[i].getCurrentMp() * 100) / member[i].getMaxMp());
//				//writeC(64);
//				//writeC(64);
//		        
//				writeD(member[i].getMapId());
//				writeH(member[i].getX());
//				writeH(member[i].getY());
//				//writeD(0);
//		        writeC(0);
//		        writeC(0);
//		        writeC(0);
//		        writeC(0);
//		        writeC(0);
//			}
//			//writeC(0x00);
//			writeC(0);
//		}
//	}
//
//	/**
//	 * 舊的隊員
//	 * 
//	 * @param pc
//	 */
//	//[Server] opcode = 10
//	//0000: 0a 69 f5 4b 28 00 b6 57 ad ab a7 43 ad b5 00 72    .i.K(..W...C...r
//	//0010: 01 00 00 db 7f 12 80 67                            ......g
//	//======================================================================
//	public void oldMember(L1PcInstance pc) {
//		writeC(S_OPCODE_PACKETBOX);
//		writeC(S_PacketBox.PARTY_OLD_MEMBER);
//		writeD(pc.getId());
//		writeS(pc.getName());
//		
//		writeC(pc.getType());
//		writeC(0x00);
//		writeC(0x00);
//		
//		writeD(pc.getMapId());
//		writeH(pc.getX());
//		writeH(pc.getY());
//	}
//
//	/**
//	 * 更換隊長
//	 * 
//	 * @param pc
//	 */
//	public void changeLeader(L1PcInstance pc) {
//		writeC(S_OPCODE_PACKETBOX);
//		writeC(S_PacketBox.PARTY_CHANGE_LEADER);
//		writeD(pc.getId());
//		writeH(0x0000);
//	}
//
//	/**
//	 * 更新隊伍
//	 * 
//	 * @param pc
//	 */
//	public void refreshParty(L1PcInstance pc) {
//		L1PcInstance member[] = pc.getParty().getMembers();
//		if (pc.getParty() == null) {
//			return;
//		} else {
//			writeC(S_OPCODE_PACKETBOX);
//			writeC(S_PacketBox.PARTY_REFRESH);
//			writeC(member.length);
//			for (int i = 0, a = member.length; i < a; i++) {
//				writeD(member[i].getId());
//				writeD(member[i].getMapId());
//				writeH(member[i].getX());
//				writeH(member[i].getY());
//			}
//			//writeC(0x00);
//		}
//	}
//
//	/**
//	 * 更新死亡隊員名稱UI顏色
//	 * 
//	 * @param pc
//	 */
//	//[Server] opcode = 10
//	//0000: 0a 6c ba 2b 3b 00 00 96                            .l.+;...
//	//[Server] opcode = 10
//	//0000: 0a 6c ba 2b 3b 00 01 15                            .l.+;...
//	public void refreshName(L1PcInstance pc, int live) {
//		writeC(S_OPCODE_PACKETBOX);
//		writeC(S_PacketBox.PARTY_DEATH_REFRESHNAME);
//		writeD(pc.getId());
//		writeC(live);
//	}

	// 8.8C組隊
	/** ????? ?? ?? **/
	public static final int JOIN_YN_OK = 1;
	public static final int MEMBER_TELEPORT = 2;
	public static final int MEMBER_HPMP_CHANGE = 3;
	public static final int MEMBER_MARK_CHANGE = 4;

	/** ??? ?? ?? **/
	public static final int PACKET_NONE = 0;
	public static final int PACKET_TYPE_NEW_MEMBER = 0x22;
	public static final int PACKET_TYPE_CHANGELEADER = 0x0a;
	public static final int PACKET_TYPE_LEAVE_MEMBER = 0x12;

	// SC_PARTY_MEMBER_MARK_CHANGE_NOTI
	// SC_PARTY_SPELL_AVATAR_NOTI

	/** ???? ?? ?? **/
	public static final int OPCODE_TYPE_PARTY_MEMBER_LIST = 823; // ????
	public static final int OPCODE_TYPE_PARTY_MEMBER_LIST_CHANGE = 824; // ??????
	public static final int OPCODE_TYPE_PARTY_MEMBER_STATUS = 825; // ???? ?? ?? ?? ??? ??? 
	public static final int OPCODE_TYPE_PARTY_OPERATION_RESULT_NOTI = 539; // ????/??? ??? 08 01 / ??? 08 02
	public static final int OPCODE_TYPE_PARTY_SYNC_PERIODIC_INFO = 827; //??? ????? ? //??? ???? ??

	public S_Party(final int type, final int sub_code, final L1PcInstance pc) {
		if (Config.Lohuver > 0) {
			writeByte(S_PartyPacket181.packet(type, sub_code, pc));
			return;
		}
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);

		switch (type) {
		case OPCODE_TYPE_PARTY_MEMBER_LIST_CHANGE:
			writeC(sub_code);
			switch (sub_code) {
			case PACKET_TYPE_LEAVE_MEMBER:
			case PACKET_TYPE_CHANGELEADER:
				writeBit(pc.getName().getBytes().length);
				writeByte(pc.getName().getBytes());
				break;
			case PACKET_TYPE_NEW_MEMBER:
				final byte[] char_info = sendPartyMemberPacket(pc);
				writeBit(char_info.length);
				writeByte(char_info);
				break;
			}
			break;
		case OPCODE_TYPE_PARTY_OPERATION_RESULT_NOTI:
			writeC(0x08);
			writeBit(sub_code == 0 ? 1 : 2);
			writeC(0x12);
			writeBit(pc.getName().getBytes().length);
			writeByte(pc.getName().getBytes());
			break;
		case OPCODE_TYPE_PARTY_SYNC_PERIODIC_INFO:
			for (final L1PcInstance member : pc.getParty().getMembers()) {
				final byte[] char_info = sendPartyMemberRefreshPacket(member);
				writeC(0x0a);
				writeBit(char_info.length);
				writeByte(char_info);
			}

			break;
		case OPCODE_TYPE_PARTY_MEMBER_LIST:
			writeC(0x0a);
			writeBit(pc.getParty().getLeader().getName().getBytes().length);
			writeByte(pc.getParty().getLeader().getName().getBytes());
			for (final L1PcInstance member : pc.getParty().getMembers()) {
				final byte[] char_info = sendPartyMemberPacket(member);
				writeC(0x12);
				writeBit(char_info.length);
				writeByte(char_info);
			}
			break;
		case OPCODE_TYPE_PARTY_MEMBER_STATUS:
			switch (sub_code) {
			case MEMBER_TELEPORT:
				writeC(0x0a);
				writeBit(pc.getName().getBytes().length);
				writeByte(pc.getName().getBytes());
				writeC(0x30);
				writeBit(pc.getMapId());
				writeC(0x38);
				writeBit(pc.getX(), pc.getY());
				break;
			case MEMBER_HPMP_CHANGE:
				writeC(0x0a);
				writeBit(pc.getName().getBytes().length);
				writeByte(pc.getName().getBytes());
				final int hpRatio = pc.getMaxHp() == 0 ? 0 : 100 * pc.getCurrentHp() / pc.getMaxHp();
				final int mpRatio = pc.getMaxMp() == 0 ? 0 : 100 * pc.getCurrentMp() / pc.getMaxMp();
				writeC(0x18);
				writeBit(hpRatio);
				writeC(0x20);
				writeBit(mpRatio);
				break;
			case MEMBER_MARK_CHANGE:
				writeC(0x0a);
				writeBit(pc.getName().getBytes().length);
				writeByte(pc.getName().getBytes());
				writeC(0x40);
				writeBit(pc.getPartySign());
				break;
			}
			break;
		}
		writeH(0);
	}

	public S_Party(final String htmlid, final int objid) {
		buildPacket(htmlid, objid, "", "", 0);
	}

	public S_Party(final String htmlid, final int objid, final String partyname, final String partymembers) {
		buildPacket(htmlid, objid, partyname, partymembers, 1);
	}

	private void buildPacket(final String htmlid, final int objid, final String partyname, final String partymembers,
			final int type) {
		writeC(S_OPCODE_SHOWHTML);
		writeD(objid);
		writeS(htmlid);
		writeH(type);
		writeH(0x02);
		writeS(partyname);
		writeS(partymembers);
	}

	private byte[] sendPartyMemberRefreshPacket(final L1PcInstance pc) {
		final BinaryOutputStream os = new BinaryOutputStream();

		os.writeC(0x0a);
		os.writeBit(pc.getName().getBytes().length);
		os.writeByte(pc.getName().getBytes());

		os.writeC(0x30);
		os.writeBit(pc.getMapId());

		os.writeC(0x38);
		os.writeBit(pc.getX(), pc.getY());

		try {
			os.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
	}

	private byte[] sendPartyMemberPacket(final L1PcInstance pc) {
		final BinaryOutputStream os = new BinaryOutputStream();

		os.writeC(0x0a);
		os.writeBit(pc.getName().getBytes().length);
		os.writeByte(pc.getName().getBytes());

		os.writeC(0x10);
		os.writeBit(pc.getId());

		os.writeC(0x18);
		os.writeBit(pc.getId());

		os.writeC(0x20);
		os.writeBit(pc.getType());

		os.writeC(0x28);
		os.writeBit(pc.get_sex());

		final int hpRatio = pc.getMaxHp() == 0 ? 0 : 100 * pc.getCurrentHp() / pc.getMaxHp();
		final int mpRatio = pc.getMaxMp() == 0 ? 0 : 100 * pc.getCurrentMp() / pc.getMaxMp();

		os.writeC(0x30);
		os.writeBit(hpRatio);

		os.writeC(0x38);
		os.writeBit(mpRatio);

		os.writeC(0x40);
		os.writeBit(pc.getMapId());

		os.writeC(0x48);
		os.writeBit(pc.getX(), pc.getY());

		os.writeC(0x50); // set_party_mark
		os.writeBit(pc.getPartySign());

		os.writeC(0x60); // set_server_no
		os.writeBit(0x64);

		try {
			os.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
	}

	public S_Party(final String name) {
		if (Config.Lohuver > 0) {
			writeByte(S_PartyPacket181.operationResult(name));
			return;
		}
		this.writeC(S_EXTENDED_PROTOBUF);
		this.writeH(539);
		this.writeInt32(1, 2);// ??
		this.writeString(2, name);// 名稱
		this.randomShort();
	}

	public S_Party(final int v1, final int v2) {
		if (Config.Lohuver > 0) {
			writeByte(S_PartyPacket181.mark(v1, v2));
			return;
		}
		this.writeC(S_EXTENDED_PROTOBUF);
		this.writeH(339);
		this.writeInt32(1, v1);// ??
		this.writeInt32(2, v2);// ??
		this.randomShort();
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
		return "[S] " + this.getClass().getSimpleName();
	}

}
