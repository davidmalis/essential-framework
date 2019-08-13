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
package org.essentialframework.web.databind;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Map;

import org.essentialframework.core.initialization.TargetAwareProxy;
import org.essentialframework.core.utility.Assert;
import org.essentialframework.web.AnnotatedHandlerMethod;
import org.essentialframework.web.DelegatingServlet;
import org.essentialframework.web.HandlerMethod;
import org.essentialframework.web.PathVariableResolver;
import org.essentialframework.web.RequestContext;
import org.essentialframework.web.annotation.PathVariable;

public class PathVariableWebContextParameterBinder 
	extends AbstractWebContextParameterBinderChainlink implements WebContextParameterBinder {

	@Override
	public Object doBind(RequestContext requestContext, Parameter parameter) {
		Assert.notNull(requestContext, "Cannot bind arguments from a null request context");
		Assert.notNull(parameter, "Cannot bind argument for a null parameter");
		
		if(parameter.isAnnotationPresent(PathVariable.class)) {
			
			final String pathVariableName = parameter.getAnnotation(PathVariable.class).value();
			final String uri = getRequestUri(requestContext);
			final HandlerMethod handler = getHandlerMethod(requestContext);
			
			if(handler instanceof AnnotatedHandlerMethod) {
				
				final String handlerUri = ((AnnotatedHandlerMethod) handler).getUri();
				final Map<String, String> pathVariableMap = 
					PathVariableResolver.parse(handlerUri).resolve(uri);
			
				if(pathVariableMap.containsKey(pathVariableName)) {
					return pathVariableMap.get(pathVariableName);
				}
			}
			
			throw new WebContextParameterBindingException(
				"Required path variable '"+ pathVariableName +"' not found in the request uri");
			
		} else {
			return super.invokeNextBinder(requestContext, parameter);
		}
		
		
	}

	private String getRequestUri(RequestContext requestContext) {
		
		String uri = (String) requestContext.getRequestAttribute(
			DelegatingServlet.JAVAX_INCLUDE_REQUEST_URI_ATTRIBUTE);
		
		if (uri == null) {
			uri = requestContext.getRequest().getRequestURI();
		}
		
		return uri;
	}
	
	private HandlerMethod getHandlerMethod(RequestContext requestContext) {
		HandlerMethod handler = (HandlerMethod) requestContext.getRequestAttribute(
				DelegatingServlet.HANDLER_METHOD_ATTRIBUTE);
		
		if(Proxy.isProxyClass(handler.getClass())) {
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(handler);
			if(invocationHandler instanceof TargetAwareProxy) {
				handler = (HandlerMethod)((TargetAwareProxy) invocationHandler).getActualTarget();
			}
		}
		
		return handler;
	}
	
	
	
}
