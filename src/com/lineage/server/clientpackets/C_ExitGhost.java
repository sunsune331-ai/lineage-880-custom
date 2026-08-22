package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;

public class C_ExitGhost extends ClientBasePacket {
	private static final Log _log = LogFactory.getLog(C_ExitGhost.class);

	public void start(byte[] decrypt, ClientExecutor client) {
		try {
			L1PcInstance pc = client.getActiveChar();

			if (!pc.isGhost()) {
				return;
			}

			pc.makeReadyEndGhost();
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
 * com.lineage.server.clientpackets.C_ExitGhost JD-Core Version: 0.6.2
 */