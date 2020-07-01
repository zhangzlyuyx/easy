package com.zhangzlyuyx.easy.shiro.filter;

import javax.servlet.ServletRequest;

import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;

public interface AuthenticationFilter {

	/**
	 * 获取 token 分组
	 * @return
	 */
	String getGroup();
	
	/**
	 * 设置 token 分组
	 * @param group
	 */
	void setGroup(String group);
	
	/**
	 * 获取认证处理器
	 * @param request
	 * @return
	 */
	AuthenticationHandler getAuthenticationHandler(ServletRequest request);
	
	/**
	 * 设置认证处理器
	 * @param authenticationHandler
	 */
	void setAuthenticationHandler(AuthenticationHandler authenticationHandler);
}
