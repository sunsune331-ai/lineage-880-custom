package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃增加精靈耐性
 * @author Admin
 */
public class Doll_Regist_Elf extends L1DollExecutor {
	private static final Log _log = LogFactory.getLog(Doll_Regist_Elf.class);
	private int _int1;

	public static L1DollExecutor get() {
		return new Doll_Regist_Elf();
	}

	public void set_power(int int1, int int2, int int3) {
		try {
			_int1 = int1;
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
			pc.addRegistElf(_int1);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void removeDoll(L1PcInstance pc) {
		try {
			pc.addRegistElf(-_int1);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public boolean is_reset() {
		return false;
	}
}
