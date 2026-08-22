package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.CONFUSION;
import static com.lineage.server.model.skill.L1SkillId.COOKING_4_0_N;
import static com.lineage.server.model.skill.L1SkillId.COOKING_4_1_N;
import static com.lineage.server.model.skill.L1SkillId.COOKING_4_2_N;
import static com.lineage.server.model.skill.L1SkillId.COOKING_4_3_N;
import static com.lineage.server.model.skill.L1SkillId.CURSE_BLIND;
import static com.lineage.server.model.skill.L1SkillId.CURSE_PARALYZE;
import static com.lineage.server.model.skill.L1SkillId.EARTH_BLESS;
import static com.lineage.server.model.skill.L1SkillId.DARKNESS;
import static com.lineage.server.model.skill.L1SkillId.DARK_BLIND;
import static com.lineage.server.model.skill.L1SkillId.DISEASE;
import static com.lineage.server.model.skill.L1SkillId.DRAGON_SKIN;
import static com.lineage.server.model.skill.L1SkillId.EARTH_BIND;
import static com.lineage.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static com.lineage.server.model.skill.L1SkillId.GUARD_BRAKE;
import static com.lineage.server.model.skill.L1SkillId.HORROR_OF_DEATH;
import static com.lineage.server.model.skill.L1SkillId.ICE_LANCE;
import static com.lineage.server.model.skill.L1SkillId.IMMUNE_TO_HARM;
import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.PANIC;
import static com.lineage.server.model.skill.L1SkillId.PATIENCE;
import static com.lineage.server.model.skill.L1SkillId.PHANTASM;
import static com.lineage.server.model.skill.L1SkillId.RESIST_FEAR;
import static com.lineage.server.model.skill.L1SkillId.SILENCE;
import static com.lineage.server.model.skill.L1SkillId.DEATH_HEAL;
import static com.lineage.server.model.skill.L1SkillId.WEAPON_BREAK;

import java.util.HashMap;

import com.lineage.server.datatables.lock.SpawnBossReading;

public class L1AttackList {
	/**
	 * 料理命中降低或追加(_weaponType != 20) && (_weaponType != 62)近距離武器
	 */
	protected static final HashMap<Integer, Integer> SKU1 = new HashMap<Integer, Integer>();

	/**
	 * 料理命中降低或追加(_weaponType == 20) && (_weaponType == 62)遠距離武器
	 */
	protected static final HashMap<Integer, Integer> SKU2 = new HashMap<Integer, Integer>();

	/**
	 * NPC需附加技能才可攻擊
	 */
	protected static final HashMap<Integer, Integer> SKNPC = new HashMap<Integer, Integer>();

	/**
	 * NPC指定外型不可攻擊
	 */
	protected static final HashMap<Integer, Integer> PLNPC = new HashMap<Integer, Integer>();

	/**
	 * 料理追加傷害(_weaponType != 20) && (_weaponType != 62)近距離武器
	 */
	protected static final HashMap<Integer, Integer> SKD1 = new HashMap<Integer, Integer>();

	/**
	 * 料理追加傷害(_weaponType == 20) && (_weaponType == 62)遠距離武器
	 */
	protected static final HashMap<Integer, Integer> SKD2 = new HashMap<Integer, Integer>();

	/**
	 * 傷害降低
	 */
	public static final HashMap<Integer, Integer> SKD3 = new HashMap<Integer, Integer>();

	/**
	 * 受到下列法術效果 傷害為0
	 */
	public static final HashMap<Integer, Integer> SKM0 = new HashMap<Integer, Integer>();

	/**
	 * 力量增加命中
	 */
	protected static final HashMap<Integer, Integer> STRH = new HashMap<Integer, Integer>();

	/**
	 * 敏捷增加命中
	 */
	protected static final HashMap<Integer, Integer> DEXH = new HashMap<Integer, Integer>();

	/**
	 * 力量增加傷害
	 */
	protected static final HashMap<Integer, Integer> STRD = new HashMap<Integer, Integer>();

	/**
	 * 敏捷增加傷害
	 */
	protected static final HashMap<Integer, Integer> DEXD = new HashMap<Integer, Integer>();

	/**
	 * NPC抵抗技能(NPCID / 技能編號) 列表中該技能對該NPC施展失敗
	 */
	protected static final HashMap<Integer, Integer[]> DNNPC = new HashMap<Integer, Integer[]>();

	/**
	 * 安全區域不可使用的技能
	 */
	protected static final HashMap<Integer, Boolean> NZONE = new HashMap<Integer, Boolean>();

	public static void load() { // BOSS抵抗技能
		for (Integer bossid : SpawnBossReading.get().bossIds()) {
			Integer[] ids = { new Integer(FOG_OF_SLEEPING), new Integer(ICE_LANCE), new Integer(CURSE_PARALYZE),
					new Integer(EARTH_BIND), new Integer(DARK_BLIND), new Integer(DARKNESS), new Integer(CURSE_BLIND),
					new Integer(SILENCE), new Integer(DISEASE), new Integer(WEAPON_BREAK), new Integer(GUARD_BRAKE),
					new Integer(RESIST_FEAR), new Integer(HORROR_OF_DEATH), new Integer(CONFUSION),
					new Integer(PHANTASM), new Integer(PANIC) };

			if (DNNPC.get(bossid) == null) {
				DNNPC.put(bossid, ids);
			}

		}

		// 安全區域不可使用的技能
		NZONE.put(new Integer(27), Boolean.valueOf(false));
		NZONE.put(new Integer(29), Boolean.valueOf(false));
		NZONE.put(new Integer(33), Boolean.valueOf(false));
		NZONE.put(new Integer(39), Boolean.valueOf(false));
		NZONE.put(new Integer(40), Boolean.valueOf(false));
		NZONE.put(new Integer(47), Boolean.valueOf(false));
		NZONE.put(new Integer(56), Boolean.valueOf(false));
		NZONE.put(new Integer(44), Boolean.valueOf(false));
		NZONE.put(new Integer(71), Boolean.valueOf(false));
		NZONE.put(new Integer(DEATH_HEAL), Boolean.valueOf(false)); // 法師新技能 治癒逆行
		// NZONE.put(new Integer(MASS_SLOW), Boolean.valueOf(false)); // 集體緩速術 改->冰霜彗星
		// NZONE.put(new Integer(ENTANGLE), Boolean.valueOf(false)); // 地面障礙 改->大地纏繞
		NZONE.put(new Integer(153), Boolean.valueOf(false));
		NZONE.put(new Integer(157), Boolean.valueOf(false));
		NZONE.put(new Integer(161), Boolean.valueOf(false));
		NZONE.put(new Integer(167), Boolean.valueOf(false));
		NZONE.put(new Integer(174), Boolean.valueOf(false));
		NZONE.put(new Integer(87), Boolean.valueOf(false));
		NZONE.put(new Integer(66), Boolean.valueOf(false));
		NZONE.put(new Integer(103), Boolean.valueOf(false));
		NZONE.put(new Integer(212), Boolean.valueOf(false));
		NZONE.put(new Integer(50), Boolean.valueOf(false));
		NZONE.put(new Integer(80), Boolean.valueOf(false));
		NZONE.put(new Integer(194), Boolean.valueOf(false));
		NZONE.put(new Integer(173), Boolean.valueOf(false));
		NZONE.put(new Integer(133), Boolean.valueOf(false));
		NZONE.put(new Integer(145), Boolean.valueOf(false));
		NZONE.put(new Integer(64), Boolean.valueOf(false));
		NZONE.put(new Integer(193), Boolean.valueOf(false));
		NZONE.put(new Integer(188), Boolean.valueOf(false));
		NZONE.put(new Integer(183), Boolean.valueOf(false));

		// 料理追加傷害(_weaponType != 20) && (_weaponType != 62)近距離武器
		SKD1.put(new Integer(3016), new Integer(1));
		SKD1.put(new Integer(3024), new Integer(1));
		SKD1.put(new Integer(3016), new Integer(1));
		SKD1.put(new Integer(3016), new Integer(1));
		SKD1.put(new Integer(COOKING_4_0_N), new Integer(2));

		// 料理追加傷害(_weaponType == 20) && (_weaponType == 62)遠距離武器
		SKD2.put(new Integer(3019), new Integer(1));
		SKD2.put(new Integer(3027), new Integer(1));
		SKD2.put(new Integer(3032), new Integer(1));
		SKD2.put(new Integer(3040), new Integer(1));
		SKD2.put(new Integer(COOKING_4_1_N), new Integer(2));

		// 傷害降低
		SKD3.put(new Integer(3008), new Integer(-5));
		SKD3.put(new Integer(3009), new Integer(-5));
		SKD3.put(new Integer(3010), new Integer(-5));
		SKD3.put(new Integer(3011), new Integer(-5));
		SKD3.put(new Integer(3012), new Integer(-5));
		SKD3.put(new Integer(3013), new Integer(-5));
		SKD3.put(new Integer(3014), new Integer(-5));
		SKD3.put(new Integer(3024), new Integer(-5));
		SKD3.put(new Integer(3025), new Integer(-5));
		SKD3.put(new Integer(3026), new Integer(-5));
		SKD3.put(new Integer(3027), new Integer(-5));
		SKD3.put(new Integer(3028), new Integer(-5));
		SKD3.put(new Integer(3029), new Integer(-5));
		SKD3.put(new Integer(3030), new Integer(-5));
		SKD3.put(new Integer(3040), new Integer(-5));
		SKD3.put(new Integer(3041), new Integer(-5));
		SKD3.put(new Integer(3042), new Integer(-5));
		SKD3.put(new Integer(3043), new Integer(-5));
		SKD3.put(new Integer(3044), new Integer(-5));
		SKD3.put(new Integer(3045), new Integer(-5));
		SKD3.put(new Integer(3046), new Integer(-5));
		SKD3.put(new Integer(3015), new Integer(-5));
		SKD3.put(new Integer(3031), new Integer(-5));
		SKD3.put(new Integer(3047), new Integer(-5));
		SKD3.put(new Integer(COOKING_4_0_N), new Integer(-2));
		SKD3.put(new Integer(COOKING_4_1_N), new Integer(-2));
		SKD3.put(new Integer(COOKING_4_2_N), new Integer(-2));
		SKD3.put(new Integer(COOKING_4_3_N), new Integer(-2));
		SKD3.put(new Integer(DRAGON_SKIN), new Integer(-5));
		SKD3.put(new Integer(PATIENCE), new Integer(-2));
		SKD3.put(new Integer(EARTH_BLESS), new Integer(-2));
		SKD3.put(new Integer(IMMUNE_TO_HARM), new Integer(68));

		// 受到下列法術效果 傷害為0
		//SKM0.put(new Integer(ABSOLUTE_BARRIER), new Integer(0));// 絕對屏障
		SKM0.put(new Integer(ICE_LANCE), new Integer(0));// 冰矛圍籬
		SKM0.put(new Integer(EARTH_BIND), new Integer(0));// 大地屏障

		// 料理追加命中(_weaponType != 20) && (_weaponType != 62)近距離武器
		SKU1.put(new Integer(3016), new Integer(1));
		SKU1.put(new Integer(3024), new Integer(1));
		SKU1.put(new Integer(3034), new Integer(2));
		SKU1.put(new Integer(3042), new Integer(2));
		SKU1.put(new Integer(COOKING_4_0_N), new Integer(1));

		// 料理命中追加(_weaponType == 20) && (_weaponType == 62)遠距離武器
		SKU2.put(new Integer(3019), new Integer(1));
		SKU2.put(new Integer(3027), new Integer(1));
		SKU2.put(new Integer(3032), new Integer(1));
		SKU2.put(new Integer(3040), new Integer(1));
		SKU2.put(new Integer(COOKING_4_1_N), new Integer(1));

		// NPC需附加技能可攻擊
		SKNPC.put(new Integer(45912), new Integer(1013));
		SKNPC.put(new Integer(45913), new Integer(1013));
		SKNPC.put(new Integer(45914), new Integer(1013));
		SKNPC.put(new Integer(45915), new Integer(1013));
		SKNPC.put(new Integer(45916), new Integer(1014));
		SKNPC.put(new Integer(45941), new Integer(1015));
		SKNPC.put(new Integer(45752), new Integer(4005));
		SKNPC.put(new Integer(45753), new Integer(4005));
		SKNPC.put(new Integer(45675), new Integer(4006));
		SKNPC.put(new Integer(81082), new Integer(4006));
		SKNPC.put(new Integer(45625), new Integer(4006));
		SKNPC.put(new Integer(45674), new Integer(4006));
		SKNPC.put(new Integer(45685), new Integer(4006));
		SKNPC.put(new Integer(87000), new Integer(4007));
		SKNPC.put(new Integer(45020), new Integer(4008));
		SKNPC.put(new Integer(99019), new Integer(1027));// 巨型骷髏

		// NPC指定外型不可攻擊
		PLNPC.put(new Integer(46069), new Integer(6035));
		PLNPC.put(new Integer(46070), new Integer(6035));
		PLNPC.put(new Integer(46071), new Integer(6035));
		PLNPC.put(new Integer(46072), new Integer(6035));
		PLNPC.put(new Integer(46073), new Integer(6035));
		PLNPC.put(new Integer(46074), new Integer(6035));
		PLNPC.put(new Integer(46075), new Integer(6035));
		PLNPC.put(new Integer(46076), new Integer(6035));
		PLNPC.put(new Integer(46077), new Integer(6035));
		PLNPC.put(new Integer(46078), new Integer(6035));
		PLNPC.put(new Integer(46079), new Integer(6035));
		PLNPC.put(new Integer(46080), new Integer(6035));
		PLNPC.put(new Integer(46081), new Integer(6035));
		PLNPC.put(new Integer(46082), new Integer(6035));
		PLNPC.put(new Integer(46083), new Integer(6035));
		PLNPC.put(new Integer(46084), new Integer(6035));
		PLNPC.put(new Integer(46085), new Integer(6035));
		PLNPC.put(new Integer(46086), new Integer(6035));
		PLNPC.put(new Integer(46087), new Integer(6035));
		PLNPC.put(new Integer(46088), new Integer(6035));
		PLNPC.put(new Integer(46089), new Integer(6035));
		PLNPC.put(new Integer(46090), new Integer(6035));
		PLNPC.put(new Integer(46091), new Integer(6035));
		PLNPC.put(new Integer(46092), new Integer(6034));
		PLNPC.put(new Integer(46093), new Integer(6034));
		PLNPC.put(new Integer(46094), new Integer(6034));
		PLNPC.put(new Integer(46095), new Integer(6034));
		PLNPC.put(new Integer(46096), new Integer(6034));
		PLNPC.put(new Integer(46097), new Integer(6034));
		PLNPC.put(new Integer(46098), new Integer(6034));
		PLNPC.put(new Integer(46099), new Integer(6034));
		PLNPC.put(new Integer(46100), new Integer(6034));
		PLNPC.put(new Integer(46100), new Integer(6034));
		PLNPC.put(new Integer(46101), new Integer(6034));
		PLNPC.put(new Integer(46102), new Integer(6034));
		PLNPC.put(new Integer(46103), new Integer(6034));
		PLNPC.put(new Integer(46104), new Integer(6034));
		PLNPC.put(new Integer(46105), new Integer(6034));
		PLNPC.put(new Integer(46106), new Integer(6034));

		int strH = 0;
		STRH.put(new Integer(++strH), new Integer(-2));// 1
		STRH.put(new Integer(++strH), new Integer(-2));
		STRH.put(new Integer(++strH), new Integer(-2));
		STRH.put(new Integer(++strH), new Integer(-2));
		STRH.put(new Integer(++strH), new Integer(-2));
		STRH.put(new Integer(++strH), new Integer(-2));
		STRH.put(new Integer(++strH), new Integer(-2));
		STRH.put(new Integer(++strH), new Integer(-2));
		STRH.put(new Integer(++strH), new Integer(-1));
		STRH.put(new Integer(++strH), new Integer(-1));
		STRH.put(new Integer(++strH), new Integer(0));
		STRH.put(new Integer(++strH), new Integer(0));
		STRH.put(new Integer(++strH), new Integer(1));
		STRH.put(new Integer(++strH), new Integer(1));
		STRH.put(new Integer(++strH), new Integer(2));
		STRH.put(new Integer(++strH), new Integer(2));
		STRH.put(new Integer(++strH), new Integer(3));
		STRH.put(new Integer(++strH), new Integer(3));
		STRH.put(new Integer(++strH), new Integer(4));
		STRH.put(new Integer(++strH), new Integer(4));
		STRH.put(new Integer(++strH), new Integer(5));
		STRH.put(new Integer(++strH), new Integer(5));
		STRH.put(new Integer(++strH), new Integer(5));
		STRH.put(new Integer(++strH), new Integer(6));
		STRH.put(new Integer(++strH), new Integer(6));
		STRH.put(new Integer(++strH), new Integer(6));
		STRH.put(new Integer(++strH), new Integer(7));
		STRH.put(new Integer(++strH), new Integer(7));
		STRH.put(new Integer(++strH), new Integer(7));
		STRH.put(new Integer(++strH), new Integer(8));
		STRH.put(new Integer(++strH), new Integer(8));
		STRH.put(new Integer(++strH), new Integer(8));
		STRH.put(new Integer(++strH), new Integer(9));
		STRH.put(new Integer(++strH), new Integer(9));
		STRH.put(new Integer(++strH), new Integer(9));// 35
		STRH.put(new Integer(++strH), new Integer(10));
		STRH.put(new Integer(++strH), new Integer(10));
		STRH.put(new Integer(++strH), new Integer(10));
		STRH.put(new Integer(++strH), new Integer(11));
		STRH.put(new Integer(++strH), new Integer(11));
		STRH.put(new Integer(++strH), new Integer(11));
		STRH.put(new Integer(++strH), new Integer(12));
		STRH.put(new Integer(++strH), new Integer(12));
		STRH.put(new Integer(++strH), new Integer(12));
		STRH.put(new Integer(++strH), new Integer(13));
		STRH.put(new Integer(++strH), new Integer(13));
		STRH.put(new Integer(++strH), new Integer(13));
		STRH.put(new Integer(++strH), new Integer(14));
		STRH.put(new Integer(++strH), new Integer(14));
		STRH.put(new Integer(++strH), new Integer(14));
		STRH.put(new Integer(++strH), new Integer(15));
		STRH.put(new Integer(++strH), new Integer(15));
		STRH.put(new Integer(++strH), new Integer(15));
		STRH.put(new Integer(++strH), new Integer(16));
		STRH.put(new Integer(++strH), new Integer(16));
		STRH.put(new Integer(++strH), new Integer(16));
		STRH.put(new Integer(++strH), new Integer(17));
		STRH.put(new Integer(++strH), new Integer(17));
		STRH.put(new Integer(++strH), new Integer(17));
		STRH.put(new Integer(++strH), new Integer(18));// 60

		int dexH = 0;
		DEXH.put(new Integer(++dexH), new Integer(-2));
		DEXH.put(new Integer(++dexH), new Integer(-2));
		DEXH.put(new Integer(++dexH), new Integer(-2));
		DEXH.put(new Integer(++dexH), new Integer(-2));
		DEXH.put(new Integer(++dexH), new Integer(-2));
		DEXH.put(new Integer(++dexH), new Integer(-2));
		DEXH.put(new Integer(++dexH), new Integer(-1));
		DEXH.put(new Integer(++dexH), new Integer(-1));
		DEXH.put(new Integer(++dexH), new Integer(0));
		DEXH.put(new Integer(++dexH), new Integer(0));
		DEXH.put(new Integer(++dexH), new Integer(1));
		DEXH.put(new Integer(++dexH), new Integer(1));
		DEXH.put(new Integer(++dexH), new Integer(2));
		DEXH.put(new Integer(++dexH), new Integer(2));
		DEXH.put(new Integer(++dexH), new Integer(3));
		DEXH.put(new Integer(++dexH), new Integer(3));
		DEXH.put(new Integer(++dexH), new Integer(4));
		DEXH.put(new Integer(++dexH), new Integer(4));
		DEXH.put(new Integer(++dexH), new Integer(5));
		DEXH.put(new Integer(++dexH), new Integer(6));
		DEXH.put(new Integer(++dexH), new Integer(7));
		DEXH.put(new Integer(++dexH), new Integer(8));
		DEXH.put(new Integer(++dexH), new Integer(9));
		DEXH.put(new Integer(++dexH), new Integer(10));
		DEXH.put(new Integer(++dexH), new Integer(11));
		DEXH.put(new Integer(++dexH), new Integer(12));
		DEXH.put(new Integer(++dexH), new Integer(13));
		DEXH.put(new Integer(++dexH), new Integer(14));
		DEXH.put(new Integer(++dexH), new Integer(15));
		DEXH.put(new Integer(++dexH), new Integer(16));
		DEXH.put(new Integer(++dexH), new Integer(17));
		DEXH.put(new Integer(++dexH), new Integer(18));
		DEXH.put(new Integer(++dexH), new Integer(19));
		DEXH.put(new Integer(++dexH), new Integer(19));
		DEXH.put(new Integer(++dexH), new Integer(19));
		DEXH.put(new Integer(++dexH), new Integer(20));
		DEXH.put(new Integer(++dexH), new Integer(20));
		DEXH.put(new Integer(++dexH), new Integer(20));
		DEXH.put(new Integer(++dexH), new Integer(21));
		DEXH.put(new Integer(++dexH), new Integer(21));
		DEXH.put(new Integer(++dexH), new Integer(21));
		DEXH.put(new Integer(++dexH), new Integer(22));
		DEXH.put(new Integer(++dexH), new Integer(22));
		DEXH.put(new Integer(++dexH), new Integer(22));
		DEXH.put(new Integer(++dexH), new Integer(23));
		DEXH.put(new Integer(++dexH), new Integer(23));
		DEXH.put(new Integer(++dexH), new Integer(23));
		DEXH.put(new Integer(++dexH), new Integer(24));
		DEXH.put(new Integer(++dexH), new Integer(24));
		DEXH.put(new Integer(++dexH), new Integer(24));
		DEXH.put(new Integer(++dexH), new Integer(25));
		DEXH.put(new Integer(++dexH), new Integer(25));
		DEXH.put(new Integer(++dexH), new Integer(25));
		DEXH.put(new Integer(++dexH), new Integer(26));
		DEXH.put(new Integer(++dexH), new Integer(26));
		DEXH.put(new Integer(++dexH), new Integer(26));
		DEXH.put(new Integer(++dexH), new Integer(27));
		DEXH.put(new Integer(++dexH), new Integer(27));
		DEXH.put(new Integer(++dexH), new Integer(27));
		DEXH.put(new Integer(++dexH), new Integer(28));

		// 力量傷害補正
		for (int str = 0; str <= 8; str++) {
			// 1~8 -2
			
			STRD.put(new Integer(str), new Integer(-2));
		}
		for (int str = 9; str <= 10; str++) {
			// 9~10 -1
			
			STRD.put(new Integer(str), new Integer(-1));
		}
		STRD.put(new Integer(11), new Integer(0));
		STRD.put(new Integer(12), new Integer(0));
		STRD.put(new Integer(13), new Integer(1));
		STRD.put(new Integer(14), new Integer(1));
		STRD.put(new Integer(15), new Integer(2));
		STRD.put(new Integer(16), new Integer(2));
		STRD.put(new Integer(17), new Integer(3));
		STRD.put(new Integer(18), new Integer(3));
		STRD.put(new Integer(19), new Integer(4));
		STRD.put(new Integer(20), new Integer(4));
		STRD.put(new Integer(21), new Integer(5));
		STRD.put(new Integer(22), new Integer(5));
		STRD.put(new Integer(23), new Integer(6));
		STRD.put(new Integer(24), new Integer(6));
		STRD.put(new Integer(25), new Integer(6));
		STRD.put(new Integer(26), new Integer(7));
		STRD.put(new Integer(27), new Integer(7));
		STRD.put(new Integer(28), new Integer(7));
		STRD.put(new Integer(29), new Integer(8));
		STRD.put(new Integer(30), new Integer(8));
		STRD.put(new Integer(31), new Integer(9));
		STRD.put(new Integer(32), new Integer(9));
		STRD.put(new Integer(33), new Integer(10));
		STRD.put(new Integer(34), new Integer(11));
		STRD.put(new Integer(35), new Integer(12));
		STRD.put(new Integer(36), new Integer(12));
		STRD.put(new Integer(37), new Integer(12));
		STRD.put(new Integer(38), new Integer(12));
		STRD.put(new Integer(39), new Integer(13));
		STRD.put(new Integer(40), new Integer(13));
		STRD.put(new Integer(41), new Integer(13));
		STRD.put(new Integer(42), new Integer(13));
		STRD.put(new Integer(43), new Integer(14));
		STRD.put(new Integer(44), new Integer(14));
		STRD.put(new Integer(45), new Integer(14));
		STRD.put(new Integer(46), new Integer(14));
		STRD.put(new Integer(47), new Integer(15));
		STRD.put(new Integer(48), new Integer(15));
		STRD.put(new Integer(49), new Integer(16));
		STRD.put(new Integer(50), new Integer(17));

		int strdmg = 18;
		for (int str = 51; str <= 127; str++) { // 51~127 4＋1
			if (str % 4 == 1) {
				strdmg++;
			}			
			STRD.put(new Integer(str), new Integer(strdmg));
		}
//		int dmgStr = -6;
//		for (int str = 0; str <= 22; str++) { // 0~22 每2+1
//			if (str % 2 == 1) {
//				dmgStr++;
//			}
//			STRD.put(new Integer(str), new Integer(dmgStr));
//		}
//		for (int str = 23; str <= 28; str++) { // 23~28 每3+1
//			if (str % 3 == 2) {
//				dmgStr++;
//			}
//			STRD.put(new Integer(str), new Integer(dmgStr));
//		}
//		for (int str = 29; str <= 32; str++) { // 29~32 每2+1
//			if (str % 2 == 1) {
//				dmgStr++;
//			}
//			STRD.put(new Integer(str), new Integer(dmgStr));
//		}
//		for (int str = 33; str <= 34; str++) { // 33~34 每1+1
//			dmgStr++;
//			STRD.put(new Integer(str), new Integer(dmgStr));
//		}
//		for (int str = 35; str <= 254; str++) { // 35~254 每4+1
//			if (str % 4 == 1) {
//				dmgStr++;
//			}
//			STRD.put(new Integer(str), new Integer(dmgStr));
//		}

		// 敏捷傷害補正
		for (int dex = 0; dex <= 11; dex++) {
			// 0~11= 0
			DEXD.put(new Integer(dex), new Integer(0));
		}
		int dexdmg = 1;
		for (int dex = 12; dex <= 127; dex++) { //12~127 =>4 +1
			if (dex % 4 == 1) {
				dexdmg++;
			}
			DEXD.put(new Integer(dex), new Integer(dexdmg));
		}
//		for (int dex = 0; dex <= 14; dex++) {
//			// 0~14 = 0
//			DEXD.put(new Integer(dex), new Integer(0));
//		}
//
//		DEXD.put(new Integer(15), new Integer(1));
//		DEXD.put(new Integer(16), new Integer(2));
//		DEXD.put(new Integer(17), new Integer(3));
//		DEXD.put(new Integer(18), new Integer(4));
//		DEXD.put(new Integer(19), new Integer(4));
//		DEXD.put(new Integer(20), new Integer(4));
//		DEXD.put(new Integer(21), new Integer(5));
//		DEXD.put(new Integer(22), new Integer(5));
//		DEXD.put(new Integer(23), new Integer(5));
//
//		int dmgDex = 5;
//		for (int dex = 24; dex <= 35; dex++) { // 24~35 每3+1
//			if (dex % 3 == 1) {
//				dmgDex++;
//			}
//			DEXD.put(new Integer(dex), new Integer(dmgDex));
//		}
//		for (int dex = 36; dex <= 127; dex++) { // 36~127 每4+1
//			if (dex % 4 == 1) {
//				dmgDex++;
//			}
//			DEXD.put(new Integer(dex), new Integer(dmgDex));
//		}
	}
}
