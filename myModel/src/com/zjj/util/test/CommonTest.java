package com.zjj.util.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


public class CommonTest {
	
	@Test
	public void aTest() {
		String name = "111";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("KEY", "ONE");
		a(name,map);
		System.out.println("a = " + name + "，key=" + map.get("KEY"));
	}
	
	public void a(String name,Map<String, Object> map) {
		name = "a";
		map.put("KEY", "aaa");
		//b(name);
	}
	public void b(String name,Map<String, Object> map) {
		name = "b";
		map.put("KEY", "bbb");
	}
	public void c(String name,Map<String, Object> map) {
		name = "c";
	}
	public void d(String name,Map<String, Object> map) {
		name = "d";
	}
}
