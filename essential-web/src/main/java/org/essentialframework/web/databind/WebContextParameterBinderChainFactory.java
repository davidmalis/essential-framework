/* MIT License
*
* Copyright (c) 2019 David Mališ
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
package org.essentialframework.web.databind;

public final class WebContextParameterBinderChainFactory {
	
	public static WebContextParameterBinder createDefaultChain() {
		return createChain(
			new GeneralWebContextParameterBinder(), 
			new BeanFactoryWebContextParameterBinder(), 
			new RequestParameterWebContextParameterBinder(),
			new RequestBodyWebContextParameterBinder());
	}
	
	
	@SafeVarargs
	public static <T extends AbstractWebContextParameterBinderChainlink> 
		WebContextParameterBinder createChain(T first, T... more) {
		
		if(first != null) {
			AbstractWebContextParameterBinderChainlink current = first;
			for(AbstractWebContextParameterBinderChainlink chainlink : more) {
				current = current.addNextBinder(chainlink);
			}
		}
		
		return first;
	}
	
	/**
	 * No instantiation
	 */
	private WebContextParameterBinderChainFactory() {}
	
}
