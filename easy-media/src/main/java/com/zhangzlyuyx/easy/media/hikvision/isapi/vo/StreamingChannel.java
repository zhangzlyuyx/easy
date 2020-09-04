package com.zhangzlyuyx.easy.media.hikvision.isapi.vo;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 流媒体通道信息
 *
 */
public class StreamingChannel extends XmlElement {

	public String getId() {
		return this.getElementText("id");
	}
	
	public void setId(String id) {
		this.setElementText("id", id);
	}
	
	public String getChannelName() {
		return this.getElementText("channelName");
	}
	
	public void setChannelName(String channelName) {
		this.setElementText("channelName", channelName);
	}
	
	public String getEnabled() {
		return this.getElementText("enabled");
	}
	
	public void setEnabled(String enabled) {
		this.setElementText("enabled", enabled);
	}
	
	/**
	 * 视频信息
	 */
	private StreamingChannelVideo video;
	
	public StreamingChannelVideo getVideo() {
		if(this.video == null) {
			this.video = StreamingChannelVideo.parse(this.getElement("Video"));
		}
		return video;
	}
	
	public void setVideo(StreamingChannelVideo video) {
		this.video = video;
	}
	
	public StreamingChannel(Element element) {
		super(element);
	}
	
	public static StreamingChannel parse(String xml) {
		try {
			Document document = DocumentHelper.parseText(xml);
			return parse(document.getRootElement());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static StreamingChannel parse(Element element) {
		StreamingChannel streamingChannel = new StreamingChannel(element);
		return streamingChannel;
	}
}
