package com.zhangzlyuyx.easy.shiro;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.shiro.authc.CasToken;

public class ShiroRealm extends org.apache.shiro.cas.CasRealm {
	
	private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);
	
	public ShiroRealm() {
		this.setCasServerUrlPrefix("http://localhost/cas");
	}
	
	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		if(!(token instanceof ShiroToken)) {
			throw new AuthenticationException("不支持的 authenticationToken 类型");
		}
		ShiroToken shiroToken = (ShiroToken)token;
		if(shiroToken.getAuthenticationHandler() == null) {
			throw new AuthenticationException("认证 authenticationHandler 不能为空");
		}
		Map<String, Object> vParams = new HashMap<>();
		if(token instanceof CasToken) {
			vParams.put(Constant.CAS_TICKETVALIDATOR_PARAM, this.ensureTicketValidator());
		}
		shiroToken.validation(this, vParams);
		Object principal = shiroToken.getAuthenticationHandler().getPrincipal(token);
		Object credentials = shiroToken.getAuthenticationHandler().getCredentials(token);
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials, this.getName());
		return authenticationInfo;
	}
	
	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Object principal = principals.getPrimaryPrincipal();
		if(!(principal instanceof ShiroPrincipal)) {
			throw new AuthorizationException("不支持的 principal 类型");
		}
		ShiroPrincipal shiroPrincipal = (ShiroPrincipal)principal;
		if(shiroPrincipal.getShiroToken() == null) {
			throw new AuthorizationException("认证 shiroToken 不能为空");
		}
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.addRoles(shiroPrincipal.getShiroToken().getAuthenticationHandler().getRoles(principal));
		authorizationInfo.addStringPermissions(shiroPrincipal.getShiroToken().getAuthenticationHandler().getStringPermissions(principal));
		authorizationInfo.addObjectPermissions(shiroPrincipal.getShiroToken().getAuthenticationHandler().getObjectPermissions(principal));
		return authorizationInfo;
	}
	
	@Override
	protected void doClearCache(PrincipalCollection principals) {
		super.doClearCache(principals);
	}
}
