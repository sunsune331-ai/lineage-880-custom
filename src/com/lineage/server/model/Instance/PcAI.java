package com.lineage.server.model.Instance;

import java.text.DecimalFormat;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.clientpackets.AcceleratorChecker;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.NpcAiThreadPool;
import com.lineage.server.types.Point;

public class PcAI implements Runnable {
	private static final Log _log = LogFactory.getLog(PcAI.class);
	private static Random _random = new Random();
	private final L1PcInstance _pc;
	public PcAI(final L1PcInstance pc) {
		_pc = pc;
	}

	public void startAI() {
		NpcAiThreadPool.get().execute(this);
	}

	@Override
	public void run() {
		try {
			// System.out.println("===AI執行===");
			// _npc.setAiRunning(true);
			while (_pc != null) {
				if (_pc.isSleeped() || _pc.isParalyzedX() || _pc.isParalyzed()) {
					Thread.sleep(200);
					continue;
				}
				// System.out.println("AI啟動2222");
				// AI的處理
				if (AIProcess()) {
					break;
				}// */

				try {
					// 移動速度延遲
					Thread.sleep(getRightInterval(2));

				} catch (final Exception e) {
					break;
				}
			}

			do {
				try {
					Thread.sleep(getRightInterval(1));

				} catch (final Exception e) {
					break;
				}
			} while (_pc.isDead());

			_pc.allTargetClear();
			_pc.setAiRunning(false);
			_pc.setActived(false);			
			Thread.sleep(10);

		} catch (final Exception e) {
			_log.error("pcAI發生例外狀況: " + this._pc.getName(), e);
		}
	}

	/**
	 * AI的處理
	 * 
	 * @return true:AI終了 false:AI續行
	 */
	private boolean AIProcess() {
		try {
			if (_pc.isDead()) {
				return true;
			}

			if (_pc.getOnlineStatus() == 0) {
				return true;
			}

			if (_pc.getCurrentHp() <= 0) {
				return true;
			}
			if (!_pc.isActived()) {
				return true;
			}	       
	        if(_pc.getMapId()==4){
	        if(_pc.getLocation().getTileLineDistance(new Point(33631, 32678)) < 78){	       
	        	_pc.sendPackets(new S_ServerMessage("城戰區域無法開啟掛機"));
	        	return true;
	         }
	        }
    		if (!_pc.getMap().isAutoBot()&&!_pc.isGm()) {
        		_pc.sendPackets(new S_ServerMessage("\\aD 飛往不可掛機地圖，自動關閉掛機！"));
        		L1Teleport.teleport(_pc, _pc.getLocation(), _pc.getHeading(), false);
        		return true;
    		}
			if (_pc.getMapId() == 4) {
				if (_pc.getLocation().getTileLineDistance(
						new Point(33429, 32813)) < 30) {
					_pc.sendPackets(new S_ServerMessage("\\aG掛機停止，主城區域內無法掛機。"));					
					return true;
				}
			}
			final int result = _pc.speed_Attack().checkInterval(
					AcceleratorChecker.ACT_TYPE.MOVE);
			if (result == AcceleratorChecker.R_DISPOSED) {
				_log.error("要求角色移動:速度異常(" + _pc.getName() + ")");
			} // */
			if (_pc.getlslocx() > 0
					&& _pc.getlslocy() > 0
					&& _pc.getLocation().getTileLineDistance(
							new Point(_pc.getlslocx(), _pc.getlslocy())) > _pc
							.getlsgjfw()) {
				L1Teleport.teleport(_pc, _pc.getlslocx(), _pc.getlslocy(),
						(short) _pc.getMapId(), 0, false);
				_pc.targetClear();
			}
			/*
			 * final int result1 =
			 * _pc.speed_Attack().checkInterval(AcceleratorChecker
			 * .ACT_TYPE.ATTACK); if (result1 ==
			 * AcceleratorChecker.R_DISCONNECTED) { _log.error("要求角色攻擊:速度異常(" +
			 * _pc.getName() + ")"); } //
			 */
			// _pc.setSleepTime(300);
			// 現有目標有效性檢查
			_pc.checkTarget();

			boolean searchTarget = true;
			if (_pc.is_now_target() != null) {
				searchTarget = false;
			}

			if (searchTarget) {
				// 進行目標搜索
				// System.out.println("AI啟動3333");
				_pc.searchTarget();
			}

			if (_pc.is_now_target() == null) {
				if (!_pc.isPathfinding()) {
					_pc.setrandomMoveDirection(_random.nextInt(8));
				}
				_pc.noTarget();
				Thread.sleep(50);
				return false;
			} else {
				_pc.onTarget();
				if (_pc.isPathfinding()) {
					_pc.setPathfinding(false);
				}
			}

			Thread.sleep(50);

		} catch (final Exception e) {
			_log.error("pcAI發生例外狀況: " + this._pc.getName(), e);
		}
		return false; // NPC AI 繼續執行
	}

	/**
	 * 正常的速度
	 * 
	 * @param type
	 *            檢測類型
	 * @return 正常應該接收的速度(MS)
	 */
	private int getRightInterval(final int type) {
		int interval = 0;

		switch (type) {
		case 1:
			interval = SprTable.get().getAttackSpeed(this._pc.getTempCharGfx(),
					this._pc.getCurrentWeapon() + 1);
			break;

		case 2:
			interval = SprTable.get().getMoveSpeed(this._pc.getTempCharGfx(),
					this._pc.getCurrentWeapon());
			break;

		default:
			return 0;
		}
		return intervalR(type, interval);
	}
	private int intervalR(final int type, int interval) {
		try {
			if (this._pc.isSkillDelay()) { // 添加動作延時防止變檔修改加速 QQ:759347094
				return interval * 2;
			}
			if (this._pc.isInvisDelay() && type == 2) { // 添加動作延時防止變檔修改加速
														// QQ:759347094
				return interval *= 1.3;
			}
			if (_pc.isHaste()) {
				interval = (int) ((double) interval * 0.755D);
			}
			if (type == 2 && _pc.isFastMovable()) {
				interval = (int) ((double) interval * 0.755D);
			}
			if (type == 2 && _pc.isFastAttackable()) {
				interval = (int) ((double) interval * 0.66500000000000004D);
			}
			if (_pc.isBrave()) {
				interval = (int) ((double) interval * 0.755D);
			}
			if (_pc.isBraveX()) {
				interval = (int) ((double) interval * 0.755D);
			}
			if (_pc.isElfBrave()) {
				interval = (int) ((double) interval * 0.85499999999999998D);
			}
			if (type == 1 && _pc.isElfBrave()) {
				interval = (int) ((double) interval * 0.90000000000000002D);
			}
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return interval;
	}

}
