package com.zhangzlyuyx.easy.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import cn.hutool.core.util.URLUtil;

/**
 * URL 工具类
 *
 */
public class URLUtils {

	/**
	 * 标准化URL字符串
	 * @param url
	 * @return
	 */
	public static String normalize(String url) {
		return URLUtil.normalize(url);
	}
	
	/**
	 * url编码
	 * @param url
	 * @return
	 */
	public static String encode(String url) {
		return URLEncoder.encode(url);
	}
	
	/**
	 * url编码
	 * @param url
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String url, String charset) throws UnsupportedEncodingException {
		return URLEncoder.encode(url, charset);
	}
	
	/**
	 * url解码
	 * @param url
	 * @return
	 */
	public static String decode(String url) {
		return URLDecoder.decode(url);
	}
	
	/**
	 * url解码
	 * @param url
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String url, String charset) throws UnsupportedEncodingException {
		return URLDecoder.decode(url, charset);
	}
	
	/**
	 * 获取URL对象
	 * @param url
	 * @return
	 */
	public static URL getURL(String url) {
		return URLUtil.url(url);
	}
	
	/**
	 * 获取 url 协议
	 * @param url
	 * @return
	 */
	public static String getProtocol(String url) {
		return getURL(url).getProtocol();
	}
	
	/**
	 * 获取 url 主机名
	 * @param url
	 * @return
	 */
	public static String getHost(String url) {
		return getURL(url).getHost();
	}
	
	/**
	 * 获取 url 端口好
	 * @param url
	 * @return
	 */
	public static int getPort(String url) {
		URL u = getURL(url);
		int port = u.getPort();
		if(port == -1) {
			if(u.getProtocol().equalsIgnoreCase("https")) {
				port = 443;
			} else if(u.getProtocol().equalsIgnoreCase("http")) {
				port = 80;
			} else if(u.getProtocol().equalsIgnoreCase("ftp")) {
				port = 21;
			} else if(u.getProtocol().equalsIgnoreCase("rtsp")) {
				port = 554;
			} else if(u.getProtocol().equalsIgnoreCase("rtmp")) {
				port = 1935;
			}
		}
		return port; 
	}
	
	/**
	 * 获取 url 路径信息
	 * @param url
	 * @return
	 */
	public static String getPath(String url) {
		return getURL(url).getPath();
	}
	
	/**
	 * 获取 url 查询参数
	 * @param url
	 * @return
	 */
	public static String getQuery(String url) {
		return getURL(url).getQuery();
	}
	
	/**
	 * 获取 url 参数
	 * @param url
	 * @param name 参数名
	 * @return
	 */
	public static String getParameter(String url, String name) {
		String query = getQuery(url);
		if(query == null || query.length() == 0) {
			return null;
		}
		//补全&符号
		query = "&" + query;
		//beginIndex
		int beginIndex = query.toLowerCase().indexOf("&" + name + "=");
		if(beginIndex < 0) {
			return null;
		}
		beginIndex = beginIndex + name.length() + 2;
		//endIndex
		int endIndex = query.toLowerCase().indexOf("&", beginIndex);
		if(endIndex < 0) {
			endIndex = query.length();
		}
		//value
		String value = query.substring(beginIndex, endIndex);
		return value;
	}
	
	/**
	 * 拼接 url 参数
	 * @param url
	 * @param name 参数名
	 * @param value 参数值
	 * @return
	 */
	public static String joinParameter(String url, String name, String value) {
		StringBuilder sb = new StringBuilder(url);
		if(url.lastIndexOf("?") < 0) {
			sb.append("?");
		} else {
			sb.append("&");
		}
		sb.append(name);
		sb.append("=");
		sb.append(value);
		return sb.toString();
	}
}
