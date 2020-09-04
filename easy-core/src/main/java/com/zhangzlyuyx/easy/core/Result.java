package com.zhangzlyuyx.easy.core;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 泛型通用结果
 * @author zhangzlyuyx
 *
 * @param <T>
 */
public class Result<T> implements IResult<T>, Serializable {

	private static final long serialVersionUID = -8568415694819117908L;
	
	public static final String CODE_SUCCESS = "success";
	
	public static final String CODE_ERROR = "error";
	
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
	
	public Result() {
		this(true, "", null);
	}
	
	public Result(boolean code, String msg) {
		this(code ? CODE_SUCCESS : CODE_ERROR, msg, null);
	}
	
	public Result(String code, String msg) {
		this(code, msg, null);
	}
	
	public Result(boolean code, String msg, T data) {
		this(code ? CODE_SUCCESS : CODE_ERROR, msg, data);
	}
	
	public Result(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
	
	/**
	 * 是否为成功结果
	 */
	@Override
	public boolean isSuccess() {
		return this.code != null && this.code.equalsIgnoreCase(CODE_SUCCESS);
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
