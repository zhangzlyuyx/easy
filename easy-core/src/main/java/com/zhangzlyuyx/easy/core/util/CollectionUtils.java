package com.zhangzlyuyx.easy.core.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CollectionUtils {
	
	public static <T> HashSet<T> newHashSet() {
		return new HashSet<T>();
	}
	
	public static <T> LinkedHashSet<T> newLinkedHashSet() {
		return new LinkedHashSet<T>();
	}
	
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}
	
	public static <K, V> HashMap<K, V> newLinkedHashMap(){
		return new LinkedHashMap<K, V>();
	}
	
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(){
		return new ConcurrentHashMap<K, V>();
	}
	
	public static <T> ArrayList<T> newArrayList() {
		return new ArrayList<T>();
	}
	
	public static <T> LinkedList<T> newLinkedList() {
		return new LinkedList<T>();
	}
	
	/**
	 * 新建一个空数组
	 * @param componentType 元素类型
	 * @param newSize 大小
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] newArray(Class<?> componentType, int newSize) {
		return (T[])Array.newInstance(componentType, newSize);
	}
	
	/**
	 * 新建一个空的 {@link List}
	 * @param componentType
	 * @param newSize
	 * @return
	 */
	public static <T> List<T> newList(Class<?> componentType, int newSize){
		return new ArrayList<T>(newSize);
	}
	
	/**
	 * 判断集合是否为空
	 * @param c
	 * @return
	 */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }
	
    /**
     * 判断集合是否为非空
     * @param c
     * @return
     */
    public static boolean isNotEmpty(Collection<?> c) {
        return c != null && c.size() != 0;
    }
	
    /**
     * 判断Map是否为空
     * @param m
     * @return
     */
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
    /**
     * 判断Map是否为非空
     * @param m
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> m) {
        return m != null && m.size() != 0;
    }
    
    /**
     * 判断数组是否为空
     * @param array
     * @return
     */
    @SafeVarargs
    public static <T> boolean isEmpty(T... array) {
    	return array == null || array.length == 0;
    }
    
    /**
     * 判断数组是否为非空
     * @param array
     * @return
     */
    @SafeVarargs
	public static <T> boolean isNotEmpty(T... array) {
    	return array != null && array.length != 0;
    }
    
    /**
     * 判断List是否为空
     * @param list
     * @return
     */
    public static boolean isEmpty(List<?> list) {
    	return list == null || list.size() == 0;
	}
    
	/**
	 * 判断List是否为非空
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @return
	 */
	public static boolean isNotEmpty(List<?> list) {
		return list != null && list.size() != 0;
	}
	
	/**
	 * 判断 iterable 是否为空
	 * @param iterable
	 * @return
	 */
	public static boolean isEmpty(Iterable<?> iterable) {
		return iterable == null || isEmpty(iterable.iterator());
	}
	
	/**
	 * 判断 iterable 是否为非空
	 * @param iterable
	 * @return
	 */
	public static boolean isNotEmpty(Iterable<?> iterable) {
		return iterable != null && isNotEmpty(iterable.iterator());
	}
	
	/**
	 * 判断 Iterator 是否为空
	 * @param iterator
	 * @return
	 */
	public static boolean isEmpty(Iterator<?> iterator) {
		return iterator == null || !iterator.hasNext();
	}
	
	/**
	 * 判断 Iterator 是否为非空
	 * @param iterator
	 * @return
	 */
	public static boolean isNotEmpty(Iterator<?> iterator) {
		return iterator != null && iterator.hasNext();
	}
	
	/**
	 * 判断 {@link Set} 是否为空
	 * @param set
	 * @return
	 */
	public static boolean isEmpty(Set<?> set) {
		return set == null || set.size() == 0;
	}
	
	/**
	 * 判断 {@link Set} 是否为非空
	 * @param set
	 * @return
	 */
	public static boolean isNotEmpty(Set<?> set) {
		return set != null && set.size() != 0;
	}
	
    /**
     * 获取 {@link Collection} 元素数量
     * @param c
     * @return
     */
    public static int size(Collection<?> c) {
        return c != null ? c.size() : 0;
    }
    
    /**
     * 获取 {@link Map} 元素数量
     * @param m
     * @return
     */
    public static int size(Map<?, ?> m) {
        return m != null ? m.size() : 0;
    }
    
    /**
	 * 获取 {@link List} 元素数量
	 * @param list
	 * @return
	 */
	public static int size(List<?> list) {
		return list != null ? list.size() : 0;
	}
	
	/**
	 * 获取数组元素数量
	 * @param array
	 * @return
	 */
	@SafeVarargs
	public static <T> int size(T... array) {
		return array != null  ? array.length : 0;
	}
	
	/**
	 * 获取 {@link Set} 元素数量
	 * @param set
	 * @return
	 */
	public static int size(Set<?> set) {
		return set != null ? set.size() : 0;
	}
	
	/**
	 * array 转换为 {@link List}
	 * @param array
	 * @return
	 */
	@SafeVarargs
	public static <T> List<T> toList(T... array) {
		if(array == null || array.length == 0) {
			return new ArrayList<>();
		}
		List<T> list = new ArrayList<>(array.length);
		for(T item : array) {
			list.add(item);
		}
		return list;
	}
	
	/**
	 * array 转换为 {@link List}
	 * @param array
	 * @return
	 */
	@SafeVarargs
	public static <T> List<T> asList(T... array) {
		return toList(array);
	}
	
	/**
	 * {@link Collection} 转换为 {@link List}
	 * @param collection
	 * @return
	 */
	public static <T> List<T> toList(Collection<T> collection) {
		if(collection == null || collection.size() == 0) {
			return new ArrayList<>();
		}
		List<T> list = new ArrayList<>(collection.size());
		list.addAll(collection);
		return list;
	}
	
	/**
	 * {@link Iterable} 转换为 {@link List}
	 * @param iterable
	 * @return
	 */
	public static <T> List<T> toList(Iterable<T> iterable) {
		if(iterable == null) {
			return new ArrayList<>();
		}
		return toList(iterable.iterator());
	}
	
	/**
	 * {@link Iterator} 转换为 {@link List}
	 * @param iter
	 * @return
	 */
	public static <T> List<T> toList(Iterator<T> iterator) {
		if(iterator == null) {
			return new ArrayList<>();
		}
		List<T> list = new ArrayList<>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		return list;
	}
	
	/**
	 * {@link Enumeration} 转 {@link List}
	 * @param enumration
	 * @return
	 */
	public static <T> List<T> toList(Enumeration<T> enumration) {
		if(enumration == null) {
			return new ArrayList<>();
		}
		List<T> list = new ArrayList<>();
		while (enumration.hasMoreElements()) {
			list.add(enumration.nextElement());
		}
		return list;
	}
	
	/**
	 * {@link Set} 转换为 {@link List}
	 * @param set
	 * @return
	 */
	public static <T> List<T> toList(Set<T> set) {
		if(set == null || set.size() == 0) {
			return new ArrayList<>();
		}
		List<T> list = new ArrayList<>(set.size());
		for(T item : set) {
			list.add(item);
		}
		return list;
	}
	
	/**
	 * {@link List} 转换为数组
	 * @param list
	 * @param componentType
	 * @return
	 */
	public static <T> T[] toArray(List<T> list, Class<T> componentType) {
		if(list == null) {
			list = new ArrayList<>();
		}
		final T[] array = newArray(componentType, list.size());
		return list.toArray(array);
	}
	
	/**
	 * {@link Set} 转换为数组
	 * @param set
	 * @param componentType
	 * @return
	 */
	public static <T> T[] toArray(Set<T> set, Class<T> componentType) {
		if(set == null) {
			set = new HashSet<>();
		}
		final T[] array = newArray(componentType, set.size());
		return set.toArray(array);
	}
}
