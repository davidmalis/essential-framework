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
package org.essentialframework.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticResourcesFilter implements Filter {
	
	private static final Logger LOGGER =
		LoggerFactory.getLogger(StaticResourcesFilter.class);
	
	private ClassLoader classLoader = StaticResourcesFilter.class.getClassLoader();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final String path = getRequestedResourcePath(request);
		
		try ( InputStream resource = this.classLoader.getResourceAsStream(path);
			  OutputStream output = response.getOutputStream(); ){
			
			if(resource != null && output != null) {
				byte[] buffer = new byte[1024];
			    int length;
			    while((length = resource.read(buffer)) > 0){
			    	output.write(buffer, 0, length);
			    }
			    output.flush();
			    
			    if( LOGGER.isDebugEnabled() ) {
					LOGGER.debug("Served '{}' from static resources.	", path);
				}
			    
			} else {
				if( LOGGER.isDebugEnabled() ) {
					LOGGER.debug("Cannot find '{}' amongst static resources. "
						+ "Letting request further down the filter chain.", path);
				}
				
				chain.doFilter(request, response);
			}
			
		} catch(IOException e) {
			LOGGER.error("Error while loading static resource", e);
			((HttpServletResponse) response).sendError(500, "Error while loading resource.");
		}
	}
	
	private static String getRequestedResourcePath(ServletRequest request) {
		return ((HttpServletRequest) request).getPathInfo().replaceAll("^/+", "");
	}

}
