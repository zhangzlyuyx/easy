package com.zhangzlyuyx.easy.shiro;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;

/**
 * shiro token 接口
 * @author zhangzlyuyx
 *
 */
public interface ShiroToken extends AuthenticationToken {
	
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
	 * 获取属性
	 * @param key
	 * @return
	 */
	Object getAttribute(String key);
	
	/**
	 * 获取属性
	 * @param key
	 * @param clazz
	 * @return
	 */
	<T> T getAttribute(String key, Class<T> clazz);
	
	/**
	 * 设置属性
	 * @param key
	 * @param value
	 */
	void setAttribute(String key, Object value);
	
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
	 * 获取认证域名称
	 * @return
	 */
	String getRealmName();
	
	/**
	 * 设置认证域名称
	 * @param realmName
	 */
	void setRealmName(String realmName);
	
	/**
	 * 执行 token 验证
	 * @param realm realm
	 * @param params 参数集合
	 * @throws AuthenticationException
	 */
	void validation(ShiroRealm realm, Map<String, Object> params) throws AuthenticationException;
}
