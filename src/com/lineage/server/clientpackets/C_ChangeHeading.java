package com.lineage.server.clientpackets;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SkinInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;

/**
 * 要求改變角色面向
 * @author daien
 */
public class C_ChangeHeading extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_ChangeHeading.class);

	@Override
	public void start(byte[] decrypt, ClientExecutor client) {
		try {
			// 資料載入
			read(decrypt);

			L1PcInstance pc = client.getActiveChar();

			if (pc.isGhost()) { // 鬼魂模式
				return;
			}
			
			if (pc.isDead()) { // 死亡
				return;
			}
			
			if (pc.isTeleport()) { // 傳送中
				return;
			}

			int heading = readC();
			switch (heading) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				pc.setHeading(heading);
				// _log.finest("Change Heading : " + pc.getHeading());
				if (!pc.isGmInvis() && !pc.isGhost() && !pc.isInvisble()) {
					pc.broadcastPacketAll(new S_ChangeHeading(pc));
				}

				final Map<Integer, L1SkinInstance> skinList = pc.getSkins();
				if (skinList.size() > 0) {
					for (final Integer gfxid : skinList.keySet()) {
						final L1SkinInstance skin = skinList.get(gfxid);
						skin.setHeading(heading);
						skin.broadcastPacketAll(new S_ChangeHeading(skin));
					}
				}
				break;
			}

		} catch (final Exception e) {
			// _log.error(e.getLocalizedMessage(), e);

		} finally {
			this.over();
		}
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}
}
