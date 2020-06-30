package com.zhangzlyuyx.easy.core;

/**
 * 泛型通用结果回调
 * @author zhangzlyuyx
 *
 * @param <T>
 */
public interface ResultCallback<T> {

	/**
	 * 结果回调方法
	 * @param t 参数
	 * @return
	 */
	IResult<T> result(T t);
}
