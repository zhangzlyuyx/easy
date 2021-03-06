package com.zhangzlyuyx.easy.storage.gofastdfs;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zhangzlyuyx.easy.core.IResult;
import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.ResultCallback;
import com.zhangzlyuyx.easy.core.util.ConvertUtils;
import com.zhangzlyuyx.easy.core.util.HttpUtils;
import com.zhangzlyuyx.easy.core.util.StringUtils;
import com.zhangzlyuyx.easy.storage.DownloadResult;
import com.zhangzlyuyx.easy.storage.StorageEngine;
import com.zhangzlyuyx.easy.storage.UploadResult;

/**
 * go-fastdfs 分布式文件系统
 * @author zhangzlyuyx
 *
 */
public class GoFastDFSStorageEngine extends StorageEngine {
	
	public static final String CONFIG_SERVER = "server";
	
	public static final String CONFIG_GROUP = "group";
	
	public static final String CONFIG_SCENE = "scene";
	
	public static final String CONFIG_OUTPUT = "output";
	
	public static final String CONFIG_PATH = "path";
	
	public static final String CONFIG_GOOGLECODE = "googleCode";
	
	public static final String CONFIG_AUTHTOKEN = "authToken";
	
	public static final String CONFIG_DOWNLOADREDIRECT = "downloadRedirect";
	
	public static final String CONFIG_DOWNLOADPATH = "downloadPath";
	
	public static final String CONFIG_DOWNLOADURLPREFIX = "downloadUrlPrefix";
	
	private String server;
	
	public String getServer() {
		return this.server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	/**
	 * 组号
	 */
	private String group = "group1";
	
	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	/**
	 * 场景
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
	 * 输出(output)
	 */
	private String output = "json";
	
	public String getOutput() {
		return output;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
	/**
	 * 自定义路径(path)
	 */
	private String path;
	
	public String getPath() {
		return this.path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	/**
	 * google认证码(code)
	 */
	private String googleCode;
	
	public String getGoogleCode() {
		return this.googleCode;
	}
	
	public void setGoogleCode(String googleCode) {
		this.googleCode = googleCode;
	}
	
	/**
	 * 自定义认证(auth_token)
	 */
	private String authToken;
	
	public String getAuthToken() {
		return this.authToken;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
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
		//server
		if(!config.containsKey(CONFIG_SERVER)) {
			return new Result<>(false, "服务器地址不能为空");
		}
		this.setServer((String)config.get(CONFIG_SERVER));
		//group
		if(config.containsKey(CONFIG_GROUP)) {
			this.setGroup((String)config.get(CONFIG_GROUP));
		}
		//scene
		if(config.containsKey(CONFIG_SCENE)) {
			this.setScene((String)config.get(CONFIG_SCENE));
		}
		//output
		if(config.containsKey("output")) {
			this.setOutput((String)config.get("output"));
		}
		//path
		if(config.containsKey(CONFIG_PATH)) {
			this.setPath((String)config.get(CONFIG_PATH));
		}
		//googleCode
		if(config.containsKey(CONFIG_GOOGLECODE)) {
			this.setGoogleCode((String)config.get(CONFIG_GOOGLECODE));
		}
		//authToken
		if(config.containsKey(CONFIG_AUTHTOKEN)) {
			this.setAuthToken((String)config.get(CONFIG_AUTHTOKEN));
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
	public Result<UploadResult> uploadFile(Map<String, String> headers, Map<String, String> params, String filename, InputStream inputStream, boolean closeStream) {
		String url = this.getUploadUrl();
		if(params == null) {
			params = new HashMap<>();
		}
		params.put("scene", this.getScene());
		params.put("output", this.getOutput());
		//执行 http 文件上传
		Result<String> ret = HttpUtils.httpFileUpload(url, headers, params, filename, inputStream, closeStream);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		try {
			JSONObject json = JSONObject.parseObject(ret.getData());
			Integer retcode = json.getInteger("retcode");
			String retmsg = json.getString("retmsg");
			//判断上传结果
			if(retcode == null || !retcode.equals(0)) {
				return new Result<>(false, retmsg);
			}
			UploadResult uploadResult = new UploadResult();
			uploadResult.setPath(json.getString("path"));
			uploadResult.setMd5(json.getString("md5"));
			uploadResult.setFileSize(json.getLong("size"));
			uploadResult.setFilename(filename);
			return new Result<UploadResult>(true, "上传成功", uploadResult);
		} catch (Exception e) {
			return new Result<>(false, e.getMessage());
		}
	}
	
	@Override
	public Result<String> downloadValidate(String filePath, Map<String, String> headers, Map<String, String> params) {
		
		Integer width = params.containsKey("width") ? ConvertUtils.toInteger(params.get("width"), null) : null;
		if(width != null && width.intValue() > 10000) {
			return new Result<>(false, "图片缩放宽度参数不能超过 10000像素");
		}
		
		Integer height = params.containsKey("height") ? ConvertUtils.toInteger(params.get("height"), null) : null;
		if(height != null && height.intValue() > 10000) {
			return new Result<>(false, "图片缩放高度参数不能超过 10000像素");
		}
		
		return super.downloadValidate(filePath, headers, params);
	}
	
	@Override
	public Result<String> downloadFile(String filePath, Map<String, String> headers, Map<String, String> params, OutputStream outputStream, boolean closeStream, final ResultCallback<DownloadResult> downloadResultCallback) {
		
		String url = this.getDownloadUrl(filePath);
		
		Result<String> ret = HttpUtils.httpFileDownload(url, headers, params, outputStream, closeStream, new ResultCallback<Map<String,String>>() {
			
			@Override
			public IResult<Map<String, String>> result(Map<String, String> headers) {
				if(downloadResultCallback != null) {
					DownloadResult downloadResult = new DownloadResult();
					downloadResult.getAttributes().putAll(headers);
					downloadResultCallback.result(downloadResult);
				}
				return new Result<>(true, "", headers);
			}
		});
		return ret;
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
				//默认重定向url前缀参考请求地址
				if(StringUtils.isEmpty(downloadUrlPrefix)) {
					downloadUrlPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
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
		
		String url = this.getDownloadUrl(filePath);
		
		try {
			final HttpServletResponse httpResponse = (HttpServletResponse)response;
			final OutputStream outputStream = response.getOutputStream();
			//执行http文件下载请求 
			Result<String> ret = HttpUtils.httpFileDownload(url, headers, params, outputStream, true, new ResultCallback<Map<String,String>>() {
				
				@Override
				public IResult<Map<String, String>> result(Map<String, String> headers) {
					//拷贝headers
					for(String headerName : headers.keySet()) {
						httpResponse.setHeader(headerName, headers.get(headerName));
					}
					return new Result<>(true, "", headers);
				}
			});
			if(!ret.isSuccess()) {
				return new Result<>(false, ret.getMsg());
			}
			return new Result<>(true, "下载成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new Result<>(false, e.getMessage());
		}
	}
	
	/**
	 * 获取文件上传 url
	 * @return
	 */
	protected String getUploadUrl() {
		String uploadServer = this.getServer();
		if(!uploadServer.endsWith("/")){
			uploadServer = uploadServer + "/";
		}
		String uploadAction = this.getGroup() + "/upload";
		if(uploadAction.startsWith("/")){
			uploadAction = uploadAction.substring(1);
		}
		String uploadUrl = uploadServer + uploadAction;
		return uploadUrl;
	}
	
	/**
	 * 获取文件下载 url
	 * @param filePath
	 * @return
	 */
	protected String getDownloadUrl(String filePath) {
		return this.getDownloadUrl(this.getServer(), filePath);
	}
	
	/**
	 * 获取文件下载 url
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
	
	public static void main(String[] args) throws Exception {
		
	}
}
