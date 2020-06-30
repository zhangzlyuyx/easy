package com.zhangzlyuyx.easy.shiro;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;

import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;

/**
 * shiro token 接口
 * @author zhangzlyuyx
 *
 */
public interface ShiroToken {
	
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
	 * 获取附加属性
	 * @return
	 */
	Map<String, Object> getAttributes();
	
	/**
	 * 设置附加属性
	 * @param attributes
	 */
	void setAttributes(Map<String, Object> attributes);
	
	/**
	 * 获取认证实现 handler
	 * @return
	 */
	AuthenticationHandler getAuthenticationHandler();
	
	/**
	 * 设置认证实现 handler
	 * @param authenticationHandler
	 */
	void setAuthenticationHandler(AuthenticationHandler authenticationHandler);
	
	/**
	 * 执行 token 验证
	 * @param realm realm
	 * @param params 参数集合
	 * @throws AuthenticationException
	 */
	void validation(ShiroRealm realm, Map<String, Object> params) throws AuthenticationException;
}
