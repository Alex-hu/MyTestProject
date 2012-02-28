package com.jije.boh.core.domain.servlet;

import java.lang.reflect.Method;
/**
 * 
 * @author Alex
 *
 */
public class InvokeMethodObject {
	private Class<?> clazz;
	private Method method;
	private String path;
	
	public InvokeMethodObject() {
	}

	public InvokeMethodObject(String path, Class<?> clazz, Method method) {
		super();
		this.clazz = clazz;
		this.method = method;
		this.path = path;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
