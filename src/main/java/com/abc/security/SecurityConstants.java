package com.abc.security;

public class SecurityConstants {
	
	public static final long EXPIRATION_TIME = 864000000; // 10 days
	public final static String POST_URL = "/author";
	public final static String POST_ALL_URL = "/authors";
	public final static String GET_URL = "/author";
	public final static String DELETE_URL = "/author";
	public final static String PUT_URL = "/author";
	public final static String GET_ALL_URL = "/authors";
	public final static String CONSOLE_URL = "/h2-console";
	
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String TOKEN_SECRET = "jfs4a90sdax83";
	
}
