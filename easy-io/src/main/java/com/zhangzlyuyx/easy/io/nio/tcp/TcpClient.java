package com.zhangzlyuyx.easy.io.nio.tcp;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.zhangzlyuyx.easy.io.Packet;
import com.zhangzlyuyx.easy.io.nio.ChannelContext;
import com.zhangzlyuyx.easy.io.nio.ClientNioHandler;
import com.zhangzlyuyx.easy.io.nio.ClientNioListener;
import com.zhangzlyuyx.easy.io.nio.NioConfig;
import com.zhangzlyuyx.easy.io.nio.NioDecodeException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TcpClient implements Runnable, Closeable {

	private TcpClientConfig config;
	
	public TcpClientConfig getConfig() {
		return this.config;
	}
	
	private EventLoopGroup workGroup;
	
	private Bootstrap clientBootstrap;
	
	private TcpClientConnectionListener clientConnectionListener;
	
	private ClientNioHandler clientNioHandler;
	
	private ClientNioListener clientNioListener;
	
	private ChannelFuture channelFuture;
	
	public TcpClient(TcpClientConfig config) {
		//config
		this.config = config;
		//workGroup
		this.workGroup = new NioEventLoopGroup();
		//clientConnectionListener
		this.clientConnectionListener = new TcpClientConnectionListener(this);
	}
	
	public ClientNioHandler getClientNioHandler() {
		return this.clientNioHandler;
	}
	
	public void setClientNioHandler(ClientNioHandler clientNioHandler) {
		this.clientNioHandler = clientNioHandler;
	}
	
	public ClientNioListener getClientNioListener() {
		return this.clientNioListener;
	}
	
	public void setClientNioListener(ClientNioListener clientNioListener) {
		this.clientNioListener = clientNioListener;
	}
	
	/**
	 * create Bootstrap
	 * @return
	 */
	public Bootstrap createBootstrap(EventLoopGroup workGroup) {
		//clientBootstrap
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(workGroup);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				socketChannel.pipeline().addLast(new TcpClientChannelHandler(TcpClient.this, socketChannel));
			}
		});
		bootstrap.remoteAddress(this.config.getServerIp(), this.config.getServerPort());
		return bootstrap;
	}
	
	@Override
	public void run() {
		this.clientBootstrap = this.createBootstrap(this.workGroup);
		this.channelFuture = this.clientBootstrap.connect().addListener(this.clientConnectionListener);
	}
	
	@Override
	public void close() throws IOException {
		if(this.channelFuture != null) {
			this.channelFuture.channel().closeFuture();
		}
		this.workGroup.shutdownGracefully();
	}
	
	public static void main(String[] args) {
		TcpClientConfig config = new TcpClientConfig();
		config.setServerIp("www.baidu.com");
		config.setServerPort(80);
		
		TcpClient client = new TcpClient(config);
		client.setClientNioListener(new ClientNioListener() {
			
			@Override
			public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove)
					throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect)
					throws Exception {
				// TODO Auto-generated method stub
				
			}
		});
		client.setClientNioHandler(new ClientNioHandler() {
			
			@Override
			public void handler(Packet packet, ChannelContext channelContext) throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public ByteBuffer encode(Packet packet, NioConfig nioConfig, ChannelContext channelContext) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext)
					throws NioDecodeException {
				// TODO Auto-generated method stub
				return null;
			}
		});
		client.run();
	}
}
