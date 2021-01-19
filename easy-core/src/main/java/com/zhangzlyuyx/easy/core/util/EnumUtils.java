package com.zhangzlyuyx.easy.core.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举工具类
 * @author zhangzlyuyx
 *
 */
public class EnumUtils {
	
	/**
	 * 判断类型是否为枚举类
	 *
	 * @param clazz 类
	 * @return
	 */
	public static boolean isEnum(Class<?> clazz) {
		return clazz != null && clazz.isEnum();
	}
	
	/**
	 * 判断对象是否为枚举类
	 *
	 * @param obj 类
	 * @return
	 */
	public static boolean isEnum(Object obj) {
		return obj != null && obj.getClass().isEnum();
	}
	
	/**
	 * 获取枚举列表
	 * @param clazz 枚举类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> List<E> getEnumList(Class<? extends Enum<E>> clazz) {
		final Enum<?>[] enums = clazz.getEnumConstants();
		final List<E> list = new ArrayList<>(enums.length);
		for(Enum<?> e : enums) {
			list.add((E)e);
		}
		return list;
	}
	
	/**
	 * 获取枚举map集合
	 * @param clazz 枚举类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<? extends Enum<E>> clazz) {
		final Enum<?>[] enums = clazz.getEnumConstants();
		final Map<String, E> map = new LinkedHashMap<>(enums.length);
		for(Enum<?> e : enums) {
			map.put(e.name(), (E)e);
		}
		return map;
	}
	
	/**
	 * 获取枚举名称集合
	 * @param clazz 枚举类
	 * @return
	 */
	public static <E extends Enum<E>> String[] getEnumNames(Class<? extends Enum<E>> clazz) {
		final Enum<?>[] enums = clazz.getEnumConstants();
		final String[] names = new String[enums.length];
		for(int i = 0; i < enums.length; i++) {
			names[i] = enums[i].name();
		}
		return names;
	}
}
