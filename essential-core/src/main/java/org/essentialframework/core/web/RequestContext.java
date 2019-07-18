package org.essentialframework.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface RequestContext {
	
	Object getRequestAttribute(String name);
	
	Object getSessionAttribute(String name);
	
	void setRequestAttribute(String name, Object value);
	
	void setSessionAttribute(String name, Object value);
	
	void removeRequestAttribute(String name);
	
	void removeSessionAttribute(String name);
	
	String[] getRequestAttributesNames();
	
	String[] getSessionAttributesNames();
	
	String getSessionId();
	
	HttpServletRequest getRequest();
	
	HttpServletResponse getResponse();
	
	HttpSession getSession(boolean createIfAbsent);
	
	//TODO session mutex?
	//TODO destruction callbacks?

}
