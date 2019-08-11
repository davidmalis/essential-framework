package org.essentialframework.web.databind;

import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.essentialframework.core.utility.Assert;
import org.essentialframework.web.RequestContext;

public class GeneralWebContextParameterBinder 
	extends AbstractWebContextParameterBinderChainlink implements WebContextParameterBinder {
	
	private boolean createSessions = true;
	
	@Override
	public Object doBind(RequestContext requestContext, Parameter parameter) {
		Assert.notNull(requestContext, "Cannot bind arguments from a null request context");
		Assert.notNull(parameter, "Cannot bind argument for a null parameter");
		
		final Class<?> parameterType = parameter.getType();
		
		if(HttpServletRequest.class.equals(parameterType)) {
			return requestContext.getRequest();
		} else if(HttpServletResponse.class.equals(parameterType)) {
			return requestContext.getResponse();
		} else if(HttpSession.class.equals(parameterType)) {
			return requestContext.getSession(this.createSessions);
		} else {
			return super.invokeNextBinder(requestContext, parameter);
		}
		
	}
	
	public boolean isCreateSessions() {
		return this.createSessions;
	}

	public void setCreateSessions(boolean createSessions) {
		this.createSessions = createSessions;
	}

}
