package com.zhangzlyuyx.easy.media.zlmediakit.vo;

import java.io.Serializable;

/**
 * 媒体信息
 *
 */
public class MediaStream implements Serializable {

	private static final long serialVersionUID = 4114582562884968604L;
	
	public final static String DEFAULT_VHOST = "__defaultVhost__";
	
	public final static String DEFAULT_APP = "live";
	
	/**
	 * 虚拟主机，例如__defaultVhost__
	 */
	private String vhost = DEFAULT_VHOST;
	
	/**
	 * 应用名，例如 live
	 */
	private String app = DEFAULT_APP;
	
	/**
	 * 流id，例如 obs
	 */
	private String stream;
	
	public MediaStream() {
		
	}
	
	public MediaStream(String stream) {
		this.setStream(stream);
	}
	
	public MediaStream(String vhost, String app, String stream) {
		this.setVhost(vhost);
		this.setApp(app);
		this.setStream(stream);
	}
	
	public String getVhost() {
		return this.vhost;
	}
	
	public void setVhost(String vhost) {
		this.vhost = vhost;
	}

	public String getApp() {
		return this.app;
	}
	
	public void setApp(String app) {
		this.app = app;
	}
	
	public String getStream() {
		return this.stream;
	}
	
	public void setStream(String stream) {
		this.stream = stream;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getVhost());
		sb.append("/");
		sb.append(this.getApp());
		sb.append("/");
		sb.append(this.getStream());
		return sb.toString();
	}
	
	/**
	 * 解析
	 * @param key
	 * @return
	 */
	public static MediaStream parse(String key) {
		if(key == null || key.length() == 0) {
			return null;
		}
		String[] strs = key.split("/");
		if(strs.length < 3) {
			return null;
		}
		MediaStream streamKey = new MediaStream();
		streamKey.setVhost(strs[0]);
		streamKey.setApp(strs[1]);
		streamKey.setStream(strs[2]);
		return streamKey;
	}
}
