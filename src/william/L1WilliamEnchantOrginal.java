package william;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_SPMR;

/**
 * 裝備強化能力系統
 */
public class L1WilliamEnchantOrginal {
	private final int _id;
	private final int _itemid;
	private final int _level;
	private int _addAc;
	private byte _addStr;
	private byte _addDex;
	private byte _addCon;
	private byte _addInt;
	private byte _addWis;
	private byte _addCha;
	private int _addMaxHp;
	private int _addMaxMp;
	private int _addHpr;
	private int _addMpr;
	private int _addDmg;
	private int _addBowDmg;
	private int _addHit;
	private int _addBowHit;
	private int _reduction_dmg;
	private int _addMr;
	private int _addSp;
	private int _addFire;
	private int _addWind;
	private int _addEarth;
	private int _addWater;
	private int _addPvpDmg;
	private int _addPvpDmg_R;
	private int _addCloseCri;
	private int _addBowCri;
	private int _addMagicCri;
	// private int _addFearLevel;
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
	private int _magic_hit_modifier; // 恐怖命中
	private int _jilvjianmian; // 全部四大命中
	private int _addEinhasadConsumeReduce; // 殷海薩祝福消耗減少(1=1%)

	public L1WilliamEnchantOrginal(final int id, final int itemid, final int level, final int addAc, final byte addStr,
			final byte addDex, final byte addCon, final byte addInt, final byte addWis, final byte addCha,
			final int addMaxHp, final int addMaxMp, final int addHpr, final int addMpr, final int addDmg,
			final int addBowDmg, final int addHit, final int addBowHit, final int reduction_dmg, final int addMr,
			final int addSp, final int addFire, final int addWind, final int addEarth, final int addWater,
			final int addPvpDmg, final int addPvpDmg_R, final int addCloseCri, final int addBowCri,
			final int addMagicCri
			// , final int addFearLevel
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
			,final int magic_hit_modifier
			//,final int jilvjianmian
			, final int addEinhasadConsumeReduce
	) {

		_id = id;
		_itemid = itemid;
		_level = level;
		_addAc = addAc;
		_addStr = addStr;
		_addDex = addDex;
		_addCon = addCon;
		_addInt = addInt;
		_addWis = addWis;
		_addCha = addCha;
		_addMaxHp = addMaxHp;
		_addMaxMp = addMaxMp;
		_addHpr = addHpr;
		_addMpr = addMpr;
		_addDmg = addDmg;
		_addBowDmg = addBowDmg;
		_addHit = addHit;
		_addBowHit = addBowHit;
		_reduction_dmg = reduction_dmg;
		_addMr = addMr;
		_addSp = addSp;
		_addFire = addFire;
		_addWind = addWind;
		_addEarth = addEarth;
		_addWater = addWater;
		_addPvpDmg = addPvpDmg;
		_addPvpDmg_R = addPvpDmg_R;
		_addCloseCri = addCloseCri;
		_addBowCri = addBowCri;
		_addMagicCri = addMagicCri;
		// _addFearLevel = addFearLevel;
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
		_magic_hit_modifier = magic_hit_modifier;
		//_jilvjianmian = jilvjianmian;
		_addEinhasadConsumeReduce = addEinhasadConsumeReduce;
	}

	public int getId() {
		return _id;
	}

	public int getItemId() {
		return _itemid;
	}

	public int getLevel() {
		return _level;
	}

	/** 額外防禦 */
	public int getAddAc() {
		return _addAc;
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

	/** 所有傷害減免 */
	public int getReduction_dmg() {
		return _reduction_dmg;
	}

	public int getAddMr() {
		return _addMr;
	}

	public int getAddSp() {
		return _addSp;
	}

	public int getAddFire() {
		return _addFire;
	}

	public int getAddWind() {
		return _addWind;
	}

	public int getAddEarth() {
		return _addEarth;
	}

	public int getAddWater() {
		return _addWater;
	}

	/** 增加PVP傷害 */
	public int getAddPvpDmg() {
		return _addPvpDmg;
	}

	/** 減免PVP傷害 */
	public int getAddPvpDmg_R() {
		return _addPvpDmg_R;
	}

	/** 近距離爆擊率 */
	public int getAddCloseCri() {
		return _addCloseCri;
	}

	/** 遠距離爆擊率 */
	public int getAddBowCri() {
		return _addBowCri;
	}

	/** 魔法爆擊率 */
	public int getAddMagicCri() {
		return _addMagicCri;
	}

	// /** 恐怖等級 */
	// public int getAddFearLevel() {
	// return _addFearLevel;
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

	/** 魔法减免 */
	public int getmagic_hit_modifier() {
		return _magic_hit_modifier;
	}

	/** 全部四大命中 */
	public int getAddHitAll() {
		return _addHitAll;
	}

	/** 全部四大命中 */
	public int getjilvjianmian() {
		return _jilvjianmian;
	}

	/** 殷海薩祝福消耗減少(1=1%) */
	public int getAddEinhasadConsumeReduce() {
		return _addEinhasadConsumeReduce;
	}

	/** 增加效果 */
	public static void getAddArmorOrginal(final L1PcInstance pc, final L1ItemInstance item) {
		L1WilliamEnchantOrginal armorOrginal = null;
		L1WilliamEnchantOrginal armorOrginalOk = null;
		final L1WilliamEnchantOrginal[] armorOrginalSize = EnchantOrginal.getInstance().getArmorList();
		for (int i = 0; i < armorOrginalSize.length; i++) {
			armorOrginalOk = EnchantOrginal.getInstance().getTemplate(i);
			if (armorOrginalOk.getItemId() == item.getItemId() // 道具編號相同
					&& item.getEnchantLevel() >= armorOrginalOk.getLevel()) { // 道具加成等級相同
				armorOrginal = armorOrginalOk;
				//break;
			}
		}
		if (armorOrginal == null) {
			return;
		}

		boolean spmr = false;
		if (armorOrginal.getAddAc() != 0) { // 額外防禦
			pc.addAc(-armorOrginal.getAddAc());
		}
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
		if (armorOrginal.getReduction_dmg() != 0) { // 所有傷害減免
			pc.addDamageReductionByArmor(armorOrginal.getReduction_dmg());
		}
		if (armorOrginal.getAddMr() != 0) {
			pc.addMr(armorOrginal.getAddMr());
			spmr = true;
		}
		if (armorOrginal.getAddSp() != 0) {
			pc.addSp(armorOrginal.getAddSp());
			spmr = true;
		}
		if (armorOrginal.getAddFire() != 0) {
			pc.addFire(armorOrginal.getAddFire());
		}
		if (armorOrginal.getAddWind() != 0) {
			pc.addWind(armorOrginal.getAddWind());
		}
		if (armorOrginal.getAddEarth() != 0) {
			pc.addEarth(armorOrginal.getAddEarth());
		}
		if (armorOrginal.getAddWater() != 0) {
			pc.addWater(armorOrginal.getAddWater());
		}
		if (armorOrginal.getAddPvpDmg() != 0) { // 增加PVP傷害
			pc.addPvpDmg(armorOrginal.getAddPvpDmg());
		}
		if (armorOrginal.getAddPvpDmg_R() != 0) { // 減免PVP傷害
			pc.addPvpDmg_R(armorOrginal.getAddPvpDmg_R());
		}
		if (armorOrginal.getAddCloseCri() != 0) { // 近距離爆擊率
			pc.addCloseCritical(armorOrginal.getAddCloseCri());
		}
		if (armorOrginal.getAddBowCri() != 0) { // 遠距離爆擊率
			pc.addBowCritical(armorOrginal.getAddBowCri());
		}
		if (armorOrginal.getAddMagicCri() != 0) { // 魔法爆擊率
			pc.addOriginalMagicCritical(armorOrginal.getAddMagicCri());
		}

		// if (armorOrginal.getAddFearLevel() != 0) { // 恐怖等級
		// pc.setHitHorror(armorOrginal.getAddFearLevel());
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
		// 全部四大命中
		if (armorOrginal.getmagic_hit_modifier() != 0) {
			pc.addMagicHit(armorOrginal.getmagic_hit_modifier());
		}
		// 全部四大命中
		/*if (armorOrginal.getjilvjianmian() != 0) {
			pc.setHitAll(armorOrginal.getjilvjianmian());
		}*/

		// 殷海薩祝福消耗減少(1=1%)
		if (armorOrginal.getAddEinhasadConsumeReduce() != 0) {
			pc.addEinhasadConsumeReduce(armorOrginal.getAddEinhasadConsumeReduce());
		}

		if (spmr) {
			pc.sendPackets(new S_SPMR(pc));
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
		if (armorOrginal.getAddStr() != 0
				|| armorOrginal.getAddDex() != 0
				|| armorOrginal.getAddCon() != 0
				|| armorOrginal.getAddWis() != 0
				|| armorOrginal.getAddInt() != 0) {
			pc.sendDetails();
		}
	}

	/** 解除效果 */
	public static void getReductionArmorOrginal(final L1PcInstance pc, final L1ItemInstance item) {
		L1WilliamEnchantOrginal armorOrginal = null;
		L1WilliamEnchantOrginal armorOrginalOk = null;
		final L1WilliamEnchantOrginal[] armorOrginalSize = EnchantOrginal.getInstance().getArmorList();
		for (int i = 0; i < armorOrginalSize.length; i++) {
			armorOrginalOk = EnchantOrginal.getInstance().getTemplate(i);
			if (armorOrginalOk.getItemId() == item.getItemId() // 道具編號相同
					&& item.getEnchantLevel() >= armorOrginalOk.getLevel()) { // 道具加成等級相同
				armorOrginal = armorOrginalOk;
				//break;
			}
		}
		if (armorOrginal == null) {
			return;
		}

		boolean spmr = false;
		if (armorOrginal.getAddAc() != 0) { // 額外防禦
			pc.addAc(armorOrginal.getAddAc());
		}
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
		if (armorOrginal.getReduction_dmg() != 0) { // 所有傷害減免
			pc.addDamageReductionByArmor(-armorOrginal.getReduction_dmg());
		}
		if (armorOrginal.getAddMr() != 0) {
			pc.addMr(-armorOrginal.getAddMr());
			spmr = true;
		}
		if (armorOrginal.getAddSp() != 0) {
			pc.addSp(-armorOrginal.getAddSp());
			spmr = true;
		}
		if (armorOrginal.getAddFire() != 0) {
			pc.addFire(-armorOrginal.getAddFire());
		}
		if (armorOrginal.getAddWind() != 0) {
			pc.addWind(-armorOrginal.getAddWind());
		}
		if (armorOrginal.getAddEarth() != 0) {
			pc.addEarth(-armorOrginal.getAddEarth());
		}
		if (armorOrginal.getAddWater() != 0) {
			pc.addWater(-armorOrginal.getAddWater());
		}
		if (armorOrginal.getAddPvpDmg() != 0) { // 增加PVP傷害
			pc.addPvpDmg(-armorOrginal.getAddPvpDmg());
		}
		if (armorOrginal.getAddPvpDmg_R() != 0) { // 減免PVP傷害
			pc.addPvpDmg_R(-armorOrginal.getAddPvpDmg_R());
		}
		if (armorOrginal.getAddCloseCri() != 0) { // 近距離爆擊率
			pc.addCloseCritical(-armorOrginal.getAddCloseCri());
		}
		if (armorOrginal.getAddBowCri() != 0) { // 遠距離爆擊率
			pc.addBowCritical(-armorOrginal.getAddBowCri());
		}
		if (armorOrginal.getAddMagicCri() != 0) { // 魔法爆擊率
			pc.addOriginalMagicCritical(-armorOrginal.getAddMagicCri());
		}

		// if (armorOrginal.getAddFearLevel() != 0) { // 恐怖等級
		// pc.setHitHorror(-armorOrginal.getAddFearLevel());
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
		// 全部四大命中
		if (armorOrginal.getmagic_hit_modifier() != 0) {
			pc.addMagicHit(-armorOrginal.getmagic_hit_modifier());
		}
		// 殷海薩祝福消耗減少(1=1%)
		if (armorOrginal.getAddEinhasadConsumeReduce() != 0) {
			pc.addEinhasadConsumeReduce(-armorOrginal.getAddEinhasadConsumeReduce());
		}

		if (spmr) {
			pc.sendPackets(new S_SPMR(pc));
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
		if (armorOrginal.getAddStr() != 0
				|| armorOrginal.getAddDex() != 0
				|| armorOrginal.getAddCon() != 0
				|| armorOrginal.getAddWis() != 0
				|| armorOrginal.getAddInt() != 0) {
			pc.sendDetails();
		}
	}
}
