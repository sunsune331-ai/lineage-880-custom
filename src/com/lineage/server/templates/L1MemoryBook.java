package com.lineage.server.templates;

/**
 * 記憶書<br>
 * 
 * 道具nameid=$5839(對話檔telbook0.tbl)<br>
 * 道具nameid=$6415(對話檔telbook1.tbl)<br>
 * 道具nameid=$8450(對話檔telbook2.tbl)<br>
 * 道具nameid=$15994(對話檔telbook2.tbl)<br>
 */
public class L1MemoryBook {
	int id;
	int bookid;
	int type;
	int backlocx;
	int backlocy;
	int backmapid;

	public int getId() {
		return id;
	}

	public void setId(final int i) {
		id = i;
	}

	public int getBookId() {
		return bookid;
	}

	public void setBookId(final int i) {
		bookid = i;
	}

	public int getType() {
		return type;
	}

	public void setType(final int i) {
		type = i;
	}

	public int getLocx() {
		return backlocx;
	}

	public void setLocx(final int i) {
		backlocx = i;
	}

	public int getLocy() {
		return backlocy;
	}

	public void setLocy(final int i) {
		backlocy = i;
	}

	public int getMapid() {
		return backmapid;
	}

	public void setMapid(final int i) {
		backmapid = i;
	}
}
