package com.lineage.server.clientpackets;

import static com.lineage.server.model.skill.L1SkillId.AI_1;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import william.ReincarnationSkill;
import william.login_Artiface;

import com.add.MJBookQuestSystem.Loader.UserMonsterBookLoader;
import com.add.MJBookQuestSystem.Loader.UserWeekQuestLoader;
import com.eric.gui.J_Main;
import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.data.event.CampSet;
import com.lineage.data.event.CardSet;
import com.lineage.data.event.ClanSkillDBSet;
import com.lineage.data.event.EffectAISet;
import com.lineage.data.event.LeavesSet;
import com.lineage.data.event.OnlineGiftSet;
import com.lineage.data.event.ProtectorSet;
import com.lineage.data.event.QuestMobSet;
import com.lineage.data.event.ValakasRoom.ValakasRoomSystem;
import com.lineage.data.event.ice.IceQueenThread;
import com.lineage.data.npc.Npc_clan;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.CheckFightTimeController;
import com.lineage.server.Controller.UserRankingController;
import com.lineage.server.datatables.C1_Name_Table;
import com.lineage.server.datatables.CastleWarGiftTable;
import com.lineage.server.datatables.CharacterAttendTable;
import com.lineage.server.datatables.CharacterGiftTable;
import com.lineage.server.datatables.CheckItemPowerTable;
import com.lineage.server.datatables.GetBackRestartTable;
import com.lineage.server.datatables.InvSwapTable;
import com.lineage.server.datatables.MapsGroupTable;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.datatables.QuestNewTable;
import com.lineage.server.datatables.ServerQuestMobTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.SpamTable;
import com.lineage.server.datatables.T_OnlineGiftTable;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.lock.CharMapTimeReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.lock.CharacterC1Reading;
import com.lineage.server.datatables.lock.CharacterConfigReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ExcludingList;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1War;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ACTION_UI;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_AttenDance;
import com.lineage.server.serverpackets.S_BookMarkLoad;
import com.lineage.server.serverpackets.S_CastleMaster;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_CharReset;
import com.lineage.server.serverpackets.S_EnterGame;
import com.lineage.server.serverpackets.S_EquipmentSlot;
import com.lineage.server.serverpackets.S_EventUI;
import com.lineage.server.serverpackets.S_GmMessage;
import com.lineage.server.serverpackets.S_InitialAbilityGrowth;
import com.lineage.server.serverpackets.S_InvList;
import com.lineage.server.serverpackets.S_Invis;
import com.lineage.server.serverpackets.S_Karma;
import com.lineage.server.serverpackets.S_Luckylottery;
import com.lineage.server.serverpackets.S_MapID;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharPack;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxActiveSpells;
import com.lineage.server.serverpackets.S_PacketBoxCharEr;
import com.lineage.server.serverpackets.S_PacketBoxConfig;
import com.lineage.server.serverpackets.S_PacketBoxExp;
import com.lineage.server.serverpackets.S_PacketBoxGame;
import com.lineage.server.serverpackets.S_PacketBoxIcon1;
import com.lineage.server.serverpackets.S_PacketBoxProtection;
import com.lineage.server.serverpackets.S_PledgeName;
import com.lineage.server.serverpackets.S_PledgeWatch;
import com.lineage.server.serverpackets.S_ProtoBuffers;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.S_TamWindow;
import com.lineage.server.serverpackets.S_TestPacket;
import com.lineage.server.serverpackets.S_War;
import com.lineage.server.serverpackets.S_WarriorSkill;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.serverpackets.ability.S_BaseAbility;
import com.lineage.server.serverpackets.ability.S_BaseAbilityDetails;
import com.lineage.server.serverpackets.ability.S_ConDetails;
import com.lineage.server.serverpackets.ability.S_DexDetails;
import com.lineage.server.serverpackets.ability.S_ElixirCount;
import com.lineage.server.serverpackets.ability.S_IntDetails;
import com.lineage.server.serverpackets.ability.S_StrDetails;
import com.lineage.server.serverpackets.ability.S_WeightStatus;
import com.lineage.server.serverpackets.ability.S_WisDetails;
import com.lineage.server.templates.L1BookMark;
import com.lineage.server.templates.L1Config;
import com.lineage.server.templates.L1GetBackRestart;
import com.lineage.server.templates.L1MapsLimitTime;
import com.lineage.server.templates.L1PcOtherList;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.templates.L1UserSkillTmp;
import com.lineage.server.templates.L1User_Power;
import com.lineage.server.timecontroller.pc.MapTimerThread;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.types.Point;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldSummons;
import com.lineage.server.world.WorldWar;

public class C_LoginToServer extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_LoginToServer.class);

	public void start(byte[] decrypt, ClientExecutor client) {
		try {
			read(decrypt);
			
			//int r = 0;
			
			String loginName = client.getAccountName();

			if (client.getActiveChar() != null) {
				_log.error("帳號重複登入人物: " + loginName + "強制中斷連線");
				client.kick();
				return;
			}

			String charName = readS();

			L1PcInstance pc = L1PcInstance.load(charName);

			if ((pc == null) || (!loginName.equals(pc.getAccountName()))) {
				_log.info("無效登入要求: " + charName + " 帳號(" + loginName + ", " + client.getIp() + ")");
				client.kick();
				return;
			}

			if (Config.GUI) {
				J_Main.getInstance().addPlayerTable(loginName, charName, client.getIp());
			}

			_log.info("登入遊戲: " + charName + "(" + pc.getLevel() + ") 帳號(" + loginName + ", " + client.getIp() + ")");

			int currentHpAtLoad = pc.getCurrentHp();
			int currentMpAtLoad = pc.getCurrentMp();
			// 重置錯誤次數
			client.set_error(0);

			pc.clearSkillMastery();// 清除技能資訊
			pc.setOnlineStatus(1);// 設定連線狀態

			CharacterTable.updateOnlineStatus(pc);
			World.get().storeObject(pc);

			pc.setNetConnection(client);// 登記封包接收組
			pc.setPacketOutput(client.out());// 登記封包發送組

			pc.sendPackets(new S_EnterGame(pc));// 宣告進入遊戲

			client.setActiveChar(pc);// 登記玩家資料
			
			pc.sendPackets(new S_InitialAbilityGrowth(pc));// 初始點數獎勵

			items(pc);// 讀取角色道具

			bookmarks(pc);// 取得記憶座標資料

			backRestart(pc);// 判斷回村座標資料

			getFocus(pc);// 遊戲焦點及人物狀態更新
			
			getOther(pc);// 額外紀錄資料

			pc.sendVisualEffectAtLogin();// 人物中毒、麻痺狀態顯示

			skills(pc);// 取得角色魔法技能資料

			buff(pc);// 人物保留的BUFF資料

			pc.turnOnOffLight();

			if (pc.getCurrentHp() > 0) {
				pc.setDead(false);
				pc.setStatus(0);
			} else {
				pc.setDead(true);
				pc.setStatus(8);
			}

			pc.sendPackets(new S_PacketBox(32));

			Reward_stats(pc); // 7.6 空身體質額外獎勵
			
            ReincarnationSkill.getInstance().getPoint(pc); // 轉生天賦
			
			// 7.6
			// XXX 7.6 ADD
			pc.sendPackets(new S_PacketBoxCharEr(pc));// 角色迴避率更新

			// XXX 7.6 能力基本資訊-力量
			pc.sendPackets(new S_StrDetails(2,
					L1ClassFeature.calcStrDmg(pc.getStr(), pc.getBaseStr()),
					L1ClassFeature.calcStrHit(pc.getStr(), pc.getBaseStr()),
					L1ClassFeature.calcStrDmgCritical(pc.getStr(), pc.getBaseStr()),
					L1ClassFeature.calcAbilityMaxWeight(pc.getStr(), pc.getCon())
					));
									
			// XXX 7.6 重量程度資訊
			pc.sendPackets(new S_WeightStatus(pc.getInventory().getWeight100(), pc.getInventory().getWeight(), (int)pc.getMaxWeight()));
									
			// XXX 7.6 能力基本資訊-智力
			pc.sendPackets(new S_IntDetails(2,
					L1ClassFeature.calcIntMagicDmg(pc.getInt(), pc.getBaseInt()),
					L1ClassFeature.calcIntMagicHit(pc.getInt(), pc.getBaseInt()),
					L1ClassFeature.calcIntMagicCritical(pc.getInt(), pc.getBaseInt()),
					L1ClassFeature.calcIntMagicBonus(pc.getType(), pc.getInt()),
					L1ClassFeature.calcIntMagicConsumeReduction(pc.getInt())
					));
									
			// XXX 7.6 能力基本資訊-精神
			pc.sendPackets(new S_WisDetails(2,
					L1ClassFeature.calcWisMpr(pc.getWis(), pc.getBaseWis()),
					L1ClassFeature.calcWisPotionMpr(pc.getWis(), pc.getBaseWis()),
					L1ClassFeature.calcStatMr(pc.getWis()) + L1ClassFeature.newClassFeature(pc.getType()).getClassOriginalMr(),
					L1ClassFeature.calcBaseWisLevUpMpUp(pc.getType(), pc.getBaseWis())
					));
									
			// XXX 7.6 能力基本資訊-敏捷
			pc.sendPackets(new S_DexDetails(2,
					L1ClassFeature.calcDexDmg(pc.getDex(), pc.getBaseDex()),
					L1ClassFeature.calcDexHit(pc.getDex(), pc.getBaseDex()),
					L1ClassFeature.calcDexDmgCritical(pc.getDex(), pc.getBaseDex()),
					L1ClassFeature.calcDexAc(pc.getDex()),
					L1ClassFeature.calcDexEr(pc.getDex())
					));
									
			// XXX 7.6 能力基本資訊-體質
			pc.sendPackets(new S_ConDetails(2,
					L1ClassFeature.calcConHpr(pc.getCon(), pc.getBaseCon()),
					L1ClassFeature.calcConPotionHpr(pc.getCon(), pc.getBaseCon()),
					L1ClassFeature.calcAbilityMaxWeight(pc.getStr(), pc.getCon()),
					L1ClassFeature.calcBaseClassLevUpHpUp(pc.getType()) + L1ClassFeature.calcBaseConLevUpExtraHpUp(pc.getType(), pc.getBaseCon())
					));
									
			// XXX 7.6 重量程度資訊
			pc.sendPackets(new S_WeightStatus(pc.getInventory().getWeight() * 100 / (int) pc.getMaxWeight(), pc.getInventory().getWeight(), (int) pc.getMaxWeight()));

			// XXX 7.6 純能力詳細資訊 階段:25
			pc.sendPackets(new S_BaseAbilityDetails(25));

			// XXX 7.6 純能力詳細資訊 階段:35
			pc.sendPackets(new S_BaseAbilityDetails(35));

			// XXX 7.6 純能力詳細資訊 階段:45
			pc.sendPackets(new S_BaseAbilityDetails(45));

			// XXX 7.6 純能力資訊
			pc.sendPackets(new S_BaseAbility(pc.getBaseStr(), pc.getBaseInt(), pc.getBaseWis(), pc.getBaseDex(), pc.getBaseCon(), pc.getBaseCha()));

			// XXX 7.6 萬能藥使用數量
			pc.sendPackets(new S_ElixirCount(pc.getElixirStats()));

			pc.sendPackets(new S_PacketBox(189));

			if (Config.UserRanking) {
				pc.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.PCBANG_SET)); // 新排行系統
				UserRankingController.getInstance().setBuffSetting(pc); // 新排行系統
			}

			// 取回快速鍵紀錄
			L1Config config = CharacterConfigReading.get().get(pc.getId());
			if (config != null) {
				pc.sendPackets(new S_PacketBoxConfig(config));
			}

			serchSummon(pc);// 殘留寵物資料

			ServerWarExecutor.get().checkCastleWar(pc);// 發佈城戰訊息

			war(pc);// 血盟與盟戰資料

			marriage(pc);// 婚姻資料

			// ClanMatching(pc);// 血盟推薦

			// 給予殷海薩祝福指數的處理
			if (LeavesSet.START) { // 殷海薩的祝福-休息系統
				int logintime = (int) (System.currentTimeMillis() / 60 / 1000);// 目前時間換算為(分)
				int minute = logintime - pc.get_other().get_login_time();// 間隔時間(分)
				if ((minute > 0) && (minute / LeavesSet.TIME > 0)) {
					final int addexp = minute / LeavesSet.TIME * LeavesSet.EXP;
					pc.get_other().set_leaves_time_exp(addexp);
					pc.sendPackets(new S_PacketBoxExp(pc.get_other().get_leaves_time_exp() / LeavesSet.EXP, pc));
				}
			}

			if (currentHpAtLoad > pc.getCurrentHp()) {
				pc.setCurrentHp(currentHpAtLoad);
			}

			if (currentMpAtLoad > pc.getCurrentMp()) {
				pc.setCurrentMp(currentMpAtLoad);
			}

			pc.startHpRegeneration();
			pc.startMpRegeneration();

			pc.startObjectAutoUpdate();// PC 可見物更新處理

			pc.beginExpMonitor(); // l1j-tw連續攻擊

			crown(pc);// 送出王冠資料

			pc.save();// 資料回存

			if (pc.getHellTime() > 0) {// 返回地獄的判斷
				pc.beginHell(false);
			}

			//pc.sendPackets(new S_CharResetInfo(pc));// 送出人物屬性資料
			S_CharReset statusinfo = new S_CharReset(pc, 0x04);// 初始能力加成顯示 // 7.6
            pc.sendPackets(statusinfo);
            
			statsReward(pc);// 點數獎勵 7.6

			pc.load_src();// 經驗值、正義值、好友度

			pc.getQuest().load();// 任務進度

			pc.sendPackets(new S_EquipmentSlot(1, 16));
			pc.showWindows();
			
			if (QuestMobSet.START) {
				ServerQuestMobTable.get().getQuestMobNote(pc);
			}

			if (pc.get_food() >= 225) {
				Calendar cal = Calendar.getInstance();
				long h_time = cal.getTimeInMillis() / 1000L;
				pc.set_h_time(h_time);
			}

			if (pc.getLevel() <= ConfigOther.ENCOUNTER_LV) {// 新手保護
				pc.sendPackets(new S_PacketBoxProtection(6, 1));
			}
			pc.lawfulUpdate();// 戰鬥特化狀態圖示更新

			if (EffectAISet.START) { // 特效驗證系統
				if (EffectAISet.AI_TIME_RANDOM != 0 && pc.getAITimer() == 0) {
					Random _random = new Random();
					pc.setAITimer(_random.nextInt(EffectAISet.AI_TIME_RANDOM)
							+ EffectAISet.AI_TIME);
				}
			}

			if (ConfigAlt.WHO_ONLINE_MSG_ON) {
				Collection<L1PcInstance> allplayer = World.get().getAllPlayers();
				for (L1PcInstance object : allplayer) {
					if ((object instanceof L1PcInstance)) {
						L1PcInstance GM = object;
						if ((GM.getAccessLevel() == 100) || (GM.getAccessLevel() == 200)) {
							/*
							 * String msg = ""; if (pc.isCrown()) { msg = "王子";
							 * } else if (pc.isKnight()) { msg = "騎士"; } else if
							 * (pc.isElf()) { msg = "妖精"; } else if
							 * (pc.isWizard()) { msg = "法師"; } else if
							 * (pc.isDarkelf()) { msg = "黑妖"; } else if
							 * (pc.isDragonKnight()) { msg = "龍騎士"; } else if
							 * (pc.isIllusionist()) { msg = "幻術士"; }
							 */
							GM.sendPackets(new S_GmMessage(pc));
							/*
							 * GM.sendPackets(new S_SystemMessage("(玩家" +
							 * pc.getName() + ")(帳號" + client.getAccountName() +
							 * ")" + "(IP" + client.getIp() + ")" + "(職業" + msg
							 * + ")(上線)"));
							 */
						}
					}
				}
			}

			/** GM 上線後自動隱身 */
			if (ConfigAlt.ALT_GM_HIDE) {
				if (pc.isGm() || pc.isMonitor()) {
					pc.setGmInvis(true);
					pc.sendPackets(new S_Invis(pc.getId(), 1));
					pc.broadcastPacketAll(new S_RemoveObject(pc));
					pc.sendPackets(new S_SystemMessage("\\F3啟用線上GM隱身模式。"));
				}
			}
			/** GM 上線後自動隱身 */

			if (CardSet.START) {
				CardSet.load_card_mode(pc);
			}

			deleteIceItem(pc);  //src026

			T_OnlineGiftTable.get().check(pc);

			//pc.setVipStatus();

			if (CampSet.CAMPSTART) { //src011
				L1User_Power value = CharacterC1Reading.get().get(pc.getId());
				if (value != null) {
					pc.set_c_power(value);
					if (value.get_c1_type() != 0) {
						pc.get_c_power().set_power(pc, true);

						pc.sendPacketsAll(new S_ChangeName(pc, true));

						String type = C1_Name_Table.get().get(pc.get_c_power().get_c1_type());
						/* pc.sendPackets(new S_ServerMessage("\\fR您目前所屬的陣營: " +
						 type));
						pc.sendPackets(new S_GmMessage("您目前所屬的陣營: " + type + "", "\\aH")); */

					}
				} /* else if (pc.getMapId() == 99) {
					pc.sendPackets(new S_PacketBoxGree(2, "選擇陣營後即可進入遊戲!"));
				} */
			}

            /*師徒系統取消
			if (ConfigAlt.APPRENTICE_SWITCH) {
				// 檢查是否有收徒弟
				L1Apprentice apprentice = CharApprenticeTable.getInstance().getApprentice(pc);
				// System.out.println(apprentice.getMaster().getName());
				if (apprentice != null) {
					pc.setApprentice(apprentice);
					pc.checkEffect();
				}
			}
			L1Master.getInstance().login(pc);*/
			
			if (ConfigOther.ADENA_CHECK_SWITCH) { // 元寶差異紀錄
				final long adenaCount = pc.getInventory().countItems(44070);
				if (adenaCount > 0) {
					pc.setShopAdenaRecord(adenaCount);
				}
				pc.setSkillEffect(L1SkillId.ADENA_CHECK_TIMER, ConfigOther.ADENA_CHECK_TIME_SEC * 1000);
			}
			
			// 7.6
			if (pc.getClanid() != 0) { // 具有血盟
				final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
				if (clan != null) {
					if (pc.getClanid() == clan.getClanId() && pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {
						// XXX 7.6C ADD
						pc.sendPackets(new S_PledgeName(pc.getClanname(), pc.getClanRank()));
					}
				}
			}

			Tam_Window(pc); // 成長果實系統(Tam幣)

			// 怪物圖鑒171020
			UserMonsterBookLoader.load(pc);
			pc.getMonsterBook().sendList();
			if (Config.Week_Quest) {
				UserWeekQuestLoader.load(pc);
			}

			// 黑名單
			L1ExcludingList exList = SpamTable.getInstance().getExcludeTable(pc.getId());
			if (exList != null) {
				setExcludeList(pc, exList);
			}

			// 安全區域右下顯示死亡懲罰狀態圖示
			safetyzone(pc);

			InvSwapTable.getInstance().toWorldJoin(pc); // 裝備切換
			pc.sendPackets(new S_PacketBox(S_PacketBox.INVENTORY_SAVE)); // 裝備切換

			pc.sendPackets(new S_EventUI(141)); // 活動UI

			securityBuff(pc); // 安全防禦

			// 給予城堡額外附加能力效果 by terry0412
			CastleWarGiftTable.get().login_gift(pc);

			// 守護者系統
			checkforProtector(pc);

			// 戰魂系統
			checkforMars(pc);

			// 檢查身上的妲蒂斯魔石持有狀態
			checkforDADISStone(pc);

			CheckItemPower(pc); // 身上持有道具給予能力系統

			// 取消商店變身
			L1PolyMorph.undoPolyPrivateShop(pc);

			// 刪除20000
			pc.removeAICheck(20000, pc.getAICheck());

			login_Artiface.forIntensifyArmor(pc);

			if (ConfigOther.FREE_FIGHT_SWITCH) { // src015
				StringBuilder sbr = CheckFightTimeController.getInstance().getMapList2();
				if (sbr != null) {
					pc.sendPackets(new S_ServerMessage(sbr.toString()));
				}
			}

			/** [原碼] 定時外掛檢測 */
			if (com.add.L1Config._2226 && !pc.isGm()) {
				if (!ConfigAlt.NO_AI_MAP_LIST.contains(Integer.valueOf(pc.getMapId()))) {
					// 城堡戰爭區域
					if (L1CastleLocation.checkInAllWarArea(pc.getX(), pc.getY(), pc.getMapId())) {
						return;
					}
					// 小屋內座標
					if (L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId())) {
						return;
					}
					// 世界樹下
					if ((pc.getMapId() == 4) && (pc.getLocation().isInScreen(new Point(33055, 32336)))) {
						return;
					}
					// 新人房間
					if (pc.getMapId() == 99) {
						return;
					}
					// 等級低於30
					if (pc.getLevel() <= 30) {
						return;
					}
					// 安全區域不判斷
					if (pc.isSafetyZone()) {
						return;
					}
					pc.setSkillEffect(AI_1, 5 * 1000);
				}
			}

			// if (CheckMail(pc) > 0) {// 檢查未讀信件狀態
			// pc.sendPackets(new S_SkillSound(pc.getId(), 1091));
			// pc.sendPackets(new S_ServerMessage(428));
			// }

			// 精靈的祝賀禮物
			CharacterGiftTable.getInstance().sendPacket(pc);

			getUpdate(pc);// 其他狀態更新

			/*
			 * if (pc.getLevel() <= 10) { pc.sendPackets(new
			 * S_GmMessage("若有圖檔對話不足問題請先吃檔")); }
			 * 
			 * L1Account account = AccountReading.get().getAccount(loginName);
			 * if (account != null && account.get_access_level() == 1) {
			 * AccountReading.get().updateAccessLevel(pc.getAccountName());
			 * 
			 * final L1ItemInstance item = ItemTable.get().createItem(140733);
			 * 
			 * if (item != null) {
			 * 
			 * item.setCount(50);
			 * 
			 * // 加入角色專屬倉庫 DwarfReading.get().insertItem(loginName, item); //
			 * 重整倉庫數據 pc.sendPackets(new S_ServerMessage(166, "管理員在你的個人倉庫加入物品："
			 * + item.getLogName()));
			 * 
			 * pc.getDwarfInventory().loadItems();
			 * 
			 * } }
			 */

			// r = Random.nextInt(30) + 1;
			// pc.setSuper2(r);
			// pc.sendPackets(new S_GmMessage("您目前VIP等級: " + pc.get_vipLevel() + "鑽石VIP", "\\aH"));
			// pc.sendPackets(new S_GmMessage("歡迎進入[幻想世界]仿正伺服器。"));

			// pc.sendPackets(new S_GmMessage("請牢記!您的幸運數字為:"+r, "\\aE"));
			// _log.info("玩家:"+pc.getName()+" 幸運數字:"+pc.getSuper2());

			/*
			 * if(!pc.getInventory().checkItem(44125, 1)){
			 * pc.getInventory().storeItem(44125, 1);
			 * pc.sendPackets(new S_GmMessage("獲得市場中心傳送符。")); //src035
			 * }
			 */

			IceQueenThread.deleteIceItem(pc);
			ValakasRoomSystem.deleteIceItem(pc);
			deleteSoulTowerItem(pc);// 刪除屍魂副本道具

			if (pc.getMapId() == 7783) { // 7783
				pc.sendPackets(new S_TestPacket(S_TestPacket.a, 7072, 3810, "00 ff ff"));// 屏幕顯示歡迎來到天堂世界
			}

			// 官方簽到系統
			if (Config.Attend) {
				CharacterAttendTable.getInstance().LoginAttendProfile(pc);
				pc.sendPackets(new S_AttenDance(S_AttenDance.WatchCreate));
				pc.sendPackets(new S_AttenDance(S_AttenDance.WatchItemList, 0));
				// pc.sendPackets(new S_AttenDance(S_AttenDance.WatchItemList, 1));
				pc.sendPackets(new S_AttenDance(pc, S_AttenDance.WatchPcPorfile, 0));
			}
			// 官方簽到系統end

			QuestNewTable.getInstance().giveQuest(pc); // 官服任務系統

			// 管理者介面byeric1300460
			/*
			 * if (Config.GUI) {
			 * 
			 * String t = charName + "-登入 (IP:" + client.getIp().toString() +
			 * ")(帳號:" + loginName + ")"; J_Main.getInstance().addConsol(t);
			 * 
			 * }
			 */

			// 測試 [技術/精靈/龍屬/恐怖]命中or耐性
			// pc.sendPackets(new S_ACTION_UI(pc, S_ACTION_UI.RESIST, 0x12, 5));//0x12=命中
			// pc.sendPackets(new S_ACTION_UI(pc, S_ACTION_UI.RESIST, 0x0a, 5));//0x0a=耐性

		} catch (Exception localException) {

		} finally {
			over();
		}
	}

	// /**
	// * 檢查是否有未讀信件
	// *
	// * @param pc
	// * @return
	// */
	// private int CheckMail(L1PcInstance pc) {
	// int count = 0;
	// Connection con = null;
	// PreparedStatement pstm1 = null;
	// ResultSet rs = null;
	// try {
	// con = DatabaseFactory.get().getConnection();
	// //pstm1 = con.prepareStatement(" SELECT count(*) as cnt FROM mails where
	// receiver =? AND read_status = 0");
	// pstm1 = con.prepareStatement(" SELECT count(*) as cnt FROM character_mail
	// where receiver =? AND read_status = 0");
	// pstm1.setString(1, pc.getName());
	//
	// rs = pstm1.executeQuery();
	// if (rs.next()) {
	// count = rs.getInt("cnt");
	// }
	//
	// } catch (SQLException e) {
	// _log.error(e.getLocalizedMessage(), e);
	// } finally {
	// SQLUtil.close(rs);
	// SQLUtil.close(pstm1);
	// SQLUtil.close(con);
	// }
	//
	// return count;
	// }

	/**
	 * 王冠資料
	 * 
	 * @param pc
	 */
	private void crown(L1PcInstance pc) {
		try {
			Map<Integer, L1Clan> map = L1CastleLocation.mapCastle();
			for (Integer key : map.keySet()) {
				L1Clan clan = (L1Clan) map.get(key);
				if (clan != null) {
					if (key.equals(Integer.valueOf(2))) {
						pc.sendPackets(new S_CastleMaster(8, clan.getLeaderId()));
					} else
						pc.sendPackets(new S_CastleMaster(key.intValue(), clan.getLeaderId()));
				}
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 遊戲焦點及人物狀態更新
	 * 
	 * @param pc
	 */
	private void getFocus(L1PcInstance pc) {//src014
		try {
			pc.set_showId(-1);// 重置副本編號

			World.get().addVisibleObject(pc);// 將物件增加到MAP世界裡

			if ((ConfigAlt.METE_LEVEL > 0) && (pc.getMeteLevel() > 0)) {// 轉生能力
				pc.resetMeteAbility();
			}
			
			pc.resetPolyPower();

			pc.sendPackets(new S_OwnCharStatus(pc));// 角色資訊

			pc.sendPackets(new S_MapID(pc, pc.getMapId(), pc.getMap().isUnderwater()));// 更新角色所在的地圖

			pc.sendPackets(new S_OwnCharPack(pc));// 物件封包(本身)			

			boolean isTimingmap = MapsTable.get().isTimingMap(pc.getMapId());// 傳送地圖是否為限時地圖
		   
			/** 加入限時地圖清單 */
			if (isTimingmap) { // 是限時地圖
				// 地圖限制時間(秒數)
				final int maxMapUsetime = MapsTable.get().getMapTime(pc.getMapId()) * 60;
				// 已使用秒數
				int usedtime = pc.getMapUseTime(pc.getMapId());
				// 剩餘時間(秒)
				int leftTime = (maxMapUsetime - usedtime);

				MapTimerThread.put(pc, leftTime);
				// System.out.println("加入限時地圖清單");

			} else if (MapTimerThread.TIMINGMAP.get(pc) != null) {// 在清單中
				MapTimerThread.TIMINGMAP.remove(pc);// 移出清單
				
				// System.out.println("移出清單");
			}

			ArrayList<L1PcInstance> otherPc = World.get().getVisiblePlayer(pc);
			if (otherPc.size() > 0) {
				for (L1PcInstance tg : otherPc) {
					tg.sendPackets(new S_OtherCharPacks(pc));//// 物件封包(其他人物)
				}
			}

			pc.setVipStatus();
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 其他狀態更新
	 * 
	 * @param pc
	 */
	private void getUpdate(L1PcInstance pc) {

		// pc.sendPackets(new S_Mail(pc, 0));
		// pc.sendPackets(new S_Mail(pc, 1));
		// pc.sendPackets(new S_Mail(pc, 2));

		pc.sendPackets(new S_SPMR(pc));

		pc.sendPackets(new S_Karma(pc));

		pc.sendPackets(new S_Weather(World.get().getWeather()));

		pc.sendPackets(new S_PacketBox(S_PacketBox.UPDATE_ER, pc.getEr()));// 迴避率更新

		pc.sendPackets(new S_PacketBoxIcon1(true, pc.get_dodge()));// 閃避率更新

	}

	/**
	 * 婚姻資料
	 * 
	 * @param pc
	 */
	private void marriage(L1PcInstance pc) {
		try {
			if (pc.getPartnerId() != 0) {
				L1PcInstance partner = (L1PcInstance) World.get().findObject(pc.getPartnerId());
				if ((partner != null) && (partner.getPartnerId() != 0) && (pc.getPartnerId() == partner.getId())
						&& (partner.getPartnerId() == pc.getId())) {
					pc.sendPackets(new S_ServerMessage(548));

					partner.sendPackets(new S_ServerMessage(549));
				}
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 其他數據
	 * 
	 * @param pc
	 * @throws Exception
	 */
	private void getOther(L1PcInstance pc) throws Exception {
		try {			
			pc.set_otherList(new L1PcOtherList(pc));

			pc.addMaxHp(pc.get_other().get_addhp());
			pc.addMaxMp(pc.get_other().get_addmp());

			// 掛賣獎勵
			OnlineGiftSet.add(pc);
			// 地圖群組設置資料 (入場時間限制)
			int time = pc.get_other().get_usemapTime();
			if (time > 0) {
				// 限時地圖
				if (pc.get_other().get_usemap() != -1 && pc.getMapId() != pc.get_other().get_usemap()) {
					pc.get_other().set_usemapTime(0);
					pc.get_other().set_usemap(-1);
					pc.sendPackets(new S_PacketBoxGame(S_PacketBoxGame.STARTTIMECLEAR));
				} else {
					ServerUseMapTimer.put(pc, time);
				}

			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 升級點數獎勵
	 * 
	 * @param pc
	 */
	private void statsReward(final L1PcInstance pc) { // 點數獎勵
		if ((pc.getLevel() >= 51 && pc.getLevel() - 50 > pc.getBonusStats())
				|| (pc.getLevel() >= 51 && pc.getLevel() - 50 > pc
						.getBonusStats() - 49)) {
			if ((pc.getBaseStr() + pc.getBaseDex() + pc.getBaseCon()
					+ pc.getBaseInt() + pc.getBaseWis() + pc.getBaseCha()) < (ConfigAlt.POWER * 6)) { // 設定能力值上限
				//pc.sendPackets(new S_bonusstats(pc.getId(), 1));
				int bonus = (pc.getLevel() - 50) - pc.getBonusStats();// 可以點的點數 XXX 7.6C ADD
				pc.sendPackets(new S_Message_YN(479, bonus));
			}
		}
	}

	/**
	 * 取得血盟 與 血盟戰爭資料
	 *
	 * @param pc
	 */
	private void war(final L1PcInstance pc) {
		try {
			if (pc.getClanid() != 0) { // 血盟資料不為0
				final L1Clan clan = WorldClan.get().getClan(pc.getClanname());
				if (clan != null) {
					// 判斷血盟名稱和血盟編號相等
					if ((pc.getClanid() == clan.getClanId())
							&& pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {
						final L1PcInstance[] clanMembers = clan.getOnlineClanMember();
						for (final L1PcInstance clanMember : clanMembers) {
							if (clanMember.getId() != pc.getId()) {
								// 843 血盟成員%0%s剛進入遊戲。
								clanMember.sendPackets(new S_ServerMessage(843, pc.getName()));
							}
						}

						// 3.8 血盟 識別盟徽狀態
						pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getShowEmblem()));// 7.6

						// 血盟關注
						if (pc.isCrown() && (pc.getId() == clan.getLeaderId())) { // 盟主
							for (final int clanid : clan.getWatchClanList()) {
								final L1Clan watch_clan = ClanReading.get().getTemplate(clanid);
								if (watch_clan == null) {
									if (clanid > 0) {
										clan.getWatchClanList().remove((Object) clanid);
										ClanReading.get().updateClan(clan);
									}
								}
							}
						}
						pc.sendPackets(new S_PledgeWatch(clan));
						// 血盟關注end

						final int clanMan = clan.getOnlineClanMember().length;
						pc.sendPackets(new S_ServerMessage("\\fU線上血盟成員:" + clanMan));

						if (clan.isClanskill()) {
							switch (pc.get_other().get_clanskill()) {
							case 1:// 狂暴
								pc.sendPackets(new S_ServerMessage(Npc_clan.SKILLINFO[0]));
								break;
							case 2:// 寂靜
								pc.sendPackets(new S_ServerMessage(Npc_clan.SKILLINFO[1]));
								break;
							case 4:// 魔擊
								pc.sendPackets(new S_ServerMessage(Npc_clan.SKILLINFO[2]));
								break;
							case 8:// 消魔
								pc.sendPackets(new S_ServerMessage(Npc_clan.SKILLINFO[3]));
								break;
							}
						}

						ClanSkillDBSet.add(pc);

						// 送出盟輝 7.6取消
						/*final L1EmblemIcon emblemIcon = ClanEmblemReading.get().get(clan.getClanId());
						if (emblemIcon != null) {
							pc.sendPackets(new S_Emblem(emblemIcon));
							// System.out.println("送出盟徽資料 ==" +
							// emblemIcon.get_emblemid());
						}*/

						// 目前全部戰爭資訊取得
						for (final L1War war : WorldWar.get().getWarList()) {
							final boolean ret = war.checkClanInWar(pc.getClanname());
							if (ret) { // 是否正在戰爭中
								final String enemy_clan_name = war.getEnemyClanName(pc.getClanname());
								if (enemy_clan_name != null) {
									// \f1目前你的血盟與 %0 血盟交戰當中。
									pc.sendPackets(new S_War(8, pc.getClanname(), enemy_clan_name));
								}
								break;
							}
						}
					}

				} else {
					pc.setClanid(0);
					pc.setClanname("");
					pc.setClanRank(0);
					pc.save();
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 所在座標位置資料判斷
	 * 
	 * @param pc
	 */
	private void backRestart(final L1PcInstance pc) {
		try {

			// 副本強制回村
			if (pc.getMapId() >= 4001 && pc.getMapId() <= 4050) {// 屍魂塔
				pc.setX(33705);
				pc.setY(32504);
				pc.setMap((short) 4);
			}
			// 指定MAP回村設置
			final L1GetBackRestart gbr = GetBackRestartTable.get()
					.getGetBackRestart(pc.getMapId());
			if (gbr != null) {
				pc.setX(gbr.getLocX());
				pc.setY(gbr.getLocY());
				pc.setMap(gbr.getMapId());
			}

			// 戰爭區域回村設置
			final int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			if (castle_id > 0) {
				if (ServerWarExecutor.get().isNowWar(castle_id)) {
					final L1Clan clan = WorldClan.get().getClan(
							pc.getClanname());
					if (clan != null) {
						if (clan.getCastleId() != castle_id) {
							// 城主
							int[] loc = new int[3];
							loc = L1CastleLocation.getGetBackLoc(castle_id);
							pc.setX(loc[0]);
							pc.setY(loc[1]);
							pc.setMap((short) loc[2]);
						}

					} else {
						// 所屬居場合歸還
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
					}
				}
			}

			//pc.setOleLocX(pc.getX());
			//pc.setOleLocY(pc.getY());
			
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 物品資料
	 * 
	 * @param pc
	 */
	public static void items(L1PcInstance pc) {
		try {
			CharacterTable.restoreInventory(pc);
			/** [原碼] 潘朵拉抽抽樂 */
			pc.sendPackets(new S_Luckylottery(pc.getPandoraInventory().getItems()));
			List<L1ItemInstance> items = pc.getInventory().getItems();
			if (items.size() > 0) {
				pc.sendPackets(new S_InvList(items));
				/*for (final L1ItemInstance item : items) {
					if (item.getItem().getType2() == 0) {
						continue;
					}
				}*/
				pc.getInventory().equippedLoad();
				pc.getInventory().viewItem();
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 取得記憶座標資料
	 *
	 * @param pc
	 */
	private void bookmarks(final L1PcInstance pc) {
		/*try {
			final ArrayList<L1BookMark> bookList = CharBookReading.get().getBookMarks(pc);
			if (bookList != null) {
				final L1BookConfig config = CharBookConfigReading.get().get(pc.getId());
				final int maxSize = ConfigAlt.CHAR_BOOK_INIT_COUNT + (config != null ? config.getMaxSize() : 0);
				pc.sendPackets(new S_Bookmarks(config != null ? config.getData() : null, maxSize, bookList));
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}*/
        L1BookMark.bookmarkDB(pc); // 日版記憶座標
        pc.sendPackets(new S_BookMarkLoad(pc)); // 日版記憶座標
	}

	/**
	 * 人物已學習技能資料
	 * 
	 * @param pc
	 */
	private void skills(final L1PcInstance pc) {
		try {
			final ArrayList<L1UserSkillTmp> skillList = CharSkillReading.get().skills(pc.getId());

			final int[] skills = new int[31];

			if (skillList != null) {
				if (skillList.size() > 0) {
					for (final L1UserSkillTmp userSkillTmp : skillList) {
						// 取得魔法資料
						final L1Skills skill = SkillsTable.get().getTemplate(userSkillTmp.get_skill_id());

						// skills[(skill.getSkillLevel() - 1)] += skill.getId();
						//
						// if (skill.getSkillId() >= 234 && skill.getSkillId() <= 241) {
						// pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.LOGIN, skill.getSkillNumber()));
						// }

						// 880新技能 暫時修改
						if (skill != null && skill.getSkillLevel() > 0 && skill.getSkillLevel() <= 30) {
							skills[(skill.getSkillLevel() - 1)] += skill.getId();
						}

						// 戰士
						if (skill != null && pc.isWarrior() && skill.getSkillLevel() == 31) {
							pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.LOGIN, skill.getSkillNumber()));
							pc.setSkillMastery(skill.getSkillId());
						}
						// 黑妖
						if (skill != null && pc.isDarkelf() && skill.getSkillLevel() == 31) {
							pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.LOGIN, skill.getSkillNumber()));
							pc.setSkillMastery(skill.getSkillId());
						}
						// 騎士
						if (skill != null && pc.isKnight() && skill.getSkillLevel() == 31) {
							pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.LOGIN, skill.getSkillNumber()));
							pc.setSkillMastery(skill.getSkillId());
						}
						// 龍騎士
						if (skill != null && pc.isDragonKnight() && skill.getSkillLevel() == 31) {
							pc.sendPackets(new S_WarriorSkill(S_WarriorSkill.LOGIN, skill.getSkillNumber()));
							pc.setSkillMastery(skill.getSkillId());
						}
					}
					// 送出資料
					pc.sendPackets(new S_AddSkill(pc, skills));
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 殘留的寵物資料
	 * 
	 * @param pc
	 */
	private void serchSummon(L1PcInstance pc) {
		try {
			Collection<L1SummonInstance> summons = WorldSummons.get().all();
			if (summons.size() > 0) {
				for (L1SummonInstance summon : summons) {
					if (summon.getMaster().getId() == pc.getId()) {
						summon.setMaster(pc);
						pc.addPet(summon);
						S_NewMaster packet = new S_NewMaster(pc.getName(), summon);
						for (L1PcInstance visiblePc : World.get().getVisiblePlayer(summon))
							visiblePc.sendPackets(packet);
					}
				}
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 保留的BUFF資料
	 * 
	 * @param pc
	 */
	private void buff(L1PcInstance pc) {
		try {
			CharBuffReading.get().buff(pc);
			pc.sendPackets(new S_PacketBoxActiveSpells(pc));
			CharMapTimeReading.get().getTime(pc);
			//下面新加暫時無效果
			// 地圖群組設置資料 (入場時間限制)
			short mapId = pc.getMapId();
			final L1MapsLimitTime mapsLimitTime = MapsGroupTable.get().findGroupMap(mapId);
			if (mapsLimitTime != null) {
				final int order_id = mapsLimitTime.getOrderId();
				final int used_time = pc.getMapsTime(order_id);
				final int limit_time = mapsLimitTime.getLimitTime();
				final int timecha = limit_time-used_time;
				if (timecha>0) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.MAP_TIMER, timecha));
				}
			}	 
			//上面新加 
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	// 檢查身上是否有守護者之魂
	public static void checkforProtector(L1PcInstance pc) {
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (item.getItemId() == ProtectorSet.ITEM_ID) {// 身上有守護者靈魂
				if (!pc.isProtector()) {// 沒有守護者效果
					pc.setProtector(true);
					break;
				}
			}
		}
	}

	// 檢查身上是否有戰神之魂
	public static void checkforMars(L1PcInstance pc) {
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (item.getItemId() == 56152) {// 身上有戰神之魂
				if (!pc.isMars()) {// 沒有戰神之魂效果
					pc.setMars(true);
					break;
				}
			}
		}
	}

	// 檢查身上的妲蒂斯魔石持有狀態
	public static void checkforDADISStone(L1PcInstance pc) {
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (item.getItemId() == 56147) {// 身上有真 妲蒂斯魔石
				// 身上沒有真妲蒂斯魔石效果
				if (!pc.isEffectDADIS()) {
					pc.setDADIS(true);
					break;
				}
			} else if (item.getItemId() == 56148) {// 身上有妲蒂斯魔石
				// 身上沒有妲蒂斯魔石效果也沒有真妲蒂斯魔石效果
				if (!pc.isEffectGS() && !pc.isEffectDADIS()) {
					pc.setGS(true);
					break;
				}
			}
		}
	}

	/**
	 * 身上持有道具給予能力系統
	 * @param pc
	 */
	public static void CheckItemPower(L1PcInstance pc) {
		for (L1ItemInstance item : pc.getInventory().getItems()) {
			if (CheckItemPowerTable.get().checkItem(item.getItemId())) {
				CheckItemPowerTable.get().givepower(pc, item.getItemId());
			}
		}
	}

	/**
	 * 血盟推薦資料
	 * 
	 * @param pc
	 */
	/*private void ClanMatching(L1PcInstance pc) {
		L1ClanMatching cml = L1ClanMatching.getInstance();
		if (pc.getClanid() == 0) {// 沒有血盟
			if (!pc.isCrown()) {// 不是王族
				// cml.loadClanMatchingApcList_User(pc);
				if (!cml.getMatchingList().isEmpty()) {
					pc.sendPackets(new S_ServerMessage(3245)); // 目前有血盟等待著您。
				}
			} else {
				pc.sendPackets(new S_ServerMessage(3247)); // 請創設血盟並簡單的告知
			}
		} else {// 有血盟
			switch (pc.getClanRank()) {
			case L1Clan.CLAN_RANK_LEAGUE_VICEPRINCE:
			case L1Clan.CLAN_RANK_LEAGUE_PRINCE:
			case L1Clan.CLAN_RANK_LEAGUE_GUARDIAN:
			case L1Clan.CLAN_RANK_GUARDIAN:
			case L1Clan.CLAN_RANK_PRINCE:
				// cml.loadClanMatchingApcList_Crown(pc);
				if (!pc.getInviteList().isEmpty()) {
					pc.sendPackets(new S_ServerMessage(3246)); // 目前有血盟成員等待著您
				}
				break;
			}

			pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_EMBLEM_STATUS, pc.getClan().getEmblemStatus()));// 盟徽識別狀態
		}
	}*/

	/** 刪除任務道具 */
	public static void deleteIceItem(L1PcInstance _pc) {
		if (_pc != null && (_pc.getLevel() >= 1)) {
			L1ItemInstance[] item = _pc.getInventory().findItemsId(5010);
			if (item != null && item.length > 0) {
				for (int i = 0; i < item.length; i++) {
					_pc.getInventory().removeItem(item[i]);
				}
			}
		}
	}

	/**
	 * 刪除屍魂副本道具
	 * @param _pc
	 */
	private static void deleteSoulTowerItem(L1PcInstance _pc) {
		//刪除副本道具
		// 640319=下層雷擊爆彈
		// 640320=下層旋風爆彈
		// 640321=下層戰鬥強化卷軸
		// 640322=下層防禦強化卷軸
		// 640323=下層治癒藥水
		// 640324=下層強力治癒藥水
		// 640325=下層魔力藥水
		for (int i = 640319; i <= 640325; i++) {
			final L1ItemInstance[] itemlist = _pc.getInventory().findItemsId(i);
			if (itemlist != null && itemlist.length > 0) {
				for (final L1ItemInstance item : itemlist) {
					_pc.getInventory().removeItem(item);
				}
			}
		}
	}

	/**
	 * 黑名單
	 * 
	 * @param pc
	 * @param exList
	 */
    private void setExcludeList(L1PcInstance pc, L1ExcludingList exList) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM character_exclude WHERE char_id = ?");
            pstm.setInt(1, pc.getId());
            rs = pstm.executeQuery();

            while (rs.next()) {
                int type = rs.getInt("type");
                String name = rs.getString("exclude_name");
                if (!exList.contains(type, name)) {
                    exList.add(type, name);
                }
            }
        } catch (SQLException e) {
           // _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
    
	/**
	 * 安全區域右下顯示死亡懲罰狀態圖示
	 * 
	 * @param pc
	 */
    private void safetyzone(L1PcInstance pc) {
        if (pc.getZoneType() == 0) {
            if (pc.getSafetyZone() == true) {
                pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, false));
                pc.setSafetyZone(false);
            }
        } else {
            if (pc.getSafetyZone() == false) {
                pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SAFETYZONE, true));
                pc.setSafetyZone(true);
            }
        }
    }

	/**
	 * 安全防禦
	 * 
	 * @param pc
	 */
	private void securityBuff(L1PcInstance pc) {
		pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_SECURITY_SERVICES));
		pc.addAc(-1);
		pc.sendPackets(new S_OwnCharAttrDef(pc));
	}

	/**
	 * 7.6空身屬性額外獎勵
	 * 
	 * @param pc
	 */
	private void Reward_stats(L1PcInstance pc) {
		// 7.6 空身智力額外獎勵
		if (pc.getBaseInt() >= 25 && pc.getBaseInt() <= 34) {
		    pc.addSp(1); // 魔攻+1
		} else if (pc.getBaseInt() >= 35 && pc.getBaseInt() <= 44) {
		    pc.addSp(2); // 魔攻+2
		} else if (pc.getBaseInt() >= 45) {
		    pc.addSp(3); // 魔攻+3
		}
    }

    /**
     * 成長果實系統(Tam幣)
     * 
     * @param pc
     */
    private void Tam_Window(L1PcInstance pc) {

        pc.sendPackets(new S_TamWindow(pc)); // 顯示TAM幣點數

        int tamcount = pc.tamcount();
        if (tamcount > 0) {
            long tamtime = pc.TamTime();

            int aftertamtime = (int) tamtime;

            if (aftertamtime < 0) {
                aftertamtime = 0;
            }

            if (tamcount == 1) {
                pc.setSkillEffect(L1SkillId.Tam_Fruit1, aftertamtime);
                pc.sendPackets(new S_TamWindow(6100, true, 4181, aftertamtime));
                pc.addAc(-1);
            } else if (tamcount == 2) {
                pc.setSkillEffect(L1SkillId.Tam_Fruit2, aftertamtime);
                pc.sendPackets(new S_TamWindow(6547, true, 4182, aftertamtime));
                pc.addAc(-2);
            } else if (tamcount == 3) {
                pc.setSkillEffect(L1SkillId.Tam_Fruit3, aftertamtime);
                pc.sendPackets(new S_TamWindow(6546, true, 4183, aftertamtime));
                pc.addAc(-3);
            }

            pc.sendPackets(new S_OwnCharStatus(pc));
        }
    }

	public String getType() {
		return getClass().getSimpleName();
	}

}
