package com.lineage.server.model.Instance;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_Trap;
import com.lineage.server.templates.L1Trap;
import com.lineage.server.types.Point;

public class L1TrapInstance extends L1Object {
	private static final long serialVersionUID = 1L;
	private static final Log _log = LogFactory.getLog(L1TowerInstance.class);
	private final L1Trap _trap;
	private final Point _baseLoc = new Point();
	private final Point _rndPt = new Point();
	private int _span;
	private int _stop = 0;
	private boolean _isEnable = true;

	private List<L1PcInstance> _knownPlayers = new CopyOnWriteArrayList();

	private static final Random _random = new Random();

	public L1TrapInstance(int id, L1Trap trap, L1Location loc, Point rndPt, int span) {
		setId(id);
		_trap = trap;
		getLocation().set(loc);
		_baseLoc.set(loc);
		_rndPt.set(rndPt);

		if (span > 0) {
			_span = (span / 1000);
		}

		resetLocation();
	}

	public L1Trap get_trap() {
		return _trap;
	}

	public void set_stop(int _stop) {
		this._stop = _stop;
	}

	public int get_stop() {
		return _stop;
	}

	public void resetLocation() {
		try {
			if ((_rndPt.getX() == 0) && (_rndPt.getY() == 0)) {
				return;
			}
			enableTrap();

			for (int i = 0; i < 50; i++) {
				int rndX = _random.nextInt(_rndPt.getX() + 1) * (_random.nextBoolean() ? 1 : -1);
				int rndY = _random.nextInt(_rndPt.getY() + 1) * (_random.nextBoolean() ? 1 : -1);

				rndX += _baseLoc.getX();
				rndY += _baseLoc.getY();

				L1Map map = getLocation().getMap();

				if ((map.isInMap(rndX, rndY)) && (map.isPassable(rndX, rndY, null))) {
					getLocation().set(rndX, rndY);
					break;
				}
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

	public int getSpan() {
		return _span;
	}

	public void enableTrap() {
		set_stop(0);
		_isEnable = true;
	}

	public void disableTrap() {
		_isEnable = false;

		for (L1PcInstance pc : _knownPlayers) {
			pc.removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this));
		}
		_knownPlayers.clear();
	}

	public boolean isEnable() {
		return _isEnable;
	}

	public void onTrod(L1PcInstance trodFrom) {
		_trap.onTrod(trodFrom, this);
	}

	public void onDetection(L1PcInstance caster) {
		_trap.onDetection(caster, this);
	}

	public void onPerceive(L1PcInstance perceivedFrom) {
		try {
			if (perceivedFrom.hasSkillEffect(2002)) {
				perceivedFrom.addKnownObject(this);
				perceivedFrom.sendPackets(new S_Trap(this, _trap.getType()));
				_knownPlayers.add(perceivedFrom);
			}
		} catch (Exception e) {
			_log.error(e.getLocalizedMessage(), e);
		}
	}

}
