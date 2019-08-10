package org.essentialframework.core.web.databind;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.essentialframework.core.annotation.RequestBody;
import org.essentialframework.core.utility.Assert;
import org.essentialframework.core.web.RequestContext;

public class RequestBodyWebContextParameterBinder
	extends AbstractWebContextParameterBinderChainlink implements WebContextParameterBinder {
	
	@Override
	public Object doBind(RequestContext requestContext, Parameter parameter) {
		Assert.notNull(requestContext, "Cannot bind arguments from a null request context");
		Assert.notNull(parameter, "Cannot bind argument for a null parameter");
		
		final HttpServletRequest request = requestContext.getRequest();
		final RequestBodyDeserializingStrategy deserializingStrategy = 
				determineResponseBodyDeserializingStrategy(request);
		
		final boolean isOptional = Optional.class.equals(parameter.getType());
		final Class<?> actualParameterType = isOptional ? 
			determineActualParameterTypeFromOptional(parameter) : parameter.getType();
		
		if(parameter.isAnnotationPresent(RequestBody.class)) {
			
			Object body = null; 
			body = deserializingStrategy.deserialize(request, actualParameterType);
			
			if(isOptional) {
				return Optional.ofNullable(body);
			} else {
				if(body == null) {
					throw new RuntimeException("Cannot bind a non-existing request body");
				}
				return body;
			}
			
		} else {
			return super.invokeNextBinder(requestContext, parameter);
		}
	
	}
	
	private static RequestBodyDeserializingStrategy 
		determineResponseBodyDeserializingStrategy(ServletRequest request) {
		//TODO determine by content-type
		return new JsonbRequestBodyDeserializingStrategy();
	}
	
	private static Class<?> determineActualParameterTypeFromOptional(Parameter parameter) {
		final ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
		return (Class<?>) parameterizedType.getActualTypeArguments()[0];
	}
	

}
