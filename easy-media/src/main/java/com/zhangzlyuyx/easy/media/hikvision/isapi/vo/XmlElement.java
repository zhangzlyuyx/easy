package com.zhangzlyuyx.easy.media.hikvision.isapi.vo;

import org.dom4j.Element;

public abstract class XmlElement {

	protected Element element;

	public XmlElement(Element element) {
		this.element = element;
	}
	
	public Element getElement(String name) {
		return this.element.element(name);
	}
	
	public String getElementText(String name) {
		return this.element.elementText(name);
	}
	
	public boolean setElementText(String name, String text) {
		Element el = this.element.element(name);
		if(el == null) {
			return false;
		}
		el.setText(text);
		return true;
	}
	
	public String asXML() {
		return this.element.asXML();
	}
}
