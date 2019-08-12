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
import java.util.Map;
import java.util.Optional;

import org.essentialframework.core.utility.Assert;
import org.essentialframework.web.RequestContext;
import org.essentialframework.web.annotation.RequestParameter;

public class RequestParameterWebContextParameterBinder 
	extends AbstractWebContextParameterBinderChainlink implements WebContextParameterBinder{

	@Override
	public Object doBind(RequestContext requestContext, Parameter parameter) {
		Assert.notNull(requestContext, "Cannot bind arguments from a null request context");
		Assert.notNull(parameter, "Cannot bind argument for a null parameter");
		
		if(parameter.isAnnotationPresent(RequestParameter.class)) {
			
			final Map<String, String[]> parameterMap = requestContext.getRequest().getParameterMap();

			final boolean isOptional = Optional.class.equals(parameter.getType());
			
			final String requestedParameterName = parameter.getAnnotation(RequestParameter.class).value();
			final int parameterIndex = determineParameterValuesIndexFromParameterName(requestedParameterName);
			final String actualParameterName = stripParameterValuesIndex(requestedParameterName);
			
			String parameterValue = null;
			
			try {
				
				if(parameterMap.containsKey(actualParameterName)) {
					parameterValue = parameterMap.get(actualParameterName)[parameterIndex];
				}

			} catch (ArrayIndexOutOfBoundsException e) {
			}
			
			if(isOptional) {
				return Optional.ofNullable(parameterValue);
			} else {
				if(parameterValue == null) {
					throw new WebContextParameterBindingException(
						"Required request parameter '"+ requestedParameterName + "' not found in the request.");
				}
				return parameterValue;
			}
			
			
		} else {
			return super.invokeNextBinder(requestContext, parameter);
		}

	}
	
	private static int determineParameterValuesIndexFromParameterName(String name) {
		int index = 0;
		
		if(name != null && name.endsWith("]")) {
			try {
				index = Integer.parseInt(
						name.substring(name.lastIndexOf("[")+1, name.length()-1));
			} catch (NumberFormatException e) {
				throw new WebContextParameterBindingException(
					"Cannot determine parameter values index from request parameter name", e);
			}
		}
		
		return index;
		
	}
	
	private static String stripParameterValuesIndex(String name) {
		if(name != null && name.endsWith("]")) {
			return name.substring(0, name.lastIndexOf("["));
		}
		return name;
	}
	
	

}
