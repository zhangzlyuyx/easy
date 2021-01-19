package com.zhangzlyuyx.easy.shiro;

import org.apache.shiro.web.servlet.SimpleCookie;

/**
 * shiro 会话 cookie
 *
 */
public class ShiroSessionCookie extends SimpleCookie {

	/** 默认 cookie 名称 */
	public final static String DEFAULT_COOKIE_NAME = "SESSIONID";
	
	/** 默认cookie有效时间(秒) */
	public final static int DEFAULT_COOKIE_MAXAGE = 30 * 60;
	
	public ShiroSessionCookie() {
		this(DEFAULT_COOKIE_NAME);
	}
	
	public ShiroSessionCookie(String name) {
		super.setName(name);
		super.setPath("/");
		super.setHttpOnly(true);
		super.setMaxAge(DEFAULT_COOKIE_MAXAGE);
	}
}
