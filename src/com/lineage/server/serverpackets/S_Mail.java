package com.lineage.server.serverpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.templates.L1Mail;

/**
 * 郵件系統
 * 
 * @author dexc
 *
 */
public class S_Mail extends ServerBasePacket {

	private static final Log _log = LogFactory.getLog(S_Mail.class);

	private byte[] _byte = null;
	
	/**
	 * 取回信件標題
	 * 
	 * @param mails 信件清單
	 * @param type 0X00 一般 0X01 血盟 0X02 保管箱
	 */
	public S_Mail(ArrayList<L1Mail> mails, int type) {
		try {
			writeC(S_OPCODE_MAIL);
			writeC(type);// 0x00 一般信件 血盟信件 0x01 保管箱0x02
			if (mails.size() > 0) {
				writeH(mails.size());
				for (int i = 0; i < mails.size(); i++) {
					L1Mail mail = mails.get(i);
					writeD(mail.getId());
					writeC(mail.getReadStatus());// 0x00 已讀 0x01 未讀
					writeD((int) (mail.getDateTime().getTime() / 1000));// 353TW收信時間
					writeC((mail.isReMail()) ? 1 : 0);// 353TW TYPE 收信者 跟 寄信者的圖案
					if (mail.isReMail()) {
						writeS(mail.getReceiverName());
					} else {
						writeS(mail.getSenderName());
					}
					writeByte(mail.getSubject());
				}
			} else {
				writeH(0);
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 接收信件
	 * @param id
	 * @param receiverName
	 * @param type
	 * @param Content
	 */
	public S_Mail(int type1, int id, String receiverName, int type2, byte[] Content) {
		try {
			writeC(S_OPCODE_MAIL);
			writeC(type1);// 0x50 一般 0x51血盟
			writeD(id);// 信件ID號
			writeC(type2);// 信件的屬性 0 一般 1 回簽			
			writeS(receiverName);// 收件者名稱
			writeByte(Content);// 內容

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 信件傳送事件回應
	 * @param type 288 已將信件寄出了
	 * @param type 32 對方的信箱已經滿了 無法寄出信件
	 */
	public S_Mail(int type) { // 受信者通知
		writeC(S_OPCODE_MAIL);
		writeC(type);
	}

	/**
	 * 讀取一般信件
	 * 信件存到保管箱
	 * 
	 * @param mail
	 * @param type
	 */
	public S_Mail(final L1Mail mail, final int type) {
		switch (type) {
		case 0x30:// 刪除一般
		case 0x31:// 刪除血盟
		case 0x32:// 刪除保管箱
		case 0x40:// 一般信件存到保管箱
		case 0x60:// 複數郵件刪除
			buildPacket_1(mail, type);
			break;

		case 0x06:// 儲存郵件刪除
			buildPacket_1(mail, 0x36);
			break;

		default:
			buildPacket_2(mail, type);
			break;
		}
	}

	private void buildPacket_1(L1Mail mail, int type) {
		writeC(S_OPCODE_MAIL);
		writeC(type);
		writeD(mail.getId());
		writeC(0x01);
	}

	private void buildPacket_2(L1Mail mail, int type) {
		writeC(S_OPCODE_MAIL);
		writeC(type);
		writeD(mail.getId());
		writeByte(mail.getContent());
	}

	public S_Mail(final L1Mail mail, final int type, final int id) {
		writeC(S_OPCODE_MAIL);
		if (type == 0x16) {
			writeC(type);
			writeD(mail.getId());
			for (int i = 0; i < mail.getSubject().length - 2; i++) {
				writeC(mail.getSubject()[i]);
			}
			for (int i = 0; i < mail.getContent().length - 2; i++) {
				writeC(mail.getContent()[i]);
			}
			return;
		}
		writeC(type);
		writeD(mail.getId());
		writeByte(mail.getContent());
		writeC(id);
		writeS(mail.getSenderName());
		writeByte(mail.getSubject());
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
