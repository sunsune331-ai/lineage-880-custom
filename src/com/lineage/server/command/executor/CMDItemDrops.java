package com.lineage.server.command.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.SQLUtil;
/**
 * 依物品名稱或者ID查詢爆率<br>
 * 創建人:四爺(siye)<br>
 * 修改時間：2019年1月13日 下午9:44:25<br>
 * 修改人:QQ:403471355<br>
 * 
 * @version<br>
 */
public class CMDItemDrops implements L1CommandExecutor {

	private CMDItemDrops() {
	}

	public static L1CommandExecutor getInstance() {
		return new CMDItemDrops();
	}

	private int parseItemId(String nameId) {
		int itemid = 0;
		try {
			itemid = Integer.parseInt(nameId);
		} catch (NumberFormatException e) {
			itemid = ItemTable.get().findItemIdByNameWithoutSpace1(nameId);
		}
		return itemid;
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {

		StringTokenizer stringtokenizer = new StringTokenizer(arg);
		String drop = stringtokenizer.nextToken();
		int dropID = parseItemId(drop);

		L1Item item = ItemTable.get().getTemplate(dropID);
		if (item == null) {
			pc.sendPackets(new S_SystemMessage("\\aG不存在該物品。"));
			return;
		}
		if (dropID == 40308) {
			pc.sendPackets(new S_SystemMessage(
					"悟空你看你腦袋又秀逗了。是個妖怪都掉金幣的嘛。和你說了多少次你總是記不住。"));
		} else {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;
			// Connection con1 = null;
			// PreparedStatement pstm1 = null;
			// ResultSet rs1 = null;
			int[] mobID;
			int[] min;
			int[] max;
			double[] chance;
			String[] name;
			try {
				String blessed;
				if (item.getBless() == 1) {
					blessed = "";
				} else if (item.getBless() == 0) {
					blessed = "\\fR";
				} else {
					blessed = "\\fY";
				}

				con = DatabaseFactory.get().getConnection();
				pstm = con
						.prepareStatement("SELECT mobId,min,max,chance FROM droplist WHERE itemId=?");
				pstm.setInt(1, dropID);
				rs = pstm.executeQuery();
				rs.last();
				int rows = rs.getRow();
				mobID = new int[rows];
				min = new int[rows];
				max = new int[rows];
				chance = new double[rows];
				name = new String[rows];
				rs.beforeFirst();

				int i = 0;
				while (rs.next()) {
					mobID[i] = rs.getInt("mobId");
					min[i] = rs.getInt("min");
					max[i] = rs.getInt("max");
					chance[i] = rs.getInt("chance") / (double) 10000;
					i++;
				}
				rs.close();
				pstm.close();
				pc.sendPackets(new S_SystemMessage(blessed + item.getName()
						+ "(" + dropID + ")常規掉落查詢:"));

				for (int j = 0; j < mobID.length; j++) {
					pstm = con
							.prepareStatement("SELECT name FROM npc WHERE npcid=?");
					pstm.setInt(1, mobID[j]);
					rs = pstm.executeQuery();
					while (rs.next()) {
						name[j] = rs.getString("name");
					}
					rs.close();
					pstm.close();
					pc.sendPackets(new S_SystemMessage("\\aD怪物:" + mobID[j]
							+ " " + name[j]));
				}
			} catch (Exception e) {
				pc.sendPackets(new S_SystemMessage("\\aD請輸入 ." + cmdName
						+ " [物品編號]|[物品名字]。"));
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}
	}
}
