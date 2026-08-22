package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.LUCIFER;
import static com.lineage.server.model.skill.L1SkillId.REDUCTION_ARMOR;

import java.util.ConcurrentModificationException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigSkill;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.model.poison.L1ParalysisPoison;
import com.lineage.server.model.poison.L1SilencePoison;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_AttackPacketNpc;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.serverpackets.S_UseAttackSkill;
import com.lineage.server.templates.L1SystemMessage;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.types.Point;
import com.lineage.server.utils.RandomArrayList;

/**
 * 攻擊判定
 * 
 * @author dexc
 */
public class L1AttackNpc extends L1AttackMode {
	
	private static final Log _log = LogFactory.getLog(L1AttackNpc.class);

	public L1AttackNpc(L1NpcInstance attacker, L1Character target) {
		if (attacker == null) {
			return;
		}
		if (target == null) {
			return;
		}
		if (target.isDead()) {
			return;
		}
		if (target.getCurrentHp() <= 0) {
			return;
		}
		_npc = attacker;
		if ((target instanceof L1PcInstance)) {
			_targetPc = ((L1PcInstance) target);
			_calcType = NPC_PC;
		} else if ((target instanceof L1NpcInstance)) {
			_targetNpc = ((L1NpcInstance) target);
			_calcType = NPC_NPC;
		}
		_target = target;
		_targetId = target.getId();
		_targetX = target.getX();
		_targetY = target.getY();
	}

	/**
	 * 命中判定
	 */
	@Override
	public boolean calcHit() {
		if (_target == null) { // 物件遺失
			_isHit = false;
			return _isHit;
		}
		switch (_calcType) {
		case NPC_PC:
			_isHit = calcPcHit();
			break;
		case NPC_NPC:
			_isHit = calcNpcHit();
			break;
		}

		return _isHit;
	}

	/**
	 * NPC對PC命中
	 * 
	 * @return
	 */
	private boolean calcPcHit() {
		if ((((_npc instanceof L1PetInstance)) || ((_npc instanceof L1SummonInstance)))
				&& (_targetPc.getZoneType() == 1)) {
			return false;
		}
		// 傷害為0
		if (dmg0(_targetPc)) {
			return false;
		}

		// 迴避攻擊
		if (calcEvasion()) {
			return false;
		}

		_hitRate += _npc.getLevel() + 5;

		if ((_npc instanceof L1PetInstance)) { // 寵物武器命中追加
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		}

		_hitRate += _npc.getHitup();

		int attackerDice = _random.nextInt(20) + 1 + _hitRate - 3;

		// 技能增加閃避
		attackerDice += attackerDice(_targetPc);

		// 防禦力抵銷
		int defenderDice = 0;

		int defenderValue = _targetPc.getAc() * -1;

		if (_targetPc.getAc() >= 0) {
			defenderDice = 10 - _targetPc.getAc();
		} else if (_targetPc.getAc() < 0) {
			defenderDice = 10 + _random.nextInt(defenderValue) + 1;
		}

		int fumble = _hitRate;

		int critical = _hitRate + 17;

		if (attackerDice <= fumble) {
			_hitRate = 0;
		} else if (attackerDice >= critical) {
			_hitRate = 100;
		} else if (attackerDice > defenderDice) {
			_hitRate = 100;
		} else if (attackerDice <= defenderDice) {
			_hitRate = 0;
		}
		/*final int critical = _hitRate + 19;
		if (attackerDice <= fumble) {
			_hitRate = 15;
		} else if (attackerDice >= critical) {
			_hitRate = 100;
		} else {
			// 防禦力抵銷
			if (attackerDice > defenderDice) {
				_hitRate = 100;
			} else if (attackerDice <= defenderDice) {
				_hitRate = 15;
			}
		}*/

		if (_npc.getNpcTemplate().get_nameid().startsWith("BOSS")) {// BOSS增加命中機率
			attackerDice += ConfigOther.BOSS_HIT;
		}
		
		int rnd = _random.nextInt(100) + 1;

		if ((_npc.get_ranged() >= 10) && (_hitRate > rnd)
				&& (_npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) >= 2)) {
			return calcErEvasion();
		}

		return _hitRate >= rnd;
	}

	/**
	 * NPC對NPC命中
	 * 
	 * @return
	 */
	private boolean calcNpcHit() {
		if (dmg0(_targetNpc)) {
			return false;
		}

		_hitRate += _npc.getLevel() + 3;

		if ((_npc instanceof L1PetInstance)) {
			_hitRate += ((L1PetInstance) _npc).getHitByWeapon();
		}

		_hitRate += _npc.getHitup();

		int attackerDice = _random.nextInt(20) + 1 + _hitRate - 3;

		attackerDice += attackerDice(_targetNpc);

		if (_npc.getNpcTemplate().get_nameid().startsWith("BOSS")) {// BOSS增加命中機率
			attackerDice += ConfigOther.BOSS_HIT;
		}

		int defenderDice = 0;

		int defenderValue = _targetNpc.getAc() * -1;

		if (_targetNpc.getAc() >= 0) {
			defenderDice = 10 - _targetNpc.getAc();
		} else if (_targetNpc.getAc() < 0) {
			defenderDice = 10 + _random.nextInt(defenderValue) + 1;
		}

		int fumble = _hitRate;
		int critical = _hitRate + 19;

		if (attackerDice <= fumble) {
			_hitRate = 0;
		} else if (attackerDice >= critical) {
			_hitRate = 100;
		} else if (attackerDice > defenderDice) {
			_hitRate = 100;
		} else if (attackerDice <= defenderDice) {
			_hitRate = 0;
		}
		/*final int critical = _hitRate + 19;
		if (attackerDice <= fumble) {
			_hitRate = 15;
		} else if (attackerDice >= critical) {
			_hitRate = 100;
		} else {
			if (attackerDice > defenderDice) {
				_hitRate = 100;
			} else if (attackerDice <= defenderDice) {
				_hitRate = 15;
			}
		}*/

		int rnd = _random.nextInt(100) + 1;
		return _hitRate >= rnd;
	}

	/**
	 * 傷害計算
	 */
	@Override
	public int calcDamage() {
		switch (_calcType) {
		case 3:
			_damage = calcPcDamage();
			break;
		case 4:
			_damage = calcNpcDamage();
			break;
		}

		return _damage;
	}

	/**
	 * 基礎傷害計算
	 * 
	 * @param dmg
	 * @return
	 */
	private double npcDmgMode(double dmg) {
		if (_npc.getNpcTemplate().get_nameid().startsWith("BOSS")) {// BOSS傷害加倍
			dmg *= ConfigOther.BOSS_POWER;
		}

		if (_random.nextInt(100) < 15) {// 15%機率爆擊
			dmg *= 2.0D;
		}

		dmg += _npc.getDmgup();

		if (isUndeadDamage()) {// 不死系夜間增加攻擊力
			dmg *= 1.2D;
		}

		dmg = (int) (dmg * (getLeverage() / 10.0D));

		if (_npc.isWeaponBreaked()) {
			dmg /= 2.0D;
		}

		return dmg;
	}

	/**
	 * NPC對PC傷害
	 * 
	 * @return
	 */
	private int calcPcDamage() {
		if (_targetPc == null) {
			return 0;
		}

		if (dmg0(_targetPc)) {
			_isHit = false;
			return 0;
		}

		int lvl = _npc.getLevel();
		double dmg = 0.0D;

		//final Integer dmgStr = L1AttackList.STRD.get((int) _npc.getStr());
		//dmg = _random.nextInt(lvl) + (_npc.getStr() * 0.8) + dmgStr;
		//怪物攻擊等於 怪物等級+力量x2
		//dmg = lvl + (_npc.getStr()) * 2;
		dmg = lvl + _npc.getStr();

		if ((_npc instanceof L1PetInstance)) {
			dmg += lvl / 7;
			dmg += ((L1PetInstance) _npc).getDamageByWeapon();
		}

		dmg = npcDmgMode(dmg);

		dmg -= calcPcDefense();// 防禦減傷

		if (!_targetPc.hasSkillEffect(L1SkillId.negativeId13)) {
			dmg -= _targetPc.getDamageReductionByArmor() // 防具傷害減免
					+ _targetPc.get_reduction_dmg();// 物理傷害減免
		}

		dmg -= _targetPc.dmgDowe();// 隨機傷害減免

		if (_targetPc.getClanid() != 0) {
			dmg -= getDamageReductionByClan(_targetPc);
		}
		// 護甲身軀
		if (_targetPc.isWarrior() && _targetPc.isARMORGARDE()) {
			// dmg -= _targetPc.getAc() / 10;
			dmg -= (Math.abs(_targetPc.getAc()) / 10);
		}
		if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {
			int targetPcLvl = Math.max(_targetPc.getLevel(), 50);
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}
		
        if (_targetPc.isDragonKnight() && _targetPc.getReincarnationSkill()[2] > 0) { // 龍騎天賦技能強之護鎧
        	dmg -= (int) (dmg / 100.0) * _targetPc.getReincarnationSkill()[2];
        }
		if (_targetPc.hasSkillEffect(220)||_targetPc.hasSkillEffect(219)) {// 化身承受
			dmg *= 1.05;
		}
		/** [原碼] 反叛者的盾牌 機率減免傷害 */
		for (L1ItemInstance item : _targetPc.getInventory().getItems()) {
			if (item.getItemId() == 400041 && item.isEquipped()) {
				Random random = new Random();
				int r = random.nextInt(100) + 1;
				if ((item.getEnchantLevel() * 2) >= r) {
					dmg -= 50;
					_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 6320));
				}
			}
		}
		if (_targetPc.getLocation().getTileDistance(_npc.getLocation()) <= 3
				&& _targetPc.isActived() && !_targetPc.hasSkillEffect(100611)) {
			_targetPc.allTargetClear();
			_targetPc._hateList.add(_npc, 0);
			_targetPc.setNowTarget(_npc);
		}
		if(_targetPc.getLevel()<=ConfigOther.pclevel){
			dmg/=ConfigOther.guiwugjbl;
		}
		boolean dmgX2 = false;// 傷害除2
		// 取回技能
		if (!_targetPc.getSkillisEmpty() && (_targetPc.getSkillEffect().size() > 0)) {
			try {
				for (final Integer key : _targetPc.getSkillEffect()) {
					final Integer integer = L1AttackList.SKD3.get(key);
					if (integer != null) {
						if (integer.equals(key)) {
							dmgX2 = true;
						} else {
							dmg += integer;
						}
					}
				}
			} catch (final ConcurrentModificationException e) {
				// 技能取回發生其他線程進行修改
			} catch (final Exception e) {
				_log.error(e.getLocalizedMessage(), e);
			}
		}

		if (dmgX2) {// 聖界減半
			dmg /= 2.0D;
		}

		if (_targetPc.hasSkillEffect(LUCIFER)) { // 暗影屏障-所承受的傷害減少10%
			dmg -= (dmg * 0.1);
		}

		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
		if (castleId > 0) {
			isNowWar = ServerWarExecutor.get().isNowWar(castleId);
		}
		if (!isNowWar) {
			if ((_npc instanceof L1PetInstance)) {
				dmg /= 8.0D;
			}
			if ((_npc instanceof L1SummonInstance)) {
				L1SummonInstance summon = (L1SummonInstance) _npc;
				if (summon.isExsistMaster()) {
					dmg /= 8.0D;
				}
			}
		}

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
		)) && _targetPc.isCrystal()) {// 足夠魔法結晶體
			if (!isShortDistance()) {
				if (_targetPc.isTITANBULLET() && (_random.nextInt(100) < ConfigSkill.isPassive_Tatin_Bullet)) {
					dmg = 0;
					actionTitan(true);
					commitTitan(_targetPc.colcTitanDmg());
				}
			} else {
				if (_targetPc.isTITANROCK() && (_random.nextInt(100) < ConfigSkill.isPassive_Tatin_Rock)) {
					dmg = 0;
					actionTitan(false);
					commitTitan(_targetPc.colcTitanDmg());
				}
			}
		}
		
		if (_targetPc.isDarkelf() && _targetPc.getReincarnationSkill()[2] > 0 && RandomArrayList.getInc(100, 1) > 100 - _targetPc.getReincarnationSkill()[2]) { // 黑妖天賦技能神之跳躍
			_targetPc.sendPackets(new S_SystemMessage(L1SystemMessage.ShowMessage(8013))); // 發動天賦技能 神之跳躍 閃避所有物理傷害。
			//_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 240));
			//_targetPc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 240));
			dmg = 0.0D;
		}

		if (dmg <= 0.0D) {
			_isHit = false;
		}

		addNpcPoisonAttack(_targetPc);

		if (!_isHit) {
			dmg = 0.0D;
		}

		return (int) dmg;
	}

	/**
	 * NPC對NPC傷害
	 * 
	 * @return
	 */
	private int calcNpcDamage() {
		if (_targetNpc == null) {
			return 0;
		}

		if (dmg0(_targetNpc)) {
			_isHit = false;
			return 0;
		}

		int lvl = _npc.getLevel();
		double dmg = 0.0D;

		if ((_npc instanceof L1PetInstance)) {
			dmg = _random.nextInt(_npc.getNpcTemplate().get_level()) + _npc.getStr() / 2 + 1;

			dmg += lvl / 14; // 每14級追加1點攻擊力 XXX
			dmg += ((L1PetInstance) _npc).getDamageByWeapon();
		} else {
			final Integer dmgStr = L1AttackList.STRD.get((int) _npc.getStr());
			dmg = _random.nextInt(lvl) + (_npc.getStr() / 2) + dmgStr;
		}

		dmg = npcDmgMode(dmg);

		dmg -= calcNpcDamageReduction();

		addNpcPoisonAttack(_targetNpc);

		if (_targetNpc.hasSkillEffect(68)) {// 聖界減半
			dmg /= 2.0D;
		}

		if (dmg <= 0.0D) {
			_isHit = false;
		}

		if (!_isHit) {
			dmg = 0.0D;
		}

		return (int) dmg;
	}

	/**
	 * 夜間攻擊力增加
	 * 
	 * @return
	 */
	private boolean isUndeadDamage() {
		boolean flag = false;
		int undead = _npc.getNpcTemplate().get_undead();
		boolean isNight = L1GameTimeClock.getInstance().currentTime().isNight();
		if (isNight) {
			switch (undead) {
			case 1:// 不死系
			case 3:// 殭屍系
			case 4:// 不死系(治療系無傷害/無法使用起死回生)
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * 毒素附加攻擊
	 * 
	 * @param target
	 */
	private void addNpcPoisonAttack(L1Character target) {
		switch (_npc.getNpcTemplate().get_poisonatk()) {
		case 1:// 出血毒
			if (15 >= _random.nextInt(100) + 1) {
				L1DamagePoison.doInfection(_npc, target, 3000, 20);
			}
			break;
		case 2:// 沉默毒
			if (15 >= _random.nextInt(100) + 1) {
				L1SilencePoison.doInfection(target);
			}
			break;
		case 4:// 麻痺毒
			if (15 >= _random.nextInt(100) + 1) {
				L1ParalysisPoison.doInfection(target, 20000, 45000);
			}
			break;
		}
		if (_npc.getNpcTemplate().get_paralysisatk() != 0) { // 麻痺攻擊
		}
	}

	/**
	 * 攻擊動作送出
	 */
	public void action() {
		try {
			if (_npc == null) {
				return;
			}
			if (_npc.isDead()) {
				return;
			}

			_npc.setHeading(_npc.targetDirection(_targetX, _targetY));

			// 距離2格以上攻擊
			boolean isLongRange = _npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) > 1;
			int bowActId = _npc.getBowActId();

			// 遠距離武器
			if ((isLongRange) && (bowActId > 0)) {
				actionX1();
				// 近距離武器
			} else {
				actionX2();
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 遠距離攻擊
	 */
	private void actionX1() {
		try {
			int bowActId = _npc.getBowActId();

			int actId = 1;// 預設攻擊動作
			if(_npc.getNpcId()==95021){
				actId = 21;
		    	
		    }
			if (getActId() > 1) {// 有指定攻擊動作編號
				actId = getActId();
			}
		    

			if (_isHit) {// 命中
				_npc.broadcastPacketAll(
						new S_UseArrowSkill(_npc, _targetId, bowActId, _targetX, _targetY, _damage, actId));
			} else {// 未命中
				_npc.broadcastPacketAll(new S_UseArrowSkill(_npc, bowActId, _targetX, _targetY, actId));
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 近距離攻擊
	 */
	private void actionX2() {
		try {
			int actId = 1;// 預設攻擊動作
			if (getActId() > 1) {// 有指定攻擊動作編號
				actId = getActId();
			}

			// 特定外型改變攻擊動作
			switch (_npc.getTempCharGfx()) {
			case 1780:// 烈炎獸
			case 7430:
			case 13076:
				actId = 30;
				break;
			case 2757:// 吸血鬼
			case 4104:
			case 13096:
				actId = 18;
				break;
			}

			if (_isHit) {// 命中
				if (getGfxId() > 0) {// 有動畫編號
					_npc.broadcastPacketAll(new S_UseAttackSkill(_npc, _target.getId(), getGfxId(), _targetX, _targetY,
							actId, _damage));
				} else {// 沒有動畫編號
					gfx7049();

					_npc.broadcastPacketAll(new S_AttackPacketNpc(_npc, _target, actId, _damage));
				}
			} else if (getGfxId() > 0) {// 未命中 有動畫編號
				_npc.broadcastPacketAll(
						new S_UseAttackSkill(_target, _npc.getId(), getGfxId(), _targetX, _targetY, actId, 0));
			} else {// 未命中 沒動畫編號
				_npc.broadcastPacketAll(new S_AttackPacketNpc(_npc, _target, actId));
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 幻術師外型 使用古奇獸
	 */
	private void gfx7049() {
		if (_npc.getStatus() != 58) {
			return;
		}
		boolean is = false;
		if (_npc.getTempCharGfx() == 6671) {
			is = true;
		}
		if (_npc.getTempCharGfx() == 6650) {
			is = true;
		}
		if (is) {// 幻術師外型 使用古奇獸
			_npc.broadcastPacketAll(new S_SkillSound(_npc.getId(), 7049));
		}
	}

	/**
	 * 傷害資訊送出
	 */
	public void commit() {
		if (_isHit) {// 命中
			switch (_calcType) {
			case NPC_PC:
				commitPc();
				break;
			case NPC_NPC:
				commitNpc();
				break;
			}
		} else {// 未命中
			if (_calcType == NPC_PC) {
				_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 13418));// MISS特效編號
			}
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

		String srcatk = _npc.getName();
		String tgatk = _targetPc.getName();
		String dmginfo = _isHit ? "傷害:" + _damage : "失敗";
		String hitinfo = " 命中:" + _hitRate + "% 剩餘hp:" + _targetPc.getCurrentHp();
		String x = srcatk + ">" + tgatk + " " + dmginfo + hitinfo;

		_targetPc.sendPackets(new S_ServerMessage(166, "受到NPC攻擊: " + x));
	}

	private void commitPc() {
		_targetPc.receiveDamage(_npc, _damage, false, false);
	}

	private void commitNpc() {
		_targetNpc.receiveDamage(_npc, _damage);
	}

	/**
	 * 是否為近距離攻擊
	 */
	public boolean isShortDistance() {
		boolean isShortDistance = true;
		boolean isLongRange = _npc.getLocation().getTileLineDistance(new Point(_targetX, _targetY)) > 1;
		int bowActId = _npc.getBowActId();

		if ((isLongRange) && (bowActId > 0)) {
			isShortDistance = false;
		}
		return isShortDistance;
	}

	/**
	 * 受到反擊屏障傷害的處理
	 */
	public void commitCounterBarrier() {
		int damage = calcCounterBarrierDamage();
		if (damage == 0) {
			return;
		}

		if (_npc.hasSkillEffect(68)) {// 聖界減傷
			damage /= 2;
		}

		/*
		 * if (damage >= _npc.getCurrentHp()) {// 如果傷害大於等於目前HP damage =
		 * _npc.getCurrentHp() - 1;// 變更傷害為目前HP-1(避免使用反屏掛機) }
		 */

		_npc.receiveDamage(_target, damage);
		_npc.broadcastPacketAll(new S_DoActionGFX(_npc.getId(), 2));
		_npc.broadcastPacketAll(new S_SkillSound(_target.getId(), 10710));
	}
	
	public int getHit(){
    	return _hitRate;
    }
}
