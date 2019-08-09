package org.essentialframework.core.web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		final HttpServletRequest request = RequestContextHolder.currentRequestContext().getRequest();
		final HttpServletResponse response = RequestContextHolder.currentRequestContext().getResponse();
		final ResponseWriter responseWriter = new ResponseWriter();
		
		Object handlerMethodInvocationResult;
		final Class<?> handlerMethodOwnerType = handlerMethod.getMethodOwnerType();
		final Object handlerMethodOwner = beanFactory.getBean(handlerMethodOwnerType);

		/* NOTE:
		 * At this point, if we are already dealing with the proxy
		 * served from the bean factory, workaround is to pass the 
		 * call on to the next invocation handler, else 
		 * do the usual invocation on the direct instance.
		 */
		if(Proxy.isProxyClass(handlerMethodOwner.getClass())) {
			
			handlerMethodInvocationResult = Proxy.getInvocationHandler(handlerMethodOwner).invoke(handlerMethodOwner, 
				handlerMethod.getMethod(), args);
			
		} else {
			handlerMethodInvocationResult = handlerMethod.getMethod().invoke(handlerMethodOwner, args);
		}

		
		responseWriter.setPayload(handlerMethodInvocationResult);
		responseWriter.write(request, response);
		
		//---
		return handlerMethodInvocationResult;
		
	}

}
