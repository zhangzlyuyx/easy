package com.zhangzlyuyx.easy.io;

import java.io.Serializable;

public class Node implements Serializable {

	private static final long serialVersionUID = 7469804125708679017L;

	/**
	 * ip
	 */
	private String ip;
	
	public String getIp() {
		return this.ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * 端口号
	 */
	private int port;
	
	public int getPort() {
		return this.port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public Node() {
		
	}
	
	public Node(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(ip).append(":").append(port);
		return builder.toString();
	}
}
