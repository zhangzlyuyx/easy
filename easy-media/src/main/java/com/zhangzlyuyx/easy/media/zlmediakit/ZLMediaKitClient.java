package com.zhangzlyuyx.easy.media.zlmediakit;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhangzlyuyx.easy.core.IResult;
import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.ResultCallback;
import com.zhangzlyuyx.easy.core.util.DateUtils;
import com.zhangzlyuyx.easy.core.util.HttpUtils;
import com.zhangzlyuyx.easy.media.zlmediakit.vo.LoadAndDeplay;
import com.zhangzlyuyx.easy.media.zlmediakit.vo.MediaInfo;
import com.zhangzlyuyx.easy.media.zlmediakit.vo.MediaRecordFile;
import com.zhangzlyuyx.easy.media.zlmediakit.vo.MediaStream;

/**
 * ZLMediaKit 流媒体服务器客户端
 * @author zhangzlyux
 *
 */
public class ZLMediaKitClient {
	
	private final static Logger log = LoggerFactory.getLogger(ZLMediaKitClient.class);
	
	private final static String api_getThreadsLoad = "/index/api/getThreadsLoad";
	
	private final static String api_getWorkThreadsLoad = "/index/api/getWorkThreadsLoad";
	
	private final static String api_getServerConfig = "/index/api/getServerConfig";

	private final static String api_setServerConfig = "/index/api/setServerConfig";
	
	private final static String api_restartServer = "/index/api/restartServer";
	
	private final static String api_isMediaOnline = "/index/api/isMediaOnline";
	
	private final static String api_getMediaList = "/index/api/getMediaList";
	
	private final static String api_getMediaInfo = "/index/api/getMediaInfo";
	
	private final static String api_close_streams = "/index/api/close_streams";
	
	private final static String api_getAllSession = "/index/api/getAllSession";
	
	private final static String api_kick_session = "/index/api/kick_session";
	
	private final static String api_kick_sessions = "/index/api/kick_sessions";
	
	private final static String api_addStreamProxy = "/index/api/addStreamProxy";
	
	private final static String api_delStreamProxy = "/index/api/delStreamProxy";
	
	private final static String api_addFFmpegSource = "/index/api/addFFmpegSource";
	
	private final static String api_delFFmpegSource = "/index/api/delFFmpegSource";
	
	private final static String api_getSsrcInfo = "/index/api/getSsrcInfo";
	
	private final static String api_getMp4RecordFile = "/index/api/getMp4RecordFile";
	
	private final static String api_startRecord = "/index/api/startRecord";
	
	private final static String api_stopRecord = "/index/api/stopRecord";
	
	private final static String api_isRecording = "/index/api/isRecording";
	
	private final static String api_getSnap = "/index/api/getSnap";
	
	private ZLMediaKitConfig config;
	
	public ZLMediaKitConfig getConfig() {
		return this.config;
	}
	
	public void setConfig(ZLMediaKitConfig config) {
		this.config = config;
	}
	
	public ZLMediaKitClient(ZLMediaKitConfig config) {
		this.config = config;
	}
	
	/**
	 * 获取各epoll(或select)线程负载以及延时
	 * @return
	 */
	public Result<List<LoadAndDeplay>> getThreadsLoad(){
		Result<JSONObject> ret = this.request(api_getThreadsLoad, new HashMap<String, String>());
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		JSONArray array = ret.getData().getJSONArray("data");
		List<LoadAndDeplay> list = LoadAndDeplay.parse(array);
		return new Result<List<LoadAndDeplay>>(true, "请求成功", list);
	}
	
	/**
	 * 获取各后台epoll(或select)线程负载以及延时
	 * @return
	 */
	public Result<List<LoadAndDeplay>> getWorkThreadsLoad(){
		Result<JSONObject> ret = this.request(api_getWorkThreadsLoad, new HashMap<String, String>());
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		JSONArray array = ret.getData().getJSONArray("data");
		List<LoadAndDeplay> list = LoadAndDeplay.parse(array);
		return new Result<List<LoadAndDeplay>>(true, "请求成功", list);
	}
	
	/**
	 * 动态添加rtsp/rtmp/hls拉流代理(只支持H264/H265/aac/G711负载)
	 * @param mediaStream 流信息
	 * @param url 拉流地址，例如rtmp://live.hkstv.hk.lxdns.com/live/hks2
	 * @param enableRtsp 是否转rtsp
	 * @param enableRtmp 是否转rtmp
	 * @param enableHls 是否转hls
	 * @param enableMp4 是否mp4录制
	 * @param rtp_type rtsp拉流时，拉流方式，0：tcp，1：udp，2：组播
	 * @return
	 */
	public Result<MediaStream> addStreamProxy(MediaStream mediaStream, String url, Boolean enableRtsp, Boolean enableRtmp, Boolean enableHls, Boolean enableMp4, Integer rtp_type) {
		Map<String, String> params = new HashMap<>();
		params.put("vhost", mediaStream.getVhost());
		params.put("app", mediaStream.getApp());
		params.put("stream", mediaStream.getStream());
		params.put("url", url);
		params.put("enable_rtsp", enableRtsp != null && enableRtsp.booleanValue() ? "1" : "0");
		params.put("enable_rtmp", enableRtmp == null || enableRtmp.booleanValue() ? "1" : "0");
		params.put("enable_hls", enableHls == null || enableHls.booleanValue() ? "1" : "0");
		params.put("enable_mp4", enableMp4 != null && enableMp4.booleanValue() ? "1" : "0");
		params.put("rtp_type", rtp_type != null ? String.valueOf(rtp_type) : "0");
		Result<JSONObject> ret = this.request(api_addStreamProxy, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		MediaStream key = MediaStream.parse(ret.getData().getJSONObject("data").getString("key"));
		return new Result<>(true, "", key);
	}
	
	/**
	 * 关闭拉流代理
	 * @param mediaStream 流信息
	 * @return
	 */
	public Result<Boolean> delStreamProxy(MediaStream mediaStream) {
		Map<String, String> params = new HashMap<>();
		params.put("key", mediaStream.toString());
		Result<JSONObject> ret = this.request(api_delStreamProxy, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		JSONObject data = ret.getData().getJSONObject("data");
		Boolean flag = data.getBoolean("flag");
		return new Result<>(true, "", flag);
	}
	
	/**
	 * 通过fork FFmpeg进程的方式拉流代理，支持任意协议
	 * @param srcUrl FFmpeg拉流地址,支持任意协议或格式(只要FFmpeg支持即可)
	 * @param dstUrl FFmpeg rtmp推流地址，一般都是推给自己，例如rtmp://127.0.0.1/live/stream_form_ffmpeg
	 * @param timeoutMs FFmpeg推流成功超时时间
	 * @return
	 */
	public Result<String> addFFmpegSource(String srcUrl, String dstUrl, Integer timeoutMs) {
		Map<String, String> params = new HashMap<>();
		params.put("src_url", srcUrl);
		params.put("dst_url", dstUrl);
		params.put("timeout_ms", String.valueOf(timeoutMs));
		Result<JSONObject> ret = this.request(api_addFFmpegSource, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		String key = ret.getData().getJSONObject("data").getString("key");
		return new Result<>(true, "", key);
	}
	
	/**
	 * 关闭ffmpeg拉流代理
	 * @param key
	 * @return
	 */
	public Result<Boolean> delFFmpegSource(String key) {
		Map<String, String> params = new HashMap<>();
		params.put("key", key);
		Result<JSONObject> ret = this.request(api_delFFmpegSource, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		JSONObject data = ret.getData().getJSONObject("data");
		Boolean flag = data.getBoolean("flag");
		return new Result<>(true, "", flag);
	}
	
	/**
	 * 关闭流(目前所有类型的流都支持关闭)
	 * @param schema 协议，例如 rtsp或rtmp
	 * @param mediaStream 流信息
	 * @return
	 */
	public Result<Integer> closeStreams(String schema, MediaStream mediaStream){
		return this.closeStreams(schema, mediaStream, null);
	}
	
	/**
	 * 关闭流(目前所有类型的流都支持关闭)
	 * @param schema 协议，例如 rtsp或rtmp
	 * @param mediaStream 流信息
	 * @param force 是否强制关闭(有人在观看是否还关闭)
	 * @return
	 */
	public Result<Integer> closeStreams(String schema, MediaStream mediaStream, Boolean force){
		Map<String, String> params = new HashMap<>();
		if(schema != null) {
			params.put("schema", schema);
		}
		if(mediaStream.getVhost() != null) {
			params.put("vhost", mediaStream.getVhost());
		}
		if(mediaStream.getApp() != null) {
			params.put("app", mediaStream.getApp());
		}
		if(mediaStream.getStream() != null) {
			params.put("stream", mediaStream.getStream());
		}
		if(force != null) {
			params.put("force", force ? "1": "0");
		}
		//请求
		Result<JSONObject> ret = this.request(api_close_streams, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		//json转对象列表
		Integer closedCount = ret.getData().getInteger("count_closed");
		return new Result<>(true, "", closedCount);
	}
	
	/**
	 * 判断直播流是否在线
	 * @param schema 协议，例如 rtsp或rtmp
	 * @param mediaStream
	 * @return
	 */
	public Result<Boolean> isMediaOnline(String schema, MediaStream mediaStream){
		Map<String, String> params = new HashMap<>();
		params.put("schema", schema != null ? schema : "rtmp");
		if(mediaStream.getVhost() != null) {
			params.put("vhost", mediaStream.getVhost());
		}
		if(mediaStream.getApp() != null) {
			params.put("app", mediaStream.getApp());
		}
		if(mediaStream.getStream() != null) {
			params.put("stream", mediaStream.getStream());
		}
		//请求
		Result<JSONObject> ret = this.request(api_isMediaOnline, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		//json转对象列表
		Boolean online = ret.getData().getBoolean("online");
		return new Result<>(true, "", online);
	}
	
	/**
	 * 获取流相关信息
	 * @param schema 协议，例如 rtsp或rtmp
	 * @param mediaStream
	 * @return
	 */
	public Result<MediaInfo> getMediaInfo(String schema, MediaStream mediaStream){
		Map<String, String> params = new HashMap<>();
		params.put("schema", schema != null ? schema : "rtmp");
		if(mediaStream.getVhost() != null) {
			params.put("vhost", mediaStream.getVhost());
		}
		if(mediaStream.getApp() != null) {
			params.put("app", mediaStream.getApp());
		}
		if(mediaStream.getStream() != null) {
			params.put("stream", mediaStream.getStream());
		}
		//请求
		Result<JSONObject> ret = this.request(api_getMediaInfo, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		//json转对象列表
		MediaInfo mediaInfo = ret.getData().toJavaObject(MediaInfo.class);
		return new Result<>(true, "", mediaInfo);
	}
	
	/**
	 * 获取流列表，可选筛选参数
	 * @param schema 筛选协议，例如 rtsp或rtmp
	 * @param vhost 筛选虚拟主机，例如__defaultVhost__
	 * @param app 筛选应用名，例如 live
	 * @param merge 是否合并相同的流
	 * @return
	 */
	public Result<List<MediaInfo>> getMediaList(String schema, String vhost, String app, Boolean merge){
		Map<String, String> params = new HashMap<>();
		if(schema != null) {
			params.put("schema", schema);
		}
		if(vhost != null) {
			params.put("vhost", vhost);
		}
		if(app != null) {
			params.put("app", app);
		}
		//请求
		Result<JSONObject> ret = this.request(api_getMediaList, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		//json转对象列表
		List<MediaInfo> mediaInfos = new ArrayList<>();
		Map<String, MediaInfo> map = new HashMap<>();
		JSONArray array = ret.getData().getJSONArray("data");
		for(int i = 0; i < array.size(); i++) {
			MediaInfo mediaInfo = array.getObject(i, MediaInfo.class);
			mediaInfo.setOnline(true);
			//流key
			String streamKey = mediaInfo.getStreamKey().toString();
			//判断是否需要合并记录
			if(merge && map.containsKey(streamKey)) {
				MediaInfo existsMediaInfo = map.get(streamKey);
				existsMediaInfo.setReaderCount(existsMediaInfo.getReaderCount() + mediaInfo.getReaderCount());
				existsMediaInfo.setSchema(existsMediaInfo.getSchema() + "," + mediaInfo.getSchema());
			} else {
				map.put(streamKey, mediaInfo);
				mediaInfos.add(mediaInfo);
			}
		}
		return new Result<>(true, "", mediaInfos);
	}
	
	/**
	 * 搜索文件系统，获取流对应的录像文件列表或日期文件夹列表
	 * @param mediaStream 流信息
	 * @param period 流的录像日期，格式为2020-02-01,如果不是完整的日期，那么是搜索录像文件夹列表，否则搜索对应日期下的mp4文件列表
	 * @return
	 */
	public Result<MediaRecordFile> getMp4RecordFile(MediaStream mediaStream, String period){
		Map<String, String> params = new HashMap<>();
		params.put("vhost", mediaStream.getVhost());
		params.put("app", mediaStream.getApp());
		params.put("stream", mediaStream.getStream());
		params.put("period", period != null ? period : DateUtils.format(new Date(), "yyyy-MM-dd"));
		//请求
		Result<JSONObject> ret = this.request(api_getMp4RecordFile, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		MediaRecordFile recordFile = ret.getData().getObject("data", MediaRecordFile.class);
		return new Result<>(true, "", recordFile);
	}
	
	/**
	 * 开始录制视频
	 * @param type 录制类型(0为hls，1为mp4)
	 * @param mediaStream 流信息
	 * @param customizedPath 录像保存目录
	 * @return
	 */
	public Result<Boolean> startRecord(Integer type, MediaStream mediaStream, String customizedPath){
		Map<String, String> params = new HashMap<>();
		params.put("type", String.valueOf(type != null ? type : 1));
		params.put("vhost", mediaStream.getVhost());
		params.put("app", mediaStream.getApp());
		params.put("stream", mediaStream.getStream());
		params.put("customized_path", customizedPath);
		//请求
		Result<JSONObject> ret = this.request(api_startRecord, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		Boolean result = ret.getData().getBoolean("result");
		return new Result<>(true, "", result);
	}
	
	/**
	 * 停止录制流
	 * @param type 类型(0为hls，1为mp4)
	 * @param mediaStream 流信息
	 * @return
	 */
	public Result<Boolean> stopRecord(Integer type, MediaStream mediaStream){
		Map<String, String> params = new HashMap<>();
		params.put("type", String.valueOf(type != null ? type : 1));
		params.put("vhost", mediaStream.getVhost());
		params.put("app", mediaStream.getApp());
		params.put("stream", mediaStream.getStream());
		//请求
		Result<JSONObject> ret = this.request(api_stopRecord, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		Boolean result = ret.getData().getBoolean("result");
		return new Result<>(true, "", result);
	}
	
	/**
	 * 获取流录制状态
	 * @param type 类型(0为hls，1为mp4)
	 * @param mediaStream 流信息
	 * @return
	 */
	public Result<Boolean> isRecording(Integer type, MediaStream mediaStream){
		Map<String, String> params = new HashMap<>();
		params.put("type", String.valueOf(type != null ? type : 1));
		params.put("vhost", mediaStream.getVhost());
		params.put("app", mediaStream.getApp());
		params.put("stream", mediaStream.getStream());
		//请求
		Result<JSONObject> ret = this.request(api_isRecording, params);
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		Boolean status = ret.getData().getBoolean("status");
		return new Result<>(true, status ? "正在录制" : "未录制", status);
	}
	
	/**
	 * 获取截图或生成实时截图并返回
	 * @param url 需要截图的url，可以是本机的，也可以是远程主机的
	 * @param timeoutSec 截图失败超时时间，防止FFmpeg一直等待截图
	 * @param expireSec 截图的过期时间，该时间内产生的截图都会作为缓存返回
	 * @param outputStream 输出流
	 * @return
	 */
	public Result<String> getSnap(String url, Integer timeoutSec, Integer expireSec, OutputStream outputStream) {
		//headers
		Map<String, String> headers = new HashMap<>();
		headers.put("X-Requested-With", "XMLHttpRequest");
		//params
		Map<String, String> params = new HashMap<>();
		params.put("secret", this.getConfig().getSecret());
		params.put("url", url);
		params.put("timeout_sec", String.valueOf(timeoutSec));
		params.put("expire_sec", String.valueOf(expireSec));
		//url
		String requestUrl = this.getConfig().getApiServer() + api_getSnap; 
		//get
		Result<String> ret = HttpUtils.httpFileDownload(requestUrl, headers, params, outputStream, false, new ResultCallback<Map<String,String>>() {
			
			@Override
			public IResult<Map<String, String>> result(Map<String, String> hs) {
				
				return new Result<>(true, "");
			}
		});
		if(!ret.isSuccess()) {
			return new Result<>(false, ret.getMsg());
		}
		return new Result<>(true, "");
	}
	
	/**
	 * 统一请求
	 * @param url
	 * @param jsonObject
	 * @return
	 */
	protected Result<JSONObject> request(String url, final Map<String, String> params) {
		//url
		String requestUrl = url.startsWith(this.getConfig().getApiServer()) ? url : (this.getConfig().getApiServer() + url); 
		//headers
		Map<String, String> headers = new HashMap<>();
		headers.put("X-Requested-With", "XMLHttpRequest");
		//params
		if(params != null && !params.containsKey("secret")) {
			params.put("secret", this.getConfig().getSecret());
		}
		if(log.isDebugEnabled()) {
			log.info("ZLMediaKit api 接口URL: {}", requestUrl);
		}
		//post
		Result<String> retPost = HttpUtils.httpPostReturnString(requestUrl, headers, params);
		if(!retPost.isSuccess()) {
			if(log.isDebugEnabled()) {
				log.info("ZLMediaKit api 响应错误: {}", retPost.getMsg());
			}
			return new Result<>(false, retPost.getMsg());
		}
		try {
			JSONObject json = JSONObject.parseObject(retPost.getData());
			if(log.isDebugEnabled()) {
				log.info("ZLMediaKit api 响应成功: {}", json.toJSONString());
			}
			if(!json.containsKey("code")) {
				return new Result<>(false, json.toJSONString());
			}
			Integer code = json.getInteger("code");
			String msg = json.containsKey("msg") ? json.getString("msg") : null;
			ZLMediaKitError apiErr = ZLMediaKitError.parse(code);
			if(apiErr == null || !apiErr.equals(ZLMediaKitError.Success)) {
				return new Result<>(false, msg != null ? msg : apiErr.getMsg());
			} else {
				return new Result<>(true, "", json);
			}
		} catch (Exception e) {
			if(log.isDebugEnabled()) {
				log.info("ZLMediaKit api 响应错误: {}", e.getMessage());
			}
			return new Result<>(false, e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		ZLMediaKitConfig config = new ZLMediaKitConfig();
		config.setLanHost("localhost");
		config.setLanHttpPort(880);
		
		ZLMediaKitClient client = new ZLMediaKitClient(config);
		System.out.println(client.getThreadsLoad());
	}
}
