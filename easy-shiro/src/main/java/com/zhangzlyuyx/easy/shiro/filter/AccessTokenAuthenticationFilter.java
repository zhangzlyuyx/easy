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
import com.zhangzlyuyx.easy.core.util.StringUtils;
import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.authc.AccessToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;
import com.zhangzlyuyx.easy.shiro.util.ShiroUtils;
import com.zhangzlyuyx.easy.spring.util.SpringUtils;

/**
 * access token 过滤器
 * @author zhangzlyuyx
 *
 */
public class AccessTokenAuthenticationFilter extends AuthenticatingFilter implements AuthenticationFilter {

	protected static final Logger log = LoggerFactory.getLogger(AccessTokenAuthenticationFilter.class);
			
	/**
	 * shiro token 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
	/**
	 * token 参数名(支持多参数逗号分隔)
	 */
	private String tokenParam = Constant.SHIROTOKEN_DEAULT_ACCESS_TOKEN_NAME;
	
	/**
	 * 认证处理器
	 */
	private AuthenticationHandler authenticationHandler;
	
	/**
	 * 是否支持url参数
	 */
	private boolean allowUrlParam = true;
	
	/**
	 * 是否支持 header参数
	 */
	private boolean allowHeader = true;
	
	/**
	 * 是否支持 cookie参数
	 */
	private boolean allowCookie = true;
	
	/**
	 * cookie 有效期(以秒为单位,默认7天)
	 */
	private int cookieExpiry = 60 * 60 * 24 * 7;
	
	public AccessTokenAuthenticationFilter() {
		
	}
	
	@Override
	public String getGroup() {
		return this.group;
	}
	
	@Override
	public void setGroup(String group) {
		this.group = group;
	}
	
	/**
	 * 获取 token 参数名
	 * @return
	 */
	public String getTokenParam() {
		return this.tokenParam;
	}
	
	/**
	 * 设置 token 参数名 
	 * @param tokenParam
	 */
	public void setTokenParam(String tokenParam) {
		this.tokenParam = tokenParam;
	}
	
	/**
	 * 获取 token 参数名集合
	 * @return
	 */
	public String[] getTokenParams() {
		String str = this.getTokenParam();
		if(str == null || str.length() == 0) {
			return new String[] {};
		}
		if(!str.contains(",")) {
			return new String[] { str };
		}
		return str.split(",");
	}
	
	public boolean isAllowCookie() {
		return this.allowCookie;
	}
	
	public void setAllowCookie(boolean allowCookie) {
		this.allowCookie = allowCookie;
	}
	
	public boolean isAllowHeader() {
		return this.allowHeader;
	}
	
	
	public void setAllowHeader(boolean allowHeader) {
		this.allowHeader = allowHeader;
	}
	
	public boolean isAllowUrlParam() {
		return this.allowUrlParam;
	}

	public void setAllowUrlParam(boolean allowUrlParam) {
		this.allowUrlParam = allowUrlParam;
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
		boolean accessAllowed = ShiroUtils.isAccessAllowed(this, request, response, mappedValue);
		if(!accessAllowed) {
			return accessAllowed;
		}
		//验证 accessToken一致性
		String accessToken = AccessToken.readAccessToken(request, this.getTokenParams(), this.isAllowHeader(), this.isAllowCookie(), this.isAllowUrlParam());
		if(!StringUtils.isEmpty(accessToken)) {
			AccessToken shiroToken = ShiroUtils.getAccessToken();
			if(shiroToken != null && !accessToken.equals(shiroToken.getAccessToken())) {
				//注销
				ShiroUtils.logout();
				//删除 cookie 
				if(this.isAllowCookie()) {
					//SpringUtils.addCookie(response, cookie);
				}
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		AccessToken token = AccessToken.create(request, this.getTokenParams(), this.isAllowHeader(), this.isAllowCookie(), this.isAllowUrlParam());
		token.setGroup(this.getGroup());
		token.setGroup(this.getGroup());
		token.setAuthenticationHandler(this.getAuthenticationHandler(request));
		return ShiroUtils.convertToken(this, token, request, response);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		
		if (log.isDebugEnabled()) {
            log.debug( "onAccessDenied: {}", SpringUtils.getRequestUrl(request));
        }
		
		//GET请求判断
		if(SpringUtils.isGetMethod((HttpServletRequest)request)) {
			//允许登录页面GET请求 
			if(!StringUtils.isEmpty(this.getLoginUrl())) {
				//判断是否为登录页
				if(this.isLoginRequest(request, response)) {
					return true;
				} else {
					//执行登录重定向
                    this.saveRequestAndRedirectToLogin(request, response);
                    //返回false,表示已处理请求
                    return false;
				}
			}
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
		
		//如果为get请求
		if(StringUtils.isEmpty(this.getLoginUrl())) {
			//运行时异常
			throw new RuntimeException(e.getMessage());
		} else {
			//登录重定向
			try {
				this.redirectToLogin(request, response);
			} catch (Exception ex) {
				log.error("登录跳转失败!", ex);
			}
		}
		//返回false, 表示请求已处理
		return false;
	}
}
