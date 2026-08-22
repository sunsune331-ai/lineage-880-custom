package com.lineage.server.clientpackets;

import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE3;
import static com.lineage.server.model.skill.L1SkillId.STATUS_RIBRAVE;
import static com.lineage.server.model.skill.L1SkillId.WIND_SHACKLE;

import java.util.EnumMap;

import com.lineage.config.ConfigOther;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SystemMessage;

public class AcceleratorChecker {//src042

	private L1PcInstance _pc;

	private int move_injusticeCount;

	private int move_justiceCount;

	private static final int INJUSTICE_COUNT_LIMIT = ConfigOther.INJUSTICE_COUNT;

	private static final int JUSTICE_COUNT_LIMIT = ConfigOther.JUSTICE_COUNT;

	private static double CHECK_STRICTNESS = (ConfigOther.CHECK_STRICTNESS - 5) / 100D;

	private static double CHECK_MOVESTRICTNESS = (ConfigOther.CHECK_MOVE_STRICTNESS - 5) / 100D;

	private static final double HASTE_RATE = 0.75; // 0.755

	private static final double WAFFLE_RATE = 0.875; // 0.9

	private int moveresult = R_OK;

	private long movenow = 0;

	private long moveinterval = 0;

	private int moverightInterval = 0;

	private int attackresult = R_OK;

	private long attacknow = 0;

	private long attackinterval = 0;

	private int attackrightInterval = 0;

	private final EnumMap<ACT_TYPE, Long> _actTimers = new EnumMap<ACT_TYPE, Long>(
			ACT_TYPE.class);

	private final EnumMap<ACT_TYPE, Long> _checkTimers = new EnumMap<ACT_TYPE, Long>(
			ACT_TYPE.class);

	public static enum ACT_TYPE {
		MOVE, ATTACK, SPELL_DIR, SPELL_NODIR
	}

	public static final int R_OK = 0;

	public static final int R_DETECTED = 1;

	public static final int R_DISPOSED = 2;

	public AcceleratorChecker(L1PcInstance pc) {
		_pc = pc;
		move_injusticeCount = 0;
		move_justiceCount = 0;
		long now = System.currentTimeMillis();
		for (ACT_TYPE each : ACT_TYPE.values()) {
			_actTimers.put(each, now);
			_checkTimers.put(each, now);
		}
	}

	public static void Setspeed() {
		CHECK_STRICTNESS = (ConfigOther.CHECK_STRICTNESS - 5) / 100D;
		CHECK_MOVESTRICTNESS = (ConfigOther.CHECK_MOVE_STRICTNESS - 5) / 100D;
	}

	public int checkInterval(ACT_TYPE type) {

		switch (type) {
		case MOVE:
			movenow = System.currentTimeMillis();
			moveinterval = movenow - _actTimers.get(type);
			moverightInterval = getRightInterval(type);
			moveinterval *= CHECK_MOVESTRICTNESS;
			if (0 < moveinterval && moveinterval < moverightInterval) {
				move_injusticeCount++;
				move_justiceCount = 0;
				if (move_injusticeCount >= INJUSTICE_COUNT_LIMIT) {

					doPunishment();
					moveresult = R_DISPOSED;
				} else {
					moveresult = R_DETECTED;
				}
			} else if (moveinterval >= moverightInterval) {
				move_justiceCount++;
				if (move_justiceCount >= JUSTICE_COUNT_LIMIT) {
					move_injusticeCount = 0;
					move_justiceCount = 0;
				}
				moveresult = R_OK;
			}
			_actTimers.put(type, movenow);

			return moveresult;
		default:
			attacknow = System.currentTimeMillis();
			attackinterval = attacknow - _actTimers.get(type);

			attackrightInterval = getRightInterval(type);
			attackinterval *= CHECK_STRICTNESS;
			if (0 < attackinterval && attackinterval < attackrightInterval) {
				attackresult = R_DISPOSED;
			} else if (attackinterval >= attackrightInterval) {
				attackresult = R_OK;
			} else {
				attackresult = R_DISPOSED;
			}
			_actTimers.put(type, attacknow);

			return attackresult;
		}
	}

	public int getRightInterval(ACT_TYPE type) {
		int interval;

		switch (type) {
		case ATTACK:
			interval = SprTable.get().getAttackSpeed(_pc.getTempCharGfx(), _pc.getCurrentWeapon() + 1);
			// 暫時解決由於缺少變身檔數據導致的揮刀不正常
			if (interval == 0) {
				interval = 1200;
				//_pc.sendPackets(new S_SystemMessage("此變身缺少變身檔數據，請連續管理員。"));
			}
			break;
		case MOVE:
			interval = SprTable.get().getMoveSpeed(_pc.getTempCharGfx(), _pc.getCurrentWeapon());
			break;
		case SPELL_DIR:
			interval = SprTable.get().getDirSpellSpeed(_pc.getTempCharGfx());
			break;
		case SPELL_NODIR:
			interval = SprTable.get().getNodirSpellSpeed(_pc.getTempCharGfx());
			break;
		default:
			return 0;
		}

		switch (_pc.getMoveSpeed()) {
		case 1:
			interval *= HASTE_RATE;
			break;
		case 2:
			interval /= HASTE_RATE;
			break;
		default:
			break;
		}

		switch (_pc.getBraveSpeed()) {
		case 1:
			interval *= HASTE_RATE;
			break;

		case 3: // 精靈餅乾 / 人物速度 x1.15(2段加速)
			//interval *= WAFFLE_RATE;
			if (type.equals(ACT_TYPE.MOVE)) {
				interval *= HASTE_RATE; // 移速 * 1.33倍
			} else {
				interval *= WAFFLE_RATE; // 攻速 * 1.15倍
			}
			break;

		case 4:
			if (type.equals(ACT_TYPE.MOVE)) {
				interval *= HASTE_RATE;
			}
			break;

		case 5: // 荒神加速
			interval *= HASTE_RATE / 2;
			break;

		case 6:
			if (type.equals(ACT_TYPE.ATTACK) && _pc.isFastAttackable()) {

				interval *= HASTE_RATE * WAFFLE_RATE;
			}
			break;

		case 9: // 狂怒之風
			interval *= HASTE_RATE;
			break;

		case 10: // 波濤之水
			if (type.equals(ACT_TYPE.MOVE)) {
				interval *= HASTE_RATE; // 移速 * 1.33倍
			} else {
				interval *= WAFFLE_RATE; // 攻速 * 1.15倍
			}
			break;

		default:
			break;
		}

		if (_pc.hasSkillEffect(STATUS_RIBRAVE) && type.equals(ACT_TYPE.MOVE)) {
			interval *= WAFFLE_RATE;
		}

		if (_pc.hasSkillEffect(STATUS_BRAVE3)) {
			interval *= WAFFLE_RATE;
		}

		if (_pc.hasSkillEffect(WIND_SHACKLE) && !type.equals(ACT_TYPE.MOVE)) {
			// interval /= 2;
			interval *= 2;
		}

		if (_pc.getMapId() == 5143) {
			interval *= 0.1;
		}
		return interval;
	}

	private void doPunishment() {
		final int punishment_type = Math.abs(ConfigOther.PUNISHMENT_TYPE);
		final int punishment_time = Math.abs(ConfigOther.PUNISHMENT_TIME);
		final int punishment_mapid = Math.abs(ConfigOther.PUNISHMENT_MAP_ID);
		if (!this._pc.isGm()) {
			int x = this._pc.getX();
			int y = this._pc.getY();
			int mapid = this._pc.getMapId();
			switch (punishment_type) {
			case 0:
				_pc.sendPackets(new S_SystemMessage("\\aG加速器檢測警告"
						+ punishment_time + "秒後強制驅離。"));
				try {
					Thread.sleep(punishment_time * 1000);
				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
				}
				_pc.sendPackets(new S_Disconnect());
				break;
			case 1:
				_pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
				_pc.sendPackets(new S_SystemMessage("\\aG加速器檢測警告"
						+ punishment_time + "秒後解除您的行動。"));
				try {
					Thread.sleep(punishment_time * 1000);
				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
				}
				_pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, false));
				break;
			case 2:
				L1Teleport.teleport(_pc, 32698, 32857,
						(short) punishment_mapid, 5, false);
				_pc.sendPackets(new S_SystemMessage("\\aG加速器檢測警告"
						+ punishment_time + "秒後傳送到地獄。"));
				try {
					Thread.sleep(punishment_time * 1000);
				} catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
				}
				L1Teleport.teleport(_pc, x, y, (short) mapid, 5, false);
				break;
			case 3:
				int[] Head = { 0, 1, 2, 3, 4, 5, 6, 7 };
				int[] X = { x, x - 1, x - 1, x - 1, x, x + 1, x + 1, x + 1 };
				int[] Y = { y + 1, y + 1, y, y - 1, y - 1, y - 1, y, y + 1 };
				for (int i = 0; i < Head.length; i++) {
					if (_pc.getHeading() == Head[i]) {
						L1Teleport.teleport(this._pc, X[i], Y[i],
								(short) mapid, _pc.getHeading(), false);
						_pc.sendPackets(new S_SystemMessage("\\aG加速器檢測。"));
					}
					try {
						Thread.sleep(punishment_time * 1000);
					} catch (Exception e) {
						System.out.println(e.getLocalizedMessage());
					}
				}
			}
		} else {
			_pc.sendPackets(new S_SystemMessage("\\aD遊戲管理員在遊戲中使用加速器檢測中。"));
			move_injusticeCount = 0;
		}
	}
}
