package com.zhangzlyuyx.easy.io.nio.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

public class TcpServerChannelHandler extends ChannelInboundHandlerAdapter {

	private TcpServer server;
	
	public TcpServer getServer() {
		return this.server;
	}
	
	private SocketChannel channel;
	
	public SocketChannel getChannel() {
		return this.channel;
	}

	public TcpServerChannelHandler(TcpServer server, SocketChannel channel) {
		this.server = server;
		this.channel = channel;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("channelRead");
		super.channelRead(ctx, msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelReadComplete");
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
	}
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelRegistered");
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelUnregistered");
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
	}
	
	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelWritabilityChanged");
		// TODO Auto-generated method stub
		super.channelWritabilityChanged(ctx);
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handlerAdded");
		// TODO Auto-generated method stub
		super.handlerAdded(ctx);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handlerRemoved");
		// TODO Auto-generated method stub
		super.handlerRemoved(ctx);
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		System.out.println("userEventTriggered");
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("exceptionCaught");
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}
	
}
