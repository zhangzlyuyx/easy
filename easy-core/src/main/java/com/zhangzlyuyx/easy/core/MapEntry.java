package com.zhangzlyuyx.easy.core;

public class MapEntry<K, V> implements java.util.Map.Entry<K, V> {

	private K key;
	
	public K getKey() {
		return this.key;
	}
	
	private V value;
	
	public V getValue() {
		return this.value;
	}

	public V setValue(V arg0) {
		return (this.value = arg0);
	}
}
