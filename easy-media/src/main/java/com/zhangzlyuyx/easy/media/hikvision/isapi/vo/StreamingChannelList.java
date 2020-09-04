package com.zhangzlyuyx.easy.media.hikvision.isapi.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 流媒体通道列表
 *
 */
public class StreamingChannelList extends XmlElement implements Serializable {

	private static final long serialVersionUID = -7873987544673776188L;
	
	/**
	 * 获取列表集合
	 * @return
	 */
	public List<StreamingChannel> getItems(){
		List<StreamingChannel> items = new ArrayList<>();
		for(Element el : this.element.elements("StreamingChannel")) {
			items.add(StreamingChannel.parse(el));
		}
		return items;
	}
	
	public StreamingChannel getItem(int index) {
		List<StreamingChannel> items = this.getItems();
		if(items.size() == 0 || index < 0 || index >= items.size()) {
			return null;
		}
		return items.get(index);
	}
	
	public StreamingChannelList(Element element) {
		super(element);
	}

	public static StreamingChannelList parse(String xml) {
		try {
			Document document = DocumentHelper.parseText(xml);
			return parse(document.getRootElement());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static StreamingChannelList parse(Element element) {
		try {
			StreamingChannelList streamingChannelList = new StreamingChannelList(element);
			return streamingChannelList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
