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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.essentialframework.core.utility.Assert;
import org.essentialframework.web.RequestContext;
import org.essentialframework.web.RequestContextHolder;

public class DefaultRequestContextMethodArgumentBinder 
	implements MethodArgumentBinder {
	
	private List<Parameter> parameters;
	
	private WebContextParameterBinder bindingChain = 
			WebContextParameterBinderChainFactory.createDefaultChain();
	
	public DefaultRequestContextMethodArgumentBinder(Method method) {
		Assert.notNull(method, "Cannot bind argument of a null method");
		this.parameters = Arrays.asList(method.getParameters());
	}
	
	public Object[] bind() {

		final RequestContext requestContext = RequestContextHolder.currentRequestContext();
		List<Object> boundArguments = new ArrayList<>();
		
		this.parameters.forEach(parameter -> {
			boundArguments.add(bindingChain.doBind(requestContext, parameter));
		});
		
		return boundArguments.toArray();
	}
	
//	private static boolean isRequestBodyParameter(Parameter parameter) {
//		return parameter.isAnnotationPresent(RequestBody.class);
//	}
//	
//	private static boolean isRequestParameter(Parameter parameter) {
//		return parameter.isAnnotationPresent(RequestParameter.class);
//	}
//	
//	private static boolean isBeanParameter(Parameter parameter) {
//		return parameter.isAnnotationPresent(Bean.class);
//	}
//	
//	private Object getBeanForParameter(Parameter parameter) {
//		
//		final BeanFactory beanFactory = WebUtils.findBeanFactory();
//		
//		final Alias alias = parameter.getAnnotation(Alias.class);
//		if(alias != null) {
//			return beanFactory.getBean(alias.value());
//		}
//		return beanFactory.getBean(parameter.getType());
//	}
//	
//	private Object getObjectForBodyParameter(Parameter parameter) {
//		
//		final RequestContext context = RequestContextHolder.currentRequestContext();
//		boolean isOptional = Optional.class.equals(parameter.getType());
//		
//		if(isOptional) {
//			final ParameterizedType parameterizedType = (ParameterizedType) parameter.getParameterizedType();
//			final Class<?> type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
//
//			return Optional.ofNullable(
//				this.requestBodyDeserializingStrategy.deserialize(context.getRequest(), type));
//		} else {
//			return this.requestBodyDeserializingStrategy.deserialize(context.getRequest(), 
//					parameter.getType());
//		}
//		
//	}
//
//	@Override
//	public Method getMethod() {
//		return this.method;
//	}
	
}
