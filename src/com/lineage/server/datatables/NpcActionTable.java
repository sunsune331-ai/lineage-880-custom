package com.lineage.server.datatables;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.npc.action.L1NpcAction;
import com.lineage.server.model.npc.action.L1NpcXmlParser;
import com.lineage.server.utils.FileUtil;
import com.lineage.server.utils.PerformanceTimer;

public class NpcActionTable {
	private static final Log _log = LogFactory.getLog(LightSpawnTable.class);
	private static NpcActionTable _instance;
	private static final List<L1NpcAction> _actions = new ArrayList<L1NpcAction>();

	private static final List<L1NpcAction> _talkActions = new ArrayList<L1NpcAction>();

	private List<L1NpcAction> loadAction(File file, String nodeName)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(file);

		if (!doc.getDocumentElement().getNodeName().equalsIgnoreCase(nodeName)) {
			return new ArrayList<L1NpcAction>();
		}
		return L1NpcXmlParser.listActions(doc.getDocumentElement());
	}

	private void loadAction(File file) throws Exception {
		_actions.addAll(loadAction(file, "NpcActionList"));
	}

	private void loadTalkAction(File file) throws Exception {
		_talkActions.addAll(loadAction(file, "NpcTalkActionList"));
	}

	private void loadDirectoryActions(File dir) throws Exception {
		for (String file : dir.list()) {
			File f = new File(dir, file);
			if (FileUtil.getExtension(f).equalsIgnoreCase("xml")) {
				loadAction(f);
				loadTalkAction(f);
			}
		}
	}

	private NpcActionTable() throws Exception {
		loadDirectoryActions(new File("./data/xml/NpcActions/"));
	}

	public static void load() {
		try {
			PerformanceTimer timer = new PerformanceTimer();
			_instance = new NpcActionTable();
			_log.info("載入NPC XML對話結果資料 (" + timer.get() + "ms)");
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
			System.exit(0);
		}
	}

	public static NpcActionTable getInstance() {
		return _instance;
	}

	public L1NpcAction get(String actionName, L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _actions) {
			if (action.acceptsRequest(actionName, pc, obj)) {
				return action;
			}
		}
		return null;
	}

	public L1NpcAction get(L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _talkActions) {
			if (action.acceptsRequest("", pc, obj)) {
				return action;
			}
		}
		return null;
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.datatables.NpcActionTable JD-Core Version: 0.6.2
 */