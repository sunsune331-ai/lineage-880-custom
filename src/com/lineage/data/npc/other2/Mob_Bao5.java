package com.lineage.data.npc.other2;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.config.ConfigQuest;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.CheckUtil;

public class Mob_Bao5 extends NpcExecutor {

	private static final Log _log = LogFactory.getLog(Mob_Bao5.class);

	private Mob_Bao5() {

	}

	public static NpcExecutor get() {
		return new Mob_Bao5();
	}

	@Override
	public int type() {
		return 8;
	}

	private static Random _random = new Random();

	@Override
	public L1PcInstance death(final L1Character lastAttacker,
			final L1NpcInstance npc) {
		try {

			final L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);

			if (pc != null) {

				if (pc.getQuest().isStart(L1PcQuest.BAO_QUEST_5)
						&& !pc.getInventory().checkItem(_checkitem,
								ConfigQuest.Bao5_Stone_MaxCount)) {
					if (_random.nextInt(100) < _chance) {

						CreateNewItem.getQuestItem(pc, npc, _checkitem, 1);
					}
				}
			}
			return pc;

		} catch (final Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
		return null;
	}
	
	private int _checkitem;
	
	private int _chance;
	
	@Override
	public void set_set(String[] set) {
		try {
			_checkitem = Integer.parseInt(set[1]);
			_chance = Integer.parseInt(set[2]);
		} catch (Exception e) {
		}
	}
}
