package com.zhangzlyuyx.easy.media.hikvision.isapi.vo;

import java.io.Serializable;

import org.dom4j.Element;


/**
 * 流媒体通道视频信息
 *
 */
public class StreamingChannelVideo extends XmlElement implements Serializable {

	private static final long serialVersionUID = 7153402863103065433L;
	
	/**
	 * 是否启用
	 * @return
	 */
	public String getEnabled() {
		return this.getElementText("enabled");
	}
	
	public void setEnabled(String enabled) {
		this.setElementText("enabled", enabled);
	}

	/**
	 * 获取视频输入通道ID
	 * @return
	 */
	public String getVideoInputChannelID() {
		return this.getElementText("videoInputChannelID");
	}
	
	public void setVideoInputChannelID(String videoInputChannelID) {
		this.setElementText("videoInputChannelID", videoInputChannelID);
	}
	
	/**
	 * 获取视频编码类型
	 * @return
	 */
	public String getVideoCodecType() {
		return this.getElementText("videoCodecType");
	}
	
	public void setVideoCodecType(String videoCodecType) {
		this.setElementText("videoCodecType", videoCodecType);
	}
	
	/**
	 * 是否为 H264视频编码格式
	 * @return
	 */
	public boolean isVideoCodecH264() {
		String videoCodecType = this.getVideoCodecType();
		if(videoCodecType == null) {
			return false;
		}
		return videoCodecType.contains("h264") || videoCodecType.contains("h.264") || videoCodecType.contains("H264") || videoCodecType.contains("H.264");
	}
	
	/**
	 * 是否为 H265视频编码格式
	 * @return
	 */
	public boolean isVideoCodecH265() {
		String videoCodecType = this.getVideoCodecType();
		if(videoCodecType == null) {
			return false;
		}
		return videoCodecType.contains("h265") || videoCodecType.contains("h.265") || videoCodecType.contains("H265") || videoCodecType.contains("H.265");
	}
	

	public void setVideoCodecTypeH264() {
		Element el1 = this.element.element("H265Profile");
		if(el1 != null) {
			this.element.remove(el1);
		}
		Element el2 = this.element.element("H264Profile");
		if(el2 == null) {
			el2 = this.element.addElement("H264Profile");
			el2.setText("Main");
		}
		this.setVideoCodecType("H.264");
	}
	
	public void setVideoCodecTypeH265() {
		this.setVideoCodecType("H.265");
	}
	
	public String getVideoScanType() {
		return this.getElementText("videoScanType");
	}
	
	public void setVideoScanType(String videoScanType) {
		this.setElementText("videoScanType", videoScanType);
	}
	
	/**
	 * 获取视频分辨率宽度
	 * @return
	 */
	public String getVideoResolutionWidth() {
		return this.getElementText("videoResolutionWidth");
	}
	
	public void setVideoResolutionWidth(String videoResolutionWidth) {
		this.setElementText("videoResolutionWidth", videoResolutionWidth);
	}
	
	/**
	 * 获取视频分辨率高度
	 * @return
	 */
	public String getVideoResolutionHeight() {
		return this.getElementText("videoResolutionHeight");
	}
	
	public void setVideoResolutionHeight(String videoResolutionHeight) {
		this.setElementText("videoResolutionHeight", videoResolutionHeight);
	}
	
	public String getVideoQualityControlType() {
		return this.getElementText("videoQualityControlType");
	}
	
	public void setVideoQualityControlType(String videoQualityControlType) {
		this.setElementText("videoQualityControlType", videoQualityControlType);
	}
	
	public String getConstantBitRate() {
		return this.getElementText("constantBitRate");
	}
	
	public void setConstantBitRate(String constantBitRate) {
		this.setElementText("constantBitRate", constantBitRate);
	}
	
	public String getFixedQuality() {
		return this.getElementText("fixedQuality");
	}
	
	public void setFixedQuality(String fixedQuality) {
		this.setElementText("fixedQuality", fixedQuality);
	}
	
	public String getMaxFrameRate() {
		return this.getElementText("maxFrameRate");
	}
	
	public void setMaxFrameRate(String maxFrameRate) {
		this.setElementText("maxFrameRate", maxFrameRate);
	}
	
	public String getKeyFrameInterval() {
		return this.getElementText("keyFrameInterval");
	}
	
	public void setKeyFrameInterval(String keyFrameInterval) {
		this.setElementText("keyFrameInterval", keyFrameInterval);
	}
	
	public String getSnapShotImageType() {
		return this.getElementText("snapShotImageType");
	}
	
	public void setSnapShotImageType(String snapShotImageType) {
		this.setElementText("snapShotImageType", snapShotImageType);
	}

	public StreamingChannelVideo(Element element) {
		super(element);
	}
	
	public static StreamingChannelVideo parse(Element element) {
		if(element == null) {
			return null;
		}
		return new StreamingChannelVideo(element);
	}
}
