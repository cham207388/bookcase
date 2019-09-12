package com.abc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
	
	@Autowired
	private Environment env;
	
	public String getHeaderString() {
		return env.getProperty("headerString");
	}

	public String getTokenSecret() {
		return env.getProperty("tokenSecret");
	}
}
