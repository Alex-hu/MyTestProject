/**
 * 
 */
package com.jije.boh.core.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jije.boh.core.domain.servlet.ServletConfig;
import com.jije.boh.core.service.inter.IActionServletHandler;
import com.jije.boh.core.service.inter.IActionServletService;
import com.jije.boh.core.service.inter.IOperateService;

/**
 * @author Murphy
 * @author Alex
 */
@Controller
public class MainController {
	
	@RequestMapping("/hello")
	public ModelAndView hello() {
		BundleContext context = FrameworkUtil.getBundle(this.getClass())
				.getBundleContext();
		ServiceReference<?> serviceReference = context
				.getServiceReference(IOperateService.class.getName());
		IOperateService operateService = (IOperateService) context
				.getService(serviceReference);
		String str = operateService.getDate();
		return new ModelAndView("hello", "date", str);
	}
	
	@RequestMapping("/service")
	public @ResponseBody String service(HttpServletRequest request, HttpServletResponse response) {
		String moduleName = request.getParameter(ServletConfig.REQUEST_PARAMETER_MODULE_NAME);
		
		return getRequestResult(moduleName, request, response);
	}
	
	@RequestMapping("/call/{moduleName}/{controller}/{funcName}")
	public @ResponseBody String callService(@PathVariable String moduleName, @PathVariable String controller, @PathVariable String funcName,HttpServletRequest request, HttpServletResponse response) {
		
		return getRequestResult(moduleName, request, response);
	}
	
	private String getRequestResult(String moduleName, 
			HttpServletRequest request, HttpServletResponse response) {
		IActionServletHandler actionServletHandler = 
				(IActionServletHandler)getService(IActionServletHandler.class);
		
		String result = "";
		if(actionServletHandler != null){
			IActionServletService service = 
					actionServletHandler.getActionServlet(moduleName);
			if(service != null){
				result = service.getResponseJson(request, response);
				System.out.println(result);
			}
		}
		
		response.setHeader("contentType", "utf-8");
		return result;
	}
	
	private Object getService(Class<?> clazz){
		BundleContext context = FrameworkUtil.getBundle(this.getClass())
				.getBundleContext();
		ServiceReference<?> serviceReference = context
				.getServiceReference(clazz.getName());
		 
		return context.getService(serviceReference);
	}
}
