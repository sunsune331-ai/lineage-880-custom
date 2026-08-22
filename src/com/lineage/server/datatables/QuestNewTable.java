package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.protobuf.ByteString;
import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ProtoBuffers;
import com.lineage.server.templates.L1QuestNew;
import com.lineage.server.utils.SQLUtil;

import l1j.server.server.datas.protobuf.PBMessageALL3;
import l1j.server.server.datas.protobuf.PBMessageALL7;

/**
 * 官服任務系統
 */
public class QuestNewTable {

	private static final Log _log = LogFactory.getLog(QuestNewTable.class);

	private static QuestNewTable _instance;

	public static QuestNewTable getInstance() {
		if (_instance == null) {
			_instance = new QuestNewTable();
		}
		return _instance;
	}

	private final HashMap<Integer, L1QuestNew> allList = new HashMap<>();

	private QuestNewTable() {
		final int[] quests = { 256, 257, 258, 259, 260, 261, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282,
				283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 299, 306, 314, 317, 318, 319, 320, 321, 322,
				323, 324, 325, 326, 328, 334, 336, 338, 340, 341, 342, 343, 346, 349 };

		loadCharQuestNewTable(); // 刪除遺失玩家資料

		// for (final int i : quests) {
		for (final int i : QuestNewSetTable.getInstance().getAllList().keySet()) {
			final L1QuestNew qn = new L1QuestNew(i);
			allList.put(i, qn);
		}
	}

	private void loadCharQuestNewTable() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_quests_new");
			rs = pstm.executeQuery();
			while (rs.next()) {
				final int objid = rs.getInt("objid");

				if (CharObjidTable.get().isChar(objid) == null) {
					// 刪除遺失玩家資料
					delete(objid);
					// _log.info(">>>>>>>>刪除character_quests_new遺失玩家資料記錄，objid:" + objid);
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 * 刪除遺失玩家資料
	 * @param objid
	 */
	private static void delete(final int objid) {
		Connection cn = null;
		PreparedStatement ps = null;
		try {
			cn = DatabaseFactory.get().getConnection();
			ps = cn.prepareStatement("DELETE FROM `character_quests_new` WHERE `objid`=?");
			ps.setInt(1, objid);
			ps.execute();

		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(ps);
			SQLUtil.close(cn);
		}
	}

	public void updateQuest(final L1PcInstance pc) {
		for (final int i : allList.keySet()) {
			if (pc.getQuestList().containsKey(i)) {
				continue;
			}
			final L1QuestNew qn = allList.get(i);

			if (pc.getLevel() >= qn.getMinQuestLevel() && pc.getLevel() <= qn.getMaxQuestLevel()) {
			//	if (qn.getMapid() == -1 || pc.getMapId() == qn.getMapid()) {

					if (!qn.getRequireClassType().equalsIgnoreCase("A")) { // 非全職
						if (!qn.getRequireClassType().equalsIgnoreCase(pc.getClassFeature().getClassToken())) {
							continue;
						}
					}

					// 每日任務另外判斷
					// if (qn.getId() >= 305 && qn.getId() <= 314) {
					// if (pc.getQuest().get_step(qn.getId()) == L1PcQuest.QUEST_END) {
					// continue;
					// }
					// }
					if (qn.getId() == 305) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew1) == L1PcQuest.QUEST_END) {
							continue;
						}
					}
					if (qn.getId() == 306) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew2) == L1PcQuest.QUEST_END) {
							continue;
						}
					}
					if (qn.getId() == 307) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew3) == L1PcQuest.QUEST_END) {
							continue;
						}
					}
					if (qn.getId() == 308) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew4) == L1PcQuest.QUEST_END) {
							continue;
						}
					}
					if (qn.getId() == 309) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew5) == L1PcQuest.QUEST_END) {
							continue;
						}
					}
					if (qn.getId() == 310) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew6) == L1PcQuest.QUEST_END) {
							continue;
						}
					}
					if (qn.getId() == 311) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew7) == L1PcQuest.QUEST_END) {
							continue;
						}
					}
					if (qn.getId() == 312) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew8) == L1PcQuest.QUEST_END) {
							continue;
						}
					}
					if (qn.getId() == 313) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew9) == L1PcQuest.QUEST_END) {
							continue;
						}
					}
					if (qn.getId() == 314) {
						if (pc.getQuest().get_step(L1PcQuest.QuestNew10) == L1PcQuest.QUEST_END) {
							continue;
						}
					}

					final L1QuestNew quest = new L1QuestNew(i);
					quest.setOwner(pc);
					if (quest.get達到等級() > 0) {
						quest.set目前等級(pc.getLevel());
					}
					pc.getQuestList().put(i, quest);
					pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.QUEST_BEGIN, quest));
				}
		//	}
		}
	}

	public void giveQuest(final L1PcInstance pc) {
		load(pc);
		updateQuest(pc);
	}

	private void load(final L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_quests_new WHERE objid=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			if (rs.next()) {
				final byte[] data = rs.getBytes("data");
				final PBMessageALL3.type8 msg = PBMessageALL3.type8.parseFrom(data);

				// System.out.println("load " + msg.getArray1List().size());
				for (final ByteString bs : msg.getArray1List()) {
					final PBMessageALL7.type28 msg2 = PBMessageALL7.type28.parseFrom(bs);
					final L1QuestNew qn = new L1QuestNew(msg2.getValue1());

					qn.setOwner(pc);
					qn.setQuestEnd(msg2.getValue2() == 1);
					qn.setQuestComplete(msg2.getValue3() == 1);
					qn.set目前等級(msg2.getValue4());

					final int[] npcCount = new int[msg2.getValue5Count()];
					if (npcCount.length > 0) {
						for (int i = 0; i < npcCount.length; i++) {
							npcCount[i] = msg2.getValue5(i);
						}
						qn.set目前獵殺怪物數量(npcCount);
					}

					final int[] itemCount = new int[msg2.getValue6Count()];
					if (itemCount.length > 0) {
						for (int i = 0; i < itemCount.length; i++) {
							itemCount[i] = msg2.getValue6(i);
						}
						qn.set目前獲得道具數量(itemCount);
					}
					final int[] itemUseCount = new int[msg2.getValue7Count()];
					if (itemUseCount.length > 0) {
						for (int i = 0; i < itemUseCount.length; i++) {
							itemUseCount[i] = msg2.getValue7(i);
						}
						qn.set目前使用道具數量(itemUseCount);
					}
					pc.getQuestList().put(qn.getId(), qn);

					if (!qn.isQuestEnd()) {
						//if (qn.getMapid() == -1 || pc.getMapId() == qn.getMapid()) {
							pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.QUEST_BEGIN, qn));
					//	}
					}
				}

			} else {
				insert(pc);
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void insert(final L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_quests_new SET objid=?, data=?");
			pstm.setInt(1, pc.getId());

			pstm.setBytes(2, getByteArray(pc));
			pstm.execute();
		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private byte[] getByteArray(final L1PcInstance pc) {
		final PBMessageALL3.type8.Builder build = PBMessageALL3.type8.newBuilder();
		for (final L1QuestNew qn : pc.getQuestList().values()) {
			final PBMessageALL7.type28.Builder builder = PBMessageALL7.type28.newBuilder();
			// 每日任務另外判斷
			if (qn.getId() >= 305 && qn.getId() <= 314) {
				continue;
			}
			builder.setValue1(qn.getId());
			builder.setValue2(qn.isQuestEnd() ? 1 : 0);
			builder.setValue3(qn.isQuestComplete() ? 1 : 0);
			builder.setValue4(qn.get目前等級());
			for (final int i : qn.get目前獵殺怪物數量()) {
				builder.addValue5(i);
			}
			for (final int i : qn.get目前獲得道具數量()) {
				builder.addValue6(i);
			}

			for (final int i : qn.get目前使用道具數量()) {
				builder.addValue7(i);
			}
			build.addArray1(builder.build().toByteString());
		}
		return build.build().toByteArray();
	}

	public void save(final L1PcInstance pc) {

		// character_quests_new
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("UPDATE character_quests_new SET data=? WHERE objid=?");

			pstm.setBytes(1, getByteArray(pc));
			pstm.setInt(2, pc.getId());
			pstm.execute();
		} catch (final SQLException e) {
			_log.error(e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public HashMap<Integer, L1QuestNew> getAllList() {
		return allList;
	}
}
