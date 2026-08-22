package com.lineage.server.utils;

import java.util.Calendar;
import java.util.TimeZone;

import com.lineage.config.Config;

public class PerformanceTimer {
	private long _begin;

	public PerformanceTimer() {
		_begin = System.currentTimeMillis();
	}

	public void reset() {
		_begin = System.currentTimeMillis();
	}

	public long get() {
		return System.currentTimeMillis() - _begin;
	}

	public static Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}
}

