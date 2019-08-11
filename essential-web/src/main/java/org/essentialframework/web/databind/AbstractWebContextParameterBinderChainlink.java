package org.essentialframework.web.databind;

import java.lang.reflect.Parameter;

import org.essentialframework.web.RequestContext;

public abstract class AbstractWebContextParameterBinderChainlink implements WebContextParameterBinder  {

	private WebContextParameterBinder nextBinder;
	
	protected AbstractWebContextParameterBinderChainlink() {
	}
	
	protected final Object invokeNextBinder(RequestContext requestContext, Parameter parameter) {
		if(this.nextBinder != null) {
			return this.nextBinder.doBind(requestContext, parameter);
		}
		
		return null;
	}

	public WebContextParameterBinder getNextBinder() {
		return nextBinder;
	}
	
	public <T extends AbstractWebContextParameterBinderChainlink>
		AbstractWebContextParameterBinderChainlink addNextBinder(T nextBinder) {
		this.nextBinder = nextBinder;
		return nextBinder;
	}
	
	@Override
	public abstract Object doBind(RequestContext requestContext, Parameter parameter);
	

}
