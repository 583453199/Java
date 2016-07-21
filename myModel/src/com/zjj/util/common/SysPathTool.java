package com.zjj.util.common;

import java.util.Map;

/**
 * 读取 SysPathConfig.xml 配置文件
 * 
 */
public class SysPathTool {

	// 用于保存配置文件中的信息
	private static Map<String, String> map;

	public static Map<String, String> getMap() {
		if (map == null || map.size() == 0) {
			SysPathReadTool srt = new SysPathReadTool();
			map = srt.ReadXML("SysPathConfig.xml");
		}
		return map;
	}

	/**
	 * 重新读取配置文件
	 */
	public static void reloadSysPathConfig() {
		SysPathReadTool srt = new SysPathReadTool();
		Map<String, String> newMap = srt.ReadXML("SysPathConfig.xml");
		if (newMap.size() > 0) {
			map = newMap;
		}
	}

	public static String getValue(String key) {
		return getMap().get(key);
	}

	public static void main(String[] args) {
		Map<String, String> m = SysPathTool.getMap();
		for (Map.Entry<String, String> entry : m.entrySet()) {
			System.out.println(entry.getKey() + "       " + entry.getValue());
		}
	}
}