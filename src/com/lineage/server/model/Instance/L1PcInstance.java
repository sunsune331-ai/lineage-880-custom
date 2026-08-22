package com.lineage.server.model.Instance;

import static com.lineage.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static com.lineage.server.model.skill.L1SkillId.AQUA_PROTECTER;
import static com.lineage.server.model.skill.L1SkillId.BLOODLUST;
import static com.lineage.server.model.skill.L1SkillId.DECREASE_WEIGHT;
import static com.lineage.server.model.skill.L1SkillId.DRESS_EVASION;
import static com.lineage.server.model.skill.L1SkillId.ELVEN_GRAVITY;
import static com.lineage.server.model.skill.L1SkillId.EXOTIC_VITALIZE;
import static com.lineage.server.model.skill.L1SkillId.FIRE_BLESS;
import static com.lineage.server.model.skill.L1SkillId.FOCUS_WAVE;
import static com.lineage.server.model.skill.L1SkillId.GMSTATUS_HPBAR;
import static com.lineage.server.model.skill.L1SkillId.GMSTATUS_HPBAR_PC;
import static com.lineage.server.model.skill.L1SkillId.GREATER_HASTE;
import static com.lineage.server.model.skill.L1SkillId.HASTE;
import static com.lineage.server.model.skill.L1SkillId.HOLY_WALK;
import static com.lineage.server.model.skill.L1SkillId.HURRICANE;
import static com.lineage.server.model.skill.L1SkillId.INVISIBILITY;
import static com.lineage.server.model.skill.L1SkillId.IMMUNE_TO_HARM;
import static com.lineage.server.model.skill.L1SkillId.JOY_OF_PAIN;
import static com.lineage.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static com.lineage.server.model.skill.L1SkillId.BLIND_HIDING;
import static com.lineage.server.model.skill.L1SkillId.MORTAL_BODY;
import static com.lineage.server.model.skill.L1SkillId.MOVING_ACCELERATION;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_ARMORGARDE;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_CRASH;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_FURY;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_SLAYER;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_TITANBULLET;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_TITANMAGIC;
import static com.lineage.server.model.skill.L1SkillId.PASSIVE_TITANROCK;
import static com.lineage.server.model.skill.L1SkillId.SAND_STORM;
import static com.lineage.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static com.lineage.server.model.skill.L1SkillId.SOLID_CARRIAGE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE2;
import static com.lineage.server.model.skill.L1SkillId.STATUS_BRAVE3;
import static com.lineage.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_HASTE;
import static com.lineage.server.model.skill.L1SkillId.STATUS_RIBRAVE;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.add.L1PcUnlock;
import com.add.BigHot.L1BigHotSpList;
import com.add.MJBookQuestSystem.UserMonsterBook;
import com.add.MJBookQuestSystem.UserWeekQuest;
import com.add.MJBookQuestSystem.Loader.UserMonsterBookLoader;
import com.add.MJBookQuestSystem.Loader.UserWeekQuestLoader;
import com.add.Mobbling.L1MobSpList;
import com.eric.gui.J_Main;
import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigKill;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.config.ConfigRecord;
import com.lineage.config.ConfigSkill;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.event.CampSet;
import com.lineage.data.event.EffectAISet;
import com.lineage.data.event.JISHIJIANLI;
import com.lineage.data.event.LeavesSet;
import com.lineage.data.event.OnlineGiftSet;
import com.lineage.data.event.ProtectorSet;
import com.lineage.data.quest.Chapter01R;
import com.lineage.echo.ClientExecutor;
import com.lineage.echo.EncryptExecutor;
import com.lineage.server.ActionCodes;
import com.lineage.server.CheckFightTimeController;
import com.lineage.server.Controller.FishingTimeController;
import com.lineage.server.clientpackets.AcceleratorChecker;
import com.lineage.server.datatables.ArmorKitPowerTable;
import com.lineage.server.datatables.C1_Name_Type_Table;
import com.lineage.server.datatables.CharApprenticeTable;
import com.lineage.server.datatables.CharacterAttendTable;
import com.lineage.server.datatables.CharacterAttendTable.UseAttendTemp;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.ExtraItemStealTable;
import com.lineage.server.datatables.ExtraMeteAbilityTable;
import com.lineage.server.datatables.ExtraPolyPowerTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.MapLevelTable;
import com.lineage.server.datatables.MapTimeTable;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.datatables.QuestNewTable;
import com.lineage.server.datatables.ServerAIEffectTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.VipSetsTable;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.lock.CharMapTimeReading;
import com.lineage.server.datatables.lock.CharOtherReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1ActionPc;
import com.lineage.server.model.L1ActionPet;
import com.lineage.server.model.L1ActionSummon;
import com.lineage.server.model.L1Apprentice;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackNpc;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1ChatParty;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1DeathMatch;
import com.lineage.server.model.L1DwarfForChaInventory;
import com.lineage.server.model.L1DwarfForElfInventory;
import com.lineage.server.model.L1DwarfForGameMallInventry;
import com.lineage.server.model.L1DwarfInventory;
import com.lineage.server.model.L1EquipmentSlot;
import com.lineage.server.model.L1HateList;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Karma;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PandoraInventory;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.L1PinkName;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.L1War;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.model.monitor.L1PcAtkMonitor;
import com.lineage.server.model.monitor.L1PcDetailsMonitor;
import com.lineage.server.model.monitor.L1PcInvisDelay;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ACTION_UI;
import com.lineage.server.serverpackets.S_AllChannelsChat;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_DelSkill;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_DoActionShop;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_Fishing;
import com.lineage.server.serverpackets.S_GameMallItemMoney;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_Invis;
import com.lineage.server.serverpackets.S_Karma;
import com.lineage.server.serverpackets.S_KillMessage;
import com.lineage.server.serverpackets.S_Lawful;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxCharEr;
import com.lineage.server.serverpackets.S_PacketBoxExp;
import com.lineage.server.serverpackets.S_PacketBoxProtection;
import com.lineage.server.serverpackets.S_PinkName;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_Resurrection;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.S_VipShow;
import com.lineage.server.serverpackets.S_VipTime;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.serverpackets.ability.S_BaseAbility;
import com.lineage.server.serverpackets.ability.S_BaseAbilityDetails;
import com.lineage.server.serverpackets.ability.S_ConDetails;
import com.lineage.server.serverpackets.ability.S_DexDetails;
import com.lineage.server.serverpackets.ability.S_ElixirCount;
import com.lineage.server.serverpackets.ability.S_IntDetails;
import com.lineage.server.serverpackets.ability.S_StrDetails;
import com.lineage.server.serverpackets.ability.S_WeightStatus;
import com.lineage.server.serverpackets.ability.S_WisDetails;
import com.lineage.server.templates.L1ArmorKitPower;
import com.lineage.server.templates.L1BookMark;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ItemPower_text;
import com.lineage.server.templates.L1ItemSteal;
import com.lineage.server.templates.L1MeteAbility;
import com.lineage.server.templates.L1Name_Power;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.templates.L1PcOtherList;
import com.lineage.server.templates.L1PolyPower;
import com.lineage.server.templates.L1PrivateShopBuyList;
import com.lineage.server.templates.L1PrivateShopSellList;
import com.lineage.server.templates.L1QuestNew;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.templates.L1SystemMessage;
import com.lineage.server.templates.L1User_Power;
import com.lineage.server.templates.L1Vip;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.pc.MapTimerThread;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.CalcStat;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.utils.DoubleUtil;
import com.lineage.server.utils.RandomArrayList;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldQuest;
import com.lineage.server.world.WorldWar;

import william.SustainEffect;
import william.L1WilliamLimitedReward;
import william.Reward;

public class L1PcInstance extends L1Character { // src015
	private static final Log _log = LogFactory.getLog(L1PcInstance.class);
	private static final long serialVersionUID = 1L;
	public static final int CLASSID_KNIGHT_MALE = 61;
	public static final int CLASSID_KNIGHT_FEMALE = 48;
	public static final int CLASSID_ELF_MALE = 138;
	public static final int CLASSID_ELF_FEMALE = 37;
	public static final int CLASSID_WIZARD_MALE = 734;
	public static final int CLASSID_WIZARD_FEMALE = 1186;
	public static final int CLASSID_DARK_ELF_MALE = 2786;
	public static final int CLASSID_DARK_ELF_FEMALE = 2796;
	public static final int CLASSID_PRINCE = 0;
	public static final int CLASSID_PRINCESS = 1;
	public static final int CLASSID_DRAGON_KNIGHT_MALE = 6658;
	public static final int CLASSID_DRAGON_KNIGHT_FEMALE = 6661;
	public static final int CLASSID_ILLUSIONIST_MALE = 6671;
	public static final int CLASSID_ILLUSIONIST_FEMALE = 6650;
	public static final int CLASSID_WARRIOR_MALE = 12490;
	public static final int CLASSID_WARRIOR_FEMALE = 12494;
	/** vip 保護經驗 */
	private boolean _death_exp = false;

	/** vip 保護物品 */
	private boolean _death_item = false;

	/** vip 保護技能 */
	private boolean _death_skill = false;

	/** vip 保護積分 */
	private boolean _death_score = false;
	private static Random _random = new Random();
	private final Map<Integer, L1SkinInstance> _skins = new HashMap<Integer, L1SkinInstance>();
	private boolean _isKill = false;
	private boolean isTripleArrow = false;
	private short _hpr = 0;

	private short _trueHpr = 0;

	private short _mpr = 0;
	private short _trueMpr = 0;

	public short _originalHpr = 0;

	public short _originalMpr = 0;
	private boolean _mpRegenActive;
	private boolean _mpReductionActiveByAwake;
	private boolean _hpRegenActive;
	private int _hpRegenType = 0;
	private int _hpRegenState = 4;

	private int _mpRegenType = 0;
	private int _mpRegenState = 4;
	public static final int REGENSTATE_NONE = 4;
	public static final int REGENSTATE_MOVE = 2;
	public static final int REGENSTATE_ATTACK = 1;
	public static final int INTERVAL_BY_AWAKE = 4;
	private int _awakeMprTime = 0;

	private int _awakeSkillId = 0;
	private int _old_lawful;
	private int _old_karma;
	private boolean _jl1 = false;
	private boolean _jl2 = false;
	private boolean _jl3 = false;
	private boolean _el1 = false;
	private boolean _el2 = false;
	private boolean _el3 = false;
	private long _old_exp;
	private boolean _isCHAOTIC = false;
	private boolean _isShow_Open_PandoraMsg = true; // 抽抽樂
	private final L1PandoraInventory _pandora = new L1PandoraInventory(this);

	// 可用技能編號列表
	private ArrayList<Integer> _skillList = new ArrayList<Integer>();

	private L1ClassFeature _classFeature = null;
	private int _PKcount;
	private int _PkCountForElf;
	private int _clanid;
	private String clanname;
	private int _clanRank;
	private byte _sex;
	private ArrayList<L1PrivateShopSellList> _sellList = new ArrayList<L1PrivateShopSellList>();

	private ArrayList<L1PrivateShopBuyList> _buyList = new ArrayList<L1PrivateShopBuyList>();
	private byte[] _shopChat;
	private boolean _isPrivateShop = false;

	private boolean _isTradingInPrivateShop = false;

	private int _partnersPrivateShopItemCount = 0;
	private EncryptExecutor _out;
	private static boolean _debug = Config.DEBUG;

	public long _oldTime = 0L;

	private static final Map<Long, Double> _magicDamagerList = new HashMap<Long, Double>();

	private int _originalEr = 0;

	private ClientExecutor _netConnection = null;
	private int _classId;
	private int _type;
	private long _exp;
	private final L1Karma _karma = new L1Karma();
	private boolean _gm;
	private boolean _monitor;
	private boolean _gmInvis;
	private short _accessLevel;
	private int _currentWeapon;
	private final L1PcInventory _inventory;
	private final L1DwarfInventory _dwarf;
	private final L1DwarfForChaInventory _dwarfForCha;
	private final L1DwarfForElfInventory _dwarfForElf;
	private final L1DwarfForGameMallInventry _dwarfForMALL;
	private L1ItemInstance _weapon;
	private L1Party _party;
	private L1ChatParty _chatParty;
	private int _partyID;
	private int _tradeID;
	private boolean _tradeOk;
	private int _tempID;
	private boolean _isTeleport = false;
	private boolean _isDrink = false;
	private boolean _isGres = false;
	private boolean _isPinkName = false;
	private L1PcQuest _quest;
	private L1ActionPc _action;
	private L1ActionPet _actionPet;
	private L1ActionSummon _actionSummon;
	public short _temp;
	private L1EquipmentSlot _equipSlot;
	private String _accountName;

	private short _baseMaxHp = 0;

	private short _baseMaxMp = 0;
	public short _baseMaxMpc;
	private int _baseAc = 0;

	private int _originalAc = 0;

	private int _baseStr = 0;

	private int _baseCon = 0;

	private int _baseDex = 0;

	private int _baseCha = 0;

	private int _baseInt = 0;

	private int _baseWis = 0;

	private int _originalStr = 0;

	private int _originalCon = 0;

	private int _originalDex = 0;

	private int _originalCha = 0;

	private int _originalInt = 0;

	private int _originalWis = 0;

	private int _originalDmgup = 0;

	private int _originalBowDmgup = 0;

	private int _originalHitup = 0;

	private int _originalBowHitup = 0;

	private int _originalMr = 0;

	private int _originalMagicCritical = 0;

	private int _originalMagicConsumeReduction = 0;

	private int _originalHpup = 0;

	private int _originalMpup = 0;

	private int _baseDmgup = 0;

	private int _baseBowDmgup = 0;

	private int _baseHitup = 0;

	private int _baseBowHitup = 0;

	private int _baseMr = 0;
	private int _advenHp;
	private int _advenMp;
	private int _highLevel;
	private int _bonusStats;
	private int _otherStats;
	private int _addPoint;
	private int _delPoint;
	private int _elixirStats;
	private int _elfAttr;
	private int _expRes;
	private int _partnerId;
	private int _onlineStatus;
	private int _homeTownId;
	private int _contribution;
	private int _hellTime;
	private boolean _banned;
	private int _food;
	private int invisDelayCounter = 0;

	private Object _invisTimerMonitor = new Object();
	private static final long DELAY_INVIS = 3000L;
	private boolean _ghost = false;

	private int _ghostTime = -1;

	private boolean _ghostCanTalk = true;

	private boolean _isReserveGhost = false;

	private int _ghostSaveLocX = 0;
	private int _ghostSaveLocY = 0;
	private short _ghostSaveMapId = 0;
	private int _ghostSaveHeading = 0;
	private Timestamp _lastPk;
	private Timestamp _lastPkForElf;
	private Timestamp _deleteTime;
	private double _weightUP = 1.0D;

	private int _weightReduction = 0;

	private int _originalStrWeightReduction = 0;

	private int _originalConWeightReduction = 0;

	private int _hasteItemEquipped = 0;

	private int _damageReductionByArmor = 0;

	private int _hitModifierByArmor = 0;

	private int _dmgModifierByArmor = 0;

	private int _bowHitModifierByArmor = 0;

	private int _bowDmgModifierByArmor = 0;
	private boolean _gresValid;

	private int _cookingId = 0;

	private int _dessertId = 0;
	
	private int _teleportX = 0;

	private int _teleportY = 0;

	private short _teleportMapId = 0;

	private int _teleportHeading = 0;
	private int _tempCharGfxAtDead;
	private boolean _isCanWhisper = true;

	private boolean _isShowTradeChat = true;

	private boolean _isShowWorldChat = true;
	private int _fightId;
	private byte _chatCount = 0;

	private long _oldChatTimeInMillis = 0L;
	private int _callClanId;
	private int _callClanHeading;
	private boolean _isInCharReset = false;

	private int _tempLevel = 1;

	private int _tempMaxLevel = 1;

	private boolean _isSummonMonster = false;

	private boolean _isShapeChange = false;
	private String _text;
	private byte[] _textByte = null;
	private L1PcOther _other;
	private L1PcOtherList _otherList;
	private int _oleLocX;
	private int _oleLocY;
	private L1Character _target = null;

	private L1DeInstance _outChat = null;
	private long _h_time;
	private boolean _mazu = false;

	private int _mazu_time = 0;
	private int _int1;
	private int _int2;
	private int _evasion;
	private double _expadd = 0.0D;
	private int _dd1;
	private int _dd2;
	private boolean _isFoeSlayer = false;
	private int _weaknss;
	private long _weaknss_t;
	private int _actionId = -1;
	private Chapter01R _hardin;
	private final Map<Integer, L1ItemPower_text> _allpowers = new ConcurrentHashMap<Integer, L1ItemPower_text>();
	private int _unfreezingTime;
	private int _misslocTime;
	private L1User_Power _c_power;
	private int _dice_hp;
	private int _sucking_hp;
	private int _dice_mp;
	private int _sucking_mp;
	private int _double_dmg;
	private int _lift;
	private int _magic_modifier_dmg = 0;

	private int _magic_reduction_dmg = 0;

	private boolean _rname = false;

	private boolean _retitle = false;

	private int _repass = 0;

	//private ArrayList<L1TradeItem> _trade_items = new ArrayList<L1TradeItem>();

	private int _mode_id = 0;

	private boolean _check_item = false;

	private boolean _vip_1 = false;

	private boolean _vip_2 = false;

	private boolean _vip_3 = false;

	private boolean _vip_4 = false;

	private long _global_time = 0L;

	private int _doll_hpr = 0;

	private int _doll_hpr_time = 0;

	private int _doll_hpr_time_src = 0;

	private int _doll_mpr = 0;

	private int _doll_mpr_time = 0;

	private int _doll_mpr_time_src = 0;

	private int[] _doll_get = new int[2];

	private int _doll_get_time = 0;

	private int _doll_get_time_src = 0;
	private String _board_title;
	private String _board_content;
	private long _spr_move_time = 0L;

	private long _spr_attack_time = 0L;

	private long _spr_skill_time = 0L;

	private int _delete_time = 0;

	private int _up_hp_potion = 0;// 增加藥水回復量%
	private int _uhp_number;// 增加藥水回復指定量

	int _venom_resist = 0;

	//private AcceleratorChecker _speed = null;

	private int _arena = 0;

	private int _temp_adena = 0;

	private long _ss_time = 0L;

	private int _ss = 0;
	private int killCount;
	private int _meteLevel;
	private L1MeteAbility _meteAbility;
	private boolean _isProtector;
	private boolean _isGetPolyPower;
	private boolean _isMars;
	private L1Apprentice _apprentice;
	private int _tempType;
	private Timestamp _punishTime;
	private int _magicDmgModifier;
	private int _magicDmgReduction;
	// 奪魂T
	private int _soulHp_r = 0;
	private int _soulHp_hpmin = 0;
	private int _soulHp_hpmax = 0;

	// 水龍甲
	private int _elitePlateMail_Fafurion = 0;
	private int _fafurion_hpmin = 0;
	private int _fafurion_hpmax = 0;

	// 風龍甲
	private int _elitePlateMail_Lindvior;
	private int _lindvior_mpmin;
	private int _lindvior_mpmax;

	// 火龍甲
	private int _elitePlateMail_Valakas;
	private int _valakas_dmgmin;
	private int _valakas_dmgmax;

	// 黑帝斯斗篷
	private int _hades_cloak;
	private int _hades_cloak_dmgmin;
	private int _hades_cloak_dmgmax;

	// 死亡騎士脛甲
	private int _death_pant;
	private int _death_pant_dmgmin;
	private int _death_pant_dmgmax;

	// 六芒星魔法符
	private int _Hexagram_Magic_Rune;
	private int _hexagram_hpmin;
	private int _hexagram_hpmax;
	private int _hexagram_gfx;

	// 蒂蜜特的祝福
	private int _dimiter_mpr_rnd;
	private int _dimiter_mpmin;
	private int _dimiter_mpmax;
	private int _dimiter_bless;
	private int _dimiter_time;

	private int _expPoint;
	private int _pay;
	private int _SummonId = 0;

	private L1PolyPower _polyPower;

	private int _lap = 1;

	private int _lapCheck = 0;

	private boolean _order_list = false;
	private static Timer _regenTimer = new Timer(true);

	public void load_src() {
		_old_exp = getExp();
		_old_lawful = getLawful();
		_old_karma = getKarma();
	}

	public boolean is_isKill() {
		return _isKill;
	}

	public void set_isKill(boolean _isKill) {
		this._isKill = _isKill;
	}

	public short getHpr() {
		return _hpr;
	}

	public void addHpr(int i) {
		_trueHpr = ((short) (_trueHpr + i));
		_hpr = ((short) Math.max(0, _trueHpr));
	}

	public short getMpr() {
		return _mpr;
	}

	public void addMpr(int i) {
		_trueMpr = ((short) (_trueMpr + i));
		_mpr = ((short) Math.max(0, _trueMpr));
	}

	public short getOriginalHpr() {
		return _originalHpr;
	}

	public short getOriginalMpr() {
		return _originalMpr;
	}

	public int getHpRegenState() {
		return _hpRegenState;
	}

	public void set_hpRegenType(int hpRegenType) {
		_hpRegenType = hpRegenType;
	}

	public int hpRegenType() {
		return _hpRegenType;
	}

	private int regenMax() {
		int[] lvlTable = { 30, 25, 20, 16, 14, 12, 11, 10, 9, 3, 2 };

		int regenLvl = Math.min(10, getLevel());
		if ((30 <= getLevel()) && (isKnight())) {
			regenLvl = 11;
		}
		return lvlTable[(regenLvl - 1)] << 2;
	}

	public boolean isRegenHp() {
		if (_temp != 0) {
			_accessLevel = _temp;
		}
		if (!_hpRegenActive) {
			return false;
		}
		if ((hasSkillEffect(EXOTIC_VITALIZE)) || (hasSkillEffect(ADDITIONAL_FIRE))) {
			return _hpRegenType >= regenMax();
		}
		if (120 <= _inventory.getWeight240()) {
			return false;
		}
		if (_food < 3) {
			return false;
		}
		return _hpRegenType >= regenMax();
	}

	public int getMpRegenState() {
		return _mpRegenState;
	}

	public void set_mpRegenType(int hpmpRegenType) {
		_mpRegenType = hpmpRegenType;
	}

	public int mpRegenType() {
		return _mpRegenType;
	}

	public boolean isRegenMp() {
		if (!_mpRegenActive) {
			return false;
		}
		if ((hasSkillEffect(EXOTIC_VITALIZE)) || (hasSkillEffect(ADDITIONAL_FIRE))) {
			return _mpRegenType >= 64;
		}
		if (120 <= _inventory.getWeight240()) {
			return false;
		}
		if (_food < 3) {
			return false;
		}

		return _mpRegenType >= 64;
	}

	public void setRegenState(int state) {
		_mpRegenState = state;
		_hpRegenState = state;
	}

	public void startHpRegeneration() {
		if (!_hpRegenActive)
			_hpRegenActive = true;
	}

	public void stopHpRegeneration() {
		if (_hpRegenActive)
			_hpRegenActive = false;
	}

	public boolean getHpRegeneration() {
		return _hpRegenActive;
	}

	public void startMpRegeneration() {
		if (!_mpRegenActive)
			_mpRegenActive = true;
	}

	public void stopMpRegeneration() {
		if (_mpRegenActive)
			_mpRegenActive = false;
	}

	public boolean getMpRegeneration() {
		return _mpRegenActive;
	}

	public int get_awakeMprTime() {
		return _awakeMprTime;
	}

	public void set_awakeMprTime(int awakeMprTime) {
		_awakeMprTime = awakeMprTime;
	}

	public void startMpReductionByAwake() {
		if (!_mpReductionActiveByAwake) {
			set_awakeMprTime(4);
			_mpReductionActiveByAwake = true;
		}
	}

	public void stopMpReductionByAwake() {
		if (_mpReductionActiveByAwake) {
			set_awakeMprTime(0);
			_mpReductionActiveByAwake = false;
		}
	}

	public boolean isMpReductionActiveByAwake() {
		return _mpReductionActiveByAwake;
	}

	public int getAwakeSkillId() {
		return _awakeSkillId;
	}

	public void setAwakeSkillId(int i) {
		_awakeSkillId = i;
	}

	private ScheduledFuture<?> _atkMonitorFuture; // l1j-tw連續攻擊
	private ScheduledFuture<?> _detailsMonitorFuture; // 760屬性更新

	public void beginExpMonitor() {
		// scheduleAtFixedRate 改-> pcScheduleAtFixedRateHell
		// l1j-tw連續攻擊
		_atkMonitorFuture = GeneralThreadPool.get().pcScheduleAtFixedRateHell(new L1PcAtkMonitor(getId()), 0L,
				ConfigOther.Pc_Atk_Time); // 300
		// 760屬性更新
		_detailsMonitorFuture = GeneralThreadPool.get().pcScheduleAtFixedRateHell(new L1PcDetailsMonitor(getId()), 0L, 500);
	}

	/**
	 * 加入PC 可見物更新處理清單
	 */
	public void startObjectAutoUpdate() {
		removeAllKnownObjects();
	}

	/**
	 * 移出各種處理清單
	 */
	public void stopEtcMonitor() {
		set_ghostTime(-1);
		setGhost(false);
		setGhostCanTalk(true);
		setReserveGhost(false);

		set_mazu_time(0);
		set_mazu(false);

		stopMpReductionByAwake();

		if (_atkMonitorFuture != null) { // l1j-tw連續攻擊
			_atkMonitorFuture.cancel(true);
			_atkMonitorFuture = null;
		}

		if (_detailsMonitorFuture != null) { // 760屬性更新
			_detailsMonitorFuture.cancel(true);
			_detailsMonitorFuture = null;
		}

		// 移出短時間計時地圖時間軸
		if (ServerUseMapTimer.MAP.get(this) != null) {
			ServerUseMapTimer.MAP.remove(this);
		}

		// 移出計時地圖時間軸
		if (MapTimerThread.TIMINGMAP.get(this) != null) {
			MapTimerThread.TIMINGMAP.remove(this);
		}

		OnlineGiftSet.remove(this);

		ListMapUtil.clear(_skillList);
		ListMapUtil.clear(_sellList);
		ListMapUtil.clear(_buyList);
		//ListMapUtil.clear(_trade_items);
		ListMapUtil.clear(_allpowers);
	}

	public int getLawfulo() {
		return _old_lawful;
	}

	public void onChangeLawful() {
		if (_old_lawful != getLawful()) {
			_old_lawful = getLawful();
			sendPacketsAll(new S_Lawful(this));

			lawfulUpdate();
		}
	}

	public int getKarmalo() {
		return _old_karma;
	}

	public void onChangeKarma() {
		if (_old_karma != getKarma()) {
			_old_karma = getKarma();
			sendPackets(new S_Karma(this));
		}
	}

	public void lawfulUpdate() {
		int l = getLawful();

		if ((l >= 10000) && (l <= 19999)) {
			if (!_jl1) {
				overUpdate();
				_jl1 = true;
				sendPackets(new S_PacketBoxProtection(0, 1));
				sendPackets(new S_OwnCharAttrDef(this));
				sendPackets(new S_SPMR(this));
			}
		} else if ((l >= 20000) && (l <= 29999)) {
			if (!_jl2) {
				overUpdate();
				_jl2 = true;
				sendPackets(new S_PacketBoxProtection(1, 1));
				sendPackets(new S_OwnCharAttrDef(this));
				sendPackets(new S_SPMR(this));
			}
		} else if ((l >= 30000) && (l <= 39999)) {
			if (!_jl3) {
				overUpdate();
				_jl3 = true;
				sendPackets(new S_PacketBoxProtection(2, 1));
				sendPackets(new S_OwnCharAttrDef(this));
				sendPackets(new S_SPMR(this));
			}
		} else if ((l >= -19999) && (l <= -10000)) {
			if (!_el1) {
				overUpdate();
				_el1 = true;
				sendPackets(new S_PacketBoxProtection(3, 1));
				sendPackets(new S_SPMR(this));
			}
		} else if ((l >= -29999) && (l <= -20000)) {
			if (!_el2) {
				overUpdate();
				_el2 = true;
				sendPackets(new S_PacketBoxProtection(4, 1));
				sendPackets(new S_SPMR(this));
			}
		} else if ((l >= -39999) && (l <= -30000)) {
			if (!_el3) {
				overUpdate();
				_el3 = true;
				sendPackets(new S_PacketBoxProtection(5, 1));
				sendPackets(new S_SPMR(this));
			}

		} else if (overUpdate()) {
			sendPackets(new S_OwnCharAttrDef(this));
			sendPackets(new S_SPMR(this));
		}
	}

	private boolean overUpdate() {
		if (_jl1) {
			_jl1 = false;
			sendPackets(new S_PacketBoxProtection(0, 0));
			return true;
		}
		if (_jl2) {
			_jl2 = false;
			sendPackets(new S_PacketBoxProtection(1, 0));
			return true;
		}
		if (_jl3) {
			_jl3 = false;
			sendPackets(new S_PacketBoxProtection(2, 0));
			return true;
		}
		if (_el1) {
			_el1 = false;
			sendPackets(new S_PacketBoxProtection(3, 0));
			return true;
		}
		if (_el2) {
			_el2 = false;
			sendPackets(new S_PacketBoxProtection(4, 0));
			return true;
		}
		if (_el3) {
			_el3 = false;
			sendPackets(new S_PacketBoxProtection(5, 0));
			return true;
		}
		return false;
	}

	private boolean isEncounter() {
		if (getLevel() <= ConfigOther.ENCOUNTER_LV) {
			return true;
		}
		return false;
	}

	public int guardianEncounter() {
		if (_jl1) {
			return 0;
		}
		if (_jl2) {
			return 1;
		}
		if (_jl3) {
			return 2;
		}
		if (_el1) {
			return 3;
		}
		if (_el2) {
			return 4;
		}
		if (_el3) {
			return 5;
		}
		return -1;
	}

	public long getExpo() {
		return _old_exp;
	}

	/**
	 * 獲得經驗值的處理
	 */
	public void onChangeExp() {
		if (_old_exp != getExp()) {
			_old_exp = getExp();

			int level = ExpTable.getLevelByExp(getExp());
			int char_level = getLevel();
			int gap = level - char_level;

			if (gap == 0) {
				// if (level <= 127) {
				// sendPackets(new S_Exp(this));
				// } else {
				sendPackets(new S_OwnCharStatus(this));
				// }

				// 升級經驗獎勵狀態
				if (hasSkillEffect(L1SkillId.LEVEL_UP_BONUS)) {
					// 這是升級獎勵的規則(效果僅只有剛升級後的3小時)
					// →52級~64級：獎勵效果僅維持到每個等級的10%經驗值
					// →65級以上：獎勵效果僅維持到每個等級的5%經驗值
					final int current_per = ExpTable.getExpPercentage(char_level, getExp());
					final int bouns_per = char_level <= 64 ? 10 : 5;

					if (current_per >= bouns_per) {
						this.removeSkillEffect(L1SkillId.LEVEL_UP_BONUS);
					}

				}
				// 升級經驗獎勵狀態 end

				return;
			}

			if (gap > 0) {
				levelUp(gap);

			} else if (gap < 0) {
				levelDown(gap);
	            /*if (ConfigOther.LEVEL_UP) { // 升級經驗獎勵狀態
					if (hasSkillEffect(L1SkillId.LEVEL_UP_BONUS)) {
		            	removeSkillEffect(L1SkillId.LEVEL_UP_BONUS);
					}
	            }*/
			}

			if (getLevel() > ConfigOther.ENCOUNTER_LV) {
				sendPackets(new S_PacketBoxProtection(6, 0));
			} else
				sendPackets(new S_PacketBoxProtection(6, 1));
		}
	}

	/**
	 * TODO 接觸資訊
	 */
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		try {
			if ((isGmInvis()) || (isGhost()) || (isInvisble())) {
				return;
			}

			if (perceivedFrom.get_showId() != get_showId()) {
				return;
			}

			perceivedFrom.addKnownObject(this);

			perceivedFrom.sendPackets(new S_OtherCharPacks(this));

			if (ConfigOther.FREE_FIGHT_SWITCH) {
				if (CheckFightTimeController.getInstance().isFightMap(getMapId())) {
					perceivedFrom.sendPackets(new S_PinkName(getId(), -1));
				}
			}

			// 隊伍成員HP狀態發送
			if (isInParty()) {
				if (getParty().isMember(perceivedFrom)) {// 對象是隊員
					perceivedFrom.sendPackets(new S_HPMeter(this));
				}
			}

			//if (_isFishing) {
				//perceivedFrom.sendPackets(new S_Fishing(getId(), 71, get_fishX(), get_fishY()));
			//}
            if (isFishing()) {
                if (fishX != 0 && fishY != 0) {
                	perceivedFrom.sendPackets(new S_Fishing(getId(), ActionCodes.ACTION_Fishing, fishX, fishY));
                } else {
                	perceivedFrom.sendPackets(new S_Fishing(getId(), ActionCodes.ACTION_Fishing, getX(), getY()));
                }
            }

			if (isPrivateShop()) { // src022
				int mapId = getMapId();
				if ((mapId != 340) && (mapId != 350) && (mapId != 360) && (mapId != 370) && (mapId != 800)) {
					// if (mapId != 800) {
					getSellList().clear();
					getBuyList().clear();

					setPrivateShop(false);
					sendPacketsAll(new S_DoActionGFX(getId(), 3));
				} else {
					perceivedFrom.sendPackets(new S_DoActionShop(getId(), getShopChat()));
				}
			}
			if (get_vipLevel() > 0) {
				final S_VipShow vipShow = new S_VipShow(getId(), get_vipLevel());
				sendPacketsAll(vipShow);
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	private void removeOutOfRangeObjects() {
		for (L1Object known : getKnownObjects())
			if (known != null) {
				if (Config.PC_RECOGNIZE_RANGE == -1) {
					if (!getLocation().isInScreen(known.getLocation())) {
						removeKnownObject(known);
						sendPackets(new S_RemoveObject(known));
					}

				} else if (getLocation().getTileLineDistance(known.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
					removeKnownObject(known);
					sendPackets(new S_RemoveObject(known));
				}
			}
	}

	/**
	 * 可見物更新處理
	 */
	public void updateObject() {
		if (getOnlineStatus() != 1) {
			return;
		}
		removeOutOfRangeObjects();

		// 指定可視範圍資料更新
		for (final L1Object visible : World.get().getVisibleObjects(this, Config.PC_RECOGNIZE_RANGE)) {
			if (visible instanceof L1MerchantInstance) {// 對話NPC
				if (!knownsObject(visible)) {
					final L1MerchantInstance npc = (L1MerchantInstance) visible;
					// 未認知物件 執行物件封包發送
					npc.onPerceive(this);
				}
				continue;
			}

			if (visible instanceof L1DwarfInstance) {// 倉庫NPC
				if (!knownsObject(visible)) {
					final L1DwarfInstance npc = (L1DwarfInstance) visible;
					// 未認知物件 執行物件封包發送
					npc.onPerceive(this);
				}
				continue;
			}

			if (visible instanceof L1FieldObjectInstance) {// 景觀
				if (!knownsObject(visible)) {
					final L1FieldObjectInstance npc = (L1FieldObjectInstance) visible;
					// 未認知物件 執行物件封包發送
					npc.onPerceive(this);
				}
				continue;
			}

			// 副本ID不相等 不相護顯示
			if (visible.get_showId() != get_showId()) {
				continue;
			}

			if (!knownsObject(visible)) {
				// 未認知物件 執行物件封包發送
				visible.onPerceive(this);

			} else {
				if (visible instanceof L1NpcInstance) {
					final L1NpcInstance npc = (L1NpcInstance) visible;
					if (getLocation().isInScreen(npc.getLocation()) && (npc.getHiddenStatus() != 0)) {
						npc.approachPlayer(this);
					}
				}
			}

			// 一般人物 HP可見設置
			if (isHpBarTarget(visible)) {
				final L1Character cha = (L1Character) visible;
				cha.broadcastPacketHP(this);
			}

			// GM HP 查看設置
			if (hasSkillEffect(GMSTATUS_HPBAR)) {
				if (isGmHpBarTarget(visible)) {
					final L1Character cha = (L1Character) visible;
					cha.broadcastPacketHP(this);
				}
			}

			// GM HP 查看設置(只看PC角色)
			if (hasSkillEffect(GMSTATUS_HPBAR_PC)) {
				if (GmHpBarForPc(visible)) {
					final L1Character cha = (L1Character) visible;
					cha.broadcastPacketHP(this);
				}
			}
		}

		// 特效驗證系統
		if ((EffectAISet.START) && (this.hasSkillEffect(L1SkillId.AIFOREND))) {
			this.sendPackets(new S_EffectLocation(this.get_aixyz()[0], this
					.get_aixyz()[1], ServerAIEffectTable.getEffectId()));
			try {
				if (this.getX() != this.get_aixyz()[0]
						|| this.getY() != this.get_aixyz()[1]) {
					if (this.get_aistay() > 0) {
						this.set_aistay(0);
						String msg = "請 勿 在 驗 證 完 畢 前 移 動。";
						//this.sendPackets(new S_BlueMessage(166, "\\f3" + msg));
						//this.sendPackets(new S_ServerMessage(msg));
						this.sendPackets(new S_AllChannelsChat(msg, 3));
					}
				}
				switch (this.get_aistay()) {
				case 0:
					if (this.getX() == this.get_aixyz()[0]
							&& this.getY() == this.get_aixyz()[1]) {
						this.set_aistay(1);
						String msg = "正在進行中  驗證倒數...3";
						// this.sendPackets(new S_BlueMessage(166, "\\f3" +
						// msg));
						this.sendPackets(new S_ServerMessage("\\fUAI" + msg));
					}
					break;
				case 1:
					if (this.getX() == this.get_aixyz()[0]
							&& this.getY() == this.get_aixyz()[1]) {
						this.set_aistay(2);
						String msg = "正在進行中  驗證倒數...2";
						// this.sendPackets(new S_BlueMessage(166, "\\f3" +
						// msg));
						this.sendPackets(new S_ServerMessage("\\fUAI" + msg));
					}
					break;
				case 2:
					if (this.getX() == this.get_aixyz()[0]
							&& this.getY() == this.get_aixyz()[1]) {
						this.set_aistay(3);
						String msg = "正在進行中  驗證倒數...1";
						// this.sendPackets(new S_BlueMessage(166, "\\f3" +
						// msg));
						this.sendPackets(new S_ServerMessage("\\fUAI" + msg));
					}
					break;
				case 3:
					if (this.getX() == this.get_aixyz()[0]
							&& this.getY() == this.get_aixyz()[1]) {
						this.set_aixyz(null);
						this.set_aistay(0);
						this.sendPackets(new S_ServerMessage(
								"\\fUAI驗證完畢，您可以自由活動了！"));
						this.killSkillEffectTimer(L1SkillId.AIFOREND);
					}
					break;
				}
				Thread.sleep(1000);
			} catch (final Exception e) {
			}
		}

	}

	/**
	 * 可以觀看HP的對象(特別定義)
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isHpBarTarget(final L1Object obj) {
		if (obj instanceof L1PcInstance) {// 加入陣營戰活動同隊血條顯示
			final L1PcInstance tgpc = (L1PcInstance) obj;
			if (this.get_redbluejoin() != 0) {
				if (this.get_redbluejoin() == tgpc.get_redbluejoin()) {
					return true;
				}
			}
		}
		// 所在地圖位置
		switch (this.getMapId()) {
		case 400:// 大洞穴/大洞穴抵抗軍/隱遁者地區
			if (obj instanceof L1FollowerInstance) {
				final L1FollowerInstance follower = (L1FollowerInstance) obj;
				if (follower.getMaster().equals(this)) {
					return true;
				}
			}
			break;
		}
		return false;
	}

	/**
	 * GM HPBAR 可以觀看HP的對象
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isGmHpBarTarget(L1Object obj) {
		if ((obj instanceof L1PetInstance)) {
			return true;
		}
		if ((obj instanceof L1MonsterInstance)) {
			return true;
		}
		if ((obj instanceof L1SummonInstance)) {
			return true;
		}
		if ((obj instanceof L1DeInstance)) {
			return true;
		}
		if ((obj instanceof L1FollowerInstance)) {
			return true;
		}
		return false;
	}

	/**
	 * GM HPBAR 只看PC角色
	 * 
	 * @param obj
	 * @return
	 */
	public boolean GmHpBarForPc(L1Object obj) {
		if ((obj instanceof L1PcInstance)) {
			return true;
		}
		return false;
	}

	private void sendVisualEffect() {
		int poisonId = 0;
		if (getPoison() != null) {
			poisonId = getPoison().getEffectId();
		}
		if (getParalysis() != null) {
			poisonId = getParalysis().getEffectId();
		}
		if (poisonId != 0)
			sendPacketsAll(new S_Poison(getId(), poisonId));
	}

	public void sendVisualEffectAtLogin() {
		sendVisualEffect();
	}

	public boolean isCHAOTIC() {
		return _isCHAOTIC;
	}

	/**
	 * 混亂效果
	 * @param flag
	 */
	public void setCHAOTIC(boolean flag) {
		_isCHAOTIC = flag;
	}

	public void sendVisualEffectAtTeleport() {
		if (isDrink()) {
			sendPackets(new S_Liquor(getId()));
		}
		if (isCHAOTIC()) {
			sendPackets(new S_Liquor(getId(), 2));
		}
		sendVisualEffect();
	}

	/**
	 * 加入技能編號列表
	 * @param skillid
	 */
	public void setSkillMastery(int skillid) {
		if (!_skillList.contains(new Integer(skillid))) {
			_skillList.add(new Integer(skillid));
		}
	}

	/**
	 * 移出技能編號列表
	 * @param skillid
	 */
	public void removeSkillMastery(int skillid) {
		if (_skillList.contains(new Integer(skillid))) {
			_skillList.remove(new Integer(skillid));
		}
	}

	/**
	 * 傳回是否具有該技能使用權
	 * @param skillid
	 * @return
	 */
	public boolean isSkillMastery(int skillid) {
		return _skillList.contains(new Integer(skillid));
	}

	/**
	 * 清空
	 */
	public void clearSkillMastery() {
		_skillList.clear();
	}

	/**
	 * TODO 起始設置
	 */
	public L1PcInstance() {
		_accessLevel = 0;
		_currentWeapon = 0;
		_inventory = new L1PcInventory(this);
		_dwarf = new L1DwarfInventory(this);
		_dwarfForCha = new L1DwarfForChaInventory(this);
		_dwarfForElf = new L1DwarfForElfInventory(this);
		_dwarfForMALL = new L1DwarfForGameMallInventry(this);
		_tradewindow = new L1Inventory(); // 交易視窗
		_quest = new L1PcQuest(this);
		_action = new L1ActionPc(this);
		_actionPet = new L1ActionPet(this);
		_actionSummon = new L1ActionSummon(this);
		_equipSlot = new L1EquipmentSlot(this);
		/** [原碼] 怪物對戰系統 */
		_MobSpList = new L1MobSpList(this);
		/** [原碼] 大樂透系統 */
		_BigHotSpList = new L1BigHotSpList(this);
		_speed = new AcceleratorChecker(this);
        _bookmarks = new ArrayList<L1BookMark>(); // 日版記憶座標
        _speedbookmarks = new ArrayList<L1BookMark>(); // 日版記憶座標
	}

	/**
	 * 設定娃娃跟隨速度
	 */
	public void setNpcSpeed() {
		try {
			if (!getDolls().isEmpty()) {
				for (Object obj : getDolls().values().toArray()) {
					L1DollInstance doll = (L1DollInstance) obj;
					if (doll != null) {
						doll.setNpcMoveSpeed();
					}
				}
			}
			if (!getDolls2().isEmpty()) {
				for (final Object obj : getDolls2().values().toArray()) {
					final L1DollInstance2 doll = (L1DollInstance2) obj;
					if (doll != null) {
						doll.setNpcMoveSpeed();
					}
				}
			}
			// 取回娃娃
			if (get_power_doll() != null) {
				get_power_doll().setNpcMoveSpeed();
			}

		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void setCurrentHp(int i) {
		int currentHp = Math.min(i, this.getMaxHp());

		if (this.getCurrentHp() == currentHp) {
			return;
		}

		if (currentHp <= 0) {
			if (this.isGm()) {
				currentHp = this.getMaxHp();
			}
			else {
				if (!this.isDead()) {
					this.death(null); // HP小於1死亡
				}
			}
		}

		this.setCurrentHpDirect(currentHp);
		this.sendPackets(new S_HPUpdate(currentHp, this.getMaxHp()));
		if (this.isInParty()) { // 隊伍狀態
			this.getParty().updateMiniHP(this);
		}
	}

	public void setCurrentMp(int i) {
		//final int currentMp = Math.min(i, getMaxMp());
		int currentMp = Math.min(i, getMaxMp());

		if (getCurrentMp() == currentMp) {
			return;
		}
		
		if (currentMp <= 0) {
			currentMp = 0;
		}

		setCurrentMpDirect(currentMp);

		this.sendPackets(new S_MPUpdate(currentMp, getMaxMp()));
		if (isInParty()) {
			getParty().updateMiniHP(this);
		}
	}

	public L1PcInventory getInventory() {
		return _inventory;
	}

	public L1DwarfInventory getDwarfInventory() {
		return _dwarf;
	}

	public L1DwarfForChaInventory getDwarfForChaInventory() {
		return _dwarfForCha;
	}

	public L1DwarfForElfInventory getDwarfForElfInventory() {
		return _dwarfForElf;
	}

	public boolean isGmInvis() {
		return _gmInvis;
	}

	public void setGmInvis(boolean flag) {
		_gmInvis = flag;
	}

	public int getCurrentWeapon() {
		return _currentWeapon;
	}

	public void setCurrentWeapon(int i) {
		_currentWeapon = i;
	}

	public int getType() {
		return _type;
	}

	public void setType(int i) {
		_type = i;
		_classFeature = L1ClassFeature.newClassFeature(i); // XXX add 7.6
	}

	public short getAccessLevel() {
		return _accessLevel;
	}

	public void setAccessLevel(short i) {
		_accessLevel = i;
	}

	public int getClassId() {
		return _classId;
	}

	public void setClassId(int i) {
		_classId = i;
		// XXX del 7.6
		//_classFeature = L1ClassFeature.newClassFeature(i);
	}

	public L1ClassFeature getClassFeature() {
		return _classFeature;
	}

	public synchronized long getExp() {
		return _exp;
	}

	public synchronized void setExp(long i) {
		_exp = i;
	}

	public int get_PKcount() {
		return _PKcount;
	}

	public void set_PKcount(int i) {
		_PKcount = i;
	}

	public int getPkCountForElf() {
		return _PkCountForElf;
	}

	public void setPkCountForElf(int i) {
		_PkCountForElf = i;
	}

	public int getClanid() {
		return _clanid;
	}

	public void setClanid(int i) {
		_clanid = i;
	}

	public String getClanname() {
		return clanname;
	}

	public void setClanname(String s) {
		clanname = s;
	}

	public L1Clan getClan() {
		return WorldClan.get().getClan(getClanname());
	}

	public int getClanRank() {
		return _clanRank;
	}

	public void setClanRank(int i) {
		_clanRank = i;
	}

	public byte get_sex() {
		return _sex;
	}

	public void set_sex(int i) {
		_sex = ((byte) i);
	}

	public boolean isGm() {
		return _gm;
	}

	public void setGm(boolean flag) {
		_gm = flag;
	}

	/**
	 * 是否有監看權限
	 * 
	 * @return
	 */
	public boolean isMonitor() {
		return _monitor;
	}

	/**
	 * 設定是否有監看權限
	 * 
	 * @param flag
	 */
	public void setMonitor(boolean flag) {
		_monitor = flag;
	}

	private L1PcInstance getStat() {
		return null;
	}

	public void reduceCurrentHp(double d, L1Character l1character) {
		getStat().reduceCurrentHp(d, l1character);
	}

	private void notifyPlayersLogout(List<L1PcInstance> playersArray) {
		for (L1PcInstance player : playersArray)
			if (player.knownsObject(this)) {
				player.removeKnownObject(this);
				player.sendPackets(new S_RemoveObject(this));
			}
	}

	public void logout() {

		// 官方簽到系統
		if (Config.Attend) {
			CharacterAttendTable.getInstance().LogOutProfile(this);
		}

		// 刪除人物墓碑
		L1EffectInstance tomb = this.get_tomb();
		if (tomb != null) {
			tomb.broadcastPacketAll(new S_DoActionGFX(tomb.getId(), 8));
			tomb.deleteMe();
		}

		CharBuffReading.get().deleteBuff(this);
		CharBuffReading.get().saveBuff(this);

		QuestNewTable.getInstance().save(this); // 官服任務系統

		getMap().setPassable(getLocation(), true);

		if (getClanid() != 0) {
			L1Clan clan = WorldClan.get().getClan(getClanname());
			if ((clan != null) && (clan.getWarehouseUsingChar() == getId())) {
				clan.setWarehouseUsingChar(0);
			}
		}

		notifyPlayersLogout(getKnownPlayers());

		if (get_showId() != -1) {
			if (WorldQuest.get().isQuest(get_showId())) {
				WorldQuest.get().remove(get_showId(), this);
			}
		}

		set_showId(-1);

		World.get().removeVisibleObject(this);
		World.get().removeObject(this);
		notifyPlayersLogout(World.get().getRecognizePlayer(this));
		this._pandora.clearItems();
		UserMonsterBookLoader.store(this);
		if (Config.Week_Quest) {
			UserWeekQuestLoader.store(this);
		}
		removeAllKnownObjects();
		stopHpRegeneration();
		stopMpRegeneration();
		setDead(true);
		setNetConnection(null);
		setPacketOutput(null);

		if (Config.GUI) {
			J_Main.getInstance().delPlayerTable(getName());
		}
	}

	public ClientExecutor getNetConnection() {
		return _netConnection;
	}

	public void setNetConnection(ClientExecutor clientthread) {
		_netConnection = clientthread;
	}

	public String getIp() {
		return _netConnection.getIp().toString();
	}

	public boolean isInParty() {
		return getParty() != null;
	}

	public L1Party getParty() {
		return _party;
	}

	public void setParty(L1Party p) {
		_party = p;
	}

	public boolean isInChatParty() {
		return getChatParty() != null;
	}

	public L1ChatParty getChatParty() {
		return _chatParty;
	}

	public void setChatParty(L1ChatParty cp) {
		_chatParty = cp;
	}

	public int getPartyID() {
		return _partyID;
	}

	public void setPartyID(int partyID) {
		_partyID = partyID;
	}

	public int getTradeID() {
		return _tradeID;
	}

	public void setTradeID(int tradeID) {
		_tradeID = tradeID;
	}

	public void setTradeOk(boolean tradeOk) {
		_tradeOk = tradeOk;
	}

	public boolean getTradeOk() {
		return _tradeOk;
	}

	public int getTempID() {
		return _tempID;
	}

	public void setTempID(int tempID) {
		_tempID = tempID;
	}

	public boolean isTeleport() {
		return _isTeleport;
	}

	public void setTeleport(boolean flag) {
		if (flag) {
			setNowTarget(null);
		}
		_isTeleport = flag;
	}

	public boolean isDrink() {
		return _isDrink;
	}

	public void setDrink(boolean flag) {
		_isDrink = flag;
	}

	public boolean isGres() {
		return _isGres;
	}

	public void setGres(boolean flag) {
		_isGres = flag;
	}

	public boolean isPinkName() {
		return _isPinkName;
	}

	public void setPinkName(boolean flag) {
		_isPinkName = flag;
	}

	public ArrayList<L1PrivateShopSellList> getSellList() {
		return _sellList;
	}

	public ArrayList<L1PrivateShopBuyList> getBuyList() {
		return _buyList;
	}

	public void setShopChat(byte[] chat) {
		_shopChat = chat;
	}

	public byte[] getShopChat() {
		return _shopChat;
	}

	public boolean isPrivateShop() {
		return _isPrivateShop;
	}

	public void setPrivateShop(boolean flag) {
		_isPrivateShop = flag;
	}

	public boolean isTradingInPrivateShop() {
		return _isTradingInPrivateShop;
	}

	public void setTradingInPrivateShop(boolean flag) {
		_isTradingInPrivateShop = flag;
	}

	public int getPartnersPrivateShopItemCount() {
		return _partnersPrivateShopItemCount;
	}

	public void setPartnersPrivateShopItemCount(int i) {
		_partnersPrivateShopItemCount = i;
	}

	public void setPacketOutput(EncryptExecutor out) {
		_out = out;
	}

	public void sendPackets(ServerBasePacket packet) {
		if (_out == null) {
			return;
		}
		try {
			_out.encrypt(packet);
		} catch (Exception e) {
			logout();
			close();
		}
	}
	
    /*public void sendPackets(ServerBasePacket serverbasepacket, boolean clear) { // 升級經驗獎勵狀態
        try {
            if ((getMapId() == 2699 || getMapId() == 2100)
                    && serverbasepacket.getType().equalsIgnoreCase("[S] S_OtherCharPacks")) {
            } else
                sendPackets(serverbasepacket);
            if (clear) {
                serverbasepacket.clear();
                serverbasepacket = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

	/**
	 * 發送單體封包
	 * 
	 * @param packet
	 *            封包
	 */
	public void sendPackets2(final ServerBasePacket packet) {
		if (this._out == null) {
			return;
		}
		// System.out.println(packet.toString());
		try {
			this._out.encrypt(packet);

		} catch (final Exception e) {
			this.logout();
			this.close();
		}
	}

	public void sendPacketsBossWeaponAll(ServerBasePacket packet) {
		if (this._out == null) {
			return;
		}

		try {
			this._out.encrypt(packet);

			if ((!isGmInvis()) && (!isInvisble())) {
				broadcastPacketBossWeaponAll(packet);
			}
		} catch (Exception e) {
			logout();
			close();
		}
	}

	/**
	 * 發送單體封包 與可見範圍發送封包 自定義開關 防具
	 * 
	 * @param packet
	 *            封包
	 */
	public void sendPacketsArmorYN(final ServerBasePacket packet) {
		if (this._out == null) {
			return;
		}

		try {
			// 自己
			if (!L1CastleLocation.checkInAllWarArea(getX(), getY(), getMapId())) {
				this._out.encrypt(packet);
			}
			if (!this.isGmInvis() && !this.isInvisble()) {
				this.broadcastPacketArmorYN(packet);
			}

		} catch (final Exception e) {
			this.logout();
			this.close();
		}
	}

	/**
	 * 發送單體封包 與可見範圍發送封包 自定義開關 武器
	 * 
	 * @param packet
	 *            封包
	 */
	public void sendPacketsYN(final ServerBasePacket packet) {
		if (this._out == null) {
			return;
		}

		try {
			// 自己
			if (!L1CastleLocation.checkInAllWarArea(getX(), getY(), getMapId())) {
				this._out.encrypt(packet);
			}
			if (!this.isGmInvis() && !this.isInvisble()) {
				this.broadcastPacketYN(packet);
			}

		} catch (final Exception e) {
			this.logout();
			this.close();
		}
	}

	/**
	 * 發送單體封包 與可見範圍發送封包
	 * 
	 * @param packet
	 *            封包
	 */
	public void sendPacketsAll(final ServerBasePacket packet) {
		if (this._out == null) {
			return;
		}

		try {
			// 自己
			this._out.encrypt(packet);
			if (!this.isGmInvis() && !this.isInvisble()) {
				this.broadcastPacketAll(packet);
			}

		} catch (final Exception e) {
			this.logout();
			this.close();
		}
	}

	public void sendPacketsAllUnderInvis(ServerBasePacket packet) {
		if (_out == null) {
			return;
		}

		try {
			_out.encrypt(packet);
			if (!isGmInvis()) {
				broadcastPacketAll(packet);
			}
		} catch (Exception e) {
			logout();
			close();
		}
	}

	/**
	 * 發送單體封包 與指定範圍發送封包(範圍8)
	 * 
	 * @param packet
	 *            封包
	 */
	public void sendPacketsX8(ServerBasePacket packet) {
		if (this._out == null) {
			return;
		}

		try {
			this._out.encrypt(packet);

			if ((!isGmInvis()) && (!isInvisble())) {
				broadcastPacketX8(packet);
			}
		} catch (Exception e) {
			logout();
			close();
		}
	}

	public void sendPacketsUserAddHp(ServerBasePacket packet) // src015
	{
		if (this._out == null) {
			return;
		}

		try {
			this._out.encrypt(packet);

			final boolean castle_area = L1CastleLocation.checkInAllWarArea(getX(), getY(), getMapId());

			if ((!castle_area) && (!isGmInvis()) && (!isInvisble())) {
				broadcastPacketX10(packet);
			}
		} catch (Exception e) {
			logout();
			close();
		}
	}

	/**
	 * 發送單體封包 與指定範圍發送封包(範圍10)
	 * 
	 * @param packet
	 *            封包
	 */
	public void sendPacketsX10(ServerBasePacket packet) {
		if (_out == null) {
			return;
		}

		try {
			_out.encrypt(packet);
			if ((!isGmInvis()) && (!isInvisble()))
				broadcastPacketX10(packet);
		} catch (Exception e) {
			logout();
			close();
		}
	}

	/**
	 * 發送單體封包 與可見指定範圍發送封包
	 * 
	 * @param packet
	 *            封包
	 * @param r
	 *            範圍
	 */
	public void sendPacketsXR(ServerBasePacket packet, int r) {
		if (_out == null) {
			return;
		}

		try {
			_out.encrypt(packet);
			if ((!isGmInvis()) && (!isInvisble()))
				broadcastPacketXR(packet, r);
		} catch (Exception e) {
			logout();
			close();
		}
	}

	/**
	 * 關閉連線線程
	 */
	private void close() {
		try {
			getNetConnection().close();
		} catch (Exception localException) {
		}
	}

	public void addSkin(L1SkinInstance skin, int gfxid) {
		this._skins.put(Integer.valueOf(gfxid), skin);
	}

	public void removeSkin(int gfxid) {
		this._skins.remove(Integer.valueOf(gfxid));
	}

	public L1SkinInstance getSkin(int gfxid) {
		return (L1SkinInstance) this._skins.get(Integer.valueOf(gfxid));
	}

	public Map<Integer, L1SkinInstance> getSkins() {
		return _skins;
	}

	/** vip 保護經驗 */
	public void set_death_exp(boolean b) {
		this._death_exp = b;
	}

	/** vip 保護物品 */
	public void set_death_item(boolean b) {
		this._death_item = b;
	}

	/** vip 保護技能 */
	public void set_death_skill(boolean b) {
		this._death_skill = b;
	}

	/** vip 保護技能 */
	public void set_death_score(boolean b) {
		this._death_score = b;
	}

	/**
	 * 對該物件攻擊的調用
	 * 
	 * @param attacker
	 *            攻擊方
	 */
	@Override
	public void onAction(L1PcInstance attacker) {

		if (attacker == null) {
			return;
		}

		if (isTeleport()) {
			return;
		}

		if ((isSafetyZone()) || (attacker.isSafetyZone())) {
			L1AttackMode attack_mortion = new L1AttackPc(attacker, this);
			attack_mortion.action();
			return;
		}

		if (checkNonPvP(this, attacker)) {
			L1AttackMode attack_mortion = new L1AttackPc(attacker, this);
			attack_mortion.action();
			return;
		}

		if ((getCurrentHp() > 0) && (!isDead())) {
			attacker.delInvis();

			boolean isCounterBarrier = false;

			L1AttackMode attack = new L1AttackPc(attacker, this);
			if (attack.calcHit()) {// 被攻擊命中時
				if (hasSkillEffect(COUNTER_BARRIER)) {
					L1Magic magic = new L1Magic(this, attacker);
					boolean isProbability = magic.calcProbabilityMagic(91);
					boolean isShortDistance = attack.isShortDistance();
					L1ItemInstance weapon = attacker.getWeapon();
					if ((isProbability) && (isShortDistance) && (weapon.getItem().getType() != 17)) {
						isCounterBarrier = true;
					}
				}
				if (!isCounterBarrier) {
					attacker.setPetTarget(this);
					attack.calcDamage();
				}
			}

			if (isCounterBarrier) {
				attack.commitCounterBarrier();
			} else {
				attack.action();
				attack.commit();
			}
		}
	}

	public boolean checkNonPvP(L1PcInstance pc, L1Character target) {
		L1PcInstance targetpc = null;
		if ((target instanceof L1PcInstance)) {
			targetpc = (L1PcInstance) target;
		} else if ((target instanceof L1PetInstance)) {
			targetpc = (L1PcInstance) ((L1PetInstance) target).getMaster();
		} else if ((target instanceof L1SummonInstance)) {
			targetpc = (L1PcInstance) ((L1SummonInstance) target).getMaster();
		}
		if (targetpc == null) {
			return false;
		}

		if (!ConfigAlt.ALT_NONPVP) {
			if (getMap().isCombatZone(getLocation())) {
				return false;
			}

			for (L1War war : WorldWar.get().getWarList()) {
				if ((pc.getClanid() != 0) && (targetpc.getClanid() != 0)) {
					boolean same_war = war.checkClanInSameWar(pc.getClanname(), targetpc.getClanname());
					if (same_war) {
						return false;
					}
				}
			}

			if ((target instanceof L1PcInstance)) {
				L1PcInstance targetPc = (L1PcInstance) target;
				if (isInWarAreaAndWarTime(pc, targetPc)) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	/**
	 * 是否在戰爭旗中並在攻城時段中
	 * 
	 * @param pc
	 * @param target
	 * @return
	 */
	private boolean isInWarAreaAndWarTime(L1PcInstance pc, L1PcInstance target) {
		int castleId = L1CastleLocation.getCastleIdByArea(pc);
		int targetCastleId = L1CastleLocation.getCastleIdByArea(target);
		if ((castleId != 0) && (targetCastleId != 0) && (castleId == targetCastleId)
				&& (ServerWarExecutor.get().isNowWar(castleId))) {
			return true;
		}

		return false;
	}

	public void setPetTarget(L1Character target) {
		if (target == null) {
			return;
		}
		if (target.isDead()) {
			return;
		}
		Map<Integer, L1NpcInstance> petList = getPetList();
		try {
			if (!petList.isEmpty()) {
				for (Iterator<?> iter = petList.values().iterator(); iter.hasNext();) {
					L1NpcInstance pet = (L1NpcInstance) iter.next();
					if (pet != null)
						if ((pet instanceof L1PetInstance)) {
							L1PetInstance pets = (L1PetInstance) pet;
							pets.setMasterTarget(target);
						} else if ((pet instanceof L1SummonInstance)) {
							L1SummonInstance summon = (L1SummonInstance) pet;
							summon.setMasterTarget(target);
						}
				}
			}
		} catch (Exception e) {
			if (_debug) {
				_log.error(e.getLocalizedMessage(), e);
			}
		}

		Map<Integer, L1IllusoryInstance> illList = get_otherList().get_illusoryList();
		try {
			if (!illList.isEmpty()) {
				if (getId() != target.getId()) {
					for (Iterator<L1IllusoryInstance> iter = illList.values().iterator(); iter.hasNext();) {
						L1IllusoryInstance ill = (L1IllusoryInstance) iter.next();
						if (ill != null)
							ill.setLink(target);
					}
				}
			}
		} catch (Exception e) {
			if (_debug)
				_log.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 解除隱身
	 */
	public void delInvis() {
		if (hasSkillEffect(INVISIBILITY)) {
			killSkillEffectTimer(INVISIBILITY);
			sendPackets(new S_Invis(getId(), 0));
			broadcastPacketAll(new S_OtherCharPacks(this));
		}
		if (hasSkillEffect(BLIND_HIDING)) {
			killSkillEffectTimer(BLIND_HIDING);
			sendPackets(new S_Invis(getId(), 0));
			broadcastPacketAll(new S_OtherCharPacks(this));
		}
	}

	public void delBlindHiding() {
		killSkillEffectTimer(BLIND_HIDING);
		sendPackets(new S_Invis(getId(), 0));
		broadcastPacketAll(new S_OtherCharPacks(this));
	}

	/**
	 * 受攻擊MP減少時的處理
	 * 
	 * @param attacker
	 * @param mpDamage
	 */
	public void receiveManaDamage(L1Character attacker, int mpDamage) {
		if ((mpDamage > 0) && (!isDead())) {

			delInvis();// 解除隱身

			if ((attacker instanceof L1PcInstance)) {
				L1PinkName.onAction(this, attacker);
			}

			if ((attacker instanceof L1PcInstance) && (((L1PcInstance) attacker).isPinkName())) {
				for (L1Object object : World.get().getVisibleObjects(attacker)) {
					if ((object instanceof L1GuardInstance)) {
						L1GuardInstance guard = (L1GuardInstance) object;
						guard.setTarget((L1PcInstance) attacker);
					}
				}
			}

			int newMp = getCurrentMp() - mpDamage;
			if (newMp > getMaxMp()) {
				newMp = getMaxMp();
			}
			newMp = Math.max(newMp, 0);

			setCurrentMp(newMp);
		}
	}

	/**
	 * 連續魔法傷害遞減
	 */
	public static void load() {
		double newdmg = 100.0D;
		for (long i = 2000L; i > 0L; i -= 1L) {
			if (i % 100L == 0L) {
				newdmg -= 3.33D;
			}
			_magicDamagerList.put(Long.valueOf(i), Double.valueOf(newdmg));
		}
	}

	/**
	 * 魔法傷害遞減計算
	 * 
	 * @param damage
	 * @return
	 */
	public double isMagicDamager(double damage) {
		long nowTime = System.currentTimeMillis();
		long interval = nowTime - _oldTime;

		double newdmg = 0.0D;
		if (damage < 0.0D) {
			newdmg = damage;
		} else {
			Double tmpnewdmg = (Double) _magicDamagerList.get(Long.valueOf(interval));
			if (tmpnewdmg != null) {
				newdmg = damage * tmpnewdmg.doubleValue() / 100.0D;
			} else {
				newdmg = damage;
			}
			newdmg = Math.max(newdmg, 0.0D);

			_oldTime = nowTime;
		}
		return newdmg;
	}

	/**
	 * 受到傷害的處理
	 * 
	 * @param attacker
	 *            攻擊者
	 * @param damage
	 *            傷害值
	 * @param isMagicDamage
	 *            是否魔法傷害遞減
	 * @param isCounterBarrier
	 *            是否執行反彈 true不反彈 false計算反彈
	 */
	public void receiveDamage(L1Character attacker, double damage, boolean isMagicDamage, boolean isCounterBarrier) {
		if (damage <= 0.0D) {// 傷害值小於0則返回
			return;
		}

		if ((getCurrentHp() > 0) && (!isDead())) {

			if (attacker != null) {
				if ((attacker != this) && (!(attacker instanceof L1EffectInstance)) && (!knownsObject(attacker))
						&& (attacker.getMapId() == getMapId())) {
					attacker.onPerceive(this);
				}

				if (isMagicDamage) {// 魔法傷害遞減
					damage = isMagicDamager(damage);
				}

				L1PcInstance attackPc = null;
				L1NpcInstance attackNpc = null;

				if ((attacker instanceof L1PcInstance)) {
					attackPc = (L1PcInstance) attacker;
				} else if ((attacker instanceof L1NpcInstance)) {
					attackNpc = (L1NpcInstance) attacker;
				}

				if (damage > 0.0D) {
					delInvis();
					removeSkillEffect(66);
					removeSkillEffect(212);

					if (attackPc != null) {
						L1PinkName.onAction(this, attackPc);
						if (attackPc.isPinkName()) {
							for (L1Object object : World.get().getVisibleObjects(attacker)) {
								if ((object instanceof L1GuardInstance)) {
									L1GuardInstance guard = (L1GuardInstance) object;
									guard.setTarget((L1PcInstance) attacker);
								}
							}
						}
					}
					//SRC0907
					boolean useWeaponCheck = false;
					if (attackPc != null) {
						L1ItemInstance weapon = attackPc.getWeapon();
						if ((weapon != null) && ((weapon.getItem().getType1() == 20) || (weapon.getItem().getType1() == 62))) {
							useWeaponCheck = true;
						}
					}

					if ((!isMagicDamage) && (attackPc != null) && (this.getCounterattack() > 0)) {
						if (!useWeaponCheck) {
							if (15 >= _random.nextInt(100) + 1) {
								final int dmgYYY = this.getCounterattack();
								this.sendPacketsX8(new S_EffectLocation(this.getX(), this.getY(), 6507));
								if (attackPc != null) {
									attackPc.sendPacketsX10(new S_DoActionGFX(attackPc.getId(), ActionCodes.ACTION_Damage));
									attackPc.receiveDamage(this, dmgYYY, false, true);
								}
							}
						}
					}

					if ((!isMagicDamage) && (attackPc != null) && (this.getBowcounterattack() > 0)) {
						if (useWeaponCheck) {
							if (15 >= _random.nextInt(100) + 1) {
								final int dmgXXX = this.getBowcounterattack();
								this.sendPacketsX8(new S_EffectLocation(this.getX(), this.getY(), 10419));
								if (attackPc != null) {
									attackPc.sendPacketsX10(new S_DoActionGFX(attackPc.getId(), ActionCodes.ACTION_Damage));
									attackPc.receiveDamage(this, dmgXXX, false, true);
								}
							}
						}
					}
					// SRC0907 END
				}

				if (!isCounterBarrier) {// 執行傷害反彈
					if ((hasSkillEffect(MORTAL_BODY)) && (getId() != attacker.getId())) {// 致命身軀反彈
						int rnd = _random.nextInt(100);
						if ((damage > 0.0D) && (rnd < 23)) {
							int dmg = 40;
							if (attackPc != null) {
								if (attackPc.hasSkillEffect(IMMUNE_TO_HARM)) {// 聖界減傷
									dmg /= 2;
								}
								attackPc.sendPacketsAll(new S_DoActionGFX(attackPc.getId(), 2));
								this.sendPacketsAll(new S_SkillSound(this.getId(), 10710));
								attackPc.receiveDamage(this, dmg, false, true);
							} else if (attackNpc != null) {
								if (attackNpc.hasSkillEffect(IMMUNE_TO_HARM)) {// 聖界減傷
									dmg /= 2;
								}
								/*
								 * if (dmg >= attackNpc.getCurrentHp()) {//
								 * 如果傷害大於等於目前HP dmg = attackNpc.getCurrentHp() -
								 * 1;// 變更傷害為目前HP-1(避免使用反屏掛機) }
								 */
								attackNpc.broadcastPacketAll(new S_DoActionGFX(attackNpc.getId(), 2));
								this.sendPacketsAll(new S_SkillSound(this.getId(), 10710));
								attackNpc.receiveDamage(this, dmg);
							}
						}
					}

					if ((!isMagicDamage) && (_elitePlateMail_Valakas > 0)) { // 巴拉卡斯的弓箭反屏
						int nowDamage = _random.nextInt(_valakas_dmgmax - _valakas_dmgmin + 1) + _valakas_dmgmin;
						if (attackPc != null) {
							L1ItemInstance weapon = attackPc.getWeapon();
							if ((weapon != null)
									&& ((weapon.getItem().getType1() == 20) || (weapon.getItem().getType1() == 62))
									&& (_random.nextInt(1000) < _elitePlateMail_Valakas)) {
								if (attackPc.hasSkillEffect(68)) {// 聖界減傷
									nowDamage /= 2;
								}
								this.sendPacketsAll(new S_SkillSound(getId(), 10419));
								attackPc.sendPacketsAll(new S_DoActionGFX(attackPc.getId(), 2));
								attackPc.receiveDamage(this, nowDamage, false, true);
							}
						} else if ((attackNpc != null) && (_random.nextInt(1000) < _elitePlateMail_Valakas)) {
							L1AttackMode attack = new L1AttackNpc(attackNpc, this);
							boolean isShortDistance = attack.isShortDistance();
							if (!isShortDistance) {
								if (attackNpc.hasSkillEffect(68)) {// 聖界減傷
									nowDamage /= 2;
								}
								/*
								 * if (nowDamage >= attackNpc.getCurrentHp())
								 * {// 如果傷害大於等於目前HP nowDamage =
								 * attackNpc.getCurrentHp() - 1;//
								 * 變更傷害為目前HP-1(避免使用反屏掛機) }
								 */
								this.sendPacketsAll(new S_SkillSound(getId(), 10419));
								attackNpc.broadcastPacketAll(new S_DoActionGFX(attackNpc.getId(), 2));
								attackNpc.receiveDamage(this, nowDamage);
							}
						}
					}
					boolean hades = false;
					// TODO 黑帝斯斗篷反彈傷害
					if (_hades_cloak > 0) {
						int nowDamage = _random.nextInt(_hades_cloak_dmgmax - _hades_cloak_dmgmin + 1)
								+ _hades_cloak_dmgmin;

						if ((attackPc != null) && (_random.nextInt(1000) < _hades_cloak)) {
							/*
							 * L1AttackMode attack = new L1AttackPc(attackPc,
							 * this); boolean isShortDistance =
							 * attack.isShortDistance(); if (isShortDistance) {
							 */
							if (attackPc.hasSkillEffect(68)) {// 聖界減傷
								nowDamage /= 2;
							}
							attackPc.sendPacketsAll(new S_DoActionGFX(attackPc.getId(), 2));
							this.sendPacketsAll(new S_SkillSound(this.getId(), 10710));
							attackPc.receiveDamage(this, nowDamage, false, true);
							hades = true;
							damage = 0;
						} else if ((attackNpc != null) && (_random.nextInt(1000) < _hades_cloak)) {
							/*
							 * L1AttackMode attack = new L1AttackNpc(attackNpc,
							 * this); boolean isShortDistance =
							 * attack.isShortDistance(); if (isShortDistance) {
							 */
							if (attackNpc.hasSkillEffect(68)) {// 聖界減傷
								nowDamage /= 2;
							}
							/*
							 * if (nowDamage >= attackNpc.getCurrentHp()) {//
							 * 如果傷害大於等於目前HP nowDamage = attackNpc.getCurrentHp()
							 * - 1;// 變更傷害為目前HP-1(避免使用反屏掛機) }
							 */
							attackNpc.broadcastPacketAll(new S_DoActionGFX(attackNpc.getId(), 2));
							this.sendPacketsAll(new S_SkillSound(this.getId(), 10710));
							attackNpc.receiveDamage(this, nowDamage);
							hades = true;
							damage = 0;
						}
					}
					// TODO 死亡騎士脛甲反彈傷害
					if (_death_pant > 0) {
						int nowDamage = _random.nextInt(_death_pant_dmgmax - _death_pant_dmgmin + 1)
								+ _death_pant_dmgmin;

						if ((attackPc != null) && (_random.nextInt(1000) < _death_pant)) {
							/*
							 * L1AttackMode attack = new L1AttackPc(attackPc,
							 * this); boolean isShortDistance =
							 * attack.isShortDistance(); if (isShortDistance) {
							 */
							if (attackPc.hasSkillEffect(68)) {// 聖界減傷
								nowDamage /= 2;
							}
							attackPc.sendPacketsAll(new S_DoActionGFX(attackPc.getId(), 2));
							//this.sendPacketsAll(new S_SkillSound(this.getId(), 10710));
						    if (ConfigAlt.Death_Pant_Gfx != 0) {
						    	this.sendPacketsAll(new S_SkillSound(this.getId(), ConfigAlt.Death_Pant_Gfx));
						    }
							attackPc.receiveDamage(this, nowDamage, false, true);
							hades = true;
							damage = 0;
						} else if ((attackNpc != null) && (_random.nextInt(1000) < _death_pant)) {
							/*
							 * L1AttackMode attack = new L1AttackNpc(attackNpc,
							 * this); boolean isShortDistance =
							 * attack.isShortDistance(); if (isShortDistance) {
							 */
							if (attackNpc.hasSkillEffect(68)) {// 聖界減傷
								nowDamage /= 2;
							}
							/*
							 * if (nowDamage >= attackNpc.getCurrentHp()) {//
							 * 如果傷害大於等於目前HP nowDamage = attackNpc.getCurrentHp()
							 * - 1;// 變更傷害為目前HP-1(避免使用反屏掛機) }
							 */
							attackNpc.broadcastPacketAll(new S_DoActionGFX(attackNpc.getId(), 2));
							//this.sendPacketsAll(new S_SkillSound(this.getId(), 10710));
						    if (ConfigAlt.Death_Pant_Gfx != 0) {
						    	this.sendPacketsAll(new S_SkillSound(this.getId(), ConfigAlt.Death_Pant_Gfx));
						    }
							attackNpc.receiveDamage(this, nowDamage);
							hades = true;
							damage = 0;
						}
					}
					if (this.has_powerid(6612)) {// 附魔系統 還擊傷害
						int rad = 15;// 機率
						int dmg = 80;// 反彈傷害值
						if ((attackPc != null) && (damage > 0) && (_random.nextInt(100) < rad)) {
							if (attackPc.hasSkillEffect(68)) {// 聖界減傷
								dmg /= 2;
							}
							attackPc.sendPacketsAll(new S_DoActionGFX(attackPc.getId(), 2));
							this.sendPacketsAll(new S_SkillSound(this.getId(), 10710));
							attackPc.receiveDamage(this, dmg, false, true);
						} else if ((attackNpc != null) && (damage > 0) && (_random.nextInt(100) < rad)) {
							if (attackNpc.hasSkillEffect(68)) {// 聖界減傷
								dmg /= 2;
							}
							if (dmg >= attackNpc.getCurrentHp()) {// 如果傷害大於等於目前HP
								dmg = attackNpc.getCurrentHp() - 1;// 變更傷害為目前HP-1(避免使用反屏掛機)
							}
							attackNpc.broadcastPacketAll(new S_DoActionGFX(attackNpc.getId(), 2));
							this.sendPacketsAll(new S_SkillSound(this.getId(), 10710));
							attackNpc.receiveDamage(this, dmg);
						}
					}
				}
			}

			if ((getInventory().checkEquipped(145)) || (getInventory().checkEquipped(149))) {// 狂斧、牛人斧
				damage *= 1.5D;
			}

			if (this.hasSkillEffect(219)) {// 化身
				damage *= ConfigSkill.ILLUSION_AVATAR_DAMAGE; // 傷害提高1.05倍
				// damage *=1.06;//傷害提高1.05倍
			}
			if (this.hasSkillEffect(1219)) {// 化身  //SRC0808
				damage *= 1+ConfigSkill.IS4;;
			}
            if (this.isCrown() && this.hasSkillEffect(L1SkillId.ReiSkill_1)) { // 王族天賦技能金剛護體
                damage = 0.0;
            }
            if ((this.isKnight()) && (this.getReincarnationSkill()[2] > 0)) { // 騎士天賦技能神盾護體
                boolean isSameAttr = false;
                if ((getHeading() == 0) && ((attacker.getHeading() == 3) || (attacker.getHeading() == 4) || (attacker.getHeading() == 4)))
                    isSameAttr = true;
                else if ((getHeading() == 1) && ((attacker.getHeading() == 4) || (attacker.getHeading() == 5) || (attacker.getHeading() == 6)))
                    isSameAttr = true;
       	        else if ((getHeading() == 2) && ((attacker.getHeading() == 5) || (attacker.getHeading() == 6) || (attacker.getHeading() == 7)))
       	            isSameAttr = true;
       	        else if ((getHeading() == 3) && ((attacker.getHeading() == 6) || (attacker.getHeading() == 7) || (attacker.getHeading() == 0)))
       	            isSameAttr = true;
       	        else if ((getHeading() == 4) && ((attacker.getHeading() == 7) || (attacker.getHeading() == 0) || (attacker.getHeading() == 1)))
       	            isSameAttr = true;
       	        else if ((getHeading() == 5) && ((attacker.getHeading() == 0) || (attacker.getHeading() == 1) || (attacker.getHeading() == 2)))
       	            isSameAttr = true;
       	        else if ((getHeading() == 6) && ((attacker.getHeading() == 1) || (attacker.getHeading() == 2) || (attacker.getHeading() == 3)))
       	            isSameAttr = true;
       	        else if ((getHeading() == 7) && ((attacker.getHeading() == 2) || (attacker.getHeading() == 3) || (attacker.getHeading() == 4))) {
       	            isSameAttr = true;
       	        }
                if ((isSameAttr) &&  (RandomArrayList.getInc(100, 1) > 100 - this.getReincarnationSkill()[2])) {
                    damage *= 0.70D;
                    //sendPackets(new S_SkillSound(getId(), 5377));
                    sendPackets(new S_SystemMessage(L1SystemMessage.ShowMessage(8045))); // 發動天賦技能 神盾護體 減少30%傷害。
       	        }
  	        }

			int addmp = 0;
			if ((_elitePlateMail_Lindvior > 0) && // 林德拜爾的魔力守護
					(_random.nextInt(1000) < _elitePlateMail_Lindvior)) {
				sendPacketsAll(new S_SkillSound(getId(), 2188));
				addmp = _random.nextInt(_lindvior_mpmax - _lindvior_mpmin + 1) + _lindvior_mpmin;
				int newMp = getCurrentMp() + addmp;
				setCurrentMp(newMp);
			}

			int addhp = 0;

			if ((getInventory().checkEquipped(21204) || getInventory().checkEquipped(21205)
					|| getInventory().checkEquipped(21206) || getInventory().checkEquipped(21207))
					&& (_elitePlateMail_Fafurion > 0) && // 法利昂的治癒守護
					(_random.nextInt(1000) < _elitePlateMail_Fafurion)) {
				sendPacketsAll(new S_SkillSound(getId(), 2187));
				addhp = _random.nextInt(_fafurion_hpmax - _fafurion_hpmin + 1) + _fafurion_hpmin;
			}

			if ((_Hexagram_Magic_Rune > 0) && // 六芒星的淨化
					(_random.nextInt(1000) < _Hexagram_Magic_Rune)) {
				sendPacketsAll(new S_SkillSound(getId(), _hexagram_gfx));
				addhp = _random.nextInt(_hexagram_hpmax - _hexagram_hpmin + 1) + _hexagram_hpmin;
			}

			if ((_dimiter_bless > 0) && // 蒂蜜特的祝福
					(_random.nextInt(1000) < _dimiter_bless)) {
				if (!this.hasSkillEffect(IMMUNE_TO_HARM)) {// 身上沒有聖界效果
					sendPacketsAll(new S_SkillSound(getId(), 11101));
					setSkillEffect(IMMUNE_TO_HARM, _dimiter_time * 1000);
					sendPackets(new S_PacketBox(S_PacketBox.ICON_I2H, _dimiter_time));
				}
			}

			if ((_dimiter_mpr_rnd > 0) && // 蒂蜜特的魔力回復
					(_random.nextInt(1000) < _dimiter_mpr_rnd)) {
				sendPacketsAll(new S_SkillSound(getId(), 2188));
				addmp = _random.nextInt(_dimiter_mpmax - _dimiter_mpmin + 1) + _dimiter_mpmin;
				int newMp = getCurrentMp() + addmp;
				setCurrentMp(newMp);
			}

			int newHp = getCurrentHp() - (int) damage + addhp;
			if (newHp > getMaxHp()) {
				newHp = getMaxHp();
			}

            if (newHp <= 10) { // 精靈新技能 魔力護盾
                if (isElf() && hasSkillEffect(L1SkillId.SOUL_BARRIER)) {
                    this.setCurrentHp(10);
                    newHp = 10;
                    int newMp = getCurrentMp() - (int) damage;
                    //if (newMp <= 0) {
        			if ((newMp <= 0) && (!isGm())) {
                        this.setCurrentHp(0);
                        death(attacker);
                    }
                    this.setCurrentMp(newMp);
                }
            }
			
			if ((newHp <= 0) && (!isGm())) {
				death(attacker);
			}

			setCurrentHp(newHp);
            if (this.isCrown() && this.getReincarnationSkill()[0] > 0 && RandomArrayList.getInc(100, 1) > 100 - this.getReincarnationSkill()[0]) { // 王族天賦技能金剛護體
                this.setSkillEffect(L1SkillId.ReiSkill_1, 1000);
                this.sendPackets(new S_SystemMessage(L1SystemMessage.ShowMessage(8018))); // 發動天賦技能無敵1秒。
                //this.sendPackets(new S_SkillSound(this.getId(), 9800));
                //this.broadcastPacketAll(new S_SkillSound(this.getId(), 9800));
            }
		} else if (!isDead()) {
			_log.error("人物hp減少處理失敗 可能原因: 初始hp為0");
			death(attacker);
		}
	}

	public void death(L1Character lastAttacker) {
		synchronized (this) {
			if (isDead()) {
				return;
			}

			setNowTarget(null);
			setDead(true);
			setStatus(8);

		}
		GeneralThreadPool.get().execute(new Death(lastAttacker));
	}

	/**
	 * 死亡噴出物品
	 * 
	 * @param count
	 */
	private void caoPenaltyResult(int count) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		for (int i = 0; i < count; i++) {
			L1ItemInstance item = getInventory().caoPenalty();
			if (item != null) {

				if (item.getBless() >= 128) {
					_log.warn("玩家：" + this.getName() + "封印裝備 死亡噴出遺失:" + item.getId() + "/" + item.getItem().getName());
					// 死亡掉落物品
					ConfigRecord.recordToFiles("死亡掉落物品",
							"IP(" + getNetConnection().getIp() + ")玩家【" + getName() + "】的(封印)【"
									+ item.getNumberedViewName(item.getCount()) + ", (ObjId: " + item.getId()
									+ ")】死亡後遺失, 時間:(" + timestamp + ")",
							timestamp);

					getInventory().deleteItem(item);

				} else {
					_log.warn("玩家：" + this.getName() + "死亡噴出物品:" + item.getId() + "/" + item.getItem().getName());
					item.set_showId(get_showId());

					int x = getX();
					int y = getY();
					short m = getMapId();
					// 死亡掉落物品
					ConfigRecord.recordToFiles("死亡掉落物品",
							"IP(" + getNetConnection().getIp() + ")玩家【" + getName() + "】的【"
									+ item.getNumberedViewName(item.getCount()) + ", (ObjId: " + item.getId()
									+ ")】死亡後掉落, 時間:(" + timestamp + ")",
							timestamp);

					getInventory().tradeItem(item, item.isStackable() ? item.getCount() : 1L,
							World.get().getInventory(x, y, m));
				}
				// 638 您損失了 %0。
				sendPackets(new S_ServerMessage(638, item.getLogName()));
			}
		}
	}

	/**
	 * <FONT COLOR="#0000ff">死亡技能遺失</FONT>
	 * 
	 * @param count
	 *            掉落數量
	 */
	private void delSkill(int count) {
		if (this._skillList.size() < 1) {
			return;
		}

		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		for (int i = 0; i < count; i++) {
			int index = _random.nextInt(_skillList.size());

			Integer skillid = (Integer) _skillList.get(index);

			if (_skillList.remove(skillid)) {
				sendPackets(new S_DelSkill(this, skillid.intValue()));
				CharSkillReading.get().spellLost(getId(), skillid.intValue());
				L1Skills _skill = SkillsTable.get().getTemplate(skillid.intValue());
				// 死亡掉落技能
				ConfigRecord.recordToFiles("死亡掉落技能", "IP(" + getNetConnection().getIp() + ")玩家【" + getName() + "】的技能【"
						+ _skill.getName() + "】死亡後掉落, 時間:(" + timestamp + ")", timestamp);
			}
		}
	}

	public void stopPcDeleteTimer() {
		setDead(false);
		set_delete_time(0);
	}

	/**
	 * <FONT COLOR="#0000ff">是否在參加攻城戰中</FONT>
	 * 
	 * @return true:是 false:不是
	 */
	public boolean castleWarResult() {
		if ((this.getClanid() != 0) && this.isCrown()) { // 具有血盟的王族
			final L1Clan clan = WorldClan.get().getClan(this.getClanname());
			if (clan.getCastleId() == 0) {
				// 取回全部戰爭清單
				for (final L1War war : WorldWar.get().getWarList()) {
					final int warType = war.getWarType();
					final boolean isInWar = war.checkClanInWar(this.getClanname());
					final boolean isAttackClan = war.checkAttackClan(this.getClanname());
					if ((this.getId() == clan.getLeaderId()) && // 攻城戰中 攻擊方盟主死亡
																// 退出戰爭
							(warType == 1) && isInWar && isAttackClan) {
						final String enemyClanName = war.getEnemyClanName(this.getClanname());
						if (enemyClanName != null) {
							war.ceaseWar(this.getClanname(), enemyClanName); // 結束
						}
						break;
					}
				}
			}
		}

		int castleId = 0;
		boolean isNowWar = false;
		castleId = L1CastleLocation.getCastleIdByArea(this);
		if (castleId != 0) { // 戰爭範圍旗幟內城堡ID
			isNowWar = ServerWarExecutor.get().isNowWar(castleId);
		}
		return isNowWar;
	}

	/**
	 * 是否參加血盟戰中
	 * 
	 * @param lastAttacker
	 * @return
	 */
	public boolean simWarResult(L1Character lastAttacker) {
		if (getClanid() == 0) {
			return false;
		}

		L1PcInstance attacker = null;
		String enemyClanName = null;
		boolean sameWar = false;

		if ((lastAttacker instanceof L1PcInstance)) {
			attacker = (L1PcInstance) lastAttacker;
		} else if ((lastAttacker instanceof L1PetInstance)) {
			attacker = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
		} else if ((lastAttacker instanceof L1SummonInstance)) {
			attacker = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
		} else if ((lastAttacker instanceof L1IllusoryInstance)) {
			attacker = (L1PcInstance) ((L1IllusoryInstance) lastAttacker).getMaster();
		} else if ((lastAttacker instanceof L1EffectInstance)) {
			attacker = (L1PcInstance) ((L1EffectInstance) lastAttacker).getMaster();
		} else {
			return false;
		}

		L1Clan clan = WorldClan.get().getClan(getClanname());

		for (L1War war : WorldWar.get().getWarList()) {
			int warType = war.getWarType();
			if (warType != 1) {
				boolean isInWar = war.checkClanInWar(getClanname());
				if (isInWar) {
					if ((attacker != null) && (attacker.getClanid() != 0)) {
						sameWar = war.checkClanInSameWar(getClanname(), attacker.getClanname());
					}

					if (getId() == clan.getLeaderId()) {
						enemyClanName = war.getEnemyClanName(getClanname());
						if (enemyClanName != null) {
							war.ceaseWar(getClanname(), enemyClanName);
						}
					}

					if ((warType == 2) && (sameWar))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * 恢復經驗值
	 */
	public void resExp() {
		int oldLevel = getLevel();
		long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		long exp = 0L;
		switch (oldLevel) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
		case 39:
		case 40:
		case 41:
		case 42:
		case 43:
		case 44:
			exp = (long) (needExp * 0.05D);
			break;
		case 45:
			exp = (long) (needExp * 0.045D);
			break;
		case 46:
			exp = (long) (needExp * 0.04D);
			break;
		case 47:
			exp = (long) (needExp * 0.035D);
			break;
		case 48:
			exp = (long) (needExp * 0.03D);
			break;
		case 49:
		case 50:
		case 51:
		case 52:
		case 53:
		case 54:
		case 55:
		case 56:
		case 57:
		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
		case 65:
		case 66:
		case 67:
		case 68:
		case 69:
		case 70:
		case 71:
		case 72:
		case 73:
		case 74:
		case 75:
		case 76:
		case 77:
		case 78:
		case 79:
		case 80:
		case 81:
		case 82:
		case 83:
		case 84:
		case 85:
		case 86:
		case 87:
		case 88:
		case 89:
			exp = (long) (needExp * 0.025D);
			break;
		default:
			exp = (long) (needExp * 0.025D);
		}

		if (exp == 0L) {
			return;
		}
		addExp(exp);
	}

	/**
	 * 死亡損失經驗值
	 * 
	 * @return
	 */
	private long deathPenalty() {
		int oldLevel = getLevel();
		long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		long exp = 0L;
		switch (oldLevel) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
		case 10:
			exp = 0L;
			break;
		case 11:
		case 12:
		case 13:
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
		case 24:
		case 25:
		case 26:
		case 27:
		case 28:
		case 29:
		case 30:
		case 31:
		case 32:
		case 33:
		case 34:
		case 35:
		case 36:
		case 37:
		case 38:
		case 39:
		case 40:
		case 41:
		case 42:
		case 43:
		case 44:
			exp = (long) (needExp * 0.1D);
			break;
		case 45:
			exp = (long) (needExp * 0.09D);
			break;
		case 46:
			exp = (long) (needExp * 0.08D);
			break;
		case 47:
			exp = (long) (needExp * 0.07D);
			break;
		case 48:
			exp = (long) (needExp * 0.06D);
			break;
		case 49:
			exp = (long) (needExp * 0.05D);
			break;
		default:
			exp = (long) (needExp * 0.05D);
		}

		if (exp == 0L) {
			return 0L;
		}
		addExp(-exp);
		return exp;
	}

	public int getOriginalEr() {
		return _originalEr;
	}

	public int getEr() {
		if (hasSkillEffect(174)) {// 精準射擊
			return 0;
		}

		int er = 0;
		// if (isKnight() || isWarrior()) {
		// er = getLevel() >> 2;
		// } else if ((isCrown()) || (isElf())) {
		// er = getLevel() >> 3;
		// } else if (isDarkelf()) {
		// er = getLevel() / 6;
		// } else if (isWizard()) {
		// er = getLevel() / 10;
		// } else if (isDragonKnight()) {
		// er = getLevel() / 7;
		// } else if (isIllusionist()) {
		// er = getLevel() / 9;
		// }
		if (isKnight() || isDarkelf() || isWarrior()) {
			er = getLevel() / 4;
		} else if ((isCrown()) || (isElf())) {
			er = getLevel() / 6;
		} else if (isWizard()) {
			er = getLevel() / 10;
		} else if (isDragonKnight()) {
			er = getLevel() / 5;
		} else if (isIllusionist()) {
			er = getLevel() / 9;
		}

		// er += (this.getDex() - 8) >> 1;/// 2;
		// XXX 7.6屬性 ADD
		er += L1ClassFeature.calcDexEr(this.getDex()); // 敏捷影響ER XXX 7.6C add

		er += getOriginalEr();

		if (this.hasSkillEffect(AQUA_PROTECTER)) {// 水之防護
			er += 5;
		}

		if (this.hasSkillEffect(DRESS_EVASION)) {// 迴避提升
			er += 12;
		}

		if (this.hasSkillEffect(SOLID_CARRIAGE)) {// 堅固防護
			er += 15;
		}

		return er;
	}

	public L1ItemInstance getWeapon() {
		return _weapon;
	}

	public void setWeapon(L1ItemInstance weapon) {
		_weapon = weapon;
	}

	public L1PcQuest getQuest() {
		return _quest;
	}

	public L1ActionPc getAction() {
		return _action;
	}

	public L1ActionPet getActionPet() {
		return _actionPet;
	}

	public L1ActionSummon getActionSummon() {
		return _actionSummon;
	}

	public boolean isCrown() {
		return (getClassId() == 0) || (getClassId() == 1);
	}

	public boolean isKnight() {
		return (getClassId() == 61) || (getClassId() == 48);
	}

	public boolean isElf() {
		return (getClassId() == 138) || (getClassId() == 37);
	}

	public boolean isWizard() {
		return (getClassId() == 734) || (getClassId() == 1186);
	}

	public boolean isDarkelf() {
		return (getClassId() == 2786) || (getClassId() == 2796);
	}

	public boolean isDragonKnight() {
		return (getClassId() == 6658) || (getClassId() == 6661);
	}

	public boolean isIllusionist() {
		return (getClassId() == 6671) || (getClassId() == 6650);
	}

	public boolean isWarrior() {
		return (getClassId() == CLASSID_WARRIOR_MALE) || (getClassId() == CLASSID_WARRIOR_FEMALE);
	}

	public String getAccountName() {
		return _accountName;
	}

	public void setAccountName(String s) {
		_accountName = s;
	}

	public short getBaseMaxHp() {
		return _baseMaxHp;
	}

	public void addBaseMaxHp(short i) {
		i = (short) (i + _baseMaxHp);
		if (i >= 32767) {
			i = 32767;
		} else if (i < 1) {
			i = 1;
		}
		addMaxHp(i - _baseMaxHp);
		_baseMaxHp = i;
	}

	public short getBaseMaxMp() {
		return _baseMaxMp;
	}

	public void addBaseMaxMp(short i) {
		i = (short) (i + _baseMaxMp);
		if (i >= 32767) {
			i = 32767;
		} else if (i < 1) {
			i = 1;
		}
		addMaxMp(i - _baseMaxMp);
		_baseMaxMp = i;
	}

	public int getBaseAc() {
		return _baseAc;
	}

	public int getOriginalAc() {
		return _originalAc;
	}

	public int getBaseStr() {
		return _baseStr;
	}

	public void addBaseStr(int i) {
		i += _baseStr;
		if (i >= 254) {
			i = 254;
		} else if (i < 1) {
			i = 1;
		}
		addStr(i - _baseStr);
		_baseStr = i;
	}

	public int getBaseCon() {
		return _baseCon;
	}

	public void addBaseCon(int i) {
		i += _baseCon;
		if (i >= 254) {
			i = 254;
		} else if (i < 1) {
			i = 1;
		}
		addCon(i - _baseCon);
		_baseCon = i;
	}

	public int getBaseDex() {
		return _baseDex;
	}

	public void addBaseDex(int i) {
		i += _baseDex;
		if (i >= 254) {
			i = 254;
		} else if (i < 1) {
			i = 1;
		}
		addDex(i - _baseDex);
		_baseDex = i;
	}

	public int getBaseCha() {
		return _baseCha;
	}

	public void addBaseCha(int i) {
		i += _baseCha;
		if (i >= 254) {
			i = 254;
		} else if (i < 1) {
			i = 1;
		}
		addCha(i - _baseCha);
		_baseCha = i;
	}

	public int getBaseInt() {
		return _baseInt;
	}

	public void addBaseInt(int i) {
		i += _baseInt;
		if (i >= 254) {
			i = 254;
		} else if (i < 1) {
			i = 1;
		}
		addInt(i - _baseInt);
		_baseInt = i;
	}

	public int getBaseWis() {
		return _baseWis;
	}

	public void addBaseWis(int i) {
		i += _baseWis;
		if (i >= 254) {
			i = 254;
		} else if (i < 1) {
			i = 1;
		}
		addWis(i - _baseWis);
		_baseWis = i;
	}

	public int getOriginalStr() {
		return _originalStr;
	}

	public void setOriginalStr(int i) {
		_originalStr = i;
	}

	public int getOriginalCon() {
		return _originalCon;
	}

	public void setOriginalCon(int i) {
		_originalCon = i;
	}

	public int getOriginalDex() {
		return _originalDex;
	}

	public void setOriginalDex(int i) {
		_originalDex = i;
	}

	public int getOriginalCha() {
		return _originalCha;
	}

	public void setOriginalCha(int i) {
		_originalCha = i;
	}

	public int getOriginalInt() {
		return _originalInt;
	}

	public void setOriginalInt(int i) {
		_originalInt = i;
	}

	public int getOriginalWis() {
		return _originalWis;
	}

	public void setOriginalWis(int i) {
		_originalWis = i;
	}

	public int getOriginalDmgup() {
		return _originalDmgup;
	}

	public int getOriginalBowDmgup() {
		return _originalBowDmgup;
	}

	public int getOriginalHitup() {
		return _originalHitup;
	}

	public int getOriginalBowHitup() {
		return _originalHitup + _originalBowHitup;
	}

	public int getOriginalMr() {
		return _originalMr;
	}

	private int _magicHit = 0; // 魔法命中

	/**
	 * 魔法命中
	 * @param i
	 */
	public void addMagicHit(final int i) {
		_magicHit += i;
	}

	/**
	 * 魔法命中
	 * @return
	 */
	public int getMagicHit() {
		return _magicHit;
	}

	public void addOriginalMagicCritical(int i) { // 增加魔法暴擊率
		_originalMagicCritical += i;
	}

	public int getOriginalMagicCritical() {
		return _originalMagicCritical;
	}

	public int getOriginalMagicConsumeReduction() {
		return _originalMagicConsumeReduction;
	}

	public int getOriginalHpup() {
		return _originalHpup;
	}

	public int getOriginalMpup() {
		return _originalMpup;
	}

	public int getBaseDmgup() {
		return _baseDmgup;
	}

	public int getBaseBowDmgup() {
		return _baseBowDmgup;
	}

	public int getBaseHitup() {
		return _baseHitup;
	}

	public int getBaseBowHitup() {
		return _baseBowHitup;
	}

	public int getBaseMr() {
		return _baseMr;
	}

	public int getAdvenHp() {
		return _advenHp;
	}

	public void setAdvenHp(int i) {
		_advenHp = i;
	}

	public int getAdvenMp() {
		return _advenMp;
	}

	public void setAdvenMp(int i) {
		_advenMp = i;
	}

	public int getHighLevel() {
		return _highLevel;
	}

	public void setHighLevel(int i) {
		_highLevel = i;
	}

	public int getBonusStats() {
		return _bonusStats;
	}

	public void setBonusStats(int i) {
		_bonusStats = i;
	}

	public int getOtherStats() {
		return _otherStats;
	}

	public void setOtherStats(int i) {
		_otherStats = i;
	}

	public int getAddPoint() {
		return _addPoint;
	}

	public void setAddPoint(int i) {
		_addPoint = i;
	}

	public int getDelPoint() {
		return _delPoint;
	}

	public void setDelPoint(int i) {
		_delPoint = i;
	}

	public int getElixirStats() {
		return _elixirStats;
	}

	public void setElixirStats(int i) {
		_elixirStats = i;
	}

	public int getElfAttr() {
		return _elfAttr;
	}

	public void setElfAttr(int i) {
		_elfAttr = i;
	}

	private int _elfAttrResetCount; // 精靈遺忘屬性技能次數

	/**
	 * 精靈遺忘屬性技能次數
	 * @return
	 */
	public int getElfAttrResetCount() {
		return _elfAttrResetCount;
	}

	/**
	 * 精靈遺忘屬性技能次數
	 * @param i
	 */
	public void setElfAttrResetCount(int i) {
		_elfAttrResetCount = i;
	}

	public int getExpRes() {
		return _expRes;
	}

	public void setExpRes(int i) {
		_expRes = i;
	}

	public int getPartnerId() {
		return _partnerId;
	}

	public void setPartnerId(int i) {
		_partnerId = i;
	}

	public int getOnlineStatus() {
		return _onlineStatus;
	}

	public void setOnlineStatus(int i) {
		_onlineStatus = i;
	}

	public int getHomeTownId() {
		return _homeTownId;
	}

	public void setHomeTownId(int i) {
		_homeTownId = i;
	}

	// 村莊貢獻度
	public int getContribution() {
		return _contribution;
	}

	public void setContribution(int i) {
		_contribution = i;
	}

	// 村莊稅收支付
	public int getPay() {
		return _pay;
	}

	public void setPay(int i) {
		_pay = i;
	}

	public int getHellTime() {
		return _hellTime;
	}

	public void setHellTime(int i) {
		_hellTime = i;
	}

	private double _GF;

	public void addGF(int i) {
		if (i > 0)
			_GF = DoubleUtil.sum(_GF, (double) i / 100D);
		else
			_GF = DoubleUtil.sub(_GF, (double) (i * -1) / 100D);
	}

	public double getGF() {
		if (_GF < 0.0D)
			return 0.0D;
		else
			return _GF;
	}

	public boolean isBanned() {
		return _banned;
	}

	public void setBanned(boolean flag) {
		_banned = flag;
	}

	public int get_food() {
		return _food;
	}

	public void set_food(int i) {
		if (i > 225) {
			i = 225;
		} else if (i < 0) {
			i = 0;
		}
		_food = i;
		if (_food == 225) {
			Calendar cal = Calendar.getInstance();
			long h_time = cal.getTimeInMillis() / 1000L;
			set_h_time(h_time);
		} else {
			set_h_time(-1L);
		}
	}

	public L1EquipmentSlot getEquipSlot() {
		return _equipSlot;
	}

	public static L1PcInstance load(String charName) {
		L1PcInstance result = null;
		try {
			result = CharacterTable.get().loadCharacter(charName);
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return result;
	}

	/**
	 * 人物資料存檔
	 *
	 * @throws Exception
	 */
	public void save() throws Exception {
		if (isGhost()) {
			return;
		}

		if (isInCharReset()) {
			return;
		}

		if (_other != null) {
			CharOtherReading.get().storeOther(getId(), _other);
		}

		CharacterTable.get().storeCharacter(this);
	}

	/**
	 * 人物VIP資料存檔
	 *
	 * @throws Exception
	 */
	public void saveVip() throws Exception {

		CharacterTable.get().updateVipTime(this);
	}

	/**
	 * 背包資料存檔
	 */
	public void saveInventory() {
		for (L1ItemInstance item : getInventory().getItems())
			getInventory().saveItem(item, item.getRecordingColumns());
	}

	public double getMaxWeight() {
		int str = getStr();
		int con = getCon();
		//double maxWeight = 150.0D * Math.floor(0.6D * str + 0.4D * con + 1.0D) * get_weightUP();
		// XXX 7.6 公式更新
		// 本身負重能力
		//double maxWeight = L1ClassFeature.calcAbilityMaxWeight(this.getStr(), this.getCon());
		double maxWeight = L1ClassFeature.calcAbilityMaxWeight(str, con);

		double weightReductionByArmor = getWeightReduction();
		weightReductionByArmor /= 100.0D;

		int weightReductionByMagic = 0;
		if ((hasSkillEffect(DECREASE_WEIGHT)) // 負重強化
				|| (hasSkillEffect(JOY_OF_PAIN)) // 降低負重
		) {
			weightReductionByMagic = 180;

		} else if (hasSkillEffect(ELVEN_GRAVITY)) { // 精靈重力
			weightReductionByMagic = 300;
		}

		// XXX 7.6取消計算初始能力負重減免
		//double originalWeightReduction = 0.0D;
		//originalWeightReduction += 0.04D * (getOriginalStrWeightReduction() + getOriginalConWeightReduction());

		//double weightReduction = 1.0D + weightReductionByArmor + originalWeightReduction;
		double weightReduction = 1.0D + weightReductionByArmor + (get_weightUP() / 100.0D);// 7.6

		maxWeight *= weightReduction;

		maxWeight += weightReductionByMagic;

		maxWeight *= ConfigRate.RATE_WEIGHT_LIMIT; // 服務器提高設置

		return maxWeight;
	}

	/**
	 * 是否具有生命之樹果實效果
	 * 
	 * @return
	 */
	public boolean isRibrave() { // 生命之樹果實 移速 * 1.15
		return hasSkillEffect(STATUS_RIBRAVE);
	}

	/**
	 * 神聖疾走效果 行走加速效果 風之疾走效果 生命之樹果實效果
	 * 
	 * @return
	 */
	public boolean isFastMovable() {
		return (this.hasSkillEffect(HOLY_WALK)
				|| this.hasSkillEffect(MOVING_ACCELERATION)
				// || this.hasSkillEffect(WIND_WALK)
				|| this.hasSkillEffect(STATUS_RIBRAVE));
	}

	public boolean isFastAttackable() {
		return false;
	}

	/**
	 * 精靈波濤之水效果BraveSpeed=10-11-12
	 * @return
	 */
	public boolean isElfFOCUS() {
		return hasSkillEffect(FOCUS_WAVE);
	}

	/**
	 * 精靈狂怒之風效果BraveSpeed=9
	 * @return
	 */
	public boolean isElfHURRICANE() {
		return hasSkillEffect(HURRICANE);
	}

	/**
	 * 是否具有勇敢藥水效果
	 * 
	 * @return
	 */
	public boolean isBrave() {
		return hasSkillEffect(STATUS_BRAVE)
				|| (hasSkillEffect(FIRE_BLESS)) // 舞躍之火
				|| (hasSkillEffect(SAND_STORM)) // 奔崩之土
				|| (hasSkillEffect(BLOODLUST));
	}

	/**
	 * 荒神加速效果
	 * 
	 * @return
	 */
	public boolean isSuperBrave() {
		return hasSkillEffect(STATUS_BRAVE2);
	}

	/**
	 * 是否具有精靈餅乾效果
	 * 
	 * @return
	 */
	public boolean isElfBrave() {
		return hasSkillEffect(STATUS_ELFBRAVE);
	}

	/**
	 * 是否具有三段加速效果
	 * 
	 * @return
	 */
	public boolean isBraveX() {
		return hasSkillEffect(STATUS_BRAVE3);
	}

	/**
	 * 是否具有綠色藥水加速效果
	 * 
	 * @return
	 */
	public boolean isHaste() {
		return (hasSkillEffect(STATUS_HASTE)) || (hasSkillEffect(HASTE)) || (hasSkillEffect(GREATER_HASTE))
				|| (getMoveSpeed() == 1);
	}

	public boolean isInvisDelay() {
		return invisDelayCounter > 0;
	}

	public void addInvisDelayCounter(int counter) {
		synchronized (_invisTimerMonitor) {
			invisDelayCounter += counter;
		}
	}

	public void beginInvisTimer() {
		addInvisDelayCounter(1);
		GeneralThreadPool.get().pcSchedule(new L1PcInvisDelay(getId()), DELAY_INVIS);
	}

	public synchronized void addLawful(int i) {
		int lawful = getLawful() + i;
		if (lawful > 32767) {
			lawful = 32767;
		} else if (lawful < -32768) {
			lawful = -32768;
		}
		setLawful(lawful);
		onChangeLawful();
	}

	public synchronized void addExp(long exp) {
		long newexp = _exp + exp;
		setExp(newexp);
		onChangeExp();
	}

	public synchronized void addContribution(int contribution) {
		_contribution += contribution;
	}

	private void levelUp(int gap) {
		resetLevel();
		for (int i = 0; i < gap; i++) {
			/*short randomHp = CalcStat.calcStatHp(getType(), getBaseMaxHp(), getBaseCon(), getOriginalHpup(), getType());
			short randomMp = CalcStat.calcStatMp(getType(), getBaseMaxMp(), getBaseWis(), getOriginalMpup());
			addBaseMaxHp(randomHp);
			addBaseMaxMp(randomMp);*/
			// XXX 7.6屬性 ADD
			final int randomHp = L1ClassFeature.calcStatHp(getType(), getBaseMaxHp(), (byte) getBaseCon());
			final int randomMp = L1ClassFeature.calcStatMp(getType(), getBaseMaxMp(), (byte) getBaseWis());
			addBaseMaxHp((short) randomHp);
			addBaseMaxMp((short) randomMp);
		}
		if ((ConfigAlt.METE_GIVE_POTION) && (getLevel() >= ConfigAlt.METE_LEVEL)
				&& (getHighLevel() < ConfigAlt.METE_LEVEL)) {
			try {
				L1Item l1item = ItemTable.get().getTemplate(43000);
				if ((l1item != null) && (getInventory().checkAddItem(l1item, 1L) == 0)) {
					getInventory().storeItem(43000, 1L);
					sendPackets(new S_ServerMessage(403, l1item.getName()));
				} else {
					sendPackets(new S_SystemMessage("無法獲得轉生藥水。可能此道具不存在！"));
				}
			} catch (Exception e) {
				sendPackets(new S_SystemMessage("無法獲得轉生藥水。可能此道具不存在！"));
			}
		}

		resetBaseHitup();
		resetBaseDmgup();
		resetBaseAc();
		resetBaseMr();
		william.L1AutoLearnSkill.forAutoLearnSkill(this);
		if (getLevel() > getHighLevel()) {
			setHighLevel(getLevel());
		}
		setCurrentHp(getMaxHp());
		setCurrentMp(getMaxMp());

		try {
			save();
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {

			// 升級經驗獎勵狀態
			if (ConfigOther.LEVEL_UP) {
				if (getLevel() >= ConfigOther.LEVEL_UP_LV) {
					if (!hasSkillEffect(L1SkillId.LEVEL_UP_BONUS)) {
						final int bufftime = 3 * 60 * 60;
						setSkillEffect(L1SkillId.LEVEL_UP_BONUS, bufftime * 1000);
						sendPackets(new S_PacketBox(S_PacketBox.ENABLE_QUAT, 0xad, 1, (((bufftime / 8) + 1) / 2)));
					}
				}
			}

			showWindows();
			getApprentice();
			sendPackets(new S_OwnCharStatus(this));
			Reward.getItem(this);
			MapLevelTable.get().get_level(getMapId(), this);
			if (JISHIJIANLI.START) {
				L1WilliamLimitedReward.check_Task_For_Level(this);
			}


			if ((ConfigAlt.APPRENTICE_SWITCH) && (getApprentice() != null)
					&& (getApprentice().getMaster().getId() != getId()) && (getLevel() >= ConfigAlt.APPRENTICE_LEVEL)) {
				for (L1PcInstance character : getApprentice().getTotalList()) {
					if (character.getId() == getId()) {
						getApprentice().getTotalList().remove(character);
						break;
					}
				}
				CharApprenticeTable.getInstance().updateApprentice(getApprentice().getMaster().getId(),
						getApprentice().getTotalList());
				setApprentice(null);

				if (!getInventory().checkItem(ConfigAlt.APPRENTICE_ITEM_ID))
					CreateNewItem.createNewItem(this, ConfigAlt.APPRENTICE_ITEM_ID, 1L);
			}

			QuestNewTable.getInstance().updateQuest(this); // 官服任務系統

			// 7.6
			if (getLevel() >= 51
					&& (getLevel() - 50 > getBonusStats())
					|| (getLevel() >= 51 && (getLevel() - 50 > getBonusStats() - 49))) {
				if ((getBaseStr() + getBaseDex() + getBaseCon() + getBaseInt()
						+ getBaseWis() + getBaseCha()) < (ConfigAlt.POWER * 6)) {
					// sendPackets(new S_bonusstats(getId(), 1));
					int bonus = (getLevel() - 50) - getBonusStats();// 可以點的點數
																	// XXX 7.6C
																	// ADD
					sendPackets(new S_Message_YN(479, bonus));
				}
			}

			// XXX 能力基本資訊-力量
			this.sendPackets(new S_StrDetails(2, L1ClassFeature.calcStrDmg(
					this.getStr(), this.getBaseStr()), L1ClassFeature
					.calcStrHit(this.getStr(), this.getBaseStr()),
					L1ClassFeature.calcStrDmgCritical(this.getStr(),
							this.getBaseStr()), L1ClassFeature
							.calcAbilityMaxWeight(this.getStr(), this.getCon())));

			// XXX 重量程度資訊
			this.sendPackets(new S_WeightStatus(this.getInventory()
					.getWeight100(), this.getInventory().getWeight(),
					(int) this.getMaxWeight()));

			// XXX 能力基本資訊-智力
			this.sendPackets(new S_IntDetails(2, L1ClassFeature
					.calcIntMagicDmg(this.getInt(), this.getBaseInt()),
					L1ClassFeature.calcIntMagicHit(this.getInt(),
							this.getBaseInt()), L1ClassFeature
							.calcIntMagicCritical(this.getInt(),
									this.getBaseInt()), L1ClassFeature
							.calcIntMagicBonus(this.getType(), this.getInt()),
					L1ClassFeature.calcIntMagicConsumeReduction(this.getInt())));

			// XXX 能力基本資訊-精神
			this.sendPackets(new S_WisDetails(2, L1ClassFeature.calcWisMpr(
					this.getWis(), this.getBaseWis()), L1ClassFeature
					.calcWisPotionMpr(this.getWis(), this.getBaseWis()),
					L1ClassFeature.calcStatMr(this.getWis())
							+ L1ClassFeature.newClassFeature(this.getType())
									.getClassOriginalMr(), L1ClassFeature
							.calcBaseWisLevUpMpUp(this.getType(),
									this.getBaseWis())));

			// XXX 能力基本資訊-敏捷
			this.sendPackets(new S_DexDetails(2, L1ClassFeature.calcDexDmg(
					this.getDex(), this.getBaseDex()), L1ClassFeature
					.calcDexHit(this.getDex(), this.getBaseDex()),
					L1ClassFeature.calcDexDmgCritical(this.getDex(),
							this.getBaseDex()), L1ClassFeature.calcDexAc(this
							.getDex()), L1ClassFeature.calcDexEr(this.getDex())));

			// XXX 7.6 ADD
			this.sendPackets(new S_PacketBoxCharEr(this));// 角色迴避率更新

			// XXX 能力基本資訊-體質
			this.sendPackets(new S_ConDetails(2, L1ClassFeature.calcConHpr(
					this.getCon(), this.getBaseCon()), L1ClassFeature
					.calcConPotionHpr(this.getCon(), this.getBaseCon()),
					L1ClassFeature.calcAbilityMaxWeight(this.getStr(),
							this.getCon()), L1ClassFeature
							.calcBaseClassLevUpHpUp(this.getType())
							+ L1ClassFeature.calcBaseConLevUpExtraHpUp(
									this.getType(), this.getBaseCon())));

			// XXX 重量程度資訊
			this.sendPackets(new S_WeightStatus(this.getInventory()
					.getWeight100(), this.getInventory().getWeight(),
					(int) this.getMaxWeight()));
			
		}
	}

	public void showWindows() {
		/*
		 * if (QuestSet.ISQUEST) { //src016 int quest =
		 * QuestTable.get().levelQuest(this, getLevel()); if (quest > 0) {
		 * isWindows(); } else if (power()) { sendPackets(new
		 * S_Bonusstats(getId())); }
		 * 
		 * } else if (power()) { sendPackets(new S_Bonusstats(getId())); }
		 */
		if (power()) {
			//sendPackets(new S_Bonusstats(getId()));
		}
	}

	public void isWindows() {
		if (power()) {
			sendPackets(new S_NPCTalkReturn(getId(), "y_qs_10"));
		} else
			sendPackets(new S_NPCTalkReturn(getId(), "y_qs_00"));
	}

	public boolean power() {
		/*if ((getLevel() >= 51) && (getLevel() - 50 > getBonusStats())) {
			int power = getBaseStr() + getBaseDex() + getBaseCon() + getBaseInt() + getBaseWis() + getBaseCha();
			if (power < ConfigAlt.POWER * 6) {
				return true;
			}
		}

		return false;*/
		if (getLevel() >= 51) {
			if ((getLevel() - 50) > getBonusStats()) {
				final int power = getBaseStr() + getBaseDex() + getBaseCon()
						+ getBaseInt() + getBaseWis() + getBaseCha();
				if (power < (ConfigAlt.POWER * 6)) {
					return true;
				}
			}
		}
		return false;
	}

	private void levelDown(int gap) {
		resetLevel();

		for (int i = 0; i > gap; i--) {
			// short randomHp = CalcStat.calcStatHp(getType(), 0, getBaseCon(), getOriginalHpup(), getType());
			// short randomMp = CalcStat.calcStatMp(getType(), 0, getBaseWis(), getOriginalMpup());
			// XXX 7.6屬性 ADD
			final int randomHp = L1ClassFeature.calcStatHp(getType(), 0, (byte) getBaseCon());
			final int randomMp = L1ClassFeature.calcStatMp(getType(), 0, (byte) getBaseWis());

			addBaseMaxHp((short) -randomHp);
			addBaseMaxMp((short) -randomMp);
		}

		if (getLevel() == 1) {
			// int initHp = CalcInitHpMp.calcInitHp(this);
			// int initMp = CalcInitHpMp.calcInitMp(this);
			// XXX 7.6屬性 ADD
			final int initHp = L1ClassFeature.calcInitHp(getType());
			final int initMp = L1ClassFeature.calcInitMp(getType(), getWis());

			addBaseMaxHp((short) -getBaseMaxHp());
			addBaseMaxHp((short) initHp);
			setCurrentHp((short) initHp);
			addBaseMaxMp((short) -getBaseMaxMp());
			addBaseMaxMp((short) initMp);
			setCurrentMp((short) initMp);
		}

		resetBaseHitup();
		resetBaseDmgup();
		resetBaseAc();
		resetBaseMr();
		getApprentice();

		try {
			// 存入資料
			save();
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		} finally {

			// 更新人物資訊
			sendPackets(new S_OwnCharStatus(this));

			// XXX 7.6 ADD
			this.sendPackets(new S_PacketBoxCharEr(this));// 角色迴避率更新

			// 地圖等級限制判斷
			MapLevelTable.get().get_level(getMapId(), this);

			if ((ConfigAlt.APPRENTICE_SWITCH) && (getApprentice() != null)
					&& (getApprentice().getMaster().getId() == getId()) && (getLevel() < ConfigAlt.APPRENTICE_LEVEL)) {
				L1Apprentice apprentice = CharApprenticeTable.getInstance().getApprentice(this);
				if (apprentice != null) {
					CharApprenticeTable.getInstance().deleteApprentice(getId());
					setApprentice(null);
				}
			}
		}
	}

	public boolean isGhost() {
		return _ghost;
	}

	private void setGhost(boolean flag) {
		_ghost = flag;
	}

	public int get_ghostTime() {
		return _ghostTime;
	}

	public void set_ghostTime(int ghostTime) {
		_ghostTime = ghostTime;
	}

	public boolean isGhostCanTalk() {
		return _ghostCanTalk;
	}

	private void setGhostCanTalk(boolean flag) {
		_ghostCanTalk = flag;
	}

	public boolean isReserveGhost() {
		return _isReserveGhost;
	}

	public void setReserveGhost(boolean flag) {
		_isReserveGhost = flag;
	}

	public void beginGhost(int locx, int locy, short mapid, boolean canTalk) {
		beginGhost(locx, locy, mapid, canTalk, 0);
	}

	public void beginGhost(int locx, int locy, short mapid, boolean canTalk, int sec) {
		if (isGhost()) {
			return;
		}
		setGhost(true);
		_ghostSaveLocX = getX();
		_ghostSaveLocY = getY();
		_ghostSaveMapId = getMapId();
		_ghostSaveHeading = getHeading();
		setGhostCanTalk(canTalk);
		L1Teleport.teleport(this, locx, locy, mapid, 5, true);
		if (sec > 0) {
			this.set_ghostTime(sec * 1000);
		}
	}

	public void makeReadyEndGhost() {
		setReserveGhost(true);
		L1Teleport.teleport(this, _ghostSaveLocX, _ghostSaveLocY, _ghostSaveMapId, _ghostSaveHeading, true);
	}

	public void makeReadyEndGhost(boolean effectble) {
		setReserveGhost(true);
		L1Teleport.teleport(this, _ghostSaveLocX, _ghostSaveLocY, _ghostSaveMapId, _ghostSaveHeading, effectble);
	}

	public void endGhost() {
		set_ghostTime(-1);
		setGhost(false);
		setGhostCanTalk(true);
		setReserveGhost(false);
	}

	/**
	 * 地獄剩餘時間處理
	 * 
	 * @param isFirst
	 *            是否傳送至地獄並計算地獄時間
	 */
	public void beginHell(boolean isFirst) {
		if (this.getMapId() != 666) {// 如果人物不在地獄則傳送至地獄
			int locx = 32701;
			int locy = 32777;
			short mapid = 666;
			L1Teleport.teleport(this, locx, locy, mapid, 5, false);
		}

		if (isFirst) {
			if (get_PKcount() <= 10) {
				setHellTime(300);
			} else {
				setHellTime(300 * (get_PKcount() - 10) + 300);
			}

			sendPackets(new S_BlueMessage(552, String.valueOf(get_PKcount()), String.valueOf(getHellTime() / 60)));

		} else {
			sendPackets(new S_BlueMessage(637, String.valueOf(getHellTime())));
		}
	}

	/**
	 * 傳出地獄
	 */
	public void endHell() {
		int[] loc = L1TownLocation.getGetBackLoc(4);
		L1Teleport.teleport(this, loc[0], loc[1], (short) loc[2], 5, true);
		try {
			save();
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void setPoisonEffect(int effectId) {
		sendPackets(new S_Poison(getId(), effectId));

		if ((!isGmInvis()) && (!isGhost()) && (!isInvisble()))
			broadcastPacketAll(new S_Poison(getId(), effectId));
	}

	public void healHp(int pt) {
		super.healHp(pt);

		sendPackets(new S_HPUpdate(this));
	}

	public int getKarma() {
		return _karma.get();
	}

	public void setKarma(int i) {
		_karma.set(i);
	}

	public void addKarma(int i) {
		synchronized (_karma) {
			_karma.add(i);
			onChangeKarma();
		}
	}

	public int getKarmaLevel() {
		return _karma.getLevel();
	}

	public int getKarmaPercent() {
		return _karma.getPercent();
	}

	public Timestamp getLastPk() {
		return _lastPk;
	}

	public void setLastPk(Timestamp time) {
		_lastPk = time;
	}

	public void setLastPk() {
		_lastPk = new Timestamp(System.currentTimeMillis());
	}

	public boolean isWanted() {
		if (_lastPk == null) {
			return false;
		}

		if (System.currentTimeMillis() - _lastPk.getTime() > 3600000L) {
			setLastPk(null);
			return false;
		}
		return true;
	}

	public Timestamp getLastPkForElf() {
		return _lastPkForElf;
	}

	public void setLastPkForElf(Timestamp time) {
		_lastPkForElf = time;
	}

	public void setLastPkForElf() {
		_lastPkForElf = new Timestamp(System.currentTimeMillis());
	}

	public boolean isWantedForElf() {
		if (_lastPkForElf == null) {
			return false;
		}
		if (System.currentTimeMillis() - _lastPkForElf.getTime() > 86400000L) {
			setLastPkForElf(null);
			return false;
		}
		return true;
	}

	public Timestamp getDeleteTime() {
		return _deleteTime;
	}

	public void setDeleteTime(Timestamp time) {
		_deleteTime = time;
	}

	public int getMagicLevel() {
		return getClassFeature().getMagicLevel(getLevel());
	}

	public double get_weightUP() {
		return _weightUP;
	}

	public void add_weightUP(int i) {
		_weightUP += i / 100.0D;
	}

	public int getWeightReduction() {
		return _weightReduction;
	}

	public void addWeightReduction(int i) {
		_weightReduction += i;
		// XXX 7.6 重量程度資訊
		this.sendPackets(new S_WeightStatus(this.getInventory().getWeight()
				* 100 / (int) this.getMaxWeight(), this.getInventory()
				.getWeight(), (int) this.getMaxWeight()));
	}

	public int getOriginalStrWeightReduction() {
		return _originalStrWeightReduction;
	}

	public int getOriginalConWeightReduction() {
		return _originalConWeightReduction;
	}

	public int getHasteItemEquipped() {
		return _hasteItemEquipped;
	}

	public void addHasteItemEquipped(int i) {
		_hasteItemEquipped += i;
	}

	public void removeHasteSkillEffect() {
		if (hasSkillEffect(29)) {
			removeSkillEffect(29);
		}

		/*if (hasSkillEffect(MASS_SLOW)) { // 集體緩速術 改->冰霜彗星
			removeSkillEffect(MASS_SLOW);
		}

		if (hasSkillEffect(ENTANGLE)) { // 地面障礙 改->大地纏繞
			removeSkillEffect(ENTANGLE);
		}*/

		if (hasSkillEffect(43)) {
			removeSkillEffect(43);
		}

		if (hasSkillEffect(54)) {
			removeSkillEffect(54);
		}

		if (hasSkillEffect(1001))
			removeSkillEffect(1001);
	}

	// 傷害減免
	public int getDamageReductionByArmor() {
		return _damageReductionByArmor;
	}

	public void addDamageReductionByArmor(int i) {
		_damageReductionByArmor += i;
	}

	public int getHitModifierByArmor() {
		return _hitModifierByArmor;
	}

	public void addHitModifierByArmor(int i) {
		_hitModifierByArmor += i;
	}

	public int getDmgModifierByArmor() {
		return _dmgModifierByArmor;
	}

	public void addDmgModifierByArmor(int i) {
		_dmgModifierByArmor += i;
	}

	public int getBowHitModifierByArmor() {
		return _bowHitModifierByArmor;
	}

	public void addBowHitModifierByArmor(int i) {
		_bowHitModifierByArmor += i;
	}

	public int getBowDmgModifierByArmor() {
		return _bowDmgModifierByArmor;
	}

	public void addBowDmgModifierByArmor(int i) {
		_bowDmgModifierByArmor += i;
	}

	private void setGresValid(boolean valid) {
		_gresValid = valid;
	}

	public boolean isGresValid() {
		return _gresValid;
	}

	// l1j-jp釣魚
    public int fishX = 0;

    public int fishY = 0;

	private boolean _isFishing = false;

	public boolean isFishing() {
		return _isFishing;
	}

	public void setFishing(boolean flag) {
		_isFishing = flag;
	}

	private boolean _isFishingReady = false;

	public boolean isFishingReady() {
		return _isFishingReady;
	}

	public void setFishingReady(boolean flag) {
		_isFishingReady = flag;
	}

	private long _fishingTime = 0;

	public long getFishingTime() {
		return _fishingTime;
	}

	public void setFishingTime(long i) {
		_fishingTime = i;
	}

	private L1ItemInstance _fishingitem;

	public L1ItemInstance getFishingItem() {
		return _fishingitem;
	}

	public void setFishingItem(L1ItemInstance item) {
		_fishingitem = item;
	}
	// l1j-jp釣魚 end

	public int getCookingId() {
		return _cookingId;
	}

	public void setCookingId(int i) {
		_cookingId = i;
	}

	public int getDessertId() {
		return _dessertId;
	}

	public void setDessertId(int i) {
		_dessertId = i;
	}

	public void resetBaseDmgup() {
		int newBaseDmgup = 0;
		int newBaseBowDmgup = 0;
		if ((isKnight()) || (isDarkelf()) || (isDragonKnight())) {
			newBaseDmgup = getLevel() / 10;
			newBaseBowDmgup = 0;
		} else if (isElf()) {
			newBaseDmgup = 0;
			newBaseBowDmgup = getLevel() / 10;
		}
		addDmgup(newBaseDmgup - _baseDmgup);
		addBowDmgup(newBaseBowDmgup - _baseBowDmgup);
		_baseDmgup = newBaseDmgup;
		_baseBowDmgup = newBaseBowDmgup;
	}

	public void resetBaseHitup() {
		int newBaseHitup = 0;
		int newBaseBowHitup = 0;
		// 職業命中加乘
		if (isKnight() || isWarrior() || isDarkelf()) {
			newBaseHitup = getLevel() / 6;
			newBaseBowHitup = getLevel() / 6;
		} else if (isCrown()) {
			newBaseHitup = getLevel() / 8;
			newBaseBowHitup = getLevel() / 8;
		} else if (isElf() || isIllusionist() || isDragonKnight()) {
			newBaseHitup = getLevel() / 10;
			newBaseBowHitup = getLevel() / 10;
		} else if (isWizard()) {
			newBaseHitup = getLevel() / 12;
			newBaseBowHitup = getLevel() / 12;
		}

		addHitup(newBaseHitup - _baseHitup);
		addBowHitup(newBaseBowHitup - _baseBowHitup);
		_baseHitup = newBaseHitup;
		_baseBowHitup = newBaseBowHitup;
	}

	public void resetBaseAc() {
		// int newAc = CalcStat.calcAc(getType(), getLevel(), getBaseDex());
		// XXX 7.6屬性 ADD
		int newAc = 10 + CalcStat.calcAc(getType(), getLevel()) + L1ClassFeature.calcDexAc(getDex());
		addAc(newAc - _baseAc);
		_baseAc = newAc;
		sendPackets(new S_OwnCharAttrDef(this));
	}

	public void resetBaseMr() {
		int newMr = 0;
		if (isCrown()) {
			newMr = 10;
		} else if (isElf()) {
			newMr = 25;
		} else if (isWizard()) {
			newMr = 15;
		} else if (isDarkelf()) {
			newMr = 10;
		} else if (isDragonKnight()) {
			newMr = 18;
		} else if (isIllusionist()) {
			newMr = 20;
		}
		// newMr += CalcStat.calcStatMr(getWis());
		// XXX 7.6屬性 ADD
		newMr += L1ClassFeature.calcStatMr(getWis());
		newMr += getLevel() / 2;
		addMr(newMr - _baseMr);
		_baseMr = newMr;
	}

	public void resetLevel() {
		setLevel(ExpTable.getLevelByExp(_exp));

		// 官服任務系統
		for (final L1QuestNew qn : this.getQuestList().values()) {
			if (qn.get達到等級() > 0) {
				qn.updateCurrentLevel(this.getLevel());
			}
		}
	}

	public void resetOriginalHpup() {
		_originalHpup = L1PcOriginal.resetOriginalHpup(this);
	}

	public void resetOriginalMpup() {
		_originalMpup = L1PcOriginal.resetOriginalMpup(this);
	}

	public void resetOriginalStrWeightReduction() {
		_originalStrWeightReduction = L1PcOriginal.resetOriginalStrWeightReduction(this);
	}

	public void resetOriginalDmgup() {
		_originalDmgup = L1PcOriginal.resetOriginalDmgup(this);
	}

	public void resetOriginalConWeightReduction() {
		_originalConWeightReduction = L1PcOriginal.resetOriginalConWeightReduction(this);
	}

	public void resetOriginalBowDmgup() {
		_originalBowDmgup = L1PcOriginal.resetOriginalBowDmgup(this);
	}

	public void resetOriginalHitup() {
		_originalHitup = L1PcOriginal.resetOriginalHitup(this);
	}

	public void resetOriginalBowHitup() {
		_originalBowHitup = L1PcOriginal.resetOriginalBowHitup(this);
	}

	public void resetOriginalMr() {
		_originalMr = L1PcOriginal.resetOriginalMr(this);
		addMr(_originalMr);
	}

	public void resetOriginalMagicConsumeReduction() {
		_originalMagicConsumeReduction = L1PcOriginal.resetOriginalMagicConsumeReduction(this);
	}

	public void resetOriginalAc() {
		_originalAc = L1PcOriginal.resetOriginalAc(this);

		addAc(0 - _originalAc);
	}

	public void resetOriginalEr() {
		_originalEr = L1PcOriginal.resetOriginalEr(this);
	}

	public void resetOriginalHpr() {
		_originalHpr = L1PcOriginal.resetOriginalHpr(this);
	}

	public void resetOriginalMpr() {
		_originalMpr = L1PcOriginal.resetOriginalMpr(this);
	}

	public void refresh() {
		resetLevel();
		resetBaseHitup();
		resetBaseDmgup();
		resetBaseMr();
		resetBaseAc();
		resetOriginalHpup();
		resetOriginalMpup();
		resetOriginalDmgup();
		resetOriginalBowDmgup();
		resetOriginalHitup();
		resetOriginalBowHitup();
		resetOriginalMr();
		resetOriginalMagicConsumeReduction();
		resetOriginalAc();
		resetOriginalEr();
		resetOriginalHpr();
		resetOriginalMpr();
		resetOriginalStrWeightReduction();
		resetOriginalConWeightReduction();
	}

	/**
	 * 人物訊息拒絕清單
	 * 
	 * @return
	 */
	//public L1ExcludingList getExcludingList() {
		//return _excludingList;
	//}
	
	/**
	 * 傳回一個信件的黑名單 // 7.6
	 * 
	 * @return
	 */
	//public L1ExcludingMailList getExcludingMailList() {
		//return _excludingMailList;
	//}

	public int getTeleportX() {
		return _teleportX;
	}

	public void setTeleportX(int i) {
		_teleportX = i;
	}

	public int getTeleportY() {
		return _teleportY;
	}

	public void setTeleportY(int i) {
		_teleportY = i;
	}

	public short getTeleportMapId() {
		return _teleportMapId;
	}

	public void setTeleportMapId(short i) {
		_teleportMapId = i;
	}

	public int getTeleportHeading() {
		return _teleportHeading;
	}

	public void setTeleportHeading(int i) {
		_teleportHeading = i;
	}

	public int getTempCharGfxAtDead() {
		return _tempCharGfxAtDead;
	}

	private void setTempCharGfxAtDead(int i) {
		_tempCharGfxAtDead = i;
	}

	/**
	 * 武器特效動畫 [自己] 開關
	 * 
	 * @return flag true:接收 false:拒絕
	 */
	private boolean _attackme = true;

	public boolean attackme() {
		return this._attackme;
	}

	public void setattackme(final boolean _attackme) {
		this._attackme = _attackme;
	}

	/**
	 * 武器特效動畫 [對方] 開關
	 * 
	 * @return flag true:接收 false:拒絕
	 */
	private boolean _attackhe = false;

	public boolean attackhe() {
		return this._attackhe;
	}

	public void setattackhe(final boolean _attackhe) {
		this._attackhe = _attackhe;
	}

	/**
	 * 套裝特效動畫 [自己] 開關
	 * 
	 * @return flag true:接收 false:拒絕
	 */
	private boolean _armorme = true;

	public boolean armorme() {
		return this._armorme;
	}

	public void setarmorme(final boolean _armorme) {
		this._armorme = _armorme;
	}

	/**
	 * 套裝特效動畫 [對方] 開關
	 * 
	 * @return flag true:接收 false:拒絕
	 */
	private boolean _armorhe = false;

	public boolean armorhe() {
		return this._armorhe;
	}

	public void setarmorhe(final boolean _armorhe) {
		this._armorhe = _armorhe;
	}

	/**
	 * 掉寶公告訊息 [自身] 開關
	 * 
	 * @return flag true:接收 false:拒絕
	 */
	private boolean _droplist = true;

	public boolean droplist() {
		return this._droplist;
	}

	public void setdroplist(final boolean _droplist) {
		this._droplist = _droplist;
	}

	/**
	 * 殺人公告訊息 [自身] 開關
	 * 
	 * @return flag true:接收 false:拒絕
	 */
	private boolean _kill = true;

	public boolean kill() {
		return this._kill;
	}

	public void setkill(final boolean _kill) {
		this._kill = _kill;
	}

	private int _weaponMD;

	/**
	 * 增加古文字魔法武器傷害
	 * 
	 * @param lift
	 */
	public void addweaponMD(int weaponMD) {
		_weaponMD += weaponMD;
	}

	public int getweaponMD() {
		return _weaponMD;
	}

	private int _weaponMDC;

	/**
	 * 增加古文字魔法武器機率
	 * 
	 * @param lift
	 */
	public void addweaponMDC(int weaponMDC) {
		_weaponMDC += weaponMDC;
	}

	public int getweaponMDC() {
		return _weaponMDC;
	}

	private int _reducedmg;

	/**
	 * 增加古文字魔法武器機率
	 * 
	 * @param lift
	 */
	public void addreducedmg(int reducedmg) {
		_reducedmg += reducedmg;
	}

	public int getreducedmg() {
		int damageReduction = 0;
		if (_reducedmg > 10) {
			damageReduction = 10 + (_random.nextInt((_reducedmg - 10)) + 1);

		} else {
			damageReduction = _reducedmg;
		}
		return damageReduction;
	}

	private int _reduceMdmg;

	/**
	 * 增加古文字魔法武器機率
	 * 
	 * @param lift
	 */
	public void addreduceMdmg(int reduceMdmg) {
		_reduceMdmg += reduceMdmg;
	}

	public int getreduceMdmg() {
		int MdamageReduction = 0;
		if (_reduceMdmg > 10) {
			MdamageReduction = 10 + (_random.nextInt((_reduceMdmg - 10)) + 1);

		} else {
			MdamageReduction = _reduceMdmg;
		}
		return MdamageReduction;
	}

	/**
	 * 全秘密語(收聽)
	 * 
	 * @return flag true:接收 false:拒絕
	 */
	public boolean isCanWhisper() {
		return _isCanWhisper;
	}

	public void setCanWhisper(boolean flag) {
		_isCanWhisper = flag;
	}

	public boolean isShowTradeChat() {
		return _isShowTradeChat;
	}

	public void setShowTradeChat(boolean flag) {
		_isShowTradeChat = flag;
	}

	public boolean isShowWorldChat() {
		return _isShowWorldChat;
	}

	public void setShowWorldChat(boolean flag) {
		_isShowWorldChat = flag;
	}

	public int getFightId() {
		return _fightId;
	}

	public void setFightId(int i) {
		_fightId = i;
	}

	public void checkChatInterval() {
		long nowChatTimeInMillis = System.currentTimeMillis();
		if (_chatCount == 0) {
			_chatCount = ((byte) (_chatCount + 1));
			_oldChatTimeInMillis = nowChatTimeInMillis;
			return;
		}

		long chatInterval = nowChatTimeInMillis - _oldChatTimeInMillis;

		if (chatInterval > 2000L) {
			_chatCount = 0;
			_oldChatTimeInMillis = 0L;
		} else {
			if (_chatCount >= 3) {
				setSkillEffect(4002, 120000);
				sendPackets(new S_PacketBox(36, 120));

				sendPackets(new S_ServerMessage(153));
				_chatCount = 0;
				_oldChatTimeInMillis = 0L;
			}
			_chatCount = ((byte) (_chatCount + 1));
		}
	}

	public int getCallClanId() {
		return _callClanId;
	}

	public void setCallClanId(int i) {
		_callClanId = i;
	}

	public int getCallClanHeading() {
		return _callClanHeading;
	}

	public void setCallClanHeading(int i) {
		_callClanHeading = i;
	}

	public boolean isInCharReset() {
		return _isInCharReset;
	}

	public void setInCharReset(boolean flag) {
		_isInCharReset = flag;
	}

	public int getTempLevel() {
		return _tempLevel;
	}

	public void setTempLevel(int i) {
		_tempLevel = i;
	}

	public int getTempMaxLevel() {
		return _tempMaxLevel;
	}

	public void setTempMaxLevel(int i) {
		_tempMaxLevel = i;
	}

	public void setSummonMonster(boolean SummonMonster) {
		_isSummonMonster = SummonMonster;
	}

	public boolean isSummonMonster() {
		return _isSummonMonster;
	}

	public void setShapeChange(boolean isShapeChange) {
		_isShapeChange = isShapeChange;
	}

	public boolean isShapeChange() {
		return _isShapeChange;
	}

	public void setText(String text) {
		_text = text;
	}

	public String getText() {
		return _text;
	}

	public void setTextByte(byte[] textByte) {
		_textByte = textByte;
	}

	public byte[] getTextByte() {
		return _textByte;
	}

	public void set_other(L1PcOther other) {
		_other = other;
	}

	public L1PcOther get_other() {
		return _other;
	}

	public void set_otherList(L1PcOtherList other) {
		_otherList = other;
	}

	public L1PcOtherList get_otherList() {
		return _otherList;
	}

	public void setOleLocX(int oleLocx) {
		_oleLocX = oleLocx;
	}

	public int getOleLocX() {
		return _oleLocX;
	}

	public void setOleLocY(int oleLocy) {
		_oleLocY = oleLocy;
	}

	public int getOleLocY() {
		return _oleLocY;
	}

	public void setNowTarget(L1Character target) {
		_target = target;
	}

	public L1Character getNowTarget() {
		return _target;
	}

	public void setPetModel() {
		try {
			for (L1NpcInstance petNpc : getPetList().values()) {
				if (petNpc != null)
					if ((petNpc instanceof L1SummonInstance)) {
						L1SummonInstance summon = (L1SummonInstance) petNpc;
						summon.set_tempModel();
					} else if ((petNpc instanceof L1PetInstance)) {
						L1PetInstance pet = (L1PetInstance) petNpc;
						pet.set_tempModel();
					}
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void getPetModel() {
		try {
			for (L1NpcInstance petNpc : getPetList().values()) {
				if (petNpc != null)
					if ((petNpc instanceof L1SummonInstance)) {
						L1SummonInstance summon = (L1SummonInstance) petNpc;
						summon.get_tempModel();
					} else if ((petNpc instanceof L1PetInstance)) {
						L1PetInstance pet = (L1PetInstance) petNpc;
						pet.get_tempModel();
					}
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public void set_outChat(L1DeInstance b) {
		_outChat = b;
	}

	public L1DeInstance get_outChat() {
		return _outChat;
	}

	public long get_h_time() {
		return _h_time;
	}

	public void set_h_time(long time) {
		_h_time = time;
	}

	public void set_mazu(boolean b) {
		_mazu = b;
	}

	public boolean is_mazu() {
		return _mazu;
	}

	public int get_mazu_time() {
		return _mazu_time;
	}

	public void set_mazu_time(int time) {
		_mazu_time = time;
	}

	/**
	 * 機率增加傷害
	 * 
	 * @param int1增加傷害
	 *            int2發動機率
	 */
	public void set_dmgAdd(int int1, int int2) {
		_int1 += int1;// 增加傷害
		_int2 += int2;// 發動機率
	}

	public int dmgAdd() {
		if (_int2 == 0) {
			return 0;
		}
		if (_random.nextInt(100) + 1 <= _int2) {
			if (!getDolls().isEmpty()) {
				for (L1DollInstance doll : getDolls().values()) {
					doll.show_action(1);
				}
			}
			if (!getDolls2().isEmpty()) {
				for (L1DollInstance2 doll : getDolls2().values()) {
					doll.show_action(1);
				}
			}
			return _int1;
		}
		return 0;
	}

	/** 增加完全閃避率 亂數1000 */
	public void set_evasion(int int1) {
		_evasion += int1;
	}

	/** 取回完全閃避率 亂數1000 */
	public int get_evasion() {
		return _evasion;
	}

	public void set_expadd(int int1) {
		_expadd += int1 / 100.0D;
	}

	public double getExpAdd() {
		return _expadd;
	}

	/**
	 * 隨機傷害減免
	 * 
	 * @param int1減免質
	 *            int2發動機率
	 */
	public void set_dmgDowe(int int1, int int2) {
		_dd1 += int1;
		_dd2 += int2;
	}

	/** 隨機傷害減免 */
	public int dmgDowe() {
		if (this.has_powerid(6611)) {// 附魔系統 隨機傷害減免
			int rad = 10;// 機率
			if (_random.nextInt(100) < rad) {
				this.sendPacketsAll(new S_SkillSound(this.getId(), 9800));
				return _dd1;
			}
		}

		if (_dd2 == 0) {
			return 0;
		}
		if (_random.nextInt(100) + 1 <= _dd2) {
			if (!getDolls().isEmpty()) {
				for (L1DollInstance doll : getDolls().values()) {
					doll.show_action(2);
				}
			}
			if (!getDolls2().isEmpty()) {
				for (L1DollInstance2 doll : getDolls2().values()) {
					doll.show_action(2);
				}
			}
			return _dd1;
		}
		return 0;
	}

	public boolean isFoeSlayer() {
		return _isFoeSlayer;
	}

	public void setFoeSlayer(boolean FoeSlayer) {
		_isFoeSlayer = FoeSlayer;
	}

	public long get_weaknss_t() {
		return _weaknss_t;
	}

	public int get_weaknss() {
		return _weaknss;
	}

	public void set_weaknss(int lv, long t) {
		_weaknss = lv;
		_weaknss_t = t;
	}

	public void set_actionId(int actionId) {
		_actionId = actionId;
	}

	public int get_actionId() {
		return _actionId;
	}

	public void set_hardinR(Chapter01R hardin) {
		_hardin = hardin;
	}

	public Chapter01R get_hardinR() {
		return _hardin;
	}

	/** 穿上附魔裝備處理 */
	public void add_power(L1ItemPower_text value, L1ItemInstance eq) {
		if (!_allpowers.containsKey(Integer.valueOf(value.get_id()))) {
			_allpowers.put(Integer.valueOf(value.get_id()), value);
		}

		if (eq.isEquipped()) {
			value.add_pc_power(this);
			sendPackets(new S_ServerMessage("\\fW獲得" + value.getMsg() + " 效果"));
		}

		if (value.getGfx() != null) {
			for (int gfx : value.getGfx()) {
				sendPacketsAll(new S_SkillSound(getId(), gfx));
			}
		}
	}

	/** 脫下附魔裝備處理 */
	public void remove_power(L1ItemPower_text value, L1ItemInstance eq) {

		if (_allpowers.containsKey(Integer.valueOf(value.get_id()))) {
			_allpowers.remove(Integer.valueOf(value.get_id()));
		}

		if (!eq.isEquipped()) {
			value.remove_pc_power(this);
			sendPackets(new S_ServerMessage("\\fY失去 " + value.getMsg() + " 效果"));
		}

	}

	/** 是否含有特定編號的附魔效果 */
	public boolean has_powerid(int powerid) {
		return _allpowers.containsKey(Integer.valueOf(powerid));
	}

	/** 取回人物身上所有附魔效果 */
	public Map<Integer, L1ItemPower_text> get_allpowers() {
		return _allpowers;
	}

	public void set_unfreezingTime(int i) {
		_unfreezingTime = i;
	}

	public int get_unfreezingTime() {
		return _unfreezingTime;
	}

	public void set_misslocTime(int i) {
		_misslocTime = i;
	}

	public int get_misslocTime() {
		return _misslocTime;
	}

	public void set_c_power(L1User_Power power) {
		_c_power = power;
	}

	public L1User_Power get_c_power() {
		return _c_power;
	}

	public void add_dice_hp(int dice_hp, int sucking_hp) {
		_dice_hp += dice_hp;
		_sucking_hp += sucking_hp;
	}

	public int dice_hp() {
		return _dice_hp;
	}

	public int sucking_hp() {
		return _sucking_hp;
	}

	public void add_dice_mp(int dice_mp, int sucking_mp) {
		_dice_mp += dice_mp;
		_sucking_mp += sucking_mp;
	}

	public int dice_mp() {
		return _dice_mp;
	}

	public int sucking_mp() {
		return _sucking_mp;
	}

	public void add_double_dmg(int double_dmg) {
		_double_dmg += double_dmg;
	}

	public int get_double_dmg() {
		return _double_dmg;
	}

	public void add_lift(int lift) {
		_lift += lift;
	}

	public int lift() {
		return _lift;
	}

	public void add_magic_modifier_dmg(int add) {
		_magic_modifier_dmg += add;
	}

	public int get_magic_modifier_dmg() {
		return _magic_modifier_dmg;
	}

	public void add_magic_reduction_dmg(int add) {
		_magic_reduction_dmg += add;
	}

	public int get_magic_reduction_dmg() {
		return _magic_reduction_dmg;
	}

	public void rename(boolean b) {
		_rname = b;
	}

	public boolean is_rname() {
		return _rname;
	}

	public boolean is_retitle() {
		return _retitle;
	}

	public void retitle(boolean b) {
		_retitle = b;
	}

	public int is_repass() {
		return _repass;
	}

	public void repass(int b) {
		_repass = b;
	}

	/*public void add_trade_item(L1TradeItem info) {
		if (_trade_items.size() == 16) {
			return;
		}
		_trade_items.add(info);
	}

	public ArrayList<L1TradeItem> get_trade_items() {
		return _trade_items;
	}

	public void get_trade_clear() {
		_tradeID = 0;
		_trade_items.clear();
	}*/

	public void set_mode_id(int mode) {
		_mode_id = mode;
	}

	public int get_mode_id() {
		return _mode_id;
	}

	public void set_check_item(boolean b) {
		_check_item = b;
	}

	public boolean get_check_item() {
		return _check_item;
	}

	public void set_VIP1(boolean b) {
		_vip_1 = b;
	}

	public void set_VIP2(boolean b) {
		_vip_2 = b;
	}

	public void set_VIP3(boolean b) {
		_vip_3 = b;
	}

	public void set_VIP4(boolean b) {
		_vip_4 = b;
	}

	public long get_global_time() {
		return _global_time;
	}

	public void set_global_time(long global_time) {
		_global_time = global_time;
	}

	public int get_doll_hpr() {
		return _doll_hpr;
	}

	public void set_doll_hpr(int hpr) {
		_doll_hpr = hpr;
	}

	public int get_doll_hpr_time() {
		return _doll_hpr_time;
	}

	public void set_doll_hpr_time(int time) {
		_doll_hpr_time = time;
	}

	public int get_doll_hpr_time_src() {
		return _doll_hpr_time_src;
	}

	public void set_doll_hpr_time_src(int time) {
		_doll_hpr_time_src = time;
	}

	public int get_doll_mpr() {
		return _doll_mpr;
	}

	public void set_doll_mpr(int mpr) {
		_doll_mpr = mpr;
	}

	public int get_doll_mpr_time() {
		return _doll_mpr_time;
	}

	public void set_doll_mpr_time(int time) {
		_doll_mpr_time = time;
	}

	public int get_doll_mpr_time_src() {
		return _doll_mpr_time_src;
	}

	public void set_doll_mpr_time_src(int time) {
		_doll_mpr_time_src = time;
	}

	public int[] get_doll_get() {
		return _doll_get;
	}

	public void set_doll_get(int itemid, int count) {
		_doll_get[0] = itemid;
		_doll_get[1] = count;
	}

	public int get_doll_get_time() {
		return _doll_get_time;
	}

	public void set_doll_get_time(int time) {
		_doll_get_time = time;
	}

	public int get_doll_get_time_src() {
		return _doll_get_time_src;
	}

	public void set_doll_get_time_src(int time) {
		_doll_get_time_src = time;
	}

	public void set_board_title(String text) {
		_board_title = text;
	}

	public String get_board_title() {
		return _board_title;
	}

	public void set_board_content(String text) {
		_board_content = text;
	}

	public String get_board_content() {
		return _board_content;
	}

	public void set_spr_move_time(long spr_time) {
		_spr_move_time = spr_time;
	}

	public long get_spr_move_time() {
		return _spr_move_time;
	}

	public void set_spr_attack_time(long spr_time) {
		_spr_attack_time = spr_time;
	}

	public long get_spr_attack_time() {
		return _spr_attack_time;
	}

	public void set_spr_skill_time(long spr_time) {
		_spr_skill_time = spr_time;
	}

	public long get_spr_skill_time() {
		return _spr_skill_time;
	}

	public void set_delete_time(int time) {
		_delete_time = time;
	}

	public int get_delete_time() {
		return _delete_time;
	}

	/**
	 * 增加藥水回復量%
	 * 
	 * @param up_hp_potion
	 */
	public void add_up_hp_potion(int up_hp_potion) {
		_up_hp_potion += up_hp_potion;
	}

	public int get_up_hp_potion() {
		return _up_hp_potion;
	}

	/**
	 * 增加藥水回復指定量
	 * 
	 * @param uhp_number
	 */
	public void add_uhp_number(int uhp_number) {
		_uhp_number += uhp_number;
	}

	public int get_uhp_number() {
		return _uhp_number;
	}

	public void set_venom_resist(int i) {
		_venom_resist += i;
	}

	public int get_venom_resist() {
		return _venom_resist;
	}

	private ArrayList<String> _InviteList = new ArrayList<String>();

	/**
	 * 加入邀請列表
	 * 
	 * @param playername
	 */
	public void addInviteList(String playername) {
		if (_InviteList.contains(playername)) {
			return;
		}
		_InviteList.add(playername);
	}

	/**
	 * 從邀請列表中移除
	 * 
	 * @param name
	 */
	public void removeInviteList(String name) {
		if (!_InviteList.contains(name)) {
			return;
		}
		_InviteList.remove(name);
	}

	/**
	 * 傳回邀請列表
	 * 
	 * @return
	 */
	public ArrayList<String> getInviteList() {
		return _InviteList;
	}

	private ArrayList<String> _cmalist = new ArrayList<String>();

	/**
	 * 加入血盟申請列表
	 * 
	 * @param clanname
	 *            血盟名稱
	 */
	public void addCMAList(String clanname) {
		if (_cmalist.contains(clanname)) {
			return;
		}
		_cmalist.add(clanname);
	}

	/**
	 * 移除血盟申請列表
	 * 
	 * @param name
	 *            申請人名稱
	 */
	public void removeCMAList(String name) {
		if (!_cmalist.contains(name)) {
			return;
		}
		_cmalist.remove(name);
	}

	/**
	 * 傳回血盟申請列表
	 * 
	 * @return
	 */
	public ArrayList<String> getCMAList() {
		return _cmalist;
	}

	/** 守護者不顯示盟徽 */
	public final int getEmblemId() {
		if ((isProtector()) || (getClanid() <= 0)) {
			return 0;
		}
		L1Clan clan = getClan();
		if (clan == null) {
			return 0;
		}
		return clan.getEmblemId();
	}

	private AcceleratorChecker _speed;

	public AcceleratorChecker speed_Attack() {
		return _speed;
	}

	public void set_arena(int i) {
		_arena = i;
	}

	public int get_arena() {
		return _arena;
	}

	public void set_temp_adena(int itemid) {
		_temp_adena = itemid;
	}

	public int get_temp_adena() {
		return _temp_adena;
	}

	public long get_ss_time() {
		return _ss_time;
	}

	public void set_ss_time(long ss_time) {
		_ss_time = ss_time;
	}

	public int get_ss() {
		return _ss;
	}

	public void set_ss_time(int ss) {
		_ss = ss;
	}

	public final int getKillCount() {
		return killCount;
	}

	public final void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	public int getMeteLevel() {
		return _meteLevel;
	}

	public void setMeteLevel(int i) {
		_meteLevel = i;
	}

	public final L1MeteAbility getMeteAbility() {
		return _meteAbility;
	}

	public final void resetMeteAbility() {
		if (_meteAbility != null) {
			ExtraMeteAbilityTable.effectBuff(this, _meteAbility, -1);
		}
		_meteAbility = ExtraMeteAbilityTable.getInstance().get(getMeteLevel(), getType());
		if (_meteAbility != null) {
			ExtraMeteAbilityTable.effectBuff(this, _meteAbility, 1);
		}
	}

	// 真 妲蒂斯魔石
	private boolean _EffectDADIS;

	/**
	 * 是否有真妲蒂斯魔石效果
	 * 
	 * @return
	 */
	public final boolean isEffectDADIS() {
		return _EffectDADIS;
	}

	/**
	 * 給予真妲蒂斯魔石效果
	 * 
	 * @param checkFlag
	 */
	public final void setDADIS(boolean checkFlag) {
		if (_EffectDADIS != checkFlag) {
			giveDADIS(checkFlag);
			sendPackets(new S_HPUpdate(this));
			if (isInParty()) {
				getParty().updateMiniHP(this);
			}
			sendPackets(new S_MPUpdate(this));
			sendPackets(new S_SPMR(this));
			L1PcUnlock.Pc_Unlock(this);
		}
	}

	/**
	 * 給予真妲蒂斯魔石效果
	 * 
	 * @param checkFlag
	 */
	public final void giveDADIS(boolean checkFlag) {
		_EffectDADIS = checkFlag;

		if (checkFlag) {
			addMaxHp(100);
			addMaxMp(100);
			addDmgup(5);
			addBowDmgup(5);
			addSp(5);
			addDamageReductionByArmor(5);
			addHpr(5);
			addMpr(5);
			sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 553));// 開啟妲蒂斯魔石圖示
		} else {
			addMaxHp(-100);
			addMaxMp(-100);
			addDmgup(-5);
			addBowDmgup(-5);
			addSp(-5);
			addDamageReductionByArmor(-5);
			addHpr(-5);
			addMpr(-5);
			sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0, 553));// 關閉妲蒂斯魔石圖示
		}
	}

	// 妲蒂斯魔石
	private boolean _EffectGS;

	/**
	 * 是否有妲蒂斯魔石效果
	 * 
	 * @return
	 */
	public final boolean isEffectGS() {
		return _EffectGS;
	}

	/**
	 * 給予妲蒂斯魔石效果
	 * 
	 * @param checkFlag
	 */
	public final void setGS(boolean checkFlag) {
		if (_EffectGS != checkFlag) {
			giveGS(checkFlag);
			sendPackets(new S_HPUpdate(this));
			if (isInParty()) {
				getParty().updateMiniHP(this);
			}
			sendPackets(new S_MPUpdate(this));
			sendPackets(new S_SPMR(this));
			L1PcUnlock.Pc_Unlock(this);
		}
	}

	/**
	 * 給予妲蒂斯魔石效果
	 * 
	 * @param checkFlag
	 */
	public final void giveGS(boolean checkFlag) {
		_EffectGS = checkFlag;

		if (checkFlag) {
			addMaxHp(30);
			addMaxMp(30);
			addDmgup(2);
			addBowDmgup(2);
			addSp(2);
			addDamageReductionByArmor(2);
			addHpr(2);
			addMpr(2);
			sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 553));// 開啟妲蒂斯魔石圖示
		} else {
			addMaxHp(-30);
			addMaxMp(-30);
			addDmgup(-2);
			addBowDmgup(-2);
			addSp(-2);
			addDamageReductionByArmor(-2);
			addHpr(-2);
			addMpr(-2);
			sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0, 553));// 關閉妲蒂斯魔石圖示
		}
	}

	/**
	 * 是否有守護者的靈魂效果
	 * 
	 * @return
	 */
	public final boolean isProtector() {
		return _isProtector;
	}

	/**
	 * 給予守護者的靈魂效果
	 * 
	 * @param checkFlag
	 */
	public final void setProtector(boolean checkFlag) {
		if (_isProtector != checkFlag) {
			giveProtector(checkFlag);
			
			//sendPackets(new S_OwnCharPack(this)); // 
			sendPackets(new S_HPUpdate(this));
			if (isInParty()) {
				getParty().updateMiniHP(this);
			}
			sendPackets(new S_MPUpdate(this));
			sendPackets(new S_SPMR(this));
			L1PcUnlock.Pc_Unlock(this);
		}
	}

	/**
	 * 守護者靈魂效果
	 * 
	 * @param checkFlag
	 */
	public final void giveProtector(boolean checkFlag) {
		_isProtector = checkFlag;
		if (checkFlag) {
			addMaxHp(ProtectorSet.HP_UP);
			addMaxMp(ProtectorSet.MP_UP);
			addDmgup(ProtectorSet.DMG_UP);
			addBowDmgup(ProtectorSet.DMG_UP);
			addDamageReductionByArmor(ProtectorSet.DMG_DOWN);
			addSp(ProtectorSet.SP_UP);
			sendPackets(new S_PacketBox(S_PacketBox.ICON_SOUL_GUARDIAN, 1));// 守護者狀態 PackBox=144
		} else {
			addMaxHp(-ProtectorSet.HP_UP);
			addMaxMp(-ProtectorSet.MP_UP);
			addDmgup(-ProtectorSet.DMG_UP);
			addBowDmgup(-ProtectorSet.DMG_UP);
			addDamageReductionByArmor(-ProtectorSet.DMG_DOWN);
			addSp(-ProtectorSet.SP_UP);
			sendPackets(new S_PacketBox(S_PacketBox.ICON_SOUL_GUARDIAN, 0));
		}
	}

	/**
	 * 是否有戰神之魂的效果
	 * 
	 * @return
	 */
	public final boolean isMars() {
		return _isMars;
	}

	/**
	 * 給予戰神之魂的效果
	 * 
	 * @param checkFlag
	 */
	public final void setMars(boolean checkFlag) {
		if (_isMars != checkFlag) {
			giveMars(checkFlag);
			sendPackets(new S_HPUpdate(this));
			if (isInParty()) {
				getParty().updateMiniHP(this);
			}
			sendPackets(new S_MPUpdate(this));
			sendPackets(new S_SPMR(this));
			L1PcUnlock.Pc_Unlock(this);
		}
	}

	/**
	 * 給予戰神之魂的效果
	 * 
	 * @param checkFlag
	 */
	public final void giveMars(boolean checkFlag) {
		_isMars = checkFlag;
		if (checkFlag) {
			addMaxHp(120);
			addMaxMp(100);
			addDmgup(15);
			addBowDmgup(15);
			addDamageReductionByArmor(8);
			addSp(5);
			sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 619));// 開啟戰神之魂圖示
		} else {
			addMaxHp(-120);
			addMaxMp(-100);
			addDmgup(-15);
			addBowDmgup(-15);
			addDamageReductionByArmor(-8);
			addSp(-5);
			sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0, 619));// 關閉戰神之魂圖示
		}
	}

	public final L1Apprentice getApprentice() {
		return _apprentice;
	}

	public final void setApprentice(L1Apprentice apprentice) {
		_apprentice = apprentice;
	}

	public final void checkEffect() {
		int checkType = 0;
		if (getApprentice() != null) {
			L1PcInstance master = World.get().getPlayer(getApprentice().getMaster().getName());
			if (master != null) {
				L1Party party = getParty();
				if (party != null) {
					checkType = party.checkMentor(getApprentice());
				} else {
					checkType = 1;
				}
			}
		}
		// System.out.println("checkType: "+checkType);
		// System.out.println("_tempType: "+_tempType!=null?_tempType:"NULL");
		if (_tempType != checkType) {
			// 先還原狀態
			if (checkType > 0) {
				sendEffectBuff(_tempType, -1);
			}
			// 再更新狀態
			if (checkType > 0) {
				sendEffectBuff(checkType, 1);
			}
			sendPackets(new S_SPMR(this));
			sendPackets(new S_OwnCharStatus(this));
			sendPackets(new S_PacketBox(132, getEr()));
			if (checkType <= 0) {
				sendPackets(new S_PacketBox(147, 0, Math.max(_tempType - 1, 0)));
			} else {
				sendPackets(new S_PacketBox(147, checkType == 0 ? 0 : 1, Math.max(checkType - 1, 0)));
			}
			_tempType = checkType;
			// System.out.println("_tempType:
			// "+_tempType!=null?_tempType:"NULL");
		}
	}

	public void addOriginalEr(int i) {
		_originalEr += i;
	}

	private final void sendEffectBuff(int buffType, int negative) {
		switch (buffType) {
		case 1:
			addAc(-1 * negative);
			break;
		case 2:
			addAc(-1 * negative);
			addMr(1 * negative);
			break;
		case 3:
			addAc(-1 * negative);
			addMr(1 * negative);
			addWater(2 * negative);
			addWind(2 * negative);
			addFire(2 * negative);
			addEarth(2 * negative);
			break;
		case 4:
			addAc(-1 * negative);
			addMr(1 * negative);
			addWater(2 * negative);
			addWind(2 * negative);
			addFire(2 * negative);
			addEarth(2 * negative);
			addOriginalEr(1 * negative);
			break;
		case 5:
			addAc(-3 * negative);
			break;
		case 6:
			addAc(-3 * negative);
			addMr(3 * negative);
			break;
		case 7:
			addAc(-3 * negative);
			addMr(3 * negative);
			addWater(6 * negative);
			addWind(6 * negative);
			addFire(6 * negative);
			addEarth(6 * negative);
			break;
		case 8:
			addAc(-3 * negative);
			addMr(3 * negative);
			addWater(6 * negative);
			addWind(6 * negative);
			addFire(6 * negative);
			addEarth(6 * negative);
			addOriginalEr(3 * negative);
		}
	}

	public final Timestamp getPunishTime() {
		return _punishTime;
	}

	public final void setPunishTime(Timestamp timestamp) {
		_punishTime = timestamp;
	}

	@Override
	public final String getViewName() {
		final StringBuffer sbr = new StringBuffer();
		if (isProtector()) {
			sbr.append(ConfigAlt.Protector_Name);
		} else {
			sbr.append(getName());
		}
		return sbr.toString();
	}

	public final String getRealName() {
		StringBuffer sbr = new StringBuffer();

		sbr.append(getName());

		return sbr.toString();
	}

	public int getMagicDmgModifier() {
		return _magicDmgModifier;
	}

	public void addMagicDmgModifier(int i) {
		_magicDmgModifier += i;
	}

	public int getMagicDmgReduction() {
		return _magicDmgReduction;
	}

	public void addMagicDmgReduction(int i) {
		_magicDmgReduction += i;
	}

	// 法利昂的治癒守護 亂數機率1000
	public void set_elitePlateMail_Fafurion(int r, int hpmin, int hpmax) {
		_elitePlateMail_Fafurion = r;
		_fafurion_hpmin = hpmin;
		_fafurion_hpmax = hpmax;
	}

	// 奪魂T設置
	public void set_soulHp_val(int r, int hpmin, int hpmax) {
		_soulHp_r = r;
		_soulHp_hpmin = hpmin;
		_soulHp_hpmax = hpmax;
	}

	private int isSoulHp = 0;

	public void set_soulHp(int flag) {
		isSoulHp = flag;
	}

	public int isSoulHp() {
		return isSoulHp;
	}

	// 奪魂T吸血量
	private ArrayList<Integer> soulHp = new ArrayList<Integer>();

	public ArrayList<Integer> get_soulHp() {
		soulHp.add(0, _soulHp_r);
		soulHp.add(1, _soulHp_hpmin);
		soulHp.add(2, _soulHp_hpmax);

		return soulHp;
	}

	// 林德拜爾的魔力守護 亂數機率1000
	public void set_elitePlateMail_Lindvior(int r, int mpmin, int mpmax) {
		_elitePlateMail_Lindvior = r;
		_lindvior_mpmin = mpmin;
		_lindvior_mpmax = mpmax;
	}

	// 巴拉卡斯的弓箭反屏 亂數機率1000
	public void set_elitePlateMail_Valakas(int r, int dmgmin, int dmgmax) {
		_elitePlateMail_Valakas = r;
		_valakas_dmgmin = dmgmin;
		_valakas_dmgmax = dmgmax;
	}

	// 黑帝斯斗篷 亂數機率1000
	public void set_hades_cloak(int r, int dmgmin, int dmgmax) {
		_hades_cloak = r;
		_hades_cloak_dmgmin = dmgmin;
		_hades_cloak_dmgmax = dmgmax;
	}

	// 黑帝斯斗篷 亂數機率1000
	public void set_death_pant(int r, int dmgmin, int dmgmax) {
		_death_pant = r;
		_death_pant_dmgmin = dmgmin;
		_death_pant_dmgmax = dmgmax;
	}

	// 六芒星的淨化 亂數機率1000
	public void set_Hexagram_Magic_Rune(int r, int hpmin, int hpmax, int gfx) {
		_Hexagram_Magic_Rune = r;
		_hexagram_hpmin = hpmin;
		_hexagram_hpmax = hpmax;
		_hexagram_gfx = gfx;
	}

	// 蒂蜜特的祝福 亂數機率1000
	public void set_DimiterBless(int r, int mpmin, int mpmax, int r2, int time) {
		_dimiter_mpr_rnd = r;
		_dimiter_mpmin = mpmin;
		_dimiter_mpmax = mpmax;

		_dimiter_bless = r2;
		_dimiter_time = time;
	}

	/**
	 * 經驗加成%數
	 * 
	 * @return
	 */
	public int getExpPoint() {
		return _expPoint;
	}

	/**
	 * 設定經驗加成%數
	 * 
	 * @param i
	 */
	public void setExpPoint(int i) {
		_expPoint = i;
	}

	public void setSummonId(int SummonId) {
		_SummonId = SummonId;
	}

	public int getSummonId() {
		return _SummonId;
	}

	public void setLap(int i) {
		_lap = i;
	}

	public int getLap() {
		return _lap;
	}

	public void setLapCheck(int i) {
		_lapCheck = i;
	}

	public int getLapCheck() {
		return _lapCheck;
	}

	public int getLapScore() {
		return _lap * 29 + _lapCheck;
	}

	public boolean isInOrderList() {
		return _order_list;
	}

	public void setInOrderList(boolean bool) {
		_order_list = bool;
	}

	/** [原碼] 怪物對戰系統 */
	private final L1MobSpList _MobSpList;

	public L1MobSpList getMobSplist() {
		return this._MobSpList;
	}

	/** [原碼] 大樂透系統 */
	private final L1BigHotSpList _BigHotSpList;

	public L1BigHotSpList getBigHotSplist() {
		return _BigHotSpList;
	}

	private int _isStar;

	public void set_star(int i) {
		_isStar = i;
	}

	public int get_star() {
		return _isStar;
	}

	private int _isBigHot;

	public void set_bighot(int i) {
		_isBigHot = i;
	}

	public int get_bighot() {
		return _isBigHot;
	}

	private String _bighot1;
	private String _bighot2;
	private String _bighot3;
	private String _bighot4;
	private String _bighot5;
	private String _bighot6;

	public void setBighot1(String bighot1) {
		_bighot1 = bighot1;
	}

	public String getBighot1() {
		return _bighot1;
	}

	public void setBighot2(String bighot2) {
		_bighot2 = bighot2;
	}

	public String getBighot2() {
		return _bighot2;
	}

	public void setBighot3(String bighot3) {
		_bighot3 = bighot3;
	}

	public String getBighot3() {
		return _bighot3;
	}

	public void setBighot4(String bighot4) {
		_bighot4 = bighot4;
	}

	public String getBighot4() {
		return _bighot4;
	}

	public void setBighot5(String bighot5) {
		_bighot5 = bighot5;
	}

	public String getBighot5() {
		return _bighot5;
	}

	public void setBighot6(String bighot6) {
		_bighot6 = bighot6;
	}

	public String getBighot6() {
		return _bighot6;
	}

	public boolean isWindShackle() {
		return hasSkillEffect(167);
	}

	private static BufferedWriter out;

	public static void writeDeathlog(L1PcInstance player1, L1PcInstance player2, String s) {
		try {
			File DeleteLog = new File("./log/Death.txt");
			if (DeleteLog.createNewFile()) {
				out = new BufferedWriter(new FileWriter("./log/Death.txt", false));
				out.write("※死亡紀錄※" + "\r\n");
				out.close();
			}
			String p1name = "";
			Short p1map = 0;
			String p2name = "";
			if (player1 != null) {
				p1name = player1.getName();
				p1map = player1.getMapId();
			}
			if (player2 != null) {
				p2name = player2.getName();
			}
			out = new BufferedWriter(new FileWriter("./log/Death.txt", true));
			out.write("\r\n");// 每次填寫資料都控一行
			out.write("死者: " + p1name + " 殺人者: " + p2name + " 地圖: " + p1map + " 時間: "
					+ new Timestamp(System.currentTimeMillis()) + "(" + s + ")" + "\r\n");
			out.close();
		} catch (IOException e) {
			System.out.println("以下是錯誤訊息: " + e.getMessage());
		}
	}

	public static void writeReceivelog(L1Character player1, L1Character player2, String s, double dmg, int pcHp) {
		try {
			File DeleteLog = new File("./log/Receive.txt");
			if (DeleteLog.createNewFile()) {
				out = new BufferedWriter(new FileWriter("./log/Receive.txt", false));
				out.write("※受攻擊紀錄※" + "\r\n");
				out.close();
			}
			String p1name = "";
			Short p1map = 0;
			String p2name = "";
			if (player1 != null) {
				p1name = player1.getName();
				p1map = player1.getMapId();
			}
			if (player2 != null) {
				p2name = player2.getName();
			}
			out = new BufferedWriter(new FileWriter("./log/Receive.txt", true));
			out.write("\r\n");// 每次填寫資料都控一行
			out.write("攻擊者: " + p1name + " 被攻擊者: " + p2name + " 血量: " + pcHp + " 傷害值: " + dmg + " 地圖: " + p1map
					+ " 時間: " + new Timestamp(System.currentTimeMillis()) + "(" + s + ")" + "\r\n");
			out.close();
		} catch (IOException e) {
			System.out.println("以下是錯誤訊息: " + e.getMessage());
		}
	}

	public static void writeDeathlog2(L1PcInstance player1, L1MonsterInstance player2, String s) {
		try {
			File DeleteLog = new File("./log/Death.txt");
			if (DeleteLog.createNewFile()) {
				out = new BufferedWriter(new FileWriter("./log/Death.txt", false));
				out.write("※死亡紀錄※" + "\r\n");
				out.close();
			}
			String p1name = "";
			Short p1map = 0;
			String p2name = "";
			if (player1 != null) {
				p1name = player1.getName();
				p1map = player1.getMapId();
			}
			if (player2 != null) {
				p2name = player2.getName();
			}
			out = new BufferedWriter(new FileWriter("./log/Death.txt", true));
			out.write("\r\n");// 每次填寫資料都控一行
			out.write("死者: " + p1name + " 殺人者: " + p2name + " 地圖: " + p1map + " 時間: "
					+ new Timestamp(System.currentTimeMillis()) + "(" + s + ")" + "\r\n");
			out.close();
		} catch (IOException e) {
			System.out.println("以下是錯誤訊息: " + e.getMessage());
		}
	}

	private class Death implements Runnable {
		private L1Character _lastAttacker;

		private Death(L1Character cha) {
			_lastAttacker = cha;
		}

		@Override
		public void run() {
			L1Character lastAttacker = _lastAttacker;
			_lastAttacker = null;

			/*
			 * if ((lastAttacker instanceof L1PcInstance)) { L1PcInstance oPc =
			 * (L1PcInstance)lastAttacker; if(oPc.getId() ==
			 * L1PcInstance.this.getId()){
			 * writeDeathlog(L1PcInstance.this,oPc,"自殺"); return; } }
			 */

			setCurrentHp(0);
			L1PcInstance.this.setGresValid(false);
			
			if (get_redbluejoin() > 0 && get_redbluepoint() > 0) {
				if (lastAttacker instanceof L1PcInstance) {
					L1PcInstance killer = (L1PcInstance) lastAttacker;
					if (get_redblueleader() == 0) {
						int nplus = ConfigOther.RedBlueNormal_point;
						if (nplus >= get_redbluepoint()) {
							nplus = get_redbluepoint();
						}
						set_redbluepoint(get_redbluepoint() - nplus);
						killer.set_redbluepoint(get_redbluepoint() + nplus);
						killer.sendPackets(new S_ServerMessage(
								"\\aG你殺死[敵軍成員]獲得\\aD" + nplus + "點\\aG積分！"));
					} else if (get_redblueleader() > 0) {
						int lplus = ConfigOther.RedBlueLeader_point;
						if (lplus >= get_redbluepoint()) {
							lplus = get_redbluepoint();
						}
						set_redbluepoint(get_redbluepoint() - lplus);
						killer.set_redbluepoint(get_redbluepoint() + lplus);
						killer.sendPackets(new S_ServerMessage(
								"\\aG你殺死[敵軍隊長]獲得\\aD" + lplus + "點\\aG積分了！"));
					}
				}
			}

			while (isTeleport())
				try {
					Thread.sleep(300L);
				} catch (Exception localException) {
				}

			/*if (isInParty()) { // 7.6 註銷 刪除 S_PacketBoxParty
				for (final L1PcInstance member : getParty().getMemberList()) { // 7.6
					//member.sendPackets(new S_PacketBoxParty(getParty(), L1PcInstance.this));
					member.sendPackets(new S_Party(0x6e, L1PcInstance.this));
				}
			}*/

			set_delete_time(300);

			// 更新 2015/06/30 修正人物離線,死亡召喚獸移除
			if (!getPetList().isEmpty()) {
				for (Object petList : getPetList().values().toArray()) {
					if (petList instanceof L1SummonInstance) {
						final L1SummonInstance summon = (L1SummonInstance) petList;
						final S_NewMaster packet = new S_NewMaster(summon);
						if (summon != null) {
							if (summon.destroyed()) {
								return;
							}
							if (summon.tamed()) {
								// 召喚獸解放
								summon.liberate();

							} else {
								// 解散
								summon.Death(null);
							}
						}
					}
				}
			}

			// 娃娃刪除
			if (!getDolls().isEmpty()) {
				for (Object obj : getDolls().values().toArray()) {
					final L1DollInstance doll = (L1DollInstance) obj;
					doll.deleteDoll();
				}
			}
			// 娃娃刪除
			if (!getDolls2().isEmpty()) {
				for (Object obj : getDolls2().values().toArray()) {
					final L1DollInstance2 doll = (L1DollInstance2) obj;
					doll.deleteDoll2();
				}
			}
			// if (!getHierarchs().isEmpty()) {
			// for (Object obj : getHierarchs().values().toArray()) {
			// final L1HierarchInstance hierarch = (L1HierarchInstance) obj;
			// hierarch.deleteMe();
			// }
			// }
			// 超級娃娃
			if (get_power_doll() != null) {
				get_power_doll().deleteDoll();
			}

			// stopHpRegeneration();
			// stopMpRegeneration();
			//
			// getMap().setPassable(getLocation(), true);

			L1PcInstance.this.stopHpRegeneration();
			L1PcInstance.this.stopMpRegeneration();

			final int targetobjid = L1PcInstance.this.getId();
			L1PcInstance.this.getMap().setPassable(L1PcInstance.this.getLocation(), true);

			if (isFishing()) {
				setFishingTime(0);
				setFishingReady(false);
				setFishing(false);
				setFishingItem(null);
				sendPacketsAll(new S_CharVisualUpdate(L1PcInstance.this));
				FishingTimeController.getInstance().removeMember(L1PcInstance.this);
				try {
					Thread.sleep(100);
				} catch (Exception localException1) {
				}
			}

			// 死亡時具有變身狀態
			int tempchargfx = 0;
			if (L1PcInstance.this.hasSkillEffect(SHAPE_CHANGE)) {
				tempchargfx = L1PcInstance.this.getTempCharGfx();
				L1PcInstance.this.setTempCharGfxAtDead(tempchargfx);
			} else {
				L1PcInstance.this.setTempCharGfxAtDead(L1PcInstance.this.getClassId());
			}

			// 死亡時 現有技能消除
			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(L1PcInstance.this, 44, getId(), getX(), getY(), 0, 1);

			if (tempchargfx != 0) {
				// System.out.println("tempchargfx: " + tempchargfx);
				L1PcInstance.this.sendPacketsAll(new S_ChangeShape(L1PcInstance.this, tempchargfx));
			} else {
				try {
					Thread.sleep(1000);
				} catch (final Exception e) {
				}
			}

			// 送出死亡動作
			sendPacketsAll(new S_DoActionGFX(L1PcInstance.this.getId(), 8));

			// 版本自帶墓碑系統 取消
//			L1EffectInstance tomb = L1SpawnUtil.spawnEffect(86126, 300, L1PcInstance.this.getX(),
//					L1PcInstance.this.getY(), L1PcInstance.this.getMapId(), L1PcInstance.this, 0);
//			L1PcInstance.this.set_tomb(tomb);

			if (getMapId() >= 2600 && getMapId() <= 2699) {// 火龍副本 //src026
				getInventory().consumeItem(5010);
			}

			if (getMapId() == 5153) { // XXX 死亡競賽
				for (Object doll : getDolls().values().toArray()) {
					L1ItemInstance item = getInventory().getItem(((L1DollInstance) doll).getItemObjId());
					item.stopEquipmentTimer(null);
					((L1DollInstance) doll).deleteDoll();
				}

				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				}

				try {
					resurrect(1);
					setCurrentHp(1);
					sendPacketsAll(new S_Resurrection(L1PcInstance.this, L1PcInstance.this, 0));
					sendPacketsAll(new S_RemoveObject(L1PcInstance.this));
					beginGhost(getX(), getY(), (short) getMapId(), false, 1800);
					sendPacketsAll(new S_CharVisualUpdate(L1PcInstance.this));
				} catch (Exception e) {
					e.printStackTrace();
				}

				sendPacketsAll(new S_ServerMessage(1271));//在戰鬥中失敗了，變更為觀戰者。
				L1DeathMatch.getInstance().sendRemainder(L1PcInstance.this);
				return;
			}

			boolean isSafetyZone = false;// 是否為安全區域

			boolean isCombatZone = false;// 是否為戰鬥區域

			boolean isWar = false;// 是否在戰爭中

			if (isSafetyZone()) {
				isSafetyZone = true;
			}
			if (isCombatZone()) {
				isCombatZone = true;
			}

			if ((lastAttacker instanceof L1GuardInstance)) {
				if (get_PKcount() > 0) {
					set_PKcount(get_PKcount() - 1);
				}
				setLastPk(null);
			}

			if ((lastAttacker instanceof L1GuardianInstance)) {
				if (getPkCountForElf() > 0) {
					setPkCountForElf(getPkCountForElf() - 1);
				}
				setLastPkForElf(null);
			}

			L1PcInstance fightPc = null;

			if ((lastAttacker instanceof L1PcInstance)) {
				fightPc = (L1PcInstance) lastAttacker;
			} else if ((lastAttacker instanceof L1PetInstance)) {
				L1PetInstance npc = (L1PetInstance) lastAttacker;
				if (npc.getMaster() != null) {
					fightPc = (L1PcInstance) npc.getMaster();
				}
			} else if ((lastAttacker instanceof L1SummonInstance)) {
				L1SummonInstance npc = (L1SummonInstance) lastAttacker;
				if (npc.getMaster() != null) {
					fightPc = (L1PcInstance) npc.getMaster();
				}
			} else if ((lastAttacker instanceof L1IllusoryInstance)) {
				L1IllusoryInstance npc = (L1IllusoryInstance) lastAttacker;
				if (npc.getMaster() != null) {
					fightPc = (L1PcInstance) npc.getMaster();
				}
			} else if ((lastAttacker instanceof L1EffectInstance)) {
				L1EffectInstance npc = (L1EffectInstance) lastAttacker;
				if (npc.getMaster() != null) {
					fightPc = (L1PcInstance) npc.getMaster();
				}
			}

			if (fightPc != null && lastAttacker.getId() != getId()) {
				if ((getFightId() == fightPc.getId()) && (fightPc.getFightId() == getId())) {
					setFightId(0);
					sendPackets(new S_PacketBox(5, 0, 0));
					fightPc.setFightId(0);
					fightPc.sendPackets(new S_PacketBox(5, 0, 0));
					return;
				}

				if ((L1PcInstance.this.isEncounter()) && (fightPc.getLevel() > getLevel())
						&& (fightPc.getLevel() - getLevel() >= 10)) {
					return;
				}

				if (castleWarResult()) {
					isWar = true;
				}

				if (simWarResult(lastAttacker)) {
					isWar = true;
				}

				if (L1PcInstance.this.isInWarAreaAndWarTime(L1PcInstance.this, fightPc)) {
					isWar = true;
				}

				if ((getLevel() >= ConfigKill.KILLLEVEL) && (!fightPc.isGm())) {
					boolean isShow = false;// 是否顯示殺人訊息
					if (isWar) {
						isShow = true;

					} else if (!isCombatZone) {
						isShow = true;
					}
					// 殺人公告
					if (isShow) {
						World.get().broadcastPacketToAllkill(new S_KillMessage(fightPc.getViewName(), getViewName()));
						fightPc.get_other().add_killCount(1);
						get_other().add_deathCount(1);
					}
				}

			}

			if (ConfigOther.FREE_FIGHT_SWITCH) {
				if (CheckFightTimeController.getInstance().isFightMap(L1PcInstance.this.getMapId())) {
					if (((L1PcInstance.this.getLawful() >= 0)
							&& (ThreadLocalRandom.current().nextInt(100) < ConfigOther.FREE_FIGHT_DROP_CHANCE_A))
							|| ((L1PcInstance.this.getLawful() < 0) && (ThreadLocalRandom.current()
									.nextInt(100) < ConfigOther.FREE_FIGHT_DROP_CHANCE_B))) {
						int dropCount = ThreadLocalRandom.current().nextInt(ConfigOther.FREE_FIGHT_MAX_DROP) + 1;

						L1PcInstance.this.caoPenaltyResult(dropCount);
					}
					return;
				}
			}

			if (isSafetyZone && !(lastAttacker instanceof L1MonsterInstance)) {
				return;
			}

			if (isCombatZone && !(lastAttacker instanceof L1MonsterInstance)) {
				return;
			}

			if (!getMap().isEnabledDeathPenalty()) {
				return;
			}

			// 位在戰爭旗中並且設定城戰中無死亡逞罰
			boolean castle_area = L1CastleLocation.checkInAllWarArea(getX(), getY(), getMapId());
			if ((castle_area) && (!ConfigAlt.ALT_WARPUNISHMENT)) {
				return;
			}

			c1TypeRate();// 陣營積分掉落判斷

			////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			boolean _shelterDropL = false; // 扣除不滅的加護
			boolean _shelterDropH = false; // 扣除高級不滅的加護
			L1ItemInstance shelterItem1 = getInventory().checkItemX(700060, 1); // 不滅的加護
			L1ItemInstance shelterItem2 = getInventory().checkItemX(700061, 1); // 高級不滅的加護
			if (shelterItem1 != null && shelterItem2 != null) {

				// 2個加護加護都有時，將會依照角色死亡時的死亡懲罰而扣除道具
				// 例如
				// 該次死亡懲罰會掉落道具，就會扣除高級不滅的加護
				// 該次死亡懲罰不會掉落道具，就會扣除不滅的加護

				// 用正義滿判斷不會掉落道具
				if (getLawful() >= 32767) {
					_shelterDropL = true; // 扣除不滅的加護
				} else {
					_shelterDropH = true; // 扣除高級不滅的加護
				}

			} else if (shelterItem1 != null && shelterItem2 == null) {

				// 只有不滅的加護時，就判斷扣除不滅加護，不會掉經驗，但會掉物品

				_shelterDropL = true; // 扣除不滅的加護

				if (getLawful() < 32767) {// 正義未滿
					if (castle_area) {// 位在戰爭旗中
						return;
					}
					if ((!isProtector()) || (ProtectorSet.DEATH_VALUE_ITEM)) {// 不是守護者OR守護者設定會掉落道具
						lostRate();// 物品掉落判斷
					}
				}

			} else if (shelterItem1 == null && shelterItem2 != null) {

				// 只有高級不滅的加護時，就判斷扣除高級不滅加護，不會掉經驗和物品

				_shelterDropH = true; // 高級不滅的加護

			} else {
				// 2個加護都沒有時，正常判斷掉落經驗和物品

				expRate();// 經驗值掉落的判斷

				if (getLawful() < 32767) {// 正義未滿
					if (castle_area) {// 位在戰爭旗中
						return;
					}
					if ((!isProtector()) || (ProtectorSet.DEATH_VALUE_ITEM)) {// 不是守護者OR守護者設定會掉落道具
						lostRate();// 物品掉落判斷
					}
				}
			}

			// 扣除不滅的加護
			if (_shelterDropL) {
				getInventory().removeItem(shelterItem1, 1); // 扣除不滅的加護
				sendPackets(new S_ServerMessage("\\fU不滅的加護消失了，沒有損失經驗。"));

				// 是被玩家殺死才掉落碎裂的不滅的加護
				if (fightPc != null) {
					// 掉落碎裂的不滅的加護
					final L1ItemInstance shelterDrop1 = ItemTable.get().createItem(700058); // 碎裂的不滅的加護
					shelterDrop1.set_showId(get_showId());
					World.get()
							.getInventory(getX() + _random.nextInt(4) - 2, getY() + _random.nextInt(4) - 2, getMapId())
							.storeItem(shelterDrop1);
				}
			}

			// 扣除高級不滅的加護
			if (_shelterDropH) {
				getInventory().removeItem(shelterItem2, 1); // 扣除高級不滅的加護
				sendPackets(new S_ServerMessage("\\fU高級不滅的加護消失了，沒有損失經驗和物品。"));

				// 是被玩家殺死才掉落碎裂的高級不滅的加護
				if (fightPc != null) {
					// 掉落碎裂的高級不滅的加護
					final L1ItemInstance shelterDrop2 = ItemTable.get().createItem(700059); // 碎裂的高級不滅的加護
					shelterDrop2.set_showId(get_showId());
					World.get()
							.getInventory(getX() + _random.nextInt(4) - 2, getY() + _random.nextInt(4) - 2, getMapId())
							.storeItem(shelterDrop2);
				}
			}
			////////////////////////////////////////////////////////////////////////////////////////////////////////
			////////////////////////////////////////////////////////////////////////////////////////////////////////

			if (lastAttacker instanceof L1MonsterInstance) {
				L1MonsterInstance mob = (L1MonsterInstance) lastAttacker;
				writeDeathlog2(L1PcInstance.this, mob, "掉%");
			} else {
				writeDeathlog(L1PcInstance.this, fightPc, "掉%");
			}

			if (getLawful() < 32767) {// 正義未滿
				if (castle_area) {// 位在戰爭旗中
					return;
				}
				if ((!isProtector()) || (ProtectorSet.DEATH_VALUE_ITEM)) {// 不是守護者OR守護者設定會掉落道具
					// lostRate();// 物品掉落判斷
					lostSkillRate();// 技能掉落的判斷
				}
			}

			if (fightPc != null && fightPc.getId() != getId()) {// 紫名、紅名判斷
				if (isWar) {// 戰爭中
					return;
				}

				// 積分掉落的判斷
				//this.c1TypeRate();

				// 經驗值掉落的判斷
				//this.expRate();

				// 道具奪取系統 by terry0412
				if (fightPc != null) {
					this.checkItemSteal(fightPc);
				}
				if ((fightPc.getClan() != null) && (getClan() != null)
						&& (WorldWar.get().isWar(fightPc.getClan().getClanName(), getClan().getClanName()))) {
					return;
				}

				if (fightPc.isSafetyZone()) {// 攻擊者在安區
					return;
				}

				if (fightPc.isCombatZone()) {// 攻擊者在戰鬥區
					return;
				}

				if ((getLawful() >= 0) && (!isPinkName())) {// 被攻擊者為正義屬性且沒有變紫
					boolean isChangePkCount = false;
					// if (fightPc.getLawful() < 30000) {
					fightPc.set_PKcount(fightPc.get_PKcount() + 1);
					isChangePkCount = true;
					if ((fightPc.isElf()) && (isElf())) {
						fightPc.setPkCountForElf(fightPc.getPkCountForElf() + 1);
					}
					// }

					fightPc.setLastPk();
					/** 正義值滿不會被警衛追殺 */
					if (fightPc.getLawful() == 32767) {
						fightPc.setLastPk(null);
					}
					if ((fightPc.isElf()) && (isElf())) {
						fightPc.setLastPkForElf();
					}

					int lawful = 0;

					if (fightPc.getLawful() >= 0) {// 藍人則紅10000
						lawful = -32768;
					} else if (fightPc.getLawful() < 0) {// 紅人殺人多500
						lawful = fightPc.getLawful() - 500;
					}

					if (lawful <= -32768) {
						lawful = -32768;
					}

					fightPc.setLawful(lawful);
					//fightPc.addLawful(lawful);

					fightPc.sendPacketsAll(new S_Lawful(fightPc));

					if (ConfigAlt.ALT_PUNISHMENT) {// 下地獄處罰
						if ((isChangePkCount) && (fightPc.get_PKcount() >= 5)) {
							fightPc.sendPackets(new S_BlueMessage(166, "你目前的擊殺得分為:" + fightPc.get_PKcount()));
						}
						/*
						 * else if ((isChangePkCount) && (fightPc.get_PKcount()
						 * >= 100)) { fightPc.beginHell(true); }
						 */
					}

				} else {
					setPinkName(false);
				}
			}
		}

		/**
		 * 陣營積分掉落判斷
		 */
		private void c1TypeRate() {
			if (CampSet.CAMPSTART) {
				if ((_c_power != null) && (_c_power.get_c1_type() != 0) && (_c_power.get_c1_type() != 0)) {
					if (_vip_2) {
						sendPackets(new S_ServerMessage("\\fU你已經啟動月卡積分保護！"));
						return;
					}
					// vip系統//src013
					if (L1PcInstance.this._death_score) {
						L1PcInstance.this.sendPackets(new S_ServerMessage("\\fU積分保護啟動！"));
						return;
					}

					L1ItemInstance item1 = getInventory().checkItemX(44165, 1L);
					if (item1 != null) {
						getInventory().removeItem(item1, 1L);
						sendPackets(new S_ServerMessage("\\fU你身上帶有" + item1.getName() + ",陣營積分受到守護!"));
						return;
					}
					L1Name_Power power = _c_power.get_power();
					int score = _other.get_score() - power.get_down();
					if (score > 0)
						_other.set_score(score);
					else {
						_other.set_score(0);
					}

					int lv = C1_Name_Type_Table.get().getLv(_c_power.get_c1_type(), _other.get_score());
					if (lv != _c_power.get_power().get_c1_id()) {
						_c_power.set_power(L1PcInstance.this, false);
						sendPackets(new S_ServerMessage("\\fR階級變更:" + _c_power.get_power().get_c1_name_type()));
						sendPacketsAll(new S_ChangeName(L1PcInstance.this, true));
					}
				}
			}
		}

		/**
		 * 道具奪取判斷 by terry0412
		 */
		private void checkItemSteal(final L1PcInstance fightPc) {
			// 沒有設置列表...
			if (ExtraItemStealTable.getInstance().getList().isEmpty()) {
				return;
			}

			// 目前時間
			final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			for (final L1ItemSteal itemSteal : ExtraItemStealTable.getInstance().getList()) {
				// 檢查身上是否有可被奪取的道具
				final L1ItemInstance steal_item = getInventory().findItemId(itemSteal.getItemId());
				if (steal_item == null) {
					continue;
				}

				// 限制可奪取玩家最低等級
				if (getLevel() < itemSteal.getLevel()) {
					continue;
				}

				// 限制可奪取玩家最低轉生數
				if (getMeteLevel() < itemSteal.getMeteLevel()) {
					continue;
				}

				// 死亡被奪取機率
				if (_random.nextInt(100) >= itemSteal.getStealChance()) {
					continue;
				}

				// 檢查身上是否有防止奪取的道具
				if (itemSteal.getAntiStealItemId() > 0
						&& getInventory().consumeItem(itemSteal.getAntiStealItemId(), 1)) {
					sendPackets(new S_SystemMessage(
							"由於身上有[" + ItemTable.get().getTemplate(itemSteal.getAntiStealItemId()).getNameId()
									+ "] 使你免於被對方奪取: " + steal_item.getLogName()));
					continue;
				}

				// 計算奪取數量
				long steal_count;

				// 可重疊物品
				if (steal_item.isStackable()) {
					// 設定隨機數量
					steal_count = _random
							.nextInt(Math.max(itemSteal.getMaxStealCount() - itemSteal.getMinStealCount(), 0) + 1)
							+ itemSteal.getMinStealCount();
					// 檢查擁有數量
					steal_count = steal_item.getCount() >= steal_count ? steal_count : steal_item.getCount();

				} else {
					// 非重疊物品永遠數量1
					steal_count = 1L;
					// 解除使用狀態
					getInventory().setEquipped(steal_item, false);
				}

				// 您損失了 %0。
				sendPackets(new S_ServerMessage(638, steal_item.getNumberedViewName(steal_count)));

				// 被奪取道具是否掉落在地面 (1=掉地面, 0=掉在攻擊者身上)
				if (itemSteal.isDropOnFloor()) {
					steal_item.set_showId(get_showId());
					// 轉移地面
					getInventory().tradeItem(steal_item, steal_count,
							World.get().getInventory(getX(), getY(), getMapId()));

					// 是否廣播
					if (itemSteal.isBroadcast()) {
						World.get().broadcastPacketToAll(new S_SystemMessage("玩家[" + getViewName() + "]死亡後, 不小心把["
								+ steal_item.getNumberedViewName(steal_count) + "]掉在地板上"));
					}

					// 記錄文件檔 by terry0412
					ConfigRecord.recordToFiles("死亡奪取物品",
							"IP(" + getNetConnection().getIp() + ")玩家【" + getName() + "】的【"
									+ steal_item.getNumberedViewName(steal_count) + ", (ObjId: " + steal_item.getId()
									+ ")】死亡後掉在地板上, 時間:(" + timestamp + ")",
							timestamp);

				} else {
					// 轉移攻擊者身上
					getInventory().tradeItem(steal_item, steal_count, fightPc.getInventory());

					// 獲得%0%o 。
					fightPc.sendPackets(new S_ServerMessage(403, steal_item.getNumberedViewName(steal_count)));

					// 是否廣播
					if (itemSteal.isBroadcast()) {
						World.get().broadcastPacketToAll(new S_SystemMessage("玩家[" + getViewName() + "]死亡後, 不小心被玩家["
								+ fightPc.getViewName() + "]搶走了[" + steal_item.getNumberedViewName(steal_count) + "]"));
					}
				}
			}
		}

		/**
		 * 經驗值掉落的判斷
		 */
		private void expRate() {

			if ((isProtector()) && (!ProtectorSet.DEATH_VALUE_EXP)) {
				return;
			}

			if (_vip_1) {
				sendPackets(new S_ServerMessage("\\fU你已經啟動月卡經驗保護！"));
				return;
			}

			// vip系統//src013
			if (L1PcInstance.this._death_exp) {
				L1PcInstance.this.sendPackets(new S_ServerMessage("\\fU經驗保護啟動！"));
				return;
			}

			L1ItemInstance item1 = getInventory().checkItemX(44164, 1L);
			if (item1 != null) {
				getInventory().removeItem(item1, 1L);
				sendPackets(new S_ServerMessage("\\fU你身上帶有" + item1.getName() + ",剛剛死掉沒有掉經驗!"));
				return;
			}

			if ((hasSkillEffect(8000)) && (getMapId() == 537)) {
				killSkillEffectTimer(8000);
				sendPackets(new S_ServerMessage("\\fU受到祝福之光的保護,剛剛死掉沒有掉%!"));
				return;
			}

			L1PcInstance.this.deathPenalty();

			L1PcInstance.this.setGresValid(true);

			if (getExpRes() == 0) {
				setExpRes(1);
			}
		}

		/**
		 * 物品掉落的判斷
		 */
		private void lostRate() {

			if (_vip_3) {
				sendPackets(new S_ServerMessage("\\fU你已經啟動月卡物品保護！"));
				return;
			}

			// vip 保護物品//src013
			if (L1PcInstance.this._death_item) {
				L1PcInstance.this.sendPackets(new S_ServerMessage("\\fU物品保護啟動！"));
				return;
			}

			L1ItemInstance item1 = getInventory().checkItemX(44163, 1L);
			if (item1 != null) {
				getInventory().removeItem(item1, 1L);
				sendPackets(new S_ServerMessage("\\fU你身上帶有" + item1.getName() + ",剛剛死掉沒有噴裝!"));
				return;
			}

			// 產生物品掉落機率
			// 正義質32000以上0%、每-1000增加0.4%
			// 正義質小於0 = 100%
			int lostRate = (int) (((getLawful() + 32768D) / 1000D - 65D) * 4D);

			if (lostRate < 0) {
				lostRate *= -1;
				if (getLawful() < 0) {
					lostRate = 1000;
				}
				final int rnd = _random.nextInt(1000) + 1;
				if (rnd <= lostRate) {
					int count = 0;
					int lawful = getLawful();
					if ((lawful >= -32768) && (lawful <= -30000)) {
						count = L1PcInstance._random.nextInt(4) + 1;
					} else if ((lawful > -30000) && (lawful <= -20000)) {
						count = L1PcInstance._random.nextInt(3) + 1;
					} else if ((lawful > -20000) && (lawful <= -10000)) {
						count = L1PcInstance._random.nextInt(2) + 1;
					} else if ((lawful > -10000) && (lawful <= 32767)) {
						count = L1PcInstance._random.nextInt(1) + 1;
					}

					if (count > 0) {
						L1PcInstance.this.caoPenaltyResult(count);
					}
				}
			}
		}

		/**
		 * 技能損失的判斷
		 */
		private void lostSkillRate() {
			if (_vip_4) {
				sendPackets(new S_ServerMessage("\\fU你已經啟動月卡技能保護！"));
				return;
			}
			// vip 技能保護 //src013
			if (L1PcInstance.this._death_skill) {
				L1PcInstance.this.sendPackets(new S_ServerMessage("\\fU技能保護啟動！"));
				return;
			}

			int skillCount = _skillList.size();

			if (skillCount > 0) {
				int count = 0;

				int lawful = getLawful();

				int random = L1PcInstance._random.nextInt(200);

				if (lawful <= -32768) {
					count = L1PcInstance._random.nextInt(4) + 1;
				} else if ((lawful > -32768) && (lawful <= -30000)) {
					if (random <= skillCount + 1) {
						count = L1PcInstance._random.nextInt(3) + 1;
					}
				} else if ((lawful > -30000) && (lawful <= -20000)) {
					if (random <= (skillCount >> 1) + 1) {
						count = L1PcInstance._random.nextInt(2) + 1;
					}
				} else if ((lawful > -20000) && (lawful <= -10000) && (random <= (skillCount >> 2) + 1)) {
					count = 1;
				}

				if (count > 0)
					L1PcInstance.this.delSkill(count);
			}
		}
	}

	/** 隊伍對決系統 */
	private boolean _isATeam = false;

	private boolean _isBTeam = false;

	public boolean isATeam() {
		return this._isATeam;
	}

	public void setATeam(boolean bool) {
		this._isATeam = bool;
	}

	public boolean isBTeam() {
		return this._isBTeam;
	}

	public void setBTeam(boolean bool) {
		this._isBTeam = bool;
	}

	// 血盟再加入時間
	private Timestamp _rejoinClanTime;

	public Timestamp getRejoinClanTime() {
		return _rejoinClanTime;
	}

	public void setRejoinClanTime(Timestamp time) {
		_rejoinClanTime = time;
	}

	// 角色生日
	/*private Timestamp _CreateTime;

	public Timestamp getCreateTime() {
		return _CreateTime;
	}

	public int getSimpleCreateTime() {
		if (_CreateTime != null) {
			SimpleDateFormat SimpleDate = new SimpleDateFormat("yyyyMMdd");
			int BornTime = Integer.parseInt(SimpleDate.format(_CreateTime.getTime()));
			return BornTime;
		} else {
			return 0;
		}
	}

	public void setCreateTime(Timestamp time) {
		_CreateTime = time;
	}

	public void setCreateTime() {
		_CreateTime = new Timestamp(System.currentTimeMillis());
	}*/

	/** 組隊類型 */
	private int _partyType;

	public void setPartyType(int type) {
		_partyType = type;
	}

	public int getPartyType() {
		return _partyType;
	}

	/** [原碼] 無限大戰計分系統 */

	private int _ubscore;

	public int getUbScore() {
		return _ubscore;
	}

	public void setUbScore(int i) {
		_ubscore = i;
	}

	/** [原碼] 定時外掛檢測 */
	private int _super;

	public int getSuper() {
		return _super;
	}

	public void setSuper(int i) {
		_super = i;
	}

	/** [原碼] 定時外掛檢測 */
	private int _super2;

	public int getSuper2() {
		return _super2;
	}

	public void setSuper2(int i) {
		_super2 = i;
	}

	private int _inputerror;// 輸入錯誤

	public int getInputError() {
		return _inputerror;
	}

	public void setInputError(int i) {
		_inputerror = i;
	}

	private int _speederror;// 加速器偵測 封鎖帳號判斷用錯誤次數

	public int getSpeedError() {
		return _speederror;
	}

	public void setSpeedError(int i) {
		_speederror = i;
	}

	private int _banerror;// 逾時斷線 封鎖帳號判斷用錯誤次數

	public int getBanError() {
		return _banerror;
	}

	public void setBanError(int i) {
		_banerror = i;
	}

	private int _inputbanerror;// 輸入錯誤斷線 封鎖帳號判斷用錯誤次數

	public int getInputBanError() {
		return _inputbanerror;
	}

	public void setInputBanError(int i) {
		_inputbanerror = i;
	}

	private int _reduction_dmg = 0;// 套裝減免物理傷害

	/**
	 * 套裝減免物理傷害
	 * @param add
	 */
	public void add_reduction_dmg(int add) {
		_reduction_dmg += add;
	}

	/**
	 * 套裝減免物理傷害
	 * @return
	 */
	public int get_reduction_dmg() {
		return _reduction_dmg;
	}

	// 7.6
	private Date _birthday;

	public void setBirthday(final String time) {
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyyMMdd").parse(time);
			_birthday = date;
		} catch (final ParseException e) {
			_log.error(e.getLocalizedMessage(), e);
		}

	}

	public Date getBirthDay() {
		return _birthday;
	}

	// 飾品開啟欄位判斷
	private int _Slot = 0;

	public void setSlot(int i) {
		_Slot = i;
	}

	public int getSlot() {
		return _Slot;
	}

	// 是否使用道具變身
	private boolean _itempoly = false;

	public void setItemPoly(boolean itempoly) {
		_itempoly = itempoly;
	}

	public boolean isItemPoly() {
		return _itempoly;
	}

	// 自訂變身卷軸
	private L1ItemInstance _polyscroll;

	public void setPolyScroll(L1ItemInstance item) {
		_polyscroll = item;
	}

	public L1ItemInstance getPolyScroll() {
		return _polyscroll;
	}

	// 墓碑系統
	private L1EffectInstance _tomb;

	public void set_tomb(L1EffectInstance tomb) {
		_tomb = tomb;
	}

	public L1EffectInstance get_tomb() {
		return _tomb;
	}

	// 是否魔法爆擊狀態
	private boolean _isMagicCritical;

	public void setMagicCritical(boolean flag) {
		_isMagicCritical = flag;
	}

	public boolean isMagicCritical() {
		return _isMagicCritical;
	}

	// 是否正在使用幻象的傲慢之塔移動傳送符
	private boolean _isPhantomTeleport;

	public void setPhantomTeleport(boolean flag) {
		_isPhantomTeleport = flag;
	}

	public boolean isPhantomTeleport() {
		return _isPhantomTeleport;
	}

	/**
	 * 奇岩地監/古魯丁地監 已使用時間(秒).
	 */
	private int _rocksPrisonTime;

	/**
	 * 取回奇岩地監/古魯丁地監已使用時間(秒).
	 * 
	 * @return
	 */
	public int getRocksPrisonTime() {
		return this._rocksPrisonTime;
	}

	/**
	 * 設定奇岩地監/古魯丁地監已使用時間.
	 * 
	 * @param time
	 *            時間
	 */
	public void setRocksPrisonTime(final int time) {
		this._rocksPrisonTime = time;
	}

	/**
	 * 拉斯塔巴德地監已使用時間(秒).
	 */
	private int _lastabardTime;

	/**
	 * 取回拉斯塔巴德地監已使用時間(秒).
	 * 
	 * @return
	 */
	public int getLastabardTime() {
		return this._lastabardTime;
	}

	/**
	 * 設定拉斯塔巴德監獄已使用時間.
	 * 
	 * @param time
	 *            時間
	 */
	public void setLastabardTime(final int time) {
		this._lastabardTime = time;
	}

	/**
	 * 象牙塔已使用時間(秒).
	 */
	private int _ivorytowerTime;

	/**
	 * 取回象牙塔已使用時間(秒).
	 * 
	 * @return
	 */
	public int getIvoryTowerTime() {
		return this._ivorytowerTime;
	}

	/**
	 * 設定象牙塔已使用時間.
	 * 
	 * @param time
	 *            時間
	 */
	public void setIvoryTowerTime(final int time) {
		this._ivorytowerTime = time;
	}

	/**
	 * 龍之谷地監已使用時間(秒).
	 */
	private int _dragonvalleyTime;

	/**
	 * 取回龍之谷地監已使用時間(秒).
	 * 
	 * @return
	 */
	public int getDragonValleyTime() {
		return this._dragonvalleyTime;
	}

	/**
	 * 設定龍之谷地監已使用時間.
	 * 
	 * @param time
	 *            時間
	 */
	public void setDragonValleyTime(final int time) {
		this._dragonvalleyTime = time;
	}
//新加限制時間地圖
	/**
	 * 新增限制時間地圖map1(秒).
	 */
	private int _timemap1;

	/**
	 * 取回新增限制時間地圖map1(秒).
	 * 
	 * @return
	 */
	public int gettimemap1() {
		return this._timemap1;
	}

	/**
	 * 設定新增限制時間地圖map1(秒).
	 * 
	 * @param time
	 *            時間
	 */
	public void settimemap1(final int time) {
		this._timemap1 = time;
	}
	/**
	 * 新增限制時間地圖map2(秒).
	 */
	private int _timemap2;

	/**
	 * 取回新增限制時間地圖map2(秒).
	 * 
	 * @return
	 */
	public int gettimemap2() {
		return this._timemap2;
	}

	/**
	 * 設定新增限制時間地圖map2(秒).
	 * 
	 * @param time
	 *            時間
	 */
	public void settimemap2(final int time) {
		this._timemap2 = time;
	}
	/**
	 * 新增限制時間地圖map2(秒).
	 */
	private int _timemap3;

	/**
	 * 取回新增限制時間地圖map3(秒).
	 * 
	 * @return
	 */
	public int gettimemap3() {
		return this._timemap3;
	}

	/**
	 * 設定新增限制時間地圖map3(秒).
	 * 
	 * @param time
	 *            時間
	 */
	public void settimemap3(final int time) {
		this._timemap3 = time;
	}
	/**
	 * 新增限制時間地圖map2(秒).
	 */
	private int _timemap4;

	/**
	 * 取回新增限制時間地圖map4(秒).
	 * 
	 * @return
	 */
	public int gettimemap4() {
		return this._timemap4;
	}

	/**
	 * 設定新增限制時間地圖map4(秒).
	 * 
	 * @param time
	 *            時間
	 */
	public void settimemap4(final int time) {
		this._timemap4 = time;
	}
	/**
	 * 噬魂塔副本已使用時間(秒).
	 */
	private int _SoulTime;
	
	
	/**
	 * 取回噬魂塔副本已使用時間(秒).
	 * 
	 * @return
	 */
	public int getSoulTime() {
		return this._SoulTime;
	}

	/**
	 * 設定噬魂塔副本已使用時間.
	 * 
	 * @param time 時間
	 */
	public void setSoulTime(final int time) {
		this._SoulTime = time;
	}
	
	/**
	 * 冰女副本已使用時間(秒).
	 */
	private int _iceTime;

	/**
	 * 取回冰女副本已使用時間(秒).
	 * 
	 * @return
	 */
	public int getIceTime() {
		return this._iceTime;
	}

	/**
	 * 設定冰女副本已使用時間.
	 * 
	 * @param time
	 *            時間
	 */
	public void setIceTime(final int time) {
		this._iceTime = time;
	}

	/**
	 * 媽祖已使用狀態.
	 */
	private int _MazuTime;

	/**
	 * 取回媽祖已使用狀態.
	 * 
	 * @return
	 */
	public int getMazuTime() {
		return this._MazuTime;
	}

	/**
	 * 設定媽祖已使用狀態.
	 * 
	 * @param i
	 *            1(已使用) or 0(未使用)
	 */
	public void setMazuTime(final int i) {
		this._MazuTime = i;
	}

	/**
	 * 重置所有限時地監已使用時間
	 */
	public void resetAllMapTime() {
		this._rocksPrisonTime = 0;
		this._lastabardTime = 0;
		this._ivorytowerTime = 0;
		this._dragonvalleyTime = 0;
		this._timemap1 = 0;
		this._timemap2 = 0;
		this._timemap3 = 0;
		this._timemap4 = 0;
		this._MazuTime = 0;
	}

	/**
	 * 取回計時地圖已使用時間(秒)
	 * 
	 * @param mapid
	 *            玩家所在的地圖
	 * @return
	 */
	public int getMapUseTime(final int mapid) {
		int result = 0;
		switch (mapid) {
		case 53: // 奇岩地監1F
		case 54: // 奇岩地監2F
		case 55: // 奇岩地監3F
		case 56: // 奇岩地監4F		
		case 15403:
		case 15404:
			result = this._rocksPrisonTime;
			break;
		case 280: // 象牙塔1F
		case 281: // 象牙塔2F
		case 282: // 象牙塔3F
		case 283: // 象牙塔4F
		case 284: // 象牙塔5F
			result = this._ivorytowerTime;
			break;
		case 285: // 突擊隊訓練場
		case 286: // 魔獸軍王之室
		case 287: // 黑魔法研究室
		case 288: // 法令軍王之室
		case 289: // 惡靈之主祭壇
			result = this._lastabardTime;
			break;
		case 4831:// 龍之谷地監 1樓		
			result = this._dragonvalleyTime;
		break;
		case 508:// 龍之谷地監 7樓
			result = this._timemap1;		
			break;
		case 430:// 龍之谷地監 1樓		
			result = this._timemap2;		
			break;
		case 103:// 龍之谷地監 1樓		
			result = this._timemap3;		
			break;
		case 807:// 龍之谷地監 1樓
		case 808:// 龍之谷地監 1樓
		case 809:// 龍之谷地監 1樓
		case 810:// 龍之谷地監 1樓
		case 811:// 龍之谷地監 1樓
		case 812:// 龍之谷地監 1樓
		case 813:// 龍之谷地監 1樓
			result = this._timemap4;		
			break;
		case 2101:// 冰女副本
		case 2151:// 冰女副本
			result = this._iceTime;
			break;

		}
		return result;
	}

	/**
	 * 設定地圖已使用時間(秒)
	 * 
	 * @param time
	 *            時間
	 */
	public void setMapUseTime(final int mapid, final int time) {
		switch (mapid) {
		case 53:// 奇岩地監1F
		case 54:// 奇岩地監2F
		case 55:// 奇岩地監3F
		case 56:// 奇岩地監4F
		case 807:// 新版古魯丁地監 1樓
		case 808:// 新版古魯丁地監 2樓
		case 809:// 新版古魯丁地監 3樓
		case 810:// 新版古魯丁地監 4樓
		case 811:// 新版古魯丁地監 5樓
		case 812:// 新版古魯丁地監 6樓
		case 813:// 新版古魯丁地監 7樓
			this.setRocksPrisonTime(time);
			break;
		case 75:// 象牙塔1F
		case 76:// 象牙塔2F
		case 77:// 象牙塔3F
		case 78:// 象牙塔4F
		case 79:// 象牙塔5F
		case 80:// 象牙塔6F
		case 81:// 象牙塔7F
		case 82:// 象牙塔8F
			this.setIvoryTowerTime(time);
			break;
		case 452: // 突擊隊訓練場
		case 453: // 魔獸軍王之室
		case 461: // 黑魔法研究室
		case 462: // 法令軍王之室
		case 471: // 惡靈之主祭壇
		case 475: // 冥法軍王之室
		case 479: // 拉斯塔巴德中央廣場
		case 492: // 暗殺軍王之室
		case 495: // 地下競技場
			this.setLastabardTime(time);
			break;
		case 30:// 龍之谷地監 1樓
		case 31:// 龍之谷地監 2樓
		case 32:// 龍之谷地監 3樓
		case 33:// 龍之谷地監 4樓
		case 35:// 龍之谷地監 5樓
		case 36:// 龍之谷地監 6樓
		case 37:// 龍之谷地監 7樓
			this.setDragonValleyTime(time);
			break;
		case 2101:// 冰女副本
		case 2151:// 冰女副本
			this.setIceTime(time);
			break;
		}
	}

	/**
	 * 是否在計時地圖 <br>
	 * 
	 * @return true:玩家位於限時的地圖內
	 */
	public boolean isInTimeMap() {
		final int map = this.getMapId();
		final int maxMapUsetime = MapsTable.get().getMapTime(map);
		return maxMapUsetime > 0;
	}

	private int _clanMemberId; // 血盟成員Id

	public int getClanMemberId() {
		return _clanMemberId;
	}

	public void setClanMemberId(int i) {
		_clanMemberId = i;
	}

	private String _clanMemberNotes; // 血盟成員備註

	public String getClanMemberNotes() {
		return _clanMemberNotes;
	}

	public void setClanMemberNotes(String s) {
		_clanMemberNotes = s;
	}

	private int _stunlevel = 0; // 昏迷等級

	public void addStunLevel(int add) {
		_stunlevel += add;
	}

	public int getStunLevel() {
		return _stunlevel;
	}

	private int _attackError = 0; // 暫存Boss攻擊失敗次數

	public void addAttackError(int add) {
		_attackError += add;
	}

	public void setAttackError(int add) {
		_attackError = add;
	}

	public int getAttackError() {
		return _attackError;
	}

	private String _attackBossName = ""; // 暫存Boss攻擊name

	public void setAttackBossName(String bossName) {
		_attackBossName = bossName;
	}

	public String getAttackBossName() {
		return _attackBossName;
	}

	// VIP能力資料
	private int _vipLevel;

	private Timestamp _startTime;

	private Timestamp _endTime;

	public int get_vipLevel() {
		return _vipLevel;
	}

	public void set_vipLevel(final int vipLevel) {
		_vipLevel = vipLevel;
	}

	private int _giftIndex;

	private boolean _isWaitEnd;

	public int getOnlineGiftIndex() {
		return _giftIndex;
	}

	public void setOnlineGiftIndex(final int onlineGiftIndex) {
		_giftIndex = onlineGiftIndex;
	}

	public boolean isOnlineGiftWiatEnd() {
		return _isWaitEnd;
	}

	public void setOnlineGiftWiatEnd(final boolean onlineGiftWiatEnd) {
		_isWaitEnd = onlineGiftWiatEnd;
	}

	public Timestamp getVipStartTime() {
		return _startTime;
	}

	public void setVipStartTime(final Timestamp vipStartTime) {
		_startTime = vipStartTime;
	}

	public Timestamp getVipEndTime() {
		return _endTime;
	}

	public void setVipEndTime(final Timestamp vipEndTime) {
		_endTime = vipEndTime;
	}

	public void setVipStatus() {

		if ((_startTime != null) && (_endTime != null)) {

			final long t = _endTime.getTime() - System.currentTimeMillis();
			if (t > 0L) {

				final L1Vip tmp = VipSetsTable._list_vip.get(_vipLevel);
				if (tmp != null) {

					addMaxHp(tmp.get_add_hp());
					addHpr(tmp.get_add_hpr());
					addMaxMp(tmp.get_add_mp());
					addMpr(tmp.get_add_mpr());
					addDmgup(tmp.get_add_dmg());
					addBowDmgup(tmp.get_add_bowdmg());
					addHitup(tmp.get_add_hit());
					addBowHitup(tmp.get_add_bowhit());
					addSp(tmp.get_add_sp());
					addMr(tmp.get_add_mr());
					addStr(tmp.getStr());
					addDex(tmp.getDex());
					addCon(tmp.getCon());
					addWis(tmp.getWis());
					addCha(tmp.getCha());
					addInt(tmp.getInt());
					set_expadd(tmp.getExpAdd());
				} else {
					this.sendPackets(new S_SystemMessage("VIP能力錯誤，請告知線上GM處理。"));
				}

				sendPackets(new S_VipTime(_vipLevel, _startTime.getTime(), _endTime.getTime()));
				sendPackets(new S_VipShow(getId(), get_vipLevel()));
				sendPackets(new S_OwnCharStatus(this));
				sendPackets(new S_SPMR(this));
				setSkillEffect(L1SkillId.VIP, (int) t);
			} else {
				_startTime = null;
				_endTime = null;
				setVipStartTime(_startTime);
				setVipEndTime(_endTime);
				set_vipLevel(0);
				try {
					saveVip();
				} catch (Exception e) {
					_log.info(getName() + " 清除VIP發生錯誤:" + e.getMessage());
				}
				System.out.println(getName() + " vip到期清除");

			}
		}
	}

	public void addVipStatus(final int dayCount, final int level) {
		if ((_endTime != null) && ((_endTime.getTime() - System.currentTimeMillis()) > 0L)) {
			removeSkillEffect(L1SkillId.VIP);
		}
		final long t = System.currentTimeMillis();
		_startTime = new Timestamp(t);
		_endTime = new Timestamp(t + (86400000L * dayCount));
		_vipLevel = level;

		setVipStatus();
	}

	public void endVipStatus() {
		final L1Vip tmp = VipSetsTable._list_vip.get(_vipLevel);
		if (tmp != null) {
			addMaxHp(-tmp.get_add_hp());
			addHpr(-tmp.get_add_hpr());
			addMaxMp(-tmp.get_add_mp());
			addMpr(-tmp.get_add_mpr());
			addDmgup(-tmp.get_add_dmg());
			addBowDmgup(-tmp.get_add_bowdmg());
			addHitup(-tmp.get_add_hit());
			addBowHitup(-tmp.get_add_bowhit());
			addSp(-tmp.get_add_sp());
			addMr(-tmp.get_add_mr());
			addStr(-tmp.getStr());
			addDex(-tmp.getDex());
			addCon(-tmp.getCon());
			addWis(-tmp.getWis());
			addCha(-tmp.getCha());
			addInt(-tmp.getInt());
			set_expadd(-tmp.getExpAdd());
		} else {
			this.sendPackets(new S_SystemMessage("VIP能力錯誤，請告知線上GM處理。"));
		}

		sendPackets(new S_VipTime(0, 0L, 0L));
		sendPacketsAll(new S_VipShow(getId(), 0));
		sendPackets(new S_OwnCharStatus(this));
		sendPackets(new S_SPMR(this));

		_startTime = null;
		_endTime = null;
		_vipLevel = 0;
	}
	// VIP END

	public L1DwarfForGameMallInventry getDwarfForGameMall() {
		return _dwarfForMALL;
	}

	public void updateGameMallMoney() {
		long money = 0;

		final L1ItemInstance moneyItem = getInventory().checkItemX(44070, 1);
		money = moneyItem == null ? 0 : moneyItem.getCount();

		sendPackets(new S_GameMallItemMoney(money));
	}

	public void sendPackets(final ArrayList<ServerBasePacket> packs) {
		if (getNetConnection() == null) {
			return;
		}
		try {
			int i = 0;
			for (final int length = packs.size(); i < length; i++) {
				sendPackets(packs.get(i));
			}

		} catch (final Exception e) {
			logout();
			close();
		}
	}

	private int _range = 0; // 設置攻擊距離

	/**
	 * 設置攻擊距離
	 * @param range
	 */
	public void setRange(final int range) {
		_range = range;
	}

	/**
	 * 設置攻擊距離
	 * @return
	 */
	public int getRange() {
		return _range;
	}

	/**
	 * 設置攻擊距離
	 */
	public void getWeaponRange() {
		boolean check = false;
		int range = 1;
		int type = 1;
		final L1ItemInstance weapon = getWeapon();
		if (weapon == null) {
			sendPackets(new S_PacketBox(S_PacketBox.WEAPON_RANGE, range, 0, check));
		} else {
			if (weapon.getItem().getType() == 4) { // 弓(雙手)
				range = 17;
			} else if ((weapon.getItem().getType() == 10) // 鐵手甲
					|| (weapon.getItem().getType() == 13)) { // 弓(單手)
				range = 14;
			} else if ((weapon.getItem().getType() == 5) // 矛(雙手)
					|| (weapon.getItem().getType() == 14) // 矛(單手)
					|| (weapon.getItem().getType() == 18)) { // 鎖鏈劍(單手)
				/*range = 1;
				final int polyId = getTempCharGfx();
				// 2格武器變身 這邊的編號有需要的話要補
				if ((polyId == 11330) || (polyId == 11344) || (polyId == 11351) || (polyId == 11368)
						|| (polyId == 12240) || (polyId == 12237) || (polyId == 11447) || (polyId == 11408)
						|| (polyId == 11409) || (polyId == 11410) || (polyId == 11411) || (polyId == 11418)
						|| (polyId == 15531) || (polyId == 15832) || (polyId == 15833) || (polyId == 15539)
						|| (polyId == 15537) || (polyId == 15534) || (polyId == 15599) || (polyId == 13152)
						|| (polyId == 13153) || (polyId == 12681) || (polyId == 12702) || (polyId == 11419) 
						|| (polyId == 12613) || (polyId == 12614) || (polyId == 13735) || (polyId == 13737)) {
					range = 2;
				} else if (!hasSkillEffect(SHAPE_CHANGE)) {
					range = 2;
				}*/
				range = 2;
			}

			if (isKnight()) {
				if (weapon.getItem().getType() == 3) { // 雙手劍(雙手)
					check = true;
				}
			} else if (isElf()) {
				if (hasSkillEffect(FIRE_BLESS)) {
					check = true;
				}
				if (((weapon.getItem().getType() == 4) // 弓(雙手)
						|| (weapon.getItem().getType() == 13)) // 弓(單手)
						&& (weapon.getItem().getType1() == 20)) { // 弓
					type = 3;
					check = true;
				}
			} else if (isDragonKnight()) {
				check = true;
				if ((weapon.getItem().getType() == 14) // 矛(單手)
						|| (weapon.getItem().getType() == 18)) { // 鎖鏈劍(單手)
					type = 10;
				}
			} else if (isDarkelf()) { // 黑妖新技能 暗殺者
				check = true;
				if ((weapon.getItem().getType() == 12)) {
					type = 4;
				}
			}

			if ((weapon.getItem().getType1() != 20) && (weapon.getItem().getType1() != 62)) {
				sendPackets(new S_PacketBox(S_PacketBox.WEAPON_RANGE, range, type, check));
			} else {
				sendPackets(new S_PacketBox(S_PacketBox.WEAPON_RANGE, range, 3, check));
			}
			setRange(range);
		}
	}

	private int[] _gfxids;

	private int _times;

	public void set_armorsets_gfx(final int[] _gfxids) {
		this._gfxids = _gfxids;
	}

	public int[] get_armorsets_gfx() {
		return _gfxids;
	}

	public void set_gfx_times(final int _times) {
		this._times = _times;
	}

	public int get_gfx_times() {
		return _times;
	}

	private L1ItemInstance _weaponWarrior;

	public L1ItemInstance getWeaponWarrior() {
		return _weaponWarrior;
	}

	public void setWeaponWarrior(final L1ItemInstance weapon) {
		_weaponWarrior = weapon;
	}

	public int colcTitanDmg() {
		if (getWeapon() == null) {
			return 0;
		}
		L1ItemInstance weapon = getWeapon();
		// 7.0 warrior slayer
		if ((getWeaponWarrior() != null) && is_change_weapon()) {
			weapon = getWeaponWarrior();
		}
		int dmg = weapon.getItem().getDmgLarge();
		dmg += weapon.getItem().getDmgModifier();
		dmg += weapon.getEnchantLevel();
		dmg *= 2;
		return dmg;
	}

	/**
	 * 狂戰士-已學泰坦系列技能並有結晶體
	 * @return
	 */
	public boolean isCrystal() {
		if (isSkillMastery(PASSIVE_TITANROCK)
				|| isSkillMastery(PASSIVE_TITANBULLET)
				|| isSkillMastery(PASSIVE_TITANMAGIC)
		) {
			if (getInventory().consumeItem(41246, 10)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 狂戰士-粉碎
	 * @return
	 */
	public boolean isCRASH() {
		if (isSkillMastery(PASSIVE_CRASH)) {
			return true;
		}
		return false;
	}

	/**
	 * 狂戰士-狂暴
	 * @return
	 */
	public boolean isFURY() {
		if (isSkillMastery(PASSIVE_FURY)) {
			return true;
		}
		return false;
	}

	/**
	 * 狂戰士-迅猛雙斧
	 * @return
	 */
	public boolean isSLAYER() {
		if (isSkillMastery(PASSIVE_SLAYER)) {
			return true;
		}
		return false;
	}

	/**
	 * 狂戰士-泰坦：岩石
	 * @return
	 */
	public boolean isTITANROCK() {
		if (isSkillMastery(PASSIVE_TITANROCK)) {
			return true;
		}
		return false;
	}

	/**
	 * 狂戰士-泰坦：子彈
	 * @return
	 */
	public boolean isTITANBULLET() {
		if (isSkillMastery(PASSIVE_TITANBULLET)) {
			return true;
		}
		return false;
	}

	/**
	 * 狂戰士-護甲身軀
	 * @return
	 */
	public boolean isARMORGARDE() {
		if (isSkillMastery(PASSIVE_ARMORGARDE)) {
			return true;
		}
		return false;
	}

	/**
	 * 狂戰士-泰坦：魔法
	 * @return
	 */
	public boolean isTITANMAGIC() {
		if (isSkillMastery(PASSIVE_TITANMAGIC)) {
			return true;
		}
		return false;
	}

	private boolean _change_weapon = false;

	public boolean is_change_weapon() {
		return _change_weapon;
	}

	public void set_change_weapon(final boolean flag) {
		_change_weapon = flag;
	}

	private int _giganticHp; // 戰士體能強化

	public int getGiganticHp() {
		return _giganticHp;
	}

	public void setGiganticHp(final int i) {
		_giganticHp = i;
	}

	private int _prideHp; // 騎士榮耀心

	public int getPrideHp() {
		return _prideHp;
	}

	public void setPrideHp(final int i) {
		_prideHp = i;
	}

	private int _attacktargetid; // 7.6連續攻擊

	public void setAttackTargetId(final int objid) {
		_attacktargetid = objid;
	}

	public int getAttackTargetId() {
		return _attacktargetid;
	}

	/**
	 * 等級速度加成
	 * @return
	 */
	public int getPolyStatus() {
		int poly = 0;
		if (getLevel() <= 29) {
			poly = 0;
		} else if ((getLevel() >= 30) && (getLevel() <= 44)) {
			poly = 1;
		} else if ((getLevel() >= 45) && (getLevel() <= 49)) {
			poly = 2;
		} else if (getLevel() == 50) {
			poly = 3;
		} else if (getLevel() == 51) {
			poly = 4;
		} else if ((getLevel() >= 52) && (getLevel() <= 54)) {
			poly = 5;
		} else if ((getLevel() >= 55) && (getLevel() <= 59)) {
			poly = 6;
		} else if ((getLevel() >= 60) && (getLevel() <= 64)) {
			poly = 7;
		} else if ((getLevel() >= 65) && (getLevel() <= 69)) {
			poly = 8;
		} else if ((getLevel() >= 70) && (getLevel() <= 74)) {
			poly = 9;
		} else if ((getLevel() >= 75) && (getLevel() <= 79)) {
			poly = 10;
		} else if (getLevel() >= 80) {
			poly = 11;
		}
		return poly;
	}

	/** 師徒系統 **/
	private int _masterid = 0;

	public int getMasterID() {
		return _masterid;
	}

	public void setMasterID(int i) {
		_masterid = i;
	}

	private Timestamp _rejoinMasterTime;

	public Timestamp getRejoinMasterTime() {
		return _rejoinMasterTime;
	}

	public void setRejoinMasterTime(Timestamp time) {
		_rejoinMasterTime = time;
	}

	public void setRejoinMasterTime() {
		_rejoinMasterTime = new Timestamp(System.currentTimeMillis());
	}

	/** 新外掛檢測 **/
	public long getAICheck() {
		long count = 0;
		if (this != null) {
			long pcCount = this.getInventory().countItems(20000);
			if (pcCount > 0) {
				count = pcCount;
			}
		}
		return count;
	}

	public void removeAICheck(int itemid, long count) {
		if (this != null) {
			this.getInventory().consumeItem(itemid, count);
		}
	}

	// --------------------------記時地圖-----------------
	private ConcurrentHashMap<Integer, Integer> mapTime;

	/**
	 * 重置記時地圖信息
	 */
	public void setMapTime(ConcurrentHashMap<Integer, Integer> map) {
		mapTime = map;
	}

	/**
	 * 更新記時地圖時間
	 */
	public void updateMapTime(int time) {
		int mapid = getMapId();
		if (mapid >= 4001 && mapid <= 4050) {
			mapid = 4001;
		}
		if (mapTime.get(mapid) == null) {
			return;
		}
		int temp = mapTime.get(mapid);
		mapTime.put((int) this.getMapId(), temp + time);
	}

	/**
	 * 返回記時地圖時間
	 */
	public int getMapTime(int mapid) {
		if (mapTime.get(mapid) == null) {
			_log.error("記時地圖ID:" + mapid + "不存在");
			return -1;
		}
		return mapTime.get(mapid);
	}

	/**
	 * 返回人物所有記時地圖信息
	 */
	public ConcurrentHashMap<Integer, Integer> getMapTime() {
		return mapTime;
	}

	/** 記時地圖ID */
	// private int timemapid;
	/** 是否在記時地圖 */
	private boolean isTimeMap;

	/** 記時地圖計時器 */
	// private TimeMap timemap;

	// /** 記時地圖ID */
	// public int getTimemapid() {
	// return timemapid;
	// }
	//
	// /** 記時地圖ID */
	// public void setTimemapid(int timemapid) {
	// this.timemapid = timemapid;
	// }

	/**
	 * 是否在記時地圖
	 * 
	 * @return true 在記時地圖
	 * @return false 不在記時地圖
	 */
	public boolean isTimeMap() {
		return isTimeMap;
	}

	/**
	 * 是否在記時地圖
	 * 
	 * @return true 在記時地圖
	 * @return false 不在記時地圖
	 */
	public void setTimeMap(boolean isTimeMap) {
		this.isTimeMap = isTimeMap;
	}

	// /** 記時地圖記時器 */
	// public TimeMap getTimemap() {
	// return timemap;
	// }
	//
	// /** 記時地圖記時器 */
	// public void setTimemap(TimeMap timemap) {
	// this.timemap = timemap;
	// }

	/** 記時地圖停止 */
	public void stopTimeMap() {
		if (this.isTimeMap) {
			this.isTimeMap = false;
			// MapTimeTable.get().deluser(this);

			// this.sendPackets(new S_PacketBoxMapTimerOut(this));
		}
	}

	/**
	 * 記時地圖開始
	 */
	public void startTimeMap() {
		// final int INTERVAL = 1000;
		if (!this.isTimeMap) {
			this.isTimeMap = true;
			MapTimeTable.get().adduser(this);
		}
	}

    // 火龍窟副本
	/** 火龍窟副本狀態控制 */
	public int ValakasStatus = 0;

	private boolean ValakasCheck() {
		if (this.getOnlineStatus() == 0)
			return false;
		if (this.getNetConnection() == null)
			return false;
		if (!(getMapId() >= 2600 && getMapId() <= 2699))
			return false;
		return true;
	}

	public ValakasThreads _valakastell = null;

	public void ValakasInnEndTel() {
		if (_valakastell != null) {
			_valakastell.end();
		}
		_valakastell = new ValakasThreads(this);
		GeneralThreadPool.get().execute(_valakastell);
	}

	class ValakasThreads implements Runnable {
		L1PcInstance _pc = null;
		boolean valakasck = true;
		int valakastime = 1800;

		public void settime(int t) {
			valakastime = t;
		}

		public void end() {
			valakasck = false;
		}

		public ValakasThreads(L1PcInstance pc) {
			_pc = pc;
		}

		@Override
		public void run() {
			try {
				while (valakasck) {
					if (!ValakasCheck()) {
						valakasck = false;
						return;
					}
					if (valakastime == 1800) {
						_pc.sendPackets(new S_AllChannelsChat("吸收死亡騎士的精神.(剩餘時間: 30分)", 2));//$18648
					} else if (valakastime == 1200) {
						_pc.sendPackets(new S_AllChannelsChat("感受到死亡騎士的意志.(剩餘時間: 20分)", 2));//$18649
					} else if (valakastime == 600) {
						_pc.sendPackets(new S_AllChannelsChat("劍在一直在火燒中.(剩餘時間: 10分)", 2));//$18650
					} else if (valakastime == 300) {
						_pc.sendPackets(new S_AllChannelsChat("劍的力量慢慢的在消失.(剩餘時間: 5分)", 2));//$18651
					} else if (valakastime == 180) {
						_pc.sendPackets(new S_AllChannelsChat("時間剩不久.(剩餘時間: 3分)", 2));//$18652
					} else if (valakastime == 60) {
						_pc.sendPackets(new S_AllChannelsChat("意識昏迷中.(剩餘時間: 1分)", 2));//$18653
					}

					if (valakastime < 5) {
						_pc.sendPackets(new S_ServerMessage(1484 - valakastime));
					} else if (valakastime == 10) {
						_pc.sendPackets(new S_ServerMessage(1478));
					} else if (valakastime == 20) {
						_pc.sendPackets(new S_ServerMessage(1477));
					} else if (valakastime == 30) {
						_pc.sendPackets(new S_ServerMessage(1476));
					}
					if (valakastime <= 0) {
						L1Teleport.teleport(L1PcInstance.this, 33702, 32502, (short) 4, 5, true);
						valakasck = false;
						return;
					}
					valakastime--;
					Thread.sleep(1000);
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
    // 火龍窟副本end

	private int _twotimes = 0;

	public int get_twotimes() {
		return this._twotimes;
	}

	public void set_twotimes(int _twotimes) {
		this._twotimes = _twotimes;
	}

	private String _vip_title = null;

	public void set_vip_title(String vip_title) {
		this._vip_title = vip_title;
	}

	public String get_vip_title() {
		return this._vip_title;
	}

	private boolean _checkSustainEffect = false;

	// 套裝特效
	private SustainEffect SustainEffect;

	public void startSustainEffect(L1PcInstance pc, int effect_id, int Interval) {
		if (!_checkSustainEffect) {
			SustainEffect = new SustainEffect(pc, effect_id);
			_regenTimer.scheduleAtFixedRate(SustainEffect, Interval, Interval);
			_checkSustainEffect = true;
		}
	}

	public void stopSustainEffect() {
		if (_checkSustainEffect) {
			SustainEffect.cancel();
			SustainEffect = null;
			_checkSustainEffect = false;
		}
	}

	private int _mcdmgModifierByArmor = 0; // 防具增加物理傷害

	public int getmcDmgModifierByArmor() {
		return this._mcdmgModifierByArmor;
	}

	public void addmcDmgModifierByArmor(final int i) {
		this._mcdmgModifierByArmor += i;
	}

	private int _Weaponsprobability = 0; // 增加娃娃魔法武器發動機率

	public int getWeaponsprobability() {
		return this._Weaponsprobability;
	}

	public void addWeaponsprobability(final int i) {
		this._Weaponsprobability += i;
	}

	private int _Weapondmg = 0; // 增加娃娃魔法武器傷害

	public int getWeapondmg() {
		return this._Weapondmg;
	}

	public void addWeapondmg(final int i) {
		this._Weapondmg += i;
	}

	private int _Propertyprobability = 0; // 增加娃娃屬性卷機率

	public int getPropertyprobability() {
		return this._Propertyprobability;
	}

	public void addPropertyprobability(final int i) {
		this._Propertyprobability += i;
	}

	private int _gfx = 0;

	public void set_gfx(int _gfx) {
		this._gfx = _gfx;
	}

	public int get_gfx() {
		return this._gfx;
	}

	private int _time = 0;

	public void set_time(int _time) {
		this._time = _time;
	}

	public int get_time() {
		return this._time;
	}

	// 城堡額外附加能力 by terry0412
	private ArrayList<Integer> _castleAbility;

	public final boolean isCastleAbility(final int value) {
		if (_castleAbility == null) {
			_castleAbility = new ArrayList<Integer>();
		}
		return _castleAbility.contains(Integer.valueOf(value));
	}

	public final void addCastleAbility(final int value) {
		if (_castleAbility == null) {
			_castleAbility = new ArrayList<Integer>();
		}
		_castleAbility.add(Integer.valueOf(value));
	}

	public final void removeCastleAbility(final int value) {
		if (_castleAbility == null) {
			_castleAbility = new ArrayList<Integer>();
		}
		_castleAbility.remove(Integer.valueOf(value));
	}

	private long _shopAdenaRecord;

	public final long getShopAdenaRecord() {
		return _shopAdenaRecord;
	}

	public final void setShopAdenaRecord(final long i) {
		_shopAdenaRecord = i;
	}

	private final Map<Integer, L1ItemPower_text> _powers = new ConcurrentHashMap<Integer, L1ItemPower_text>();

	public void add_power(L1ItemPower_text value) {
		if (!_powers.containsKey(value.get_id())) {
			_powers.put(value.get_id(), value);
			value.add_pc_power(this);
			// sendPackets(new S_ServerMessage("\\fW" +
			// value.getMsg()));//src002
			// 套裝效果動畫
			if (value.getGfx() != null) {
				for (int gfx : value.getGfx()) {
					// 動畫效果
					sendPacketsX8(new S_SkillSound(getId(), gfx));
				}
			}
		}
	}

	public void remove_power(L1ItemPower_text value) {
		if (_powers.containsKey(value.get_id())) {
			_powers.remove(value.get_id());
			value.remove_pc_power(this);
			// sendPackets(new S_ServerMessage("\\fY失去 " + value.getMsg() + "
			// 效果"));//src002
		}
	}

	public boolean get_power_contains(L1ItemPower_text value) {
		return _powers.containsValue(value);
	}

	public Map<Integer, L1ItemPower_text> get_powers() {
		return _powers;
	}

	/**
	 * 經驗值增加
	 */

	private double _expRateToPc = 0.0;

	public void addExpRateToPc(int s) {// src013
		if (s > 0) {
			_expRateToPc = DoubleUtil.sum(_expRateToPc, (double) s / 100D);
		} else
			_expRateToPc = DoubleUtil.sub(_expRateToPc, (double) (s * -1) / 100D);
	}

	public double getExpRateToPc() {
		if (_expRateToPc < 0.0D) {
			return 0.0D;
		} else {
			return _expRateToPc;
		}
	}

	// TODO Roy 直接給予 Exp 值
	public synchronized void setExp_Direct(final long i) {
		this.setExp(i);
		this.onChangeExp();
	}

	private int _amount = 0;

	public int getAmount() {
		return this._amount;
	}

	public void setAmount(int i) {
		this._amount = i;
	}

	// 武器DIY特效
	private int _gfx1 = 0;

	public void set_DIY_gfx(int _gfx) {
		this._gfx1 = _gfx;
	}

	public int get_DIY_gfx() {
		return this._gfx1;
	}

	private int _time1 = 0;

	public void set_DIY_time(int _time) {
		this._time1 = _time;
	}

	public int get_DIY_time() {
		return this._time1;
	}

	// 特效編號 (每XX秒出現1次) by terry0412
	private int _effectId;

	public int getEffectId() {
		return _effectId;
	}

	public void setEffectId(int i) {
		_effectId = i;
	}

	// 抽抽樂
	public boolean isShow_Open_PandoraMsg() {
		return this._isShow_Open_PandoraMsg;
	}

	public void setShow_Open_PandoraMsg(boolean flag) {
		this._isShow_Open_PandoraMsg = flag;
	}

	public L1PandoraInventory getPandoraInventory() {
		return this._pandora;
	}// end

	/**
	 * 挂机开始
	 * 
	 * @return
	 */
	public final L1HateList _hateList = new L1HateList();// 目标清单
	private boolean _firstAttack = false;
	protected NpcMoveExecutor _pcMove = null;// XXX
	private int move = 0;

	/**
	 * 启用PC AI
	 */
	public synchronized void startAI() {
		if (this.isDead()) {
			return;
		}
		if (this.isGhost()) {
			return;
		}
		if (this.getCurrentHp() <= 0) {
			return;
		}
		if (this.isPrivateShop()) {
			return;
		}
		if (this.isParalyzed()) {
			return;
		}

		if (_pcMove != null) {
			_pcMove = null;
		}
		_pcMove = new pcMove(this);
		this.setAiRunning(true);
		this.setActived(true);
		final PcAI npcai = new PcAI(this);
		npcai.startAI();
	}

	private boolean _aiRunning = false; // PC AI时间轴 正在运行

	/**
	 * PC AI时间轴 正在运行
	 * 
	 * @param aiRunning
	 */
	protected void setAiRunning(final boolean aiRunning) {
		this._aiRunning = aiRunning;
	}

	/**
	 * PC AI时间轴 正在运行
	 * 
	 * @return
	 */
	protected boolean isAiRunning() {
		return this._aiRunning;
	}

	/**
	 * 清除全部目标
	 */
	public void allTargetClear() {
		// XXX
		if (_pcMove != null) {
			_pcMove.clear();
		}
		_hateList.clear();
		_target = null;
		setFirstAttack(false);
	}

	/**
	 * 清除单个目标
	 */
	public void targetClear() {
		if (_target == null) {
			return;
		}
		_hateList.remove(_target);
		_target = null;
	}

	/**
	 * 有效目标检查
	 */
	public void checkTarget() {
		try {
			if (_target == null) {// 目标为空
				// targetClear();
				return;
			}
			if (_target.getMapId() != getMapId()) {// 目标地图不相等
				targetClear();
				return;
			}
			if (_target.getCurrentHp() <= 0) {// 目标HP小于等于0
				targetClear();
				return;
			}

			if (_target.isDead()) {// 目标死亡
				targetClear();
				return;
			}
			if (_target instanceof L1PetInstance) {
				targetClear();
				return;
			}
			if (!_hateList.containsKey(_target)) {// 目标不在已有攻击清单中
				targetClear();
				return;
			}
			final int distance = getLocation().getTileDistance(
					_target.getLocation());
			if (distance > 15) {
				targetClear();
				return;
			}

		} catch (final Exception e) {
			return;
		}
	}

	/**
	 * 现在目标
	 */
	public L1Character is_now_target() {
		return _target;
	}

	/**
	 * 对目标进行攻击
	 * 
	 * @param target
	 */
	public void attackTarget(final L1Character target) {

		if (this.getInventory().getWeight240() >= 197) { // 重量过重
			// 110 \f1当负重过重的时候，无法战斗。
			this.sendPackets(new S_ServerMessage(110));
			// _log.error("要求角色攻击:重量过重");
			return;
		}
		if (target instanceof L1PcInstance) {
			final L1PcInstance player = (L1PcInstance) target;
			if (player.isTeleport()) { // テレポート处理中
				return;
			}
			if (!player.isPinkName()) {
				this.allTargetClear();
				return;
			}

		} else if (target instanceof L1PetInstance) {
			final L1PetInstance pet = (L1PetInstance) target;
			final L1Character cha = pet.getMaster();
			if (cha instanceof L1PcInstance) {
				final L1PcInstance player = (L1PcInstance) cha;
				if (player.isTeleport()) { // テレポート处理中
					return;
				}
			}

		} else if (target instanceof L1SummonInstance) {
			final L1SummonInstance summon = (L1SummonInstance) target;
			final L1Character cha = summon.getMaster();
			if (cha instanceof L1PcInstance) {
				final L1PcInstance player = (L1PcInstance) cha;
				if (player.isTeleport()) { // テレポート处理中
					return;
				}
			}
		}

		if (target instanceof L1NpcInstance) {
			final L1NpcInstance npc = (L1NpcInstance) target;
			if (npc.getHiddenStatus() != 0) { // 地中に潜っているか、飞んでいる
				this.allTargetClear();
				return;
			}
		}
		if (target instanceof L1PetInstance) {
			this.allTargetClear();
			return;
		}
		if (target.getCurrentHp() > 0 && !target.isDead()) {
			target.onAction(this);
		}
	}

	/**
	 * 对目标进行攻击
	 * 
	 * @param target
	 */
	public void attackTarget1(final L1Character target) {

		if (this.getInventory().getWeight240() >= 197) { // 重量过重
			// 110 \f1当负重过重的时候，无法战斗。
			this.sendPackets(new S_ServerMessage(110));
			// _log.error("要求角色攻击:重量过重");
			return;
		}
		if (target instanceof L1PcInstance) {
			final L1PcInstance player = (L1PcInstance) target;
			if (player.isTeleport()) { // テレポート处理中
				return;
			}
			if (!player.isPinkName()) {
				this.allTargetClear();
				return;
			}

		} else if (target instanceof L1PetInstance) {
			final L1PetInstance pet = (L1PetInstance) target;
			final L1Character cha = pet.getMaster();
			if (cha instanceof L1PcInstance) {
				final L1PcInstance player = (L1PcInstance) cha;
				if (player.isTeleport()) { // テレポート处理中
					return;
				}
			}

		} else if (target instanceof L1SummonInstance) {
			final L1SummonInstance summon = (L1SummonInstance) target;
			final L1Character cha = summon.getMaster();
			if (cha instanceof L1PcInstance) {
				final L1PcInstance player = (L1PcInstance) cha;
				if (player.isTeleport()) { // テレポート处理中
					return;
				}
			}
		}

		if (target instanceof L1NpcInstance) {
			final L1NpcInstance npc = (L1NpcInstance) target;
			if (npc.getHiddenStatus() != 0) { // 地中に潜っているか、飞んでいる
				this.allTargetClear();
				return;
			}
		}
		if (target instanceof L1PetInstance) {
			this.allTargetClear();
			return;
		}
		if (target.getCurrentHp() > 0 && !target.isDead()) {
			// target.onAction(this);

			// 挂机技能部分
			final int distance = getLocation().getTileDistance(
					_target.getLocation());
			L1SkillUse l1skilluse = new L1SkillUse();
			if (this.getygjnzc() > 0) {
				final L1Skills skill = SkillsTable.get().getTemplate(
						this.getygjnzc());
				if (skill != null) {
					int mpConsume = skill.getMpConsume();
					int hpConsume = skill.getHpConsume();
					if (mpConsume > 0 && this.getCurrentMp() <= mpConsume) {

						return;
					}
					if (hpConsume > 0 && this.getCurrentHp() <= hpConsume) {

						return;
					}
					// 技能施放延遲狀態中使用不可
					if (isSkillDelay()) {
						return;
					}
					if (!this.hasSkillEffect(this.getId()) && distance <= skill.getRanged()
							&& distance >= this.getygjlsz()) {
						l1skilluse.handleCommands(this, this.getygjnzc(),
								target.getId(), target.getX(), target.getY(),
								0, L1SkillUse.TYPE_GMBUFF);
						this.setSkillEffect(this.getId(),this.getgjjgsj() * 1000);
						this.killSkillEffectTimer(L1SkillId.MEDITATION);
						if (mpConsume > 0) {
							this.setCurrentMp(this.getCurrentMp() - mpConsume);
						}
						if (hpConsume > 0) {
							this.setCurrentMp(this.getCurrentHp() - hpConsume);
						}
					}
				}
			}
			if (this.getgjjnzc() > 0) {
				final L1Skills skill1 = SkillsTable.get().getTemplate(
						this.getgjjnzc());
				if (skill1 != null) {
					int mpConsume1 = skill1.getMpConsume();
					int hpConsume1 = skill1.getHpConsume();
					if (mpConsume1 > 0 && this.getCurrentMp() <= mpConsume1) {

						return;
					}
					if (hpConsume1 > 0 && this.getCurrentHp() <= hpConsume1) {

						return;
					}
					if (this.getMaxMp() * this.getgjml() / 100 > this
							.getCurrentMp()) {

						return;
					}
					if (this.getgjjnzc() == 187) {
						if (this.getMaxHp() * this.getgjml() / 100 > this
								.getCurrentHp()) {

							return;
						}
					}
					// 技能施放延遲狀態中使用不可
					if (isSkillDelay()) {
						return;
					}

					if (!this.hasSkillEffect(this.getId())&& distance <= skill1.getRanged()
							&& distance < this.getygjlsz()) {
						if (CharSkillReading.get().spellCheck(this.getId(),
								this.getgjjnzc())) {
							l1skilluse.handleCommands(this, this.getgjjnzc(),
									target.getId(), target.getX(),
									target.getY(), 0, L1SkillUse.TYPE_GMBUFF);
							this.killSkillEffectTimer(L1SkillId.MEDITATION);
							setjnxh(false);
							this.setSkillEffect(this.getId(),this.getgjjgsj() * 1000);
							if (mpConsume1 > 0) {
								this.setCurrentMp(this.getCurrentMp()
										- mpConsume1);
							}
							if (hpConsume1 > 0) {
								this.setCurrentMp(this.getCurrentHp()
										- hpConsume1);
							}
						}
					}
				}
			}
		}
	}

	/** 搜索附近有没有怪物 **/
	public void searchTarget() {
		// 攻击目标搜寻
		// System.out.println("AI启动44444");
		// L1MonsterInstance targetPlayer = searchTarget(this);
		// System.out.println("AI启动666==" + targetPlayer);
		// if (targetPlayer != null) {
		// _hateList.add(targetPlayer, 0);
		// _target = targetPlayer;
		//
		// }
		final Collection<L1Object> allObj = World.get().getVisibleObjects(this,
				-1);
		for (final Iterator<L1Object> iter = allObj.iterator(); iter.hasNext();) {
			final L1Object obj = iter.next();
			if (!(obj instanceof L1MonsterInstance)) {
				continue;
			}
			final L1MonsterInstance mob = (L1MonsterInstance) obj;
			if (mob.isDead()) {
				continue;
			}
			if (mob.getCurrentHp() <= 0) {
				continue;
			}
			if (mob.getHiddenStatus() > 0) {
				continue;
			}
			if (mob.getAtkspeed() == 0) {
				continue;
			}
			if (mob.hasSkillEffect(this.getId() + 100000)
					&& !this.isAttackPosition(mob.getX(), mob.getY(), 1)) {
				continue;
			}
			if (mob != null) {
				int Distance = 10 - this.getTileLineDistance(mob);
				if (obj instanceof L1NpcInstance) {
					final L1NpcInstance npc = (L1NpcInstance) obj;
					if (!npc.getHateList().isEmpty()
							&& !npc.getHateList().containsKey(this)
							&& limao() == true) {
						targetClear();
						Distance = +30;
						return;
					}
				}
				/*
				 * if (this.glanceCheck(mob.getX(), mob.getY())) { Distance = +
				 * 30; }
				 */
				_hateList.add(mob, Distance);

			}
		}
		_target = _hateList.getMaxHateCharacter();
		// System.out.println("AI启动666 _target==:" + _target);
		if ((this.getMap().isTeleportable() || this.getInventory().checkItem(
				99257))
				&& move >= 30 && getlslocx() == 0 && getlslocy() == 0) { // 地图可瞬移身上有白瞬卷
																			// QQ：403471355
			L1Teleport.randomTeleport(this, true);
			move = 0;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		if (_target == null && getgjsykg() == true) { // 如果目标等于空

			// 等待处理，，瞬移。。等等设置
			if (this.getMap().isTeleportable()
					&& (this.getInventory().consumeItem(40100, 1) || this
							.getInventory().checkItem(99257))) { // 地图可瞬移身上有白瞬卷
																	// QQ：403471355
				L1Teleport.randomTeleport(this, true);
				move = 0;
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			/*
			 * if (this.getInventory().checkItem(99257)) { // 地图可瞬移身上有白瞬卷 //
			 * QQ：403471355 L1Teleport.randomTeleport(this, true); move = 0; try
			 * { Thread.sleep(500); } catch (InterruptedException e) { // TODO
			 * 自动生成的 catch 块 e.printStackTrace(); } }
			 */
		}
		allObj.clear();
	}

	// private L1MonsterInstance searchTarget(L1PcInstance pc) {
	// System.out.println("AI启动55555");
	// L1MonsterInstance targetPlayer = null;
	//
	// for (final L1Object npc : World.get().getVisibleObjects(pc)) {
	// try {
	// Thread.sleep(10);
	// } catch (InterruptedException e) {
	// _log.error(e.getLocalizedMessage(), e);
	// }
	// if (npc instanceof L1MonsterInstance) {
	// final L1MonsterInstance mob = (L1MonsterInstance) npc;
	// if (mob.isDead()) {
	// continue;
	// }
	// if (mob.getCurrentHp() <= 0) {
	// continue;
	// }
	// if (mob.getHiddenStatus() > 0) {
	// continue;
	// }
	// if (mob.getAtkspeed() == 0) {
	// continue;
	// }
	// if(mob.hasSkillEffect(this.getId() + 100000)) {//暂不攻击状态 QQ：403471355
	// continue;
	// }
	//
	// targetPlayer = mob;
	// }
	// }
	// return targetPlayer;
	// }

	/**
	 * 具有目标的处理 (攻击的判断)
	 */
	public void onTarget() {
		try {
			// System.out.println("具有目标的处理");
			// setActived(true);

			// ここから先は_targetが变わると影响出るので别领域に参照确保
			final L1Character target = _target;

			if (target == null) {
				return;
			}
			attack(target);

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	private void attack(L1Character target) {
		// 攻击可能位置
		int attack_Range = 1;
		if (this.getWeapon() != null) {
			attack_Range = this.getWeapon().getItem().getRange();
		}
		if (attack_Range < 0) {
			attack_Range = 12;
		}
		if (attack_Range > 12) {
			return;
		}
		attackTarget1(target);
		if (isAttackPosition(target.getX(), target.getY(), attack_Range)) {// 已经到达可以攻击的距离
			setHeading(targetDirection(target.getX(), target.getY()));
			attackTarget(target);
			move = 0;
			// XXX
			if (_pcMove != null) {
				_pcMove.clear();
			}

		} else { // 攻击不可能位置
			// final int distance = getLocation().getTileDistance(
			// target.getLocation());
			if (_pcMove != null) {
				final int dir = _pcMove.moveDirection(target.getX(),
						target.getY());
				if (dir == -1) {
					_target.setSkillEffect(this.getId() + 100000, 20000);// 给予20秒状态
					targetClear();
				} else {
					_pcMove.setDirectionMove(dir);
					move++;
					// setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
				}
			}
		}
	}

	private boolean _actived = false; // 挂机激活
	private boolean _Pathfinding = false; // 寻路中.. QQ：403471355

	/**
	 * PC已经激活
	 * 
	 * @param actived
	 *            true:激活 false:无
	 */
	public void setActived(final boolean actived) {
		this._actived = actived;
	}

	/**
	 * PC已经激活
	 * 
	 * @return true:激活 false:无
	 */
	public boolean isActived() {
		return this._actived;
	}

	protected void setFirstAttack(final boolean firstAttack) {
		this._firstAttack = firstAttack;
	}

	protected boolean isFirstAttack() {
		return this._firstAttack;
	}

	/**
	 * 攻击目标设置
	 * 
	 * @param cha
	 * @param hate
	 */
	public void setHate(final L1Character cha, int hate) {
		try {
			if ((cha != null) && /* (cha.getId() != getId()) */_target != null) {
				if (!isFirstAttack() && (hate > 0)) {
					// hate += getMaxHp() / 10; // ＦＡヘイト
					setFirstAttack(true);
					if (_pcMove != null) {
						_pcMove.clear();// XXX
					}
					// System.out.println("isFirstAttack=" + isFirstAttack());
					_hateList.add(cha, 5);
					_target = _hateList.getMaxHateCharacter();
					checkTarget();
				}
			}

		} catch (final Exception e) {
			return;
		}
	}

	/**
	 * 目标为空挂机寻路中
	 * 
	 * @return
	 */
	public boolean isPathfinding() {
		return this._Pathfinding;
	}

	public void setPathfinding(final boolean fla) {
		this._Pathfinding = fla;
	}

	// 随机移动距离
	// private int _randomMoveDistance = 0;
	// 随机移动方向
	private int _randomMoveDirection = 0;

	public int getrandomMoveDirection() {
		return _randomMoveDirection;
	}

	public void setrandomMoveDirection(int randomMoveDirection) {
		this._randomMoveDirection = randomMoveDirection;
	}

	/**
	 * 没有目标的处理 (传回本次AI是否执行完成)<BR>
	 * 具有主人 跟随主人移动
	 * 
	 * @return true:本次AI执行完成 <BR>
	 *         false:本次AI执行未完成
	 */
	public void noTarget() {
		// 如果移动距离已经为0 重新定义随机移动
		// if (_randomMoveDistance == 0) {
		// // 产生移动距离
		// _randomMoveDistance = _random.nextInt(3) + 1;

		// 产生移动方向(随机数值超出7物件会暂停移动)
		// _randomMoveDirection = _random.nextInt(8);
		// if (_randomMoveDirection < 8) {
		//
		// }
		//
		// } else {
		// _randomMoveDistance--;
		// }
		if (!_Pathfinding) {
			_Pathfinding = true; // 设置寻路中
		}
		if (_randomMoveDirection > 7) {
			_randomMoveDirection = 0;
		}
		// System.out.println("_randomMoveDirection=:" + _randomMoveDirection);
		if (_pcMove != null) {
			if (getrandomMoveDirection() < 8) {
				int dir = _pcMove.checkObject(_randomMoveDirection);
				dir = _pcMove.openDoor(dir);

				if (dir != -1) {
					_pcMove.setDirectionMove(dir);
				} else {
					_randomMoveDirection = _random.nextInt(8);
					move++;
				}
			}
		}
	}

	// 挂机结束

	// 地圖時間記錄
	private Map<Integer, Integer> _mapsList;

	public final void setMapsList(final HashMap<Integer, Integer> list) {
		_mapsList = list;
	}

	public final int getMapsTime(final int key) {
		if (_mapsList == null || !_mapsList.containsKey(key)) {
			return 0;
		}
		return _mapsList.get(key);
	}

	public void putMapsTime(final int key, final int value) {
		if (_mapsList == null) {
			_mapsList = CharMapTimeReading.get().addTime(getId(), key, value);
		}
		_mapsList.put(key, value);
	}

	public void removeMapsTime(final int key) { // src022
		if (_mapsList.containsKey(key)) {
			_mapsList.remove(key);
		}

	}

	public boolean isTripleArrow() {
		return this.isTripleArrow;
	}

	public void setTripleArrow(boolean isTripleArrow) {
		this.isTripleArrow = isTripleArrow;
	}

	// 戒指欄位擴充紀錄 by terry0412 //src013
	private byte _ringsExpansion;

	public final byte getRingsExpansion() {
		return this._ringsExpansion;
	}

	public final void setRingsExpansion(final byte i) {
		this._ringsExpansion = i;
	}

	private byte _earringsExpansion;

	public final byte getEarringsExpansion() {
		return this._earringsExpansion;
	}

	public final void setEarringsExpansion(final byte i) {
		this._earringsExpansion = i;
	}
	
	// src1003
	private byte _equipmentindexamulet;

	public final byte getEquipmentIndexAmulet() {
		return this._equipmentindexamulet;
	}

	public final void setEquipmentIndexAmulet(final byte i) {
		this._equipmentindexamulet = i;
	}
	//四海新加
	// 四海新加
		/**
		 * 掛機是否瞬移
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		private boolean _gjsykg = true;
		/**
		 * 掛機是否瞬移
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		public boolean getgjsykg() {
			return this._gjsykg;
		}
		/**
		 * 掛機是否瞬移
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		public void setgjsykg(final boolean gjsykg) {
			this._gjsykg = gjsykg;
		}
		/**
		 * 遇到bOSS無條件瞬移開關
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		private boolean _bossgjsykg = true;
		/**
		 * 遇到bOSS無條件瞬移開關
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		public boolean getbossgjsykg() {
			return this._bossgjsykg;
		}
		/**
		 * 遇到bOSS無條件瞬移開關
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		public void setbossgjsykg(final boolean gjsykg) {
			this._bossgjsykg = gjsykg;
		}
		/**
		 * 掛機不搶怪模式
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		private static boolean _jnxh = true;
		/**
		 * 掛機不搶怪模式
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		public boolean getjnxh() {
			return _jnxh;
		}
		/**
		 * 掛機不搶怪模式
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		public void setjnxh(final boolean jnxh) {
			_jnxh = jnxh;
		}
		/**
		 * 掛機不搶怪模式
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		private boolean _limao = false;
		/**
		 * 掛機不搶怪模式
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		public boolean limao() {
			return this._limao;
		}
		/**
		 * 掛機不搶怪模式
		 * 
		 * @return flag true:接收 false:拒絕
		 */
		public void setlimao(final boolean _limao) {
			this._limao = _limao;
		}
		/** 範圍掛機臨時中心點X坐標 **/
		private int _lslocx = 0;

		/** 範圍掛機臨時中心點X坐標 **/
		public int getlslocx() {
			return _lslocx;
		}

		/** 範圍掛機臨時中心點X坐標 **/
		public void setlslocx(int i) {
			_lslocx = i;
		}
		/** 範圍掛機臨時中心點Y坐標 **/
		private int _lslocy = 0;

		/** 範圍掛機臨時中心點Y坐標 **/
		public int getlslocy() {
			return _lslocy;
		}

		/** 範圍掛機臨時中心點Y坐標 **/
		public void setlslocy(int i) {
			_lslocy = i;
		}
		/** 範圍掛機臨時中心點MAPID **/
		private int _lsmapid = 0;

		/** 範圍掛機臨時中心點MAPID **/
		public int getlsmapid() {
			return _lsmapid;
		}

		/** 範圍掛機臨時中心點MAPID **/
		public void setlsmapid(int i) {
			_lsmapid = i;
		}
		/** 掛機範圍 **/
		private int _lsgjfw = 0;

		/** 掛機範圍 **/
		public int getlsgjfw() {
			return _lsgjfw;
		}

		/** 掛機範圍 **/
		public void setlsgjfw(int i) {
			_lsgjfw = i;
		}
		/** 掛機魔法間隔時間 **/
		private  int _gjjgsj = 1;

		/** 掛機魔法間隔時間 **/
		public  int getgjjgsj() {
			return _gjjgsj;
		}

		/** 掛機魔法間隔時間 **/
		public void setgjjgsj(int i) {
			_gjjgsj = i;
		}

		/** 掛機狩獵技能ID暫存 **/
		private int _gjjnzc = 0;

		/** 掛機狩獵技能ID暫存 **/
		public int getgjjnzc() {
			return _gjjnzc;
		}

		/** 掛機狩獵技能ID暫存 **/
		public void setgjjnzc(int i) {
			_gjjnzc = i;
		}
		/** 多少魔力以上才使用掛機魔法 **/
		private int _gjml = 0;

		/** 多少魔力以上才使用掛機魔法 **/
		public int getgjml() {
			return _gjml;
		}
		/** 多少魔力以上才使用掛機魔法 **/
		public void setgjml(int i) {
			_gjml = i;
		}

		/** 掛機引怪技能ID暫存 **/
		private int _ygjnzc = 0;

		/** 掛機引怪技能ID暫存 **/
		public int getygjnzc() {
			return _ygjnzc;
		}

		/** 掛機引怪技能ID暫存 **/
		public void setygjnzc(int i) {
			_ygjnzc = i;
		}

		/** 引怪技能距離設置默認3 **/
		private int _ygjlsz = 3;

		/** 引怪技能距離設置默認3 **/
		public int getygjlsz() {
			return _ygjlsz;
		}

		/** 引怪技能距離設置默認3 **/
		public void setygjlsz(int i) {
			_ygjlsz = i;
			if (i < 2) {
				i = 2;
			}
		}

	private byte _huizhangkz;

	public final byte gethuizhangkz() {
		return this._huizhangkz;
	}

	public final void sethuizhangkz(final byte i) {
		this._huizhangkz = i;
	}
	private byte _jianjiakz;

	public final byte getjianjiakz() {
		return this._jianjiakz;
	}

	public final void setjianjiakz(final byte i) {
		this._jianjiakz = i;
	}
	//四海新加
	// 7.6
	// public final int getWeaponType(final int Type1) {
	// if (Type1 == ActionCodes.ACTION_ChainswordWalk) {
	// if (SprTable.get().containsChainswordSpr(getTempCharGfx())) {
	// return ActionCodes.ACTION_ChainswordWalk;
	//
	// } else {
	// return ActionCodes.ACTION_SpearWalk;
	// }
	// }
	// return Type1;
	// }
	
	private byte _redblueReward;

	public final byte getRedblueReward() {
		return this._redblueReward;
	}

	public final void setRedblueReward(final byte i) {
		this._redblueReward = i;
	}

	public final boolean isGetPolyPower()// src014
	{
		return this._isGetPolyPower;
	}

	public final void setGetPolyPower(boolean flag) {
		this._isGetPolyPower = flag;
	}

	public final L1PolyPower getPolyPower() {
		return this._polyPower;
	}

	public final void resetPolyPower() {
		if (this._polyPower != null) {
			ExtraPolyPowerTable.effectBuff(this, this._polyPower, -1);
		}
		this._polyPower = ExtraPolyPowerTable.getInstance().get(getTempCharGfx());

		if (this._polyPower != null) {
			ExtraPolyPowerTable.effectBuff(this, this._polyPower, 1);
		}
	}

	// 占卜
	private int _probability;

	public int getprobability() {
		return this._probability;
	}

	public void setprobability(int probability) {
		this._probability = probability;
	}

	private boolean _bbdmg = false;

	public void setbbdmg(boolean bbdmg) {
		this._bbdmg = bbdmg;
	}

	public boolean getbbdmg() {
		return this._bbdmg;
	}

	// 占卜 沖暈
	private boolean _bbdmg1 = false;

	public void setbbdmg1(boolean bbdmg1) {
		this._bbdmg1 = bbdmg1;
	}

	public boolean getbbdmg1() {
		return this._bbdmg1;
	}

	private boolean _bbdmg2 = false;

	public void setbbdmg2(boolean bbdmg2) {
		this._bbdmg2 = bbdmg2;
	}

	public boolean getbbdmg2() {
		return this._bbdmg2;
	}

	private boolean _bbdmg3 = false;

	public void setbbdmg3(boolean bbdmg3) {
		this._bbdmg3 = bbdmg3;
	}

	public boolean getbbdmg3() {
		return this._bbdmg3;
	}

	private int counterattack = 0;

	public int getCounterattack() {
		return counterattack;
	}

	public void addCounterattack(final int i) {
		this.counterattack += i;
	}

	private int bowcounterattack = 0;

	public int getBowcounterattack() {
		return bowcounterattack;
	}

	public void addBowcounterattack(final int i) {
		this.bowcounterattack += i;
	}
	
	private int _redbluejoin = 0;

	public int get_redbluejoin() {
		return this._redbluejoin;
	}

	public void set_redbluejoin(int redbluejoin) {
		this._redbluejoin = redbluejoin;
	}

	private int _redblueroom = 0;

	public int get_redblueroom() {
		return this._redblueroom;
	}

	public void set_redblueroom(int redblueroom) {
		this._redblueroom = redblueroom;
	}

	private int _redblueleader = 0;

	public int get_redblueleader() {
		return this._redblueleader;
	}

	public void set_redblueleader(int redblueleader) {
		this._redblueleader = redblueleader;
	}

	private int _redbluepoint = 0;

	public int get_redbluepoint() {
		return this._redbluepoint;
	}

	public void set_redbluepoint(int redbluepoint) {
		this._redbluepoint = redbluepoint;
	}

	/**
	 * 760屬性更新 -> 移至L1PcDetailsMonitor<br>
	 */
	public void sendDetails() {
		// XXX 7.6 ADD
		/*this.sendPackets(new S_PacketBoxCharEr(this));// 角色迴避率更新

		// XXX 7.6 能力基本資訊-力量
		this.sendPackets(new S_StrDetails(2, L1ClassFeature.calcStrDmg(
				this.getStr(), this.getBaseStr()), L1ClassFeature.calcStrHit(
				this.getStr(), this.getBaseStr()), L1ClassFeature
				.calcStrDmgCritical(this.getStr(), this.getBaseStr()),
				L1ClassFeature.calcAbilityMaxWeight(this.getStr(),
						this.getCon())));

		// XXX 7.6 重量程度資訊
		this.sendPackets(new S_WeightStatus(this.getInventory().getWeight100(),
				this.getInventory().getWeight(), (int) this.getMaxWeight()));

		// XXX 7.6 能力基本資訊-智力
		this.sendPackets(new S_IntDetails(2, L1ClassFeature.calcIntMagicDmg(
				this.getInt(), this.getBaseInt()), L1ClassFeature
				.calcIntMagicHit(this.getInt(), this.getBaseInt()),
				L1ClassFeature.calcIntMagicCritical(this.getInt(),
						this.getBaseInt()), L1ClassFeature.calcIntMagicBonus(
						this.getType(), this.getInt()), L1ClassFeature
						.calcIntMagicConsumeReduction(this.getInt())));

		// XXX 7.6 能力基本資訊-精神
		this.sendPackets(new S_WisDetails(2, L1ClassFeature.calcWisMpr(
				this.getWis(), this.getBaseWis()), L1ClassFeature
				.calcWisPotionMpr(this.getWis(), this.getBaseWis()),
				L1ClassFeature.calcStatMr(this.getWis())
						+ L1ClassFeature.newClassFeature(this.getType())
								.getClassOriginalMr(),
				L1ClassFeature.calcBaseWisLevUpMpUp(this.getType(),
						this.getBaseWis())));

		// XXX 7.6 能力基本資訊-敏捷
		this.sendPackets(new S_DexDetails(2, L1ClassFeature.calcDexDmg(
				this.getDex(), this.getBaseDex()), L1ClassFeature.calcDexHit(
				this.getDex(), this.getBaseDex()), L1ClassFeature
				.calcDexDmgCritical(this.getDex(), this.getBaseDex()),
				L1ClassFeature.calcDexAc(this.getDex()), L1ClassFeature
						.calcDexEr(this.getDex())));

		// XXX 7.6 能力基本資訊-體質
		this.sendPackets(new S_ConDetails(2, L1ClassFeature.calcConHpr(
				this.getCon(), this.getBaseCon()), L1ClassFeature
				.calcConPotionHpr(this.getCon(), this.getBaseCon()),
				L1ClassFeature.calcAbilityMaxWeight(this.getStr(),
						this.getCon()), L1ClassFeature
						.calcBaseClassLevUpHpUp(this.getType())
						+ L1ClassFeature.calcBaseConLevUpExtraHpUp(
								this.getType(), this.getBaseCon())));

		// XXX 7.6 重量程度資訊
		this.sendPackets(new S_WeightStatus(this.getInventory().getWeight()
				* 100 / (int) this.getMaxWeight(), this.getInventory()
				.getWeight(), (int) this.getMaxWeight()));

		// XXX 7.6 純能力詳細資訊 階段:25
		this.sendPackets(new S_BaseAbilityDetails(25));

		// XXX 7.6 純能力詳細資訊 階段:35
		this.sendPackets(new S_BaseAbilityDetails(35));

		// XXX 7.6 純能力詳細資訊 階段:45
		this.sendPackets(new S_BaseAbilityDetails(45));

		// XXX 7.6 純能力資訊
		this.sendPackets(new S_BaseAbility(this.getBaseStr(),
				this.getBaseInt(), this.getBaseWis(), this.getBaseDex(), this
						.getBaseCon(), this.getBaseCha()));

		// XXX 7.6 萬能藥使用數量
		this.sendPackets(new S_ElixirCount(this.getElixirStats()));*/
	}

	/**
	 * 使用萬能藥後更新
	 */
	public void sendAbilityDetails() {
		// XXX 7.6 純能力詳細資訊 階段:25
		this.sendPackets(new S_BaseAbilityDetails(25));

		// XXX 7.6 純能力詳細資訊 階段:35
		this.sendPackets(new S_BaseAbilityDetails(35));

		// XXX 7.6 純能力詳細資訊 階段:45
		this.sendPackets(new S_BaseAbilityDetails(45));

		// XXX 7.6 純能力資訊
		this.sendPackets(new S_BaseAbility(this.getBaseStr(), this.getBaseInt(), this.getBaseWis(), this.getBaseDex(),
				this.getBaseCon(), this.getBaseCha()));

		// XXX 7.6 萬能藥使用數量
		this.sendPackets(new S_ElixirCount(this.getElixirStats()));
	}

    // 王者加護
	// private boolean _Pbavatar = false;
	//
	// public boolean getPbavatar() {
	// return _Pbavatar;
	// }
	//
	// public void setPbavatar(boolean Pbavatar) {
	// _Pbavatar = Pbavatar;
	// }
	//
	// private boolean _Pbavataron = false;
	//
	// public boolean getPbavataron() {
	// return _Pbavataron;
	// }
	//
	// public void setPbavataron(boolean Pbavataron) {
	// _Pbavataron = Pbavataron;
	// }
	//
	// public int _Pbacount = 0;
	//
	// public int getPbacount() {
	// return _Pbacount;
	// }
	//
	// public void setPbacount(int i) {
	// _Pbacount = i;
	// }
	// private int _braveAvatarLevel = 0;// 王者加護加成記錄
	//
	// /**
	// * 王者加護加成記錄
	// * @return
	// */
	// public int getBraveAvatarLevel() {
	// return _braveAvatarLevel;
	// }
	//
	// /**
	// * 王者加護加成記錄
	// * @param i
	// */
	// public void setBraveAvatarLevel(int i) {
	// _braveAvatarLevel = i;
	// }
    // 王者加護 end

    // 王族新技能 恩典庇護
    private int graceLv = 0;

    public int getGraceLv() {
        return graceLv;
    }

    public void setGraceLv(int i) {
        graceLv = i - 80;
        if (graceLv < 0) {
            graceLv = 0;
        } else if (graceLv > 5) {
            graceLv = 5;
        }
    }
    // 王族新技能 恩典庇護 end

    // 幻術師新技能 衝突強化
    private int impactUp = 0;

    public int getImpactUp() {
        return impactUp;
    }

    public void setImpactUp(int i) {
        impactUp = i;
    }
    // 幻術師新技能 衝突強化 end

    // 狂戰士新技能 泰坦狂暴
    private int risingUp = 0;

    /**
     * 狂戰士新技能 泰坦狂暴
     * @return
     */
    public int getRisingUp() {
        return risingUp;
    }

    /**
     * 狂戰士新技能 泰坦狂暴
     * @param i
     */
    public void setRisingUp(int i) {
        risingUp = i;
    }
    // 狂戰士新技能 泰坦狂暴 end

	// private int _fearlevel = 0;//恐怖等級
	//
	// /**
	// * 恐怖等級
	// * @param add
	// */
	// public void add_FearLevel(int add) {
	// _fearlevel += add;
	// }
	//
	// /**
	// * 恐怖等級
	// * @return
	// */
	// public int get_FearLevel() {
	// return _fearlevel;
	// }

	private int _titanhp = 0;//泰坦系列技能發動HP區間增加(設1就是+1%)

	/**
	 * 泰坦系列技能發動HP區間增加(設1就是+1%)
	 * @return
	 */
	public int get_TiTanHp() {
		return _titanhp;
	}

	/**
	 * 泰坦系列技能發動HP區間增加(設1就是+1%)
	 * @param titanhp
	 */
	public void add_TiTanHp(int titanhp) {
		_titanhp += titanhp;
	}

    // 8.1連擊系統
    private int comboCount;
    
    public int getComboCount() {
        return this.comboCount;
    }

    public void setComboCount(int comboCount) {
        this.comboCount = comboCount;
    }
    // 8.1連擊系統 end
    
    // 安全區域右下顯示死亡懲罰狀態圖示
    private boolean isSafetyZone;

    public boolean getSafetyZone() {
        return isSafetyZone;
    }

	public void setSafetyZone(boolean value) {
		isSafetyZone = value;
	}
	// 安全區域右下顯示死亡懲罰狀態圖示 end

	// 底比斯大戰遊戲
	public boolean isSiege = false;

	private int SiegeTeam = -1;

	public void setTeam(int i) {
		SiegeTeam = i;
	}

	public int getTeam() {
		return SiegeTeam;
	}
	// 底比斯大戰遊戲 end

	// 怪物圖鑒
	private UserMonsterBook _monsterBook;

	public void setMonsterBook(UserMonsterBook book) {
		_monsterBook = book;
	}

	public UserMonsterBook getMonsterBook() {
		return _monsterBook;
	}

	// 周任務
	private UserWeekQuest _weekQuest;

	public void setWeekQuest(UserWeekQuest quest) {
		_weekQuest = quest;
	}

	public UserWeekQuest getWeekQuest() {
		return _weekQuest;
	}

    // 轉生天賦
    private int[] reincarnationSkill = new int[3];
    
    public int[] getReincarnationSkill() {
        return reincarnationSkill;
    }
    
    private int _turnLifeSkillCount;
    
    public int getTurnLifeSkillCount() {
        return _turnLifeSkillCount;
    }
    
    public void setTurnLifeSkillCount(final int i) {
        _turnLifeSkillCount = i;
    }
    
    private int si = -1;
    
    public final int getSi() {
        return si;
    }
    
    public final void setSi(final int si) {
        this.si = si;
    }
    // 轉生天賦end

    // 增加PVP傷害
    private int _PvpDmg = 0;
    
    /**
     * 增加PVP傷害
     * @return
     */
    public int getPvpDmg() {
        return _PvpDmg;
    }
    
    /**
     * 增加PVP傷害
     * @param i
     */
    public void addPvpDmg(final int i) {
    	_PvpDmg += i;
    }
    
    // 減免PVP傷害
    private int _PvpDmg_R = 0;
    
    /**
     * 減免PVP傷害
     * @return
     */
    public int getPvpDmg_R() {
        return _PvpDmg_R;
    }
    
    /**
     * 減免PVP傷害
     * @param i
     */
    public void addPvpDmg_R(final int i) {
    	_PvpDmg_R += i;
    }

	// // 正義滿角色名稱變黃
	// private boolean _LawfulName = false;
	//
	// /**
	// * 正義滿角色名稱變黃
	// * @return
	// */
	// public boolean isLawfulName() {
	// return _LawfulName;
	// }
	//
	// /**
	// * 正義滿角色名稱變黃
	// * @param LawfulName
	// */
	// public void setLawfulName(final boolean flag) {
	// _LawfulName = flag;
	// }

	private L1ArmorKitPower _armorKitPower;

	public final L1ArmorKitPower getArmorKitPower() {
		return _armorKitPower;
	}

	private ArrayList<Integer> _armorKit;

	public final List<Integer> getArmorList() {
		if (_armorKit == null) {
			_armorKit = new ArrayList<Integer>();
		}
		return _armorKit;
	}

	public final void addArmorKit(final int value) {
		if (value <= 0) {
			return;
		}

		if (_armorKit == null) {
			_armorKit = new ArrayList<Integer>();
		}

		final Set<Integer> uniqueSet2 = new HashSet<Integer>(this.getArmorList());

		for (int kitType : uniqueSet2) {

			final int kitCount = Collections.frequency(this.getArmorList(), kitType);

			_armorKitPower = ArmorKitPowerTable.getInstance().get(kitType, kitCount);

			if (_armorKitPower != null) {
				ArmorKitPowerTable.effectBuff(this, _armorKitPower, -1);
			}
		}

		_armorKit.add(Integer.valueOf(value));

		final Set<Integer> uniqueSet = new HashSet<Integer>(this.getArmorList());

		for (int kitType : uniqueSet) {

			final int kitCount = Collections.frequency(this.getArmorList(), kitType);

			_armorKitPower = ArmorKitPowerTable.getInstance().get(kitType, kitCount);

			if (_armorKitPower != null) {
				ArmorKitPowerTable.effectBuff(this, _armorKitPower, 1);
			}
		}

	}

	public final void removeArmorKit(final int value) {
		if (value <= 0) {
			return;
		}

		if (_armorKit == null) {
			_armorKit = new ArrayList<Integer>();
		}

		final Set<Integer> uniqueSet = new HashSet<Integer>(this.getArmorList());

		for (int kitType : uniqueSet) {

			final int kitCount = Collections.frequency(this.getArmorList(), kitType);

			_armorKitPower = ArmorKitPowerTable.getInstance().get(kitType, kitCount);

			if (_armorKitPower != null) {
				ArmorKitPowerTable.effectBuff(this, _armorKitPower, -1);
			}
		}

		_armorKit.remove(Integer.valueOf(value));

		final Set<Integer> uniqueSet2 = new HashSet<Integer>(this.getArmorList());

		for (int kitType : uniqueSet2) {

			final int kitCount = Collections.frequency(this.getArmorList(), kitType);

			_armorKitPower = ArmorKitPowerTable.getInstance().get(kitType, kitCount);

			if (_armorKitPower != null) {
				ArmorKitPowerTable.effectBuff(this, _armorKitPower, 1);
			}
		}

	}

	// 特效驗證系統
	private int _aistay = 0;

	public int get_aistay() {
		return this._aistay;
	}

	public void set_aistay(int aistay) {
		this._aistay = aistay;
	}

	private int[] _aixyz = null;

	public int[] get_aixyz() {
		return this._aixyz;
	}

	public void set_aixyz(int[] aixyz) {
		this._aixyz = aixyz;
	}

	private int _ai_timer = 0;

	public void setAITimer(int time) {
		this._ai_timer = time;
	}

	public void addAITimer() {
		this.setAITimer(this._ai_timer - 1);
	}

	public int getAITimer() {
		return this._ai_timer;
	}

	private int _ai_error = 0;

	public void setAIERROR() {
		this._ai_error = 0;
	}

	public void addAIERROR() {
		this._ai_error += 1;
	}

	public int getAIERROR() {
		return this._ai_error;
	}
	// 特效驗證系統 end

    // 成長果實系統(Tam幣)
    public int tamcount() {
        Connection con = null;
        Connection con2 = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        PreparedStatement pstm2 = null;
        Timestamp tamtime = null;
        int count = 0;
        long sysTime = System.currentTimeMillis();
        int char_objid = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `characters` WHERE account_name = ?"); // 選來
            pstm.setString(1, getAccountName());
            rs = pstm.executeQuery();
            while (rs.next()) {
                tamtime = rs.getTimestamp("TamEndTime");
                char_objid = rs.getInt("objid");
                if (tamtime != null) {
                    if (sysTime <= tamtime.getTime()) {
                        count++;
                    } else {
                        if (Tam_wait_count(char_objid) != 0) {
                            int day = Nexttam(char_objid);
                            if (day != 0) {
                                Timestamp deleteTime = null;
                                deleteTime = new Timestamp(sysTime + (86400000 * (long) day) + 10000);// 7日
                                // deleteTime = new Timestamp(sysTime +
                                // 1000*60);//7日

                                if (getId() == char_objid) {
                                    setTamTime(deleteTime);
                                }
                                con2 = DatabaseFactory.get().getConnection();
                                pstm2 = con2.prepareStatement(
                                        "UPDATE `characters` SET TamEndTime=? WHERE account_name = ? AND objid = ?"); // 君主上
                                pstm2.setTimestamp(1, deleteTime);
                                pstm2.setString(2, getAccountName());
                                pstm2.setInt(3, char_objid);
                                pstm2.executeUpdate();
                                tamdel(char_objid);
                                count++;
                            }
                        }
                    }
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
           // _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return count;
        } finally {
            SQLUtil.close(pstm2);
            SQLUtil.close(con2);
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void tamdel(int objectId) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("delete from Tam where objid = ? order by id asc limit 1");
            pstm.setInt(1, objectId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            // _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public int Nexttam(int objectId) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int day = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT day FROM `tam` WHERE objid = ? order by id asc limit 1"); // 君主選來
            pstm.setInt(1, objectId);
            rs = pstm.executeQuery();
            while (rs.next()) {
                day = rs.getInt("Day");
            }
        } catch (SQLException e) {
            // _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return day;
    }

    public int Tam_wait_count(int charid) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `tam` WHERE objid = ?");
            pstm.setInt(1, charid);
            rs = pstm.executeQuery();
            while (rs.next()) {
                count = getId();
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
           // _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return count;
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
    
    public long TamTime() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Timestamp tamtime = null;
        long time = 0;
        long sysTime = System.currentTimeMillis();
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement(
                    "SELECT `TamEndTime` FROM `characters` WHERE account_name = ? ORDER BY `TamEndTime` ASC"); // 
            pstm.setString(1, getAccountName());
            rs = pstm.executeQuery();
            while (rs.next()) {
                tamtime = rs.getTimestamp("TamEndTime");
                if (tamtime != null) {
                    if (sysTime < tamtime.getTime()) {
                        time = tamtime.getTime() - sysTime;
                        break;
                    }
                }
            }
            return time;
        } catch (Exception e) {
            e.printStackTrace();
            //_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return time;
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
    
    private Timestamp _tamTime;

    public Timestamp getTamTime() {
        return _tamTime;
    }

    public void setTamTime(Timestamp time) {
        _tamTime = time;
    }

    /*private int _tamreserve;

    public int getTamReserve() {
        return _tamreserve;
    }

    public void setTamReserve(int i) {
        _tamreserve = i;
    }*/
    // 成長果實系統(Tam幣)end

    // 日版記憶座標
    public final ArrayList<L1BookMark> _bookmarks;

    public final ArrayList<L1BookMark> _speedbookmarks;

    public L1BookMark[] getBookMarkArray() {
        return _bookmarks.toArray(new L1BookMark[_bookmarks.size()]);
    }

    public L1BookMark[] getSpeedBookMarkArray() {
        return _speedbookmarks.toArray(new L1BookMark[_speedbookmarks.size()]);
    }

    private int _markcount;

    public void setMark_count(int i) {
        _markcount = i;
    }

    public int getMark_count() {
        return _markcount;
    }

	public L1BookMark getBookMark(final int x, final int y) {
		if (x == 0 || y == 0) {
			return null;
		}
		for (final L1BookMark element : getBookMarkArray()) {
			if (element.getLocX() == x && element.getLocY() == y) {
				return element;
			}
		}
		return null;
	}

    public L1BookMark getBookMark(String name) {
        L1BookMark element = null;
        int size = _bookmarks.size();
        for (int i = 0; i < size; i++) {
            element = _bookmarks.get(i);
            if (element == null)
                continue;
            if (element.getName().equalsIgnoreCase(name)) {
                return element;
            }
        }
        return null;
    }

    public L1BookMark getBookMark(int id) {
        L1BookMark element = null;
        int size = _bookmarks.size();
        for (int i = 0; i < size; i++) {
            element = _bookmarks.get(i);
            if (element == null)
                continue;
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    public int getBookMarkSize() {
        return _bookmarks.size();
    }

    public void addBookMark(L1BookMark book) {
        _bookmarks.add(book);
    }

    public void removeBookMark(L1BookMark book) {
        _bookmarks.remove(book);
    }
    // 日版記憶座標end

	private final L1Inventory _tradewindow; // 交易視窗

	public L1Inventory getTradeWindowInventory() { // 交易視窗
		return this._tradewindow;
	}

	/**
	 * 官方簽到系統
	 */
	public UseAttendTemp attendTemp;

	/**
	 * 官方簽到系統
	 */
	public boolean PCRoom_Buff = false;

	private int _closeCritical = 0; // 近距離爆擊率

	/**
	 * 近距離爆擊率
	 * @return
	 */
	public int getCloseCritical() {
		return _closeCritical;
	}

	/**
	 * 近距離爆擊率
	 * @param i
	 */
	public void addCloseCritical(final int i) {
		_closeCritical += i;
	}

	private int _bowCritical = 0; // 遠距離爆擊率

	/**
	 * 遠距離爆擊率
	 * @return
	 */
	public int getBowCritical() {
		return _bowCritical;
	}

	/**
	 * 遠距離爆擊率
	 * @param i
	 */
	public void addBowCritical(final int i) {
		_bowCritical += i;
	}

	private int _breaklevel = 0; // 破壞等級

	/**
	 * 破壞等級
	 * @param i
	 */
	public void addBreakLevel(final int i) {
		_breaklevel += i;
	}

	/**
	 * 破壞等級
	 * @return
	 */
	public int getBreakLevel() {
		return _breaklevel;
	}

	private int _foeSlayerDmg = 0; // 屠宰者階段別傷害

	/**
	 * 屠宰者階段別傷害
	 * @param i
	 */
	public void addFoeSlayerDmg(final int i) {
		_foeSlayerDmg += i;
	}

	/**
	 * 屠宰者階段別傷害
	 * @return
	 */
	public int getFoeSlayerDmg() {
		return _foeSlayerDmg;
	}

	private int _hit_technology; // 技術命中

	/** 技術命中 **/
	public int getHitTechnology() {
		return _hit_technology;
	}

	/** 技術命中 **/
	public void setHitTechnology(final int i) {
		_hit_technology += i;
		sendPackets(new S_ACTION_UI(this, S_ACTION_UI.RESIST, 0x12, 1));
	}

	private int _hit_elf; // 精靈命中

	/** 精靈命中 **/
	public int getHitElf() {
		return _hit_elf;
	}

	/** 精靈命中 **/
	public void setHitElf(final int i) {
		_hit_elf += i;
		sendPackets(new S_ACTION_UI(this, S_ACTION_UI.RESIST, 0x12, 2));
	}

	private int _hit_dragon; // 龍屬命中

	/** 龍屬命中 **/
	public int getHitDragon() {
		return _hit_dragon;
	}

	/** 龍屬命中 **/
	public void setHitDragon(final int i) {
		_hit_dragon += i;
		sendPackets(new S_ACTION_UI(this, S_ACTION_UI.RESIST, 0x12, 3));
	}

	private int _hit_horror; // 恐怖命中

	/** 恐怖命中 **/
	public int getHitHorror() {
		return _hit_horror;
	}

	/** 恐怖命中 **/
	public void setHitHorror(final int i) {
		_hit_horror += i;
		sendPackets(new S_ACTION_UI(this, S_ACTION_UI.RESIST, 0x12, 4));
	}

	private int _hit_all; // 全部四大命中

	/** 全部四大命中 **/
	public int getHitAll() {
		return _hit_all;
	}

	/** 全部四大命中 **/
	public void setHitAll(final int i) {
		_hit_all += i;
		sendPackets(new S_ACTION_UI(this, S_ACTION_UI.RESIST, 0x12, 5));
	}

	private int _antiDamageReduction = 0; // 無視減免

	/**
	 * 無視減免
	 * @return
	 */
	public int getAntiDamageReduction() {
		return _antiDamageReduction;
	}

	/**
	 * 無視減免
	 * @param i
	 */
	public void addAntiDamageReduction(int i) {
		_antiDamageReduction += i;
	}

	private int _einhasadConsumeReduce = 0; // 殷海薩祝福消耗減少(1=1%)，該值有多少將會減少祝福數值的消耗速度

	/**
	 * 殷海薩祝福消耗減少(1=1%)，該值有多少將會減少祝福數值的消耗速度
	 * @return
	 */
	public int getEinhasadConsumeReduce() {
		int einhasadConsumeReduceLv = 0;
		if (getLevel() >= 80 && getLevel() <= 85) {
			einhasadConsumeReduceLv = 5 + ((getLevel() - 80) * 1);

		} else if (getLevel() >= 86 && getLevel() <= 90) {
			einhasadConsumeReduceLv = 10 + ((getLevel() - 85) * 2);

		} else if (getLevel() >= 91 && getLevel() <= 95) {
			einhasadConsumeReduceLv = 20 + ((getLevel() - 90) * 3);

		} else if (getLevel() > 95) {
			einhasadConsumeReduceLv = 35;
		}
		return _einhasadConsumeReduce + einhasadConsumeReduceLv;
	}

	/**
	 * 殷海薩祝福消耗減少(1=1%)，該值有多少將會減少祝福數值的消耗速度
	 * @param i
	 */
	public void addEinhasadConsumeReduce(final int i) {
		_einhasadConsumeReduce += i;
		// 開啟殷海薩的祝福-休息系統
    	if (LeavesSet.START) {
        	if (get_other().get_leaves_time_exp() > 0) { // 還有剩餘經驗額度
        		sendPackets(new S_PacketBoxExp(get_other().get_leaves_time_exp() / LeavesSet.EXP, this));
        	}
    	}
	}

	private int _partySign; // 組隊標記-8.8C組隊

	/**
	 * 組隊標記-8.8C組隊
	 * @return
	 */
	public int getPartySign() {
		return this._partySign;
	}

	/**
	 * 組隊標記-8.8C組隊
	 * @param i
	 */
	public void setPartySign(final int i) {
		this._partySign = i;
	}

	// 官服任務系統
	private final HashMap<Integer, L1QuestNew> questList = new HashMap<>();

	public HashMap<Integer, L1QuestNew> getQuestList() {
		return questList;
	}

	private boolean check_lv = false;

	public void setcheck_lv(final boolean b) {
		check_lv = b;
	}

	public boolean getcheck_lv() {
		return check_lv;
	}
	/**
	 * ses.scheduleAtFixedRate(执行任务, 延迟(首次执行的时间), 周期, 延迟/周期的单位);
	 */
	public void Ds02() {
		// 需要定时执行的任务
		Runnable runnable = new Runnable() {
			public void run() {
				if(!getjnxh()){
					setjnxh(true);
				//System.out.println("-----定时器-----");
				}
			}
		};
		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
	    //立即执行，并且每5秒执行一次
		ses.scheduleAtFixedRate(runnable, 0,this.getgjjgsj()*1000, TimeUnit.MILLISECONDS);
	}
}
