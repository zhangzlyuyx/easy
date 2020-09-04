package com.zhangzlyuyx.easy.storage;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * 上传结果
 * @author zhangzlyuyx
 *
 */
public class UploadResult implements Serializable {
	
	private static final long serialVersionUID = 834130458577377101L;
	
	/**
	 * 文件相对路径
	 */
	private String path;
	
	public String getPath() {
		return this.path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * 文件名
	 */
	private String filename;
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	/**
	 * 文件大小
	 */
	private Long fileSize;

	public Long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	/**
	 * 文件 md5 值
	 */
	private String md5;
	
	public String getMd5() {
		return this.md5;
	}
	
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	@Override
	public String toString() {
		if(this.md5 != null || this.path != null) {
			return JSONObject.toJSONString(this);
		}
		return super.toString();
	}
}
