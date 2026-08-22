package com.lineage.data.cmd;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRecord;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxGree;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import william.server_lv;

/**
 * 
 * @author daien
 * 
 */
public class EnchantArmor extends EnchantExecutor {

	private static final Log _log = LogFactory.getLog(EnchantArmor.class);

	/**
	 * 強化失敗
	 * @param pc 執行者
	 * @param item 對像物件
	 */
	@Override
	public void failureEnchant(final L1PcInstance pc, final L1ItemInstance item) {
		final StringBuilder s = new StringBuilder();

		/*if (ConfigRecord.LOGGING_BAN_ENCHANT) {
			LogEnchantReading.get().failureEnchant(pc, item);
		} */

		if (!item.isIdentified()) {
			s.append(item.getName());

		} else {
			s.append(item.getLogName());
		}

		if (item.get_hasProtect() != 0) {

			ConfigRecord.recordToFiles("保護裝備紀錄", "IP("
					+ pc.getNetConnection().getIp() + ")玩家:【 " + pc.getName()
					+ " 】的【" + item.getRecordName(item.getCount())
					+ ", (ObjectId: " + item.getId() + ")】 保護卷軸效果消失, 時間:("
					+ new Timestamp(System.currentTimeMillis()) + ")");

			if (item.get_hasProtect() == 1) {
				item.setEnchantLevel(0);
				pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
				pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
				pc.sendPackets(new S_ServerMessage(s.toString() + " 因為保護效果，歸零了。"));
				item.set_hasProtect(0);
				pc.sendPackets(new S_ItemName(item));
				server_lv.forIntensifyArmor(pc, item);//// src056
				return;
			}

			if (item.get_hasProtect() == 2) {
				item.setEnchantLevel(item.getEnchantLevel() - 1);
				pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
				pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
				pc.sendPackets(new S_ServerMessage(s.toString() + " 因為保護效果，降階了。"));
				item.set_hasProtect(0);
				pc.sendPackets(new S_ItemName(item));
				server_lv.forIntensifyArmor(pc, item);//// src056
				return;
			}

			if (item.get_hasProtect() == 3) {
				pc.sendPackets(new S_ServerMessage(s.toString() + " 因為保護效果，保留了。"));
				item.set_hasProtect(0);
				pc.sendPackets(new S_ItemName(item));
				return;
			}

		}

		pc.sendPackets(new S_ServerMessage(164, s.toString(), "$252"));
		pc.getInventory().removeItem(item, item.getCount());

		_log.info("人物:" + pc.getName() + "點爆物品" + item.getItem().getName()
				+ " 物品OBJID:" + item.getId());
		// 記錄文件檔 by terry0412
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ConfigRecord.recordToFiles(
				"強化防具紀錄",
				"IP("
						+ pc.getNetConnection().getIp()
						+ ")玩家【"
						+ pc.getName()
						+ "】的【"
						+ item.getRecordName(item.getCount())
						+ ", (ObjId: " + item.getId()
						+ ")】強化失敗, 時間:(" + timestamp + ")",timestamp);
		
	}

	/**
	 * 強化成功
	 * @param pc 執行者
	 * @param item 對像物件
	 * @param i 強化質
	 */
	@Override
	public void successEnchant(final L1PcInstance pc, final L1ItemInstance item, final int i) {
		final StringBuilder s = new StringBuilder();
		final StringBuilder sa = new StringBuilder();
		final StringBuilder sb = new StringBuilder();
		/*if (!item.isIdentified()) {
			s.append(item.getName());
		} else if (item.getEnchantLevel() > 0) {
			s.append("+" + item.getEnchantLevel() + " " + item.getName());
		} else if (item.getEnchantLevel() < 0) {
			s.append(item.getEnchantLevel() + " " + item.getName());
		} else {
			s.append(item.getName());
		}*/
		// 未鑒定
		if (!item.isIdentified()) {
			s.append(item.getName());

		} else {
			if (item.getEnchantLevel() > 0) {
				s.append("+" + item.getEnchantLevel() + " " + item.getName());

			} else if (item.getEnchantLevel() < 0) {
				s.append(item.getEnchantLevel() + " " + item.getName());

			} else {
				s.append(item.getName());
			}
		}
		if (item.get_hasProtect() != 0) {
			pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
			item.set_hasProtect(0);
			pc.sendPackets(new S_ItemName(item));

		}
		switch (i) {
		case 0:
			// \f1%0%s %2 產生激烈的 %1 光芒，但是沒有任何事情發生。
			pc.sendPackets(new S_ServerMessage(160, s.toString(), "$252",
					"$248"));
			return;
		case -1:
			sa.append("$246");// 黑色的
			sb.append("$247");// 一瞬間發出
			break;

		case 1: // '\001'
			sa.append("$252");// 銀色的
			sb.append("$247");// 一瞬間發出
			break;

		case 2: // '\002'
		case 3: // '\003'
			sa.append("$252");// 銀色的
			sb.append("$248");// 持續發出
			break;
		}

		// 161 \f1%0%s %2 %1 光芒。
		pc.sendPackets(new S_ServerMessage(161, s.toString(), sa.toString(), sb
				.toString()));

		final int oldEnchantLvl = item.getEnchantLevel();// 原追加值
		final int newEnchantLvl = oldEnchantLvl + i;// 新追加值

		if (oldEnchantLvl != newEnchantLvl) {
			item.setEnchantLevel(newEnchantLvl);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
			//if (ConfigAlt.enchantoverTF=true && (newEnchantLvl - item.getItem().get_safeenchant()) >=ConfigAlt.enchantovertell2) {// 強化值等於或超過9
			if (ConfigAlt.enchantoverTF
					&& (newEnchantLvl - item.getItem().get_safeenchant()) >= ConfigAlt.enchantovertell2) {// 強化值等於或超過9
				// 產生訊息封包 (強化成功)
				if (ConfigAlt.enchantoverType == 0) {
					World.get().broadcastPacketToAll(new S_HelpMessage("公告:" + pc.getName(),
							s.toString() + " " + sb.toString() + " " + sa.toString() + " $251"));
				} else {
					World.get().broadcastPacketToAll(new S_PacketBoxGree(2,
							"公告:" + pc.getName() + s.toString() + " " + sb.toString() + " " + sa.toString() + " $251"));
				}
			}

			// 記錄文件檔 by terry0412
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ConfigRecord.recordToFiles(
					"強化防具紀錄",
					"IP("
							+ pc.getNetConnection().getIp()
							+ ")玩家【"
							+ pc.getName()
							+ "】的【"
							+ item.getRecordName(item.getCount())
							+ ", (ObjId: " + item.getId()
							+ ")】強化成功, 時間:(" + timestamp + ")",timestamp);
		

			if (item.isEquipped()) {
				// 取得物件觸發事件
				final int use_type = item.getItem().getUseType();
				switch (use_type) {
				case 2:// 盔甲
				case 22:// 頭盔
				case 19:// 斗篷
				case 18:// T恤
				case 20:// 手套
				case 21:// 靴
				case 25:// 盾牌
					pc.addAc(-i);

					boolean isSPMR = false;

					final int mr = item.getMr();
					if (mr != 0) {
						pc.addMr(i * item.getItem().getInfluenceMr());
						isSPMR = true;
					}

					final int influence_sp = item.getItem().getInfluenceSp();
					if (influence_sp != 0) {
						pc.addSp(i * influence_sp);
						isSPMR = true;
					}

					if (isSPMR) {
						pc.sendPackets(new S_SPMR(pc));
					}

					final int influence_hp = item.getItem().getInfluenceHp();
					if (influence_hp != 0) {
						pc.addMaxHp(i * influence_hp);
						pc.sendPackets(new S_HPUpdate(pc));
					}

					final int influence_mp = item.getItem().getInfluenceMp();
					if (influence_mp != 0) {
						pc.addMaxMp(i * influence_mp);
						pc.sendPackets(new S_MPUpdate(pc));
					}

					final int influence_dmgR = item.getItem().getInfluenceDmgR();
					if (influence_dmgR != 0) {
						pc.addDamageReductionByArmor(i * influence_dmgR);
					}

					final int influence_hitAndDmg = item.getItem().getInfluenceHitAndDmg();
					if (influence_hitAndDmg != 0) {
						pc.addHitup(i * influence_hitAndDmg);
						pc.addDmgup(i * influence_hitAndDmg);
					}

					final int influence_bowHitAndDmg = item.getItem().getInfluenceBowHitAndDmg();
					if (influence_bowHitAndDmg != 0) {
						pc.addBowHitup(i * influence_bowHitAndDmg);
						pc.addBowDmgup(i * influence_bowHitAndDmg);
					}

					break;

				// 強化飾品設置 -> DB化
				// case 23:// 戒指
				// case 24:// 項鏈
				// case 37:// 腰帶
				// case 40:// 耳環
				// if (item.getItem().get_greater() != 3) {
				// item.greater(pc, true);
				// }
				// break;

				default:
					break;
				}
				pc.sendPackets(new S_OwnCharStatus(pc));
			}
		}
	}
}
