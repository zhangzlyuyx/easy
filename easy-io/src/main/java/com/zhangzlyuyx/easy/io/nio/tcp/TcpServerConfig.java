package com.zhangzlyuyx.easy.io.nio.tcp;

import com.zhangzlyuyx.easy.io.Node;
import com.zhangzlyuyx.easy.io.nio.NioConfig;
import com.zhangzlyuyx.easy.io.nio.ServerNioHandler;
import com.zhangzlyuyx.easy.io.nio.ServerNioListener;

public class TcpServerConfig extends NioConfig {

	private static final long serialVersionUID = 5603335667680794278L;
	
	private static final String DEFAULT_SERVERIP = "0.0.0.0";
	
	private static final int DEFAULT_SERVERPORT = 0;
	
	private static final int DEFAULT_BACKLOG = 1024;
	
	private static final int DEFAULT_CONNECTTIMEOUT = 10000;
	
	private static final boolean DEFAULT_KEEPALIVE = true;
	
	private static final boolean DEFAULT_TCPNODELAY = true;
	
	private static final int DEFAULT_SENDBUFFERSIZE = 32 * 1024;
	
	private static final int DEFAULT_RECEIVEBUFFERSIZE = 32 * 1024;

	public TcpServerConfig() {

	}
	
	public TcpServerConfig(int serverPort) {
		this.getServerNode().setPort(serverPort);
	}
	
	public TcpServerConfig(String serverIp, int serverPort) {
		this.getServerNode().setIp(serverIp);
		this.getServerNode().setPort(serverPort);
	}
	
	private Node serverNode;
	
	public Node getServerNode() {
		if(this.serverNode == null) {
			this.serverNode = new Node(DEFAULT_SERVERIP, DEFAULT_SERVERPORT);
		}
		return this.serverNode;
	}
	
	public void setServerNode(Node serverNode) {
		this.serverNode = serverNode;
	}
	
	private ServerNioHandler serverNioHandler;
	
	public ServerNioHandler getServerNioHandler() {
		return this.serverNioHandler;
	}
	
	public void setServerNioHandler(ServerNioHandler serverNioHandler) {
		this.serverNioHandler = serverNioHandler;
	}
	
	private ServerNioListener serverNioListener;
	
	public ServerNioListener getServerNioListener() {
		return this.serverNioListener;
	}
	
	public void setServerNioListener(ServerNioListener serverNioListener) {
		this.serverNioListener = serverNioListener;
	}
	
	private int backlog = DEFAULT_BACKLOG;
	
	public int getBacklog() {
		return this.backlog;
	}
	
	public void setBacklog(int backlog) {
		this.backlog = backlog;
	}
	
	private int connectTimeout = DEFAULT_CONNECTTIMEOUT;
	
	public int getConnectTimeout() {
		return this.connectTimeout;
	}
	
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
	/**
	 * 发送数据缓冲大小
	 */
	private int sendBufferSize = DEFAULT_SENDBUFFERSIZE;
	
	public int getSendBufferSize() {
		return this.sendBufferSize;
	}
	
	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}
	
	/**
	 * 接收数据缓冲大小
	 */
	private int receiveBufferSize = DEFAULT_RECEIVEBUFFERSIZE;
	
	public int getReceiveBufferSize() {
		return this.receiveBufferSize;
	}
	
	public void setReceiveBufferSize(int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
	}
	
	/**
	 * 是否保持连接
	 */
	private boolean keepalive = DEFAULT_KEEPALIVE;
	
	public boolean isKeepalive() {
		return this.keepalive;
	}
	
	public void setKeepalive(boolean keepalive) {
		this.keepalive = keepalive;
	}
	
	/**
	 * 是否禁用Nagle算法,允许小包的发送
	 */
	private boolean tcpNodelay = DEFAULT_TCPNODELAY;
	
	public boolean isTcpNodelay() {
		return this.tcpNodelay;
	}
	
	public void setTcpNodelay(boolean tcpNodelay) {
		this.tcpNodelay = tcpNodelay;
	}
}
