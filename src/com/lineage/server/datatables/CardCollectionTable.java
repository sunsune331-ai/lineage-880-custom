package com.lineage.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Item;

/**
 * 變身卡收藏圖鑑。卡片來源採用「道具合成系統」資料表；首次取得卡片後永久點亮。
 */
public final class CardCollectionTable {

	private static final Log _log = LogFactory.getLog(CardCollectionTable.class);
	private static final int PAGE_SIZE = 9;
	private static final int DEFAULT_CARD_POLY_DURATION_SECONDS = 1800;
	private static CardCollectionTable _instance;

	private final Map<Integer, CardDefinition> _cards = new HashMap<Integer, CardDefinition>();
	/**
	 * 卡片變身對照由資料庫 card_polymorph_map 載入。
	 * 不可使用卡片的背包圖片編號推測變身外觀；每一筆均須人工確認。
	 */
	private final Map<Integer, PolymorphDefinition> _cardPolymorphs = new HashMap<Integer, PolymorphDefinition>();
	private final Map<Integer, ViewState> _views = new ConcurrentHashMap<Integer, ViewState>();

	private static final class CardDefinition {
		private final int itemId;
		private final String name;
		private final int level;

		private CardDefinition(final int itemId, final String name, final int level) {
			this.itemId = itemId;
			this.name = name;
			this.level = level;
		}
	}

	private static final class ViewState {
		private int level;
		private int page;
	}

	private static final class PolymorphDefinition {
		private final int polyId;
		private final int durationSeconds;

		private PolymorphDefinition(final int polyId, final int durationSeconds) {
			this.polyId = polyId;
			this.durationSeconds = durationSeconds;
		}
	}

	public static synchronized CardCollectionTable get() {
		if (_instance == null) {
			_instance = new CardCollectionTable();
		}
		return _instance;
	}

	private CardCollectionTable() {
		load();
	}

	public synchronized void load() {
		_cards.clear();
		_cardPolymorphs.clear();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			ps = con.prepareStatement("SELECT item_id, note, level FROM `道具合成系統` ORDER BY level, item_id");
			rs = ps.executeQuery();
			while (rs.next()) {
				final int itemId = rs.getInt("item_id");
				final L1Item item = ItemTable.get().getTemplate(itemId);
				if (item == null) {
					_log.warn("變身收藏略過未定義物品: " + itemId);
					continue;
				}
				_cards.put(itemId, new CardDefinition(itemId, rs.getString("note"), rs.getInt("level")));
			}
			_log.info("載入變身收藏卡片資料: " + _cards.size());
		} catch (final Exception e) {
			_log.error("載入變身收藏資料失敗", e);
		} finally {
			close(rs);
			close(ps);
			close(con);
		}
		loadPolymorphs();
	}

	/** 載入卡片物品與已確認變身外觀的對照。 */
	private void loadPolymorphs() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			ps = con.prepareStatement("SELECT item_id, poly_id, duration_seconds FROM card_polymorph_map WHERE enabled=1");
			rs = ps.executeQuery();
			while (rs.next()) {
				final int itemId = rs.getInt("item_id");
				final int polyId = rs.getInt("poly_id");
				final int durationValue = rs.getInt("duration_seconds");
				final int duration = durationValue > 0 ? durationValue : DEFAULT_CARD_POLY_DURATION_SECONDS;
				if ((ItemTable.get().getTemplate(itemId) == null) || (PolyTable.get().getTemplate(polyId) == null)) {
					_log.warn("變身卡片略過未定義對照: item=" + itemId + ", poly=" + polyId);
					continue;
				}
				_cardPolymorphs.put(Integer.valueOf(itemId), new PolymorphDefinition(polyId, duration));
			}
			_log.info("載入變身卡片外觀對照: " + _cardPolymorphs.size());
		} catch (final Exception e) {
			_log.error("載入變身卡片外觀對照失敗", e);
		} finally {
			close(rs);
			close(ps);
			close(con);
		}
	}

	/** 物品進入角色背包時呼叫；已有紀錄不會重複寫入。 */
	public void recordObtained(final L1PcInstance pc, final L1ItemInstance item) {
		if ((pc == null) || (item == null) || !_cards.containsKey(item.getItemId())) {
			return;
		}
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = DatabaseFactory.get().getConnection();
			ps = con.prepareStatement("INSERT IGNORE INTO card_collection (char_id, card_id, count, first_obtained_at) VALUES (?, ?, 1, NOW())");
			ps.setInt(1, pc.getId());
			ps.setInt(2, item.getItemId());
			ps.executeUpdate();
		} catch (final Exception e) {
			_log.error("記錄變身收藏失敗: " + pc.getName() + ", item=" + item.getItemId(), e);
		} finally {
			close(ps);
			close(con);
		}
	}

	/** 為既有背包卡片補建收藏紀錄。 */
	public void syncInventory(final L1PcInstance pc) {
		for (final L1ItemInstance item : pc.getInventory().getItems()) {
			recordObtained(pc, item);
		}
	}

	public boolean handleAction(final L1PcInstance pc, final String cmd) {
		if ("cc_open".equalsIgnoreCase(cmd)) {
			syncInventory(pc);
			openMenu(pc);
			return true;
		}
		if ("cc_home".equalsIgnoreCase(cmd)) {
			openMenu(pc);
			return true;
		}
		if ("C_1".equalsIgnoreCase(cmd)) {
			open(pc, 11);
			return true;
		}
		if ("C_2".equalsIgnoreCase(cmd)) {
			open(pc, 12);
			return true;
		}
		if ("C_3".equalsIgnoreCase(cmd)) {
			open(pc, 13);
			return true;
		}
		if ("C_4".equalsIgnoreCase(cmd)) {
			open(pc, 14);
			return true;
		}
		if ("C_5".equalsIgnoreCase(cmd)) {
			open(pc, 17);
			return true;
		}
		if ("C_6".equalsIgnoreCase(cmd)) {
			openReward(pc);
			return true;
		}
		if (cmd != null && cmd.startsWith("cc_level_")) {
			try {
				open(pc, Integer.parseInt(cmd.substring("cc_level_".length())));
				return true;
			} catch (final NumberFormatException e) {
				return true;
			}
		}
		if (cmd != null && cmd.startsWith("cc_card_")) {
			try {
				useCard(pc, Integer.parseInt(cmd.substring("cc_card_".length())));
				return true;
			} catch (final NumberFormatException e) {
				return true;
			}
		}
		if ("cc_prev".equalsIgnoreCase(cmd) || "cc_next".equalsIgnoreCase(cmd)) {
			ViewState state = _views.get(pc.getId());
			if (state == null) {
				state = new ViewState();
				state.level = firstLevel();
			}
			final int pages = pageCount(state.level);
			if ("cc_prev".equalsIgnoreCase(cmd) && state.page > 0) {
				state.page--;
			} else if ("cc_next".equalsIgnoreCase(cmd) && state.page + 1 < pages) {
				state.page++;
			}
			_views.put(pc.getId(), state);
			show(pc, state);
			return true;
		}
		return false;
	}

	/** 圖鑑頁面卡槽的點擊處理；僅已收藏且已人工確認外觀的卡片可變身。 */
	private void useCard(final L1PcInstance pc, final int slot) {
		if ((slot < 0) || (slot >= PAGE_SIZE)) {
			return;
		}
		final ViewState state = _views.get(pc.getId());
		if (state == null) {
			return;
		}
		final List<CardDefinition> cards = cardsForLevel(state.level);
		final int index = (state.page * PAGE_SIZE) + slot;
		if (index >= cards.size()) {
			return;
		}
		final CardDefinition card = cards.get(index);
		final boolean[] owned = getOwned(pc, Collections.singletonList(card));
		if ((owned.length == 0) || !owned[0]) {
			return;
		}
		final PolymorphDefinition polymorph = _cardPolymorphs.get(Integer.valueOf(card.itemId));
		if (polymorph == null) {
			return;
		}
		L1PolyMorph.doPoly(pc, polymorph.polyId, polymorph.durationSeconds,
				L1PolyMorph.MORPH_BY_ITEMMAGIC);
	}

	public void open(final L1PcInstance pc, final int level) {
		final ViewState state = new ViewState();
		state.level = ((level == 17) || hasLevel(level)) ? level : firstLevel();
		state.page = 0;
		_views.put(pc.getId(), state);
		show(pc, state);
	}

	/** 圖鑑獎勵入口；獎勵規則日後可直接擴充此頁。 */
	public void openReward(final L1PcInstance pc) {
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "card_collection_reward", new String[0]));
	}

	/** 顯示六階主選單。使用純文字連結，避免 8.5C 專屬按鈕圖形在 8.8C 顯示錯誤。 */
	public void openMenu(final L1PcInstance pc) {
		syncInventory(pc);
		final String[] args = { currentPolymorphName(pc) };
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "card_collection_menu", args));
	}

	/** 依角色當前外觀圖號找出變身名稱；未變身時使用明確文字。 */
	private String currentPolymorphName(final L1PcInstance pc) {
		if (pc.getTempCharGfx() == pc.getClassId()) {
			return "未變身";
		}
		// 圖鑑卡片已設定的外觀，優先回傳卡片的中文名稱。
		// polymorphs 資料表常使用 lv15 werewolf 等內部英文名稱，不適合直接顯示給玩家。
		for (final Map.Entry<Integer, PolymorphDefinition> entry : _cardPolymorphs.entrySet()) {
			if (entry.getValue().polyId == pc.getTempCharGfx()) {
				final CardDefinition card = _cards.get(entry.getKey());
				if ((card != null) && (card.name != null) && (card.name.trim().length() > 0)) {
					return card.name.replace("變身卡：", "").trim();
				}
			}
		}
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			ps = con.prepareStatement("SELECT name FROM polymorphs WHERE polyid=? LIMIT 1");
			ps.setInt(1, pc.getTempCharGfx());
			rs = ps.executeQuery();
			if (rs.next()) {
				final String name = rs.getString("name");
				if ((name != null) && (name.trim().length() > 0)) {
					return name.trim();
				}
			}
		} catch (final Exception e) {
			_log.warn("讀取目前變身名稱失敗: " + pc.getName(), e);
		} finally {
			close(rs);
			close(ps);
			close(con);
		}
		return "特殊變身";
	}

	private void show(final L1PcInstance pc, final ViewState state) {
		final List<CardDefinition> cards = cardsForLevel(state.level);
		final int pages = Math.max(1, (cards.size() + PAGE_SIZE - 1) / PAGE_SIZE);
		if (state.page >= pages) {
			state.page = pages - 1;
		}
		final boolean[] owned = getOwned(pc, cards);
		int ownedCount = 0;
		for (final boolean value : owned) {
			if (value) {
				ownedCount++;
			}
		}
		final String[] args = new String[14];
		args[0] = levelName(state.level);
		args[1] = String.valueOf(state.page + 1);
		args[2] = String.valueOf(pages);
		args[3] = String.valueOf(ownedCount);
		args[4] = String.valueOf(cards.size());
		final int start = state.page * PAGE_SIZE;
		for (int i = 0; i < PAGE_SIZE; i++) {
			final int index = start + i;
			if (index < cards.size()) {
				final CardDefinition card = cards.get(index);
				args[5 + i] = owned[index] ? "[*] " + displayName(card) : "[ ] " + levelName(state.level) + "變身卡片:未收藏";
			} else {
				args[5 + i] = "";
			}
		}
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), pageHtmlId(state), args));
	}

	/** 一般、高級、稀有使用各頁固定 3×3 圖片版面；其餘階級暫用文字版。 */
	private String pageHtmlId(final ViewState state) {
		if ((state.level >= 11) && (state.level <= 13)) {
			return "card_collection_" + state.level + "_" + (state.page + 1);
		}
		return "card_collection";
	}

	private boolean[] getOwned(final L1PcInstance pc, final List<CardDefinition> cards) {
		final boolean[] result = new boolean[cards.size()];
		if (cards.isEmpty()) {
			return result;
		}
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = DatabaseFactory.get().getConnection();
			ps = con.prepareStatement("SELECT card_id FROM card_collection WHERE char_id=? AND count>0");
			ps.setInt(1, pc.getId());
			rs = ps.executeQuery();
			final Map<Integer, Boolean> ids = new HashMap<Integer, Boolean>();
			while (rs.next()) {
				ids.put(rs.getInt("card_id"), Boolean.TRUE);
			}
			for (int i = 0; i < cards.size(); i++) {
				result[i] = ids.containsKey(cards.get(i).itemId);
			}
		} catch (final Exception e) {
			_log.error("讀取變身收藏失敗: " + pc.getName(), e);
		} finally {
			close(rs);
			close(ps);
			close(con);
		}
		return result;
	}

	private List<CardDefinition> cardsForLevel(final int level) {
		final List<CardDefinition> result = new ArrayList<CardDefinition>();
		for (final CardDefinition card : _cards.values()) {
			if ((card.level == level) || ((level == 17) && ((card.level == 15) || (card.level == 16)))) {
				result.add(card);
			}
		}
		Collections.sort(result, new Comparator<CardDefinition>() {
			@Override
			public int compare(final CardDefinition left, final CardDefinition right) {
				return left.itemId - right.itemId;
			}
		});
		return result;
	}

	private boolean hasLevel(final int level) {
		for (final CardDefinition card : _cards.values()) {
			if (card.level == level) {
				return true;
			}
		}
		return false;
	}

	private int firstLevel() {
		int result = Integer.MAX_VALUE;
		for (final CardDefinition card : _cards.values()) {
			result = Math.min(result, card.level);
		}
		return result == Integer.MAX_VALUE ? 11 : result;
	}

	private String levelName(final int level) {
		switch (level) {
		case 11:
			return "一般";
		case 12:
			return "高級";
		case 13:
			return "稀有";
		case 14:
			return "英雄";
		case 15:
			return "傳說";
		case 16:
			return "神話";
		case 17:
			return "傳說／神話";
		default:
			return String.valueOf(level);
		}
	}

	/**
	 * 圖鑑一律使用統一的階級前綴，並移除資料庫舊資料中的 \f 色碼。
	 */
	private String displayName(final CardDefinition card) {
		String name = card.name == null ? "" : card.name;
		name = name.replaceAll("\\\\f.", "");
		name = name.replace("變身卡：", "").replace("變身卡:", "").trim();
		return levelName(card.level) + "變身:" + name;
	}

	private int pageCount(final int level) {
		return Math.max(1, (cardsForLevel(level).size() + PAGE_SIZE - 1) / PAGE_SIZE);
	}

	private static void close(final AutoCloseable closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.close();
		} catch (final Exception e) {
			// ignore close failure
		}
	}
}
