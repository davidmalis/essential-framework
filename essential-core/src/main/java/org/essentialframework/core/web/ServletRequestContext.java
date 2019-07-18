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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.essentialframework.core.utility.Assert;

/**
 * 
 * @author David Malis
 * @version 1.0
 */
public class ServletRequestContext implements RequestContext {
	
	/**
	 * 
	 */
	private volatile boolean isRequestActive = true;
	
	/**
	 * 
	 */
	private HttpServletRequest request;
	
	/**
	 * 
	 */
	private HttpServletResponse response;
	
	/**
	 * 
	 */
	private volatile HttpSession session;
	
	/**
	 * 
	 */
	private static final Set<Class<?>> IMMUTABLE_TYPES =
		Stream.of( Boolean.class, 
				   Character.class, 
				   String.class, 
				   Byte.class, 
				   Short.class, 
				   Integer.class,
				   Long.class, 
				   BigInteger.class, 
				   Float.class, 
				   Double.class, 
				   BigDecimal.class ).collect(Collectors.toSet());

	/**
	 * 
	 */
	private final Map<String, Object> sessionAttributesForUpdate
		= new ConcurrentHashMap<>(1);
	
	
	/**
	 * 
	 * @param request
	 */
	ServletRequestContext(HttpServletRequest request){
		Assert.notNull(request, "Request must not be null");
		this.request = request;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 */
	ServletRequestContext(HttpServletRequest request, HttpServletResponse response){
		this(request);
		this.response = response;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final HttpServletRequest getRequest() {
		return this.request;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final HttpServletResponse getResponse() {
		return this.response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final HttpSession getSession(boolean createIfAbsent) {
		
		if(isRequestActive) {
			session = request.getSession(createIfAbsent);

		} else {

			if(session == null) {
				
				Assert.state(!createIfAbsent, 
					"No session found on the request that has been completed. "+
					"Cannot create new session.");
				
				session = request.getSession(false);
			}
		}
		
		return session;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getRequestAttribute(String name) {

		Assert.state(isRequestActive, 
			"Cannot get request attribute from an inactive request.");
		
		return this.request.getAttribute(name);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getSessionAttribute(String name) {
		
		HttpSession session = getSession(false);
		if(session != null) {
			try {
				Object attribute = session.getAttribute(name);
				if(attribute != null) {
					this.sessionAttributesForUpdate.put(name, attribute);
				}
				return attribute;
				
				
			} catch (IllegalStateException e) {
				// TODO log(debug) invalidated session
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRequestAttribute(String name, Object value) {
		
		Assert.state(isRequestActive, 
			"Cannot set request attribute on an inactive request.");
			
		this.request.setAttribute(name, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSessionAttribute(String name, Object value) {
		
		HttpSession session = getSession(true);
		Assert.state(session != null, "There is no HttpSession");
		
		this.sessionAttributesForUpdate.remove(name);
		session.setAttribute(name, value);
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeRequestAttribute(String name) {
		if(isRequestActive) {
			this.request.removeAttribute(name);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSessionAttribute(String name) {
		
		HttpSession session = getSession(false);
		if(session != null) {
			
			this.sessionAttributesForUpdate.remove(name);

			try {
				session.removeAttribute(name);
			
			} catch (IllegalStateException ex) {
				// TODO log(debug) invalidated session
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getRequestAttributesNames() {
		
		Assert.state(isRequestActive, 
			"Cannot get request attributes on an inactive request.");
		
		return Stream.of(request.getAttributeNames()).toArray(String[]::new);
	
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getSessionAttributesNames() {
		
		HttpSession session = getSession(false);
		if(session != null) {
			try {
				return Stream.of().toArray(String[]::new);

			} catch(IllegalStateException e) {
				// TODO log(debug) invalidated session
			}
		}
		return new String[0];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSessionId() {
		
		HttpSession session = getSession(true);
		Assert.state(session != null, "There is no HttpSession");
		return session.getId();
		
	}
	
	private static boolean isImmutableType(Object object) {
		return object == null || IMMUTABLE_TYPES.contains(object.getClass());
	}
	
	private void updateSessionAttributes() {
		if(sessionAttributesForUpdate.isEmpty()) {
			
			HttpSession session = getSession(false);
			if(session != null) {
				try {
					sessionAttributesForUpdate.forEach((name, attribute)->{
						Object oldAttribute = session.getAttribute(name);
						if(oldAttribute == attribute && !isImmutableType(attribute)) {
							session.setAttribute(name, attribute);
						}
					});
				} catch (IllegalStateException e) {
					//TODO log.debug session invalidated
				}
			}
			sessionAttributesForUpdate.clear();
		}
		
	}
	
	/**
	 * 
	 */
	public void requestCompleted() {
		updateSessionAttributes();
		isRequestActive = false;
	}

}
