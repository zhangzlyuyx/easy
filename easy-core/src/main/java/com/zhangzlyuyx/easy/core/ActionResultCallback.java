package com.zhangzlyuyx.easy.core;

public interface ActionResultCallback<T, R> {

	Result<R> action(T obj);
}
