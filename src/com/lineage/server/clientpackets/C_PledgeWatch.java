/**
 * License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE").
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE
 * MAY BE CONSIDERED TO BE A CONTRACT, THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_PledgeWatch;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

/**
 * 血盟關注
 * @author l1j-tw
 */
public class C_PledgeWatch extends ClientBasePacket {

	@Override
	public void start(final byte[] decrypt, final ClientExecutor client) {
		// 資料載入
		this.read(decrypt);

		final L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}

		if (pc.getClanid() == 0) {
			pc.sendPackets(new S_ServerMessage(518)); // 血盟君主才可使用此命令。
			return;
		}

		final L1Clan clan = ClanReading.get().getTemplate(pc.getClanid());

		final int type = readC();

		if (type == 0) { // 新增要關注的血盟名
			final String name = readS();

			if (clan.getClanName().equalsIgnoreCase(name)) {
				return;
			}

			// final L1Clan targetClan = ClanReading.get().getTemplate(name);
			final L1Clan targetClan = WorldClan.get().getClan(name);

			if (targetClan == null) {
				pc.sendPackets(new S_ServerMessage(3982)); // 為不存在的血盟
				return;
			}

			final L1PcInstance targetClanLeader = World.get().getPlayer(targetClan.getLeaderName());

			if (targetClanLeader == null) {
				pc.sendPackets(new S_ServerMessage(3349)); // 對像血盟的王族為離線狀態。
				return;
			}

			// 3348 %0 血盟想要查看您的血盟，接受嗎？
			targetClanLeader.sendPackets(new S_Message_YN(3348, clan.getClanName()));
			targetClanLeader.setTempID(clan.getClanId());

		} else if (type == 1) { // 刪除關注的血盟名
			final String clanName = readS();

			// final L1Clan targetClan = ClanTable.getInstance().getTemplate(clanName);
			final L1Clan targetClan = WorldClan.get().getClan(clanName);

			if (targetClan == null) {
				pc.sendPackets(new S_ServerMessage(3982)); // 為不存在的血盟
				return;
			}
			clan.getWatchClanList().remove((Object) targetClan.getClanId());
			// for (final L1PcInstance member : clan.getOnlineMemberList()) {
			final L1PcInstance clanMember[] = clan.getOnlineClanMember();
			for (final L1PcInstance member : clanMember) {
				member.sendPackets(new S_ServerMessage(3359, targetClan.getClanName())); // %0 血盟不可再進行查看了。
				member.sendPackets(new S_PledgeWatch(clan));
			}
			ClanReading.get().updateClan(clan);

			// target clan
			targetClan.getWatchClanList().remove((Object) clan.getClanId());
			// for (final L1PcInstance member : targetClan.getOnlineMemberList()) {
			final L1PcInstance clanMember1[] = targetClan.getOnlineClanMember();
			for (final L1PcInstance member1 : clanMember1) {
				member1.sendPackets(new S_ServerMessage(3359, clan.getClanName())); // %0 血盟不可再進行查看了。
				member1.sendPackets(new S_PledgeWatch(targetClan));
			}
			ClanReading.get().updateClan(targetClan);
		}

	}

	@Override
	public String getType() {
		return "C_PledgeWatch";
	}

}
