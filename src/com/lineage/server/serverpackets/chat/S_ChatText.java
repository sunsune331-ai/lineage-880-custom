package com.lineage.server.serverpackets.chat;

import com.lineage.config.Config;
import com.lineage.server.Controller.UserRankingController;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.templates.L1UserRanking;

/**
 * 聊天頻道字串(7.6ADD)
 * @author kyo
 *
 */
public class S_ChatText extends ServerBasePacket {
	
	private byte[] _byte = null;
	
	/**
	 * 聊天頻道字串
	 * @param chat_time
	 * @param chat_type
	 * @param chat_text
	 * @param chat_name
	 * @param server_id
	 * @param chat_objid
	 * @param chat_locX
	 * @param chat_locY
	 */
	public S_ChatText(final int chat_time, final int chat_type, final String chat_text, final String chat_name, final int server_id, int chat_objid, int chat_locX, int chat_locY) {
		this.writeC(S_EXTENDED_PROTOBUF);
		this.writeH(0x0204);
		this.writeInt32(1, chat_time);
		this.writeInt32(2, chat_type);
		this.writeString(3, chat_text);
		this.writeString(5, chat_name);
		this.writeInt32(6, server_id);
		this.writeInt32(7, chat_objid);
		this.writeInt32(8, chat_locX);
		this.writeInt32(9, chat_locY);

		// 新排行系統
		if (Config.UserRanking) {
			L1UserRanking rank = UserRankingController.getInstance().getTotalRank(chat_name);
			if (rank != null) {
				int uRank = rank.getCurRank();
				int number = 0;
				if (uRank >= 1 && uRank <= 10) {
					number = 11;
				} else if (uRank >= 11 && uRank <= 20) {
					number = 10;
				} else if (uRank >= 21 && uRank <= 40) {
					number = 9;
				} else if (uRank >= 41 && uRank <= 60) {
					number = 8;
				} else if (uRank >= 61 && uRank <= 80) {
					number = 7;
				} else if (uRank >= 81 && uRank <= 100) {
					number = 6;
				} else if (uRank >= 101 && uRank <= 120) {
					number = 5;
				} else if (uRank >= 121 && uRank <= 140) {
					number = 4;
				} else if (uRank >= 141 && uRank <= 160) {
					number = 3;
				} else if (uRank >= 161 && uRank <= 180) {
					number = 2;
				} else if (uRank >= 181 && uRank <= 200) {
					number = 1;
				}
				this.writeInt32(10, number);
			}
		}

		this.randomShort();
	}
	
	/**
	 * 聊天頻道字串
	 * @param chat_time
	 * @param chat_type
	 * @param chat_text
	 * @param chat_name
	 * @param server_id
	 */
	public S_ChatText(final int chat_time, final int chat_type, final String chat_text, final String chat_name, final int server_id) {
		this.writeC(S_EXTENDED_PROTOBUF);
		this.writeH(0x0204);
		this.writeInt32(1, chat_time);
		this.writeInt32(2, chat_type);
		this.writeString(3, chat_text);
		this.writeString(5, chat_name);
		this.writeInt32(6, server_id);

		// 新排行系統
		if (Config.UserRanking) {
			L1UserRanking rank = UserRankingController.getInstance().getTotalRank(chat_name);
			if (rank != null) {
				int uRank = rank.getCurRank();
				int number = 0;
				if (uRank >= 1 && uRank <= 10) {
					number = 11;
				} else if (uRank >= 11 && uRank <= 20) {
					number = 10;
				} else if (uRank >= 21 && uRank <= 40) {
					number = 9;
				} else if (uRank >= 41 && uRank <= 60) {
					number = 8;
				} else if (uRank >= 61 && uRank <= 80) {
					number = 7;
				} else if (uRank >= 81 && uRank <= 100) {
					number = 6;
				} else if (uRank >= 101 && uRank <= 120) {
					number = 5;
				} else if (uRank >= 121 && uRank <= 140) {
					number = 4;
				} else if (uRank >= 141 && uRank <= 160) {
					number = 3;
				} else if (uRank >= 161 && uRank <= 180) {
					number = 2;
				} else if (uRank >= 181 && uRank <= 200) {
					number = 1;
				}
				this.writeInt32(10, number);
			}
		}

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
