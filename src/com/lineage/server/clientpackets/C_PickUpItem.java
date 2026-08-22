package com.lineage.server.clientpackets;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigRecord;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

public class C_PickUpItem extends ClientBasePacket {
	private static final Log _log = LogFactory.getLog(C_PickUpItem.class);

	public void start(byte[] decrypt, ClientExecutor client) {
		try {
			read(decrypt);

			L1PcInstance pc = client.getActiveChar();

			if (pc.isGhost())
				;
			while ((pc.isDead()) || (pc.isTeleport()) || (pc.isPrivateShop()) || (pc.isInvisble())
					|| (pc.isInvisDelay())) {
				return;
			}

			int x = readH();
			int y = readH();
			int objectId = readD();
			long pickupCount = readD();
			if (pickupCount > 2147483647L) {
				pickupCount = 2147483647L;
			}
			pickupCount = Math.max(0L, pickupCount);
			L1Inventory groundInventory = World.get().getInventory(x, y, pc.getMapId());

			L1Object object = groundInventory.getItem(objectId);
			if ((object != null) && (!pc.isDead())) {
				L1ItemInstance item = (L1ItemInstance) object;
				if (item.getCount() <= 0L) {
					return;
				}
				if ((item.getItemOwnerId() != 0) && (pc.getId() != item.getItemOwnerId())) {
					pc.sendPackets(new S_ServerMessage(623));
					return;
				}
				if (pc.getLocation().getTileLineDistance(item.getLocation()) > 3) {
					return;
				}
				item.set_showId(-1);

				if ((pc.getInventory().checkAddItem(item, pickupCount) == 0) && (item.getX() != 0)
						&& (item.getY() != 0)) {
					groundInventory.tradeItem(item, pickupCount, pc.getInventory());

					pc.turnOnOffLight();

					pc.setHeading(pc.targetDirection(item.getX(), item.getY()));

					if (!pc.isGmInvis()) {
						pc.broadcastPacketAll(new S_ChangeHeading(pc));

						pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 15));
					}
					//拾取物品記錄
					final Timestamp timestamp = new Timestamp(
							System.currentTimeMillis());
					ConfigRecord.recordToFiles("拾取物品記錄", "IP("
							+ pc.getNetConnection().getIp()
							+ ")玩家:【" + pc.getName()
							+ "】帳號:【" + pc.getAccountName()
							+ "】拾取 【" +"+"+item.getEnchantLevel() + " "
							+ item.getItem().getName()+"】 物品代號【"+item.getItemId()+"】【"+ item.getCount()+"】個  OBJID:" + item.getId() + "時間:(" + timestamp + ")",
							timestamp);
					//WriteLogTxt.Recording(
							//"拾取物品記錄",
							//"人物:" + pc.getName() + "拾取 +"
									//+ item.getEnchantLevel() + " "
									//+ item.getName() + "("
									//+ item.getCount() + ")" + " ItmeID:"
									//+ item.getItemId() + " 物品OBJID:"
									//+ item.getId());
				}
			}

		} catch (Exception localException) {
		} finally {
			over();
		}
	}

	public String getType() {
		return getClass().getSimpleName();
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.clientpackets.C_PickUpItem JD-Core Version: 0.6.2
 */