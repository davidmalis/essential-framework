package org.essentialframework.core.web.databind;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Optional;

import org.essentialframework.core.annotation.RequestParameter;
import org.essentialframework.core.utility.Assert;
import org.essentialframework.core.web.RequestContext;

public class RequestParameterWebContextParameterBinder 
	extends AbstractWebContextParameterBinderChainlink implements WebContextParameterBinder{

	@Override
	public Object doBind(RequestContext requestContext, Parameter parameter) {
		Assert.notNull(requestContext, "Cannot bind arguments from a null request context");
		Assert.notNull(parameter, "Cannot bind argument for a null parameter");
		
		if(parameter.isAnnotationPresent(RequestParameter.class)) {
			
			final Map<String, String[]> parameterMap = requestContext.getRequest().getParameterMap();

			final boolean isOptional = Optional.class.equals(parameter.getType());
			
			final String requestedParameterName = parameter.getAnnotation(RequestParameter.class).value();
			final int parameterIndex = determineParameterValuesIndexFromParameterName(requestedParameterName);
			final String actualParameterName = stripParameterValuesIndex(requestedParameterName);
			
			String parameterValue = null;
			
			try {
				
				if(parameterMap.containsKey(actualParameterName)) {
					parameterValue = parameterMap.get(actualParameterName)[parameterIndex];
				}

			} catch (ArrayIndexOutOfBoundsException e) {
			}
			
			if(isOptional) {
				return Optional.ofNullable(parameterValue);
			} else {
				if(parameterValue == null) {
					throw new RuntimeException("Cannot bind a non-existing parameter");
				}
				return parameterValue;
			}
			
			
		} else {
			return super.invokeNextBinder(requestContext, parameter);
		}

	}
	
	private static int determineParameterValuesIndexFromParameterName(String name) {
		int index = 0;
		
		if(name != null && name.endsWith("]")) {
			try {
				index = Integer.parseInt(
						name.substring(name.lastIndexOf("[")+1, name.length()-1));
			} catch (NumberFormatException e) {
				throw new RuntimeException("Cannot determine parameter values index from request parameter name", e);
			}
		}
		
		return index;
		
	}
	
	private static String stripParameterValuesIndex(String name) {
		if(name != null && name.endsWith("]")) {
			return name.substring(0, name.lastIndexOf("["));
		}
		return name;
	}
	
	

}
