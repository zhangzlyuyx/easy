package com.zhangzlyuyx.easy.storage.aliyunoss;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.ResultCallback;
import com.zhangzlyuyx.easy.core.util.DateUtils;
import com.zhangzlyuyx.easy.core.util.FileUtils;
import com.zhangzlyuyx.easy.core.util.IoUtils;
import com.zhangzlyuyx.easy.core.util.StringUtils;
import com.zhangzlyuyx.easy.storage.DownloadResult;
import com.zhangzlyuyx.easy.storage.StorageEngine;
import com.zhangzlyuyx.easy.storage.UploadResult;

import cn.hutool.extra.servlet.ServletUtil;

/**
 * 阿里云 OSS 存储引擎
 * @author zhangzlyuyx
 *
 */
public class AliyunOSSStorageEngine extends StorageEngine {
	
	public static final String CONFIG_SCENE = "scene";
	
	public static final String CONFIG_FORMAT = "format";
	
	public static final String CONFIG_ENDPOINT = "endpoint";
	
	public static final String CONFIG_ACCESSKEYID = "accessKeyId";
	
	public static final String CONFIG_ACCESSKEYSECRET = "accessKeySecret";
	
	public static final String CONFIG_BUCKETNAME = "bucketName";
	
	public static final String CONFIG_DOWNLOADREDIRECT = "downloadRedirect";
	
	public static final String CONFIG_DOWNLOADPATH = "downloadPath";
	
	public static final String CONFIG_DOWNLOADURLPREFIX = "downloadUrlPrefix";
	
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

	private String endpoint;
	
	public String getEndpoint() {
		return endpoint;
	}
	
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	private String accessKeyId;
	
	public String getAccessKeyId() {
		return accessKeyId;
	}
	
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	
	private String accessKeySecret;
	
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	
	private String bucketName;
	
	public String getBucketName() {
		return bucketName;
	}
	
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	
	private OSS ossClient;
	
	/**
	 * 获取 oss 客户端
	 * @return
	 */
	public OSS getOSSClient() {
		if(this.ossClient != null) {
			return this.ossClient;
		}
		OSS client = null;
		try {
			client = new OSSClientBuilder().build(this.getEndpoint(), this.getAccessKeyId(), this.getAccessKeySecret());
			this.ossClient = client;
		} catch (Exception e) {
			log.error("", e);
			if(client != null) {
				client.shutdown();
			}
		}
		return this.ossClient;
	}
	
	/**
	 * 是否开启重定向下载
	 */
	private String downloadRedirect = "0";
	
	public String getDownloadRedirect() {
		return this.downloadRedirect;
	}
	
	public void setDownloadRedirect(String downloadRedirect) {
		this.downloadRedirect = downloadRedirect;
	}
	
	/**
	 * 下载url前缀(http协议、域名、端口号)
	 */
	private String downloadUrlPrefix;
	
	public String getDownloadUrlPrefix() {
		return downloadUrlPrefix;
	}
	
	public void setDownloadUrlPrefix(String downloadUrlPrefix) {
		this.downloadUrlPrefix = downloadUrlPrefix;
	}
	
	/**
	 * 默认下载子路径
	 */
	private String downloadPath;
	
	public String getDownloadPath() {
		return downloadPath;
	}
	
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	
	@Override
	public Result<String> loadConfig(Map<String, Object> config) {
		//endpoint
		if(!config.containsKey(CONFIG_ENDPOINT)) {
			return new Result<>(false, CONFIG_ENDPOINT + " 不能为空!");
		}
		this.setEndpoint((String)config.get(CONFIG_ENDPOINT));
		//accessKeyId
		if(!config.containsKey(CONFIG_ACCESSKEYID)) {
			return new Result<>(false, CONFIG_ACCESSKEYID + " 不能为空!");
		}
		this.setAccessKeyId((String)config.get(CONFIG_ACCESSKEYID));
		//accessKeySecret
		if(!config.containsKey(CONFIG_ACCESSKEYSECRET)) {
			return new Result<>(false, CONFIG_ACCESSKEYSECRET + "不能为空!");
		}
		this.setAccessKeySecret((String)config.get(CONFIG_ACCESSKEYSECRET));
		//bucketName
		if(!config.containsKey(CONFIG_BUCKETNAME)) {
			return new Result<>(false, CONFIG_BUCKETNAME + "不能为空!");
		}
		this.setBucketName((String)config.get(CONFIG_BUCKETNAME));
		//scene
		if(config.containsKey(CONFIG_SCENE)) {
			this.setScene((String)config.get(CONFIG_SCENE));
		}
		//format
		if(config.containsKey(CONFIG_FORMAT)) {
			this.setFormat((String)config.get(CONFIG_FORMAT));
		}
		//downloadRedirect
		if(config.containsKey(CONFIG_DOWNLOADREDIRECT)) {
			this.setDownloadRedirect((String)config.get(CONFIG_DOWNLOADREDIRECT));
		}
		//downloadUrlPrefix
		if(config.containsKey(CONFIG_DOWNLOADURLPREFIX)) {
			this.setDownloadUrlPrefix((String)config.get(CONFIG_DOWNLOADURLPREFIX));
		}
		//downloadPath
		if(config.containsKey(CONFIG_DOWNLOADPATH)) {
			this.setDownloadPath((String)config.get(CONFIG_DOWNLOADPATH));
		}
		return super.loadConfig(config);
	}
	
	@Override
	public Result<UploadResult> uploadFile(Map<String, String> headers, Map<String, String> params, String filename,
			InputStream inputStream, boolean closeStream) {
		//场景
		String scene = params.containsKey(CONFIG_SCENE) ? params.get(CONFIG_SCENE) : null;
		//获取上传的文件路径
		String uploadPath = this.getUploadPath(scene, filename);
		//获取客户端
		OSS ossClient = this.getOSSClient();
		if(ossClient == null) {
			return new Result<>(false, "创建OSS存储客户端失败!");
		}
		try {
			PutObjectResult putObjectResult = ossClient.putObject(this.getBucketName(), uploadPath, inputStream);
			ResponseMessage responseMessage = putObjectResult.getResponse();
			//putObjectResult.get
			UploadResult data = new UploadResult();
			data.setFilename(filename);
			data.setPath(uploadPath);
			data.setMd5(putObjectResult.getETag());
			data.setFileSize(responseMessage != null ? responseMessage.getContentLength() : null);
			return new Result<UploadResult>(true, "上传成功", data);
		} catch (Exception e) {
			log.error("", e);
			return new Result<>(false, e.getMessage());
		}
	}

	@Override
	public Result<String> downloadFile(String filePath, Map<String, String> headers, Map<String, String> params,
			OutputStream outputStream, boolean closeStream, ResultCallback<DownloadResult> downloadResultCallback) {
		//获取客户端
		OSS ossClient = this.getOSSClient();
		if(ossClient == null) {
			return new Result<>(false, "创建OSS存储客户端失败!");
		}
		OSSObject ossObj = null;
		InputStream inputStream = null;
		try {
			//downloadPath
			String downloadPath = this.getDownloadPath(filePath);
			
			//获取对象信息
			ossObj = ossClient.getObject(this.getBucketName(), downloadPath);
			
			//inputStream
			inputStream = ossObj.getObjectContent();
			
			//写出流
			IoUtils.write(inputStream, outputStream);

			return new Result<>(true, "下载成功!");
		} catch (Exception e) {
			log.error("", e);
			return new Result<>(false, e.getMessage());
		} finally {
			//inputStream
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception ex) {
					log.error("", ex);
				}
			}
			//obj
			if(ossObj != null) {
				try {
					ossObj.forcedClose();
				} catch (Exception ex) {
					log.error("", ex);
				}
			}
		}
	}
	
	@Override
	public Result<String> downloadFile(String filePath, Map<String, String> headers, Map<String, String> params, 
			ServletResponse response) {
		return this.downloadFile(filePath, headers, params, null, response);
	}

	@Override
	public Result<String> downloadFile(String filePath, Map<String, String> headers, Map<String, String> params,
			ServletRequest request, ServletResponse response) {
		
		//是否支持重定向下载
		if(this.downloadRedirect != null && this.downloadRedirect.equals("1")) {
			try {
				String downloadUrlPrefix = this.downloadUrlPrefix;
				//默认重定向url前缀参考 endpoint
				if(StringUtils.isEmpty(downloadUrlPrefix)) {
					downloadUrlPrefix =  request.getScheme() + "://" + this.getBucketName() + "." + this.getEndpoint();
				}
				//补充下载子路径 
				if(!StringUtils.isEmpty(this.getDownloadPath())) {
					downloadUrlPrefix = downloadUrlPrefix + this.getDownloadPath();
				}
				//下载url绝对路径
				String downloadUrl = this.getDownloadUrl(downloadUrlPrefix, filePath);
				//请求重定向
				((HttpServletResponse)response).sendRedirect(downloadUrl);
				return new Result<>(true, "");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return new Result<>(false, e.getMessage());
			}
		}
		//获取客户端
		OSS ossClient = this.getOSSClient();
		if(ossClient == null) {
			return new Result<>(false, "创建OSS存储客户端失败!");
		}
		OSSObject ossObj = null;
		InputStream inputStream = null;
		try {
			//downloadPath
			String downloadPath = this.getDownloadPath(filePath);
			
			//获取对象信息
			ossObj = ossClient.getObject(this.getBucketName(), downloadPath);
			
			//获取元数据信息
			ObjectMetadata metadata = ossObj.getObjectMetadata();
			
			response.setContentType(metadata.getContentType());
			response.setContentLengthLong(metadata.getContentLength());
			response.setCharacterEncoding(metadata.getContentEncoding());
			
			//inputStream
			inputStream = ossObj.getObjectContent();
			
			ServletUtil.write((HttpServletResponse)response, inputStream, IoUtils.DEFAULT_BUFFER_SIZE);
			
			return new Result<>(true, "下载成功!");
		} catch (Exception e) {
			log.error("", e);
			return new Result<>(false, e.getMessage());
		} finally {
			//inputStream
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception ex) {
					log.error("", ex);
				}
			}
			//obj
			if(ossObj != null) {
				try {
					ossObj.forcedClose();
				} catch (Exception ex) {
					log.error("", ex);
				}
			}
		}
	}
	
	/**
	 * 获取文件下载 url
	 * @param filePath
	 * @return
	 */
	protected String getDownloadPath(String filePath) {
		if(filePath.startsWith("/")) {
			filePath = StringUtils.trimStart(filePath, "/");
		}
		return filePath;
	}
	
	/**
	 * 获取文件下载url
	 * @param downloadUrlPrefix
	 * @param filePath
	 * @return
	 */
	protected String getDownloadUrl(String downloadUrlPrefix, String filePath) {
		String downloadServer = downloadUrlPrefix;
		if(!downloadServer.endsWith("/")){
			downloadServer = downloadServer + "/";
		}
		if(filePath.startsWith("/")){
			filePath = filePath.substring(1);
		}
		String downloadUrl = downloadServer + filePath;
		return downloadUrl;
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
		String randomName = FileUtils.getFileRandomName(FileUtils.getFileExtName(filename));
		path = new File(path, randomName);
		//返回文件相对路径
		String uploadPath = path.getPath();
		if(uploadPath.contains("\\")) {
			uploadPath = StringUtils.replace(uploadPath, "\\", "/");
		}
		return uploadPath;
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		
		//释放OSS客户端
		if(this.ossClient != null) {
			this.ossClient.shutdown();
			this.ossClient = null;
		}
	}

	public static void main(String[] args) throws IOException {
		
	}
}
