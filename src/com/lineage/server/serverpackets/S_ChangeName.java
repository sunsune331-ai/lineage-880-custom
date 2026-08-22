package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 物件名稱改變
 * 
 * @author dexc
 */
public class S_ChangeName extends ServerBasePacket {
	
	private byte[] _byte = null;

	/**
	 * 物件名稱改變
	 * 
	 * @param objectId
	 * @param name
	 */
	public S_ChangeName(int objectId, String name) {
		writeC(S_OPCODE_CHANGENAME);
		writeD(objectId);
		writeS(name);
	}

	/**
	 * 物件名稱改變(GM)
	 * 
	 * @param objectId
	 * @param name
	 * @param mode
	 */
	public S_ChangeName(int objectId, String name, int mode) {
		String color = "";
		switch (mode) {
		case 0:
			color = "\\f=\\f1";
			break;
		case 1:
			color = "\\f=\\f2";
			break;
		case 2:
			color = "\\f=\\f3";
			break;
		case 3:
			color = "\\f=\\f4";
			break;
		case 4:
			color = "\\f=\\f5";
			break;
		case 5:
			color = "\\f=\\f6";
			break;
		case 6:
			color = "\\f=\\f7";
			break;
		case 7:
			color = "\\f=\\f8";
			break;
		case 8:
			color = "\\f=\\f9";
			break;
		case 9:
			color = "\\f=\\f=";
			break;
		case 10:
			color = "\\f=\\f<";
			break;
		}

		writeC(S_OPCODE_CHANGENAME);
		writeD(objectId);
		writeS(color + "GM \\f=" + name);
	}

	/**
	 * 物件名稱改變(顏色)
	 * 
	 * @param pc 執行人物
	 * @param isName 執行爵位顏色改變 true:執行 false:不執行
	 */
	public S_ChangeName(L1PcInstance pc, boolean isName) {
		writeC(S_OPCODE_CHANGENAME);
		writeD(pc.getId());

		StringBuilder stringBuilder = new StringBuilder();
		if (isName) {
			if (pc.get_other().get_color() != 0) {
				stringBuilder.append(pc.get_other().color());
			}
		}
		stringBuilder.append(pc.getViewName());
		writeS(stringBuilder.toString());
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
