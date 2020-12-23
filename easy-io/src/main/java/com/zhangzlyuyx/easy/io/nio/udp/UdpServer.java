package com.zhangzlyuyx.easy.io.nio.udp;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.io.nio.tcp.TcpServer;
import com.zhangzlyuyx.easy.io.nio.tcp.TcpServerConfig;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpServer implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(UdpServer.class);
			
	private UdpServerConfig config;
	
	private Bootstrap bootstrap;
	
	private EventLoopGroup workGroup = new NioEventLoopGroup();
	
	/**
	 * channelHandlers
	 */
	private List<ChannelHandler> channelHandlers;
	
	public List<ChannelHandler> getChannelHandlers() {
		if(this.channelHandlers != null) {
			this.channelHandlers = new ArrayList<>();
		}
		return this.channelHandlers;
	}
	
	private ChannelFuture channelFuture;
	
	public UdpServer(UdpServerConfig config) {
		this.config = config;
	}
	
	/**
	 * createBootstrap
	 * @param eventLoopGroup
	 * @return
	 */
	public Bootstrap createBootstrap(EventLoopGroup eventLoopGroup) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup);
		bootstrap.channel(NioDatagramChannel.class);
		bootstrap.handler(new ChannelInitializer<NioDatagramChannel>() {
			@Override
			protected void initChannel(NioDatagramChannel ch) throws Exception {
				final ChannelPipeline pipeline = ch.pipeline();
				for(ChannelHandler channelHandler : getChannelHandlers()) {
					pipeline.addLast(channelHandler);
				}
			}
		});
		bootstrap.option(ChannelOption.SO_BROADCAST, true);
		bootstrap.localAddress(this.config.getServerNode().getIp(), this.config.getServerNode().getPort());
		return bootstrap;
	}

	@Override
	public void run() {
		this.bootstrap = this.createBootstrap(this.workGroup);
		try {
			this.channelFuture = this.bootstrap.bind().sync();
			
			log.info("udp port {} is running.", this.config.getServerNode().getPort());
			
			if(this.channelFuture.isSuccess()) {
				return;
			} else {
				this.channelFuture.cause().printStackTrace();
				this.workGroup.shutdownGracefully();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			this.workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		
		UdpServerConfig config = new UdpServerConfig(81);
		UdpServer server = new UdpServer(config);
		server.run();
	}
}
