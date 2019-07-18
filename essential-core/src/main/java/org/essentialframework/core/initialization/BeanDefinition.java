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
import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import org.essentialframework.core.annotation.Alias;

/**
 * This interface puts a contract on any object that wish 
 * to describe a bean. 
 * 
 * @author David Malis
 * @since 1.0
 */
public interface BeanDefinition<T> {
	
	public static final Class<?>[] NON_APPLICABLE_INTERFACES
		= { Serializable.class, Externalizable.class, Cloneable.class };
	
	/**
	 * Should return bean name using {@link Class#getSimpleName()} or
	 * bean name specified via {@link Alias} annotation.
	 * @see Alias
	 */
	String getBeanName();
	
	/**
	 * Checks if {@link BeanDefinition} describes a {@link BeanScope#SINGLETON}
	 * scoped bean.
	 * @return {@code true/false}
	 */
	boolean isSingleton();
	
	/**
	 * Checks if {@link BeanDefinition} describes a {@link BeanScope#PROTOTYPE}
	 * scoped bean.
	 * @return {@code true/false}
	 */
	boolean isPrototype();
	
	/**
	 * Checks if {@link BeanDefinition} describes an abstract bean.
	 * @return {@code true/false}
	 */
	boolean isAbstract();
	
	/**
	 * Returns any interfaces implemented by bean described 
	 * by {@link BeanDefinition}
	 * @return {@link Class}
	 */
	Class<? super T>[] getInterfaces();
	
	/**
	 * Returns {@link BeanScope} of bean described by this {@link BeanDefinition}
	 * @return {@link BeanScope}
	 */
	BeanScope getScope();
	
	/**
	 * Returns type of bean described by this {@link BeanDefinition}
	 * @return {@link Class}
	 */
	Class<T> getType();
	
	/**
	 * Returns constructor candidate for instantiating bean described by 
	 * this {@link BeanDefinition}. 
	 * @return {@link Constructor}
	 */
	Constructor<T> getConstructorCandidate();
	
	/**
	 * Returns parameters of constructor candidate for instantiating bean
	 * described by this {@link BeanDefinition}
	 * @return {@link Parameter}[]
	 */
	Parameter[] getConstructorArguments();

}
