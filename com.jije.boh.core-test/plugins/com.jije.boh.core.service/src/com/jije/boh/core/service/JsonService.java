package com.jije.boh.core.service;

import java.io.IOException;

import com.jije.boh.core.service.inter.IJsonService;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

@Component("jsonService")
public class JsonService implements IJsonService {
	@Override
	public String getJson(Object object) {
		return getJsonDetail(object);
	}
	
	public static String getJsonDetail(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
