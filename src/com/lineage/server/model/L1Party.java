package com.lineage.server.model;

import static com.lineage.server.model.skill.L1SkillId.BRAVE_AVATAR;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigAlt;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_Party;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.RandomArrayList;

/**
 * 8.8C組隊
 */
public class L1Party {

	private static final Log _log = LogFactory.getLog(L1Party.class);

	private final List<L1PcInstance> _membersList = new CopyOnWriteArrayList<L1PcInstance>();

	private L1PcInstance _leader = null;

	// public void addBattleZonePartyMember(L1PcInstance pc) {
	// if (pc == null) {
	// throw new NullPointerException();
	// }
	//
	// if (_membersList.isEmpty()) {
	// setLeader(pc);
	// }
	//
	// _membersList.add(pc);
	// pc.setParty(this);
	// showAddPartyInfo(pc);
	// PcPartyRefreshThread.getInstance().addPlayer(pc);
	// }

	/**
	 * 加入新的隊伍成員
	 * @param pc
	 */
	public void addMember(final L1PcInstance pc) {
		try {
			if (pc == null) {
				throw new NullPointerException();
			}
			if (_membersList.size() == ConfigAlt.MAX_PARTY_SIZE || _membersList.contains(pc)) {
				// 417：你的隊伍已經滿了，無法再接受隊員。
				pc.sendPackets(new S_ServerMessage(417));
				return;
			}

			if (_membersList.isEmpty()) { // 隊員清單為空
				// 初始化設置隊長
				setLeader(pc);
			}

			// 加入清單
			_membersList.add(pc);

			// 設置隊伍數據
			pc.setParty(this);

			pc.sendPackets(new S_Party(S_Party.OPCODE_TYPE_PARTY_MEMBER_LIST, S_Party.PACKET_NONE, pc));

			// 顯示隊伍UI數據
			showAddPartyInfo(pc);
			// PcPartyRefreshThread.getInstance().addPlayer(pc);

			// 王者加護
			if (pc.isCrown() // 加入隊伍的是王族
					&& pc.isSkillMastery(BRAVE_AVATAR) // 習得王者加護
					&& !isContainsBraveCrown(pc)) { // 隊伍內沒有其它學王者加護的王族
				for (final L1PcInstance member : getMemberList()) {
					final L1SkillUse l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(member, BRAVE_AVATAR, member.getId(), member.getX(), member.getY(), 0,
							L1SkillUse.TYPE_GMBUFF);
				}
			} else if (isContainsBraveCrown(pc)) { // 其他職業- 隊伍內有另一個有學的王族
				for (final L1PcInstance member : getMemberList()) {
					final L1SkillUse l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(member, BRAVE_AVATAR, member.getId(), member.getX(), member.getY(), 0,
							L1SkillUse.TYPE_GMBUFF);
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 隊伍內有學王者加護的王族
	 * @param pc
	 * @return
	 */
	private boolean isContainsBraveCrown(final L1PcInstance pc) {
		for (final L1PcInstance member : getMemberList()) {
			if (member.getId() == pc.getId()) {
				continue;
			}
			if (member.isCrown() && member.isSkillMastery(BRAVE_AVATAR)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 移出隊伍成員
	 * @param pc
	 */
	private void removeMember(final L1PcInstance pc) {
		try {
			if (!_membersList.contains(pc)) {
				return;
			}

			// 隊長離開隊伍時隊員總數大於2位，則自動委任隊長給其他隊員
			if (pc.getParty().isLeader(pc) && getMembers().length > 2) {
				passLeader(_membersList.get(1));
			}

			_membersList.remove(pc);
			pc.setParty(null);

			// int[] delete_skill = { L1SkillId.CUBE_AVATAR,
			// L1SkillId.CUBE_GOLEM,
			// L1SkillId.CUBE_OGRE, L1SkillId.CUBE_LICH };
			// for (Integer i : delete_skill) {
			// if (pc.getSkillEffectTimerSet().hasSkillEffect(i)) {
			// pc.getSkillEffectTimerSet().removeSkillEffect(i);
			// }
			// }

			// 王者加護
			if (pc.hasSkillEffect(BRAVE_AVATAR)) {
				pc.removeSkillEffect(BRAVE_AVATAR);

				if (_membersList.size() <= 1) {
					return;
				}
			}
			if (pc.isCrown() // 離開隊伍的是王族
					&& pc.isSkillMastery(BRAVE_AVATAR)) { // 習得王者加護
				if (!isContainsBraveCrown(pc)) { // 隊伍內沒有其它學王者加護的王族
					for (final L1PcInstance member : getMemberList()) {
						member.removeSkillEffect(BRAVE_AVATAR);
					}
					return;
				}
			}
			if (isContainsBraveCrown(pc)) { // 其他職業- 隊伍內有另一個有學的王族
				for (final L1PcInstance member : getMemberList()) {
					final L1SkillUse l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(member, BRAVE_AVATAR, member.getId(), member.getX(), member.getY(), 0,
							L1SkillUse.TYPE_GMBUFF);
				}
			}

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 隊伍成員尚未飽和
	 * @return
	 */
	public boolean isVacancy() {
		return _membersList.size() < ConfigAlt.MAX_PARTY_SIZE;
	}

	/**
	 * 剩餘可加入隊伍人數
	 * @return
	 */
	public int getVacancy() {
		return ConfigAlt.MAX_PARTY_SIZE - _membersList.size();
	}

	/**
	 * 是否為隊員
	 * @param pc
	 * @return
	 */
	public boolean isMember(final L1PcInstance pc) {
		return _membersList.contains(pc);
	}

	/**
	 * 設置隊長
	 * @param pc
	 */
	private void setLeader(final L1PcInstance pc) {
		_leader = pc;
	}

	/**
	 * 傳回隊長
	 * @return
	 */
	public L1PcInstance getLeader() {
		return _leader;
	}

	/**
	 * 是否為隊長
	 * @param pc
	 * @return
	 */
	public boolean isLeader(final L1PcInstance pc) {
		return pc.getId() == _leader.getId();
	}

	public boolean isAutoDivision(final L1PcInstance pc) {
		return pc.getPartyType() == 1 || pc.getPartyType() == 4;
	}

	/**
	 * 全隊員名稱
	 * @return
	 */
	public String getMembersNameList() {
		String _result = new String("");
		for (L1PcInstance pc : _membersList) {
			_result = _result + pc.getName() + " ";
		}
		return _result;
	}

	/**
	 * 顯示組隊UI介面
	 * @param pc
	 */
	private void showAddPartyInfo(final L1PcInstance pc) {
		for (final L1PcInstance member : getMembers()) {
			if (pc.getId() == member.getId()) {
				continue;
			}
			member.sendPackets(new S_Party(S_Party.OPCODE_TYPE_PARTY_MEMBER_LIST_CHANGE, S_Party.PACKET_TYPE_NEW_MEMBER, pc));
			member.sendPackets(new S_Party(S_Party.OPCODE_TYPE_PARTY_OPERATION_RESULT_NOTI, S_Party.JOIN_YN_OK, pc));
		}
	}

	/**
	 * 隊員血條更新
	 * @param pc
	 */
	public void updateMiniHP(final L1PcInstance pc) {
		for (final L1PcInstance member : getMembers()) {
			/*
			 * if (pc.getId() == member.getId()) continue;
			 */
			member.sendPackets(new S_Party(S_Party.OPCODE_TYPE_PARTY_MEMBER_STATUS, S_Party.MEMBER_HPMP_CHANGE, pc));
		}
	}

	/**
	 * 隊長委任給其他隊員
	 * @param pc
	 */
	public void passLeader(final L1PcInstance pc) {
		for (final L1PcInstance member : getMembers()) {
			member.getParty().setLeader(pc);
			member.sendPackets(new S_Party(S_Party.OPCODE_TYPE_PARTY_MEMBER_LIST_CHANGE, S_Party.PACKET_TYPE_CHANGELEADER, pc));
		}
	}

	/**
	 * 離開隊伍
	 * @param pc
	 */
	public void leaveMember(final L1PcInstance pc) {
		if (getMembers().length == 2) {
			for (final L1PcInstance member : getMembers()) {
				member.sendPackets(new S_Party(S_Party.OPCODE_TYPE_PARTY_MEMBER_LIST_CHANGE, S_Party.PACKET_TYPE_LEAVE_MEMBER, member));
				// 418：您解散您的隊伍了!!
				member.sendPackets(new S_ServerMessage(418));
				removeMember(member);
			}
		} else {
			for (final L1PcInstance member : getMembers()) {
				member.sendPackets(new S_Party(S_Party.OPCODE_TYPE_PARTY_MEMBER_LIST_CHANGE, S_Party.PACKET_TYPE_LEAVE_MEMBER, pc));
			}
			removeMember(pc);
		}
	}

	public void refresh(final L1PcInstance pc) {
		pc.sendPackets(new S_Party(S_Party.OPCODE_TYPE_PARTY_SYNC_PERIODIC_INFO, S_Party.PACKET_NONE, pc));
	}

	/**
	 * 驅逐隊員
	 * @param pc
	 */
	public void kickMember(L1PcInstance pc) {

		for (L1PcInstance member : getMembers()) {
			member.sendPackets(new S_Party(S_Party.OPCODE_TYPE_PARTY_MEMBER_LIST_CHANGE, S_Party.PACKET_TYPE_LEAVE_MEMBER, pc));
		}

		removeMember(pc);
		// 419：您從隊伍中被驅逐了。
		pc.sendPackets(new S_ServerMessage(419));
	}

	/**
	 * 隊伍成員清單
	 * @return
	 */
	public L1PcInstance[] getMembers() {
		return _membersList.toArray(new L1PcInstance[_membersList.size()]);
	}

	/**
	 * 隊伍成員數量
	 * @return
	 */
	public int getNumOfMembers() {
		return _membersList.size();
	}

	/**
	 * 全部隊員名單
	 * @return
	 */
	public List<L1PcInstance> getMemberList() {
		return _membersList;
	}

	/**
	 * 該隊伍目前人數(同地圖)
	 * @return
	 */
	public int partyUserInMap(final short mapid) {
		int i = 0;
		if (this._membersList.isEmpty()) {
			return 0;
		}
		if (this._membersList.size() <= 0) {
			return 0;
		}

		for (final L1PcInstance tgpc : getMembers()) {
			short tgpcMapid = tgpc.getMapId();
			if (tgpcMapid == mapid) {
				i += 1;
			}
		}
		return i;
	}

	public final int checkMentor(final L1Apprentice apprentice) {
		int checkType = 0;
		for (final L1PcInstance member : _membersList) {
			if (apprentice.getMaster().getId() == member.getId()) {
				checkType += 4;
			} else if (apprentice.getTotalList().contains(member)) {
				checkType++;
			}
		}
		return checkType;
	}

	/**
	 * 傳回隊長OBJID
	 * @return
	 */
	public int getLeaderID() {
		return _leader.getId();
	}

	public L1PcInstance partyUser() {
		final List<L1PcInstance> userList = new CopyOnWriteArrayList<L1PcInstance>();
		for (final L1PcInstance pc : _membersList) {
			if (!_leader.equals(pc)) {
				userList.add(pc);
			}
		}
		if (!userList.isEmpty()) {
			return userList.get(RandomArrayList.getInt(userList.size()));
		}
		return null;
	}

	public List<String> getPartyMembers() {
		final List<String> partyMembers = new CopyOnWriteArrayList<String>();
		for (final L1PcInstance pc : _membersList) {
			if (!_leader.equals(pc)) {
				partyMembers.add(pc.getName());
			}
		}
		return partyMembers;
	}
}
