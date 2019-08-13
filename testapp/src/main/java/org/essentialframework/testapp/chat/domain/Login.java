package org.essentialframework.testapp.chat.domain;

import java.io.Serializable;

public class Login implements Serializable {

	private static final long serialVersionUID = 
		-2365990683110353401L;
	
	private String sessionId;
	private User user;
	
	public String getSessionId() {
		return sessionId;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	

}
