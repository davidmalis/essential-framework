package org.essentialframework.core.web.bind;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.essentialframework.core.annotation.Alias;
import org.essentialframework.core.annotation.Bean;
import org.essentialframework.core.annotation.RequestBody;
import org.essentialframework.core.annotation.RequestParameter;
import org.essentialframework.core.initialization.BeanFactory;
import org.essentialframework.core.utility.Assert;
import org.essentialframework.core.utility.WebUtils;
import org.essentialframework.core.web.ReflectiveMethodProvider;
import org.essentialframework.core.web.RequestContext;
import org.essentialframework.core.web.RequestContextHolder;

public class DefaultRequestContextBasedMethodArgumentBinder 
	implements MethodArgumentBinder {
	
	private RequestBodyDeserializingStrategy requestBodyDeserializingStrategy = 
		new JsonbRequestBodyDeserializingStrategy();
	
	private Method method;
	
	private List<Parameter> parameters;
	
	public DefaultRequestContextBasedMethodArgumentBinder(ReflectiveMethodProvider methodAccessor) {
		Assert.notNull(methodAccessor, "Cannot obtain method reflection from a null source");
		Assert.notNull(methodAccessor.getMethod(), "Cannot bind argument of a null method");
		this.method = methodAccessor.getMethod();
		this.parameters = Arrays.asList(method.getParameters());
	}
	
	public Object[] bind() {
		
		List<Object> boundArguments = new ArrayList<>();

		final RequestContext requestContext = RequestContextHolder.currentRequestContext();
		
		this.parameters.forEach( parameter ->{
			
			Class<?> type = parameter.getType();
			
			if(HttpServletRequest.class.equals(type)) {
				boundArguments.add(requestContext.getRequest());
			
			} else if(HttpServletResponse.class.equals(type)) {
				boundArguments.add(requestContext.getResponse());
			
			} else if(BeanFactory.class.equals(type)) {
				boundArguments.add(WebUtils.findBeanFactory());
			
			} else if(isBeanParameter(parameter)) {
				boundArguments.add(getBeanForParameter(parameter));

			} else if(isRequestParameter(parameter)) {
				//TODO

			} else if(isRequestBodyParameter(parameter)) {
				boundArguments.add(getObjectForBodyParameter(parameter));
				
			} else {
				boundArguments.add(null);
			}
			
		});
		
		
		return boundArguments.toArray();
	}
	
	private static boolean isRequestBodyParameter(Parameter parameter) {
		return parameter.isAnnotationPresent(RequestBody.class);
	}
	
	private static boolean isRequestParameter(Parameter parameter) {
		return parameter.isAnnotationPresent(RequestParameter.class);
	}
	
	private static boolean isBeanParameter(Parameter parameter) {
		return parameter.isAnnotationPresent(Bean.class);
	}
	
	private Object getBeanForParameter(Parameter parameter) {
		
		final BeanFactory beanFactory = WebUtils.findBeanFactory();
		
		final Alias alias = parameter.getAnnotation(Alias.class);
		if(alias != null) {
			return beanFactory.getBean(alias.value());
		}
		return beanFactory.getBean(parameter.getType());
	}
	
	private Object getObjectForBodyParameter(Parameter parameter) {
		
		final RequestContext context = RequestContextHolder.currentRequestContext();
		boolean isOptional = Optional.class.equals(parameter.getType());
		
		if(isOptional) {
			final ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
			final Class<?> type = (Class<?>) parameterizedType.getActualTypeArguments()[0];

			return Optional.ofNullable(
				this.requestBodyDeserializingStrategy.deserialize(context.getRequest(), type));
		} else {
			return this.requestBodyDeserializingStrategy.deserialize(context.getRequest(), 
					parameter.getType());
		}
		
	}

	@Override
	public Method getMethod() {
		return this.method;
	}
	
}
