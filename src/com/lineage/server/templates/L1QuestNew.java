package com.lineage.server.templates;

import com.lineage.server.datatables.QuestNewSetTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ProtoBuffers;

/**
 * 官服任務系統
 */
public class L1QuestNew {

	// --------------- 設定值 ---------------
	private int id = 0;

	private int minQuestLevel = 1;

	private int maxQuestLevel = 55;

	private int mapid = 3; // 7783

	private int[] teleportLoc = new int[0];

	private int[] rewardItemid = new int[0];

	private int[] rewardItemCount = new int[0];

	private int[] rewardItemEnchant = new int[0];

	private int[] rewardSelectItemid = new int[0];

	private int[] rewardSelectItemCount = new int[0];

	private int[] rewardSelectItemEnchant = new int[0];

	private int rewardExp = 0;

	private String requireClassType = "A";

	private int 達到等級 = 0; // 達到等級

	private boolean isRecoverRequireItem = false; // 是否回收任務道具

	// 獵殺怪物
	private int[] 獵殺怪物編號 = new int[0];

	private int[] 獵殺怪物數量 = new int[0];

	// 獲得道具
	private int[] 獲得道具編號 = new int[0];

	private int[] 獲得道具數量 = new int[0];

	private int[] 獲得道具加成 = new int[0];

	// 使用道具
	private int[] 使用道具編號 = new int[0];

	private int[] 使用道具數量 = new int[0];

	// -------------- 變數 ---------------
	private boolean isQuestEnd = false;

	private boolean isQuestComplete = false; // 完成不等於結束 領完東西才算?

	private L1PcInstance owner = null;

	private int 目前等級 = 1;

	private int[] 目前獵殺怪物數量 = new int[0];

	private int[] 目前獲得道具數量 = new int[0];

	private int[] 目前使用道具數量 = new int[0];
	// -----------

	public void updateCurrentLevel(final int level) {
		if (!isQuestComplete) {
			目前等級 = (level > 達到等級) ? 達到等級 : level;
			checkRequire();
		}
	}

	// private boolean requireClan = false;

	public void addCurrentNpcCount(final int idx) {
		if (!isQuestComplete) {

			if (目前獵殺怪物數量[idx] < 獵殺怪物數量[idx]) {
				目前獵殺怪物數量[idx] += 1;
				checkRequire();
			}

		}
	}

	public void updateCurrentItemCount(final int idx, final int count) {
		if (!isQuestComplete) {

			目前獲得道具數量[idx] = (count > 獲得道具數量[idx]) ? 獲得道具數量[idx] : count;
			checkRequire();

		}
	}

	public void addCurrentUseItemCount(final int idx) {
		if (!isQuestComplete) {
			if (目前使用道具數量[idx] < 使用道具數量[idx]) {
				目前使用道具數量[idx] += 1;
				checkRequire();
			}

		}
	}

	private void checkRequire() {

		if (owner != null) {
			owner.sendPackets(new S_ProtoBuffers(S_ProtoBuffers.QUEST_UPDATE, this));
		}

		if (達到等級 > 0 && 目前等級 < 達到等級) {
			return;
		}

		if (獵殺怪物編號.length > 0) {
			for (int i = 0; i < 獵殺怪物編號.length; i++) {
				if (目前獵殺怪物數量[i] < 獵殺怪物編號[i]) {
					return;
				}
			}
		}

		if (獲得道具編號.length > 0) {
			for (int i = 0; i < 獲得道具編號.length; i++) {
				if (目前獲得道具數量[i] < 獲得道具數量[i]) {
					return;
				}
			}
		}
		if (使用道具編號.length > 0) {
			for (int i = 0; i < 使用道具編號.length; i++) {
				if (目前使用道具數量[i] < 使用道具數量[i]) {
					return;
				}
			}
		}

		isQuestComplete = true;
	}

	public L1QuestNew(final int questid) {

		id = questid;

		// switch (questid) {
		// case 256:// 道具使用
		// minQuestLevel = 1;
		// maxQuestLevel = 5;
		// 使用道具編號 = new int[] { 40030 };
		// 使用道具數量 = new int[] { 1 };
		// 目前使用道具數量 = new int[使用道具編號.length];
		// rewardExp = 100;
		// teleportLoc = new int[] { 32731, 32811, 3 };
		// break;
		//
		// case 257: // 攻擊訓練
		// maxQuestLevel = 1;
		// 達到等級 = 5;
		// rewardExp = 1000;
		// teleportLoc = new int[] { 32703, 32811, 3 };
		// break;
		//
		// case 258: // 實戰訓練
		// minQuestLevel = 5;
		// maxQuestLevel = 55;
		// 獵殺怪物編號 = new int[] { 45620 };
		// 獵殺怪物數量 = new int[] { 1 };
		// 目前獵殺怪物數量 = new int[獵殺怪物編號.length];
		// rewardExp = 28416;
		// rewardItemid = new int[] { 40308, }; // 修練者治癒藥水:15
		// rewardItemCount = new int[] { 1314, };
		// rewardItemEnchant = new int[] { 0, };
		// teleportLoc = new int[] { 33438, 32810, 4 };
		// break;
		// }

		for (final L1QuestNewSet questNew : QuestNewSetTable.getInstance().getAllList().values()) {
			if (questNew != null) {
				if (questNew.getQuestid() == questid) {
					// 
					if (questNew.getMinQuestLevel() != 0) {
						minQuestLevel = questNew.getMinQuestLevel();
					}
					// 
					if (questNew.getMaxQuestLevel() != 0) {
						maxQuestLevel = questNew.getMaxQuestLevel();
					}
					// 
					// if (questNew.getMapid() >= 0) {
					mapid = questNew.getMapid();
					// }
					// 
					if (questNew.getTeleportLoc() != null) {
						teleportLoc = questNew.getTeleportLoc();
					}
					// 
					if (questNew.getRewardItemid() != null
							&& questNew.getRewardItemCount() != null
							&& questNew.getRewardItemEnchant() != null
					) {
						rewardItemid = questNew.getRewardItemid();
						rewardItemCount = questNew.getRewardItemCount();
						rewardItemEnchant = questNew.getRewardItemEnchant();
					}
					// 
					if (questNew.getRewardSelectItemid() != null
							&& questNew.getRewardSelectItemCount() != null
							&& questNew.getRewardSelectItemEnchant() != null
					) {
						rewardSelectItemid = questNew.getRewardSelectItemid();
						rewardSelectItemCount = questNew.getRewardSelectItemCount();
						rewardSelectItemEnchant = questNew.getRewardSelectItemEnchant();
					}
					// 
					if (questNew.getRewardExp() != 0) {
						rewardExp = questNew.getRewardExp();
					}
					// 
					if (questNew.getRequireClassType() != null
							&& !questNew.getRequireClassType().isEmpty()
					) {
						requireClassType = questNew.getRequireClassType();
					}
					// 
					if (questNew.get達到等級() != 0) {
						達到等級 = questNew.get達到等級();
					}
					// 
					if (questNew.isRecoverRequireItem()) {
						isRecoverRequireItem = true;
					}
					// 
					if (questNew.get獵殺怪物編號() != null
							&& questNew.get獵殺怪物數量() != null
					) {
						獵殺怪物編號 = questNew.get獵殺怪物編號();
						獵殺怪物數量 = questNew.get獵殺怪物數量();
						目前獵殺怪物數量 = new int[獵殺怪物編號.length];
					}
					// 
					if (questNew.get獲得道具編號() != null
							&& questNew.get獲得道具數量() != null
							&& questNew.get獲得道具加成() != null
					) {
						獲得道具編號 = questNew.get獲得道具編號();
						獲得道具數量 = questNew.get獲得道具數量();
						獲得道具加成 = questNew.get獲得道具加成();
						目前獲得道具數量 = new int[獲得道具編號.length];
					}
					// 
					if (questNew.get使用道具編號() != null
							&& questNew.get使用道具數量() != null
					) {
						使用道具編號 = questNew.get使用道具編號();
						使用道具數量 = questNew.get使用道具數量();
						目前使用道具數量 = new int[使用道具編號.length];
					}
					break;
				}
			}
		}
	}

	public int getId() {
		return id;
	}

	public int getMinQuestLevel() {
		return minQuestLevel;
	}

	public int getMaxQuestLevel() {
		return maxQuestLevel;
	}

	public int getMapid() {
		return mapid;
	}

	public int[] getTeleportLoc() {
		return teleportLoc;
	}

	public int[] getRewardItemid() {
		return rewardItemid;
	}

	public int[] getRewardItemCount() {
		return rewardItemCount;
	}

	public int[] getRewardItemEnchant() {
		return rewardItemEnchant;
	}

	public int[] getRewardSelectItemid() {
		return rewardSelectItemid;
	}

	public int[] getRewardSelectItemCount() {
		return rewardSelectItemCount;
	}

	public int[] getRewardSelectItemEnchant() {
		return rewardSelectItemEnchant;
	}

	public int getRewardExp() {
		return rewardExp;
	}

	public String getRequireClassType() {
		return requireClassType;
	}

	public int get達到等級() {
		return 達到等級;
	}

	public boolean isRecoverRequireItem() {
		return isRecoverRequireItem;
	}

	public int[] get獵殺怪物編號() {
		return 獵殺怪物編號;
	}

	public int[] get獵殺怪物數量() {
		return 獵殺怪物數量;
	}

	public int[] get獲得道具編號() {
		return 獲得道具編號;
	}

	public int[] get獲得道具數量() {
		return 獲得道具數量;
	}

	public int[] get獲得道具加成() {
		return 獲得道具加成;
	}

	public int[] get使用道具編號() {
		return 使用道具編號;
	}

	public int[] get使用道具數量() {
		return 使用道具數量;
	}

	public boolean isQuestEnd() {
		return isQuestEnd;
	}

	public void setQuestEnd(final boolean flag) {
		isQuestEnd = flag;
	}

	public boolean isQuestComplete() {
		return isQuestComplete;
	}

	public void setQuestComplete(final boolean flag) {
		isQuestComplete = flag;
	}

	public L1PcInstance getOwner() {
		return owner;
	}

	public void setOwner(final L1PcInstance user) {
		owner = user;
	}

	public int get目前等級() {
		return 目前等級;
	}

	public void set目前等級(final int i) {
		目前等級 = i;
	}

	public int[] get目前獵殺怪物數量() {
		return 目前獵殺怪物數量;
	}

	public void set目前獵殺怪物數量(final int[] i) {
		目前獵殺怪物數量 = i;
	}

	public int[] get目前獲得道具數量() {
		return 目前獲得道具數量;
	}

	public void set目前獲得道具數量(final int[] i) {
		目前獲得道具數量 = i;
	}

	public int[] get目前使用道具數量() {
		return 目前使用道具數量;
	}

	public void set目前使用道具數量(final int[] i) {
		目前使用道具數量 = i;
	}

}
