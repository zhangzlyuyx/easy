package com.zhangzlyuyx.easy.media.zlmediakit.vo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 媒体信息
 *
 */
public class MediaInfo {

	/**
	 * 协议
	 */
	private String schema;
	
	public String getSchema() {
		return this.schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	/**
	 * 虚拟主机名
	 */
	private String vhost;
	
	public String getVhost() {
		return this.vhost;
	}
	
	public void setVhost(String vhost) {
		this.vhost = vhost;
	}
	
	/**
	 * 应用名
	 */
	private String app;
	
	public String getApp() {
		return this.app;
	}
	
	public void setApp(String app) {
		this.app = app;
	}
	
	/**
	 * 流id
	 */
	private String stream;
	
	public String getStream() {
		return stream;
	}
	
	public void setStream(String stream) {
		this.stream = stream;
	}
	
	/**
	 * 本协议观看人数
	 */
	private Integer readerCount;
	
	public Integer getReaderCount() {
		return readerCount;
	}
	
	public void setReaderCount(Integer readerCount) {
		this.readerCount = readerCount;
	}
	
	/**
	 * 观看总人数，包括hls/rtsp/rtmp/http-flv/ws-flv
	 */
	private Integer totalReaderCount;
	
	public Integer getTotalReaderCount() {
		return this.totalReaderCount;
	}
	
	public void setTotalReaderCount(Integer totalReaderCount) {
		this.totalReaderCount = totalReaderCount;
	}
	
	/**
	 * 音视频轨道
	 */
	private List<MediaTrack> tracks;
	
	public List<MediaTrack> getTracks() {
		if(this.tracks == null) {
			this.tracks = new ArrayList<>();
		}
		return this.tracks;
	}
	
	public void setTracks(List<MediaTrack> tracks) {
		this.tracks = tracks;
	}
	
	/**
	 * 获取视频轨道信息
	 * @return
	 */
	@JSONField(serialize = false)
	public MediaTrack getVideoTrack() {
		for(MediaTrack track : this.getTracks()) {
			if(track.getCodec_type().equals(0)) {
				return track;
			}
		}
		return null;
	}
	
	/**
	 * 获取音频轨道信息
	 * @return
	 */
	@JSONField(serialize = false)
	public MediaTrack getAudioTrack() {
		for(MediaTrack track : this.getTracks()) {
			if(track.getCodec_type().equals(1)) {
				return track;
			}
		}
		return null;
	}
	
	/**
	 * 获取流信息
	 * @return
	 */
	@JSONField(serialize = false)
	public MediaStream getStreamKey() {
		return new MediaStream(this.getVhost(), this.getApp(), this.getStream());
	}
}
