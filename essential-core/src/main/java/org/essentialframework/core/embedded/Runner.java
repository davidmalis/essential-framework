package org.essentialframework.core.embedded;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.essentialframework.core.initialization.AnnotationBasedBeanFactory;
import org.essentialframework.core.initialization.BeanFactory;
import org.essentialframework.core.web.DelegatingServlet;

public class Runner {
	
	public static Server run(int port) throws Exception {
		return run(port, new AnnotationBasedBeanFactory(false));
	}
	
	public static Server run(int port, BeanFactory context) throws Exception {
		
		Server server = new Server(port);
		ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
		
        handler.addServletWithMapping(new ServletHolder(
        	new DelegatingServlet(context)), "/*");
        
        server.start();
        server.join();
        
        return server;
	}

}
