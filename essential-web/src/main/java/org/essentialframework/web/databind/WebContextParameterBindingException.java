package org.essentialframework.web.databind;

public class WebContextParameterBindingException 
	extends DataBindingException {
	
	private static final long serialVersionUID = 
		839900815756209052L;

	/**
	 * Constructs an instance with the specified message.
	 * @param message
	 */
	public WebContextParameterBindingException(String message) {
		super(message);
	}
	/**
	 * Constructs an instance with the specified message
	 * wrapping a {@link Throwable}.
	 * @param message
	 * @param throwable
	 */
	public WebContextParameterBindingException(String message, 
		Throwable throwable) {
		super(message, throwable);
	}

}
