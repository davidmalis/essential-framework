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
