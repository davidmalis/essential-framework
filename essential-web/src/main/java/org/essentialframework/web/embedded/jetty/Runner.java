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

public class Runner {
	
	public static Server run(int port) throws Exception {
		return run(port, new AnnotationBasedBeanFactory(false));
	}
	
	public static Server run(int port, BeanFactory context) throws Exception {
		
		final Server server = new Server(port);
		
		final ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(new ServletHolder(
	        	new DelegatingServlet(context)), "/*");
		
		server.setHandler(servletHandler);
		
		enableSessionManagement(server, servletHandler);
        
        server.start();
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
