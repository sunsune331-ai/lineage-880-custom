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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.data.event.BaseResetSet;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.serverpackets.S_CharReset;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.utils.CalcStat;

/**
 * 處理收到客戶端傳來角色升級/出生的封包
 */
public class C_CharReset extends ClientBasePacket {

	private static final Log _log = LogFactory.getLog(C_CharReset.class);

	/**
	 * //配置完初期點數 按確定 127.0.0.1 Request Work ID : 120 0000: 78 01 0d 0a 0b 0a 12
	 * 0d
	 * 
	 * //提升10級 127.0.0.1 Request Work ID : 120 0000: 78 02 07 00 //提升1級
	 * 127.0.0.1 Request Work ID : 120 0000: 78 02 00 04
	 * 
	 * //提升完等級 127.0.0.1 Request Work ID : 120 0000: 78 02 08 00 x...
	 * 
	 * //萬能藥 127.0.0.1 Request Work ID : 120 0000: 78 03 23 0a 0b 17 12 0d
	 */

	@Override
	public void start(final byte[] decrypt, final ClientExecutor client) {
		try {
			// 資料載入
			this.read(decrypt);

			final L1PcInstance pc = client.getActiveChar();
			if (pc == null) {
				return;
			}

			final int stage = readC();

			if (stage == 0x01) { // 0x01:初始化人物數質
				final int str = readC();
				final int intel = readC();
				final int wis = readC();
				final int dex = readC();
				final int con = readC();
				final int cha = readC();

				// 檢查是否有異常能力值
				boolean isStatusError = false;

				final int originalStr = L1ClassFeature.ORIGINAL_STR[pc.getType()];
				final int originalDex = L1ClassFeature.ORIGINAL_DEX[pc.getType()];
				final int originalCon = L1ClassFeature.ORIGINAL_CON[pc.getType()];
				final int originalWis = L1ClassFeature.ORIGINAL_WIS[pc.getType()];
				final int originalCha = L1ClassFeature.ORIGINAL_CHA[pc.getType()];
				final int originalInt = L1ClassFeature.ORIGINAL_INT[pc.getType()];
				final int originalAmount = L1ClassFeature.ORIGINAL_AMOUNT[pc.getType()];

				if ((str < originalStr) || (dex < originalDex) || (con < originalCon) || (wis < originalWis)
						|| (cha < originalCha) || (intel < originalInt)
						|| (str > (originalStr + originalAmount)) || (dex > (originalDex + originalAmount))
						|| (con > (originalCon + originalAmount)) || (wis > (originalWis + originalAmount))
						|| (cha > (originalCha + originalAmount))
						|| (intel > (originalInt + originalAmount))) {
					isStatusError = true;
				}
				final int statusAmount = str + intel + wis + dex + con + cha;
				if ((statusAmount != 75) || (isStatusError)) {
					return;
				}

				int hp = 0;
				int mp = 0;
				if (BaseResetSet.RETAIN != 0) {// 保留血魔量的百分比
					hp = ((pc.getMaxHp() * BaseResetSet.RETAIN) / 100);
					mp = ((pc.getMaxMp() * BaseResetSet.RETAIN) / 100);

				} else {
					// hp = CalcInitHpMp.calcInitHp(pc);
					// mp = CalcInitHpMp.calcInitMp(pc);
					// XXX 7.6屬性 ADD
					hp = L1ClassFeature.calcInitHp(pc.getType());
					mp = L1ClassFeature.calcInitMp(pc.getType(), pc.getWis());
				}

				pc.sendPackets(new S_OwnCharStatus2(pc));

				/**
				 * 『來源:伺服器』<位址:64>{長度:8}(時間:1233793211) 0000: 40 04 00 00 04 01 8b
				 * df @....... 尚未知的封包
				 */
				pc.sendPackets(new S_CharReset(pc, 1, hp, mp, 10, str, intel, wis, dex, con, cha));
				initCharStatus(pc, hp, mp, str, intel, wis, dex, con, cha);
				CharacterTable.get();
				CharacterTable.saveCharStatus(pc);

			} else if (stage == 0x02) { // 0x02:等級分配
				final int type2 = readC();
				if (type2 == 0x00) { // 0x00:提升1級
					setLevelUp(pc, 1);
				} else if (type2 == 0x07) { // 0x07:提升10級
					if (pc.getTempMaxLevel() - pc.getTempLevel() < 10) {
						return;
					}
					if (pc.getTempLevel() >= 40) {
						return;
					}
					setLevelUp(pc, 10);
				} else if (type2 == 0x01) { // 提升1級(力量)
					pc.addBaseStr(1);
					setLevelUp(pc, 1);
				} else if (type2 == 0x02) { // 提升1級(智力)
					pc.addBaseInt(1);
					setLevelUp(pc, 1);
				} else if (type2 == 0x03) { // 提升1級(精神)
					pc.addBaseWis(1);
					setLevelUp(pc, 1);
				} else if (type2 == 0x04) { // 提升1級(敏捷)
					pc.addBaseDex(1);
					setLevelUp(pc, 1);
				} else if (type2 == 0x05) { // 提升1級(體質)
					pc.addBaseCon(1);
					setLevelUp(pc, 1);
				} else if (type2 == 0x06) { // 提升1級(魅力)
					pc.addBaseCha(1);
					setLevelUp(pc, 1);
				} else if (type2 == 0x08) { // 完成
					switch (readC()) {
					case 1:
						pc.addBaseStr(1);
						break;
					case 2:
						pc.addBaseInt(1);
						break;
					case 3:
						pc.addBaseWis(1);
						break;
					case 4:
						pc.addBaseDex(1);
						break;
					case 5:
						pc.addBaseCon(1);
						break;
					case 6:
						pc.addBaseCha(1);
						break;
					}
					if (pc.getElixirStats() > 0) {
						pc.sendPackets(new S_CharReset(pc.getElixirStats()));
						return;
					}
					saveNewCharStatus(pc);
				}
			} else if (stage == 0x03) { // 萬能藥可重置的點數分配
				int read1 = this.readC();
				int read2 = this.readC();
				int read3 = this.readC();
				int read4 = this.readC();
				int read5 = this.readC();
				int read6 = this.readC();
				pc.addBaseStr((byte) (read1 - pc.getBaseStr()));
				pc.addBaseInt((byte) (read2 - pc.getBaseInt()));
				pc.addBaseWis((byte) (read3 - pc.getBaseWis()));
				pc.addBaseDex((byte) (read4 - pc.getBaseDex()));
				pc.addBaseCon((byte) (read5 - pc.getBaseCon()));
				pc.addBaseCha((byte) (read6 - pc.getBaseCha()));
				saveNewCharStatus(pc);
			}

		} catch (final Exception e) {
			// _log.error(e.getLocalizedMessage(), e);

		} finally {
			this.over();
		}
	}

	private void saveNewCharStatus(final L1PcInstance pc) {
		pc.setInCharReset(false);

		if (pc.getOriginalAc() > 0) {
			pc.addAc(pc.getOriginalAc());
		}

		if (pc.getOriginalMr() > 0) {
			pc.addMr(0 - pc.getOriginalMr());
		}

		pc.refresh();
		pc.setCurrentHp(pc.getMaxHp());
		pc.setCurrentMp(pc.getMaxMp());
		if (pc.getTempMaxLevel() != pc.getLevel()) {
			pc.setLevel(pc.getTempMaxLevel());
			pc.setExp(ExpTable.getExpByLevel(pc.getTempMaxLevel()));
		}
		if (pc.getLevel() > 50) {
			pc.setBonusStats(pc.getLevel() - 50);
		} else {
			pc.setBonusStats(0);
		}
		pc.sendPackets(new S_OwnCharStatus(pc));

		// S_InitialAbilityGrowth AbilityGrowth = new S_InitialAbilityGrowth(pc);
		// pc.sendPackets(AbilityGrowth);

		final L1ItemInstance item = pc.getInventory().findItemId(49142); // 回憶蠟燭
		if (item != null) {
			try {
				pc.getInventory().removeItem(item, 1);
				pc.save(); // 儲存玩家的資料到資料庫中

			} catch (final Exception e) {
				_log.error(e.getLocalizedMessage(), e);
			}
		}
		// 重置後傳送地圖
		L1Teleport.teleport(pc, 32628, 32772, (short) 4, 4, true);
	}

	private void initCharStatus(final L1PcInstance pc, final int hp, final int mp, final int str, final int intel,
			final int wis, final int dex, final int con, final int cha) {
		pc.addBaseMaxHp((short) (hp - pc.getBaseMaxHp()));
		pc.addBaseMaxMp((short) (mp - pc.getBaseMaxMp()));
		pc.addBaseStr((byte) (str - pc.getBaseStr()));
		pc.addBaseInt((byte) (intel - pc.getBaseInt()));
		pc.addBaseWis((byte) (wis - pc.getBaseWis()));
		pc.addBaseDex((byte) (dex - pc.getBaseDex()));
		pc.addBaseCon((byte) (con - pc.getBaseCon()));
		pc.addBaseCha((byte) (cha - pc.getBaseCha()));
		pc.addMr(0 - pc.getMr());
		pc.addDmgup(0 - pc.getDmgup());
		pc.addHitup(0 - pc.getHitup());
	}

	private void setLevelUp(final L1PcInstance pc, final int addLv) {
		pc.setTempLevel(pc.getTempLevel() + addLv);
		for (int i = 0; i < addLv; i++) {
			/*final short randomHp = CalcStat.calcStatHp(pc.getType(), pc.getBaseMaxHp(), pc.getBaseCon(),
					pc.getOriginalHpup(),pc.getType());
			final short randomMp = CalcStat.calcStatMp(pc.getType(), pc.getBaseMaxMp(), pc.getBaseWis(),
					pc.getOriginalMpup());
			pc.addBaseMaxHp(randomHp);
			pc.addBaseMaxMp(randomMp);*/
			// XXX 7.6屬性 ADD
			final int randomHp = L1ClassFeature.calcStatHp(pc.getType(), pc.getBaseMaxHp(), (byte) pc.getBaseCon());
			final int randomMp = L1ClassFeature.calcStatMp(pc.getType(), pc.getBaseMaxMp(), (byte) pc.getBaseWis());
			pc.addBaseMaxHp((short) randomHp);
			pc.addBaseMaxMp((short) randomMp);
		}
		// final int newAc = CalcStat.calcAc(pc.getType(),pc.getLevel(),pc.getBaseDex());
		// XXX 7.6屬性 ADD
		final int newAc = 10 + CalcStat.calcAc(pc.getType(), pc.getLevel()) + L1ClassFeature.calcDexAc(pc.getBaseDex());
		pc.sendPackets(new S_CharReset(pc, pc.getTempLevel(), pc.getBaseMaxHp(), pc.getBaseMaxMp(), newAc,
				pc.getBaseStr(), pc.getBaseInt(), pc.getBaseWis(), pc.getBaseDex(), pc.getBaseCon(), pc.getBaseCha()));
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}

}
