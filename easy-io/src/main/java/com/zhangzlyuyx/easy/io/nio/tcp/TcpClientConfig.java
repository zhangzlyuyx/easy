package com.zhangzlyuyx.easy.io.nio.tcp;

import java.io.Serializable;

public class TcpClientConfig implements Serializable {

	private static final long serialVersionUID = -6021511457423053791L;

	private String serverIp;
	
	public String getServerIp() {
		return serverIp;
	}
	
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	private int serverPort;
	
	public int getServerPort() {
		return this.serverPort;
	}
	
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	private int reconnectDelay = 0;
	
	public int getReconnectDelay() {
		return this.reconnectDelay;
	}
	
	public void setReconnectDelay(int reconnectDelay) {
		this.reconnectDelay = reconnectDelay;
	}
}
