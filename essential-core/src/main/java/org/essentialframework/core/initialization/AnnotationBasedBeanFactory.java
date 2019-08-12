/* MIT License
*
* Copyright (c) 2018 David Mališ
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/
package org.essentialframework.core.initialization;
import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.essentialframework.core.annotation.Component;
import org.essentialframework.core.annotation.Controller;
import org.essentialframework.core.annotation.Repository;
import org.essentialframework.core.annotation.Service;
import org.essentialframework.core.utility.ClassGraphUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link BeanFactory} implementation built upon {@link GenericScopedBeanFactory} 
 * and {@link WrappingProxyBeanFactory} which supplies main dependency injection 
 * features of the framework. Factory registers itself as a bean so it
 * can be injected via dependency injection.
 * 
 * @author David Malis
 * @since 1.0
 */
public class AnnotationBasedBeanFactory 
	extends WrappingProxyBeanFactory implements BeanFactory {

	private static final Logger LOGGER 
		= LoggerFactory.getLogger(AnnotationBasedBeanFactory.class);

	private String[] scanPackages;
	
	public AnnotationBasedBeanFactory(boolean lazy, String... scanPackages) {
		LOGGER.warn("Instantiating framework context...");
		
		this.scanPackages = scanPackages;
		
		if(LOGGER.isDebugEnabled()) {
			LOGGER.debug("Registering bean definitions on the {} bean factory", lazy ? "lazy" : "");
		}
				
		Arrays.asList( 
			Controller.class,
			Service.class, 
			Repository.class, 
			Component.class)
		.forEach(this::registerAnnotatedBeans);
		
		if( !lazy ) {
			LOGGER.debug("Instantiating singletons eagerly");
			instantiateSingletons();
		}
	}

	/**
	 * Registers annotated bean candidates in the factory. 
	 * Using {@link Index}, method gets candidate types from
	 * the dedicated index file.
	 * @param annotation
	 * @see Index
	 */
	private void registerAnnotatedBeans(Class<? extends Annotation> annotation) {
		for (Class<?> type : ClassGraphUtils.getAnnotated(annotation, scanPackages)) {
			super.registerDefinitionFor(type);
		}
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public <T> T getBean(Class<T> type) {
		return (T) getInstanceFor(getBeanDefinitionFor(type));
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Object getBean(String name) {
		return getInstanceFor(getBeanDefinitionFor(name));
	}

}
