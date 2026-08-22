package com.lineage.server.serverpackets;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.lineage.server.model.Instance.L1PcInstance;

/**
 * 角色座標名單
 * 
 * @author dexc
 *
 */
public class S_BookMarkLoad extends ServerBasePacket {

    private static final String S_BookMarkLoad = "[S] S_BookmarkLoad";

    private byte[] _byte = null;

    private static Logger _log = Logger.getLogger(S_BookMarkLoad.class.getName());

    public S_BookMarkLoad(L1PcInstance pc) {
        try {
            int size = pc._bookmarks.size();
			writeC(S_VOICE_CHAT);
			writeC(0x2a);
			writeH(0x0080);
            writeC(0x02);
			for (int i = 1; i < 128; i++) {
				writeC(0xff);
			}
			writeH(60);
			writeH(size);
			for (int i = 0; i < size; i++) {
				writeD(pc._bookmarks.get(i).getId());
                writeS(pc._bookmarks.get(i).getName());
                writeH(pc._bookmarks.get(i).getMapId());
                writeH(pc._bookmarks.get(i).getLocX());
                writeH(pc._bookmarks.get(i).getLocY());
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, "S_BookMarkLoad發生例外。", e);
        } finally {
        }
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
            _byte = getBytes();
        }
        return _byte;
    }

    @Override
    public String getType() {
        return S_BookMarkLoad;
    }

}
