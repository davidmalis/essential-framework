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
import java.util.Comparator;
import org.essentialframework.core.annotation.Order;
import static org.essentialframework.core.annotation.Order.*;

/**
 * Comparator implementation that is aware of {@link Order} annotation
 * and is used to sort objects by specified precedence value.
 * <p>
 * If object is not annotated it gets the {@link Order#LOWEST_PRECEDENCE} 
 * factor.
 * 
 * @author David Mališ
 * @since 1.0 
 */
public class AnnotationBasedOrderComparator 
	implements Comparator<Object> {
	
	private static final Comparator<Object> INSTANCE 
		= new AnnotationBasedOrderComparator();
	
	public static final Comparator<Object> getInstance(){
		return INSTANCE;
	}

	/**
	 * Compares two objects by ordering precedence factor
	 * specified with {@link Order} annotation on the class
	 * level.
	 */
	@Override
	public int compare(final Object object, final Object another) {
		int o1 = getOrder(object);
		int o2 = getOrder(another);
		return (o1 < o2) ? -1 : (o1 > o2) ? 1 : 0;
	}
	
	/**
	 * If {@link Order} annotation is present on the object's class
	 * this method returns it's value, otherwise it returns 
	 * {@link #LOWEST_PRECEDENCE} value.

	 * @param object
	 * @return precedence value
	 */
	private static int getOrder(final Object object) {
		if (object != null) {
			Order order = object.getClass().getAnnotation(Order.class);
			if (order != null) {
				return order.value();
			}
		}
		return LOWEST_PRECEDENCE;
	}
}
