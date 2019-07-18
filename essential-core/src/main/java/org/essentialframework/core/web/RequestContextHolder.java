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
package org.essentialframework.core.web;
import org.essentialframework.core.utility.Assert;

/**
 * TODO
 * @author David Malis
 * @version 1.0
 */
public class RequestContextHolder {
	
	/**
	 * 
	 */
	private static final ThreadLocal<RequestContext> HOLDER =
		new InheritableThreadLocal<>();
	

	/**
	 * 
	 */
	public static void reset() {
		HOLDER.remove();
	}
	
	/**
	 * 
	 * @param context
	 */
	public static void setRequestContext(RequestContext context) {
		if(context == null) {
			reset();
		} else {
			HOLDER.set(context);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static RequestContext getRequestContext() {
		return HOLDER.get();
	}
	
	/**
	 * 
	 * @return
	 */
	public static RequestContext currentRequestContext() {
		RequestContext context = getRequestContext();
		Assert.state(context != null, "No thread-bound request context found.");
		return context;
	}

}
