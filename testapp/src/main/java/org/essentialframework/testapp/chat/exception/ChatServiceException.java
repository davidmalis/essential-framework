package org.essentialframework.testapp.chat.exception;

public class ChatServiceException extends Exception {

	private static final long serialVersionUID = -8802021568846918177L;

	public ChatServiceException(String message) {
		super(message);
	}
	
	public ChatServiceException(String message, Throwable t) {
		super(message, t);
	}
	
}
