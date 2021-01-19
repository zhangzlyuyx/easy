package com.zhangzlyuyx.easy.spring.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("baseVo")
public class BaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8910855359338155284L;
	
	@ApiModelProperty(value  = "响应代码(success:成功,error:失败,denied:用户认证失败)")
	private String code;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@ApiModelProperty(value = "代码(200:成功,400:失败,401:认证失败,denied:拒绝)", example = "200")
	private Integer status;
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@ApiModelProperty(value = "响应消息")
	private String msg;

	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
