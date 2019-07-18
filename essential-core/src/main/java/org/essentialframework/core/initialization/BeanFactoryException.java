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
 * General {@link BeanFactory} exception, which can be
 * thrown when anything goes wrong with {@link BeanFactory}
 * instance and functionality.
 * 
 * @author David Malis
 * @since 1.0
 */
public class BeanFactoryException 
	extends RuntimeException {
	
	private static final long serialVersionUID 
		= -8178486387973433720L;
	
	/**
	 * Constructs an instance with the specified message.
	 * @param message
	 */
	public BeanFactoryException(String message) {
		super(message);
	}
	/**
	 * Constructs an instance with the specified message
	 * wrapping a {@link Throwable}.
	 * @param message
	 * @param throwable
	 */
	public BeanFactoryException(String message, 
		Throwable throwable) {
		super(message, throwable);
	}

}
