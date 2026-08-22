package com.lineage.data.item_etcitem.reel;

import java.sql.Timestamp;
import java.util.Random;

import com.lineage.config.ConfigRecord;
import com.lineage.data.cmd.EnchantExecutor;
import com.lineage.data.cmd.EnchantWeapon;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1ItemUpdata;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_ItemStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

/**
 * <font color=#00800>工匠的對武器施法的卷軸(+9適用)</font><BR>
 *
 */
public class FantasyEnchantWeapon1 extends ItemExecutor {

	/**
	 *
	 */
	private FantasyEnchantWeapon1() {
		// TODO Auto-generated constructor stub
	}

	public static ItemExecutor get() {
		return new FantasyEnchantWeapon1();
	}

	/**
	 * 道具物件執行
	 * 
	 * @param data
	 *            參數
	 * @param pc
	 *            執行者
	 * @param item
	 *            物件
	 */
	@Override
	public void execute(final int[] data, final L1PcInstance pc, final L1ItemInstance item) {
		// 對像OBJID
		final int targObjId = data[0];

		final L1ItemInstance tgItem = pc.getInventory().getItem(targObjId);

		if (tgItem == null) {
			return;
		}
		if (tgItem.isEquipped()) {
			pc.sendPackets(new S_ServerMessage("\\fU你必須先解除物品裝備。"));// 沒有任何事發生//20180720文字修改
			return;
		}
		final int safe_enchant = tgItem.getItem().get_safeenchant();
		boolean isErr = false;

		// 取得物件觸發事件
		final int use_type = tgItem.getItem().getUseType();
		switch (use_type) {
		case 1:// 武器
			if (safe_enchant < 0) { // 物品不可強化
				isErr = true;
			}
			break;

		default:
			isErr = true;
			break;
		}

		final int weaponId = tgItem.getItem().getItemId();
		if ((weaponId >= 246) && (weaponId <= 255)) { // 物品不可強化
			isErr = true;
		}
		if ((weaponId >= 301) && (weaponId <= 305)) { // 耀武20161118
			isErr = true;
		}
		if ((weaponId >= 852) && (weaponId <= 856)) { // 屍魂 //20180720修改編號
			isErr = true;
		}
		if ((weaponId >= 857) && (weaponId <= 860)) { // 遺忘武器//20180720修改編號
			isErr = true;
		}
		if (tgItem.getBless() >= 128) {// 封印的裝備
			isErr = true;
		}
		if (isErr) {
			pc.sendPackets(new S_ServerMessage(79));// 沒有任何事發生
			return;
		}
		// 只能對+9的武器使用。使用後強化失敗時，武器不會消失。
		if (tgItem.getEnchantLevel() != 9) {// 20161118
			pc.sendPackets(new S_ServerMessage("只能對+9的武器使用。"));
			return;
		}
		// 物品已追加值
		final int enchant_level = tgItem.getEnchantLevel();
		final EnchantExecutor enchantExecutor = new EnchantWeapon();
		int randomELevel = enchantExecutor.randomELevel(tgItem, item.getBless());
		pc.getInventory().removeItem(item, 1);

		boolean isEnchant = true;
		if (enchant_level < -6) {// 武器將會消失,最大可追加到-
			isEnchant = false;

		} else if (enchant_level < safe_enchant) {// 安定值內
			isEnchant = true;

		} else {

			final Random random = new Random();
			final int rnd2 = random.nextInt(100) + 1;
			int enchant_chance_wepon;
			int enchant_level_tmp;

			if (safe_enchant == 0) { // 對武器安定直為0初始計算+6
				enchant_level_tmp = enchant_level + 6;

			} else {
				enchant_level_tmp = enchant_level;
			}

			if (enchant_level >= 9) {
				enchant_chance_wepon = (int) L1ItemUpdata.enchant_wepon_up9(enchant_level_tmp);

			} else {
				enchant_chance_wepon = (int) L1ItemUpdata.enchant_wepon_dn9(enchant_level_tmp);
			}

			if (item.getItemId() == 44066) {// 潘朵拉黃金武器魔法卷軸
				enchant_chance_wepon *= 2;// 機率加倍
			}

			if (rnd2 < enchant_chance_wepon) {
				isEnchant = true;

			} else {
				if ((enchant_level >= 9) && (rnd2 < (enchant_chance_wepon * 2))) {
					randomELevel = 0;

				} else {
					isEnchant = false;
				}
			}
		}
		if ((randomELevel <= 0) && (enchant_level > -6)) {
			isEnchant = true;
		}

		if (isEnchant) {// 成功
//			ConfigRecord.recordToFiles("工匠武卷強化記錄",
//					"IP" + "(" + pc.getNetConnection().getIp() + ")" + "玩家" + ":【 " + pc.getName() + " 】 " + "的"
//							+ "【 + " + tgItem.getEnchantLevel() + " " + tgItem.getName() + "】- 強化成功" + "時間:"
//							+ "(" + new Timestamp(System.currentTimeMillis()) + ")。");
			enchantExecutor.successEnchant(pc, tgItem, randomELevel); // 20161114
			tgItem.setproctect(false);/** [原碼] 關閉裝備保護 */
			//tgItem.setproctect1(false);/** [原碼] 關閉裝備保護 */
			//tgItem.setproctect2(false);/** [原碼] 關閉裝備保護 */
			pc.sendPackets(new S_ItemStatus(tgItem));
			pc.getInventory().saveItem(tgItem, L1PcInventory.COL_ENCHANTLVL);
			if (pc.getAccessLevel() == 0 && pc.getcheck_lv() == false) { // 20161123
				if (tgItem.getEnchantLevel() >= tgItem.getItem().get_safeenchant() + 3) {
					World.get().broadcastPacketToAll(new S_BlueMessage(166,
							"\\f=【" + pc.getName() + "】的+" + enchant_level + " " + tgItem.getName() + "強化成功"));
				}
			}
			pc.setcheck_lv(false);// 關閉20161123
		} else {// 失敗
			if (tgItem.getproctect() == true) { // A級-如果裝備受到保護中往下執行
				tgItem.setproctect(false);
				pc.sendPackets(new S_ItemStatus(tgItem));
				pc.getInventory().updateItem(tgItem, L1PcInventory.COL_ENCHANTLVL);
				pc.getInventory().saveItem(tgItem, L1PcInventory.COL_ENCHANTLVL);
				//pc.sendPackets(new S_ServerMessage("受到高級裝備保護卷軸的影響,失敗後物品無變化。"));

			} else {
				//enchantExecutor.failureEnchant(pc, tgItem);
				// \f1%0%s %2 產生激烈的 %1 光芒，但是沒有任何事情發生。
				pc.sendPackets(new S_ServerMessage(160, tgItem.getName(), "$252", "$248"));
				if (tgItem.getproctect() == false) {
					ConfigRecord.recordToFiles("工匠武卷強化失敗記錄",
							"IP" + "(" + pc.getNetConnection().getIp() + ")" + "玩家" + ":【" + pc.getName() + "】" + "的"
									+ "【+" + tgItem.getEnchantLevel() + " " + tgItem.getName() + "】- 強化失敗," + "時間:"
									+ "(" + new Timestamp(System.currentTimeMillis()) + ")。");
				}
			}
		}
	}

}
