package com.lineage;

import java.util.Map;

import com.lineage.server.model.map.L1Map;

public abstract class MapReader {
	public abstract Map<Integer, L1Map> read();
}
