package com.zhangzlyuyx.easy.shiro.authz;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.jasig.cas.client.validation.TicketValidator;

import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.shiro.ShiroRealm;
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
	 * 创建 token 验证参数
	 * @param token
	 * @return
	 */
	Map<String, Object> createValidateParams(ShiroRealm realm, AuthenticationToken token);
	
	/**
	 * realm 验证 token
	 * @param realm
	 * @param token
	 * @return
	 * @throws AuthenticationException
	 */
	Result<String> validateToken(ShiroRealm realm, AuthenticationToken token) throws AuthenticationException;
	
	/**
	 * filter 验证 token
	 * @param authenticationFilter
	 * @param token
	 * @param request
	 * @param response
	 * @return
	 */
	Result<String> validateToken(AuthenticationFilter authenticationFilter, AuthenticationToken token, ServletRequest request, ServletResponse response);
	
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
	
	/**
	 * 获取认证缓存 key
	 * @param token
	 * @return
	 */
	Object getAuthenticationCacheKey(AuthenticationToken token);
	
	/**
	 * 获取认证缓存 key
	 * @param principal
	 * @return
	 */
	Object getAuthenticationCacheKey(Object principal);
	
	/**
	 * 获取授权缓存 key
	 * @param principal
	 * @return
	 */
	Object getAuthorizationCacheKey(Object principal);
}
