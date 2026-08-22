package com.lineage.server.serverpackets;

import com.lineage.server.datatables.SoulTowerTable.SoulTowerRank;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Craft;
import com.lineage.server.templates.L1QuestNew;
import com.lineage.server.utils.LineageUtil;

import l1j.server.server.datas.protobuf.PBMessageALL;
import l1j.server.server.datas.protobuf.PBMessageALL3;
import l1j.server.server.datas.protobuf.PBMessageALL4;
import l1j.server.server.datas.protobuf.PBMessageALL5;
import l1j.server.server.datas.protobuf.PBMessageALL8;

public class S_ProtoBuffers extends ServerBasePacket {

	//private static Logger _log = Logger.getLogger(S_ProtoBuffers.class.getName());

	// 道具製作
	public static final int CRAFT_LIST = 55;

	// 取得道具製作明細
	public static final int CRAFT_GET = 57;

	// 道具製作結果:用途是扣掉"一個單位"的制做道具的資訊
	public static final int CRAFT_RESULT = 59;

	// 釣魚時間(光棒)
	public static final int FISHING_TIME = 63;

	public static final int WAR_EMBLEM = 65;
	// [MSG_0] 1= int= 764471
	// [MSG_0] 2= int= 1

	// XXX 已佔領城堡的血盟如下
	public static final int WAR_UNKNOW = 68;

	// 攻/守城資訊
	public static final int WAR_INFO = 76;

	// 道具製作時間
	public static final int CRAFT_TIME = 93;
	// 0000: 18 5d 00 08 00 10 1d 24 80

	// 潘朵拉的抽抽樂

	public static final int LUCKY_DRAW = 101;

	// 特羅斯廣播
	public static final int DEPOROJU_TALK2 = 102;

	// 帶圖片的訊息
	public static final int DEPOROJU_TALK1 = 103;// 無%s

	// XXX 被指定的血盟名可攻擊
	public static final int WAR_ATTACK_CLAN = 104;

	// 新的活動圖示
	public static final int NEW_ICON = 110;

	public static final int SHIFT_SERVER = 113; // 從原本伺服器轉至活動伺服器

	public static final int SHIFT_LOGIN_LOCK = 116; // 轉換伺服器的鎖定畫面 (等待人物轉換成功登入)

	public static final int MAPID = 118;

	public static final int OBJ_PACK = 119;

	public static final int WHO = 120; // XXX 0000: a6 78 00 10 e5 18 00 02

	// 魔法娃娃合成
	public static final int PROMOTE_DOLL = 123;
	public static final int PROMOTE_DOLL_RESULT = 125;
	public static final int PROMOTE_DOLL_BEGIN = 128;

    public static final int PCBANG_SET = 126; // 新排行系統

	public static final int THEBES_RANK = 133; // 底比斯排名

	// 活動通知
	public static final int ACTIVITY_NEWS = 141;

	public static final int HTML_TELEPORT_LOCK = 145; // 用於傳送師傳送時顯示傳送特效

	public static final int CASTLE_INFO = 318;
	// 表情符號
	public static final int EMOTICONS = 320;
	// 血盟介面 ?
	public static final int PLEDGE_SETTING3 = 325;
	// 血盟招募-變更的結果
	public static final int PLEDGE_SETTING2 = 327;
	// 血盟招募-變更
	public static final int PLEDGE_SETTING = 333;

	public static final int SOUL_TOEWR_RANK = 335;

	// 好友名單
	public static final int QUERY_BUDDY = 337;

	// 組隊標記
	public static final int PARTY_MARK = 339;
	// 18 53 01 08 dd f9 1a 10 05 1d b0

	// 戰士被動(登入)
	public static final int ADDSKILL_LOGIN = 401;
	// 戰士被動
	public static final int ADDSKILL_NEW = 402;

	// tam點數
	public static final int TAM_POINT = 450;

	// 手游資訊
	public static final int TAM_INFO = 461;

	// 新的圖示
	public static final int NEW_ICON2 = 463;

	public static final int ABILITY_INFO = 483; // 初始能力資料
	public static final int WEIGHT = 485; // 負重能力
	public static final int BASE_ABILITY_GUIDE = 487; // 基礎能力附加效果

	public static final int ELIXIR_COUNT = 489; // 萬能藥使用數量

	public static final int BASE_ABILITY = 490; // 基礎能力

	public static final int CHAT_SELF = 515;
	public static final int CHAT = 516;

	public static final int QUEST_BEGIN = 518; // 任務開始
	public static final int QUEST_UPDATE = 519; // 任務更新
	public static final int QUEST_GUIDE = 521; // 任務指引
	public static final int QUEST_END = 525; // 任務結束

	public static final int CLAN_NAME = 537;

	public static final int PARTY_ADD_MEMBER = 539;

	public static final int COUNT_DOWN = 540; // 左上角倒數計時+DESC(原本用於底比斯大戰)

	public static final int MONSTER_LIST1 = 559; // 怪物圖鑒1-獎勵狀態

	public static final int MONSTER_LIST2 = 560; // 怪物圖鑒2

	public static final int MONSTER_LIST_STAGE_OK = 564; // 怪物圖鑒階段領取

	public static final int MONSTER_LIST_ADD = 567; // 怪物圖鑒ADD

	public static final int MONSTER_LIST_STAGE = 568; // 怪物圖鑒階段
	// S_EXTENDED_PROTOBUF-568 (71:14) 2016.12.11 21:30:02
	// 0000: 47 38 02 08 ea 03 10 9c a9 b5 c2 05 34 20
	// [MSG_0] 1= int= 490 一個編號3個階段
	// [MSG_0] 2= int= 1481462940
	public static final int ADEN_TEL = 579; // 傳送師地圖

	public static final int EQUIP_CHANGE = 800; // 裝備切換

	public static final int LIMIT_MAP = 803; // 地圖剩餘時間

	public static final int MONSTER_LIST_WEEK = 810;

	public static final int MONSTER_LIST_WEEK_COUNT = 813;

	public static final int MONSTER_LIST_WEEK_STAGE = 814;

	public S_ProtoBuffers(final int type) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {

		case PCBANG_SET: // 新排行系統
			final PBMessageALL.type1.Builder builderb = PBMessageALL.type1.newBuilder();
			builderb.setValue1(0);
			builderb.setValue2(1);
			writeByte(builderb.build().toByteArray());
			break;
		}
		writeH(0x00);
	}

	public S_ProtoBuffers(final int type, final int value) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);

		switch (type) {

		// 官服任務系統
		case QUEST_UPDATE: // 任務更新
		case QUEST_GUIDE: // 任務指引
		case QUEST_END: // 任務結束
			final PBMessageALL.type1.Builder bu11 = PBMessageALL.type1.newBuilder();
			bu11.setValue1(0);
			bu11.setValue2(value);
			writeByte(bu11.build().toByteArray());
			writeH(0x00);
			break;

		case CRAFT_TIME: // 道具製作時間
			// writeC(0x00);
			// [MSG_0] 1= int= 0
			// [MSG_0] 2= int= 29
			final PBMessageALL.type1.Builder builderc = PBMessageALL.type1.newBuilder();
			builderc.setValue1(0);// 連續製造的量(?)
			builderc.setValue2(value); // id 會反應至C包上

			writeByte(builderc.build().toByteArray());
			writeH(0);
			break;
		case CRAFT_LIST: // 道具製作
			// writeC(0x00);
			writeC(0x08);
			writeC(value);// craftinfo.dat-> 0:create 1:edit 2:save 3:load
			writeH(0);
			break;

		// c8 cf 01 08 00 10 00 18 00 ab fd 消失
		// c8 cf 01 08 80 01 10 00 18 00 9a 8a 出現
		case NEW_ICON2:
			// writeC(0x01);
			final PBMessageALL.type1.Builder builder1 = PBMessageALL.type1.newBuilder();
			builder1.setValue1(value); // boolean
			builder1.setValue2(0);
			builder1.setValue3(0);
			writeByte(builder1.build().toByteArray());
			writeH(0);
			break;

		case NEW_ICON: // 解除圖示用
			// writeC(0x00);
			final PBMessageALL.type1.Builder builder = PBMessageALL.type1.newBuilder();
			builder.setValue1(0x03); // 0x01 0x02顯示圖示 0x03解除圖示
			builder.setValue2(value); // id-重複會覆蓋0~609固定 610以後可自訂圖案(setValue5)跟敘述(setValue8)
			builder.setValue3(0); // time
			builder.setValue4(0);// 單位
			builder.setValue5(0); // tbt icon
			builder.setValue6(0); // 狀態圖示效果結束提示TBT編號(offIcon)
			builder.setValue7(1); // 狀態圖示排列優先等級 排列等級最高1最低5(priority)
			builder.setValue8(0); // string-c
			builder.setValue9(0);// 狀態開始訊息編號string-c.tbl(startMsg)
			builder.setValue10(0);// 狀態結束訊息編號string-c.tbl(endMsg)
			builder.setValue11(0);// 狀態圖示類型 正面(跟第一排對齊):0x01 負面(跟第二排對齊):0x00

			writeByte(builder.build().toByteArray());
			writeH(0x00);
			break;
		}
	}

	public S_ProtoBuffers(final int type, final L1Craft craft, final L1ItemInstance craft_item, final int value) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {

		case CRAFT_RESULT:
			// writeC(0x00);
			final PBMessageALL4.type12.Builder builder12 = PBMessageALL4.type12.newBuilder();
			// 0:道具製作成功
			// 1:道具製作失敗
			// 2:材料道具錯誤
			// 3:金幣不足
			// 4:物品欄空間不足
			// 5:超過角色負重
			// 6:錯誤的製作請求
			// 7:玩家的能力不足
			// 8:錯誤的製作請求
			// 9:製作所需要的道具不足
			// 10:錯誤的製作請求導致製作失敗
			// 11:製作材料道具不足
			// 12:製作材料選項道具不足
			// 13:製作 NPC在範圍之外
			// 14:暫時中斷該製作服務.

			builder12.setValue1(value);
			builder12.setArray2(craft.getCraftItemData(craft_item, false));
			builder12.setArray3(craft.getIntArray(0, 0));
			writeByte(builder12.build().toByteArray());
			writeH(0);
			break;
		}
	}

	// 寫入道具清單
	public S_ProtoBuffers(final int type, final L1Craft craft, final int value) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case CRAFT_LIST:
			// writeC(0x00);
			writeC(0x08);
			writeC(value);
			final PBMessageALL3.type10.Builder builder = PBMessageALL3.type10.newBuilder();
			builder.addArray2(craft.getByteString());
			writeByte(builder.build().toByteArray());
			writeH(0);
			break;

		}

	}

	// public S_ProtoBuffers(final int type, final int value, final int value2)
	// {
	// writeC(S_OPCODE_EXTENDED_PROTOBUF);
	// writeH(type);
	// switch (type) {
	// case FISHING_TIME:
	// // writeC(0);
	// final PBMessageALL.type1.Builder builder =
	// PBMessageALL.type1.newBuilder();
	// builder.setValue1(1); // 2:?? 正服結束時會出現
	// builder.setValue2(value);// sint
	// builder.setValue3(value2); // 1:未安裝線軸 2:安裝線軸
	// writeByte(builder.build().toByteArray());
	// writeH(0);
	// break;
	// case WAR_EMBLEM:
	// // writeC(0);
	// final PBMessageALL.type1.Builder builderm =
	// PBMessageALL.type1.newBuilder();
	// builderm.setValue1(value);
	// builderm.setValue2(value2); // 1:紅騎士 2:黑騎士 3:取消
	// writeByte(builderm.build().toByteArray());
	// writeH(0);
	// break;
	// }
	// }

	public S_ProtoBuffers(final int type, final int i, final boolean ck, final long time) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case FISHING_TIME:
			// writeC(0);
			final PBMessageALL.type1.Builder builder = PBMessageALL.type1.newBuilder();
			if (1 == i) {
				builder.setValue1(i); // 2:?? 正服結束時會出現
				builder.setValue2((int) time);// sint
				builder.setValue3(ck ? 2 : 1); // 1:未安裝線軸 2:安裝線軸
			} else {
				builder.setValue1(1); // 2:?? 正服結束時會出現
			}
			writeByte(builder.build().toByteArray());
			writeH(0);
			break;
		}
	}

	public S_ProtoBuffers(final int type, final String... data) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);

		switch (type) {
		case CRAFT_GET: // 道具製作的編號
			// writeC(0x00);

			final PBMessageALL3.type10.Builder builder10 = PBMessageALL3.type10.newBuilder();
			builder10.setValue1(0);
			builder10.setValue3(0);

			for (final String s : data) {
				if (s.trim().length() == 0) {
					continue;
				}

				final PBMessageALL.type1.Builder builder1 = PBMessageALL.type1.newBuilder();
				builder1.setValue1(Integer.parseInt(s));
				builder1.setValue2(0);
				builder1.setValue3(0);
				builder10.addArray2(builder1.build().toByteString());
			}

			writeByte(builder10.build().toByteArray());
			writeH(0x00);
			break;
		}
	}

	public S_ProtoBuffers(final int type, final L1PcInstance pc) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case CRAFT_GET: // 道具製作的編號
			// 沒有符合製作條件的清單
			final PBMessageALL3.type10.Builder builder10 = PBMessageALL3.type10.newBuilder();
			builder10.setValue1(0);
			builder10.setValue3(0);
			writeByte(builder10.build().toByteArray());
			writeH(0x00);
			break;
		}
	}

	// 屍魂塔排名
	public S_ProtoBuffers(final SoulTowerRank... ranks) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(SOUL_TOEWR_RANK);

		final PBMessageALL4.type15.Builder builder15 = PBMessageALL4.type15.newBuilder();

		for (final SoulTowerRank rank : ranks) {

			final PBMessageALL8.typeSoulTower.Builder builder8 = PBMessageALL8.typeSoulTower.newBuilder();

			builder8.setArray1(LineageUtil.getByteString(rank.name)); // name
			builder8.setValue2(rank.classType);// class
			builder8.setValue3(rank.time);// clear time (s)
			builder8.setValue4((int) (rank.date / 1000));// date
			builder8.setValue5(1); // bool 0:clear 1:show

			// 歷史紀錄
			if (builder15.getArray1Count() < 3) {
				builder15.addArray1(builder8.build().toByteString());
			}

			// 目前
			if (builder15.getArray2Count() < 10) {
				builder15.addArray2(builder8.build().toByteString());
			}
		}

		writeByte(builder15.build().toByteArray());
		writeH(0);
	}

	/**
	 * 新的icon格式
	 * @param id // id-重複會覆蓋0~609固定 610以後可自訂圖案(setValue5)跟敘述(setValue8)
	 * @param time // 0x01 0x02顯示圖示 0x03解除圖示 // time (-1= 無限時間)
	 * @param unit // 單位<br>
	 * 單位<br>
	 * 0x00:剩餘時間(--秒) 0x01:剩餘的祝福指數(%百分比)<br>
	 * 0x02:剩餘時間(分) 0x03:無顯示 0x04:剩餘時間(--秒換算分)0x05:無顯示 0x06:剩餘時間(--秒)倒數會閃爍<br>
	 * 0x07:--日:時:分 0x08:--分:秒 0x0a:--<br><br>
	 * @param icon // tbt icon
	 * @param iconEnd // 狀態圖示效果結束提示TBT編號(offIcon)
	 * @param stringc // string-c
	 * @param startMsg // 狀態開始訊息編號string-c.tbl(startMsg)
	 * @param endMsg // 狀態結束訊息編號string-c.tbl(endMsg)
	 * @param seq // 狀態圖示排列優先等級 排列等級最高1最低5(priority)
	 */
	public S_ProtoBuffers(final int id, final int time, final int unit, final int icon, final int iconEnd,
			final int stringc, final int startMsg, final int endMsg, final int seq) {
		// 王者XX icon
		// 0000: c8 6e 00 08 01 10 b9 13 18 88 0e 20 08 28 f6 22
		// 0010: 30 00 38 01 40 a3 22 48 00 50 00 58 01 0f d2
		// 王者XXmsg:
		// 1: 1 2: 2489 3: 1800 4: 8 5: 4470 6: 0 7: 1 8: 4387 9: 0 10: 0 11: 1
		// S_ProtoBuffers(2489,1800,4470,4387)

		// 大地女神的祝福 icon
		// 0000: c8 6e 00 08 01 10 cb 13 18 88 0e 20 08 28 ae 26
		// 0010: 30 00 38 01 40 bf 22 48 00 50 00 58 01 bb 04

		// S_ProtoBuffers(2507,1800,4910,4415)
		// 成長之環
		// [MSG_0] 1= int= 2
		// [MSG_0] 2= int= 2272
		// [MSG_0] 3= int= 1350301
		// [MSG_0] 4= int= 8
		// [MSG_0] 5= int= 6100
		// [MSG_0] 6= int= 0
		// [MSG_0] 7= int= 3
		// [MSG_0] 8= int= 3913
		// [MSG_0] 9= int= 4181
		// [MSG_0] 10= int= 0
		// [MSG_0] 11= int= 1
		writeC(S_EXTENDED_PROTOBUF);
		writeH(NEW_ICON);

		final PBMessageALL.type1.Builder builder = PBMessageALL.type1.newBuilder();
		builder.setValue1(time == 0 ? 3 : 1); // 0x01 0x02顯示圖示 0x03解除圖示
		builder.setValue2(id); // id-重複會覆蓋0~609固定 610以後可自訂圖案(setValue5)跟敘述(setValue8)
		builder.setValue3(time); // time (-1= 無限時間)
		builder.setValue4(unit);// 單位
		// 0x00:剩餘時間(--秒) 0x01:剩餘的祝福指數(%百分比)
		// 0x02:剩餘時間(分) 0x03:無顯示 0x04:剩餘時間(--秒換算分)0x05:無顯示 0x06:剩餘時間(--秒)倒數會閃爍
		// 0x07:--日:時:分 0x08:--分:秒 0x0a:--

		builder.setValue5(icon); // tbt icon
		builder.setValue6(iconEnd); // 狀態圖示效果結束提示TBT編號(offIcon)
		builder.setValue7(seq); // 狀態圖示排列優先等級 排列等級最高1最低5(priority)
		builder.setValue8(stringc); // string-c
		builder.setValue9(startMsg);// 狀態開始訊息編號string-c.tbl(startMsg)
		builder.setValue10(endMsg);// 狀態結束訊息編號string-c.tbl(endMsg)
		builder.setValue11(1);// 狀態圖示類型 正面(跟第一排對齊):0x01 負面(跟第二排對齊):0x00
		//

		writeByte(builder.build().toByteArray());
		writeH(0x00);
	}

	// 官服任務系統
	public S_ProtoBuffers(final int type, final L1QuestNew qn) {
		writeC(S_EXTENDED_PROTOBUF);
		writeH(type);

		final PBMessageALL5.type16.Builder builder16 = PBMessageALL5.type16.newBuilder();
		builder16.setValue1(qn.getId());
		builder16.setValue2(qn.getOwner().getId());
		////
		final PBMessageALL.type1.Builder builder1 = PBMessageALL.type1.newBuilder();
		int index = 1;
		if (qn.get達到等級() > 0) {
			builder1.setValue1(index++);
			builder1.setValue2(qn.get目前等級());
			builder1.setValue3(qn.get達到等級());
			builder16.addArray4(builder1.build().toByteString());
		}
		if (qn.get獵殺怪物編號().length > 0) {
			for (int i = 0; i < qn.get獵殺怪物編號().length; i++) {
				builder1.setValue1(index++);
				builder1.setValue2(qn.get目前獵殺怪物數量()[i]);
				builder1.setValue3(qn.get獵殺怪物數量()[i]);
				builder16.addArray4(builder1.build().toByteString());
			}
		}
		if (qn.get獲得道具編號().length > 0) {
			for (int i = 0; i < qn.get獲得道具編號().length; i++) {
				builder1.setValue1(index++);
				builder1.setValue2(qn.get目前獲得道具數量()[i]);
				builder1.setValue3(qn.get獲得道具數量()[i]);
				builder16.addArray4(builder1.build().toByteString());
			}
		}
		if (qn.get使用道具編號().length > 0) {
			for (int i = 0; i < qn.get使用道具編號().length; i++) {
				builder1.setValue1(index++);
				builder1.setValue2(qn.get目前使用道具數量()[i]);
				builder1.setValue3(qn.get使用道具數量()[i]);
				builder16.addArray4(builder1.build().toByteString());
			}
		}
		////
		builder16.setValue5(1); // unknow

		final PBMessageALL5.type17.Builder builder = PBMessageALL5.type17.newBuilder();
		builder.setArray1(builder16.build().toByteString());

		writeByte(builder.build().toByteArray());
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return _bao.toByteArray();
	}

	@Override
	public String getType() {
		return "S_ProtoBuffers";
	}
}
