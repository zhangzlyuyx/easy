package com.zhangzlyuyx.easy.io.nio.tcp;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * nio tcp 服务器
 *
 */
public class TcpServer implements Runnable, Closeable {
	
	private static final Logger log = LoggerFactory.getLogger(TcpServer.class);

	/**
	 * tcpServerConfig
	 */
	private TcpServerConfig config;
	
	/**
	 * 获取 tcp 服务器配置
	 * @return
	 */
	public TcpServerConfig getConfig() {
		return this.config;
	}
	
	/**
	 * serverBootstrap
	 */
	private ServerBootstrap serverBootstrap;
	
	/**
	 * 处理accept连接事件的线程，这里线程数设置为1即可，netty处理链接事件默认为单线程，过度设置反而浪费cpu资源
	 */
	private EventLoopGroup bossGroup;
	
	/**
	 * 处理hadnler的工作线程，其实也就是处理IO读写 。线程数据默认为 CPU 核心数乘以2
	 */
	private EventLoopGroup workerGroup;
	
	/**
	 * channelHandlers
	 */
	private List<ChannelHandler> channelHandlers;
	
	/**
	 * 获取 channelHandlers 
	 * @return
	 */
	public List<ChannelHandler> getChannelHandlers() {
		if(this.channelHandlers == null) {
			this.channelHandlers = new ArrayList<>();
		}
		return this.channelHandlers;
	}
	
	/**
	 * channelFuture
	 */
	private ChannelFuture channelFuture;

	/**
	 * TcpServer
	 * @param config
	 */
	public TcpServer(TcpServerConfig config) {
		//config
		this.config = config;
	}
	
	@Override
	public void run() {
		
		//shutdown
		this.shutdown();
		
		//bossGroup
		this.bossGroup = new NioEventLoopGroup(this.config.getBossGroupThreads());
		
		//workerGroup
		this.workerGroup = new NioEventLoopGroup(this.config.getWorkerGroupThreads());
		
		//serverBootstrap
		this.serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(this.bossGroup, this.workerGroup);
		serverBootstrap.channel(NioServerSocketChannel.class);
		if(this.config.getLogLevel() != null) {
			serverBootstrap.handler(new LoggingHandler(this.config.getLogLevel()));
		}
		serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
			
			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {
				
				final ChannelPipeline pipeline = ch.pipeline();
				
				//IdleStateHandler
				if(config.getIdleStateReaderTimeout() > 0 || config.getIdleStateWriterTimeout() > 0) {
					pipeline.addLast(new IdleStateHandler(config.getIdleStateReaderTimeout(), config.getIdleStateWriterTimeout(), 0));
				}
				
				for(ChannelHandler channelHandler : getChannelHandlers()) {
					pipeline.addLast(channelHandler);
				}
			}
		});
		serverBootstrap.option(ChannelOption.SO_BACKLOG, this.config.getBacklog());
		serverBootstrap.option(ChannelOption.SO_SNDBUF, this.config.getSendBufferSize());
		serverBootstrap.option(ChannelOption.SO_RCVBUF, this.config.getReceiveBufferSize());
		serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.config.getConnectTimeout());
		serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, this.config.isKeepalive());
		serverBootstrap.childOption(ChannelOption.TCP_NODELAY, this.config.isTcpNodelay());
		serverBootstrap.localAddress(this.config.getServerNode().getIp(), this.config.getServerNode().getPort());
		
		try {
			//绑定监听端口
			this.channelFuture = this.serverBootstrap.bind().sync();
			
			log.debug("tcp port {} is running.", this.config.getServerNode().getPort());
			
			if(this.channelFuture.isSuccess()) {
				return;
			} else {
				this.shutdown();
				log.error(this.channelFuture.cause().getMessage(), this.channelFuture.cause());
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			this.shutdown();
		}
	}

	@Override
	public void close() throws IOException {
		//workerGroup
		if(this.workerGroup != null) {
			try {
				this.workerGroup.shutdownGracefully();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.workerGroup = null;
			}
		}

		//bossGroup
		if(this.bossGroup != null) {
			try {
				this.bossGroup.shutdownGracefully();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				this.bossGroup = null;
			}
		}
	}
	
	/**
	 * shutdown
	 */
	public void shutdown() {
		
		try {
			this.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		BasicConfigurator.configure();
		
		TcpServerConfig config = new TcpServerConfig(81);
		config.setLogLevel(LogLevel.DEBUG);
		TcpServer server = new TcpServer(config);
		server.run();
		server.close();
		server.run();
	}
}
