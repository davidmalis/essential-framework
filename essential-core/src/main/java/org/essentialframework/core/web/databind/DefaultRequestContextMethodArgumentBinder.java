package org.essentialframework.core.web.databind;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.essentialframework.core.utility.Assert;
import org.essentialframework.core.web.MethodWrapper;
import org.essentialframework.core.web.RequestContext;
import org.essentialframework.core.web.RequestContextHolder;

public class DefaultRequestContextMethodArgumentBinder 
	implements MethodArgumentBinder {
	
	private Method method;
	
	private List<Parameter> parameters;
	
	private WebContextParameterBinder bindingChain = 
			WebContextParameterBinderChainFactory.createDefaultChain();
	
	public DefaultRequestContextMethodArgumentBinder(MethodWrapper methodWrapper) {
		Assert.notNull(methodWrapper, "Cannot obtain method reflection from a null source");
		Assert.notNull(methodWrapper.getMethod(), "Cannot bind argument of a null method");
		this.method = methodWrapper.getMethod();
		this.parameters = Arrays.asList(method.getParameters());
	}
	
	public Object[] bind() {

		final RequestContext requestContext = RequestContextHolder.currentRequestContext();
		List<Object> boundArguments = new ArrayList<>();
		
		this.parameters.forEach(parameter -> {
			boundArguments.add(bindingChain.doBind(requestContext, parameter));
		});
		
		return boundArguments.toArray();
	}
	
//	private static boolean isRequestBodyParameter(Parameter parameter) {
//		return parameter.isAnnotationPresent(RequestBody.class);
//	}
//	
//	private static boolean isRequestParameter(Parameter parameter) {
//		return parameter.isAnnotationPresent(RequestParameter.class);
//	}
//	
//	private static boolean isBeanParameter(Parameter parameter) {
//		return parameter.isAnnotationPresent(Bean.class);
//	}
//	
//	private Object getBeanForParameter(Parameter parameter) {
//		
//		final BeanFactory beanFactory = WebUtils.findBeanFactory();
//		
//		final Alias alias = parameter.getAnnotation(Alias.class);
//		if(alias != null) {
//			return beanFactory.getBean(alias.value());
//		}
//		return beanFactory.getBean(parameter.getType());
//	}
//	
//	private Object getObjectForBodyParameter(Parameter parameter) {
//		
//		final RequestContext context = RequestContextHolder.currentRequestContext();
//		boolean isOptional = Optional.class.equals(parameter.getType());
//		
//		if(isOptional) {
//			final ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
//			final Class<?> type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
//
//			return Optional.ofNullable(
//				this.requestBodyDeserializingStrategy.deserialize(context.getRequest(), type));
//		} else {
//			return this.requestBodyDeserializingStrategy.deserialize(context.getRequest(), 
//					parameter.getType());
//		}
//		
//	}
//
//	@Override
//	public Method getMethod() {
//		return this.method;
//	}
	
}
