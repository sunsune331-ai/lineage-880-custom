package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharPack;
import com.lineage.server.utils.Teleportation;

public class C_Ship extends ClientBasePacket {
	private static final Log _log = LogFactory.getLog(C_Ship.class);

	public void start(byte[] decrypt, ClientExecutor client) {
		try {
			read(decrypt);

			L1PcInstance pc = client.getActiveChar();
			if (pc == null) {
				return;
			}
			int shipMapId = readH();
			if (shipMapId == 0xffff) {
				Teleportation.teleportation(pc);
				return;
			}
			if (decrypt.length < 7) {
				return;
			}
			int mapId = pc.getMapId();

			int item_id = 0;
			switch (mapId) {
			case 5:
				item_id = 40299;
				break;
			case 6:
				item_id = 40298;
				break;
			case 83:
				item_id = 40300;
				break;
			case 84:
				item_id = 40301;
				break;
			case 446:
				item_id = 40303;
				break;
			case 447:
				item_id = 40302;
			}

			if (item_id != 0) {
				pc.getInventory().consumeItem(item_id, 1L);

				int locX = readH();
				int locY = readH();

				pc.sendPackets(new S_OwnCharPack(pc));
				L1Teleport.teleport(pc, locX, locY, (short) shipMapId, 0, false);
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
 * com.lineage.server.clientpackets.C_Ship JD-Core Version: 0.6.2
 */
