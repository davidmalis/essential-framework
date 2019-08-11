package org.essentialframework.web.utility;

import org.essentialframework.core.initialization.BeanFactory;
import org.essentialframework.web.DelegatingServlet;
import org.essentialframework.web.RequestContext;
import org.essentialframework.web.RequestContextHolder;

public class WebUtils {

	public static BeanFactory findBeanFactory() {
		final RequestContext context = RequestContextHolder.currentRequestContext();
		BeanFactory beanFactory = null;
		if(context != null) {
			beanFactory = (BeanFactory) context.getRequest()
				.getAttribute(DelegatingServlet.BEAN_FACTORY_ATTRIBUTE);
		}
		return beanFactory;
	}
	
	
	private WebUtils() {}
}
