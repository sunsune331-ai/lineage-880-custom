package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.GREATER_HASTE;
import static com.lineage.server.model.skill.L1SkillId.HASTE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_HASTE;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigOther;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.doll.Doll_Skill;
import com.lineage.server.model.doll.L1DollExecutor;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.serverpackets.S_NPCPack_Doll;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldItem;

public class L1DollInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;
	private static final Log _log = LogFactory.getLog(L1DollInstance.class);

	private static Random _random = new Random();
	private int _itemObjId;
	private boolean _power_doll = false;// 該娃娃具備輔助技能效果
	private L1Doll _type;
	
	private int _time = 0;

	private int _skillid = -1;

	private int _r = -1;

	private int _olX = 0;

	private int _olY = 0;

	/**
	 * 對像:魔法娃娃
	 * 
	 * @param template
	 * @param master
	 * @param dollType
	 * @param itemObjId
	 */
	public L1DollInstance(final L1Npc template, final L1PcInstance master,
			final int itemObjId, final L1Doll type, final boolean power_doll) {
		super(template);
		try {
			_power_doll = power_doll;
			setId(IdFactoryNpc.get().nextId());
			// 設置副本編號
			set_showId(master.get_showId());

			setItemObjId(itemObjId);

			_type = type;
			setGfxId(type.get_gfxid());
			setTempCharGfx(type.get_gfxid());
			setNameId(type.get_nameid());
			set_time(type.get_time());

			setMaster(master);
			setX(master.getX() + _random.nextInt(5) - 2);
			setY(master.getY() + _random.nextInt(5) - 2);
			setMap(master.getMapId());
			setHeading(5);
			setLightSize(template.getLightSize());

			World.get().storeObject(this);
			World.get().addVisibleObject(this);
			for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
				onPerceive(pc);
			}
			if (_power_doll) {
				master.add_power_doll(this);
			} else {
				master.addDoll(this);
			}

			L1PcInstance masterpc = null;
			if (_master instanceof L1PcInstance) {
				masterpc = (L1PcInstance) _master;
				// 設置能力
				if (!_type.getPowerList().isEmpty()) {
					for (L1DollExecutor p : _type.getPowerList()) {
						if (p instanceof Doll_Skill) {
							final Doll_Skill vv = (Doll_Skill) p;
							set_skill(vv.get_int()[0], vv.get_int()[1],
									vv.get_int()[2]);

						} else {
							p.setDoll(masterpc);
						}
					}
				}
				master.sendPackets(new S_PacketBox(S_PacketBox.DOLL, type
						.get_time()));
			}
			// TODO T1
			if (_type.get_Doll_Ac() != 0) {
				master.addAc(_type.get_Doll_Ac());
				master.sendPackets(new S_OwnCharAttrDef(master));
			}
			if (_type.get_Doll_Hit() != 0) {
				master.addHitup(_type.get_Doll_Hit());
			}
			if (_type.get_Doll_Dmg() != 0) {
				master.addDmgup(_type.get_Doll_Dmg());
			}
			if (_type.get_Doll_DmgBow() != 0) {
				master.addBowDmgup(_type.get_Doll_DmgBow());
			}
			if (_type.get_Doll_HitBow() != 0) {
				master.addBowHitup(_type.get_Doll_HitBow());
			}
			if (_type.get_Doll_Hp() != 0) {
				master.addMaxHp(_type.get_Doll_Hp());
				master.sendPackets(new S_HPUpdate(master.getCurrentHp(), master
						.getMaxHp()));
			}
			if (_type.get_Doll_Mp() != 0) {
				master.addMaxMp(_type.get_Doll_Mp());
				master.sendPackets(new S_MPUpdate(master.getCurrentMp(), master
						.getMaxMp()));
			}
			if (_type.get_Doll_Sp() != 0) {
				master.addSp(_type.get_Doll_Sp());
				master.sendPackets(new S_SPMR(master));
			}
			if (_type.get_Doll_Stat_Str() != 0) {
				master.addStr(_type.get_Doll_Stat_Str());
				master.sendPackets(new S_OwnCharStatus2(master));
			}
			if (_type.get_Doll_Stat_Con() != 0) {
				master.addCon(_type.get_Doll_Stat_Con());
				master.sendPackets(new S_OwnCharStatus2(master));
			}
			if (_type.get_Doll_Stat_Dex() != 0) {
				master.addDex(_type.get_Doll_Stat_Dex());
				master.sendPackets(new S_OwnCharStatus2(master));
			}
			if (_type.get_Doll_Stat_Int() != 0) {
				master.addInt(_type.get_Doll_Stat_Int());
				master.sendPackets(new S_OwnCharStatus2(master));
			}
			if (_type.get_Doll_Stat_Wis() != 0) {
				master.addWis(_type.get_Doll_Stat_Wis());
				master.sendPackets(new S_OwnCharStatus2(master));
			}
			if (_type.get_Doll_Stat_Cha() != 0) {
				master.addCha(_type.get_Doll_Stat_Cha());
				master.sendPackets(new S_OwnCharStatus2(master));
			}
			if (_type.get_Doll_Mr() != 0) {
				master.addMr(_type.get_Doll_Mr());
				master.sendPackets(new S_SPMR(master));
			}
			if (_type.get_Doll_DefenseWater() != 0) {
				master.addWater(_type.get_Doll_DefenseWater());
				master.sendPackets(new S_OwnCharAttrDef(master));
			}
			if (_type.get_Doll_DefenseWind() != 0) {
				master.addWind(_type.get_Doll_DefenseWind());
				master.sendPackets(new S_OwnCharAttrDef(master));
			}
			if (_type.get_Doll_DefenseFire() != 0) {
				master.addFire(_type.get_Doll_DefenseFire());
				master.sendPackets(new S_OwnCharAttrDef(master));
			}
			if (_type.get_Doll_DefenseEarth() != 0) {
				master.addEarth(_type.get_Doll_DefenseEarth());
				master.sendPackets(new S_OwnCharAttrDef(master));
			}
			// 耐性
			if (_type.get_Doll_Regist_Technology() != 0) {
				master.addRegistTechnology(_type.get_Doll_Regist_Technology());
			}
			if (_type.get_Doll_Regist_Elf() != 0) {
				master.addRegistElf(_type.get_Doll_Regist_Elf());
			}
			if (_type.get_Doll_Regist_Dragon() != 0) {
				master.addRegistDragon(_type.get_Doll_Regist_Dragon());
			}
			if (_type.get_Doll_Regist_Horror() != 0) {
				master.addRegistHorror(_type.get_Doll_Regist_Horror());
			}
			if (_type.get_Doll_Regist_All() != 0) {
				master.addRegistAll(_type.get_Doll_Regist_All());
			}
			// 命中
			if (_type.get_Doll_Hit_Technology() != 0) {
				master.setHitTechnology(_type.get_Doll_Hit_Technology());
			}
			if (_type.get_Doll_Hit_Elf() != 0) {
				master.setHitElf(_type.get_Doll_Hit_Elf());
			}
			if (_type.get_Doll_Hit_Dragon() != 0) {
				master.setHitDragon(_type.get_Doll_Hit_Dragon());
			}
			if (_type.get_Doll_Hit_Horror() != 0) {
				master.setHitHorror(_type.get_Doll_Hit_Horror());
			}
			if (_type.get_Doll_Hit_All() != 0) {
				master.setHitAll(_type.get_Doll_Hit_All());
			}
			if (_type.get_pvpdmg() != 0) {
				master.addPvpDmg(_type.get_pvpdmg());
			}
			if (_type.get_pvpjm() != 0) {
				master.addPvpDmg_R(_type.get_pvpjm());
			}
			if (_type.get_yshzf() != 0) {
				master.addEinhasadConsumeReduce(_type.get_yshzf());
			}
			// 娃娃登場
			broadcastPacketX10(new S_SkillSound(getId(), 5935));
			set_olX(getX());
			set_olY(getY());

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 對像:魔法娃娃(DE)
	 * 
	 * @param template
	 * @param master
	 * @param type
	 */
	public L1DollInstance(final L1Npc template, final L1NpcInstance master,
			final L1Doll type) {
		super(template);
		try {
			setId(IdFactoryNpc.get().nextId());
			// 設置副本編號
			set_showId(master.get_showId());

			_type = type;
			setGfxId(type.get_gfxid());
			setTempCharGfx(type.get_gfxid());
			setNameId(type.get_nameid());
			set_time(type.get_time());

			set_time(1800);

			setMaster(master);
			setX(master.getX() + _random.nextInt(5) - 2);
			setY(master.getY() + _random.nextInt(5) - 2);
			setMap(master.getMapId());
			setHeading(5);
			setLightSize(template.getLightSize());

			World.get().storeObject(this);
			World.get().addVisibleObject(this);
			for (final L1PcInstance pc : World.get().getRecognizePlayer(this)) {
				onPerceive(pc);
			}
			master.addDoll(this);

			// 娃娃登場
			broadcastPacketX10(new S_SkillSound(getId(), 5935));
			set_olX(getX());
			set_olY(getY());

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}


	/**
	 * 收回娃娃
	 */
	public void deleteDoll() {
		try {
			L1ItemInstance item = WorldItem.get().getItem(_itemObjId);// 找回物品資料
			//2016/6/21測試修正
			if(item!=null){
			item.stopEquipmentTimer(null);// 停止物品計時
			}
			
			
			broadcastPacketAll(new S_SkillSound(getId(), 5936));

			L1PcInstance masterpc = null;
			if (_master instanceof L1PcInstance) {
				masterpc = (L1PcInstance) _master;
				// 移除能力
				if (!_type.getPowerList().isEmpty()) {
					for (L1DollExecutor p : _type.getPowerList()) {
						p.removeDoll(masterpc);
					}
				}
				masterpc.sendPackets(new S_PacketBox(S_PacketBox.DOLL, 0));
			}
			// TODO 2
			if (_type.get_Doll_Ac() != 0) {
				masterpc.addAc(-_type.get_Doll_Ac());
				masterpc.sendPackets(new S_OwnCharAttrDef(masterpc));
			}
			if (_type.get_Doll_Hit() != 0) {
				masterpc.addHitup(-_type.get_Doll_Hit());
			}
			if (_type.get_Doll_Dmg() != 0) {
				masterpc.addDmgup(-_type.get_Doll_Dmg());
			}
			if (_type.get_Doll_DmgBow() != 0) {
				masterpc.addBowDmgup(-_type.get_Doll_DmgBow());
			}
			if (_type.get_Doll_HitBow() != 0) {
				masterpc.addBowHitup(-_type.get_Doll_HitBow());
			}
			if (_type.get_Doll_Hp() != 0) {
				masterpc.addMaxHp(-_type.get_Doll_Hp());
				masterpc.sendPackets(new S_HPUpdate(masterpc.getCurrentHp(),
						masterpc.getMaxHp()));
			}
			if (_type.get_Doll_Mp() != 0) {
				masterpc.addMaxMp(-_type.get_Doll_Mp());
				masterpc.sendPackets(new S_MPUpdate(masterpc.getCurrentMp(),
						masterpc.getMaxMp()));
			}
			if (_type.get_Doll_Sp() != 0) {
				masterpc.addSp(-_type.get_Doll_Sp());
				masterpc.sendPackets(new S_SPMR(masterpc));
			}
			if (_type.get_Doll_Stat_Str() != 0) {
				masterpc.addStr(-_type.get_Doll_Stat_Str());
				masterpc.sendPackets(new S_OwnCharStatus2(masterpc));
			}
			if (_type.get_Doll_Stat_Con() != 0) {
				masterpc.addCon(-_type.get_Doll_Stat_Con());
				masterpc.sendPackets(new S_OwnCharStatus2(masterpc));
			}
			if (_type.get_Doll_Stat_Dex() != 0) {
				masterpc.addDex(-_type.get_Doll_Stat_Dex());
				masterpc.sendPackets(new S_OwnCharStatus2(masterpc));
			}
			if (_type.get_Doll_Stat_Int() != 0) {
				masterpc.addInt(-_type.get_Doll_Stat_Int());
				masterpc.sendPackets(new S_OwnCharStatus2(masterpc));
			}
			if (_type.get_Doll_Stat_Wis() != 0) {
				masterpc.addWis(-_type.get_Doll_Stat_Wis());
				masterpc.sendPackets(new S_OwnCharStatus2(masterpc));
			}
			if (_type.get_Doll_Stat_Cha() != 0) {
				masterpc.addCha(-_type.get_Doll_Stat_Cha());
				masterpc.sendPackets(new S_OwnCharStatus2(masterpc));
			}
			if (_type.get_Doll_Mr() != 0) {
				masterpc.addMr(-_type.get_Doll_Mr());
				masterpc.sendPackets(new S_SPMR(masterpc));
			}
			if (_type.get_Doll_DefenseWater() != 0) {
				masterpc.addWater(-_type.get_Doll_DefenseWater());
				masterpc.sendPackets(new S_OwnCharAttrDef(masterpc));
			}
			if (_type.get_Doll_DefenseWind() != 0) {
				masterpc.addWind(-_type.get_Doll_DefenseWind());
				masterpc.sendPackets(new S_OwnCharAttrDef(masterpc));
			}
			if (_type.get_Doll_DefenseFire() != 0) {
				masterpc.addFire(-_type.get_Doll_DefenseFire());
				masterpc.sendPackets(new S_OwnCharAttrDef(masterpc));
			}
			if (_type.get_Doll_DefenseEarth() != 0) {
				masterpc.addEarth(-_type.get_Doll_DefenseEarth());
				masterpc.sendPackets(new S_OwnCharAttrDef(masterpc));
			}
			// 耐性
			if (_type.get_Doll_Regist_Technology() != 0) {
				masterpc.addRegistTechnology(-_type.get_Doll_Regist_Technology());
			}
			if (_type.get_Doll_Regist_Elf() != 0) {
				masterpc.addRegistElf(-_type.get_Doll_Regist_Elf());
			}
			if (_type.get_Doll_Regist_Dragon() != 0) {
				masterpc.addRegistDragon(-_type.get_Doll_Regist_Dragon());
			}
			if (_type.get_Doll_Regist_Horror() != 0) {
				masterpc.addRegistHorror(-_type.get_Doll_Regist_Horror());
			}
			if (_type.get_Doll_Regist_All() != 0) {
				masterpc.addRegistAll(-_type.get_Doll_Regist_All());
			}
			// 命中
			if (_type.get_Doll_Hit_Technology() != 0) {
				masterpc.setHitTechnology(-_type.get_Doll_Hit_Technology());
			}
			if (_type.get_Doll_Hit_Elf() != 0) {
				masterpc.setHitElf(-_type.get_Doll_Hit_Elf());
			}
			if (_type.get_Doll_Hit_Dragon() != 0) {
				masterpc.setHitDragon(-_type.get_Doll_Hit_Dragon());
			}
			if (_type.get_Doll_Hit_Horror() != 0) {
				masterpc.setHitHorror(-_type.get_Doll_Hit_Horror());
			}
			if (_type.get_Doll_Hit_All() != 0) {
				masterpc.setHitAll(-_type.get_Doll_Hit_All());
			}
			if (_type.get_pvpdmg() != 0) {
				masterpc.addPvpDmg(-_type.get_pvpdmg());
			}
			if (_type.get_pvpjm() != 0) {
				masterpc.addPvpDmg_R(-_type.get_pvpjm());
			}
			if (_type.get_yshzf() != 0) {
				masterpc.addEinhasadConsumeReduce(-_type.get_yshzf());
			}
			if (_power_doll) {
				_master.remove_power_doll();
			} else {
				_master.removeDoll(this);
			}

			deleteMe();

		} catch (final Exception e) {
			String msg = "玩家:" + _master.getName() + " 回收娃娃出錯:" + e.getMessage();
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 接觸資訊
	 */
	public void onPerceive(L1PcInstance perceivedFrom) {
		try {
			if (perceivedFrom.get_showId() != get_showId()) {
				return;
			}

			perceivedFrom.addKnownObject(this);

			perceivedFrom.sendPackets(new S_NPCPack_Doll(this, perceivedFrom));

			if (getBraveSpeed() > 0) {
				perceivedFrom.sendPackets(new S_SkillBrave(getId(), getBraveSpeed(), 600000));
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 跟隨主人變更移動/速度狀態
	 */
	public void setNpcMoveSpeed() {
		try {
			if (!ConfigOther.WAR_DOLL) {
				for (int castle_id = 1; castle_id < 8; castle_id++) {
					if (ServerWarExecutor.get().isNowWar(castle_id)) { // 攻城戰期間內
						if (L1CastleLocation.checkInWarArea(castle_id, this)) {
							deleteDoll();
							return;
						}
					}
				}
			}
			if ((_master != null) && (_master.isInvisble())) {
				deleteDoll();
				return;
			}

			if (_master.isDead()) {
				deleteDoll();
				return;
			}

			if (_master.getMoveSpeed() != this.getMoveSpeed()) {
				setMoveSpeed(_master.getMoveSpeed());
			}

			if (_master.getBraveSpeed() != this.getBraveSpeed()) {
				setBraveSpeed(_master.getBraveSpeed());
			}

			if ((_master != null) && (_master.getMapId() == getMapId())) {
				if (getLocation().getTileLineDistance(_master.getLocation()) > 2) {
					int dir = targetDirection(_master.getX(), _master.getY());

					for (L1Object object : World.get().getVisibleObjects(this, 1)) {
						if ((dir >= 0) && (dir <= 7)) {
							int locx = getX() + HEADING_TABLE_X[dir];
							int locy = getY() + HEADING_TABLE_Y[dir];
							if (((object instanceof L1DollInstance)) && (locx == object.getX())
									&& (locy == object.getY())) {
								dir++;
							}
						}
					}

					if ((dir >= 0) && (dir <= 7)) {
						setDirectionMoveSrc(dir);

						broadcastPacketAll(new S_MoveCharPacket(this));
					}
				}
			} else {
				deleteDoll();
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public int getItemObjId() {
		return _itemObjId;
	}

	public void setItemObjId(int i) {
		_itemObjId = i;
	}

	/**
	 * 傳回娃娃召喚剩餘時間
	 * 
	 * @return
	 */
	public int get_time() {
		return _time;
	}

	/**
	 * 設定娃娃召喚時間
	 * 
	 * @param time
	 */
	public void set_time(int time) {
		_time = time;
	}

	/**
	 * 執行娃娃施放技能
	 * @param target 被攻擊者
	 * @param calculated 是否已預先計算過機率
	 */
	public void startDollSkill(L1Character target, boolean calculated) {
		try {
			boolean start = false;// 施展技能
			if (_skillid != -1) {
				L1Skills skill = SkillsTable.get().getTemplate(_skillid);
				int castgfx = skill.getCastGfx();

				if (!calculated && _random.nextInt(100) <= _r) {
					start = true;
				} else if (calculated) {
					start = true;
				}

				if (start) {// 施展技能
					switch (_skillid) {
					case 4:// 光箭 1100
					case 6:// 冰箭 1100
					case 7:// 風刃 1100
					case 10:// 寒冷戰慄 1100
					case 15:// 火箭 1300
					case 16:// 地獄之牙 1300
					case 17:// 極光雷電 1500
					case 18:// 起死回生術 1500
					case 22:// 寒冰氣息 1300
					case 25:// 燃燒的火球 1600
					case 28:// 吸血鬼之吻 1250
					case 30:// 巖牢 1600
					case 34:// 極道落雷 1250
					case 38:// 冰錐 1100
					case 45:// 地裂術 1100
					case 46:// 烈炎術 1650
					case 50:// 冰矛圍籬 1600
					case 58:// 火牢 1100
					case 65:// 雷霆風暴 3000
					case 74:// 流星雨 4000
					case 77:// 究極光裂術 6000
					case 189:// 岩漿之箭
					case 192:// 奪命之雷 5150
					case 203:// 粉碎能量 3100
					case 207:// 心靈破壞 4050
						// broadcastPacketAll(new S_DoActionGFX(getId(), 67));
						L1Magic magic = new L1Magic(_master, target);
						int magic_dmg = magic.calcMagicDamage(_skillid);
						magic.commit(magic_dmg, 0);

						target.broadcastPacketAll(new S_SkillSound(target.getId(), castgfx));
						if (target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) target;
							pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
						}
						break;
					case 29:// 緩速術 1600
						switch (target.getMoveSpeed()) {
						case 0:
							if (target instanceof L1PcInstance) {
								final L1PcInstance pc = (L1PcInstance) target;
								pc.sendPackets(new S_SkillHaste(pc.getId(), 2, skill.getBuffDuration()));
							}
							target.broadcastPacketAll(new S_SkillHaste(target.getId(), 2, skill.getBuffDuration()));
							target.setMoveSpeed(2);
							break;

						case 1:
							int skillNum = 0;
							if (target.hasSkillEffect(HASTE)) {
								skillNum = HASTE;

							} else if (target.hasSkillEffect(GREATER_HASTE)) {
								skillNum = GREATER_HASTE;

							} else if (target.hasSkillEffect(STATUS_HASTE)) {
								skillNum = STATUS_HASTE;
							}

							if (skillNum != 0) {
								target.removeSkillEffect(skillNum);
								target.setMoveSpeed(0);
							}
							break;
						}
						break;
					case 11:// 毒咒 1650
						L1SkillUse skillUse = new L1SkillUse();
						skillUse.handleCommands(null, _skillid, target.getId(), target.getX(), target.getX(), 0,
								L1SkillUse.TYPE_GMBUFF, this);
						break;
					case 5000:// 火焰衝擊
						if (target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) target;
							pc.sendPacketsAll(new S_SkillSound(pc.getId(), castgfx));
							pc.receiveDamage(_master, 80, true, true);
						} else if (target instanceof L1NpcInstance) {
							L1NpcInstance npc = (L1NpcInstance) target;
							npc.broadcastPacketAll(new S_SkillSound(npc.getId(), castgfx));
							npc.receiveDamage(_master, 80);
						}
						break;
					case 5001:// 爆擊火焰衝擊
						if (target instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) target;
							pc.sendPacketsAll(new S_SkillSound(pc.getId(), castgfx));
							pc.receiveDamage(_master, 160, true, true);
						} else if (target instanceof L1NpcInstance) {
							L1NpcInstance npc = (L1NpcInstance) target;
							npc.broadcastPacketAll(new S_SkillSound(npc.getId(), castgfx));
							npc.receiveDamage(_master, 160);
						}
						break;
					case 5002:// 死騎娃娃-地獄火
						if (!calculated) {// 沒有預先計算過機率(物理攻擊)
							L1Magic magic2 = new L1Magic(_master, target);
							int magic_dmg2 = magic2.calcMagicDamage(_skillid);
							magic2.commit(magic_dmg2, 0);
							this.broadcastPacketAll(new S_SkillSound(this.getId(), castgfx));// 發送地獄火動畫
						}
						break;
					case 5003:// 獨眼巨人娃娃-巖牢
						if (!calculated) {// 沒有預先計算過機率(物理攻擊)
							L1Magic magic2 = new L1Magic(_master, target);
							int magic_dmg2 = magic2.calcMagicDamage(_skillid);
							magic2.commit(magic_dmg2, 0);
							//this.broadcastPacketAll(new S_SkillSound(this.getId(), castgfx));// 發送巖牢動畫
							if (target instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) target;
								pc.sendPacketsAll(new S_SkillSound(pc.getId(), castgfx));
								//pc.receiveDamage(_master, magic_dmg2, true, true);
							} else if (target instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) target;
								npc.broadcastPacketAll(new S_SkillSound(npc.getId(), castgfx));
								//npc.receiveDamage(_master, magic_dmg2);
							}
						}
						break;	
						default:
							//測試娃娃有害魔法使用
							skillUse = new L1SkillUse();
							skillUse.handleCommands(null, _skillid, target.getId(), target.getX(), target.getX(), 0,
									L1SkillUse.TYPE_GMBUFF, this);
							break;
					}
				}
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public int _gif = -1;// 值3
	
	public void set_skill(int int1, int int2, int int3) {
		_skillid = int1;// 值1
		if (_skillid != -1) {
			this.setLevel(_master.getLevel());
			this.setInt(_master.getInt());
		}
		_r = int2;// 值2
		_gif = int3;
	}

	public int get_skillid() {
		return _skillid;
	}

	public int get_skillrandom() {
		return _r;
	}

	public void set_olX(int x) {
		_olX = x;
	}

	public int get_olX() {
		return _olX;
	}

	public void set_olY(int y) {
		_olY = y;
	}

	public int get_olY() {
		return _olY;
	}

	// 輔助類型娃娃
	public void startDollSkill() {
		if (!_type.getPowerList().isEmpty()) {
			// int i = 0;
			if (_master instanceof L1PcInstance) {
				final L1PcInstance masterpc = (L1PcInstance) _master;
				for (L1DollExecutor p : _type.getPowerList()) {
					if (p.is_reset()) {
						p.setDoll(masterpc);
					}
				}
			}
		}
	}

	/**
	 * 觸發娃娃特效動畫
	 * 
	 * @param i
	 */
	public void show_action(int i) {
		if (!_type.getPowerList().isEmpty()) {
			if (i == 1) {
				// broadcastPacketAll(new S_DoActionGFX(getId(), 67));
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPacketsAll(new S_SkillSound(_master.getId(), 6319));
				}
			}
			if (i == 2) {
				// broadcastPacketAll(new S_DoActionGFX(getId(), 67));
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPacketsAll(new S_SkillSound(_master.getId(), 6320));
				}
			}
			if (i == 3) {
				// broadcastPacketAll(new S_DoActionGFX(getId(), 67));
				if (_master instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _master;
					pc.sendPacketsAll(new S_SkillSound(_master.getId(), 6321));
				}
			}
		}
	}

	/** 具有輔助技能 */
	public boolean is_power_doll() {
		return _power_doll;
	}
}

