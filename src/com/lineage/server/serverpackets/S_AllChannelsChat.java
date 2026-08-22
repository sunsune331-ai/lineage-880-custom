package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

import william.L1WilliamSystemMessage;

/**
 * 全頻聊天字串(全頻廣播器)
 * @author user
 *
 */
public final class S_AllChannelsChat extends ServerBasePacket {
	
	private byte[] _byte = null;
  
	/**
	 * 全頻聊天字串(全頻廣播器)
	 * @param pc
	 * @param chat
	 * @param color
	 */
	public S_AllChannelsChat(L1PcInstance pc, String chat, int color) {
	    writeC(S_OPCODE_GLOBALCHAT);
	    writeC(18);
	    String message = String.format(L1WilliamSystemMessage.ShowMessage(887), new Object[] { pc.getName(), chat });
	    writeS(message);
	    writeH(color);
	}
  
	/**
	 * 全頻聊天字串(全頻廣播器)
	 * @param chat
	 * @param color
	 */
	public S_AllChannelsChat(String chat, int color) {
	    writeC(S_OPCODE_GLOBALCHAT);
	    writeC(18);
	    writeS(chat);
	    writeH(color);
	}
  
	@Override
	public String getType() {
	    return "[S] " + getClass().getSimpleName();
	}
  
	@Override
	public byte[] getContent() {
	    if (this._byte == null) {
	        this._byte = getBytes();
	    }
	    return this._byte;
	}
  
}
