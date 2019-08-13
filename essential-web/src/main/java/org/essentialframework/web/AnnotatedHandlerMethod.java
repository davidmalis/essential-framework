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

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.essentialframework.core.annotation.Alias;
import org.essentialframework.web.databind.DefaultRequestContextMethodArgumentBinder;
import org.essentialframework.web.databind.MethodArgumentBinder;

/**
 * TODO
 * @author David Malis
 * @version 1.0
 */
public class AnnotatedHandlerMethod implements HandlerMethod {
	
	private HttpMethod httpMethod;
	
	private String uri;
	
	private String methodOwnerName;
	
	private Class<?> methodOwnerType;
	
	private Method method;
	
	private Parameter[] parameters;
	
	private Class<?> returnType;
	
	private MethodArgumentBinder argumentBinder;
	
	AnnotatedHandlerMethod(final HttpMethod httpMethod, final String uri, final Method method) {
		this.httpMethod = httpMethod;
		this.uri = uri;
		this.method = method;
		this.methodOwnerType = this.method.getDeclaringClass();
		if(this.methodOwnerType.isAnnotationPresent(Alias.class)) {
			this.methodOwnerName = this.methodOwnerType.getAnnotation(Alias.class).value();
		}
		this.parameters = this.method.getParameters();
		this.returnType = this.method.getReturnType();
		this.argumentBinder = new DefaultRequestContextMethodArgumentBinder(this.method);
	}

	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response) {
	}


	public String getMethodOwnerName() {
		return methodOwnerName;
	}


	public void setMethodOwnerName(String methodOwnerName) {
		this.methodOwnerName = methodOwnerName;
	}


	public Class<?> getMethodOwnerType() {
		return methodOwnerType;
	}


	public void setMethodOwnerType(Class<?> methodOwnerType) {
		this.methodOwnerType = methodOwnerType;
	}

	@Override
	public Method getMethod() {
		return method;
	}


	public void setMethod(Method method) {
		this.method = method;
	}


	public Parameter[] getParameters() {
		return parameters;
	}


	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}


	public Class<?> getReturnType() {
		return returnType;
	}


	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

	@Override
	public MethodArgumentBinder getArgumentBinder() {
		return this.argumentBinder;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public String getUri() {
		return uri;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
