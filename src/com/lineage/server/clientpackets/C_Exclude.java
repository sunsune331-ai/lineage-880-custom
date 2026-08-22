package com.lineage.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.DeNameTable;
import com.lineage.server.datatables.SpamTable;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1ExcludingList;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.templates.DeName;
import com.lineage.server.templates.L1CharName;
import com.lineage.server.utils.SQLUtil;

/** Handles the 1810102501 chat/mail exclusion packet. */
public class C_Exclude extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_Exclude.class);

	@Override
	public void start(final byte[] decrypt, final ClientExecutor client) {
		try {
			read(decrypt);
			final L1PcInstance pc = client.getActiveChar();
			if (pc == null) {
				return;
			}

			final String wireName = readS();
			if ((wireName == null) || wireName.isEmpty() || wireName.equalsIgnoreCase(pc.getName())) {
				return;
			}

			final int type = readC();
			if ((type != 0) && (type != 1)) {
				return;
			}

			final L1ExcludingList list = SpamTable.getInstance().getExcludeTable(pc.getId());
			if (list.contains(type, wireName)) {
				deleteExclude(pc.getId(), type, wireName);
				list.remove(type, wireName);
				pc.sendPackets(new S_PacketBox(S_PacketBox.REM_EXCLUDE, type, wireName));
				return;
			}

			final ExcludeTarget target = findTarget(wireName);
			if (target == null) {
				return;
			}

			insertExclude(pc.getId(), type, target.objectId, target.name);
			list.add(type, target.name);
			pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE, type, target.name));
		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			over();
		}
	}

	private static ExcludeTarget findTarget(final String name) {
		for (final L1CharName character : CharacterTable.get().getCharNameList()) {
			if (name.equalsIgnoreCase(character.getName())) {
				return new ExcludeTarget(character.getId(), character.getName());
			}
		}
		for (final DeName character : DeNameTable.get().getDeNameList()) {
			if (name.equalsIgnoreCase(character.get_name())) {
				return new ExcludeTarget(character.get_deobjid(), character.get_name());
			}
		}
		return null;
	}

	private static void insertExclude(final int charId, final int type, final int objectId, final String name)
			throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseFactory.get().getConnection();
			statement = connection.prepareStatement(
					"INSERT INTO character_exclude SET char_id=?, type=?, exclude_id=?, exclude_name=?");
			statement.setInt(1, charId);
			statement.setInt(2, type);
			statement.setInt(3, objectId);
			statement.setString(4, name);
			statement.execute();
		} finally {
			SQLUtil.close(statement);
			SQLUtil.close(connection);
		}
	}

	private static void deleteExclude(final int charId, final int type, final String name) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = DatabaseFactory.get().getConnection();
			statement = connection.prepareStatement(
					"DELETE FROM character_exclude WHERE char_id=? AND type=? AND exclude_name=?");
			statement.setInt(1, charId);
			statement.setInt(2, type);
			statement.setString(3, name);
			statement.execute();
		} finally {
			SQLUtil.close(statement);
			SQLUtil.close(connection);
		}
	}

	@Override
	public String getType() {
		return getClass().getSimpleName();
	}

	private static final class ExcludeTarget {
		private final int objectId;
		private final String name;

		private ExcludeTarget(final int objectId, final String name) {
			this.objectId = objectId;
			this.name = name;
		}
	}
}
