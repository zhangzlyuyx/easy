package com.zhangzlyuyx.easy.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.util.ReflectUtil;

/**
 * 反射工具类
 * @author zhangzlyuyx
 *
 */
public class ReflectUtils {
	
	private static final Logger log = LoggerFactory.getLogger(ReflectUtils.class);
	
	/**
	 * 获取 class
	 * @param className
	 * @return
	 */
	public static Class<?> getClassByName(String className){
		try {
			return Class.forName(className);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}
	
	/**
	 * 查找指定方法 如果找不到对应的方法则返回null 
	 * @param clazz
	 * @param methodName 函数名
	 * @param paramTypes 参数类型(可空)
	 * @return
	 */
	public static Method getMethodByName(Class<?> clazz, String methodName, Class<?>... paramTypes) {
		if (paramTypes == null || paramTypes.length == 0) {
			return ReflectUtil.getMethodByName(clazz, methodName);
		} else {
			return ReflectUtil.getMethod(clazz, methodName, paramTypes);
		}
	}

	/**
	 * 查找指定类中的所有字段（包括非public字段），也包括父类和Object类的字段， 字段不存在则返回null
	 * @param clazz
	 * @param name 字段名
	 * @return
	 */
	public static Field getField(Class<?> clazz, String name) {
		return ReflectUtil.getField(clazz, name);
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
	
	/**
	 * 设置字段值
	 * @param obj 对象
	 * @param name 字段名
	 * @param value 字段值
	 * @return
	 */
	public static boolean setFieldValue(Object obj, String name, Object value) {
		Field field = getField(obj.getClass(), name);
		return setFieldValue(obj, field, value);
	}
	
	/**
	 * 设置字段值
	 * @param obj 对象
	 * @param field 字段
	 * @param value 字段值
	 * @return
	 */
	public static boolean setFieldValue(Object obj, Field field, Object value) {
		if(field == null) {
			return false;
		}
		try {
			ReflectUtil.setFieldValue(obj, field, value);
			return true;
		} catch (Exception e) {
			log.error("", e);
			return false;
		}
	}
	
	/**
	 * 创建类型实例
	 * @param clazz
	 * @param params
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz, Object... params) {
		try {
			return ReflectUtil.newInstance(clazz, params);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}

	public static void main(String[] args) {

	}
}
