package com.zhangzlyuyx.easy.core.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
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
	 * 获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		return FileUtil.getName(filePath);
	}
	
	/**
	 * 获取文件扩展名,扩展名不带“.”
	 * @param file 文件
	 * @return
	 */
	public static String getFileExtName(File file) {
		return FileUtil.extName(file);
	}
	
	/**
	 * 获取文件扩展名,扩展名不带“.”
	 * @param file 文件
	 * @return
	 */
	@Deprecated
	public static String getExtName(File file) {
		return FileUtil.extName(file);
	}
	
	/**
	 * 获取文件扩展名,扩展名不带“.”
	 * @param fileName 文件名
	 * @return
	 */
	public static String getFileExtName(String fileName) {
		return FileUtil.extName(fileName);
	}
	
	/**
	 * 获取文件扩展名,扩展名不带“.”
	 * @param fileName 文件名
	 * @return
	 */
	@Deprecated
	public static String getExtName(String fileName) {
		return FileUtil.extName(fileName);
	}
	
	/**
	 * 获取随机文件名
	 * @return
	 */
	public static String getFileRandomName(String extName) {
		return UUID.randomUUID().toString().replace("-", "") + (extName != null ? "." + extName.trim() : "");
	}
	
	/**
	 * 获取随机文件名
	 * @return
	 */
	@Deprecated
	public static String getRandomName(String extName) {
		return UUID.randomUUID().toString().replace("-", "") + (extName != null ? "." + extName.trim() : "");
	}
	
	/**
	 * 判断文件是否存在，
	 * @param path
	 * @return
	 */
	public static boolean existFile(String path) {
		return FileUtil.exist(path);
	}
	
	/**
	 * 判断文件是否存在，
	 * @param path
	 * @return
	 */
	@Deprecated
	public static boolean exist(String path) {
		return FileUtil.exist(path);
	}
	
	/**
	 * 删除文件或者文件夹
	 * @param file
	 * @return
	 */
	public static boolean deleteFile(File file) {
		if(file == null || !file.exists()) {
			return false;
		}
		return FileUtil.del(file);
	}
	
	/**
	 * 修改文件或目录的文件名，不变更路径，只是简单修改文件名
	 * @param file
	 * @param newName
	 * @param isOverride
	 * @return
	 */
	public static File renameFile(File file, String newName, boolean isOverride) {
		return FileUtil.rename(file, newName, false, isOverride);
	}
	
	/**
	 * 拷贝文件或目录
	 * @param src 源文件
	 * @param dest 
Parameters:src 源文件dest 
	 * @param isOverride
	 * @return
	 */
	public static File copyFile(File src, File dest, boolean isOverride) {
		File file = FileUtil.copy(src, dest, isOverride);
		return file;
	}
	
	/**
	 * 将流的内容写入文件
	 * @param inputStream
	 * @param dest
	 * @return
	 * @throws IORuntimeException
	 */
	public static File writeFile(InputStream inputStream, File dest) throws IORuntimeException {
		return FileUtil.writeFromStream(inputStream, dest);
	}
	
	/**
	 * 写数据到文件中
	 * @param data
	 * @param dest
	 * @return
	 */
	public static File writeFile(byte[] data, File dest) throws IORuntimeException {
		return FileUtil.writeBytes(data, dest);
	}
	
	/**
	 * 将文件写入流中
	 * @param file
	 * @param outputStream
	 * @return
	 */
	public static boolean writeToStream(File file, OutputStream outputStream) throws IORuntimeException {
		FileUtil.writeToStream(file, outputStream);
		return true;
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
