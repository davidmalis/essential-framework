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
package org.essentialframework.web.embedded.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionIdManager;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.essentialframework.core.initialization.AnnotationBasedBeanFactory;
import org.essentialframework.core.initialization.BeanFactory;
import org.essentialframework.web.DelegatingServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Runner {
	
	public static final Logger LOGGER =
		LoggerFactory.getLogger(Runner.class);
	
	public static Server run(int port) throws Exception {
		return run(port, new AnnotationBasedBeanFactory(false));
	}
	
	public static Server run(int port, BeanFactory context) throws Exception {
		
		final long start = System.currentTimeMillis();
		LOGGER.warn("Starting embedded Jetty on port {}", port);
		
		final Server server = new Server(port);
		
		final ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(new ServletHolder(
	        	new DelegatingServlet(context)), "/*");
		
		server.setHandler(servletHandler);
		
		enableSessionManagement(server, servletHandler);
        
        server.start();
        LOGGER.warn("Application started in {} ms.", System.currentTimeMillis()-start);
        
        server.join();
        return server;
	}
	
	private static void enableSessionManagement(Server server, ServletHandler servletHandler) {
		final SessionHandler sessionHandler = new SessionHandler();
		final SessionIdManager sessionIdManager = new DefaultSessionIdManager(server);
		server.setSessionIdManager(sessionIdManager);
		servletHandler.setHandler(sessionHandler);
	}

}
