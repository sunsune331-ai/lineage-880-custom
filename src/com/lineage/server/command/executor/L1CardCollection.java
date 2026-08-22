package com.lineage.server.command.executor;

import com.lineage.server.datatables.CardCollectionTable;
import com.lineage.server.model.Instance.L1PcInstance;

/** GM 測試入口：.圖鑑 或 .圖鑑 11 */
public class L1CardCollection implements L1CommandExecutor {

	private L1CardCollection() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1CardCollection();
	}

	@Override
	public void execute(final L1PcInstance pc, final String cmdName, final String arg) {
		CardCollectionTable.get().syncInventory(pc);
		if ((arg == null) || (arg.trim().length() == 0)) {
			CardCollectionTable.get().openMenu(pc);
			return;
		}
		try {
			CardCollectionTable.get().open(pc, Integer.parseInt(arg.trim()));
		} catch (final NumberFormatException e) {
			CardCollectionTable.get().openMenu(pc);
		}
	}
}
