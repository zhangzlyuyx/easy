package com.zhangzlyuyx.easy.core;

/**
 * 通用结果接口
 * @author zhangzlyuyx
 *
 * @param <T>
 */
public interface IResult<T> {
	
	String getCode();
	
	String getMsg();
	
	T getData();
	
	boolean isSuccess();
}
