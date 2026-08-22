package com.lineage.server.model.monitor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.serverpackets.S_PacketBoxCharEr;
import com.lineage.server.serverpackets.ability.S_ConDetails;
import com.lineage.server.serverpackets.ability.S_DexDetails;
import com.lineage.server.serverpackets.ability.S_IntDetails;
import com.lineage.server.serverpackets.ability.S_StrDetails;
import com.lineage.server.serverpackets.ability.S_WeightStatus;
import com.lineage.server.serverpackets.ability.S_WisDetails;

/**
 * 760屬性更新
 * @author Admin
 */
public class L1PcDetailsMonitor extends L1PcMonitor {

	private int _old_str = 0;

	private int _old_dex = 0;

	private int _old_con = 0;

	private int _old_int = 0;

	private int _old_wis = 0;

	private int _old_base_str = 0;

	private int _old_base_dex = 0;

	private int _old_base_con = 0;

	private int _old_base_int = 0;

	private int _old_base_wis = 0;

	/**
	 * 760屬性更新
	 * @param pcObjId
	 * @param delay
	 */
	public L1PcDetailsMonitor(final int pcObjId) {
		super(pcObjId);
	}

	@Override
	public void execTask(final L1PcInstance pc) {

		// color：2:一般 16:亮黃色(基礎能力增加) 32:升級確認

		if (_old_str != pc.getStr()) {
			_old_str = pc.getStr();

			int color = 2;
			if (_old_base_str == 0) {
				_old_base_str = pc.getBaseStr();
			} else if (_old_base_str != pc.getBaseStr()) { // 基礎能力改變
				_old_base_str = pc.getBaseStr();
				color = 16;
			}
			// XXX 7.6 能力基本資訊-力量
			pc.sendPackets(new S_StrDetails(color,
					L1ClassFeature.calcStrDmg(pc.getStr(), pc.getBaseStr()),
					L1ClassFeature.calcStrHit(pc.getStr(), pc.getBaseStr()),
					L1ClassFeature.calcStrDmgCritical(pc.getStr(), pc.getBaseStr()),
					L1ClassFeature.calcAbilityMaxWeight(pc.getStr(), pc.getCon())));

			// XXX 7.6 重量程度資訊
			pc.sendPackets(new S_WeightStatus(pc.getInventory().getWeight100(), pc.getInventory().getWeight(),
					(int) pc.getMaxWeight()));
		}

		if (_old_dex != pc.getDex()) {
			_old_dex = pc.getDex();
			int color = 2;
			if (_old_base_dex == 0) {
				_old_base_dex = pc.getBaseDex();
			} else if (_old_base_dex != pc.getBaseDex()) {
				_old_base_dex = pc.getBaseDex();
				color = 16;
			}
			pc.resetBaseAc();
			// XXX 7.6 ADD
			pc.sendPackets(new S_PacketBoxCharEr(pc));// 角色迴避率更新
			// XXX 7.6 能力基本資訊-敏捷
			pc.sendPackets(new S_DexDetails(color,
					L1ClassFeature.calcDexDmg(pc.getDex(), pc.getBaseDex()),
					L1ClassFeature.calcDexHit(pc.getDex(), pc.getBaseDex()),
					L1ClassFeature.calcDexDmgCritical(pc.getDex(), pc.getBaseDex()),
					L1ClassFeature.calcDexAc(pc.getDex()), L1ClassFeature.calcDexEr(pc.getDex())));
		}

		if (_old_int != pc.getInt()) {
			_old_int = pc.getInt();
			int color = 2;
			if (_old_base_int == 0) {
				_old_base_int = pc.getBaseInt();
			} else if (_old_base_int != pc.getBaseInt()) {
				_old_base_int = pc.getBaseInt();
				color = 16;
			}
			// XXX 7.6 能力基本資訊-智力
			pc.sendPackets(new S_IntDetails(color,
					L1ClassFeature.calcIntMagicDmg(pc.getInt(), pc.getBaseInt()),
					L1ClassFeature.calcIntMagicHit(pc.getInt(), pc.getBaseInt()),
					L1ClassFeature.calcIntMagicCritical(pc.getInt(), pc.getBaseInt()),
					L1ClassFeature.calcIntMagicBonus(pc.getType(), pc.getInt()),
					L1ClassFeature.calcIntMagicConsumeReduction(pc.getInt())));
		}

		if (_old_con != pc.getCon()) {
			_old_con = pc.getCon();
			int color = 2;
			if (_old_base_con == 0) {
				_old_base_con = pc.getBaseCon();
			} else if (_old_base_con != pc.getBaseCon()) {
				_old_base_con = pc.getBaseCon();
				color = 16;
			}
			// XXX 7.6 能力基本資訊-體質
			pc.sendPackets(new S_ConDetails(color,
					L1ClassFeature.calcConHpr(pc.getCon(), pc.getBaseCon()),
					L1ClassFeature.calcConPotionHpr(pc.getCon(), pc.getBaseCon()),
					L1ClassFeature.calcAbilityMaxWeight(pc.getStr(), pc.getCon()),
					L1ClassFeature.calcBaseClassLevUpHpUp(pc.getType()) + L1ClassFeature.calcBaseConLevUpExtraHpUp(pc.getType(), pc.getBaseCon())));

			// XXX 7.6 重量程度資訊
			pc.sendPackets(new S_WeightStatus(pc.getInventory().getWeight() * 100 / (int) pc.getMaxWeight(),
					pc.getInventory().getWeight(), (int) pc.getMaxWeight()));
		}

		if (_old_wis != pc.getWis()) {
			_old_wis = pc.getWis();
			int color = 2;
			if (_old_base_wis == 0) {
				_old_base_wis = pc.getBaseWis();
			} else if (_old_base_wis != pc.getBaseWis()) {
				_old_base_wis = pc.getBaseWis();
				color = 16;
			}
			// XXX 7.6 能力基本資訊-精神
			pc.sendPackets(new S_WisDetails(color,
					L1ClassFeature.calcWisMpr(pc.getWis(), pc.getBaseWis()),
					L1ClassFeature.calcWisPotionMpr(pc.getWis(), pc.getBaseWis()),
					L1ClassFeature.calcStatMr(pc.getWis()) + L1ClassFeature.newClassFeature(pc.getType()).getClassOriginalMr(),
					L1ClassFeature.calcBaseWisLevUpMpUp(pc.getType(), pc.getBaseWis())));
		}
	}

}
