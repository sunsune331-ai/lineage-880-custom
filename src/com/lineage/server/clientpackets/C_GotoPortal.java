package com.lineage.server.clientpackets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.Teleportation;

public class C_GotoPortal extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_GotoPortal.class);

	@Override
	public void start(final byte[] decrypt, final ClientExecutor client) {
		try {
			final L1PcInstance pc = client.getActiveChar();
			if (pc != null) {
				Teleportation.teleportation(pc);
			}
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
