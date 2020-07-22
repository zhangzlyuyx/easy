package com.zhangzlyuyx.easy.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.shiro.ShiroToken;

/**
 * 表单 过滤器
 * @author zhangzlyuyx
 *
 */
public class FormAuthenticationFilter extends GeneralAuthenticationFilter {

	protected static final Logger log = LoggerFactory.getLogger(FormAuthenticationFilter.class);
	
	public FormAuthenticationFilter() {
		
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		ShiroToken token = this.createUsernamePasswordToken(request, response);
		return this.getAuthenticationHandler(request).createToken(this, token, request, response);
	}
}
