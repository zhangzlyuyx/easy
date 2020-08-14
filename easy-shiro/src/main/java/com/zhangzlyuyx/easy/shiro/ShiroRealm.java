package com.zhangzlyuyx.easy.shiro;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.subject.PrincipalCollection;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.Saml11TicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.core.util.StringUtils;
import com.zhangzlyuyx.easy.shiro.authc.AccessToken;
import com.zhangzlyuyx.easy.shiro.authc.CasToken;
import com.zhangzlyuyx.easy.shiro.authc.GeneralToken;
import com.zhangzlyuyx.easy.shiro.authc.UsernamePasswordToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;
import com.zhangzlyuyx.easy.shiro.util.ShiroUtils;

public class ShiroRealm extends org.apache.shiro.cas.CasRealm {
	
	private static final Logger log = LoggerFactory.getLogger(ShiroRealm.class);
	
	/**
	 * cas 票据验证器 map
	 */
	protected Map<String, TicketValidator> casTicketValidatorMap = new HashMap<String, TicketValidator>();
	
	/**
	 * 认证缓存 key前缀
	 */
	private String authenticationCacheKeyPrefix = "";
	
	public ShiroRealm() {
		this.setCasServerUrlPrefix("http://localhost/cas");
	}
	
	public String getAuthenticationCacheKeyPrefix() {
		return authenticationCacheKeyPrefix;
	}
	
	public void setAuthenticationCacheKeyPrefix(String authenticationCacheKeyPrefix) {
		this.authenticationCacheKeyPrefix = authenticationCacheKeyPrefix;
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
		//realm 验证 token
		Result<String> ret = shiroToken.getAuthenticationHandler().validateToken(this, shiroToken);
		if(!ret.isSuccess()) {
			throw new AuthenticationException(ret.getMsg());
		}
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
	
	/**
	 * 创建 cas 票据验证器
	 * @param token
	 * @return
	 */
	public TicketValidator createCasTicketValidator(AuthenticationToken token) {
		String casServerUrlPrefix = null;
		String casValidationProtocol = null;
		if(token instanceof CasToken) {
			casServerUrlPrefix = ((CasToken)token).getCasServerUrlPrefix();
			casValidationProtocol = ((CasToken)token).getCasValidationProtocol();
		}
		if(StringUtils.isEmpty(casServerUrlPrefix)) {
			casServerUrlPrefix = this.getCasServerUrlPrefix();
		}
		if(StringUtils.isEmpty(casValidationProtocol)) {
			casValidationProtocol = this.getValidationProtocol();
		}
		String ticketValidatorKey = casValidationProtocol + casServerUrlPrefix;
		if(this.casTicketValidatorMap.containsKey(ticketValidatorKey)) {
			return casTicketValidatorMap.get(ticketValidatorKey);
		}
		TicketValidator ticketValidator = null;
		if ("saml".equalsIgnoreCase(casValidationProtocol)) {
			ticketValidator = new Saml11TicketValidator(casServerUrlPrefix);
        } else {
        	ticketValidator = new Cas20ServiceTicketValidator(casServerUrlPrefix);
        }
        this.casTicketValidatorMap.put(ticketValidatorKey, ticketValidator);
        return ticketValidator;
	}
	
	@Override
	protected Object getAuthenticationCacheKey(AuthenticationToken token) {
		AuthenticationHandler authenticationHandler = ShiroUtils.getAuthenticationHandler(token);
		if(authenticationHandler != null) {
			return authenticationHandler.getAuthenticationCacheKey(token);
		}
		return super.getAuthenticationCacheKey(token);
	}
	
	@Override
	protected Object getAuthenticationCacheKey(PrincipalCollection principals) {
		AuthenticationHandler authenticationHandler = ShiroUtils.getAuthenticationHandler(principals.getPrimaryPrincipal());
		if(authenticationHandler != null) {
			return authenticationHandler.getAuthenticationCacheKey(principals.getPrimaryPrincipal());
		}
		return super.getAuthenticationCacheKey(principals);
	}
	
	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		AuthenticationHandler authenticationHandler = ShiroUtils.getAuthenticationHandler(principals.getPrimaryPrincipal());
		if(authenticationHandler != null) {
			return authenticationHandler.getAuthorizationCacheKey(principals.getPrimaryPrincipal());
		}
		return super.getAuthorizationCacheKey(principals);
	}
	
	/**
	 * 支持的 token 类型
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		if(token instanceof UsernamePasswordToken) {
			return true;
		} else if(token instanceof CasToken) {
			return true;
		} else if(token instanceof AccessToken) {
			return true;
		} else if(token instanceof GeneralToken) {
			return true;
		}
		return super.supports(token) || token instanceof ShiroToken;
	}
	
	@Override
	protected void doClearCache(PrincipalCollection principals) {
		super.doClearCache(principals);
	}
	
	/**
	 * 清除认证缓存
	 * @return
	 */
	public boolean clearAuthenticationCache() {
		Cache<Object, AuthenticationInfo>  authenticationCache = this.getAuthenticationCache();
		if(authenticationCache == null) {
			return false;
		}
		authenticationCache.clear();
		return true;
	}
	
	/**
	 * 清除授权缓存
	 * @return
	 */
	public boolean clearAuthorizationCache() {
		Cache<Object, AuthorizationInfo> authorizationCache = this.getAuthorizationCache();
		if(authorizationCache == null) {
			return false;
		}
		this.getAuthorizationCache().clear();
		return true;
	}
}
