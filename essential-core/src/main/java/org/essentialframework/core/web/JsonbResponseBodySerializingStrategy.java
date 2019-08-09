package org.essentialframework.core.web;

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
