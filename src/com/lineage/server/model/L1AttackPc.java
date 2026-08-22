package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.ARMOR_BREAK;
import static com.lineage.server.model.skill.L1SkillId.BERSERKERS;
import static com.lineage.server.model.skill.L1SkillId.BLOW_ATTACK;
import static com.lineage.server.model.skill.L1SkillId.BOUNCE_ATTACK;
import static com.lineage.server.model.skill.L1SkillId.BRAKE_DESTINY;
import static com.lineage.server.model.skill.L1SkillId.BRAVE_AURA;
import static com.lineage.server.model.skill.L1SkillId.BURNING_SLASH;
import static com.lineage.server.model.skill.L1SkillId.BURNING_SPIRIT;
import static com.lineage.server.model.skill.L1SkillId.BURNING_WEAPON;
import static com.lineage.server.model.skill.L1SkillId.DOUBLE_BREAK;
import static com.lineage.server.model.skill.L1SkillId.ELEMENTAL_FIRE;
import static com.lineage.server.model.skill.L1SkillId.ENCHANT_VENOM;
import static com.lineage.server.model.skill.L1SkillId.FIRESTUN;
import static com.lineage.server.model.skill.L1SkillId.FOU_SLAYER_BRAVE;
import static com.lineage.server.model.skill.L1SkillId.LUCIFER;
import static com.lineage.server.model.skill.L1SkillId.MIRROR_IMAGE;
import static com.lineage.server.model.skill.L1SkillId.MOONATTACK;
import static com.lineage.server.model.skill.L1SkillId.QUAKE;
import static com.lineage.server.model.skill.L1SkillId.REDUCTION_ARMOR;
import static com.lineage.server.model.skill.L1SkillId.SOUL_OF_FLAME;
import static com.lineage.server.model.skill.L1SkillId.TRUEFIRESTUN;
import static com.lineage.server.model.skill.L1SkillId.TRUE_TARGET;
import static com.lineage.server.model.skill.L1SkillId.UNCANNY_DODGE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE3;
import static com.lineage.server.model.skill.L1SkillId.STRIKER_GALE2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigSkill;
import com.lineage.data.event.FeatureItemSet;
import com.lineage.data.event.RedBlueSet;
import com.lineage.server.ActionCodes;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1DollInstance2;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.weaponskill.WeaponSkillStart;
import com.lineage.server.serverpackets.S_AttackPacketPc;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_NewSkillIcon;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_PacketBoxDk;
import com.lineage.server.serverpackets.S_PacketBoxDurability;
import com.lineage.server.serverpackets.S_PacketBoxThirdSpeed;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.serverpackets.S_UseAttackSkill;
import com.lineage.server.templates.L1BossWeapon;
import com.lineage.server.templates.L1CriticalHitStone;
import com.lineage.server.templates.L1MagicWeapon;
import com.lineage.server.templates.L1SystemMessage;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.utils.RandomArrayList;

public class L1AttackPc extends L1AttackMode {

	private static final Log _log = LogFactory.getLog(L1AttackPc.class);

	private byte _attackType = 0;

	private int _weaponDamage;// 武器大小傷

	private int _weaponTotalDamage;// 武器總傷害

	private int hit_rnd;// 攻擊命中機率
	
	private int pcHitValue;// 玩家攻擊命中值

	public L1AttackPc(L1PcInstance attacker, L1Character target) {
		if (target == null) {
			return;
		}

		if (target.isDead()) {
			return;
		}

		_pc = attacker;

		if (_pc.hasSkillEffect(L1SkillId.ReiSkill_2)) { // 戰士天賦技能冰封心智
			// _pc.sendPackets(new S_SystemMessage(L1SystemMessage.ShowMessage(8040))); // 受到戰士天賦技能冰封心智效果影響，暫無法攻擊。
			return;
		}

		if ((target instanceof L1PcInstance)) {
			_targetPc = ((L1PcInstance) target);
			_calcType = PC_PC;
			if (_targetPc.get_redbluejoin() != 0 && _pc.get_redbluejoin() != 0) {
				if (_targetPc.get_redbluejoin() == _pc.get_redbluejoin()
						|| (_pc.get_redblueroom() == 1 && RedBlueSet.step1 == 3)
						|| (_pc.get_redblueroom() == 2 && RedBlueSet.step2 == 3)) {
					return;
				}
			}
		} else if ((target instanceof L1NpcInstance)) {
			_targetNpc = ((L1NpcInstance) target);
			_calcType = PC_NPC;
		}

		_weapon = _pc.getWeapon();
		/** 戰士 第二把武器 副武器 */ // src016
		if (_random.nextBoolean() && _pc.getWeaponWarrior() != null) {// 50%機率使用副手攻擊
			_weapon = _pc.getWeaponWarrior();// 副武器

		} else {
			_weapon = _pc.getWeapon();// 主武器
		}
		/** END */

		if (_weapon != null) {
			_weaponId = _weapon.getItem().getItemId();
			_weaponType = _weapon.getItem().getType1();
			_weaponType2 = _weapon.getItem().getType();
			_weaponAddHit = (_weapon.getItem().getHitModifier() + _weapon.getHitByMagic());
			_weaponAddDmg = (_weapon.getItem().getDmgModifier() + _weapon.getDmgByMagic() + _weapon.getItemAttack() + _weapon.getItemBowAttack());// terry770106

			_weaponSmall = _weapon.getItem().getDmgSmall();
			_weaponLarge = _weapon.getItem().getDmgLarge();
			_weaponRange = _weapon.getItem().getRange();
			_weaponBless = _weapon.getItem().getBless();

			if ((_weaponType != 20) && (_weaponType != 62)) {
				_weaponEnchant = (_weapon.getEnchantLevel() - _weapon.get_durability());
			} else {
				_weaponEnchant = _weapon.getEnchantLevel();
			}

			_weaponMaterial = _weapon.getItem().getMaterial();
			if (_weaponType == 20) {
				_arrow = _pc.getInventory().getArrow();
				if (_arrow != null) {
					_weaponBless = _arrow.getItem().getBless();
					_weaponMaterial = _arrow.getItem().getMaterial();
				}
			}

			if (_weaponType == 62) {
				_sting = _pc.getInventory().getSting();
				if (_sting != null) {
					_weaponBless = _sting.getItem().getBless();
					_weaponMaterial = _sting.getItem().getMaterial();
				}
			}

			_weaponDoubleDmgChance = _weapon.getDoubleDmgChance();
			_weaponAttrEnchantKind = _weapon.getAttrEnchantKind();
			_weaponAttrEnchantLevel = _weapon.getAttrEnchantLevel();
		}

		// 力量敏捷傷害加成 7.2
		// if (_weaponType == 20 || _weaponType == 62) {
		// Integer dmg = (Integer) L1AttackList.DEXD.get(Integer.valueOf(_pc.getDex()));
		// if (dmg != null)
		// _statusDamage = dmg.intValue();
		// } else {
		// Integer dmg = (Integer) L1AttackList.STRD.get(Integer.valueOf(_pc.getStr()));
		// if (dmg != null) {
		// _statusDamage = dmg.intValue();
		// }
		// }

		// 力量敏捷傷害加成 7.6
		// 遠距離
		if (_weaponType == 20 || _weaponType == 62) {
			// _statusDamage = CalcStat.calcDexDmg(_pc.getDex(), _pc.getBaseDex());
			// Integer dmg = (Integer) L1AttackList.DEXD.get(Integer.valueOf(_pc.getDex()));
			// if (dmg != null) {
			// _statusDamage += dmg.intValue();
			// }
			// XXX 7.6屬性 ADD
			_statusDamage = L1ClassFeature.calcDexDmg(_pc.getDex(), _pc.getBaseDex());
		// 近距離
		} else {
			// _statusDamage = CalcStat.calcStrDmg(_pc.getStr(), _pc.getBaseStr());
			// Integer dmg = (Integer) L1AttackList.STRD.get(Integer.valueOf(_pc.getStr()));
			// if (dmg != null) {
			// _statusDamage += dmg.intValue();
			// }
			// XXX 7.6屬性 ADD
			_statusDamage = L1ClassFeature.calcStrDmg(_pc.getStr(), _pc.getBaseStr());
		}

		_target = target;
		_targetId = target.getId();
		_targetX = target.getX();
		_targetY = target.getY();
	}

	/**
	 * 取得命中值
	 */
	public int getHit() {
		return pcHitValue;
	}

	/**
	 * 計算攻擊是否命中
	 */
	public boolean calcHit() {
		if (_target == null) {
			_isHit = false;
			return _isHit;
		}

		if (_weaponRange != -1) {
			int location = _pc.getLocation().getTileLineDistance(_target.getLocation());

			if (location > _weaponRange + 1) {
				_isHit = false;
				return _isHit;
			}

		} else if (!_pc.getLocation().isInScreen(_target.getLocation())) {
			_isHit = false;
			return _isHit;
		}

		if ((_weaponType == 20) && (_weaponId != 190) && (_weaponId != 410220) && (_arrow == null)) {
			_isHit = false;
		} else if ((_weaponType == 62) && (_sting == null)) {
			_isHit = false;
		} else if (!_pc.glanceCheck(_targetX, _targetY)) {
			_isHit = false;
		} else if ((_weaponId == 247) || (_weaponId == 248) || (_weaponId == 249)) {
			_isHit = false;
		} else if (_calcType == PC_PC) {
			_isHit = calcPcHit();
		} else if (_calcType == PC_NPC) {
			_isHit = calcNpcHit();
		}

		return _isHit;
	}

	private void hitRateFunc() {
		final int pclv = _pc.getLevel();
		// 等級命中加乘
		_hitRate = pclv;

		// 近戰命中加成 7.6變更
		if ((_weaponType != 20) && (_weaponType != 62)) {
			// _hitRate += CalcStat.calcStrHit(_pc.getStr(), _pc.getBaseStr());
			// XXX 7.6屬性 ADD
			_hitRate += L1ClassFeature.calcStrHit(_pc.getStr(), _pc.getBaseStr());
			_hitRate += ((int) (_weaponAddHit + _pc.getHitup() + /* _pc.getOriginalHitup() + */ _weaponEnchant * 0.5D));
			_hitRate += _pc.getHitModifierByArmor();

			// 遠攻命中加成 7.6變更
		} else {
			// _hitRate += CalcStat.calcDexHit(_pc.getDex(), _pc.getBaseDex());
			// XXX 7.6屬性 ADD
			_hitRate += L1ClassFeature.calcDexHit(_pc.getDex(), _pc.getBaseDex());
			_hitRate += ((int) (_weaponAddHit + _pc.getBowHitup() + /* _pc.getOriginalBowHitup() + */ _weaponEnchant * 0.5D));
			_hitRate += _pc.getBowHitModifierByArmor();
		}

		if (_weaponType == 20) {
			if (_arrow != null) {
				_hitRate += _arrow.getItem().getArrowBowHit();
			}
		}

		// 負重影響命中結果
		int weight240 = _pc.getInventory().getWeight240();
		if (weight240 > 80) {
			if ((80 < weight240) && (120 >= weight240)) {
				_hitRate -= 1;
			} else if ((121 <= weight240) && (160 >= weight240)) {
				_hitRate -= 3;
			} else if ((161 <= weight240) && (200 >= weight240)) {
				_hitRate -= 5;
			}
		}

		// 料理追加命中
		_hitRate += hitUp();

		// 媽祖祝福攻擊命中+5
		if (_pc.is_mazu()) {
			_hitRate += 5;
		}

	}

	// 對PC命中計算
	private boolean calcPcHit() {
		// 玩家為空
		if (_targetPc == null) {
			return false;
		}
		// 身上有特定法術效果 傷害為0
		if (dmg0(_targetPc)) {
			return false;
		}
		// 完全閃避率
		if (calcEvasion()) {
			return false;
		}

		if (_pc.hasSkillEffect(L1SkillId.ABSOLUTE_BLADE)) { // 騎士新技能 絕御之刃
			if (_target.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {
				int chance = _pc.getLevel() - 79;
				if (chance >= 10)
					chance = 10;
				if (chance >= _random.nextInt(100) + 1) {
					_targetPc.removeSkillEffect(L1SkillId.ABSOLUTE_BARRIER);
					_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 14539));
					_targetPc.broadcastPacketX8(new S_SkillSound(_targetPc.getId(), 14539));
				}
			}
		}
		if (_targetPc.hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) {
			return false;
		}

		// 奇古獸命中100%
		if (_weaponType2 == 17) {
			return true;
		}

		// _hitRate += str_dex_Hit(); 7.2舊寫法
		hitRateFunc();// 7.6新寫法

		// _hitRate += (attackerDice(_targetPc) * 10);
		//
		// int defenderDice = 0;
		//
		// int defenderValue = (int) (_targetPc.getAc() * 1.5D) * -1;// 防禦*1.5並轉為正整數
		//
		// if (_targetPc.getAc() >= 0) {
		// defenderDice = 10 - _targetPc.getAc();
		// } else if (_targetPc.getAc() < 0) {
		// defenderDice = 10 + defenderValue + 1;
		//
		// }
		//
		// hit_rnd = _hitRate * 100 / defenderDice;// 攻擊命中機率
		//
		// if (hit_rnd > 95) {
		// hit_rnd = 95;
		// } else if (hit_rnd <= 5) {
		// hit_rnd = 5;
		// }
		// // System.out.println("hit_rnd == " + hit_rnd);
		//
		// int rnd = _random.nextInt(100) + 1;// 比對機率
		//
		// if ((_weaponType == 20 || _weaponType == 62) // 遠距離武器
		// && hit_rnd >= rnd) {// 命中時
		// return calcErEvasion();// ER閃避計算
		// }

		int attackerDice = _random.nextInt(20) + 1 + _hitRate - 10;

		// 被攻擊者防禦
		int defenderAc = _targetPc.getAc();

		if (_targetPc.hasSkillEffect(188)) {// 恐懼無助
			attackerDice += 5;
		}

		int defenderValue = (int) (defenderAc * 1.6) * -1;
		int defenderDice = defenderValue + calcDefenderDice(defenderAc);

		if (_weaponType == 20 || _weaponType == 62) {
			// defenderDice += (_targetPc.getEr() * 3);
			if (_pc.isElf()
					&& _pc.getReincarnationSkill()[2] > 0
					&& RandomArrayList.getInc(100, 1) > 100 - (_pc.getReincarnationSkill()[2] * 2)) { // 妖精天賦技能傷害擊殺
				_pc.sendPackets(new S_SystemMessage(L1SystemMessage.ShowMessage(8042))); // 發動天賦技能 傷害擊殺 無視玩家迴避率
			} else {
				defenderDice += (_targetPc.getEr() * 3);
			}
		}

		int otherValue = 5 + calcSkillAdd();

		hit_rnd = 0;
		if (_targetPc.isWizard()) {
			defenderDice -= 20;
		}
		if (attackerDice <= defenderDice) {
			double temp = ((defenderDice - attackerDice) * 0.1);
			if (temp < 1) {
				temp = 1;
			}
			otherValue += temp;
		}

		// if (_targetPc.hasSkillEffect(L1SkillId.ANTA_MAAN) // ??? ??-?? ???? ??
		// || _targetPc.hasSkillEffect(L1SkillId.BIRTH_MAAN) // ??? ??-?? ???? ??
		// || _targetPc.hasSkillEffect(L1SkillId.SHAPE_MAAN) // ??? ??-?? ???? ??
		// || _targetPc.hasSkillEffect(L1SkillId.LIFE_MAAN)) { // ??? ??-?? ???? ??
		// int MaanHitRnd = _random.nextInt(100) + 1;
		// if (MaanHitRnd <= 10) { // ??
		// otherValue += 10;
		// }
		// }

		hit_rnd = 100 - otherValue;

		if (_random.nextInt(100) + 1 < otherValue) {
			hit_rnd = 0;

		}

		int rnd = _random.nextInt(100) + 1;

		return hit_rnd >= rnd;
	}

	private int calcDefenderDice(int defenderAc) {
		int defenderDice=0;
		if (defenderAc >= 0){
			defenderDice = 10 - defenderAc;
		}else if (defenderAc < 0) {
		
			if (defenderAc <= -170)
				defenderDice += 10;
			else if (defenderAc <= -160)
				defenderDice += 7;
			else if (defenderAc <= -150)
				defenderDice += 5;
			else if (defenderAc <= -140)
				defenderDice += 4;
			else if (defenderAc <= -130)
				defenderDice += 3;
			else if (defenderAc <= -120)
				defenderDice += 2;
			else if (defenderAc <= -110)
				defenderDice += 1;
			else if (defenderAc <= -100)
				defenderDice += 0;
			else if (defenderAc <= -90)
				defenderDice += 0;
			else if (defenderAc <= -80)
				defenderDice += 0;
			else if (defenderAc <= -70)
				defenderDice += 0;
			else if (defenderAc <= -60)
				defenderDice += 0;
			else if (defenderAc <= -50)
				defenderDice += 0;
			else if (defenderAc <= -40)
				defenderDice += 0;
			else if (defenderAc <= -30)
				defenderDice += 0;
			else if (defenderAc <= -20)
				defenderDice += 0;
			else if (defenderAc <= -10)
				defenderDice += 0;
		}
		return defenderDice;
	}

	private boolean calcNpcHit() {

		int gfxid = _targetNpc.getNpcTemplate().get_gfxid();
		switch (gfxid) {
		case 2412:
			if (!_pc.getInventory().checkEquipped(20046)) {
				return false;
			}

			break;
		}

		if (dmg0(_targetNpc)) {
			return false;
		}

		if (_targetNpc.isShop()) {
			return false;
		}

		/*
		 * if(!checkAttackError(_targetNpc,_pc)){ return false; }
		 */

		if (_weaponType2 == 17) {// 奇古獸
			return true;
		}

		if (_targetNpc.hasSkillEffect(91)) {
			L1Magic magic = new L1Magic(_targetNpc, _pc);
			boolean isCounterBarrier = false;
			boolean isProbability = magic.calcProbabilityMagic(91);
			boolean isShortDistance = isShortDistance();
			L1ItemInstance weapon = _pc.getWeapon();
			if ((isProbability) && (isShortDistance) && (weapon.getItem().getType() != 17)) {
				isCounterBarrier = true;
			}
			if (isCounterBarrier) {
				commitCounterBarrier();
				return false;
			}
		}

		if (_targetNpc.hasSkillEffect(11060)) {
			commitCounterBarrier();
			return false;
		}

		// _hitRate += str_dex_Hit();7.2舊寫法
		hitRateFunc();// 7.6新寫法

		// int attackerDice = _random.nextInt(20) + 2 + _hitRate - 10;
		//
		// attackerDice += attackerDice(_targetNpc);
		//
		// int defenderDice = 20 - _targetNpc.getAc();
		//
		// int fumble = _hitRate - 9;
		// int critical = _hitRate + 10;
		//
		// if (attackerDice <= fumble) {
		// _hitRate = 0;
		// } else if (attackerDice >= critical) {
		// _hitRate = 100;
		// } else {
		// if (attackerDice > defenderDice) {
		// _hitRate = 100;
		// } else if (attackerDice <= defenderDice) {
		// _hitRate = 0;
		// }
		// }
		int pcHit = _hitRate;

		pcHitValue = _hitRate;

		int npcAc = (int) (_targetNpc.getAc() * -1) + 1;

		int finalHit = 0;

		if (pcHit - npcAc >= 0) {
			finalHit = (pcHit - npcAc) * 5;

		} else {
			finalHit = 5;
		}

		if (finalHit >= 95) {
			finalHit = 95;
		}

		if (finalHit <= 5) {
			finalHit = 5;
		}

		// int attackerDice = _random.nextInt(20) + 1 + _hitRate - 10;
		// //被攻擊者防禦
		// int defenderAc=_targetNpc.getAc();
		//
		// if (_targetNpc.hasSkillEffect(188)){
		// attackerDice += 5;
		// }
		//
		//
		// int defenderValue = (int) (defenderAc * 3.2) * -1;
		// int defenderDice = defenderValue+calcDefenderDice(defenderAc);
		//
		//
		// double attackTmp = 5 + calcSkillAdd();
		//
		// double attackOk = 0;
		// if (attackerDice <= defenderDice) {
		// double temp = ((defenderDice - attackerDice) * 0.1);
		//
		// if (temp < 1) {
		// temp = 1;
		// }
		//
		// attackTmp += temp;
		//
		// }
		//
		// attackOk = 100 - attackTmp;

		if (_random.nextInt(100) + 1 < 5) {
			finalHit = 0;

		}
		hit_rnd = finalHit;

		int npcId = _targetNpc.getNpcTemplate().get_npcId();

		Integer tgskill = (Integer) L1AttackList.SKNPC.get(Integer.valueOf(npcId));
		if ((tgskill != null) && (!_pc.hasSkillEffect(tgskill.intValue()))) {
			hit_rnd = 0;
		}

		Integer tgpoly = (Integer) L1AttackList.PLNPC.get(Integer.valueOf(npcId));
		if ((tgpoly != null) && (tgpoly.equals(Integer.valueOf(_pc.getTempCharGfx())))) {
			hit_rnd = 0;
		}

		int rnd = _random.nextInt(100) + 1;

		return hit_rnd >= rnd;
	}

	/**
	 * 鏡像 暗影閃避
	 * 
	 * @return
	 */
	private int calcSkillAdd() {
		int value = 0;

		if (_calcType == PC_PC) {
			if (_targetPc.hasSkillEffect(MIRROR_IMAGE)) { // 鏡像
				if (_targetPc.getAc() >= -69) {
					value += 3;
				} else if (_targetPc.getAc() >= -79) {
					value += 5;
				} else if (_targetPc.getAc() >= -89) {
					value += 7;
				} else if (_targetPc.getAc() >= -99) {
					value += 10;
				} else if (_targetPc.getAc() <= -100) {
					value += 15;
				}
			}
			if (_targetPc.hasSkillEffect(UNCANNY_DODGE)) { // 暗影閃避
				if (_targetPc.getAc() >= -69) {
					value += 3;
				} else if (_targetPc.getAc() >= -79) {
					value += 5;
				} else if (_targetPc.getAc() >= -89) {
					value += 7;
				} else if (_targetPc.getAc() >= -99) {
					value += 10;
				} else if (_targetPc.getAc() <= -100) {
					value += 15;
				}
			}
		} else if (_calcType == PC_NPC) {
			if (_targetNpc.hasSkillEffect(MIRROR_IMAGE)) { // 鏡像
				if (_targetNpc.getAc() >= -69) {
					value += 3;
				} else if (_targetNpc.getAc() >= -79) {
					value += 5;
				} else if (_targetNpc.getAc() >= -89) {
					value += 7;
				} else if (_targetNpc.getAc() >= -99) {
					value += 10;
				} else if (_targetNpc.getAc() <= -100) {
					value += 15;
				}
			}
			if (_targetNpc.hasSkillEffect(UNCANNY_DODGE)) { // 暗影閃避
				if (_targetNpc.getAc() >= -69) {
					value += 3;
				} else if (_targetNpc.getAc() >= -79) {
					value += 5;
				} else if (_targetNpc.getAc() >= -89) {
					value += 7;
				} else if (_targetNpc.getAc() >= -99) {
					value += 10;
				} else if (_targetNpc.getAc() <= -100) {
					value += 15;
				}
			}
		}

		return value;
	}

	/**
	 * 料理追加命中
	 * 
	 * @return
	 */
	private int hitUp() {
		int hitUp = 0;
		if (_pc.getSkillEffect().size() <= 0) {
			return hitUp;
		}

		if (!_pc.getSkillisEmpty()) {
			try {
				if ((_weaponType != 20) && (_weaponType != 62)) {
					for (Integer key : _pc.getSkillEffect()) {
						Integer integer = (Integer) L1AttackList.SKU1.get(key);
						if (integer != null) {
							hitUp += integer.intValue();
						}
					}
				} else {
					for (Integer key : _pc.getSkillEffect()) {
						Integer integer = (Integer) L1AttackList.SKU2.get(key);
						if (integer != null)
							hitUp += integer.intValue();
					}
				}
			} catch (ConcurrentModificationException localConcurrentModificationException) {
			} catch (Exception e) {
				_log.error(e.getLocalizedMessage(), e);
			}
		}

		return hitUp;
	}

	/**
	 * 最終傷害計算
	 */
	public int calcDamage() {
		switch (_calcType) {
		case PC_PC:
			_damage = calcPcDamage();
			break;
		case PC_NPC:
			_damage = calcNpcDamage();
			break;
		}

		for (L1ItemInstance item : this._pc.getInventory().getItems()) { // SRC0711
			if (item.isEquipped()) {
				Random rnd = new Random();
				String[] byarmor = item.getItem().getclassname().split(" ");
				if (byarmor[0].equals("SkillByArmor")) {
					boolean isin = false;
					for (int castle_id = 1; castle_id < 8; castle_id++) {
						if ((ServerWarExecutor.get().isNowWar(castle_id))
								&& (L1CastleLocation.checkInWarArea(castle_id, this._pc))) {
							isin = true;
						}
					}
					int bonus = 0;
					switch (item.getItem().getUseType()) {
					case 23:
					case 24:
					case 37:
					case 40:
						bonus = item.getEnchantLevel();
						if (bonus <= 0) {
							bonus = 0;
						}
						break;
					default:
						bonus = item.getEnchantLevel() - item.getItem().get_safeenchant();
						if ((bonus <= 0) || (item.getItem().get_safeenchant() == -1)) {
							bonus = 0;
						}
						break;
					}
					if (Integer.valueOf(byarmor[1]).intValue() + bonus >= rnd.nextInt(100) + 1) {
						this._damage += Integer.valueOf(byarmor[3]).intValue();
						if (!isin) {
							if (Integer.valueOf(byarmor[5]).intValue() == 0) {
								this._pc.sendPacketsAll(new S_SkillSound(this._target.getId(), Integer.valueOf(byarmor[2]).intValue()));
							} else if (Integer.valueOf(byarmor[5]).intValue() == 1) {
								S_UseAttackSkill packet = new S_UseAttackSkill(this._pc, this._target.getId(),
										Integer.valueOf(byarmor[2]).intValue(), this._target.getX(), this._target.getY(), 1, false);
								this._pc.sendPacketsAll(packet);
							}
						}
					}
				}
			}
		}
		if ((this._pc.hasSkillEffect(SOUL_OF_FLAME)) && (this._weaponType != 20) // SRC0808
				&& (this._weaponType != 62)) {
			if (_pc.getMeteLevel() >= 4) {
				//this._damage = ((int) (this._damage * (ConfigSkill.E4SOUL_OF_FLAME)));
				_damage = ((int) (_damage * (ConfigSkill.SOUL_OF_FLAME_DAMAGE + ConfigSkill.E4SOUL_OF_FLAME)));
			} else {
				this._damage = ((int) (this._damage * (ConfigSkill.SOUL_OF_FLAME_DAMAGE)));
			}
		}

		/*2017/04/19取消
		int outcount = 0;
		if (this._weapon != null) {
			if (FeatureItemSet.POWER_START) {
				L1AttackPower attackPower = new L1AttackPower(this._pc,
						this._target, this._weaponAttrEnchantKind,
						this._weaponAttrEnchantLevel);

				int add = attackPower.set_item_power(this._damage);
				if (add > 0) {
					this._damage += add;
					outcount++;
				}
			}*/	
		if (this._weapon != null) {
			if ((this._weapon.get_power_name() != null) && (this._weapon.get_power_name().get_boss_weapon() != null)) {

				boolean isLongRange;

				if ((this._weaponType == 20) || (this._weaponType == 62)) {
					isLongRange = true;
				} else {
					isLongRange = false;
				}

				this._damage = ((int) (this._damage + L1BossWeapon.getWeaponSkillDamage(this._pc, this._target,
						this._damage, this._weapon.get_power_name().get_boss_weapon(), isLongRange)));
			}

			if ((this._weapon.get_power_name() != null) && (this._weapon.get_power_name().get_magic_weapon() != null)) {
				boolean isLongRange;
				if ((this._weaponType == 20) || (this._weaponType == 62)) {
					isLongRange = true;
				} else {
					isLongRange = false;
				}
				this._damage = ((int) (this._damage + L1MagicWeapon.getWeaponSkillDamage(this._pc, this._target,
						this._damage, this._weapon.get_power_name().get_magic_weapon(), isLongRange)));
			}
			if (_weapon.get_power_name() != null) {
				final L1CriticalHitStone stone = _weapon.get_power_name().get_critical_hit_stone();
				if (stone != null

						&& ThreadLocalRandom.current().nextInt(100) < stone.getCriticalHitChance()) {

					_damage += getCriticalHitDamage(stone, _damage);
				}
			}
			// TODO 三重矢原始傷害倍率
			if (this._pc.isTripleArrow()) { // SRC0808
				double E2TripleArrow = 1.0;
				if (_pc.getMeteLevel() >= 2) {
					E2TripleArrow = ConfigSkill.E2TRIPLE_ARROW;
				}
				if (this._pc.getDex() >= ConfigSkill.TRIPLE_ARROW_DEX) {
					this._damage = (int) (this._damage * this._pc.getDex() / 100 * E2TripleArrow);
				} else {
					this._damage = ((int) (this._damage * ConfigSkill.TRIPLE_ARROW_DMG * E2TripleArrow));
				}
			}

			// 武器+9(含)以上附加額外增加傷害值
			if ((ConfigAlt.WEAPON_POWER) && (this._weaponEnchant >= 9)) {
				this._damage += ConfigAlt.WEAPON_POWER_LIST[Math.min(this._weaponEnchant - 9,
						ConfigAlt.WEAPON_POWER_LIST.length - 1)];
			}
		}

		/*2017/04/19取消
		if (outcount > 1) {
			{
			this._damage /= outcount;
		}*/

		/** 觸發機率區 START */

		/*2017/04/19取消
		if (_weapon != null) {
			if (FeatureItemSet.POWER_START) {// 特殊屬性武器
				final L1AttackPower attackPower = new L1AttackPower(_pc, _target, _weaponAttrEnchantKind,
						_weaponAttrEnchantLevel);
				_damage = attackPower.set_item_power(_damage);
			}
		}*/
		
		if (_pc.isWarrior() && _pc.getReincarnationSkill()[2] > 0) { // 戰士天賦技能殺氣擊斃
			if (_target.getCurrentHp() < (_target.getMaxHp() / 100) * 45) {
				_damage += (int) (_damage / 100.0) * (_pc.getReincarnationSkill()[2] * 3);
			}
		}
		
		if (_pc.isKnight()
				&& _pc.getReincarnationSkill()[1] > 0
				&& (_pc.getCurrentHp() < (_pc.getMaxHp() / 100) * 30) // 血量低於30%
				&& RandomArrayList.getInc(100, 1) > 100 - _pc.getReincarnationSkill()[1]) { // 騎士天賦技能狂暴致命
			int Reidamage = _pc.getMaxHp() / _pc.getCurrentHp(); // 傷害點數算法.最大血量/目前血量=傷害倍數
			if (Reidamage > 3) {
				Reidamage = 3; // 最高3倍
			}
			_damage *= Reidamage;
		}
		
		if (_pc.isDragonKnight() && _pc.getReincarnationSkill()[1] > 0 && RandomArrayList.getInc(100, 1) > 100 - _pc.getReincarnationSkill()[1] * 2) { // 龍騎天賦技能乾坤挪移
			_pc.setCurrentHp((short) (_pc.getCurrentHp() + _damage));
		}

		/*if (!_pc.isFoeSlayer()) {
			dk_dmgUp();// 弱點曝光機率計算
		}*/

		addPcPoisonAttack(_target);// 附加劇毒機率計算

		AttrAmuletEffect();// 觸發火焰之暈
		/** 觸發機率區 END */

		// 重複上面-註銷
		// 武器+9(含)以上附加額外增加傷害值
		// if ((ConfigAlt.WEAPON_POWER) && (this._weaponEnchant >= 9)) {
		// this._damage +=
		// ConfigAlt.WEAPON_POWER_LIST[Math.min(this._weaponEnchant - 9,
		// ConfigAlt.WEAPON_POWER_LIST.length - 1)];
		// }

		if (_pc.isFoeSlayer()) {// 使用屠宰者
			int dollFoeSlayerDmg = 0;
			if (_pc.getFoeSlayerDmg() > 0) { // 屠宰者階段別傷害
				dollFoeSlayerDmg = _pc.getFoeSlayerDmg();
			}
			if (_pc.get_weaknss() == 1) {
				_damage += 20 + dollFoeSlayerDmg;
			} else if (_pc.get_weaknss() == 2) {
				_damage += 40 + dollFoeSlayerDmg;
			} else if (_pc.get_weaknss() == 3) {
				_damage += 60 + dollFoeSlayerDmg;
			}
			// 880
			else if (_pc.get_weaknss() == 4) {
				_damage += 80 + dollFoeSlayerDmg;
			}
		}

		// pvp武器增傷
		if (_calcType == PC_PC && _weaponId >= 410214 && _weaponId <= 410221 && _weaponEnchant >= 8) {
			_damage += _weaponEnchant - 8 + 2;
		}
		if (_calcType == PC_PC) {
			switch (_weaponId) {

			case 21384:
			case 21385:
			case 21386:
				_damage += 1;
				break;
			case 21387:
			case 21388:
			case 21389:
				_damage += 2;
				break;
			case 21390:
			case 21391:
			case 21392:
				_damage += 4;
				break;
			case 21393:
			case 21394:
			case 21395:
				_damage += 5;
				break;

			default:
				break;
			}
		}
		if (_calcType == PC_PC) {
			if (_targetPc != null && _targetPc.getInventory().getItemEquipped(2, 17) != null) {
				switch (_targetPc.getInventory().getItemEquipped(2, 17).getItemId()) {
				case 330012:
				case 330013:
				case 330014:
					_damage -= 2;
					break;
				case 330015:
				case 330016:
				case 330017:
					_damage -= 5;
					break;
				default:
					break;
				}
			}
		}

		if (_pc.hasSkillEffect(L1SkillId.negativeId15)) {
			_damage /= 2.0D;
		}

		if (_damage > 0) {
			soulHp();
		}

		return _damage;
	}

	// 武器爆擊特效
	private void bonGfx() {

		if (_isHit) {// 命中
			int attackgfx = 0;
			switch (_weaponType2) {
			case 1:// 劍
				attackgfx = 13411;
				break;
			case 2:// 匕首
				attackgfx = 13412;
				break;
			case 3:// 雙手劍
				attackgfx = 13410;
				break;
			case 4:// 雙手弓
			case 13:// 單手弓
				attackgfx = 13392;
				break;
			case 14:// 矛(單手)
			case 5:// 矛(雙手)
				attackgfx = 13402;
				break;
			case 6:// 斧(單手)
			case 15:// 雙手斧
				attackgfx = 13415;
				break;
			case 7:// 魔杖
			case 16:// 魔杖(雙手)
				attackgfx = 13414;
				break;
			case 10:// 鐵手甲
				attackgfx = 13409;
				break;
			case 18:// 鎖鏈劍
				attackgfx = 13413;
				break;
			case 11:// 鋼爪
				attackgfx = 13416;
				break;
			case 12:// 雙刀
				attackgfx = 13417;
				break;
			case 17:// 奇古獸
				attackgfx = 13396;
				break;
			}

			if (attackgfx > 0 && attackgfx != 13396) {// 空手以及奇古獸以外的攻擊特效
				L1Location loc = _target.getLocation();
				L1NpcInstance dummy = L1SpawnUtil.spawnS(loc, 86132, _pc.get_showId(), 1, _pc.getHeading());
				dummy.broadcastPacketAll(new S_NPCPack(dummy));
				dummy.broadcastPacketAll(new S_SkillSound(dummy.getId(), attackgfx));
			}
		}
	}

	/**
	 * 武器亂數傷害計算
	 * 
	 * @param weaponMaxDamage
	 * @return
	 */
	private int weaponDamage1(int weaponMaxDamage) {
		int weaponDamage = 0;
		// 烈魂判斷 空手 傷害固定為0
		if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {// 烈焰之魂
			if (_weaponType2 == 0) {
				weaponDamage = 0;
			} else if (_weaponType != 20 && _weaponType != 62) {
				weaponDamage = _weaponSmall;
			} else {
				weaponDamage = 0;
			}
		} else {
			if (_weaponType2 == 0) {
				weaponDamage = 0;
			} else if (_weaponType != 20 && _weaponType != 62) {
				weaponDamage = _random.nextInt(weaponMaxDamage) + 1;
			} else {
				weaponDamage = 0;
			}
			if (/* _pc.getBaseStr() >= 40 && */_weaponType != 20 && _weaponType != 62) {// 力量
				if (_weaponType2 == 0) {
					weaponDamage = 0;
				} else {
					// 近距離爆擊率
					int closeRnd = L1ClassFeature.calcStrDmgCritical(_pc.getStr(), _pc.getBaseStr());

					// 近距離爆擊率
					if (_pc.getCloseCritical() > 0) {
						closeRnd += _pc.getCloseCritical();
					}

					if (_random.nextInt(100) < closeRnd) {
						weaponDamage = (int) (weaponDamage * 1.2D);
						bonGfx();
					}
				}
			}
		}

		switch (_weaponType2) {
		case 0:// 空手
			break;
		case 3:// 巨劍
			if ((_weaponId == 217) && (_pc.getLawful() < 500)) { // 吉爾塔斯之劍，邪惡值越高 攻擊力越高
				int a = _pc.getLawful() / 1000;
				int b = Math.abs(a);
				weaponDamage += b;
			} else if ((_weaponId == 410165) && (_pc.getLawful() < 500)) { // 紅騎士巨劍，邪惡值越高 攻擊力越高
				int a = _pc.getLawful() / 3000;
				int b = Math.abs(a);
				weaponDamage += b;

			}
			break;
		case 11:// 鋼爪

			if (_random.nextInt(100) + 1 <= _weaponDoubleDmgChance) {
				weaponDamage = weaponMaxDamage;// 重擊
				_attackType = 2;
			}
			break;

		}

		if (_pc.getClanid() != 0) {
			weaponDamage = (int) (weaponDamage + getDamageUpByClan(_pc));// 血盟技能增加傷害
		}

		switch (_pc.guardianEncounter()) {
		case 3:
			weaponDamage++;
			break;
		case 4:
			weaponDamage += 3;
			break;
		case 5:
			weaponDamage += 5;
			break;
		}
		// System.out.println("weaponDamage ==" + weaponDamage);
		return weaponDamage;
	}

	/**
	 * 屬性加成、道具加成、力量加成、初始加成計算
	 * 
	 * @param weaponTotalDamage
	 * @return
	 */
	private double weaponDamage2(double weaponTotalDamage) {
		double dmg = 0.0D;
		switch (_weaponType2) {
		case 1:// 劍
		case 2:// 匕首
		case 3:// 雙手劍
		case 5:// 矛(雙手)
		case 6:// 斧(單手)
		case 7:// 魔杖
		case 8:// 飛刀
		case 9:// 箭
		case 14:// 矛(單手)
		case 15:// 雙手斧
		case 16:// 魔杖(雙手)
		case 18:// 鎖鏈劍
			weaponTotalDamage += calcAttrEnchantDmg();// 屬性武器增傷計算
			weaponTotalDamage += calcAmuletAttrDmg();// 屬性項鏈增傷計算
			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getOriginalDmgup();
			break;
		case 11:// 鋼爪
			weaponTotalDamage += calcAttrEnchantDmg();// 屬性武器增傷計算
			weaponTotalDamage += calcAmuletAttrDmg();// 屬性項鏈增傷計算

			int addchance = 0;
			if (_pc.getLevel() > 45) {// 等級超過45級之後每5級增加1%發動機率
				addchance = (_pc.getLevel() - 45) / 5;
			}

			if (_pc.isSkillMastery(BRAKE_DESTINY)) { // 習得雙重破壞：強化
				addchance += 5; // 雙重破壞：強化-機率基礎+5
				if (_pc.getLevel() > 80) {
					// 從80等級開始每提升1等級增加1%
					addchance += _pc.getLevel() - 80;
				}
				if (ConfigSkill.AddPassiveSkillMsg) {
					_pc.sendPackets(new S_SystemMessage("測試雙重破壞：強化加機率。"));
				}
			}

			int totalchance = 20 + addchance;// 總發動機率

			if ((_pc.hasSkillEffect(DOUBLE_BREAK)) && (_random.nextInt(100) < totalchance)) {// 觸發雙重破壞
				weaponTotalDamage *= 2.0D;
			}

			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getOriginalDmgup();
			break;
		case 12:// 雙刀
			if (_random.nextInt(100) + 1 <= _weaponDoubleDmgChance) {
				weaponTotalDamage *= 2;// 雙擊
				_attackType = 4;
			}

			weaponTotalDamage += calcAttrEnchantDmg();// 屬性武器增傷計算
			weaponTotalDamage += calcAmuletAttrDmg();// 屬性項鏈增傷計算

			int addchance2 = 0;
			if (_pc.getLevel() > 45) {// 等級超過45級之後每5級增加1%發動機率
				addchance2 = (_pc.getLevel() - 45) / 5;
			}

			if (_pc.isSkillMastery(BRAKE_DESTINY)) { // 習得雙重破壞：強化
				addchance2 += 5; // 雙重破壞：強化-機率基礎+5
				if (_pc.getLevel() > 80) {
					// 從80等級開始每提升1等級增加1%
					addchance2 += _pc.getLevel() - 80;
				}
				if (ConfigSkill.AddPassiveSkillMsg) {
					_pc.sendPackets(new S_SystemMessage("測試雙重破壞：強化加機率。"));
				}
			}

			int totalchance2 = 20 + addchance2;// 總發動機率

			if ((_pc.hasSkillEffect(DOUBLE_BREAK)) && (_random.nextInt(100) < totalchance2)) {// 觸發雙重破壞
				weaponTotalDamage *= 2.0D;
			}

			dmg = weaponTotalDamage + _statusDamage + _pc.getDmgup() + _pc.getOriginalDmgup();
			break;
		case 0:// 空手
			dmg = _random.nextInt(5) + 4 >> 2;
			break;
		case 4:// 雙手弓
		case 13:// 單手弓
			weaponTotalDamage += calcAttrEnchantDmg();// 屬性武器增傷計算
			weaponTotalDamage += calcAmuletAttrDmg();// 屬性項鏈增傷計算

			dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup() + _pc.getOriginalBowDmgup();

			if (_arrow != null) {
				// int add_dmg = Math.max(_arrow.getItem().getDmgSmall(), 1);
				int add_dmg = Math.max(_arrow.getItem().getArrowBowDmg(), 1);

				// 遠距離爆擊率
				int bowRnd = L1ClassFeature.calcDexDmgCritical(_pc.getDex(), _pc.getBaseDex());

				// 遠距離爆擊率
				if (_pc.getBowCritical() > 0) {
					bowRnd += _pc.getBowCritical();
				}

				if (_random.nextInt(100) < bowRnd) {
					dmg = dmg * 1.2D;
					bonGfx();
				} else {
					dmg += _random.nextInt(add_dmg) + 1;
				}

			} else if (_arrow == null && (_weaponId == 190 || _weaponId == 410220)) {
				// int add_dmg = Math.max(12, 1);
				dmg += _random.nextInt(15) + 1;
			} // TODO 精準射擊傷害倍率
			if (_target.hasSkillEffect(174)) {
				dmg += dmg * (ConfigSkill.STRIKER_DMG2 - 1);
			}
			break;
		case 10:// 鐵手甲
			weaponTotalDamage += calcAttrEnchantDmg();// 屬性武器增傷計算
			weaponTotalDamage += calcAmuletAttrDmg();// 屬性項鏈增傷計算

			dmg = weaponTotalDamage + _statusDamage + _pc.getBowDmgup() + _pc.getOriginalBowDmgup();

			if (_sting != null) {
				int add_dmg = Math.max(_sting.getItem().getDmgSmall(), 1);
				dmg += _random.nextInt(add_dmg) + 1;
			}
			break;
		case 17:// 奇古獸
			dmg = L1WeaponSkill.getKiringkuDamage(_pc, _target);
			weaponTotalDamage += calcAttrEnchantDmg();// 屬性武器增傷計算
			weaponTotalDamage += calcAmuletAttrDmg();// 屬性項鏈增傷計算
			dmg += weaponTotalDamage;
			
			if(_pc.isIllusionist()&&_pc.getMeteLevel()>=2){  //SRC0808
				dmg += dmg * ConfigSkill.IS2;
			}
			// System.out.println("武器大小傷 = " + _weaponDamage);
			break;
		}

		if (_weaponType2 != 0) {
			int add_dmg = _weapon.getItem().get_add_dmg();// DB武器額外傷害設置
			if (add_dmg != 0) {
				dmg += add_dmg;
			}
		}

		return dmg;
	}

	/**
	 * 其它增傷計算
	 * 
	 * @param dmg
	 * @return
	 */
	private double pcDmgMode(double dmg) {
		dmg = calcBuffDamage(dmg);// 近戰BUFF增傷

		if ((_weaponType != 20) && (_weaponType != 62)) {// 近距離武器
			if (_weaponType != 58) {// 除了奇古獸
				dmg += _pc.getDmgModifierByArmor();
			}

		} else {// 遠距離武器
			dmg += _pc.getBowDmgModifierByArmor();
		}

		dmg += dmgUp();// 料理增傷
		dmg += calcCrash(_target);
		dmg += _pc.dmgAdd();// 娃娃機率增傷
		dmg += dk_dmgUp();// 弱點曝光機率計算

		// 附魔系統 兩倍傷害
		if ((_pc.get_double_dmg() != 0) && (_random.nextInt(100) + 1 <= _pc.get_double_dmg())) {
			dmg *= 2.0D;
		}

		return dmg;
	}

	private int calcCrash(final L1Character target) {
		int dmg = 0;
		int WW2RANDOM = 0;
		if (!_pc.isWarrior()) {
			return dmg;
		} // TODO 戰士 粉碎
		if (_pc.isWarrior() && _pc.getMeteLevel() >= 2) { // SRC0808
			WW2RANDOM = ConfigSkill.WHYW2;
		}
		if (_pc.isCRASH() && ((_random.nextInt(100) + 1) <= (ConfigSkill.isPassive_Tatin_CRASH + WW2RANDOM))) {
			int crashdmg = (2 + _pc.getLevel()) - 45;
			int gfxid = 12487;
			// 戰士 狂暴
			if (_pc.isFURY() && ((_random.nextInt(100) + 1) <= (ConfigSkill.isPassive_Tatin_FURY + WW2RANDOM))) {
				crashdmg *= 2;
				gfxid = 12489;
			}
			dmg += crashdmg;
			_pc.sendPacketsAll(new S_SkillSound(target.getId(), gfxid));
		}
		return dmg;
	}

	/**
	 * 攻擊PC時的傷害計算
	 * 
	 * @return
	 */
	public int calcPcDamage() {
		if (_targetPc == null) {
			return 0;
		}

		if (dmg0(_targetPc)) {
			_isHit = false;
			_drainHp = 0;
			return 0;
		}

		if (!_isHit) {
			return 0;
		}

		int c3_power_type = c3_power();
		if (c3_power() != 0) {
			return c3_power_to_pc(c3_power_type);
		}

		int weaponMaxDamage = _weaponSmall;

		/*
		 * if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {//烈焰之魂 if (_weaponType != 20 &&
		 * _weaponType != 62) { _weaponDamage = _weaponSmall; } }
		 */

		_weaponDamage = weaponDamage1(weaponMaxDamage);// 武器亂數傷害計算

		if (_pc.is_mazu()) {// 媽祖祝福武器傷害+5
			_weaponDamage += 5;
		}

		_weaponTotalDamage = _weaponDamage + _weaponAddDmg + _weaponEnchant;

		double dmg = weaponDamage2(_weaponTotalDamage); // 屬性加成、道具加成、力量加成、初始加成計算

		dmg = pcDmgMode(dmg);// 其他增傷計算

		if (_pc.hasSkillEffect(TRUE_TARGET)) {// 精準目標增傷
			double attackerlv = _pc.getLevel();
			double adddmg = (attackerlv / 15) / 100 + 1.01D;
			dmg *= adddmg;
		}
		if (_targetPc.hasSkillEffect(220)||_targetPc.hasSkillEffect(219)) {// 化身承受
			dmg *= 1.05;
		}
		// if ((_targetPc.hasSkillEffect(ARMOR_BREAK)) && (isShortDistance())) { // 破壞盔甲
		// dmg *= 1.58D;
		// }

		// 黑妖新技能
		if (_pc.hasSkillEffect(L1SkillId.ASSASSIN)) { // 黑妖新技能 暗殺者
			if (!_pc.getInventory().checkEquipped(20077) // 隱身斗篷
					&& !_pc.getInventory().checkEquipped(120077) // 隱身斗篷
					&& !_pc.getInventory().checkEquipped(20062) // 炎魔的血光斗篷
			) {
				if (_random.nextInt(100) + 1 <= 60) {
					dmg *= 2.5D;
					_pc.sendPackets(new S_SkillSound(_pc.getId(), 14547));
					_pc.broadcastPacketX8(new S_SkillSound(_pc.getId(), 14547));
					// 黑妖新技能 熾烈鬥志
					if (CharSkillReading.get().spellCheck(_pc.getId(), L1SkillId.BLAZING_SPIRITS)) {
						int time = 3 + (_pc.getLevel() - 85);
						if (time > 8) {
							time = 8;
						}
						_pc.setSkillEffect(L1SkillId.BLAZING_SPIRITS, time * 1000);
						_pc.sendPackets(new S_NewSkillIcon(L1SkillId.BLAZING_SPIRITS, true, time));
					}
				}
			} else {
				_pc.sendPackets(new S_SystemMessage("目前透明狀態下，這個技能發動無效。"));
			}
			_pc.removeSkillEffect(L1SkillId.ASSASSIN);
		}
		if (_pc.isDarkelf()) { // 黑妖新技能 暗殺者
			// 黑妖新技能 熾烈鬥志
			if (_pc.hasSkillEffect(L1SkillId.BLAZING_SPIRITS)) {
				dmg *= 2.5D;
				_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 14547));
				_targetPc.broadcastPacketX8(new S_SkillSound(_targetPc.getId(), 14547));
			}
		}
		// 黑妖新技能end

		// 占卜 2017/04/25
		Random random = new Random();
		if (_pc.hasSkillEffect(9971) && random.nextInt(100) + 1 <= 3) {
			dmg *= 1.2;
			_pc.sendPackets(new S_ServerMessage("發動-魔法占卜[3%發動爆擊]"));
		}
		if (_targetPc.hasSkillEffect(9976) && random.nextInt(100) + 1 <= 3) {
			_pc.sendPackets(new S_ServerMessage("發動-魔法占卜[減傷效果]"));
			dmg /= 3;
		}
		// 2017/04/25
		if (_pc.getbbdmg1() && RandomArrayList.getInc(100, 1) <= 3) {
			L1SpawnUtil.spawnEffect(81162, 1, _targetPc.getX(), _targetPc.getY(), _targetPc.getMapId(), _targetPc, 0);
			_targetPc.setSkillEffect(L1SkillId.SHOCK_STUN, 1000);
			_targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
			_pc.sendPackets(new S_ServerMessage("發動-魔法占卜[沖暈效果]"));
		}
		if (_pc.getbbdmg2() && RandomArrayList.getInc(100, 1) <= 10) {
			int drainHp = (int) (_random.nextInt(20) + 1);
			if (_pc.getCurrentHp() < (_pc.getMaxHp() * 0.97)) {
				this._pc.setCurrentHp((short) (this._pc.getCurrentHp() + drainHp));
				_pc.sendPackets(new S_ServerMessage("發動-魔法占卜[吸血效果]"));
			}
		}

		// 2017/04/19 修正
		if (_weapon != null) {
			if (FeatureItemSet.POWER_START) {// 特殊屬性武器
				final L1AttackPower attackPower = new L1AttackPower(_pc, _target, _weaponAttrEnchantKind,
						_weaponAttrEnchantLevel);
				dmg = attackPower.set_item_power((int) dmg);
			}
		}
		//2017/04/25
		if (_pc.getbbdmg3() && RandomArrayList.getInc(100, 1) <= 5) {
			int drainMp = (int)(_random.nextInt(20)+1);
			this._pc.setCurrentMp((short) (_pc.getCurrentMp() + drainMp));
			if (this._targetPc != null) {
				this._targetPc.setCurrentMp(Math.max(this._targetPc.getCurrentMp() - (drainMp/2), 0));
				_pc.sendPackets(new S_ServerMessage("發動-魔法占卜[吸魔效果]"));
		
			}
		}

		dmg += weaponSkill(_pc, _targetPc, _weaponTotalDamage);// 武器魔法增傷

		// dmg -= calcPcDefense(); // 被攻擊者防禦力傷害減低

		if (!_targetPc.hasSkillEffect(L1SkillId.negativeId13)) {
			dmg -= _targetPc.getDamageReductionByArmor()// 防具減免計算
					+ _targetPc.get_reduction_dmg();// 物理傷害減免
		}

		dmg -= _targetPc.dmgDowe();// 隨機傷害減免

		if (_targetPc.getClanid() != 0) {
			dmg -= getDamageReductionByClan(_targetPc);// 血盟技能減免
		}

		if (_pc.hasSkillEffect(L1SkillId.DESTROY)) { // 龍騎士新技能 撕裂護甲
			if (_pc.getWeapon().getItem().getType() == 18) { // 鎖鏈劍
				ArmorDestory();
			}
		}

		// TODO 戰士 護甲身軀
		if (_targetPc.isWarrior() && _targetPc.isARMORGARDE()) {
			// dmg -= _targetPc.getAc() / 10;
			dmg -= (Math.abs(_targetPc.getAc()) / 10);
		}

		if (_targetPc.hasSkillEffect(REDUCTION_ARMOR)) {// 增幅防禦減免
			int targetPcLvl = Math.max(_targetPc.getLevel(), 50);
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}

		/** [原碼] 反叛者的盾牌 機率減免傷害 */
		for (L1ItemInstance item : _targetPc.getInventory().getItems()) {
			if (item.getItemId() == 400041 && item.isEquipped()) {
				int r = random.nextInt(100) + 1;
				if ((item.getEnchantLevel() * 2) >= r) {
					dmg -= 50;
					_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 6320));
				}
			}
		}
		if (_targetPc.getInventory().checkEquipped(404007)) {// 1階貝爾丹蒂 祝福長靴 //2017/4/21
			dmg -= 1;
		} else if (_targetPc.getInventory().checkEquipped(404085)) {// 2階貝爾丹蒂 祝福長靴
			dmg -= 2;
		} else if (_targetPc.getInventory().checkEquipped(404086)) {// 3階貝爾丹蒂 祝福長靴
			dmg -= 3;
		} else if (_targetPc.getInventory().checkEquipped(404087)) {// 4階貝爾丹蒂 祝福長靴
			dmg -= 4;
		} else if (_targetPc.getInventory().checkEquipped(404088)) {// 5階貝爾丹蒂 祝福長靴
			dmg -= 5;
		} else if (_targetPc.getInventory().checkEquipped(404089)) {// 6階貝爾丹蒂 祝福長靴
			dmg -= 6;
		} else if (_targetPc.getInventory().checkEquipped(404090)) {// 7階貝爾丹蒂 祝福長靴
			dmg -= 7;
		} else if (_targetPc.getInventory().checkEquipped(404091)) {// 8階貝爾丹蒂 祝福長靴
			dmg -= 8;
		} else if (_targetPc.getInventory().checkEquipped(404092)) {// 9階貝爾丹蒂 祝福長靴
			dmg -= 9;
		}

		if (_targetPc.getPvpDmg_R() != 0) { // 減免PVP傷害
			if (_targetPc.hasSkillEffect(L1SkillId.negativeId13)) {
				dmg -= _targetPc.getPvpDmg_R() / 2;
			} else {
				dmg -= _targetPc.getPvpDmg_R();
			}
		}

		// 新排行系統 最強的祝福 PVP減免+8
		if (_targetPc.hasSkillEffect(L1SkillId.RANKING_BUFF_TOP)) {
			dmg -= 8;
		}

		if (_pc.getPvpDmg() != 0) { // 增加PVP傷害
			dmg += _pc.getPvpDmg();
		}

		// dmg += _pc.getAntiDamageReduction(); // 無視減免 -> 暫改直接加傷害
		// 無視減免
		if (_pc.getAntiDamageReduction() > 0) {
			// 被攻擊玩家的減傷值
			int targetReduc = _targetPc.getDamageReductionByArmor() + _targetPc.get_reduction_dmg();
			if (targetReduc > _pc.getAntiDamageReduction()) {
				targetReduc = _pc.getAntiDamageReduction();
			}
			if (targetReduc > 0) {
				dmg += targetReduc;
			} else {
				dmg += 0;
			}
			// _pc.sendPackets(new S_SystemMessage("測試targetReduc：" + targetReduc));
		}

		// 其他減免計算
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
		if (_pc.isCrown() && _targetPc.isCrown()) {
			dmg *= ConfigSkill.Crown1;
		}

		if (_pc.isCrown() && _targetPc.isKnight()) {
			dmg *= ConfigSkill.Crown2;
		}

		if (_pc.isCrown() && _targetPc.isElf()) {
			dmg *= ConfigSkill.Crown3;
		}

		if (_pc.isCrown() && _targetPc.isWizard()) {
			dmg *= ConfigSkill.Crown4;
		}

		if (_pc.isCrown() && _targetPc.isDarkelf()) {
			dmg *= ConfigSkill.Crown5;
		}

		if (_pc.isCrown() && _targetPc.isDragonKnight()) {
			dmg *= ConfigSkill.Crown6;
		}

		if (_pc.isCrown() && _targetPc.isIllusionist()) {
			dmg *= ConfigSkill.Crown7;
		}
		if (_pc.isCrown() && _targetPc.isWarrior()) {
			dmg *= ConfigSkill.Crown8;
		}
		//

		if (_pc.isKnight() && _targetPc.isCrown()) {
			dmg *= ConfigSkill.Knight1;
		}

		if (_pc.isKnight() && _targetPc.isKnight()) {
			dmg *= ConfigSkill.Knight2;
		}

		if (_pc.isKnight() && _targetPc.isElf()) {
			dmg *= ConfigSkill.Knight3;
		}

		if (_pc.isKnight() && _targetPc.isWizard()) {
			dmg *= ConfigSkill.Knight4;
		}

		if (_pc.isKnight() && _targetPc.isDarkelf()) {
			dmg *= ConfigSkill.Knight5;
		}

		if (_pc.isKnight() && _targetPc.isDragonKnight()) {
			dmg *= ConfigSkill.Knight6;
		}

		if (_pc.isKnight() && _targetPc.isIllusionist()) {
			dmg *= ConfigSkill.Knight7;
		}
		if (_pc.isKnight() && _targetPc.isWarrior()) {
			dmg *= ConfigSkill.Knight8;
		}
		//

		if (_pc.isElf() && _targetPc.isCrown()) {
			dmg *= ConfigSkill.Elf1;
		}

		if (_pc.isElf() && _targetPc.isKnight()) {
			dmg *= ConfigSkill.Elf2;
		}

		if (_pc.isElf() && _targetPc.isElf()) {
			dmg *= ConfigSkill.Elf3;
		}

		if (_pc.isElf() && _targetPc.isWizard()) {
			dmg *= ConfigSkill.Elf4;
		}

		if (_pc.isElf() && _targetPc.isDarkelf()) {
			dmg *= ConfigSkill.Elf5;
		}

		if (_pc.isElf() && _targetPc.isDragonKnight()) {
			dmg *= ConfigSkill.Elf6;
		}

		if (_pc.isElf() && _targetPc.isIllusionist()) {
			dmg *= ConfigSkill.Elf7;
		}
		if (_pc.isElf() && _targetPc.isWarrior()) {
			dmg *= ConfigSkill.Elf8;
		}
		//

		if (_pc.isWizard() && _targetPc.isCrown()) {
			dmg *= ConfigSkill.Wizard1;
		}

		if (_pc.isWizard() && _targetPc.isKnight()) {
			dmg *= ConfigSkill.Wizard2;
		}

		if (_pc.isWizard() && _targetPc.isElf()) {
			dmg *= ConfigSkill.Wizard3;
		}

		if (_pc.isWizard() && _targetPc.isWizard()) {
			dmg *= ConfigSkill.Wizard4;
		}

		if (_pc.isWizard() && _targetPc.isDarkelf()) {
			dmg *= ConfigSkill.Wizard5;
		}

		if (_pc.isWizard() && _targetPc.isDragonKnight()) {
			dmg *= ConfigSkill.Wizard6;
		}

		if (_pc.isWizard() && _targetPc.isIllusionist()) {
			dmg *= ConfigSkill.Wizard7;
		}
		if (_pc.isWizard() && _targetPc.isWarrior()) {
			dmg *= ConfigSkill.Wizard8;
		}
		//

		if (_pc.isDarkelf() && _targetPc.isCrown()) {
			dmg *= ConfigSkill.Darkelf1;
		}

		if (_pc.isDarkelf() && _targetPc.isKnight()) {
			dmg *= ConfigSkill.Darkelf2;
		}

		if (_pc.isDarkelf() && _targetPc.isElf()) {
			dmg *= ConfigSkill.Darkelf3;
		}

		if (_pc.isDarkelf() && _targetPc.isWizard()) {
			dmg *= ConfigSkill.Darkelf4;
		}

		if (_pc.isDarkelf() && _targetPc.isDarkelf()) {
			dmg *= ConfigSkill.Darkelf5;
		}

		if (_pc.isDarkelf() && _targetPc.isDragonKnight()) {
			dmg *= ConfigSkill.Darkelf6;
		}

		if (_pc.isDarkelf() && _targetPc.isIllusionist()) {
			dmg *= ConfigSkill.Darkelf7;
		}
		if (_pc.isDarkelf() && _targetPc.isWarrior()) {
			dmg *= ConfigSkill.Darkelf8;
		}
		//

		if (_pc.isDragonKnight() && _targetPc.isCrown()) {
			dmg *= ConfigSkill.DragonKnight1;
		}

		if (_pc.isDragonKnight() && _targetPc.isKnight()) {
			dmg *= ConfigSkill.DragonKnight2;
		}

		if (_pc.isDragonKnight() && _targetPc.isElf()) {
			dmg *= ConfigSkill.DragonKnight3;
		}

		if (_pc.isDragonKnight() && _targetPc.isWizard()) {
			dmg *= ConfigSkill.DragonKnight4;
		}

		if (_pc.isDragonKnight() && _targetPc.isDarkelf()) {
			dmg *= ConfigSkill.DragonKnight5;
		}

		if (_pc.isDragonKnight() && _targetPc.isDragonKnight()) {
			dmg *= ConfigSkill.DragonKnight6;
		}

		if (_pc.isDragonKnight() && _targetPc.isIllusionist()) {
			dmg *= ConfigSkill.DragonKnight7;
		}
		if (_pc.isDragonKnight() && _targetPc.isWarrior()) {
			dmg *= ConfigSkill.DragonKnight8;
		}
		//

		if (_pc.isIllusionist() && _targetPc.isCrown()) {
			dmg *= ConfigSkill.Illusionist1;
		}

		if (_pc.isIllusionist() && _targetPc.isKnight()) {
			dmg *= ConfigSkill.Illusionist2;
		}

		if (_pc.isIllusionist() && _targetPc.isElf()) {
			dmg *= ConfigSkill.Illusionist3;
		}

		if (_pc.isIllusionist() && _targetPc.isWizard()) {
			dmg *= ConfigSkill.Illusionist4;
		}

		if (_pc.isIllusionist() && _targetPc.isDarkelf()) {
			dmg *= ConfigSkill.Illusionist5;
		}

		if (_pc.isIllusionist() && _targetPc.isDragonKnight()) {
			dmg *= ConfigSkill.Illusionist6;
		}

		if (_pc.isIllusionist() && _targetPc.isIllusionist()) {
			dmg *= ConfigSkill.Illusionist7;
		}
		if (_pc.isIllusionist() && _targetPc.isWarrior()) {
			dmg *= ConfigSkill.Illusionist8;
		}
		if (_pc.isWarrior() && _targetPc.isCrown()) {
			dmg *= ConfigSkill.Warrior1;
		}

		if (_pc.isWarrior() && _targetPc.isKnight()) {
			dmg *= ConfigSkill.Warrior2;
		}

		if (_pc.isWarrior() && _targetPc.isElf()) {
			dmg *= ConfigSkill.Warrior3;
		}

		if (_pc.isWarrior() && _targetPc.isWizard()) {
			dmg *= ConfigSkill.Warrior4;
		}

		if (_pc.isWarrior() && _targetPc.isDarkelf()) {
			dmg *= ConfigSkill.Warrior5;
		}

		if (_pc.isWarrior() && _targetPc.isDragonKnight()) {
			dmg *= ConfigSkill.Warrior6;
		}

		if (_pc.isWarrior() && _targetPc.isIllusionist()) {
			dmg *= ConfigSkill.Warrior7;
		}
		if (_pc.isWarrior() && _targetPc.isWarrior()) {
			dmg *= ConfigSkill.Warrior8;
		}
		dmg = BuffDmgUp(dmg);// 屬火、燃斗、勇猛意志增傷計算

		if (dmgX2) {// 聖界減傷
			dmg /= 2.0D;
		}

		if (_targetPc.hasSkillEffect(LUCIFER)) { // 暗影屏障-所承受的傷害減少10%
			dmg -= (dmg * 0.1);
		}

		// 魔法娃娃特殊技能
		if (!_pc.getDolls().isEmpty()) {// 娃娃攻擊技能機率計算
			for (Object obj : _pc.getDolls().values().toArray()) {
				L1DollInstance doll = (L1DollInstance) obj;
				doll.startDollSkill(_targetPc, false);
			}
		}

		// 魔法娃娃特殊技能
		if (!_pc.getDolls2().isEmpty()) {// 娃娃攻擊技能機率計算
			for (Object obj : _pc.getDolls2().values().toArray()) {
				L1DollInstance2 doll = (L1DollInstance2) obj;
				doll.startDollSkill(_targetPc, false);
			}
		}

		// TODO 近距離武器 黑妖 破壞盔甲傷害倍率
		if (_weaponRange != -1 && _targetPc.hasSkillEffect(ARMOR_BREAK)) {
			// dmg += dmg * 0.58;
			dmg += dmg * ConfigSkill.Armor_Break_Dmg;
		}

		// TODO 近距離武器
		// 近距離武器
		// 近距離武器
		if (_targetPc.hasSkillEffect(113)) { // SRC0808
			if (_targetPc.getMeteLevel() < 2) {
				dmg += dmg * (ConfigSkill.STRIKER_DMG - 1);
			} else {
				dmg += dmg * (ConfigSkill.STRIKER_DMG - 1 + ConfigSkill.C2);
			}
		}

		// 魔法娃娃特殊技能(OLD)
		// if (!_pc.getDolls().isEmpty()) {
		// for (final Iterator<L1DollInstance> iter =
		// _pc.getDolls().values().iterator(); iter.hasNext();) {
		// final L1DollInstance doll = iter.next();
		// doll.startDollSkill(_targetPc, dmg);
		// }
		// }

		// TODO 戰士魔法
		int TiTanHp = 0;
		if (_targetPc.get_TiTanHp() > 0) { // 人物-泰坦系列技能發動HP區間%增加
			TiTanHp += _targetPc.get_TiTanHp();
		}
		if (_targetPc.isWarrior() && _targetPc.getMeteLevel() >= 4) {
			TiTanHp += ConfigSkill.WHYW4;
		}
		if (_targetPc.getRisingUp() > 0) { // 泰坦狂暴-泰坦系列技能發動HP區間%增加
			TiTanHp += _targetPc.getRisingUp();
		}

		if (_targetPc.isWarrior()
				&& (_targetPc.getCurrentHp() < ((_targetPc.getMaxHp() / 100) * (ConfigSkill.Warrior_Magic + TiTanHp))) // 血量低於40%
		        && _targetPc.isCrystal()) {// 足夠魔法結晶體
			if ((_weaponType != 20) && (_weaponType != 62)) {
				// 泰坦：岩石
				if (_targetPc.isTITANROCK() && (_random.nextInt(100) < ConfigSkill.isPassive_Tatin_Rock)) {
					dmg = 0;
					actionTitan(false);
					commitTitan(_targetPc.colcTitanDmg());
				}
			} else {// 泰坦：子彈
				if (_targetPc.isTITANBULLET() && (_random.nextInt(100) < ConfigSkill.isPassive_Tatin_Bullet)) {
					dmg = 0;
					actionTitan(true);
					commitTitan(_targetPc.colcTitanDmg());
				}
			}
		}

	    //判斷人物等級已大於 使用武器最大等級
		// if(_weaponType2!=0 && _pc.getWeapon()!=null){
		// if(_pc.getLevel()>_pc.getWeapon().getItem().getMaxLevel()){
		// dmg/=2;
		// }
		// }

		if (_pc.isWarrior()
				&& _pc.getReincarnationSkill()[0] > 0 && RandomArrayList.getInc(100, 1) > 100 - _pc.getReincarnationSkill()[0]) { // 戰士天賦技能冰封心智
			_targetPc.setSkillEffect(L1SkillId.ReiSkill_2, 2 * 1000);
			_targetPc.sendPackets(new S_SystemMessage(L1SystemMessage.ShowMessage(8040))); // 受到戰士天賦技能冰封心智效果影響，暫無法攻擊。
		}

		if (_pc.isDragonKnight()
				//&& _weaponType2 == 18
				&& _pc.getReincarnationSkill()[0] > 0
				&& _pc.isFoeSlayer()
				&& RandomArrayList.getInc(100, 1) > 100 - _pc.getReincarnationSkill()[0]) { // 龍騎天賦技能屠宰擊殺
			dmg += (int) (_targetPc.getCurrentHp() / 100) * 3;
		}
		
        if (_targetPc.isDragonKnight() && _targetPc.getReincarnationSkill()[2] > 0) { // 龍騎天賦技能強之護鎧
        	dmg -= (int) (dmg / 100.0) * _targetPc.getReincarnationSkill()[2];
        }
		
		if (_pc.isElf()
				&& _pc.getReincarnationSkill()[1] > 0 && RandomArrayList.getInc(100, 1) > 100 - _pc.getReincarnationSkill()[1]) { // 妖精天賦技能吞噬魔力
			_pc.sendPackets(new S_SystemMessage(L1SystemMessage.ShowMessage(8014))); // 發動天賦技能 吞噬魔力 扣除對方2%魔力。
			//_targetPc..sendPackets(new S_SkillSound(_targetPc..getId(), 240));
			//_targetPc..broadcastPacketAll(new S_SkillSound(_targetPc..getId(), 240));
			int ReiMp = (_targetPc.getCurrentMp() / 100) * 2;
			int newMp = _targetPc.getCurrentMp() - ReiMp;
			if (newMp < 0) {
				newMp = 0;
			}
			_targetPc.setCurrentMp(newMp);
		}
		
		if ((_pc.isDarkelf()) && (_pc.getReincarnationSkill()[0] > 0)) { // 黑妖天賦技能背部襲擊
			boolean isSameAttr = false;
			if ((_targetPc.getHeading() == 0) && ((_pc.getHeading() == 7) || (_pc.getHeading() == 0) || (_pc.getHeading() == 1)))
				isSameAttr = true;
			else if ((_targetPc.getHeading() == 7) && ((_pc.getHeading() == 6) || (_pc.getHeading() == 7) || (_pc.getHeading() == 0)))
				isSameAttr = true;
			else if ((_targetPc.getHeading() == _pc.getHeading()) || (_targetPc.getHeading() == _pc.getHeading() + 1) || (_targetPc.getHeading() == _pc.getHeading() - 1)) {
				isSameAttr = true;
			}
			if (isSameAttr) {
				if (RandomArrayList.getInc(100, 1) > 100 - _pc.getReincarnationSkill()[0]) {
					dmg = (int) (dmg * 1.5D);
					//_pc.sendPackets(new S_SkillSound(_pc.getId(), 5377));
					_pc.sendPackets(new S_SystemMessage(L1SystemMessage.ShowMessage(8044))); // 發動天賦技能 背部襲擊 造成1.5倍傷害
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
			_drainHp = 0;
		}

		return (int) dmg;
	}

	private int c3_power() {
		if (_pc.hasSkillEffect(7005)) {
			return 1;
		}
		if (_pc.hasSkillEffect(7006)) {
			return 2;
		}
		return 0;
	}

	private int c3_power_to_pc(int type) {// 陣營增傷
		int damage = c3_power_dmg(type);
		int resist = 0;
		switch (type) {
		case 1:
			resist = _targetPc.getFire();

			if (resist > 0) {
				damage = c3_power_dmg_down(damage, Math.min(100, resist));
			} else if (resist < 0) {
				damage = c3_power_dmg_up(damage, Math.min(0, resist));
			}
			break;
		case 2:
			resist = _targetPc.getWater();

			if (resist > 0) {
				damage = c3_power_dmg_down(damage, Math.min(100, resist));
			} else if (resist < 0) {
				damage = c3_power_dmg_up(damage, Math.min(0, resist));
			}
			break;
		}
		return damage;
	}

	private int c3_power_to_npc(int type) {// 陣營增傷
		int damage = c3_power_dmg(type);
		switch (type) {
		case 1:
			if ((_targetNpc instanceof L1MonsterInstance)) {
				L1MonsterInstance tgmob = (L1MonsterInstance) _targetNpc;
				if (!tgmob.isDead()) {
					tgmob.receiveDamage(_pc, damage, 2);
					tgmob.broadcastPacketX8(new S_DoActionGFX(tgmob.getId(), 2));
				}
			}

			break;
		case 2:
			if ((_targetNpc instanceof L1MonsterInstance)) {
				L1MonsterInstance tgmob = (L1MonsterInstance) _targetNpc;
				if (!tgmob.isDead()) {
					tgmob.receiveDamage(_pc, damage, 4);
					tgmob.broadcastPacketX8(new S_DoActionGFX(tgmob.getId(), 2));
				}
			}
			break;
		}

		return 0;
	}

	private int c3_power_dmg_down(int damage, int resist) {
		int r = 100 - resist;
		int dmg = damage * r / 100;
		return Math.max(10, dmg);
	}

	private int c3_power_dmg_up(int damage, int resist) {
		int dmg = damage - damage * resist / 100;
		return Math.abs(dmg);
	}

	private int c3_power_dmg(int type) {
		int damage = 0;
		int level = _pc.getLevel();
		switch (type) {
		case 1:
			if ((level >= 50) && (level < 70)) {
				damage = random_dmg(40, 100);
			} else if ((level >= 70) && (level < 90)) {
				damage = random_dmg(50, 120);
			} else if ((level >= 90) && (level < 110)) {
				damage = random_dmg(60, 140);
			} else if ((level >= 110) && (level < 130)) {
				damage = random_dmg(70, 160);
			} else if ((level >= 130) && (level < 150)) {
				damage = random_dmg(80, 180);
			} else if ((level >= 150) && (level < 175)) {
				damage = random_dmg(90, 200);
			} else if ((level >= 175) && (level < 190)) {
				damage = random_dmg(100, 250);
			} else if ((level >= 190) && (level < 200)) {
				damage = random_dmg(110, 300);
			} else {
				damage = random_dmg(200, 300);
			}
			break;
		case 2:
			if ((level >= 50) && (level < 70)) {
				damage = random_dmg(40, 100);
			} else if ((level >= 70) && (level < 90)) {
				damage = random_dmg(50, 120);
			} else if ((level >= 90) && (level < 110)) {
				damage = random_dmg(60, 140);
			} else if ((level >= 110) && (level < 130)) {
				damage = random_dmg(70, 160);
			} else if ((level >= 130) && (level < 150)) {
				damage = random_dmg(80, 180);
			} else if ((level >= 150) && (level < 175)) {
				damage = random_dmg(90, 200);
			} else if ((level >= 175) && (level < 190)) {
				damage = random_dmg(100, 250);
			} else if ((level >= 190) && (level < 200)) {
				damage = random_dmg(110, 300);
			} else {
				damage = random_dmg(200, 300);
			}
			break;
		}
		return damage;
	}

	private int random_dmg(int i, int j) {
		return _random.nextInt(j - i) + i;
	}

	/**
	 * 攻擊NPC時的傷害計算
	 * @return
	 */
	private int calcNpcDamage() {
		if (_targetNpc == null) {
			return 0;
		}

		if (dmg0(_targetNpc)) {
			_isHit = false;
			_drainHp = 0;
			return 0;
		}

		if (!_isHit) {
			return 0;
		}

		int c3_power_type = c3_power();
		if (c3_power() != 0) {
			return c3_power_to_npc(c3_power_type);
		}

		int weaponMaxDamage = 0;

		if ((_targetNpc.getNpcTemplate().isSmall()) && (_weaponSmall > 0)) {
			weaponMaxDamage = _weaponSmall;
		} else if ((_targetNpc.getNpcTemplate().isLarge()) && (_weaponLarge > 0)) {
			weaponMaxDamage = _weaponLarge;
		} else if (_weaponSmall > 0) {
			weaponMaxDamage = _weaponSmall;
		}

		/*
		 * if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {//烈焰之魂 if (_weaponType != 20 &&
		 * _weaponType != 62) { _weaponDamage = weaponMaxDamage; } }
		 */

		_weaponDamage = weaponDamage1(weaponMaxDamage);

		if (_pc.is_mazu()) {// 媽祖祝福武器傷害+5
			_weaponDamage += 5;
		}

		_weaponTotalDamage = _weaponDamage + _weaponAddDmg + _weaponEnchant;

		_weaponTotalDamage += calcMaterialBlessDmg();// 武器材質、祝福狀態增傷計算

		double dmg = weaponDamage2(_weaponTotalDamage);// 屬性加成、道具加成、力量加成、初始加成計算

		dmg = pcDmgMode(dmg);// 其他增傷計算

		// 黑妖新技能
		if (_pc.hasSkillEffect(L1SkillId.ASSASSIN)) { // 黑妖新技能 暗殺者
			if (!_pc.getInventory().checkEquipped(20077) // 隱身斗篷
					&& !_pc.getInventory().checkEquipped(120077) // 隱身斗篷
					&& !_pc.getInventory().checkEquipped(20062) // 炎魔的血光斗篷
			) {
				if (_random.nextInt(100) + 1 <= 60) {
					dmg *= 2.5D;
					_pc.sendPackets(new S_SkillSound(_pc.getId(), 14547));
					_pc.broadcastPacketX8(new S_SkillSound(_pc.getId(), 14547));
					// 黑妖新技能 熾烈鬥志
					if (CharSkillReading.get().spellCheck(_pc.getId(), L1SkillId.BLAZING_SPIRITS)) {
						int time = 3 + (_pc.getLevel() - 85);
						if (time > 8) {
							time = 8;
						}
						_pc.setSkillEffect(L1SkillId.BLAZING_SPIRITS, time * 1000);
						_pc.sendPackets(new S_NewSkillIcon(L1SkillId.BLAZING_SPIRITS, true, time));
					}
				}
			} else {
				_pc.sendPackets(new S_SystemMessage("目前透明狀態下，這個技能發動無效。"));
			}
			_pc.removeSkillEffect(L1SkillId.ASSASSIN);
		}
		if (_pc.isDarkelf()) { // 黑妖新技能 暗殺者
			// 黑妖新技能 熾烈鬥志
			if (_pc.hasSkillEffect(L1SkillId.BLAZING_SPIRITS)) {
				dmg *= 2.5D;
				_targetNpc.broadcastPacketX8(new S_SkillSound(_targetNpc.getId(), 14547));
			}
		}
		// 黑妖新技能end

		//System.out.println("TEST first:"+dmg);

		//2017/04/19 修正	
		if (_weapon != null) {
			if (FeatureItemSet.POWER_START) {// 特殊屬性武器
				final L1AttackPower attackPower = new L1AttackPower(_pc, _target, _weaponAttrEnchantKind,
						_weaponAttrEnchantLevel);
				dmg = attackPower.set_item_power((int)dmg);
			}
		}

		dmg += weaponSkill(_pc, _targetNpc, _weaponTotalDamage);// 武器魔法增傷

		// dmg -= calcNpcDamageReduction(); // NPC防禦力傷害減免
		// 貫通效果 暫定為無視硬皮NPC防禦力額外傷害減低
		if (_targetNpc.getNpcTemplate().is_hard() && _weapon != null && _weapon.getItem().get_penetrate() == 1) {
		} else {
			dmg -= calcNpcDamageReduction(); // NPC防禦力傷害減免
		}

		boolean isNowWar = false;
		int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
		if (castleId > 0) {
			isNowWar = ServerWarExecutor.get().isNowWar(castleId);
		}

		if (!isNowWar) {// 不是在攻城區內
			if ((_targetNpc instanceof L1PetInstance)) {// 寵物減免
				dmg /= 8.0D;
			}
			if ((_targetNpc instanceof L1SummonInstance)) {// 召喚獸減免
				L1SummonInstance summon = (L1SummonInstance) _targetNpc;
				if (summon.isExsistMaster()) {
					dmg /= 8.0D;
				}
			}
		}
		if (_pc.isActived() && !_pc.hasSkillEffect(100611)) {
			_pc.setSkillEffect(100611, 500000);
		}
		int cc = 0;
		if (_pc.isActived() && dmg <= 0) {
			cc++;
		} else if (_pc.isActived() && dmg > 0 && cc > 0) {
			cc--;
		}
		if (cc >= 10 && _pc.isActived()) {
			L1Teleport.randomTeleport(_pc, true);
			cc = 0;
		}
		dmg = BuffDmgUp(dmg);// 屬火、燃斗、勇猛意志增傷計算

		if (_targetNpc.hasSkillEffect(68)) {// 聖界減傷
			dmg /= 2.0D;
		}

		// 魔法娃娃特殊技能
		if (!_pc.getDolls().isEmpty()) {// 娃娃攻擊技能機率計算
			for (Object obj : _pc.getDolls().values().toArray()) {
				L1DollInstance doll = (L1DollInstance) obj;
				doll.startDollSkill(_targetNpc, false);
			}
		}
		// 魔法娃娃特殊技能
		if (!_pc.getDolls2().isEmpty()) {// 娃娃攻擊技能機率計算
			for (Object obj : _pc.getDolls2().values().toArray()) {
				L1DollInstance2 doll = (L1DollInstance2) obj;
				doll.startDollSkill(_targetNpc, false);
			}
		}
		// 2017/04/25
		Random random = new Random();
		if (_pc.hasSkillEffect(9971) && random.nextInt(100) + 1 <= 3) {
			dmg *= 1.2;
			_pc.sendPackets(new S_ServerMessage("發動-魔法占卜[3%發動爆擊]"));
		}
		if (_pc.getbbdmg3() && RandomArrayList.getInc(100, 1) <= 4) {
			int drainMp = (int) (_random.nextInt(20) + 1);
			this._pc.setCurrentMp((short) (_pc.getCurrentMp() + drainMp));
			_pc.sendPackets(new S_ServerMessage("發動-魔法占卜[吸魔效果]"));
		}
		if (_pc.getbbdmg2() && RandomArrayList.getInc(100, 1) <= 10) {
			int drainHp = (int) (_random.nextInt(20) + 1);
			if (_pc.getCurrentHp() < (_pc.getMaxHp() * 0.97)) {
				this._pc.setCurrentHp((short) (this._pc.getCurrentHp() + drainHp));
				_pc.sendPackets(new S_ServerMessage("發動-魔法占卜[吸血效果]"));
			}

		}

		// 判斷人物等級已大於 使用武器最大等級
//		if (_weaponType2 != 0 && _pc.getWeapon() != null) {
//			if (_pc.getLevel() > _pc.getWeapon().getItem().getMaxLevel()) {
//				dmg /= 2;
//			}
//		}
		//dmg *= coatArms();

		if (_pc.isDragonKnight()
				//&& _weaponType2 == 18
				&& _pc.getReincarnationSkill()[0] > 0
				&& _pc.isFoeSlayer()
				&& RandomArrayList.getInc(100, 1) > 100 - _pc.getReincarnationSkill()[0]) { // 龍騎天賦技能屠宰擊殺
			dmg += 200;
		}
		
		if (dmg <= 0.0D) {
			_isHit = false;
			_drainHp = 0;
		}
		//System.out.println("TEST final:"+dmg);
		return (int) dmg;
	}

	/**
	 * 弱點曝光機率計算
	 */
	private int dk_dmgUp() {
		int dmg = 0;
		if (_pc.isDragonKnight() && _weaponType2 == 18) {// 鎖煉劍
			long h_time = Calendar.getInstance().getTimeInMillis() / 1000;// 換算為秒
			int random = _random.nextInt(100);
			int weaponchance = 12;// 弱點曝光機率
			if (_weapon.getItemId() == 410189) {// 殲滅者鎖鏈劍
				weaponchance = 18;
			}

			if (_pc.get_tmp_targetid() != _targetId) {// 目標objid不相同則重置弱點曝光
				_pc.set_weaknss(0, 0L);
				_pc.sendPackets(new S_PacketBoxDk(0));
			}

			_pc.set_tmp_targetid(_targetId);// 暫存目標OBJID

			int DK2weaknss1 = 0; // SRC0808
			int DK2weaknss2 = 0;
			int DK2weaknss3 = 0;
			int DK2weaknss4 = 0;

			if (_pc.isDragonKnight() && _pc.getMeteLevel() >= 2) {
				DK2weaknss1 = ConfigSkill.DK2weaknss1;
				DK2weaknss2 = ConfigSkill.DK2weaknss2;
				DK2weaknss3 = ConfigSkill.DK2weaknss3;
				DK2weaknss4 = ConfigSkill.DK2weaknss4;
			}

			switch (_pc.get_weaknss()) {// 弱點曝光階段
			case 0:
				if (_pc.isFoeSlayer()) {// 使用屠宰者
					return 0;
				}
				if ((_random.nextInt(100)) < 20) {
					_pc.set_weaknss(1, h_time);
					_pc.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV1));
					dmg += ConfigSkill.Vulnerability1 + DK2weaknss1; // SRC0808
				}
				break;

			case 1:
				if (_pc.isFoeSlayer()) {// 使用屠宰者
					return 0;
				}
				if ((_random.nextInt(100)) < 40) {
					_pc.set_weaknss(1, h_time);
					_pc.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV1));
					dmg += ConfigSkill.Vulnerability1 + DK2weaknss1; // SRC0808
				} else {
					_pc.set_weaknss(2, h_time);
					_pc.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV2));
					dmg += ConfigSkill.Vulnerability2 + DK2weaknss2; // SRC0808
				}
				break;

			case 2:
				if (_pc.isFoeSlayer()) {// 使用屠宰者
					return 0;
				}
				if ((_random.nextInt(100)) < 60) {
					_pc.set_weaknss(2, h_time);
					_pc.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV2));
					dmg += ConfigSkill.Vulnerability2 + DK2weaknss2;
				} else {
					_pc.set_weaknss(3, h_time);
					_pc.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV3));
					dmg += ConfigSkill.Vulnerability3 + DK2weaknss3;
				}
				break;

			case 3:
				if (_pc.isFoeSlayer()) {// 使用屠宰者
					return 0;
				}
				// if ((_random.nextInt(100)) < 20) {
				// _pc.set_weaknss(3, h_time);
				// _pc.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV3));
				// dmg += ConfigSkill.Vulnerability3 + DK2weaknss3;
				// }
				// 880弱點曝光第4段攻擊
				if ((_random.nextInt(100)) < 30) {
					_pc.set_weaknss(3, h_time);
					_pc.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV3));
					dmg += ConfigSkill.Vulnerability3 + DK2weaknss3;
				} else {
					_pc.set_weaknss(4, h_time);
					_pc.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV4));
					dmg += ConfigSkill.Vulnerability4 + DK2weaknss4; // 4階段的加傷害
				}
				break;

			case 4:
				// 習得屠宰者：強化 可進行弱點曝光第4段攻擊
				// if (!_pc.isSkillMastery(FOU_SLAYER_BRAVE)) {
				if (_pc.isFoeSlayer()) {// 使用屠宰者
					return 0;
				}
				// }
				if ((_random.nextInt(100)) < 15) {
					_pc.set_weaknss(4, h_time);
					_pc.sendPackets(new S_PacketBoxDk(S_PacketBoxDk.LV4));
					dmg += ConfigSkill.Vulnerability4 + DK2weaknss4; // 4階段的加傷害
				}
				break;
			}
		}
		return dmg;
	}

	/**
	 * 料裡增傷
	 * @return
	 */
	private double dmgUp() {
		double dmg = 0.0D;

		if (_pc.getSkillEffect().size() <= 0) {
			return dmg;
		}

		if (!_pc.getSkillisEmpty()) {
			try {
				// 料理追加傷害(近距離武器)
				if ((_weaponType != 20) && (_weaponType != 62)) {
					for (final Integer key : _pc.getSkillEffect()) {
						final Integer integer = L1AttackList.SKD1.get(key);
						if (integer != null) {
							dmg += integer;
						}
					}

					// 料理追加傷害(遠距離武器)
				} else {
					for (final Integer key : _pc.getSkillEffect()) {
						final Integer integer = L1AttackList.SKD2.get(key);
						if (integer != null) {
							dmg += integer;
						}
					}
				}

			} catch (ConcurrentModificationException localConcurrentModificationException) {
			} catch (Exception e) {
				_log.error(e.getLocalizedMessage(), e);
			}
		}

		return dmg;
	}

	/**
	 * 魔法武器傷害計算
	 * 
	 * @param pcInstance
	 * @param character
	 * @param weaponTotalDamage
	 * @return
	 */
	private double weaponSkill(L1PcInstance pcInstance, L1Character character, double weaponTotalDamage) {
		double dmg = 0.0D;
		dmg = WeaponSkillStart.start_weapon_skill(pcInstance, character, _weapon, weaponTotalDamage);
		// System.out.println("dmg:"+dmg);
		if (dmg != 0.0D) {
			return dmg;
		}

		switch (_weaponId) {
		case 124: // 巴風特魔杖
			dmg = L1WeaponSkill.getBaphometStaffDamage(_pc, _target);
			break;
		case 86://紅影雙刀
		case 204://深紅之弩
		case 100204://深紅之弩
			L1WeaponSkill.giveFettersEffect(_pc, _target);
			break;
		case 261://大法師魔仗
			L1WeaponSkill.giveArkMageDiseaseEffect(_pc, _target);
			break;
		case 410131://天使魔杖
			L1WeaponSkill.giveTurn_Undead(_pc, _target);
			break;
		case 260://狂風之斧
		case 263://酷寒之矛
			dmg = L1WeaponSkill.getAreaSkillWeaponDamage(_pc, _target, _weaponId);
			break;
		case 264://雷雨之劍
			dmg = L1WeaponSkill.getLightningEdgeDamage(_pc, _target);
			break;
		default:
			dmg = L1WeaponSkill.getWeaponSkillDamage(_pc, _target, _weaponId);
			break;
		}

		return dmg;
	}

	/**
	 * 近戰武器輔助魔法增傷
	 * 
	 * @param dmg
	 * @return
	 */
	private double calcBuffDamage(double dmg) {
		if (_weaponType == 20) {// 弓
			return dmg;
		}
		if (_weaponType == 62) {// 鐵手甲
			return dmg;
		}
		if (_weaponType == 58) {// 奇古獸
			return dmg;
		}
		final int random = _random.nextInt(100) + 1;
		
		// if (_pc.hasSkillEffect(FIRE_WEAPON)) {
		// dmg += 4.0D;
		// }

		/*
		 * if (_pc.hasSkillEffect(155)) { dmg += 5.0D; }
		 */

		if (_pc.hasSkillEffect(BURNING_WEAPON)) {
			dmg += 6.0D;
		}

		if (_pc.hasSkillEffect(BERSERKERS)) {
			dmg += 5.0D;
		} // TODO 黑妖 雙重破壞傷害倍率
		if (_pc.hasSkillEffect(DOUBLE_BREAK)) { // SRC0808
			if (random <= 33) {
				if (_pc.getMeteLevel() < 4) {
					dmg *= ConfigSkill.Double_Brake_Dmg;// 2010-11-26(0.45)
				} else {
					dmg *= ConfigSkill.Double_Brake_Dmg + ConfigSkill.D4;// 2010-11-26(0.45)
				}
			}
		}

		// 燃燒鬥志
		if (_pc.hasSkillEffect(BURNING_SPIRIT)) {
			if (random <= ConfigSkill.BURNINGSPIRITchance) {
				dmg *= ConfigSkill.BURNINGSPIRITdmg / 10;// 2010-11-26(0.45)
			}
		}
		if (_pc.hasSkillEffect(BURNING_SLASH)) { // 燃燒擊砍
			dmg += 10.0D;
			_pc.sendPacketsX10(new S_EffectLocation(_targetX, _targetY, 6591));
			_pc.killSkillEffectTimer(BURNING_SLASH);
		}
		return dmg;
	}

	/**
	 * 祝福武器 銀/米索莉/奧裡哈魯根材質武器<BR>
	 * 其他屬性定義
	 * 
	 * @return
	 */
	private int calcMaterialBlessDmg() {
		int damage = 0;
		if (_pc.getWeapon() != null) {
			final int undead = _targetNpc.getNpcTemplate().get_undead();
			switch (undead) {
			case 1:// 不死系
			case 3:// 殭屍系
			case 4:// 不死系(治療系無傷害/無法使用起死回生)
				if ((_weaponMaterial == 14) || (_weaponMaterial == 17) || (_weaponMaterial == 22)) {// 銀/米索莉/奧裡哈魯根
					damage += _random.nextInt(20) + 1;
				}
				if (_weaponBless == 0) { // 祝福武器
					damage += _random.nextInt(4) + 1;
				}
				switch (_weaponType) {
				case 20:
				case 62:
					break;
				default:
					if (_weapon.getHolyDmgByMagic() != 0) {
						damage += _weapon.getHolyDmgByMagic();// 武器強化魔法
					}
					break;
				}
				break;
			case 2:// 惡魔系
				if ((_weaponMaterial == 17) || (_weaponMaterial == 22)) {// 米索莉/奧裡哈魯根
					damage += _random.nextInt(3) + 1;
				}
				if (_weaponBless == 0) { // 祝福武器
					damage += _random.nextInt(4) + 1;
				}
				break;
			case 5:// 狼人系
				if ((_weaponMaterial == 14) || (_weaponMaterial == 17) || (_weaponMaterial == 22)) {// 銀/米索莉/奧裡哈魯根
					damage += _random.nextInt(20) + 1;
				}
				break;
			}
		}
		return damage;
	}

	/**
	 * 奪魂T恤吸血
	 */
	private void soulHp() {

		switch (_calcType) {
		case PC_PC:
			if (_pc.isSoulHp() > 0) {
				ArrayList<Integer> soulHp = new ArrayList<Integer>();
				soulHp = _pc.get_soulHp();
				int r = soulHp.get(0);
				int min = soulHp.get(1);
				int max = soulHp.get(2);
				if (_random.nextInt(100) < r) {
					if (_pc.isSoulHp() == 1) {
						_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 11673));
					} else {
						// _targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 11677));
						if (ConfigAlt.Soul_Hp_Gfx != 0) {
							_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), ConfigAlt.Soul_Hp_Gfx));
						}
					}

					int hpadd = _random.nextInt((max - min)) + min;
					int newHp = (int) (_pc.getCurrentHp() + hpadd);
					if (newHp >= _pc.getMaxHp()) {
						_pc.setCurrentHp(_pc.getMaxHp());
					} else {
						_pc.setCurrentHp(newHp);
					}

					_targetPc.receiveDamage(_pc, hpadd, false, true);
				}

			}
			break;
		case PC_NPC:
			if (_pc.isSoulHp() > 0 && _targetNpc instanceof L1MonsterInstance) {
				ArrayList<Integer> soulHp = new ArrayList<Integer>();
				soulHp = _pc.get_soulHp();
				int r = soulHp.get(0);
				int min = soulHp.get(1);
				int max = soulHp.get(2);

				if (_random.nextInt(100) < r) {
					if (_pc.isSoulHp() == 1) {

						_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 11673));
					} else {
						// _targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 11677));
						if (ConfigAlt.Soul_Hp_Gfx != 0) {
							_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), ConfigAlt.Soul_Hp_Gfx));
						}
					}
					int hpadd = _random.nextInt((max - min)) + min;
					int newHp = (int) (_pc.getCurrentHp() + hpadd);
					if (newHp >= _pc.getMaxHp()) {
						_pc.setCurrentHp(_pc.getMaxHp());
					} else {
						_pc.setCurrentHp(newHp);
					}
					_targetNpc.receiveDamage(_pc, hpadd);

				}
			}
			break;
		}

	}

	/**
	 * 觸發火焰之暈
	 */
	private void AttrAmuletEffect() {
		int dmg = 0;
		switch (_calcType) {
		case PC_PC:
			if (_pc.hasSkillEffect(FIRESTUN)) {
				if (_random.nextInt(100) < 5) {// 5%機率傷害+50
					dmg = 50;
					_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 13542));
					_targetPc.receiveDamage(_pc, dmg, false, true);
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetPc, 0, (int)dmg));
				}
			} else if (_pc.hasSkillEffect(TRUEFIRESTUN)) {
				if (_random.nextInt(100) < 5) {// 7%機率傷害+80
					dmg = 80;
					_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 13542));
					_targetPc.receiveDamage(_pc, dmg, false, true);
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetPc, 0, (int)dmg));
				}
			} else if (_pc.hasSkillEffect(MOONATTACK)) {
				if (_random.nextInt(100) < 5) {// 7%機率傷害+120
					dmg = 120;
					_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 13989));
					_targetPc.receiveDamage(_pc, dmg, false, true);
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetPc, 0, (int)dmg));
					
				}
			}
			break;
		case PC_NPC:
			if (_pc.hasSkillEffect(FIRESTUN)) {
				if (_random.nextInt(100) < 5) {// 5%機率傷害+50
					dmg = 50;
					_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 13542));
					_targetNpc.receiveDamage(_pc, dmg);
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetNpc, 0, (int)dmg));
				}
			} else if (_pc.hasSkillEffect(TRUEFIRESTUN)) {
				if (_random.nextInt(100) < 5) {// 7%機率傷害+80
					dmg = 80;
					_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 13542));
					_targetNpc.receiveDamage(_pc, dmg);
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetNpc, 0, (int)dmg));
					
				}
			} else if (_pc.hasSkillEffect(MOONATTACK)) {
				if (_random.nextInt(100) < 5) {// 7%機率傷害+120
					dmg = 120;
					_targetNpc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 13989));
					_targetNpc.receiveDamage(_pc, dmg);
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _targetNpc, 0, (int)dmg));
				}
			}
			break;
		}
	}

	/** 屬性項鏈增傷計算 */
	private int calcAmuletAttrDmg() {
		int damage = 0;
		L1Inventory pc_inv = _pc.getInventory();
		int[] amulets = new int[] { 402000, 402001, 402002, 402003, 402004, 402005, 402006, 402007 };
		for (int i = 0; i < amulets.length; i++) {
			L1ItemInstance amulet = pc_inv.findItemId(amulets[i]);
			if (amulet != null && amulet.isEquipped()) {
				damage += 5;
				break;
			}
		}

		int resist = 0;
		switch (_calcType) {
		case PC_PC:
			switch (amulets.length) {
			case 1:// 地靈守護之煉
				resist = _targetPc.getEarth();
				break;
			case 2:// 水靈守護之煉
				resist = _targetPc.getWater();
				break;
			case 3:// 火靈守護之煉
				resist = _targetPc.getFire();
				break;
			case 4:// 風靈守護之煉
				resist = _targetPc.getWind();
				break;
			case 5:// 真 地靈守護之煉
				resist = _targetPc.getEarth();
				break;
			case 6:// 真 水靈守護之煉
				resist = _targetPc.getWater();
				break;
			case 7:// 真 火靈守護之煉
				resist = _targetPc.getFire();
				break;
			case 8:// 真 風靈守護之煉
				resist = _targetPc.getWind();
				break;
			}
			break;

		case PC_NPC:
			switch (amulets.length) {
			case 1:// 地靈守護之煉
				resist = _targetNpc.getEarth();
				break;
			case 2:// 水靈守護之煉
				resist = _targetNpc.getWater();
				break;
			case 3:// 火靈守護之煉
				resist = _targetNpc.getFire();
				break;
			case 4:// 風靈守護之煉
				resist = _targetNpc.getWind();
				break;
			case 5:// 真 地靈守護之煉
				resist = _targetNpc.getEarth();
				break;
			case 6:// 真 水靈守護之煉
				resist = _targetNpc.getWater();
				break;
			case 7:// 真 火靈守護之煉
				resist = _targetNpc.getFire();
				break;
			case 8:// 真 風靈守護之煉
				resist = _targetNpc.getWind();
				break;
			}
			break;
		}

		int resistFloor = (int) (0.16D * Math.abs(resist));

		if (resist < 0) {
			resistFloor *= -1;
		}

		double attrDeffence = resistFloor / 32.0D;
		double attrCoefficient = 1.0D - attrDeffence;

		damage = (int) (damage * attrCoefficient);

		return damage;
	}

	/** 屬性武器增傷計算 */
	private int calcAttrEnchantDmg() {
		int damage = 0;
		switch (_weaponAttrEnchantLevel) {
		case 1:
			damage = 1;
			break;
		case 2:
			damage = 3;
			break;
		case 3:
			damage = 5;
			break;
		case 4:
			damage = 7;
			break;
		case 5:
			damage = 9;
			break;
		}

		if (_weaponType == 20) {// 武器為弓
			if (_arrow != null) {
				// if (_arrow.getItemId() >= 84077 && _arrow.getItemId() <= 84080) {// 屬性黑色米索莉箭
				// damage += 3;
				// }
				// 箭的武器屬性傷害
				if (_arrow.getItem().getArrowAttrDmg() > 0) {
					damage += _arrow.getItem().getArrowAttrDmg();
				}
			}
		}

		int resist = 0;
		switch (_calcType) {
		case PC_PC:
			switch (_weaponAttrEnchantKind) {
			case 1:// 地屬性
				resist = _targetPc.getEarth();
				break;
			case 2:// 火屬性
				resist = _targetPc.getFire();
				break;
			case 4:// 水屬性
				resist = _targetPc.getWater();
				break;
			case 8:// 風屬性
				resist = _targetPc.getWind();
				break;
			}
			break;

		case PC_NPC:
			switch (_weaponAttrEnchantKind) {
			case 1:// 地屬性
				resist = _targetNpc.getEarth();
				break;
			case 2:// 火屬性
				resist = _targetNpc.getFire();
				break;
			case 4:// 水屬性
				resist = _targetNpc.getWater();
				break;
			case 8:// 風屬性
				resist = _targetNpc.getWind();
				break;
			}
			break;
		}

		int resistFloor = (int) (0.16D * Math.abs(resist));

		if (resist < 0) {
			resistFloor *= -1;
		}

		double attrDeffence = resistFloor / 32.0D;

		double attrCoefficient = 1.0D - attrDeffence;

		damage = (int) (damage * attrCoefficient);
		// System.out.println("attrCoefficient ==" + attrCoefficient);

		return damage;
	}

	/**
	 * 附加劇毒機率計算
	 * 
	 * @param target
	 */
	private void addPcPoisonAttack(L1Character target) {
		if ((_weaponId != 0) && (_pc.hasSkillEffect(ENCHANT_VENOM))) {
			int chance = _random.nextInt(100) + 1;
			// 黑妖天賦技能附加劇毒
			int ReiHp = 5;
			if (_pc.getReincarnationSkill()[1] > 0) {
				ReiHp += _pc.getReincarnationSkill()[1] * 15;
			}
			if (chance <= 10) {
				//L1DamagePoison.doInfection(_pc, target, 3000, 5);
				L1DamagePoison.doInfection(_pc, target, 3000, ReiHp);
			}
		}
	}

	/**
	 * 屬火、燃斗、勇猛意志增傷計算
	 * 
	 * @param dmg
	 */
	private double BuffDmgUp(double dmg) {
		int random = _random.nextInt(100) + 1;
		int D2BURNING_SPIRIT = 0;
		if (_pc.isDarkelf() && _pc.getMeteLevel() >= 2) { // SRC0808
			D2BURNING_SPIRIT = ConfigSkill.D2;
		}
		if ((_pc.hasSkillEffect(ELEMENTAL_FIRE) && (_weaponType != 20) && (_weaponType != 62)) && // 屬性之火
				(random <= 33)) {
			if (_pc.getMeteLevel() < 4) { // SRC0808
				dmg *= 1.5D;
			} else {
				dmg *= ConfigSkill.E2ELEMENTAL_FIRE;
			}
			// 新增特效
			if (_calcType == PC_PC) {
				_pc.sendPackets(new S_SkillSound(_targetPc.getId(),7727));
				_pc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 7727));
			} else if (_calcType == PC_NPC) {
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 7727));
				_pc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 7727));
			}

		} else if ((_pc.hasSkillEffect(QUAKE) && (_weaponType != 20) && (_weaponType != 62)) && // 大地纏繞
				(random <= 33)) {
			dmg *= 1.5D;
			// 新增特效
			if (_calcType == PC_PC) {
				_pc.sendPackets(new S_SkillSound(_targetPc.getId(),7727));
				_pc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 7727));
			} else if (_calcType == PC_NPC) {
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 7727));
				_pc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 7727));
			}

		} else if ((_pc.hasSkillEffect(BLOW_ATTACK) && (_weaponType != 20) && (_weaponType != 62)) && // 榮耀盾
				(random <= 33)) {
			dmg *= 1.5D;
			// 新增特效
			if (_calcType == PC_PC) {
				_pc.sendPackets(new S_SkillSound(_targetPc.getId(),7727));
				_pc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 7727));
			} else if (_calcType == PC_NPC) {
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 7727));
				_pc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 7727));
			}

		} else if ((_pc.hasSkillEffect(BURNING_SPIRIT) && (_weaponType != 20) && (_weaponType != 62)) && // 燃燒鬥志
				(random <= 33 + D2BURNING_SPIRIT)) { // SRC0808
			dmg *= 1.5D;
			// 新增特效
			if (_calcType == PC_PC) {
				_pc.sendPackets(new S_SkillSound(_targetPc.getId(),13323));
				_pc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 13323));
			} else if (_calcType == PC_NPC) {
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 13323));
				_pc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 13323));
			}

		} else if ((_pc.hasSkillEffect(BRAVE_AURA) && (_weaponType != 20) && (_weaponType != 62)) && // 勇猛意志
				(random <= 33)) {
			if (_pc.getMeteLevel() < 4) { // SRC0808
				dmg *= 1.5D;
			} else {
				dmg *= ConfigSkill.C4;
			}
			// 新增特效
			if (_calcType == PC_PC) {
				_pc.sendPackets(new S_SkillSound(_targetPc.getId(),7727));
				_pc.broadcastPacketAll(new S_SkillSound(_targetPc.getId(), 7727));
			} else if (_calcType == PC_NPC) {
				_pc.sendPackets(new S_SkillSound(_targetNpc.getId(), 7727));
				_pc.broadcastPacketAll(new S_SkillSound(_targetNpc.getId(), 7727));
			}

		} else if ((_target.hasSkillEffect(STRIKER_GALE2) && ((_weaponType == 20) || (_weaponType == 62)))) { // SRC0808
			dmg *= ConfigSkill.E4STRIKER_GALE;
		}

		return dmg;
	}

	/**
	 * 攻擊動作送出
	 */
	@Override
	public void action() {
		try {
			if (_pc == null) {
				return;
			}
			if (_target == null) {
				return;
			}
			// 改變面向
			_pc.setHeading(_pc.targetDirection(_targetX, _targetY));

			if (_weaponRange == -1) {// 遠距離武器
				actionX1();

			} else {// 近距離武器
				actionX2();
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 近距離武器/空手
	 */
	private void actionX2() {
		try {
			
			if (_isHit) {// 命中
				int attackgfx = 0;
				switch (_weaponType2) {
				case 1:// 劍
					attackgfx = 13411;
					break;
				case 2:// 匕首
					attackgfx = 13412;
					break;
				case 3:// 雙手劍
					attackgfx = 13410;
					break;
				case 14:// 矛(單手)
				case 5:// 矛(雙手)
					attackgfx = 13402;
					break;
				case 6:// 斧(單手)
				case 15:// 雙手斧
					attackgfx = 13415;
					break;
				case 7:// 魔杖
				case 16:// 魔杖(雙手)
					attackgfx = 13414;
					break;
				case 18:// 鎖鏈劍
					attackgfx = 13413;
					break;
				case 11:// 鋼爪
					attackgfx = 13416;
					break;
				case 12:// 雙刀
					attackgfx = 13417;
					break;
				case 17:// 奇古獸
					attackgfx = 13396;
					break;
				}

				if (_pc.getTempCharGfx() >= 13715 && _pc.getTempCharGfx() <= 13745) {// TOP10變身
					if (attackgfx > 0 && attackgfx != 13396) {// 空手以及奇古獸以外的攻擊特效
						if (_pc.getTempCharGfx() != 13731 && _pc.getTempCharGfx() != 13733) { // 不是真黑妖外型
							if (_random.nextInt(100) < 20) {// 20%機率出現特效
								L1Location loc = _target.getLocation();
								L1NpcInstance dummy = L1SpawnUtil.spawnS(loc, 86132, _pc.get_showId(), 1,
										_pc.getHeading());
								dummy.broadcastPacketAll(new S_NPCPack(dummy));
								dummy.broadcastPacketAll(new S_SkillSound(dummy.getId(), attackgfx));
							}
						} else if ((_attackType == 2) || (_attackType == 4)) {// 真黑妖重擊或雙擊
							L1Location loc = _target.getLocation();
							L1NpcInstance dummy = L1SpawnUtil.spawnS(loc, 86132, _pc.get_showId(), 1, _pc.getHeading());
							dummy.broadcastPacketAll(new S_NPCPack(dummy));
							dummy.broadcastPacketAll(new S_SkillSound(dummy.getId(), attackgfx));
						}
					}
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _target, 0, _damage));

				} else {// 其它外形
					_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _target, _attackType, _damage));
				}

			} else if (_targetId > 0) {// 未命中但是具有目標
				_pc.sendPacketsAll(new S_AttackPacketPc(_pc, _target));

			} else {// 空攻擊
				_pc.sendPacketsAll(new S_AttackPacketPc(_pc));
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 遠距離武器
	 */
	private void actionX1() {
		try {
			switch (_pc.getTempCharGfx()) {
			case 8842:// 海露拜
			case 8900:// 海露拜
				_arrowGfxid = 8904;
				_stingGfxid = 8904;
				break;
			case 8845:// 絲莉安
			case 8913:// 絲莉安
				_arrowGfxid = 8916;
				_stingGfxid = 8916;
				break;
			case 7959:// 天上騎士
			case 7967:// 天上騎士
			case 7968:// 天上騎士
			case 7969:// 天上騎士
			case 7970:// 天上騎士
				_arrowGfxid = 7972;
				_stingGfxid = 7972;
				break;
			case 13631:// 歐薇82
				_arrowGfxid = 13657;
				_stingGfxid = 13657;
				break;
			case 13635:// 歐薇85  //src013
				_arrowGfxid = 13659;
				_stingGfxid = 13659;
				break;
			case 13723:// 新妖精變身(男)
				_arrowGfxid = 13658;
				_stingGfxid = 13658;
				break;
			case 13725:// 新妖精變身(女)
				_arrowGfxid = 13656;
				_stingGfxid = 13656;
				break;
			default:
				break;
			}

			/*if (_pc.getTempCharGfx() >= 13715 && _pc.getTempCharGfx() <= 13745) {// TOP10變身
				_arrowGfxid = 11762;
				_stingGfxid = 11762;
			}*/

			if (_isHit) {// 命中
				int attackgfx = 0;
				switch (_weaponType) {
				case 20:// 弓
					if (_arrow != null) {// 具有箭
						if (_pc.getTempCharGfx() >= 13715 && _pc.getTempCharGfx() <= 13745) {// TOP10變身
							attackgfx = 13392;
							if (_random.nextInt(100) < 20) {// 20%機率出現特效
								L1Location loc = _target.getLocation();
								L1NpcInstance dummy = L1SpawnUtil.spawnS(loc, 86132, _pc.get_showId(), 1,
										_pc.getHeading());
								dummy.broadcastPacketAll(new S_NPCPack(dummy));
								dummy.broadcastPacketAll(new S_SkillSound(dummy.getId(), attackgfx));
							}
						}

						_pc.sendPacketsAll(
								new S_UseArrowSkill(_pc, _targetId, _arrowGfxid, _targetX, _targetY, _damage, 1));

						_pc.getInventory().removeItem(_arrow, 1L);
					} else {// 沒有箭
						if (_weaponId == 190 || _weaponId ==410220) {// 沙哈之弓

							_pc.sendPacketsAll(
									new S_UseArrowSkill(_pc, _targetId, 2349, _targetX, _targetY, _damage, 1));
						}
					}
					break;
				case 62:// 鐵手甲
					if (_sting != null) {// 具有飛刀
						if (_pc.getTempCharGfx() >= 13715 && _pc.getTempCharGfx() <= 13745) {// TOP10變身
							attackgfx = 13409;
							if (_random.nextInt(100) < 20) {// 20%機率出現特效
								L1Location loc = _target.getLocation();
								L1NpcInstance dummy = L1SpawnUtil.spawnS(loc, 86132, _pc.get_showId(), 1,
										_pc.getHeading());
								dummy.broadcastPacketAll(new S_NPCPack(dummy));
								dummy.broadcastPacketAll(new S_SkillSound(dummy.getId(), attackgfx));
							}
						}

						_pc.sendPacketsAll(
								new S_UseArrowSkill(_pc, _targetId, _stingGfxid, _targetX, _targetY, _damage, 1));

						_pc.getInventory().removeItem(_sting, 1L);
					}
					break;
				default:
					break;
				}
			} else if (_targetId > 0) {// 未命中但是具有目標
				switch (_weaponType) {
				case 20:// 弓
					if (_arrow != null) {// 具有箭
						_pc.sendPacketsAll(new S_UseArrowSkill(_pc, _arrowGfxid, _targetX, _targetY, 1));
						_pc.getInventory().removeItem(_arrow, 1L);
					} else {// 沒有箭
						if (_weaponId == 190 || _weaponId ==410220) {// 沙哈之弓

							_pc.sendPacketsAll(
									new S_UseArrowSkill(_pc, _targetId, 2349, _targetX, _targetY, _damage, 1));
						} else {// 其他遠距離武器
							_pc.sendPacketsAll(new S_UseArrowSkill(_pc));
						}
					}
					break;
				case 62:// 鐵手甲
					if (_sting != null) {// 具有飛刀
						_pc.sendPacketsAll(new S_UseArrowSkill(_pc, _stingGfxid, _targetX, _targetY, 1));
						_pc.getInventory().removeItem(_sting, 1L);
					} else {// 沒有飛刀
						_pc.sendPacketsAll(new S_UseArrowSkill(_pc));
					}
					break;
				default:
					break;
				}
			} else {// 空攻擊
				_pc.sendPacketsAll(new S_UseArrowSkill(_pc));
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 傷害資訊送出
	 */
	public void commit() {
		if (_isHit) {// 命中
			if ((_pc.dice_hp() != 0) && (_random.nextInt(100) + 1 <= _pc.dice_hp())) {// 附魔系統
																						// 機率吸取體力
				_drainHp = _pc.sucking_hp();
			}

			if ((_pc.dice_mp() != 0) && (_random.nextInt(100) + 1 <= _pc.dice_mp())) {// 附魔系統
																						// 機率吸取魔力
				_drainMana = _pc.sucking_mp();
			}

			if (_pc.has_powerid(6610)) {// 附魔系統 三段加速5秒
				int rad = 3;// 機率
				int time = 5;// 秒
				if ((_random.nextInt(100) < rad) && (!_pc.hasSkillEffect(STATUS_BRAVE3))) {
					_pc.setSkillEffect(STATUS_BRAVE3, time * 1000);// 三段加速五秒
					_pc.sendPacketsAll(new S_Liquor(_pc.getId(), 8));
					_pc.sendPacketsAll(new S_SkillSound(_pc.getId(), 11817));// 蛋糕煙火特效
					_pc.sendPackets(new S_PacketBoxThirdSpeed(time));
				}
			}

			switch (_calcType) {
			case PC_PC:
				if (_pc.lift() != 0) {// 附魔系統 機率卸除對方裝備
					int counter = _random.nextInt(_pc.lift()) + 1;
					StringBuffer sbr = new StringBuffer();
					Iterator<L1ItemInstance> iterator2 = _targetPc.getInventory().getItems().iterator();
					while (iterator2.hasNext()) {
						L1ItemInstance item = (L1ItemInstance) iterator2.next();
						if (item.getItem().getType2() != 2 || !item.isEquipped())
							continue;
						_targetPc.getInventory().setEquipped(item, false, false, false);
						sbr.append("[").append(item.getNumberedName(1L, false)).append("]");
						if (--counter <= 0)
							break;
					}
					if (sbr.length() > 0) {
						_targetPc.sendPackets(new S_SystemMessage("以下裝備被對方卸除:" + sbr.toString()));
						_pc.sendPackets(new S_SystemMessage("成功卸除對方以下裝備:" + sbr.toString()));
					}
				}

				commitPc();
				break;
			case PC_NPC:
				commitNpc();
				
				break;
			}
		} else {// 未命中
			if (_calcType == PC_PC) {
				_targetPc.sendPacketsAll(new S_SkillSound(_targetPc.getId(), 13418));// MISS特效編號
			}
		}

		if (!ConfigAlt.ALT_ATKMSG) {// 傷害資訊顯示關閉
			return;
		} else {// 傷害資訊顯示開啟
			switch (_calcType) {
			case PC_PC:
				if ((!_pc.isGm()) && (!_targetPc.isGm())) {
					return;
				}
				break;
			case PC_NPC:
				if (!_pc.isGm()) {
					return;
				}
				break;
			}

			String srcatk = _pc.getName();
			String tgatk = "";
			String hitinfo = "";
			String dmginfo = "";
			String x = "";

			switch (_calcType) {
			case PC_PC:
				tgatk = _targetPc.getName();
				hitinfo = "命中機率:" + hit_rnd + "% 剩餘hp:" + _targetPc.getCurrentHp();
				dmginfo = _isHit ? "傷害:" + _damage + " " : "未命中 ";
				x = srcatk + ">" + tgatk + " " + dmginfo + hitinfo;
				if (_pc.isGm()) {
					_pc.sendPackets(new S_ServerMessage(166, "對PC送出攻擊: " + x));
				}
				
				if (_targetPc.isGm()) {
					_targetPc.sendPackets(new S_ServerMessage(166, "受到PC攻擊: " + x));
				}
				break;
			case PC_NPC:
				tgatk = _targetNpc.getName();
				hitinfo = "命中機率:" + hit_rnd + "% 剩餘hp:" + _targetNpc.getCurrentHp();
				dmginfo = _isHit ? "傷害:" + _damage + " " : "未命中 ";
				x = srcatk + ">" + tgatk + " " + dmginfo + hitinfo;
				if (_pc.isGm()) {
					_pc.sendPackets(new S_ServerMessage(166, "對NPC送出攻擊: " + x));
				}
				
				break;
			}
		}
	}

	/**
	 * 傷害資訊送出
	 */
	private void commitPc() {
		if ((_drainMana > 0) && (_targetPc.getCurrentMp() > 0)) {
			if (_drainMana > _targetPc.getCurrentMp()) {
				_drainMana = _targetPc.getCurrentMp();
			}
			_targetPc.receiveManaDamage(_pc, _drainMana);

			int newMp = _pc.getCurrentMp() + _drainMana;
			_pc.setCurrentMp(newMp);
		}

		if (_drainHp > 0) {
			short newHp = (short) (_pc.getCurrentHp() + _drainHp);
			_pc.setCurrentHp(newHp);
		}

		// damagePcWeaponDurability();
		_targetPc.receiveDamage(_pc, _damage, false, false);
	}

	/**
	 * 傷害資訊送出
	 */
	private void commitNpc() {
		if (_drainMana > 0) {
			int drainValue = _targetNpc.drainMana(_drainMana);
			if (drainValue > 0) {
				_targetNpc.ReceiveManaDamage(_pc, drainValue);

				int newMp = _pc.getCurrentMp() + drainValue;
				_pc.setCurrentMp(newMp);
			}
		}

		if (_drainHp > 0) {
			short newHp = (short) (_pc.getCurrentHp() + _drainHp);
			_pc.setCurrentHp(newHp);
		}

		damageNpcWeaponDurability();
		_targetNpc.receiveDamage(_pc, _damage);
	}

	public boolean isShortDistance() {
		boolean isShortDistance = true;
		if ((_weaponType == 20) || (_weaponType == 62)) {
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
		// 2016/3/14 修正反彈問題
		if (_pc.getId() == _target.getId()) {
			return;
		}
		if (_pc.hasSkillEffect(68)) {// 聖界減傷
			damage /= 2;
		}

		_pc.sendPacketsAll(new S_DoActionGFX(_pc.getId(), 2));
		_pc.sendPacketsAll(new S_SkillSound(_target.getId(), 10710));
		_pc.receiveDamage(_target, damage, false, true);
	}

	/**
	 * 攻擊NPC時武器受損
	 */
	private void damageNpcWeaponDurability() {
		if (_calcType != PC_NPC) {
			return;
		}

		if (!_targetNpc.getNpcTemplate().is_hard()) {
			return;
		}

		if (_weaponType == 0) {
			return;
		}

		if (_weapon.getItem().get_canbedmg() == 0) {
			return;
		}

		if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {//烈焰之魂不壞刀
			return;
		}

		int random = _random.nextInt(100) + 1;
		switch (_weaponBless) {
		case 0:
			if (random < 3) {
				_pc.sendPackets(new S_ServerMessage(268, _weapon.getLogName()));
				_pc.getInventory().receiveDamage(_weapon);
				_pc.sendPacketsX8(new S_SkillSound(_pc.getId(), 10712));
				_pc.sendPackets(new S_PacketBoxDurability(_weapon));
			}
			break;
		case 1:
		case 2:
			if (random < 10) {
				_pc.sendPackets(new S_ServerMessage(268, _weapon.getLogName()));
				_pc.getInventory().receiveDamage(_weapon);
				_pc.sendPacketsX8(new S_SkillSound(_pc.getId(), 10712));
				_pc.sendPackets(new S_PacketBoxDurability(_weapon));
			}
			break;
		}
	}

	/**
	 * 攻擊PC時武器受損
	 */
	@SuppressWarnings("unused")
	private void damagePcWeaponDurability() {
		if (_calcType != PC_PC) {
			return;
		}

		if (_weaponType == 0) {
			return;
		}

		if (_weaponType == 20) {
			return;
		}

		if (_weaponType == 62) {
			return;
		}

		if (!_targetPc.hasSkillEffect(BOUNCE_ATTACK)) {
			return;
		}

		if (_pc.hasSkillEffect(SOUL_OF_FLAME)) {
			return;
		}

		if (_random.nextInt(100) + 1 <= 10) {
			_pc.sendPackets(new S_ServerMessage(268, _weapon.getLogName()));
			_pc.getInventory().receiveDamage(_weapon);
			_pc.sendPacketsX8(new S_SkillSound(_pc.getId(), 10712));
			_pc.sendPackets(new S_PacketBoxDurability(_weapon));
		}
	}

	private final int getCriticalHitDamage(final L1CriticalHitStone stone, final int damage) {

		final int gfxId = stone.getGfxId();
		if (gfxId != 0) {

			if (stone.isGfxIdTarget() && stone.isArrowType()) {
				final S_UseAttackSkill packet = new S_UseAttackSkill(_pc, _target.getId(), gfxId, _target.getX(),
						_target.getY(), ActionCodes.ACTION_Attack, false);

				_pc.sendPacketsX10(packet);

			} else if (stone.isGfxIdTarget()) {
				_pc.sendPacketsX10(new S_SkillSound(_target.getId(), gfxId));

			} else {
				_pc.sendPacketsX10(new S_SkillSound(_pc.getId(), gfxId));
			}
		}

		return (int) (damage * stone.getCriticalHitDamage() / 100f);
	}

    /**
     * 龍騎士新技能 撕裂護甲
     */
	public void ArmorDestory() {
		for (L1ItemInstance armorItem : _targetPc.getInventory().getItems()) {
			if (armorItem.getItem().getType2() == 2 && armorItem.getItem().getType() == 2) {
				int armorId = armorItem.getItemId();
				L1ItemInstance item = _targetPc.getInventory().findEquippedItemId(armorId);
				if (item != null) {
					int chance = _random.nextInt(100) + 1;
					if (item.get_durability() == (armorItem.getItem().get_ac() * -1)) {
						break;
					} else {
						if (chance <= 15) {
							item.set_durability(item.get_durability() + 1);
							_targetPc.getInventory().updateItem(item, L1PcInventory.COL_DURABILITY);
							_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 14549));
							_targetPc.addAc(1);
							_targetPc.sendPackets(new S_OwnCharAttrDef(_targetPc));
							_targetPc.sendPackets(new S_ServerMessage(268, armorItem.getLogName()));
							Broadcaster.broadcastPacket(_targetPc, new S_SkillSound(_targetPc.getId(), 14549));
						}
					}
				}
			}
		}
	}

}
