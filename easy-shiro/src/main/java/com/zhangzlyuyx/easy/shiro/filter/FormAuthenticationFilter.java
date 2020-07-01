package com.zhangzlyuyx.easy.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.authc.UsernamePasswordToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;
import com.zhangzlyuyx.easy.shiro.util.ShiroUtils;

/**
 * 表单 过滤器
 * @author zhangzlyuyx
 *
 */
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter implements AuthenticationFilter {

	protected static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilter.class);
	
	/**
	 * shiro token 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
	/**
	 * 认证处理器
	 */
	private AuthenticationHandler authenticationHandler;
	
	public FormAuthenticationFilter() {
		
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
		return authenticationHandler;
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
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		org.apache.shiro.authc.UsernamePasswordToken usernamePasswordToken = (org.apache.shiro.authc.UsernamePasswordToken)super.createToken(request, response);
		UsernamePasswordToken token = UsernamePasswordToken.parse(usernamePasswordToken);
		token.setGroup(this.getGroup());
		token.setAuthenticationHandler(this.getAuthenticationHandler(request));
		return ShiroUtils.convertToken(this, token, request, response);
	}
}
