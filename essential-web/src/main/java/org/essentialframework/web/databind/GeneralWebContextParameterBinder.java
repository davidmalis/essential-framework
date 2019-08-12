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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.essentialframework.core.utility.Assert;
import org.essentialframework.web.RequestContext;

public class GeneralWebContextParameterBinder 
	extends AbstractWebContextParameterBinderChainlink implements WebContextParameterBinder {
	
	private boolean createSessions = true;
	
	@Override
	public Object doBind(RequestContext requestContext, Parameter parameter) {
		Assert.notNull(requestContext, "Cannot bind arguments from a null request context");
		Assert.notNull(parameter, "Cannot bind argument for a null parameter");
		
		final Class<?> parameterType = parameter.getType();
		
		if(HttpServletRequest.class.equals(parameterType)) {
			return requestContext.getRequest();
		} else if(HttpServletResponse.class.equals(parameterType)) {
			return requestContext.getResponse();
		} else if(HttpSession.class.equals(parameterType)) {
			return requestContext.getSession(this.createSessions);
		} else {
			return super.invokeNextBinder(requestContext, parameter);
		}
		
	}
	
	public boolean isCreateSessions() {
		return this.createSessions;
	}

	public void setCreateSessions(boolean createSessions) {
		this.createSessions = createSessions;
	}

}
