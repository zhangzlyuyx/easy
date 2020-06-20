package com.zhangzlyuyx.easy.core;

import java.io.Serializable;

public class Result<T> implements IResult<T>, Serializable {

	private static final long serialVersionUID = -8568415694819117908L;
	
	public static final String CODE_SUCCESS = "success";
	
	public static final String CODE_ERROR = "error";
	
	private String code;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	private String msg;
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	private T data;
	
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
	
	@Override
	public boolean isSuccess() {
		return this.code != null && this.code.equalsIgnoreCase(CODE_SUCCESS);
	}
}
