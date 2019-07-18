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
package org.essentialframework.core.initialization;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.essentialframework.core.annotation.Order;

/**
 * Interface to be implemented in Servlet 3.0+ environments in order
 * to configure {@link ServletContext} programmatically instead of traditional
 * xml deployment descriptor. Implementations of this interface will be 
 * auto-detected by {@link ContainerInitializer} which is coupled with
 * Servlet 3.0+ container specification of bootstrapping.
 * <p>
 * Implementations may optionally be annotated with {@link Order} annotation.
 * If so, the initializers will be ordered prior to invocation which provides 
 * a mechanism for users to ensure the order in which servlet container 
 * initalization occurs.
 * </p>

 * @author David Malis
 * @since 1.0
 */
public interface ApplicationInitializer {
	
	/**
	 * Configure the given {@link ServletContext} with any servlets, 
	 * filters, listeners, context-params and attributes necessary 
	 * for initializing this web application.
	 * @param servletContext the {@link ServletContext} to initialize
	 * @throws ServletException
	 */
	public void onStartup(ServletContext context) throws ServletException;

}
