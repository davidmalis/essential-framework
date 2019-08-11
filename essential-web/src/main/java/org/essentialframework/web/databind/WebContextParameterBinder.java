package org.essentialframework.web.databind;

import java.lang.reflect.Parameter;

import org.essentialframework.web.RequestContext;

public interface WebContextParameterBinder {
	
	Object doBind(RequestContext requestContext, Parameter parameter);
	
//	WebContextParameterBinder addNextBinder(WebContextParameterBinder nextBinder);

}
