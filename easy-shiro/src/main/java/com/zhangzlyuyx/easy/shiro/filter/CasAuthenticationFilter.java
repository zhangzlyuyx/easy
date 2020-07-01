package com.zhangzlyuyx.easy.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.authc.CasToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;
import com.zhangzlyuyx.easy.shiro.util.ShiroUtils;

/**
 * cas 过滤器
 * @author zhangzlyuyx
 *
 */
public class CasAuthenticationFilter extends org.apache.shiro.cas.CasFilter implements AuthenticationFilter {

	protected static final Logger log = LoggerFactory.getLogger(CasAuthenticationFilter.class);
	
	/**
	 * shiro token 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
	/**
	 * 认证处理器
	 */
	private AuthenticationHandler authenticationHandler;
	
	public CasAuthenticationFilter() {
		
	}

	@Override
	public String getGroup() {
		return this.group;
	}

	@Override
	public void setGroup(String group) {
		this.group = group;
	}
	
	@Override
	public AuthenticationHandler getAuthenticationHandler(ServletRequest request) {
		if(this.authenticationHandler == null) {
			this.authenticationHandler = ShiroUtils.getAuthenticationHandler(request);
		}
		return this.authenticationHandler;
	}
	
	@Override
	public void setAuthenticationHandler(AuthenticationHandler authenticationHandler) {
		this.authenticationHandler = authenticationHandler;
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return ShiroUtils.isAccessAllowed(this, request, response, mappedValue);
	}
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		org.apache.shiro.cas.CasToken casToken = (org.apache.shiro.cas.CasToken)super.createToken(request, response);
		CasToken token = CasToken.parse(casToken);
		token.setGroup(this.getGroup());
		token.setAuthenticationHandler(this.getAuthenticationHandler(request));
		return ShiroUtils.convertToken(this, token, request, response);
	}
}
