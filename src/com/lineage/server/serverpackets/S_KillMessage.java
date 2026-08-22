package com.lineage.server.serverpackets;

import java.util.Random;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigKill;

/**
 * 殺人公告
 * 
 * @author dexc
 */
public class S_KillMessage extends ServerBasePacket {
	
	private byte[] _byte = null;

	private static final Random _random = new Random();

	/**
	 * 殺人公告
	 * 
	 * @param winName
	 * @param deathName
	 */
	public S_KillMessage(String winName, String deathName) {
        if (ConfigAlt.KILL_BROAD_SCREEN) {
            writeC(S_MESSAGE); // 螢幕
            writeC(84);
            writeC(2);
    		String x1 = (String) ConfigKill.KILL_TEXT_LIST.get(Integer.valueOf(_random.nextInt(ConfigKill.KILL_TEXT_LIST.size()) + 1));
    		writeS(String.format(x1, new Object[] { winName, deathName }));
        } else {
			writeC(S_MESSAGE); // 聊天
    		writeC(0);
    		writeD(0);
    		String x1 = (String) ConfigKill.KILL_TEXT_LIST.get(Integer.valueOf(_random.nextInt(ConfigKill.KILL_TEXT_LIST.size()) + 1));
    		writeS(String.format(x1, new Object[] { winName, deathName }));
        }
	}
	/**
	 * 取物品文字
	 * @param winName
	 * @param deathName
	 */
	public S_KillMessage(final String deathName) {
		//this.writeC(S_OPCODE_NPCSHOUT);
		//this.writeD(0x00000000);
		//殺人公告使用系統顏色字
		this.writeC(S_OPCODE_GLOBALCHAT);
		//this.writeC(0x09);// 顏色
		this.writeC(0x0b);
		//殺人公告使用系統顏色字
		this.writeS(deathName);
  }
	/**
	 * 賭場NPC對話
	 * 
	 * @param winName
	 * @param deathName
	 */
	public S_KillMessage(String name, String msg, int i) {
		writeC(S_MESSAGE);
		writeC(0);
		writeD(0);
		writeS(" \\fY[" + name + "] " + msg);
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
