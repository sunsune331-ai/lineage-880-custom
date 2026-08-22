package com.lineage.server.model;

import java.util.ArrayList;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.world.World;

public class Broadcaster {
    /**
     * 可視範圍、送信。
     *
     * @param packet 送信示ServerBasePacket。
     */
	public static void broadcastPacket(L1Character cha, ServerBasePacket packet) {
		ArrayList<L1PcInstance> list = null;
		list = World.get().getVisiblePlayer(cha);
		for (L1PcInstance pc : list) {
			pc.sendPackets(packet);
		}
	}

    /**
     * 可視範圍、送信。、畫面送信。
     *
     * @param packet 送信示ServerBasePacket。
     */
	public static void broadcastPacketExceptTargetSight(L1Character cha, ServerBasePacket packet, L1Character target) {
		ArrayList<L1PcInstance> list = null;
		list = World.get().getVisiblePlayerExceptTargetSight(cha, target);
		for (L1PcInstance pc : list) {
			pc.sendPackets(packet);
		}
	}

    /**
     * 50以內、送信。
     *
     * @param packet 送信示ServerBasePacket。
     */
	public static void wideBroadcastPacket(L1Character cha, ServerBasePacket packet) {
		ArrayList<L1PcInstance> list = null;
		list = World.get().getVisiblePlayer(cha, 50);
		for (L1PcInstance pc : list) {
			pc.sendPackets(packet);
		}
	}
}
