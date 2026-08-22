package william;


import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.lineage.DatabaseFactory;
import com.lineage.Server;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;



public class login_Artiface1 {
	private static ArrayList<ArrayList<Object>> aData = new ArrayList<ArrayList<Object>>();
	private static boolean BUILD_DATA = false;
	private static login_Artiface1 _instance;
	public static final String TOKEN = ",";
	
	public static login_Artiface1 getInstance() {
		if (_instance == null) {
			_instance = new login_Artiface1();
		}
		return _instance;
	}
	
	
	public static void main(String a[]) {
		while(true) {
			try {
			Server.main(null);
			} catch(Exception ex) {
			}
		}
	}

	public static void forIntensifyArmor(L1PcInstance pc,int type) {
		ArrayList<Object> aTempData = null;
		//L1ItemInstance tgItem = pc.getInventory().getItem(l);
		

		if (!BUILD_DATA) {
			BUILD_DATA = true;
			getData();
		}
		
		
		
		for (int i = 0; i < aData.size(); i++) {
			aTempData = (ArrayList<Object>) aData.get(i);
			
		
			switch (type) {
case 1:
				
				if(((Integer)aTempData.get(12)).intValue() == 1 && pc.get_other().getLv_Artifact() == ((Integer)aTempData.get(0)).intValue() && ((Integer)aTempData.get(10)).intValue() > 0){
					pc.sendPackets(new S_SystemMessage((String)aTempData.get(11)));
					return;
				}
				if (pc.get_other().getLv_Artifact() == ((Integer)aTempData.get(0)).intValue() && ((Integer)aTempData.get(12)).intValue() == 1) {
					pc.sendPackets(new S_ServerMessage((String)aTempData.get(6)));
					}
				if (pc.get_other().getLv_Artifact() == ((Integer)aTempData.get(0)).intValue() && pc.get_other().getArtifact() >= ((Integer)aTempData.get(1)).intValue() && ((Integer)aTempData.get(12)).intValue() == 1) {
					pc.get_other().setLv_Artifact(pc.get_other().getLv_Artifact() + 1);
					pc.get_other().setArtifact(pc.get_other().getArtifact() - ((Integer)aTempData.get(1)).intValue());
					//if (((Integer)aTempData.get(8)).intValue() == 1) {
					World.get().broadcastPacketToAll(new S_BlueMessage(166, pc.getName() + (String) aTempData.get(9)));
					//}
					pc.sendPackets(new S_ServerMessage((String)aTempData.get(6)));
					pc.sendPackets(new S_ServerMessage((String)aTempData.get(7)));
				
				}
				break;
			case 2:
				if(((Integer)aTempData.get(12)).intValue() == 0 && pc.get_other().getLv_Redmg_Artifact() == ((Integer)aTempData.get(2)).intValue() && ((Integer)aTempData.get(10)).intValue() > 0){
					pc.sendPackets(new S_SystemMessage((String)aTempData.get(11)));
					return;
				}
			
				if (pc.get_other().getLv_Redmg_Artifact() == ((Integer)aTempData.get(2)).intValue() && ((Integer)aTempData.get(12)).intValue() == 0) {
				pc.sendPackets(new S_ServerMessage((String)aTempData.get(6)));
				}
				if (pc.get_other().getLv_Redmg_Artifact() == ((Integer)aTempData.get(2)).intValue()  && pc.get_other().getArtifact1() >=((Integer)aTempData.get(3)).intValue() && ((Integer)aTempData.get(12)).intValue() == 0) {
					
					pc.get_other().setLv_Redmg_Artifact(pc.get_other().getLv_Redmg_Artifact() + 1);
					pc.get_other().setArtifact1(pc.get_other().getArtifact1() - ((Integer)aTempData.get(3)).intValue());
					//if (((Integer)aTempData.get(8)).intValue() == 1) {
						World.get().broadcastPacketToAll(new S_BlueMessage(166, pc.getName() + (String) aTempData.get(9)));
						//}
						
						pc.sendPackets(new S_ServerMessage((String)aTempData.get(7)));
				
				}
				break;
			
			}
		}
	}
				

	private static void getData() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseFactory.get().getConnection();
			pstmt = conn.prepareStatement("SELECT * FROM z_artifactlv");
			rs = pstmt.executeQuery();
			ArrayList<Object> aReturn = null;
			if (rs != null) {
				while (rs.next()) {
					aReturn = new ArrayList<Object>();
					aReturn.add(0, new Integer(rs.getInt("Lv_Artifact_weapon")));
					aReturn.add(1, new Integer(rs.getInt("Artifact_weapon")));
					aReturn.add(2, new Integer(rs.getInt("Lv_Artifact_armor")));
					aReturn.add(3, new Integer(rs.getInt("Artifact_armor")));
					aReturn.add(4, new Integer(rs.getInt("Lv_Artifact_other")));
					aReturn.add(5, new Integer(rs.getInt("Artifact_other")));
					aReturn.add(6, rs.getString("message"));
					aReturn.add(7, rs.getString("message1"));
					aReturn.add(8, new Integer(rs.getInt("check_world_mesage")));
					aReturn.add(9, rs.getString("world_message"));
					aReturn.add(10, new Integer(rs.getInt("lv_stop")));
					aReturn.add(11, rs.getString("stop_message"));
					aReturn.add(12, new Integer(rs.getInt("type")));
					aData.add(aReturn);
				}
			}
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstmt);
			SQLUtil.close(conn);
		}
	}
}
