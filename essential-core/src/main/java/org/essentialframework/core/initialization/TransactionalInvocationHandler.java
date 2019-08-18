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

/**
 * TODO
 * @author David Malis
 * @since 1.0
 */
public class TransactionalInvocationHandler 
	extends AbstractTargetAwareInvocationHandler 
		implements InvocationHandler, TargetAwareProxy {

	//TransactionContext tc;
	
	/**
	 * TODO
	 * @param target
	 */
	public TransactionalInvocationHandler(Object target) {
		super(target);
	}

	/**
	 * TODO
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//boolean isTransactional = method.isAnnotationPresent(@Transaction);
		
		//if(isTransactional) {
			//tc.begin()
		//}
		
		Object result = null;
		try {
			result = method.invoke(target, args);
		} catch(Throwable t) {
			//log
			//tc.rollback()
			throw t;
		}
		//tc.commit();
		return result;
	}

}
