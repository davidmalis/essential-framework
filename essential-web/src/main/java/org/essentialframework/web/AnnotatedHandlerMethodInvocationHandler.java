/* MIT License
*
* Copyright (c) 2019 David Mališ
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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.essentialframework.core.initialization.BeanFactory;
import org.essentialframework.web.databind.MethodArgumentBinder;

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
		final MethodArgumentBinder argumentBinder = handlerMethod.getArgumentBinder();
		final ResponseWriter responseWriter = new ResponseWriter();
		
		Object handlerMethodInvocationResult;
		final Class<?> handlerMethodOwnerType = handlerMethod.getMethodOwnerType();
		final Object handlerMethodOwner = beanFactory.getBean(handlerMethodOwnerType);
		
		Object[] boundArguments = argumentBinder.bind();

		/* NOTE:
		 * At this point, if we are already dealing with the proxy
		 * served from the bean factory, workaround is to pass the 
		 * call on to the next invocation handler, else 
		 * do the usual invocation on the direct instance.
		 */
		if(Proxy.isProxyClass(handlerMethodOwner.getClass())) {
			
			handlerMethodInvocationResult = Proxy.getInvocationHandler(handlerMethodOwner).invoke(handlerMethodOwner, 
				handlerMethod.getMethod(), boundArguments);
			
		} else {
			handlerMethodInvocationResult = handlerMethod.getMethod().invoke(
				handlerMethodOwner,	boundArguments);
		}

		responseWriter.setPayload(handlerMethodInvocationResult);
		responseWriter.write(request, response);
		
		//---
		return handlerMethodInvocationResult;
		
		
	}
	
	
//	List<Object> bindArguments(HttpServletRequest request, HttpServletResponse response, AnnotatedHandlerMethod handlerMethod){
//		
//		final List<Object> boundArguments = new ArrayList<>();
//		for(Parameter parameter : handlerMethod.getParameters()) {
//			if(HttpServletRequest.class.equals(parameter.getType())) {
//				boundArguments.add(request);
//			}
//			else if(HttpServletResponse.class.equals(parameter.getType())) {
//				boundArguments.add(response);
//			} else {
//				boundArguments.add(null);
//			}
//		}
//		return boundArguments;
//		
//	}
//	
//	
	
	
	
	

}
