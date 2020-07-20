package com.zhangzlyuyx.easy.media.zlmediakit.vo;

import java.io.Serializable;

/**
 * 音视频轨道信息
 *
 */
public class MediaTrack implements Serializable {

	private static final long serialVersionUID = 7191255929480788014L;
	
	/**
	 * 音频通道数
	 */
	private Integer channels;
	
	public Integer getChannels() {
		return this.channels;
	}
	
	public void setChannels(Integer channels) {
		this.channels = channels;
	}
	
	/**
	 * H264 = 0, H265 = 1, AAC = 2, G711A = 3, G711U = 4
	 */
	private Integer codec_id;
	
	public Integer getCodec_id() {
		return codec_id;
	}
	
	public void setCodec_id(Integer codec_id) {
		this.codec_id = codec_id;
	}
	
	/**
	 * 编码类型名称
	 */
	private String codec_id_name;
	
	public String getCodec_id_name() {
		return this.codec_id_name;
	}
	
	public void setCodec_id_name(String codec_id_name) {
		this.codec_id_name = codec_id_name;
	}
	
	/**
	 * Video = 0, Audio = 1
	 */
	private Integer codec_type;
	
	public Integer getCodec_type() {
		return codec_type;
	}
	
	public void setCodec_type(Integer codec_type) {
		this.codec_type = codec_type;
	}
	
	/**
	 * 轨道是否准备就绪
	 */
	private Boolean ready;
	
	public Boolean getReady() {
		return ready;
	}
	
	public void setReady(Boolean ready) {
		this.ready = ready;
	}
	
	/**
	 * 音频采样位数
	 */
	private Integer sample_bit;
	
	public Integer getSample_bit() {
		return sample_bit;
	}
	
	public void setSample_bit(Integer sample_bit) {
		this.sample_bit = sample_bit;
	}
	
	/**
	 * 音频采样率
	 */
	private Integer sample_rate;
	
	public Integer getSample_rate() {
		return sample_rate;
	}
	
	public void setSample_rate(Integer sample_rate) {
		this.sample_rate = sample_rate;
	}
	
	/**
	 * 视频fps
	 */
	private Integer fps;
	
	public Integer getFps() {
		return fps;
	}
	
	public void setFps(Integer fps) {
		this.fps = fps;
	}
	
	/**
	 * 视频宽
	 */
	private Integer width;
	
	public Integer getWidth() {
		return width;
	}
	
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	/**
	 * 视频高
	 */
	private Integer height;
	
	public Integer getHeight() {
		return height;
	}
	
	public void setHeight(Integer height) {
		this.height = height;
	}
}
