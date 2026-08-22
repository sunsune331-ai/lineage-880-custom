package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigBad {
	private static final Log _log = LogFactory.getLog(ConfigBad.class);

	public static final ArrayList<String> BAD_TEXT_LIST = new ArrayList<String>();
	private static final String _bad_text = "./data/badtext.txt";

	public static void load() throws ConfigErrorException {
		try {
			InputStream is = new FileInputStream(new File(_bad_text));
			// 指定檔案編碼
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			LineNumberReader lnr = new LineNumberReader(isr);

			boolean isWhile = false;
			String desc = null;
			while ((desc = lnr.readLine()) != null) {
				if (!isWhile) { // 忽略第一行
					isWhile = true;
				} else if ((desc.trim().length() != 0) && (!desc.startsWith("#"))) {
					if (!BAD_TEXT_LIST.contains(desc)) {
						BAD_TEXT_LIST.add(desc);
					}
				}
			}
			is.close();
			isr.close();
			lnr.close();
		} catch (Exception e) {
			_log.error("設置檔案遺失: " + _bad_text);
		}
	}
}
