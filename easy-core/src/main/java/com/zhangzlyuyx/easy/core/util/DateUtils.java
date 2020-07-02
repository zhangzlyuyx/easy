package com.zhangzlyuyx.easy.core.util;

import java.util.Calendar;
import java.util.Date;

import cn.hutool.core.date.DateUtil;

/**
 * 日期工具类
 * @author zhangzlyuxy
 *
 */
public class DateUtils {
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static Date getDate() {
		return new Date();
	}

	/**
	 * 根据特定格式格式化日期
	 * @param date 被格式化的日期
	 * @param format 日期格式
	 * @return
	 */
	public static String format(Date date, String format) {
		return DateUtil.format(date, format);
	}
	
	/**
	 * 将特定格式的日期转换为Date对象
	 * @param dateStr 特定格式的日期
	 * @param format 格式，例如yyyy-MM-dd
	 * @return
	 */
	public static Date parse(String dateStr, String format){
		return DateUtil.parse(dateStr, format);
	}
	
	/**
	 * 时间增加
	 * @param date 当前时间
	 * @param field 增加的部分
	 * @param amount 增加的值
	 * @return
	 */
	public static Date add(Date date, int field, int amount){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		date = calendar.getTime();
		return date;
	}
}
