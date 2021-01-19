package com.zhangzlyuyx.easy.shiro.authc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.springframework.util.StringUtils;

import com.zhangzlyuyx.easy.core.util.ConvertUtils;
import com.zhangzlyuyx.easy.core.util.CryptoUtils;
import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.ShiroRealm;
import com.zhangzlyuyx.easy.shiro.ShiroToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;
import com.zhangzlyuyx.easy.spring.util.SpringUtils;

/**
 * access token
 * @author zhangzlyuyx
 *
 */
public class AccessToken implements AuthenticationToken, ShiroToken {

	private static final long serialVersionUID = -2104070493824661614L;
	
	/**
	 * token 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
	/**
	 * 访问令牌
	 */
	private String accessToken;
	
	/**
	 * 附加属性
	 */
	private Map<String, Object> attributes;
	
	/**
	 * 认证处理器
	 */
	private transient AuthenticationHandler authenticationHandler;
	
	/**
	 * 认证域名称
	 */
	private String realmName;
	
	/**
	 * 缓存 key
	 */
	private String cacheKey;
	
	public AccessToken() {
		
	}
	
	@Override
	public Object getPrincipal() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return this.accessToken;
	}
	
	/**
	 * 获取访问令牌
	 * @return
	 */
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
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
	public Map<String, Object> getAttributes() {
		if(this.attributes == null) {
			this.attributes = new HashMap<>();
		}
		return this.attributes;
	}
	
	@Override
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public Object getAttribute(String key) {
		if(!this.getAttributes().containsKey(key)) {
			return null;
		}
		return this.getAttributes().get(key);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAttribute(String key, Class<T> clazz) {
		Object value = this.getAttribute(key);
		if(value == null) {
			return null;
		}
		if(value.getClass().equals(clazz) || value.getClass().isAssignableFrom(clazz)) {
			return (T)value;
		}
		return ConvertUtils.convert(clazz, value);
	}
	
	@Override
	public void setAttribute(String key, Object value) {
		this.getAttributes().put(key, value);
	}

	@Override
	public AuthenticationHandler getAuthenticationHandler() {
		return this.authenticationHandler;
	}
	
	@Override
	public void setAuthenticationHandler(AuthenticationHandler authenticationHandler) {
		this.authenticationHandler = authenticationHandler;
	}
	
	@Override
	public String getRealmName() {
		return this.realmName;
	}
	
	@Override
	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}
	
	@Override
	public String getCacheKey() {
		if(this.cacheKey == null || this.cacheKey.length() == 0) {
			this.cacheKey = CryptoUtils.encodeBase64(this.getAccessToken());
		}
		return this.cacheKey;
	}
	
	@Override
	public void setCacheKey(String key) {
		this.cacheKey = key;
	}

	@Override
	public void validate(ShiroRealm realm, Map<String, Object> params) throws AuthenticationException {
		this.setRealmName(realm.getName());
	}
	
	/**
	 * 创建 token
	 * @param request
	 * @return
	 */
	public static AccessToken create(ServletRequest request, String[] tokenParams, boolean allowHeader, boolean allowCookie, boolean allowUrlParam) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        AccessToken token = new AccessToken();
        String accessToken = readAccessToken(httpRequest, tokenParams, allowHeader, allowCookie, allowUrlParam);
        token.setAccessToken(accessToken);
        return token;
	}
	
	/**
	 * 读取 accessToken
	 * @param request
	 * @param tokenParams
	 * @param allowHeader
	 * @param allowUrlParam
	 * @param allowCookie
	 * @return
	 */
	public static String readAccessToken(ServletRequest request, String[] tokenParams, boolean allowHeader, boolean allowCookie, boolean allowUrlParam) {
		if(tokenParams == null || tokenParams.length == 0) {
			return null;
		}
		String accessToken = null;
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		//header读取
		if(StringUtils.isEmpty(accessToken) && allowHeader) {
			for(String tokenParam : tokenParams) {
				accessToken = SpringUtils.getHeader(httpRequest, tokenParam);
				if(!StringUtils.isEmpty(accessToken)) {
					break;
				}
			}
			
		}	
		//cookie读取
		if(StringUtils.isEmpty(accessToken) && allowCookie) {
			for(String tokenParam : tokenParams) {
				Cookie cookie = SpringUtils.getCookie(httpRequest, tokenParam);
				if(cookie != null) {
					accessToken = cookie.getValue();
					if(!StringUtils.isEmpty(accessToken)) {
						break;
					}
				}
			}
			
		}
		//parameter 获取
		if(StringUtils.isEmpty(accessToken) && allowUrlParam){
			for(String tokenParam : tokenParams) {
				accessToken = SpringUtils.getParameter(httpRequest, tokenParam);
				if(!StringUtils.isEmpty(accessToken)) {
					break;
				}
			}
		}
		return accessToken;
	}
}
