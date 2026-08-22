package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SkillSound;

public class C_GMTeleport extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_GMTeleport.class);

	@Override
	public void start(final byte[] decrypt, final ClientExecutor client) {
		final L1PcInstance pc = client.getActiveChar();
		if (pc == null || !pc.isGm()) {
			return;
		}
		try {
			read(decrypt);
			final int mapId = readH();
			final int x = readH();
			final int y = readH();
			L1Teleport.teleport(pc, x, y, (short) mapId, pc.getHeading(), true);
			pc.sendPacketsAll(new S_SkillSound(pc.getId(), 12446));
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			over();
		}
	}

	@Override
	public String getType() {
		return getClass().getSimpleName();
	}
}
