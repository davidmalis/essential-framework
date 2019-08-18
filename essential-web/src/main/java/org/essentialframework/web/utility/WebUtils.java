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
package org.essentialframework.web.utility;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.essentialframework.core.initialization.BeanFactory;
import org.essentialframework.web.DelegatingServlet;
import org.essentialframework.web.RequestContext;
import org.essentialframework.web.RequestContextHolder;

public class WebUtils {

	public static BeanFactory findBeanFactory() {
		final RequestContext context = RequestContextHolder.currentRequestContext();
		BeanFactory beanFactory = null;
		if(context != null) {
			beanFactory = (BeanFactory) context.getRequest()
				.getAttribute(DelegatingServlet.BEAN_FACTORY_ATTRIBUTE);
		}
		return beanFactory;
	}
	
	public static String getBaseUrl(HttpServletRequest request) {
	   StringBuilder sb = new StringBuilder(request.getScheme())
		   .append("://")
		   .append(request.getServerName())
		   .append(":")
		   .append(request.getServerPort())
		   .append(Optional.ofNullable(request.getContextPath()).orElse(""));
		return sb.toString();
	}
	
	
	private WebUtils() {}
}
