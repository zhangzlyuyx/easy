package com.zhangzlyuyx.easy.spring.util;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.extra.servlet.ServletUtil;

/**
 * spring 工具类
 * @author zhangzlyux
 *
 */
public class SpringUtils {

	/**
	 * 是否为GET请求
	 * @param request
	 * @return
	 */
	public static boolean isGetMethod(HttpServletRequest request) {
		return ServletUtil.isGetMethod(request);
	}
	
	/**
	 * 是否为POST请求
	 * @param request
	 * @return
	 */
	public static boolean isPostMethod(HttpServletRequest request) {
		return ServletUtil.isPostMethod(request);
	}

	/**
	 * 获取客户端IP
	 * @param request
	 * @param headerNames 可空
	 * @return
	 */
	public static String getClientIP(HttpServletRequest request, String... headerNames) {
		return ServletUtil.getClientIP(request, headerNames);
	}
	
	/******************** begin cookie ********************/
	
	/**
	 * 获取 cookie
	 * @param request
	 * @param name cookie 名称
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		return ServletUtil.getCookie(request, name);
	}
	
	/**
	 * 获取 cookie 集合
	 * @param request
	 * @return
	 */
	public static Map<String, Cookie> getCookies(HttpServletRequest request) {
		return ServletUtil.readCookieMap(request);
	}
	
	/**
	 * 设定返回给客户端的Cookie
	 * @param response
	 * @param name cookie名称
	 * @param value cookie值
	 * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. >0 : Cookie存在的秒数.
	 * @param path 路径
	 * @param domain 域名
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds, String path, String domain) {
		ServletUtil.addCookie(response, name, value, maxAgeInSeconds, path, domain);
	}
	
	/**
	 * 设定返回给客户端的Cookie
	 * @param response
	 * @param cookie
	 */
	public static void addCookie(HttpServletResponse response, Cookie cookie) {
		ServletUtil.addCookie(response, cookie);
	}
	
	/******************** end cookie ********************/
}
