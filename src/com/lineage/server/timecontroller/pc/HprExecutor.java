package com.lineage.server.timecontroller.pc;

import static com.lineage.server.model.Instance.L1PcInstance.REGENSTATE_NONE;
import static com.lineage.server.model.skill.L1SkillId.COOKING_1_5_N;
import static com.lineage.server.model.skill.L1SkillId.COOKING_1_5_S;
import static com.lineage.server.model.skill.L1SkillId.COOKING_2_4_N;
import static com.lineage.server.model.skill.L1SkillId.COOKING_2_4_S;
import static com.lineage.server.model.skill.L1SkillId.COOKING_3_6_N;
import static com.lineage.server.model.skill.L1SkillId.COOKING_3_6_S;
import static com.lineage.server.model.skill.L1SkillId.NATURES_TOUCH;
import static com.lineage.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.datatables.MapHprMprTable;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.types.Point;

/**
 * PC HP回覆執行
 * @author dexc
 */
public class HprExecutor {

	private static final Log _log = LogFactory.getLog(HprExecutor.class);

	// 技能回復HP增加
	private static final Map<Integer, Integer> _skill = new HashMap<Integer, Integer>();

	// HP減少的MAP(任務MAP)
	private static final Map<Integer, Integer> _mapIdD = new HashMap<Integer, Integer>();

	private static HprExecutor _instance;

	protected static HprExecutor get() {
		if (_instance == null) {
			_instance = new HprExecutor();
		}
		return _instance;
	}

	private HprExecutor() {
		// 技能回復HP增加
		_skill.put(NATURES_TOUCH, 15);
		_skill.put(COOKING_1_5_N, 3);
		_skill.put(COOKING_1_5_S, 3);
		_skill.put(COOKING_2_4_N, 2);
		_skill.put(COOKING_2_4_S, 2);
		_skill.put(COOKING_3_6_N, 2);
		_skill.put(COOKING_3_6_S, 2);

		// HP減少的MAP(任務MAP)
		_mapIdD.put(410, -10);// 魔族神殿
		_mapIdD.put(CKEWLv50_1.MAPID, -10);// 再生聖殿 1樓/2樓/3樓
		_mapIdD.put(DarkElfLv50_1.MAPID, -10);// 黑暗妖精試煉地監
	}

	/**
	 * PC HP回覆執行 判斷
	 * @param tgpc
	 * @return true:執行 false:不執行
	 */
	protected boolean check(L1PcInstance tgpc) {
		try {
			// 人物為空
			if (tgpc == null) {
				return false;
			}

			// 人物登出
			// if (tgpc.getOnlineStatus() == 0) {
			// return false;
			// }

			// 中斷連線
			if (tgpc.getNetConnection() == null) {
				return false;
			}

			// 死亡
			if (tgpc.isDead()) {
				return false;
			}

			// 傳送狀態
			if (tgpc.isTeleport()) {
				return false;
			}

			// HP減少的MAP(任務MAP)
			Integer dhp = (Integer) _mapIdD.get(new Integer(tgpc.getMapId()));
			if (dhp != null) {
				return true;
			}

			// 在水中區域HP減少判斷
			if (isUnderwater(tgpc)) {
				return true;
			}

			// HP已滿
			if (tgpc.getCurrentHp() >= tgpc.getMaxHp()) {
				return false;
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
			return false;
		}
		return true;
	}

	protected void checkRegenHp(L1PcInstance tgpc) {
		try {
			tgpc.set_hpRegenType(tgpc.hpRegenType() + tgpc.getHpRegenState());
			tgpc.setRegenState(REGENSTATE_NONE);

			if (tgpc.isRegenHp()) {
				regenHp(tgpc);
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	private static void regenHp(L1PcInstance tgpc) {
		tgpc.set_hpRegenType(0);
		int maxBonus = 1;

		// 等級大於11
		if ((tgpc.getLevel() > 11) && (tgpc.getCon() >= 14)) {
			maxBonus = Math.min(tgpc.getCon() - 12, 14); // 取回最小
		}

		int equipHpr = tgpc.getInventory().hpRegenPerTick();
		equipHpr += tgpc.getHpr();

		Random random = new Random();
		int bonus = random.nextInt(maxBonus) + 1;

		// 技能補正
		if ((!tgpc.getSkillisEmpty()) && (tgpc.getSkillEffect().size() > 0)) {
			try {
				for (Integer skillid : _skill.keySet()) {
					if (tgpc.hasSkillEffect(skillid.intValue())) {
						Integer integer = (Integer) _skill.get(skillid);
						if (integer != null) {
							bonus += integer.intValue();
						}
					}
				}

			} catch (ConcurrentModificationException localConcurrentModificationException) {
				// 技能取回發生其他線程進行修改
			} catch (Exception e) {
				_log.error(e.getLocalizedMessage(), e);
			}

		}

		// 7.6空身體質回覆獎勵
		// bonus += L1ClassFeature.calcConHpr(tgpc.getCon(), tgpc.getBaseCon());
		if (tgpc.getBaseCon() >= 25 && tgpc.getBaseCon() <= 34) {
			bonus += 1;
		} else if (tgpc.getBaseCon() >= 35 && tgpc.getBaseCon() <= 44) {
			bonus += 2;
		} else if (tgpc.getBaseCon() >= 45) {
			bonus += 5;
		}

		short mapid = tgpc.getMapId();

		// 血盟小屋
		if (L1HouseLocation.isInHouse(tgpc.getX(), tgpc.getY(), mapid)) {
			mapid = 32767;
		}

		// 地下盟屋
		if (L1HouseLocation.isInHouse(mapid)) {
			mapid = 32766;
		}

		// 世界樹
		if (tgpc.isElf()) {
			if (mapid == 4) {
				if (tgpc.getLocation().isInScreen(new Point(33055, 32336))) {
					mapid = 32765;
				}
			}
		}

		// 地圖回血回魔系統
		int maphp = MapHprMprTable.get().getMapHpr(tgpc, mapid);
		if (maphp != 0) {
			bonus += maphp;
		}

		if (tgpc.getOriginalHpr() > 0) { // CON HPR補正
			bonus += tgpc.getOriginalHpr();
		}

		// 治癒能量風暴
		boolean inLifeStream = false;
		if (isPlayerInLifeStream(tgpc)) {
			inLifeStream = true;
			bonus += 3;
		}

		int newHp = tgpc.getCurrentHp();
		newHp += bonus + equipHpr;

		newHp = Math.max(newHp, 1);

		if (isUnderwater(tgpc)) {
			newHp -= 20;
		}

		// HP減少的MAP(任務MAP)
		Integer dhp = (Integer) _mapIdD.get(new Integer(tgpc.getMapId()));
		if ((dhp != null) && (!inLifeStream)) {
			bonus += dhp.intValue();
		}

		newHp = Math.max(newHp, 0);

		tgpc.setCurrentHp(newHp);
	}

	/**
	 * 在水中區域HP減少判斷
	 * @param pc
	 * @return
	 */
	private static boolean isUnderwater(L1PcInstance pc) {
		if (pc.getInventory().checkEquipped(20207)) { // 深水長靴
			return false;
		}
		if (pc.hasSkillEffect(STATUS_UNDERWATER_BREATH)) { // 伊娃的祝福藥水效果
			return false;
		}
		if ((pc.getInventory().checkEquipped(21048)) // 修好的戒指
				&& (pc.getInventory().checkEquipped(21049)) // 修好的耳環
				&& (pc.getInventory().checkEquipped(21050)) // 修好的項鏈
		) {
			return false;
		}

		return pc.getMap().isUnderwater();
	}

	/**
	 * 法師技能(治癒能量風暴)
	 * @param pc PC
	 * @return true PC在4格範圍內
	 */
	private static boolean isPlayerInLifeStream(L1PcInstance pc) {
		for (L1Object object : pc.getKnownObjects()) {
			if ((object instanceof L1EffectInstance)) {
				L1EffectInstance effect = (L1EffectInstance) object;
				// 法師技能(治癒能量風暴)
				if ((effect.getNpcId() == 81169) && (effect.getLocation().getTileLineDistance(pc.getLocation()) < 4)) {
					return true;
				}
			}
		}
		return false;
	}
}
