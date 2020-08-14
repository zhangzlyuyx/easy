package com.zhangzlyuyx.easy.shiro.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.util.StringUtils;
import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.ShiroRealm;
import com.zhangzlyuyx.easy.shiro.ShiroToken;
import com.zhangzlyuyx.easy.shiro.authc.AccessToken;
import com.zhangzlyuyx.easy.shiro.authc.CasToken;
import com.zhangzlyuyx.easy.shiro.authc.GeneralToken;
import com.zhangzlyuyx.easy.shiro.authc.OAuthToken;
import com.zhangzlyuyx.easy.shiro.authc.UsernamePasswordToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;
import com.zhangzlyuyx.easy.shiro.util.ShiroUtils;
import com.zhangzlyuyx.easy.spring.util.SpringUtils;

/**
 * 通用过滤器
 * @author zhangzlyuyx
 *
 */
public class GeneralAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter implements AuthenticationFilter {

	protected static final Logger log = LoggerFactory.getLogger(GeneralAuthenticationFilter.class);
	
	/**
	 * shiro token 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
	/**
	 * 是否允许 post自动登录请求
	 */
	private boolean allowPostAutoLogin = true;
	
	public boolean isAllowPostAutoLogin() {
		return allowPostAutoLogin;
	}
	
	public void setAllowPostAutoLogin(boolean allowPostAutoLogin) {
		this.allowPostAutoLogin = allowPostAutoLogin;
	}
	
	/**
	 * 是否允许get自动登录请求
	 */
	private boolean allowGetAutoLogin = true;
	
	public boolean isAllowGetAutoLogin() {
		return allowGetAutoLogin;
	}
	
	public void setAllowGetAutoLogin(boolean allowGetAutoLogin) {
		this.allowGetAutoLogin = allowGetAutoLogin;
	}
	
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

	/**
	 * 判断操作权限
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		//subject
		Subject subject = this.getSubject(request, response);
		//已认证状态直接允许通过
		if(subject.isAuthenticated() || subject.isRemembered()) {
			return true;
		}
		//白名单请求，且非登录请求直接允许通过
		if(this.isPermissive(mappedValue) && !this.isLoginRequest(request, response)) {
			return true;
		}
		//获取 token
		ShiroToken token = ShiroUtils.getShiroToken();
		if(token == null) {
			ShiroUtils.logout();
			return this.onLogout(token, request, response);
		}
		//注销请求处理
		if(this.isLogoutRequest(request, response)) {
			ShiroUtils.logout();
			return this.onLogout(token, request, response);
		}
		//token group
		if(!token.getGroup().equals(this.getGroup())) {
			ShiroUtils.logout();
			return this.onLogout(token, request, response);
		}
		//isAccessAllowed - token
		if(!this.isAccessAllowed(request, response, mappedValue, token)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断操作权限
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @param shiroToken
	 * @return
	 */
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue, ShiroToken shiroToken) {
		//shiroToken
		if(shiroToken == null) {
			ShiroUtils.logout();
			return this.onLogout(shiroToken, request, response);
		}
		//AccessToken
		if(shiroToken instanceof AccessToken) {
			AccessToken accessToken = (AccessToken)shiroToken;
			//读取当前求的 accessToken 值，匹配一致性
			String accessTokenValue = this.getAccessTokenValue(request);
			if(!accessToken.getAccessToken().equals(accessTokenValue)) {
				ShiroUtils.logout();
				return this.onLogout(shiroToken, request, response);
			}
		}
		return true;
	}

	/**
	 * 操作拒绝处理
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		
		if (log.isDebugEnabled()) {
            log.debug( "onAccessDenied: {}", SpringUtils.getRequestUrl(request));
        }
		
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		
		if(SpringUtils.isGetMethod((HttpServletRequest)request)) {
			
			//登录页get直接显示
			if(this.isLoginRequest(httpRequest, response)) {
				return true;
			}
			
			//失败请求直接允许通过
			if(this.isFailureRequest(request, response)) {
				return true;
			}
			
			//get 自动登录
			if(this.isAllowGetAutoLogin()) {
				
				//cas 自动登录判断
				if(this.isCasToken(httpRequest, response)) {
					return this.executeLogin(httpRequest, response);
				}
				
				//accessToken 自动登录判断
				if(this.isAccessToken(httpRequest, response)) {
					return this.executeLogin(httpRequest, response);
				}
			}
			
			//登录重定向
			if(!StringUtils.isEmpty(this.getLoginUrl())) {
				this.saveRequestAndRedirectToLogin(httpRequest, response);
				return false;
			} else {
				return this.executeLogin(httpRequest, response);
			}
		}
		
		//执行登录请求
		return this.executeLogin(request, response);
	}
	
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		super.redirectToLogin(request, response);
	}
	
	/**
	 * 执行登录
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = null;
        try {
        	//创建 token
        	token = this.createToken(request, response);
            if (token == null) {
                String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
                        "must be created in order to execute a login attempt.";
                throw new AuthenticationException(msg);
            }
            //执行 token 验证
        	Result<String> retValidate = this.getAuthenticationHandler(request).validateToken(this, token, request, response);
        	if(!retValidate.isSuccess()) {
        		return this.onLoginFailure(token, new AuthenticationException(retValidate.getMsg()), request, response);
        	}
        	//shiro登录
            Subject subject = this.getSubject(request, response);
            subject.login(token);
            return this.onLoginSuccess(token, subject, request, response);
        } catch (AuthenticationException e) {
            return this.onLoginFailure(token, e, request, response);
        } catch (Exception e) {
            return this.onLoginFailure(token, new AuthenticationException(e), request, response);
        }
	}
	
	/**
	 * 登录成功
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		
		if (log.isDebugEnabled()) {
            log.debug( "onLoginSuccess: {}", JSONObject.toJSONString(token));
        }
		
		//自动添加 cookie
		if(this.isAllowAccessTokenCookie() && this.getAccessTokenCookieExpires() > 0) {
			AccessToken accessToken = ShiroUtils.getAccessToken();
			if(accessToken != null) {
				Map<String, Cookie> cookieMap = SpringUtils.getCookies((HttpServletRequest)request);
				for(String accessTokenParam : this.getAccessTokenParams()) {
					if(!cookieMap.containsKey(accessTokenParam.toLowerCase())) {
						SpringUtils.addCookie((HttpServletResponse)response, accessTokenParam, accessToken.getAccessToken(), this.getAccessTokenCookieExpires(), "/", request.getServerName());
					}
				}
			}
		}
		
		//get请求处理
		if(SpringUtils.isGetMethod((HttpServletRequest)request)) {
			
			//if(!StringUtils.isEmpty(this.getSuccessUrl()) && !this.isSuccessRequest(request, response)) {
			if(!StringUtils.isEmpty(this.getSuccessUrl()) && this.isLoginRequest(request, response) && !this.isSuccessRequest(request, response)) {
				//重定向到成功url
				this.issueSuccessRedirect(request, response);
				//返回false, 表示请求已处理
		        return false;
			}
		}
		
		//返回true, 表示请求需要继续处理
		return true;
	}
	
	/**
	 * 登录失败
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		
		if (log.isDebugEnabled()) {
            log.debug( "onLoginFailure: {}", e.getMessage());
        }
		
		this.setFailureAttribute(request, e);
		
		//get 请求
		if(SpringUtils.isGetMethod((HttpServletRequest)request)) {
			//判断是否需要跳转登录
	        if(!StringUtils.isEmpty(this.getLoginUrl()) && !this.isLoginRequest(request, response)) {
				try {
					//重定向到成功url
					this.redirectToLogin(request, response);
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
				}
				//返回false, 表示请求已处理
		        return false;
			}
		}
		//登录失败输出
		return this.renderLoginFailure(e, request, response);
	}
	
	/**
	 * 登录失败输出
	 * @param e
	 * @param request
	 * @param response
	 */
	protected boolean renderLoginFailure(AuthenticationException e, ServletRequest request, ServletResponse response) {
		//输出拒绝信息
		SpringUtils.writeDenied((HttpServletResponse)response, e.getMessage(), null);
		//返回false, 表示请求已处理
		return false;
	}
	
	/**
	 * 用户注销
	 * @param token
	 * @param request
	 * @param response
	 * @return
	 */
	public boolean onLogout(AuthenticationToken token, ServletRequest request, ServletResponse response) {
		
		//自动删除 cookie
		if(this.isAllowAccessTokenCookie()) {
			AccessToken accessToken = ShiroUtils.getAccessToken();
			if (accessToken != null) {
				for (String accessTokenParam : this.getAccessTokenParams()) {
					SpringUtils.addCookie((HttpServletResponse)response, accessTokenParam, null, 0, "/", request.getServerName());
				}
			}
		}
		
		//判断是否为注销页
		if(this.isLogoutRequest(request, response)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 创建 token
	 */
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		ShiroToken token = null;
		if(this.isUsernamePasswordToken(request, response)) {
			token = this.createUsernamePasswordToken(request, response);
		} else if(this.isCasToken(request, response)) {
			token = this.createCasToken(request, response);
		} else if(this.isAccessToken(request, response)) {
			token = this.createAccessToken(request, response);
		} else {
			token = this.createGeneralToken(request, response);
		}
		return this.getAuthenticationHandler(request).createToken(this, token, request, response);
	}
	
	/******************** begin url ********************/
	
	/**
	 * 认证失败 url
	 */
	private String failureUrl;
	
	public String getFailureUrl() {
		return this.failureUrl;
	}
	
	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}
	
	/**
	 * 注销 url
	 */
	private String logoutUrl;
	
	public String getLogoutUrl() {
		return logoutUrl;
	}
	
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
	/**
	 * 获取 cas 服务器地址
	 * @return
	 */
	public String getCasServerUrlPrefix() {
		Realm realm = ShiroUtils.getRealm(null);
		return (realm != null && realm instanceof ShiroRealm) ? ((ShiroRealm)realm).getCasServerUrlPrefix() : null;
	}
	
	/**
	 * 获取 cas 服务 url
	 * @return
	 */
	public String getCasServiceUrl() {
		Realm realm = ShiroUtils.getRealm(null);
		return (realm != null && realm instanceof ShiroRealm) ? ((ShiroRealm)realm).getCasService() : null;
	}
	
	/**
	 * 是否为登录请求
	 */
	@Override
	protected boolean isLoginRequest(ServletRequest request, ServletResponse response) {
		String loginUrl = this.getLoginUrl();
		if(loginUrl == null || loginUrl.length() == 0) {
			return false;
		}
		return this.pathsMatch(loginUrl, request);
	}
	
	/**
	 * 是否为失败请求
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isFailureRequest(ServletRequest request, ServletResponse response) {
		String failureUrl = this.getFailureUrl();
		if(failureUrl == null || failureUrl.length() == 0) {
			return false;
		}
		return this.pathsMatch(failureUrl, request);
	}
	
	/**
	 * 是否为注销请求
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isLogoutRequest(ServletRequest request, ServletResponse response) {
		String logoutUrl = this.getLogoutUrl();
		if(logoutUrl == null || logoutUrl.length() == 0) {
			return false;
		}
		return this.pathsMatch(logoutUrl, request);
	}
	
	/**
	 * 是否为登录成功页
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isSuccessRequest(ServletRequest request, ServletResponse response) {
		String successUrl = this.getSuccessUrl();
		if(successUrl == null || successUrl.length() == 0) {
			return false;
		}
		return this.pathsMatch(successUrl, request);
	}
	
	/******************** end url ********************/
	
	/******************** begin usernamePasswordToken ********************/
	
	/**
	 * 是否为 usernamePasswordToken
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isUsernamePasswordToken(ServletRequest request, ServletResponse response) {
		String username = this.getUsername(request);
		String password = this.getPassword(request);
		return username != null && username.length() > 0 && password != null && password.length() > 0;
	}
	
	/**
	 * 创建 usernamePasswordToken
	 * @param request
	 * @param response
	 * @return
	 */
	protected UsernamePasswordToken createUsernamePasswordToken(ServletRequest request, ServletResponse response) {
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken();
		usernamePasswordToken.setUsername(this.getUsername(request));
		usernamePasswordToken.setPassword(this.getPassword(request).toCharArray());
		usernamePasswordToken.setRememberMe(this.isRememberMe(request));
		usernamePasswordToken.setHost(this.getHost(request));
		usernamePasswordToken.setGroup(this.getGroup());
		usernamePasswordToken.setAuthenticationHandler(this.getAuthenticationHandler(request));
		return usernamePasswordToken;
	}
	
	/******************** end usernamePasswordToken ********************/
	
	/******************** begin casToken ********************/
	
	/**
	 * cas ticket 参数名
	 */
	private String casTicketParam = Constant.CAS_TICKET_PARAMETER;
	
	public String getCasTicketParam() {
		return this.casTicketParam;
	}
	
	public void setCasTicketParam(String casTicketParam) {
		this.casTicketParam = casTicketParam;
	}
	
	/**
	 * 获取 cas ticket
	 * @param request
	 * @return
	 */
	public String getCasTicket(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String ticket = httpRequest.getParameter(this.getCasTicketParam());
		return ticket;
	}
	
	/**
	 * 判断是否为 casToken
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isCasToken(ServletRequest request, ServletResponse response) {
		String casTicket = this.getCasTicket(request);
		return casTicket != null && casTicket.length() > 0;
	}
	
	/**
	 * 创建 casToken
	 * @param request
	 * @param response
	 * @return
	 */
	protected CasToken createCasToken(ServletRequest request, ServletResponse response) {
		CasToken casToken = new CasToken(this.getCasTicket(request));
		casToken.setGroup(this.getGroup());
		casToken.setAuthenticationHandler(this.getAuthenticationHandler(request));
		return casToken;
	}
	
	/******************** end casToken ********************/
	
	/******************** begin accessToken ********************/
	
	/**
	 * accessToken 参数名
	 */
	private String accessTokenParam = Constant.SHIROTOKEN_DEAULT_ACCESS_TOKEN_NAME;
	
	public String getAccessTokenParam() {
		return this.accessTokenParam;
	}
	
	public void setAccessTokenParam(String accessTokenParam) {
		this.accessTokenParam = accessTokenParam;
	}
	
	/**
	 * 获取 token 参数名集合
	 * @return
	 */
	public String[] getAccessTokenParams() {
		String str = this.getAccessTokenParam();
		if(str == null || str.length() == 0) {
			return new String[] {};
		}
		if(!str.contains(",")) {
			return new String[] { str };
		}
		return str.split(",");
	}
	
	/**
	 * 是否支持url参数
	 */
	private boolean allowAccessTokenUrlParam = true;
	
	public void setAllowAccessTokenUrlParam(boolean allowAccessTokenUrlParam) {
		this.allowAccessTokenUrlParam = allowAccessTokenUrlParam;
	}
	
	public boolean isAllowAccessTokenUrlParam() {
		return allowAccessTokenUrlParam;
	}
	
	/**
	 * 是否支持 header参数
	 */
	private boolean allowAccessTokenHeader = true;
	
	public boolean isAllowAccessTokenHeader() {
		return allowAccessTokenHeader;
	}
	
	public void setAllowAccessTokenHeader(boolean allowAccessTokenHeader) {
		this.allowAccessTokenHeader = allowAccessTokenHeader;
	}
	
	/**
	 * 是否支持 cookie参数
	 */
	private boolean allowAccessTokenCookie = true;
	
	public boolean isAllowAccessTokenCookie() {
		return allowAccessTokenCookie;
	}
	
	public void setAllowAccessTokenCookie(boolean allowAccessTokenCookie) {
		this.allowAccessTokenCookie = allowAccessTokenCookie;
	}
	
	/**
	 * accessToken cookie 过期时间(秒)
	 */
	private int accessTokenCookieExpires = 60 * 60 * 24 * 7;
	
	public int getAccessTokenCookieExpires() {
		return this.accessTokenCookieExpires;
	}
	
	public void setAccessTokenCookieExpires(int accessTokenCookieExpires) {
		this.accessTokenCookieExpires = accessTokenCookieExpires;
	}
	
	/**
	 * 获取 accessToken 值
	 * @param request
	 * @return
	 */
	public String getAccessTokenValue(ServletRequest request) {
		String accessToken = AccessToken.readAccessToken(request, this.getAccessTokenParams(), this.isAllowAccessTokenHeader(), this.isAllowAccessTokenCookie(), this.isAllowAccessTokenUrlParam());
		return accessToken;
	}
	
	/**
	 * 是否为 accessToken
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isAccessToken(ServletRequest request, ServletResponse response) {
		String accessToken = this.getAccessTokenValue(request);
		return accessToken != null && accessToken.length() > 0;
	}
	
	/**
	 * 创建 accessToken
	 * @param request
	 * @param response
	 * @return
	 */
	protected AccessToken createAccessToken(ServletRequest request, ServletResponse response) {
		AccessToken accessToken = new AccessToken();
		accessToken.setAccessToken(this.getAccessTokenValue(request));
		accessToken.setGroup(this.getGroup());
		accessToken.setAuthenticationHandler(this.getAuthenticationHandler(request));
		return accessToken;
	}
	
	/**
	 * 是否为 OAuthToken
	 * @param request
	 * @param response
	 * @return
	 */
	protected boolean isOAuthToken(ServletRequest request, ServletResponse response) {
		String userAgent = SpringUtils.getUserAgent((HttpServletRequest)request);
		if (userAgent.toLowerCase().indexOf("micromessenger") != -1) {
			
		}
		return false;
	}
	
	protected OAuthToken createOAuthToken(ServletRequest request, ServletResponse response) {
		
		return null;
	}
	
	/**
	 * 创建 generalToken
	 * @param request
	 * @param response
	 * @return
	 */
	protected GeneralToken createGeneralToken(ServletRequest request, ServletResponse response) {
		GeneralToken generalToken = new GeneralToken();
		generalToken.setGroup(this.getGroup());
		generalToken.setAuthenticationHandler(this.getAuthenticationHandler(request));
		return generalToken;
	}
	
}
