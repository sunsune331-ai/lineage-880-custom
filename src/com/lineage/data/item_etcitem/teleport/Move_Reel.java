package com.lineage.data.item_etcitem.teleport;

import static com.lineage.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Trade;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1BookMark;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.utils.Teleportation;

/**
 * 魔法卷轴(指定传送)40863<br>
 * 瞬间移动卷轴 40100<br>
 * 瞬间移动卷轴（祝福）140100
 */
public class Move_Reel extends ItemExecutor {

	/**
	 *
	 */
	private Move_Reel() {
		// TODO Auto-generated constructor stub
	}

	public static ItemExecutor get() {
		return new Move_Reel();
	}

	/**
	 * 道具物件执行
	 * 
	 * @param data
	 *            参数
	 * @param pc
	 *            执行者
	 * @param item
	 *            物件
	 */
	@Override
	public void execute(final int[] data, final L1PcInstance pc,
			final L1ItemInstance item) {
		if (!CheckUtil.getUseItem(pc)) {
			return;
		}

		final int mapid = pc.getMapId();

		// final int map = data[0]; // 日版记忆座标
		final int x = data[1]; // 日版记忆座标
		final int y = data[2]; // 日版记忆座标

		int btele = 0;
		final L1BookMark book = pc.getBookMark(x, y);
		if (book != null) {
			btele = book.getId();
		}

		boolean isTeleport = pc.getMap().isTeleportable();// 地图设定是否可顺移

		if (pc.getInventory().checkItem(84041, 1) && mapid == 3301) {// 傲慢之塔支配传送符(1楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84042, 1) && mapid == 3302) {// 傲慢之塔支配传送符(2楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84043, 1) && mapid == 3303) {// 傲慢之塔支配传送符(3楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84044, 1) && mapid == 3304) {// 傲慢之塔支配传送符(4楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84045, 1) && mapid == 3305) {// 傲慢之塔支配传送符(5楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84046, 1) && mapid == 3306) {// 傲慢之塔支配传送符(6楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84047, 1) && mapid == 3307) {// 傲慢之塔支配传送符(7楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84048, 1) && mapid == 3308) {// 傲慢之塔支配传送符(8楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84049, 1) && mapid == 3309) {// 傲慢之塔支配传送符(9楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84050, 1) && mapid == 3310) {// 傲慢之塔支配传送符(10楼)
			isTeleport = true;
		} else if (pc.getInventory().checkItem(84071, 1) && mapid >= 3301
				&& mapid <= 3310) {// 幻象的傲慢之塔移动传送符
			isTeleport = true;
		} else if (pc.getInventory().checkItem(602030, 1) &&( mapid == 15410||mapid == 15420||mapid == 15430
				|| mapid == 15440)) {// 幻象的傲慢之塔移动传送符
			isTeleport = true;
		} else if (pc.getInventory().checkItem(700252, 1) && mapid >= 12852&& mapid <= 12862) {// 幻象的傲慢之塔移动传送符
			isTeleport = true;
		}			

		if (!isTeleport) {
			// 647 这附近的能量影响到瞬间移动。在此地无法使用瞬间移动。
			pc.sendPackets(new S_ServerMessage(647));
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
					false));
		} else {
			// 取出记忆座标
			final L1BookMark bookm = pc.getBookMark(btele);
			if (bookm != null) { // 日版记忆座标
				pc.getInventory().removeItem(item, 1L);

				if (pc.getTradeID() != 0) {
					final L1Trade trade = new L1Trade();
					trade.tradeCancel(pc);
				}

				pc.setTeleportX(bookm.getLocX()); // x
				pc.setTeleportY(bookm.getLocY()); // y
				pc.setTeleportMapId(bookm.getMapId()); // map
				pc.setTeleportHeading(5);
				pc.sendPacketsAll(new S_SkillSound(pc.getId(), 169));
				Teleportation.teleportation(pc);

			} else {
				pc.getInventory().removeItem(item, 1L);

				L1Location newLocation;
				int newX = pc.getX();
				int newY = pc.getY();
				short mapId = pc.getMapId();
				boolean right = false;
				while (!right) {
					newLocation = pc.getLocation().randomLocation(200, true);
					newX = newLocation.getX();
					newY = newLocation.getY();
					mapId = (short) newLocation.getMapId();
					if (newX == pc.getX() && newY == pc.getY()) {
						right = false;
					} else {
						right = true;
					}
				}

				if (pc.getTradeID() != 0) {
					final L1Trade trade = new L1Trade();
					trade.tradeCancel(pc);
				}

				pc.setTeleportX(newX);
				pc.setTeleportY(newY);
				pc.setTeleportMapId(mapId);
				pc.setTeleportHeading(5);
				pc.sendPacketsAll(new S_SkillSound(pc.getId(), 169));
				Teleportation.teleportation(pc);
			}
			// 绝对屏障解除
			if (pc.hasSkillEffect(ABSOLUTE_BARRIER)) { // 绝对屏障
				pc.killSkillEffectTimer(ABSOLUTE_BARRIER);
				pc.startHpRegeneration();
				pc.startMpRegeneration();
			}
		}
	}

}
