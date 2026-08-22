package com.lineage.server.serverpackets;

import java.util.ArrayList;

import com.lineage.config.Config;
import com.lineage.server.datatables.AttenDanceTable;
import com.lineage.server.datatables.AttenDanceTable.Attendtemp;
import com.lineage.server.datatables.CharacterAttendTable.idTemp;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.BinaryOutputStream;

/**
 * 官方簽到系統
 */
public class S_AttenDance extends ServerBasePacket {
	{ suppressForProtocol181(); }
	private static final String S_AttenDance = "[S] S_AttenDance";
	private byte[] _byte = null;

	public static final int WatchCreate = 0x20;
	public static final int WatchPcPorfile = 0x21;
	public static final int WatchStoreItem = 0xef;
	public static final int WatchItemList = 0x24;

	// 13 23 02 08 01 10 02 18 00 00 d9
	public S_AttenDance(int subCode, int type, int id) {
		writeC(S_EXTENDED_PROTOBUF);
		writeC(subCode);
		switch (subCode) {
		case WatchStoreItem:
			writeC(0x03);
			writeC(0x08);
			writeC(id);
			writeC(0x10);
			writeC(type);
			writeC(0x18);
			writeC(0x03);
			break;
		}
		writeH(0);
	}

	public S_AttenDance(int subCode) {
		writeC(S_EXTENDED_PROTOBUF);
		writeC(subCode);
		switch (subCode) {
		case WatchCreate:
			writeC(0x02);
			writeC(0x08);// ????
			writeBit(3600);
			writeC(0x10);// ?????
			writeBit(86400);
			writeC(0x18);
			writeC(1);
			writeC(0x20);
			writeC(1);
			writeC(0x28);
			writeC(2);
			break;
		}
		writeH(0);
	}

	public S_AttenDance(int subCode, int type) {
		writeC(S_EXTENDED_PROTOBUF);
		writeC(subCode);
		switch (subCode) {
		case WatchItemList:
			writeC(0x02);

			if (type == 0) {
				// ?? ??? ???
				// 一般項目清單
				if (AttenDanceTable.getInstance().getNormalSize() > 0) {
					writeC(0x08);
					writeC(0x00);
					byte[] status = itemProfile(0);
					for (byte b : status) {
						writeC(b);
					}
				}
			} else {
				// pc? ??? ???
				// 網吧房間清單
				if (AttenDanceTable.getInstance().getPcRoomSize() > 0) {
					writeC(0x08);
					writeC(0x01);
					byte[] status = itemProfile(1);
					for (byte b : status) {
						writeC(b);
					}
				}
			}
			break;
		}
		writeH(0);
	}

	public S_AttenDance(L1PcInstance pc, int subCode, int type) {
		writeC(S_EXTENDED_PROTOBUF);
		writeC(subCode);
		switch (subCode) {
		case WatchPcPorfile:
			writeC(0x02);

			writeC(0x0a);
			byte[] status = PcPorfile(pc, 0);
			writeBit(status.length);
			for (byte b : status) {
				writeC(b);
			}

			writeC(0x0a);
			status = PcPorfile(pc, 1);
			writeBit(status.length);
			for (byte b : status) {
				writeC(b);
			}
			// ?? ???? ????? ?? ??
			// 照亮時鐘目標的部分
			if (type == 0) {
				writeC(0x10);
				writeC(0x00);
				writeC(0x18);
				writeC(0x01);
				writeC(0x18);
				writeC(0x00);
			} else {
				writeC(0x10);
				writeC(0x01);
				writeC(0x18);
				writeC(0x00);
				writeC(0x18);
				writeC(0x01);
			}
			break;
		}
		writeH(0);
	}

	private byte[] PcPorfile(L1PcInstance pc, int page) {
		byte[] result = null;
		try {
			BinaryOutputStream os = new BinaryOutputStream();
			os.writeC(0x08); // ??? 0??,1??
			os.writeC(page);
			if (page == 0) {
				for (idTemp temp : pc.attendTemp.Nomarlist) {
					os.writeC(0x12);
					os.writeC(0x06);
					os.writeC(0x08);
					os.writeC(temp.id);
					os.writeC(0x10);
					if (temp.state >= 2) {
						temp.state = 2;
						os.writeC(temp.state);
					} else {
						os.writeC(temp.state);
					}
					os.writeC(0x18);
					os.writeC(0x00);
				}
				os.writeC(0x18);
				os.writeC(pc.attendTemp.isNormal); // 0 ??, 1????
				os.writeC(0x20);
				os.writeC(pc.attendTemp.Count_Normal); // ??? ??? ?
				os.writeC(0x28);
				os.writeC(Config.NormalMaxCount); // ??? ?? ? ?? ??? ?? ?? = ???
				os.writeC(0x30);
				os.writeBit(pc.attendTemp.time_Normal); // ?? ?????
				os.writeC(0x38);
				os.writeBit(Config.NormalMaxTime); // ???? - ??? ??

			} else {
				for (idTemp temp : pc.attendTemp.PcRoomlist) {
					os.writeC(0x12);
					os.writeC(0x06);
					os.writeC(0x08);
					os.writeC(temp.id);
					os.writeC(0x10);
					if (temp.state >= 2) {
						temp.state = 2;
						os.writeC(temp.state);
					} else {
						os.writeC(temp.state);
					}
					os.writeC(0x18);
					os.writeC(0x00);
				}
				os.writeC(0x18);

				if (pc.PCRoom_Buff) {
					os.writeC(pc.attendTemp.isPcRoom); // 0 ??, 1????
				} else {
					os.writeC(1);
				}

				os.writeC(0x20);
				os.writeC(pc.attendTemp.Count_PcRoom); // ??? ??? ?
				os.writeC(0x28);
				os.writeC(Config.PcRoomMaxCount); // ??? ?? ? ?? ??? ?? ?? = ???
				os.writeC(0x30);
				os.writeBit(pc.attendTemp.time_PcRoom); // ?? ?????
				os.writeC(0x38);
				os.writeBit(Config.PcRoomMaxTime); // ???? - ??? ??

			}
			result = os.getBytes();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private byte[] itemProfile(int page) {
		byte[] result = null;
		try {
			BinaryOutputStream os = new BinaryOutputStream();
			ArrayList<Attendtemp> itemlist = null;
			if (page == 0) {
				itemlist = AttenDanceTable.getInstance().getNormalList();
			} else {
				itemlist = AttenDanceTable.getInstance().getPcRoomList();
			}

			byte[] status = null;
			for (Attendtemp temp : itemlist) {
				status = itemState(temp.item_id, temp.item_count);
				os.writeC(0x12);
				os.writeBit(status.length + 4);
				os.writeC(0x08);
				os.writeC(temp.id);
				os.writeC(0x12);
				os.writeBit(status.length);
				for (byte b : status) {
					os.writeC(b);
				}
			}
			result = os.getBytes();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public byte[] itemState(int itemid, int count) {
		byte[] result = null;
		try {
			BinaryOutputStream os = new BinaryOutputStream();
			os.writeC(0x08);
			os.writeC(0x02);
			L1Item item = ItemTable.get().getTemplate(itemid);

			// L1ItemInstance temp = ItemTable.get().FunctionItem(item);
			L1ItemInstance temp = null;
			temp = new L1ItemInstance(item);

			os.writeC(0x10);
			os.writeBit(item.getItemDescId());
			os.writeC(0x18);
			os.writeBit(count);
			os.writeC(0x22); // ????????
			os.writeC(0x00);
			os.writeC(0x28); // ?? ????
			os.writeC(0x00);
			os.writeC(0x30); // ???
			os.writeBit(item.getGfxId());
			os.writeC(0x38); // ??
			os.writeC(0x01);
			os.writeC(0x42); // ??
			os.writeC(item.getNameId().getBytes().length);
			os.writeByte(item.getNameId().getBytes());

			os.writeC(0x4a);
			byte[] status = temp.getStatusBytes();
			os.writeBit(status.length);
			for (byte b : status) {
				os.writeC(b);
			}

			os.writeC(0x50); // ?? 0x97, 0x87 2?? ???? ????
			os.writeC(0x97);
			os.writeC(0xff);
			os.writeC(0xff);
			os.writeC(0xff);
			os.writeC(0xff);
			os.writeC(0xff);
			os.writeC(0xff);
			os.writeC(0xff);
			os.writeC(0xff);
			os.writeC(0x01);
			result = os.getBytes();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
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
		return S_AttenDance;
	}
}
