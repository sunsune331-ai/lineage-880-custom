package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃增加屠宰者階段別傷害
 */
public class Doll_FoeSlayerDmg extends L1DollExecutor {

	private static final Log _log = LogFactory.getLog(Doll_FoeSlayerDmg.class);

	private int _int1;// 值1

	/**
	 * 娃娃能力:屠宰者階段別傷害增加
	 */
	public Doll_FoeSlayerDmg() {
	}

	public static L1DollExecutor get() {
		return new Doll_FoeSlayerDmg();
	}

	@Override
	public void set_power(final int int1, final int int2, final int int3) {
		try {
			_int1 = int1;
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	private String _note;

	public void set_note(final String note) {
		_note = note;
	}

	public String get_note() {
		return _note;
	}

	@Override
	public void setDoll(final L1PcInstance pc) {
		try {
			pc.addFoeSlayerDmg(_int1);
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void removeDoll(final L1PcInstance pc) {
		try {
			pc.addFoeSlayerDmg(-_int1);
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public boolean is_reset() {
		return false;
	}
}
