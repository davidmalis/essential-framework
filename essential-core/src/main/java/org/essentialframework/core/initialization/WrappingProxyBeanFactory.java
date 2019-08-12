/* MIT License
*
* Copyright (c) 2018 David Mališ
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package org.essentialframework.core.initialization;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.essentialframework.core.annotation.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * @author David Malis
 * @since 1.0
 */
public abstract class WrappingProxyBeanFactory 
	extends GenericScopedBeanFactory implements BeanFactory {
	
	private static final Logger LOGGER =
		LoggerFactory.getLogger(WrappingProxyBeanFactory.class);
	
	private List<Class<? extends AbstractTargetAwareInvocationHandler>> handlerTypes = 
		new LinkedList<>();
	
	/**
	 * TODO
	 */
	{
		//default invocation handlers (ordered from most-inner to most-outer)
		this.addHandlerType(TransactionalInvocationHandler.class)
			.addHandlerType(MethodInvocationLoggingHandler.class);
	}
	
	/**
	 * TODO
	 * @param type
	 * @return
	 */
	public WrappingProxyBeanFactory addHandlerType(Class<? extends AbstractTargetAwareInvocationHandler> type) {
		if(type != null) {
			
			if( LOGGER.isDebugEnabled() ) {
				LOGGER.debug("Adding {} bean proxy invocation handler", type.getSimpleName());
			}
			handlerTypes.add(type);
		}
		return this;
	}
	
	/**
	 * TODO
	 * @param type
	 * @param target
	 * @return
	 */
	private static InvocationHandler createHandler(Class<? extends AbstractTargetAwareInvocationHandler> type, Object target) {
		try {
			return (InvocationHandler) type.getConstructor(Object.class).newInstance(target);
		} catch ( InstantiationException    | 
				  IllegalAccessException    | 
				  IllegalArgumentException  | 
				  InvocationTargetException | 
				  NoSuchMethodException     | 
				  SecurityException e) {
		
			throw new BeanFactoryException("Cannot create instance of Invocation Handler for type" 
					+ type.getSimpleName());
		}
	}
	
	@Override
	protected List<Object> getParameterInstancesForBeanConstruction(Parameter[] parameters){
		
		List<Object> instances = new ArrayList<>();
		for(Parameter parameter : parameters) {
			
			BeanDefinition<?> definition;
			
			final Alias alias = parameter.getAnnotation(Alias.class);
			if(alias != null) {
				definition = getBeanDefinitionFor(alias.value());
			} else {
				definition = getBeanDefinitionFor(parameter.getType());
			}
			
			/* if parameter type is not an interface, skip proxy
			 * wrapping and serve direct instance
			 */
			if( !parameter.getType().isInterface() ) {
				LOGGER.debug("Skipping proxy wrapping for the type {}. Serving direct instance.", 
					parameter.getType().getSimpleName());
				
				instances.add(super.getInstanceFor(definition));
				continue;
			} 
			instances.add(getInstanceFor(definition));
		}
		return instances;
	}
	
	/**
	 * TODO
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <T> T getInstanceFor(final BeanDefinition<T> definition) {
		
		T target = super.getInstanceFor(definition);
		if(target == null) {
			LOGGER.debug("Target is null. Serving null instance.");
			return null;
		}
		if(definition.getInterfaces().length == 0) {
			
			if( LOGGER.isDebugEnabled() ) {
				LOGGER.debug("Bean {} does not implement any applicable interface. Serving direct instance.",
						definition.getBeanName());
			}
			
			return target;
		}
		Object wrapper = null;
		for(Class<? extends AbstractTargetAwareInvocationHandler> handlerType : handlerTypes) {

			if( LOGGER.isTraceEnabled() ) {
				LOGGER.trace("Wrapping bean '{}' with the '{}' proxy", 
						definition.getBeanName(), handlerType.getSimpleName());
			}
			
			wrapper = Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),
				definition.getInterfaces(), 
				createHandler(handlerType, target));
		}
		return (T) wrapper;
	}
	
}
