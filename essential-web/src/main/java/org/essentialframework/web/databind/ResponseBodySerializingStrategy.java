package org.essentialframework.web.databind;

import javax.servlet.http.HttpServletResponse;

public interface ResponseBodySerializingStrategy {
	
	void serialize(HttpServletResponse response, Object payload);

}
