package com.zhangzlyuyx.easy.shiro.filter;

public interface AuthenticationFilter {

	/**
	 * 获取 token 分组
	 * @return
	 */
	String getGroup();
}
