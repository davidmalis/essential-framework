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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.essentialframework.core.annotation.Alias;
import org.essentialframework.core.annotation.Scope;
import org.essentialframework.core.annotation.Wired;
import org.essentialframework.core.utility.Assert;

/**
 * Helper class for operations for defining bean elements
 * and aspects. 
 * <p>
 * Intended for internal framework use only (package-private).
 * 
 * @author David Malis
 * @since 1.0
 */
final class BeanDefinitionUtils {

	/**
	 * No instantiation.
	 */
	private BeanDefinitionUtils() {}
	
	/**
	 * Checks if specified type is a non applicable interface. 
	 * See {@link BeanDefinition#NON_APPLICABLE_INTERFACES} list.
	 * @param type
	 * @return {@code true/false}
	 */
	private static boolean isNonApplicableInterface(final Class<?> type) {
		return type.isInterface() && Arrays.asList(
			BeanDefinition.NON_APPLICABLE_INTERFACES).contains(type);
	}
	
	/**
	 * Finds implemented interfaces of specified type (or extended 
	 * interfaces in case of specified interface) that are not
	 * non-applicable. See {@link BeanDefinition#NON_APPLICABLE_INTERFACES} 
	 * list.
	 * @param type
	 * @return Class[]
	 */
	@SuppressWarnings("unchecked")
	final static <T> Class<? super T>[] findApplicableInterfacesOf(final Class<T> type) {
		return (Class<? super T>[]) 
				Arrays.stream(type.getInterfaces())
					  .filter(i -> { return !isNonApplicableInterface(i); })
					  .toArray(Class<?>[]::new);
	}
	/**
	 * Returns a scope of a bean specified via {@link Scope} annotation.
	 * If annotation is not present, default scope of {@link BeanScope#SINGLETON}
	 * is returned.
	 * @param type
	 * @return {@link BeanScope}
	 * @see Scope
	 */
	final static BeanScope findScopeOf(final Class<?> type) {
		final org.essentialframework.core.annotation.Scope declaredScope = 
		type.getAnnotation(org.essentialframework.core.annotation.Scope.class);
		
		if(declaredScope == null) {
			return BeanScope.SINGLETON;
		}
		
		final BeanScope scope = BeanScope.resolve(declaredScope.value());
		if(scope != null) {
			return scope;
		}
		throw new BeanDefinitionException(
			"Non existing scope declared");
	}
	/**
	 * Finds an appropriate constructor candidate for bean instatiation.
	 * Bean must have exactly one public constructor or exactly one constructor
	 * annotated with {@link Wired} annotation in orded to apply.
	 * @param type
	 * @return {@link Constructor}
	 * @see Wired
	 */
	static <T> Constructor<T> findConstructorCandidateFor(final Class<T> type){
		// We'll not be adding additional constructors of
		// other types to this array. Might as well cast it now.
		@SuppressWarnings("unchecked")
		final Constructor<T>[] constructors 
			= (Constructor<T>[]) type.getConstructors();
		
		if(constructors.length <= 0) {
			throw new BeanDefinitionException(
				"Type " + type.getSimpleName() +" has no public constructors");
		}
		if(constructors.length == 1) {
			return constructors[0];
		}
		
		final List<Constructor<T>> wiredConstructors 
			= Arrays.stream(constructors)
					.filter(c-> c.isAnnotationPresent(Wired.class))
					.collect(Collectors.toList());
		
		if(wiredConstructors.size() != 1) {
			throw new BeanDefinitionException(
				"There are more than one public and/or wired constructors of type " 
					+ type.getSimpleName());
		}
		return wiredConstructors.get(0);
		
	}
	/**
	 * Finds all fields of a bean annotated with {@link Wired} annotation.
	 * @param type
	 * @return {@link Field}[]
	 * @see Wired
	 */
	static Field[] findWiredFields(final Class<?> type) {
		return Arrays.stream(type.getDeclaredFields())
					 .filter(f -> f.isAnnotationPresent(Wired.class))
					 .toArray(Field[]::new);
	}
	
	/**
	 * Returns a bean name using {@link Class#getSimpleName()} or
	 * name specified via {@link Alias} annotation.
	 * @param type
	 * @return String 
	 * @see Alias
	 */
	static String lookForBeanName(final Class<?> type) {
		String name = type.getSimpleName();
		Alias alias = type.getAnnotation(Alias.class);
		if(alias != null) {
			name = alias.value();
		}
		Assert.hasText(name, "Cannot find applicable bean name for " + type);
		return name;
	}
	
}
