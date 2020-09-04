package com.zhangzlyuyx.easy.io.nio.tcp;

import java.io.Closeable;
import java.io.IOException;

import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.io.nio.ServerNioHandler;
import com.zhangzlyuyx.easy.io.nio.ServerNioListener;

import cn.hutool.core.util.EscapeUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TcpServer implements Runnable, Closeable {

	private TcpServerConfig config;
	
	public TcpServerConfig getConfig() {
		return this.config;
	}
	
	private ServerBootstrap serverBootstrap;
	
	/**
	 * 处理accept连接事件的线程，这里线程数设置为1即可，netty处理链接事件默认为单线程，过度设置反而浪费cpu资源
	 */
	private EventLoopGroup bossGroup;
	
	/**
	 * 处理hadnler的工作线程，其实也就是处理IO读写 。线程数据默认为 CPU 核心数乘以2
	 */
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private ChannelFuture channelFuture;

	public TcpServer(TcpServerConfig config) {
		//config
		this.config = config;
		//bossGroup
		this.bossGroup = new NioEventLoopGroup(1);
		//workerGroup
		this.workerGroup = new NioEventLoopGroup();
	}
	
	private ServerBootstrap createBootstrap() {
		//serverBootstrap
		final ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(this.bossGroup, this.workerGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				final ChannelPipeline pipeline = ch.pipeline();
				
				pipeline.addLast("handler", null);
				//socketChannel.pipeline().addLast(new TcpServerChannelHandler(TcpServer.this, socketChannel));
			}
		});
		serverBootstrap.option(ChannelOption.SO_BACKLOG, this.config.getBacklog());
		serverBootstrap.option(ChannelOption.SO_SNDBUF, this.config.getSendBufferSize());
		serverBootstrap.option(ChannelOption.SO_RCVBUF, this.config.getReceiveBufferSize());
		serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.config.getConnectTimeout());
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, this.config.isKeepalive());
		serverBootstrap.childOption(ChannelOption.TCP_NODELAY, this.config.isTcpNodelay());
		serverBootstrap.localAddress(this.config.getServerNode().getIp(), this.config.getServerNode().getPort());
		return serverBootstrap;
	}
	
	@Override
	public void run() {
		this.serverBootstrap = this.createBootstrap();
		try {
			//绑定监听端口
			this.channelFuture = this.serverBootstrap.bind().sync();
			
			if(this.channelFuture.isSuccess()) {
				return;
			} else {
				this.channelFuture.cause().printStackTrace();
				this.bossGroup.shutdownGracefully();
				this.workerGroup.shutdownGracefully();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() throws IOException {
		try {
			this.workerGroup.shutdownGracefully();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.bossGroup.shutdownGracefully();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		TcpServerConfig config = new TcpServerConfig(81);
		TcpServer server = new TcpServer(config);
		server.run();
	}
}
