package com.lineage.server.clientpackets;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.DeNameTable;
import com.lineage.server.datatables.SpamTable;
import com.lineage.server.datatables.lock.MailReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ExcludingList;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_Mail;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.DeName;
import com.lineage.server.templates.L1Mail;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 要求使用信件系統
 */
public class C_Mail extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_Mail.class);

	/**一般*/
	private static final int TYPE_NORMAL_MAIL = 0x00;

	/**血盟*/
	private static final int TYPE_CLAN_MAIL = 0x01;

	/**保管箱*/
	private static final int TYPE_MAIL_BOX = 0x02;

	@Override
	public void start(final byte[] decrypt, final ClientExecutor client) {
		try {
			// 資料載入
			this.read(decrypt);

			final L1PcInstance pc = client.getActiveChar();

			final int type = this.readC();			

			switch (type) {
			case 0x00:// 私人信件標題取回
			case 0x01:// 血盟信件標題取回
			case 0x02: // 保管箱信件標題取回
				if (pc != null) {
					clientPackA(pc, type);
				}
				break;

			case 0x10:
			case 0x11:
			case 0x12: // 讀取
			case 0x16: // 儲存郵件讀取
				if (pc != null) {
					final int id = this.readD();
					clientPackB(pc, type, id);
				}
				break;

			case 0x20: // 一般信件寄出
				if (pc != null) {
					@SuppressWarnings("unused")
					final int unknow01 = this.readH();
					final String receiverName = this.readS();
					final byte[] textr = this.readByte();

					clientPackD(pc, type, receiverName, textr);
				}
				break;

			case 0x21: // 血盟信件寄出
				if (pc != null) {
					@SuppressWarnings("unused")
					final int unknow02 = this.readH();
					final String clanName = this.readS();
					final byte[] text = this.readByte();

					clientPackE(pc, type, clanName, text);
				}
				break;

			case 0x30:// XXX 從一般信件內容刪除一般信件
			case 0x31:// XXX 從血盟信件內容刪除血盟般信件
			case 0x32: // XXX 從保管箱信件內容刪除保管箱信件
				if (pc != null) {
					final int delid = this.readD();					
					clientPackF(pc, delid);					
				}
				break;

			case 0x40: // 保存一般信件至保管箱
			/**
			 * 	XXX 保存血盟信件至保管箱 這個TYPE並未實做官方 官方的作法是血盟信件是禁止刪除的 血盟信件存到保管箱 官方為產生一個保管箱專屬的OBJID
			 * 意思就是又產生了一個新的OBJID COPY血盟信件的內容過去 原本的血盟信件清單內的信件並不會移除掉 會一直保留 也許等到解散血盟才會移除???
			 */
			case 0x41: // 保存血盟信件至保管箱
				if (pc != null) {
					final int saveid = this.readD();					
					clientPackG(pc, type, saveid);
				}
				break;

			case 0x60: // 刪除個人信件(從清單刪除)(353TW ADD)
			case 0x61: // 刪除血盟信件(從清單刪除)(353TW ADD)
			case 0x62: // 刪除保管箱信件(從清單刪除)(353TW ADD)			
			case 0x66: // 儲存郵件複數刪除
				if (pc != null) {
					final int delcount = this.readD();
					for (int i = 0; i < delcount; i++) {
						final int delid = this.readD();
						clientPackF(pc, delid);
					}
				}
				break;
			}

		} catch (final Exception e) {
			//_log.error(e.getLocalizedMessage(), e);

		} finally {
			this.over();
		}
	}

	/**
	 * 儲存信件至保管箱
	 * @param pc
	 * @param type
	 * @param saveid
	 */
	private void clientPackG(final L1PcInstance pc, final int type, final int saveid) {
		try {
			final L1Mail mail = MailReading.get().getMail(saveid);
			
			if (mail != null) {
				MailReading.get().setMailType(saveid, TYPE_MAIL_BOX);
				pc.sendPackets(new S_Mail(mail, type));
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 刪除信件
	 * @param pc
	 * @param type
	 * @param delid
	 */
	private void clientPackF(final L1PcInstance pc, final int delid) {
		try {
			final L1Mail mail = MailReading.get().getMail(delid);			
			if (mail != null) {
				MailReading.get().deleteMail(delid);
				if (mail.getType() == 0) {
					pc.sendPackets(new S_Mail(mail, 0x30));
				} else if (mail.getType() == 1) {
					pc.sendPackets(new S_Mail(mail, 0x31));
				} else if (mail.getType() == 2) {
					pc.sendPackets(new S_Mail(mail, 0x32));
				}				
			}
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 血盟信件寄出
	 * @param pc
	 * @param type
	 * @param clanName
	 * @param text
	 */
	private void clientPackE(final L1PcInstance pc, final int type, final String clanName, final byte[] text) {
		try {
			final L1Clan clan = WorldClan.get().getClan(clanName);

			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
				if (clan != null) {
					// type 288 已將信件寄出了
					pc.sendPackets(new S_Mail(288));

					for (final String name : clan.getAllMembers()) {
						// 取回血盟成員的血盟信件是否大於=80封
						final int size = MailReading.get().getMailSizeByReceiver(name, TYPE_CLAN_MAIL);
						// 大於80跳過該成員
						if (size >= 80) {
							continue;
						}
						// 對收信者加入信件清單
						// 建立一個新的收件者信件物件暫存
						final L1Mail receiverMail = new L1Mail();
						// 設置MAIL ID
						receiverMail.setId(IdFactory.get().nextId());
						// 寫入數據庫
						MailReading.get().writeMail(receiverMail, TYPE_CLAN_MAIL, name, pc, text, 0);
						final L1PcInstance clanPc = World.get().getPlayer(name);
						if (clanPc != null) { // 血盟成員在線
							// 在線成員發送信件更新封包
							clanPc.sendPackets(new S_Mail(0x51, receiverMail.getId(), pc.getName(), 0, text));
						}
					}
				}

			} else {
				// 189：\f1金幣不足。
				pc.sendPackets(new S_ServerMessage(189));
			}
			
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 發送一般信件
	 * @param pc
	 * @param type
	 * @param receiverName
	 * @param textr
	 */
	private void clientPackD(final L1PcInstance pc, final int type, final String receiverName, final byte[] textr) {
		try {

			// 找尋假人物件
			for (final DeName de : DeNameTable.get().getDeNameList()) {
				if (receiverName.equalsIgnoreCase(de.get_name())) {
					// 對方的信箱已經滿了，所以無法再寄信到對方信箱。
					pc.sendPackets(new S_Mail(32));
					return;
				}
			}

			// 找尋玩家物件
			final L1PcInstance receiver = World.get().getPlayer(receiverName);
			
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 50)) {
				if (receiver != null) { // 連線
					if (MailReading.get().getMailSizeByReceiver(receiverName, TYPE_NORMAL_MAIL) >= 40) {
						// 對方的信件已滿了 無法再發送信件
						pc.sendPackets(new S_Mail(32));
						return;
					}

					// 信件黑名單
		            L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(receiver.getId());
		            if (exList.contains(0, pc.getName())) {
						// 信件遭阻擋無法寄出
		            	pc.sendPackets(new S_ServerMessage(3082));
		                return;
		            }

					// 對寄信者寫入一個寄送信件回簽信加入信件清單
					// 建立一個新的寄件者信件物件暫存
					final L1Mail sendMail = new L1Mail();
					// 設置MAIL ID
					sendMail.setId(IdFactory.get().nextId());
					// 寫入數據庫
					MailReading.get().writeMail(sendMail, TYPE_NORMAL_MAIL, receiverName, pc, textr, 1);
					// 已將信件寄出了
					pc.sendPackets(new S_Mail(288));
					// 發送信件更新封包
					pc.sendPackets(new S_Mail(0x50, sendMail.getId(), receiverName, 1, textr));
					// 對收信者加入信件清單
					// 建立一個新的收件者信件物件暫存
					final L1Mail receiverMail = new L1Mail();
					// 設置MAIL ID
					receiverMail.setId(IdFactory.get().nextId());
					// 寫入數據庫
					MailReading.get().writeMail(receiverMail, TYPE_NORMAL_MAIL, receiverName, pc, textr, 0);
					// 收件者在線
					if (receiver.getOnlineStatus() == 1) {
						// 發送信件更新封包
						receiver.sendPackets(new S_Mail(0x50, receiverMail.getId(), pc.getName(), 0, textr));
					}					
				} else { // 離線
					try {
						final L1PcInstance restorePc = CharacterTable.get().restoreCharacter(receiverName);
						if (restorePc != null) {
							if (MailReading.get().getMailSizeByReceiver(receiverName, TYPE_NORMAL_MAIL) >= 40) {
								// 對方的信件已滿了 無法再發送信件
								pc.sendPackets(new S_Mail(32));
								return;
							}
							
							// 對寄信者寫入一個寄送信件回簽信加入信件清單
							// 建立一個新的寄件者信件物件暫存
							final L1Mail sendMail = new L1Mail();
							// 設置MAIL ID
							sendMail.setId(IdFactory.get().nextId());					
							MailReading.get().writeMail(sendMail, TYPE_NORMAL_MAIL, receiverName, pc, textr, 1);
							// 已將信件寄出了
							pc.sendPackets(new S_Mail(288));
							// 發送信件更新封包
							pc.sendPackets(new S_Mail(0x50, sendMail.getId(), receiverName, 1, textr));
							
							// 對收信者加入信件清單
							// 建立一個新的收件者信件物件暫存
							final L1Mail receiverMail = new L1Mail();
							// 設置MAIL ID
							receiverMail.setId(IdFactory.get().nextId());
							// 寫入數據庫
							MailReading.get().writeMail(receiverMail, TYPE_NORMAL_MAIL, receiverName, pc, textr, 0);							
						} else {
							// 109:沒有叫%0的人。
							pc.sendPackets(new S_ServerMessage(109, receiverName));
						}
					} catch (final Exception e) {
						_log.error(e.getLocalizedMessage(), e);
					}
				}
			} else {
				// 189：\f1金幣不足。
				pc.sendPackets(new S_ServerMessage(189));
			}
			
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 讀取信件內容
	 * @param pc
	 * @param type
	 * @param id
	 */
	private void clientPackB(final L1PcInstance pc, final int type, final int id) {
		try {
			final L1Mail mail = MailReading.get().getMail(id);
			
			if (mail != null) {
				if (mail.getReadStatus() == 0) {
					MailReading.get().setReadStatus(id);
				}
				if (type == 0x16) {
					pc.sendPackets(new S_Mail(mail, type, id));
				} else {
					pc.sendPackets(new S_Mail(mail, type));
				}
			}
			
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 取回信件標題
	 * @param pc 持有信件的PC
	 * @param type 信件屬性 0 私人信件 1血盟信件 2保管箱信件
	 */
	private void clientPackA(final L1PcInstance pc, final int type) {
		try {
			if (type == 0) {//個人信件
				final ArrayList<L1Mail> mails = MailReading.get().getMails(pc.getName());
				
				if (mails != null) {
					if (!mails.isEmpty()) {
						pc.sendPackets(new S_Mail(mails, type));
					}
				}
			} else if (type == 1) {// 血盟信件
				final ArrayList<L1Mail> mails = MailReading.get().getPMails(pc.getName());				
				if (mails != null) {
					if (!mails.isEmpty()) {
						pc.sendPackets(new S_Mail(mails, type));
					}
				}
			} else if (type == 2) {// 保管箱信件
				final ArrayList<L1Mail> mails = MailReading.get().getKMails(pc.getName());				
				if (mails != null) {
					pc.sendPackets(new S_Mail(mails, type));
				}
			}
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}
}
