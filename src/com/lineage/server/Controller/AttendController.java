package com.lineage.server.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.server.datatables.CharacterAttendTable.UseAttendTemp;
import com.lineage.server.datatables.CharacterAttendTable.idTemp;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AttenDance;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.CommonUtil;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;

/**
 * 官方簽到系統
 */
public class AttendController implements Runnable {
	private static Logger _log = Logger.getLogger(AttendController.class.getName());

	private static AttendController _instance;

	public static AttendController getInstance() {
		if (_instance == null)
			_instance = new AttendController();
		return _instance;
	}

	public AttendController() {
		GeneralThreadPool.get().execute(this);
	}

	private boolean isNow = false;
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				AttendNormal();
				AttendPcRoom();
				ClearTimeCheck();
				continue;
			} catch (Exception e) {
				e.printStackTrace();
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	private int _time = 0;

	private void ClearTimeCheck() {
		_time = Integer.valueOf(CommonUtil.dateFormat("HH"));
		if (!isNow) {
			if (_time == Config.ClearClock) {
				isNow = true;
				// ????? ??? ??? 初始化內存中的信息
				// ??????? ????? 初始化數據庫的時間
				updateAll(); // ?? ??? ??????. 危險的內存洩漏是危險的
				// ???? ???? 全球訪問用戶
				updateOnline();
			}
		} else {
			int cktime = Config.ClearClock - 1;
			if (cktime < 0) {
				cktime = 23;
			}

			if (_time == cktime) {
				isNow = false;
			}
		}
	}

	public void updateOnline() {
		UseAttendTemp temp = null;
		for (L1PcInstance pc : World.get().getAllPlayers()) {
			if (pc == null || /*pc.noPlayerCK || */pc.getNetConnection() == null) {
				continue;
			}
			temp = pc.attendTemp;
			if (temp == null || temp.id_Normal >= 43 || temp.id_PcRoom >= 43) {
				continue;
			}

			temp.isNormal = 0;
			temp.time_Normal = 0;
			temp.Count_Normal = 0;

			temp.isPcRoom = 0;
			temp.time_PcRoom = 0;
			temp.Count_PcRoom = 0;

			pc.sendPackets(new S_AttenDance(pc, S_AttenDance.WatchPcPorfile, 0));
		}
	}

	public void updateAll() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con.prepareStatement("UPDATE character_attend SET isSuccessNormal=?, SuccessNormalCount=?, isSuccessPcRoom=?, SuccessPcRoomCount=?, NormalTime=?, PcRoomTime=?");
			pstm.setInt(1, 0);
			pstm.setInt(2, 0);
			pstm.setInt(3, 0);
			pstm.setInt(4, 0);
			pstm.setInt(5, 0);
			pstm.setInt(6, 0);

			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "CharacterAttendTable[]Error4", e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void AttendNormal() {
		if (World.get().getAllPlayers().size() <= 0) {
			return;
		}

		UseAttendTemp temp = null;
		for (L1PcInstance pc : World.get().getAllPlayers()) {
			if (pc == null || /*pc.noPlayerCK || */pc.getNetConnection() == null || 
				pc.isPrivateShop() || pc.getLevel() < Config.AttendLevel) {
				// ???? ???? ?? ?? ??, ?? ???? ??? ?? ???
				// 如果您想查看出勤檢查，請與我們聯繫。
				continue;
			}

			temp = pc.attendTemp;
			if (temp == null || temp.isNormal >= 1 || temp.Count_Normal >= Config.NormalMaxCount || temp.id_Normal >= 43) {
				continue;
			}

			temp.time_Normal += 1;
			if (temp.time_Normal < Config.NormalMaxTime) {
				continue;
			}

			temp.time_Normal = 0; // ????? 時間重置
			temp.Count_Normal += 1; // ?? ????? ?? 添加到此頁面
			if (temp.Count_Normal >= Config.NormalMaxCount) {
				temp.isNormal = 1;
			}

			for (idTemp att : temp.Nomarlist) {
				if (att.id == temp.id_Normal) {
			        att.state += 1;
			        temp.id_Normal += 1;
					pc.sendPackets(new S_AttenDance(pc, S_AttenDance.WatchPcPorfile, 0));
					break;
				}
			}

		}
	}

	private void AttendPcRoom() {
		if (World.get().getAllPlayers().size() <= 0) {
			return;
		}

		UseAttendTemp temp = null;
		for (L1PcInstance pc : World.get().getAllPlayers()) {
			// ???? ???? 出勤檢查更正請求
			// pc? ?? ??, ?? ???? ??? ?? ??? 電腦室限制，如果您有任何限制，請在這裡
			// ?????? ????? ?????. ??? pc? ?? ??????? ??????.
			// 我想你可以刪除氏族成員資格部分。 並添加pc buff檢查。
			if (pc == null || /*pc.noPlayerCK || */pc.getNetConnection() == null || 
				pc.isPrivateShop() || pc.getLevel() < Config.AttendLevel
				|| !pc.PCRoom_Buff // ????
			) {
				continue;
			}

			temp = pc.attendTemp;
			if (temp == null || temp.isPcRoom >= 1 || temp.Count_PcRoom >= Config.PcRoomMaxCount || temp.id_PcRoom >= 43) {
				continue;
			}

			temp.time_PcRoom += 1;
			if (temp.time_PcRoom < Config.PcRoomMaxTime) {
				continue;
			}

			temp.time_PcRoom = 0; // ????? 時間重置
			temp.Count_PcRoom += 1; // ?? ????? ?? 添加到此頁面
			if (temp.Count_PcRoom >= Config.PcRoomMaxCount) {
				temp.isPcRoom = 1;
			}

			// PC? ??? ???? ???? ??
			/*for (idTemp att : temp.PcRoomlist) {
				if (att.id == temp.id_PcRoom) {
			        att.state += 1;
			        temp.id_PcRoom += 1;
					pc.sendPackets(new S_AttenDance(pc, S_AttenDance.WatchPcPorfile, 0));
					break;
				}
			}*/
		}
	}

}
