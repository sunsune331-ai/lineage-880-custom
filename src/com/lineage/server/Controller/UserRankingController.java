package com.lineage.server.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_UserRankSystem;
import com.lineage.server.templates.L1UserRanking;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 新排行系統
 */
public class UserRankingController implements Runnable {

	private static Logger _log = Logger.getLogger(UserRankingController.class.getName());

	private static UserRankingController _instance;

	public static int RankingItemId = 700001; // 最強的祝福

	public static boolean RankingGm = false; // 設true的話GM不顯示在排行，設false的話GM顯示在排行

	public static boolean isRenewal = false;

	public static UserRankingController getInstance() {
		if (_instance == null) {
			_instance = new UserRankingController();
		}
		return _instance;
	}

	private static ArrayList<L1UserRanking> list = null;
	private static ArrayList<L1UserRanking> listPrince = null;
	private static ArrayList<L1UserRanking> listKnight = null;
	private static ArrayList<L1UserRanking> listElf = null;
	private static ArrayList<L1UserRanking> listWizard = null;
	private static ArrayList<L1UserRanking> listDarkElf = null;
	private static ArrayList<L1UserRanking> listDragonKnight = null;
	private static ArrayList<L1UserRanking> listIllusionist = null;
	private static ArrayList<L1UserRanking> listWarrior = null;

	public UserRankingController() {
		list = new ArrayList<L1UserRanking>();
		listPrince = new ArrayList<L1UserRanking>();
		listKnight = new ArrayList<L1UserRanking>();
		listElf = new ArrayList<L1UserRanking>();
		listWizard = new ArrayList<L1UserRanking>();
		listDarkElf = new ArrayList<L1UserRanking>();
		listDragonKnight = new ArrayList<L1UserRanking>();
		listIllusionist = new ArrayList<L1UserRanking>();
		listWarrior = new ArrayList<L1UserRanking>();
		load();
		GeneralThreadPool.get().schedule(this, 1000);
	}

	@Override
	public void run() {
		try {

			if (isRenewal) {
				isRenewal = false;

				load();

				for (L1PcInstance pc : World.get().getAllPlayers()) {
					int star = getStarCount(pc.getName());

					if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_11)) { // 1-10名
						if (star != 11) {
							pc.getInventory().consumeItem(RankingItemId, 1);
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_11);
							setStatBuff(pc, -1);
							pc.addPvpDmg(-2); // 增加PVP傷害
							pc.addPvpDmg_R(-2); // 減免PVP傷害
							pc.addMaxHp(-200);
							pc.addAc(3);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_11, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_10)) { // 11名-20名
						if (star != 10) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_10);
							pc.addPvpDmg(-2); // 增加PVP傷害
							pc.addPvpDmg_R(-2); // 減免PVP傷害
							pc.addMaxHp(-200);
							pc.addAc(3);
							setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_10, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_9)) { // 21名-40名
						if (star != 9) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_9);
							pc.addPvpDmg(-2); // 增加PVP傷害
							pc.addPvpDmg_R(-2); // 減免PVP傷害
							pc.addMaxHp(-200);
							pc.addAc(3);
							// setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_9, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_8)) { // 41名-60名
						if (star != 8) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_8);
							pc.addPvpDmg_R(-2); // 減免PVP傷害
							pc.addMaxHp(-200);
							pc.addAc(3);
							// setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_8, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_7)) { // 61名-80名
						if (star != 7) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_7);
							pc.addPvpDmg_R(-1); // 減免PVP傷害
							pc.addMaxHp(-200);
							pc.addAc(3);
							// setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_7, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_6)) { // 81名-100名
						if (star != 6) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_6);
							pc.addPvpDmg_R(-1); // 減免PVP傷害
							pc.addMaxHp(-200);
							pc.addAc(2);
							// setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_6, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_5)) { // 101名-120名
						if (star != 5) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_5);
							pc.addMaxHp(-200);
							pc.addAc(1);

							// setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_5, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_4)) { // 121名-140名
						if (star != 4) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_4);
							pc.addMaxHp(-200);

							// setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_4, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_3)) { // 141名-160名
						if (star != 3) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_3);
							pc.addMaxHp(-100);

							// setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_3, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_2)) { // 161名-180名
						if (star != 2) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_2);
							pc.addMaxHp(-50);

							// setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_2, false, pc.getType(), 0));
						}

					} else if (pc.hasSkillEffect(L1SkillId.RANKING_BUFF_1)) { // 180名-200名
						if (star != 1) {
							pc.killSkillEffectTimer(L1SkillId.RANKING_BUFF_1);

							// setStatBuff(pc, -1);
							pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_1, false, pc.getType(), 0));
						}
					}

					if (star != 0) {
						if (!pc.hasSkillEffect(L1SkillId.RANKING_BUFF_10)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_11)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_9)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_8)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_7)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_6)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_5)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_4)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_3)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_2)
								&& !pc.hasSkillEffect(L1SkillId.RANKING_BUFF_1)) {
							setBuffSetting(pc);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.get().schedule(this, 1000);
	}

	public ArrayList<L1UserRanking> getList(int classId) {
		synchronized (list) {
			if (classId == 8)
				return list;
			else if (classId == 0)
				return listPrince;
			else if (classId == 1)
				return listKnight;
			else if (classId == 2)
				return listElf;
			else if (classId == 3)
				return listWizard;
			else if (classId == 4)
				return listDarkElf;
			else if (classId == 5)
				return listDragonKnight;
			else if (classId == 6)
				return listIllusionist;
			else if (classId == 7)
				return listWarrior;
			return null;
		}
	}

	public L1UserRanking getTotalRank(String name) {
		synchronized (list) {
			for (L1UserRanking user : list) {
				if (user.getName().equalsIgnoreCase(name))
					return user;
			}

			return null;
		}
	}

	public L1UserRanking getClassRank(int classId, String name) {
		for (L1UserRanking rank : getList(classId)) {
			if (rank.getName().equalsIgnoreCase(name)) {
				return rank;
			}
		}
		return null;
	}

	public int getStarCount(String name) {
		L1UserRanking rank = getTotalRank(name);
		if (rank != null) {
			int curRank = rank.getCurRank();

			if (curRank >= 1 && curRank <= 10) {
				return 11;
			} else if (curRank >= 11 && curRank <= 20) {
				return 10;
			} else if (curRank >= 21 && curRank <= 40) {
				return 9;
			} else if (curRank >= 41 && curRank <= 60) {
				return 8;
			} else if (curRank >= 61 && curRank <= 80) {
				return 7;
			} else if (curRank >= 81 && curRank <= 100) {
				return 6;
			} else if (curRank >= 101 && curRank <= 120) {
				return 5;
			} else if (curRank >= 121 && curRank <= 140) {
				return 4;
			} else if (curRank >= 141 && curRank <= 160) {
				return 3;
			} else if (curRank >= 161 && curRank <= 180) {
				return 2;
			} else if (curRank >= 181 && curRank <= 200) {
				return 1;
			}
		}
		return 0;
	}

	public void setBuffSetting(L1PcInstance pc) {

		L1UserRanking rank = getTotalRank(pc.getName());
		if (rank != null) {
			int curRank = rank.getCurRank();

			if (curRank >= 1 && curRank <= 10) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_11, 0);

				if (!pc.getInventory().checkItem(RankingItemId)) {
					pc.getInventory().storeItem(RankingItemId, 1);
				}
				pc.addPvpDmg(2); // 增加PVP傷害
				pc.addPvpDmg_R(2); // 減免PVP傷害
				pc.addAc(-3);
				pc.addMaxHp(200);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_11, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 11 && curRank <= 20) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_10, 0);
				pc.addPvpDmg(2); // 增加PVP傷害
				pc.addPvpDmg_R(2); // 減免PVP傷害
				pc.addAc(-3);
				pc.addMaxHp(200);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_10, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 21 && curRank <= 40) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_9, 0);
				pc.addPvpDmg(2); // 增加PVP傷害
				pc.addPvpDmg_R(2); // 減免PVP傷害
				pc.addAc(-3);
				pc.addMaxHp(200);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				// setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_9, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 41 && curRank <= 60) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_8, 0);
				pc.addPvpDmg_R(2); // 減免PVP傷害
				pc.addAc(-3);
				pc.addMaxHp(200);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				// setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_8, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 61 && curRank <= 80) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_7, 0);
				pc.addPvpDmg_R(1); // 減免PVP傷害
				pc.addAc(-3);
				pc.addMaxHp(200);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				// setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_7, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 81 && curRank <= 100) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_6, 0);
				pc.addPvpDmg_R(1); // 減免PVP傷害
				pc.addAc(-2);
				pc.addMaxHp(200);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				// setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_6, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 101 && curRank <= 120) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_5, 0);

				pc.addAc(-1);
				pc.addMaxHp(200);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				// setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_5, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 121 && curRank <= 140) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_4, 0);
				// pc.addAc(-3);

				pc.addMaxHp(200);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				// setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_4, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 141 && curRank <= 160) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_3, 0);
				// pc.addAc(-3);

				pc.addMaxHp(100);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				// setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_3, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 161 && curRank <= 180) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_2, 0);
				// pc.addAc(-3);

				pc.addMaxHp(50);
				pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				// setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_2, true, pc.getType(), -1));
				pc.sendPackets(new S_OwnCharStatus(pc));

			} else if (curRank >= 181 && curRank <= 200) {
				pc.setSkillEffect(L1SkillId.RANKING_BUFF_1, 0);

				// pc.addAc(-3);
				// pc.addMaxHp(200);
				// pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				// setStatBuff(pc, 1);
				pc.sendPackets(new S_UserRankSystem(L1SkillId.RANKING_BUFF_1, true, pc.getType(), -1));
				//pc.sendPackets(new S_OwnCharStatus(pc));
			}
		}

		if (!pc.hasSkillEffect(L1SkillId.RANKING_BUFF_11)) {
			pc.getInventory().consumeItem(RankingItemId, 1);
		}

	}

	public void setStatBuff(L1PcInstance pc, int flag) {
		if (pc.isCrown() || pc.isKnight() || pc.isDarkelf() || pc.isDragonKnight() || pc.isWarrior()) {
			pc.addStr(1 * flag);

		} else if (pc.isElf()) {
			pc.addDex(1 * flag);

		} else if (pc.isWizard() || pc.isIllusionist()) {
			pc.addInt(1 * flag);
		}
	}

	private void load() {

		ArrayList<L1UserRanking> templist = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistPrince = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistKnight = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistElf = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistWizard = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistDarkElf = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistDragonKnight = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistIllusionist = new ArrayList<L1UserRanking>();
		ArrayList<L1UserRanking> templistWarrior = new ArrayList<L1UserRanking>();

		for (int a = 0; a < 9; a++) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			int i = 0;
			try {
				con = DatabaseFactory.get().getConnection();
				if (a == 8) {
					// RankingGm 設true的話GM不顯示在排行，設false的話GM顯示在排行
					if (RankingGm) {
						pstm = con.prepareStatement(
								"SELECT char_name, Type FROM characters WHERE level >= 1 AND AccessLevel = 0 order by Exp desc limit 200");
					} else {
						pstm = con.prepareStatement(
								"SELECT char_name, Type FROM characters WHERE level >= 1 order by Exp desc limit 200");
					}
				} else {
					if (RankingGm) {
						pstm = con.prepareStatement("SELECT char_name, Type FROM characters WHERE Type = " + a
								+ " AND level >= 1 AND AccessLevel = 0 order by Exp desc limit 200");
					} else {
						pstm = con.prepareStatement("SELECT char_name, Type FROM characters WHERE Type = " + a
								+ " AND level >= 1 order by Exp desc limit 200");
					}
				}
				rs = pstm.executeQuery();

				while (rs.next()) {
					String name = rs.getString("char_name");
					int type = rs.getInt("Type");

					L1UserRanking rank = new L1UserRanking();

					rank.setName(name);
					rank.setCurRank(++i);

					L1UserRanking oldRank = null;
					if (a == 8) {
						oldRank = getTotalRank(name);
					} else {
						oldRank = getClassRank(a, name);
					}

					if (oldRank == null) {
						rank.setOldRank(rank.getCurRank());
					} else {
						rank.setOldRank(oldRank.getCurRank());
					}

					rank.setClassId(type);

					if (a == 8) {
						templist.add(rank);
					} else if (a == 0) {
						templistPrince.add(rank);
					} else if (a == 1) {
						templistKnight.add(rank);
					} else if (a == 2) {
						templistElf.add(rank);
					} else if (a == 3) {
						templistWizard.add(rank);
					} else if (a == 4) {
						templistDarkElf.add(rank);
					} else if (a == 5) {
						templistDragonKnight.add(rank);
					} else if (a == 6) {
						templistIllusionist.add(rank);
					} else if (a == 7) {
						templistWarrior.add(rank);
					}
				}

			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}

		synchronized (list) {
			list.clear();
			listPrince.clear();
			listKnight.clear();
			listElf.clear();
			listWizard.clear();
			listDarkElf.clear();
			listDragonKnight.clear();
			listIllusionist.clear();
			listWarrior.clear();

			list.addAll(templist);
			listPrince.addAll(templistPrince);
			listKnight.addAll(templistKnight);
			listElf.addAll(templistElf);
			listWizard.addAll(templistWizard);
			listDarkElf.addAll(templistDarkElf);
			listDragonKnight.addAll(templistDragonKnight);
			listIllusionist.addAll(templistIllusionist);
			listWarrior.addAll(templistWarrior);
		}
	}
}
