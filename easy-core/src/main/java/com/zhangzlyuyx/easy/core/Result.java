package com.zhangzlyuyx.easy.core;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * 泛型通用结果
 * @author zhangzlyuyx
 *
 * @param <T>
 */
public class Result<T> implements IResult<T>, Serializable {

	private static final long serialVersionUID = -8568415694819117908L;
	
	public static final int STATUS_SUCCESS = 200;
	
	public static final int STATUS_ERROR = 400;
	
	public static final int STATUS_DENIED = 401;
	
	public static final String CODE_SUCCESS = "success";
	
	public static final String CODE_ERROR = "error";
	
	public static final String CODE_DENIED = "denied";
	
	/**
	 * 状态
	 */
	private Integer status;
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	/**
	 * 代码
	 */
	private String code;
	
	@Override
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setCode(boolean success) {
		this.code = success ? CODE_SUCCESS : CODE_ERROR;
	}
	
	/**
	 * 消息
	 */
	private String msg;
	
	@Override
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * 数据
	 */
	private T data;
	
	@Override
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	/**
	 * 初始化
	 */
	public Result() {
		this.success("");
	}
	
	/**
	 * 初始化
	 * @param code
	 * @param msg
	 */
	public Result(boolean code, String msg) {
		if(code) {
			this.success(msg);
		} else {
			this.error(msg);
		}
	}
	
	/**
	 * 初始化
	 * @param code
	 * @param msg
	 * @param data
	 */
	public Result(boolean code, String msg, T data) {
		if(code) {
			this.success(msg, data);
		} else {
			this.error(msg, data);
		}
	}
	
	/**
	 * 初始化
	 * @param code
	 * @param msg
	 */
	public Result(String code, String msg) {
		this(code, msg, null);
	}
	
	/**
	 * 初始化
	 * @param code
	 * @param msg
	 * @param data
	 */
	public Result(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	/**
	 * 初始化
	 * @param status
	 * @param msg
	 */
	public Result(Integer status, String msg) {
		this(status, msg, null);
	}
	
	/**
	 * 初始化
	 * @param status
	 * @param msg
	 * @param data
	 */
	public Result(Integer status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	/**
	 * 成功结果
	 * @param msg 消息
	 * @return
	 */
	public Result<T> success(String msg) {
		this.setCode(CODE_SUCCESS);
		this.setStatus(STATUS_SUCCESS);
		this.setMsg(msg);
		return this;
	}
	
	/**
	 * 成功结果
	 * @param msg 消息
	 * @param data 数据
	 * @return
	 */
	public Result<T> success(String msg, T data) {
		this.setCode(CODE_SUCCESS);
		this.setStatus(STATUS_SUCCESS);
		this.setMsg(msg);
		this.setData(data);
		return this;
	}
	
	/**
	 * 错误结果
	 * @param msg
	 * @return
	 */
	public Result<T> error(String msg) {
		this.setCode(CODE_ERROR);
		this.setStatus(STATUS_ERROR);
		this.setMsg(msg);
		return this;
	}
	
	/**
	 * 错误结果
	 * @param msg
	 * @param data
	 * @return
	 */
	public Result<T> error(String msg, T data) {
		this.setCode(CODE_ERROR);
		this.setStatus(STATUS_ERROR);
		this.setMsg(msg);
		this.setData(data);
		return this;
	}
	
	/**
	 * 认证/登录失败
	 * @param msg
	 * @return
	 */
	public Result<T> denied(String msg) {
		this.setCode(CODE_DENIED);
		this.setStatus(STATUS_DENIED);
		this.setMsg(msg);
		return this;
	}
	
	/**
	 * 是否为成功结果
	 */
	@Override
	public boolean isSuccess() {
		//code
		if(this.code != null && this.code.equalsIgnoreCase(CODE_SUCCESS)) {
			return true;
		}
		//status
		if(this.status != null && this.status.equals(STATUS_SUCCESS)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		try {
			return JSONObject.toJSONString(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.toString();
	}
}
