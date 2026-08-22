package com.lineage.echo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.Config;
import com.lineage.server.clientpackets.*;

/**
 * 客戶端封包處理
 * 
 * @author dexc
 */
public class PacketHandler extends PacketHandlerExecutor {

	private static final Log _log = LogFactory.getLog(PacketHandler.class);

	// Map<K,V>
	private static final Map<Integer, ClientBasePacket> _opListClient = new HashMap<Integer, ClientBasePacket>();

	private final ClientExecutor _client;

	/**
	 * 客戶端封包處理
	 * 
	 * @param decrypt
	 * @param object
	 * @throws Exception
	 */
	@Override
	public void handlePacket(final byte[] decrypt) {
		if (decrypt == null) {
			return;
		}
		if (decrypt.length <= 0) {
			return;
		}

		// 一般的處理封包方式
		final int i = decrypt[0] & 0xff;
        if (Config.opcode_C) { // 日版封包顯示
        	System.out.println("[C opocde] = " + i + "[Length] = " + decrypt.length);
        	System.out.println(DataToPacket(decrypt, decrypt.length));
        }
		if (_client.getActiveChar() == null) {
			if (_client.getAccount() == null) {
				if (i == C_VERSION) {
					new C_ServerVersion().start(decrypt, _client);
					return;
				}
				if (i == C_EXTENDED_PROTOBUF) {
					new C_ProtoBuffers().start(decrypt, _client);
					return;
				}
				if (i == C_LOGIN) {
					new C_AuthLogin().start181(decrypt, _client);
					return;
				}
				if (i == C_SHIFT_SERVER) {
					new C_AutoLogin().start(decrypt, _client);
					return;
				}
			} else if (i == C_READ_NEWS) {
				new C_CommonClick().start(decrypt, _client);
				return;
			}
		}
		// try {
		//System.out.println("[Client] opcode = " + i);
		if (i == C_OPCODE_CHARRESET) {
			new C_CharReset().start(decrypt, _client);
		} else if (i == C_OPCODE_EXCLUDE) {
			new C_Exclude().start(decrypt, _client);
		} else if (i == C_OPCODE_LOGOUT) {
			new C_ReturnToLogin().start(decrypt, _client);
		} else if (i == C_OPCODE_CHARACTERCONFIG) {
			new C_CharcterConfig().start(decrypt, _client);
		} else if (i == C_OPCODE_DOOR) {
			new C_Door().start(decrypt, _client);
		} else if (i == C_OPCODE_TITLE) {
			new C_Title().start(decrypt, _client);
		} else if (i == C_OPCODE_BOARDDELETE) {
			new C_BoardDelete().start(decrypt, _client);
		} else if (i == C_OPCODE_PLEDGE) {
			new C_Pledge().start(decrypt, _client);
		} else if (i == C_OPCODE_CHANGEHEADING) {
			new C_ChangeHeading().start(decrypt, _client);
		} else if (i == C_OPCODE_NPCACTION) {
			new C_NPCAction().start(decrypt, _client);
		} else if (i == C_OPCODE_USESKILL) {
			new C_UseSkill().start(decrypt, _client);
		} else if (i == C_OPCODE_EMBLEMDOWNLOAD) {
			new C_EmblemDownload().start(decrypt, _client);//
		} else if (i == C_OPCODE_EMBLEMUPLOAD) {
			new C_EmblemUpload().start(decrypt, _client);//
		} else if (i == C_OPCODE_TRADEADDCANCEL) {
			new C_TradeCancel().start(decrypt, _client);
		} else if (i == C_OPCODE_BOOKMARK) {
			new C_AddBookmark().start(decrypt, _client);
		} else if (i == C_OPCODE_CREATECLAN) {
			new C_CreateClan().start(decrypt, _client);
		} else if (i == C_OPCODE_CLIENTVERSION) {
			new C_ServerVersion().start(decrypt, _client);
		} else if (i == C_OPCODE_PROPOSE) {
			new C_Propose().start(decrypt, _client);
		} else if (i == C_OPCODE_BOARDBACK) {
			new C_BoardBack().start(decrypt, _client);
		} else if (i == C_OPCODE_SHOP) {
			new C_Shop().start(decrypt, _client);
		} else if (i == C_OPCODE_BOARDREAD) {
			new C_BoardRead().start(decrypt, _client);
		} else if (i == C_OPCODE_TRADE) {
			new C_Trade().start(decrypt, _client);
		} else if (i == C_OPCODE_DELETECHAR) {
			new C_DeleteChar().start(decrypt, _client);
		} else if (i == C_OPCODE_ATTR) {
			new C_Attr().start(decrypt, _client);
		} else if (i == C_OPCODE_LOGINPACKET) {
			//new C_AuthLogin().start(decrypt, _client);
			if (Config.Bean_Fun) {
				new C_Login_BeanFun().start(decrypt, _client);
			} else {
				new C_AuthLogin().start(decrypt, _client);
			}
		} else if (i == C_OPCODE_RESULT) {
			new C_Result().start(decrypt, _client);
		} else if (i == C_OPCODE_DEPOSIT) {
			new C_Deposit().start(decrypt, _client);
		} else if (i == C_OPCODE_DRAWAL) {
			new C_Drawal().start(decrypt, _client);
		} else if (i == C_OPCODE_LOGINTOSERVEROK) {
			new C_LoginToServerOK().start(decrypt, _client);
		} else if (i == C_OPCODE_SKILLBUY) {
			new C_SkillBuy().start(decrypt, _client);
		} else if (i == C_OPCODE_SKILLBUYOK) {
			new C_SkillBuyOK().start(decrypt, _client);
		} else if (i == C_OPCODE_SKILLBUYITEM) {
			new C_SkillBuyItem().start(decrypt, _client);
		} else if (i == C_OPCODE_SKILLBUYITEMOK) {
			new C_SkillBuyItemOK().start(decrypt, _client);
		} else if (i == C_OPCODE_TRADEADDITEM) {
			new C_TradeAddItem().start(decrypt, _client);
		} else if (i == C_OPCODE_ADDBUDDY) {
			new C_AddBuddy().start(decrypt, _client);
		} else if (i == C_OPCODE_CHAT) {
			new C_Chat().start(decrypt, _client);
		} else if (i == C_OPCODE_TRADEADDOK) {
			new C_TradeOK().start(decrypt, _client);
		} else if (i == C_OPCODE_CHECKPK) {
			new C_CheckPK().start(decrypt, _client);
		} else if (i == C_OPCODE_TAXRATE) {
			new C_TaxRate().start(decrypt, _client);
		} else if (i == C_OPCODE_CHANGECHAR) {
			new C_NewCharSelect().start(decrypt, _client);
		} else if (i == C_OPCODE_BUDDYLIST) {
			new C_Buddy().start(decrypt, _client);
		} else if (i == C_OPCODE_DROPITEM) {
			new C_DropItem().start(decrypt, _client);
		} else if (i == C_OPCODE_LEAVEPARTY) {
			new C_LeaveParty().start(decrypt, _client);
		} else if (i == C_OPCODE_ATTACK) {
			new C_Attack().start(decrypt, _client);
		} else if (i == C_OPCODE_ARROWATTACK) {
			new C_AttackBow().start(decrypt, _client);
		} else if (i == C_OPCODE_BANCLAN) {
			new C_BanClan().start(decrypt, _client);
		} else if (i == C_OPCODE_BOARD) {
			new C_Board().start(decrypt, _client);
		} else if (i == C_OPCODE_DELETEINVENTORYITEM) {
			new C_DeleteInventoryItem().start(decrypt, _client);
		} else if (i == C_OPCODE_CHATWHISPER) {//
			new C_ChatWhisper().start(decrypt, _client);
		} else if (i == C_OPCODE_CAHTPARTY) {
			new C_ChatParty().start(decrypt, _client);
		} else if (i == C_OPCODE_PARTYLIST) {
			new C_Party().start(decrypt, _client);
		} else if (i == C_OPCODE_PICKUPITEM) {
			new C_PickUpItem().start(decrypt, _client);
		} else if (i == C_OPCODE_WHO) {
			new C_Who().start(decrypt, _client);
		} else if (i == C_OPCODE_GIVEITEM) {
			new C_GiveItem().start(decrypt, _client);
		} else if (i == C_OPCODE_MOVECHAR) {
			new C_MoveChar().start(decrypt, _client);
		} else if (i == C_OPCODE_BOOKMARKDELETE) {
			new C_DeleteBookmark().start(decrypt, _client);
		} else if (i == C_OPCODE_RESTART) {
			new C_Restart().start(decrypt, _client);
		} else if (i == C_OPCODE_LEAVECLANE) {
			new C_LeaveClan().start(decrypt, _client);
		} else if (i == C_OPCODE_NPCTALK) {
			new C_NPCTalk().start(decrypt, _client);
		} else if (i == C_OPCODE_BANPARTY) {
			new C_BanParty().start(decrypt, _client);
		} else if (i == C_OPCODE_DELBUDDY) {
			new C_DelBuddy().start(decrypt, _client);
		} else if (i == C_OPCODE_WAR) {
			new C_War().start(decrypt, _client);
		} else if (i == C_OPCODE_LOGINTOSERVER) {
			new C_LoginToServer().start(decrypt, _client);
		} else if (i == C_OPCODE_PRIVATESHOPLIST) {
			new C_ShopList().start(decrypt, _client);
		} else if (i == C_OPCODE_CHATGLOBAL) {
			new C_ChatGlobal().start(decrypt, _client);
		} else if (i == C_OPCODE_JOINCLAN) {
			new C_JoinClan().start(decrypt, _client);
		} else if (i == C_OPCODE_COMMONCLICK) {
			new C_CommonClick().start(decrypt, _client);
		} else if (i == C_OPCODE_NEWCHAR) {
			new C_CreateChar().start(decrypt, _client);
		} else if (i == C_OPCODE_EXTCOMMAND) {
			new C_ExtraCommand().start(decrypt, _client);
		} else if (i == C_OPCODE_BOARDWRITE) {
			new C_BoardWrite().start(decrypt, _client);
		} else if (i == C_OPCODE_USEITEM) {
			new C_ItemUSe().start(decrypt, _client);
		} else if (i == C_OPCODE_CREATEPARTY) {
			new C_CreateParty().start(decrypt, _client);
		} else if (i == C_OPCODE_ENTERPORTAL) {
			new C_EnterPortal().start(decrypt, _client);
		} else if (i == C_GOTO_PORTAL) {
			new C_GotoPortal().start(decrypt, _client);
		} else if (i == C_GM_TELEPORT) {
			new C_GMTeleport().start(decrypt, _client);
		} else if (i == C_OPCODE_AMOUNT) {
			new C_Amount().start(decrypt, _client);
		} else if (i == C_OPCODE_FIX_WEAPON_LIST) {
			new C_FixWeaponList().start(decrypt, _client);
		} else if (i == C_OPCODE_SELECTLIST) {
			new C_SelectList().start(decrypt, _client);
		} else if (i == C_OPCODE_EXIT_GHOST) {
			new C_ExitGhost().start(decrypt, _client);
		} else if (i == C_OPCODE_CALL) {
			new C_CallPlayer().start(decrypt, _client);
		} else if (i == C_OPCODE_SELECTTARGET) {
			new C_SelectTarget().start(decrypt, _client);
		} else if (i == C_OPCODE_PETMENU) {
			new C_PetMenu().start(decrypt, _client);
		} else if (i == C_OPCODE_USEPETITEM) {
			new C_UsePetItem().start(decrypt, _client);
		} else if (i == C_OPCODE_FIGHT) {
			new C_Fight().start(decrypt, _client);
		} else if (i == C_OPCODE_MAIL) {
			new C_Mail().start(decrypt, _client);
		} else if (i == C_OPCODE_SHIP) {
			new C_Ship().start(decrypt, _client);
		} else if (i == C_OPCODE_RANK) {
			new C_Rank().start(decrypt, _client);
		} else if (i == C_OPCODE_TELEPORT) {
			new C_Teleport().start(decrypt, _client);
		} else if (i == C_OPCODE_TELEPORTLOCK) {
			new C_UnLock().start(decrypt, _client);
		} else if (i == C_OPCODE_KEEPALIVE) {
			new C_KeepALIVE().start(decrypt, _client);
		} else if (i == C_OPCODE_SENDLOCATION) {
			new C_Windows().start(decrypt, _client);
		} else if (i == C_OPCODE_BEANFUNLOGINPACKET) {//
			//new C_AutoLogin().start(decrypt, _client);
			if (Config.Bean_Fun) {
				new C_AuthLogin().start(decrypt, _client);
			} else {
				new C_AutoLogin().start(decrypt, _client);
			}
		} else if (i == C_OPCODE_FISHCLICK) {
			new C_FishClick().start(decrypt, _client);
		} else if (i == C_OPCODE_QUITGAME) {
			new C_Disconnect().start(decrypt, _client);
		} else if (i == C_OPCODE_FIRE_SMITH_P) {
			new C_PledgeContent().start(decrypt, _client);
		} else if (i == C_OPCODE_PLEDGE_WATCH) {//
			new C_PledgeWatch().start(decrypt, _client); // 血盟關注
		} else if (i == C_OPCODE_WAREHOUSELOCK) {
			new C_Password().start(decrypt, _client);
		} else if (i == C_OPCODE_EXTENDED_PROTOBUF) {
			new C_ProtoBuffers().start(decrypt, _client);
		} else if (i == C_OPCODE_ATTACKRUNING) { // XXX C_OPCODE_ATTACK_CONTINUE
			// 修正為 C_OPCODE_ATTACKRUNING
           new C_AttackHandler().start(decrypt, _client);
		} else if (i == C_OPCODE_CLIENT_READY) {
			new C_ClientReady().start(decrypt, _client);
	    } else {
			new C_Unkonwn().start(decrypt, _client);
		}
	}

	public PacketHandler(final ClientExecutor client) {
		_client = client;
	}

	/**
	 * 設置執行類
	 */
	public static void put(final Integer key, final ClientBasePacket value) {
		if (_opListClient.get(key) == null) {
			_opListClient.put(key, value);

		} else {
			if (!key.equals(-1)) {
				_log.error("重複標記的OPID: " + key + " " + value.getType());
			}
		}
	}

	/**
	 * 日版封包顯示
	 * 
	 * @param data
	 * @param len
	 * @return
	 */
	public String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(HexToDex(i, 4) + ": ");
			}
			result.append(HexToDex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80) {
						result.append((char) t1);
					} else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}
		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80) {
					result.append((char) t1);
				} else {
					result.append('.');
				}
			}
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * 日版封包顯示
	 * 
	 * @param data
	 * @param digits
	 * @return
	 */
    private String HexToDex(int data, int digits) {
    	String number = Integer.toHexString(data);
    	for (int i = number.length(); i < digits; i++)
    		number = "0" + number;
    	return number;
    }
}
