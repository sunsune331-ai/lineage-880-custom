package com.lineage.server.serverpackets;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import com.google.protobuf.ByteString;
import com.lineage.config.Config;
import com.lineage.data.protobuf.PBMessageALL3;
import com.lineage.data.protobuf.PBMessageALL9_181;
import com.lineage.server.model.Instance.L1ItemInstance;

final class S_ItemPacket181 extends ServerBasePacket {
    private S_ItemPacket181(final List<L1ItemInstance> items, final boolean inventoryList) {
        writeC(S_EXTENDED_PROTOBUF);
        writeH(588);
        final PBMessageALL3.type8.Builder list = PBMessageALL3.type8.newBuilder();
        for (final L1ItemInstance item : items) {
            list.addArray1(buildItem(item).toByteString());
        }
        if (inventoryList) {
            list.setValue2(1);
        }
        writeByte(list.build().toByteArray());
        writeH(0);
    }

    static byte[] add(final L1ItemInstance item) {
        return new S_ItemPacket181(Collections.singletonList(item), false).getContent();
    }

    static byte[] inventory(final List<L1ItemInstance> items) {
        return new S_ItemPacket181(items, true).getContent();
    }

    private static PBMessageALL9_181.typeInvList buildItem(final L1ItemInstance item) {
        final PBMessageALL9_181.typeInvList.Builder value = PBMessageALL9_181.typeInvList.newBuilder();
        value.setValue1(item.getId());
        value.setValue2(item.getItem().getItemDescId());
        value.setValue3(item.getId());
        value.setValue4((int) item.getCount());
        value.setValue5(item.getItem().getUseType());
        value.setValue7(item.get_gfxid());
        value.setValue8(item.getBless());
        value.setValue9(item.getItemStatusX());
        value.setValue10(8);
        value.setValue11(0);
        if (item.getItem().getType() == 10) {
            value.setValue6(0);
            value.setValue13(0);
        } else {
            value.setValue6(item.getChargeCount());
            value.setValue13(item.getEnchantLevel());
        }
        value.setValue14(item.getBless() >= 128 ? 3 : item.getItem().isTradable() ? 7 : 2);
        value.setArray18(encode(item.getViewName()));
        if (item.isIdentified()) {
            value.setArray19(ByteString.copyFrom(item.getStatusBytes()));
        }
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
