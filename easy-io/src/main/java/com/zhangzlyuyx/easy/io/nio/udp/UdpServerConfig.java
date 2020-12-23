package com.zhangzlyuyx.easy.io.nio.udp;

import com.zhangzlyuyx.easy.io.Node;

public class UdpServerConfig extends UdpConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1312143957982713632L;

	private static final String DEFAULT_SERVERIP = "0.0.0.0";
	
	private static final int DEFAULT_SERVERPORT = 0;
	
	public UdpServerConfig() {
		this.setServerNode(new Node(DEFAULT_SERVERIP, DEFAULT_SERVERPORT));
	}
	
	public UdpServerConfig(int serverport) {
		this.setServerNode(new Node(DEFAULT_SERVERIP, serverport));
	}
	
	public UdpServerConfig(String serverip, int serverport, int timeout) {
		this.setServerNode(new Node(serverip, serverport));
		this.setTimeout(timeout);
	}
}
