package com.lineage.server.model.skill;

import static com.lineage.server.model.skill.L1SkillId.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.skill.skillmode.*;

public class L1SkillMode {

	private static final Log _log = LogFactory.getLog(L1SkillMode.class);

	// Map<K,V>
	private static final Map<Integer, SkillMode> _skillMode = new HashMap<Integer, SkillMode>();

	private static L1SkillMode _instance;

	public static L1SkillMode get() {
		if (_instance == null) {
			_instance = new L1SkillMode();
		}
		return _instance;
	}

	/**
	 * 不会被相消的技能
	 * 
	 * @param skillNum
	 * @return
	 */
	public boolean isNotCancelable(final int skillNum) {
		return (skillNum == ENCHANT_WEAPON) // 拟似魔法武器
				|| (skillNum == BLESSED_ARMOR) // 铠甲护持
				|| (skillNum == ADVANCE_SPIRIT) // 灵魂升华
				|| (skillNum == SHOCK_STUN) // 冲击之晕
				|| (skillNum == REDUCTION_ARMOR) // 增幅防御
				|| (skillNum == BOUNCE_ATTACK) // 尖刺盔甲
				|| (skillNum == SOLID_CARRIAGE) // 坚固防护
				|| (skillNum == COUNTER_BARRIER) // 反击屏障
				|| (skillNum == UNCANNY_DODGE) // 暗影闪避
				|| (skillNum == SHADOW_ARMOR) // 影之防护
				|| (skillNum == ARMOR_BREAK) // 破坏盔甲
				|| (skillNum == SHADOW_FANG) // 暗影之牙
				|| (skillNum == DRESS_MIGHTY) // 力量提升
				|| (skillNum == DRESS_DEXTERITY) // 敏捷提升
				|| (skillNum == DRESS_EVASION) // 闪避提升
				|| (skillNum == AWAKEN_ANTHARAS) // 觉醒：安塔瑞斯
				|| (skillNum == AWAKEN_FAFURION) // 觉醒：法利昂
				|| (skillNum == AWAKEN_VALAKAS) // 觉醒：巴拉卡斯
				|| (skillNum == ILLUSION_OGRE) // 幻觉：欧吉
				|| (skillNum == ILLUSION_LICH) // 幻觉：巫妖
				|| (skillNum == ILLUSION_DIA_GOLEM) // 幻觉：钻石高仑
				|| (skillNum == ILLUSION_AVATAR) // 幻觉：化身
				|| (skillNum == BONE_BREAK) // 骷髅毁坏
				|| (skillNum == STATUS_BRAVE3) // 三段加速
				|| (skillNum == ILLUSION_OGRE) // 三段加速
				|| (skillNum == CUBE_IGNITION) // 三段加速
				|| (skillNum == ILLUSION_LICH) // 三段加速
				|| (skillNum == CUBE_QUAKE) // 三段加速
				|| (skillNum == ILLUSION_DIA_GOLEM) // 三段加速
				|| (skillNum == CUBE_SHOCK ) // 三段加速
				|| (skillNum == ILLUSION_AVATAR) // 三段加速
				|| (skillNum == CUBE_BALANCE); // 三段加速

	}

	public void load() {
		try {
			_skillMode.put(Integer.valueOf(HASTE), new HASTE()); // 加速术43
			_skillMode.put(Integer.valueOf(CANCELLATION), new CANCELLATION()); // 魔法相消术44
			_skillMode.put(Integer.valueOf(CURE_POISON), new CURE_POISON()); // 解毒术9
			_skillMode.put(Integer.valueOf(REMOVE_CURSE), new REMOVE_CURSE()); // 圣洁之光37
			_skillMode.put(Integer.valueOf(SHAPE_CHANGE), new SHAPE_CHANGE()); // 变形术67
			_skillMode.put(Integer.valueOf(RESURRECTION), new RESURRECTION()); // 返生术61
			_skillMode.put(Integer.valueOf(GREATER_RESURRECTION),
					new GREATER_RESURRECTION()); // 终极返生术75
			_skillMode.put(Integer.valueOf(ADVANCE_SPIRIT),
					new ADVANCE_SPIRIT()); // 灵魂升华79
			_skillMode.put(Integer.valueOf(CURSE_PARALYZE),
					new CURSE_PARALYZE()); // 木乃伊的诅咒33
			_skillMode.put(Integer.valueOf(CURSE_PARALYZE2),
					new CURSE_PARALYZE()); // 魔法效果:麻痹
			_skillMode.put(Integer.valueOf(CURSE_BLIND), new CURSE_BLIND()); // 闇盲咒术20
			_skillMode.put(Integer.valueOf(DARKNESS), new CURSE_BLIND()); // 黑闇之影40
			_skillMode.put(Integer.valueOf(DECAY_POTION), new DECAY_POTION()); // 药水霜化术71

			_skillMode.put(Integer.valueOf(CALL_CLAN), new CALL_CLAN()); // 呼唤盟友116
			_skillMode.put(Integer.valueOf(RUN_CLAN), new RUN_CLAN()); // 援护盟友118
			_skillMode.put(Integer.valueOf(TRUE_TARGET), new TRUE_TARGET());// 精准目标113
			_skillMode.put(BRAVE_AVATAR, new BRAVE_AVATAR());// 王者加护121
			_skillMode.put(IMPACT, new IMPACT());// 王者加护121

			_skillMode.put(Integer.valueOf(SHOCK_STUN), new SHOCK_STUN()); // 冲击之晕87
			_skillMode.put(Integer.valueOf(BOUNCE_ATTACK), new BOUNCE_ATTACK()); // 尖刺盔甲89
			_skillMode.put(Integer.valueOf(SOLID_CARRIAGE),
					new SOLID_CARRIAGE()); // 坚固防护90
			_skillMode.put(Integer.valueOf(PRIDE), new PRIDE()); // 荣耀心93
			_skillMode.put(Integer.valueOf(BLOW_ATTACK), new BLOW_ATTACK()); // 荣耀盾94

			_skillMode.put(Integer.valueOf(CALL_OF_NATURE),
					new CALL_OF_NATURE()); // 生命呼唤165
			_skillMode.put(Integer.valueOf(ELEMENTAL_FALL_DOWN),
					new ELEMENTAL_FALL_DOWN()); // 弱化属性133
			_skillMode.put(Integer.valueOf(BODY_TO_MIND), new BODY_TO_MIND()); // 心灵转换130
			_skillMode.put(Integer.valueOf(BLOODY_SOUL), new BLOODY_SOUL()); // 魂体转换146
			_skillMode.put(Integer.valueOf(132), new TRIPLE_ARROW());
			_skillMode.put(Integer.valueOf(131), new TELEPORT_TO_MATHER());
			_skillMode.put(Integer.valueOf(AQUA_PROTECTER),
					new AQUA_PROTECTER());
			_skillMode.put(Integer.valueOf(162), new GREATER_ELEMENTAL());
			_skillMode.put(Integer.valueOf(154), new LESSER_ELEMENTAL());
			_skillMode.put(Integer.valueOf(167), new WIND_SHACKLE());
			_skillMode.put(Integer.valueOf(FIRE_BLESS), new FIRE_BLESS()); // 舞跃之火
			_skillMode.put(Integer.valueOf(157), new EARTH_BIND());

			_skillMode.put(Integer.valueOf(EGLE_EYE), new EGLE_EYE()); // 鹰眼

			_skillMode.put(Integer.valueOf(FOCUS_WAVE), new FOCUS_WAVE()); // 波涛之水
			_skillMode.put(Integer.valueOf(HURRICANE), new HURRICANE()); // 狂怒之风
			_skillMode.put(Integer.valueOf(SAND_STORM), new SAND_STORM()); // 奔崩之土

			_skillMode.put(Integer.valueOf(DRESS_EVASION), new DRESS_EVASION());
			_skillMode.put(Integer.valueOf(UNCANNY_DODGE), new UNCANNY_DODGE());
			_skillMode.put(Integer.valueOf(103), new DARK_BLIND());
			_skillMode.put(Integer.valueOf(SHADOW_ARMOR), new SHADOW_ARMOR());
			_skillMode.put(Integer.valueOf(LUCIFER), new LUCIFER()); // 黑妖新技能
																		// 暗影屏障

			_skillMode.put(Integer.valueOf(AWAKEN_ANTHARAS),
					new AWAKEN_ANTHARAS());
			_skillMode.put(Integer.valueOf(AWAKEN_FAFURION),
					new AWAKEN_FAFURION());
			_skillMode.put(Integer.valueOf(AWAKEN_VALAKAS),
					new AWAKEN_VALAKAS());
			_skillMode.put(Integer.valueOf(SCALES_WIND_DRAGON),
					new SCALES_WIND_DRAGON()); // 觉醒：林德拜尔
			_skillMode.put(Integer.valueOf(FOE_SLAYER), new FOE_SLAYER()); // 屠宰者
			_skillMode.put(Integer.valueOf(186), new BLOODLUST());
			_skillMode.put(Integer.valueOf(188), new RESIST_FEAR());
			_skillMode.put(Integer.valueOf(THUNDER_GRAB), new THUNDER_GRAB()); // 夺命之雷

			_skillMode.put(Integer.valueOf(202), new CONFUSION());
			_skillMode.put(Integer.valueOf(212), new PHANTASM());
			_skillMode.put(Integer.valueOf(217), new PANIC());
			_skillMode.put(Integer.valueOf(216), new INSIGHT());
			_skillMode.put(Integer.valueOf(208), new BONE_BREAK());
			_skillMode.put(Integer.valueOf(207), new MIND_BREAK());
			// _skillMode.put(Integer.valueOf(219), new ILLUSION_AVATAR());
			// _skillMode.put(Integer.valueOf(209), new ILLUSION_LICH());
			_skillMode.put(Integer.valueOf(201), new MIRROR_IMAGE());
			_skillMode.put(Integer.valueOf(FOCUS_SPRITS), new FOCUS_SPRITS());

			_skillMode.put(Integer.valueOf(GIGANTIC), new GIGANTIC());// 狂战士
																		// 体能强化

			_skillMode.put(Integer.valueOf(68), new IMMUNE_TO_HARM()); // SRC0808
			_skillMode.put(Integer.valueOf(174), new STRIKER_GALE()); // SRC0808

			// _skillMode.put(Integer.valueOf(218), new JOY_OF_PAIN());

			_skillMode.put(Integer.valueOf(4000), new STATUS_FREEZE());

			_skillMode.put(Integer.valueOf(6683), new DRAGONEYE_VALAKAS());
			_skillMode.put(Integer.valueOf(6684), new DRAGONEYE_ANTHARAS());
			_skillMode.put(Integer.valueOf(6685), new DRAGONEYE_FAFURION());
			_skillMode.put(Integer.valueOf(6686), new DRAGONEYE_LINDVIOR());
			_skillMode.put(Integer.valueOf(6687), new DRAGONEYE_LIFE());
			_skillMode.put(Integer.valueOf(6688), new DRAGONEYE_BIRTH());
			_skillMode.put(Integer.valueOf(6689), new DRAGONEYE_FIGURE());

			_skillMode.put(Integer.valueOf(ADLV80_1), new ADLV80_1());
			_skillMode.put(Integer.valueOf(ADLV80_2), new ADLV80_2());
			_skillMode.put(Integer.valueOf(ADLV80_3), new ADLV80_3());
			_skillMode.put(Integer.valueOf(ADLV80_4), new ADLV80_4()); // 巴拉卡斯副本

			_skillMode.put(Integer.valueOf(11060), new KIRTAS_BARRIER1());
			_skillMode.put(Integer.valueOf(11059), new KIRTAS_BARRIER2());
			_skillMode.put(Integer.valueOf(11058), new KIRTAS_BARRIER3());
			_skillMode.put(Integer.valueOf(11057), new KIRTAS_BARRIER4());
			_skillMode.put(Integer.valueOf(11061), new LINDVIOR_SKY_SPIKED());

			_skillMode.put(Integer.valueOf(4500), new DS_GX00());
			_skillMode.put(Integer.valueOf(4501), new DS_GX01());
			_skillMode.put(Integer.valueOf(4502), new DS_GX02());
			_skillMode.put(Integer.valueOf(4503), new DS_GX03());
			_skillMode.put(Integer.valueOf(4504), new DS_GX04());
			_skillMode.put(Integer.valueOf(4505), new DS_GX05());
			_skillMode.put(Integer.valueOf(4506), new DS_GX06());
			_skillMode.put(Integer.valueOf(4507), new DS_GX07());
			_skillMode.put(Integer.valueOf(4508), new DS_GX08());
			_skillMode.put(Integer.valueOf(4509), new DS_GX09());

			_skillMode.put(Integer.valueOf(4510), new DS_AX00());
			_skillMode.put(Integer.valueOf(4511), new DS_AX01());
			_skillMode.put(Integer.valueOf(4512), new DS_AX02());
			_skillMode.put(Integer.valueOf(4513), new DS_AX03());
			_skillMode.put(Integer.valueOf(4514), new DS_AX04());
			_skillMode.put(Integer.valueOf(4515), new DS_AX05());
			_skillMode.put(Integer.valueOf(4516), new DS_AX06());
			_skillMode.put(Integer.valueOf(4517), new DS_AX07());
			_skillMode.put(Integer.valueOf(4518), new DS_AX08());
			_skillMode.put(Integer.valueOf(4519), new DS_AX09());

			_skillMode.put(Integer.valueOf(4520), new DS_WX00());
			_skillMode.put(Integer.valueOf(4521), new DS_WX01());
			_skillMode.put(Integer.valueOf(4522), new DS_WX02());
			_skillMode.put(Integer.valueOf(4523), new DS_WX03());
			_skillMode.put(Integer.valueOf(4524), new DS_WX04());
			_skillMode.put(Integer.valueOf(4525), new DS_WX05());
			_skillMode.put(Integer.valueOf(4526), new DS_WX06());
			_skillMode.put(Integer.valueOf(4527), new DS_WX07());
			_skillMode.put(Integer.valueOf(4528), new DS_WX08());
			_skillMode.put(Integer.valueOf(4529), new DS_WX09());

			_skillMode.put(Integer.valueOf(4530), new DS_ASX00());
			_skillMode.put(Integer.valueOf(4531), new DS_ASX01());
			_skillMode.put(Integer.valueOf(4532), new DS_ASX02());
			_skillMode.put(Integer.valueOf(4533), new DS_ASX03());
			_skillMode.put(Integer.valueOf(4534), new DS_ASX04());
			_skillMode.put(Integer.valueOf(4535), new DS_ASX05());
			_skillMode.put(Integer.valueOf(4536), new DS_ASX06());
			_skillMode.put(Integer.valueOf(4537), new DS_ASX07());
			_skillMode.put(Integer.valueOf(4538), new DS_ASX08());
			_skillMode.put(Integer.valueOf(4539), new DS_ASX09());

			_skillMode.put(Integer.valueOf(4401), new BS_GX01());
			_skillMode.put(Integer.valueOf(4402), new BS_GX02());
			_skillMode.put(Integer.valueOf(4403), new BS_GX03());
			_skillMode.put(Integer.valueOf(4404), new BS_GX04());
			_skillMode.put(Integer.valueOf(4405), new BS_GX05());
			_skillMode.put(Integer.valueOf(4406), new BS_GX06());
			_skillMode.put(Integer.valueOf(4407), new BS_GX07());
			_skillMode.put(Integer.valueOf(4408), new BS_GX08());
			_skillMode.put(Integer.valueOf(4409), new BS_GX09());

			_skillMode.put(Integer.valueOf(4411), new BS_AX01());
			_skillMode.put(Integer.valueOf(4412), new BS_AX02());
			_skillMode.put(Integer.valueOf(4413), new BS_AX03());
			_skillMode.put(Integer.valueOf(4414), new BS_AX04());
			_skillMode.put(Integer.valueOf(4415), new BS_AX05());
			_skillMode.put(Integer.valueOf(4416), new BS_AX06());
			_skillMode.put(Integer.valueOf(4417), new BS_AX07());
			_skillMode.put(Integer.valueOf(4418), new BS_AX08());
			_skillMode.put(Integer.valueOf(4419), new BS_AX09());

			_skillMode.put(Integer.valueOf(4421), new BS_WX01());
			_skillMode.put(Integer.valueOf(4422), new BS_WX02());
			_skillMode.put(Integer.valueOf(4423), new BS_WX03());
			_skillMode.put(Integer.valueOf(4424), new BS_WX04());
			_skillMode.put(Integer.valueOf(4425), new BS_WX05());
			_skillMode.put(Integer.valueOf(4426), new BS_WX06());
			_skillMode.put(Integer.valueOf(4427), new BS_WX07());
			_skillMode.put(Integer.valueOf(4428), new BS_WX08());
			_skillMode.put(Integer.valueOf(4429), new BS_WX09());

			_skillMode.put(Integer.valueOf(4431), new BS_ASX01());
			_skillMode.put(Integer.valueOf(4432), new BS_ASX02());
			_skillMode.put(Integer.valueOf(4433), new BS_ASX03());
			_skillMode.put(Integer.valueOf(4434), new BS_ASX04());
			_skillMode.put(Integer.valueOf(4435), new BS_ASX05());
			_skillMode.put(Integer.valueOf(4436), new BS_ASX06());
			_skillMode.put(Integer.valueOf(4437), new BS_ASX07());
			_skillMode.put(Integer.valueOf(4438), new BS_ASX08());
			_skillMode.put(Integer.valueOf(4439), new BS_ASX09());

			_skillMode
					.put(Integer.valueOf(DRAGON_BLOOD_1), new DRAGON_BLOOD1());
			_skillMode
					.put(Integer.valueOf(DRAGON_BLOOD_2), new DRAGON_BLOOD2());
			_skillMode
					.put(Integer.valueOf(DRAGON_BLOOD_3), new DRAGON_BLOOD3());
			_skillMode
					.put(Integer.valueOf(DRAGON_BLOOD_4), new DRAGON_BLOOD4()); // 巴拉卡斯副本

			_skillMode.put(Integer.valueOf(40001), new BAPHOMET());
			_skillMode.put(Integer.valueOf(4018), new MOVE_STOP());

			_skillMode.put(POWERGRIP, new POWERGRIP()); // 狂战士 拘束移动
			_skillMode.put(DESPERADO, new DESPERADO()); // 狂战士 亡命之徒
			_skillMode.put(TOMAHAWK, new TOMAHAWK()); // 狂战士 战斧投掷
			_skillMode.put(ARMOR_BREAK, new ARMOR_BREAK()); // 破坏盔甲


		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public SkillMode getSkill(final int skillid) {
		return _skillMode.get(skillid);
	}

}
