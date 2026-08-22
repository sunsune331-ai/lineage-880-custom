package com.lineage.server.model.doll;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 娃娃增加破壞盔甲等級
 */
public class Doll_BreakLevel extends L1DollExecutor {

	private static final Log _log = LogFactory.getLog(Doll_BreakLevel.class);

	private int _int1;// 值1

	/**
	 * 娃娃能力:破壞盔甲等級增加
	 */
	public Doll_BreakLevel() {
	}

	public static L1DollExecutor get() {
		return new Doll_BreakLevel();
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
			pc.addBreakLevel(_int1);
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void removeDoll(final L1PcInstance pc) {
		try {
			pc.addBreakLevel(-_int1);
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public boolean is_reset() {
		return false;
	}
}
