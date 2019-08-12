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

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.essentialframework.core.utility.Assert;
import org.essentialframework.web.RequestContext;
import org.essentialframework.web.annotation.RequestBody;

public class RequestBodyWebContextParameterBinder
	extends AbstractWebContextParameterBinderChainlink implements WebContextParameterBinder {
	
	@Override
	public Object doBind(RequestContext requestContext, Parameter parameter) {
		Assert.notNull(requestContext, "Cannot bind arguments from a null request context");
		Assert.notNull(parameter, "Cannot bind argument for a null parameter");
		
		final HttpServletRequest request = requestContext.getRequest();
		final RequestBodyDeserializingStrategy deserializingStrategy = 
				determineResponseBodyDeserializingStrategy(request);
		
		final boolean isOptional = Optional.class.equals(parameter.getType());
		final Class<?> actualParameterType = isOptional ? 
			determineActualParameterTypeFromOptional(parameter) : parameter.getType();
		
		if(parameter.isAnnotationPresent(RequestBody.class)) {
			
			Object body = null; 
			body = deserializingStrategy.deserialize(request, actualParameterType);
			
			if(isOptional) {
				return Optional.ofNullable(body);
			} else {
				if(body == null) {
					throw new WebContextParameterBindingException(
						"Required request body is not found in the request.");
				}
				return body;
			}
			
		} else {
			return super.invokeNextBinder(requestContext, parameter);
		}
	
	}
	
	private static RequestBodyDeserializingStrategy 
		determineResponseBodyDeserializingStrategy(ServletRequest request) {
		//TODO determine by content-type
		return new JsonbRequestBodyDeserializingStrategy();
	}
	
	private static Class<?> determineActualParameterTypeFromOptional(Parameter parameter) {
		final ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
		return (Class<?>) parameterizedType.getActualTypeArguments()[0];
	}
	

}
