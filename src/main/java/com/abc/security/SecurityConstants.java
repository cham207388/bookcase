package com.abc.security;

import com.abc.SpringApplicationContext;

public class SecurityConstants {
	
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public final static String POST_URL = "/author";
	public final static String POST_ALL_URL = "/authors";
	public final static String GET_URL = "/author";
	public final static String DELETE_URL = "/author";
	public final static String PUT_URL = "/author";
	public final static String GET_ALL_URL = "/authors";
	public final static String CONSOLE_URL = "/h2-console";
	public final static String SHUTDOWN = "/shutdownContext";
	public static final String TOKEN_PREFIX = "Bearer ";
	
	static AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
	
	public static String getHeaderString() {
		return appProperties.getHeaderString();
	}
	public static String getTokenSecret() {
		return appProperties.getTokenSecret();
	}	
}