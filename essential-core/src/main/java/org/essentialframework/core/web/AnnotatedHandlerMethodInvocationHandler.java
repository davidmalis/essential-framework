package org.essentialframework.core.web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.essentialframework.core.initialization.BeanFactory;

public class AnnotatedHandlerMethodInvocationHandler 
	implements InvocationHandler {
	
	private BeanFactory beanFactory;
	
	private AnnotatedHandlerMethod handlerMethod;
	
	AnnotatedHandlerMethodInvocationHandler(BeanFactory beanFactory, 
			AnnotatedHandlerMethod handlerMethod){
		
		this.beanFactory = beanFactory;
		
		this.handlerMethod = handlerMethod;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		Class<?> methodOwner = handlerMethod.getMethodOwnerType();
		
		return handlerMethod.getMethod().invoke(beanFactory.getBean(methodOwner), args);
		
	}

}
