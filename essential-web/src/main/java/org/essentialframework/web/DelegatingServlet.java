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
package org.essentialframework.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.essentialframework.core.initialization.BeanFactory;
import org.essentialframework.core.initialization.BeanFactoryAware;
import org.essentialframework.core.utility.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * @author David Malis
 * @version 1.0
 */
public class DelegatingServlet extends HttpServlet implements BeanFactoryAware {

	private static final long serialVersionUID = 
		-4303621952406227515L;
	
	private static final Logger LOGGER =
		LoggerFactory.getLogger(DelegatingServlet.class);
	
	public static final String JAVAX_INCLUDE_REQUEST_URI_ATTRIBUTE = 
		"javax.servlet.include.request_uri";
	
	public static final String BEAN_FACTORY_ATTRIBUTE = 
		DelegatingServlet.class.getName() + ".beans.beanfactory";

	private BeanFactory beanFactory;
	
	private RequestHandlerRegistry<HandlerMethod> requestHandlerRegistry;
	
	/**
	 * TODO
	 * @param beanFactory
	 */
	public DelegatingServlet(BeanFactory beanFactory) {
		this(beanFactory, new DefaultRequestHandlerRegistry(beanFactory));
	}
	
	/**
	 * TODO
	 * @param beanFactory
	 * @param handlerRegistry
	 */
	public DelegatingServlet(final BeanFactory beanFactory, 
			final RequestHandlerRegistry<HandlerMethod> handlerRegistry) {
		
		Assert.notNull(beanFactory, "BeanFactory must not be null");
		Assert.notNull(handlerRegistry, "RequestHandlerRegistry must not be null");
		
		this.beanFactory = beanFactory;
		this.requestHandlerRegistry = handlerRegistry;

	}
	
	private static String getRequestUri(HttpServletRequest request) {
	
		String uri = (String) request.getAttribute(JAVAX_INCLUDE_REQUEST_URI_ATTRIBUTE);
		
		if (uri == null) {
			uri = request.getRequestURI();
		}
		
		return uri;
		
	}
	
	/**
	 * TODO
	 * @param request
	 * @param response
	 * @param handler
	 */
	private void delegate(HttpServletRequest request,
			HttpServletResponse response, HandlerMethod handler) {
		
		RequestContextHolder.setRequestContext(new ServletRequestContext(request, response));
		registerBeanFactory(request);
		
		try {
			
			handler.handle(request, response);
			
		} catch(Throwable e) {
			//TODO
			LOGGER.error("Servlet error: {}",e);
			throw e;
			
		} finally {
			RequestContextHolder.reset();
		}
		
	}
	
	/**
	 * TODO
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
		
		HandlerMethod handler = requestHandlerRegistry.findHandler(
				HttpMethod.GET, getRequestUri(request));
		
		delegate(request, response, handler);
	}
	
	/**
	 * TODO
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	
		HandlerMethod handler = requestHandlerRegistry.findHandler(
				HttpMethod.POST, getRequestUri(request));
		
		delegate(request, response, handler);
	}
	
	/**
	 * TODO
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		
		HandlerMethod handler = requestHandlerRegistry.findHandler(
				HttpMethod.PUT, getRequestUri(request));
		
		delegate(request, response, handler);
	}
	
	/**
	 * TODO
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		
		HandlerMethod handler = requestHandlerRegistry.findHandler(
				HttpMethod.DELETE, getRequestUri(request));
		
		delegate(request, response, handler);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	public BeanFactory getBeanFactory() {
		return this.beanFactory;
	}
	
	private void registerBeanFactory(ServletRequest request) {
		if(this.beanFactory != null) {
			request.setAttribute(BEAN_FACTORY_ATTRIBUTE, this.beanFactory);
		}
	}
	
}
