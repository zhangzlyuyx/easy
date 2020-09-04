package com.zhangzlyuyx.easy.io.nio.tcp;

import java.util.concurrent.TimeUnit;

import com.zhangzlyuyx.easy.io.nio.ClientNioListener;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;

public class TcpClientChannelHandler extends ChannelInboundHandlerAdapter {

	private TcpClient client;
	
	public TcpClient getClient() {
		return this.client;
	}
	
	private SocketChannel channel;
	
	public SocketChannel getChannel() {
		return this.channel;
	}
	
	public TcpClientChannelHandler(TcpClient client, SocketChannel channel) {
		this.client = client;
		this.channel = channel;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ClientNioListener listener = this.getClient().getClientNioListener();
		if(listener != null) {
			
			//listener.onAfterConnected(channelContext, true, false);
		}
		super.channelActive(ctx);
		ctx.channel().writeAndFlush("111");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		
		final EventLoop eventLoop = ctx.channel().eventLoop();
		
		eventLoop.schedule(new Runnable() {
			
			@Override
			public void run() {
				
				getClient().run();
			}
		}, 1L, TimeUnit.SECONDS);
		
		super.channelInactive(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
}
