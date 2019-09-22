package com.abc.security;

import com.abc.SpringApplicationContext;

public class SecurityConstants {
	
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public final static String SWAGGER_API = "/v2/**";
	public final static String SWAGGER_UI = "/swagger-ui.html**";
	public final static String SAVE_AUTHOR = "/author";
	public final static String SAVE_AUTHORS = "/authors";
	public final static String GET_AUTHORS = "/authors";
	public final static String SHUTDOWN = "/shutdownContext";
	
	static AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("appProperties");
	
	public static String getHeaderString() {
		return appProperties.getHeaderString();
	}
	public static String getTokenSecret() {
		return appProperties.getTokenSecret();
	}
	public static String getTokenPrefix() {
		return appProperties.getTokenPrefix();
	}
	public static String getHeaderEmail() {
		return appProperties.getHeaderEmail();
	}
	public static String getLoggingEmail() {
		return appProperties.getLoggingEmail();
	}

}