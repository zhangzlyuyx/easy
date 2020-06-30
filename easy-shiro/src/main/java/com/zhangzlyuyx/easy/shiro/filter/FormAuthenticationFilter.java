package com.zhangzlyuyx.easy.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;

import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.authc.UsernamePasswordToken;

/**
 * 表单 过滤器
 * @author zhangzlyuyx
 *
 */
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

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
	protected AuthenticationToken createToken(String username, String password, boolean rememberMe, String host) {
		org.apache.shiro.authc.UsernamePasswordToken usernamePasswordToken = (org.apache.shiro.authc.UsernamePasswordToken)super.createToken(username, password, rememberMe, host);
		UsernamePasswordToken token = UsernamePasswordToken.parse(usernamePasswordToken);
		token.setGroup(this.getGroup());
		return token;
	}
}
