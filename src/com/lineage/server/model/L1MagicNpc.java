package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.ConcurrentModificationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.Random;

public class L1MagicNpc extends L1MagicMode {
	private static final Log _log = LogFactory.getLog(L1MagicNpc.class);

	public L1MagicNpc(L1NpcInstance attacker, L1Character target) {
		if (attacker == null) {
			return;
		}

		if ((target instanceof L1PcInstance)) {
			_calcType = NPC_PC;
			_npc = attacker;
			_targetPc = ((L1PcInstance) target);
		} else {
			_calcType = NPC_NPC;
			_npc = attacker;
			_targetNpc = ((L1NpcInstance) target);
		}
	}

	private int getMagicLevel() {
		int magicLevel = _npc.getMagicLevel();
		return magicLevel;
	}

	private int getMagicBonus() {
		int magicBonus = _npc.getMagicBonus();
		return magicBonus;
	}

	private int getLawful() {
		int lawful = _npc.getLawful();
		return lawful;
	}

	/**
	 * 機率性魔法是否成功
	 */
	public boolean calcProbabilityMagic(int skillId) {
		int probability = 0;
		boolean isSuccess = false;

		switch (_calcType) {
		case NPC_PC:
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

			if (skillId == CANCELLATION) {// 魔法相消術
				return true;
			}

			break;
		case NPC_NPC:
			if (_targetNpc.hasSkillEffect(ICE_LANCE)) {
				if (skillId != CANCELLATION) {
					return false;
				}
			}

			if (_targetNpc.hasSkillEffect(EARTH_BIND)) {
				if (skillId != CANCELLATION) {
					return false;
				}
			}

			break;
		}

		probability = calcProbability(skillId);

		int rnd = _random.nextInt(100) + 1;

		probability = Math.min(probability, 90);
		if (probability >= rnd) {
			isSuccess = true;
		} else {
			isSuccess = false;
		}

		if (calcEvasion()) {
			return false;
		}
		return isSuccess;
	}

	private int calcProbability(int skillId) {
		L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
		int attackLevel = _npc.getLevel();
		int defenseLevel = 0;
		int probability = 0;

		switch (_calcType) {
		case NPC_PC:
			defenseLevel = _targetPc.getLevel();
			break;
		case NPC_NPC:
			defenseLevel = _targetNpc.getLevel();
			if ((skillId == RETURN_TO_NATURE) && ((_targetNpc instanceof L1SummonInstance))) {
				L1SummonInstance summon = (L1SummonInstance) _targetNpc;
				defenseLevel = summon.getMaster().getLevel();
			}

			break;
		}

		switch (skillId) {
		case ELEMENTAL_FALL_DOWN:// 弱化屬性
		case RETURN_TO_NATURE:// 釋放元素
		// case ENTANGLE:// 地面障礙 改->大地纏繞
		case WIND_SHACKLE:// 風之枷鎖
		case ERASE_MAGIC:// 魔法消除
		case EARTH_BIND:// 大地屏障
		case AREA_OF_SILENCE:// 封印禁地
		case POLLUTE_WATER:// 污濁之水
		case STRIKER_GALE:// 精準射擊
			probability = (int) (l1skills.getProbabilityDice() / 10.0D * (attackLevel - defenseLevel));
			probability += l1skills.getProbabilityValue();
			probability -= getTargetMr() / 10;
			probability = (int) (probability * (getLeverage() / 10.0D));// DB設定增加命中倍率
			break;
		case SHOCK_STUN:// 衝擊之暈
			if (attackLevel > defenseLevel) {// 攻擊方等級大於防禦方
				probability = 70;
			} else if (attackLevel == defenseLevel) {// 攻擊方等級相等防禦方
				probability = 50;
			} else if (attackLevel < defenseLevel) {// 攻擊方等級小於防禦方
				probability = 30;
			}
			probability = (int) (probability * (getLeverage() / 10.0D));// DB設定增加命中倍率
			break;
		case COUNTER_BARRIER:// 反擊屏障
			probability = 20;
			break;
		case DARK_BLIND:// 暗黑盲咒
		case ARMOR_BREAK:// 破壞盔甲
			probability = 38 + (attackLevel - defenseLevel) * (_random.nextInt(3) + 2);
			probability = (int) (probability * (getLeverage() / 10.0D));// DB設定增加命中倍率
			break;
		case CONFUSION:// 混亂
		case PHANTASM:// 幻想
		case PANIC:// 恐慌
		case BONE_BREAK:// 骷髏毀壞
			probability = Random.nextInt(11) + 20;
			probability += (attackLevel - defenseLevel) * 2;
			probability = (int) (probability * (getLeverage() / 10.0D));// DB設定增加命中倍率
			break;
		case GUARD_BRAKE:// 護衛毀滅
		case RESIST_FEAR:// 恐懼無助
		case THUNDER_GRAB:// 奪命之雷
		case HORROR_OF_DEATH:// 驚悚死神
			probability = (int) (l1skills.getProbabilityDice() / 10.0D * (attackLevel - defenseLevel));
			probability += l1skills.getProbabilityValue();
			probability = (int) (probability * (getLeverage() / 10.0D));// DB設定增加命中倍率
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

			probability = (int) (probability * (getLeverage() / 10.0D));// DB設定增加命中倍率

			probability -= getTargetMr();
			break;
		}

		if (_calcType == NPC_PC) {
			switch (skillId) {
			/*case EARTH_BIND:// 大地屏障
			case THUNDER_GRAB:// 奪命之雷
				probability -= _targetPc.getRegistSustain();
				break;
			case SHOCK_STUN:// 衝擊之暈
			case BONE_BREAK:// 骷髏毀壞
				probability -= _targetPc.getRegistStun();
				break;
			case CURSE_PARALYZE:// 木乃伊的詛咒
				probability -= _targetPc.getRegistStone();
				break;
			case FOG_OF_SLEEPING:// 沉睡之霧
			case DARK_BLIND:// 暗黑盲咒
			case PHANTASM:// 幻想
				probability -= _targetPc.getRegistSleep();
				break;
			case ICE_LANCE:// 冰矛圍籬
			case FREEZING_BLIZZARD:// 寒冰尖刺
				probability -= _targetPc.getRegistFreeze();
				break;
			case CURSE_BLIND:// 闇盲咒術
			case DARKNESS:// 黑闇之影
				probability -= _targetPc.getRegistBlind();
				break;*/

			case SHOCK_STUN:// 衝擊之暈
				// 技術耐性 + 全部耐性
				probability -= _targetPc.getRegistTechnology() + _targetPc.getRegistAll();
				break;

			case EARTH_BIND:// 大地屏障
			case DARK_BLIND:// 暗黑盲咒
				// 精靈耐性 + 全部耐性
				probability -= _targetPc.getRegistElf() + _targetPc.getRegistAll();
				break;

			case THUNDER_GRAB:// 奪命之雷
			case BONE_BREAK:// 骷髏毀壞
			case PHANTASM:// 幻想
				// 龍屬耐性 + 全部耐性
				probability -= _targetPc.getRegistDragon() + _targetPc.getRegistAll();
				break;
			}
		}

		return probability;
	}

	public int calcMagicDamage(int skillId) {
		int damage = 0;
		switch (_calcType) {
		case NPC_PC:
			damage = calcPcMagicDamage(skillId);
			break;
		case NPC_NPC:
			damage = calcNpcMagicDamage(skillId);
			break;
		}

		damage = calcMrDefense(damage);
		return damage;
	}

	private int calcPcMagicDamage(int skillId) {
		if (_targetPc == null) {
			return 0;
		}
		if ((((_npc instanceof L1PetInstance)) || ((_npc instanceof L1SummonInstance)))
				&& (_targetPc.getZoneType() == 1)) {
			return 0;
		}

		if (dmg0(_targetPc)) {
			return 0;
		}

		if (calcEvasion()) {
			return 0;
		}

		int dmg = 0;
		if (skillId == FINAL_BURN) { // 會心一擊
			dmg = _npc.getCurrentMp();

		} else {
			dmg = calcMagicDiceDamage(skillId);
			dmg = (int) (dmg * (getLeverage() / 10.0D));
			if (_npc.getNpcTemplate().get_nameid().startsWith("BOSS")) {// BOSS傷害加倍
				dmg *= ConfigOther.BOSS_POWER;
			}
		}

		dmg -= _targetPc.getDamageReductionByArmor() + _targetPc.getMagicDmgReduction();

		dmg -= _targetPc.dmgDowe();

		if (_targetPc.getClanid() != 0) {
			dmg = (int) (dmg - getDamageReductionByClan(_targetPc));
		}

		if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
			int targetPcLvl = Math.max(_targetPc.getLevel(), 50);
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}
		if (_targetPc.getLocation().getTileDistance(_npc.getLocation()) <= 3
				&& _targetPc.isActived() && !_targetPc.hasSkillEffect(100611)) {
			_targetPc.allTargetClear();
			_targetPc._hateList.add(_npc, 0);
			_targetPc.setNowTarget(_npc);
		}
		boolean dmgX2 = false;

		if ((!_targetPc.getSkillisEmpty()) && (_targetPc.getSkillEffect().size() > 0)) {
			try {
				for (Integer key : _targetPc.getSkillEffect()) {
					Integer integer = (Integer) L1AttackList.SKD3.get(key);

					if (integer != null)
						if (integer.equals(key)) {
							dmgX2 = true;
						} else {
							dmg += integer.intValue();
						}
				}
			} catch (ConcurrentModificationException localConcurrentModificationException) {
			} catch (Exception e) {
				_log.error(e.getLocalizedMessage(), e);
			}
		}

		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
		if (castleId > 0) {
			isNowWar = ServerWarExecutor.get().isNowWar(castleId);
		}

		if (!isNowWar) {
			if ((_npc instanceof L1PetInstance)) {
				dmg >>= 3;
			}
			if ((_npc instanceof L1SummonInstance)) {
				L1SummonInstance summon = (L1SummonInstance) _npc;
				if (summon.isExsistMaster()) {
					dmg >>= 3;
				}
			}
		}

		if (dmgX2) {
			dmg >>= 1;
		}

		if (_targetPc.hasSkillEffect(LUCIFER)) { // 暗影屏障-所承受的傷害減少10%
			dmg -= (dmg * 0.1);
		}	
	   if(_targetPc.getLevel()<=ConfigOther.pclevel){
		dmg/=ConfigOther.guiwugjblmf;
	   }
		if ((_targetPc.hasSkillEffect(6685)// 水龍之魔眼
				|| _targetPc.hasSkillEffect(6687)// 生命之魔眼
				|| _targetPc.hasSkillEffect(6688)// 誕生之魔眼
				|| _targetPc.hasSkillEffect(6689))// 形象之魔眼
				&& (_random.nextInt(100) < 10)) {// 魔眼10%機率減傷
			dmg /= 2;
			_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 6320));
		}

		if (_targetPc.hasSkillEffect(134)) {
			int npcId = _npc.getNpcTemplate().get_npcId();
			switch (npcId) {
			case 45681:
			case 45682:
			case 45683:
			case 45684:
			case 71014:
			case 71015:
			case 71016:
			case 71026:
			case 71027:
			case 71028:
			case 97204:
			case 97205:
			case 97206:
			case 97207:
			case 97208:
			case 97209:
				break;
			default:
				if (_npc.getNpcTemplate().get_IsErase()) {
					if (_targetPc.getWis() >= _random.nextInt(100)) {
						_npc.broadcastPacketAll(new S_DoActionGFX(_npc.getId(), 2));
						_npc.receiveDamage(_targetPc, dmg);
						_npc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 4395));
						dmg = 0;
						_targetPc.killSkillEffectTimer(134);
					}
				}
				break;
			}
		}
		int dmgOut = Math.max(dmg, 0);

		return dmgOut;
	}

	private int calcNpcMagicDamage(int skillId) {
		if (_targetNpc == null) {
			return 0;
		}

		if (dmg0(_targetNpc)) {
			return 0;
		}

		int dmg = 0;
		if (skillId == FINAL_BURN) { // 會心一擊
			dmg = _npc.getCurrentMp();

		} else {
			dmg = calcMagicDiceDamage(skillId);
			dmg = (int) (dmg * (getLeverage() / 10.0D));
			if (_npc.getNpcTemplate().get_nameid().startsWith("BOSS")) {// BOSS傷害加倍
				dmg *= ConfigOther.BOSS_POWER;
			}
		}

		if (_targetNpc.hasSkillEffect(IMMUNE_TO_HARM)) { // 聖結界
			dmg /= 2;
		}

		// 鏡反射
		/*if ((_targetNpc.hasSkillEffect(COUNTER_MIRROR)) && (_npc.getNpcTemplate().get_IsErase())) {
			if (_targetNpc.getWis() >= _random.nextInt(100)) {
				_npc.broadcastPacketAll(new S_DoActionGFX(_npc.getId(), 2));
				_npc.receiveDamage(_targetNpc, dmg);
				_npc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 4395));
				dmg = 0;
				_targetNpc.killSkillEffectTimer(COUNTER_MIRROR);
			}
		}*/

		if ((_targetNpc.hasSkillEffect(11059)) && (_npc.getNpcTemplate().get_IsErase())) {
			_npc.broadcastPacketAll(new S_DoActionGFX(_npc.getId(), 2));
			_npc.receiveDamage(_targetNpc, dmg);
			_npc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 4395));
			dmg = 0;
		}

		return dmg;
	}

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

		int spByItem = getTargetSp();
		charaIntelligence = Math.max(_npc.getInt() + spByItem - 12, 1);

		double attrDeffence = calcAttrResistance(l1skills.getAttr());

		double coefficient = Math.max(1.0D - attrDeffence + charaIntelligence * 3.0D / 32.0D, 0.0D);

		magicDamage = (int) (magicDamage * coefficient);

		return magicDamage;
	}

	public int calcHealing(int skillId) {
		L1Skills l1skills = SkillsTable.get().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;

		int magicBonus = Math.min(getMagicBonus(), 10);

		int diceCount = value + magicBonus;
		for (int i = 0; i < diceCount; i++) {
			magicDamage += _random.nextInt(dice) + 1;
		}

		double alignmentRevision = 1.0D;
		if (getLawful() > 0) {
			alignmentRevision += getLawful() / 32768.0D;
		}

		magicDamage = (int) (magicDamage * alignmentRevision);

		magicDamage = (int) (magicDamage * (getLeverage() / 10.0D));

		return magicDamage;
	}

	private int calcMrDefense(int dmg) {
		int mr = getTargetMr();

		int rnd = _random.nextInt(100) + 1;
		if (mr >= rnd) {
			dmg /= 2;
		}

		return dmg;
	}

	public void commit(int damage, int drainMana) {
		switch (_calcType) {
		case NPC_PC:
			commitPc(damage, drainMana);
			break;
		case NPC_NPC:
			commitNpc(damage, drainMana);
		}

		if (!ConfigAlt.ALT_ATKMSG) {
			return;
		}

		if (_calcType == NPC_NPC) {
			return;
		}
		if (!_targetPc.isGm()) {
			return;
		}

		StringBuilder atkMsg = new StringBuilder();
		atkMsg.append("受到NPC技能: ");
		atkMsg.append(_npc.getNameId() + ">");
		atkMsg.append(_targetPc.getName() + " ");
		atkMsg.append("傷害: " + damage);

		_targetPc.sendPackets(new S_ServerMessage(166, atkMsg.toString()));
	}

	private void commitPc(int damage, int drainMana) {
		try {
			_targetPc.receiveDamage(_npc, damage, true, false);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	private void commitNpc(int damage, int drainMana) {
		try {
			_targetNpc.receiveDamage(_npc, damage);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
}
