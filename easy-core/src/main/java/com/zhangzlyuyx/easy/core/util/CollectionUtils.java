package com.zhangzlyuyx.easy.core.util;

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
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.collection.CollUtil;

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
	
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
	
	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}
	
	/**
	 * Array 转换为 list
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
	 * Collection 转换为 List
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
	 * Iterable 转换为 List
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
	 * Iterator 转换为 List
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
	 * Enumeration 转 List
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
}
