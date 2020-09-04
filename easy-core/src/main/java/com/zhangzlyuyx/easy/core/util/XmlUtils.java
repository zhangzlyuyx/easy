package com.zhangzlyuyx.easy.core.util;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * xml 工具类(基于dom4j)
 * @author zhangzlyuyx
 *
 */
public class XmlUtils {
	
	private static final Logger log = LoggerFactory.getLogger(XmlUtils.class);

	/**
	 * 解析 xml 文本
	 * @param xmlText
	 * @return
	 */
	public static Document parseText(String xmlText) {
		try {
			return DocumentHelper.parseText(xmlText);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 解析 xml 数据流
	 * @param inputStream
	 * @return
	 */
	public static Document parseStream(InputStream inputStream) {
		try {
			SAXReader saxReader = new SAXReader();
			return saxReader.read(inputStream);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 解析 xml 数据流
	 * @param file
	 * @return
	 */
	public static Document parseFile(File file) {
		try {
			SAXReader saxReader = new SAXReader();
			return saxReader.read(file);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 解析 xml URL or filename  
	 * @param systemId
	 * @return
	 */
	public static Document parseSystemId(String systemId) {
		try {
			SAXReader saxReader = new SAXReader();
			return saxReader.read(systemId);
		} catch (Exception e) {
			log.error("", e);
			return null;
		}
	}
	
	/**
	 * 获取根节点元素
	 * @param document 文档对象
	 * @return
	 */
	public static Element getRootElement(Document document) {
		return document != null ? document.getRootElement() : null;
	}
	
	/**
	 * 获取元素
	 * @param element
	 * @param name
	 * @return
	 */
	public static Element getElement(Element element, String name) {
		return element != null ? element.element(name) : null;
	}
	
	/**
	 * 获取元素集合
	 * @param element
	 * @param name
	 * @return
	 */
	public static List<Element> getElements(Element element, String name) {
		return element != null ? element.elements() : null;
	}
	
	/**
	 * 获取元素文本 
	 * @param element
	 * @return
	 */
	public static String getElementText(Element element) {
		return element != null ? element.getTextTrim() : null;
	}
	
	/**
	 * 获取子元素文本
	 * @param element
	 * @param name
	 * @return
	 */
	public static String getElementText(Element element, String name) {
		return element != null ? element.elementTextTrim(name) : null;
	}
	
	/**
	 * 设置元素文本
	 * @param element
	 * @param text
	 * @return
	 */
	public static Element setElementText(Element element, String text) {
		if(element == null) {
			return null;
		}
		element.setText(text);
		return element;
	}
	
	/**
	 * 设置子元素文本
	 * @param element
	 * @param name
	 * @param text
	 * @return
	 */
	public static Element setElementText(Element element, String name, String text) {
		if(element == null) {
			return null;
		}
		Element child = getElement(element, name);
		if(child == null) {
			return child;
		}
		child.setText(text);
		return child;
	}
	
	/**
	 * 获取元素 xml
	 * @param element
	 * @return
	 */
	public static String getElementXML(Element element) {
		return element != null ? element.asXML() : null;
	}
	
	/**
	 * 获取子元素 xml
	 * @param element
	 * @param name
	 * @return
	 */
	public static String getElementXML(Element element, String name) {
		Element child = getElement(element, name);
		return child != null ? child.asXML() : null;
	}
	
	/**
	 * 添加元素
	 * @param element
	 * @param name
	 * @param text
	 * @return
	 */
	public static Element addElement(Element element, String name, String text) {
		return element != null ? element.addEntity(name, text) : null;
	}
	
	/**
	 * 添加元素
	 * @param element
	 * @param child
	 * @return
	 */
	public static Element addElement(Element element, Element child) {
		if(element == null) {
			return null;
		}
		element.add(child);
		return child;
	}
	
	/**
	 * 删除元素
	 * @param element
	 * @param name
	 * @return
	 */
	public static boolean removeElement(Element element, String name) {
		if(element == null) {
			return false;
		}
		List<Element> childs = getElements(element, name);
		if(childs == null || childs.size() == 0) {
			return false;
		}
		for(Element child : childs) {
			element.remove(child);
		}
		return true;
	}
}
