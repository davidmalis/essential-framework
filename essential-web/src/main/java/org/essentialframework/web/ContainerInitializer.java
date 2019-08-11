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
package org.essentialframework.web;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;

import org.essentialframework.core.utility.AnnotationBasedOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link javax.servlet.ServletContainerInitializer} from the Servlet 3.0+ 
 * specification which supports code-based configuration of the servlet container using 
 * {@link ApplicationInitializer} interface instead of the traditional xml deployment descriptor.
 * <p>
 * This class is loaded, instantiated and it's method {@link #onStartup(Set, ServletContext)
 * is invoked by any Servlet 3.0-compliant container during container startup assuming that 
 * the essential-core module JAR is present on the classpath. This is made possible through 
 * the JAR Services API {@link java.util.ServiceLoader#load(Class)} method which detects the 
 * essential-core module's META-INF/services/javax.servlet.ServletContainerInitializer 
 * configuration file. See the JAR Services API documentation as well as section 8.2.4 of the 
 * Servlet 3.0 Final Draft specification for complete details.
 * </p><p>
 * In short, this class is responsible for delegating {@link ServletContext} instance to all
 * {@link ApplicationInitializer} implementations which are holding the responsibility of actually 
 * configuring the context.
 * </p>
 * If no {@link ApplicationInitializer} implementations are available on the classpath, this 
 * initializer will have no effect other than logging into servlet container's log file.
 * 
 * @author David Malis
 * @since 1.0
 * @see ApplicationInitializer
 */
@HandlesTypes(ApplicationInitializer.class)
public class ContainerInitializer implements ServletContainerInitializer {
	
	private static final Logger LOGGER = 
		LoggerFactory.getLogger(ContainerInitializer.class);
	
	/**
	 * This class declares @{@link HandlesTypes}(ApplicationInitializer.class) so Servlet 3.0+ 
	 * containers will automatically scan the classpath for {@link ApplicationInitializer} 
	 * implementations and provide the set of such types to the initializerTypes parameter of
	 * this method.
	 * <p>
	 * This method then delegates {@link ServletContext} instance to all detected {@link ApplicationInitializer}
	 * implementations on the classpath by invoking {@link ApplicationInitializer#
	 * onStartup(ServletContext)} method and passing the instance. It is upon the user 
	 * to actually implement {@link ApplicationInitializer} interface and do a servlet 
	 * configuration.
	 * </p><p>
	 * If no {@link ApplicationInitializer} implementations are found on the
	 * classpath, this method is effectively a no-op.
	 * </p>
	 * @param initializerTypes all implementations of {@link ApplicationInitializer} found on 
	 * the application classpath
	 * @see ApplicationInitializer
	 */
	public void onStartup(final Set<Class<?>> initializerTypes, 
			final ServletContext servletContext) throws ServletException {
		
		final List<ApplicationInitializer> initializers 
			= new LinkedList<>();
		
		LOGGER.warn("Looking for Application Initializers...");
		
		for(Class<?> type : Optional.ofNullable(initializerTypes).orElse(Collections.emptySet())) {
			
			if( !type.isInterface() && !Modifier.isAbstract(type.getModifiers())
					&& ApplicationInitializer.class.isAssignableFrom(type) ) {
				try {
					initializers.add(
						(ApplicationInitializer) type.getConstructor().newInstance());
				} catch(Throwable e) {
					LOGGER.error("Error while instantiating {}", type);
					throw new ServletException("Failed to instantiate ApplicationInitializer type: ", e);
				}
				
			}
		}
		
		if(initializers.isEmpty()) {
			LOGGER.warn("No manageable ApplicationInitializers found on the classpath.");
			servletContext.log(
			"No manageable ApplicationInitializer types detected on classpath");
			return;
		}
		
		LOGGER.warn("Found initializers: {}", initializers);
		servletContext.log(
			"ApplicationInitializer types detected on classpath: "+ initializers);
		
		initializers.sort(new AnnotationBasedOrderComparator());
		
		for(ApplicationInitializer initializer : initializers){
			initializer.onStartup(servletContext);
		}
	}
}
