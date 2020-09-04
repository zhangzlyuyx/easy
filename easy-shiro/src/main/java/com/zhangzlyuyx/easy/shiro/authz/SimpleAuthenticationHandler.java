package com.zhangzlyuyx.easy.shiro.authz;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.Permission;

import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.ShiroPrincipal;
import com.zhangzlyuyx.easy.shiro.ShiroRealm;
import com.zhangzlyuyx.easy.shiro.ShiroToken;
import com.zhangzlyuyx.easy.shiro.authc.AccessToken;
import com.zhangzlyuyx.easy.shiro.authc.CasToken;
import com.zhangzlyuyx.easy.shiro.authc.UsernamePasswordToken;
import com.zhangzlyuyx.easy.shiro.filter.AuthenticationFilter;
import com.zhangzlyuyx.easy.spring.util.SpringUtils;

/**
 * 简易认证处理器抽象类
 * @author zhangzlyuyx
 *
 */
public abstract class SimpleAuthenticationHandler implements AuthenticationHandler {
	
	/**
	 * 单例模式认证处理器
	 */
	private static SimpleAuthenticationHandler instance;
	
	/**
	 * 获取单例认证处理器
	 * @return
	 */
	public static synchronized SimpleAuthenticationHandler getInstance() {
		return instance;
	}
	
	/**
	 * 设置单例认证处理器
	 * @param instance
	 */
	public static synchronized void setInstance(SimpleAuthenticationHandler instance) {
		SimpleAuthenticationHandler.instance = instance;
	}
	
	public SimpleAuthenticationHandler() {
		
	}
	
	/**
	 * 初始化
	 */
	@PostConstruct
	public void init() {
		if(SimpleAuthenticationHandler.getInstance() == null) {
			SimpleAuthenticationHandler.setInstance(this);
		}
	}
	
	@Override
	public AuthenticationToken createToken(AuthenticationFilter authenticationFilter, AuthenticationToken token,
			ServletRequest request, ServletResponse response) {
		
		if(token instanceof ShiroToken) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			ShiroToken shiroToken = (ShiroToken)token;
			shiroToken.setGroup(authenticationFilter.getGroup());
			shiroToken.setAuthenticationHandler(authenticationFilter.getAuthenticationHandler(request));
			shiroToken.getAttributes().put(Constant.SHIROTOKEN__ATTRIBUTE_USERAGENT, SpringUtils.getUserAgent(httpRequest));
			shiroToken.getAttributes().put(Constant.SHIROTOKEN__ATTRIBUTE_CLIENTIP, SpringUtils.getClientIP(httpRequest));
			shiroToken.getAttributes().put(Constant.SHIROTOKEN__ATTRIBUTE_URL, SpringUtils.getRequestUrl(request));
			shiroToken.setAttribute(Constant.SHIROTOKEN__ATTRIBUTE_HEADERS, SpringUtils.getHeaderMap(httpRequest));
			shiroToken.setAttribute(Constant.SHIROTOKEN__ATTRIBUTE_PARAMETERS, SpringUtils.getParameterMap(httpRequest));
		}
		
		return token;
	}
	
	@Override
	public Map<String, Object> createValidateParams(ShiroRealm realm, AuthenticationToken token){
		Map<String, Object> params = new HashMap<>();
		return params;
	}
	
	@Override
	public Result<String> validateToken(ShiroRealm realm, AuthenticationToken token) throws AuthenticationException {
		if(token instanceof ShiroToken) {
			((ShiroToken)token).validate(realm, this.createValidateParams(realm, token));
		}
		return new Result<>(true, "");
	}
	
	@Override
	public Result<String> validateToken(AuthenticationFilter authenticationFilter, AuthenticationToken token,
			ServletRequest request, ServletResponse response) {
		
		if(token.getPrincipal() == null) {
			return new Result<>(false, "");
		}
		return new Result<>(true, "");
	}

	@Override
	public Object getPrincipal(AuthenticationToken token) {
		if(token instanceof ShiroToken) {
			((ShiroToken)token).setAuthenticationHandler(this);
		}
		return token.getPrincipal();
	}
	
	@Override
	public Object getCredentials(AuthenticationToken token) {
		return token.getCredentials();
	}
	
	@Override
	public Collection<String> getRoles(Object principal) {
		if(principal instanceof ShiroPrincipal) {
			return ((ShiroPrincipal)principal).getRoles();
		}
		return new HashSet<>();
	}

	@Override
	public Collection<String> getStringPermissions(Object principal) {
		if(principal instanceof ShiroPrincipal) {
			return ((ShiroPrincipal)principal).getPermissions();
		}
		return new HashSet<String>();
	}

	@Override
	public Collection<Permission> getObjectPermissions(Object principal) {
		return new HashSet<Permission>();
	}
	
	@Override
	public Object getAuthenticationCacheKey(AuthenticationToken token) {
		if(token == null) {
			return null;
		}
		if(token instanceof ShiroToken) {
			return ((ShiroToken)token).getCacheKey();
		}
		return token.getPrincipal();
	}
	
	@Override
	public Object getAuthenticationCacheKey(Object principal) {
		if(principal == null) {
			return null;
		}
		if(principal instanceof ShiroPrincipal) {
			return ((ShiroPrincipal)principal).getCacheKey();
		}
		return principal;
	}
	
	@Override
	public Object getAuthorizationCacheKey(Object principal) {
		if(principal == null) {
			return null;
		}
		if(principal instanceof ShiroPrincipal) {
			return ((ShiroPrincipal)principal).getCacheKey();
		}
		return principal;
	}
}
