package com.zhangzlyuyx.easy.core.cache;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * 简单缓存，无超时实现，使用{@link WeakHashMap}实现缓存自动清理
 * @author zhangzlyuyx
 *
 * @param <K>
 * @param <V>
 */
public class SimpleCache<K,V> {
	
	private final Map<K, V> map = new WeakHashMap<>();
	
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	private final ReadLock readLock = readWriteLock.readLock();
	
	private final WriteLock writeLock = readWriteLock.writeLock();
	
	/**
	 * 从缓存池中查找值
	 * 
	 * @param key 键
	 * @return 值
	 */
	public V get(K key) {
		// 尝试读取缓存
		this.readLock.lock();
		V value;
		try {
			value = this.map.get(key);
		} finally {
			this.readLock.unlock();
		}
		return value;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @return 值
	 */
	public V put(K key, V value){
		this.writeLock.lock();
		try {
			this.map.put(key, value);
		} finally {
			this.writeLock.unlock();
		}
		return value;
	}

	/**
	 * 移除缓存
	 * 
	 * @param key 键
	 * @return 移除的值
	 */
	public V remove(K key) {
		this.writeLock.lock();
		try {
			return this.map.remove(key);
		} finally {
			this.writeLock.unlock();
		}
	}

	/**
	 * 清空缓存池
	 */
	public void clear() {
		this.writeLock.lock();
		try {
			this.map.clear();
		} finally {
			this.writeLock.unlock();
		}
	}
}
