package com.zhangzlyuyx.easy.io.nio.udp;

import java.io.Serializable;

import com.zhangzlyuyx.easy.io.Node;

public class UdpConfig implements Serializable {

	private static final long serialVersionUID = 5837303906026933787L;
	
	private static final int DEFAULT_TIMEOUT = 5000;
	
	private static final String DEFAULT_CHARSET = "utf-8";

	/**
	 * 超时时间
	 */
	private int timeout = DEFAULT_TIMEOUT;
	
	public int getTimeout() {
		return this.timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * 服务端节点
	 */
	private Node serverNode;
	
	public Node getServerNode() {
		return this.serverNode;
	}
	
	public void setServerNode(Node serverNode) {
		this.serverNode = serverNode;
	}
	
	/**
	 * 字符集
	 */
	private String charset = DEFAULT_CHARSET;
	
	public String getCharset() {
		return this.charset;
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}
}
