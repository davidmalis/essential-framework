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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.essentialframework.core.annotation.Alias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GenericScopedBeanFactory 
	implements BeanFactory {
	
	private static final Logger LOGGER =
		LoggerFactory.getLogger(GenericScopedBeanFactory.class);
	
	
	private Map<String, BeanDefinition<?>> beanDefinitions =
		new ConcurrentHashMap<>();
	
	private Map<Class<?>, List<String>> beanNames =
		new ConcurrentHashMap<>();
		
	private Map<String, Object> singletons =
		new ConcurrentHashMap<>();

	
	protected GenericScopedBeanFactory() {
		registerSingletonInstance(this);
	}
	
	public void registerSingletonInstance(final Object instance) {
		if(instance != null ) {
			BeanDefinition<?> definition = registerDefinitionFor(instance.getClass());
			singletons.put(definition.getBeanName(), instance);
		}
	}
	
	private void registerNameFor(final Class<?> type, final String name) {
		beanNames.putIfAbsent(type, new ArrayList<>());
		beanNames.get(type).add(name);
	}
	
	protected <T> BeanDefinition<T> registerDefinitionFor(final Class<T> type) {
		
		final BeanDefinition<T> definition = new ScopedBeanDefinition<>(type);
		if( beanDefinitions.containsKey(definition.getBeanName()) ) {
			throw new NotUniqueBeanException(
				"Bean under the name '"+definition.getBeanName()+"' already exist. " +
				"Consider using @Alias to differentiate bean names or " +
				"if you are already using @Alias, check specified names.");
		}
		
		if( LOGGER.isDebugEnabled() ){
			LOGGER.debug("Registering definition for '{}' of type {} ", 
					definition.getBeanName(), definition.getType());
		}
		
		beanDefinitions.put(definition.getBeanName(), definition);
		registerNameFor(type, definition.getBeanName());
		
		for(Class<?> i : definition.getInterfaces()) {
			registerNameFor(i, definition.getBeanName());
		}
		
		return definition;
	}
	
	@SuppressWarnings("unchecked")
	protected <T> BeanDefinition<T> getBeanDefinitionFor(final Class<T> type) {
		
		final List<String> names = beanNames.get(type);
		if( names == null || names.size() <= 0) {
			throw new NoSuchBeanException(
				"No bean definition found for type "+ type);
		}
		if( names.size() > 1 ) {
			throw new NotUniqueBeanException(
				"Cannot determine bean definition for ambiguous type " + type + ". Consider using @Alias.");
		}
		return (BeanDefinition<T>) getBeanDefinitionFor(names.get(0));
	}
	
	protected BeanDefinition<?> getBeanDefinitionFor(final String name){
		
		final BeanDefinition<?> definition = beanDefinitions.get(name);
		if( definition == null ) {
			throw new NoSuchBeanException(
				"No bean definition found for name " + name);
		}
		return definition;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getSingletonInstance(final BeanDefinition<T> definition) {
		
		final String name = definition.getBeanName();
		if( !singletons.containsKey(name) ) {
			
			if( LOGGER.isDebugEnabled() ) {
				LOGGER.debug("Instantiating and registering singleton of type {} under name '{}'.", 
						definition.getType().getName(), name);
			}
			singletons.put(name, newInstance(definition));
		}
		
		return (T) singletons.get(name);
	}
	
	public <T> T newInstance(final BeanDefinition<T> definition) {
		
		final Constructor<T> c = definition.getConstructorCandidate();
		final List<Object> args = getParameterInstancesForBeanConstruction(c.getParameters());
		
		try {
			
			T instance = c.newInstance(args.toArray());
			
			handleBeanFactoryAware(instance);
			handleWiredFields(instance);
			
			return instance;
		
		} catch ( InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException
				| InvocationTargetException e) {
			 
			throw new BeanFactoryException(
				"Error while instantiating bean of type " 
					+ definition.getType().getName(), e);
		}
	}
	
	protected <T> void handleWiredFields(T instance) {
		for(Field field : BeanDefinitionUtils.findWiredFields(instance.getClass())) {
			
			BeanDefinition<?> definition;
			try {
				
				final Alias alias = field.getAnnotation(Alias.class);
				if(alias != null) {
					definition = getBeanDefinitionFor(alias.value());
				} else {
					definition = getBeanDefinitionFor(field.getType());
				}
				
				boolean wasAccessible = field.isAccessible();
				field.setAccessible(true);
				field.set(instance, getInstanceFor(definition));
				field.setAccessible(wasAccessible);
				
			} catch (Throwable t) {
				throw new BeanFactoryException("Failed to inject @Wired fields for bean of type", t);
			}
		}
	}
	
	protected List<Object> getParameterInstancesForBeanConstruction(Parameter[] parameters){
		
		final List<Object> instances = new ArrayList<>();
		for(Parameter parameter : parameters) {

			BeanDefinition<?> definition;
			
			final Alias alias = parameter.getAnnotation(Alias.class);
			if(alias != null) {
				definition = getBeanDefinitionFor(alias.value());
			} else {
				definition = getBeanDefinitionFor(parameter.getType());
			}
				
			instances.add(getInstanceFor(definition));
		}
		return instances;
		
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getInstanceFor(final BeanDefinition<T> definition) {
		
		if(definition == null) {
			throw new BeanFactoryException("Bean definition must not be null");
		}
		
		switch(definition.getScope()) {
			
			case PROTOTYPE: 
				return newInstance(definition);
				
			case SESSION:
			case REQUEST:
				
				if(definition.getInterfaces().length == 0) {
					LOGGER.error("Request/Session scoped bean {} does not implement "+
								 "any applicable interface. Cannot create proxy.");
					throw new BeanFactoryException("Request/Session scoped bean does "+
								 "not implement any applicable interface");
				}
				
				return (T)Proxy.newProxyInstance(
					Thread.currentThread().getContextClassLoader(), 
					definition.getInterfaces(), 
					getRequestContextInvocationHandler(definition));
			
			default:	
			case SINGLETON:
				return getSingletonInstance(definition);
		}
	}
	
	protected void instantiateSingletons() {
		for(BeanDefinition<?> bd : beanDefinitions.values()) {
			if(bd.isSingleton()) {
				getInstanceFor(bd);
			}
		}
	}

	protected Object handleBeanFactoryAware(Object object) {
		if(object instanceof BeanFactoryAware) {
			
			if( LOGGER.isDebugEnabled() ) {
				LOGGER.debug("Injecting BeanFactory to the BeanFactoryAware bean of type {}", 
						object.getClass().getSimpleName());
			}
			
			((BeanFactoryAware) object).setBeanFactory(this);
		}
		return object;
	}
	
	private InvocationHandler getRequestContextInvocationHandler(
			BeanDefinition<?> beanDefinition) {
		
		Class<?> requestContextInvocationHandlerClass = null;
		try {
			requestContextInvocationHandlerClass = 
				Class.forName("org.essentialframework.web.RequestContextInvocationHandler");
			
			return (InvocationHandler) requestContextInvocationHandlerClass
					.getConstructor(GenericScopedBeanFactory.class, BeanDefinition.class)
					.newInstance(this, beanDefinition);
			
		} catch(ClassNotFoundException e) {
			throw new BeanFactoryException(
					"RequestContextInvocationHandler (from essential-web module) is not found on classpath. "
					+ "Cannot instantiate a proxy for a web-scoped bean "+ beanDefinition.getBeanName());

		} catch(Throwable t) {
			throw new BeanFactoryException("Cannot instantiate a RequestContextInvocationHandler proxy for "
					+ "web-scoped bean"+beanDefinition.getBeanName(), t);
		}
		
	}
	
}
