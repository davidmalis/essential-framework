package org.essentialframework.core.web.databind;

import java.lang.reflect.Parameter;

import org.essentialframework.core.web.RequestContext;

public interface WebContextParameterBinder {
	
	Object doBind(RequestContext requestContext, Parameter parameter);
	
//	WebContextParameterBinder addNextBinder(WebContextParameterBinder nextBinder);

}
