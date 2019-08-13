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

import java.io.IOException;
import java.io.PrintWriter;

import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbException;
import javax.servlet.http.HttpServletResponse;

import org.essentialframework.core.utility.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonbResponseBodySerializingStrategy 
	implements ResponseBodySerializingStrategy {
	
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void serialize(HttpServletResponse response, Object payload) {
		
		Assert.notNull(response, "Cannot serialize payload to a null response");
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		
		if(payload != null) {

			PrintWriter writer = null;
			try {				
				writer = response.getWriter();
				writer.write(JsonbBuilder.create().toJson(payload));
				
			} catch(JsonbException e) {
				log.error("Error while serializing payload to json", e);
			} catch (IOException e) {
				log.error("Error while writing to response", e);
			} finally {
				if(writer != null) {
					writer.flush();
					writer.close();
				}
			}
		}
	}
	
}
