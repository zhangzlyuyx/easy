package com.zhangzlyuyx.easy.core.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import cn.hutool.core.util.StrUtil;

/**
 * 字符串工具类
 * @author zhangzlyuyx
 *
 */
public class StringUtils {

	/** 空字符 */
	public static final String EMPTY = "";
	/** 空JSON */
	public static final String EMPTY_JSON = "{}";
	/** 空格 */
	public static final String SPACE = " ";
	/** TAB制表符 */
	public static final String TAB = "	";
	/** 10个数字 */
	public static final String NUMBERS = "0123456789";
	/** 26个字母 */
	public static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
	
	/** HTML空格符 */
	public static final String HTML_NBSP = "&nbsp;";
	public static final String HTML_AMP = "&amp;";
	public static final String HTML_QUOTE = "&quot;";
	public static final String HTML_APOS = "&apos;";
	/** HTML < 符号*/
	public static final String HTML_LT = "&lt;";
	/** HTML > 符号 */
	public static final String HTML_GT = "&gt;";
	
	/**
	 * 字符串是否为空白
	 * @param str 被检测的字符串
	 * @return
	 */
	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}
	
	/**
	 * 判断是否为非空字符串
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(CharSequence str) {
		return !isEmpty(str);
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
	 * 判断是否为非空白字符
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(CharSequence str) {
		return !isBlank(str);
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
	 * 去除前缀
	 * @param str
	 * @param prefix
	 * @return
	 */
	public static String trimStart(CharSequence str, CharSequence prefix){
		return StrUtil.removePrefix(str, prefix);
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
	 * 去除后缀
	 * @param str
	 * @param suffix 
	 * @return
	 */
	public static String trimEnd(CharSequence str, CharSequence suffix){
		return StrUtil.removeSuffix(str, suffix);
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
	
	/**
	 * 切分字符串
	 * @param str 被切分的字符串
	 * @param separator 分隔符
	 * @return
	 */
	public static String[] split(String str, String separator) {
		return StrUtil.split(str, separator);
	}
	
	/**
	 * 字符串
	 * @param conjunction 分隔符
	 * @param objs
	 * @return
	 */
	public static String join(CharSequence conjunction, Object... objs) {
		return StrUtil.join(conjunction, objs);
	}
	
	/**
	 * 是否以指定字符串开头
	 * 
	 * @param str 被监测字符串
	 * @param prefix 开头字符串
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix) {
		return StrUtil.startWith(str, prefix);
	}
	
	/**
	 * 是否以指定字符串开头<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 * 
	 * @param str 被监测字符串
	 * @param prefix 开头字符串
	 * @param isIgnoreCase 是否忽略大小写
	 * @return 是否以指定字符串开头
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix, boolean isIgnoreCase) {
		return StrUtil.startWith(str, prefix, isIgnoreCase);
	}
	
	/**
	 * 是否以指定字符串结尾
	 * 
	 * @param str 被监测字符串
	 * @param suffix 结尾字符串
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix) {
		return StrUtil.endWith(str, suffix);
	}
	
	/**
	 * 是否以指定字符串结尾<br>
	 * 如果给定的字符串和开头字符串都为null则返回true，否则任意一个值为null返回false
	 * 
	 * @param str 被监测字符串
	 * @param suffix 结尾字符串
	 * @param isIgnoreCase 是否忽略大小写
	 * @return 是否以指定字符串结尾
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix, boolean isIgnoreCase) {
		return StrUtil.endWith(str, suffix, isIgnoreCase);
	}
	
	/**
	 * 左填充字符
	 * @param str
	 * @param minLength
	 * @param padChar
	 * @return
	 */
	public static String padLeft(CharSequence str, int minLength, char padChar){
		return StrUtil.padPre(str, minLength, padChar);
	}
	
	/**
	 * 右填充字符
	 * @param str
	 * @param minLength
	 * @param padChar
	 * @return
	 */
	public static String padRight(CharSequence str, int minLength, char padChar){
		return StrUtil.padAfter(str, minLength, padChar);
	}
	
	/**
	 * 截取字符串
	 * @param str
	 * @param fromIndex 开始的index（包括）
	 * @param toIndex 结束的index（不包括）
	 * @return
	 */
	public static String sub(CharSequence str, int fromIndex, int toIndex) {
		return StrUtil.sub(str, fromIndex, toIndex);
	}
	
	/**
	 * 截取分隔字符串之后的字符串，不包括分隔字符串
	 * @param string
	 * @param separator
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return
	 */
	public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		return StrUtil.subAfter(string, separator, isLastSeparator);
	}
	
	/**
	 * 截取分隔字符串之前的字符串，不包括分隔字符串
	 * @param string
	 * @param separator
	 * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
	 * @return
	 */
	public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		return StrUtil.subBefore(string, separator, isLastSeparator);
	}
	
	/**
	 * 截取指定字符串中间部分，不包括标识字符串
	 * @param str 被切割的字符串
	 * @param before 截取开始的字符串标识
	 * @param after 截取到的字符串标识
	 * @return 截取后的字符串
	 */
	public static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
		return StrUtil.subBetween(str, before, after);
	}
	
	/**
	 * 随机UUID
	 * @return
	 */
	public static String randomUUID() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 随机字符串
	 * @param baseStr 随机字符选取的样本
	 * @param length 字符串的长度
	 * @return 随机字符串
	 */
	public static String randomString(String baseStr, int length) {
		final StringBuilder sb = new StringBuilder();

		if (length < 1) {
			length = 1;
		}
		int baseLength = baseStr.length();
		for (int i = 0; i < length; i++) {
			int number = ThreadLocalRandom.current().nextInt(baseLength);
			sb.append(baseStr.charAt(number));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		
	}
}
