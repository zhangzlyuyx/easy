package com.zhangzlyuyx.easy.shiro.authz;

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.Permission;

import com.zhangzlyuyx.easy.shiro.filter.AuthenticationFilter;

/**
 * 认证处理器接口
 * @author zhangzlyuyx
 *
 */
public interface AuthenticationHandler {
	
	/**
	 * 创建  token
	 * @param authenticationFilter
	 * @param token
	 * @param request
	 * @param response
	 * @return
	 */
	AuthenticationToken createToken(AuthenticationFilter authenticationFilter, AuthenticationToken token, ServletRequest request, ServletResponse response);
	
	/**
	 * 获取 token 认证主体信息
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	Object getPrincipal(AuthenticationToken token) throws AuthenticationException;
	
	/**
	 * 获取 token 凭据信息
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	Object getCredentials(AuthenticationToken token) throws AuthenticationException;

	/**
	 * 获取认证主体角色集合
	 * @param principal
	 * @return
	 */
	Collection<String> getRoles(Object principal);
	
	/**
	 * 获取认证主体字符权限集合
	 * @param principal
	 * @return
	 */
	Collection<String> getStringPermissions(Object principal);
	
	/**
	 * 获取认证主体对象权限集合
	 * @param principal
	 * @return
	 */
	Collection<Permission> getObjectPermissions(Object principal);
}
