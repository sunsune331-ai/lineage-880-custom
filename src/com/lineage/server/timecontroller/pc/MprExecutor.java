package com.lineage.server.timecontroller.pc;

import static com.lineage.server.model.Instance.L1PcInstance.REGENSTATE_NONE;
import static com.lineage.server.model.skill.L1SkillId.CONCENTRATION;
import static com.lineage.server.model.skill.L1SkillId.COOKING_1_2_N;
import static com.lineage.server.model.skill.L1SkillId.COOKING_1_2_S;
import static com.lineage.server.model.skill.L1SkillId.COOKING_2_4_N;
import static com.lineage.server.model.skill.L1SkillId.COOKING_2_4_S;
import static com.lineage.server.model.skill.L1SkillId.COOKING_3_5_N;
import static com.lineage.server.model.skill.L1SkillId.COOKING_3_5_S;
import static com.lineage.server.model.skill.L1SkillId.MEDITATION;
import static com.lineage.server.model.skill.L1SkillId.STATUS_BLUE_POTION;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.MapHprMprTable;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.types.Point;

/**
 * PC MP回覆執行
 */
public class MprExecutor {

	private static final Log _log = LogFactory.getLog(MprExecutor.class);

	// 技能 回覆MP增加/減少 <技能編號, 影響質>
	private static final Map<Integer, Integer> _skill = new HashMap<Integer, Integer>();

	private static MprExecutor _instance;

	protected static MprExecutor get() {
		if (_instance == null) {
			_instance = new MprExecutor();
		}
		return _instance;
	}

	private MprExecutor() {

		// 技能回復MP增加
		_skill.put(MEDITATION, 5);
		_skill.put(CONCENTRATION, 2);
		_skill.put(COOKING_1_2_N, 3);
		_skill.put(COOKING_1_2_S, 3);
		_skill.put(COOKING_2_4_N, 2);
		_skill.put(COOKING_2_4_S, 2);
		_skill.put(COOKING_3_5_N, 2);
		_skill.put(COOKING_3_5_S, 2);
	}

	/**
	 * PC MP回覆執行 判斷
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

			// MP已滿
			if (tgpc.getCurrentMp() >= tgpc.getMaxMp()) {
				return false;
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
			return false;
		}
		return true;
	}

	protected void checkRegenMp(L1PcInstance tgpc) {
		try {
			tgpc.set_mpRegenType(tgpc.mpRegenType() + tgpc.getMpRegenState());
			tgpc.setRegenState(REGENSTATE_NONE);

			if (tgpc.isRegenMp()) {
				regenMp(tgpc);
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	private static void regenMp(L1PcInstance tgpc) {
		tgpc.set_mpRegenType(0);

		short mapid = tgpc.getMapId();

		if (mapid == 201) { // 法師試煉地監
			// 魔力不會自動回覆
			return;
		}

		// int baseMpr = 1;
		//
		// switch (tgpc.getWis()) {
		// case 0:
		// case 1:
		// case 2:
		// case 3:
		// case 4:
		// case 5:
		// case 6:
		// case 7:
		// case 8:
		// case 9:
		// case 10:
		// case 11:
		// case 12:
		// case 13:
		// case 14:
		// baseMpr = 1;
		// break;
		// case 15:
		// case 16:
		// baseMpr = 2;
		// break;
		// default:
		// baseMpr = 3;
		// }

		// 精神MP恢復增加量
		int baseMpr = L1ClassFeature.calcWisMpr(tgpc.getWis(), tgpc.getBaseWis());
		// 精神MP藥水恢復增加量
		int PotionStatMpr = L1ClassFeature.calcWisPotionMpr(tgpc.getWis(), tgpc.getBaseWis());

		// 技能補正
		if ((!tgpc.getSkillisEmpty()) && (tgpc.getSkillEffect().size() > 0)) {
			try {
				if (tgpc.hasSkillEffect(STATUS_BLUE_POTION)) { // 藍水
					if (tgpc.getWis() < 11) {
						baseMpr += 1;
						baseMpr += PotionStatMpr; // 精神MP藥水恢復增加量
					} else {
						baseMpr += tgpc.getWis() - 10;
						baseMpr += PotionStatMpr; // 精神MP藥水恢復增加量
					}
				}
				for (Integer skillid : _skill.keySet()) {
					if (tgpc.hasSkillEffect(skillid.intValue())) {
						Integer integer = (Integer) _skill.get(skillid);
						if (integer != null) {
							baseMpr += integer.intValue();
						}
					}
				}

			} catch (ConcurrentModificationException e) {
				// 技能取回發生其他線程進行修改
			} catch (Exception e) {
				_log.error(e.getLocalizedMessage(), e);
			}
		}

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
		int mapmp = MapHprMprTable.get().getMapMpr(mapid);
		if (mapmp != 0) {
			baseMpr += mapmp;
		}

		if (tgpc.getOriginalMpr() > 0) { // WIS MPR補正
			baseMpr += tgpc.getOriginalMpr();
		}

		int itemMpr = tgpc.getInventory().mpRegenPerTick();
		itemMpr += tgpc.getMpr();

		int mpr = baseMpr + itemMpr;
		int newMp = tgpc.getCurrentMp() + mpr;

		newMp = Math.max(newMp, 0);

		tgpc.setCurrentMp(newMp);
	}
}
