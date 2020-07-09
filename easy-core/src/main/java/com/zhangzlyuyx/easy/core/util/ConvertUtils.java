package com.zhangzlyuyx.easy.core.util;

import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.convert.Convert;

/**
 * 类型转换工具类
 * @author zhangzlyuyx
 *
 */
public class ConvertUtils {
	
	/**
	 * 转换值为指定类型
	 * @param type 目标类型
	 * @param value 值
	 * @return
	 */
	public static <T> T convert(Class<T> type, Object value) {
		return Convert.convert(type, value);
	}

	/**
	 * byte数组转16进制串
	 * @param bytes 被转换的byte数组
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		return Convert.toHex(bytes);
	}
	
	/**
	 * Hex字符串转换为Byte值
	 * @param hex Byte字符串，每个Byte之间没有分隔符
	 * @return
	 */
	public static byte[] hexToBytes(String hex) {
		return Convert.hexToBytes(hex);
	}
	
	/**
	 * 将阿拉伯数字转为中文表达方式
	 * @param number 数字
	 * @param isUseTraditonal 是否使用繁体字（金额形式）
	 * @return
	 */
	public static String numberToChinese(double number, boolean isUseTraditonal) {
		return Convert.numberToChinese(number, isUseTraditonal);
	}
	
	/**
	 * 转换为int
	 * @param value
	 * @param defaultValue 默认值
	 * @return
	 */
	public static Integer toInteger(Object value, Integer defaultValue) {
		return Convert.toInt(value, defaultValue);
	}
	
	/**
	 * 转换为long
	 * @param value
	 * @param defaultValue 默认值
	 * @return
	 */
	public static Long toLong(Object value, Long defaultValue) {
		return Convert.toLong(value, defaultValue);
	}
	
	/**
	 * 转换为double
	 * @param value
	 * @param defaultValue 默认值
	 * @return
	 */
	public static Double toDouble(Object value, Double defaultValue) {
		return Convert.toDouble(value, defaultValue);
	}
	
	/**
	 * 转换为字符串
	 * @param value
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String toString(Object value, String defaultValue) {
		return Convert.toStr(value, defaultValue);
	}
	
	/**
	 * 转换为BigDecimal
	 * @param value
	 * @param defaultValue 默认值
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
		return Convert.toBigDecimal(value, defaultValue);
	}
	
	/**
	 * 转换为Date
	 * @param value
	 * @param defaultValue 默认值
	 * @return
	 */
	public static Date toDate(Object value, Date defaultValue) {
		return Convert.toDate(value, defaultValue);
	}
	
	/**
	 * 转换为boolean
	 * @param value
	 * @param defaultValue 默认值
	 * @return
	 */
	public static Boolean toBoolean(Object value, Boolean defaultValue) {
		return Convert.toBool(value, defaultValue);
	}
}
