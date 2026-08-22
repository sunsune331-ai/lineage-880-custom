/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * http://www.gnu.org/copyleft/gpl.html
 */

package com.eric.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.lineage.config.Config;
import com.lineage.config.ConfigIpCheck;
import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.EnchantExecutor;
import com.lineage.data.cmd.EnchantWeapon;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.EchoServerTimer;
import com.lineage.server.Shutdown;
import com.lineage.server.clientpackets.AcceleratorChecker;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.datatables.lock.DwarfForElfReading;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_InvList;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ItemStatus;
import com.lineage.server.serverpackets.S_KillMessage;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.world.World;

import java.util.Date;

/**
 * @author eric1
 */
public class J_Main extends javax.swing.JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private static J_Main instance;

	private int select = 0;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	ImageIcon img = new ImageIcon("img/icon.png");
	
	/** Creates new form J_Main */
	public J_Main() {
		iniPlayerTable();
		initComponents();
		TA_Consol.setForeground(Color.WHITE);
		TA_AllChat.setForeground(Color.WHITE);
		TA_Clan.setForeground(Color.BLACK);
		TA_Normal.setForeground(Color.BLACK);
		TA_Private.setForeground(Color.BLACK);
		TA_Team.setForeground(Color.BLACK);
		TA_World.setForeground(Color.BLACK);
		
		
		TA_Consol.setBackground(Color.BLACK);
		TA_AllChat.setBackground(Color.BLACK);
		TA_Clan.setBackground(Color.BLACK);
		TA_Normal.setBackground(Color.BLACK);
		TA_Private.setBackground(Color.BLACK);
		TA_Team.setBackground(Color.BLACK);
		TA_World.setBackground(Color.BLACK);
		this.setIconImage(img.getImage());
		iniAction();
		T_Item.setSize(300, 400);
		D_Item.pack();
		String s[] = { "物品名稱", "物品數量", "物品ID" , "裝備等級" };
		DTM_Item.setColumnIdentifiers(s);
		/*
		 * if(SystemTray.isSupported()){ iniTray(); }
		 */
	}

	private void iniAction() {
		MI_Kill.addActionListener(this);
		MI_BanIP.addActionListener(this);
		MI_Item.addActionListener(this);
		MI_ItemX.addActionListener(this);
		MI_PC.addActionListener(this);		
		MI_Item2.addActionListener(this);
		MI_ShowPlayer.addActionListener(this);
		MI_Whisper.addActionListener(this);
		MI_Save.addActionListener(this);
		TPCHECKPACK.addActionListener(this);
		MI_Close.addActionListener(this);
		MI_Angel.addActionListener(this);
		MI_SetClose.addActionListener(this);
		MI_AllBuff.addActionListener(this);
		
		MI_Allbux.addActionListener(this);
		
		MI_AllBIG.addActionListener(this);
		MI_AllRess.addActionListener(this);	
		MI_AllRessstop.addActionListener(this);	
		//增加管理器倍數 by24918380 
        MI_XP_PET.addActionListener(this);
        MI_CHANCE_WEAPON.addActionListener(this);
        MI_CHANCE_ARMOR.addActionListener(this);
        MI_ATTR_ENCHANT_CHANCE.addActionListener(this);
        MI_LA.addActionListener(this);
        //增加管理器倍數 增加管理器倍數 by24918380 
		MI_Adena.addActionListener(this);
		MI_Exp.addActionListener(this);
		MI_Drop.addActionListener(this);
	}

	private DefaultTableModel DTM = new DefaultTableModel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	};

	private DefaultTableModel DTM_Item = new DefaultTableModel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}
	};

	public static J_Main getInstance() {
		if (instance == null) {
			instance = new J_Main();
		}
		return instance;
	}

	public void addWorldChat(String from, String text) {
		Calendar cal = Calendar.getInstance();
		AllChat(sdf.format(cal.getTime()) + "【" + from + "】:" + text + "\r\n");
		TA_World.append(from + " : " + text + "\r\n");
		TA_World.setCaretPosition(TA_World.getDocument().getLength());
	}

	public void addClanChat(String from, String text) {
		Calendar cal = Calendar.getInstance();
		AllChat(sdf.format(cal.getTime()) + "「" + from + "」:" + text + "\r\n");
		TA_Clan.append(from + " : " + text + "\r\n");
		TA_Clan.setCaretPosition(TA_Clan.getDocument().getLength());
	}

	public void addNormalChat(String from, String text) {
		Calendar cal = Calendar.getInstance();
		AllChat(sdf.format(cal.getTime()) + "{" + from + "}:" + text + "\r\n");
		TA_Normal.append(from + " : " + text + "\r\n");
		TA_Normal.setCaretPosition(TA_Normal.getDocument().getLength());
	}

	public void addTeamChat(String from, String text) {
		Calendar cal = Calendar.getInstance();
		AllChat(sdf.format(cal.getTime()) + "[" + from + "]:" + text + "\r\n");
		TA_Team.append(from + " : " + text + "\r\n");
		TA_Team.setCaretPosition(TA_Team.getDocument().getLength());
	}

	public static void addConsol(String text) {
		TA_Consol.append(text + "\r\n");
		TA_Consol.setCaretPosition(TA_Consol.getDocument().getLength());
	}

	public void addConsolPost(String text) {
		TA_Consol.append(text + "\r\n");
		TA_Consol.setCaretPosition(TA_Consol.getDocument().getLength());
	}

	public void addConsolNoLR(String text) {
		TA_Consol.append(text);
		TA_Consol.setCaretPosition(TA_Consol.getDocument().getLength());
	}

	public void AllChat(String text) {
		TA_AllChat.append(text + "\r\n");
		TA_AllChat.setCaretPosition(TA_AllChat.getDocument().getLength());
	}

	public void addPrivateChat(String from, String to, String text) {
		Calendar cal = Calendar.getInstance();
		AllChat(sdf.format(cal.getTime()) + "(" + from + "->" + to + "):"
				+ text + "\r\n");
		TA_Private.append(from + "->" + to + " : " + text + "\r\n");
		TA_Private.setCaretPosition(TA_Private.getDocument().getLength());
	}
	//增加 顯示武器裝備加成
	public void addItemTable(String itemname, long l, long id, int i) {
		Object o[] = { itemname, l, id, i};
		DTM_Item.addRow(o);
	}
	//public void addItemTable(String itemname, int cont, long id) {
	//	Object o[] = { itemname, cont, id };
	//	DTM_Item.addRow(o);
	//}

	public void iniTable() {
		int cont = DTM_Item.getRowCount();
		while (cont > 1) {
			DTM_Item.removeRow(cont - 1);
			cont--;
		}
	}
	public void addPlayerTable(String account, String name, StringBuilder stringBuilder) {
		Object o[] = { account, name, stringBuilder };
		DTM.addRow(o);
	}


	private int findPlayer(String name) {
		try {
			for (int j = 0; j < DTM.getRowCount(); j++) {
				if (name.equals(DTM.getValueAt(j, 1).toString())) {
					return j; // 找到了
				}
			}
			// 沒找到
			return -1;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			// row_counter=0;
			// dont do
			return -1;
		}
	}

	public void delPlayerTable(String name) {
		int findNum = 0;
		if ((findNum = findPlayer(name)) != -1) { // 找到
			DTM.removeRow(findNum);
		}
	}

	private void iniPlayerTable() {
		String s[] = { "帳號", "角色名稱", "IP" };
		DTM.setColumnIdentifiers(s);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {

		F_Player = new javax.swing.JFrame();
		L_Name = new javax.swing.JLabel();
		L_Title = new javax.swing.JLabel();
		L_Account = new javax.swing.JLabel();
		L_Leavl = new javax.swing.JLabel();
		L_AccessLevel = new javax.swing.JLabel();
		L_Exp = new javax.swing.JLabel();
		L_Hp = new javax.swing.JLabel();
		L_Mp = new javax.swing.JLabel();
		L_Int = new javax.swing.JLabel();
		L_Str = new javax.swing.JLabel();
		L_Con = new javax.swing.JLabel();
		L_Dex = new javax.swing.JLabel();
		L_Wis = new javax.swing.JLabel();
		L_Cha = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		L_Image = new javax.swing.JLabel();
		L_Clan = new javax.swing.JLabel();
		L_AccessLevel7 = new javax.swing.JLabel();
		L_Mp1 = new javax.swing.JLabel();
		L_Map = new javax.swing.JLabel();
		L_X = new javax.swing.JLabel();
		L_Y = new javax.swing.JLabel();
		TF_Account = new javax.swing.JTextField();
		TF_Name = new javax.swing.JTextField();
		TF_Title = new javax.swing.JTextField();
		TF_Level = new javax.swing.JTextField();
		TF_AccessLevel = new javax.swing.JTextField();
		TF_Clan = new javax.swing.JTextField();
		TF_Exp = new javax.swing.JTextField();
		TF_Hp = new javax.swing.JTextField();
		TF_Mp = new javax.swing.JTextField();
		TF_Sex = new javax.swing.JTextField();
		TF_Str = new javax.swing.JTextField();
		TF_Con = new javax.swing.JTextField();
		TF_Dex = new javax.swing.JTextField();
		TF_Wis = new javax.swing.JTextField();
		TF_Int = new javax.swing.JTextField();
		TF_Cha = new javax.swing.JTextField();
		TF_Ac = new javax.swing.JTextField();
		TF_Map = new javax.swing.JTextField();
		TF_X = new javax.swing.JTextField();
		TF_Y = new javax.swing.JTextField();
		B_Item = new javax.swing.JButton();
		CB_Item = new javax.swing.JComboBox();
		PM_Player = new javax.swing.JPopupMenu();
		MI_Kill = new javax.swing.JMenuItem();
		MI_BanIP = new javax.swing.JMenuItem();
		MI_Item = new javax.swing.JMenuItem();
		MI_ItemX = new javax.swing.JMenuItem();
		MI_PC = new javax.swing.JMenuItem();
		MI_spawn = new javax.swing.JMenuItem();
		MI_Item2 = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		MI_ShowPlayer = new javax.swing.JMenuItem();
		jSeparator2 = new javax.swing.JSeparator();
		MI_Whisper = new javax.swing.JMenuItem();
		jLabel1 = new javax.swing.JLabel();
		D_Item = new javax.swing.JDialog();
		jScrollPane1 = new javax.swing.JScrollPane();
		T_Item = new JTable(DTM_Item);
		SP_Split = new javax.swing.JSplitPane();
		TP = new javax.swing.JTabbedPane();
		SP_Consol = new javax.swing.JScrollPane();
		TA_Consol = new javax.swing.JTextArea();
		SP_AllChat = new javax.swing.JScrollPane();
		TA_AllChat = new javax.swing.JTextArea();
		SP_World = new javax.swing.JScrollPane();
		TA_World = new javax.swing.JTextArea();
		SP_Normal = new javax.swing.JScrollPane();
		TA_Normal = new javax.swing.JTextArea();
		SP_ = new javax.swing.JScrollPane();
		TA_Private = new javax.swing.JTextArea();
		SP_Clan = new javax.swing.JScrollPane();
		TA_Clan = new javax.swing.JTextArea();
		SP_Team = new javax.swing.JScrollPane();
		TA_Team = new javax.swing.JTextArea();
		SP_player = new javax.swing.JScrollPane();
		T_Player = new JTable(DTM);
		jPanel2 = new javax.swing.JPanel();
		CB_Channel = new javax.swing.JComboBox();
		TF_Target = new javax.swing.JTextField();
		B_Submit = new javax.swing.JButton();
		TF_Msg = new javax.swing.JTextField();
		MB = new javax.swing.JMenuBar();
		M_File = new javax.swing.JMenu();
		MI_Save = new javax.swing.JMenuItem();
		TPCHECKPACK = new javax.swing.JMenuItem();
		jSeparator3 = new javax.swing.JSeparator();
		MI_SetClose = new javax.swing.JMenuItem();
		MI_Close = new javax.swing.JMenuItem();
		M_Edit = new javax.swing.JMenu();
		M_Special = new javax.swing.JMenu();
		//增加管理器倍數 by24918380 
        MI_LA= new javax.swing.JMenuItem(); // 正義倍率(W)
        MI_ATTR_ENCHANT_CHANCE= new javax.swing.JMenuItem(); // 屬性強化倍率(X) 
        MI_CHANCE_ARMOR = new javax.swing.JMenuItem(); // 防具強化值倍率(S)
        MI_CHANCE_WEAPON = new javax.swing.JMenuItem(); // 武器強化值倍率(Q)
        MI_XP_PET = new javax.swing.JMenuItem(); // 寵物經驗值倍率(G)
        //增加管理器倍數 by24918380 
		// 在線修改經驗/掉寶/掉錢倍率by cwt123
		MI_Exp = new javax.swing.JMenuItem(); // 經驗值倍率(F)
		MI_Drop = new javax.swing.JMenuItem(); // 掉寶倍率(D)
		MI_Adena = new javax.swing.JMenuItem(); // 掉錢倍率(M)
		// 在線修改經驗/掉寶/掉錢倍率by cwt123
		MI_Angel = new javax.swing.JMenuItem();
		MI_AllBuff = new javax.swing.JMenuItem();
		MI_Allbux= new javax.swing.JMenuItem();
		MI_AllBIG = new javax.swing.JMenuItem();
		MI_AllRess = new javax.swing.JMenuItem();
		MI_AllRessstop = new javax.swing.JMenuItem();
		L_Name.setText("名字:");
		L_Title.setText("稱號:");
		L_Account.setText("帳號:");
		L_Leavl.setText("等級:");
		L_AccessLevel.setText("權限:");
		L_Exp.setText(" Exp:");
		L_Hp.setText("Hp:");
		L_Mp.setText("Mp:");
		L_Int.setText("智力:");
		L_Str.setText("力量:");
		L_Con.setText("體質:");
		L_Dex.setText("敏捷:");
		L_Wis.setText("精神:");
		L_Cha.setText("魅力:");
		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						jPanel1Layout.createSequentialGroup().addContainerGap()
						.addComponent(L_Image,
								javax.swing.GroupLayout.DEFAULT_SIZE, 108,
								Short.MAX_VALUE).addContainerGap()));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						jPanel1Layout.createSequentialGroup().addContainerGap()
						.addComponent(L_Image,
								javax.swing.GroupLayout.DEFAULT_SIZE, 180,
								Short.MAX_VALUE).addContainerGap()));
		L_Clan.setText("血盟:");
		L_AccessLevel7.setText("防禦力:");
		L_Mp1.setText("性別:");
		L_Map.setText("Map:");
		L_X.setText("X:");
		L_Y.setText("Y:");
		TF_Account.setEditable(false);
		TF_Name.setEditable(false);
		TF_Title.setEditable(false);
		TF_Level.setEditable(false);
		TF_AccessLevel.setEditable(false);
		TF_Clan.setEditable(false);
		TF_Exp.setEditable(false);
		TF_Hp.setEditable(false);
		TF_Mp.setEditable(false);
		TF_Sex.setEditable(false);
		TF_Str.setEditable(false);
		TF_Con.setEditable(false);
		TF_Dex.setEditable(false);
		TF_Wis.setEditable(false);
		TF_Int.setEditable(false);
		TF_Cha.setEditable(false);
		TF_Ac.setEditable(false);
		TF_Map.setEditable(false);
		TF_X.setEditable(false);
		TF_Y.setEditable(false);
		B_Item.setText("物品欄顯示");
		B_Item.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				B_ItemActionPerformed(evt);
			}
		});
		CB_Item.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"0,身上物品", "1,倉庫", "2,血盟倉庫", "3,妖森倉庫" }));
		javax.swing.GroupLayout F_PlayerLayout = new javax.swing.GroupLayout(
				F_Player.getContentPane());
		F_Player.getContentPane().setLayout(F_PlayerLayout);
		F_PlayerLayout
		.setHorizontalGroup(F_PlayerLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								F_PlayerLayout
								.createSequentialGroup()
								.addComponent(
										jPanel1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addGroup(
												F_PlayerLayout
												.createParallelGroup(
														javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																F_PlayerLayout
																.createSequentialGroup()
																.addComponent(
																		L_Account)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(
																						TF_Account,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						108,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																						.addGroup(
																								F_PlayerLayout
																								.createSequentialGroup()
																								.addGroup(
																										F_PlayerLayout
																										.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING,
																												false)
																												.addComponent(
																														L_Name)
																														.addComponent(
																																L_Title)
																																.addComponent(
																																		L_Leavl)
																																		.addComponent(
																																				L_AccessLevel)
																																				.addComponent(
																																						L_Clan)
																																						.addComponent(
																																								L_Exp,
																																								javax.swing.GroupLayout.Alignment.TRAILING,
																																								javax.swing.GroupLayout.DEFAULT_SIZE,
																																								27,
																																								Short.MAX_VALUE)
																																								.addComponent(
																																										L_Hp,
																																										javax.swing.GroupLayout.Alignment.TRAILING)
																																										.addComponent(
																																												L_Mp,
																																												javax.swing.GroupLayout.Alignment.TRAILING)
																																												.addComponent(
																																														L_Mp1,
																																														javax.swing.GroupLayout.Alignment.TRAILING))
																																														.addPreferredGap(
																																																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																.addGroup(
																																																		F_PlayerLayout
																																																		.createParallelGroup(
																																																				javax.swing.GroupLayout.Alignment.LEADING,
																																																				false)
																																																				.addComponent(
																																																						TF_Mp,
																																																						javax.swing.GroupLayout.DEFAULT_SIZE,
																																																						108,
																																																						Short.MAX_VALUE)
																																																						.addComponent(
																																																								TF_Sex,
																																																								javax.swing.GroupLayout.DEFAULT_SIZE,
																																																								108,
																																																								Short.MAX_VALUE)
																																																								.addComponent(
																																																										TF_Hp,
																																																										javax.swing.GroupLayout.DEFAULT_SIZE,
																																																										108,
																																																										Short.MAX_VALUE)
																																																										.addComponent(
																																																												TF_Exp,
																																																												javax.swing.GroupLayout.DEFAULT_SIZE,
																																																												108,
																																																												Short.MAX_VALUE)
																																																												.addComponent(
																																																														TF_Clan,
																																																														javax.swing.GroupLayout.DEFAULT_SIZE,
																																																														108,
																																																														Short.MAX_VALUE)
																																																														.addComponent(
																																																																TF_AccessLevel,
																																																																javax.swing.GroupLayout.DEFAULT_SIZE,
																																																																108,
																																																																Short.MAX_VALUE)
																																																																.addComponent(
																																																																		TF_Level,
																																																																		javax.swing.GroupLayout.DEFAULT_SIZE,
																																																																		108,
																																																																		Short.MAX_VALUE)
																																																																		.addComponent(
																																																																				TF_Title,
																																																																				javax.swing.GroupLayout.DEFAULT_SIZE,
																																																																				108,
																																																																				Short.MAX_VALUE)
																																																																				.addComponent(
																																																																						TF_Name,
																																																																						javax.swing.GroupLayout.DEFAULT_SIZE,
																																																																						108,
																																																																						Short.MAX_VALUE)
																																																																						.addComponent(
																																																																								CB_Item,
																																																																								0,
																																																																								javax.swing.GroupLayout.DEFAULT_SIZE,
																																																																								Short.MAX_VALUE))))
																																																																								.addPreferredGap(
																																																																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																										.addGroup(
																																																																												F_PlayerLayout
																																																																												.createParallelGroup(
																																																																														javax.swing.GroupLayout.Alignment.LEADING)
																																																																														.addGroup(
																																																																																F_PlayerLayout
																																																																																.createParallelGroup(
																																																																																		javax.swing.GroupLayout.Alignment.TRAILING)
																																																																																		.addGroup(
																																																																																				F_PlayerLayout
																																																																																				.createSequentialGroup()
																																																																																				.addGroup(
																																																																																						F_PlayerLayout
																																																																																						.createParallelGroup(
																																																																																								javax.swing.GroupLayout.Alignment.TRAILING)
																																																																																								.addComponent(
																																																																																										L_Int)
																																																																																										.addComponent(
																																																																																												L_Wis)
																																																																																												.addComponent(
																																																																																														L_Dex)
																																																																																														.addComponent(
																																																																																																L_Cha)
																																																																																																.addComponent(
																																																																																																		L_AccessLevel7)
																																																																																																		.addComponent(
																																																																																																				L_Con)
																																																																																																				.addComponent(
																																																																																																						L_Str)
																																																																																																						.addComponent(
																																																																																																								L_Map)
																																																																																																								.addComponent(
																																																																																																										L_X))
																																																																																																										.addPreferredGap(
																																																																																																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																																												.addGroup(
																																																																																																														F_PlayerLayout
																																																																																																														.createParallelGroup(
																																																																																																																javax.swing.GroupLayout.Alignment.LEADING)
																																																																																																																.addComponent(
																																																																																																																		TF_Str,
																																																																																																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																		108,
																																																																																																																		javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																		.addComponent(
																																																																																																																				TF_Con,
																																																																																																																				javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																				108,
																																																																																																																				javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																				.addComponent(
																																																																																																																						TF_Dex,
																																																																																																																						javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																						108,
																																																																																																																						javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																						.addComponent(
																																																																																																																								TF_Wis,
																																																																																																																								javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																								108,
																																																																																																																								javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																								.addComponent(
																																																																																																																										TF_Int,
																																																																																																																										javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																										108,
																																																																																																																										javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																										.addComponent(
																																																																																																																												TF_Cha,
																																																																																																																												javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																												108,
																																																																																																																												javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																												.addComponent(
																																																																																																																														TF_Ac,
																																																																																																																														javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																														108,
																																																																																																																														javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																														.addComponent(
																																																																																																																																TF_Map,
																																																																																																																																javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																108,
																																																																																																																																javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																																.addComponent(
																																																																																																																																		TF_X,
																																																																																																																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																		108,
																																																																																																																																		javax.swing.GroupLayout.PREFERRED_SIZE)))
																																																																																																																																		.addGroup(
																																																																																																																																				F_PlayerLayout
																																																																																																																																				.createSequentialGroup()
																																																																																																																																				.addComponent(
																																																																																																																																						L_Y)
																																																																																																																																						.addPreferredGap(
																																																																																																																																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																																																																								.addComponent(
																																																																																																																																										TF_Y,
																																																																																																																																										javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																										108,
																																																																																																																																										javax.swing.GroupLayout.PREFERRED_SIZE)))
																																																																																																																																										.addComponent(B_Item))
																																																																																																																																										.addContainerGap(52, Short.MAX_VALUE)));
		F_PlayerLayout
		.setVerticalGroup(F_PlayerLayout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								F_PlayerLayout
								.createSequentialGroup()
								.addGroup(
										F_PlayerLayout
										.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														F_PlayerLayout
														.createSequentialGroup()
														.addContainerGap()
														.addGroup(
																F_PlayerLayout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.BASELINE)
																		.addComponent(
																				L_Account)
																				.addComponent(
																						TF_Account,
																						javax.swing.GroupLayout.PREFERRED_SIZE,
																						18,
																						javax.swing.GroupLayout.PREFERRED_SIZE))
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																								.addGroup(
																										F_PlayerLayout
																										.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.BASELINE)
																												.addComponent(
																														L_Name)
																														.addComponent(
																																TF_Name,
																																javax.swing.GroupLayout.PREFERRED_SIZE,
																																18,
																																javax.swing.GroupLayout.PREFERRED_SIZE))
																																.addGap(
																																		5,
																																		5,
																																		5)
																																		.addGroup(
																																				F_PlayerLayout
																																				.createParallelGroup(
																																						javax.swing.GroupLayout.Alignment.BASELINE)
																																						.addComponent(
																																								L_Title)
																																								.addComponent(
																																										TF_Title,
																																										javax.swing.GroupLayout.PREFERRED_SIZE,
																																										18,
																																										javax.swing.GroupLayout.PREFERRED_SIZE))
																																										.addPreferredGap(
																																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																												.addGroup(
																																														F_PlayerLayout
																																														.createParallelGroup(
																																																javax.swing.GroupLayout.Alignment.BASELINE)
																																																.addComponent(
																																																		L_Leavl)
																																																		.addComponent(
																																																				TF_Level,
																																																				javax.swing.GroupLayout.PREFERRED_SIZE,
																																																				18,
																																																				javax.swing.GroupLayout.PREFERRED_SIZE))
																																																				.addGap(
																																																						5,
																																																						5,
																																																						5)
																																																						.addGroup(
																																																								F_PlayerLayout
																																																								.createParallelGroup(
																																																										javax.swing.GroupLayout.Alignment.BASELINE)
																																																										.addComponent(
																																																												L_AccessLevel)
																																																												.addComponent(
																																																														TF_AccessLevel,
																																																														javax.swing.GroupLayout.PREFERRED_SIZE,
																																																														18,
																																																														javax.swing.GroupLayout.PREFERRED_SIZE))
																																																														.addPreferredGap(
																																																																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																.addGroup(
																																																																		F_PlayerLayout
																																																																		.createParallelGroup(
																																																																				javax.swing.GroupLayout.Alignment.BASELINE)
																																																																				.addComponent(
																																																																						L_Clan)
																																																																						.addComponent(
																																																																								TF_Clan,
																																																																								javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																								18,
																																																																								javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																								.addPreferredGap(
																																																																										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																										.addGroup(
																																																																												F_PlayerLayout
																																																																												.createParallelGroup(
																																																																														javax.swing.GroupLayout.Alignment.BASELINE)
																																																																														.addComponent(
																																																																																L_Exp)
																																																																																.addComponent(
																																																																																		TF_Exp,
																																																																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																		18,
																																																																																		javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																		.addPreferredGap(
																																																																																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																				.addGroup(
																																																																																						F_PlayerLayout
																																																																																						.createParallelGroup(
																																																																																								javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																								.addComponent(
																																																																																										L_Hp)
																																																																																										.addComponent(
																																																																																												TF_Hp,
																																																																																												javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																												18,
																																																																																												javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																												.addPreferredGap(
																																																																																														javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																														.addGroup(
																																																																																																F_PlayerLayout
																																																																																																.createParallelGroup(
																																																																																																		javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																		.addComponent(
																																																																																																				L_Mp)
																																																																																																				.addComponent(
																																																																																																						TF_Mp,
																																																																																																						javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																						18,
																																																																																																						javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																						.addPreferredGap(
																																																																																																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																																								.addGroup(
																																																																																																										F_PlayerLayout
																																																																																																										.createParallelGroup(
																																																																																																												javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																												.addComponent(
																																																																																																														L_Mp1)
																																																																																																														.addComponent(
																																																																																																																TF_Sex,
																																																																																																																javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																18,
																																																																																																																javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																.addComponent(
																																																																																																																		L_Y)
																																																																																																																		.addComponent(
																																																																																																																				TF_Y,
																																																																																																																				javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																				18,
																																																																																																																				javax.swing.GroupLayout.PREFERRED_SIZE)))
																																																																																																																				.addGroup(
																																																																																																																						F_PlayerLayout
																																																																																																																						.createSequentialGroup()
																																																																																																																						.addGap(
																																																																																																																								26,
																																																																																																																								26,
																																																																																																																								26)
																																																																																																																								.addComponent(
																																																																																																																										jPanel1,
																																																																																																																										javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																										javax.swing.GroupLayout.DEFAULT_SIZE,
																																																																																																																										javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																																										.addGroup(
																																																																																																																												F_PlayerLayout
																																																																																																																												.createSequentialGroup()
																																																																																																																												.addContainerGap()
																																																																																																																												.addGroup(
																																																																																																																														F_PlayerLayout
																																																																																																																														.createParallelGroup(
																																																																																																																																javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																.addComponent(
																																																																																																																																		L_Str)
																																																																																																																																		.addComponent(
																																																																																																																																				TF_Str,
																																																																																																																																				javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																				18,
																																																																																																																																				javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																																																				.addPreferredGap(
																																																																																																																																						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																																																																						.addGroup(
																																																																																																																																								F_PlayerLayout
																																																																																																																																								.createParallelGroup(
																																																																																																																																										javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																										.addComponent(
																																																																																																																																												L_Con)
																																																																																																																																												.addComponent(
																																																																																																																																														TF_Con,
																																																																																																																																														javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																														18,
																																																																																																																																														javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																																																														.addGap(
																																																																																																																																																5,
																																																																																																																																																5,
																																																																																																																																																5)
																																																																																																																																																.addGroup(
																																																																																																																																																		F_PlayerLayout
																																																																																																																																																		.createParallelGroup(
																																																																																																																																																				javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																																				.addComponent(
																																																																																																																																																						L_Dex)
																																																																																																																																																						.addComponent(
																																																																																																																																																								TF_Dex,
																																																																																																																																																								javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																																								18,
																																																																																																																																																								javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																																																																								.addGap(
																																																																																																																																																										5,
																																																																																																																																																										5,
																																																																																																																																																										5)
																																																																																																																																																										.addGroup(
																																																																																																																																																												F_PlayerLayout
																																																																																																																																																												.createParallelGroup(
																																																																																																																																																														javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																																														.addComponent(
																																																																																																																																																																L_Wis)
																																																																																																																																																																.addComponent(
																																																																																																																																																																		TF_Wis,
																																																																																																																																																																		javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																																																		18,
																																																																																																																																																																		javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																																																																																		.addGap(
																																																																																																																																																																				5,
																																																																																																																																																																				5,
																																																																																																																																																																				5)
																																																																																																																																																																				.addGroup(
																																																																																																																																																																						F_PlayerLayout
																																																																																																																																																																						.createParallelGroup(
																																																																																																																																																																								javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																																																								.addComponent(
																																																																																																																																																																										L_Int)
																																																																																																																																																																										.addComponent(
																																																																																																																																																																												TF_Int,
																																																																																																																																																																												javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																																																												18,
																																																																																																																																																																												javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																																																																																												.addPreferredGap(
																																																																																																																																																																														javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																																																																																																														.addGroup(
																																																																																																																																																																																F_PlayerLayout
																																																																																																																																																																																.createParallelGroup(
																																																																																																																																																																																		javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																																																																		.addComponent(
																																																																																																																																																																																				L_Cha)
																																																																																																																																																																																				.addComponent(
																																																																																																																																																																																						TF_Cha,
																																																																																																																																																																																						javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																																																																						18,
																																																																																																																																																																																						javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																																																																																																						.addPreferredGap(
																																																																																																																																																																																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																																																																																																																								.addGroup(
																																																																																																																																																																																										F_PlayerLayout
																																																																																																																																																																																										.createParallelGroup(
																																																																																																																																																																																												javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																																																																												.addComponent(
																																																																																																																																																																																														L_AccessLevel7)
																																																																																																																																																																																														.addComponent(
																																																																																																																																																																																																TF_Ac,
																																																																																																																																																																																																javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																																																																																18,
																																																																																																																																																																																																javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																																																																																																																.addPreferredGap(
																																																																																																																																																																																																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																																																																																																																																		.addGroup(
																																																																																																																																																																																																				F_PlayerLayout
																																																																																																																																																																																																				.createParallelGroup(
																																																																																																																																																																																																						javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																																																																																						.addComponent(
																																																																																																																																																																																																								L_Map)
																																																																																																																																																																																																								.addComponent(
																																																																																																																																																																																																										TF_Map,
																																																																																																																																																																																																										javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																																																																																										18,
																																																																																																																																																																																																										javax.swing.GroupLayout.PREFERRED_SIZE))
																																																																																																																																																																																																										.addPreferredGap(
																																																																																																																																																																																																												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																																																																																																																																												.addGroup(
																																																																																																																																																																																																														F_PlayerLayout
																																																																																																																																																																																																														.createParallelGroup(
																																																																																																																																																																																																																javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																																																																																																.addComponent(
																																																																																																																																																																																																																		L_X)
																																																																																																																																																																																																																		.addComponent(
																																																																																																																																																																																																																				TF_X,
																																																																																																																																																																																																																				javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																																																																																																				18,
																																																																																																																																																																																																																				javax.swing.GroupLayout.PREFERRED_SIZE))))
																																																																																																																																																																																																																				.addPreferredGap(
																																																																																																																																																																																																																						javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																																																																																																																																																																																																																						.addGroup(
																																																																																																																																																																																																																								F_PlayerLayout
																																																																																																																																																																																																																								.createParallelGroup(
																																																																																																																																																																																																																										javax.swing.GroupLayout.Alignment.BASELINE)
																																																																																																																																																																																																																										.addComponent(
																																																																																																																																																																																																																												CB_Item,
																																																																																																																																																																																																																												javax.swing.GroupLayout.PREFERRED_SIZE,
																																																																																																																																																																																																																												javax.swing.GroupLayout.DEFAULT_SIZE,
																																																																																																																																																																																																																												javax.swing.GroupLayout.PREFERRED_SIZE)
																																																																																																																																																																																																																												.addComponent(B_Item))
																																																																																																																																																																																																																												.addContainerGap(27, Short.MAX_VALUE)));
		MI_Kill.setMnemonic('K');
		MI_Kill.setText("強制踢除(K)");
		PM_Player.add(MI_Kill);
		MI_BanIP.setMnemonic('B');
		MI_BanIP.setText("封鎖IP(B)");
		PM_Player.add(MI_BanIP);
		PM_Player.add(jSeparator1);
		
		MI_ItemX.setMnemonic('6');
		MI_ItemX.setText("物品增加屬性(6)");
		PM_Player.add(MI_ItemX);
		PM_Player.add(jSeparator1);
		
		MI_PC.setMnemonic('[');
		MI_PC.setText("屬性變更([)");
		PM_Player.add(MI_PC);
		PM_Player.add(jSeparator1);
		
		MI_Item.setMnemonic('1');
		MI_Item.setText("贈送物品(1)");
		PM_Player.add(MI_Item);
		PM_Player.add(jSeparator1);
		MI_Item2.setMnemonic('2');
		MI_Item2.setText("刪除物品(2)");
		PM_Player.add(MI_Item2);
		PM_Player.add(jSeparator1);	
		MI_ShowPlayer.setMnemonic('P');
		MI_ShowPlayer.setText("玩家資料(P)");
		PM_Player.add(MI_ShowPlayer);
		PM_Player.add(jSeparator2);
		MI_Whisper.setMnemonic('W');
		MI_Whisper.setText("密語(W)");
		PM_Player.add(MI_Whisper);
		jLabel1.setText("jLabel1");
		D_Item.getContentPane().setLayout(new java.awt.GridLayout(1, 0));
		jScrollPane1.setViewportView(T_Item);
		D_Item.getContentPane().add(jScrollPane1);
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("四海製作修改  私自買賣,後果自負。");
		setLocationByPlatform(true);
		setMinimumSize(new java.awt.Dimension(780, 500));
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				formWindowClosed(evt);
			}
		});
		SP_Split.setDividerLocation(550);
		SP_Consol.setAutoscrolls(true);
		TA_Consol.setBackground(new java.awt.Color(0, 0, 0));
		TA_Consol.setColumns(20);
		TA_Consol.setEditable(false);
		TA_Consol.setForeground(new java.awt.Color(255, 255, 255));
		TA_Consol.setRows(5);
		TA_Consol.setEnabled(false);
		SP_Consol.setViewportView(TA_Consol);
		TP.addTab("Consol", SP_Consol);
		SP_AllChat.setAutoscrolls(true);
		TA_AllChat.setBackground(new java.awt.Color(0, 0, 0));
		TA_AllChat.setColumns(20);
		TA_AllChat.setEditable(false);
		TA_AllChat.setForeground(new java.awt.Color(255, 255, 255));
		TA_AllChat.setRows(5);
		SP_AllChat.setViewportView(TA_AllChat);
		TP.addTab("全部頻", SP_AllChat);
		SP_World.setAutoscrolls(true);
		TA_World.setColumns(20);
		TA_World.setEditable(false);
		TA_World.setForeground(new java.awt.Color(0, 0, 204));
		TA_World.setRows(5);
		TA_World.setEnabled(false);
		SP_World.setViewportView(TA_World);
		TP.addTab("世界 ", SP_World);
		SP_Normal.setAutoscrolls(true);
		TA_Normal.setColumns(20);
		TA_Normal.setEditable(false);
		TA_Normal.setRows(5);
		TA_Normal.setEnabled(false);
		SP_Normal.setViewportView(TA_Normal);
		TP.addTab("一般", SP_Normal);
		SP_.setAutoscrolls(true);
		TA_Private.setColumns(20);
		TA_Private.setEditable(false);
		TA_Private.setForeground(new java.awt.Color(204, 0, 51));
		TA_Private.setRows(5);
		TA_Private.setEnabled(false);
		SP_.setViewportView(TA_Private);
		TP.addTab("密語", SP_);
		SP_Clan.setAutoscrolls(true);
		TA_Clan.setColumns(20);
		TA_Clan.setEditable(false);
		TA_Clan.setForeground(new java.awt.Color(153, 51, 0));
		TA_Clan.setRows(5);
		TA_Clan.setEnabled(false);
		SP_Clan.setViewportView(TA_Clan);
		TP.addTab("血盟", SP_Clan);
		SP_Team.setAutoscrolls(true);
		TA_Team.setColumns(20);
		TA_Team.setEditable(false);
		TA_Team.setForeground(new java.awt.Color(102, 0, 102));
		TA_Team.setRows(5);
		TA_Team.setEnabled(false);
		SP_Team.setViewportView(TA_Team);
		TP.addTab("組隊", SP_Team);
		SP_Split.setLeftComponent(TP);
		T_Player.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				T_PlayerMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				T_PlayerMouseReleased(evt);
			}
		});
		SP_player.setViewportView(T_Player);
		SP_Split.setRightComponent(SP_player);
		getContentPane().add(SP_Split, java.awt.BorderLayout.CENTER);
		CB_Channel.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"訊息頻道", "密語" }));
		B_Submit.setText("發送");
		B_Submit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				B_SubmitActionPerformed(evt);
			}
		});
		TF_Msg.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				TF_MsgKeyPressed(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
		.setHorizontalGroup(jPanel2Layout
				.createParallelGroup(
						javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
								.createSequentialGroup()
								.addComponent(
										CB_Channel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(
														TF_Target,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														68,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		TF_Msg,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		310,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																				.addComponent(B_Submit).addGap(175,
																						175, 175)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						jPanel2Layout.createSequentialGroup().addGap(6, 6, 6).addGroup(
								jPanel2Layout.createParallelGroup(
										javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(CB_Channel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(TF_Target,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(TF_Msg,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
																.addComponent(B_Submit))));
		getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
		M_File.setMnemonic('F');
		M_File.setText("設置(F)");
		MI_Save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		MI_Save.setMnemonic('S');
		MI_Save.setText("啟動端口(S)");
		M_File.add(MI_Save);
		M_File.add(jSeparator3);
		MI_Close.setMnemonic('C');
		MI_Close.setText("關閉端口(C)");
		M_File.add(MI_Close);
		TPCHECKPACK.setMnemonic('t');
		TPCHECKPACK.setText("客戶端封包驗證(t)");
		M_File.add(TPCHECKPACK);
		MI_SetClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_E,
				java.awt.event.InputEvent.CTRL_MASK));
		MI_SetClose.setMnemonic('E');
		MI_SetClose.setText("設定關閉伺服器(E)...");
		M_File.add(MI_SetClose);
		MB.add(M_File);
		M_Edit.setMnemonic('E');
		M_Edit.setText("編輯(E)");
		//增加管理器倍數 by24918380
        MI_LA.setMnemonic('W');
        MI_LA.setText("正義倍率(W)");
        M_Edit.add(MI_LA);
        MI_ATTR_ENCHANT_CHANCE.setMnemonic('X');
        MI_ATTR_ENCHANT_CHANCE.setText("屬性強化倍率(X)");
        M_Edit.add(MI_ATTR_ENCHANT_CHANCE);
        MI_CHANCE_ARMOR.setMnemonic('S');
        MI_CHANCE_ARMOR.setText("防具強化倍率(S)");
        M_Edit.add(MI_CHANCE_ARMOR);
        MI_CHANCE_WEAPON.setMnemonic('Q');
        MI_CHANCE_WEAPON.setText("武器強化倍率(Q)");
        M_Edit.add(MI_CHANCE_WEAPON);
        MI_XP_PET.setMnemonic('G');
        MI_XP_PET.setText("寵物經驗倍率(G)");
        M_Edit.add(MI_XP_PET);
        MI_Exp.setMnemonic('F');
        MI_Exp.setText("經驗倍率(F)");
        M_Edit.add(MI_Exp);
        //增加管理器倍數 by24918380
		// 在線修改經驗/掉寶/掉錢倍率by cwt123
		MI_Exp.setMnemonic('F');
		MI_Exp.setText("經驗倍率(F)");
		MI_Drop.setMnemonic('D');
		MI_Drop.setText("掉寶率(D)");
		MI_Adena.setMnemonic('M');
		MI_Adena.setText("掉錢倍率(M)");
		M_Edit.add(MI_Exp);
		M_Edit.add(MI_Drop);
		M_Edit.add(MI_Adena);
		// 在線修改經驗/掉寶/掉錢倍率by cwt123
		MB.add(M_Edit);
		M_Special.setMnemonic('S');
		M_Special.setText("功能(S)");
		MI_Angel.setMnemonic('A');
		MI_Angel.setText("解封帳號 IP(A)");
		M_Special.add(MI_Angel);
		MI_AllBuff.setMnemonic('B');
		MI_AllBuff.setText("終極祝福(B)");
		M_Special.add(MI_AllBuff);		
		MI_Allbux.setMnemonic('U');
		MI_Allbux.setText("全體復活補血魔(U)");
		M_Special.add(MI_Allbux);
		MI_AllBIG.setMnemonic('3');
		MI_AllBIG.setText("防加速調整(3)");
		M_Special.add(MI_AllBIG);
		
		MI_AllRess.setMnemonic('R');
		MI_AllRess.setText("啟動城戰(R)");
		M_Special.add(MI_AllRess);
		
		MI_AllRessstop.setMnemonic('5');
		MI_AllRessstop.setText("關閉城戰(5)");
		M_Special.add(MI_AllRessstop);
		MB.add(M_Special);
		setJMenuBar(MB);
		pack();
	}// </editor-fold>//GEN-END:initComponents

	// 線上玩家清單點兩下
	private void T_PlayerMouseReleased(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_T_PlayerMouseReleased
		// 點左鍵兩下
		if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
			select = T_Player.getSelectedRow();
			setPlayerView((String) DTM.getValueAt(select, 1));
			F_Player.pack();
			F_Player.setVisible(true);
		}
		// 顯示快顯功能表
		if (evt.isPopupTrigger()) {
			select = T_Player.getSelectedRow();
			PM_Player.show(T_Player, evt.getX(), evt.getY());
		}
	}// GEN-LAST:event_T_PlayerMouseReleased

	// 關閉控制視窗
	private void formWindowClosed(java.awt.event.WindowEvent evt) {
		closeServer();
	}

	// 關閉伺服器
	private void closeServer() {
		// tray.remove(trayIcon);
		//saveChatData(false);
		//GameServer.get().shutdown();
		//System.exit(0);
	}

	private void T_PlayerMousePressed(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_T_PlayerMousePressed
		processEvent(evt);
	}// GEN-LAST:event_T_PlayerMousePressed

	private void B_SubmitActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_B_SubmitActionPerformed
		submitMsg(CB_Channel.getSelectedIndex());
	}// GEN-LAST:event_B_SubmitActionPerformed

	private void TF_MsgKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_TF_MsgKeyPressed
		if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
			submitMsg(CB_Channel.getSelectedIndex());
		}
	}// GEN-LAST:event_TF_MsgKeyPressed

	private void submitMsg(int select) {
		if (TF_Msg.getText().equals("")) {
			return;
		}
		switch (select) {
		case 0:// 訊息頻
			World.get().broadcastServerMessage(
					"【管理器訊息】:" + TF_Msg.getText());
			addWorldChat("【管理器】", TF_Msg.getText());
			break;
		case 1:// 密頻
			if (World.get().getPlayer(TF_Target.getText()) == null) {
				return;
			}
			L1PcInstance target = World.get().getPlayer(
					TF_Target.getText());
			target.sendPackets(new S_SystemMessage("【管理器密語】:"
					+ TF_Msg.getText()));
			addPrivateChat("【管理器】", TF_Target.getText(), TF_Msg.getText());
			break;
		}
		TF_Msg.setText("");
	}

	/*
	 * 顯示物品 0:身上 1:倉庫 2:血盟倉庫3:妖森倉庫
	 */
	private void showItemTable(int num) {
		iniTable();
		L1PcInstance pc = L1PcInstance.load(TF_Name.getText());
		if (pc.getInventory().getSize() == 0) {
			//此地方需要做宣告連續2次
			items(pc);
			//此地方需要做宣告連續2次
		}
		L1Inventory inv = null;
		switch (num) {
		case 0:// 0:身上
			if (pc.getInventory() == null) {
				return;
			}
			inv = pc.getInventory();
			D_Item.setTitle("身上物品");
			for (L1ItemInstance item : inv.getItems()) {
				addItemTable(item.getName2()+" 物品編號"+item.getId(), item.getCount(), item.getItemId(), item.getEnchantLevel());
			}
			break;
		case 1:// 1:倉庫
			if (pc.getDwarfInventory() == null) {
				return;
			}
			D_Item.setTitle("倉庫物品");
			inv = pc.getDwarfInventory();
			for (L1ItemInstance item : inv.getItems()) {
				addItemTable(item.getName2()+" 物品編號"+item.getId(), item.getCount(), item.getItemId(), item.getEnchantLevel());
			}
			break;
		case 2:// 2:血盟倉庫
			//if (pc.getClan().getDwarfForClanInventory() == null) {
			//修復查看角色血盟倉庫 沒有盟無法顯示
			if (pc.getClan() == null) {
			//修復查看角色血盟倉庫 沒有盟無法顯示
				return;
			}
			D_Item.setTitle("血盟倉庫物品");
			inv = pc.getClan().getDwarfForClanInventory();
			for (L1ItemInstance item : inv.getItems()) {
				addItemTable(item.getName2()+" 物品編號"+item.getId(), item.getCount(), item.getItemId(), item.getEnchantLevel());
			}
			break;
		case 3:// 3:妖森倉庫
			if (pc.getDwarfForElfInventory() == null) {
				return;
			}
			D_Item.setTitle("妖森倉庫物品");
			inv = pc.getDwarfForElfInventory();
			for (L1ItemInstance item : inv.getItems()) {
				addItemTable(item.getName2()+" 物品編號"+item.getId(), item.getCount(), item.getItemId(), item.getEnchantLevel());
			}
			break;
		}
		D_Item.setVisible(true);
	}

	private void B_ItemActionPerformed(java.awt.event.ActionEvent evt) {
		showItemTable(CB_Item.getSelectedIndex());
	}

	/*
	 * private void TP_ItemStateChanged(javax.swing.event.ChangeEvent evt) {
	 * //showItemTable(TP_Item.getSelectedIndex()); //showItemTable(0);
	 * //System.out.println("1"); //setPlayerView("asdsad"); }
	 */

	private void setPlayerView(String name) {
		L1PcInstance pc = L1PcInstance.load(name);
		int job = 0;
		switch (pc.getClassId()) {
		case L1PcInstance.CLASSID_PRINCE:
			job = 715;
			break;
		case L1PcInstance.CLASSID_PRINCESS:
			job = 647;
			break;
		case L1PcInstance.CLASSID_KNIGHT_MALE:
			job = 384;
			break;
		case L1PcInstance.CLASSID_KNIGHT_FEMALE:
			job = 317;
			break;
		case L1PcInstance.CLASSID_ELF_MALE:
			job = 247;
			break;
		case L1PcInstance.CLASSID_ELF_FEMALE:
			job = 198;
			break;
		case L1PcInstance.CLASSID_WIZARD_MALE:
			job = 532;
			break;
		case L1PcInstance.CLASSID_WIZARD_FEMALE:
			job = 452;
			break;
		case L1PcInstance.CLASSID_DARK_ELF_MALE:
			job = 145;
			break;
		case L1PcInstance.CLASSID_DARK_ELF_FEMALE:
			job = 25;
			break;
		case L1PcInstance.CLASSID_DRAGON_KNIGHT_MALE:
			job = 903;
			break;
		case L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE:
			job = 930;
			break;
		case L1PcInstance.CLASSID_ILLUSIONIST_MALE:
			job = 1029;
			break;
		case L1PcInstance.CLASSID_ILLUSIONIST_FEMALE:
			job = 1056;
			break;
		case L1PcInstance.CLASSID_WARRIOR_MALE:
			job = 1057;
			break;
		case L1PcInstance.CLASSID_WARRIOR_FEMALE:
			job = 1058;
			break;	

		}
		ImageIcon imageIcon = new ImageIcon("img/" + job + ".png");
		// ImageIcon imageIcon = new ImageIcon("img/"+job+".png");
		Icon icon = (Icon) imageIcon;
		L_Image.setIcon(icon);
		TF_Account.setText(pc.getAccountName());
		TF_Name.setText(pc.getName());
		TF_Title.setText(pc.getTitle());
		TF_AccessLevel.setText("" + pc.getAccessLevel());
		TF_Sex.setText((pc.get_sex() == 1) ? "女" : "男");
		TF_Ac.setText(pc.getAc() + "");
		TF_Cha.setText(pc.getCha() + "");
		TF_Int.setText(pc.getInt() + "");
		TF_Str.setText(pc.getStr() + "");
		TF_Con.setText(pc.getCon() + "");
		TF_Wis.setText(pc.getWis() + "");
		TF_Dex.setText(pc.getDex() + "");
		TF_Exp.setText(pc.getExp() + "");
		TF_Map.setText(pc.getMapId() + "");
		TF_X.setText(pc.getX() + "");
		TF_Y.setText(pc.getY() + "");
		TF_Clan.setText(pc.getClanname());
		TF_Level.setText(pc.getLevel() + "");
		TF_Hp.setText(pc.getCurrentHp() + " / " + pc.getMaxHp());
		TF_Mp.setText(pc.getCurrentMp() + " / " + pc.getMaxMp());
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new J_Main().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton B_Item;

	private javax.swing.JButton B_Submit;

	private javax.swing.JComboBox CB_Channel;

	private javax.swing.JComboBox CB_Item;

	private javax.swing.JDialog D_Item;

	private javax.swing.JFrame F_Player;

	private javax.swing.JLabel L_AccessLevel;

	private javax.swing.JLabel L_AccessLevel7;

	private javax.swing.JLabel L_Account;

	private javax.swing.JLabel L_Cha;

	private javax.swing.JLabel L_Clan;

	private javax.swing.JLabel L_Con;

	private javax.swing.JLabel L_Dex;

	private javax.swing.JLabel L_Exp;

	private javax.swing.JLabel L_Hp;

	private javax.swing.JLabel L_Image;

	private javax.swing.JLabel L_Int;

	private javax.swing.JLabel L_Leavl;

	private javax.swing.JLabel L_Map;

	private javax.swing.JLabel L_Mp;

	private javax.swing.JLabel L_Mp1;

	private javax.swing.JLabel L_Name;

	private javax.swing.JLabel L_Str;

	private javax.swing.JLabel L_Title;

	private javax.swing.JLabel L_Wis;

	private javax.swing.JLabel L_X;

	private javax.swing.JLabel L_Y;

	private javax.swing.JMenuBar MB;
	//增加管理器倍數 by24918380
    private javax.swing.JMenuItem MI_LA;//正義倍率
    private javax.swing.JMenuItem MI_ATTR_ENCHANT_CHANCE;
    private javax.swing.JMenuItem MI_CHANCE_ARMOR;
    private javax.swing.JMenuItem MI_CHANCE_WEAPON;
    private javax.swing.JMenuItem MI_XP_PET;
    //增加管理器倍數 by24918380
	// 在線修改經驗/掉寶/掉錢倍率by cwt123
	private javax.swing.JMenuItem MI_Exp;

	private javax.swing.JMenuItem MI_Drop;

	private javax.swing.JMenuItem MI_Adena;

	// 在線修改經驗/掉寶/掉錢倍率by cwt123
	private javax.swing.JMenuItem MI_AllBuff;
	
	private javax.swing.JMenuItem MI_Allbux;
	
	private javax.swing.JMenuItem MI_AllBIG;

	private javax.swing.JMenuItem MI_AllRess;
	
	private javax.swing.JMenuItem MI_AllRessstop;

	private javax.swing.JMenuItem MI_Angel;

	private javax.swing.JMenuItem MI_BanIP;
	
	private javax.swing.JMenuItem MI_Item;
	
	private javax.swing.JMenuItem MI_ItemX;
	
	private javax.swing.JMenuItem MI_PC;
	
	
	private javax.swing.JMenuItem MI_spawn;
	
	private javax.swing.JMenuItem MI_Item2;

	private javax.swing.JMenuItem MI_Close;

	private javax.swing.JMenuItem MI_Kill;

	private javax.swing.JMenuItem MI_Save;
	
	private javax.swing.JMenuItem TPCHECKPACK;

	private javax.swing.JMenuItem MI_SetClose;

	private javax.swing.JMenuItem MI_ShowPlayer;

	private javax.swing.JMenuItem MI_Whisper;

	private javax.swing.JMenu M_Edit;

	private javax.swing.JMenu M_File;

	private javax.swing.JMenu M_Special;

	private javax.swing.JPopupMenu PM_Player;

	private javax.swing.JScrollPane SP_;

	private javax.swing.JScrollPane SP_AllChat;

	private javax.swing.JScrollPane SP_Clan;

	private javax.swing.JScrollPane SP_Consol;

	private javax.swing.JScrollPane SP_Normal;

	private javax.swing.JSplitPane SP_Split;

	private javax.swing.JScrollPane SP_Team;

	private javax.swing.JScrollPane SP_World;

	private javax.swing.JScrollPane SP_player;

	private static javax.swing.JTextArea TA_AllChat;

	private static javax.swing.JTextArea TA_Clan;

	private static javax.swing.JTextArea TA_Consol;

	private static javax.swing.JTextArea TA_Normal;

	private static javax.swing.JTextArea TA_Private;

	private static javax.swing.JTextArea TA_Team;

	private static javax.swing.JTextArea TA_World;

	private javax.swing.JTextField TF_Ac;

	private javax.swing.JTextField TF_AccessLevel;

	private javax.swing.JTextField TF_Account;

	private javax.swing.JTextField TF_Cha;

	private javax.swing.JTextField TF_Clan;

	private javax.swing.JTextField TF_Con;

	private javax.swing.JTextField TF_Dex;

	private javax.swing.JTextField TF_Exp;

	private javax.swing.JTextField TF_Hp;

	private javax.swing.JTextField TF_Int;

	private javax.swing.JTextField TF_Level;

	private javax.swing.JTextField TF_Map;

	private javax.swing.JTextField TF_Mp;

	private javax.swing.JTextField TF_Msg;

	private javax.swing.JTextField TF_Name;

	private javax.swing.JTextField TF_Sex;

	private javax.swing.JTextField TF_Str;

	private javax.swing.JTextField TF_Target;

	private javax.swing.JTextField TF_Title;

	private javax.swing.JTextField TF_Wis;

	private javax.swing.JTextField TF_X;

	private javax.swing.JTextField TF_Y;

	private javax.swing.JTabbedPane TP;

	private javax.swing.JTable T_Item;

	private javax.swing.JTable T_Player;

	private javax.swing.JLabel jLabel1;

	private javax.swing.JPanel jPanel1;

	private javax.swing.JPanel jPanel2;

	private javax.swing.JScrollPane jScrollPane1;

	private javax.swing.JSeparator jSeparator1;

	private javax.swing.JSeparator jSeparator2;

	private javax.swing.JSeparator jSeparator3;

	// End of variables declaration//GEN-END:variables

	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if ((e.getModifiers() & ActionEvent.MOUSE_EVENT_MASK) == 0) {
			if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0
					|| (e.getModifiers() & MouseEvent.BUTTON2_MASK) != 0) {
				return;
			}
		}
		if (command.equals("強制踢除(K)")) {
			L1PcInstance target = World.get().getPlayer(
					(String) DTM.getValueAt(select, 1));
			if (target != null) {
				addConsol((new StringBuilder()).append(
						"您把玩家：" + (String) DTM.getValueAt(select, 1)
						+ "強制剔除遊戲。").toString());
				target.sendPackets(new S_Disconnect());
			} else {
				addConsol("此玩家不在線上。");
				getInstance().delPlayerTable(target.getName());
			}
			
		/*} else if (command.equals("防加速調整(3)")) {
			String temp2 = "";
			try {
			temp2 = JOptionPane.showInputDialog("請輸入加速限制現在為 "+AcceleratorChecker.CHECK_STRICTNESS);
			if (temp2 == null || temp2.equals("")) {
				return;
			}
			final double set = Double.parseDouble(temp2);
			AcceleratorChecker.CHECK_STRICTNESS = set;
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");
			}*/	
		} else if (command.equals("刪除物品(2)")) {
			String temp2 = "";
			String temp3 = "";
			try {
		     L1PcInstance target = World.get().getPlayer((String) DTM.getValueAt(select, 1));
				if (target == null ){
					JOptionPane.showMessageDialog(this, "此玩家不在線上無法贈送");
					return;
				}
				temp2 = JOptionPane.showInputDialog("請輸入物品ID");
				if (temp2 == null ){
					return;
				}
				int second2 = Integer.valueOf(temp2);
				final L1ItemInstance item = ItemTable.get().createItem(second2);
				if (item == null ){
					JOptionPane.showMessageDialog(this, "物品ID錯誤 請重新輸入");
					return;
				}	
				
				temp3 = JOptionPane.showInputDialog("你確定要刪除"+item.getName2()+"個");
				if (temp3.equals("") ){
					return;
				}
				int second3 = Integer.valueOf(temp3);	
				if (!target.getInventory().checkItem(second2, second3)) {
					 JOptionPane.showMessageDialog(this, "玩家沒有這麼多物品刪除");
					 return;
				}
				target.getInventory().consumeItem(second2, second3);
				addConsol(target.getName()+"刪除"+item.getName2()+second3+"個成功");
				target.sendPackets(new S_KillMessage("GM通過管理器刪除你"+item.getName2()+" "+second3+"個"));
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");		
			}
		} else if (command.equals("屬性變更([)")) {
		 Object[] options ={ "力量", "體力" , "敏捷" , "智力", "精神", "魅力", "權限" , "血魔", "等級" }; 
			int m = JOptionPane.showOptionDialog(null, " 請選擇屬性 ", "玩家屬性變更",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
			L1PcInstance target = World.get().getPlayer((String) DTM.getValueAt(select, 1));
			switch(m){
			case 0:
				String temp0 = JOptionPane.showInputDialog("變更力量為多少");
				if (temp0 == null || temp0.equals("")) {
					return;
				}
				int second0 = Integer.valueOf(temp0);	
				target.addBaseStr((byte) -target.getBaseStr()+second0); // 素WIS值+1
				target.sendPackets(new S_OwnCharStatus(target));
				break;
			case 1:
				String temp1 = JOptionPane.showInputDialog("變更體力為多少");
				if (temp1 == null || temp1.equals("")) {
					return;
				}
				int second1 = Integer.valueOf(temp1);	
				target.addBaseCon((byte) -target.getBaseCon()+second1); // 素WIS值+1
				target.sendPackets(new S_OwnCharStatus(target));
				break;
			case 2:
				String temp2 = JOptionPane.showInputDialog("變更敏捷為多少");
				if (temp2 == null || temp2.equals("")) {
					return;
				}
				int second2 = Integer.valueOf(temp2);	
				target.addBaseDex((byte) -target.getBaseDex()+second2); // 素WIS值+1
				target.sendPackets(new S_OwnCharStatus(target));
				break;
			case 3:
				String temp3 = JOptionPane.showInputDialog("變更智力為多少");
				if (temp3 == null || temp3.equals("")) {
					return;
				}
				int second3 = Integer.valueOf(temp3);	
				target.addBaseInt((byte) -target.getBaseInt()+second3); // 素WIS值+1
				target.sendPackets(new S_OwnCharStatus(target));
				break;
			case 4:	
				String temp4= JOptionPane.showInputDialog("變更精神為多少");
				if (temp4 == null || temp4.equals("")) {
					return;
				}
				int second4 = Integer.valueOf(temp4);	
				target.addBaseWis((byte) -target.getBaseWis()+second4); // 素WIS值+1
				target.sendPackets(new S_OwnCharStatus(target));
				break;
			case 5:
				String temp5 = JOptionPane.showInputDialog("變更魅力為多少");
				if (temp5 == null || temp5.equals("")) {
					return;
				}
				int second5 = Integer.valueOf(temp5);	
				target.addBaseCha((byte) -target.getBaseCha()+second5); // 素WIS值+1
				target.sendPackets(new S_OwnCharStatus(target));
				break;
			case 6:
				String temp6 = JOptionPane.showInputDialog("設置GM權限多少");
				if (temp6 == null || temp6.equals("")) {
					return;
				}
				int second6 = Integer.valueOf(temp6);	
				target.setAccessLevel((short) second6);
				break;
			case 7:
				String temp7 = JOptionPane.showInputDialog("設置血魔加多少");
				if (temp7 == null || temp7.equals("")) {
					return;
				}
				int second7 = Integer.valueOf(temp7);	
				target.addBaseMaxHp((short)second7);
				target.addBaseMaxMp((short)second7);
				target.sendPackets(new S_OwnCharStatus(target));
				break;
			case 8:
				String temp8 = JOptionPane.showInputDialog("設置角色等級多少");
				if (temp8 == null || temp8.equals("")) {
					return;
				}
				int second8 = Integer.valueOf(temp8);	
				target.setExp(ExpTable.getExpByLevel(second8));
			}			
		} else if (command.equals("贈送物品(1)")) {
			String temp2 = "";
			String temp3 = "";
			try {
		     L1PcInstance target = World.get().getPlayer((String) DTM.getValueAt(select, 1));
				if (target == null ){
					JOptionPane.showMessageDialog(this, "此玩家不在線上無法贈送");
					return;
				}
				temp2 = JOptionPane.showInputDialog("請輸入物品ID");
				if (temp2 == null ){
					return;
				}
				int second2 = Integer.valueOf(temp2);
				final L1ItemInstance item = ItemTable.get().createItem(second2);
				if (item == null ){
					JOptionPane.showMessageDialog(this, "物品ID錯誤 請重新輸入");
					return;
				}	
				
				temp3 = JOptionPane.showInputDialog("你確定要贈送"+item.getName2()+"個");
				if (temp3 == null ){
					return;
				}
				int second3 = Integer.valueOf(temp3);	
				if ( (!item.isStackable () ) && (second3 > 5) ){
				 JOptionPane.showMessageDialog(this, "重疊的物品無法贈送5個以上");
				 return;
				}
			 	   //for (final Object obj : World.get().getVisibleObjects(4).values()) {
		     	      // 	if (obj instanceof L1DeInstance) {
		     	          //  final L1DeInstance pc2 = (L1DeInstance) obj;
		     	          // pc2.deleteMe();
		     	      // 	}
			 	  // }
				target.getInventory().storeItem(second2, second3);
				addConsol(target.getName()+"贈送"+item.getName2()+second3+"個 成功");
				target.sendPackets(new S_KillMessage("GM通過管理器送給你"+item.getName2()+" "+second3+"個"));
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "操作失敗!");
			}
			
		} else if (command.equals("封鎖IP(B)")) {
			try {
				L1PcInstance target = World.get().getPlayer((String) DTM.getValueAt(select, 1));
				final ClientExecutor targetClient = target.getNetConnection();
				final String ipaddr = targetClient.getAccountName().toString();
				final String ipaddr2 = targetClient.getIp().toString();
				IpReading.get().add(ipaddr2.toString(),"管理器" + "：IP");
				IpReading.get().add(ipaddr.toString(),"管理器" + "：帳號");
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(this, "操作失敗!");
			}
		} else if (command.equals("玩家資料(P)")) {
			setPlayerView((String) DTM.getValueAt(select, 1));
			F_Player.pack();
			F_Player.setVisible(true);
			// perform paste operation
		} else if (command.equals("密語(W)")) {
			TF_Target.setText((String) DTM.getValueAt(select, 1));
			CB_Channel.setSelectedIndex(1);

		} else if (command.equals("解封帳號 IP(A)")) { 
			String temp = "";
			try {
			temp = JOptionPane.showInputDialog("請輸入要解封的帳號或者IP");
			if (temp == null || temp.equals("")) {
				return;
			}
					IpReading.get().remove(temp);
					addConsol(temp+"解封成功");
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "操作失敗");
			}
		} else if (command.equals("啟動端口(S)")) {
			String temp = "";
			try {
		    temp = JOptionPane.showInputDialog("請輸入要啟動的端口");
		    int second = Integer.valueOf(temp);
			EchoServerTimer.get().startPort(second);
			//addConsol("端口 "+second+" 啟動成功");
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "輸入錯誤");
			}
		} else if (command.equals("關閉端口(C)")) {
			
			String temp = "";
			try {
		    temp = JOptionPane.showInputDialog("請輸入要關閉的端口");
			int second = Integer.valueOf(temp);
			EchoServerTimer.get().stopPort(second);
			
			//addConsol("端口 "+second+" 關閉成功");
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "輸入錯誤");
			}
		}
		//增加管理器倍數 by24918380
		else if (command.equals("正義倍率(W)")) {
			String temp = "";
			try {
				temp = JOptionPane.showInputDialog("當前服務正義倍率：" + ConfigRate.RATE_LA
						+ " 請輸入新倍率：");
				if (temp == null || temp.equals("")) {
					return;
				}
				int second = Integer.valueOf(temp);

				ConfigRate.RATE_LA = second;
				World.get().broadcastServerMessage(
						"正義倍率變更為：" + ConfigRate.RATE_LA + "倍。");
				addConsol(" 正義率變更為：" + ConfigRate.RATE_LA + "倍。");
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");
			}
		}
	 else if (command.equals("屬性強化倍率(X)")) {
		String temp = "";
		try {
			temp = JOptionPane.showInputDialog("當前服務屬性強化倍率："
					+ ConfigRate.ATTR_ENCHANT_CHANCE + " 請輸入新倍率：");
			if (temp == null || temp.equals("")) {
				return;
			}
			int second = Integer.valueOf(temp);

			ConfigRate.ATTR_ENCHANT_CHANCE = second;
			World.get().broadcastServerMessage(
					"屬性強化倍率變更為：" + ConfigRate.ATTR_ENCHANT_CHANCE + "%。");
			addConsol(" 屬性強化倍率變更為：" + ConfigRate.ATTR_ENCHANT_CHANCE + "%。");
		} catch (NumberFormatException e2) {
			JOptionPane.showMessageDialog(this, "請輸入整數!");
		}
		} else if (command.equals("防具強化倍率(S)")) {
			String temp = "";
			try {
				temp = JOptionPane.showInputDialog("當前服務防具強化倍率："
						+ ConfigRate.ENCHANT_CHANCE_ARMOR + " 請輸入新倍率：");
				if (temp == null || temp.equals("")) {
					return;
				}
				int second = Integer.valueOf(temp);

				ConfigRate.ENCHANT_CHANCE_ARMOR = second;
				World.get().broadcastServerMessage(
						"防具強化%變更為：" + ConfigRate.ENCHANT_CHANCE_ARMOR + "%。");
				addConsol(" 防具強化率變更為：" + ConfigRate.ENCHANT_CHANCE_ARMOR + "%。");
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");
			}
		} else if (command.equals("武器強化倍率(Q)")) {
			String temp = "";
			try {
				temp = JOptionPane.showInputDialog("當前服務武器強化倍率："
						+ ConfigRate.ENCHANT_CHANCE_WEAPON + " 請輸入新倍率：");
				if (temp == null || temp.equals("")) {
					return;
				}
				int second = Integer.valueOf(temp);

				ConfigRate.ENCHANT_CHANCE_WEAPON = second;
				World.get().broadcastServerMessage(
						"武器強化倍率變更為：" + ConfigRate.ENCHANT_CHANCE_WEAPON + "%。");
				addConsol(" 武器強化倍率變更為：" + ConfigRate.ENCHANT_CHANCE_WEAPON + "%。");
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");
			}
		} else if (command.equals("寵物經驗倍率(G)")) {
			String temp = "";
			try {
				temp = JOptionPane.showInputDialog("當前服務器寵物經驗倍率："
						+ ConfigRate.RATE_XP + " 請輸入新倍率：");
				if (temp == null || temp.equals("")) {
					return;
				}
				int second = Integer.valueOf(temp);

				ConfigRate.RATE_XP = second;
				World.get().broadcastServerMessage(
						"遊戲寵物經驗倍率變更為：" + ConfigRate.RATE_XP + "倍。");
				addConsol(" 遊戲寵物經驗倍率變更為：" + ConfigRate.RATE_XP + "倍。");
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");
			}
		}
		//增加管理器倍數 by24918380
		// 在線修改經驗/掉寶/掉錢倍率by cwt123
		else if (command.equals("經驗倍率(F)")) {
			String temp = "";
			try {
				temp = JOptionPane.showInputDialog("當前服務器經驗倍率："
						+ ConfigRate.RATE_XP + " 請輸入新倍率：");
				if (temp == null || temp.equals("")) {
					return;
				}
				int second = Integer.valueOf(temp);
				ConfigRate.RATE_XP = second;
				World.get().broadcastServerMessage(
						"遊戲經驗倍率變更為：" + ConfigRate.RATE_XP);
				addConsol(" 遊戲經驗倍率變更為：" + ConfigRate.RATE_XP);
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");
			}
		} else if (command.equals("掉錢倍率(M)")) {
			String temp = "";
			try {
				temp = JOptionPane.showInputDialog("當前服務器掉錢率："
						+ ConfigRate.RATE_DROP_ADENA + " 請輸入新倍率：");
				if (temp == null || temp.equals("")) {
					return;
				}
				int second = Integer.valueOf(temp);

				ConfigRate.RATE_DROP_ADENA = second;
				World.get().broadcastServerMessage(
						"掉錢倍率變更為：" + ConfigRate.RATE_DROP_ADENA);

				addConsol(" 掉錢倍率變更為：" + ConfigRate.RATE_DROP_ADENA);
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");
			}
		} else if (command.equals("掉寶率(D)")) {
			String temp = "";
			try {
				temp = JOptionPane.showInputDialog("當前服務器掉寶倍率："
						+ ConfigRate.RATE_DROP_ITEMS + " 請輸入新倍率：");
				if (temp == null || temp.equals("")) {
					return;
				}
				int second = Integer.valueOf(temp);

				ConfigRate.RATE_DROP_ITEMS = second;
				World.get().broadcastServerMessage(
						"掉寶率變更為：" + ConfigRate.RATE_DROP_ITEMS);
				addConsol(" 掉寶率變更為：" + ConfigRate.RATE_DROP_ITEMS);
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");
			}
		}
		// 在線修改經驗/掉寶/掉錢倍率by cwt123
		else if (command.equals("設定關閉伺服器(E)...")) {
			String temp = "";
			try {
				temp = JOptionPane.showInputDialog("請輸入幾秒重後重開!");
				if (temp == null || temp.equals("")) {
					return;
				}
				int second = Integer.valueOf(temp);
				
				if (second == 0) {
					closeServer();
				}
           //管理界面修改重啟設置秒數
				Shutdown.getInstance().startShutdown(null, second, true);
		   //管理界面修改重啟設置秒數
				//GameServer.getInstance().shutdownWithCountdown(second * 60);
			} catch (NumberFormatException e2) {
				JOptionPane.showMessageDialog(this, "請輸入整數!");
			}		
		} else if (command.equals("終極祝福(B)")) {
			L1PcInstance targetpc;
				int[] allBuffSkill = { 14, 26, 42, 48, 55, 68, 79, 88, 89, 90, 98, 102, 104, 105, 106, 111, 114, 117,
						129, 137, 147, 160, 163, 168, 169, 170, 171, 175, 176 };

				for (Iterator localIterator = World.get().getAllPlayers().iterator(); localIterator.hasNext();) {
					targetpc = (L1PcInstance) localIterator.next();
					L1BuffUtil.haste(targetpc, 3600000);
					L1BuffUtil.brave(targetpc, 3600000);

					for (int element : allBuffSkill) {
						if ((element == 26) || (element == 42)) {
							L1Skills skill = SkillsTable.get().getTemplate(element);
							new L1SkillUse().handleCommands(targetpc, element, targetpc.getId(), targetpc.getX(),
									targetpc.getY(), skill.getBuffDuration(), 4);
						} else {
							L1Skills skill = SkillsTable.get().getTemplate(element);
							new L1SkillUse().handleCommands(targetpc, element, targetpc.getId(), targetpc.getX(),
									targetpc.getY(), skill.getBuffDuration(), 4);
						}
					}
					targetpc.sendPackets(new S_ServerMessage(166, "祝福降臨人世,全體玩家得到祝福,GM是個大好人"));
				}
			} else if (command.equals("全體復活補血魔(U)")) {
				for (L1PcInstance tg : World.get().getAllPlayers())
					if ((tg.getCurrentHp() == 0) && (tg.isDead())) {
						tg.sendPackets(new S_SystemMessage("GM幫你復活嚕。"));
						tg.broadcastPacketX10(new S_SkillSound(tg.getId(), 3944));
						tg.sendPackets(new S_SkillSound(tg.getId(), 3944));
						tg.setTempID(tg.getId());
						tg.sendPackets(new S_Message_YN(322, ""));
					} else {
						tg.sendPackets(new S_SystemMessage("GM幫你治癒嚕。"));
						tg.broadcastPacketX10(new S_SkillSound(tg.getId(), 832));
						tg.sendPackets(new S_SkillSound(tg.getId(), 832));
						tg.setCurrentHp(tg.getMaxHp());
						tg.setCurrentMp(tg.getMaxMp());
					}			
		      } else if (command.equals("啟動城戰(R)")) {
			try {
				Object[] options ={ "肯特城", "妖魔城", "風木城", "奇岩城 ", "海音城", "侏儒城 ", "亞丁城"};   
				int m = JOptionPane.showOptionDialog(null, "請選擇城池", "是否開啟攻城戰",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
					   WarCastle(m+1);
			} catch (NumberFormatException e2) {

			}
		
	} else if (command.equals("關閉城戰(5)")) {
		try {
			Object[] options ={ "肯特城", "妖魔城", "風木城", "奇岩城 ", "海音城", "侏儒城 ", "亞丁城"};   
			int m = JOptionPane.showOptionDialog(null, "請選擇城池", "是否開關閉城戰",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]); 
		    WarCastlestop(m+1);

		} catch (NumberFormatException e2) {

		}
	}
	}

	private void processEvent(MouseEvent e) {
		if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
			int modifiers = e.getModifiers();
			modifiers -= MouseEvent.BUTTON3_MASK;
			modifiers |= MouseEvent.BUTTON1_MASK;
			MouseEvent ne = new MouseEvent(e.getComponent(), e.getID(), e
					.getWhen(), modifiers, e.getX(), e.getY(), e
					.getClickCount(), false);
			T_Player.dispatchEvent(ne);
		}
	}
	/**
	 * 取得物品資料
	 *
	 * @param pc
	 */
	public static void items(final L1PcInstance pc) {
		try {
			// 背包物品封包傳遞
			CharacterTable.restoreInventory(pc);
			final List<L1ItemInstance> items = pc.getInventory().getItems();
			if (items.size() > 0) {
				pc.sendPackets(new S_InvList(pc.getInventory().getItems()));
			
			}
			
		} catch (final Exception e) {

		}
	}
	/**
	 * 管理器控制城戰
	 *
	 * @param pc
	 */
	public void WarCastle(int a) {
		int castleId = 0;
		final Calendar calendar = PerformanceTimer.getRealTime();
		String name = null;
		switch(a){
		case 1:
			castleId = L1CastleLocation.KENT_CASTLE_ID;
			name = "肯特城";
			break;
		case 2:
			castleId = L1CastleLocation.OT_CASTLE_ID;
			name = "妖魔城";
			break;
		case 3:
			castleId = L1CastleLocation.WW_CASTLE_ID;
			name = "風木城";
			break;
		case 4:
			castleId = L1CastleLocation.GIRAN_CASTLE_ID;
			name = "奇岩城";
			break;
		case 5:
			castleId = L1CastleLocation.HEINE_CASTLE_ID;
			name = "海音城";
			break;
		case 6:
			castleId = L1CastleLocation.DOWA_CASTLE_ID;
			name = "侏儒城";
			break;
		case 7:
			castleId = L1CastleLocation.ADEN_CASTLE_ID;
			name = "亞丁城";
			break;
		default:
	
			break;
		}
		if (castleId != 0) {
	    addConsol(name+" 城戰開啟.");
		final L1Castle castle = CastleReading.get().getCastleTable(castleId);
		castle.setWarTime(calendar);
		ServerWarExecutor.get().setWarTime(castleId, calendar);
		ServerWarExecutor.get().setEndWarTime(castleId, calendar);
		}
	}
	
	/**
	 * 管理器控制城戰關閉
	 *
	 * @param pc
	 */
	public void WarCastlestop(int a) {
		int castleId = 0;
		final Calendar calendar = PerformanceTimer.getRealTime();
		String name = null;
		switch(a){
		case 1:
			castleId = L1CastleLocation.KENT_CASTLE_ID;
			name = "肯特城";
			calendar.add(Calendar.HOUR_OF_DAY, -4);// 減4小時
			break;
		case 2:
			castleId = L1CastleLocation.OT_CASTLE_ID;
			name = "妖魔城";
			calendar.add(Calendar.HOUR_OF_DAY, -4);// 減4小時
			break;
		case 3:
			castleId = L1CastleLocation.WW_CASTLE_ID;
			name = "風木城";
			calendar.add(Calendar.HOUR_OF_DAY, -4);// 減4小時
			break;
		case 4:
			castleId = L1CastleLocation.GIRAN_CASTLE_ID;
			name = "奇岩城";
			calendar.add(Calendar.HOUR_OF_DAY, -4);// 減4小時
			break;
		case 5:
			castleId = L1CastleLocation.HEINE_CASTLE_ID;
			name = "海音城";
			calendar.add(Calendar.HOUR_OF_DAY, -4);// 減4小時
			break;
		case 6:
			castleId = L1CastleLocation.DOWA_CASTLE_ID;
			name = "侏儒城";
			calendar.add(Calendar.HOUR_OF_DAY, -4);// 減4小時
			break;
		case 7:
			castleId = L1CastleLocation.ADEN_CASTLE_ID;
			name = "亞丁城";
			calendar.add(Calendar.HOUR_OF_DAY, -4);// 減4小時
			break;
		default:

			break;
		}
		if (castleId != 0) {
	    addConsol(name+" 城戰關閉.");
		final L1Castle castle = CastleReading.get().getCastleTable(castleId);
		castle.setWarTime(calendar);
		ServerWarExecutor.get().setWarTime(castleId, calendar);
		ServerWarExecutor.get().setEndWarTime(castleId, calendar);
		}
	}
	public static void saveChatData(boolean bool) {
		SimpleDateFormat sdfmt = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date d = Calendar.getInstance().getTime();
		String date = " " + sdfmt.format(d);
		try {
			// Consol
			FileOutputStream fos = new FileOutputStream("管理器/日記" + date
					+ ".txt");
			fos.write(TA_Consol.getText().getBytes());
			fos.close();
			// AllChat
			//fos = new FileOutputStream("服務器管理記憶/AllChat" + date + ".txt");
			//fos.write(TA_AllChat.getText().getBytes());
			//fos.close();
			// World
			//fos = new FileOutputStream("服務器管理記憶/World" + date + ".txt");
			//fos.write(TA_World.getText().getBytes());
			//fos.close();
			// Clan
			//fos = new FileOutputStream("服務器管理記憶/Clan" + date + ".txt");
			//fos.write(TA_Clan.getText().getBytes());
			//fos.close();
			// Normal
		//	fos = new FileOutputStream("服務器管理記憶/Normal" + date + ".txt");
			//fos.write(TA_Normal.getText().getBytes());
			//fos.close();
			// Team
			///fos = new FileOutputStream("服務器管理記憶/Team" + date + ".txt");
			//fos.write(TA_Team.getText().getBytes());
			//fos.close();
			// Whisper
			//fos = new FileOutputStream("服務器管理記憶/Whisper" + date + ".txt");
			//fos.write(TA_Private.getText().getBytes());
			//fos.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
