package com.lineage.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OutErrorMsg {
	private static final Log _log = LogFactory.getLog(OutErrorMsg.class);

	public static void put(String className, String string, Throwable t) {
		_log.error(string);
		StringBuilder putInfo = new StringBuilder();
		putInfo.append(string + "###");
		StackTraceElement[] locations = t.getStackTrace();

		for (StackTraceElement stackTraceElement : locations) {
			putInfo.append("   " + stackTraceElement.toString() + "###");
		}
		overOut(className, putInfo);
	}

	public static void put(int oid, String string) {
		_log.error(string);
		StringBuilder putInfo = new StringBuilder();
		String nowDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		putInfo.append(string + "/" + nowDate);

		overOut(String.valueOf(oid), putInfo);
	}

	private static void overOut(String name, StringBuilder string) {
		try {
			File file = new File("./" + name + ".txt");
			file.createNewFile();

			FileOutputStream outStream = new FileOutputStream(file, true);

			OutputStreamWriter printWriter = new OutputStreamWriter(outStream, "utf-8");

			String[] clientStrAry = string.toString().split("###");

			for (String txt : clientStrAry) {
				printWriter.write(txt);
				printWriter.write("\r\n");
			}
			printWriter.write("\r\n");
			printWriter.flush();
			printWriter.close();
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.OutErrorMsg JD-Core Version: 0.6.2
 */