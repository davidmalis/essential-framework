package org.essentialframework.web;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.essentialframework.core.initialization.BeanDefinition;
import org.essentialframework.core.initialization.BeanScope;
import org.essentialframework.core.initialization.GenericScopedBeanFactory;
import org.essentialframework.core.utility.Assert;

public class RequestContextInvocationHandler<T>
	implements InvocationHandler {
	
	private static final String ATTRIBUTE_PREFIX = 
		RequestContextInvocationHandler.class.getName() + ".beans.";
	
	private GenericScopedBeanFactory scopedBeanFactory;
	
	private BeanDefinition<T> targetDefinition;
	
	private final Object lock = new Object();
	
	public RequestContextInvocationHandler(GenericScopedBeanFactory scopedBeanFactory, BeanDefinition<T> beanDefinition) {
		Assert.notNull(scopedBeanFactory, "BeanFactory must not be null");
		Assert.notNull(beanDefinition, "Target bean definition must not be null");
		Assert.state(
			BeanScope.REQUEST == beanDefinition.getScope() ||
			BeanScope.SESSION == beanDefinition.getScope(), 
			"BeanDefinition must define a request or session scoped bean");
		
		this.scopedBeanFactory = scopedBeanFactory;
		this.targetDefinition = beanDefinition;
	}

	public Object findTargetInstance() {
		
		final RequestContext context = RequestContextHolder.currentRequestContext();
		final String scopedBeanName = getBeanAttributeName(this.targetDefinition);
		
		Object instance = null;
		
		if(BeanScope.REQUEST == targetDefinition.getScope()) {
			final HttpServletRequest request = context.getRequest();
			
			instance = request.getAttribute(scopedBeanName);
			if(instance == null) {
				instance = scopedBeanFactory.newInstance(this.targetDefinition);
				request.setAttribute(scopedBeanName, instance);
			}
			
			
		} else {
			final HttpSession session = context.getSession(true);
			
			if(session == null) {
				throw new IllegalStateException("Cannot handle session scoped bean with absent session");
			}

			if(session.getAttribute(scopedBeanName) == null) {
				synchronized(this.lock) {
					if(session.getAttribute(scopedBeanName) == null) {
						instance = scopedBeanFactory.newInstance(this.targetDefinition);
						session.setAttribute(scopedBeanName, instance);
					}
				}
				
			} else {
				instance = session.getAttribute(scopedBeanName);
			} 
			
		} 
		
		return instance;
		
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) 
			throws Throwable {
		
		final Object instance = findTargetInstance();
		return method.invoke(instance, args);
	}
	
	private static final String getBeanAttributeName(BeanDefinition<?> definition) {
		StringBuilder sb = new StringBuilder(ATTRIBUTE_PREFIX);
		sb.append(definition.getBeanName());
		return sb.toString();
	}

}
