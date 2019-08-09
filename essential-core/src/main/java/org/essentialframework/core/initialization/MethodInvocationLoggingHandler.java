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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 * @author David Mališ
 * @since 1.0
 */
public class MethodInvocationLoggingHandler 
	extends AbstractTargetAwareInvocationHandler implements InvocationHandler {

	private static final Logger LOGGER 
		= LoggerFactory.getLogger(MethodInvocationLoggingHandler.class);
	
	/**
	 * TODO
	 * @param target
	 */
	public MethodInvocationLoggingHandler(Object target) {
		super(target);
	}
	
	/**
	 * TODO
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		LOGGER.debug("Invoking method: {}" , method.getDeclaringClass()+"."+method.getName());
		final long start = System.currentTimeMillis();
		Object result = method.invoke(target, args);
		LOGGER.debug("Method {} finished in {} ms", 
				method.getDeclaringClass()+"."+method.getName(), (System.currentTimeMillis()-start) );
		return result;
	}

}
