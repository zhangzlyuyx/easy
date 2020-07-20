package com.zhangzlyuyx.easy.media.zlmediakit.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 录像文件
 *
 */
public class MediaRecordFile implements Serializable {

	private static final long serialVersionUID = 4088494954818186781L;

	/**
	 * mp4文件列表
	 */
	private List<String> paths;
	
	/**
	 * 存储目录
	 */
	private String rootPath;
	
	public List<String> getPaths() {
		if(this.paths == null) {
			this.paths = new ArrayList<>();
		}
		return this.paths;
	}
	
	public void setPaths(List<String> paths) {
		this.paths = paths;
	}
	
	public String getRootPath() {
		return this.rootPath;
	}
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
}
