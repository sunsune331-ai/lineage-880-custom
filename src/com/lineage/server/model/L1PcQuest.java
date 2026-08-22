package com.lineage.server.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.event.QuestMobSet;
import com.lineage.server.datatables.ServerQuestMobTable;
import com.lineage.server.datatables.lock.CharacterQuestReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.CharQuest;

public class L1PcQuest {

	private static final Log _log = LogFactory.getLog(L1PcQuest.class);

	public static final int QUEST_OILSKINMANT = 11;
	public static final int QUEST_DOROMOND = 20;
	public static final int QUEST_RUBA = 21;
	public static final int QUEST_AREX = 22;
	public static final int QUEST_LUKEIN1 = 23;
	public static final int QUEST_TBOX1 = 24;
	public static final int QUEST_TBOX2 = 25;
	public static final int QUEST_TBOX3 = 26;
	public static final int QUEST_SIMIZZ = 27;
	public static final int QUEST_DOIL = 28;
	public static final int QUEST_RUDIAN = 29;
	public static final int QUEST_RESTA = 30;
	public static final int QUEST_CADMUS = 31;
	public static final int QUEST_KAMYLA = 32;
	public static final int QUEST_CRYSTAL = 33;
	public static final int QUEST_LIZARD = 34;
	public static final int QUEST_KEPLISHA = 35;
	public static final int QUEST_DESIRE = 36;
	public static final int QUEST_SHADOWS = 37;
	public static final int QUEST_TOSCROLL = 39;
	public static final int QUEST_MOONOFLONGBOW = 40;
	public static final int QUEST_GENERALHAMELOFRESENTMENT = 41;

	public static final int QUEST_NOT = 0; // 任務尚未開始
	public static final int QUEST_START = 1; // src035 任務已經開始,未結束
	public static final int QUEST_END = 255; // 任務已經結束

	/** 新手輔助 */
	public static final int QUEST_TUTOR = 300;
	public static final int QUEST_TUTOR2 = 304;

	/** 結婚系統 */
	public static final int QUEST_MARRY = 74;
	/** 擴充 */
	public static final int QUEST_SLOT59 = 81; // Lv59 耳環
	public static final int QUEST_SLOT76 = 79; // Lv76 戒指
	public static final int QUEST_SLOT81 = 80; // Lv81 戒指
	public static final int QUEST_BOOKMARK = 82; // 最大記點數量

	public static final int BAO_QUEST_1 = 2001;

	public static final int BAO_QUEST_2 = 2002;

	public static final int BAO_QUEST_3 = 2003;

	public static final int BAO_QUEST_4 = 2004;

	public static final int BAO_QUEST_5 = 2005;

	public static final int Mazu_Use = 30000;

	/** 火窟副本 **/
	public static final int QUEST_HAMO = 63;

	/** 馬賓 **/
	public static final int QUEST_MARBIN = 64;

	/** 潘朵拉商城消費總額給予VIP-1 **/
	public static final int QUEST_GIVE_VIP_1 = 65;

	/** 潘朵拉商城消費總額給予VIP-2 **/
	public static final int QUEST_GIVE_VIP_2 = 66;

	/** 潘朵拉商城消費總額給予VIP-3 **/
	public static final int QUEST_GIVE_VIP_3 = 67;

	/** 潘朵拉商城消費總額給予VIP-4 **/
	public static final int QUEST_GIVE_VIP_4 = 68;

	/** 潘朵拉商城消費總額給予VIP-5 **/
	public static final int QUEST_GIVE_VIP_5 = 69;

	// 官服任務系統
	// 每日任務另外判斷
	public static final int QuestNew1 = 83;
	public static final int QuestNew2 = 84;
	public static final int QuestNew3 = 85;
	public static final int QuestNew4 = 86;
	public static final int QuestNew5 = 87;
	public static final int QuestNew6 = 88;
	public static final int QuestNew7 = 89;
	public static final int QuestNew8 = 90;
	public static final int QuestNew9 = 91;
	public static final int QuestNew10 = 92;

	private L1PcInstance _owner = null;

	private Map<Integer, CharQuest> _quest = null;

	public L1PcQuest(final L1PcInstance owner) {
		this._owner = owner;
	}

	public L1PcInstance get_owner() {
		return this._owner;
	}

	/**
	 * 傳回任務進度
	 * 
	 * @param quest_id
	 *            任務編號
	 * @return 進度
	 */
	public int get_step(int quest_id) {
		try {

			if (_quest != null) {//

				CharQuest step = _quest.get(new Integer(quest_id));
				if (step == null) {
					return 0;

				} else {
					return step.get_quest_step();
				}
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return 0;
	}

	/**
	 * 建立/更新 任務資料
	 * 
	 * @param quest_id
	 *            任務編號
	 * @param step
	 *            進度
	 */
	public void set_step(int quest_id, int step) {
		try {
			CharQuest quest = (CharQuest) this._quest.get(new Integer(quest_id));

			if (quest == null) {
				quest = new CharQuest();
				quest.set_quest_step(step);

				if (QuestMobSet.START) {
					quest.set_mob_count(ServerQuestMobTable.get().getMobCount(quest_id, step));
				} else {
					quest.set_mob_count(null);
				}
				CharacterQuestReading.get().storeQuest(this._owner.getId(), quest_id, quest);
			} else {
				quest.set_quest_step(step);

				CharacterQuestReading.get().updateQuest(this._owner.getId(), quest_id, quest);
			}

			this._quest.put(new Integer(quest_id), quest);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 結束任務
	 * 
	 * @param quest_id
	 *            任務編號
	 */
	public void set_end(final int quest_id) {
		try {

			CharQuest quest = (CharQuest) this._quest.get(new Integer(quest_id));
			if (quest != null) {
				quest.set_quest_step(QUEST_END);
				CharacterQuestReading.get().updateQuest(this._owner.getId(), quest_id, quest);
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 該任務是否開始 (get_step 大於0 小於255 傳回任務已經開始)
	 * 
	 * @param quest_id
	 *            任務編號
	 * @return true:已經開始 false:尚未開始
	 */
	public boolean isStart(final int quest_id) {
		try {
			final int step = this.get_step(quest_id);
			// 大於0 小於255
			if (step > QUEST_NOT && step < QUEST_END) {
				return true;
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return false;
	}

	/**
	 * 該任務是否結束
	 * 
	 * @param quest_id
	 *            任務編號
	 * @return true:已經結束 false:尚未結束
	 */
	public boolean isEnd(final int quest_id) {
		try {
			if (this.get_step(quest_id) == QUEST_END) {
				return true;
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return false;
	}

	/**
	 * 取回人物任務紀錄
	 */
	public void load() {
		try {

			_quest = CharacterQuestReading.get().get(this._owner.getId());
			if (this._quest == null) {
				_quest = new HashMap<Integer, CharQuest>();
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void set_step(final int quest_id, final int step, final int clear) {
		try {
			CharQuest quest = (CharQuest) this._quest.get(new Integer(quest_id));

			if (quest == null) {
				quest = new CharQuest();
				quest.set_quest_step(step);
				if (QuestMobSet.START) {
					quest.set_mob_count(ServerQuestMobTable.get().getMobCount(quest_id, step));
				} else {
					quest.set_mob_count(null);
				}
				CharacterQuestReading.get().storeQuest(this._owner.getId(), quest_id, quest, clear);
			} else {
				quest.set_quest_step(step);
				CharacterQuestReading.get().updateQuest(this._owner.getId(), quest_id, quest);
			}

			this._quest.put(new Integer(quest_id), quest);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public int[] get_mob_count(int quest_id) {
		return ((CharQuest) this._quest.get(new Integer(quest_id))).get_mob_count();
	}

	public void add_mob_count(int quest_id, int nob) {
		CharQuest quest = (CharQuest) this._quest.get(new Integer(quest_id));
		if (quest != null) {
			quest.add_mob_count(nob);
			CharacterQuestReading.get().updateQuest(this._owner.getId(), quest_id, quest);
		}
	}

	public void set_mob_count(int quest_id, int step) {
		CharQuest quest = (CharQuest) this._quest.get(new Integer(quest_id));
		if (quest != null) {
			quest.set_mob_count(ServerQuestMobTable.get().getMobCount(quest_id, step));
			CharacterQuestReading.get().updateQuest(this._owner.getId(), quest_id, quest);
		}
	}
}
