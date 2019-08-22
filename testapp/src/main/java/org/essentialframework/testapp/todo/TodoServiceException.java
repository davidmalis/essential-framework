package org.essentialframework.testapp.todo;

public class TodoServiceException extends Exception {

	private static final long serialVersionUID = 6348778560908654320L;
	
	public TodoServiceException(String message) {
		super(message);
	}
	
	public TodoServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
