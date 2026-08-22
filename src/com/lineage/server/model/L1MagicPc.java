package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.ConcurrentModificationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigSkill;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1DeInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_AttackPacketPc;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.templates.L1SystemMessage;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.RandomArrayList;

public class L1MagicPc extends L1MagicMode {
	private static final Log _log = LogFactory.getLog(L1MagicPc.class);

	public L1MagicPc(L1PcInstance attacker, L1Character target) {
		if (attacker == null) {
			return;
		}

		_pc = attacker;

		if ((target instanceof L1PcInstance)) {
			_calcType = PC_PC;
			_targetPc = ((L1PcInstance) target);
		} else {
			_calcType = PC_NPC;
			_targetNpc = ((L1NpcInstance) target);
		}
	}

	private int getMagicLevel() {
		return _pc.getMagicLevel();
	}

	private int getMagicBonus() {
		return _pc.getMagicBonus();
	}

	private int getLawful() {
		return _pc.getLawful();
	}

	public boolean calcProbabilityMagic(int skillId) {
		int probability = 0;
		boolean isSuccess = false;

		switch (_calcType) {
		case PC_PC:
			if ((skillId == CANCELLATION) && // 相消
					(_pc != null) && (_targetPc != null)) {

				if (_pc.getId() == _targetPc.getId()) {// 消自己
					return true;
				}

				// 相同血盟100%成功
				if (_pc.getClanid() > 0 && _pc.getMapId() != 10502) { // 底比斯大戰地圖例外
					if (_pc.getClanid() == _targetPc.getClanid()) {
						if(_pc.get_redbluejoin()==0){//判斷陣營戰同盟之間
						return true;
						}
					}
				}

				/*
				 * if ((_pc.isInParty()) &&
				 * (_pc.getParty().isMember(_targetPc))) {//消隊友 return true; }
				 */

			}

			if (skillId == SHAPE_CHANGE) {// 變形術
				if (_pc.getId() == _targetPc.getId()) {// 變自己
					return true;
				}

				if ((_pc.getClanid() > 0) && (_pc.getClanid() == _targetPc.getClanid())) {// 變盟友
					return true;
				}
			}

			/*
			 * if (_pc.isGm()) { return true; }
			 */

			if (!checkZone(skillId)) {
				return false;
			}

			if (_targetPc.hasSkillEffect(ICE_LANCE)) {
				if (skillId != CANCELLATION) {
					return false;
				}
			}

			if (_targetPc.hasSkillEffect(EARTH_BIND)) {
				if (skillId != CANCELLATION) {
					return false;
				}
			}

			if (calcEvasion()) {
				return false;
			}

			break;
		case PC_NPC:
			/*
			 * if (_pc.isGm()) { return true; }
			 */

			if (_targetNpc != null) {
				if ((_targetNpc instanceof L1DeInstance)) {
					if (!checkZoneDE(skillId)) {
						return false;
					}
				}

				int gfxid = _targetNpc.getNpcTemplate().get_gfxid();
				switch (gfxid) {
				case 2412:
					if (!_pc.getInventory().checkEquipped(20046)) {
						return false;
					}

					break;
				}

				int npcId = _targetNpc.getNpcTemplate().get_npcId();
				Integer tgskill = (Integer) L1AttackList.SKNPC.get(Integer.valueOf(npcId));
				if ((tgskill != null) && (!_pc.hasSkillEffect(tgskill.intValue()))) {
					return false;
				}

				Integer tgpoly = (Integer) L1AttackList.PLNPC.get(Integer.valueOf(npcId));
				if ((tgpoly != null) && (tgpoly.equals(Integer.valueOf(_pc.getTempCharGfx())))) {
					return false;
				}

				boolean dgskill = L1AttackList.DNNPC.containsKey(Integer.valueOf(npcId));
				if (dgskill) {
					Integer[] dgskillids = (Integer[]) L1AttackList.DNNPC.get(Integer.valueOf(npcId));
					for (Integer dgskillid : dgskillids) {
						if (dgskillid.equals(Integer.valueOf(skillId))) {
							return false;
						}
					}
				}
			}

			if (skillId == CANCELLATION) {
				return true;
			}

			if (skillId == SHAPE_CHANGE) {// 變形術
				if (_targetNpc.getLevel() >= 50) {// 怪物等級大於50級
					return false;
				}
			}

			if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
				if ((skillId != WEAPON_BREAK) && (skillId != CANCELLATION)) {
					return false;
				}
			}

			if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
				if ((skillId != WEAPON_BREAK) && (skillId != CANCELLATION)) {
					return false;
				}
			}

			break;
		}

		probability = calcProbability(skillId);// 命中機率計算

		int rnd = _random.nextInt(100) + 1;

		probability = Math.min(probability, 100);

		probability = Math.max(probability, 0);

		if (probability >= rnd) {
			isSuccess = true;
		} else {
			isSuccess = false;
		}

		if (!ConfigAlt.ALT_ATKMSG) {
			return isSuccess;
		}

		switch (_calcType) {
		case 1:
			if ((!_pc.isGm()) && (!_targetPc.isGm())) {
				return isSuccess;
			}

			break;
		case 2:
			if (!_pc.isGm()) {
				return isSuccess;
			}

			break;
		}

		switch (_calcType) {
		case PC_PC:
			if (_pc.isGm()) {
				StringBuilder atkMsg = new StringBuilder();
				atkMsg.append("對PC送出技能: ");
				atkMsg.append(_pc.getName() + ">");
				atkMsg.append(_targetPc.getName() + " ");
				atkMsg.append(isSuccess ? "成功" : "失敗");
				atkMsg.append(" 成功機率:" + probability + "%");

				_pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
			}
			if (_targetPc.isGm()) {
				StringBuilder atkMsg = new StringBuilder();
				atkMsg.append("受到PC技能: ");
				atkMsg.append(_pc.getName() + ">");
				atkMsg.append(_targetPc.getName() + " ");
				atkMsg.append(isSuccess ? "成功" : "失敗");
				atkMsg.append(" 成功機率:" + probability + "%");

				_targetPc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
			}
			break;
		case PC_NPC:
			if (_pc.isGm()) {
				StringBuilder atkMsg = new StringBuilder();
				atkMsg.append("對NPC送出技能: ");
				atkMsg.append(_pc.getName() + ">");
				atkMsg.append(_targetNpc.getName() + " ");
				atkMsg.append(isSuccess ? "成功" : "失敗");
				atkMsg.append(" 成功機率:" + probability + "%");

				_pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
			}
			break;
		}
		return isSuccess;
	}

	private boolean checkZone(int skillId) {
		if ((_pc != null) && (_targetPc != null)) {
			if ((_pc.isSafetyZone()) || (_targetPc.isSafetyZone())) {
				Boolean isBoolean = (Boolean) L1AttackList.NZONE.get(Integer.valueOf(skillId));
				if (isBoolean != null) {
					_pc.sendPackets(new S_ServerMessage("在安全區域無法使用此技能。"));
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkZoneDE(int skillId) {
		if ((_pc != null) && (_targetNpc != null)) {
			if ((_pc.isSafetyZone()) || (_targetNpc.isSafetyZone())) {
				Boolean isBoolean = (Boolean) L1AttackList.NZONE.get(Integer.valueOf(skillId));
				if (isBoolean != null) {
					_pc.sendPackets(new S_ServerMessage("在安全區域無法使用此技能。"));
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 命中機率計算
	 * 
	 * @param skillId
	 * @return
	 */
	private int calcProbability(int skillId) {
		L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
		int attackLevel = _pc.getLevel();
		int defenseLevel = 0;
		int probability = 0;

		switch (_calcType) {
		case PC_PC:
			if (_targetPc.isGm()) {
				return -1;
			}
			if(_targetPc.getId() == _pc.getId()){
				return -1;
			}
			defenseLevel = _targetPc.getLevel();
			break;
		case PC_NPC:
			defenseLevel = _targetNpc.getLevel();
			if ((skillId == RETURN_TO_NATURE) && ((_targetNpc instanceof L1SummonInstance))) {
				L1SummonInstance summon = (L1SummonInstance) _targetNpc;
				defenseLevel = summon.getMaster().getLevel();
			}

			break;
		}

		switch (skillId) {
		/*case ELEMENTAL_FALL_DOWN:// 弱化屬性
		case RETURN_TO_NATURE:// 釋放元素
		// case ENTANGLE:// 地面障礙 改->大地纏繞
		case WIND_SHACKLE:// 風之枷鎖
		case EARTH_BIND:// 大地屏障
		case AREA_OF_SILENCE:// 封印禁地
		case POLLUTE_WATER:// 污濁之水
			probability = (int) (l1skills.getProbabilityDice() / 10.0D * (attackLevel - defenseLevel));
			probability += l1skills.getProbabilityValue();
			probability -= getTargetMr() / 10;
			probability += (_pc.getOriginalMagicHit());// 魔法命中增加成功機率
			break;

		case STRIKER_GALE:// 精準射擊
			// 成功確率 魔法固有係數 × LV差 + 基本確率
			if (attackLevel < defenseLevel) {// 攻擊者等級小於被攻擊者
				probability = ConfigSkill.Precision1;
			} else if (attackLevel == defenseLevel) { // 攻擊者等級 等於 被攻擊者
				probability = ConfigSkill.Precision2;
			} else { // 攻擊者等級大於等於被攻擊者
				probability = ConfigSkill.Precision3;
			}
			break;

		case ERASE_MAGIC:// 魔法消除
			probability = (int) (l1skills.getProbabilityDice() / 10.0D * (attackLevel - defenseLevel));

			if (attackLevel - defenseLevel >= 5) {
				probability += l1skills.getProbabilityValue();
			} else {
				probability += (l1skills.getProbabilityValue() / 2);
			}

			if (_calcType == PC_NPC) {

				probability -= getTargetMr();

			} else {
				probability -= getTargetMr() / 30;
			}

			probability += (_pc.getOriginalMagicHit());// 魔法命中增加成功機率

			if (probability > 50) {
				probability = 50;
			}

			break;

		case SHOCK_STUN:// 衝擊之暈
			if (attackLevel < defenseLevel) {// 攻擊者等級小於被攻擊者
				probability = ConfigSkill.ImpactHalo1;// SRC 20

			} else if (attackLevel == defenseLevel) { // 攻擊者等級 等於 被攻擊者
				probability = ConfigSkill.ImpactHalo2;// SRC NO

			} else {// 攻擊者等級大於等於被攻擊者
				probability = ConfigSkill.ImpactHalo3;// SRC 80
			}
			if (_pc.isKnight() && _pc.getReincarnationSkill()[0] > 0) { // 騎士天賦技能強化沖暈
				probability += _pc.getReincarnationSkill()[0];
			}
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
			break;

		case COUNTER_BARRIER:// 反擊屏障
			// 成功機率 基本確率 + LV差1每 +-1%
			// probability = l1skills.getProbabilityValue() + attackLevel - defenseLevel;
			probability = ConfigSkill.BARRIERchance;
			// 追加2倍智力影響(>> 1: 除) (<< 1: 乘)
			probability += (_pc.getOriginalMagicHit() << 1);
			if (_pc.getMeteLevel() >= 4) { // SRC0808
				probability += ConfigSkill.K4;
			}
			if (_pc.isSkillMastery(COUNTER_BARRIER_VETERAN)) { // 習得反擊屏障：強化
				probability += 3; // 反擊屏障：強化-機率基礎+3
				if (_pc.getLevel() > 85) {
					// 從85等級開始每提升1等級增加1%
					probability += _pc.getLevel() - 85;
				}
				if (ConfigSkill.AddPassiveSkillMsg) {
					_pc.sendPackets(new S_SystemMessage("測試反擊屏障：強化加機率。"));
				}
			}
			break;

		case DARK_BLIND:// 暗黑盲咒
			// int doll = 0;
			// if (_pc.hasSkillEffect(5221)) {
			// doll = 10;
			// }
			// 成功確率 魔法固有係數 × LV差 + 基本確率
			if (attackLevel < defenseLevel) {// 攻擊者等級小於被攻擊者
				probability = ConfigSkill.Damage1;// SRC 20

			} else if (attackLevel == defenseLevel) { // 攻擊者等級 等於 被攻擊者
				probability = ConfigSkill.Damage2;// SRC NO

			} else { // 攻擊者等級大於等於被攻擊者
				probability = ConfigSkill.Damage3;// SRC 80
			}
			break;

		case ARMOR_BREAK:// 破壞盔甲
			int dollBreakLevel = 0;
			if (_pc.getBreakLevel() > 0) { // 破壞盔甲等級增加成功機率
				dollBreakLevel = _pc.getBreakLevel();
			}
			// 成功確率 魔法固有係數 × LV差 + 基本確率
			if (attackLevel < defenseLevel) {// 攻擊者等級小於被攻擊者
				probability = ConfigSkill.Damage1 + dollBreakLevel;// SRC 20

			} else if (attackLevel == defenseLevel) { // 攻擊者等級 等於 被攻擊者
				probability = ConfigSkill.Damage2 + dollBreakLevel;// SRC NO

			} else { // 攻擊者等級大於等於被攻擊者
				probability = ConfigSkill.Damage3 + dollBreakLevel;// SRC 80
			}
			if (_pc.isSkillMastery(ARMOR_DESTINY)) { // 習得破壞盔甲：強化
				probability += 3; // 破壞盔甲：強化-機率基礎+3
				if (_pc.getLevel() > 85) {
					// 從85等級開始每提升1等級增加3%
					probability += (_pc.getLevel() - 85) * 3;
				}
				if (ConfigSkill.AddPassiveSkillMsg) {
					_pc.sendPackets(new S_SystemMessage("測試破壞盔甲：強化加機率。"));
				}
			}
			break;

		case CONFUSION:// 混亂
		case PHANTASM:// 幻想
		case PANIC:// 恐慌
			probability = Random.nextInt(11) + 20;
			probability += (attackLevel - defenseLevel) * 2;
			probability += (_pc.getOriginalMagicHit());// 魔法命中增加成功機率
			break;

		case BONE_BREAK://骷髏毀壞
			probability = Random.nextInt(11) + 20;
			int stunlevel2 = attackLevel + _pc.getStunLevel();// 人物昏迷等級
			probability += (stunlevel2 - defenseLevel) * 2;
			probability += (_pc.getOriginalMagicHit());// 魔法命中增加成功機率
			if (_pc.isIllusionist() && _pc.getReincarnationSkill()[0] > 0) { // 幻術天賦技能毀壞之身
				probability += _pc.getReincarnationSkill()[0];
			}
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
			break;

		case GUARD_BRAKE:// 護衛毀滅
		case RESIST_FEAR:// 恐懼無助
		case THUNDER_GRAB:// 奪命之雷
		case HORROR_OF_DEATH:// 驚悚死神
			probability = (int) (l1skills.getProbabilityDice() / 10.0D * (attackLevel - defenseLevel));
			probability += l1skills.getProbabilityValue();
			probability += (_pc.getOriginalMagicHit());// 魔法命中增加成功機率
			break;

		case SHAPE_CHANGE:// 變形術
			// probability = 3 * (attackLevel - defenseLevel) + 200 - getTargetMr();
			probability = (attackLevel - defenseLevel) + _pc.getSp() + 110 - getTargetMr();
			break;

		case POWERGRIP:// 拘束移動
		case DESPERADO:// 亡命之徒	
			probability = (int) (l1skills.getProbabilityDice() / 10.0D * (attackLevel - defenseLevel));
			probability += l1skills.getProbabilityValue();
			probability += _pc.getOriginalMagicHit();// 魔法命中增加成功機率
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
			// probability += _pc.get_FearLevel();// 恐怖等級增加成功機率
			break;*/

		case SHOCK_STUN:// 衝擊之暈
			if (attackLevel >= defenseLevel) {
				// 攻擊者等級 高於等於 被攻擊者等級時，施放機率為100%
				probability = 100;
			} else if (attackLevel < defenseLevel) {
				// 攻擊者等級 低於 被攻擊者等級時，每低1級就減少機率5
				probability = (int) (100 - ((defenseLevel - attackLevel) * 5));
			}
			if (_pc.isKnight() && _pc.getReincarnationSkill()[0] > 0) { // 騎士天賦技能強化沖暈
				probability += _pc.getReincarnationSkill()[0];
			}
            probability += _pc.getStunLevel();// 人物昏迷等級
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
            probability += _pc.getHitTechnology() + _pc.getHitAll(); // 技術命中+全部命中
			break;

		case COUNTER_BARRIER:// 反擊屏障
			// 成功機率 基本確率 + LV差1每 +-1%
			// probability = l1skills.getProbabilityValue() + attackLevel - defenseLevel;
			probability = ConfigSkill.BARRIERchance;
			// 追加2倍智力影響(>> 1: 除) (<< 1: 乘)
			// probability += (_pc.getOriginalMagicHit() << 1);
			if (_pc.getMeteLevel() >= 4) { // SRC0808
				probability += ConfigSkill.K4;
			}
			if (_pc.isSkillMastery(COUNTER_BARRIER_VETERAN)) { // 習得反擊屏障：強化
				probability += 3; // 反擊屏障：強化-機率基礎+3
				if (_pc.getLevel() > 85) {
					// 從85等級開始每提升1等級增加1%
					probability += _pc.getLevel() - 85;
				}
				if (ConfigSkill.AddPassiveSkillMsg) {
					_pc.sendPackets(new S_SystemMessage("測試反擊屏障：強化加機率。"));
				}
			}
			break;

		case ELEMENTAL_FALL_DOWN:// 弱化屬性
		case RETURN_TO_NATURE:// 釋放元素
		case ERASE_MAGIC:// 魔法消除
		case EARTH_BIND:// 大地屏障
		case AREA_OF_SILENCE:// 封印禁地
		case WIND_SHACKLE:// 風之枷鎖
		case POLLUTE_WATER:// 污濁之水
		case STRIKER_GALE:// 精準射擊
		case DARK_BLIND:// 暗黑盲咒
			if (attackLevel >= defenseLevel) {
				// 攻擊者等級 高於等於 被攻擊者等級時，施放機率為100%
				probability = 100;
			} else if (attackLevel < defenseLevel) {
				// 攻擊者等級 低於 被攻擊者等級時，每低1級就減少機率5
				probability = (int) (100 - ((defenseLevel - attackLevel) * 5));
			}
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
            probability += _pc.getHitElf() + _pc.getHitAll(); // 精靈命中+全部命中
			break;

		case ARMOR_BREAK:// 破壞盔甲
			if (attackLevel >= defenseLevel) {
				// 攻擊者等級 高於等於 被攻擊者等級時，施放機率為100%
				probability = 100;
			} else if (attackLevel < defenseLevel) {
				// 攻擊者等級 低於 被攻擊者等級時，每低1級就減少機率5
				probability = (int) (100 - ((defenseLevel - attackLevel) * 5));
			}
			if (_pc.isSkillMastery(ARMOR_DESTINY)) { // 習得破壞盔甲：強化
				probability += 3; // 破壞盔甲：強化-機率基礎+3
				if (_pc.getLevel() > 85) {
					// 從85等級開始每提升1等級增加3%
					probability += (_pc.getLevel() - 85) * 3;
				}
				if (ConfigSkill.AddPassiveSkillMsg) {
					_pc.sendPackets(new S_SystemMessage("測試破壞盔甲：強化加機率。"));
				}
			}
            probability += _pc.getBreakLevel(); // 破壞盔甲等級增加成功機率
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
            probability += _pc.getHitElf() + _pc.getHitAll(); // 精靈命中+全部命中
			break;

		case CONFUSION:// 混亂
		case PHANTASM:// 幻想
		case PANIC:// 恐慌
			if (attackLevel >= defenseLevel) {
				// 攻擊者等級 高於等於 被攻擊者等級時，施放機率為100%
				probability = 100;
			} else if (attackLevel < defenseLevel) {
				// 攻擊者等級 低於 被攻擊者等級時，每低1級就減少機率5
				probability = (int) (100 - ((defenseLevel - attackLevel) * 5));
			}
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
            probability += _pc.getHitDragon() + _pc.getHitAll(); // 龍屬命中+全部命中
			break;

		case BONE_BREAK://骷髏毀壞
			if (attackLevel >= defenseLevel) {
				// 攻擊者等級 高於等於 被攻擊者等級時，施放機率為100%
				probability = 100;
			} else if (attackLevel < defenseLevel) {
				// 攻擊者等級 低於 被攻擊者等級時，每低1級就減少機率5
				probability = (int) (100 - ((defenseLevel - attackLevel) * 5));
			}
			if (_pc.isIllusionist() && _pc.getReincarnationSkill()[0] > 0) { // 幻術天賦技能毀壞之身
				probability += _pc.getReincarnationSkill()[0];
			}
            probability += _pc.getStunLevel();// 人物昏迷等級
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
            probability += _pc.getHitDragon() + _pc.getHitAll(); // 龍屬命中+全部命中
			break;

		case GUARD_BRAKE:// 護衛毀滅
		case RESIST_FEAR:// 恐懼無助
		case THUNDER_GRAB:// 奪命之雷
		case HORROR_OF_DEATH:// 驚悚死神
			if (attackLevel >= defenseLevel) {
				// 攻擊者等級 高於等於 被攻擊者等級時，施放機率為100%
				probability = 100;
			} else if (attackLevel < defenseLevel) {
				// 攻擊者等級 低於 被攻擊者等級時，每低1級就減少機率5
				probability = (int) (100 - ((defenseLevel - attackLevel) * 5));
			}
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
            probability += _pc.getHitDragon() + _pc.getHitAll(); // 龍屬命中+全部命中
			break;

		case SHAPE_CHANGE:// 變形術
			// probability = 3 * (attackLevel - defenseLevel) + 200 - getTargetMr();
			probability = (attackLevel - defenseLevel) + _pc.getSp() + 110 - getTargetMr();
			break;

		case POWERGRIP:// 拘束移動
		case DESPERADO:// 亡命之徒	
			if (attackLevel >= defenseLevel) {
				// 攻擊者等級 高於等於 被攻擊者等級時，施放機率為100%
				probability = 100;
			} else if (attackLevel < defenseLevel) {
				// 攻擊者等級 低於 被攻擊者等級時，每低1級就減少機率5
				probability = (int) (100 - ((defenseLevel - attackLevel) * 5));
			}
            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
            probability += _pc.getHitHorror() + _pc.getHitAll(); // 恐怖命中+全部命中
			break;

		default:
			int dice2 = l1skills.getProbabilityDice();
			int diceCount2 = 0;

			diceCount2 = getMagicBonus() + getMagicLevel();

			diceCount2 = Math.max(diceCount2, 1);

			for (int i = 0; i < diceCount2; i++) {
				if (dice2 > 0) {
					probability += (_random.nextInt(dice2) + 1);
				}
			}

			probability += (_pc.getMagicHit() + L1ClassFeature.calcIntMagicHit(_pc.getInt(), _pc.getBaseInt()));// 魔法命中增加成功機率

			probability -= getTargetMr();

			/*
			 * if (getTargetMr() >= 156) {//魔防超過156免疫法師負面魔法 probability = 0; }
			 * /
			//int Chances = _pc.getInt() - ConfigSkill.IntBigger / ConfigSkill.IntRepeated;

			if (_calcType == PC_PC
					&& (skillId == 27 // 壞物術
					|| skillId == 29  // 緩速術
					|| skillId == 33  // 木乃伊的詛咒
					|| skillId == 36  // 迷魅術
					|| skillId == 39  // 魔力奪取
					|| skillId == 44  // 魔法相消術
					|| skillId == 50  // 冰矛圍籬
					|| skillId == 56  // 疾病術
					|| skillId == 64  // 魔法封印
					|| skillId == 66  // 沉睡之霧
					|| skillId == 71  // 藥水霜化術
					// || skillId == MASS_SLOW  // 集體緩速術 改->冰霜彗星
					|| skillId == 202) // 混亂
					&& _pc.getId() != _targetPc.getId()) {
				if (ConfigSkill.AddInt) {
					probability -= getTargetMr() / 3;
				} else {
					probability -= getTargetMr();
					if (probability < 0) {
						probability = 0;
					}
				}
				if (_calcType == PC_PC && probability <= 0 && _pc.getInt() > ConfigSkill.IntBigger) {
					if (ConfigSkill.AddInt) {
						if (_pc.getInt() > ConfigSkill.IntBigger && _pc.getInt() < ConfigSkill.IntLess) {
							//probability += Chances;
							for (int s = ConfigSkill.IntBigger; s < _pc.getInt(); s++) {
								probability += ConfigSkill.IntRepeated;
							}
						}
						if (_pc.getInt() > ConfigSkill.IntBigger && _pc.getInt() >= ConfigSkill.IntLess) {
							probability = 100;
						}
					}
				}
			}*/
			
			if (skillId == TAMING_MONSTER) { // 迷魅術
				double probabilityRevision = 1;
				if ((_targetNpc.getMaxHp() * 1 / 4) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.3;
				} else if ((_targetNpc.getMaxHp() * 2 / 4) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.2;
				} else if ((_targetNpc.getMaxHp() * 3 / 4) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.1;
				}
				probability *= probabilityRevision;
			}

			break;
		}

		switch (_calcType) {
		case PC_PC:
			switch (skillId) {
			case DEATH_HEAL:
				probability = ConfigSkill.DEATH_HEAL;
				break;
			/*case EARTH_BIND:// 大地屏障
			case THUNDER_GRAB:// 奪命之雷
			case POWERGRIP:// 拘束移動
				probability -= _targetPc.getRegistSustain();
				break;
			case SHOCK_STUN:// 衝擊之暈
			case BONE_BREAK:// 骷髏毀壞
				probability -= _targetPc.getRegistStun();
				if (probability < 10) {
					probability = 10;
				}
				break;
			case CURSE_PARALYZE: // 木乃伊的詛咒
				probability -= _targetPc.getRegistStone();
				break;
			case FOG_OF_SLEEPING: // 沉睡之霧
			case DARK_BLIND: // 暗黑盲咒
			case PHANTASM: // 幻想
				probability -= _targetPc.getRegistSleep();
				break;
			case ICE_LANCE: // 冰矛圍籬
			case FREEZING_BLIZZARD: // 寒冰尖刺
				probability -= _targetPc.getRegistFreeze();
				break;
			case CURSE_BLIND: // 闇盲咒術
			case DARKNESS: // 黑闇之影
				probability -= _targetPc.getRegistBlind();
				break;*/

			case SHOCK_STUN:// 衝擊之暈
				// 技術耐性 + 全部耐性
				probability -= _targetPc.getRegistTechnology() + _targetPc.getRegistAll();
				break;

			case ELEMENTAL_FALL_DOWN:// 弱化屬性
			case RETURN_TO_NATURE:// 釋放元素
			case ERASE_MAGIC:// 魔法消除
			case EARTH_BIND:// 大地屏障
			case AREA_OF_SILENCE:// 封印禁地
			case WIND_SHACKLE:// 風之枷鎖
			case POLLUTE_WATER:// 污濁之水
			case STRIKER_GALE:// 精準射擊
			case DARK_BLIND:// 暗黑盲咒
			case ARMOR_BREAK:// 破壞盔甲
				// 精靈耐性 + 全部耐性
				probability -= _targetPc.getRegistElf() + _targetPc.getRegistAll();
				break;

			case THUNDER_GRAB:// 奪命之雷
			case BONE_BREAK:// 骷髏毀壞
			case GUARD_BRAKE:// 護衛毀滅
			case RESIST_FEAR:// 恐懼無助
			case HORROR_OF_DEATH:// 驚悚死神
			case CONFUSION:// 混亂
			case PHANTASM:// 幻想
			case PANIC:// 恐慌
				// 龍屬耐性 + 全部耐性
				probability -= _targetPc.getRegistDragon() + _targetPc.getRegistAll();
				break;

			case POWERGRIP:// 拘束移動
			case DESPERADO:// 亡命之徒
				// 恐怖耐性 + 全部耐性
				probability -= _targetPc.getRegistHorror() + _targetPc.getRegistAll();
				break;
			}
			break;
		}

		// 負面技能機率設置DB化
		if (_calcType == PC_PC && _pc.getId() != _targetPc.getId()) {
			if (l1skills.getOpen() > 0) {
				if (l1skills.getProbability_Lv1() != 0
						&& l1skills.getProbability_Lv2() != 0
						&& l1skills.getProbability_Lv3() != 0
				) {
					if (attackLevel < defenseLevel) {
						probability = l1skills.getProbability_Lv1(); // 攻擊者等級小於被攻擊者

					} else if (attackLevel == defenseLevel) {
						probability = l1skills.getProbability_Lv2(); // 攻擊者等級等於被攻擊者

					} else {
						probability = l1skills.getProbability_Lv3(); // 攻擊者等級大於被攻擊者
					}
					// 攻擊者智力加機率(設0=不增加 設1=目前智力除以1 設10=目前智力除以10)
					if (l1skills.get_intel_add_probability() != 0) {
						// 智力增加機率上限值 (設0=不限 設50=智力增加機率超過50等於50)
			            int max = l1skills.get_intel_add_probability_max();
			            int pc_int = _pc.getInt();
			            int pc_intprobability = (int) (pc_int / l1skills.get_intel_add_probability());
			            if (max != 0 && pc_intprobability > max) {
			            	pc_intprobability = max;
			            }
			            probability += pc_intprobability;
			        }
					// 攻擊者魔法命中加機率(設0=不增加 設1=目前魔法命中除以1 設10=目前魔法命中除以10)
					if (l1skills.get_magic_hit_probability() != 0) {
						int pc_magicHit = _pc.getMagicHit() + L1ClassFeature.calcIntMagicHit(_pc.getInt(), _pc.getBaseInt());
						probability += (int) (pc_magicHit / l1skills.get_magic_hit_probability());
					}
					// 轉生增加機率
					if (l1skills.get_level_met() != 0 && l1skills.get_level_met_probability() != 0) {
						if (_pc.getMeteLevel() >= l1skills.get_level_met()) {
							probability += l1skills.get_level_met_probability();
						}
					}
					// 技術命中加機率
					if (l1skills.getHitTechnology() != 0) {
			            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
						probability += _pc.getHitTechnology() + _pc.getHitAll();
					}
					// 精靈命中加機率
					if (l1skills.getHitElf() != 0) {
			            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
						probability += _pc.getHitElf() + _pc.getHitAll();
					}
					// 龍屬命中加機率
					if (l1skills.getHitDragon() != 0) {
			            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
						probability += _pc.getHitDragon() + _pc.getHitAll();
					}
					// 恐怖命中加機率
					if (l1skills.getHitHorror() != 0) {
			            probability += _pc.getImpactUp(); // 幻術師新技能 衝突強化
						probability += _pc.getHitHorror() + _pc.getHitAll();
					}
					// 特定技能有額外判斷的需補上
					if (l1skills.getSkillId() == SHOCK_STUN) { // 衝擊之暈
						if (_pc.isKnight() && _pc.getReincarnationSkill()[0] > 0) { // 騎士天賦技能強化沖暈
							probability += _pc.getReincarnationSkill()[0];
						}
			            probability += _pc.getStunLevel();// 人物昏迷等級
					}
					if (l1skills.getSkillId() == COUNTER_BARRIER) { // 反擊屏障
						if (_pc.getMeteLevel() >= 4) { // SRC0808
							probability += ConfigSkill.K4;
						}
						if (_pc.isSkillMastery(COUNTER_BARRIER_VETERAN)) { // 習得反擊屏障：強化
							probability += 3; // 反擊屏障：強化-機率基礎+3
							if (_pc.getLevel() > 85) {
								// 從85等級開始每提升1等級增加1%
								probability += _pc.getLevel() - 85;
							}
							if (ConfigSkill.AddPassiveSkillMsg) {
								_pc.sendPackets(new S_SystemMessage("測試反擊屏障：強化加機率1。"));
							}
						}
					}
					if (l1skills.getSkillId() == ARMOR_BREAK) { // 破壞盔甲
						if (_pc.isSkillMastery(ARMOR_DESTINY)) { // 習得破壞盔甲：強化
							probability += 3; // 破壞盔甲：強化-機率基礎+3
							if (_pc.getLevel() > 85) {
								// 從85等級開始每提升1等級增加3%
								probability += (_pc.getLevel() - 85) * 3;
							}
							if (ConfigSkill.AddPassiveSkillMsg) {
								_pc.sendPackets(new S_SystemMessage("測試破壞盔甲：強化加機率1。"));
							}
						}
			            probability += _pc.getBreakLevel(); // 破壞盔甲等級增加成功機率
					}
					if (l1skills.getSkillId() == BONE_BREAK) { // 骷髏毀壞
						if (_pc.isIllusionist() && _pc.getReincarnationSkill()[0] > 0) { // 幻術天賦技能毀壞之身
							probability += _pc.getReincarnationSkill()[0];
						}
			            probability += _pc.getStunLevel();// 人物昏迷等級
					}

					// 被攻擊者扣機率

					// 被攻擊者魔防扣機率百分比
					if (l1skills.getProbability_Mr() != 0) {
						probability -= (int) (getTargetMr() / l1skills.getProbability_Mr());
					}
					// 被攻擊者技術耐性扣機率
					if (l1skills.getRegistTechnology() != 0) {
						probability -= _targetPc.getRegistTechnology() + _targetPc.getRegistAll();
					}
					// 被攻擊者精靈耐性扣機率
					if (l1skills.getRegistElf() != 0) {
						probability -= _targetPc.getRegistElf() + _targetPc.getRegistAll();
					}
					// 被攻擊者龍屬耐性扣機率
					if (l1skills.getRegistDragon() != 0) {
						probability -= _targetPc.getRegistDragon() + _targetPc.getRegistAll();
					}
					// 被攻擊者恐怖耐性扣機率
					if (l1skills.getRegistHorror() != 0) {
						probability -= _targetPc.getRegistHorror() + _targetPc.getRegistAll();
					}
				}
			}
		}
		// 負面技能機率設置DB化end

		// _pc.sendPackets(new S_SystemMessage("機率：" + probability));

		return probability;
	}

	/**
	 * 魔法傷害計算
	 */
	public int calcMagicDamage(int skillId) {
		int damage = 0;
		switch (_calcType) {
		case 1:
			damage = calcPcMagicDamage(skillId);
			break;
		case 2:
			damage = calcNpcMagicDamage(skillId);
			break;
		}

		//damage = calcMrDefense(damage);
		if (_pc.isWizard() && _pc.getReincarnationSkill()[0] > 0 && RandomArrayList.getInc(100, 1) > 100 - _pc.getReincarnationSkill()[0] * 2) { // 法師天賦技能致命一擊
			_pc.sendPackets(new S_SystemMessage(L1SystemMessage.ShowMessage(8041))); // 發動天賦技能 致命一擊 無視對方抗魔。
		} else {
			damage = calcMrDefense(damage);
		}
		return damage;
	}

	/**
	 * 攻擊PC時的魔法傷害計算
	 * 
	 * @param skillId
	 * @return
	 */
	private int calcPcMagicDamage(int skillId) {
		if (_targetPc == null) {
			return 0;
		}

		if (dmg0(_targetPc)) {
			return 0;
		}

		if (calcEvasion()) {
			return 0;
		}

		int dmg = 0;
		if (skillId == FINAL_BURN) {
			dmg = _pc.getCurrentMp();
		} else {
			dmg = calcMagicDiceDamage(skillId);// 魔法基礎傷害計算
			dmg = (int) (dmg * (getLeverage() / 10.0D));
		}

		dmg -= _targetPc.getDamageReductionByArmor() + _targetPc.getMagicDmgReduction();// 裝備傷害減免

		dmg -= _targetPc.dmgDowe();// 隨機傷害減免

		if (_targetPc.getClanid() != 0) {
			dmg = (int) (dmg - getDamageReductionByClan(_targetPc));// 血盟魔法減傷
		}
		// 護甲身軀
		if (_targetPc.isWarrior() && _targetPc.isARMORGARDE()) {
			// dmg -= _targetPc.getAc() / 10;
			dmg -= (Math.abs(_targetPc.getAc()) / 10);
		}
		// 增幅防禦 傷害減免
		if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
			// if (_targetPc.getLevel() >= 50 && ConfigSkill.KnightYN &&
			// _targetPc.getMeteLevel() >= 0) {
			// dmg -= Math.min((_targetPc.getLevel() - 50) / 5 + 1, 7);
			// }
			int targetPcLvl = Math.max(_targetPc.getLevel(), 50);
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}
		if (skillId == METEOR_STRIKE || skillId == DISINTEGRATE) { // SRC0808
			if (_pc.getMeteLevel() >= 4) {
				dmg *= ConfigSkill.W4;
			}
		}

		if (dmg > _targetPc.getCurrentHp()) { // 精靈新技能 魔力護盾
			if (_targetPc.isElf() && _targetPc.hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
				if (dmg > _targetPc.getCurrentHp() + _targetPc.getCurrentMp()) {
					dmg = _targetPc.getCurrentHp() + _targetPc.getCurrentMp();
				}
			} else {
				dmg = _targetPc.getCurrentHp();
			}
		}

		// dmg += _pc.getAntiDamageReduction(); // 無視減免 -> 暫改直接加傷害
		// 無視減免
		// if (_pc.getAntiDamageReduction() > 0) {
		// // 被攻擊玩家的減傷值
		// int targetReduc = _targetPc.getDamageReductionByArmor() +
		// _targetPc.getMagicDmgReduction();
		// if (targetReduc > _pc.getAntiDamageReduction()) {
		// targetReduc = _pc.getAntiDamageReduction();
		// }
		// if (targetReduc > 0) {
		// dmg += targetReduc;
		// } else {
		// dmg += 0;
		// }
		// // _pc.sendPackets(new S_SystemMessage("測試targetReduc：" + targetReduc));
		// }

		// 其他傷害減免
		boolean dmgX2 = false;
		if ((!_targetPc.getSkillisEmpty()) && (_targetPc.getSkillEffect().size() > 0)) {
			try {
				for (Integer key : _targetPc.getSkillEffect()) {
					Integer integer = (Integer) L1AttackList.SKD3.get(key);

					if (integer != null) {
						if (integer.equals(key)) {
							dmgX2 = true;
						} else {
							dmg += integer.intValue();
						}
					}
				}
			} catch (ConcurrentModificationException localConcurrentModificationException) {
			} catch (Exception e) {
				_log.error(e.getLocalizedMessage(), e);
			}
		}

		L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
		if (l1skills.getTarget().equals("attack") && (l1skills.getArea() == 0)) {// 單體攻擊魔法
			dmg += AttrAmuletEffect();// 火焰之暈
		}

		if (dmgX2) {// 聖界減半
			dmg /= 2;
		}

		if (_targetPc.hasSkillEffect(LUCIFER)) { // 暗影屏障-所承受的傷害減少10%
			dmg -= (dmg * 0.1);
		}

		if ((_targetPc.hasSkillEffect(6685)// 水龍之魔眼
				|| _targetPc.hasSkillEffect(6687)// 生命之魔眼
				|| _targetPc.hasSkillEffect(6688)// 誕生之魔眼
				|| _targetPc.hasSkillEffect(6689))// 形象之魔眼
				&& (_random.nextInt(100) < 10)) {// 魔眼10%機率減傷
			dmg /= 2;
			_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 6320));
		}

		// 鏡反射
		/*if ((_targetPc.hasSkillEffect(COUNTER_MIRROR)) && (_calcType == PC_PC) && (_targetPc.getWis() >= _random.nextInt(100))) {
			_pc.sendPacketsAll(new S_DoActionGFX(_pc.getId(), 2));
			_pc.receiveDamage(_targetPc, dmg, false, false);
			_pc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 4395));
			dmg = 0;
			_targetPc.killSkillEffectTimer(COUNTER_MIRROR);
		}*/

		// TODO 戰士魔法
		int TiTanHp = 0;
		if (_targetPc.get_TiTanHp() > 0) { // 泰坦系列技能發動HP區間%增加
			TiTanHp += _targetPc.get_TiTanHp();
		}
		if (_targetPc.isWarrior() && _targetPc.getMeteLevel() >= 4) {
			TiTanHp += ConfigSkill.WHYW4;
		}

		if (_targetPc.isWarrior() && (_targetPc.getCurrentHp() < ((_targetPc.getMaxHp() / 100)
				* (ConfigSkill.Warrior_Magic + TiTanHp + _targetPc.getRisingUp())// 血量低於40%
		        )) && _targetPc.isCrystal()// 足夠魔法結晶體
				&& _targetPc.isTITANMAGIC() && (_random.nextInt(100) < ConfigSkill.isPassive_Tatin_Magic)) { // SRC0808
			dmg = 0;
			actionTitan();
			commitTitan(_targetPc.colcTitanDmg());
		}

		return Math.max(dmg, 0);
	}

	/**
	 * 攻擊NPC時的魔法傷害計算
	 * 
	 * @param skillId
	 * @return
	 */
	private int calcNpcMagicDamage(int skillId) {
		if (_targetNpc == null) {
			return 0;
		}

		if (dmg0(_targetNpc)) {
			return 0;
		}

		/*
		 * if(!checkAttackError(_targetNpc,_pc)){ return 0; }
		 */

		int npcId = _targetNpc.getNpcTemplate().get_npcId();
		Integer tgskill = (Integer) L1AttackList.SKNPC.get(Integer.valueOf(npcId));
		if ((tgskill != null) && (!_pc.hasSkillEffect(tgskill.intValue()))) {
			return 0;
		}

		Integer tgpoly = (Integer) L1AttackList.PLNPC.get(Integer.valueOf(npcId));
		if ((tgpoly != null) && (tgpoly.equals(Integer.valueOf(_pc.getTempCharGfx())))) {
			return 0;
		}

		int dmg = 0;
		if (skillId == FINAL_BURN) {
			dmg = _pc.getCurrentMp();
		} else {
			dmg = calcMagicDiceDamage(skillId);
			dmg = (int) (dmg * (getLeverage() / 10.0D));
		}

		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
		if (castleId > 0) {
			isNowWar = ServerWarExecutor.get().isNowWar(castleId);
		}

		boolean isPet = false;
		if ((_targetNpc instanceof L1PetInstance)) {
			isPet = true;
			if (_targetNpc.getMaster().equals(_pc)) {
				dmg = 0;
			}
		}
		if ((_targetNpc instanceof L1SummonInstance)) {
			L1SummonInstance summon = (L1SummonInstance) _targetNpc;
			if (summon.isExsistMaster()) {
				isPet = true;
			}
			if (_targetNpc.getMaster().equals(_pc)) {
				dmg = 0;
			}
		}

		if (skillId == METEOR_STRIKE || skillId == DISINTEGRATE) { // SRC0808
			if (_pc.getMeteLevel() >= 4) {
				dmg *= ConfigSkill.W4;
			}
		}

		if (dmg > _pc.getCurrentHp()) { // 精靈新技能 魔力護盾
			if (_pc.isElf() && _pc.hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
				if (dmg > _pc.getCurrentHp() + _pc.getCurrentMp()) {
					dmg = _pc.getCurrentHp() + _pc.getCurrentMp();
				}
			} else {
				dmg = _pc.getCurrentHp();
			}
		}

		if ((!isNowWar) && (isPet) && (dmg != 0)) {
			dmg /= 8;
		}

		L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
		if (l1skills.getTarget().equals("attack") && (l1skills.getArea() == 0)) {// 單體攻擊魔法
			dmg += AttrAmuletEffect();// 火焰之暈
		}

		/*
		 * if (l1skills.getTarget().equals("none") && (l1skills.getArea() > 0))
		 * {// 無方向範圍魔法 if (dmg >= _targetNpc.getCurrentHp()) {// 如果傷害大於等於目前HP
		 * dmg = _targetNpc.getCurrentHp() - 1;// 變更傷害為目前HP-1(避免使用範圍魔法掛機) } }
		 */

		if (_targetNpc.hasSkillEffect(IMMUNE_TO_HARM)) {// 聖界減傷
			dmg /= 2;
		}

		// 鏡反射
		/*if ((_targetNpc.hasSkillEffect(COUNTER_MIRROR)) && (_calcType == PC_NPC)
				&& (_targetNpc.getWis() >= _random.nextInt(100))) {
			_pc.sendPacketsAll(new S_DoActionGFX(_pc.getId(), 2));
			_pc.receiveDamage(_targetNpc, dmg, false, false);
			_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 4395));
			dmg = 0;
			_targetNpc.killSkillEffectTimer(COUNTER_MIRROR);
		}*/

		// 吉爾塔斯-全體鏡反射
		if ((_targetNpc.hasSkillEffect(11059)) && (_calcType == PC_NPC)) {
			_pc.sendPacketsAll(new S_DoActionGFX(_pc.getId(), 2));
			_pc.receiveDamage(_targetNpc, dmg, false, false);
			_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 4395));
			dmg = 0;
		}

		return dmg;
	}

	/** 火焰之暈傷害計算 */
	private int AttrAmuletEffect() {
		int dmg = 0;
		switch (_calcType) {
		case PC_PC:
			if (_pc.hasSkillEffect(FIRESTUN)) {
				if (_random.nextInt(100) < 5) {// 5%機率傷害+50
					dmg = 50;
					_targetPc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 13542));
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetPc, 0, (int)dmg));
				}
			} else if (_pc.hasSkillEffect(TRUEFIRESTUN)) {
				if (_random.nextInt(100) < 5) {// 7%機率傷害+80
					dmg = 80;
					_targetPc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 13542));
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetPc, 0, (int)dmg));
				}
			}
			else if (_pc.hasSkillEffect(MOONATTACK)) {
				if (_random.nextInt(100) < 5) {// 7%機率傷害+120
					dmg = 120;
					_targetPc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 13989));
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetPc, 0, (int)dmg));
				}
			}
			break;
		case PC_NPC:
			if (_pc.hasSkillEffect(FIRESTUN)) {
				if (_random.nextInt(100) < 5) {// 5%機率傷害+50
					dmg = 50;
					_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 13542));
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetNpc, 0, (int)dmg));
				}
			} else if (_pc.hasSkillEffect(TRUEFIRESTUN)) {
				if (_random.nextInt(100) < 5) {// 7%機率傷害+80
					dmg = 80;
					_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 13542));
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetNpc, 0, (int)dmg));
				}
			}
			else if (_pc.hasSkillEffect(MOONATTACK)) {
				if (_random.nextInt(100) < 5) {// 7%機率傷害+120
					dmg = 120;
					_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 13989));
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetNpc, 0, (int)dmg));
					
				}
			}
			break;
		}
		// System.out.println("dmg ==" + dmg);
		return dmg;
	}

	/**
	 * 魔法基礎傷害計算
	 * 
	 * @param skillId
	 * @return
	 */
	private int calcMagicDiceDamage(int skillId) {
		L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
		int dice = l1skills.getDamageDice();// 骰面
		int diceCount = l1skills.getDamageDiceCount();// 骰數
		int value = l1skills.getDamageValue();// 固定增加傷害
		int magicDamage = 0;
		int charaIntelligence = 0;

		for (int i = 0; i < diceCount; i++) {
			magicDamage += _random.nextInt(dice) + 1;
		}
		magicDamage += value;

		if (_pc.getClanid() != 0) {
			magicDamage = (int) (magicDamage + getDamageUpByClan(_pc));// 血盟魔法增傷
		}

		int spByItem = getTargetSp();// 計算施展者額外增加魔攻

		charaIntelligence = Math.max(_pc.getInt() + spByItem - 12, 1);

		double attrDeffence = calcAttrResistance(l1skills.getAttr());

		double coefficient = Math.max(1.0D - attrDeffence + charaIntelligence * 3.0D / 32.0D, 0.0D);

		magicDamage = (int) (magicDamage * coefficient);

		int rnd = _random.nextInt(100) + 1;

		// 魔法暴擊率
		int magicCritical = L1ClassFeature.calcIntMagicCritical(_pc.getInt(), _pc.getBaseInt())
				+ _pc.getOriginalMagicCritical();

		if ((l1skills.getSkillLevel() <= 6 || skillId == DISINTEGRATE) && // 小於六級魔法 或是究極光裂術
				// (rnd <= 10 + _pc.getOriginalMagicCritical())) {// 魔法爆擊率
				(rnd <= magicCritical)) {// 魔法爆擊率
			double criticalCoefficient = 1.5D;// 1.5倍傷害
			magicDamage = (int) (magicDamage * criticalCoefficient);
			_pc.setMagicCritical(true);// 魔法爆擊狀態
		}

		magicDamage += _pc.getMagicDmgModifier() + L1ClassFeature.calcIntMagicDmg(_pc.getInt(), _pc.getBaseInt());

		return magicDamage;
	}

	/**
	 * 治癒魔法的處理
	 */
	public int calcHealing(int skillId) {
		// L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
		// int dice = l1skills.getDamageDice();
		// int value = l1skills.getDamageValue();
		// int magicDamage = 0;
		// int magicBonus = getMagicBonus();
		//
		// int diceCount = value + magicBonus;
		// for (int i = 0; i < diceCount; i++) {
		// magicDamage += _random.nextInt(dice) + 1;
		// }
		//
		// double alignmentRevision = 1.0D;
		// if (getLawful() > 0) {
		// alignmentRevision += getLawful() / 32768.0D;
		// }
		//
		// magicDamage = (int) (magicDamage * alignmentRevision);
		//
		// magicDamage = (int) (magicDamage * (getLeverage() / 10.0D));
		//
		// if (_pc.isWizard() || _pc.isIllusionist()) {
		// magicDamage *= 1.0;
		// } else if (_pc.isElf() || _pc.isDragonKnight()) {
		// magicDamage *= 0.8;
		// } else if (_pc.isDarkelf() || _pc.isCrown()) {
		// magicDamage *= 0.5;
		// } else if (_pc.isKnight() || _pc.isWarrior()) {
		// magicDamage *= 0.3;
		// }
		//
		// return magicDamage;

		L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;

		if (skillId != NATURES_BLESSING) {
			int magicBonus = (getMagicBonus()-2);
			if (magicBonus > 10) {
				magicBonus = 10;
			}

			int diceCount = value + magicBonus;
			for (int i = 0; i < diceCount; i++) {
				magicDamage += (_random.nextInt(dice) + 1);
			}
		} else {
			int Int = 0;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				Int = _pc.getInt();
			} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
				Int = _npc.getInt();
			}
			if (Int < 12)
				Int = 12;
			for (int i = 12; i <= Int; i++) {
				if (i == 12)
					magicDamage += (100 + _random.nextInt(80));
				else if (i >= 13 && i <= 17)
					magicDamage += (3 + _random.nextInt(2));
				else if (i >= 18 && i <= 25)
					magicDamage += (10 + _random.nextInt(6));
				else if (i >= 26)
					magicDamage += (1 + _random.nextInt(2));
			}
			magicDamage /= 2.2;
		}

		double alignmentRevision = 1.0;
		if (getLawful() > 0) {
			alignmentRevision += (getLawful() / 32768.0);
		}

		magicDamage *= alignmentRevision;

		if (skillId != NATURES_BLESSING){
			magicDamage = (magicDamage * getLeverage()) / 10;
		}

		if (skillId == NATURES_BLESSING && _pc.getMeteLevel() >= 4) {  //SRC0808
			magicDamage *= ConfigSkill.E4NATURES_BLESSING;
		}

		if (_pc.isElf() && _pc.getReincarnationSkill()[0] > 0
				&& (skillId == HEAL || skillId == EXTRA_HEAL || skillId == GREATER_HEAL || skillId == NATURES_BLESSING)) { // 妖精天賦技能神聖祝福
			magicDamage += (int) (magicDamage * (_pc.getReincarnationSkill()[0] * 0.05)); // 每+1點補血量+5%
		}
		
		if (_pc.isWizard() && _pc.getReincarnationSkill()[1] > 0
				&& RandomArrayList.getInc(100, 1) <= 10 // 機率固定10%
				&& (skillId == HEAL || skillId == EXTRA_HEAL || skillId == GREATER_HEAL || skillId == FULL_HEAL)) { // 法師天賦技能療愈風暴
			magicDamage += (int) (magicDamage * (_pc.getReincarnationSkill()[1] * 0.1)); // 每+1點補血量+10%
		}

		return magicDamage;
	}

	/**
	 * 魔防減傷的計算
	 * 
	 * @param dmg
	 * @return
	 */
	private int calcMrDefense(int dmg) {
		int mr = getTargetMr();

		double mrFloor = 0;

		int magicHit = 0;
		// 7.6智力魔法命中補正
		magicHit = _pc.getMagicHit() + L1ClassFeature.calcIntMagicHit(_pc.getInt(), _pc.getBaseInt());

		if (mr < 100) {
			mrFloor = Math.floor((mr - magicHit) / 2);
		} else if (mr >= 100) {
			mrFloor = Math.floor((mr - magicHit) / 10);
		}
		double mrCoefficient = 0;
		if (mr < 100) {
			mrCoefficient = 1 - 0.01 * mrFloor;
		} else if (mr >= 100) {
			mrCoefficient = 0.6 - 0.01 * mrFloor;
		}

		dmg *= mrCoefficient;

		return dmg;
	}

	/**
	 * 傷害資訊送出
	 */
	public void commit(int damage, int drainMana) {
		
		switch (_calcType) {
		case 1:
			commitPc(damage, drainMana);
			break;
		case 2:
			commitNpc(damage, drainMana);
			break;
		}

		if (!ConfigAlt.ALT_ATKMSG) {
			return;
		}
		switch (_calcType) {
		case 1:
			if ((_pc.getAccessLevel() == 0) && (_targetPc.getAccessLevel() == 0)) {
				return;
			}

			break;
		case 2:
			if (_pc.getAccessLevel() == 0) {
				return;
			}
			break;
		}

		switch (_calcType) {
		case 1:
			if (_pc.getAccessLevel() > 0) {
				StringBuilder atkMsg = new StringBuilder();
				atkMsg.append("對PC送出技能: ");
				atkMsg.append(_pc.getName() + ">");
				atkMsg.append(_targetPc.getName() + " ");
				atkMsg.append("傷害: " + damage);
				atkMsg.append(" 目標HP:" + _targetPc.getCurrentHp());

				_pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
			}
			if (_targetPc.getAccessLevel() > 0) {
				StringBuilder atkMsg = new StringBuilder();
				atkMsg.append("受到PC技能: ");
				atkMsg.append(_pc.getName() + ">");
				atkMsg.append(_targetPc.getName() + " ");
				atkMsg.append("傷害: " + damage);
				atkMsg.append(" 目標HP:" + _targetPc.getCurrentHp());

				_targetPc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
			}
			break;
		case 2:
			if (_pc.getAccessLevel() > 0) {
				StringBuilder atkMsg = new StringBuilder();
				atkMsg.append("對NPC送出技能: ");
				atkMsg.append(_pc.getName() + ">");
				atkMsg.append(_targetNpc.getNameId() + " ");
				atkMsg.append("傷害: " + damage);
				atkMsg.append(" 目標HP:" + _targetNpc.getCurrentHp());

				_pc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
			}
			break;
		}
	}

	/**
	 * 傷害資訊送出
	 */
	private void commitPc(int damage, int drainMana) {
		try {
			if (drainMana > 0) {
				if (_targetPc.getCurrentMp() > 0) {
					drainMana = Math.min(drainMana, _targetPc.getCurrentMp());
					int newMp = _pc.getCurrentMp() + drainMana;
					_pc.setCurrentMp(newMp);
				} else {
					drainMana = 0;
				}
			}

			_targetPc.receiveManaDamage(_pc, drainMana);
			_targetPc.receiveDamage(_pc, damage, true, false);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 傷害資訊送出
	 */
	private void commitNpc(int damage, int drainMana) {
		try {
			
			if (drainMana > 0) {
				if (_targetNpc.getCurrentMp() > 0) {
					
					int drainValue = _targetNpc.drainMana(drainMana);
					
					int newMp = _pc.getCurrentMp() + (drainValue>=2?drainValue/2:drainValue);
					
					_pc.setCurrentMp(newMp);
				} else {
					drainMana = 0;
				}
			}
			_targetNpc.ReceiveManaDamage(_pc, drainMana);
			_targetNpc.receiveDamage(_pc, damage);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
}
