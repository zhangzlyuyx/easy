package com.zhangzlyuyx.easy.media.hikvision.isapi.vo;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 设备状态信息
 *
 */
public class DeviceStatus extends XmlElement {

	public DeviceStatus(Element element) {
		super(element);
	}
	
	/**
	 * 获取当前设备时间
	 * @return
	 */
	public String getCurrentDeviceTime() {
		return this.getElementText("currentDeviceTime");
	}
	
	public void setCurrentDeviceTime(String currentDeviceTime) {
		this.setElementText("currentDeviceTime", currentDeviceTime);
	}
	
	/**
	 * 获取设备在线时间
	 * @return
	 */
	public String getDeviceUpTime() {
		return this.getElementText("deviceUpTime");
	}
	
	public void setDeviceUpTime(String deviceUpTime) {
		this.setElementText("deviceUpTime", deviceUpTime);
	}
	
	public static DeviceStatus parse(String xml) {
		try {
			Document document = DocumentHelper.parseText(xml);
			return parse(document.getRootElement());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static DeviceStatus parse(Element element) {
		DeviceStatus deviceStatus = new DeviceStatus(element);
		return deviceStatus;
	}
}
