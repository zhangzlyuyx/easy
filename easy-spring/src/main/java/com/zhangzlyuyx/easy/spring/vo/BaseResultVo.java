package com.zhangzlyuyx.easy.spring.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "结果Vo")
public class BaseResultVo<T> extends BaseVo {

	private static final long serialVersionUID = 7945405640752951709L;
	
	@ApiModelProperty(value = "数据")
	private T data;
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public BaseResultVo() {
		
	}
	
	public BaseResultVo(boolean code, String msg) {
		this.setCode(code ? "success" : "error");
		this.setStatus(code ? 200 : 400);
		this.setMsg(msg);
		this.setData(null);
	}
	
	public BaseResultVo(Integer status, String msg) {
		this.setStatus(status);
		this.setMsg(msg);
		this.setData(null);
	}
	
	public BaseResultVo(boolean code, String msg, T data) {
		this.setCode(code ? "success" : "error");
		this.setStatus(code ? 200 : 400);
		this.setMsg(msg);
		this.setData(data);
	}
	
	public BaseResultVo(String code, String msg, T data) {
		this.setCode(code);
		this.setMsg(msg);
		this.setData(data);
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this, SerializerFeature.WriteNullStringAsEmpty);
	}
}
