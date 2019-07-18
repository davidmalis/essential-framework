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
package org.essentialframework.core.web;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.essentialframework.core.annotation.Controller;
import org.essentialframework.core.annotation.RequestHandler;
import org.essentialframework.core.initialization.BeanFactory;
import org.essentialframework.core.utility.ClassGraphUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * @author David Malis
 * @since 1.0
 */
public class DefaultRequestHandlerRegistry implements RequestHandlerRegistry {
	
	private static final Logger LOGGER 
		= LoggerFactory.getLogger(DefaultRequestHandlerRegistry.class);
	
	private Map<HttpMethod, Map<String, HandlerMethod>> handlers = 
		new ConcurrentHashMap<>();
	
	private BeanFactory bf;
	
	/**
	 * @throws AmbiguousHandlerException 
	 * 
	 */
	DefaultRequestHandlerRegistry(BeanFactory bf, String... scanPackages) throws AmbiguousHandlerException {
		this.bf = bf;
		
		final Iterator<Class<?>> it 
			= ClassGraphUtils.getAnnotated(Controller.class, scanPackages).iterator();
		
		while(it.hasNext()) {
			mapRequestHandlers(it.next());
		}
	}

	/**
	 * 
	 * @param controller
	 * @throws AmbiguousHandlerException
	 */
	private void mapRequestHandlers(Class<?> handlersClass) { 
		if(handlersClass == null) {
			return;
		}
		
		for(Method m : handlersClass.getMethods()){
			final RequestHandler handlerAnnotation = m.getAnnotation(RequestHandler.class);
			if(handlerAnnotation != null) {
				final HttpMethod httpMethod = handlerAnnotation.method();
				final String url = handlerAnnotation.url();
				
				handlers.putIfAbsent(httpMethod, new ConcurrentHashMap<String, HandlerMethod>());
				final Map<String, HandlerMethod> urlMap = handlers.get(httpMethod);
				if(urlMap.containsKey(url)) {
					
					if(LOGGER.isDebugEnabled()){
						LOGGER.debug("Ambigiuous request handler found for url: {}", url);
					}
					throw new AmbiguousHandlerException("Ambigiuous request handler found for url: "+url);
				}
				urlMap.put(url, new AnnotatedHandlerMethod(m, handlersClass));
			}
		}
		
	}

	/**
	 * {@inheritDoc}}
	 * @throws NoHandlerFoundException 
	 */
	@Override
	public HandlerMethod findHandler(final HttpMethod httpMethod, final String url) 
		throws NoHandlerFoundException {
		
		final Map<String, HandlerMethod> urlMap = handlers.get(httpMethod);
		if(urlMap != null) {
			final HandlerMethod method = urlMap.get(url);
			if(method != null) {
				return createProxyHandler(method);
			}
		}
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("No request handler found for {}: {}", httpMethod, url);
		}
		throw new NoHandlerFoundException("No request handler found for "+httpMethod+": "+url);
	}
	
	private HandlerMethod createProxyHandler(HandlerMethod method) {
		
		return (HandlerMethod) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), 
				new Class<?>[] {HandlerMethod.class}, 
			new AnnotatedHandlerMethodInvocationHandler(bf, (AnnotatedHandlerMethod) method));
		
	}
}
