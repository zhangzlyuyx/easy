package com.zhangzlyuyx.easy.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.ResultCallback;

/**
 * 存储引擎 -抽象类
 * @author zhangzlyuyx
 *
 */
public abstract class StorageEngine {

	protected static final Logger log = LoggerFactory.getLogger(StorageEngine.class);
	
	/**
	 * 存储配置
	 */
	protected Map<String, Object> config;
	
	/**
	 * 加载配置
	 * @param config
	 * @return
	 */
	public Result<String> loadConfig(Map<String, Object> config){
		this.config = config;
		return new Result<>(true, "加载成功");
	}
	
	/**
	 * 上传文件 File
	 * @param headers
	 * @param params
	 * @param file
	 * @return
	 */
	public Result<UploadResult> uploadFile(final Map<String, String> headers, final Map<String, String> params, final File file){
		if(file == null || !file.exists()) {
			return new Result<>(false, "需要上传文件不存在!");
		}
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			return this.uploadFile(headers, params, file.getName(), inputStream, false);
		} catch (Exception e) {
			return new Result<>(false, e.getMessage());
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			}
		}
	}
	
	/**
	 * 上传文件 MultipartFile
	 * @param headers
	 * @param params
	 * @param file
	 * @return
	 */
	public Result<UploadResult> uploadFile(final Map<String, String> headers, final Map<String, String> params, final MultipartFile file){
		//HACK:if(file == null || !file.isEmpty()) {
		if(file == null || file.isEmpty()) {
			return new Result<>(false, "需要上传文件不存在!");
		}
		try {
			InputStream inputStream = file.getInputStream();
			return this.uploadFile(headers, params, file.getOriginalFilename(), inputStream, false);
		} catch (Exception e) {
			return new Result<>(false, e.getMessage());
		}
	}
	
	/**
	 * 上传文件
	 * @param headers
	 * @param params
	 * @param filename
	 * @param inputStream
	 * @param closeStream
	 * @return
	 */
	public abstract Result<UploadResult> uploadFile(Map<String, String> headers, Map<String, String> params, String filename, InputStream inputStream, boolean closeStream);
	
	/**
	 * 下载文件验证
	 * @param filePath
	 * @param headers
	 * @param params
	 * @return
	 */
	public Result<String> downloadValidate(String filePath, Map<String, String> headers, Map<String, String> params){
		return new Result<>(true, "");
	}

	/**
	 * 下载文件
	 * @param filePath
	 * @param headers
	 * @param params
	 * @param outputStream
	 * @param closeStream
	 * @param headersCallback
	 * @return
	 */
	public abstract Result<String> downloadFile(String filePath, Map<String, String> headers, Map<String, String> params, OutputStream outputStream, boolean closeStream, ResultCallback<DownloadResult> downloadResultCallback);

	/**
	 * 下载文件
	 * @param filePath
	 * @param headers
	 * @param params
	 * @param response
	 * @return
	 */
	public abstract Result<String> downloadFile(String filePath, Map<String, String> headers, Map<String, String> params, ServletResponse response);
}
