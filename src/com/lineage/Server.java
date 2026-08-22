package com.lineage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogManager;
import java.net.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.eric.gui.J_Main;
import com.lineage.commons.system.LanSecurityManager;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigBad;
import com.lineage.config.ConfigBoxMsg;
import com.lineage.config.ConfigCharSetting;
import com.lineage.config.ConfigDescs;
import com.lineage.config.ConfigGiveVip;
import com.lineage.config.ConfigIpCheck;
import com.lineage.config.ConfigKill;
import com.lineage.config.ConfigMobKill;
import com.lineage.config.ConfigNew;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigQuest;
import com.lineage.config.ConfigRate;
import com.lineage.config.ConfigRecord;
import com.lineage.config.ConfigSQL;
import com.lineage.config.ConfigSkill;
import com.lineage.server.GameServer;
import com.lineage.server.utils.DBClearAllUtil;

public class Server { 
	private static final String _log_prop = "./config/logging.properties";
	private static final String _log_4j = "./config/log4j.properties";
	private static final String _loginfo = "./loginfo";
	private static final String _back = "./back";
	private static final String _licence = "Copyright (C) 2008-2016 by 7.0\n";
	// private static final String _jseverip = "220.134.189.191";//src023
	// private static final String _jsevermac = "E0-3F-49-A4-CF-48"; //src023

	public static void main(String[] args) throws Exception {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));// 轉成16進位
			}
			// System.out.println("你的實體ip:"+ip.getHostAddress());
			// System.out.println("你的實體mac:");
			// System.out.println(sb.toString());
			/*if ((!ip.getHostAddress().equals(_jseverip))||(!sb.toString().equals(_jsevermac))) {
				// 沒有任何事情發生
				System.out.println("\n\r       版權擁有，您未授權使用。");
				Thread.sleep(1 * 1000);// 延遲
				System.exit(0);
			}*/
		} catch (Exception localException1) {
		}

		CompressFile bean = new CompressFile();
		File readfile;
		try {
			File file = new File(_back);
			if (!file.exists()) {
				file.mkdir();
			}

			String nowDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
			bean.zip(_loginfo, "./back/" + nowDate + ".zip");

			File loginfofile = new File(_loginfo);
			String[] loginfofileList = loginfofile.list();
			for (String fileName : loginfofileList) {
				readfile = new File("./loginfo/" + fileName);
				if ((readfile.exists()) && (!readfile.isDirectory()))
					readfile.delete();
			}
		} catch (IOException e) {
			System.out.println("資料夾不存在: ./back 已經自動建立!");
		}

		boolean error = false;
		try {
			InputStream is = new BufferedInputStream(new FileInputStream(_log_prop));
			LogManager.getLogManager().readConfiguration(is);
			is.close();
		} catch (IOException e) {
			System.out.println("檔案遺失: ./config/logging.properties");
			error = true;
		}

		try {
			PropertyConfigurator.configure(_log_4j);
		} catch (Exception e) {
			System.out.println("檔案遺失: ./config/log4j.properties");
			System.exit(0);
		}
		try {
			Config.load();
			ConfigAlt.load();
			ConfigCharSetting.load();
			
			
			
			ConfigRate.load();
			ConfigSQL.load();
			ConfigRecord.load();
			// ConfigRevision.load(null);// 授權開通 //src004
			ConfigDescs.load();
			ConfigBad.load();
			ConfigKill.load();
			ConfigIpCheck.load();
			ConfigBoxMsg.load();
			ConfigMobKill.load();// src014
			
			ConfigQuest.load();// src035
			ConfigGiveVip.load();// 潘朵拉商城消費給予VIP狀態
			ConfigOther.load();
			//ConfigSkill.load();
		} catch (Exception e) {
			// System.out.println("CONFIG 資料加載異常!" + e);
			error = true;
		}

		System.out.println("Copyright (C) 2008-2026 \n\n");
		Log log = LogFactory.getLog(Server.class);

		String infoX = "\n\r##################################################\n\r       服務器 (核心版本:L1AtuTw_8.8C/Lineage880)\n\r##################################################";

		log.info(infoX);

		// File file = new File("./jar");
		// String[] fileNameList = file.list();
		// for (String fileName : fileNameList) {
		// File readfile1 = new File(fileName);
		// if (!readfile1.isDirectory()) {
		// log.info("加載引用JAR: " + fileName);
		// }
		// }

		if (error) {
			System.exit(0);
		}

		// SQL讀取初始化
		DatabaseFactoryLogin.setDatabaseSettings();
		DatabaseFactory.setDatabaseSettings();

		DatabaseFactoryLogin.get();
		DatabaseFactory.get();

		ConfigNew.load();

		if (Config.GUI) {
			J_Main.getInstance().setVisible(true);
		}

		if (ConfigNew.DBClearAll) {// 是否開啟絕對還原設定(開新服專用)
			DBClearAllUtil.start();
		}

		// 安全管理器
		LanSecurityManager securityManager = new LanSecurityManager();
		System.setSecurityManager(securityManager);
		log.info("加載 安全管理器: LanSecurityManager");

		String osname = System.getProperties().getProperty("os.name");
		if (osname.lastIndexOf("Linux") != -1) {
			Config.ISUBUNTU = true;
		}

		GameServer.getInstance().initialize();
	}
}
