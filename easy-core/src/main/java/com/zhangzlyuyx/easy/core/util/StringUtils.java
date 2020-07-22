package com.zhangzlyuyx.easy.core.util;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 * @author zhangzlyuyx
 *
 */
public class StringUtils {

	/**
	 * 字符串是否为空白
	 * @param str 被检测的字符串
	 * @return
	 */
	public static boolean isEmpty(CharSequence str) {
		return StrUtil.isEmpty(str);
	}
	
	/**
	 * 字符串是否为空白
	 * @param str 被检测的字符串
	 * @return
	 */
	public static boolean isBlank(CharSequence str) {
		return StrUtil.isBlank(str);
	}
	
	/**
	 * 除去字符串头尾部的空白
	 * @param str 要处理的字符串
	 * @return
	 */
	public static String trim(CharSequence str) {
		return StrUtil.trim(str);
	}
	
	/**
	 * 除去字符串头部的空白
	 * @param str 要处理的字符串
	 * @return
	 */
	public static String trimStart(CharSequence str) {
		return StrUtil.trimStart(str);
	}
	
	/**
	 * 除去字符串尾部的空白
	 * @param str 要处理的字符串
	 * @return
	 */
	public static String trimEnd(CharSequence str) {
		return StrUtil.trimEnd(str);
	}
	
	/**
	 * 替换字符串中的指定字符串
	 * @param str 字符串
	 * @param searchStr 被查找的字符串
	 * @param replacement 被替换的字符串
	 * @return
	 */
	public static String replace(CharSequence str, CharSequence searchStr, CharSequence replacement) {
		return StrUtil.replace(str, searchStr, replacement);
	}
	
	/**
	 * 格式化字符串
	 * @param format
	 * @param args
	 * @return
	 */
	public static String format(String format, Object... args){
		if(format == null){
			return format;
		}
		if(format.contains("{}")){
			return StrUtil.format(format, args);
		}else if(format.contains("{0}")){
			return StrUtil.indexedFormat(format, args);
		}else{
			return String.format(format, args);
		}
	}
	
}
