package com.lineage.server.command.executor;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.WorldMob;

/**
 * [殺死、查詢、回收]指定地圖怪物
 * @author
 */
public class L1KillMapNpc implements L1CommandExecutor {

	private static final Log _log = LogFactory.getLog(L1KillMapNpc.class);

	private L1KillMapNpc() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1KillMapNpc();
	}

	@Override
	public void execute(final L1PcInstance pc, final String cmdName, final String arg) {
		try {

			final StringTokenizer st = new StringTokenizer(arg);
			final int mapid = Integer.parseInt(st.nextToken());

			int type = 0;
			try {
				type = Integer.parseInt(st.nextToken());
			} catch (final Exception e) {
				type = 1;
			}

			if (type == 1) { // 殺死指定地圖全部怪物
				for (L1NpcInstance npc : WorldMob.get().all()) {
					if (npc.getMapId() == mapid) {
						int hp = npc.getMaxHp() + 1000;
						npc.receiveDamage(pc, hp);
					}
				}

			} else if (type == 2) { // 查詢指定地圖全部怪物數量
				/*pc.sendPackets(new S_ServerMessage("目前" + mapid + "地圖巨型哈維數量：" + WorldMob.get().getCount(mapid, 300030)));
				pc.sendPackets(new S_ServerMessage("目前" + mapid + "地圖巨型毆吉數量：" + WorldMob.get().getCount(mapid, 300031)));
				pc.sendPackets(new S_ServerMessage("目前" + mapid + "地圖巨型狼人數量：" + WorldMob.get().getCount(mapid, 300032)));
				pc.sendPackets(new S_ServerMessage("目前" + mapid + "地圖巨型萊肯數量：" + WorldMob.get().getCount(mapid, 300033)));
				pc.sendPackets(new S_ServerMessage("目前" + mapid + "地圖藍色蛇女數量：" + WorldMob.get().getCount(mapid, 300034)));
				pc.sendPackets(new S_ServerMessage("目前" + mapid + "地圖綠色蛇女數量：" + WorldMob.get().getCount(mapid, 300035)));*/
				
				pc.sendPackets(new S_ServerMessage("目前" + mapid + "地圖怪物數量：" + WorldMob.get().getCount(mapid)));

			} else if (type == 3) { // 回收指定地圖全部怪物
				for (L1NpcInstance npc : WorldMob.get().all()) {
					if (npc.getMapId() == mapid) {
						npc.deleteMe();
					}
				}
			}

		} catch (final Exception e) {
			_log.error("錯誤的GM指令格式: " + this.getClass().getSimpleName() + " 執行的GM:" + pc.getName());
			// 261 \f1指令錯誤。
			pc.sendPackets(new S_ServerMessage(261));
		}
	}
}
