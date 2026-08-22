package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PrivateShop;

public class C_ShopList extends ClientBasePacket {
	private static final Log _log = LogFactory.getLog(C_ShopList.class);

	public void start(byte[] decrypt, ClientExecutor client) {
		try {
			read(decrypt);

			L1PcInstance pc = client.getActiveChar();

			if (pc.isGhost())
				;
			while ((pc.isDead()) || (pc.isTeleport()) || (pc.isPrivateShop())) {
				return;
			}

			int mapId = pc.getMapId();

			boolean isShopMap = false;
			
			  if (mapId == 340) { isShopMap = true; }
			  
			  if (mapId == 350) { isShopMap = true; }
			  
			  if (mapId == 360) { isShopMap = true; }
			  
			  if (mapId == 370) { isShopMap = true; }
			 

			if (mapId == 800) {
				isShopMap = true;
			}

			if (isShopMap) {
				int type = readC();
				int objectId = readD();

				pc.sendPackets(new S_PrivateShop(pc, objectId, type));
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
 * com.lineage.server.clientpackets.C_ShopList JD-Core Version: 0.6.2
 */