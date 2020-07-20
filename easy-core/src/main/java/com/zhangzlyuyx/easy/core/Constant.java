package com.zhangzlyuyx.easy.core;

/**
 * 常量
 * @author zhangzlyuyx
 *
 */
public class Constant {

	public static final String CHARSET_UTF_8 = "UTF-8";
	
	public static final String CHARSET_GBK = "GBK";
	
	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
	
	public static String DEFAULT_CHARSET = CHARSET_UTF_8;

	public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	
	public static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
	
	public static final String HTTP_HEADER_User_Agent = "User-Agent";
	
	public static final String HTTP_HEADER_Referer = "Referer";
	
	public static final String HTTP_HEADER_Server = "Server";
	
	public static final String HTTP_CONTENTTYPE_JSON = "application/json";
	
	public static final String HTTP_CONTENTTYPE_HTML = "text/html";
	
	public static final String HTTP_CONTENTTYPE_JS = "application/javascript";
	
	public static final String HTTP_CONTENTTYPE_CSS = "text/css";
	
	public static final String CHARACTER_BLANK = "";
	
	public static final String CHARACTER_SPACE = " ";
	
	public static final byte CHARACTER_SPACE_BYTE = ' ';
	
	public static final String CHARACTER_SLASH = "\\";
	
	public static final byte CHARACTER_SLASH_BYTE = '\\';
	
	public static final String CHARACTER_BACKSLASH = "/";
	
	public static final byte CHARACTER_BACKSLASH_BYTE = '/';

	public static final String CHARACTER_CR = "\r";
	
	public static final byte CHARACTER_CR_BYTE = '\r';
	
	public static final String CHARACTER_LF = "\n";
	
	public static final byte CHARACTER_LF_BYTE = '\n';
	
	public static final String CHARACTER_CR_LF = CHARACTER_CR + CHARACTER_LF;
	
	public static final byte[] CHARACTER_CR_LF_BYTES = new byte[] { CHARACTER_CR_BYTE, CHARACTER_LF_BYTE };
	
	public static final String CHARACTER_COL = ":";
	
	public static final byte CHARACTER_COL_BYTE = ':';
}
