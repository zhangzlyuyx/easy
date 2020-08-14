package com.zhangzlyuyx.easy.spring.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.zhangzlyuyx.easy.core.Constant;
import com.zhangzlyuyx.easy.core.util.StringUtils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.servlet.ServletUtil;

/**
 * spring 工具类
 * @author zhangzlyux
 *
 */
public class SpringUtils {
	
	private static final Logger log = LoggerFactory.getLogger(SpringUtils.class);

	/**
	 * 获取 spring webApplicationContext
	 * @return
	 */
	public static WebApplicationContext getWebApplicationContext(ServletContext servletContext) {
		if(servletContext != null) {
			return WebApplicationContextUtils.getWebApplicationContext(servletContext);
		}
		return ContextLoader.getCurrentWebApplicationContext();
	}
	
	/**
	 * 是否为GET请求
	 * @param request
	 * @return
	 */
	public static boolean isGetMethod(ServletRequest request) {
		return ServletUtil.isGetMethod((HttpServletRequest)request);
	}
	
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
	public static boolean isPostMethod(ServletRequest request) {
		return ServletUtil.isPostMethod((HttpServletRequest)request);
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
	 * 是否为 ajax 请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		boolean ajax = false;
		String xRequestedWith = ((HttpServletRequest)request).getHeader("X-Requested-With");
		if(xRequestedWith != null && xRequestedWith.equalsIgnoreCase("XMLHttpRequest")) {
			ajax = true;
		}
		return ajax;
	}
	
	/**
	 * 是否为Multipart类型表单，此类型表单用于文件上传
	 * @param request
	 * @return
	 */
	public static boolean isMultipart(HttpServletRequest request) {
		return ServletUtil.isMultipart(request);
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
	
	/**
	 * 获取客户端UA信息
	 * @param request
	 * @return
	 */
	public static String getUserAgent(HttpServletRequest request) {
		return getHeader(request, Constant.HTTP_HEADER_User_Agent, true);
	}
	
	/******************** begin cookie ********************/
	
	/**
	 * 获取 cookie
	 * @param request
	 * @param name cookie 名称(忽略大小写)
	 * @return
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		//return ServletUtil.getCookie(request, name);
		Map<String, Cookie> cookieMap = ServletUtil.readCookieMap(request);
		if(cookieMap == null || cookieMap.size() == 0) {
			return null;
		}
		for(Entry<String, Cookie> kv : cookieMap.entrySet()) {
			if(kv.getKey().equalsIgnoreCase(name)) {
				return kv.getValue();
			}
		}
		return null;
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
	
	/**
	 * 清除给客户端的Cookie
	 * @param response
	 * @param name cookie名称
	 * @param path 路径
	 * @param domain 域名
	 */
	public static void clearCookie(HttpServletResponse response, String name, String path, String domain) {
		ServletUtil.addCookie(response, name, null, 0, path, domain);
	}
	
	/******************** end cookie ********************/
	
	/******************** begin header ********************/
	
	/**
	 * 获取 header map
	 * @param request
	 * @return
	 */
	public static Map<String, String> getHeaderMap(HttpServletRequest request) {
		Map<String, String> headers = new LinkedHashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			headers.put(headerName, request.getHeader(headerName));
		}
		return headers;
	}
	
	/**
	 * 获取请求 header
	 * @param request
	 * @param name header名称
	 * @return
	 */
	public static String getHeader(HttpServletRequest request, String name) {
		return getHeader(request, name, false);
	}
	
	/**
	 * 获取请求 header
	 * @param request
	 * @param name header名称
	 * @param ignoreCaseName 是否忽略header名称大小写
	 * @return
	 */
	public static String getHeader(HttpServletRequest request, String name, boolean ignoreCaseName) {
		String header = ignoreCaseName ? ServletUtil.getHeaderIgnoreCase(request, name) : request.getHeader(name);
		if(header == null) {
			return header;
		}
		header = StringUtils.trim(header);
		return header;
	}
	
	/**
	 * 设置响应的Header
	 * @param response
	 * @param name header名称
	 * @param value 值，可以是String，Date， int
	 */
	public static void setHeader(HttpServletResponse response, String name, Object value) {
		ServletUtil.setHeader(response, name, value);
	}
	
	/******************** end header ********************/
	
	/******************** begin parameter ********************/
	
	/**
	 * 获取 param map
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParameterMap(HttpServletRequest request) {
		Map<String, String> params = new LinkedHashMap<>();
		Enumeration<String> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			params.put(paramName, request.getParameter(paramName));
		}
		return params;
	}
	
	/**
	 * 获取请求参数
	 * @param request
	 * @param name 参数名称
	 * @return
	 */
	public static String getParameter(HttpServletRequest request, String name) {
		return getParameter(request, name, Constant.CHARSET_UTF_8);
	}
	
	/**
	 * 获取请求参数
	 * @param request
	 * @param name 参数名称
	 * @param charset 字符集
	 * @return
	 */
	public static String getParameter(HttpServletRequest request, String name, String charset) {
		String parameter = request.getParameter(name);
		if(parameter == null) {
			return parameter;
		}
		//字符集不匹配则自动转换
		if(charset != null && !charset.equalsIgnoreCase(request.getCharacterEncoding())) {
			String characterEncoding = request.getCharacterEncoding();
			try {
				parameter = new String(parameter.getBytes(characterEncoding), charset);
			} catch (Exception e) {
				log.warn(e.getMessage(), e);
			}
		}
		//清除首尾空白
		parameter = StringUtils.trim(parameter);
		return parameter;
	}
	
	/******************** end parameter ********************/
	
	/**
	 * 获得MultiPart表单文件
	 * @param request
	 * @return
	 */
	public static List<MultipartFile> getMultipartFiles(HttpServletRequest request){
		List<MultipartFile> multipartFiles = new ArrayList<MultipartFile>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		if(!multipartResolver.isMultipart(request)){
			return multipartFiles;
		}
		// 转换成多部分request
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Iterator<String> iterator = multiRequest.getFileNames();
        while (iterator.hasNext()) {
        	String name = iterator.next();
        	List<MultipartFile> files = multiRequest.getFiles(name);
        	for(MultipartFile file : files){
        		String fileName = file.getOriginalFilename();
        		if (StringUtils.isEmpty(fileName)) {
        			continue;
        		}
        		multipartFiles.add(file);
        	}
        }
        return multipartFiles;
	}
	
	/**
	 * 获取请求路径
	 * @param request
	 * @return
	 */
	public static String getRequestPath(ServletRequest request){
		StringBuilder str = new StringBuilder();
		str.append(request.getScheme());
		str.append("://");
		str.append(request.getServerName());
		if(request.getScheme().equalsIgnoreCase("http") && request.getServerPort() == 80) {
			str.append("");
		}else if(request.getScheme().equalsIgnoreCase("https") && request.getServerPort() == 443) {
			str.append("");
		}else {
			str.append(":");
			str.append(request.getServerPort());
		}
		str.append(request.getServletContext().getContextPath());
		return str.toString(); 
	}
	
	/**
	 * 获取请求 url
	 * @param request 请求对象
	 * @return
	 */
	public static String getRequestUrl(ServletRequest request) {
		// 获取当前请求的 url
		StringBuffer requestURL = ((HttpServletRequest)request).getRequestURL();
		String queryString = ((HttpServletRequest)request).getQueryString();
		if(queryString != null && queryString.length() > 0 && !queryString.contains("?")) {
			requestURL.append("?");
			requestURL.append(queryString);
		}
		return requestURL.toString();
	}
	
	/**
	 * 输出成功数据
	 * @param msg
	 * @param data
	 * @return
	 */
	public static JSONObject renderSuccess(String msg, Object data) {
		JSONObject json = new JSONObject();
		json.put("code", "success");
		json.put("msg", msg);
		if(data != null) {
			json.put("data", data);
		}
		return json;
	}
	
	/**
	 * 返回json成功数据给客户端
	 * @param response
	 * @param msg
	 * @param data
	 */
	public static void writeSuccess(HttpServletResponse response, String msg, Object data) {
		writeJson(response, renderSuccess(msg, data).toJSONString());
	}
	
	/**
	 * 输出失败数据
	 * @param msg
	 * @param data
	 * @return
	 */
	public static JSONObject renderFail(String msg, Object data) {
		JSONObject json = new JSONObject();
		json.put("code", "error");
		json.put("msg", msg);
		if(data != null) {
			json.put("data", data);
		}
		return json;
	}
	
	/**
	 * 返回json失败数据给客户端
	 * @param response
	 * @param msg
	 */
	public static void writeFail(HttpServletResponse response, String msg, Object data) {
		writeJson(response, renderFail(msg, data).toJSONString());
	}
	
	/**
	 * 输出拒绝数据
	 * @param msg
	 * @param data
	 * @return
	 */
	public static JSONObject renderDenied(String msg, Object data) {
		JSONObject json = new JSONObject();
		json.put("code", "denied");
		json.put("msg", msg);
		if(data != null) {
			json.put("data", data);
		}
		return json;
	}
	
	/**
	 * 返回json拒绝数据给客户端
	 * @param response
	 * @param msg
	 */
	public static void writeDenied(HttpServletResponse response, String msg, Object data) {
		writeJson(response, renderDenied(msg, data).toJSONString());
	}
	
	/**
	 * 返回json数据给客户端
	 * @param response
	 * @param json
	 */
	public static void writeJson(HttpServletResponse response, String json) {
		write(response, json, Constant.HTTP_CONTENTTYPE_JSON);
	}
	
	/**
	 * 返回数据给客户端
	 * @param response
	 * @param text 返回的内容
	 * @param contentType 返回的类型
	 */
	public static void write(HttpServletResponse response, String text, String contentType) {
		response.setCharacterEncoding(Constant.DEFAULT_CHARSET);
		ServletUtil.write(response, text, contentType);
	}
	
	/**
	 * 返回数据给客户端
	 * @param response
	 * @param in 需要返回客户端的内容
	 * @param contentType 返回的类型
	 * @param bufferSize 缓存大小
	 */
	public static void write(HttpServletResponse response, InputStream in, String contentType, int bufferSize) {
		if(!StringUtils.isEmpty(contentType)) {
			response.setContentType(contentType);
		}
		if(bufferSize <= 1) {
			bufferSize = IoUtil.DEFAULT_BUFFER_SIZE;
		}
		ServletUtil.write(response, in, bufferSize);
	}
}
