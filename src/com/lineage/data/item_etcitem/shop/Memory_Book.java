package com.lineage.data.item_etcitem.shop;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.datatables.MemoryBookTable;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1MemoryBook;

/**
 * 記憶書<br>
 * 
 * shop.Memory_Book _type _removetype<br><br>
 * 
 * 道具nameid=$5839(對話檔telbook0.tbl)<br>
 * 道具nameid=$6415(對話檔telbook1.tbl)<br>
 * 道具nameid=$8450(對話檔telbook2.tbl)<br>
 * 道具nameid=$15994(對話檔telbook2.tbl)<br>
 */
public class Memory_Book extends ItemExecutor {

	/**
	 *
	 */
	private Memory_Book() {
		// TODO Auto-generated constructor stub
	}

	public static ItemExecutor get() {
		return new Memory_Book();
	}

	/**
	 * 道具物件執行
	 * @param data 參數
	 * @param pc 執行者
	 * @param item 物件
	 */
	@Override
	public void execute(final int[] data, final L1PcInstance pc, final L1ItemInstance item) {

		final int bookid = data[0];
		final int chargeCount = item.getChargeCount();

		for (final L1MemoryBook book : MemoryBookTable.get().getAllHuntMemoryBook().values()) {
			if (book != null) {
				if (book.getType() == _type && bookid == book.getBookId()) { // 自行對照telbook.tbl檔案加入對應座標
					if (!pc.getMap().isEscapable()) {
						// 276 \f1在此無法使用傳送。
						pc.sendPackets(new S_ServerMessage(276));
						// 解除傳送鎖定
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
						return;
					}
					if (book.getLocx() == 0 || book.getLocy() == 0/* || book.getMapid() == 0*/) {
						// pc.sendPackets(new S_ServerMessage("此為說明，不能傳送！"));
						return;
					}
					L1Teleport.teleport(pc, book.getLocx(), book.getLocy(), (short) book.getMapid(), pc.getHeading(), true);
				}
			}
		}

		if (_removetype == 0) { // 永久使用

		} else if (_removetype == 1) { // 判斷次數,次數小於等於1再刪除道具
			item.setChargeCount(item.getChargeCount() - 1);
			pc.getInventory().updateItem(item, L1PcInventory.COL_REMAINING_TIME);
			if (chargeCount <= 1) {
				pc.getInventory().removeItem(item, 1);
			}

		} else if (_removetype == 2) { // 使用1次刪除1個道具
			pc.getInventory().removeItem(item, 1);
		}
	}

	private int _type = 0;
	private int _removetype = 0;

	@Override
	public void set_set(final String[] set) {
		try {
			_type = Integer.parseInt(set[1]);
		} catch (final Exception e) {
		}
		try {
			_removetype = Integer.parseInt(set[2]);
		} catch (final Exception e) {
		}
	}
}
