package com.zhangzlyuyx.easy.spring.vo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhangzlyuyx.easy.core.Result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "结果Vo")
public class BaseResultVo<T> extends BaseVo {

	private static final long serialVersionUID = 7945405640752951709L;
	
	@ApiModelProperty(value = "数据")
	private T data;
	
	public T getData() {
		return this.data;
	}
	
	public BaseResultVo<T> setData(T data) {
		this.data = data;
		return this;
	}
	
	/**
	 * 初始化
	 */
	public BaseResultVo() {
		super.success("");
	}
	
	/**
	 * 初始化
	 * @param success
	 */
	public BaseResultVo(boolean success) {
		super(success);
	}
	
	/**
	 * 初始化
	 * @param success
	 * @param msg
	 */
	public BaseResultVo(boolean success, String msg) {
		super(success, msg);
	}
	
	/**
	 * 初始化
	 * @param success
	 * @param msg
	 * @param data
	 */
	public BaseResultVo(boolean success, String msg, T data) {
		super(success, msg);
		this.data = data;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(this, SerializerFeature.WriteNullStringAsEmpty);
	}
	
	@Override
	public BaseResultVo<T> success(String msg) {
		super.success(msg);
		return this;
	}
	
	public BaseResultVo<T> success(String msg, T data) {
		super.success(msg);
		this.data = data;
		return this;
	}
	
	@Override
	public BaseResultVo<T> error(String msg) {
		super.error(msg);
		return this;
	}
	
	@Override
	public BaseResultVo<T> denied(String msg) {
		super.denied(msg);
		return this;
	}
	
	@Override
	public BaseResultVo<T> load(Result<?> result) {
		super.load(result);
		if(this.isParameterizedType(result.getClass())) {
			this.data = (T)result.getData();
		}
		return this;
	}
}
