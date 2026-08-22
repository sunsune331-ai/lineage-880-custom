package com.lineage.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class L1UbPattern {
	private boolean _isFrozen = false;

	private Map<Integer, ArrayList<L1UbSpawn>> _groups = new HashMap();

	public void addSpawn(int groupNumber, L1UbSpawn spawn) {
		if (_isFrozen) {
			return;
		}

		ArrayList spawnList = (ArrayList) _groups.get(Integer.valueOf(groupNumber));
		if (spawnList == null) {
			spawnList = new ArrayList();
			_groups.put(Integer.valueOf(groupNumber), spawnList);
		}

		spawnList.add(spawn);
	}

	public void freeze() {
		if (_isFrozen) {
			return;
		}

		for (ArrayList spawnList : _groups.values()) {
			Collections.sort(spawnList);
		}

		_isFrozen = true;
	}

	public boolean isFrozen() {
		return _isFrozen;
	}

	public ArrayList<L1UbSpawn> getSpawnList(int groupNumber) {
		if (!_isFrozen) {
			return null;
		}

		return (ArrayList) _groups.get(Integer.valueOf(groupNumber));
	}
}

/*
 * Location: C:\Users\kenny\Downloads\奧茲之戰\Server_Game.jar Qualified Name:
 * com.lineage.server.model.L1UbPattern JD-Core Version: 0.6.2
 */