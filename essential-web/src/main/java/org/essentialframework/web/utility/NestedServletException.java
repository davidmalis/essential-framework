package org.essentialframework.web.utility;

import javax.servlet.ServletException;

public class NestedServletException extends ServletException {

	private static final long serialVersionUID 
		= -8406763131189432285L;

	public NestedServletException(String message, Throwable t) {
		super(message, t);
	}
	
	@Override
	public String getMessage() {
		final String message = super.getMessage();
		final Throwable cause = super.getCause();
		if(cause == null) {
			return super.getMessage();
		}
		StringBuilder sb = new StringBuilder(64);
		if (message != null) {
			sb.append(message).append("; ");
		}
		sb.append("nested exception is ").append(cause);
		return sb.toString();
	}
	
}
