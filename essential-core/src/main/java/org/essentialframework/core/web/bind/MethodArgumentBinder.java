package org.essentialframework.core.web.bind;

import java.lang.reflect.Method;

public interface MethodArgumentBinder {

	Object[] bind();
	
	Method getMethod();

}
