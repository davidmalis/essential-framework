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
import org.essentialframework.core.annotation.Scope;

/**
 * Possible scopes of a bean. Should be specified via {@link Scope}
 * annotation directly on the bean type.
 * 
 * @author David Malis
 * @since 1.0
 * @see Scope
 */
public enum BeanScope {
	
	/**
	 * Singleton scope which instructs {@link BeanFactory} 
	 * to instantiate bean only once per application and 
	 * serve the same instance to all requestors.
	 */
	SINGLETON, 
	
	/**
	 * Prototype scope which instructs {@link BeanFactory}
	 * to instantiate bean every time it is requested.
	 */
	PROTOTYPE,
	
	/**
	 * TODO
	 */
	SESSION,
	
	/**
	 * TODO
	 */
	REQUEST
	;
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static BeanScope resolve(String name) {
		for(BeanScope beanScope : values()) {
			if(beanScope.name().equalsIgnoreCase(name)) {
				return beanScope;
			}
		}
		return null;
	}
	
}
