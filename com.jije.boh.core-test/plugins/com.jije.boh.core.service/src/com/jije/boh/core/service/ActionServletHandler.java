package com.jije.boh.core.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.jije.boh.core.domain.servlet.ServletConfig;
import com.jije.boh.core.service.inter.IActionServletHandler;
import com.jije.boh.core.service.inter.IActionServletService;
import com.jije.boh.core.service.singleton.ModuleMapSingleton;

@Component("actionServletHandler")
public class ActionServletHandler implements IActionServletHandler {

	public synchronized void onBind(IActionServletService actionServlet, Map<?,?> serviceProps){
		String moduleName = getModuleName(actionServlet, serviceProps);

		Map<String, IActionServletService> moduleMap = ModuleMapSingleton.getInstance();
		moduleMap.put(moduleName, actionServlet);
	}
	
	public synchronized void onUnbind(IActionServletService actionServlet, Map<?,?> serviceProps){
		String moduleName = getModuleName(actionServlet, serviceProps);
		
		Map<String, IActionServletService> moduleMap = ModuleMapSingleton.getInstance();
		if(moduleMap.containsKey(moduleName)) {
			moduleMap.remove(moduleName);
		}
	}
	
	public IActionServletService getActionServlet(String path) {
		return ModuleMapSingleton.getInstance().get(path);
	}
	
	private String getModuleName(IActionServletService actionServlet, Map<?,?> serviceProps) {
		String moduleName = null;
		if(actionServlet != null) {
			String bundleSymbolicName = (String) serviceProps.get("Bundle-SymbolicName");
			moduleName = StringUtils.remove(bundleSymbolicName.toString(), ServletConfig.BUNDLE_SYMBOLIC_PRE_NAME);
		}
		return moduleName;
	}

}
