package com.lineage.server.command.executor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1CharName;
import com.lineage.server.world.World;

/**
 * 重置限時地監時間
 *
 * @author dexc
 *
 */
public class L1ResetTime implements L1CommandExecutor {

	private static final Log _log = LogFactory.getLog(L1ResetTime.class);

	private L1ResetTime() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1ResetTime();
	}

	@Override
	public void execute(final L1PcInstance pc, final String cmdName, final String arg) {
		try {

			for (L1CharName charName : CharacterTable.get().getCharNameList()) {
				L1PcInstance tgpc = World.get().getPlayer(charName.getName());
				if (tgpc != null) {// 人物在線上
					tgpc.resetAllMapTime();
					tgpc.save();// 人物資料存檔
					tgpc.sendPackets(new S_ServerMessage("\\fT限時地監的使用時間已重置。"));
					tgpc.removeSkillEffect(40000);
				} else {// 人物不在線上
					L1PcInstance offlinepc = CharacterTable.get().restoreCharacter(charName.getName());
					offlinepc.removeSkillEffect(40000);
					offlinepc.resetAllMapTime();
					offlinepc.save();// 人物資料存檔
				}
			}
			CharBuffReading.get().deleteBuff_skill(40000);

			if (pc == null) {
				_log.warn("系統命令執行: " + cmdName + "重置所有玩家限時地監時間。");
			} else {
				pc.sendPackets(new S_SystemMessage("重置所有玩家限時地監時間。"));
			}

		} catch (Exception e) {
			_log.info(e.getMessage());
			// 261 \f1指令錯誤。
			pc.sendPackets(new S_ServerMessage(261));
		}
	}
}
