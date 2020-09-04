package com.zhangzlyuyx.easy.io.nio.udp;

import com.zhangzlyuyx.easy.io.nio.ChannelContext;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpClientChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private UdpClient client;
	
	public UdpClient getClient() {
		return this.client;
	}
	
	public UdpClientChannelHandler(UdpClient client) {
		this.client = client;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		
		
		ByteBuf buf = packet.copy().content();
		
		ChannelContext channelContext = null;
		
		this.getClient().getClientNioListener().onAfterReceivedBytes(channelContext, buf.readableBytes());
		
		//通过ByteBuf的readableBytes方法可以获取缓冲区可读的字节数
		byte[] req = new byte[buf.readableBytes()];
		
		//通过ByteBuf的readBytes方法将缓冲区中的字节数组复制到新建的byte数组中
		buf.readBytes(req);
		
		System.out.println(new String(req));
		
		this.getClient().send(req, packet.sender()).sync();
	}
}
