package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 服務器限制設置
 * 
 * @author dexc
 */
public final class ConfigSkill {

	@SuppressWarnings("unused")
	private static final Log _log = LogFactory.getLog(ConfigSkill.class);
	
	/** 部分被動技能效果發動提示訊息(用於測試) */
	public static boolean AddPassiveSkillMsg = false;// true or false

	// /** 精準射擊機率設定 **/
	// public static int Precision1;
	// public static int Precision2;
	// public static int Precision3;
	// public static int Damage1;
	// public static int Damage2;
	// public static int Damage3;
	public static int Skulldamage;
	// public static int ImpactHalo1;
	// public static int ImpactHalo2;
	// public static int ImpactHalo3;
	public static String STUN_SEC;
	public static String CURSE_SEC;
	public static int FOE_SLAYER_RND;
	public static int FOE_SLAYER_SEC;
	public static int DISINTEGRATE_RND;
	public static double SOUL_OF_FLAME_DAMAGE;
	public static double STRIKER_DMG;
	public static double STRIKER_DMG2;
	public static int Counterattack;
	public static int Vulnerability1;
	public static int Vulnerability2;
	public static int Vulnerability3;
	public static int Vulnerability4;
	public static int JOY_OF_PAIN_DMG;
	public static int ILLUSION_AVATAR_DMG;
	public static int SHOCK1TIME;
	public static double ILLUSION_AVATAR_DAMAGE;
	public static String NO_CD;
	public static String HAS_SKILL;
	// /** #施放負面魔法是否依照智力，超過IntBigger點每提升一點就增加IntRepeated %機率， */
	// /** #當智力大於(含)IntLess點，機率百分之百? (True=是, False=否) */
	// public static boolean AddInt;
	// public static int IntBigger;
	// public static int IntRepeated;
	// public static int IntLess;
	public static double Crown1;
	public static double Crown2;
	public static double Crown3;
	public static double Crown4;
	public static double Crown5;
	public static double Crown6;
	public static double Crown7;
	public static double Crown8;
	public static double Knight1;
	public static double Knight2;
	public static double Knight3;
	public static double Knight4;
	public static double Knight5;
	public static double Knight6;
	public static double Knight7;
	public static double Knight8;
	public static double Elf1;
	public static double Elf2;
	public static double Elf3;
	public static double Elf4;
	public static double Elf5;
	public static double Elf6;
	public static double Elf7;
	public static double Elf8;
	public static double Wizard1;
	public static double Wizard2;
	public static double Wizard3;
	public static double Wizard4;
	public static double Wizard5;
	public static double Wizard6;
	public static double Wizard7;
	public static double Wizard8;
	public static double Darkelf1;
	public static double Darkelf2;
	public static double Darkelf3;
	public static double Darkelf4;
	public static double Darkelf5;
	public static double Darkelf6;
	public static double Darkelf7;
	public static double Darkelf8;
	public static double DragonKnight1;
	public static double DragonKnight2;
	public static double DragonKnight3;
	public static double DragonKnight4;
	public static double DragonKnight5;
	public static double DragonKnight6;
	public static double DragonKnight7;
	public static double DragonKnight8;
	public static double Illusionist1;
	public static double Illusionist2;
	public static double Illusionist3;
	public static double Illusionist4;
	public static double Illusionist5;
	public static double Illusionist6;
	public static double Illusionist7;
	public static double Illusionist8;
	public static double Warrior1;
	public static double Warrior2;
	public static double Warrior3;
	public static double Warrior4;
	public static double Warrior5;
	public static double Warrior6;
	public static double Warrior7;
	public static double Warrior8;
	//TODO
	public static boolean RoyalfamilyYN;
	public static boolean KnightYN;
	public static boolean AlluringwomanYN;
	public static boolean HeiyaoYN;
	public static boolean DragoonYN;
	public static boolean IllusionYN;
    public static int BURNINGSPIRITchance;
	public static int BURNINGSPIRITdmg;
	public static int BARRIERchance;
	public static boolean elfisBARRIER = false;
	public static int ElfThree;
	public static double Double_Brake_Dmg;
	public static double Armor_Break_Dmg;
	public static double Warrior_Magic;
	public static int isPassive_Tatin_Magic;
	public static int isPassive_Tatin_Rock;
	public static int isPassive_Tatin_Bullet;
	public static int isPassive_Tatin_CRASH;
	public static int isPassive_Tatin_FURY;
	public static int TRIPLE_ARROW_DEX;//三重矢原始傷害倍率
	public static double TRIPLE_ARROW_DMG;//三重矢原始傷害倍率
	public static double C2;
	public static double C4;
	public static int K2;
	public static int K4;
	public static int W2;//法師二轉
	public static double W4;//法師二轉
	public static double E2TRIPLE_ARROW;
	public static double E2ELEMENTAL_FIRE;
	public static double E4STRIKER_GALE;
	public static double E4SOUL_OF_FLAME;
	public static double E4NATURES_BLESSING;
	public static int D2;
	public static double D4;
	public static int DK2weaknss1;
	public static int DK2weaknss2;
	public static int DK2weaknss3;
	public static int DK2weaknss4;
	public static int DK4RANDOM;
	public static int DK4SHOCKTIME;
	public static double IS2;
	public static double IS4;
	public static int WHYW2;
	public static int WHYW4;
	/** 法師新技能 治癒逆行機率設定 **/
	public static int DEATH_HEAL;

	private static final String ALT_SETTINGS_FILE = "./config/skills.properties";

	public static void load() throws ConfigErrorException {
		// _log.info("載入服務器限制設置!");
		final Properties set = new Properties();
		try {
			final InputStream is = new FileInputStream(new File(
					ALT_SETTINGS_FILE));
			// 指定檔案編碼
			final InputStreamReader isr = new InputStreamReader(is, "utf-8");
			set.load(isr);
			is.close();
			// TODO
			RoyalfamilyYN = Boolean.parseBoolean(set.getProperty("RoyalfamilyYN", "true"));
			KnightYN = Boolean.parseBoolean(set.getProperty("KnightYN", "true"));
			AlluringwomanYN = Boolean.parseBoolean(set.getProperty("AlluringwomanYN", "true"));
			HeiyaoYN = Boolean.parseBoolean(set.getProperty("HeiyaoYN", "true"));
			DragoonYN = Boolean.parseBoolean(set.getProperty("DragoonYN", "true"));
			IllusionYN = Boolean.parseBoolean(set.getProperty("IllusionYN", "true"));

			// ImpactHalo1 = Integer.parseInt(set.getProperty("ImpactHalo1", "1"));
			// ImpactHalo2 = Integer.parseInt(set.getProperty("ImpactHalo2", "1"));
			// ImpactHalo3 = Integer.parseInt(set.getProperty("ImpactHalo3", "1"));
			// Precision1 = Integer.parseInt(set.getProperty("Precision1", "0"));
			// Precision2 = Integer.parseInt(set.getProperty("Precision2", "0"));
			// Precision3 = Integer.parseInt(set.getProperty("Precision3", "0"));
			STUN_SEC = set.getProperty("STUN_SEC", "3~6");

			CURSE_SEC = set.getProperty("CURSE_SEC", "3~7");

			FOE_SLAYER_RND = Integer.parseInt(set.getProperty("FOE_SLAYER_RND", "25"));

			FOE_SLAYER_SEC = Integer.parseInt(set.getProperty("FOE_SLAYER_SEC", "100"));

			DISINTEGRATE_RND = Integer.parseInt(set.getProperty("DISINTEGRATE_RND", "25"));

			STRIKER_DMG = Double.parseDouble(set.getProperty("STRIKER_DMG", "1.0"));
			STRIKER_DMG2 = Double.parseDouble(set.getProperty("STRIKER_DMG2", "1.0"));
			Counterattack = Integer.parseInt(set.getProperty("Counterattack", "3.0"));

			SOUL_OF_FLAME_DAMAGE = Double.parseDouble(set.getProperty("SOUL_OF_FLAME_DAMAGE", "1.0"));
			Vulnerability1 = Integer.parseInt(set.getProperty("Vulnerability1", "20"));
			Vulnerability2 = Integer.parseInt(set.getProperty("Vulnerability2", "40"));
			Vulnerability3 = Integer.parseInt(set.getProperty("Vulnerability3", "60"));
			Vulnerability4 = Integer.parseInt(set.getProperty("Vulnerability4", "80"));

			JOY_OF_PAIN_DMG = Integer.parseInt(set.getProperty("JOY_OF_PAIN_DMG", "0"));

			ILLUSION_AVATAR_DMG = Integer.parseInt(set.getProperty("ILLUSION_AVATAR_DMG", "1"));
			ILLUSION_AVATAR_DAMAGE = Double.parseDouble(set.getProperty("ILLUSION_AVATAR_DAMAGE", "1.5"));
			SHOCK1TIME = Integer.parseInt(set.getProperty("SHOCK1TIME", "2"));
			NO_CD = set.getProperty("NO_CD", "null");
			HAS_SKILL = set.getProperty("HAS_SKILL", "null");
			// AddInt = Boolean.parseBoolean(set.getProperty("AddInt", "false"));
			// IntBigger = Integer.parseInt(set.getProperty("IntBigger", "25"));
			// IntRepeated = Integer.parseInt(set.getProperty("IntRepeated", "1"));
			// IntLess = Integer.parseInt(set.getProperty("IntLess", "127"));

			// Damage1 = Integer.parseInt(set.getProperty("Damage1", "10"));
			// Damage2 = Integer.parseInt(set.getProperty("Damage2", "10"));
			// Damage3 = Integer.parseInt(set.getProperty("Damage3", "10"));
			Skulldamage = Integer.parseInt(set.getProperty("Skulldamage", "10"));

			Crown1 = Double.parseDouble(set.getProperty("Crown1", "0"));
			Crown2 = Double.parseDouble(set.getProperty("Crown2", "0"));
			Crown3 = Double.parseDouble(set.getProperty("Crown3", "0"));
			Crown4 = Double.parseDouble(set.getProperty("Crown4", "0"));
			Crown5 = Double.parseDouble(set.getProperty("Crown5", "0"));
			Crown6 = Double.parseDouble(set.getProperty("Crown6", "0"));
			Crown7 = Double.parseDouble(set.getProperty("Crown7", "0"));
			Crown8 = Double.parseDouble(set.getProperty("Crown8", "0"));
			Knight1 = Double.parseDouble(set.getProperty("Knight1", "0"));
			Knight2 = Double.parseDouble(set.getProperty("Knight2", "0"));
			Knight3 = Double.parseDouble(set.getProperty("Knight3", "0"));
			Knight4 = Double.parseDouble(set.getProperty("Knight4", "0"));
			Knight5 = Double.parseDouble(set.getProperty("Knight5", "0"));
			Knight6 = Double.parseDouble(set.getProperty("Knight6", "0"));
			Knight7 = Double.parseDouble(set.getProperty("Knight7", "0"));
			Knight8 = Double.parseDouble(set.getProperty("Knight8", "0"));
			Elf1 = Double.parseDouble(set.getProperty("Elf1", "0"));
			Elf2 = Double.parseDouble(set.getProperty("Elf2", "0"));
			Elf3 = Double.parseDouble(set.getProperty("Elf3", "0"));
			Elf4 = Double.parseDouble(set.getProperty("Elf4", "0"));
			Elf5 = Double.parseDouble(set.getProperty("Elf5", "0"));
			Elf6 = Double.parseDouble(set.getProperty("Elf6", "0"));
			Elf7 = Double.parseDouble(set.getProperty("Elf7", "0"));
			Elf8 = Double.parseDouble(set.getProperty("Elf8", "0"));
			Wizard1 = Double.parseDouble(set.getProperty("Wizard1", "0"));
			Wizard2 = Double.parseDouble(set.getProperty("Wizard2", "0"));
			Wizard3 = Double.parseDouble(set.getProperty("Wizard3", "0"));
			Wizard4 = Double.parseDouble(set.getProperty("Wizard4", "0"));
			Wizard5 = Double.parseDouble(set.getProperty("Wizard5", "0"));
			Wizard6 = Double.parseDouble(set.getProperty("Wizard6", "0"));
			Wizard7 = Double.parseDouble(set.getProperty("Wizard7", "0"));
			Wizard8 = Double.parseDouble(set.getProperty("Wizard8", "0"));
			Darkelf1 = Double.parseDouble(set.getProperty("Darkelf1", "0"));
			Darkelf2 = Double.parseDouble(set.getProperty("Darkelf2", "0"));
			Darkelf3 = Double.parseDouble(set.getProperty("Darkelf3", "0"));
			Darkelf4 = Double.parseDouble(set.getProperty("Darkelf4", "0"));
			Darkelf5 = Double.parseDouble(set.getProperty("Darkelf5", "0"));
			Darkelf6 = Double.parseDouble(set.getProperty("Darkelf6", "0"));
			Darkelf7 = Double.parseDouble(set.getProperty("Darkelf7", "0"));
			Darkelf8 = Double.parseDouble(set.getProperty("Darkelf8", "0"));
			DragonKnight1 = Double.parseDouble(set.getProperty("DragonKnight1", "0"));
			DragonKnight2 = Double.parseDouble(set.getProperty("DragonKnight2", "0"));
			DragonKnight3 = Double.parseDouble(set.getProperty("DragonKnight3", "0"));
			DragonKnight4 = Double.parseDouble(set.getProperty("DragonKnight4", "0"));
			DragonKnight5 = Double.parseDouble(set.getProperty("DragonKnight5", "0"));
			DragonKnight6 = Double.parseDouble(set.getProperty("DragonKnight6", "0"));
			DragonKnight7 = Double.parseDouble(set.getProperty("DragonKnight7", "0"));
			DragonKnight8 = Double.parseDouble(set.getProperty("DragonKnight8", "0"));
			Illusionist1 = Double.parseDouble(set.getProperty("Illusionist1", "0"));
			Illusionist2 = Double.parseDouble(set.getProperty("Illusionist2", "0"));
			Illusionist3 = Double.parseDouble(set.getProperty("Illusionist3", "0"));
			Illusionist4 = Double.parseDouble(set.getProperty("Illusionist4", "0"));
			Illusionist5 = Double.parseDouble(set.getProperty("Illusionist5", "0"));
			Illusionist6 = Double.parseDouble(set.getProperty("Illusionist6", "0"));
			Illusionist7 = Double.parseDouble(set.getProperty("Illusionist7", "0"));
			Illusionist8 = Double.parseDouble(set.getProperty("Illusionist8", "0"));
			Warrior1 = Double.parseDouble(set.getProperty("Warrior1", "0"));
			Warrior2 = Double.parseDouble(set.getProperty("Warrior2", "0"));
			Warrior3 = Double.parseDouble(set.getProperty("Warrior3", "0"));
			Warrior4 = Double.parseDouble(set.getProperty("Warrior4", "0"));
			Warrior5 = Double.parseDouble(set.getProperty("Warrior5", "0"));
			Warrior6 = Double.parseDouble(set.getProperty("Warrior6", "0"));
			Warrior7 = Double.parseDouble(set.getProperty("Warrior7", "0"));
			Warrior8 = Double.parseDouble(set.getProperty("Warrior8", "0"));
			BURNINGSPIRITchance = Integer.parseInt(set.getProperty("BURNINGSPIRITchance", "33"));
			BURNINGSPIRITdmg = Integer.parseInt(set.getProperty("BURNINGSPIRITdmg", "5"));
			BARRIERchance = Integer.parseInt(set.getProperty("BARRIERchance", "33"));
			elfisBARRIER = Boolean.parseBoolean(set.getProperty("elfisBARRIER", "false"));
			ElfThree = Integer.parseInt(set.getProperty("ElfThree", "3"));
			//黑妖 雙重破壞傷害倍率(說明:設置1.8為1.8倍 )
			Double_Brake_Dmg = Double.parseDouble(set.getProperty("Double_Brake_Dmg","1.8"));
			//黑妖 破壞盔甲傷害倍率(說明:設置0.58為額外加0.58%傷害)
			Armor_Break_Dmg = Double.parseDouble(set.getProperty("Armor_Break_Dmg","0.58"));
			//戰士魔法 泰坦:子彈 泰坦:魔法 泰坦:岩石 血量低於多少%才發動(0.4=40%  正服設定40%)
			Warrior_Magic = Double.parseDouble(set.getProperty("Warrior_Magic", "0.4"));
		    //戰士魔法 (泰坦: 魔法)反彈時額外隨機傷害增加(設定50 = 隨機反彈1 ~ 50傷害)
			isPassive_Tatin_Magic = Integer.parseInt(set.getProperty("isPassive_Tatin_Magic", "10"));
		    //戰士魔法 (泰坦: 岩石)反彈時額外隨機傷害增加(設定50 = 隨機反彈1 ~ 50傷害)
			isPassive_Tatin_Rock = Integer.parseInt(set.getProperty("isPassive_Tatin_Rock", "10"));
		    //戰士魔法 (泰坦: 子彈)反彈時額外隨機傷害增加(設定50 = 隨機反彈1 ~ 50傷害)
			isPassive_Tatin_Bullet = Integer.parseInt(set.getProperty("isPassive_Tatin_Bullet", "10"));
			// 戰士魔法 (粉碎)
			isPassive_Tatin_CRASH = Integer.parseInt(set.getProperty("isPassive_Tatin_CRASH", "19"));
			// 戰士魔法 (狂暴)
			isPassive_Tatin_FURY = Integer.parseInt(set.getProperty("isPassive_Tatin_FURY", "19"));
			// 敏捷大於多少三重矢強化
			TRIPLE_ARROW_DEX = Integer.parseInt(set.getProperty("TripleArrowDex", "180"));
			// 三重矢原始傷害倍率
			TRIPLE_ARROW_DMG = Double.parseDouble(set.getProperty("TripleArrowDmg", "0.8"));

			C2 = Double.parseDouble(set.getProperty("C2", "1.0"));
			C4 = Double.parseDouble(set.getProperty("C4", "1.0"));
			K2 = Integer.parseInt(set.getProperty("K2", "1"));
			K4 = Integer.parseInt(set.getProperty("K4", "1"));
			W2 = Integer.parseInt(set.getProperty("W2", "1"));
			W4 = Double.parseDouble(set.getProperty("W4", "1.0"));
			E2TRIPLE_ARROW = Double.parseDouble(set.getProperty("E2TRIPLE_ARROW", "1.0"));
			E2ELEMENTAL_FIRE = Double.parseDouble(set.getProperty("E2ELEMENTAL_FIRE", "1.0"));
			E4STRIKER_GALE = Double.parseDouble(set.getProperty("E4STRIKER_GALE", "1.0"));
			E4SOUL_OF_FLAME = Double.parseDouble(set.getProperty("E4SOUL_OF_FLAME", "1.0"));
			E4NATURES_BLESSING = Double.parseDouble(set.getProperty("E4NATURES_BLESSING", "1.0"));
			D2 = Integer.parseInt(set.getProperty("D2", "1"));
			D4 = Double.parseDouble(set.getProperty("D4", "1.0"));
			DK2weaknss1 = Integer.parseInt(set.getProperty("DK2weaknss1", "5"));
			DK2weaknss2 = Integer.parseInt(set.getProperty("DK2weaknss2", "10"));
			DK2weaknss3 = Integer.parseInt(set.getProperty("DK2weaknss3", "15"));
			DK2weaknss4 = Integer.parseInt(set.getProperty("DK2weaknss4", "20"));
			DK4RANDOM = Integer.parseInt(set.getProperty("DK4RANDOM", "15"));
			DK4SHOCKTIME = Integer.parseInt(set.getProperty("DK4SHOCKTIME", "1"));
			IS2 = Double.parseDouble(set.getProperty("IS2", "0.1"));
			IS4 = Double.parseDouble(set.getProperty("IS4", "1.5"));
			WHYW2 = Integer.parseInt(set.getProperty("WHYW2", "15"));
			WHYW4 = Integer.parseInt(set.getProperty("WHYW4", "15"));
			DEATH_HEAL = Integer.parseInt(set.getProperty("DEATH_HEAL", "20"));// 法師新技能治癒逆行機率設定

		} catch (final Exception e) {
			throw new ConfigErrorException("設置檔案遺失: " + ALT_SETTINGS_FILE);

		} finally {
			set.clear();
		}
	}
}
