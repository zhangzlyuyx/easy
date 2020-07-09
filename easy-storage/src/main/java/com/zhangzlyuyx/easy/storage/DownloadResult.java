package com.zhangzlyuyx.easy.storage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件下载结果
 * @author zhangzlyuyx
 *
 */
public class DownloadResult implements Serializable {

	private static final long serialVersionUID = -2731289622576630129L;
	
	/**
	 * 附加属性
	 */
	private Map<String, Object> attributes;
	
	public Map<String, Object> getAttributes() {
		if(this.attributes == null) {
			this.attributes = new HashMap<>();
		}
		return this.attributes;
	}
	
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
