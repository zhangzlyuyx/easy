package com.zhangzlyuyx.easy.core;

/**
 * 泛型通用转换回调
 * @author zhangzlyuyx
 *
 * @param <IN> 输入类型
 * @param <OUT> 输出类型
 */
public interface ConvertCallback<IN, OUT> {

	/**
	 * 转换回调方法
	 * @param input
	 * @return
	 */
	OUT convert(IN input);
}
