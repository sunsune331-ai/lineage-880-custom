package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxIcon1;
import com.lineage.server.serverpackets.S_PacketBoxSelect;

public class C_NewCharSelect extends ClientBasePacket {
	private static final Log _log = LogFactory.getLog(C_NewCharSelect.class);

	public void start(byte[] decrypt, ClientExecutor client) {
		try {
			L1PcInstance pc = client.getActiveChar();

			if (pc == null)
				;
			while ((pc.getMapId() == 9000) || (pc.getMapId() == 9101)) {
				return;
			}

			pc.sendPackets(new S_ChangeName(pc, false));

			pc.sendPackets(new S_PacketBox(S_PacketBox.UPDATE_ER, pc.getEr()));// 迴避率更新

			pc.sendPackets(new S_PacketBoxIcon1(true, pc.get_dodge()));// 閃避率更新

			Thread.sleep(250L);
			client.quitGame();

			client.out().encrypt(new S_PacketBoxSelect());

			_log.info("角色切換: " + pc.getName());
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
 * com.lineage.server.clientpackets.C_NewCharSelect JD-Core Version: 0.6.2
 */