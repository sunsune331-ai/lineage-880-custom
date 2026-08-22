package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SkinInstance;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.templates.L1ItemVIP;
import com.lineage.server.timecontroller.pc.VIPGfxTimer;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;

/**
 * 道具Vip系統
 * 
 * */
public class ItemVIPTable {

	private static final Log _log = LogFactory.getLog(ItemVIPTable.class);
	private static final Map<Integer, L1ItemVIP> _VIPList = new HashMap<Integer, L1ItemVIP>();
	private static ItemVIPTable _instance;

	public static ItemVIPTable get() {
		if (_instance == null) {
			_instance = new ItemVIPTable();
		}
		return _instance;
	}

	private ItemVIPTable() {
		load();
	}

	private void load() {
		final PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `etcitem_vip`");
		    rs = pstm.executeQuery();
			//for (rs = pstm.executeQuery(); rs.next(); _VIPList.put(Integer.valueOf(item_id), vip)) {
			while (rs.next()) {
				final int item_id = rs.getInt("item_id");
				final int type = rs.getInt("type");
				final int gif = rs.getInt("gif"); // 魔法武器傷害
				final int gif_time = rs.getInt("gif_time"); // 魔法武器發動機率
				final int add_wmd = rs.getInt("add_wmd"); // 魔法武器傷害
				final int add_wmc = rs.getInt("add_wmc"); // 魔法武器發動機率
				final int add_str = rs.getInt("add_str"); // 力量
				final int add_dex = rs.getInt("add_dex"); // 敏捷
				final int add_con = rs.getInt("add_con"); // 體質
				final int add_int = rs.getInt("add_int"); // 智力
				final int add_wis = rs.getInt("add_wis"); // 精神
				final int add_cha = rs.getInt("add_cha"); // 魅力
				final int add_ac = rs.getInt("add_ac"); // 防禦
				final int add_hp = rs.getInt("add_hp"); // 最大血量
				final int add_mp = rs.getInt("add_mp"); // 最大魔量
				final int add_hpr = rs.getInt("add_hpr"); // 回寫速度
				final int add_mpr = rs.getInt("add_mpr"); // 回魔速度
				final int add_dmg = rs.getInt("add_dmg"); // 增加近戰傷害
				final int add_hit = rs.getInt("add_hit"); // 增加近戰命中率
				final int add_bow_dmg = rs.getInt("add_bow_dmg"); // 增加遠攻傷害
				final int add_bow_hit = rs.getInt("add_bow_hit"); // 增加遠攻命中率
				final int add_dmg_r = rs.getInt("add_dmg_r"); // 增加物理傷害減免
				final int add_magic_r = rs.getInt("add_magic_r"); // 增加魔法傷害減免
				final int add_mr = rs.getInt("add_mr"); // 增加抗魔
				final int add_sp = rs.getInt("add_sp"); // 增加魔法攻擊
				final int add_fire = rs.getInt("add_fire"); // 增加火屬性
				final int add_wind = rs.getInt("add_wind"); // 增加封屬性
				final int add_earth = rs.getInt("add_earth"); // 增加地屬性
				final int add_water = rs.getInt("add_water"); // 增加水屬性
				// final int add_stun = rs.getInt("add_stun"); // 增加耐昏迷
				// final int add_stone = rs.getInt("add_stone"); // 增加耐石化
				// final int add_sleep = rs.getInt("add_sleep"); // 增加耐睡眠
				// final int add_freeze = rs.getInt("add_freeze"); // 增加耐冰
				// final int add_sustain = rs.getInt("add_sustain"); // 增加耐支撐
				// final int add_blind = rs.getInt("add_blind"); // 增加耐暗黑
				final int add_exp = rs.getInt("add_exp"); // 增加經驗率 2 = 2倍
				final int add_adena = rs.getInt("add_gf"); // 增加打到的金幣率
				int skin_id = 0;
				// if (DiabloItemSet.START)
				// add_mf = rs.getInt("add_mf");
				// 不知道作用暫時現這樣處理
				skin_id = rs.getInt("skin_id");

				final boolean death_exp = rs.getBoolean("death_exp"); // 死亡不噴經驗
				final boolean death_item = rs.getBoolean("death_item"); // 死亡不噴道具
				final boolean death_skill = rs.getBoolean("death_skill"); // 死亡不噴技能
				final boolean death_score = rs.getBoolean("death_score"); // 死亡不噴積分
				final L1ItemVIP vip = new L1ItemVIP();
				vip.set_type(type);
				vip.set_gif(gif);
				vip.set_gif_time(gif_time);
				vip.set_add_wmd(add_wmd);
				vip.set_add_wmc(add_wmc);
				vip.set_add_str(add_str);
				vip.set_add_dex(add_dex);
				vip.set_add_con(add_con);
				vip.set_add_int(add_int);
				vip.set_add_wis(add_wis);
				vip.set_add_cha(add_cha);
				vip.set_add_ac(add_ac);
				vip.set_add_hp(add_hp);
				vip.set_add_mp(add_mp);
				vip.set_add_hpr(add_hpr);
				vip.set_add_mpr(add_mpr);
				vip.set_add_dmg(add_dmg);
				vip.set_add_hit(add_hit);
				vip.set_add_bow_dmg(add_bow_dmg);
				vip.set_add_bow_hit(add_bow_hit);
				vip.set_add_dmg_r(add_dmg_r);
				vip.set_add_magic_r(add_magic_r);
				vip.set_add_mr(add_mr);
				vip.set_add_sp(add_sp);
				vip.set_add_fire(add_fire);
				vip.set_add_wind(add_wind);
				vip.set_add_earth(add_earth);
				vip.set_add_water(add_water);
				// vip.set_add_stun(add_stun);
				// vip.set_add_stone(add_stone);
				// vip.set_add_sleep(add_sleep);
				// vip.set_add_freeze(add_freeze);
				// vip.set_add_sustain(add_sustain);
				// vip.set_add_blind(add_blind);
				vip.set_add_exp(add_exp);
				// if (DiabloItemSet.START)
				// vip.set_add_mf(add_mf);
				// 不知道作用暫時這樣處理
				vip.set_skin_id(skin_id);

				vip.set_add_adena(add_adena);
				vip.set_death_exp(death_exp);
				vip.set_death_item(death_item);
				vip.set_death_skill(death_skill);
				vip.set_death_score(death_score);
		        _VIPList.put(item_id, vip);
			}

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
			//_log.info((new StringBuilder("載入VIP道具加值數量: ")).append(_VIPList.size()).append("(").append(timer.get()).append("ms)").toString());
		}
		_log.info("載入VIP道具加值數量: " + _VIPList.size() + "(" + timer.get() + "ms)");
	}

	public L1ItemVIP getVIP(final int item_id) {
		if (_VIPList.isEmpty()) {
			return null;
		}
		if (_VIPList.containsKey(item_id)) {
			return _VIPList.get(item_id);
		}
		return null;
	}

	public boolean checkVIP(final int item_id) {
		return _VIPList.containsKey(item_id);
	}

	/**
	 * 增加效果
	 * 
	 * */
	public void addItemVIP(final L1PcInstance pc, final int item_id) {
		if (_VIPList.isEmpty()) {
			return;
		}

		if (!_VIPList.containsKey(item_id)) {
			return;
		}

		final L1ItemVIP vip = _VIPList.get(item_id);

		final boolean status = false;
		boolean status2 = false;
		boolean spmr = false;
		boolean attr = false;
		final int add_wmd = vip.get_add_wmd();
		if (add_wmd != 0) {
			pc.addweaponMD(add_wmd);
			status2 = true;
		}
		final int add_wmc = vip.get_add_wmc();
		if (add_wmc != 0) {
			pc.addweaponMDC(add_wmc);
			status2 = true;
		}
		final int add_str = vip.get_add_str();
		if (add_str != 0) {
			pc.addStr(add_str);
			status2 = true;
		}
		final int add_dex = vip.get_add_dex();
		if (add_dex != 0) {
			pc.addDex(add_dex);
			status2 = true;
		}
		final int add_con = vip.get_add_con();
		if (add_con != 0) {
			pc.addCon(add_con);
			status2 = true;
		}
		final int add_int = vip.get_add_int();
		if (add_int != 0) {
			pc.addInt(add_int);
			status2 = true;
		}
		final int add_wis = vip.get_add_wis();
		if (add_wis != 0) {
			pc.addWis(add_wis);
			status2 = true;
		}
		final int add_cha = vip.get_add_cha();
		if (add_cha != 0) {
			pc.addCha(add_cha);
			status2 = true;
		}
		final int add_ac = vip.get_add_ac();
		if (add_ac != 0) {
			pc.addAc(-add_ac);
			attr = true;
		}
		final int add_hp = vip.get_add_hp();
		if (add_hp != 0) {
			pc.addMaxHp(add_hp);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty())
				pc.getParty().updateMiniHP(pc);
		}
		final int add_mp = vip.get_add_mp();
		if (add_mp != 0) {
			pc.addMaxMp(add_mp);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		}
		final int add_hpr = vip.get_add_hpr();
		if (add_hpr != 0)
			pc.addHpr(add_hpr);
		final int add_mpr = vip.get_add_mpr();
		if (add_mpr != 0)
			pc.addMpr(add_mpr);
		final int add_dmg = vip.get_add_dmg();
		if (add_dmg != 0)
			pc.addDmgup(add_dmg);
		final int add_hit = vip.get_add_hit();
		if (add_hit != 0)
			pc.addHitup(add_hit);
		final int add_bow_dmg = vip.get_add_bow_dmg();
		if (add_bow_dmg != 0)
			pc.addBowDmgup(add_bow_dmg);
		final int add_bow_hit = vip.get_add_bow_hit();
		if (add_bow_hit != 0)
			pc.addBowHitup(add_bow_hit);
		final int add_dmg_r = vip.get_add_dmg_r();
		if (add_dmg_r != 0)
			pc.add_reduction_dmg(add_dmg_r);
		final int add_magic_r = vip.get_add_magic_r();
		if (add_magic_r != 0)
			pc.add_magic_reduction_dmg(add_magic_r);
		final int add_mr = vip.get_add_mr();
		if (add_mr != 0) {
			pc.addMr(add_mr);
			spmr = true;
		}
		final int add_sp = vip.get_add_sp();
		if (add_sp != 0) {
			pc.addSp(add_sp);
			spmr = true;
		}
		final int add_fire = vip.get_add_fire();
		if (add_fire != 0) {
			pc.addFire(add_fire);
			attr = true;
		}
		final int add_wind = vip.get_add_wind();
		if (add_wind != 0) {
			pc.addWind(add_wind);
			attr = true;
		}
		final int add_earth = vip.get_add_earth();
		if (add_earth != 0) {
			pc.addEarth(add_earth);
			attr = true;
		}
		final int add_water = vip.get_add_water();
		if (add_water != 0) {
			pc.addWater(add_water);
			attr = true;
		}
		// final int add_stun = vip.get_add_stun();
		// if (add_stun != 0)
		// pc.addRegistStun(add_stun);
		// final int add_stone = vip.get_add_stone();
		// if (add_stone != 0)
		// pc.addRegistStone(add_stone);
		// final int add_sleep = vip.get_add_sleep();
		// if (add_sleep != 0)
		// pc.addRegistSleep(add_sleep);
		// final int add_freeze = vip.get_add_freeze();
		// if (add_freeze != 0)
		// pc.add_regist_freeze(add_freeze);
		// final int add_sustain = vip.get_add_sustain();
		// if (add_sustain != 0)
		// pc.addRegistSustain(add_sustain);
		// final int add_blind = vip.get_add_blind();
		// if (add_blind != 0)
		// pc.addRegistBlind(add_blind);
		if (vip.get_skin_id() != 0) {
			final L1SkinInstance skin = L1SpawnUtil.spawnSkin(pc, vip.get_skin_id());
			if (skin != null) {
				skin.setMoveType(1);
				pc.addSkin(skin, vip.get_skin_id());
			}
		}

		final int add_gif = vip.get_gif();
		final int add_gif_time = vip.get_gif_time();
		if (add_gif != 0 && add_gif_time != 0) {
			// pc.set_vip_gfx(add_gif);
			// pc.set_vip_time(add_gif_time);
			pc.set_gfx(add_gif);
			pc.set_time(add_gif_time);
			VIPGfxTimer.addMember(pc);
		}

		final boolean death_exp = vip.get_death_exp();
		if (death_exp)
			pc.set_death_exp(true);
		final boolean death_item = vip.get_death_item();
		if (death_item)
			pc.set_death_item(true);
		final boolean death_skill = vip.get_death_skill();
		if (death_skill)
			pc.set_death_skill(true);
		final boolean death_score = vip.get_death_score();
		if (death_score)
			pc.set_death_score(true);
		final int add_exp = vip.get_add_exp();
		if (add_exp != 0) { // 經驗%數
			pc.addExpRateToPc(add_exp);// src013
		}

		final int add_adena = vip.get_add_adena();
		if (add_adena != 0) {
			pc.addGF(add_adena);
		}

		if (status) {
			pc.sendPackets(new S_OwnCharStatus(pc));
		} else {
			if (status2)
				pc.sendDetails();
			pc.sendPackets(new S_OwnCharStatus2(pc));
			if (attr)
				pc.sendPackets(new S_OwnCharAttrDef(pc));
		}
		if (spmr)
			pc.sendPackets(new S_SPMR(pc));
	}

	/**
	 * 移除效果
	 * 
	 * */
	public void deleItemVIP(final L1PcInstance pc, final int item_id) {
		if (!_VIPList.containsKey(item_id)) {
			return;
		}

		final L1ItemVIP vip = _VIPList.get(item_id);
		
		final boolean status = false;
		boolean status2 = false;
		boolean spmr = false;
		boolean attr = false;
		final int add_wmd = vip.get_add_wmd();
		if (add_wmd != 0) {
			pc.addweaponMD(-add_wmd);
			status2 = true;
		}
		final int add_wmc = vip.get_add_wmc();
		if (add_wmc != 0) {
			pc.addweaponMDC(-add_wmc);
			status2 = true;
		}
		final int add_str = vip.get_add_str();
		if (add_str != 0) {
			pc.addStr(-add_str);
			status2 = true;
		}

		final int add_dex = vip.get_add_dex();
		if (add_dex != 0) {
			pc.addDex(-add_dex);
			status2 = true;
		}
		final int add_con = vip.get_add_con();
		if (add_con != 0) {
			pc.addCon(-add_con);
			status2 = true;
		}
		final int add_int = vip.get_add_int();
		if (add_int != 0) {
			pc.addInt(-add_int);
			status2 = true;
		}
		final int add_wis = vip.get_add_wis();
		if (add_wis != 0) {
			pc.addWis(-add_wis);
			status2 = true;
		}
		final int add_cha = vip.get_add_cha();
		if (add_cha != 0) {
			pc.addCha(-add_cha);
			status2 = true;
		}
		final int add_ac = vip.get_add_ac();
		if (add_ac != 0) {
			pc.addAc(add_ac);
			attr = true;
		}
		final int add_hp = vip.get_add_hp();
		if (add_hp != 0) {
			pc.addMaxHp(-add_hp);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty())
				pc.getParty().updateMiniHP(pc);
		}
		final int add_mp = vip.get_add_mp();
		if (add_mp != 0) {
			pc.addMaxMp(-add_mp);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
		}
		final int add_hpr = vip.get_add_hpr();
		if (add_hpr != 0)
			pc.addHpr(-add_hpr);
		final int add_mpr = vip.get_add_mpr();
		if (add_mpr != 0)
			pc.addMpr(-add_mpr);
		final int add_dmg = vip.get_add_dmg();
		if (add_dmg != 0)
			pc.addDmgup(-add_dmg);
		final int add_hit = vip.get_add_hit();
		if (add_hit != 0)
			pc.addHitup(-add_hit);
		final int add_bow_dmg = vip.get_add_bow_dmg();
		if (add_bow_dmg != 0)
			pc.addBowDmgup(-add_bow_dmg);
		final int add_bow_hit = vip.get_add_bow_hit();
		if (add_bow_hit != 0)
			pc.addBowHitup(-add_bow_hit);
		final int add_dmg_r = vip.get_add_dmg_r();
		if (add_dmg_r != 0)
			pc.add_reduction_dmg(-add_dmg_r);
		final int add_magic_r = vip.get_add_magic_r();
		if (add_magic_r != 0)
			pc.add_magic_reduction_dmg(-add_magic_r);
		final int add_mr = vip.get_add_mr();
		if (add_mr != 0) {
			pc.addMr(-add_mr);
			spmr = true;
		}
		final int add_sp = vip.get_add_sp();
		if (add_sp != 0) {
			pc.addSp(-add_sp);
			spmr = true;
		}
		final int add_fire = vip.get_add_fire();
		if (add_fire != 0) {
			pc.addFire(-add_fire);
			attr = true;
		}
		final int add_wind = vip.get_add_wind();
		if (add_wind != 0) {
			pc.addWind(-add_wind);
			attr = true;
		}
		final int add_earth = vip.get_add_earth();
		if (add_earth != 0) {
			pc.addEarth(-add_earth);
			attr = true;
		}
		final int add_water = vip.get_add_water();
		if (add_water != 0) {
			pc.addWater(-add_water);
			attr = true;
		}
		// final int add_stun = vip.get_add_stun();
		// if (add_stun != 0)
		// pc.addRegistStun(-add_stun);
		// final int add_stone = vip.get_add_stone();
		// if (add_stone != 0)
		// pc.addRegistStone(-add_stone);
		// final int add_sleep = vip.get_add_sleep();
		// if (add_sleep != 0)
		// pc.addRegistSleep(-add_sleep);
		// final int add_freeze = vip.get_add_freeze();
		// if (add_freeze != 0)
		// pc.add_regist_freeze(-add_freeze);
		// final int add_sustain = vip.get_add_sustain();
		// if (add_sustain != 0)
		// pc.addRegistSustain(-add_sustain);
		// final int add_blind = vip.get_add_blind();
		// if (add_blind != 0)
		// pc.addRegistBlind(-add_blind);
		if (vip.get_skin_id() != 0 && pc.getSkin(vip.get_skin_id()) != null) {
			pc.getSkin(vip.get_skin_id()).deleteMe();
			pc.removeSkin(vip.get_skin_id());
		}

		final int add_gif = vip.get_gif();
		final int add_gif_time = vip.get_gif_time();
		if (add_gif != 0 && add_gif_time != 0) {
			// pc.set_vip_gfx(0);
			// pc.set_vip_time(0);
			pc.set_gfx(0);
			pc.set_time(0);
			VIPGfxTimer.removeMember(pc);
		}

		final boolean death_exp = vip.get_death_exp();
		if (death_exp)
			pc.set_death_exp(false);
		final boolean death_item = vip.get_death_item();
		if (death_item)
			pc.set_death_item(false);
		final boolean death_skill = vip.get_death_skill();
		if (death_skill)
			pc.set_death_skill(false);

		final boolean death_score = vip.get_death_score();
		if (death_score)
			pc.set_death_score(false);

		final int add_exp = vip.get_add_exp();
		if (add_exp != 0) { // 經驗%數
			pc.addExpRateToPc(-add_exp); // src013
		}

		final int add_adena = vip.get_add_adena();
		if (add_adena != 0) {
			pc.addGF(-add_adena);
		}

		if (status) {
			pc.sendPackets(new S_OwnCharStatus(pc));
		} else {
			if (status2)
				pc.sendDetails();
			pc.sendPackets(new S_OwnCharStatus2(pc));
			if (attr)
				pc.sendPackets(new S_OwnCharAttrDef(pc));
		}
		if (spmr)
			pc.sendPackets(new S_SPMR(pc));
	}

}
