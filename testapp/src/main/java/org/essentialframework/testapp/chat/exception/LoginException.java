package org.essentialframework.testapp.chat.exception;

public class LoginException extends ChatServiceException {

	private static final long serialVersionUID = -6009412162670258927L;

	public LoginException(String message) {
		super(message);
	}
	
	public LoginException(String message, Throwable t) {
		super(message, t);
	}
}
