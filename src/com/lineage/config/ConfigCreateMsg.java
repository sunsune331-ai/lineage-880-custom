package com.lineage.config;

import com.lineage.server.serverpackets.S_BoxMessage;
import com.lineage.server.world.World;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigCreateMsg {
	private static final Log _log = LogFactory.getLog(ConfigCreateMsg.class);
	private static final Map<Integer, String> _create_msg_list = new HashMap<Integer, String>();
	private static final Random _random = new Random();
	public static boolean ISMSG = false;
	private static final String _create_text = "./config/create_desc.txt";

	public static void load() throws ConfigErrorException {
		try {
			InputStream is = new FileInputStream(new File(_create_text));

			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			LineNumberReader lnr = new LineNumberReader(isr);

			boolean isWhile = false;
			int i = 1;
			String desc = null;
			while ((desc = lnr.readLine()) != null) {
				if (!isWhile) {
					isWhile = true;
				} else if ((desc.trim().length() != 0)
						&& (!desc.startsWith("#"))) {
					if (desc.startsWith("ISMSG")) {
						desc = desc.replaceAll(" ", "");
						ISMSG = Boolean.parseBoolean(desc.substring(6));
					} else {
						_create_msg_list.put(new Integer(i++), desc);
					}
				}
			}
			is.close();
			isr.close();
			lnr.close();
		} catch (Exception e) {
			_log.error("設置檔案遺失: " + _create_text);
		}
	}

	public static void msg(String string1) {
		try {
			String msg = (String) _create_msg_list.get(Integer.valueOf(_random
					.nextInt(_create_msg_list.size()) + 1));
			if (msg != null) {
				String out = String.format(msg, new Object[] { string1 });

				World.get().broadcastPacketToAll(new S_BoxMessage(out));
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}
}
