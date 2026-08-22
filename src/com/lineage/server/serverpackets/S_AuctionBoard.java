package com.lineage.server.serverpackets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1AuctionBoardTmp;

/**
 * 盟屋拍賣公告欄列表
 * 
 * @author dexc
 *
 */
public class S_AuctionBoard extends ServerBasePacket {

	private byte[] _byte = null;

	public S_AuctionBoard(final L1NpcInstance board) {
		this.buildPacket(board);
	}

	private void buildPacket(final L1NpcInstance board) {
		final ArrayList<L1AuctionBoardTmp> houseListX = new ArrayList<L1AuctionBoardTmp>();

		final Collection<L1AuctionBoardTmp> boardList = AuctionBoardReading.get().getAuctionBoardTableList().values();
		for (final L1AuctionBoardTmp boardX : boardList) {
			final int houseId = boardX.getHouseId();
			if ((board.getX() == 33424) && (board.getY() == 32824)) { // 奇岩
				if ((houseId >= 262145) && (houseId <= 262189)) {
					houseListX.add(boardX);
				}
			} else if ((board.getX() == 33585) && (board.getY() == 33235)) { // 海音
				if ((houseId >= 327681) && (houseId <= 327691)) {
					houseListX.add(boardX);
				}
			} else if ((board.getX() == 33959) && (board.getY() == 33253)) { // 亞丁
				if ((houseId >= 458753) && (houseId <= 458819)) {
					houseListX.add(boardX);
				}
			} else if ((board.getX() == 32611) && (board.getY() == 32775)) { // 古魯丁
				//if ((houseId >= 524289) && (houseId <= 524294)) {
				if (houseId >= 65537 && houseId <= 65542) { // 古魯丁血盟小屋1~6
					houseListX.add(boardX);
				}
			}
		}
		this.writeC(S_OPCODE_HOUSELIST);
		// this.writeC(0x00); // ?
		this.writeD(board.getId());
		this.writeH(houseListX.size()); // 數
		for (final L1AuctionBoardTmp boardX : houseListX) {
			this.writeD(boardX.getHouseId()); // 小屋編號
			this.writeS(boardX.getHouseName()); // 小屋名稱
			this.writeH(boardX.getHouseArea()); // 小屋大小
			final Calendar cal = boardX.getDeadline();
			this.writeC(cal.get(Calendar.MONTH) + 1); // 締切月
			this.writeC(cal.get(Calendar.DATE)); // 締切日
			this.writeD((int) boardX.getPrice()); // 現在入札價格
		}
	}

	@Override
	public byte[] getContent() {
		if (this._byte == null) {
			this._byte = this.getBytes();
		}
		return this._byte;
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}
}
