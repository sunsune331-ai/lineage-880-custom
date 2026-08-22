package com.lineage.server.timecontroller;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.server.timecontroller.pc.HprTimerCrown;
import com.lineage.server.timecontroller.pc.HprTimerDarkElf;
import com.lineage.server.timecontroller.pc.HprTimerDragonKnight;
import com.lineage.server.timecontroller.pc.HprTimerElf;
import com.lineage.server.timecontroller.pc.HprTimerIllusionist;
import com.lineage.server.timecontroller.pc.HprTimerKnight;
import com.lineage.server.timecontroller.pc.HprTimerWarrior;
import com.lineage.server.timecontroller.pc.HprTimerWizard;
import com.lineage.server.timecontroller.pc.MapTimerThread;
import com.lineage.server.timecontroller.pc.MprTimerCrown;
import com.lineage.server.timecontroller.pc.MprTimerDarkElf;
import com.lineage.server.timecontroller.pc.MprTimerDragonKnight;
import com.lineage.server.timecontroller.pc.MprTimerElf;
import com.lineage.server.timecontroller.pc.MprTimerIllusionist;
import com.lineage.server.timecontroller.pc.MprTimerKnight;
import com.lineage.server.timecontroller.pc.MprTimerWarrior;
import com.lineage.server.timecontroller.pc.MprTimerWizard;
import com.lineage.server.timecontroller.pc.PartyTimer;
import com.lineage.server.timecontroller.pc.PcAutoSaveInventoryTimer;
import com.lineage.server.timecontroller.pc.PcAutoSaveTimer;
import com.lineage.server.timecontroller.pc.PcDeleteTimer;
import com.lineage.server.timecontroller.pc.PcEffectTimer;
import com.lineage.server.timecontroller.pc.PcGhostTimer;
import com.lineage.server.timecontroller.pc.PcHellTimer;
import com.lineage.server.timecontroller.pc.PcWeaponTimer;
import com.lineage.server.timecontroller.pc.UnfreezingTimer;
import com.lineage.server.timecontroller.pc.UpdateObjectCTimer;
import com.lineage.server.timecontroller.pc.UpdateObjectDKTimer;
import com.lineage.server.timecontroller.pc.UpdateObjectDTimer;
import com.lineage.server.timecontroller.pc.UpdateObjectETimer;
import com.lineage.server.timecontroller.pc.UpdateObjectITimer;
import com.lineage.server.timecontroller.pc.UpdateObjectKTimer;
import com.lineage.server.timecontroller.pc.UpdateObjectOTimer;
import com.lineage.server.timecontroller.pc.UpdateObjectWTimer;
import com.lineage.server.timecontroller.pc.VIPGfxTimer;

public class StartTimer_Pc {
	public void start() throws InterruptedException {
		if (Config.AUTOSAVE_INTERVAL > 0) {
			PcAutoSaveTimer save = new PcAutoSaveTimer();
			save.start();
		}

		if (Config.AUTOSAVE_INTERVAL_INVENTORY > 0) {
			PcAutoSaveInventoryTimer save = new PcAutoSaveInventoryTimer();
			save.start();
		}

		UpdateObjectCTimer objectCTimer = new UpdateObjectCTimer();
		objectCTimer.start();
		UpdateObjectDKTimer objectDKTimer = new UpdateObjectDKTimer();
		objectDKTimer.start();
		UpdateObjectDTimer objectDTimer = new UpdateObjectDTimer();
		objectDTimer.start();
		UpdateObjectETimer objectETimer = new UpdateObjectETimer();
		objectETimer.start();
		UpdateObjectITimer objectITimer = new UpdateObjectITimer();
		objectITimer.start();
		UpdateObjectKTimer objectKTimer = new UpdateObjectKTimer();
		objectKTimer.start();
		UpdateObjectWTimer objectWTimer = new UpdateObjectWTimer();
		objectWTimer.start();
		UpdateObjectOTimer objectOTimer = new UpdateObjectOTimer();
		objectOTimer.start();
		Thread.sleep(50);// 延遲

		HprTimerCrown hprCrown = new HprTimerCrown();
		hprCrown.start();
		HprTimerDarkElf hprDarkElf = new HprTimerDarkElf();
		hprDarkElf.start();
		HprTimerDragonKnight hprDK = new HprTimerDragonKnight();
		hprDK.start();
		HprTimerElf hprElf = new HprTimerElf();
		hprElf.start();
		HprTimerIllusionist hprIllusionist = new HprTimerIllusionist();
		hprIllusionist.start();
		HprTimerKnight hprKnight = new HprTimerKnight();
		hprKnight.start();
		HprTimerWizard hprWizard = new HprTimerWizard();
		hprWizard.start();
		final HprTimerWarrior hprWarrior = new HprTimerWarrior();
		hprWarrior.start();
		Thread.sleep(50);// 延遲

		MprTimerCrown mprCrown = new MprTimerCrown();
		mprCrown.start();
		MprTimerDarkElf mprDarkElf = new MprTimerDarkElf();
		mprDarkElf.start();
		MprTimerDragonKnight mprDragonKnight = new MprTimerDragonKnight();
		mprDragonKnight.start();
		MprTimerElf mprElf = new MprTimerElf();
		mprElf.start();
		MprTimerIllusionist mprIllusionist = new MprTimerIllusionist();
		mprIllusionist.start();
		MprTimerKnight mprKnight = new MprTimerKnight();
		mprKnight.start();
		MprTimerWizard mprWizard = new MprTimerWizard();
		mprWizard.start();
		MprTimerWarrior mprWarrior = new MprTimerWarrior();
		mprWarrior.start();
		Thread.sleep(50);// 延遲

		PcDeleteTimer deleteTimer = new PcDeleteTimer();
		deleteTimer.start();

		PcGhostTimer ghostTimer = new PcGhostTimer();
		ghostTimer.start();

		UnfreezingTimer unfreezingTimer = new UnfreezingTimer();
		unfreezingTimer.start();

		PartyTimer partyTimer = new PartyTimer();
		partyTimer.start();

		/** 限時地圖時間軸 */
		MapTimerThread MapTimer = new MapTimerThread();
		MapTimer.start();
		Thread.sleep(50);// 延遲

		if (ConfigAlt.ALT_PUNISHMENT) {
			PcHellTimer hellTimer = new PcHellTimer();
			hellTimer.start();
		}

		// VIP定時特效計時時間軸
		final VIPGfxTimer vip = new VIPGfxTimer();
		vip.start();
		Thread.sleep(50);// 延遲

		// 城堡定時特效計時時間軸
		final VIPGfxTimer pc = new VIPGfxTimer();
		pc.start();
		Thread.sleep(50);// 延遲

		// 武器DIY特效
		final PcEffectTimer effectTimer = new PcEffectTimer();
		effectTimer.start();

		// PC 武器加成特效時間軸
		if (ConfigAlt.WEAPON_EFFECT_DELAY > 0) {
			final PcWeaponTimer weaponTimer = new PcWeaponTimer();
			weaponTimer.start(ConfigAlt.WEAPON_EFFECT_DELAY * 1000);
		}
	}
}
