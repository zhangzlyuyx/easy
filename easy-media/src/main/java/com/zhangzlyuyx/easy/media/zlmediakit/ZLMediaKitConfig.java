package com.zhangzlyuyx.easy.media.zlmediakit;

import java.io.Serializable;

/**
 * ZLMediaKit 配置
 * @author zhangzlyuyx
 *
 */
public class ZLMediaKitConfig implements Serializable {
	
	private static final long serialVersionUID = 3056749744686480069L;

	public static final String DEFAULT_SECRET = "035c73f7-bb6b-4889-a715-d9eb2d1925cc";

	/**
	 * 内网地址
	 */
	private String lanHost = "localhost";
	
	public String getLanHost() {
		return this.lanHost;
	}
	
	public void setLanHost(String lanHost) {
		this.lanHost = lanHost;
	}
	
	/**
	 * 内网端口
	 */
	private Integer lanHttpPort;
	
	public Integer getLanHttpPort() {
		return this.lanHttpPort;
	}
	
	public void setLanHttpPort(Integer lanHttpPort) {
		this.lanHttpPort = lanHttpPort;
	}
	
	/**
	 * 公网地址
	 */
	private String wanHost = "localhost";
	
	public String getWanHost() {
		return this.wanHost;
	}
	
	public void setWanHost(String wanHost) {
		this.wanHost = wanHost;
	}
	
	/**
	 * 公网端口
	 */
	private Integer wanHttpPort;
	
	public Integer getWanHttpPort() {
		return this.wanHttpPort;
	}
	
	public void setWanHttpPort(Integer wanHttpPort) {
		this.wanHttpPort = wanHttpPort;
	}
	
	/**
	 * 是否使用公网
	 */
	private boolean useWan;
	
	public boolean isUseWan() {
		return this.useWan;
	}
	
	public void setUseWan(boolean useWan) {
		this.useWan = useWan;
	}
	
	/**
	 * 是否使用ssl
	 */
	private boolean useSsl;
	
	public boolean isUseSsl() {
		return this.useSsl;
	}
	
	public void setUseSsl(boolean useSsl) {
		this.useSsl = useSsl;
	}
	
	/**
	 * 密钥
	 */
	private String secret = DEFAULT_SECRET;
	
	public String getSecret() {
		return this.secret;
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	/**
	 * 获取 api 服务器地址
	 * @return
	 */
	public String getApiServer() {
		if(this.isUseWan()) {
			return (this.isUseSsl() ? "https://" : "http://") + this.getWanHost() + ":" + this.getWanHttpPort() + "/";
		}else {
			return (this.isUseSsl() ? "https://" : "http://") + this.getLanHost() + ":" + this.getLanHttpPort() + "/";
		}
	}
}
