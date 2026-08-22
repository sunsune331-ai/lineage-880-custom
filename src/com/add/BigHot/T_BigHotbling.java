package com.add.BigHot;

import java.util.Timer;
import java.util.TimerTask;

import com.lineage.server.model.TimeInform;
import com.lineage.server.thread.GeneralThreadPool;

public class T_BigHotbling extends TimerTask {

	private static T_BigHotbling _instance;
	private final Timer _timeHandler = new Timer(true);

	private boolean _isBigHotta = false;

	public static T_BigHotbling getStart() {
		if (_instance == null) {
			_instance = new T_BigHotbling();
		}
		return _instance;
	}

	private T_BigHotbling() {
		_timeHandler.schedule(this, 5000, 10000);

		GeneralThreadPool.get().execute(this);
	}

	public void run() {
		final String mTime = TimeInform.time().getNow_YMDHMS(3);
		final String hTime = TimeInform.time().getNow_YMDHMS(4);
		final int mm = Integer.parseInt(mTime);
		final int hh = Integer.parseInt(hTime);
		switch (hh) {
		case 2:
		case 6:
		case 10:
		case 14:
		case 18:
		case 22:
			if ((mm == 1) && (!_isBigHotta)) {
				final BigHotblingTime BigHot = new BigHotblingTime();
				BigHot.startBigHotbling();
				_isBigHotta = true;
			}

			if ((mm == 3) && (_isBigHotta)) {
				_isBigHotta = false;
			}

			break;
		}
	}

	public void startT_BigHotbling() {
		getStart();
	}
}
