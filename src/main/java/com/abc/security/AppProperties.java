package com.abc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
	
	@Autowired
	private Environment env;
	
	public String getHeaderString() {

		return env.getProperty("header.string");
	}

	public String getTokenSecret() {
		return env.getProperty("token.secret");
	}

    public String getTokenPrefix() {
		return env.getProperty("token.prefix");
    }

	public String getHeaderEmail() {
		return env.getProperty("header.email");
	}

	public String getLoggingEmail() {
		return env.getProperty("login.email");
	}
}
