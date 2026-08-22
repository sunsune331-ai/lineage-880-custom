package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigMobKill//src014
{
  private static final Log _log = LogFactory.getLog(ConfigMobKill.class);
  
  public static final Map<Integer, String> MOBKILL_TEXT_LIST = new HashMap<Integer, String>();
  private static final String _mobkill_text = "./config/mobkill_desc.txt";
  
  public static void load() throws ConfigErrorException
  {
    try
    {
      InputStream is = new FileInputStream(new File(_mobkill_text));
      
      InputStreamReader isr = new InputStreamReader(is, "utf-8");
      LineNumberReader lnr = new LineNumberReader(isr);
      
      int i = 0;
      String desc = null;
      boolean isWhile = false;
      while ((desc = lnr.readLine()) != null) {
        if (!isWhile) {
          isWhile = true;
        }
        else if ((!desc.trim().isEmpty()) && (!desc.startsWith("#")))
        {
          MOBKILL_TEXT_LIST.put(new Integer(++i), desc); }
      }
      is.close();
      isr.close();
      lnr.close();
    } catch (Exception e) {
      _log.error("設置檔案遺失: " + _mobkill_text);
    }
  }
}
