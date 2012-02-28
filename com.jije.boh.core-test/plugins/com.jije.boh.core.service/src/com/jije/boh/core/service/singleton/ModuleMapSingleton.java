package com.jije.boh.core.service.singleton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jije.boh.core.service.inter.IActionServletService;

public class ModuleMapSingleton {
	private static class ModuleMap{
		public static Map<String, IActionServletService> module = new ConcurrentHashMap<String, IActionServletService>();
	}
	
	public static Map<String, IActionServletService> getInstance(){
		return ModuleMap.module;
	}

	private ModuleMapSingleton() {
	}
}
