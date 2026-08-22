/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package william;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_SPMR;

/**
 * 飾品加成能力系統
 * @author
 */
public class L1WilliamEnchantAccessory {

	/**
	 * 增加飾品加成能力
	 * @param pc
	 * @param item
	 */
	public static void getAddArmorOrginal(final L1PcInstance pc, final L1ItemInstance item) {
		L1WilliamEnchantAccessory armorOrginal = null;
		L1WilliamEnchantAccessory armorOrginalOk = null;
		final L1WilliamEnchantAccessory[] armorOrginalSize = EnchantAccessory.getInstance().getArmorList();
		for (int i = 0; i < armorOrginalSize.length; i++) {
			armorOrginalOk = EnchantAccessory.getInstance().getTemplate(i);
			if (item.getItem().getType2() == 2) {
				if (armorOrginalOk.getType() == item.getItem().getType()) {
					if (armorOrginalOk.getType() == item.getItem().getType()
							&& armorOrginalOk.getStrength() == item.getItem().get_greater()
							&& armorOrginalOk.getLevel() == item.getEnchantLevel()) {
						armorOrginal = armorOrginalOk;
						break;
					}
				}
			}
		}
		if (armorOrginal == null) {
			return;
		}

		boolean spmr = false;
		if (armorOrginal.getAddStr() != 0) {
			pc.addStr(armorOrginal.getAddStr());
		}
		if (armorOrginal.getAddDex() != 0) {
			pc.addDex(armorOrginal.getAddDex());
		}
		if (armorOrginal.getAddCon() != 0) {
			pc.addCon(armorOrginal.getAddCon());
		}
		if (armorOrginal.getAddInt() != 0) {
			pc.addInt(armorOrginal.getAddInt());
		}
		if (armorOrginal.getAddWis() != 0) {
			pc.addWis(armorOrginal.getAddWis());
		}
		if (armorOrginal.getAddCha() != 0) {
			pc.addCha(armorOrginal.getAddCha());
		}
		if (armorOrginal.getAddAc() != 0) {
			pc.addAc(-armorOrginal.getAddAc());
		}
		if (armorOrginal.getAddMaxHp() != 0) {
			pc.addMaxHp(armorOrginal.getAddMaxHp());
		}
		if (armorOrginal.getAddMaxMp() != 0) {
			pc.addMaxMp(armorOrginal.getAddMaxMp());
		}
		if (armorOrginal.getAddHpr() != 0) {
			pc.addHpr(armorOrginal.getAddHpr());
		}
		if (armorOrginal.getAddMpr() != 0) {
			pc.addMpr(armorOrginal.getAddMpr());
		}
		if (armorOrginal.getAddDmg() != 0) {
			pc.addDmgup(armorOrginal.getAddDmg());
		}
		if (armorOrginal.getAddHit() != 0) {
			pc.addHitup(armorOrginal.getAddHit());
		}
		if (armorOrginal.getAddBowDmg() != 0) {
			pc.addBowDmgup(armorOrginal.getAddBowDmg());
		}
		if (armorOrginal.getAddBowHit() != 0) {
			pc.addBowHitup(armorOrginal.getAddBowHit());
		}
		if (armorOrginal.getAddDmgReduction() != 0) {
			pc.addDamageReductionByArmor(armorOrginal.getAddDmgReduction());
		}
		if (armorOrginal.getAddMr() != 0) {
			pc.addMr(armorOrginal.getAddMr());
			spmr = true;
		}
		if (armorOrginal.getAddSp() != 0) {
			pc.addSp(armorOrginal.getAddSp());
			spmr = true;
		}
		if (armorOrginal.getPVPdmg() != 0) {
			pc.addPvpDmg(armorOrginal.getPVPdmg());
		}
		if (armorOrginal.getPVPdmgReduction() != 0) {
			pc.addPvpDmg_R(armorOrginal.getPVPdmgReduction());
		}
		if (armorOrginal.getPotion_Heal() != 0) {
			pc.add_up_hp_potion(armorOrginal.getPotion_Heal());
		}
		if (armorOrginal.getPotion_Healling() != 0) {
			pc.add_uhp_number(armorOrginal.getPotion_Healling());
		}
		if (armorOrginal.getAddMagicHit() != 0) {
			pc.addMagicHit(armorOrginal.getAddMagicHit());
		}
		// if (armorOrginal.getAddRegistFear() != 0) {
		// pc.addRegistFear(armorOrginal.getAddRegistFear());
		// }
		// 技術耐性
		if (armorOrginal.getAddRegistTechnology() != 0) {
			pc.addRegistTechnology(armorOrginal.getAddRegistTechnology());
		}

		// 精靈耐性
		if (armorOrginal.getAddRegistElf() != 0) {
			pc.addRegistElf(armorOrginal.getAddRegistElf());
		}

		// 龍屬耐性
		if (armorOrginal.getAddRegistDragon() != 0) {
			pc.addRegistDragon(armorOrginal.getAddRegistDragon());
		}

		// 恐怖耐性
		if (armorOrginal.getAddRegistHorror() != 0) {
			pc.addRegistHorror(armorOrginal.getAddRegistHorror());
		}

		// 全部四大耐性
		if (armorOrginal.getAddRegistAll() != 0) {
			pc.addRegistAll(armorOrginal.getAddRegistAll());
		}

		//////////////////////////////////////////////////////

		// 技術命中
		if (armorOrginal.getAddHitTechnology() != 0) {
			pc.setHitTechnology(armorOrginal.getAddHitTechnology());
		}

		// 精靈命中
		if (armorOrginal.getAddHitElf() != 0) {
			pc.setHitElf(armorOrginal.getAddHitElf());
		}

		// 龍屬命中
		if (armorOrginal.getAddHitDragon() != 0) {
			pc.setHitDragon(armorOrginal.getAddHitDragon());
		}

		// 恐怖命中
		if (armorOrginal.getAddHitHorror() != 0) {
			pc.setHitHorror(armorOrginal.getAddHitHorror());
		}

		// 全部四大命中
		if (armorOrginal.getAddHitAll() != 0) {
			pc.setHitAll(armorOrginal.getAddHitAll());
		}

		// 殷海薩祝福消耗減少(1=1%)
		if (armorOrginal.getAddEinhasadConsumeReduce() != 0) {
			pc.addEinhasadConsumeReduce(armorOrginal.getAddEinhasadConsumeReduce());
		}

		if (spmr) {
			pc.sendPackets(new S_SPMR(pc));
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	/**
	 * 移除飾品加成能力
	 * @param pc
	 * @param item
	 */
	public static void getReductionArmorOrginal(final L1PcInstance pc, final L1ItemInstance item) {
		L1WilliamEnchantAccessory armorOrginal = null;
		L1WilliamEnchantAccessory armorOrginalOk = null;
		final L1WilliamEnchantAccessory[] armorOrginalSize = EnchantAccessory.getInstance().getArmorList();
		for (int i = 0; i < armorOrginalSize.length; i++) {
			armorOrginalOk = EnchantAccessory.getInstance().getTemplate(i);
			if (item.getItem().getType2() == 2) {
				if (armorOrginalOk.getType() == item.getItem().getType()) {
					if (armorOrginalOk.getType() == item.getItem().getType()
							&& armorOrginalOk.getStrength() == item.getItem().get_greater()
							&& armorOrginalOk.getLevel() == item.getEnchantLevel()) {
						armorOrginal = armorOrginalOk;
						break;
					}
				}
			}
		}
		if (armorOrginal == null) {
			return;
		}

		boolean spmr = false;
		if (armorOrginal.getAddStr() != 0) {
			pc.addStr(-armorOrginal.getAddStr());
		}
		if (armorOrginal.getAddDex() != 0) {
			pc.addDex(-armorOrginal.getAddDex());
		}
		if (armorOrginal.getAddCon() != 0) {
			pc.addCon(-armorOrginal.getAddCon());
		}
		if (armorOrginal.getAddInt() != 0) {
			pc.addInt(-armorOrginal.getAddInt());
		}
		if (armorOrginal.getAddWis() != 0) {
			pc.addWis(-armorOrginal.getAddWis());
		}
		if (armorOrginal.getAddCha() != 0) {
			pc.addCha(-armorOrginal.getAddCha());
		}
		if (armorOrginal.getAddAc() != 0) {
			pc.addAc(armorOrginal.getAddAc());
		}
		if (armorOrginal.getAddMaxHp() != 0) {
			pc.addMaxHp(-armorOrginal.getAddMaxHp());
		}
		if (armorOrginal.getAddMaxMp() != 0) {
			pc.addMaxMp(-armorOrginal.getAddMaxMp());
		}
		if (armorOrginal.getAddHpr() != 0) {
			pc.addHpr(-armorOrginal.getAddHpr());
		}
		if (armorOrginal.getAddMpr() != 0) {
			pc.addMpr(-armorOrginal.getAddMpr());
		}
		if (armorOrginal.getAddDmg() != 0) {
			pc.addDmgup(-armorOrginal.getAddDmg());
		}
		if (armorOrginal.getAddHit() != 0) {
			pc.addHitup(-armorOrginal.getAddHit());
		}
		if (armorOrginal.getAddBowDmg() != 0) {
			pc.addBowDmgup(-armorOrginal.getAddBowDmg());
		}
		if (armorOrginal.getAddBowHit() != 0) {
			pc.addBowHitup(-armorOrginal.getAddBowHit());
		}
		if (armorOrginal.getAddDmgReduction() != 0) {
			pc.addDamageReductionByArmor(-armorOrginal.getAddDmgReduction());
		}
		if (armorOrginal.getAddMr() != 0) {
			pc.addMr(-armorOrginal.getAddMr());
			spmr = true;
		}
		if (armorOrginal.getAddSp() != 0) {
			pc.addSp(-armorOrginal.getAddSp());
			spmr = true;
		}
		if (armorOrginal.getPVPdmg() != 0) {
			pc.addPvpDmg(-armorOrginal.getPVPdmg());
		}
		if (armorOrginal.getPVPdmgReduction() != 0) {
			pc.addPvpDmg_R(-armorOrginal.getPVPdmgReduction());
		}
		if (armorOrginal.getPotion_Heal() != 0) {
			pc.add_up_hp_potion(-armorOrginal.getPotion_Heal());
		}
		if (armorOrginal.getPotion_Healling() != 0) {
			pc.add_uhp_number(-armorOrginal.getPotion_Healling());
		}
		if (armorOrginal.getAddMagicHit() != 0) {
			pc.addMagicHit(-armorOrginal.getAddMagicHit());
		}
		// if (armorOrginal.getAddRegistFear() != 0) {
		// pc.addRegistFear(-armorOrginal.getAddRegistFear());
		// }
		// 技術耐性
		if (armorOrginal.getAddRegistTechnology() != 0) {
			pc.addRegistTechnology(-armorOrginal.getAddRegistTechnology());
		}

		// 精靈耐性
		if (armorOrginal.getAddRegistElf() != 0) {
			pc.addRegistElf(-armorOrginal.getAddRegistElf());
		}

		// 龍屬耐性
		if (armorOrginal.getAddRegistDragon() != 0) {
			pc.addRegistDragon(-armorOrginal.getAddRegistDragon());
		}

		// 恐怖耐性
		if (armorOrginal.getAddRegistHorror() != 0) {
			pc.addRegistHorror(-armorOrginal.getAddRegistHorror());
		}

		// 全部四大耐性
		if (armorOrginal.getAddRegistAll() != 0) {
			pc.addRegistAll(-armorOrginal.getAddRegistAll());
		}

		//////////////////////////////////////////////////////

		// 技術命中
		if (armorOrginal.getAddHitTechnology() != 0) {
			pc.setHitTechnology(-armorOrginal.getAddHitTechnology());
		}

		// 精靈命中
		if (armorOrginal.getAddHitElf() != 0) {
			pc.setHitElf(-armorOrginal.getAddHitElf());
		}

		// 龍屬命中
		if (armorOrginal.getAddHitDragon() != 0) {
			pc.setHitDragon(-armorOrginal.getAddHitDragon());
		}

		// 恐怖命中
		if (armorOrginal.getAddHitHorror() != 0) {
			pc.setHitHorror(-armorOrginal.getAddHitHorror());
		}

		// 全部四大命中
		if (armorOrginal.getAddHitAll() != 0) {
			pc.setHitAll(-armorOrginal.getAddHitAll());
		}

		// 殷海薩祝福消耗減少(1=1%)
		if (armorOrginal.getAddEinhasadConsumeReduce() != 0) {
			pc.addEinhasadConsumeReduce(-armorOrginal.getAddEinhasadConsumeReduce());
		}

		if (spmr) {
			pc.sendPackets(new S_SPMR(pc));
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	private final int _id;
	private final int _strength;
	private final int _type;
	private final int _level;
	private final byte _addStr;
	private final byte _addDex;
	private final byte _addCon;
	private final byte _addInt;
	private final byte _addWis;
	private final byte _addCha;
	private final int _addAc;
	private final int _addMaxHp;
	private final int _addMaxMp;
	private final int _addHpr;
	private final int _addMpr;
	private final int _addDmg;
	private final int _addBowDmg;
	private final int _addHit;
	private final int _addBowHit;
	private final int _addDmgReduction;
	private final int _addMr;
	private final int _addSp;
	private final int _PVPdmg;
	private final int _PVPdmgReduction;
	private final int _potion_heal;
	private final int _potion_healling;
	private final int _magic_hit;
	// private final int _regist_fear;
	private int _addRegistTechnology; // 技術耐性
	private int _addRegistElf; // 精靈耐性
	private int _addRegistDragon; // 龍屬耐性
	private int _addRegistHorror; // 恐怖耐性
	private int _addRegistAll; // 全部四大耐性

	private int _addHitTechnology; // 技術命中
	private int _addHitElf; // 精靈命中
	private int _addHitDragon; // 龍屬命中
	private int _addHitHorror; // 恐怖命中
	private int _addHitAll; // 全部四大命中

	private int _addEinhasadConsumeReduce; // 殷海薩祝福消耗減少(1=1%)

	public L1WilliamEnchantAccessory(final int id, final int type, final int strength, final int level,
			final byte addStr, final byte addDex, final byte addCon, final byte addInt, final byte addWis,
			final byte addCha, final int addAc, final int addMaxHp, final int addMaxMp, final int addHpr,
			final int addMpr, final int addDmg, final int addBowDmg, final int addHit, final int addBowHit,
			final int addDmgReduction, final int addMr, final int addSp, final int PVPdmg, final int PVPdmgReduction,
			final int potion_heal, final int potion_healling, final int magic_hit
			// , final int regist_fear
			, final int addRegistTechnology
			, final int addRegistElf
			, final int addRegistDragon
			, final int addRegistHorror
			, final int addRegistAll
			, final int addHitTechnology
			, final int addHitElf
			, final int addHitDragon
			, final int addHitHorror
			, final int addHitAll
			, final int addEinhasadConsumeReduce
	) {
		_id = id;
		_type = type;
		_strength = strength;
		_level = level;
		_addStr = addStr;
		_addDex = addDex;
		_addCon = addCon;
		_addInt = addInt;
		_addWis = addWis;
		_addCha = addCha;
		_addAc = addAc;
		_addMaxHp = addMaxHp;
		_addMaxMp = addMaxMp;
		_addHpr = addHpr;
		_addMpr = addMpr;
		_addDmg = addDmg;
		_addBowDmg = addBowDmg;
		_addHit = addHit;
		_addBowHit = addBowHit;
		_addDmgReduction = addDmgReduction;
		_addMr = addMr;
		_addSp = addSp;
		_PVPdmg = PVPdmg;
		_PVPdmgReduction = PVPdmgReduction;
		_potion_heal = potion_heal;
		_potion_healling = potion_healling;
		_magic_hit = magic_hit;
		// _regist_fear = regist_fear;
		_addRegistTechnology = addRegistTechnology;
		_addRegistElf = addRegistElf;
		_addRegistDragon = addRegistDragon;
		_addRegistHorror = addRegistHorror;
		_addRegistAll = addRegistAll;
		_addHitTechnology = addHitTechnology;
		_addHitElf = addHitElf;
		_addHitDragon = addHitDragon;
		_addHitHorror = addHitHorror;
		_addHitAll = addHitAll;
		_addEinhasadConsumeReduce = addEinhasadConsumeReduce;
	}

	public int getId() {
		return _id;
	}

	public int getType() {
		return _type;
	}

	public int getStrength() {
		return _strength;
	}

	public int getLevel() {
		return _level;
	}

	public byte getAddStr() {
		return _addStr;
	}

	public byte getAddDex() {
		return _addDex;
	}

	public byte getAddCon() {
		return _addCon;
	}

	public byte getAddInt() {
		return _addInt;
	}

	public byte getAddWis() {
		return _addWis;
	}

	public byte getAddCha() {
		return _addCha;
	}

	public int getAddAc() {
		return _addAc;
	}

	public int getAddMaxHp() {
		return _addMaxHp;
	}

	public int getAddMaxMp() {
		return _addMaxMp;
	}

	public int getAddHpr() {
		return _addHpr;
	}

	public int getAddMpr() {
		return _addMpr;
	}

	public int getAddDmg() {
		return _addDmg;
	}

	public int getAddBowDmg() {
		return _addBowDmg;
	}

	public int getAddHit() {
		return _addHit;
	}

	public int getAddBowHit() {
		return _addBowHit;
	}

	public int getAddDmgReduction() {
		return _addDmgReduction;
	}

	public int getAddMr() {
		return _addMr;
	}

	public int getAddSp() {
		return _addSp;
	}

	public int getPVPdmg() {
		return _PVPdmg;
	}

	public int getPVPdmgReduction() {
		return _PVPdmgReduction;
	}

	public int getPotion_Heal() {
		return _potion_heal;
	}

	public int getPotion_Healling() {
		return _potion_healling;
	}

	public int getAddMagicHit() {
		return _magic_hit;
	}
	
	// public int getAddRegistFear() {
	// return _regist_fear;
	// }

	/** 技術耐性 */
	public int getAddRegistTechnology() {
		return _addRegistTechnology;
	}

	/** 精靈耐性 */
	public int getAddRegistElf() {
		return _addRegistElf;
	}

	/** 龍屬耐性 */
	public int getAddRegistDragon() {
		return _addRegistDragon;
	}

	/** 恐怖耐性 */
	public int getAddRegistHorror() {
		return _addRegistHorror;
	}

	/** 全部四大耐性 */
	public int getAddRegistAll() {
		return _addRegistAll;
	}

	/** 技術命中 */
	public int getAddHitTechnology() {
		return _addHitTechnology;
	}

	/** 精靈命中 */
	public int getAddHitElf() {
		return _addHitElf;
	}

	/** 龍屬命中 */
	public int getAddHitDragon() {
		return _addHitDragon;
	}

	/** 恐怖命中 */
	public int getAddHitHorror() {
		return _addHitHorror;
	}

	/** 全部四大命中 */
	public int getAddHitAll() {
		return _addHitAll;
	}

	/** 殷海薩祝福消耗減少(1=1%) */
	public int getAddEinhasadConsumeReduce() {
		return _addEinhasadConsumeReduce;
	}
}
