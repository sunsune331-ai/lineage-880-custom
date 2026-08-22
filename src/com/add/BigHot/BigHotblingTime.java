package com.add.BigHot;

import java.util.Timer;
import java.util.TimerTask;

import com.add.L1Config;
import com.lineage.server.IdFactory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.serverpackets.S_NpcChatPacket;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;

public class BigHotblingTime extends TimerTask {

	private final Timer _timeHandler = new Timer(true);

	private boolean _isOver = false;

	private int _BigHottTime = 0;

	private final BigHotblingTimeList _BigHot = BigHotblingTimeList.BigHot();

	public String BigHotAN;

	private int BigHotAN1 = 0;
	private int BigHotAN2 = 0;
	private int BigHotAN3 = 0;
	private int BigHotAN4 = 0;
	private int BigHotAN5 = 0;
	private int BigHotAN6 = 0;

	public void startBigHotbling() {
		_timeHandler.schedule(this, 500, 500);

		GeneralThreadPool.get().execute(this);

		nowStart();
	}

	public void run() {
		if (_isOver) {
			try {
				Thread.sleep(10000);
				clear();

			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

		_BigHottTime += 1;
		switch (_BigHottTime) {
		case 2:
			toAllTimeM("60");
			donumber1();
			World.get().broadcastPacketToAll(new S_SystemMessage("距離大樂透開獎時間還有1個小時。"));
			break;

		case 6000:
			toAllTimeM("10");
			World.get().broadcastPacketToAll(new S_SystemMessage("距離大樂透開獎時間還有10分鐘。"));
			break;

		case 6600:
			toAllTimeM("5");
			World.get().broadcastPacketToAll(new S_SystemMessage("距離大樂透開獎時間還有5分鐘。"));
			break;

		case 6720:
			toAllTimeM("4");
			break;

		case 6840:
			toAllTimeM("3");
			break;

		case 6960:
			toAllTimeM("2");
			break;

		case 7080:
			toAllTimeM("1");
			World.get().broadcastPacketToAll(new S_SystemMessage("距離大樂透開獎時間還有1分鐘。"));
			break;

		case 7190:
			toAllTimeS("5");
			break;
		case 7192:
			toAllTimeS("4");
			break;
		case 7194:
			toAllTimeS("3");
			break;
		case 7196:
			toAllTimeS("2");
			break;
		// case 7118:
		case 7198:
			toAllTimeS("1");
			break;
		case 7200:
			Start();
			break;
		case 7206:
			toRate1(1);
			break;
		case 7212:
			toRate1(2);
			break;
		case 7218:
			toRate1(3);
			break;
		case 7224:
			toRate(1, BigHotAN1);
			break;
		case 7234:
			toRate(2, BigHotAN2);
			break;
		case 7244:
			toRate(3, BigHotAN3);
			break;
		case 7254:
			toRate(4, BigHotAN4);
			break;
		case 7264:
			toRate(5, BigHotAN5);
			break;
		case 7274:
			toRate(6, BigHotAN6);
			break;
		case 7284:
			checkVictory();
			final int BigHotId = _BigHot.get_BigHotId();
			World.get().broadcastPacketToAll(
					new S_SystemMessage("大樂透 第 " + String.valueOf(BigHotId) + " 期開出的號碼是 " + BigHotAN + "。"));
			break;
		}
	}

	private void toRate(final int type, final int info) {
		for (final L1Object object : World.get().getObject()) {
			if (!(object instanceof L1NpcInstance))
				continue;
			final L1NpcInstance npc = (L1NpcInstance) object;
			if (npc.getNpcTemplate().get_npcId() == L1Config._2162) {
				String Name = null;
				switch (type) {
				case 1:
					Name = "一";
					break;
				case 2:
					Name = "二";
					break;
				case 3:
					Name = "三";
					break;
				case 4:
					Name = "四";
					break;
				case 5:
					Name = "五";
					break;
				case 6:
					Name = "六";
					break;
				}

				final String toUser = "開出的第" + Name + "個號碼是(" + info + ")";

				if (npc != null) {
					npc.broadcastPacketAll(new S_NpcChatPacket(npc, toUser, 2));
				}
			}
		}
	}

	private void toRate1(final int type) {
		for (final L1Object object : World.get().getObject()) {
			if (!(object instanceof L1NpcInstance))
				continue;
			final L1NpcInstance npc = (L1NpcInstance) object;
			if (npc.getNpcTemplate().get_npcId() == L1Config._2162) {
				String Name1 = null;
				int money = 0;
				switch (type) {
				case 1:
					Name1 = "頭獎";
					money = _BigHot.get_bigmoney1();
					break;
				case 2:
					Name1 = "一獎";
					money = _BigHot.get_bigmoney2();
					break;
				case 3:
					Name1 = "二獎";
					money = _BigHot.get_bigmoney3();
					break;
				}

				final String toUser = "本期" + Name1 + "的獎金是(" + money + ")";

				if (npc != null) {
					npc.broadcastPacketAll(new S_NpcChatPacket(npc, toUser, 2));
				}
			}
		}
	}

	private void toAllTimeM(final String info) {
		for (final L1Object object : World.get().getObject()) {
			if (!(object instanceof L1NpcInstance))
				continue;
			final L1NpcInstance npc = (L1NpcInstance) object;
			if (npc.getNpcTemplate().get_npcId() == L1Config._2162) {
				final String toUser = "距離開獎$376 " + info + " $377";
				if (npc != null) {
					npc.broadcastPacketAll(new S_NpcChatPacket(npc, toUser, 2));
				}
			}
		}
	}

	private void toAllTimeS(final String info) {
		for (final L1Object object : World.get().getObject()) {
			if (!(object instanceof L1NpcInstance))
				continue;
			final L1NpcInstance npc = (L1NpcInstance) object;
			if ((npc.getNpcTemplate().get_npcId() == L1Config._2162) && (npc != null)) {
				npc.broadcastPacketAll(new S_NpcChatPacket(npc, info, 2));
			}
		}
	}

	private void Start() {
		for (final L1Object object : World.get().getObject()) {
			if (!(object instanceof L1NpcInstance))
				continue;
			final L1NpcInstance npc = (L1NpcInstance) object;
			if (npc.getNpcTemplate().get_npcId() == L1Config._2162) {
				final String toUser = "大樂透即將開獎囉！！";

				if (npc != null) {
					npc.broadcastPacketAll(new S_NpcChatPacket(npc, toUser, 2));
				}

				_BigHot.set_isStart(true);
			}
		}

		if (_BigHot.get_yuanbao() == 0) {
			_BigHot.add_yuanbao(L1Config._2164);
		}

		final L1BigHotbling BigHotInfo = BigHotblingLock.create().getBigHotbling(_BigHot.get_BigHotId() - 1);
		if (BigHotInfo != null) {
			if (BigHotInfo.get_count() == 0) {
				if (BigHotInfo.get_money1() < L1Config._2166) {
					_BigHot.add_yuanbao(BigHotInfo.get_money1());
				} else {
					_BigHot.add_yuanbao(L1Config._2165);
				}
			} else {
				_BigHot.add_yuanbao(L1Config._2165);
			}
			if (BigHotInfo.get_count1() == 0) {
				_BigHot.add_yuanbao(BigHotInfo.get_money2());
			}
			if (BigHotInfo.get_count2() == 0) {
				_BigHot.add_yuanbao(BigHotInfo.get_money3());
			}
		} else {
			_BigHot.add_yuanbao(L1Config._2165);
		}

		_BigHot.computationBigHot();
	}

	private void clear() {
		if (cancel()) {
			_timeHandler.purge();
		}

		BigHotAN = null;
		_BigHottTime = 0;
		_isOver = false;

		_BigHot.clear();

		System.gc();
	}

	private void checkVictory() {
		for (final L1Object object : World.get().getObject()) {
			if (!(object instanceof L1NpcInstance))
				continue;
			final L1NpcInstance npc = (L1NpcInstance) object;
			if ((npc.getNpcTemplate().get_npcId() == L1Config._2162) && (BigHotAN != null)) {
				final int BigHotId = _BigHot.get_BigHotId();

				final String toUser = "大樂透 $375 " + String.valueOf(BigHotId) + " 期開出的號碼是 " + BigHotAN + "。";

				if (npc != null) {
					npc.broadcastPacketAll(new S_NpcChatPacket(npc, toUser, 2));
				}
				isOver();

				_BigHot.set_isStart(false);
			}
		}
	}

	private void donumber1() {
		BigHotAN = "";
		while (BigHotAN.split(",").length < 6) {
			final int sk = 1 + (int) (Math.random() * 46.0D);
			if (BigHotAN.indexOf(sk + ",") < 0)
				BigHotAN = (BigHotAN + String.valueOf(sk) + ",");
			if (BigHotAN.split(",").length == 1) {
				BigHotAN1 = sk;
			}
			if (BigHotAN.split(",").length == 2) {
				BigHotAN2 = sk;
			}
			if (BigHotAN.split(",").length == 3) {
				BigHotAN3 = sk;
			}
			if (BigHotAN.split(",").length == 4) {
				BigHotAN4 = sk;
			}
			if (BigHotAN.split(",").length == 5) {
				BigHotAN5 = sk;
			}
			if (BigHotAN.split(",").length == 6) {
				BigHotAN6 = sk;
			}
		}

		_BigHot.set_BigHotId1(BigHotAN);
	}

	private void isOver() {
		final int yuanbao = _BigHot.get_yuanbao();

		final int yuanbao1 = _BigHot.get_bigmoney1();

		final int yuanbao2 = _BigHot.get_bigmoney2();

		final int yuanbao3 = _BigHot.get_bigmoney3();

		final int count1 = _BigHot.get_count1();

		final int count2 = _BigHot.get_count2();

		final int count3 = _BigHot.get_count3();

		final int count4 = _BigHot.get_count4();

		BigHotblingLock.create().create(_BigHot.get_BigHotId(), BigHotAN, yuanbao, yuanbao1, count1, yuanbao2, count2,
				yuanbao3, count3, count4);

		_isOver = true;
	}

	private void nowStart() {
		final int BigHotId = IdFactory.get().nextBigHotId();
		_BigHot.set_BigHotId(BigHotId);

		_BigHot.set_isWaiting(true);

		_BigHot.set_isBuy(true);
	}
}
