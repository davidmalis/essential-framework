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

import java.io.BufferedReader;
import java.io.IOException;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.servlet.http.HttpServletRequest;

import org.essentialframework.core.utility.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonbRequestBodyDeserializingStrategy 
	implements RequestBodyDeserializingStrategy {

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public <T> T deserialize(HttpServletRequest request, Class<T> type) {
		Assert.notNull(request, "Cannot deserialize payload from a null request");
		
		T result = null;
		
		BufferedReader reader = null;
		try {
			reader = request.getReader();
			if(reader != null && reader.ready()) {
				result = JsonbBuilder.create().fromJson(reader, 
						type == null ? Object.class : type);
			}
			
		} catch(JsonbException e) {
			log.error("Error while deserializing payload from json", e);
		} catch(IOException e) {
			log.error("Error while reading from request");
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error("Error while closing reader", e);
				}
			}
		}
		
		return result;
	
	}
	
	
	
	
	
}
