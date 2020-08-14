package com.zhangzlyuyx.easy.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;

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
}
