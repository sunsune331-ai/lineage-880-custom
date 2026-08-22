package com.add;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1CharName;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

public class L1ResetMapTime {

	private static final Log _log = LogFactory.getLog(L1ResetMapTime.class);

	private static L1ResetMapTime _instance;

	public static L1ResetMapTime get() {
		if (_instance == null) {
			_instance = new L1ResetMapTime();
		}
		return _instance;
	}

	/**
	 * 重置限時地監的處理
	 */
	public void ResetTimingMap() {
		try {
			Calendar reset_cal = L1Config._4002;// 取回地監重置時間日曆
			long nowtime = System.currentTimeMillis();// 現在時間(毫秒)
			long resettime = reset_cal.getTimeInMillis();// 地圖重置時間(毫秒)
			long oneday = 60 * 60 * 1000 * 24;// 一天(毫秒)

			if (nowtime - resettime >= oneday) {// 超過一天
				for (L1CharName charName : CharacterTable.get().getCharNameList()) {
					L1PcInstance tgpc = World.get().getPlayer(charName.getName());
					if (tgpc != null) {// 人物在線上

						tgpc.resetAllMapTime();
						tgpc.save();// 人物資料存檔
						tgpc.removeSkillEffect(40000);
						CharBuffReading.get().deleteBuff_skill_buff(tgpc.getId());
						tgpc.sendPackets(new S_ServerMessage("\\fT限時地監的使用時間已重置。"));

					} else {// 人物不在線上
						L1PcInstance offlinepc = CharacterTable.get().restoreCharacter(charName.getName());
						if (offlinepc != null) {
							offlinepc.resetAllMapTime();
							offlinepc.save();// 人物資料存檔
							offlinepc.removeSkillEffect(40000);
							CharBuffReading.get().deleteBuff_skill_buff(offlinepc.getId());
						}
					}
				}
				CharBuffReading.get().deleteBuff_skill(40000);
				System.out.println("重置所有角色的限時地監時間");
				reset_cal.add(Calendar.DAY_OF_MONTH, 1);// 延後一天
				updateResetMapTime(4002, reset_cal);// 更新重置時間
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 更新限時地監重置時間
	 * 
	 * @param id
	 * @param reset_cal
	 */
	private void updateResetMapTime(int id, Calendar reset_cal) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("UPDATE `system_message` SET `resetMaptime`=? WHERE `id`=?");

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String fm = sdf.format(reset_cal.getTime());

			int i = 0;
			pstm.setString(++i, fm);
			pstm.setInt(++i, id);
			pstm.execute();
			// System.out.println("更新限時地監重置時間");
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
