package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.AWAKEN_ANTHARAS;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_FAFURION;
import static com.lineage.server.model.skill.L1SkillId.AWAKEN_VALAKAS;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import william.ReincarnationSkill;

import com.lineage.config.Config;
import com.lineage.config.ConfigNew;
import com.lineage.config.ConfigOther;
import com.lineage.data.QuestClass;
import com.lineage.server.command.executor.L1ToPC;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.PolyTable;
import com.lineage.server.datatables.QuestTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.lock.CharacterQuestReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.templates.L1Quest;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.server.ServerRestartTimer;
import com.lineage.server.utils.CalcStat;

/**
 * 對話命令來自PC的執行與判斷
 * 
 * @author daien
 *
 */
public class L1ActionPc {

	private static final Log _log = LogFactory.getLog(L1ActionPc.class);

	private final L1PcInstance _pc;

	/**
	 * 對話命令來自PC的執行與判斷
	 * 
	 * @param pc
	 *            執行者
	 */
	public L1ActionPc(final L1PcInstance pc) {
		_pc = pc;
	}

	/**
	 * 傳回執行命令者
	 * 
	 * @return
	 */
	public L1PcInstance get_pc() {
		return _pc;
	}

	/**
	 * 選單命令執行
	 * 
	 * @param cmd
	 * @param amount
	 */
	public void action(final String cmd, final long amount) {
		try {

		    // 轉生天賦
            if (cmd.equalsIgnoreCase("rei_1")) {
                if (_pc.getReincarnationSkill()[0] >= ConfigOther.ReiPointLv_1) {
                    _pc.sendPackets(new S_SystemMessage("此轉生技能已達最大上限等級"+ConfigOther.ReiPointLv_1+"，請選擇其它轉生技能。"));
                    return;
                }
                ReincarnationSkill.getInstance().addCheck(_pc, 0);
                return;
            }
            if (cmd.equalsIgnoreCase("rei_2")) {
                if (_pc.getReincarnationSkill()[1] >= ConfigOther.ReiPointLv_2) {
                    _pc.sendPackets(new S_SystemMessage("此轉生技能已達最大上限等級"+ConfigOther.ReiPointLv_2+"，請選擇其它轉生技能。"));
                    return;
                }
                ReincarnationSkill.getInstance().addCheck(_pc, 1);
                return;
            }
            if (cmd.equalsIgnoreCase("rei_3")) {
                if (_pc.getReincarnationSkill()[2] >= ConfigOther.ReiPointLv_3) {
                    _pc.sendPackets(new S_SystemMessage("此轉生技能已達最大上限等級"+ConfigOther.ReiPointLv_3+"，請選擇其它轉生技能。"));
                    return;
                }
                ReincarnationSkill.getInstance().addCheck(_pc, 2);
                return;
            }
            if (cmd.equalsIgnoreCase("rei_reset")) {
                final L1Item reicash = ItemTable.get().getTemplate(ConfigOther.ReiItemId);
                if (reicash == null) {
                    return;
                }
                if (_pc.getInventory().checkItem(ConfigOther.ReiItemId, ConfigOther.ReiItemCount)) {
                    _pc.sendPackets(new S_Message_YN(2761));
                    //_pc.sendPackets(new S_Message_YN(2761, "您確定要將轉生技能點數重置嗎？"));
                } else {
                    _pc.sendPackets(new S_SystemMessage(reicash.getName() + "(" +ConfigOther.ReiItemCount + ")不足，無法重置。"));
                }
				return;
            }
            // 轉生天賦end
			
			// 展開變身控制選單
			if (_pc.isShapeChange()) {
				// 解除GM管理狀態
				_pc.get_other().set_gmHtml(null);
				final int awakeSkillId = _pc.getAwakeSkillId();
				if ((awakeSkillId == AWAKEN_ANTHARAS) || (awakeSkillId == AWAKEN_FAFURION)
						|| (awakeSkillId == AWAKEN_VALAKAS)) {
					// 目前狀態中無法變身。
					_pc.sendPackets(new S_ServerMessage(1384));
					return;
				}
				L1PolyMorph.handleCommands(_pc, cmd);
				_pc.setShapeChange(false);
				_pc.setSummonMonster(false);
				return;
			}

			if (_pc.isItemPoly()) {// 是否使用道具變身
				L1ItemInstance item = _pc.getPolyScroll();// 取回自訂變身卷軸道具
				L1PolyMorph poly = PolyTable.get().getTemplate(cmd);
				if ((poly != null) || cmd.equals("none")) {
					if (item.getItemId() == 44212) {// 神秘的魔法變身書
						usePolyBook(_pc, item, cmd);
						return;
					} else {
						usePolyScroll(_pc, item, cmd);
						return;
					}
				}
			}

			if (_pc.isPhantomTeleport()) {// 是否正在使用幻象的傲慢之塔移動傳送符
				usePhantomTeleport(_pc, cmd);
				return;
			}

			// GM選單不為空
			if (_pc.get_other().get_gmHtml() != null) {
				_pc.get_other().get_gmHtml().action(cmd);
				return;
			}

			// 解除GM管理狀態
			_pc.get_other().set_gmHtml(null);

			// GM跟隨
			if (this._pc.isGm()) {
				if (cmd.equals("tp_refresh")) {
					L1ToPC.checkTPhtmlPredicate(this._pc, 0, true);
				} else if (cmd.equals("tp_refresh_map")) {
					L1ToPC.checkTPhtmlPredicate(this._pc, 0, false);
				} else if (cmd.equals("tp_page_up")) {
					L1ToPC.checkTPhtml(this._pc, this._pc.get_other().get_page() - 1);
				} else if (cmd.equals("tp_page_down")) {
					L1ToPC.checkTPhtml(this._pc, this._pc.get_other().get_page() + 1);
				} else if (cmd.matches("tp_[0-9]+")) {
					int index = Integer.parseInt(cmd.substring(3));
					L1ToPC.teleport2Player(this._pc, index);
				}
			}

			// 任務選單 FIXME
			if (cmd.equalsIgnoreCase("power")) {// 能力選取視窗
				// 判斷是否出現能力選取視窗
				if (_pc.power()) {
					//_pc.sendPackets(new S_Bonusstats(_pc.getId()));
				}

				/*
				 * } else if (cmd.equalsIgnoreCase("shop")) {// 道具商城
				 * _pc.sendPackets(new S_ShopSellListCnX(_pc, _pc.getId()));
				 */

			} else if (cmd.equalsIgnoreCase("index")) {// 任務查詢系統
				_pc.isWindows();

			} else if (cmd.equalsIgnoreCase("locerr1")) {// 解除人物卡點
				if (_pc.getHellTime() > 0) {
					_pc.sendPackets(new S_SystemMessage("在地獄中無法使用此功能"));
					return;
				}
				if (!_pc.getMap().isEscapable()) {
					_pc.sendPackets(new S_SystemMessage("此地圖中無法使用此功能"));
					return;
				}
	            if (_pc.isFishing()) {
					// 1413目前情況是無法使用的。
					_pc.sendPackets(new S_ServerMessage(1413));
	                return;
	            }
				if (_pc.getMapId() == ConfigNew.FishMapId) {
					_pc.sendPackets(new S_SystemMessage("所在地圖無法使用該功能。"));
					return;
				}
				_pc.set_unfreezingTime(10);// 延遲10秒

			} else if (cmd.equalsIgnoreCase("locerr2")) {// 修正人物錯位
	            if (_pc.isFishing()) {
					// 1413目前情況是無法使用的。
					_pc.sendPackets(new S_ServerMessage(1413));
	                return;
	            }
				if (_pc.getMapId() == ConfigNew.FishMapId) {
					_pc.sendPackets(new S_SystemMessage("所在地圖無法使用該功能。"));
					return;
				}
				_pc.set_misslocTime(5);// 延遲5秒

			} else if (cmd.equalsIgnoreCase("qt")) {// 查看執行中任務
				showStartQuest(_pc, _pc.getId());

			} else if (cmd.equalsIgnoreCase("quest")) {// 查看可執行任務
				showQuest(_pc, _pc.getId());

			} else if (cmd.equalsIgnoreCase("questa")) {// 查看全部任務
				showQuestAll(_pc, _pc.getId());

			} else if (cmd.equalsIgnoreCase("i")) {// 任務介紹
				final L1Quest quest = QuestTable.get().getTemplate(_pc.getTempID());
				_pc.setTempID(0);
				// 確認該任務存在
				if (quest == null) {
					return;
				}
				QuestClass.get().showQuest(_pc, quest.get_id());

			} else if (cmd.equalsIgnoreCase("d")) {// 任務回收
				final L1Quest quest = QuestTable.get().getTemplate(_pc.getTempID());
				_pc.setTempID(0);
				// 確認該任務存在
				if (quest == null) {
					return;
				}
				// 任務已經完成
				if (_pc.getQuest().isEnd(quest.get_id())) {
					questDel(quest);
					return;
				}
				// 任務尚未開始
				if (!_pc.getQuest().isStart(quest.get_id())) {
					// 很抱歉!!你並未開始執行這個任務!
					_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_q_not6"));
					return;
				}
				// 執行中 未完成任務
				questDel(quest);

			} else if (cmd.equalsIgnoreCase("dy")) {// 任務移除
				final L1Quest quest = QuestTable.get().getTemplate(_pc.getTempID());
				_pc.setTempID(0);
				// 確認該任務存在
				if (quest == null) {
					return;
				}
				// 任務已經完成
				if (_pc.getQuest().isEnd(quest.get_id())) {
					isDel(quest);
					return;
				}
				// 任務尚未開始
				if (!_pc.getQuest().isStart(quest.get_id())) {
					// 很抱歉!!你並未開始執行這個任務!
					_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_q_not6"));
					return;
				}
				// 執行中 未完成任務
				isDel(quest);

			} else if (cmd.equalsIgnoreCase("up")) {// 上一頁(管理)
				final int page = _pc.get_other().get_page() - 1;
				final L1ActionShowHtml show = new L1ActionShowHtml(_pc);
				show.showQuestMap(page);

			} else if (cmd.equalsIgnoreCase("dn")) {// 下一頁(管理)
				final int page = _pc.get_other().get_page() + 1;
				final L1ActionShowHtml show = new L1ActionShowHtml(_pc);
				show.showQuestMap(page);

			} else if (cmd.equalsIgnoreCase("q0")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 0;
				showPage(key);

			} else if (cmd.equalsIgnoreCase("q1")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 1;
				showPage(key);

			} else if (cmd.equalsIgnoreCase("q2")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 2;
				showPage(key);

			} else if (cmd.equalsIgnoreCase("q3")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 3;
				showPage(key);

			} else if (cmd.equalsIgnoreCase("q4")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 4;
				showPage(key);

			} else if (cmd.equalsIgnoreCase("q5")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 5;
				showPage(key);

			} else if (cmd.equalsIgnoreCase("q6")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 6;
				showPage(key);

			} else if (cmd.equalsIgnoreCase("q7")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 7;
				showPage(key);

			} else if (cmd.equalsIgnoreCase("q8")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 8;
				showPage(key);

			} else if (cmd.equalsIgnoreCase("q9")) {// 頁面內指定位置
				final int key = (_pc.get_other().get_page() * 10) + 9;
				showPage(key);
			}
			// 開關 FIXME

			else if (cmd.equalsIgnoreCase("attackheyes")) {//
				_pc.setattackhe(true);
				this._pc.sendPackets(new S_SystemMessage("武器特效開啟。"));
				showCulture2(_pc);

			} else if (cmd.equalsIgnoreCase("attackheno")) {//
				_pc.setattackhe(false);
				this._pc.sendPackets(new S_SystemMessage("武器特效關閉。"));
				showCulture2(_pc);

			} else if (cmd.equalsIgnoreCase("armorheyes")) {//
				_pc.setarmorhe(true);
				this._pc.sendPackets(new S_SystemMessage("裝備特效開啟。"));
				showCulture2(_pc);

			} else if (cmd.equalsIgnoreCase("armorheno")) {//
				_pc.setarmorhe(false);
				this._pc.sendPackets(new S_SystemMessage("裝備特效關閉。"));
				showCulture2(_pc);

			} else if (cmd.equalsIgnoreCase("droplistyes")) {//
				_pc.setdroplist(true);
				this._pc.sendPackets(new S_SystemMessage("開啟掉落寶物公告。"));
				showCulture2(_pc);

			} else if (cmd.equalsIgnoreCase("droplistno")) {//
				_pc.setdroplist(false);
				this._pc.sendPackets(new S_SystemMessage("關閉掉落寶物公告。"));
				showCulture2(_pc);

			} else if (cmd.equalsIgnoreCase("killyes")) {//
				_pc.setkill(true);
				this._pc.sendPackets(new S_SystemMessage("開啟殺人公告。"));
				showCulture2(_pc);

			} else if (cmd.equalsIgnoreCase("killno")) {//
				_pc.setkill(false);
				this._pc.sendPackets(new S_SystemMessage("關閉殺人公告。"));
				showCulture2(_pc);

			} else if (cmd.equalsIgnoreCase("Status11")) { // 檢查個人狀態
				showStatus11(_pc);
				// 下面是掛機設置
			} else if (cmd.equalsIgnoreCase("kgjnjm")) {//
				guajiygjm();
			} else if (cmd.equalsIgnoreCase("fashisl")) {//
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fhuiguaji")) {//
				guajisz();
			} else if (cmd.equalsIgnoreCase("kgjn_gb")) {//
				_pc.setygjnzc(0);
				guajiygjm();
			} else if (cmd.equalsIgnoreCase("sljn_gb")) {//
				_pc.setgjjnzc(0);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("ygjn_4")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 4)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setygjnzc(4);
				guajiygjm();
			} else if (cmd.equalsIgnoreCase("ygjn_6")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 6)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setygjnzc(6);
				guajiygjm();
			} else if (cmd.equalsIgnoreCase("ygjn_7")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 7)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setygjnzc(7);
				guajiygjm();
			} else if (cmd.equalsIgnoreCase("ygjn_15")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 15)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setygjnzc(15);
				guajiygjm();
			} else if (cmd.equalsIgnoreCase("ygjn_25")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 15)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setygjnzc(25);
				guajiygjm();
			} else if (cmd.equalsIgnoreCase("ygjn_189")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 189)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setygjnzc(189);
				guajiygjm();
			} else if (cmd.equalsIgnoreCase("ygjn_203")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 203)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setygjnzc(203);
				guajiygjm();
			} else if (cmd.equalsIgnoreCase("fashig_4")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 4)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(4);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_6")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 6)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(6);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_7")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 7)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(7);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_10")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 10)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(10);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_15")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 15)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(15);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_16")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 16)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(16);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_17")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 17)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(17);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_22")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 22)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(22);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_25")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 25)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(25);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_28")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 28)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(28);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_34")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 34)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(34);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_38")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 38)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(38);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_39")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 39)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(39);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_45")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 45)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(45);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_46")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 46)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(46);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_50")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 50)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(50);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_53")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 53)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(53);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_59")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 59)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(59);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_62")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 62)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(62);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_65")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 65)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(65);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_70")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 70)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(70);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_74")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 74)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(74);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_76")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 76)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(76);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_77")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 77)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(77);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("fashig_80")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 80)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(80);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("heiyao_4")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 4)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(4);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("heiyao_6")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 6)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(6);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("heiyao_7")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 7)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(7);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("heiyao_10")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 10)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(10);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("heiyao_15")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 15)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(15);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("heiyao_16")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 16)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(16);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("huanshu_203")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 203)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(203);
			} else if (cmd.equalsIgnoreCase("huanshu_207")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 207)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(207);
			} else if (cmd.equalsIgnoreCase("huanshu_208")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 208)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(208);
			} else if (cmd.equalsIgnoreCase("huanshu_202")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 202)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(202);
			} else if (cmd.equalsIgnoreCase("huanshu_212")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 212)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(212);
			} else if (cmd.equalsIgnoreCase("longqi_187")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 187)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(187);
			} else if (cmd.equalsIgnoreCase("longqi_184")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 184)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(184);
			} else if (cmd.equalsIgnoreCase("longqi_183")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 183)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(183);
			} else if (cmd.equalsIgnoreCase("longqi_189")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 189)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(189);
			} else if (cmd.equalsIgnoreCase("longqi_188")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 188)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(188);
			} else if (cmd.equalsIgnoreCase("longqi_192")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 192)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(192);
			} else if (cmd.equalsIgnoreCase("longqi_193")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 193)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(193);
			} else if (cmd.equalsIgnoreCase("qishi_4")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 4)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(4);
			} else if (cmd.equalsIgnoreCase("qishi_6")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 6)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(6);
			} else if (cmd.equalsIgnoreCase("qishi_7")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 7)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(7);
			} else if (cmd.equalsIgnoreCase("qishi_87")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 87)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(87);
			} else if (cmd.equalsIgnoreCase("wangzi_4")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 4)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(4);
			} else if (cmd.equalsIgnoreCase("wangzi_6")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 6)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(6);
			} else if (cmd.equalsIgnoreCase("wangzi_7")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 7)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(7);
			} else if (cmd.equalsIgnoreCase("wangzi_10")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 10)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(10);
			} else if (cmd.equalsIgnoreCase("wangzi_15")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 15)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(15);
			} else if (cmd.equalsIgnoreCase("wangzi_4")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 4)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(4);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("wangzi_6")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 6)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(6);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("wangzi_7")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 7)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(7);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_4")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 4)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(4);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_6")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 6)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(6);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_7")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 7)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(7);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_10")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 10)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(10);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_15")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 15)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(15);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_16")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 16)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(16);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_17")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 17)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(17);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_22")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 22)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(22);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_25")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 25)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(25);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_28")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 28)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(28);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_34")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 34)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(34);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_38")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 38)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(38);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_39")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 39)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(39);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_45")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 45)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(45);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_46")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 46)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(46);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("yaojin_132")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 132)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(132);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("zhanshi_4")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 4)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(4);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("zhanshi_6")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 6)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(6);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("zhanshi_7")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 7)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(7);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("zhanshi_230")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 230)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(230);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("zhanshi_225")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 225)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(225);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("zhanshi_228")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 228)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(228);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("zhanshi_229")) {//
				if (!CharSkillReading.get().spellCheck(_pc.getId(), 229)) {
					_pc.sendPackets(new S_SystemMessage("\\aG還沒學習此技能，請重新選擇"));
					return;
				}
				_pc.setgjjnzc(229);
				fashishoulie();
			} else if (cmd.equalsIgnoreCase("gjjnsj_2")) {//
				_pc.setgjjgsj(2);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjjnsj_3")) {//
				_pc.setgjjgsj(3);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjjnsj_4")) {//
				_pc.setgjjgsj(4);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjjnsj_5")) {//
				_pc.setgjjgsj(5);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjjnsj_6")) {//
				_pc.setgjjgsj(6);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjjnsj_7")) {//
				_pc.setgjjgsj(7);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjjnsj_8")) {//
				_pc.setgjjgsj(8);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjjnsj_9")) {//
				_pc.setgjjgsj(9);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjml_20")) {//
				_pc.setgjml(20);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjml_30")) {//
				_pc.setgjml(30);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjml_40")) {//
				_pc.setgjml(40);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjml_50")) {//
				_pc.setgjml(50);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjml_60")) {//
				_pc.setgjml(60);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjml_70")) {//
				_pc.setgjml(70);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjml_80")) {//
				_pc.setgjml(80);
				guajisz();
			} else if (cmd.equalsIgnoreCase("gjsy")) {// 我要轉職業 王族
				if (_pc.getgjsykg() == true) {
					_pc.setgjsykg(false);// 關閉
					_pc.sendPackets(new S_SystemMessage("\\aD掛機瞬移：關閉"));
					guajisz();
				} else {
					_pc.setgjsykg(true);
					_pc.setlslocx(0);
					_pc.setlslocy(0);
					_pc.setlsgjfw(0);
					_pc.sendPackets(new S_SystemMessage("\\aD掛機瞬移：開啟"));
					_pc.sendPackets(new S_SystemMessage("\\aD請確認地圖可以瞬移"));
					_pc.sendPackets(new S_SystemMessage("\\aD帶足夠的瞬移卷"));
					guajisz();
				}
			} else if (cmd.equalsIgnoreCase("bosssy")) {// 我要轉職業 王族

				if (_pc.getbossgjsykg() == true) {
					_pc.setbossgjsykg(false);// 關閉
					_pc.sendPackets(new S_SystemMessage("\\aD掛機遇BOSS瞬移：關閉"));
					guajisz();
				} else {
					_pc.setbossgjsykg(true);// 關閉
					_pc.sendPackets(new S_SystemMessage("\\aD掛機遇BOSS瞬移：開啟"));
					guajisz();
				}
				_pc.sendPackets(new S_OwnCharStatus(_pc));
			} else if (cmd.equalsIgnoreCase("fwgj")) {// 我要轉職業 王族
				_pc.setlslocx(_pc.getX());
				_pc.setlslocy(_pc.getY());
				_pc.setgjsykg(false);// 關閉
				// _pc.set_AutoHpType(1);
				_pc.setlsgjfw(_pc.getlsgjfw() + 10);
				_pc.sendPackets(new S_SystemMessage("掛機範圍加10"));
				guajisz();
			} else if (cmd.equalsIgnoreCase("fwgj1")) {// 我要轉職業 王族
				_pc.setlslocx(_pc.getX());
				_pc.setlslocy(_pc.getY());
				_pc.setgjsykg(false);// 關閉
				// _pc.set_AutoHpType(1);
				_pc.setlsgjfw(_pc.getlsgjfw() - 10);
				guajisz();
				if (_pc.getlsgjfw() < 0) {
					_pc.setlsgjfw(0);
				}
				_pc.sendPackets(new S_SystemMessage("掛機範圍減10"));
				guajisz();
			} else if (cmd.equalsIgnoreCase("ksgj")) {// 我要轉職業 王族
				if (_pc.getWeapon() == null) { // 重量過重
					// 110 \f1當負重過重的時候，無法戰鬥。
					_pc.sendPackets(new S_ServerMessage("\\aD請佩戴武器後開啟掛機"));
					// _log.error("要求角色攻擊:重量過重");
					return;
				}
				if (!_pc.isActived()) {
					// OnlineGiftSet.remove(pc);
					_pc.startAI();
					_pc.sendPackets(new S_ServerMessage("\\aD您已經開始掛機,雙擊掛機符結束掛機"));
				} else {
					_pc.setActived(false);
					_pc.setlslocx(0);
					_pc.setlslocy(0);
					_pc.sendPackets(new S_ServerMessage("\\aD您的掛機結束了"));
					L1Teleport.teleport(_pc, _pc.getX(), _pc.getY(),
							(short) _pc.getMapId(), _pc.getHeading(), true);
				}
			} else if (cmd.equalsIgnoreCase("lmms")) {// 掛機禮貌模式不搶怪
				if (_pc.limao() == true) {
					_pc.setlimao(false);// 關閉
					_pc.sendPackets(new S_SystemMessage("\\aD禮貌模式：關閉"));
					guajisz();
				} else {
					_pc.setlimao(true);
					_pc.sendPackets(new S_SystemMessage("\\aD禮貌模式：掛機不搶怪"));
					guajisz();
				}
				_pc.sendPackets(new S_OwnCharStatus(_pc));
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 使用自訂變身卷軸
	 * 
	 * @param pc
	 * @param item
	 * @param s
	 */
	private void usePolyScroll(L1PcInstance pc, L1ItemInstance item, String s) {
		try {
			L1PolyMorph poly = PolyTable.get().getTemplate(s);
			int time = 1800;
			if (item.getBless() == 0) {
				time = 2100;
			}
			if (item.getBless() == 128) {
				time = 2100;
			}
			boolean isUseItem = false;
			if (s.equals("none")) {
				if (pc.getTempCharGfx() == 6034 || pc.getTempCharGfx() == 6035) {
					isUseItem = true;
				} else {
					L1PolyMorph.undoPoly(pc);
					isUseItem = true;
				}
			} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {// 符合等級限制或是GM

				if ((poly.getPolyId() == 13715) && (pc.get_sex() != 0 || !pc.isCrown())) {// 不符合變身真王子條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13717) && (pc.get_sex() != 1 || !pc.isCrown())) {// 不符合變身真公主條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13719) && (pc.get_sex() != 0 || !pc.isKnight())) {// 不符合變身真騎士條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13721) && (pc.get_sex() != 1 || !pc.isKnight())) {// 不符合變身真女騎士條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13723) && (pc.get_sex() != 0 || !pc.isElf())) {// 不符合變身真妖精條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13725) && (pc.get_sex() != 1 || !pc.isElf())) {// 不符合變身真女妖精條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13727) && (pc.get_sex() != 0 || !pc.isWizard())) {// 不符合變身真法師條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13729) && (pc.get_sex() != 1 || !pc.isWizard())) {// 不符合變身真女法師條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13731) && (pc.get_sex() != 0 || !pc.isDarkelf())) {// 不符合變身真黑妖條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13733) && (pc.get_sex() != 1 || !pc.isDarkelf())) {// 不符合變身真女黑妖條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13735) && (pc.get_sex() != 0 || !pc.isDragonKnight())) {// 不符合變身真龍騎條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13737) && (pc.get_sex() != 1 || !pc.isDragonKnight())) {// 不符合變身真女龍騎條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13739) && (pc.get_sex() != 0 || !pc.isIllusionist())) {// 不符合變身真幻術條件
					isUseItem = false;
				} else if ((poly.getPolyId() == 13741) && (pc.get_sex() != 1 || !pc.isIllusionist())) {// 不符合變身真女幻術條件
					isUseItem = false;

				} else {// 符合所有變身條件
					// 執行變身
					L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
					isUseItem = true;
				}
			}

			if (isUseItem) {
				pc.getInventory().removeItem(item, 1);// 刪除道具
				pc.sendPackets(new S_CloseList(pc.getId()));
			} else {
				pc.sendPackets(new S_ServerMessage(181)); // 181:\f1無法變成你指定的怪物。
				pc.sendPackets(new S_CloseList(pc.getId()));
			}

			pc.setItemPoly(false);
			pc.setPolyScroll(null);

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}

	}

	/**
	 * 使用神秘的魔法變身書
	 * 
	 * @param pc
	 * @param item
	 * @param s
	 */
	private void usePolyBook(L1PcInstance pc, L1ItemInstance item, String s) {
		try {
			L1PolyMorph poly = PolyTable.get().getTemplate(s);
			int time = 1800;
			if (item.getBless() == 0) {
				time = 2100;
			}
			if (item.getBless() == 128) {
				time = 2100;
			}
			boolean isUseItem = false;
			if (s.equals("none")) {
				if (pc.getTempCharGfx() == 6034 || pc.getTempCharGfx() == 6035) {
					isUseItem = true;
				} else {
					L1PolyMorph.undoPoly(pc);
					isUseItem = true;
				}
			} else if ((poly.getMinLevel() <= pc.getLevel() || item.getItemId() == 44212) || pc.isGm()) {
				// 執行變身
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
				isUseItem = true;
			}

			if (isUseItem) {
				pc.sendPackets(new S_CloseList(pc.getId()));
			} else {
				pc.sendPackets(new S_ServerMessage(181)); // \f1變身。
				pc.sendPackets(new S_CloseList(pc.getId()));
			}
			pc.setItemPoly(false);
			pc.setPolyScroll(null);

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 使用幻象的傲慢之塔傳送符進行移動
	 * 
	 * @param pc
	 * @param cmd
	 */
	private void usePhantomTeleport(L1PcInstance pc, String cmd) {
		try {
			int x = 0;
			int y = 0;
			short mapid = 0;

			switch (cmd) {
			case "a":
				x = 32797;
				y = 32799;
				mapid = 3301;
				break;
			case "b":
				x = 32797;
				y = 32799;
				mapid = 3302;
				break;
			case "c":
				x = 32797;
				y = 32799;
				mapid = 3303;
				break;
			case "d":
				x = 32668;
				y = 32864;
				mapid = 3304;
				break;
			case "e":
				x = 32668;
				y = 32864;
				mapid = 3305;
				break;
			case "f":
				x = 32717;
				y = 32871;
				mapid = 3306;
				break;
			case "g":
				x = 32668;
				y = 32864;
				mapid = 3307;
				break;
			case "h":
				x = 32668;
				y = 32864;
				mapid = 3308;
				break;
			case "i":
				x = 32668;
				y = 32864;
				mapid = 3309;
				break;
			case "j":
				x = 32797;
				y = 32799;
				mapid = 3310;
				break;
			case "k":
				x = 32760;
				y = 32894;
				mapid = 7100;
				break;
			case "l":
				x = 32692;
				y = 32903;
				mapid = 7100;
				break;
			}

			L1Teleport.teleport(pc, x, y, mapid, pc.getHeading(), true);
			pc.setPhantomTeleport(false);

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 任務解除執行
	 * 
	 * @param quest
	 */
	private void questDel(final L1Quest quest) {
		try {
			if (quest.is_del()) {
				_pc.setTempID(quest.get_id());
				String over = null;
				// 該任務完成
				if (_pc.getQuest().isEnd(quest.get_id())) {
					over = "完成任務";// 完成任務!
				} else {
					over = _pc.getQuest().get_step(quest.get_id()) + " / " + quest.get_difficulty();
				}

				final String[] info = new String[] { quest.get_questname(), // 任務名稱
						Integer.toString(quest.get_questlevel()), // 任務等級
						over, // 任務進度
						// 額外說明
				};
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qi2", info));

			} else {
				// 任務不可刪除
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_q_not5"));
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 確定解除任務執行
	 * 
	 * @param quest
	 */
	private void isDel(final L1Quest quest) {
		try {
			if (quest.is_del()) {
				// 任務終止
				QuestClass.get().stopQuest(_pc, quest.get_id());

				CharacterQuestReading.get().delQuest(_pc.getId(), quest.get_id());
				final String[] info = new String[] { quest.get_questname(), // 任務名稱
						Integer.toString(quest.get_questlevel()), // 任務等級
				};
				// 刪除任務
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qi3", info));

			} else {
				// 任務不可刪除
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_q_not5"));
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 查看執行中任務
	 * 
	 * @param pc
	 * @param id
	 */
	public static void showStartQuest(L1PcInstance pc, int objid) {
		try {
			// 清空暫存任務清單
			pc.get_otherList().QUESTMAP.clear();

			int key = 0;
			for (int i = QuestTable.MINQID; i <= QuestTable.MAXQID; i++) {
				final L1Quest value = QuestTable.get().getTemplate(i);
				if (value != null) {
					// 該任務已經結束
					if (pc.getQuest().isEnd(value.get_id())) {
						continue;
					}
					// 執行中任務判斷
					if (pc.getQuest().isStart(value.get_id())) {
						pc.get_otherList().QUESTMAP.put(key++, value);
					}
				}
			}

			if (pc.get_otherList().QUESTMAP.size() <= 0) {
				// 很抱歉!!你並沒有任何執行中的任務!
				pc.sendPackets(new S_NPCTalkReturn(objid, "y_q_not7"));

			} else {
				final L1ActionShowHtml show = new L1ActionShowHtml(pc);
				show.showQuestMap(0);
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 可執行任務
	 * 
	 * @param pc
	 * @param objid
	 */
	public static void showQuest(L1PcInstance pc, int objid) {
		try {
			// 清空暫存任務清單
			pc.get_otherList().QUESTMAP.clear();

			int key = 0;

			for (int i = QuestTable.MINQID; i <= QuestTable.MAXQID; i++) {
				final L1Quest value = QuestTable.get().getTemplate(i);
				if (value != null) {
					// 大於可執行等級
					if (pc.getLevel() >= value.get_questlevel()) {
						// 該任務已經結束
						if (pc.getQuest().isEnd(value.get_id())) {
							continue;
						}
						// 該任務已經開始
						if (pc.getQuest().isStart(value.get_id())) {
							continue;
						}
						// 可執行職業判斷
						if (value.check(pc)) {
							pc.get_otherList().QUESTMAP.put(key++, value);
						}
					}
				}
			}

			if (pc.get_otherList().QUESTMAP.size() <= 0) {
				// 很抱歉!!目前你的任務已經全部完成!
				pc.sendPackets(new S_NPCTalkReturn(objid, "y_q_not4"));

			} else {
				final L1ActionShowHtml show = new L1ActionShowHtml(pc);
				show.showQuestMap(0);
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 全部任務
	 * 
	 * @param pc
	 * @param objid
	 */
	public static void showQuestAll(L1PcInstance pc, int objid) {
		try {
			// 清空暫存任務清單
			pc.get_otherList().QUESTMAP.clear();

			int key = 0;
			for (int i = QuestTable.MINQID; i <= QuestTable.MAXQID; i++) {
				final L1Quest value = QuestTable.get().getTemplate(i);
				if (value != null) {
					// 可執行職業判斷
					if (value.check(pc)) {
						pc.get_otherList().QUESTMAP.put(key++, value);
					}
				}
			}
			final L1ActionShowHtml show = new L1ActionShowHtml(pc);
			show.showQuestMap(0);

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 展示指定任務進度資料
	 * 
	 * @param key
	 */
	private void showPage(int key) {
		try {
			final L1Quest quest = _pc.get_otherList().QUESTMAP.get(key);
			_pc.setTempID(quest.get_id());
			String over = null;
			// 該任務完成
			if (_pc.getQuest().isEnd(quest.get_id())) {
				over = "完成任務";// 完成任務!
			} else {
				over = _pc.getQuest().get_step(quest.get_id()) + " / " + quest.get_difficulty();
			}

			final String[] info = new String[] { quest.get_questname(), // 任務名稱
					Integer.toString(quest.get_questlevel()), // 任務等級
					over, // 任務進度
					""// 額外說明
			};
			_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "y_qi1", info));

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 檢查個人狀態
	 * @param pc
	 */
	private void showStatus11(final L1PcInstance pc) {
		try {
			//目前已吃萬靈藥
            String n0 = String.valueOf(pc.getElixirStats());
		
			//你的基本力量點數
            String n1 = String.valueOf(pc.getBaseStr());
            //你的基本敏捷點數
            String n2 = String.valueOf(pc.getBaseDex());
            //你的基本智力點數
            String n3 = String.valueOf(pc.getBaseInt());
            //你的基本魅力點數
            String n4 = String.valueOf(pc.getBaseCha());
            //你的基本體質點數
            String n5 = String.valueOf(pc.getBaseCon());
            //你的基本精神點數
            String n6 = String.valueOf(pc.getBaseWis());
			//額外增加力量點數
            String n7 = String.valueOf(pc.getStr()-pc.getBaseStr());
            //額外增加敏捷點數
            String n8 = String.valueOf(pc.getDex()-pc.getBaseDex());
            //額外增加智力點數
            String n9 = String.valueOf(pc.getInt()-pc.getBaseInt());
            //額外增加魅力點數
            String n10 = String.valueOf(pc.getCha()-pc.getBaseCha());
            //額外增加體質點數
            String n11 = String.valueOf(pc.getCon()-pc.getBaseCon());
            //額外增加精神點數
            String n12 = String.valueOf(pc.getWis()-pc.getBaseWis());
		    //人物基本魔攻點數
            String n13 = String.valueOf(pc.getTrueSp());
            //額外增加魔攻點數
            String n14 = String.valueOf(pc.getSp()-pc.getTrueSp());
			//基本血量
            String n15 = String.valueOf(pc.getBaseMaxHp());
            //額外血量
            String n16 = String.valueOf(pc.getMaxHp()-pc.getBaseMaxHp());
            //基本魔量
            String n17 = String.valueOf(pc.getBaseMaxMp());
            //額外魔量
            String n18 = String.valueOf(pc.getMaxMp()-pc.getBaseMaxMp());
            //近距離命中加成
            String n19 = "";//String.valueOf(calcPcHit(pc,1));
            //遠距離命中加成
            String n20 = "";//String.valueOf(calcPcHit(pc,2));
            //近距離傷害加成
            String n21 = "";//String.valueOf(pcDmgMode(pc,1));
            //遠距離傷害加成
			String n22 = "";// String.valueOf(pcDmgMode(pc,2));
			// 魔法命中加成
			String n23 = String.valueOf(pc.getMagicHit() + L1ClassFeature.calcIntMagicHit(pc.getInt(), pc.getBaseInt()));
			// 防禦加成
			// String n24 = String.valueOf(CalcStat.calcAc(pc.getType(), pc.getLevel(), pc.getBaseDex()));
			// XXX 7.6屬性 ADD
            String n24 = String.valueOf(10 + CalcStat.calcAc(pc.getType(), pc.getLevel()) + L1ClassFeature.calcDexAc(pc.getBaseDex()));

			final String[] info = new String[] { n0, n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15,
					n16, n17, n18, n19, n20, n21, n22, n23, n24 };

			pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_status_1", info));

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 遊戲便捷優化
	 * 
	 * @param pc
	 * @param add_type
	 * @param consume_item
	 */
	private void showCulture2(final L1PcInstance pc) {
		try {
			final L1PcOther other = pc.get_other();

			// 在取得一次最新資料
			String attackhe = "";
			//
			if (_pc.attackhe() == true)
				attackhe = "[ 開啟中 ]";
			else {
				attackhe = "[ 關閉中 ]";
			}

			String armorhe = "";
			//
			if (_pc.armorhe() == true)
				armorhe = "[ 開啟中 ]";
			else {
				armorhe = "[ 關閉中 ]";
			}
			String droplist = "";
			//
			if (_pc.droplist() == true)
				droplist = "[ 開啟中 ]";
			else {
				droplist = "[ 關閉中 ]";
			}
			String kill = "";
			//
			if (_pc.kill() == true)
				kill = "[ 開啟中 ]";
			else {
				kill = "[ 關閉中 ]";
			}
			final String nowDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

			final String[] info = new String[] { Config.SERVERNAME, String.valueOf(attackhe), String.valueOf(armorhe),
					String.valueOf(droplist), String.valueOf(kill), nowDate, // 目前時間
					ServerRestartTimer.get_restartTime()// , // 重啟時間
			};
			// pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_who", info));

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 掛機設置
	 * 
	 * @param pc
	 * @param add_type
	 * @param consume_item
	 */
	private void guajisz() {
		try {
			String kaiguai = "關閉狀態";
			if (_pc.getygjnzc() > 0) {
				final L1Skills skill = SkillsTable.get().getTemplate(
						_pc.getygjnzc());
				if (skill != null) {
					kaiguai = skill.getName();
				}
			}
			String shoulie = "關閉狀態";
			if (_pc.getgjjnzc() > 0) {
				final L1Skills skill1 = SkillsTable.get().getTemplate(
						_pc.getgjjnzc());
				if (skill1 != null) {
					shoulie = skill1.getName();
				}
			}
			int gjjnjg = _pc.getgjjgsj();
			int gjml = _pc.getgjml();
			String lmm="關閉中";
			if(_pc.limao()){
				   lmm="開啟中";
			}			
			int gjfw =_pc.getlsgjfw();
			String bosssy="關閉中";
			if(_pc.getbossgjsykg()){
				   bosssy="開啟中";
			}	
			String gjsy="關閉中";
			if(_pc.getgjsykg()){
				   gjsy="開啟中";
			}
			String[] info = new String[] { String.valueOf(kaiguai),
					String.valueOf(shoulie), String.valueOf(gjjnjg),
					String.valueOf(gjml), String.valueOf(lmm),
					String.valueOf(gjfw), String.valueOf(bosssy),
					String.valueOf(gjsy), };
			_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "guajiqd", info));
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 開怪技能界面
	 * 
	 * @param pc
	 * @param add_type
	 * @param consume_item
	 */
	private void guajiygjm() {
		try {
			String xzjnname = "關閉狀態";
			if (_pc.getygjnzc() > 0) {
				final L1Skills skill = SkillsTable.get().getTemplate(
						_pc.getygjnzc());
				if (skill != null) {
					xzjnname = skill.getName();
				}
			}
			String[] info = new String[] { String.valueOf(xzjnname) };
			_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "kgjn", info));
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 法師狩獵技能界面
	 * 
	 * @param pc
	 * @param add_type
	 * @param consume_item
	 */
	private void fashishoulie() {
		try {
			String xzjnname = "關閉狀態";
			if (_pc.getgjjnzc() > 0) {
				final L1Skills skill = SkillsTable.get().getTemplate(
						_pc.getgjjnzc());
				if (skill != null) {
					xzjnname = skill.getName();
				}
			}
			String[] info = new String[] { String.valueOf(xzjnname) };
			if (_pc.isWizard()) {
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "fashisl",
						info));
			}
			if (_pc.isDarkelf()) {
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "heiyaosl",
						info));
			}
			if (_pc.isElf()) {
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "yaojinsl",
						info));
			}
			if (_pc.isKnight()) {
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "qishisl",
						info));
			}
			if (_pc.isCrown()) {
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "wangzisl",
						info));
			}
			if (_pc.isDragonKnight()) {
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "longqisl",
						info));
			}
			if (_pc.isIllusionist()) {
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "huanshusl",
						info));
			}
			if (_pc.isWarrior()) {
				_pc.sendPackets(new S_NPCTalkReturn(_pc.getId(), "zhanshisl",
						info));
			}
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
}

