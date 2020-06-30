package com.zhangzlyuyx.easy.core.util;

import java.io.File;

import cn.hutool.core.io.FileUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.dialect.Props;

/**
 * 文件工具类
 * @author zhangzlyuyx
 *
 */
public class FileUtils {

	/**
	 * 是否为Windows环境
	 * @return
	 */
	public static boolean isWindows() {
		return FileUtil.isWindows();
	}
	
	/**
	 * 获取Web项目下的web root路径
	 * @return
	 */
	public static File getWebRoot() {
		return FileUtil.getWebRoot();
	}
	
	/**
	 * 获取文件扩展名,扩展名不带“.”
	 * @param file 文件
	 * @return
	 */
	public static String getExtName(File file) {
		return FileUtil.extName(file);
	}
	
	/**
	 * 获取文件扩展名,扩展名不带“.”
	 * @param fileName 文件名
	 * @return
	 */
	public static String getExtName(String fileName) {
		return FileUtil.extName(fileName);
	}
	
	/******************** begin properties ********************/
	
	/**
	 * 读取 properties文件 属性值
	 * @param path 配置文件路径
	 * @param key 属性key
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String readProperty(String path, String key, String defaultValue) {
		Props props = new Props(path);
		return props.getStr(key, defaultValue);
	}
	
	/**
	 * 保存 properties文件 属性值
	 * @param path
	 * @param key
	 * @param value
	 */
	public static void writeProperty(String path, String key, String value) {
		Props props = new Props(path);
		props.setProperty(key, value);
		props.store(path);
	}
	
	/**
	 * 读取 setting 文件设置值
	 * @param path 设置文件路径
	 * @param key 设置 key
	 * @param group 设置分组
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String readSetting(String path, String key, String group, String defaultValue) {
		Setting setting = new Setting(path);
		return setting.getStr(key, group, defaultValue);
	}
	
	/**
	 * 写出 setting 文件设置值
	 * @param path 设置文件路径
	 * @param key 设置 key
	 * @param group c设置分组
	 * @param value 默认值
	 */
	public static void writeSetting(String path, String key, String group, String value) {
		Setting setting = new Setting(path);
		setting.put(group, key, value);
		setting.store(path);
	}
	
	/******************** end properties ********************/
}
