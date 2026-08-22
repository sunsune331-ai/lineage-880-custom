package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;

public class C_Disconnect extends ClientBasePacket {
	private static final Log _log = LogFactory.getLog(C_Disconnect.class);

	public void start(byte[] decrypt, ClientExecutor client) {
		
		over();
	}

	public String getType() {
		return getClass().getSimpleName();
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.clientpackets.C_Disconnect JD-Core Version: 0.6.2
 */