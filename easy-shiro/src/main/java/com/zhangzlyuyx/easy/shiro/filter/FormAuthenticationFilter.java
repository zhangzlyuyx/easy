package com.zhangzlyuyx.easy.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;

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

	/**
	 * shiro token 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
	public FormAuthenticationFilter() {
		
	}
	
	/**
	 * 获取 token 分组
	 * @return
	 */
	@Override
	public String getGroup() {
		return this.group;
	}
	
	/**
	 * 设置 token 分组
	 * @param group
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		return super.isAccessAllowed(request, response, mappedValue);
	}
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		org.apache.shiro.authc.UsernamePasswordToken usernamePasswordToken = (org.apache.shiro.authc.UsernamePasswordToken)super.createToken(request, response);
		UsernamePasswordToken token = UsernamePasswordToken.parse(usernamePasswordToken);
		token.setGroup(this.getGroup());
		
		AuthenticationHandler authenticationHandler = ShiroUtils.getAuthenticationHandler(request);
		if(authenticationHandler == null) {
			throw new AuthenticationException("authenticationHandler is empty");
		}
		AuthenticationToken authenticationToken = authenticationHandler.createToken(this, token, request, response);
		return authenticationToken;
	}
}
