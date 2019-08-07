package org.essentialframework.core.web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
		
		final Class<?> methodOwnerType = handlerMethod.getMethodOwnerType();
		final Object methodOwner = beanFactory.getBean(methodOwnerType);

		/* NOTE:
		 * At this point, if we are already dealing with the proxy
		 * served from the bean factory, workaround is to pass the 
		 * call on to the next invocation handler, else 
		 * do the usual invocation on the direct instance.
		 */
		if(Proxy.isProxyClass(methodOwner.getClass())) {
			
			return Proxy.getInvocationHandler(methodOwner).invoke(methodOwner, 
				handlerMethod.getMethod(), args);
			
		} else {
			return handlerMethod.getMethod().invoke(methodOwner, args);
		}
		
	}

}
