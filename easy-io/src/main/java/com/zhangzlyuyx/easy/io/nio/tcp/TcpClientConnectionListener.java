package com.zhangzlyuyx.easy.io.nio.tcp;

import java.util.concurrent.TimeUnit;

import com.zhangzlyuyx.easy.io.nio.ClientNioListener;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

public class TcpClientConnectionListener implements ChannelFutureListener {

	private TcpClient client;
	
	public TcpClient getClient() {
		return this.client;
	}
	
	public TcpClientConnectionListener(TcpClient client) {
		this.client = client;
	}
	
	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		
		if(future.isSuccess()) {
			return;
		}

		final EventLoop eventLoop = future.channel().eventLoop();
		
		eventLoop.schedule(new Runnable() {
			
			@Override
			public void run() {
				
				getClient().run();
			}
		}, 1L, TimeUnit.SECONDS);
	}

}
