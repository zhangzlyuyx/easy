package com.zhangzlyuyx.easy.storage;

/**
 * 存储类型枚举
 * @author zhangzlyuyx
 *
 */
public enum StorageType {
	
	/**
	 * 本地文件存储
	 */
	File("file", "文件存储"),
	/**
	 * go-fastdfs 分布式文件存储
	 */
	GoFastDFS("gofastdfs", "go-fastdfs 存储"),
	;
	
	private String code;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	StorageType(String code, String name){
		this.code = code;
		this.name = name;
	}
	
	/**
	 * 根据 code 获取枚举
	 * @param code
	 * @return
	 */
	public static StorageType getInstanceByCode(String code) {
		for (StorageType s : StorageType.values()) {
			if (s.code.equalsIgnoreCase(code)) {
				return s;
			}
		}
		return null;
	}
}
