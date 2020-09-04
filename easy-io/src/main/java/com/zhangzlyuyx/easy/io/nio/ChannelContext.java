package com.zhangzlyuyx.easy.io.nio;

import io.netty.channel.Channel;

public class ChannelContext {

	private Channel channel;
	
	public Channel getChannel() {
		return this.channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public ChannelContext(Channel channel) {
		this.channel = channel;
	}
}
