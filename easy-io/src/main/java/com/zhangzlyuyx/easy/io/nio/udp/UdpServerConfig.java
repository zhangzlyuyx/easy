package com.zhangzlyuyx.easy.io.nio.udp;

import com.zhangzlyuyx.easy.io.Node;

public class UdpServerConfig extends UdpConfig {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1312143957982713632L;

	public UdpServerConfig() {
		
	}
	
	public UdpServerConfig(String serverip, int serverport, int timeout) {
		this.setServerNode(new Node(serverip, serverport));
		this.setTimeout(timeout);
	}
}
