package com.zhangzlyuyx.easy.io.nio.udp;

import java.io.Closeable;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.zhangzlyuyx.easy.io.Packet;
import com.zhangzlyuyx.easy.io.nio.ChannelContext;
import com.zhangzlyuyx.easy.io.nio.ClientNioHandler;
import com.zhangzlyuyx.easy.io.nio.ClientNioListener;
import com.zhangzlyuyx.easy.io.nio.NioConfig;
import com.zhangzlyuyx.easy.io.nio.NioDecodeException;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpClient implements Runnable, Closeable {

	/**
	 * udp 服务器配置 
	 */
	private UdpClientConfig config;
	
	private Bootstrap bootstrap = new Bootstrap();
	
	private EventLoopGroup workGroup = new NioEventLoopGroup();
	
	private UdpClientChannelHandler serverChannelHandler;
	
	private ChannelFuture channelFuture;
	
	private Channel channel;
	
	private ClientNioListener clientNioListener;
	
	private ClientNioHandler clientNioHandler;

	public UdpClient(UdpClientConfig config) {
		this.config = config;
		this.serverChannelHandler = new UdpClientChannelHandler(this);
	}
	
	public UdpClientConfig getConfig() {
		return this.config;
	}
	
	public ClientNioListener getClientNioListener() {
		return this.clientNioListener;
	}
	
	public void setClientNioListener(ClientNioListener clientNioListener) {
		this.clientNioListener = clientNioListener;
	}
	
	public ClientNioHandler getClientNioHandler() {
		return this.clientNioHandler;
	}
	
	public void setClientNioHandler(ClientNioHandler clientNioHandler) {
		this.clientNioHandler = clientNioHandler;
	}
	
	public Bootstrap createBootstrap(EventLoopGroup eventLoopGroup) {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup);
		bootstrap.channel(NioDatagramChannel.class);
		bootstrap.handler(this.serverChannelHandler);
		bootstrap.option(ChannelOption.SO_BROADCAST, true);
		return bootstrap;
	}
	
	@Override
	public void run() {
		this.bootstrap = this.createBootstrap(this.workGroup);
		this.channelFuture = this.bootstrap.bind(this.config.getClientNode().getIp(), this.config.getClientNode().getPort());
		try {
			this.channel = this.channelFuture.sync().channel();
			//更新本地udp端口
			this.config.getClientNode().setPort(((InetSocketAddress)this.channelFuture.channel().localAddress()).getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		this.workGroup.shutdownGracefully();
	}
	
	/**
	 * 发送数据
	 * @param datagramPacket
	 * @return
	 */
	public ChannelFuture send(DatagramPacket datagramPacket) {
		return this.channel.writeAndFlush(datagramPacket);
	}
	
	/**
	 * 发送数据
	 * @param data
	 * @param recipient
	 * @return
	 */
	public ChannelFuture send(byte[] data, InetSocketAddress recipient) {
		if(recipient == null) {
			recipient = new InetSocketAddress(this.config.getServerNode().getIp(), this.config.getServerNode().getPort());
		}
		DatagramPacket datagramPacket = new DatagramPacket(Unpooled.copiedBuffer(data), recipient);
		return this.send(datagramPacket);
	}
	
	public static void main(String[] args) {
		
		UdpClientConfig config = new UdpClientConfig();
		config.getClientNode().setPort(9999);
		UdpClient udpClient = new UdpClient(config);
		udpClient.setClientNioHandler(new ClientNioHandler() {
			
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
		udpClient.run();
		//udpClient.close();
	}
}
