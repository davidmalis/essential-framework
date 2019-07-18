package org.essentialframework.core.initialization;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class RequestContextInvocationHandler<T>
	implements InvocationHandler {
	
	private BeanDefinition<T> targetDefinition;
	
	public RequestContextInvocationHandler(BeanDefinition<T> beanDefinition) {
		this.targetDefinition = beanDefinition;
	}

	public Object findTargetInstance() {
		///RequestContext.blabla TODO
		return null;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) 
			throws Throwable {
		
		final Object instance = findTargetInstance();
		return method.invoke(instance, args);
	}
}
