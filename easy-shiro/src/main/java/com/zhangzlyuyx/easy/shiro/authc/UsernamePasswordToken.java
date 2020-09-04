package com.zhangzlyuyx.easy.shiro.authc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;

import com.zhangzlyuyx.easy.core.util.ConvertUtils;
import com.zhangzlyuyx.easy.core.util.CryptoUtils;
import com.zhangzlyuyx.easy.core.util.StringUtils;
import com.zhangzlyuyx.easy.shiro.Constant;
import com.zhangzlyuyx.easy.shiro.ShiroRealm;
import com.zhangzlyuyx.easy.shiro.ShiroToken;
import com.zhangzlyuyx.easy.shiro.authz.AuthenticationHandler;

/**
 * userpassword token
 * @author zhangzlyuyx
 *
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken implements ShiroToken {

	private static final long serialVersionUID = 6289263822802522812L;
	
	/**
	 * 分组
	 */
	private String group = Constant.SHIROTOKEN_DEFAULT_GROUP;
	
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
	
	public UsernamePasswordToken() {
		
	}

	/**
	 * 解析 UsernamePasswordToken
	 * @param usernamePasswordToken usernamePasswordToken
	 * @return
	 */
	public static UsernamePasswordToken parse(org.apache.shiro.authc.UsernamePasswordToken usernamePasswordToken) {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setHost(usernamePasswordToken.getHost());
		token.setUsername(usernamePasswordToken.getUsername());
		token.setPassword(usernamePasswordToken.getPassword());
		token.setRememberMe(usernamePasswordToken.isRememberMe());
		return token;
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
			this.cacheKey = CryptoUtils.encodeBase64(StringUtils.join(":", this.getUsername(), (this.getPassword() != null ? new String(this.getPassword()) : "")));
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
	public static UsernamePasswordToken create(ServletRequest request) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
        UsernamePasswordToken token = new UsernamePasswordToken();
        return token;
	}
}
