package org.essentialframework.core.web;

import java.lang.reflect.Method;

import org.essentialframework.core.web.bind.MethodArgumentBinder;

public interface ReflectiveMethodProvider {

	Method getMethod();
	
	MethodArgumentBinder getArgumentBinder();
	
}
