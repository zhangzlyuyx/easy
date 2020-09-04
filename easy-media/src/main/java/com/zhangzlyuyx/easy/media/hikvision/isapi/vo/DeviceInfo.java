package com.zhangzlyuyx.easy.media.hikvision.isapi.vo;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class DeviceInfo extends XmlElement {

	/**
	 * 获取设备名称
	 * @return
	 */
	public String getDeviceName() {
		return this.getElementText("deviceName");
	}
	
	public void setDeviceName(String deviceName) {
		this.setElementText("deviceName", deviceName);
	}
	
	/**
	 * 获取设备ID
	 * @return
	 */
	public String getDeviceID() {
		return this.getElementText("deviceID");
	}
	
	/**
	 * 获取序列号
	 * @return
	 */
	public String getSerialNumber() {
		return this.getElementText("serialNumber");
	}
	
	/**
	 * 获取MAC地址
	 * @return
	 */
	public String getMacAddress() {
		return this.getElementText("macAddress");
	}
	
	public DeviceInfo(Element element) {
		super(element);
	}
	
	public static DeviceInfo parse(String xml) {
		try {
			Document document = DocumentHelper.parseText(xml);
			return parse(document.getRootElement());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static DeviceInfo parse(Element element) {
		DeviceInfo info = new DeviceInfo(element);
		return info;
	}
}
