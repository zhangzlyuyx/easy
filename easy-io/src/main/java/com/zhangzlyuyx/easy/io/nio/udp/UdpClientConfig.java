package com.zhangzlyuyx.easy.io.nio.udp;

import com.zhangzlyuyx.easy.io.Node;

/**
 * 客户端配置
 * @author zhangzlyuyx
 *
 */
public class UdpClientConfig extends UdpConfig {

	private static final long serialVersionUID = -145025975055269281L;
	
	private static final String DEFAULT_SERVERIP = "0.0.0.0";
	
	private static final int DEFAULT_SERVERPORT = 0;
	
	/**
	 * 本地节点
	 */
	private Node clientNode;
	
	public Node getClientNode() {
		return this.clientNode;
	}
	
	public void setClientNode(Node clientNode) {
		this.clientNode = clientNode;
	}
	
	public UdpClientConfig() {
		this.setClientNode(new Node(DEFAULT_SERVERIP, DEFAULT_SERVERPORT));
	}
	
	public UdpClientConfig(int clientPort) {
		this.getClientNode().setPort(clientPort);
	}
	
	public UdpClientConfig(String serverIp, int serverPort, int timeout) {
		this.setClientNode(new Node(DEFAULT_SERVERIP, DEFAULT_SERVERPORT));
		this.setServerNode(new Node(serverIp, serverPort));
		this.setTimeout(timeout);
	}
}
