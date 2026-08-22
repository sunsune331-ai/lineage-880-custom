package com.lineage.server.serverpackets;

import com.lineage.server.model.Instance.L1SkinInstance;

public class S_NPCPack_Skin extends ServerBasePacket
{
  private byte[] _byte = null;

  public S_NPCPack_Skin(L1SkinInstance pet)
  {
		writeByte(S_ObjectPack181.npc(pet));
  }

  public byte[] getContent()
  {
    if (this._byte == null) {
      this._byte = getBytes();
    }
    return this._byte;
  }

  public String getType()
  {
    return getClass().getSimpleName();
  }
}
