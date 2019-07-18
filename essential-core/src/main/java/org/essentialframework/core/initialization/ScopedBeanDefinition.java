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
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

/**
 * Object describing a bean with a defined {@link BeanScope},
 * a constructor candidate, implemented interfaces and a bean name.
 * 
 * @author David Malis
 * @since 1.0
 * @see BeanDefinition
 * @see BeanDefinitionUtils
 */
public class ScopedBeanDefinition<T> 
	implements BeanDefinition<T> {
	
	private String name;
	private Class<T> type;
	private BeanScope scope;
	private Constructor<T> constructorCandidate;
	private Class<? super T>[] interfaces;
	
	/**
	 * Constructs an instance defining specified type.
	 * @param type
	 */
	public ScopedBeanDefinition(Class<T> type) {
		
		if(type == null) {
			throw new BeanDefinitionException("Cannot create bean definition for null type");
		}
		if(type.isInterface()) {
			throw new BeanDefinitionException("Cannot create bean definition for an interface "
				+ type.getSimpleName());
		}
		
		this.type = type;
		this.name = BeanDefinitionUtils.lookForBeanName(type);
		this.scope = BeanDefinitionUtils.findScopeOf(type);
		this.constructorCandidate = BeanDefinitionUtils.findConstructorCandidateFor(type);
		this.interfaces = BeanDefinitionUtils.findApplicableInterfacesOf(type);
	
	}
	
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public String getBeanName() {
		return name;
	}
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public boolean isSingleton() {
		return BeanScope.SINGLETON.equals(scope);
	}
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public boolean isPrototype() {
		return BeanScope.PROTOTYPE.equals(scope);
	}
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public boolean isAbstract() {
		return Modifier.isAbstract(type.getModifiers());
	}
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Class<? super T>[] getInterfaces() {
		return interfaces;
	}
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public BeanScope getScope() {
		return scope;
	}
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Class<T> getType() {
		return type;
	}
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Constructor<T> getConstructorCandidate() {
		return constructorCandidate;
	}
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Parameter[] getConstructorArguments() {
		return constructorCandidate.getParameters();
	}
}
