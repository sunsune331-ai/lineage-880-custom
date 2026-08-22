package com.lineage.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.SQLUtil;
/**
 * 依怪物名稱或者ID查詢爆率<br>
 * 創建人:四爺(siye)<br>
 * 修改時間：2019年1月13日 下午9:44:25<br>
 * 修改人:QQ:403471355<br>
 * 
 * @version<br>
 */
public class CMDMobDrops implements L1CommandExecutor {

	private CMDMobDrops() {
	}

	public static L1CommandExecutor getInstance() {
		return new CMDMobDrops();
	}

	private int parseNpcId(final String nameId) {
		int npcid = 0;
		try {
			// 依照ID取回
			npcid = Integer.parseInt(nameId);

		} catch (final NumberFormatException e) {
			// 依照名稱取回
			npcid = NpcTable.get().findNpcIdByNameWithoutSpace(nameId);
		}
		return npcid;
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {

		StringTokenizer stringtokenizer = new StringTokenizer(arg);
		String mob = stringtokenizer.nextToken();
		int mobID = this.parseNpcId(mob);

		L1Npc npc = NpcTable.get().getTemplate(mobID);
		if (npc == null) {
			pc.sendPackets(new S_SystemMessage("\\aG不存在該怪物。你可以在怪物附近輸入.4查看id"));
			return;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		// Connection con1 = null;
		// PreparedStatement pstm1 = null;
		// ResultSet rs1 = null;
		int[] itemID;
		int[] min;
		int[] max;
		double[] chance;
		String[] name;
		try {
			con = DatabaseFactory.get().getConnection();
			pstm = con
					.prepareStatement("SELECT itemId,min,max,chance FROM droplist WHERE mobId=?");
			pstm.setInt(1, mobID);
			rs = pstm.executeQuery();
			rs.last();
			int rows = rs.getRow();
			itemID = new int[rows];
			min = new int[rows];
			max = new int[rows];
			chance = new double[rows];
			name = new String[rows];
			rs.beforeFirst();

			int i = 0;
			while (rs.next()) {
				itemID[i] = rs.getInt("itemId");
				min[i] = rs.getInt("min");
				max[i] = rs.getInt("max");
				chance[i] = rs.getInt("chance") / (double) 10000;
				i++;
			}
			rs.close();
			pstm.close();
			pc.sendPackets(new S_SystemMessage(npc.get_name() + "(" + mobID
					+ ") 常規掉落查詢:"));

			for (int j = 0; j < itemID.length; j++) {
				pstm = con
						.prepareStatement("SELECT name FROM etcitem WHERE item_id=?");
				pstm.setInt(1, itemID[j]);
				rs = pstm.executeQuery();
				while (rs.next()) {
					name[j] = rs.getString("name");
				}
				rs.close();
				pstm.close();
				pstm = con
						.prepareStatement("SELECT name FROM weapon WHERE item_id=?");
				pstm.setInt(1, itemID[j]);
				rs = pstm.executeQuery();
				while (rs.next()) {
					name[j] = rs.getString("name");
				}
				rs.close();
				pstm.close();
				pstm = con
						.prepareStatement("SELECT name FROM armor WHERE item_id=?");
				pstm.setInt(1, itemID[j]);
				rs = pstm.executeQuery();
				while (rs.next()) {
					name[j] = rs.getString("name");
				}
				rs.close();
				pstm.close();
				pc.sendPackets(new S_SystemMessage("\\aD物品:" + itemID[j] + " "
						+ name[j] + " " + " 幾率:" + chance[j] + "%"));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("\\aD請輸入 ." + cmdName
					+ " [怪物編號]|[怪物名字]。"));
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

	}
}
