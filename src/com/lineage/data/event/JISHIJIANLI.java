package com.lineage.data.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.lock.MaryReading;
import com.lineage.server.templates.L1Event;

public class JISHIJIANLI extends EventExecutor {
	private static final Log _log = LogFactory.getLog(JISHIJIANLI.class);

	public static boolean START = false;

	public static EventExecutor get() {
		return new JISHIJIANLI();
	}

	public void execute(L1Event event) {
		try {
			START = true;

			MaryReading.get().load();
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
}

