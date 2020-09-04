package com.zhangzlyuyx.easy.io.nio.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpServerChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private UdpServer server;
	
	public UdpServer getServer() {
		return server;
	}
	
	public UdpServerChannelHandler(UdpServer server) {
		this.server = server;
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {

		ByteBuf buf = packet.copy().content();
		
		//通过ByteBuf的readableBytes方法可以获取缓冲区可读的字节数
		byte[] req = new byte[buf.readableBytes()];
		
		//通过ByteBuf的readBytes方法将缓冲区中的字节数组复制到新建的byte数组中
		buf.readBytes(req);
		
		System.out.println(new String(req));
		
		//this.getServer().send(req, packet.sender());
	}
}
