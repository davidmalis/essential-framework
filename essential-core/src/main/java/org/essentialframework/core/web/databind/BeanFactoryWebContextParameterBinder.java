package org.essentialframework.core.web.databind;

import java.lang.reflect.Parameter;

import org.essentialframework.core.annotation.Alias;
import org.essentialframework.core.annotation.Bean;
import org.essentialframework.core.initialization.BeanFactory;
import org.essentialframework.core.utility.Assert;
import org.essentialframework.core.utility.WebUtils;
import org.essentialframework.core.web.RequestContext;

public class BeanFactoryWebContextParameterBinder 
	extends AbstractWebContextParameterBinderChainlink implements WebContextParameterBinder {

	@Override
	public Object doBind(RequestContext requestContext, Parameter parameter) {
		Assert.notNull(requestContext, "Cannot bind arguments from a null request context");
		Assert.notNull(parameter, "Cannot bind argument for a null parameter");
		
		final Class<?> parameterType = parameter.getType();
		
		if(BeanFactory.class.equals(parameterType)) {
			return WebUtils.findBeanFactory();

		} else if (isBeanParameter(parameter)) {
			return getBeanForParameter(parameter);
		
		} else {
			return super.invokeNextBinder(requestContext, parameter);
		}
	}

	private static boolean isBeanParameter(Parameter parameter) {
		return parameter.isAnnotationPresent(Bean.class);
	}
	
	private static Object getBeanForParameter(Parameter parameter) {
		final BeanFactory beanFactory = WebUtils.findBeanFactory();
		final Alias alias = parameter.getAnnotation(Alias.class);
		if(alias != null) {
			return beanFactory.getBean(alias.value());
		}
		return beanFactory.getBean(parameter.getType());
	}

	
	
}
