package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_PacketBoxGree;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_TrueTarget;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1ServerQuestMob;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerQuestMobTable {  //src027
 
	private static final Log _log = LogFactory
			.getLog(ServerQuestMobTable.class);

	private static ServerQuestMobTable _instance;

	private static final HashMap<Integer, HashMap<Integer, L1ServerQuestMob>> _questMobList = new HashMap<Integer, HashMap<Integer, L1ServerQuestMob>>();

	public static ServerQuestMobTable get() {
		if (_instance == null) {
			_instance = new ServerQuestMobTable();
		}
		return _instance;
	}

	private ServerQuestMobTable() {
		load();
	}

	private void load() {
		PerformanceTimer timer = new PerformanceTimer();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM `server_quest_mob`");
			rs = pstm.executeQuery();

			while (rs.next()) {
				int quest_id = rs.getInt("quest_id");
		        
				int quest_step = rs.getInt("quest_step");
		        
				String note = rs.getString("note");

				int lv = rs.getInt("lv");
				int npc_gfxid = rs.getInt("npc_gfxid");
				int[] mob_id = getArray(rs.getString("mob_id"));
				int[] mob_count = getArray(rs.getString("mob_count"));
				int[] item_id = getArray(rs.getString("item_id"));
				int[] item_lv = getArray(rs.getString("item_lv"));
				int[] item_count = getArray(rs.getString("item_count"));
				int save_quest_order = rs.getInt("save_quest_step");
				int tele_x = rs.getInt("tele_x");
				int tele_y = rs.getInt("tele_y");
				int tele_m = rs.getInt("tele_m");
				int tele_delay = rs.getInt("tele_delay");

				final L1ServerQuestMob serverQuestMob = new L1ServerQuestMob();

				serverQuestMob.set_quest_id(quest_id);
				serverQuestMob.set_quest_step(quest_step);

				serverQuestMob.set_note(note);
				serverQuestMob.set_lv(lv);
				serverQuestMob.set_npc_gfxid(npc_gfxid);
				serverQuestMob.set_mob_id(mob_id);
				serverQuestMob.set_mob_count(mob_count);
				serverQuestMob.set_item_id(item_id);
				serverQuestMob.set_item_lv(item_lv);
				serverQuestMob.set_item_count(item_count);
				serverQuestMob.set_save_quest_order(save_quest_order);
				serverQuestMob.set_tele_x(tele_x);
				serverQuestMob.set_tele_y(tele_y);
				serverQuestMob.set_tele_m(tele_m);
				serverQuestMob.set_tele_delay(tele_delay);

				HashMap<Integer, L1ServerQuestMob> questMap = (HashMap<Integer, L1ServerQuestMob>) _questMobList
						.get(quest_id);

				if (questMap == null) {
					questMap = new HashMap<Integer, L1ServerQuestMob>();

					questMap.put(quest_step, serverQuestMob);

					_questMobList.put(quest_id, questMap);

				} else {
					questMap.put(quest_step, serverQuestMob);
				}
			}

			_log.info("載入[改寫]狩獵任務設置資料數量: " + _questMobList.size() + "("
					+ timer.get() + "ms)");
		} catch (SQLException e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void getQuestMobNote(final L1PcInstance pc) {

		if (_questMobList == null) {
			return;
		}
		for (final Integer quest_id : _questMobList.keySet()) {
			if (pc.getQuest().isStart(quest_id.intValue())) {
				final HashMap<Integer, L1ServerQuestMob> map = (HashMap<Integer, L1ServerQuestMob>) _questMobList
						.get(quest_id);
				for (Integer quest_order : map.keySet()) {
					if (pc.getQuest().get_step(quest_id.intValue()) == quest_order
							.intValue()) {

						final int[] mob_id = ((L1ServerQuestMob) map
								.get(quest_order)).get_mob_id();
						final int[] mob_count = ((L1ServerQuestMob) map
								.get(quest_order)).get_mob_count();
						final int[] pc_mob_count = pc.getQuest().get_mob_count(
								quest_id.intValue());

						if (mob_id.length == mob_count.length) {

							StringBuffer stringBuffer = new StringBuffer();

							stringBuffer.append(((L1ServerQuestMob) map
									.get(quest_order)).get_note() + "\n");

							for (int i = 0; i < mob_id.length; i++) {
								final String npc_name = NpcTable.get()
										.getTemplate(mob_id[i]).get_name();

								stringBuffer.append(npc_name + "("
										+ pc_mob_count[i] + "/" + mob_count[i]
										+ ")\n");

							}
							pc.sendPackets(new S_ServerMessage(stringBuffer
									.toString(), 11));

						} else if (mob_count.length == 1) {

							pc.sendPackets(new S_ServerMessage(
									((L1ServerQuestMob) map.get(quest_order))
											.get_note()
											+ "\n 該區域任意怪物("
											+ pc_mob_count[0]
											+ "/"
											+ mob_count[0] + ")", 11));
						}
					}
				}
			}
		}
	}

	/**
	 * 判定玩家所接的任務編號，並且讓怪物顯示特效
	 * @param pc
	 * @param npc
	 */
	public synchronized void checkQuestMobGfx(final L1PcInstance pc, L1NpcInstance npc) {
		if (_questMobList == null) {
			return;
		}
		for (final Integer quest_id : _questMobList.keySet()) {
			final HashMap<Integer, L1ServerQuestMob> map = (HashMap<Integer, L1ServerQuestMob>) _questMobList.get(quest_id);
			if (pc.getQuest().isStart(quest_id.intValue())) {
				for (Integer quest_order : map.keySet()) {
					if (((L1ServerQuestMob) map.get(quest_order)).get_npc_gfxid() != 0) {
						final int[] mob_id = ((L1ServerQuestMob) map.get(quest_order)).get_mob_id();
						final int[] mob_count = ((L1ServerQuestMob) map.get(quest_order)).get_mob_count();
						final int[] pc_mob_count = pc.getQuest().get_mob_count(quest_id.intValue());
						for (int i = 0; i < mob_id.length; i++) {
							if (pc_mob_count[i] < mob_count[i]) {
								final L1Npc l1npc = NpcTable.get().getTemplate(mob_id[i]);
								if (l1npc != null) {
									if (npc.getNpcId() == l1npc.get_npcId()) {
										pc.sendPackets(new S_TrueTarget(npc.getId(), ((L1ServerQuestMob) map.get(quest_order)).get_npc_gfxid(), 1));
									}
								}
							}
						}
				    }
				}
			}
		}
	}
	
	public synchronized void checkQuestMob(final L1PcInstance pc,
			final int mobId) {

		if (_questMobList == null) {
			return;
		}

		for (final Integer quest_id : _questMobList.keySet()) {

			final HashMap<Integer, L1ServerQuestMob> questMap = (HashMap<Integer, L1ServerQuestMob>) _questMobList
					.get(quest_id);

			cheakQuest(quest_id.intValue(), mobId, pc, questMap);

			if (pc.getQuest().isStart(quest_id.intValue())) {
				checkMob(quest_id.intValue(), mobId, pc, questMap);
			}

		}
	}

	private void cheakQuest(final int quest_id, final int mobId,
			final L1PcInstance pc,
			final HashMap<Integer, L1ServerQuestMob> questMap) {

		if (!pc.getQuest().isStart(quest_id)) {

			for (final L1ServerQuestMob serverQuestMob : questMap.values()) {
				final int mobid[] = serverQuestMob.get_mob_id();

				if (pc.getLevel() + (pc.getMeteLevel() * 99) < serverQuestMob
						.get_lv()) {
					return;
				}

				if (pc.getQuest().get_step(quest_id) == serverQuestMob
						.get_save_quest_order()) {
					return;
				}

				for (int i = 0; i < serverQuestMob.get_mob_id().length; i++) {
					if (mobid[i] == mobId) {

						pc.getQuest().set_step(quest_id,
								serverQuestMob.get_quest_step());
					}
				}
			}
		}

		if (pc.isInParty()) {
			//final Object[] pcs = pc.getParty().partyUsers().values().toArray();
			final Object[] pcs = pc.getParty().getMemberList().toArray();// 7.6
			if (pcs.length <= 0) {
				return;
			}
			for (Object obj : pcs) {
				if (obj instanceof L1PcInstance) {
					final L1PcInstance tgpc = (L1PcInstance) obj;

					if (pc != tgpc && World.get().getVisibleObjects(pc, tgpc)) {
						if (!tgpc.getQuest().isStart(quest_id)) {
							for (final L1ServerQuestMob serverQuestMob : questMap
									.values()) {

								if (tgpc.getLevel() + (tgpc.getMeteLevel() * 99) < serverQuestMob
										.get_lv()) {
									return;
								}

								if (tgpc.getQuest().get_step(quest_id) == serverQuestMob
										.get_save_quest_order()) {
									return;
								}
								final int mobid[] = serverQuestMob.get_mob_id();
								for (int i = 0; i < serverQuestMob.get_mob_id().length; i++) {
									if (mobid[i] == mobId) {

										tgpc.getQuest()
												.set_step(
														quest_id,
														serverQuestMob
																.get_quest_step());

										if (tgpc.getQuest().isStart(quest_id)) {
											checkMob(quest_id, mobId, tgpc,
													questMap);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void checkMob(final int quest_id, final int mobId,
			final L1PcInstance pc,
			final HashMap<Integer, L1ServerQuestMob> questMap) {

		for (final Integer quest_step : questMap.keySet()) {

			if (pc.getQuest().get_step(quest_id) == quest_step.intValue()) {

				final L1ServerQuestMob quest = (L1ServerQuestMob) questMap
						.get(quest_step);

				if (pc.getLevel() + (pc.getMeteLevel() * 99) < quest.get_lv()) {
					return;
				}

				final int[] mob_id = quest.get_mob_id();
				final int[] mob_count = quest.get_mob_count();
				final int[] pc_mob_count = pc.getQuest()
						.get_mob_count(quest_id);

				int mustMobCountLength = 0;
				boolean mustMobCountOnly = false;

				if (mob_id.length == mob_count.length) {
					for (int i = 0; i < mob_id.length; i++) {
						if ((mobId == mob_id[i])
								&& (pc_mob_count[i] < mob_count[i])) {
							String npc_name = NpcTable.get()
									.getTemplate(mob_id[i]).get_name();
							pc.getQuest().add_mob_count(quest_id, i);

							/*pc.sendPackets(new S_ServerMessage(quest.get_note()
									+ npc_name + "(" + pc_mob_count[i] + "/"
									+ mob_count[i] + ")", 11));*/   //src036
							pc.sendPackets(new S_PacketBoxGree(quest.get_note()
									+ npc_name + "(" + pc_mob_count[i] + "/"
									+ mob_count[i] + ")"));
						}

						if (pc_mob_count[i] == mob_count[i]) {
							mustMobCountLength++;
						}
					}

				} else if (mob_count.length == 1) {

					for (int i = 0; i < mob_id.length; i++) {
						if ((mobId == mob_id[i])

						&& (pc_mob_count[0] < mob_count[0])) {

							pc.getQuest().add_mob_count(quest_id, 0);

							/*pc.sendPackets(new S_ServerMessage(quest.get_note()
									+ "(" + pc_mob_count[0] + "/"
									+ mob_count[0] + ")", 11));*/  //src036
							pc.sendPackets(new S_PacketBoxGree(quest.get_note()
									+ "(" + pc_mob_count[0] + "/"
									+ mob_count[0] + ")"));
						}

						if (pc_mob_count[0] == mob_count[0]) {
							mustMobCountOnly = true;
						}
					}
				}

				if (mustMobCountLength == mob_count.length) {
					pc.sendPackets(new S_ServerMessage("完成了狩獵任務,獎勵物品:", 17));

					final StringBuilder name = new StringBuilder();

					final int[] item_id = quest.get_item_id();
					final int[] item_lv = quest.get_item_lv();
					final int[] item_count = quest.get_item_count();

					for (int i = 0; i < item_id.length; i++) {
						final L1ItemInstance item = ItemTable.get().createItem(
								item_id[i]);
						if (item != null) {
							item.setCount(item_count[i]);
							if (item_lv[i] != 0) {
								item.setEnchantLevel(item_lv[i]);
							}

							if (pc.getInventory().checkAddItem(item,
									item_count[i]) == 0) {
								pc.getInventory().storeItem(item);
							} else {
								item.set_showId(pc.get_showId());

								World.get()
										.getInventory(pc.getX(), pc.getY(),
												pc.getMapId()).storeItem(item);
							}

							if (name.length() != 0) {
								name.append("\n\r");
							}
							name.append(item.getRecordName(item.getCount()));
						} else {
							_log.error("狩獵任務給予物件失敗 原因: 指定編號物品不存在(" + item_id
									+ ")");
						}
					}

					pc.sendPackets(new S_ServerMessage(name.toString(), 11));

					final int step = quest.get_save_quest_order();

					if (step != 0) {
						pc.getQuest().set_mob_count(quest_id,
								pc.getQuest().get_step(quest_id));
						pc.getQuest().set_step(quest_id, step);
					}

					final int x = quest.get_tele_x();
					final int y = quest.get_tele_y();
					final int tomapid = quest.get_tele_m();
					final int delay = quest.get_tele_delay();
					if (x != 0 && y != 0 && tomapid >= 0) {
						if (delay != 0) {
							try {
								Thread.sleep(delay);
							} catch (InterruptedException e) {

								e.printStackTrace();
							}
						}
						L1Teleport.teleport(pc, x, y, (short) tomapid, 5, true);
					}
				}

				if (mustMobCountOnly) {
					pc.sendPackets(new S_ServerMessage("完成了狩獵任務,獎勵物品:", 17));
					final StringBuilder name = new StringBuilder();

					final int[] item_id = quest.get_item_id();
					final int[] item_lv = quest.get_item_lv();
					final int[] item_count = quest.get_item_count();

					for (int i = 0; i < item_id.length; i++) {
						final L1ItemInstance item = ItemTable.get().createItem(
								item_id[i]);
						if (item != null) {
							item.setCount(item_count[i]);
							if (item_lv[i] != 0) {
								item.setEnchantLevel(item_lv[i]);
							}

							if (pc.getInventory().checkAddItem(item,
									item_count[i]) == 0) {
								pc.getInventory().storeItem(item);
							} else {
								item.set_showId(pc.get_showId());

								World.get()
										.getInventory(pc.getX(), pc.getY(),
												pc.getMapId()).storeItem(item);
							}

							if (name.length() != 0) {
								name.append("\n\r");
							}
							name.append(item.getRecordName(item.getCount()));
						} else {
							_log.error("狩獵任務給予物件失敗 原因: 指定編號物品不存在(" + item_id
									+ ")");
						}
					}

					pc.sendPackets(new S_ServerMessage(name.toString(), 11));

					final int step = quest.get_save_quest_order();

					if (step != 0) {
						pc.getQuest().set_mob_count(quest_id,
								pc.getQuest().get_step(quest_id));
						pc.getQuest().set_step(quest_id, step);
					}

					final int x = quest.get_tele_x();
					final int y = quest.get_tele_y();
					final int tomapid = quest.get_tele_m();
					final int delay = quest.get_tele_delay();
					if (x != 0 && y != 0 && tomapid >= 0) {
						if (delay != 0) {
							try {
								Thread.sleep(delay);
							} catch (InterruptedException e) {

								e.printStackTrace();
							}
						}
						L1Teleport.teleport(pc, x, y, (short) tomapid, 5, true);
					}
				}
			}
		}
	}

	private static int[] getArray(String s) {
		StringTokenizer st = new StringTokenizer(s, ",");
		int iSize = st.countTokens();
		String sTemp = null;

		int[] iReturn = new int[iSize];
		for (int i = 0; i < iSize; i++) {
			sTemp = st.nextToken();
			iReturn[i] = Integer.parseInt(sTemp);
		}
		return iReturn;
	}

	public int[] getMobCount(int quest_id, int quest_order) {
		if ((_questMobList.containsKey(Integer.valueOf(quest_id)))
				&& (((HashMap<Integer, L1ServerQuestMob>) _questMobList
						.get(Integer.valueOf(quest_id))).containsKey(Integer
						.valueOf(quest_order)))) {
			int length = ((L1ServerQuestMob) ((HashMap<Integer, L1ServerQuestMob>) _questMobList
					.get(Integer.valueOf(quest_id))).get(Integer
					.valueOf(quest_order))).get_mob_count().length;
			int[] data = new int[length];

			return data;
		}

		return null;
	}

	public Set<Integer> getQuestMobListId() {
		if (_questMobList != null) {
			return _questMobList.keySet();
		}
		return null;
	}
}
