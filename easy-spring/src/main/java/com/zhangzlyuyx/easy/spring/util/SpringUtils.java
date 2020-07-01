package com.zhangzlyuyx.easy.spring.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.zhangzlyuyx.easy.core.util.StringUtils;
import com.zhangzlyuyx.easy.spring.Constant;

import cn.hutool.core.io.IoUtil;
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
		return getHeader(request, Constant.HEADER_USER_AGENT, true);
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
	
	/**
	 * 获取请求参数
	 * @param request
	 * @param name 参数名称
	 * @return
	 */
	public static String getParameter(HttpServletRequest request, String name) {
		String parameter = request.getParameter(name);
		if(parameter == null) {
			return parameter;
		}
		parameter = StringUtils.trim(parameter);
		return parameter;
	}
	
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
		str.append(":");
		str.append(request.getServerPort());
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
	 * 返回json成功数据给客户端
	 * @param response
	 * @param msg
	 * @param data
	 */
	public static void writeSuccess(HttpServletResponse response, String msg, Object data) {
		JSONObject json = new JSONObject();
		json.put("code", "success");
		json.put("msg", msg);
		if(data != null) {
			json.put("data", data);
		}
		writeJson(response, json.toJSONString());
	}
	
	/**
	 * 返回json失败数据给客户端
	 * @param response
	 * @param msg
	 */
	public static void writeFail(HttpServletResponse response, String msg, Object data) {
		JSONObject json = new JSONObject();
		json.put("code", "error");
		json.put("msg", msg);
		if(data != null) {
			json.put("data", data);
		}
		writeJson(response, json.toJSONString());
	}
	
	/**
	 * 返回json拒绝数据给客户端
	 * @param response
	 * @param msg
	 */
	public static void writeDenied(HttpServletResponse response, String msg, Object data) {
		JSONObject json = new JSONObject();
		json.put("code", "denied");
		json.put("msg", msg);
		if(data != null) {
			json.put("data", data);
		}
		writeJson(response, json.toJSONString());
	}
	
	/**
	 * 返回json数据给客户端
	 * @param response
	 * @param json
	 */
	public static void writeJson(HttpServletResponse response, String json) {
		write(response, json, Constant.CONTENTTYPE_JSON);
	}
	
	/**
	 * 返回数据给客户端
	 * @param response
	 * @param text 返回的内容
	 * @param contentType 返回的类型
	 */
	public static void write(HttpServletResponse response, String text, String contentType) {
		response.setCharacterEncoding(Constant.CHARACTERENCODING_UTF8);
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
