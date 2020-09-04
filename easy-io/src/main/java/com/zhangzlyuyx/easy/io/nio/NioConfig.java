package com.zhangzlyuyx.easy.io.nio;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NioConfig implements Serializable {

	private static final long serialVersionUID = 5630601387519477892L;

	/**
	 * 启动时间
	 */
	private long startTime = System.currentTimeMillis();
	
	public long getStartTime() {
		return this.startTime;
	}
	
	/**
	 * 连接
	 */
	private Map<String, ChannelContext> connections = new ConcurrentHashMap<>();
	
	public Map<String, ChannelContext> getConnections() {
		return this.connections;
	}
}
