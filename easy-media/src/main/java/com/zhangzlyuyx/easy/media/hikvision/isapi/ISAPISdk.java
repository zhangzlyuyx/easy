package com.zhangzlyuyx.easy.media.hikvision.isapi;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.core.ActionCallback;
import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.util.HttpUtils;
import com.zhangzlyuyx.easy.core.util.HttpUtils.HttpClientRequest;
import com.zhangzlyuyx.easy.media.hikvision.isapi.vo.DeviceInfo;
import com.zhangzlyuyx.easy.media.hikvision.isapi.vo.DeviceStatus;
import com.zhangzlyuyx.easy.media.hikvision.isapi.vo.StreamingChannel;
import com.zhangzlyuyx.easy.media.hikvision.isapi.vo.StreamingChannelList;
import com.zhangzlyuyx.easy.core.util.StringUtils;

/**
 * 海康 ISAPI SDK
 * @author Administrator
 *
 */
public class ISAPISdk {

	private static final Logger log = LoggerFactory.getLogger(ISAPISdk.class);
	
	public static final Integer DEFAULT_PORT = 80;
	
	public static final String DEFAULT_USERNAME = "admin";
	
	public static final String DEFAULT_CHANNEL = "1";
	
	/**
	 * 能力
	 */
	private static final String ISAPI_SYSTEM_CAPABILITIES = "/ISAPI/System/capabilities";
	
	/**
	 * 设备
	 */
	private static final String ISAPI_SYSTEM_DEVICEINFO = "/ISAPI/System/deviceInfo";

	private static final String ISAPI_SYSTEM_STATUS = "/ISAPI/System/status";
	
	private static final String ISAPI_SYSTEM_TIME = "/ISAPI/System/time";
	
	/**
	 * 通道
	 */
	private static final String ISAPI_STREAMING_CHANNELS = "/ISAPI/Streaming/channels/";
	
	private static final String ISAPI_STREAMING_CHANNEL = "/ISAPI/Streaming/channels/{}";
	
	/**
	 * 通道
	 */
	private static final String ISAPI_STREAMING_CHANNELS_CAPABILITIES = "/ISAPI/Streaming/channels/{}/capabilities";
	
	private static final String ISAPI_STREAMING_CHANNELS_DYNAMICCAP = "/ISAPI/Streaming/channels/{}/dynamicCap";
	
	/**
	 * 主机
	 */
	private String host;
	
	/**
	 * 端口号
	 */
	private Integer port = DEFAULT_PORT;
	
	/**
	 * 用户名
	 */
	private String username = DEFAULT_USERNAME;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 是否使用 ssl
	 */
	private boolean useSsl;

	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isUseSsl() {
		return useSsl;
	}
	
	public void setUseSsl(boolean useSsl) {
		this.useSsl = useSsl;
	}
	
	/**
	 * 获取系统设备信息
	 * @return
	 */
	public Result<DeviceInfo> getSystemDeviceInfo() {
		Map<String, String> params = new HashMap<>();
		Result<String> ret = this.httpGet(ISAPI_SYSTEM_DEVICEINFO, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		DeviceInfo deviceInfo = DeviceInfo.parse(ret.getData());
		return new Result<DeviceInfo>(true, "", deviceInfo);
	}
	
	/**
	 * 获取系统状态信息 
	 * @return
	 */
	public Result<DeviceStatus> getSystemStatus() {
		Map<String, String> params = new HashMap<>();
		Result<String> ret = this.httpGet(ISAPI_SYSTEM_STATUS, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		DeviceStatus deviceStatus = DeviceStatus.parse(ret.getData());
		return new Result<DeviceStatus>(true, "", deviceStatus);
	}
	
	/*
	public Object getSystemTime() {
		Map<String, String> params = new HashMap<>();
		Result<String> ret = this.httpGet(ISAPI_SYSTEM_TIME, params);
		return ret;
	}
	*/
	
	/*
	public Object getSystemCapabilities() {
		Map<String, String> params = new HashMap<>();
		Result<String> ret = this.httpGet(ISAPI_SYSTEM_CAPABILITIES, params);
		return ret;
	}
	*/
	
	/**
	 * 获取媒体通道列表
	 * @return
	 */
	public Result<StreamingChannelList> getStreamingChannels() {
		Map<String, String> params = new HashMap<>();
		Result<String> ret = this.httpGet(ISAPI_STREAMING_CHANNELS, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		StreamingChannelList list = StreamingChannelList.parse(ret.getData());
		return new Result<StreamingChannelList>(true, "", list);
	}
	
	/**
	 * 获取媒体通道信息
	 * @param channelId 通道id
	 * @return
	 */
	public Result<StreamingChannel> getStreamingChannel(String channelId) {
		Map<String, String> params = new HashMap<>();
		Result<String> ret = this.httpGet(StringUtils.format(ISAPI_STREAMING_CHANNEL, channelId), params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		StreamingChannel streamingChannel = StreamingChannel.parse(ret.getData());
		return new Result<StreamingChannel>(true, "", streamingChannel);
	}
	
	/**
	 * 设置媒体通道信息
	 * @param channelId 通道id
	 * @param streamingChannel 通道信息
	 * @return
	 */
	public Result<String> setStreamChannel(String channelId, StreamingChannel streamingChannel) {
		String params = streamingChannel.asXML();
		Result<String> ret = this.httpPut(StringUtils.format(ISAPI_STREAMING_CHANNEL, channelId), params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		try {
			Document document = DocumentHelper.parseText(ret.getData());
			String statusCode = document.getRootElement().elementText("statusCode");
			String statusString = document.getRootElement().elementText("statusString");
			if(statusCode.equals("1")) {
				return new Result<>(true, statusString);
			}
			return new Result<>(false, statusString);
		} catch (Exception e) {
			return new Result<>(false, e.getMessage());
		}
	}
	
	/**
	 * http get 请求
	 * @param uri
	 * @param params
	 * @return
	 */
	private Result<String> httpGet(final String uri, final Map<String, String> params) {
		final HttpClientRequest httpClientRequest = HttpClientRequest.createHttpGet(this.getRequestUri(uri), null, params);
		return this.httpRequest(httpClientRequest);
	}
	
	/**
	 * http put 请求
	 * @param uri
	 * @param putEntity
	 * @return
	 */
	private Result<String> httpPut(final String uri, final String putEntity) {
		StringEntity stringEntity = new StringEntity(putEntity, ContentType.APPLICATION_FORM_URLENCODED);
		final HttpClientRequest httpClientRequest = HttpClientRequest.createHttpPut(this.getRequestUri(uri), null, stringEntity);
		return this.httpRequest(httpClientRequest);
	}
	
	/**
	 * http 请求
	 * @param httpClientRequest
	 * @return
	 */
	private Result<String> httpRequest(final HttpClientRequest httpClientRequest) {
		httpClientRequest.setClientBuilderCallback(new ActionCallback<HttpClientBuilder>() {
			
			@Override
			public void action(HttpClientBuilder httpClientBuilder) {

				URI serverURI = httpClientRequest.getHttpRequest().getURI();
				
				//CredentialsProvider
				CredentialsProvider credsProvider = new BasicCredentialsProvider();
	            credsProvider.setCredentials(new AuthScope(serverURI.getHost(), serverURI.getPort()),
	                    new UsernamePasswordCredentials(getUsername(), getPassword()));
	            
	            httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
			}
		});
		return HttpUtils.httpRequestReturnString(httpClientRequest);
	}
	
	/**
	 * 获取请求 uri
	 * @param uri
	 * @return
	 */
	private String getRequestUri(String uri) {
		if(uri.startsWith("http://") || uri.startsWith("https://")) {
			return uri;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(this.isUseSsl() ? "https://" : "http://");
		sb.append(this.getHost());
		sb.append(":");
		sb.append(this.getPort());
		sb.append(uri);
		return sb.toString();
	}
	
	public static void main(String[] args) {
		
	}
}
