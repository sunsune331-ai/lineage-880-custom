package com.lineage.server.storage.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.CharOtherReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.storage.CharacterStorage;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.utils.SQLUtil;

public class MySqlCharacterStorage implements CharacterStorage {//src013
	private static final Log _log = LogFactory.getLog(MySqlCharacterStorage.class);

	public L1PcInstance loadCharacter(String charName) {
		L1PcInstance pc = null;
		Connection con = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm2 = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, charName);

			rs = pstm.executeQuery();
			if (!rs.next()) {
				return null;
			}
			pc = new L1PcInstance();
			String loginName = rs.getString("account_name").toLowerCase();
			pc.setAccountName(loginName);

			int objid = rs.getInt("objid");
			pc.setId(objid);

			pc.set_showId(-1);

			L1PcOther other = CharOtherReading.get().getOther(pc);
			if (other == null) {
				other = new L1PcOther();
				other.set_objid(objid);
			}
			pc.set_other(other);

			pc.setName(rs.getString("char_name"));
			pc.setHighLevel(rs.getInt("HighLevel"));
			pc.setExp(rs.getLong("Exp"));
			pc.addBaseMaxHp(rs.getShort("MaxHp"));
			short currentHp = rs.getShort("CurHp");
			if (currentHp < 1) {
				currentHp = 1;
			}
			pc.setDead(false);
			pc.setCurrentHpDirect(currentHp);
			pc.setStatus(0);
			pc.addBaseMaxMp(rs.getShort("MaxMp"));
			pc.setCurrentMpDirect(rs.getShort("CurMp"));
			pc.addBaseStr(rs.getInt("Str"));
			pc.addBaseCon(rs.getInt("Con"));
			pc.addBaseDex(rs.getInt("Dex"));
			pc.addBaseCha(rs.getInt("Cha"));
			pc.addBaseInt(rs.getInt("Intel"));
			pc.addBaseWis(rs.getInt("Wis"));
			int status = rs.getInt("Status");
			pc.setCurrentWeapon(status);
			int classId = rs.getInt("Class");
			pc.setClassId(classId);
			pc.setTempCharGfx(classId);
			pc.setGfxId(classId);
			pc.set_sex(rs.getInt("Sex"));
			pc.setType(rs.getInt("Type"));
			int head = rs.getInt("Heading");
			if (head > 7) {
				head = 0;
			}
			pc.setHeading(head);

			pc.setX(rs.getInt("locX"));
			pc.setY(rs.getInt("locY"));
			pc.setMap(rs.getShort("MapID"));
			pc.set_food(rs.getInt("Food"));
			pc.setLawful(rs.getInt("Lawful"));
			pc.setTitle(rs.getString("Title"));
			pc.setClanid(rs.getInt("ClanID"));
			pc.setClanname(rs.getString("Clanname"));
			pc.setClanRank(rs.getInt("ClanRank"));
			pc.setRejoinClanTime(rs.getTimestamp("rejoin_clan_time"));
			pc.setBonusStats(rs.getInt("BonusStatus"));
			pc.setElixirStats(rs.getInt("ElixirStatus"));
			pc.setElfAttr(rs.getInt("ElfAttr"));
			pc.setElfAttrResetCount(rs.getInt("ElfAttrResetCount"));
			pc.set_PKcount(rs.getInt("PKcount"));
			pc.setPkCountForElf(rs.getInt("PkCountForElf"));
			pc.setExpRes(rs.getInt("ExpRes"));
			pc.setPartnerId(rs.getInt("PartnerID"));
			pc.setAccessLevel(rs.getShort("AccessLevel"));

			if (pc.getAccessLevel() >= 200) {
				pc.setGm(true);
				pc.setMonitor(false);
			} else if (pc.getAccessLevel() == 100) {
				pc.setGm(false);
				pc.setMonitor(true);
			} else {
				pc.setGm(false);
				pc.setMonitor(false);
			}

			pc.setOnlineStatus(rs.getInt("OnlineStatus"));
			pc.setHomeTownId(rs.getInt("HomeTownID"));
			pc.setContribution(rs.getInt("Contribution"));
			pc.setPay(rs.getInt("Pay"));
			pc.setHellTime(rs.getInt("HellTime"));
			pc.setBanned(rs.getBoolean("Banned"));
			pc.setKarma(rs.getInt("Karma"));
			pc.setLastPk(rs.getTimestamp("LastPk"));
			pc.setLastPkForElf(rs.getTimestamp("LastPkForElf"));
			pc.setDeleteTime(rs.getTimestamp("DeleteTime"));
			pc.setOriginalStr(rs.getInt("OriginalStr"));
			pc.setOriginalCon(rs.getInt("OriginalCon"));
			pc.setOriginalDex(rs.getInt("OriginalDex"));
			pc.setOriginalCha(rs.getInt("OriginalCha"));
			pc.setOriginalInt(rs.getInt("OriginalInt"));
			pc.setOriginalWis(rs.getInt("OriginalWis"));
			//pc.setCreateTime(rs.getTimestamp("CreateTime"));
			pc.setBirthday(rs.getString("CreateTime"));// 7.6
			pc.setClanMemberNotes(rs.getString("ClanMemberNotes"));// 7.6血盟個人備註
			pc.setMeteLevel(rs.getInt("MeteLevel"));
            pc.setTurnLifeSkillCount(rs.getByte("ReincarnationSkillCount")); // 轉生天賦
			pc.setPunishTime(rs.getTimestamp("PunishTime"));
			pc.setBanError(rs.getInt("BanError"));
			pc.setInputBanError(rs.getInt("InputBanError"));
			pc.setSpeedError(rs.getInt("SpeedError"));

			pc.setRocksPrisonTime(rs.getInt("RocksPrisonTime"));
			pc.setIvoryTowerTime(rs.getInt("IvorytowerTime"));
			pc.setLastabardTime(rs.getInt("LastabardTime"));
			pc.setDragonValleyTime(rs.getInt("DragonValleyTime"));
			pc.settimemap1(rs.getInt("timemap1"));
			pc.settimemap2(rs.getInt("timemap2"));
			pc.settimemap3(rs.getInt("timemap3"));
			pc.settimemap4(rs.getInt("timemap4"));
			pc.setMazuTime(rs.getInt("MazuTime"));
			pc.setAITimer(rs.getInt("AI_TIMES")); // 特效驗證系統
            pc.setTamTime(rs.getTimestamp("TamEndTime")); // 成長果實系統(Tam幣)
            pc.setMark_count(rs.getInt("Mark_Count")); // 日版記憶座標
			pc.setOnlineGiftIndex(rs.getInt("OnlineGiftIndex"));
			pc.setOnlineGiftWiatEnd(rs.getBoolean("OnlineGiftWiatEnd"));
			pc.setVipStartTime(rs.getTimestamp("VipStartTime"));
			pc.setVipEndTime(rs.getTimestamp("VipEndTime"));
			pc.set_vipLevel(rs.getInt("VipLevel"));
			//額外重置能力
			pc.setOtherStats(rs.getInt("OtherStatus"));
			pc.setAddPoint(rs.getInt("AddPoint"));
			pc.setDelPoint(rs.getInt("DelPoint"));
			pc.setRingsExpansion(rs.getByte("RingsExpansion"));
			pc.setEarringsExpansion(rs.getByte("EarringsExpansion"));
			pc.setEquipmentIndexAmulet(rs.getByte("EquipmentIndexAmulet")); // src1003
			pc.sethuizhangkz(rs.getByte("huizhangkz")); // src1003
			pc.setjianjiakz(rs.getByte("jianjiakz")); // src1003
			pc.setRedblueReward(rs.getByte("RedblueReward"));
			
			rs.close();

			pc.refresh();
			pc.setMoveSpeed(0);
			pc.setBraveSpeed(0);
			pc.setGmInvis(false);

			if (pc.getClanid() > 0) {
				pstm2 = con.prepareStatement("SELECT * FROM clan_members WHERE char_id=?");
				pstm2.setInt(1, pc.getId());
				rs = pstm2.executeQuery();
				if (!rs.next()) {
					return null;
				}
				pc.setClanMemberId(rs.getInt("index_id"));
				pc.setClanMemberNotes(rs.getString("notes"));
			}

		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
			return null;
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		SQLUtil.close(rs);
		SQLUtil.close(pstm);
		SQLUtil.close(con);

		return pc;
	}

	public void createCharacter(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int i = 0;
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement(
					"INSERT INTO characters SET account_name=?,objid=?,char_name=?,level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,Con=?,Dex=?,Cha=?,Intel=?,Wis=?,Status=?,Class=?,Sex=?,Type=?,Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?,rejoin_clan_time=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,ElfAttrResetCount=?,PKcount=?,PkCountForElf=?,ExpRes=?,PartnerID=?,AccessLevel=?,OnlineStatus=?,HomeTownID=?,Contribution=?,Pay=?,HellTime=?,Banned=?,Karma=?,LastPk=?,LastPkForElf=?,DeleteTime=?,CreateTime=?,ClanMemberNotes=?,MeteLevel=?,ReincarnationSkillCount=?,PunishTime=?,BanError=?,InputBanError=?,SpeedError=?,RocksPrisonTime=?,IvorytowerTime=?,LastabardTime=?,DragonValleyTime=?,timemap1=?,timemap2=?,timemap3=?,timemap4=?,MazuTime=?,AI_TIMES=?,TamEndTime=?,Mark_Count=? ");

			pstm.setString(++i, pc.getAccountName());
			pstm.setInt(++i, pc.getId());
			pstm.setString(++i, pc.getName());
			pstm.setInt(++i, pc.getLevel());
			pstm.setInt(++i, pc.getHighLevel());
			pstm.setLong(++i, pc.getExp());
			pstm.setInt(++i, pc.getBaseMaxHp());
			int hp = pc.getCurrentHp();
			if (hp < 1) {
				hp = 1;
			}
			pstm.setInt(++i, hp);
			pstm.setInt(++i, pc.getBaseMaxMp());
			pstm.setInt(++i, pc.getCurrentMp());
			pstm.setInt(++i, pc.getAc());
			pstm.setInt(++i, pc.getBaseStr());
			pstm.setInt(++i, pc.getBaseCon());
			pstm.setInt(++i, pc.getBaseDex());
			pstm.setInt(++i, pc.getBaseCha());
			pstm.setInt(++i, pc.getBaseInt());
			pstm.setInt(++i, pc.getBaseWis());
			pstm.setInt(++i, pc.getCurrentWeapon());
			pstm.setInt(++i, pc.getClassId());
			pstm.setInt(++i, pc.get_sex());
			pstm.setInt(++i, pc.getType());
			pstm.setInt(++i, pc.getHeading());
			pstm.setInt(++i, pc.getX());
			pstm.setInt(++i, pc.getY());
			pstm.setInt(++i, pc.getMapId());
			pstm.setInt(++i, pc.get_food());
			pstm.setInt(++i, pc.getLawful());
			pstm.setString(++i, pc.getTitle());
			pstm.setInt(++i, pc.getClanid());
			pstm.setString(++i, pc.getClanname());
			pstm.setInt(++i, pc.getClanRank());
			pstm.setTimestamp(++i, pc.getRejoinClanTime());
			pstm.setInt(++i, pc.getBonusStats());
			pstm.setInt(++i, pc.getElixirStats());
			pstm.setInt(++i, pc.getElfAttr());
			pstm.setInt(++i, pc.getElfAttrResetCount());
			pstm.setInt(++i, pc.get_PKcount());
			pstm.setInt(++i, pc.getPkCountForElf());
			pstm.setInt(++i, pc.getExpRes());
			pstm.setInt(++i, pc.getPartnerId());
			short accesslevel = pc.getAccessLevel();
			if (accesslevel > 200) {
				accesslevel = 0;
			}
			pstm.setShort(++i, accesslevel);
			pstm.setInt(++i, pc.getOnlineStatus());
			pstm.setInt(++i, pc.getHomeTownId());
			pstm.setInt(++i, pc.getContribution());
			pstm.setInt(++i, pc.getPay());
			pstm.setInt(++i, pc.getHellTime());
			pstm.setBoolean(++i, pc.isBanned());
			pstm.setInt(++i, pc.getKarma());
			pstm.setTimestamp(++i, pc.getLastPk());
			pstm.setTimestamp(++i, pc.getLastPkForElf());
			pstm.setTimestamp(++i, pc.getDeleteTime());

			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			final String times = sdf.format(System.currentTimeMillis());
			int time = Integer.parseInt(times.replace("-", ""));
			pstm.setInt(++i, time);

			pstm.setString(++i, pc.getClanMemberNotes());// 7.6血盟個人備註
			pstm.setInt(++i, pc.getMeteLevel());
            pstm.setInt(++i, pc.getTurnLifeSkillCount()); // 轉生天賦
			pstm.setTimestamp(++i, pc.getPunishTime());
			pstm.setInt(++i, pc.getBanError());
			pstm.setInt(++i, pc.getInputBanError());
			pstm.setInt(++i, pc.getSpeedError());

			pstm.setInt(++i, pc.getRocksPrisonTime());
			pstm.setInt(++i, pc.getIvoryTowerTime());
			pstm.setInt(++i, pc.getLastabardTime());
			pstm.setInt(++i, pc.getDragonValleyTime());
			pstm.setInt(++i, pc.gettimemap1());
			pstm.setInt(++i, pc.gettimemap2());
			pstm.setInt(++i, pc.gettimemap3());
			pstm.setInt(++i, pc.gettimemap4());
			pstm.setInt(++i, pc.getMazuTime());
			pstm.setInt(++i, pc.getAITimer()); // 特效驗證系統
            pstm.setTimestamp(++i, pc.getTamTime()); // 成長果實系統(Tam幣)
            pstm.setInt(++i, pc.getMark_count()); // 日版記憶座標

			pstm.execute();
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@SuppressWarnings("resource")
	public void deleteCharacter(String accountName, String charName) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE account_name=? AND char_name=?");
			pstm.setString(1, accountName);
			pstm.setString(2, charName);
			rs = pstm.executeQuery();

			if (!rs.next()) {
				throw new RuntimeException("could not delete character");
			}

			int objid = CharObjidTable.get().charObjid(charName);

			if (objid != 0) {
				CharItemsReading.get().delUserItems(Integer.valueOf(objid));
			}

			pstm = con.prepareStatement(
					"DELETE FROM character_buddys WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
			pstm.setString(1, charName);
			pstm.execute();

			pstm = con.prepareStatement(
					"DELETE FROM character_buff WHERE char_obj_id IN (SELECT objid FROM characters WHERE char_name = ?)");
			pstm.setString(1, charName);
			pstm.execute();

			pstm = con.prepareStatement(
					"DELETE FROM character_config WHERE object_id IN (SELECT objid FROM characters WHERE char_name = ?)");
			pstm.setString(1, charName);
			pstm.execute();

			pstm = con.prepareStatement(
					"DELETE FROM character_quests WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
			pstm.setString(1, charName);
			pstm.execute();

			pstm = con.prepareStatement(
					"DELETE FROM character_skills WHERE char_obj_id IN (SELECT objid FROM characters WHERE char_name = ?)");
			pstm.setString(1, charName);
			pstm.execute();

			pstm = con.prepareStatement(
					"DELETE FROM character_teleport WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
			pstm.setString(1, charName);
			pstm.execute();

			pstm = con.prepareStatement("DELETE FROM characters WHERE char_name=?");
			pstm.setString(1, charName);
			pstm.execute();

			pstm = con.prepareStatement(
					"DELETE FROM clan_members WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
			pstm.setString(1, charName);
			pstm.execute();

		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	/* 更新VIP*/
	public void updateVipTime(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int i = 0;
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement(
					"UPDATE characters SET VipStartTime=?,VipEndTime=?,VipLevel=? WHERE objid=?");

			
			pstm.setTimestamp(++i, pc.getVipStartTime());
			pstm.setTimestamp(++i, pc.getVipEndTime());
			pstm.setInt(++i, pc.get_vipLevel());
			pstm.setInt(++i, pc.getId());
			pstm.execute();
			_log.info(pc.getName()+"----------VIP更新成功--------");
		} catch (SQLException e) {
			_log.error(pc.getName()+"----------VIP更新錯誤---------"+e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
	}
	public void storeCharacter(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			int i = 0;
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement(
					"UPDATE characters SET level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,Con=?,Dex=?,Cha=?,Intel=?,Wis=?,Status=?,Class=?,Sex=?,Type=?,Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?,rejoin_clan_time=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,ElfAttrResetCount=?,PKcount=?,PkCountForElf=?,ExpRes=?,PartnerID=?,AccessLevel=?,OnlineStatus=?,HomeTownID=?,Contribution=?,HellTime=?,Banned=?,Karma=?,LastPk=?,LastPkForElf=?,DeleteTime=?,ClanMemberNotes=?,MeteLevel=?,ReincarnationSkillCount=?,PunishTime=?,BanError=?,InputBanError=?,SpeedError=?,RocksPrisonTime=?,IvorytowerTime=?,LastabardTime=?,DragonValleyTime=?,timemap1=?,timemap2=?,timemap3=?,timemap4=?,MazuTime=?,AI_TIMES=?,TamEndTime=?,Mark_Count=?,OnlineGiftIndex=?,OnlineGiftWiatEnd=?,OtherStatus=?,AddPoint=?,DelPoint=? WHERE objid=?");

			pstm.setInt(++i, pc.getLevel());
			pstm.setInt(++i, pc.getHighLevel());
			pstm.setLong(++i, pc.getExp());
			pstm.setInt(++i, pc.getBaseMaxHp());
			int hp = pc.getCurrentHp();
			if (hp < 1) {
				hp = 1;
			}
			pstm.setInt(++i, hp);
			pstm.setInt(++i, pc.getBaseMaxMp());
			pstm.setInt(++i, pc.getCurrentMp());
			pstm.setInt(++i, pc.getAc());
			pstm.setInt(++i, pc.getBaseStr());
			pstm.setInt(++i, pc.getBaseCon());
			pstm.setInt(++i, pc.getBaseDex());
			pstm.setInt(++i, pc.getBaseCha());
			pstm.setInt(++i, pc.getBaseInt());
			pstm.setInt(++i, pc.getBaseWis());
			pstm.setInt(++i, pc.getCurrentWeapon());
			pstm.setInt(++i, pc.getClassId());
			pstm.setInt(++i, pc.get_sex());
			pstm.setInt(++i, pc.getType());
			pstm.setInt(++i, pc.getHeading());
			pstm.setInt(++i, pc.getX());
			pstm.setInt(++i, pc.getY());
			pstm.setInt(++i, pc.getMapId());
			pstm.setInt(++i, pc.get_food());
			pstm.setInt(++i, pc.getLawful());
			pstm.setString(++i, pc.getTitle());
			pstm.setInt(++i, pc.getClanid());
			pstm.setString(++i, pc.getClanname());
			pstm.setInt(++i, pc.getClanRank());
			pstm.setTimestamp(++i, pc.getRejoinClanTime());
			pstm.setInt(++i, pc.getBonusStats());
			pstm.setInt(++i, pc.getElixirStats());
			pstm.setInt(++i, pc.getElfAttr());
			pstm.setInt(++i, pc.getElfAttrResetCount());
			pstm.setInt(++i, pc.get_PKcount());
			pstm.setInt(++i, pc.getPkCountForElf());
			pstm.setInt(++i, pc.getExpRes());
			pstm.setInt(++i, pc.getPartnerId());
			short accesslevel = pc.getAccessLevel();
			if (accesslevel > 200) {
				accesslevel = 0;
			}
			pstm.setShort(++i, accesslevel);
			pstm.setInt(++i, pc.getOnlineStatus());
			pstm.setInt(++i, pc.getHomeTownId());
			pstm.setInt(++i, pc.getContribution());
			pstm.setInt(++i, pc.getHellTime());
			pstm.setBoolean(++i, pc.isBanned());
			pstm.setInt(++i, pc.getKarma());
			pstm.setTimestamp(++i, pc.getLastPk());
			pstm.setTimestamp(++i, pc.getLastPkForElf());
			pstm.setTimestamp(++i, pc.getDeleteTime());
			// System.out.println("存儲備註：" + pc.getClanMemberNotes());
			pstm.setString(++i, pc.getClanMemberNotes());// 7.6血盟個人備註
			pstm.setInt(++i, pc.getMeteLevel());
            pstm.setInt(++i, pc.getTurnLifeSkillCount()); // 轉生天賦
			pstm.setTimestamp(++i, pc.getPunishTime());
			pstm.setInt(++i, pc.getBanError());
			pstm.setInt(++i, pc.getInputBanError());
			pstm.setInt(++i, pc.getSpeedError());

			pstm.setInt(++i, pc.getRocksPrisonTime());
			pstm.setInt(++i, pc.getIvoryTowerTime());
			pstm.setInt(++i, pc.getLastabardTime());
			pstm.setInt(++i, pc.getDragonValleyTime());
			pstm.setInt(++i, pc.gettimemap1());
			pstm.setInt(++i, pc.gettimemap2());
			pstm.setInt(++i, pc.gettimemap3());
			pstm.setInt(++i, pc.gettimemap4());
			pstm.setInt(++i, pc.getMazuTime());
			pstm.setInt(++i, pc.getAITimer()); // 特效驗證系統
            pstm.setTimestamp(++i, pc.getTamTime()); // 成長果實系統(Tam幣)
            pstm.setInt(++i, pc.getMark_count()); // 日版記憶座標
			pstm.setInt(++i, pc.getOnlineGiftIndex());
			pstm.setBoolean(++i, pc.isOnlineGiftWiatEnd());
			
			pstm.setInt(++i, pc.getOtherStats());
			pstm.setInt(++i, pc.getAddPoint());
			pstm.setInt(++i, pc.getDelPoint());

			pstm.setInt(++i, pc.getId());
			pstm.execute();
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

}
