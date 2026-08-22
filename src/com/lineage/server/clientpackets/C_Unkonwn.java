package com.lineage.server.clientpackets;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.echo.encryptions.PacketPrint;

public class C_Unkonwn extends ClientBasePacket {
	private static final Log _log = LogFactory.getLog(C_Unkonwn.class);

	public void start(byte[] decrypt, ClientExecutor client) {
		try {
			//_log.info("未處理封包: " + (decrypt[0] & 0xFF) + " (" + getNow_YMDHMS() + " 核心管理者紀錄用!)");
			//_log.info(PacketPrint.get().printData(decrypt, decrypt.length));
		} catch (Exception localException) {
		} finally {
			over();
		}
	}

	public String getType() {
		return getClass().getSimpleName();
	}

	private final String getNow_YMDHMS() {
		String nowDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		return nowDate;
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.clientpackets.C_Unkonwn JD-Core Version: 0.6.2
 */