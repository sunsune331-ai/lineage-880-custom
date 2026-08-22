package com.lineage.server.datatables.lock;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.lineage.server.datatables.sql.MailTable;
import com.lineage.server.datatables.storage.MailStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Mail;

/**
 * 信件資料
 */
public class MailReading {

	private final Lock _lock;

	private final MailStorage _storage;

	private static MailReading _instance;

	private MailReading() {
		this._lock = new ReentrantLock(true);
		this._storage = new MailTable();
	}

	public static MailReading get() {
		if (_instance == null) {
			_instance = new MailReading();
		}
		return _instance;
	}

	public void load() {
		this._lock.lock();
		try {
			this._storage.load();
			
		} finally {
			this._lock.unlock();
		}
	}

	public void setReadStatus(final int mailId) {
		this._lock.lock();
		try {
			this._storage.setReadStatus(mailId);
			
		} finally {
			this._lock.unlock();
		}
	}

	public void setMailType(final int mailId, final int type) {
		this._lock.lock();
		try {
			this._storage.setMailType(mailId, type);
			
		} finally {
			this._lock.unlock();
		}
	}

	public void deleteMail(final int mailId) {
		this._lock.lock();
		try {
			this._storage.deleteMail(mailId);
			
		} finally {
			this._lock.unlock();
		}
	}

	public void writeMail(L1Mail mail, final int type, final String receiver, final L1PcInstance writer, final byte[] text, final int isReMail) {
		this._lock.lock();
		try {
			this._storage.writeMail(mail, type, receiver, writer, text, isReMail);
			
		} finally {
			this._lock.unlock();
		}
	}

	public Map<Integer, L1Mail> getAllMail() {
		this._lock.lock();
		Map<Integer, L1Mail> tmp;
		try {
			tmp = this._storage.getAllMail();
			
		} finally {
			this._lock.unlock();
		}
		return tmp;
	}
	
	/**
	 * 以信件ID號取回信件資料
	 * @param mailId
	 * @return
	 */
	public L1Mail getMail(final int mailId) {
		this._lock.lock();
		L1Mail tmp;
		try {
			tmp = this._storage.getMail(mailId);
			
		} finally {
			this._lock.unlock();
		}
		return tmp;
	}

	/**
	 * 取回一般信件的標題
	 * @param receiverName
	 * @return
	 */
	public ArrayList<L1Mail> getMails(final String receiverName) {
		final ArrayList<L1Mail> mailList = new ArrayList<L1Mail>();
		for (final L1Mail mail : this.getAllMail().values()) {
			// 他人寄送給自己的信件 加入清單
			if (mail.getReceiverName().equalsIgnoreCase(receiverName)) {
				if (mail.getType() == 0 && !mail.isReMail()) {
					mailList.add(mail);
				}				
			}
			// 發送者名稱是自己該信件的屬性為寄送便簽加入清單
			if (mail.getSenderName().equalsIgnoreCase(receiverName)) {
				if (mail.getType() == 0 && mail.isReMail()) {
					mailList.add(mail);
				}				
			}
		}		
		return mailList;
	}
	
	/**
	 * 取回血盟信件的標題
	 * @param receiverName
	 * @return
	 */
	public ArrayList<L1Mail> getPMails(final String receiverName) {
		final ArrayList<L1Mail> mailList = new ArrayList<L1Mail>();
		for (final L1Mail mail : this.getAllMail().values()) {
			if (mail.getReceiverName().equalsIgnoreCase(receiverName)) {
				if (mail.getType() == 1 && !mail.isReMail()) {
					mailList.add(mail);
				}				
			}
		}		
		return mailList;
	}
	
	/**
	 * 取回保管箱信件的標題
	 * @param receiverName
	 * @return
	 */
	public ArrayList<L1Mail> getKMails(final String receiverName) {
		final ArrayList<L1Mail> mailList = new ArrayList<L1Mail>();
		for (final L1Mail mail : this.getAllMail().values()) {
			// 他人寄送給自己的信件 加入清單
			if (mail.getReceiverName().equalsIgnoreCase(receiverName)) {
				if (mail.getType() == 2 && !mail.isReMail()) {
					mailList.add(mail);
				}				
			}
			// 發送者名稱是自己該信件的屬性為寄送便簽加入清單
			if (mail.getSenderName().equalsIgnoreCase(receiverName)) {
				if (mail.getType() == 2 && mail.isReMail()) {
					mailList.add(mail);
				}				
			}
		}		
		return mailList;
	}
	
	/**
	 * 取回PC的信箱空間容量
	 * @param receiverName
	 * @param type
	 * @return
	 */
	public int getMailSizeByReceiver(final String receiverName, final int type) {
		final ArrayList<L1Mail> mailList = new ArrayList<L1Mail>();
		for (final L1Mail mail : this.getAllMail().values()) {
			if (mail.getReceiverName().equalsIgnoreCase(receiverName)) {
				// 排除回簽
				if (mail.getType() == type && !mail.isReMail()) {
					mailList.add(mail);
				}
			}
		}
		if (mailList.size() > 0) {
			return mailList.size();
		}
		return 0;
	}
}
