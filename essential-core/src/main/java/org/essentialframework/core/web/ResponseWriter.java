package org.essentialframework.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;

public class ResponseWriter {
	
	private Object payload;
	
	private ResponseBodySerializingStrategy responseBodySerializingStrategy;
	
	public ResponseWriter() {
		this(new JsonbResponseBodySerializingStrategy());
	}
	
	public ResponseWriter(ResponseBodySerializingStrategy responseBodySerializingStrategy) {
		this.responseBodySerializingStrategy = responseBodySerializingStrategy;
	}
	
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	public void setResponseBodySerializingStrategy(ResponseBodySerializingStrategy responseBodySerializingStrategy) {
		this.responseBodySerializingStrategy = responseBodySerializingStrategy;
	}
	
	public void write(HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpStatus.OK_200);
		this.responseBodySerializingStrategy.serialize(response, this.payload);
	}

}