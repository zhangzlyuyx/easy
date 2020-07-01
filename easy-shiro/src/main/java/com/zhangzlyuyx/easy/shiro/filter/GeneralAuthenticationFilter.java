package com.zhangzlyuyx.easy.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.authc.GeneralToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;
import com.zhangzlyuyx.easy.shiro.util.ShiroUtils;
import com.zhangzlyuyx.easy.spring.util.SpringUtils;

/**
 * 通用过滤器
 * @author zhangzlyuyx
 *
 */
public class GeneralAuthenticationFilter extends AuthenticatingFilter implements AuthenticationFilter {

	protected static final Logger log = LoggerFactory.getLogger(GeneralAuthenticationFilter.class);
	
	/**
	 * shiro token 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
	/**
	 * 认证处理器
	 */
	private AuthenticationHandler authenticationHandler;
	
	public GeneralAuthenticationFilter() {
		
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
		GeneralToken token = new GeneralToken();
		token.setGroup(this.getGroup());
		token.setAuthenticationHandler(this.getAuthenticationHandler(request));
		return ShiroUtils.convertToken(this, token, request, response);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		
		if (log.isDebugEnabled()) {
            log.debug( "onAccessDenied: {}", SpringUtils.getRequestUrl(request));
        }
		
		return this.executeLogin(request, response);
	}
	
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		
		if (log.isDebugEnabled()) {
            log.debug( "onLoginSuccess: {}", JSONObject.toJSONString(token));
        }
		
		return super.onLoginSuccess(token, subject, request, response);
	}
	
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		
		if (log.isDebugEnabled()) {
            log.debug( "onLoginFailure", e);
        }
		
		//如果为post请求，则返回失败
		if(SpringUtils.isPostMethod((HttpServletRequest)request)) {
			//输出拒绝信息
			SpringUtils.writeDenied((HttpServletResponse)response, e.getMessage(), null);
			//返回false, 表示请求已处理
			return false;
		}
		return super.onLoginFailure(token, e, request, response);
	}
}
