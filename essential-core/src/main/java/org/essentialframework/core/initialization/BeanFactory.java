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

/**
 * Implementations of this interface should be responsible 
 * for complete bean lifecycle, dependency injection features
 * and should provide direct access to bean instances.
 * 
 * @author David Malis
 * @since 1.0
 */
public interface BeanFactory {
	
	/**
	 * Should return a bean instance of specified type 
	 * from the factory.
	 * @param type
	 * @return T
	 */
	<T> T getBean(Class<T> type);
	
	/**
	 * Should return a bean instance with a specified name
	 * from the factory.
	 * @param name
	 * @return {@link Object}
	 */
	Object getBean(String name);
	
}
