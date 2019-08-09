package org.essentialframework.core.web.bind;

import javax.servlet.http.HttpServletRequest;

public interface RequestBodyDeserializingStrategy {
	
	<T> T deserialize(HttpServletRequest request, Class<T> type);

}
