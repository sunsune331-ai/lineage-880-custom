package com.lineage.server.serverpackets;

import java.io.UnsupportedEncodingException;

import com.google.protobuf.ByteString;
import com.lineage.config.Config;
import com.lineage.data.protobuf.PBMessageALL;
import com.lineage.data.protobuf.PBMessageALL3;
import com.lineage.data.protobuf.PBMessageALL7;
import com.lineage.data.protobuf.PBMessageALL8;
import com.lineage.server.model.Instance.L1PcInstance;

final class S_PartyPacket181 extends ServerBasePacket {
    private S_PartyPacket181() {
    }

    static byte[] packet(final int type, final int subCode, final L1PcInstance pc) {
        final S_PartyPacket181 out = new S_PartyPacket181();
        if (type == S_Party.OPCODE_TYPE_PARTY_MEMBER_LIST) {
            out.memberList(pc);
        } else if (type == S_Party.OPCODE_TYPE_PARTY_MEMBER_LIST_CHANGE) {
            out.memberChange(subCode, pc);
        } else if (type == S_Party.OPCODE_TYPE_PARTY_MEMBER_STATUS) {
            out.memberStatus(subCode, pc);
        } else if (type == S_Party.OPCODE_TYPE_PARTY_OPERATION_RESULT_NOTI) {
            out.operationResult(pc);
        } else if (type == S_Party.OPCODE_TYPE_PARTY_SYNC_PERIODIC_INFO) {
            out.refresh(pc);
        }
        return out.getContent();
    }

    static byte[] operationResult(final String name) {
        final S_PartyPacket181 out = new S_PartyPacket181();
        out.writeC(S_EXTENDED_PROTOBUF);
        out.writeH(539);
        final PBMessageALL3.type6.Builder value = PBMessageALL3.type6.newBuilder();
        value.setValue1(2);
        value.setArray2(encode(name));
        out.writeByte(value.build().toByteArray());
        out.writeH(0);
        return out.getContent();
    }

    static byte[] mark(final int value1, final int value2) {
        final S_PartyPacket181 out = new S_PartyPacket181();
        out.writeC(S_EXTENDED_PROTOBUF);
        out.writeH(339);
        final PBMessageALL.type1.Builder value = PBMessageALL.type1.newBuilder();
        value.setValue1(value1);
        value.setValue2(value2);
        out.writeByte(value.build().toByteArray());
        out.writeH(0);
        return out.getContent();
    }

    private void operationResult(final L1PcInstance pc) {
        writeC(S_EXTENDED_PROTOBUF);
        writeH(539);
        final PBMessageALL3.type6.Builder value = PBMessageALL3.type6.newBuilder();
        value.setValue1(pc.getParty() == null ? 1 : pc.getParty().getNumOfMembers());
        value.setArray2(encode(pc.getName()));
        writeByte(value.build().toByteArray());
        writeH(0);
    }

    private void memberList(final L1PcInstance pc) {
        writeC(S_EXTENDED_PROTOBUF);
        writeH(823);
        final PBMessageALL7.type30.Builder list = PBMessageALL7.type30.newBuilder();
        list.setArray1(encode(pc.getParty().getLeader().getName()));
        for (final L1PcInstance member : pc.getParty().getMembers()) {
            list.addArray2(member(member).toByteString());
        }
        writeByte(list.build().toByteArray());
        writeH(0);
    }

    private void memberChange(final int subCode, final L1PcInstance pc) {
        writeC(S_EXTENDED_PROTOBUF);
        writeH(824);
        if (subCode == S_Party.PACKET_TYPE_NEW_MEMBER) {
            final PBMessageALL3.type6.Builder value = PBMessageALL3.type6.newBuilder();
            value.setArray4(member(pc).toByteString());
            writeByte(value.build().toByteArray());
        } else {
            final PBMessageALL7.type30.Builder value = PBMessageALL7.type30.newBuilder();
            value.setArray1(encode(pc.getName()));
            writeByte(value.build().toByteArray());
        }
        writeH(0);
    }

    private void memberStatus(final int subCode, final L1PcInstance pc) {
        writeC(S_EXTENDED_PROTOBUF);
        writeH(825);
        final PBMessageALL8.typeSoulTower.Builder value = PBMessageALL8.typeSoulTower.newBuilder();
        value.setArray1(encode(pc.getName()));
        if (subCode == S_Party.MEMBER_HPMP_CHANGE) {
            value.setValue2(pc.getId());
            value.setValue3(pc.getMaxHp() == 0 ? 0 : 100 * pc.getCurrentHp() / pc.getMaxHp());
            value.setValue4(pc.getMaxMp() == 0 ? 0xff : 100 * pc.getCurrentMp() / pc.getMaxMp());
        } else if (subCode == S_Party.MEMBER_TELEPORT) {
            value.setValue6(pc.getMapId());
            value.setValue7((pc.getY() << 16) + pc.getX());
        } else if (subCode == S_Party.MEMBER_MARK_CHANGE) {
            value.setValue8(pc.getPartySign());
        }
        writeByte(value.build().toByteArray());
        writeH(0);
    }

    private void refresh(final L1PcInstance pc) {
        writeC(S_EXTENDED_PROTOBUF);
        writeH(827);
        final PBMessageALL3.type8.Builder list = PBMessageALL3.type8.newBuilder();
        for (final L1PcInstance member : pc.getParty().getMembers()) {
            final PBMessageALL8.typeSoulTower.Builder value = PBMessageALL8.typeSoulTower.newBuilder();
            value.setArray1(encode(member.getName()));
            value.setValue6(member.getMapId());
            value.setValue7((member.getY() << 16) + member.getX());
            list.addArray1(value.build().toByteString());
        }
        writeByte(list.build().toByteArray());
        writeH(0);
    }

    private static PBMessageALL8.typeSoulTower member(final L1PcInstance pc) {
        final PBMessageALL8.typeSoulTower.Builder value = PBMessageALL8.typeSoulTower.newBuilder();
        value.setArray1(encode(pc.getName()));
        value.setValue2(pc.getId());
        value.setValue3(pc.getId());
        value.setValue4(pc.getType());
        value.setValue5(pc.get_sex());
        value.setValue6(pc.getMaxHp() == 0 ? 0 : 100 * pc.getCurrentHp() / pc.getMaxHp());
        value.setValue7(pc.getMaxMp() == 0 ? 0 : 100 * pc.getCurrentMp() / pc.getMaxMp());
        value.setValue8(pc.getMapId());
        value.setValue9((pc.getY() << 16) + pc.getX());
        value.setValue10(0);
        value.setValue11(Config.SERVERNO);
        return value.build();
    }

    private static ByteString encode(final String value) {
        try {
            return ByteString.copyFrom(value.getBytes(Config.CLIENT_LANGUAGE_CODE));
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public byte[] getContent() {
        return getBytes();
    }
}
