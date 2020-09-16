package com.zhangzlyuyx.easy.core;

import java.util.List;

/**
 * 泛型通用列表结果
 * @author zhangzlyuyx
 *
 * @param <T>
 */
public class ListResult<T> extends Result<List<T>> {

	private static final long serialVersionUID = -7708009217467248523L;

	public ListResult() {
		super(true, "", null);
	}
	
	public ListResult(boolean code, String msg) {
		super(code, msg);
	}
	
	public ListResult(String code, String msg) {
		super(code, msg);
	}
	
	public ListResult(boolean code, String msg, List<T> data) {
		super(code, msg, data);
	}
	
	public ListResult(String code, String msg, List<T> data) {
		super(code, msg, data);
	}
}
