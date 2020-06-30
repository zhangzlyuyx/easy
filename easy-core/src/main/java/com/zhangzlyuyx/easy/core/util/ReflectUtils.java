package com.zhangzlyuyx.easy.core.util;

import java.lang.reflect.Field;

import cn.hutool.core.util.ReflectUtil;

/**
 * 反射工具类
 * @author zhangzlyuyx
 *
 */
public class ReflectUtils {

	/**
	 * 查找指定类中的所有字段（包括非public字段），也包括父类和Object类的字段， 字段不存在则返回null
	 * @param classz
	 * @param name 字段名
	 * @return
	 */
	public static Field getField(Class<?> classz, String name) {
		return ReflectUtil.getField(classz, name);
	}
	
	/**
	 * 获取字段值
	 * @param obj 对象
	 * @param field 字段
	 * @return
	 */
	public static Object getFieldValue(Object obj, Field field) {
		return ReflectUtil.getFieldValue(obj, field);
	}
	
	/**
	 * 获取字段值
	 * @param obj  对象
	 * @param name 字段名
	 * @return
	 */
	public static Object getFieldValue(Object obj, String name) {
		return ReflectUtil.getFieldValue(obj, name);
	}
}
