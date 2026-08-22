package com.lineage.data.item_etcitem;

import com.lineage.config.Config;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.Controller.UserRankingController;
import com.lineage.server.datatables.PolyTable;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.event.ranking.RankingHeroTimer;

public class Sosc_PolyReel extends ItemExecutor {
	public static ItemExecutor get() {
		return new Sosc_PolyReel();
	}

	public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
		String text = pc.getText();

		if (text == null) {
			return;
		}

		pc.setText(null);

		int time = 1800;

		if (item.getBless() == 0) {
			time = 2100;
		}

		if (item.getBless() == 128) {
			time = 2100;
		}

		if (item.getItemId() == 42029) {
			time = 5150;
		}

		int awakeSkillId = pc.getAwakeSkillId();
		if ((awakeSkillId == 185) || (awakeSkillId == 190)
				|| (awakeSkillId == 195)) {
			pc.sendPackets(new S_ServerMessage(1384));
			return;
		}

		// 新排行系統
		if (Config.UserRanking) {
			if (text.startsWith("rangking ")) {
				int star = UserRankingController.getInstance().getStarCount(
						pc.getName());
				if (star != 9 && star != 10 && star != 11) {
					return;
				}				
			 if ((!RankingHeroTimer.get_top10().containsValue(
						pc.getName())
						&& !RankingHeroTimer.get_top3C().containsValue(
								pc.getName())
						&& !RankingHeroTimer.get_top3K().containsValue(
								pc.getName())
						&& !RankingHeroTimer.get_top3E().containsValue(
								pc.getName())
						&& !RankingHeroTimer.get_top3W().containsValue(
								pc.getName())
						&& !RankingHeroTimer.get_top3D().containsValue(
								pc.getName())
						&& !RankingHeroTimer.get_top3G().containsValue(
								pc.getName())
						&& !RankingHeroTimer.get_top3I().containsValue(
								pc.getName())
						&& !RankingHeroTimer.get_top3Warrior().containsValue(
								pc.getName()))
						&& (!pc.isGm())) {// 不在排行榜內
				 return;
			    }	
			 }
			if (text.equalsIgnoreCase("ranking class polymorph")) {
				if (pc.isCrown()) {
					if (pc.get_sex() == 0) {
						text = "rangking prince male";
					} else {
						text = "rangking prince female";
					}
				} else if (pc.isKnight()) {
					if (pc.get_sex() == 0) {
						text = "rangking knight male";
					} else {
						text = "rangking knight female";
					}
				} else if (pc.isElf()) {
					if (pc.get_sex() == 0) {
						text = "rangking elf male";
					} else {
						text = "rangking elf female";
					}
				} else if (pc.isWizard()) {
					if (pc.get_sex() == 0) {
						text = "rangking wizard male";
					} else {
						text = "rangking wizard female";
					}
				} else if (pc.isDarkelf()) {
					if (pc.get_sex() == 0) {
						text = "rangking darkelf male";
					} else {
						text = "rangking darkelf female";
					}
				} else if (pc.isDragonKnight()) {
					if (pc.get_sex() == 0) {
						text = "rangking dragonknight male";
					} else {
						text = "rangking dragonknight female";
					}
				} else if (pc.isIllusionist()) {
					if (pc.get_sex() == 0) {
						text = "rangking illusionist male";
					} else {
						text = "rangking illusionist female";
					}
				} else if (pc.isWarrior()) {
					if (pc.get_sex() == 0) {
						text = "rangking warrior male";
					} else {
						text = "rangking warrior female";
					}
				}
			}
		}
		// 新排行系統end

		L1PolyMorph poly = PolyTable.get().getTemplate(text);

		if ((poly != null) || (text.equals(""))) {
			if (text.equals("")) {
				L1PolyMorph.undoPoly(pc);
				// System.out.println("變形卷軸取消變身");
				pc.getInventory().removeItem(item, 1L);
			} else if ((poly.getMinLevel() <= pc.getLevel()) || (pc.isGm())) {
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, 1);
				pc.getInventory().removeItem(item, 1L);
			}

		} else {
			pc.sendPackets(new S_ServerMessage(181));
		}
	}
}
