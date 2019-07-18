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
package org.essentialframework.core.utility;

/**
 * Convenient helper class for assertion statements,
 * particularly useful for validating method arguments.
 * 
 * @author David Malis
 * @since 1.0
 */
public final class Assert {
	
	/**
	 * No instantiation.
	 */
	private Assert() {}
	
	/**
	 * Checks that an object is not null, 
	 * throws {@link IllegalArgumentException} otherwise.
	 * 
	 * @param object - the object to check
	 * @param message - message to be passed in the exception
	 * @throws IllegalArgumentException - if the object is null
	 */
	public static final void notNull(final Object object, final String message) {
		if(object == null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Checks that expression evaluates as true, 
	 * throws {@link IllegalStateException} otherwise.
	 * 
	 * @param expression - expression to check
	 * @param message - message to be passed in the exception
	 * @throws IllegalStateException - if the expression is not true
	 */
	public static final void state(final boolean expression, final String message) {
		if(!expression) {
			throw new IllegalStateException(message);
		}
	}
	
	/**
	 * Checks that a string is not null or empty,
	 * throws {@link IllegalArgumentException} otherwise.
	 * @param s
	 * @param message
	 * @see StringUtils#isEmpty(String)
	 */
	public static final void notEmpty(final String s, final String message) {
		if(StringUtils.isEmpty(s)) {
			return;
		}
		throw new IllegalArgumentException(message);
	}
	
	/**
	 * Checks that a string has some characters other than whitespace,
	 * throws {@link IllegalArgumentException} otherwise.
	 * @param s
	 * @param message
	 * @see StringUtils#hasText(String)
	 */
	public static final void hasText(final String s, final String message) {
		if(StringUtils.hasText(s)) {
			return;
		}
		throw new IllegalArgumentException(message);
	}

}
