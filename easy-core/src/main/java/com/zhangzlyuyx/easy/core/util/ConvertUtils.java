package com.zhangzlyuyx.easy.core.util;

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
}
