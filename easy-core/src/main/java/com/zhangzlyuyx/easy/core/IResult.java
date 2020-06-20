package com.zhangzlyuyx.easy.core;

public interface IResult<T> {
	
	String getCode();
	
	String getMsg();
	
	T getData();
	
	boolean isSuccess();
}
