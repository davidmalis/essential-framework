package org.essentialframework.core.web;

import java.lang.reflect.Method;

import org.essentialframework.core.web.databind.MethodArgumentBinder;

public interface MethodWrapper {

	Method getMethod();
	
	MethodArgumentBinder getArgumentBinder();
	
}
