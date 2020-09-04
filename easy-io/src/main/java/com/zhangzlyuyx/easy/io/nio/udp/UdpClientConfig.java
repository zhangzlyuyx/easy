package com.zhangzlyuyx.easy.io.nio.udp;

import com.zhangzlyuyx.easy.io.Node;

/**
 * 客户端配置
 * @author zhangzlyuyx
 *
 */
public class UdpClientConfig extends UdpConfig {

	private static final long serialVersionUID = -145025975055269281L;
	
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
		this.setClientNode(new Node("0.0.0.0", 0));
	}
	
	public UdpClientConfig(String serverIp, int serverPort, int timeout) {
		this.setClientNode(new Node("0.0.0.0", 0));
		this.setServerNode(new Node(serverIp, serverPort));
		this.setTimeout(timeout);
	}
}
