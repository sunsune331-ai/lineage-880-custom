package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.list.OnlineUser;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Account;

public class C_ReturnToLogin extends ClientBasePacket {
	private static final Log _log = LogFactory.getLog(C_ReturnToLogin.class);

	public void start(byte[] decrypt, ClientExecutor client) {
		try {
			L1PcInstance pc = client.getActiveChar();
			if (pc != null) {
				client.quitGame();
			}

			L1Account account = client.getAccount();
			if (account != null) {
				OnlineUser.get().remove(account.get_login());
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
 * com.lineage.server.clientpackets.C_ReturnToLogin JD-Core Version: 0.6.2
 */