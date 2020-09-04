package com.zhangzlyuyx.easy.storage.file;

import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.ResultCallback;
import com.zhangzlyuyx.easy.core.util.CryptoUtils;
import com.zhangzlyuyx.easy.core.util.DateUtils;
import com.zhangzlyuyx.easy.core.util.FileUtils;
import com.zhangzlyuyx.easy.core.util.HttpUtils;
import com.zhangzlyuyx.easy.storage.DownloadResult;
import com.zhangzlyuyx.easy.storage.StorageEngine;
import com.zhangzlyuyx.easy.storage.StorageFactory;
import com.zhangzlyuyx.easy.storage.StorageType;
import com.zhangzlyuyx.easy.storage.UploadResult;

/**
 * 本地文件存储
 * @author zhangzlyuyx
 *
 */
public class FileStorageEngine extends StorageEngine {
	
	public static final String CONFIG_FILEROOT = "fileRoot";
	
	public static final String CONFIG_SCENE = "scene";
	
	public static final String CONFIG_FORMAT = "format";
	
	/**
	 * 文件根目录
	 */
	private String fileRoot;
	
	public String getFileRoot() {
		return this.fileRoot;
	}
	
	public void setFileRoot(String fileRoot) {
		this.fileRoot = fileRoot;
	}
	
	/**
	 * 场景(一级子目录)
	 */
	private String scene = "default";
	
	public String getScene() {
		if(this.scene == null) {
			this.scene = "";
		}
		return this.scene.trim();
	}
	
	public void setScene(String scene) {
		this.scene = scene;
	}

	/**
	 * 存储路径格式
	 */
	private String format = "yyyy/MM/dd";
	
	public String getFormat() {
		return this.format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	@Override
	public Result<String> loadConfig(Map<String, Object> config) {
		//fileRoot
		if(config.containsKey(CONFIG_FILEROOT)) {
			this.setFileRoot((String)config.get(CONFIG_FILEROOT));
		} else {
			this.setFileRoot(FileUtils.getWebRoot().getAbsolutePath());
		}
		//scene
		if(config.containsKey(CONFIG_SCENE)) {
			this.setScene((String)config.get(CONFIG_SCENE));
		}
		return super.loadConfig(config);
	}
	
	@Override
	public Result<UploadResult> uploadFile(Map<String, String> headers, Map<String, String> params, String filename, InputStream inputStream, boolean closeStream) {
		//场景
		String scene = params.containsKey(CONFIG_SCENE) ? params.get(CONFIG_SCENE) : null;
		//获取上传的文件路径
		String uploadPath = this.getUploadPath(scene, filename);
		String uploadFile = this.getUploadFile(uploadPath);
		try {
			File file = new File(uploadFile);
			if(file.exists()) {
				return new Result<>(false, "文件已存在!");
			} else if(file.isDirectory()) {
				return new Result<>(false, "文件路径不符合要求!");
			}
			FileUtils.writeFile(inputStream, file);
			UploadResult uploadResult = new UploadResult();
			uploadResult.setPath(uploadPath);
			uploadResult.setMd5(CryptoUtils.encodeMd5(file));
			uploadResult.setFileSize(file.length());
			uploadResult.setFilename(filename);
			return new Result<UploadResult>(true, "上传成功", uploadResult);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result<>(false, e.getMessage());
		} finally {
			if(closeStream) {
				try {
					inputStream.close();
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			}
		}
	}

	@Override
	public Result<String> downloadFile(String filePath, Map<String, String> headers, Map<String, String> params, OutputStream outputStream, boolean closeStream, ResultCallback<DownloadResult> downloadResultCallback) {
		String downloadFile = this.getDownloadFile(filePath);
		try {
			File file = new File(downloadFile);
			if(!file.exists()) {
				return new Result<>(false, "文件不存在!");
			}
			
			//写出文件
			FileUtils.writeToStream(file, outputStream);
			
			return new Result<>(true, "下载成功");
		} catch (Exception e) {
			return new Result<>(true, e.getMessage());
		} finally {
			if(closeStream) {
				try {
					outputStream.close();
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			}
		}
	}
	
	@Override
	public Result<String> downloadFile(String filePath, Map<String, String> headers, Map<String, String> params, ServletResponse response) {
		
		String downloadFile = this.getDownloadFile(filePath);
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			
			File file = new File(downloadFile);
			//fileName
			String fileName = file.getName();
			//contentType
			String contentType = new MimetypesFileTypeMap().getContentType(fileName);
			if(contentType == null || contentType.length() == 0){
				contentType = "application/octet-stream;charset=UTF-8";
			}
			
			//headers
			httpResponse.setContentType(contentType);
			httpResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, HttpUtils.DEFAULT_CHARSET));
			httpResponse.setHeader("Content-Length", String.valueOf(file.length()));
			
			//写出文件
			FileUtils.writeToStream(file, outputStream);

			return new Result<>(true, "下载成功");
		} catch (Exception e) {
			return new Result<>(true, e.getMessage());
		} finally {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
			}
		}
	}

	/**
	 * 获取上传相对路径
	 * @param scene
	 * @param filename
	 * @return
	 */
	protected String getUploadPath(String scene, String filename) {
		//场景
		if(scene == null) {
			scene = this.getScene();
		}
		File path = new File(scene);
		//目录格式
		if(this.getFormat() != null) {
			String format = DateUtils.format(DateUtils.getDate(), this.getFormat());
			if(format != null) {
				path = new File(path, format);
			}
		}
		//文件
		String randomName = FileUtils.getRandomName(FileUtils.getExtName(filename));
		path = new File(path, randomName);
		//返回文件相对路径
		return path.getPath();
	}
	
	/**
	 * 获取上传文件绝对路径
	 * @param path
	 * @return
	 */
	protected String getUploadFile(String path) {
		File root = new File(this.getFileRoot());
		return new File(root, path).getAbsolutePath();
	}
	
	/**
	 * 获取文件下载绝对路径
	 * @param filePath
	 * @return
	 */
	protected String getDownloadFile(String filePath) {
		File root = new File(this.getFileRoot());
		return new File(root, filePath).getAbsolutePath();
	}
	
	public static void main(String[] args) throws Exception {
		
		Map<String, Object> config = new HashMap<>();
		
		config.put(FileStorageEngine.CONFIG_FILEROOT, "c:\\files");
		
		StorageEngine storageEngine = StorageFactory.getInstance().getStorageEngine(StorageType.File, config);
		
		File uploadFile = new File("c:\\windows\\system32\\license.rtf");
		
		Result<UploadResult> retU = storageEngine.uploadFile(null, null, uploadFile);
		
		System.out.println(JSON.toJSONString(retU));
		
		if(retU.isSuccess()) {
			
			File downloadFile = new File(FileUtils.getRandomName(FileUtils.getExtName(uploadFile)));
			
			OutputStream outputStream = new FileOutputStream(downloadFile);
			
			Result<String> retD = storageEngine.downloadFile(retU.getData().getPath(), null, null, outputStream, true, null);
			
			System.out.println(JSON.toJSONString(retD));
			
			System.out.println(downloadFile.getAbsolutePath());
		}
	}
}
