package com.zjj.comet4j;

import java.util.HashMap;
import java.util.Map;

public class AppStore {
	private static Map<String, String> map;
	private static AppStore instance;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static AppStore getInstance() {
		if (instance == null) {
			instance = new AppStore();
			map = new HashMap();
		}
		return instance;
	}

	public void put(String key, String value) {
		map.put(key, value);
	}

	public String get(String key) {
		return (String) map.get(key);
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void destroy() {
		map.clear();
		map = null;
	}
}