package com.zhangzlyuyx.easy.core.util;

import java.math.BigDecimal;

import cn.hutool.core.util.NumberUtil;

/**
 * 数字 - 工具类
 * @author zhangzlyuyx
 *
 */
public class NumberUtils {

	/**
	 * 解析转换数字字符串为int型数字
	 * @param number
	 * @return
	 */
	public static int parseInt(String number) {
		return NumberUtil.parseInt(number);
	}
	
	/**
	 * 解析转换数字字符串为long型数字
	 * @param number
	 * @return
	 */
	public static long parseLong(String number) {
		return NumberUtil.parseLong(number);
	}
	
	/**
	 * 将指定字符串转换为Number 对象
	 * @param numberStr
	 * @return
	 */
	public static Number parseNumber(String numberStr) {
		return NumberUtil.parseNumber(numberStr);
	}
	
	/**
	 * 是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		return NumberUtil.isNumber(str);
	}
	
	/**
	 * 判断String是否是整数
	 * @param s
	 * @return
	 */
	public static boolean isInteger(String s) {
		return NumberUtil.isInteger(s);
	}
	
	/**
	 * 判断字符串是否是Long类型
	 * @param s
	 * @return
	 */
	public static boolean isLong(String s) {
		return NumberUtil.isLong(s);
	}
	
	/**
	 * 判断字符串是否是浮点数
	 * @param s
	 * @return
	 */
	public static boolean isDouble(String s) {
		return NumberUtil.isDouble(s);
	}
	
	/**
	 * (多个)数值相加
	 * @param values 数值集合
	 * @return
	 */
	public static BigDecimal add(BigDecimal... values) {
		return NumberUtil.add(values);
	}
	
	/**
	 * 数值相减
	 * @param v1 数值1
	 * @param v2 数值2
	 * @return
	 */
	public static BigDecimal subtract(BigDecimal v1, BigDecimal v2) {
		return NumberUtil.sub(v1, v2);
	}
	
	/**
	 * (多个)数值相乘
	 * @param values 数值集合
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal... values) {
		return NumberUtil.mul(values);
	}
	
	/**
	 * 数值相除
	 * @param v1 数值1
	 * @param v2 数值2
	 * @return
	 */
	public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {
		return NumberUtil.div(v1, v2);
	}
	
	/**
	 * 数值四舍五入(RoundingMode.HALF_UP)
	 * @param number 数值
	 * @param scale 保留小数位数
	 * @return
	 */
	public static BigDecimal round(BigDecimal number, int scale) {
		return NumberUtil.round(number, scale);
	}
	
	/**
	 * 数值四舍五入(RoundingMode.HALF_UP)
	 * @param v 数值
	 * @param scale 保留小数位数
	 * @return
	 */
	public static BigDecimal round(double v, int scale) {
		return NumberUtil.round(v, scale);
	}
}
