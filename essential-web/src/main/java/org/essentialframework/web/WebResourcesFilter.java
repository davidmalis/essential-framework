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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
		final Path resourcePath = getRequestedResourcePath(httpRequest);
		
		final URL resource = this.classLoader.getResource(resourcePath.toString());
		if(resource == null) {
			if( LOGGER.isDebugEnabled() ) {
				LOGGER.debug("Cannot find '{}' amongst web resources. "
					+ "Letting request further down the filter chain.", resource);
			}
			chain.doFilter(request, response);
			return;
		}
		
		
		if(isTextualWebResource(resourcePath)) {
			
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
				
			}
			
		} else {
			
			try ( InputStream in = resource.openStream();
					OutputStream out = response.getOutputStream() ){
				
				byte[] buffer = new byte[1024];
				int length;
				while((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}
				out.flush();
			}
			
		}
		
		if( LOGGER.isDebugEnabled() ) {
			LOGGER.debug("Served '{}' from "+ (isStatic(resource)?"static":"") 
					+" web resources.", resource);
		}
		
	}
	
	private static Path getRequestedResourcePath(HttpServletRequest request) {
		return Paths.get(request.getPathInfo().replaceAll("^/+", ""));
	}
	
	private static boolean isStatic(URL resource) {
		return resource.toString().contains(STATIC);
	}
	
	private static boolean isTextualWebResource(Path path) throws IOException {
		String contentType = Files.probeContentType(path);
		
		if(contentType != null) {
			return contentType.startsWith("text/") || 
					contentType.contains("javascript") ||
					contentType.contains("json") ||
					contentType.contains("xml") || 
					contentType.contains("rtf") || 
					contentType.contains("css");
			
		} else {
			//fallback to simple extension finding algorithm
			String str = path.toString();
			int separatorIndex = str.lastIndexOf(File.separator);
			if(separatorIndex != -1) {
				str = str.substring(separatorIndex+1);
			} 
			int extensionIndex = str.lastIndexOf(".");
			if(extensionIndex != -1) {
				str = str.substring(extensionIndex+1);
			}
			return Arrays.asList("txt", "html", "js", "css", "json", "xml", "md")
					.contains(str);
		}
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
