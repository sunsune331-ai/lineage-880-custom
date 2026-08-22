package com.lineage.server.serverpackets;

import com.google.protobuf.ByteString;
import com.lineage.config.Config;
import com.lineage.config.ConfigOther;
import com.lineage.data.protobuf.PBMessageALL2;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;

final class S_ObjectPack181 extends ServerBasePacket {

    private S_ObjectPack181(final L1PcInstance pc, final boolean own) {
        writeC(S_EXTENDED_PROTOBUF);
        writeH(119);

        final PBMessageALL2.typeCharPack.Builder builder = PBMessageALL2.typeCharPack.newBuilder();
        builder.setValue1((pc.getY() << 16) + pc.getX());
        builder.setValue2(pc.getId());
        builder.setValue3(pc.isDead() ? pc.getTempCharGfxAtDead() : pc.getTempCharGfx());

        int action = pc.isDead() ? pc.getStatus() : pc.getCurrentWeapon();
        if (!own) {
            if (pc.isFishing()) {
                action = ActionCodes.ACTION_Fishing;
            } else if (pc.isPrivateShop()) {
                action = ActionCodes.ACTION_Shop;
            }
        }
        builder.setValue4(action);
        builder.setValue5(pc.getHeading());
        builder.setValue6(pc.getOwnLightSize());
        builder.setValue7(own ? 1 : pc.getMoveSpeed());
        builder.setValue8(pc.getLawful());
        builder.setArray9(byteString(displayName(pc, own)));
        builder.setArray10(byteString(displayTitle(pc, own)));
        builder.setValue11(pc.getMoveSpeed());
        builder.setValue12(pc.getBraveSpeed() > 0 ? 1 : 0);
        builder.setValue13(pc.isBraveX() ? 8 : pc.isDrink() ? 1 : 0);
        builder.setValue14(own ? (pc.isInvisble() || pc.isGmInvis() ? 1 : 0) : (pc.isInvisble() ? 1 : 0));
        builder.setValue15(pc.getPoison() != null && pc.getPoison().getEffectId() == 2 ? 1 : 0);
        builder.setValue16(1);
        builder.setValue17(pc.isGhost() ? 1 : 0);
        builder.setValue18(pc.getPoison() != null && pc.getPoison().getEffectId() == 1 ? 1 : 0);
        builder.setValue19(showClan(pc) ? pc.getEmblemId() : 0);
        builder.setArray20(byteString(showClan(pc) ? pc.getClanname() : ""));
        builder.setArray21(byteString(""));
        builder.setValue22(own && pc.getClanRank() > 0 ? pc.getClanRank() << 4 : 0);
        builder.setValue23(own && pc.isInParty() ? 100 * pc.getCurrentHp() / pc.getMaxHp() : -1);
        builder.setValue24(0);
        builder.setArray25(byteString(""));
        builder.setValue26(-1);
        builder.setValue27(0);
        builder.setValue28(pc.getPolyStatus());
        builder.setValue30(-1);
        builder.setValue32(Config.SERVERNO);
        writeByte(builder.build().toByteArray());
        writeH(0);
    }

    private S_ObjectPack181(final L1NpcInstance npc) {
        writeC(S_EXTENDED_PROTOBUF);
        writeH(119);

        final PBMessageALL2.typeCharPack.Builder builder = PBMessageALL2.typeCharPack.newBuilder();
        builder.setValue1((npc.getY() << 16) + npc.getX());
        builder.setValue2(npc.getId());
        final int npcGfx = npc.getTempCharGfx() == 0 ? npc.getGfxId() : npc.getTempCharGfx();
        builder.setValue3(protocol181NpcGfx(npcGfx));
        builder.setValue4(npc.getNpcTemplate().is_doppel() && npc.getGfxId() != 31 ? 4 : npc.getStatus());
        builder.setValue5(npc.getHeading());
        builder.setValue6(npc.getChaLightSize());
        builder.setValue7(npc.getMoveSpeed());
        builder.setValue8(npc.getLawful());
        builder.setArray9(byteString(npc.getNameId()));
        builder.setArray10(byteString(npc.getTitle()));
        builder.setValue11(npc.getMoveSpeed());
        builder.setValue12(npc.getBraveSpeed());
        builder.setValue13(0);
        builder.setValue14(0);
        builder.setValue15(npc.getPoison() != null && npc.getPoison().getEffectId() == 2 ? 1 : 0);
        builder.setValue16(npc.getNpcTemplate().is_doppel() ? 1 : 0);
        builder.setValue17(0);
        builder.setValue18(npc.getPoison() != null && npc.getPoison().getEffectId() == 1 ? 1 : 0);
        builder.setValue19(0);
        builder.setArray20(byteString(""));
        builder.setArray21(byteString(""));
        builder.setValue22(npc.getHiddenStatus());
        builder.setValue23(-1);
        builder.setValue24(npc.getLevel());
        builder.setValue26(-1);
        builder.setValue27(0);
        builder.setValue28(0);
        builder.setValue30(-1);
        builder.setValue32(0);
        builder.setValue34(12);
        builder.setValue39(npc.getNpcId());
        writeByte(builder.build().toByteArray());
        writeH(0);
    }

    static byte[] own(final L1PcInstance pc) {
        return new S_ObjectPack181(pc, true).getContent();
    }

    static byte[] other(final L1PcInstance pc) {
        return new S_ObjectPack181(pc, false).getContent();
    }

    static byte[] npc(final L1NpcInstance npc) {
        return new S_ObjectPack181(npc).getContent();
    }

    private static int protocol181NpcGfx(final int gfx) {
        return gfx >= 14000 ? 949 : gfx;
    }

    private static boolean showClan(final L1PcInstance pc) {
        final int team = pc.get_redbluejoin();
        return team != 11 && team != 21 && team != 12 && team != 22;
    }

    private static String displayTitle(final L1PcInstance pc, final boolean own) {
        return own || showClan(pc) ? pc.getTitle() : "";
    }

    private static String displayName(final L1PcInstance pc, final boolean own) {
        if (!own) {
            final int team = pc.get_redbluejoin();
            if (team == 11 || team == 21) {
                return "\\f=紅隊";
            }
            if (team == 12 || team == 22) {
                return "\\f2藍隊";
            }
            if (pc.isProtector()) {
                return "**守護者**";
            }
            if (ConfigOther.SHOW_SP_TITLE) {
                final StringBuilder result = new StringBuilder();
                if (pc.get_c_power() != null && pc.get_c_power().get_c1_type() != 0) {
                    result.append(pc.get_c_power().get_power().get_c1_name_type());
                }
                if (pc.get_other().get_color() != 0) {
                    result.append(pc.get_other().color());
                }
                result.append(pc.getName());
                if (pc.getMeteAbility() != null) {
                    result.append(pc.getMeteAbility().getTitle());
                }
                return result.toString();
            }
            if (pc.get_other().get_color() != 0) {
                return pc.get_other().color() + pc.getName();
            }
        }
        return pc.getName();
    }

    private static ByteString byteString(final String value) {
        final String text = value == null ? "" : value;
        try {
            return ByteString.copyFrom(text.getBytes(CLIENT_LANGUAGE_CODE));
        } catch (final Exception e) {
            return ByteString.copyFromUtf8(text);
        }
    }

    @Override
    public byte[] getContent() {
        return _bao.toByteArray();
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }
}
