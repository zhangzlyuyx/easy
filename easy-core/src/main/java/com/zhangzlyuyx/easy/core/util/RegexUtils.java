package com.zhangzlyuyx.easy.core.util;

import java.util.regex.Pattern;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ReUtil;

/**
 * 正则 -工具类
 * @author zhangzlyuyx
 *
 */
public class RegexUtils {

	/**
	 * 获得匹配的字符串，对应分组0表示整个匹配内容，1表示第一个括号分组内容，依次类推
	 * @param pattern 编译后的正则模式
	 * @param content 被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return
	 */
	public static String get(Pattern pattern, CharSequence content, int groupIndex) {
		return ReUtil.get(pattern, content, groupIndex);
	}
	
	/**
	 * 获得匹配的字符串
	 * @param regex 匹配的正则
	 * @param content 被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return
	 */
	public static String get(String regex, CharSequence content, int groupIndex) {
		return ReUtil.get(regex, content, groupIndex);
	}
	
	/**
	 * 验证是否为可用邮箱地址
	 * @param content
	 * @return
	 */
	public boolean isEmail(String content) {
		return Validator.isEmail(content);
	}
	
	/**
	 * 验证是否为出生日期
	 * @param content
	 * @return
	 */
	public boolean isBirthday(String content) {
		return Validator.isBirthday(content);
	}
	
	/**
	 * 验证是否为身份证号码（18位中国）
	 * @param content
	 * @return
	 */
	public boolean isCitizenId(String content) {
		return Validator.isCitizenId(content);
	}
	
	/**
	 * 验证是否为邮政编码（中国）
	 * @param content
	 * @return
	 */
	public boolean isZipCode(String content) {
		return Validator.isZipCode(content);
	}
	
	/**
	 * 验证是否为给定长度范围的英文字母 、数字和下划线
	 * @param content
	 * @param min 最小长度，负数自动识别为0
	 * @param max 最大长度，0或负数表示不限制最大长度
	 * @return
	 */
	public boolean isGeneral(String content, int min, int max) {
		return Validator.isGeneral(content, min, max);
	}
	
	/**
	 * 验证该字符串是否是数字
	 * @param content
	 * @return
	 */
	public boolean isNumber(String content) {
		return Validator.isNumber(content);
	}
	
	/**
	 * 验证是否为汉字
	 * @param content
	 * @return
	 */
	public boolean isChinese(String content) {
		return Validator.isChinese(content);
	}
	
}
