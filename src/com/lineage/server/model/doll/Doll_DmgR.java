package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

public class Doll_DmgR extends L1DollExecutor {
	private static final Log _log = LogFactory.getLog(Doll_DmgR.class);
	private int _int1;
	private int _int2;

	public static L1DollExecutor get() {
		return new Doll_DmgR();
	}

	public void set_power(int int1, int int2, int int3) {
		try {
			_int1 = int1;
			_int2 = int2;
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	private String _note;

	public void set_note(String note) {
		_note = note;
	}

	public String get_note() {
		return _note;
	}

	public void setDoll(L1PcInstance pc) {
		try {
			pc.set_dmgAdd(_int1, _int2);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void removeDoll(L1PcInstance pc) {
		try {
			pc.set_dmgAdd(-_int1, -_int2);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public boolean is_reset() {
		return false;
	}
}