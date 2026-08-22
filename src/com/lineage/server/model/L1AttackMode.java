package com.lineage.server.model;

import java.util.ConcurrentModificationException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigSkill;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_SkillSound;

public abstract class L1AttackMode {
	private static final Log _log = LogFactory.getLog(L1AttackMode.class);
	// 目標物件
	protected L1Character _target;
	// 執行PC
	protected L1PcInstance _pc;
	// 目標PC
	protected L1PcInstance _targetPc;
	// 執行NPC
	protected L1NpcInstance _npc;
	// 目標NPC
	protected L1NpcInstance _targetNpc;
	protected int _targetId;
	protected int _targetX;
	protected int _targetY;
	protected int _statusDamage;
	protected int _hitRate;
	protected int _calcType;
	protected static final int PC_PC = 1;
	protected static final int PC_NPC = 2;
	protected static final int NPC_PC = 3;
	protected static final int NPC_NPC = 4;
	protected boolean _isHit;
	protected int _damage;
	protected int _drainMana;
	protected int _drainHp;
	protected int _attckGrfxId;
	protected int _attckActId;
	protected L1ItemInstance _weapon;
	protected int _weaponId;
	protected int _weaponType;
	protected int _weaponType2;
	protected int _weaponAddHit;
	protected int _weaponAddDmg;
	protected int _weaponSmall;
	protected int _weaponLarge;
	protected int _weaponRange = 1;

	protected int _weaponBless = 1;
	protected int _weaponEnchant;
	protected int _weaponMaterial;
	protected int _weaponDoubleDmgChance;
	protected int _weaponAttrEnchantKind;
	protected int _weaponAttrEnchantLevel;
	protected int _weaponboosEnchantKind = 0;
    protected int _weaponboosEnchantLevel = 0;
	protected L1ItemInstance _arrow;
	protected int _arrowGfxid = 66;
	protected L1ItemInstance _sting;
	protected int _stingGfxid = 2989;
	protected int _leverage = 10; // 攻擊倍率(1/10)

	protected static final Random _random = new Random();

	/**
	 * 血盟技能傷害增加
	 * 
	 * @return
	 */
	protected static double getDamageUpByClan(L1PcInstance pc) {
		double dmg = 0.0D;
		try {
			if (pc == null) {
				return 0.0D;
			}
			L1Clan clan = pc.getClan();
			if (clan == null) {
				return 0.0D;
			}

			if (clan.isClanskill()) {
				if (pc.get_other().get_clanskill() == 1) {
					int clanMan = clan.getOnlineClanMemberSize50();
					dmg += 0.25D * clanMan;
				}
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return dmg;
	}

	/**
	 * 血盟技能傷害減免
	 * 
	 * @param targetPc
	 * @return
	 */
	protected static double getDamageReductionByClan(L1PcInstance targetPc) {
		double dmg = 0.0D;
		try {
			if (targetPc == null) {
				return 0.0D;
			}
			L1Clan clan = targetPc.getClan();
			if (clan == null) {
				return 0.0D;
			}

			if (clan.isClanskill()) {
				if (targetPc.get_other().get_clanskill() == 2) {
					int clanMan = clan.getOnlineClanMemberSize50();
					dmg += 0.25D * clanMan;
				}
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return dmg;
	}

	/**
	 * 身上有特定法術效果 傷害為0
	 * 
	 * @param character
	 * @return
	 */
	protected static boolean dmg0(L1Character character) {
		try {
			if (character == null) {
				return false;
			}

			if (character.getSkillisEmpty()) {
				return false;
			}

			if (character.getSkillEffect().size() <= 0) {
				return false;
			}

			for (Integer key : character.getSkillEffect()) {
				//Integer integer = (Integer) L1AttackList.SKM0.get(key);
				final Integer integer = L1AttackList.SKM0.get(key);
				if (integer != null) {
					return true;
				}
			}
		//} catch (ConcurrentModificationException localConcurrentModificationException) {
			//_log.error(localConcurrentModificationException.getLocalizedMessage(), localConcurrentModificationException);
		} catch (final ConcurrentModificationException e) {
			// 技能取回發生其他線程進行修改
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return false;
	}

	/** 技能增加閃避 */
	protected static int attackerDice(L1Character character) {
		try {
			int attackerDice = 0;
			if (character.get_dodge() > 0) {
				attackerDice -= character.get_dodge();
			}
			if (character.get_dodge_down() > 0) {
				attackerDice += character.get_dodge_down();
			}
			return attackerDice;
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return 0;
	}

	/**
	 * 攻擊倍率(1/10)
	 * 
	 * @param i
	 */
	public void setLeverage(int i) {
		_leverage = i;
	}

	/**
	 * 攻擊倍率(1/10)
	 * 
	 * @return
	 */
	protected int getLeverage() {
		return _leverage;
	}

	public void setActId(int actId) {
		_attckActId = actId;
	}

	public void setGfxId(int gfxId) {
		_attckGrfxId = gfxId;
	}

	public int getActId() {
		return _attckActId;
	}

	public int getGfxId() {
		return _attckGrfxId;
	}

	/** 遠距離迴避率計算 亂數100 */
	protected boolean calcErEvasion() {
		int er = _targetPc.getEr();
		int rnd = _random.nextInt(100) + 1;

		return er < rnd; // true:命中 false:未命中
	}

	/** 完全閃避率計算 亂數1000 */
	protected boolean calcEvasion() {
		if (_targetPc == null) {
			return false;
		}
		int ev = _targetPc.get_evasion();
		if (ev == 0) {
			return false;
		}
		int rnd = _random.nextInt(1000) + 1;
		if (rnd <= ev) {
			if (!_targetPc.getDolls().isEmpty()) {
				for (L1DollInstance doll : _targetPc.getDolls().values()) {
					doll.show_action(2);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * PC防禦力傷害減低
	 * 
	 * @return
	 */
	protected int calcPcDefense() {
		try {
			if (_targetPc != null) {
				int ac = Math.max(0, 10 - _targetPc.getAc());

				int acDefMax = _targetPc.getClassFeature().getAcDefenseMax(ac);
				if (acDefMax != 0) {
					// (>> 1: 除) (<< 1: 乘) XXX
					int srcacd = Math.max(1, acDefMax >> 3);
					return _random.nextInt(acDefMax) + srcacd;
				}
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return 0;
	}

	/**
	 * (NPC防禦力 + 額外傷害減低) 傷害減低
	 * 
	 * @return
	 */
	protected int calcNpcDamageReduction() {
		int damagereduction = _targetNpc.getNpcTemplate().get_damagereduction();// 額外傷害減低
		try {
			int srcac = _targetNpc.getAc();
			int ac = Math.max(0, 10 - srcac);

			int acDefMax = ac / 7;// 防禦力傷害減免降低1/7 XXX
			if (acDefMax != 0) {
				int srcacd = Math.max(1, acDefMax);
				return _random.nextInt(acDefMax) + srcacd + damagereduction;
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}

		return damagereduction;
	}

	/**
	 * 計算反擊屏障傷害
	 * 
	 * @return
	 */
	protected int calcCounterBarrierDamage() {
		int damage = 0;
		try {
			// 反擊對象是PC
			if (_targetPc != null) {
				final L1ItemInstance weapon = _targetPc.getWeapon();
				if (weapon != null) {
					if (weapon.getItem().getType() == 3) { // 雙手劍
						// (BIG最大+強化數+追加)*2
						// (>> 1: 除) (<< 1: 乘)
						damage = (weapon.getItem().getDmgLarge() + weapon.getEnchantLevel()
								+ weapon.getItem().getDmgModifier()) * ConfigSkill.Counterattack;
					}
				}

			} else if (_targetNpc != null) {
				damage = _targetNpc.getStr() + _targetNpc.getLevel() << 1;
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return damage;
	}

	public abstract boolean calcHit();
	
	public abstract int getHit();

	/**
	 * 攻擊資訊送出
	 */
	public abstract void action();

	/**
	 * 傷害計算
	 * 
	 * @return
	 */
	public abstract int calcDamage();

	/**
	 * 計算結果反映
	 */
	public abstract void commit();

	/**
	 * 攻擊使用武器是否為近距離武器判斷
	 * 
	 * @return
	 */
	public abstract boolean isShortDistance();

	/**
	 * 反擊屏障的傷害反擊
	 */
	public abstract void commitCounterBarrier();

	public void commitTitan(final int dmg) {
		if (dmg == 0) {
			return;
		}
		if (_calcType == PC_PC) {
			if ((_pc != null) && (_targetPc != null)) {
				_pc.receiveDamage(_targetPc, dmg, false, false);
			}
		} else if (_calcType == NPC_PC) {
			if ((_npc != null) && (_targetPc != null)) {
				_npc.receiveDamage(_targetPc, dmg);
			}
		}
	}

	public void actionTitan(final boolean check) {
		int gfxid = 12555;
		if (check) {
			gfxid = 12557;
		}
		if (_calcType == PC_PC) {
			_pc.setHeading(_pc.targetDirection(_targetX, _targetY));
			_pc.sendPacketsAll(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
			_pc.sendPacketsAll(new S_SkillSound(_targetId, gfxid));

		} else if (_calcType == NPC_PC) {
			_npc.setHeading(_npc.targetDirection(_targetX, _targetY));
			_npc.broadcastPacketAll(new S_DoActionGFX(_npc.getId(), ActionCodes.ACTION_Damage));
			_npc.broadcastPacketAll(new S_SkillSound(_targetId, gfxid));
		}
	}
}
