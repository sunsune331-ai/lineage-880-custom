package com.lineage.data.event;

import java.util.HashMap;
import java.util.Map;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;

public class SkillTeacherSet extends EventExecutor {

	public static final Map<Integer, Integer> RESKILLLIST = new HashMap<Integer, Integer>();

	public static EventExecutor get() {
		return new SkillTeacherSet();
	}

	public void execute(L1Event event) {
		String[] set = event.get_eventother().split(",");
		for (String string : set)
			RESKILLLIST.put(Integer.valueOf(Integer.parseInt(string) - 1), Integer.valueOf(Integer.parseInt(string)));
	}
}
