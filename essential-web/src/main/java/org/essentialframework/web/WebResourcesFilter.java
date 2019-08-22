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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.essentialframework.web.utility.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebResourcesFilter implements Filter {
	
	private static final Logger LOGGER =
		LoggerFactory.getLogger(WebResourcesFilter.class);
	
	private static final String STATIC = "static";
	
	private ClassLoader classLoader = WebResourcesFilter.class.getClassLoader();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		
		final URL resource = this.classLoader.getResource(getRequestedResourcePath(httpRequest));
		
		if(resource != null) {
			try ( BufferedReader in = new BufferedReader(new InputStreamReader(
					resource.openStream(), StandardCharsets.UTF_8));
				  PrintWriter out = response.getWriter() ) {

				for(String line;(line=in.readLine())!= null;) {

					if(!isStatic(resource)) {
						line = new BaseUrlVariableProcessor(httpRequest).process(line);
					}
				   
					out.println(line);
				}
				
				out.flush();
				
				if( LOGGER.isDebugEnabled() ) {
					LOGGER.debug("Served '{}' from "+ (isStatic(resource)?"static":"") 
							+" static web resources.", resource);
				}
			}
			
		} else {
			if( LOGGER.isDebugEnabled() ) {
				LOGGER.debug("Cannot find '{}' amongst web resources. "
					+ "Letting request further down the filter chain.", resource);
			}
			chain.doFilter(request, response);
		}
		
	}
	
	private static String getRequestedResourcePath(HttpServletRequest request) {
		return request.getPathInfo().replaceAll("^/+", "");
	}
	
	private static boolean isStatic(URL resource) {
		return resource.toString().contains(STATIC);
	}
	
	/**
	 * Simple variable injector.
	 * 
	 * Replaces all occurences of ${base.url} in the non-static web resource
	 * with the actual value determined from the HttpServletRequest.
	 */
	private static class BaseUrlVariableProcessor {

		private HttpServletRequest request;
		
		private BaseUrlVariableProcessor(HttpServletRequest request) {
			this.request = request;
		}
		
		private String process(String line) {
			if(line != null) {
				line = line.replaceAll(Pattern.quote("${base.url}"), 
					WebUtils.getBaseUrl(this.request));
			}
			return line;
		}
		
	}

}
