package com.lineage.server.templates;

import java.sql.Timestamp;

public class L1Mail {

	public L1Mail() {
	}

	private int _id;

	private int _type;

	private String _senderName;

	private String _receiverName;

	private String _date = null; // yy/mm/dd

	private Timestamp _dateTime = null;

	private int _readStatus = 0;

	private byte[] _subject = null;

	private byte[] _content = null;

	private boolean _reMail = false;

	public int getId() {
		return _id;
	}

	public void setId(final int i) {
		_id = i;
	}

	public int getType() {
		return _type;
	}

	public void setType(final int i) {
		_type = i;
	}

	public String getSenderName() {
		return _senderName;
	}

	public void setSenderName(final String s) {
		_senderName = s;
	}

	public String getReceiverName() {
		return _receiverName;
	}

	public void setReceiverName(final String s) {
		_receiverName = s;
	}

	public String getDate() {
		return _date;
	}

	public void setDate(final String s) {
		_date = s;
	}

	public Timestamp getDateTime() {
		return _dateTime;
	}

	public void setDateTime(final Timestamp t) {
		_dateTime = t;
	}

	public int getReadStatus() {
		return _readStatus;
	}

	public void setReadStatus(final int i) {
		_readStatus = i;
	}

	public byte[] getSubject() {
		return _subject;
	}

	public void setSubject(final byte[] arg) {
		final byte[] newarg = new byte[arg.length - 2];
		System.arraycopy(arg, 0, newarg, 0, newarg.length);
		_subject = arg;
	}

	public byte[] getContent() {
		return _content;
	}

	public void setContent(final byte[] arg) {
		_content = arg;
	}

	public boolean isReMail() {
		return _reMail;
	}

	public void setReMail(final boolean flag) {
		_reMail = flag;
	}
}
