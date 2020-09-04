package com.zhangzlyuyx.easy.io.nio.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpServer implements Runnable {

	private UdpServerConfig config;
	
	private Bootstrap bootstrap;
	
	private EventLoopGroup workGroup = new NioEventLoopGroup();
	
	public UdpServer(UdpServerConfig config) {
		this.config = config;
	}
	
	public Bootstrap createBootstrap(EventLoopGroup eventLoopGroup) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup);
		bootstrap.channel(NioDatagramChannel.class);
		//bootstrap.handler(this.serverChannelHandler);
		bootstrap.option(ChannelOption.SO_BROADCAST, true);
		return bootstrap;
	}

	@Override
	public void run() {
		this.bootstrap = this.createBootstrap(this.workGroup);
		
	}
}
